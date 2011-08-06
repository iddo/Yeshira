package org.yeshira.web.controllers.validation;

import org.apache.log4j.Logger;

public class ValidationError {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(ValidationError.class);
	
	private String field;
	private String errorMessage;
	
	public ValidationError(String field, String errorMessage) {
		this.field = field;
		this.errorMessage = errorMessage;
	}

	public ValidationError(String errorMessage) {
		this(null, errorMessage);
	}

	public String getField() {
		return field;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
