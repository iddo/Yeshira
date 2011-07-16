package org.yeshira.model;

import org.apache.log4j.Logger;
import org.jcouchdb.document.BaseDocument;

public class User extends AbstractDomainObject {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(User.class);

	public static final String PROPERTY_EMAIL = "email";
	public static final String PROPERTY_PASSWORD_HASH = "password";
	public static final String PROPERTY_PHONE_NUMBER = "phone";

	public User() {
		super();
	}

	public User(BaseDocument document) {
		super(document);
	}

	public void setEmail(String email) {
		this.setProperty(PROPERTY_EMAIL, email);
	}

	public String getEmail() {
		return (String) getProperty(PROPERTY_EMAIL);
	}

	public void setPassword(String password) {
		this.setProperty(PROPERTY_PASSWORD_HASH, password);
	}

	public void setPhone(String phone) {
		this.setProperty(PROPERTY_PHONE_NUMBER, phone);
	}

	public String getPhone() {
		return (String) getProperty(PROPERTY_PHONE_NUMBER);
	}

	public String getPasswordHash() {
		return (String) getProperty(PROPERTY_PASSWORD_HASH);
	}

	public String getDisplayName() {
		String email = getEmail();
		return email.substring(0, email.indexOf("@"));
	}

}
