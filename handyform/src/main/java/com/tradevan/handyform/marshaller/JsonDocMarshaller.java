package com.tradevan.handyform.marshaller;

import java.io.File;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradevan.handyform.exception.FormMarshallerException;

/**
 * Title: JsonDocMarshaller<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Component
public class JsonDocMarshaller {
	
	public <T> String marshal(T jsonObj) {
        try {
			return (new ObjectMapper()).writeValueAsString(jsonObj);
		}
        catch (Exception e) {
			throw new FormMarshallerException(e);
		}
    }
	
	public <T> String marshalFromFile(String fileName, Class<T> clazz) {
        try {
        	ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(mapper.readValue(this.getClass().getClassLoader().getResourceAsStream("handyform/" + fileName + ".json"), clazz));
		}
        catch (Exception e) {
			throw new FormMarshallerException(e);
		}
    }
	
	public <T> T unmarshal(String jsonStr, Class<T> clazz) {
		try {
			return (new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)).readValue(jsonStr,  clazz);
		}
        catch (Exception e) {
			throw new FormMarshallerException(e);
		}
    }
	
	public <T> T unmarshal(File jsonFile, Class<T> clazz) {
		try {
			return (new ObjectMapper()).readValue(jsonFile,  clazz);
		}
        catch (Exception e) {
			throw new FormMarshallerException(e);
		}
    }
	
	public <T> T unmarshal(InputStream jsonFileInputStream, Class<T> clazz) {
		try {
			return (new ObjectMapper()).readValue(jsonFileInputStream,  clazz);
		}
        catch (Exception e) {
			throw new FormMarshallerException(e);
		}
    }
	
}
