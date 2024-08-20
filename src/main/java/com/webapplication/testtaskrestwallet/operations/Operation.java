package com.webapplication.testtaskrestwallet.operations;

import com.webapplication.testtaskrestwallet.entity.Wallet;

public interface Operation {
    void execute(Wallet wallet, int amount);
}
