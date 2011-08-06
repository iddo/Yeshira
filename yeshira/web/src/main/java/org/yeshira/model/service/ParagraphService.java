package org.yeshira.model.service;

import java.util.List;

import org.yeshira.model.Document;
import org.yeshira.model.Paragraph;

public interface ParagraphService {

	public List<Paragraph> getParagraphs(Document document);

}
