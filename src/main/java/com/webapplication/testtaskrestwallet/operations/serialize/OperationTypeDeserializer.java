package com.webapplication.testtaskrestwallet.operations.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.webapplication.testtaskrestwallet.operations.OperationType;
import com.webapplication.testtaskrestwallet.operations.exceptions.OperationNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
@Slf4j
public class OperationTypeDeserializer extends JsonDeserializer<OperationType> {

    @Override
    public OperationType deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String value = parser.getText().toUpperCase();
        try {
            return OperationType.valueOf(value);
        } catch (IllegalArgumentException e) {
            log.info("Operation with name {} not found.", value);
            throw new OperationNotFoundException(value);
        }
    }
}
