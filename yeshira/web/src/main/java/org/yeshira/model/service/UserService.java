package org.yeshira.model.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.yeshira.model.User;

public interface UserService {

	/**
	 * Create or update a user
	 * 
	 * @param user
	 */
	void saveUser(User user);

	User getUserById(String userId);

	User getUserByEmail(String userEmail);

	User login(String assertion, String realm) throws MalformedURLException,
			UnsupportedEncodingException, IOException;

}