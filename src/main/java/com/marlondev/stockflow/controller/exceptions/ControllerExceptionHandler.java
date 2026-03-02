package com.marlondev.stockflow.controller.exceptions;

import com.marlondev.stockflow.services.exceptions.DatabaseException;
import com.marlondev.stockflow.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> recursoNaoEncontrado(ResourceNotFoundException e, HttpServletRequest request){
        String error = "ResourceNotFoundException";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> cpfDuplicado(DatabaseException e, HttpServletRequest request){
        String error = "DatabaseException";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validacaoInvalida(MethodArgumentNotValidException e, HttpServletRequest request){
        String error = "MethodArgumentNotValidException";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String mensagem = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        StandardError err = new StandardError(Instant.now(), status.value(), error, mensagem, request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}
