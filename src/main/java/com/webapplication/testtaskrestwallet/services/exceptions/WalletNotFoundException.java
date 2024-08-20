package com.webapplication.testtaskrestwallet.services.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;
@AllArgsConstructor
@Getter
public class WalletNotFoundException extends RuntimeException {
    private UUID id;
}
