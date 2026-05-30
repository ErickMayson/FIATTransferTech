package com.br.fiattransfer.exception;

public class InvalidFeeException extends RuntimeException {
    public InvalidFeeException(String message) {
        super(message);
    }
}