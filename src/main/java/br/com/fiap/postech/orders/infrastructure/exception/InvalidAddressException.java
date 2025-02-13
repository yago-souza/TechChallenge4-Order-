package br.com.fiap.postech.orders.infrastructure.exception;

public class InvalidAddressException extends RuntimeException{
    public InvalidAddressException (String message) {
        super(message);
    }
}
