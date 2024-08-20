package com.webapplication.testtaskrestwallet.DTO.wallet;

import com.webapplication.testtaskrestwallet.operations.OperationType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class WalletOperationRequest {
    @NotNull(message = "UUID can not be null")
    UUID walletId;
    @NotNull(message = "operationType can not be null")
    OperationType operationType;
    @NotNull(message = "amount can not be null")
    @Min(value = 1, message = "amount must be greater than 0")
    Integer amount;

}
