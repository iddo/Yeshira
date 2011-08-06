package org.yeshira.model;

import org.apache.log4j.Logger;
import org.jcouchdb.document.BaseDocument;

public abstract class AbstractUserObject extends AbstractDomainObject {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(AbstractDomainObject.class);

	public static final String PROPERTY_USER = "user";
	
	private transient User user;

	protected AbstractUserObject(User user) {
		super();
		setUser(user);
	}

	protected AbstractUserObject(BaseDocument baseDocument) {
		super(baseDocument);

	}

	protected User getUser() {
		return user;
	}

	private void setUser(User user) {
		setProperty(PROPERTY_USER, user.getId());
	}

	public String getUserId() {
		return (String) getProperty(PROPERTY_USER);
	}

}
