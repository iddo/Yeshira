package org.yeshira.model.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.yeshira.model.Document;
import org.yeshira.model.Paragraph;
import org.yeshira.model.service.ParagraphService;

@Service
public class ParagraphServiceImpl extends AbstractService implements ParagraphService {

	@SuppressWarnings("unchecked")
	@Override
	public List<Paragraph> getParagraphs(Document document) {
		return (List<Paragraph>) getByIds(document.getParagraphIds());
	}

}
