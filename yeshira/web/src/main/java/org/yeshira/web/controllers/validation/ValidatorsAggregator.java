package org.yeshira.web.controllers.validation;

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.yeshira.web.controllers.validation.exceptions.ValidationException;

public class ValidatorsAggregator {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(ValidatorsAggregator.class);
	
	protected Collection<Validator> validators;
	
	public ValidatorsAggregator() {
		this.validators = new Vector<Validator>();
	}
	
	public void addValidator(Validator validator) {
		this.validators.add(validator);
	}
	
	public void validateAll() throws ValidationException {
		Collection<ValidationError> errors = new Vector<ValidationError>();
		for (Validator validator : validators) {
			errors.addAll(validator.validate());
		}
		
		if (!errors.isEmpty()) {
			throw new ValidationException(errors);
		}
	}

}
