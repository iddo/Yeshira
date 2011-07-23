package org.yeshira.web.filters;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;
import org.yeshira.model.User;
import org.yeshira.model.service.UserService;
import org.yeshira.utils.JsonUtils;

/**
 * A filter that logs the user in using cookies User: iddo Date: Mar 13, 2009
 * Time: 2:08:01 PM
 */
@Component
public class UserFilter implements Filter {

	private static final String LOGIN_REDIRECT_URL_SESSION_ATTRIBUTE = "LOGIN_REDIRECT_URL_SESSION_ATTRIBUTE";

	private static final String CURRENT_USER_SESSION_ATTRIBUTE = "currentUser";

	private static final String CURRENT_USER_JSON_SESSION_ATTRIBUTE = "currentUserJson";

	private static Log logger = LogFactory.getLog(UserFilter.class);

	private UserService userService;

	private String remembermeCookieIdName;
	private String remembermeCookieTokenName;
	private int remembermeCookieMaxAge;

	private UrlPathHelper urlPathHelper;

	private JsonUtils jsonUtils;

	@Resource
	public void setConfig(@Qualifier("config") Properties config) {
		this.remembermeCookieIdName = config
				.getProperty("cookie.rememberme.id.name");
		this.remembermeCookieTokenName = config
				.getProperty("cookie.rememberme.token.name");
		this.remembermeCookieMaxAge = Integer.parseInt(config
				.getProperty("cookie.rememberme.maxage"));
	}

	@Autowired
	public void setJsonUtils(JsonUtils jsonUtils) {
		this.jsonUtils = jsonUtils;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		urlPathHelper = new UrlPathHelper();
	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = null;
		try {
			httpRequest = HttpServletRequest.class.cast(servletRequest);
		} catch (ClassCastException e) {
		}

		if (httpRequest != null) {
			getCurrentUser(httpRequest, (HttpServletResponse) servletResponse);
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {
	}

	/**
	 * Tries to get the current user from session or cookies, and if not logged
	 * in redirects or forwards the request according to request method while
	 * saving the original requested url in a session attribute.
	 * <p/>
	 * Note: if this method returns null no further response can be sent. Note
	 * 2: Don't use inside a controller using UserFilter because this will waste
	 * cpu
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public User getCurrentUserOrRedirect(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		User user = getCurrentUser(request, response);
		if (user == null) {
			redirectToLogin(request, response, null);
		}
		return user;
	}

	public void redirectToLogin(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		redirectToLogin(request, response, null);
	}

	public void redirectToLogin(HttpServletRequest request,
			HttpServletResponse response, String redirectUrl)
			throws IOException, ServletException {
		if (redirectUrl == null && request.getMethod().equals("GET")) {
			// Only construct original url if the request method is GET (POST
			// method contains additional information which can't be saved)
			redirectUrl = getOriginalUrl(request);
		}

		if (redirectUrl != null) {
			WebUtils.setSessionAttribute(request,
					LOGIN_REDIRECT_URL_SESSION_ATTRIBUTE, redirectUrl);

			// RequestDispatcher dispatcher =
			// request.getRequestDispatcher("/login.html");
			// dispatcher.forward(request, response);
		} // else {
		response.sendRedirect("/login.html");
		// }
	}

	public String getOriginalUrl(HttpServletRequest request) {
		String queryString = urlPathHelper.getOriginatingQueryString(request);
		StringBuilder reqestedUrl = new StringBuilder(
				urlPathHelper.getOriginatingRequestUri(request));
		if (queryString != null && queryString.length() > 0) {
			reqestedUrl.append("?").append(queryString);
		}
		return reqestedUrl.toString();
	}

	/**
	 * Gets the current user from session, and if not found searches for the
	 * correct cookies and logs the user in
	 * <p/>
	 * Note: Don't use inside a controller using UserFilter because this will
	 * waste cpu
	 */
	public User getCurrentUser(HttpServletRequest request,
			HttpServletResponse response) {
		User currentUser = (User) WebUtils.getSessionAttribute(request,
				CURRENT_USER_SESSION_ATTRIBUTE);

		if (currentUser == null) {
			// Retrieve the cookies
			Cookie rememberMeIdCookie = null, rememberMeHashCookie = null;
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies)
				// Get both cookies in a single pass
				{
					if (rememberMeIdCookie == null
							&& cookie.getName().equals(remembermeCookieIdName)) {
						rememberMeIdCookie = cookie;
						if (rememberMeHashCookie != null) {
							break;
						}
					} else if (rememberMeHashCookie == null
							&& cookie.getName().equals(
									remembermeCookieTokenName)) {
						rememberMeHashCookie = cookie;
						if (rememberMeIdCookie != null) {
							break;
						}
					}
				}
			}

			// If the relavent cookies exist log the user in
			if (rememberMeIdCookie != null && rememberMeHashCookie != null) {
				try {
					currentUser = userService.loginByToken(
							rememberMeIdCookie.getValue(),
							rememberMeHashCookie.getValue());
				} catch (Exception e) {
					if (logger.isDebugEnabled()) {
						logger.debug(
								"Unable to login user using email '"
										+ rememberMeIdCookie.getValue()
										+ "' and token '"
										+ rememberMeHashCookie.getValue() + "'",
								e);
					}
				}

				setUser(currentUser, request, response, true);
			}
		} /*
		 * else { // Update the user from cache/persistency currentUser =
		 * userService.loadUserById(currentUser.getId()); }
		 * 
		 * if (currentUser != null) { try { if (currentUser.getBalance() ==
		 * null) { // Update the balance and re-get the object
		 * currentUser.setBalance(userService.getUserBalance(currentUser)); }
		 * updateSessionUser(currentUser, request); } catch (Exception e) {
		 * logger.debug(e); } }
		 */
		return currentUser;
	}

	/**
	 * Get the current user from the reqest attribute
	 * 
	 * @param request
	 * @return the currently logged in user
	 */
	public User getCurrentUser(HttpServletRequest request) {
		return (User) WebUtils.getSessionAttribute(request,
				CURRENT_USER_SESSION_ATTRIBUTE);
	}

	/**
	 * Sets the currently logged in user along with the necessary cookies
	 * 
	 * @param user
	 *            the user to set or null to clear the cookies
	 * @param response
	 */
	public void setUser(User user, HttpServletRequest request,
			HttpServletResponse response, boolean useCookie) {
		Cookie rememberMeIdCookie = new Cookie(remembermeCookieIdName, null);
		Cookie rememberMeHashCookie = new Cookie(remembermeCookieTokenName,
				null);

		//TODO externalize
		rememberMeIdCookie.setDomain("iddo.homeip.net");
		rememberMeIdCookie.setPath(request.getContextPath());
		rememberMeHashCookie.setDomain("iddo.homeip.net");
		rememberMeHashCookie.setPath(request.getContextPath());
		
		if (useCookie && user != null) {
			// Renew the cookies
			rememberMeIdCookie.setValue(user.getId());
			rememberMeIdCookie.setMaxAge(remembermeCookieMaxAge);

			rememberMeHashCookie.setValue(userService.getUserToken(user));
			rememberMeHashCookie.setMaxAge(remembermeCookieMaxAge);
		} else {
			// Delete the false cookies
			rememberMeIdCookie.setMaxAge(0);
			rememberMeHashCookie.setMaxAge(0);
		}
		// Add the cookies to the response
		response.addCookie(rememberMeHashCookie);
		response.addCookie(rememberMeIdCookie);

		updateSessionUser(user, request);
	}

	/**
	 * Updates the session attribute with a new user object (when updating user
	 * information or getting a new version from the DB)
	 * 
	 * @param user
	 * @param request
	 */
	public void updateSessionUser(User user, HttpServletRequest request) {
		WebUtils.setSessionAttribute(request,
				CURRENT_USER_SESSION_ATTRIBUTE, null);
		if (user != null) {
			logger.debug(jsonUtils.toJsonString(user));
		} else {
			WebUtils.setSessionAttribute(request,
					CURRENT_USER_JSON_SESSION_ATTRIBUTE, null);
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}
		}
	}
}
