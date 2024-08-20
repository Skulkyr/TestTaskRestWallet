package com.webapplication.testtaskrestwallet.controller;

import com.webapplication.testtaskrestwallet.DTO.wallet.WalletOperationRequest;
import com.webapplication.testtaskrestwallet.entity.Wallet;
import com.webapplication.testtaskrestwallet.services.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class WalletController {
    private final WalletService walletService;

    @GetMapping("/wallets/{id}")
    public ResponseEntity<Wallet> getWallet(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(walletService.getById(id));
    }
    @PostMapping("/wallet")
    public ResponseEntity<?> executeOperation(@Valid @RequestBody WalletOperationRequest walletOperationRequest,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity
                    .badRequest()
                    .body(bindingResult.getAllErrors().stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .collect(Collectors.joining(", ")));


        return ResponseEntity.ok(walletService.executeOperation(walletOperationRequest));
    }
}
