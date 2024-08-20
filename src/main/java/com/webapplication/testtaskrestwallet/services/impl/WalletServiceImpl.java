package com.webapplication.testtaskrestwallet.services.impl;

import com.webapplication.testtaskrestwallet.DTO.wallet.WalletOperationRequest;
import com.webapplication.testtaskrestwallet.entity.Wallet;
import com.webapplication.testtaskrestwallet.operations.Operation;
import com.webapplication.testtaskrestwallet.operations.OperationRegistry;
import com.webapplication.testtaskrestwallet.repository.WalletRepository;
import com.webapplication.testtaskrestwallet.services.WalletService;
import com.webapplication.testtaskrestwallet.services.exceptions.WalletNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@RequiredArgsConstructor
@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final OperationRegistry operationRegistry;

    @Override
    public Wallet getById(UUID id) {
        return walletRepository.findById(id).orElseThrow(() -> new WalletNotFoundException(id));
    }

    @Override
    @Transactional
    public Wallet executeOperation(WalletOperationRequest request) {
        Wallet wallet = walletRepository.findByIdWithLock(request.getWalletId())
                .orElseThrow(() -> new WalletNotFoundException(request.getWalletId()));

        Operation operation = operationRegistry.getOperation(request.getOperationType());
        operation.execute(wallet, request.getAmount());

        return walletRepository.save(wallet);
    }
}
