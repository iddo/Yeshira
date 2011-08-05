package org.yeshira.web.controllers;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yeshira.model.Document;
import org.yeshira.model.Paragraph;
import org.yeshira.model.User;
import org.yeshira.model.service.UserService;
import org.yeshira.web.controllers.model.DocumentView;
import org.yeshira.web.controllers.validation.ValidationError;
import org.yeshira.web.controllers.validation.exceptions.ValidationException;
import org.yeshira.web.filters.UserFilter;

/**
 * Servlet implementation class Summary
 */
@Controller
public class DocumentController {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DocumentController.class);

	private static final String PARAMETER_DOCUMENT_CONTENT = "contnet";

	private UserService userService;
	private UserFilter userFilter;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setUserFilter(UserFilter userFilter) {
		this.userFilter = userFilter;
	}

	@RequestMapping(value = "/document/create", method = RequestMethod.POST)
	@ResponseBody
	public DocumentView createDocument(HttpServletRequest request,
			@RequestParam(PARAMETER_DOCUMENT_CONTENT) String content,
			@RequestParam(Document.PROPERTY_TITLE) String title) {
		User currentUser = userFilter.getCurrentUser(request);
		

		Document document = new Document(currentUser);
		document.setTitle(title);

		// Separate paragraphs
		List<Paragraph> paragraphs = new Vector<Paragraph>();
		for (String parContent : content.split("\n")) {
			Paragraph par = new Paragraph(currentUser);
			par.setContent(parContent);
			paragraphs.add(par);
		}
		
		userService.saveDocument(document, paragraphs);

		return new DocumentView(document, paragraphs);
	}
	
	@ExceptionHandler(ValidationException.class)
	@ResponseBody
	public Collection<ValidationError> handleValidationException(
			ValidationException exception, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return exception.getErrors();
	}

}