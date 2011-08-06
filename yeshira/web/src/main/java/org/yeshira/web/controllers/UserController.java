package org.yeshira.web.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yeshira.model.User;
import org.yeshira.model.service.views.UserView;

/**
 * Servlet implementation class Summary
 */
@Controller
public class UserController extends AbstractController {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(UserController.class);

	
	private static final String PARAMETER_ASSERTION = "assertion";
	private static final String PARAMETER_REALM = "realm";

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


}