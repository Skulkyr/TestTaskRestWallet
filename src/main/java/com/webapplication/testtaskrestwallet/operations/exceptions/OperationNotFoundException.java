package com.webapplication.testtaskrestwallet.operations.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OperationNotFoundException extends Error {
    private String operation;
}
