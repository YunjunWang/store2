package com.yunjun.store2.exceptions;

import com.yunjun.store2.carts.CartIsEmptyException;
import com.yunjun.store2.carts.CartNotFoundException;
import com.yunjun.store2.orders.OrderNotFoundException;
import com.yunjun.store2.products.CategoryNotFoundException;
import com.yunjun.store2.products.ProductNotFoundException;
import com.yunjun.store2.users.UserAlreadyExistsException;
import com.yunjun.store2.dtos.ErrorDto;
import com.yunjun.store2.payments.PaymentException;
import com.yunjun.store2.users.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleHttpMessageNotReadableException(){
        return ResponseEntity.badRequest().body(new ErrorDto("Invalid request body!"));
    }

    @ExceptionHandler({
            BadCredentialsException.class,
            IllegalAccessException.class})
    public ResponseEntity<ErrorDto> handleBadCredentialsException(){
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
            CategoryNotFoundException.class,
            CartNotFoundException.class,
            CartIsEmptyException.class,
            ProductNotFoundException.class,
            UserNotFoundException.class,
            UserAlreadyExistsException.class,
            IllegalArgumentException.class})
    public ResponseEntity<ErrorDto> handleExceptions(Exception e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDto> handleException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorDto> handlePaymentException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorDto("An error occurred while processing the payment."));
    }
}
