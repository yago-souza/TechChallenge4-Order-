package br.com.fiap.postech.orders.infrastructure.exception;

public class InvalidTrackingCodeException extends RuntimeException {
    public InvalidTrackingCodeException (String message) {
        super(message);
    }
}
