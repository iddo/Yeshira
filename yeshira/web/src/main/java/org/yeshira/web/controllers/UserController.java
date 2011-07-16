package org.yeshira.web.controllers;

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
import org.yeshira.utils.DigestUtils;
import org.yeshira.web.controllers.validation.ValidationError;
import org.yeshira.web.controllers.validation.exceptions.ValidationException;
import org.yeshira.web.filters.UserFilter;

/**
 * Servlet implementation class Summary
 */
@Controller
public class UserController {
	private static final Logger logger = Logger.getLogger(UserController.class);

	private static final String FIELD_REMEMBER_ME  = "rememberme";

	private UserService userService;
	private UserFilter userFilter;
	
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	private DigestUtils digestUtils;
	
	@Autowired
	public void setWebUtils(DigestUtils digestUtils) {
		this.digestUtils = digestUtils;
	}


	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public UserView register(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(User.PROPERTY_EMAIL) String email,
			@RequestParam(User.PROPERTY_PASSWORD_HASH) String password,
			@RequestParam(User.PROPERTY_PHONE_NUMBER) String phone) {
		User user = new User();
		user.setEmail(email);
		user.setPassword(digestUtils.sh1(password));
		user.setPhone(phone);

		userService.saveUser(user);
		
		userFilter.setUser(user, request, response, false);
		
		return new UserView(user);
	}
	
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	@ResponseBody
	public UserView login(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(User.PROPERTY_EMAIL) String userEmail,
			@RequestParam(User.PROPERTY_PASSWORD_HASH) String password,
			@RequestParam(value = FIELD_REMEMBER_ME, required = false) Boolean rememberMe) {

		User user = userService.login(userEmail, digestUtils.sh1(password));
		if (user != null) {
			logger.info("User logged in " + user);
			userFilter.setUser(user, request, response, Boolean.TRUE.equals(rememberMe));
		} else {
			throw new ValidationException("שם משתמש או סיסמה שגויים");
		}
	
		return new UserView(user);
	}
	
	@RequestMapping(value="/user/logout", method = RequestMethod.GET)
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
	public Collection<ValidationError> handleValidationException(ValidationException exception, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return exception.getErrors();
	}

}