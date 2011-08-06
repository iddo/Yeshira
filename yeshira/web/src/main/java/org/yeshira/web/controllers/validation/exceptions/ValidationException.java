package org.yeshira.web.controllers.validation.exceptions;

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.yeshira.web.controllers.validation.ValidationError;

public class ValidationException extends RuntimeException {
	private static final long serialVersionUID = 6342731440025315716L;
	
	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(ValidationException.class);

	private Collection<ValidationError> validationErrors;

	public ValidationException(String generalError) {
		this.validationErrors = new Vector<ValidationError>();
		validationErrors.add(new ValidationError(generalError));
	}
	
	public ValidationException(String field, String errorMessage) {
		this.validationErrors = new Vector<ValidationError>();
		validationErrors.add(new ValidationError(field, errorMessage));
	}

	public ValidationException(Collection<ValidationError> errors) {
		this.validationErrors = errors;
	}
	
	public Collection<ValidationError> getErrors() {
		return validationErrors;
	}
}
