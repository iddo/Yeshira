package org.yeshira.model.validators;

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.yeshira.model.Document;
import org.yeshira.web.controllers.validation.ValidationError;
import org.yeshira.web.controllers.validation.Validator;

public class DocumentValidator implements Validator {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DocumentValidator.class);
	
	private Document document;

	public DocumentValidator(Document document) {
		this.document = document;
	}

	@Override
	public Collection<ValidationError> validate() {
		Collection<ValidationError> errors = new Vector<ValidationError>();

		if (document.getUserId() == null) {
			errors.add(new ValidationError(Document.PROPERTY_USER, "חובה להיות מחוברים בשביל ליצור מסמך"));
		}
		
		// TODO: check other fields
		
		return errors;
	}
	
	
}
