package org.yeshira.web.controllers;

import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yeshira.model.Document;
import org.yeshira.model.Paragraph;
import org.yeshira.model.User;
import org.yeshira.web.controllers.exceptions.NotLoggedInException;
import org.yeshira.web.controllers.model.DocumentView;

/**
 * Servlet implementation class Summary
 */
@Controller
public class DocumentController extends AbstractController {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(DocumentController.class);

	private static final String PARAMETER_DOCUMENT_CONTENT = "content";

	@RequestMapping(value = "/document/create", method = RequestMethod.POST)
	@ResponseBody
	public DocumentView createDocument(HttpServletRequest request,
			@RequestParam(PARAMETER_DOCUMENT_CONTENT) String content,
			@RequestParam(Document.PROPERTY_TITLE) String title)
			throws NotLoggedInException {
		User currentUser = userFilter.getCurrentUser(request);
		if (currentUser == null) {
			throw new NotLoggedInException();
		}

		Document document = new Document(currentUser);
		document.setTitle(title);

		// Separate paragraphs
		List<Paragraph> paragraphs = new Vector<Paragraph>();
		for (String parContent : content.split("\n")) {
			Paragraph par = new Paragraph(currentUser);
			par.setContent(parContent);
			paragraphs.add(par);
		}

		documentService.saveDocument(document, paragraphs);

		return new DocumentView(document, paragraphs, currentUser);
	}

	@RequestMapping(value = "/document/{" + Paragraph.PROPERTY_DOCUMENT +"}" , method = RequestMethod.GET)
	@ResponseBody
	public DocumentView getDocument(HttpServletRequest request,
			@PathVariable(Paragraph.PROPERTY_DOCUMENT) String documentId)
			throws NotLoggedInException {
		User currentUser = userFilter.getCurrentUser(request);
		if (currentUser == null) {
			throw new NotLoggedInException();
		}

		Document document = documentService.getDocument(documentId);
		return new DocumentView(document, paragraphService.getParagraphs(document),
				userService.getUserById(document.getUserId()));
	}

}