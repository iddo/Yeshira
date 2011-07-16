package org.yeshira.web.controllers.validation;

import java.util.Collection;

public interface Validator {

	public Collection<ValidationError> validate();
	
}
