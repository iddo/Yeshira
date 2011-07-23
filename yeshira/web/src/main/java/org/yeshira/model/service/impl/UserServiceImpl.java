package org.yeshira.model.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jcouchdb.db.Database;
import org.jcouchdb.db.Options;
import org.jcouchdb.document.BaseDocument;
import org.jcouchdb.document.ValueAndDocumentRow;
import org.jcouchdb.document.ViewAndDocumentsResult;
import org.jcouchdb.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yeshira.model.AbstractDomainObject;
import org.yeshira.model.User;
import org.yeshira.model.service.UserAlreadyExistsException;
import org.yeshira.model.service.UserService;
import org.yeshira.model.validators.UserValidator;
import org.yeshira.utils.JsonUtils;
import org.yeshira.web.controllers.model.AssertionDetails;
import org.yeshira.web.controllers.validation.ValidatorsAggregator;
import org.yeshira.web.controllers.validation.exceptions.ValidationException;

@Component
public class UserServiceImpl implements UserService {
	private static final Logger logger = Logger
			.getLogger(UserServiceImpl.class);
	private static final Logger audit = Logger.getLogger("audit");

	private static final String CONFIG_ASSERTION_VERIFICATION_SERIVE_URL = "assertion.verification.url";

	private static final String VIEW_USERS = "web/users";

	private Database db;

	private String assertionVerificationServiceUrl;

	private JsonUtils jsonUtils;

	@Autowired
	public void setJsonUtils(JsonUtils jsonUtils) {
		this.jsonUtils = jsonUtils;
	}

	@Autowired
	public void setConfig(Properties config) {
		this.assertionVerificationServiceUrl = config
				.getProperty(CONFIG_ASSERTION_VERIFICATION_SERIVE_URL);
	}

	@Autowired
	public void setDb(Database db) {
		this.db = db;
	}

	@Override
	public void saveUser(User user) {
		ValidatorsAggregator validatorsAggregator = new ValidatorsAggregator();
		validatorsAggregator.addValidator(new UserValidator(user));

		// Will throw ValidationException if errors are found
		validatorsAggregator.validateAll();

		if (!user.isSaved()) {
			// check if user with same email or phone already exists
			if (getUserByEmail(user.getEmail()) != null) {
				throw new UserAlreadyExistsException(
						"A user with that e-mail already exists");
			}
			db.createDocument(user.getDocument());
		} else {
			db.updateDocument(user.getDocument());
		}

		db.createOrUpdateDocument(user.getDocument());
	}

	@Override
	public AbstractDomainObject getById(String id) {
		try {
			return AbstractDomainObject.fromDocument(db.getDocument(
					BaseDocument.class, id));
		} catch (NotFoundException e) {
			logger.debug(e);
		}
		return null;
	}

	@Override
	public User getUserById(String userId) {
		return null;
	}

	@Override
	public User login(String assertion, String realm)
			throws MalformedURLException, UnsupportedEncodingException,
			IOException {
		audit.info("User login - " + assertion);

		AssertionDetails assertionDetails = extractAssertionDetails(assertion,
				realm);

		// validate assertion details is ok
		if (assertionDetails.isSuccess()) {

			User user = getUserByEmail(assertionDetails.getEmail());
			if (user == null) {
				// create user in system
				user = new User();
				user.setEmail(assertionDetails.getEmail());
				saveUser(user);
			}

			return user;
		} else {
			logger.debug(assertionDetails);
			throw new ValidationException("Login failed. "
					+ assertionDetails.getReason());
		}

	}

	private AssertionDetails extractAssertionDetails(String assertion,
			String realm) throws MalformedURLException, IOException,
			UnsupportedEncodingException {
		URL url = new URL(assertionVerificationServiceUrl);
		URLConnection connection = url.openConnection();
		StringBuilder sb = new StringBuilder();
		sb.append("audience=");
		sb.append(URLEncoder.encode(realm, "UTF-8"));
		sb.append("&assertion=");
		sb.append(URLEncoder.encode(assertion, "UTF-8"));
		connection.setDoOutput(true);

		IOUtils.write(sb.toString(), connection.getOutputStream());
		List<String> jsonLines = IOUtils.readLines(connection.getInputStream(),
				"UTF-8");
		String json = StringUtils.join(jsonLines, null);
		return jsonUtils.fromJsonString(json, AssertionDetails.class);

	}

	@Override
	public User getUserByEmail(String email) {
		Options o = new Options();
		o.key(email);
		o.includeDocs(true);

		ViewAndDocumentsResult<Object, BaseDocument> viewResult = db
				.queryViewAndDocuments(VIEW_USERS, Object.class,
						BaseDocument.class, o, null);
		List<ValueAndDocumentRow<Object, BaseDocument>> rows = viewResult
				.getRows();
		int totalRows = rows.size();
		if (totalRows == 0) {
			return null;
		} else if (totalRows > 1) {
			throw new RuntimeException("Multiple results for email address");
		}
		return (User) User.fromDocument(rows.get(0).getDocument());
	}

}
