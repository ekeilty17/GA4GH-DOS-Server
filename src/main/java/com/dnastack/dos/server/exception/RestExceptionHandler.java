package com.dnastack.dos.server.exception;

import com.dnastack.dos.server.response.ErrorResponse;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

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

	/**
	 * GET Request Parameter Exception handling If a required parameter in the url
	 * is missing (as of right now there are no required fields)
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse("required parameter '" + ex.getParameterName() + "' is missing",
				HttpStatus.BAD_REQUEST.value(), ex);
		return buildResponseEntity(errorResponse);
	}

	/**
	 * POST Request Body Exception Handling If the request is not a valid JSON
	 * object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
		log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
		ErrorResponse errorResponse = new ErrorResponse("Malformed JSON request.", HttpStatus.BAD_REQUEST.value(), ex);
		return buildResponseEntity(errorResponse);
	}

	/**
	 * Validation Error when @Valid spots bad input Specifically if any NonNull-able
	 * fields are null
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Have a better error message
		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), BAD_REQUEST.value(), ex);
		return buildResponseEntity(errorResponse);
	}

	/**
	 * org.joda.time.IllegalFieldValueException created/updated field is not to
	 * RFC3339 specification
	 */
	@ExceptionHandler({ org.joda.time.IllegalFieldValueException.class, java.lang.IllegalArgumentException.class })
	protected ResponseEntity<Object> handleDateTimeConflict(RuntimeException ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), BAD_REQUEST.value(), ex);
		return buildResponseEntity(errorResponse);
	}

	/**
	 * com.dnastack.dos.server.exception.EntityNotFoundException: If
	 * repo.findOne(id) in the service layer returns null Used in GET, PUT, and
	 * DELETE endpoints
	 */
	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), NOT_FOUND.value(), ex);
		return buildResponseEntity(errorResponse);
	}

	/**
	 * org.hibernate.exception.ConstraintViolationException When two data objects
	 * share the same urls because the way I have configured the database, the urls
	 * are their own entities and therefore need their own unique Id
	 */
	// FIXME I don't think this is being called
	@ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
	protected ResponseEntity<Object> multipleUrlException(RuntimeException ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(
				ex.getMessage() + ". Most likely, two data objects contain the same url.", BAD_REQUEST.value(), ex);
		return buildResponseEntity(errorResponse);
	}

	/**
	 * Generic Exception Handler
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> exception(Exception ex) {
		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), INTERNAL_SERVER_ERROR.value(), ex);
		return buildResponseEntity(errorResponse);
	}

	// Error response builder
	private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
		return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorResponse.getStatus()));
	}

}