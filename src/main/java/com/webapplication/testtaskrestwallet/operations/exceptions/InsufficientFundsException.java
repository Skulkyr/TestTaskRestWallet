package com.webapplication.testtaskrestwallet.operations.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;
@AllArgsConstructor
@Getter
public class InsufficientFundsException extends RuntimeException {
    private UUID id;
}
