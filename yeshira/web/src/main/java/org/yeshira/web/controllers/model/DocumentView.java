package org.yeshira.web.controllers.model;

import java.util.List;

import org.yeshira.model.Document;
import org.yeshira.model.Paragraph;
import org.yeshira.model.User;

public class DocumentView {

	private List<Paragraph> paragraphs;
	private Document document;
	private UserView user;

	public DocumentView(Document document, List<Paragraph> paragraphs, User user) {
		this.document = document;
		this.paragraphs = paragraphs;
		this.user = new UserView(user);
	}

	public String getTitle() {
		return document.getTitle();
	}

	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}
	
	public UserView getUser() {
		return user;
	}

}
