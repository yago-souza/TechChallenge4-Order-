package br.com.fiap.postech.orders.infrastructure.exception;

public class InvalidPriceException extends RuntimeException {
    public InvalidPriceException(String message) {
        super(message);
    }
}