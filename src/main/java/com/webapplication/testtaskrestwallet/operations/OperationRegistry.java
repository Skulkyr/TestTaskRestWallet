package com.webapplication.testtaskrestwallet.operations;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OperationRegistry {
    private final Map<OperationType, Operation> operations = new HashMap<>();


    public OperationRegistry(List<Operation> operationBeans) {
        for (Operation operation : operationBeans) {
            if (operation instanceof DepositOperation opr)
                operations.put(OperationType.DEPOSIT, opr);
            else if (operation instanceof WithdrawOperation opr)
                operations.put(OperationType.WITHDRAW, opr);
        }
    }


    public Operation getOperation(OperationType type) {
        return operations.get(type);
    }
}
