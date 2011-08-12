package org.yeshira.web.config;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.jcouchdb.db.Database;
import org.jcouchdb.db.Server;
import org.jcouchdb.db.ServerImpl;
import org.jcouchdb.exception.CouchDBException;
import org.jcouchdb.util.CouchDBUpdater;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerExceptionResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.yeshira.utils.DigestUtils;
import org.yeshira.utils.JsonUtils;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

@Configuration
public class AppConfig {
	private static final Logger logger = Logger.getLogger(AppConfig.class);

	@Resource
	WebApplicationContext context;

	@Bean
	public Properties config() throws IOException {
		logger.info("Loading config.properties");
		ClassPathResource cpr = new ClassPathResource("config.properties");
		Properties p = new Properties();
		p.load(cpr.getInputStream());
		return p;
	}

	@Bean
	public Server server() throws IOException {
		Properties config = config();
		Server server = new ServerImpl(config.getProperty("couchdb.host"),
				Integer.valueOf(config.getProperty("couchdb.port")));
		return server;
	}

	@Bean
	public Database couchDb() throws IOException {
		Properties config = config();
		String dbName = config.getProperty("couchdb.db");
		Server server = server();

		if (Boolean.parseBoolean(config.getProperty("couchdb.dropdb",
				Boolean.FALSE.toString()))) {
			try {
				server.deleteDatabase(dbName);
			} catch (CouchDBException e) {
			}
		}

		Database db = new Database(server, dbName);

		CouchDBUpdater updater = new CouchDBUpdater();
		updater.setCreateDatabase(true);
		updater.setDatabase(db);

		File designDocsDir;
		if (context != null) {
			designDocsDir = new File(context.getServletContext().getRealPath(
					"WEB-INF/designdocs/"));
		} else {
			designDocsDir = new File("WebContent/WEB-INF/designdocs/");
		}
		logger.info("Updating design docs from "
				+ designDocsDir.getAbsolutePath());
		updater.setDesignDocumentDir(designDocsDir);
		updater.updateDesignDocuments();

		return db;
	}

	@Bean
	public ViewResolver viewResolver() {
		logger.info("Initializing view resolver");
		InternalResourceViewResolver vr = new InternalResourceViewResolver();
		vr.setViewClass(JstlView.class);
		vr.setPrefix("/WEB-INF/html/");
		vr.setSuffix(".html");
		return vr;
	}

	@Bean
	public DigestUtils webUtils() throws NoSuchAlgorithmException {
		return new DigestUtils();
	}

	@Bean
	public JsonUtils jsonUtils() {
		return new JsonUtils();
	}

	@Bean
	public AnnotationMethodHandlerAdapter messageConvertors() {
		AnnotationMethodHandlerAdapter adapter = new AnnotationMethodHandlerAdapter();

		adapter.setMessageConverters(new HttpMessageConverter[] { jsonHttpMessageConverter() });
		return adapter;
	}

	@Bean
	public MappingJacksonHttpMessageConverter jsonHttpMessageConverter() {
		MappingJacksonHttpMessageConverter convertor = new MappingJacksonHttpMessageConverter();
		return convertor;
	}

	@Bean
	public AbstractHandlerExceptionResolver exceptionResolver() {
		AnnotationMethodHandlerExceptionResolver resolver = new AnnotationMethodHandlerExceptionResolver();
		HttpMessageConverter<?>[] messageConvertors = { jsonHttpMessageConverter() };
		resolver.setMessageConverters(messageConvertors);
		return resolver;
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public PhoneNumberUtil phoneUtil() {
		return PhoneNumberUtil.getInstance();
	}
	
}
