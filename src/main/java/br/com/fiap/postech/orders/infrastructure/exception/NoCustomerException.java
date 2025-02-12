package br.com.fiap.postech.orders.infrastructure.exception;

public class NoCustomerException extends RuntimeException {
    public NoCustomerException(String message) {
        super(message);
    }
}