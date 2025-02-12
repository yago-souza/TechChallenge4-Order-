package br.com.fiap.postech.orders.infrastructure.exception;

public class InvalidQuantityException extends RuntimeException {
    public InvalidQuantityException(String message) {
        super(message);
    }
}