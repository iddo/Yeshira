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

	private UrlPathHelper urlPathHelper;

	private JsonUtils jsonUtils;

	@Resource
	public void setConfig(@Qualifier("config") Properties config) {
	}

	@Autowired
	public void setJsonUtils(JsonUtils jsonUtils) {
		this.jsonUtils = jsonUtils;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		urlPathHelper = new UrlPathHelper();
	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
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
		User user = getCurrentUser(request);
		if (user == null) {
			redirectToLogin(request, response, null);
		}
		return user;
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
	 * Updates the session attribute with a new user object (when updating user
	 * information or getting a new version from the DB)
	 * 
	 * @param user
	 * @param request
	 */
	public void updateSessionUser(User user, HttpServletRequest request) {
		WebUtils.setSessionAttribute(request, CURRENT_USER_SESSION_ATTRIBUTE,
				null);
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
