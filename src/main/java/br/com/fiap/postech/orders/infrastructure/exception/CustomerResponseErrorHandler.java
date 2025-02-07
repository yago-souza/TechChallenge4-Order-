package br.com.fiap.postech.orders.infrastructure.exception;

import org.apache.coyote.BadRequestException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class CustomerResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        switch (response.getStatusCode()) {
            case NOT_FOUND:
                throw new ResourceNotFoundException("Recurso não encontrado");
            case BAD_REQUEST:
                throw new BadRequestException("Requisição inválida");
            default:
                throw new GenericException("Erro genérico");
        }
    }
}
