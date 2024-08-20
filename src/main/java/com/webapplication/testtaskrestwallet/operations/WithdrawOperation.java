package com.webapplication.testtaskrestwallet.operations;

import com.webapplication.testtaskrestwallet.entity.Wallet;
import com.webapplication.testtaskrestwallet.operations.exceptions.InsufficientFundsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class WithdrawOperation implements Operation {

    public void execute(Wallet wallet, int amount) {
        if (wallet.getBalance() >= amount) {
            wallet.setBalance(wallet.getBalance() - amount);
        } else {
            log.info("Cant execute operation withdraw on wallet: " + wallet.getId() + " because there are not enough funds");
            throw new InsufficientFundsException(wallet.getId());
        }
    }
}
