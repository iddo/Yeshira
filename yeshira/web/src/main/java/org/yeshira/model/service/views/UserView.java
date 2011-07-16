package org.yeshira.model.service.views;

import org.yeshira.model.User;

public class UserView {
	private User user;

	public UserView(User user) {
		this.user = user;
	}
	
	public String getEmail() {
		return user.getEmail();
	}
}
