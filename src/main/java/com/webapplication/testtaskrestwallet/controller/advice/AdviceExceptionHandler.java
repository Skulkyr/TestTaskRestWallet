package com.webapplication.testtaskrestwallet.controller.advice;

import com.webapplication.testtaskrestwallet.operations.exceptions.InsufficientFundsException;
import com.webapplication.testtaskrestwallet.operations.exceptions.OperationNotFoundException;
import com.webapplication.testtaskrestwallet.services.exceptions.WalletNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class AdviceExceptionHandler {
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<?> handleInsufficientFoundsException(InsufficientFundsException ex) {
        return ResponseEntity
                .status(HttpStatus.PAYMENT_REQUIRED)
                .body(new ExceptionMessage("Insufficient funds in the wallet " + ex.getId()));
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<?> handleWalletNotFoundException(WalletNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionMessage("Can't find wallet " + ex.getId()));
    }

    @ExceptionHandler(OperationNotFoundException.class)
    public ResponseEntity<?> handleOperationNotFoundException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionMessage("Operation not found"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
                                                                   HttpServletRequest request) {

        String message;
        String exceptionInfo = ex.getMessage();
        if (exceptionInfo.contains("Cannot deserialize value of type `java.util.UUID`"))
            message = "Cannot deserialize value of type `java.util.UUID`";
        else {
            message = "Can't read or serialized request";
            log.warn("HttpMessageNotReadableException:\n{} on request: {}", ex.getMessage() ,getRequestBody(request));
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionMessage(message));
    }


    @Getter
    private static class ExceptionMessage {
        private final String date;
        private final String message;

        private ExceptionMessage(String message) {
            this.date = LocalDateTime.now().format(DateTimeFormatter
                    .ofPattern("yyyy.MM.dd  HH:mm:ss"));

            this.message = message;
        }
    }

    private String getRequestBody(HttpServletRequest request) {

            return request.getHeader("body");

    }
}
