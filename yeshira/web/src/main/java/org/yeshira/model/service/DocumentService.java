package org.yeshira.model.service;

import java.util.List;

import org.yeshira.model.Document;
import org.yeshira.model.Paragraph;

public interface DocumentService {

	void saveDocument(Document document, List<Paragraph> paragraphs);

	Document getDocument(String documentId);

}
