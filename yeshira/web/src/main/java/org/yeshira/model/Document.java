package org.yeshira.model;

import java.util.List;

import org.apache.log4j.Logger;
import org.jcouchdb.document.BaseDocument;

/**
 * A document is composed of paragraphes
 * 
 * @author iddo
 * 
 */
public class Document extends AbstractUserObject {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(Document.class);

	public static final String PROPERTY_TITLE = "title";
	private static final String PROPERTY_PARAGRAPHES = "paragraphes";

	public Document(User user) {
		super(user);
	}

	public Document(BaseDocument document) {
		super(document);
	}

	public String getTitle() {
		return (String) getProperty(PROPERTY_TITLE);
	}

	public void setTitle(String title) {
		setProperty(PROPERTY_TITLE, title);
	}

	public void setParagraphs(List<Paragraph> paragraphs) {
		String[] paragraphIds = new String[paragraphs.size()];
		int i=0;
		for (Paragraph par : paragraphs) {
			paragraphIds[i++] = par.getId();
		}
		setProperty(PROPERTY_PARAGRAPHES, paragraphIds);
	}

	@SuppressWarnings("unchecked")
	public List<String> getParagraphIds() {
		return (List<String>) getProperty(PROPERTY_PARAGRAPHES);
	}

}
