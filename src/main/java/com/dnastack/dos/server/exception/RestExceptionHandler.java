package com.dnastack.dos.server.exception;

import com.dnastack.dos.server.response.ErrorResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	
	/*
	 * GET Request Parameter Exception handling
	 */
	
	@Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse("required parameter '" + ex.getParameterName() + "' is missing", HttpStatus.BAD_REQUEST.value(), ex);
        return buildResponseEntity(errorResponse);
    }
	
	
	/*
	 * POST Request Body Exception Handling
	 */
	
	// If the request is not a valid JSON object
	// I don't think Springboot has a dedicated Exception for this
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        ErrorResponse errorResponse = new ErrorResponse("Malformed JSON request.", HttpStatus.BAD_REQUEST.value(), ex);
        return buildResponseEntity(errorResponse);
    }
    
    // Validation Error: when @Valid fails. Specifically if
    //		any NonNull-able fields are null (possibly they are miss spelled)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), BAD_REQUEST.value(), ex);
        return buildResponseEntity(errorResponse);
    }
    
    // org.joda.time.IllegalFieldValueException:
    //		created/updated field is not to RFC3339 specification
    @ExceptionHandler({org.joda.time.IllegalFieldValueException.class, java.lang.IllegalArgumentException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
    	ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), BAD_REQUEST.value(), ex);
        return buildResponseEntity(errorResponse);
    }
    
    // com.dnastack.dos.server.exception.EntityNotFoundException:
    //		My custom implementation
    //		If getDataObject or getDataBundle return null;
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
    	ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), NOT_FOUND.value(), ex);
        return buildResponseEntity(errorResponse);
    }
    
    // Exception: handles any unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception ex) {
    	ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), INTERNAL_SERVER_ERROR.value(), ex);
    	return buildResponseEntity(errorResponse);
    }
    
    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
    }
    
}