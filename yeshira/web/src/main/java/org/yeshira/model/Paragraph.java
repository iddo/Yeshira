package org.yeshira.model;

import org.apache.log4j.Logger;
import org.jcouchdb.document.BaseDocument;

public class Paragraph extends AbstractUserObject {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(Paragraph.class);

	public static final String PROPERTY_DOCUMENT = "document";
	private static final String PROPERTY_CONTENT = "content";

	public Paragraph(User user) {
		super(user);
	}

	public Paragraph(BaseDocument document) {
		super(document);
	}

	public String getDocumentId() {
		return (String) getProperty(PROPERTY_DOCUMENT);
	}

	public void setDocument(Document document) {
		setProperty(PROPERTY_DOCUMENT, document.getId());
	}

	public void setContent(String content) {
		setProperty(PROPERTY_CONTENT, content);
	}

	public String getContent() {
		return (String) getProperty(PROPERTY_CONTENT);
	}

}
