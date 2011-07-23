package org.yeshira.web.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Collection;

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
import org.yeshira.model.User;
import org.yeshira.model.service.UserService;
import org.yeshira.model.service.views.UserView;
import org.yeshira.web.controllers.validation.ValidationError;
import org.yeshira.web.controllers.validation.exceptions.ValidationException;
import org.yeshira.web.filters.UserFilter;

/**
 * Servlet implementation class Summary
 */
@Controller
public class UserController {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(UserController.class);

	
	private static final String PARAMETER_ASSERTION = "assertion";
	private static final String PARAMETER_REALM = "realm";

	private UserService userService;
	private UserFilter userFilter;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	@ResponseBody
	public UserView login(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(PARAMETER_ASSERTION) String assertion,
			@RequestParam(PARAMETER_REALM) String realm)
			throws MalformedURLException, UnsupportedEncodingException,
			IOException {
		
		User user = userService.login(assertion, realm);
		userFilter.updateSessionUser(user, request);

		return new UserView(user);

	}


	@RequestMapping(value = "/user/logout", method = RequestMethod.POST)
	@ResponseBody
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		userFilter.updateSessionUser(null, request);
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

}