package com.webapplication.testtaskrestwallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapplication.testtaskrestwallet.DTO.wallet.WalletOperationRequest;
import com.webapplication.testtaskrestwallet.entity.Wallet;
import com.webapplication.testtaskrestwallet.operations.OperationType;
import com.webapplication.testtaskrestwallet.repository.WalletRepository;
import com.webapplication.testtaskrestwallet.services.WalletService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerIT {

    @Autowired
    MockMvc mvc;

    @Autowired
    WalletService walletService;

    @Autowired
    WalletRepository walletRepository;

    ObjectMapper objectMapper;
    UUID id;
    Wallet wallet;

    @BeforeEach
    void init() {
        objectMapper = new ObjectMapper();
        wallet = new Wallet(null, 1000);
        wallet = walletRepository.saveAndFlush(wallet);
        id = wallet.getId();
    }

    @AfterEach
    void destroy() {
        walletRepository.delete(wallet);
    }

    @Test
    @SneakyThrows
    void getWallet_ifIdIsInvalid_thenReturn400() {
        mvc.perform(get("/api/v1/wallets/123e4567-e89b-12d3-a456-426614174012"))


                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Can't find wallet 123e4567-e89b-12d3-a456-426614174012"));
    }

    @Test
    @SneakyThrows
    void executeOperation_ifOperationIsInvalid_thenReturn400() {
        String request = String.format("""
                {
                "walletId" : "%s",
                "operationType" : "TEST",
                "amount" : "500"
                }
                """, id);


        mvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))


                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Operation not found")));
    }

    @Test
    @SneakyThrows
    void executeOperation_ifIdIsInvalid_thenReturn400() {
        String request = """
                {
                "walletId" : "123e4567-e89b-12d3-a456-426614174",
                "operationType" : "TEST",
                "amount" : "500"
                }
                """;


        mvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))


                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Cannot deserialize value of type `java.util.UUID`")));
    }

    @Test
    @SneakyThrows
    void executeOperation_ifAmountMoreBalance_thenReturn402() {
        WalletOperationRequest request = new WalletOperationRequest();
        request.setWalletId(id);
        request.setOperationType(OperationType.WITHDRAW);
        request.setAmount(1001);


        mvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))


                .andExpect(status().isPaymentRequired())
                .andExpect(content().string(containsString("Insufficient funds in the wallet")));
    }


    @Test
    @SneakyThrows
    void executeOperation_RpmTest() {
        WalletOperationRequest deposit = new WalletOperationRequest();
        WalletOperationRequest withdraw = new WalletOperationRequest();
        deposit.setWalletId(id);
        withdraw.setWalletId(id);
        deposit.setOperationType(OperationType.DEPOSIT);
        withdraw.setOperationType(OperationType.WITHDRAW);
        deposit.setAmount(1);
        withdraw.setAmount(1);
        OperationThread thread1 = new OperationThread(deposit);
        OperationThread thread2 = new OperationThread(deposit);
        OperationThread thread3 = new OperationThread(withdraw);
        OperationThread thread4 = new OperationThread(withdraw);
        int balanceBefore = walletService.getById(id).getBalance();


        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();


        int balanceAfter = walletService.getById(id).getBalance();
        assertEquals(balanceBefore, balanceAfter);

    }



    private class OperationThread extends Thread {
        private final WalletOperationRequest request;

        private OperationThread(WalletOperationRequest request) {
            this.request = request;
        }

        @Override
        public void run() {
            for (int i = 0; i < 1000; i++)
                walletService.executeOperation(request);
        }
    }

}
