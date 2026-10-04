package com.equity.reports.exception;

import java.util.stream.Collectors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

/**
 * Converts exceptions into RFC 7807 problem responses for all controllers.
 * Ordered first so its handlers win over Spring's default problem-details handler.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		problem.setTitle("Resource not found");
		return problem;
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ProblemDetail handleBadRequest(IllegalArgumentException ex) {
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
		problem.setTitle("Invalid request");
		return problem;
	}

	/** Constraint violations on request parameters, e.g. {@code limit=0} with {@code @Min(1)}. */
	@ExceptionHandler(HandlerMethodValidationException.class)
	public ProblemDetail handleParameterValidation(HandlerMethodValidationException ex) {
		String detail = ex.getAllValidationResults().stream()
				.flatMap(result -> result.getResolvableErrors().stream()
						.map(error -> "'" + result.getMethodParameter().getParameterName() + "' "
								+ error.getDefaultMessage()))
				.collect(Collectors.joining("; "));
		ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
		problem.setTitle("Invalid request");
		return problem;
	}
}
