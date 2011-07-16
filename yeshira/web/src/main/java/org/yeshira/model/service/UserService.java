package org.yeshira.model.service;

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

	String getUserToken(User user);

	User loginByToken(String userId, String token);

	User getUserById(String userId);

	User getParkingById(String parkingId);

	User login(String userEmail, String password);
}
