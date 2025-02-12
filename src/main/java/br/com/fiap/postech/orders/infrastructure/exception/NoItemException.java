package br.com.fiap.postech.orders.infrastructure.exception;

public class NoItemException extends RuntimeException {
    public NoItemException(String message) {
        super(message);
    }
}