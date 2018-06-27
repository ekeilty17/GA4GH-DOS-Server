package com.dnastack.dos.server.exception;

import com.dnastack.dos.server.response.ErrorResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	// If the request is not a valid JSON object
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        ErrorResponse errorResponse = new ErrorResponse("Malformed JSON request.", HttpStatus.BAD_REQUEST.value(), ex);
        return buildResponseEntity(errorResponse);
    }
    
    // Validation Error: when @Valid fails. Specifically if
    //		id = NULL
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse("The request is malformed.", BAD_REQUEST.value(), ex);
        return buildResponseEntity(errorResponse);
    }
    
    // org.joda.time.IllegalFieldValueException:
    //		created/updated not to RFC3339 spec
    @ExceptionHandler(org.joda.time.IllegalFieldValueException.class)
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
    	ErrorResponse errorResponse = new ErrorResponse("This request is malformed.", BAD_REQUEST.value(), ex);
        return buildResponseEntity(errorResponse);
    }
    
    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
    }
    
}