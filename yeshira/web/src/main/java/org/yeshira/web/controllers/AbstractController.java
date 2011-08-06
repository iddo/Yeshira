package org.yeshira.web.controllers;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yeshira.model.service.DocumentService;
import org.yeshira.model.service.ParagraphService;
import org.yeshira.model.service.UserService;
import org.yeshira.web.controllers.exceptions.NotLoggedInException;
import org.yeshira.web.controllers.validation.ValidationError;
import org.yeshira.web.controllers.validation.exceptions.ValidationException;
import org.yeshira.web.filters.UserFilter;

public abstract class AbstractController {
	protected UserService userService;
	protected DocumentService documentService;
	protected ParagraphService paragraphService;

	protected UserFilter userFilter;

	@Autowired
	public void setParagraphService(ParagraphService paragraphService) {
		this.paragraphService = paragraphService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setDocumentService(DocumentService userService) {
		this.documentService = userService;
	}

	@Autowired
	public void setUserFilter(UserFilter userFilter) {
		this.userFilter = userFilter;
	}

	@ExceptionHandler(ValidationException.class)
	@ResponseBody
	public Collection<ValidationError> handleValidationException(
			ValidationException exception, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return exception.getErrors();
	}

	@ExceptionHandler(NotLoggedInException.class)
	@ResponseBody
	public ValidationError handleNotLoggedInException(NotLoggedInException exception,
			HttpServletResponse response) throws IOException {
		String msg = "User must log in to perform this action";
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		return new ValidationError(msg);
	}

}
