package br.com.fiap.postech.orders.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<String> handleClientNotFound(CustomerNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFound(ProductNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<String> handleInsufficientStock(InsufficientStockException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(NoItemException.class)
    public ResponseEntity<String> handleNoItem(NoItemException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
    }

    @ExceptionHandler(NoCustomerException.class)
    public ResponseEntity<String> handleNoCustomer(NoCustomerException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
    }

    @ExceptionHandler(InvalidPriceException.class)
    public ResponseEntity<String> handleInvalidPrice(InvalidPriceException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(InvalidTotalAmountException.class)
    public ResponseEntity<String> handleInvalidTotalPrice(InvalidTotalAmountException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<String> handleInvalidQuantity(InvalidQuantityException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFound(OrderNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<String> handleInvalidStatus(InvalidStatusException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}