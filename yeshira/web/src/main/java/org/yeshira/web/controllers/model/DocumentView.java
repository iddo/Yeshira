package org.yeshira.web.controllers.model;

import java.util.List;
import java.util.Vector;

import org.yeshira.model.Document;
import org.yeshira.model.Paragraph;
import org.yeshira.model.User;

public class DocumentView {

	private List<ParagraphView> paragraphs;
	private Document document;
	private UserView user;

	public DocumentView(Document document, List<Paragraph> paragraphs, User user) {
		this.document = document;
		this.paragraphs = new Vector<ParagraphView>();
		for (Paragraph paragraph : paragraphs) {
			this.paragraphs.add(new ParagraphView(paragraph));
		}
		this.user = new UserView(user);
	}
	
	public String getType() {
		return document.getType();
	}
	
	public String getId() {
		return document.getId();
	}

	public String getTitle() {
		return document.getTitle();
	}

	public List<ParagraphView> getParagraphs() {
		return paragraphs;
	}
	
	public UserView getUser() {
		return user;
	}

}
