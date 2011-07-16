package org.yeshira.model.service;

import org.apache.log4j.Logger;

public class UserAlreadyExistsException extends RuntimeException {
	public UserAlreadyExistsException(String string) {
		super(string);
	}

	private static final long serialVersionUID = -2103082376165307871L;
	
	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(UserAlreadyExistsException.class);
}
