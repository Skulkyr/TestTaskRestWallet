package com.webapplication.testtaskrestwallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapplication.testtaskrestwallet.DTO.wallet.WalletOperationRequest;
import com.webapplication.testtaskrestwallet.entity.Wallet;
import com.webapplication.testtaskrestwallet.operations.OperationType;
import com.webapplication.testtaskrestwallet.services.WalletService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {
    @MockBean
    WalletService walletService;

    @Autowired
    MockMvc mvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @SneakyThrows
    void getWallet_ifIdIsValid_thenReturn200() {
        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Wallet wallet = new Wallet(id, 1000);
        when(walletService.getById(id)).thenReturn(wallet);

        mvc.perform(get("/api/v1/wallets/123e4567-e89b-12d3-a456-426614174000"))


                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(wallet)));
        verify(walletService, times(1)).getById(id);
    }


    @Test
    @SneakyThrows
    void executeOperation_ifDataIsValid_thenReturn200() {
        UUID id = UUID.randomUUID();
        WalletOperationRequest request = new WalletOperationRequest();
        request.setWalletId(id);
        request.setOperationType(OperationType.DEPOSIT);
        request.setAmount(100);
        Wallet wallet = new Wallet(id, 1100);
        when(walletService.executeOperation(request)).thenReturn(wallet);


        mvc.perform(post("/api/v1/wallet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))


                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(wallet)));
        verify(walletService, times(1)).executeOperation(any());
    }


    @Test
    @SneakyThrows
    void executeOperation_ifAmountLessZero_thenReturn400() {
        UUID id = UUID.randomUUID();
        WalletOperationRequest request = new WalletOperationRequest();
        request.setWalletId(id);
        request.setOperationType(OperationType.DEPOSIT);
        request.setAmount(-1);


        mvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))


                .andExpect(status().isBadRequest())
                .andExpect(content().string("amount must be greater than 0"));
        verify(walletService, never()).executeOperation(any());
    }


    @Test
    @SneakyThrows
    void executeOperation_ifDataIsNull_thenReturn400() {
        WalletOperationRequest request = new WalletOperationRequest();

        mvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))


                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("UUID can not be null")))
                .andExpect(content().string(containsString("amount can not be null")))
                .andExpect(content().string(containsString("operationType can not be null")));
        verify(walletService, never()).executeOperation(any());
    }

}
