package org.yeshira.web.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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
import org.yeshira.utils.JsonUtils;
import org.yeshira.web.controllers.model.AssertionDetails;
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

	private static final String FIELD_REMEMBER_ME = "rememberme";

	private static final String PARAMETER_ASSERTION = "assertion";

	private UserService userService;
	private UserFilter userFilter;
	private JsonUtils jsonUtils;

	@Autowired
	public void setJsonUtils(JsonUtils jsonUtils) {
		this.jsonUtils = jsonUtils;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	@ResponseBody
	public UserView login(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = FIELD_REMEMBER_ME, required = false) Boolean rememberMe,
			@RequestParam(PARAMETER_ASSERTION) String assertion)
			throws MalformedURLException, UnsupportedEncodingException,
			IOException {
		AssertionDetails assertionDetails = extractAssertionDetails(assertion);

		// validate assertion details is ok
		if (assertionDetails.isSuccess()) {

			User user = userService.getUserByEmail(assertionDetails.getEmail());
			if (user == null) {
				// create user in system
				user = new User();
				user.setEmail(assertionDetails.getEmail());
				userService.saveUser(user);
			}

			userFilter.setUser(user, request, response,
					Boolean.TRUE.equals(rememberMe));

			return new UserView(user);
		} else {
			throw new ValidationException("Login failed. "
					+ assertionDetails.getReason());
		}
	}

	private AssertionDetails extractAssertionDetails(String assertion)
			throws MalformedURLException, IOException,
			UnsupportedEncodingException {
		// validate assertion with mozilla BrowserID and extract details
		URL url = new URL("https://browserid.org/verify");
		URLConnection connection = url.openConnection();
		StringBuilder sb = new StringBuilder();
		sb.append("audience=localhost:8080&assertion=");
		sb.append(URLEncoder.encode(assertion, "UTF-8"));
		connection.setDoOutput(true);

		IOUtils.write(sb.toString(), connection.getOutputStream());
		List<String> jsonLines = IOUtils.readLines(connection.getInputStream(),
				"UTF-8");
		String json = StringUtils.join(jsonLines, null);
		return jsonUtils.fromJsonString(json, AssertionDetails.class);

	}

	@RequestMapping(value = "/user/logout", method = RequestMethod.GET)
	@ResponseBody
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		userFilter.setUser(null, request, response, true);
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