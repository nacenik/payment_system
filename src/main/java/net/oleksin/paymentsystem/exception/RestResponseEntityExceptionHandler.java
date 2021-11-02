package net.oleksin.paymentsystem.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AccountNotFoundException.class, PersonNotFoundException.class})
    public ResponseEntity<Object> handleAccountsException(Exception ex, WebRequest webRequest) {
        return new ResponseEntity<>(ExceptionDto.builder().status(404).message(ex.getMessage()).build(), new HttpHeaders(), 404);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<Object> handlePaymentsException(Exception ex, WebRequest webRequest) {
        return new ResponseEntity<>(ExceptionDto.builder().status(400).message(ex.getMessage()).build(), new HttpHeaders(), 400);
    }
}
