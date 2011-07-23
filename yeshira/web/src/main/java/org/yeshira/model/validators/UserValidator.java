package org.yeshira.model.validators;

import java.util.Collection;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.apache.log4j.Logger;
import org.yeshira.model.User;
import org.yeshira.web.controllers.validation.ValidationError;
import org.yeshira.web.controllers.validation.Validator;

public class UserValidator implements Validator {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(UserValidator.class);
	
	private User user;

	public UserValidator(User user) {
		this.user = user;
	}

	@Override
	public Collection<ValidationError> validate() {
		Collection<ValidationError> errors = new Vector<ValidationError>();

		if (StringUtils.isEmpty(user.getEmail())) {
			errors.add(new ValidationError(User.PROPERTY_EMAIL, "חובה לציין כתובת דואר אלקטרוני"));
		} else {
			EmailValidator emailValidtor = EmailValidator.getInstance();
			if (!emailValidtor.isValid(user.getEmail())) {
				errors.add(new ValidationError(User.PROPERTY_EMAIL, "חובה לציין כתובת דואר אלקטרוני תקינה"));
			}
		}
		
		// TODO: check other fields
		
		return errors;
	}
	
	
}
