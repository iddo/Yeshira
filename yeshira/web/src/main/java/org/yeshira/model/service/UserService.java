package org.yeshira.model.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.yeshira.model.AbstractDomainObject;
import org.yeshira.model.User;

public interface UserService {

	/**
	 * Create or update a user
	 * 
	 * @param user
	 */
	void saveUser(User user);

	/**
	 * Get a db object by its id
	 * 
	 * @param id
	 * @return
	 */
	AbstractDomainObject getById(String id);

	User getUserById(String userId);

	User getUserByEmail(String userEmail);

	User login(String assertion, String realm) throws MalformedURLException,
			UnsupportedEncodingException, IOException;
}
