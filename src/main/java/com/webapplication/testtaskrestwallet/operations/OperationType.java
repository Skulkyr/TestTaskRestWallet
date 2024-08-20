package com.webapplication.testtaskrestwallet.operations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.webapplication.testtaskrestwallet.operations.serialize.OperationTypeDeserializer;

@JsonDeserialize(using = OperationTypeDeserializer.class)
public enum OperationType {
    DEPOSIT,
    WITHDRAW

}
