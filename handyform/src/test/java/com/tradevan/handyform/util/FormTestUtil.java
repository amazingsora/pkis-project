package com.tradevan.handyform.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FormTestUtil {
	
	public static FormTestUtil getInstance() {
		return new FormTestUtil();
	}
	
	public <T> T getTestJsonObj(String fileName, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(this.getClass().getClassLoader().getResourceAsStream("handyform/" + fileName + ".json"), clazz);
    }
	
	public <T> String getTestJsonStr(String fileName, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(getTestJsonObj(fileName, clazz));
    }
	
}
