package org.yeshira.model.service.impl;

import org.jcouchdb.db.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yeshira.model.AbstractDomainObject;
import org.yeshira.model.User;
import org.yeshira.model.service.UserService;

@Component
public class UserServiceImpl implements UserService {

	private Database db;

	@Autowired
	public void setDb(Database db) {
		this.db = db;
	}

	@Override
	public void saveUser(User user) {
		db.createOrUpdateDocument(user.getDocument());
	}

	@Override
	public AbstractDomainObject getById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserToken(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User loginByToken(String userId, String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserById(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getParkingById(String parkingId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User login(String userEmail, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserByEmail(String userEmail) {
		// TODO Auto-generated method stub
		return null;
	}

}
