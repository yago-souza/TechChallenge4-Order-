package br.com.fiap.postech.orders.infrastructure.exception;

public class InvalidTotalAmountException extends RuntimeException {
    public InvalidTotalAmountException(String message) {
        super(message);
    }
}