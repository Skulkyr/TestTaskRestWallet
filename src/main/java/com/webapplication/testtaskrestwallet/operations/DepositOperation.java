package com.webapplication.testtaskrestwallet.operations;

import com.webapplication.testtaskrestwallet.entity.Wallet;
import org.springframework.stereotype.Component;

@Component
public class DepositOperation implements Operation {
    @Override
    public void execute(Wallet wallet, int amount) {
        wallet.setBalance(wallet.getBalance() + amount);
    }
}
