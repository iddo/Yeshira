package org.yeshira.model.validators;

import java.util.Collection;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.apache.log4j.Logger;
import org.yeshira.model.User;
import org.yeshira.web.controllers.validation.ValidationError;
import org.yeshira.web.controllers.validation.Validator;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class UserValidator implements Validator {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(UserValidator.class);
	
	private User user;

	private PhoneNumberUtil phoneUtil;
	
	public UserValidator(User user, PhoneNumberUtil phoneUtil) {
		this.user = user;
		this.phoneUtil = phoneUtil;
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
		
		if (StringUtils.isEmpty(user.getPhone())) {
			errors.add(new ValidationError(User.PROPERTY_PHONE_NUMBER, "חובה להוסיף מספר טלפון"));
		} else {
			PhoneNumber phoneNumber;
			try {
				phoneNumber = phoneUtil.parse(user.getPhone(), "IL");

				// TODO: check number is from israel (972)

				if (phoneUtil.getNumberType(phoneNumber) != PhoneNumberType.MOBILE) {
					errors.add(new ValidationError(User.PROPERTY_PHONE_NUMBER, "חובה לציין מספר טלפון סלולרי"));
				}
				
				
			} catch (NumberParseException e) {
				errors.add(new ValidationError(User.PROPERTY_PHONE_NUMBER, "חובה לציין מספר טלפון אמיתי"));
			}
		}
		
		// TODO: check other fields
		
		return errors;
	}
	
	
}
