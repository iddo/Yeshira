package org.yeshira.web.controllers.model;

import org.yeshira.model.User;

final class UserView {

	private User user;

	public UserView(User user) {
		this.user = user;
	}

	public String getId() {
		return user.getId();
	}

	public String getDisplayName() {
		return user.getDisplayName();
	}
}
