package br.com.fiap.postech.orders.infrastructure.exception;

public class InvalidPaymentMethodException extends RuntimeException{
    public InvalidPaymentMethodException (String message) {
        super(message);
    }
}
