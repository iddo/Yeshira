package org.yeshira.utils;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class JsonUtils {
	private static final Logger logger = Logger.getLogger(JsonUtils.class);
	
	private ObjectMapper objectMapper;

	@Autowired
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	public String toJsonString(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			logger.error(e);
		} catch (JsonMappingException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}
	
	public <T> T fromJsonString(String object, Class<T> clazz) throws JsonParseException, JsonMappingException, JsonProcessingException, IOException {
		return objectMapper.treeToValue(objectMapper.readTree(object), clazz);
	}

}
