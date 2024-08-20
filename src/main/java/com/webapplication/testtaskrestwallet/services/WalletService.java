package com.webapplication.testtaskrestwallet.services;

import com.webapplication.testtaskrestwallet.DTO.wallet.WalletOperationRequest;
import com.webapplication.testtaskrestwallet.entity.Wallet;

import java.util.UUID;

public interface WalletService {
    Wallet getById(UUID id);

    Wallet executeOperation(WalletOperationRequest request);
}
