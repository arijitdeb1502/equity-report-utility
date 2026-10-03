package com.equity.reports.exception;

/** Thrown when a requested customer, trade, etc. does not exist; mapped to HTTP 404. */
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String resource, Object id) {
		super(resource + " not found: " + id);
	}
}
