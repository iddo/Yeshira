package org.yeshira.web.controllers.model;

import org.yeshira.model.Paragraph;

public class ParagraphView {
	private Paragraph paragraph;

	public ParagraphView(Paragraph paragraph) {
		this.paragraph = paragraph;
	}

	public String getId() {
		return paragraph.getId();
	}

	public String getContent() {
		return paragraph.getContent();
	}
}
