package br.com.fiap.postech.orders.infrastructure.exception;

public class GenericException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GenericException(String message) {
        super(message);
    }
}
