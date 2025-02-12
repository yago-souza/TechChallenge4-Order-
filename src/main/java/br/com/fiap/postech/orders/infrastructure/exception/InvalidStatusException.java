package br.com.fiap.postech.orders.infrastructure.exception;

public class InvalidStatusException extends RuntimeException{
    public InvalidStatusException (String message) {
        super(message);
    }
}
