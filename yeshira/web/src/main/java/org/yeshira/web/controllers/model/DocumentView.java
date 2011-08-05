package org.yeshira.web.controllers.model;

import java.util.List;

import org.yeshira.model.Document;
import org.yeshira.model.Paragraph;

public class DocumentView {

	private List<Paragraph> paragraphs;
	private Document document;

	public DocumentView(Document document, List<Paragraph> paragraphs) {
		this.document = document;
		this.paragraphs = paragraphs;
	}

	public String getTitle() {
		return document.getTitle();
	}

	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}

}
