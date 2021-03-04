package com.tradevan.handyflow.parser;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tradevan.handyflow.model.flow.HandyFlow;

/**
 * Title: FileFlowParser<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0.1
 */
@Component
public class FileFlowParser implements FlowParser {

	@Value("${flow.highDeptUserApplyOff}")
	protected String highDeptUserApplyOff;
	
	@Override
	public HandyFlow parse(String flowId, String version, boolean validate) throws DocumentException {
        SAXReader reader = new SAXReader();
        reader.setValidation(validate);
        Document doc = reader.read(this.getClass().getClassLoader().getResourceAsStream("handyflow/" + flowId + "_v" + version + ".xml"));
        Element root = doc.getRootElement();
        HandyFlow handyflow = new HandyFlow(
        		null,
        		root.attributeValue("id"), 
        		root.attributeValue("name"), 
        		root.attributeValue("desc"), 
        		root.attributeValue("version"));
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("highDeptUserApplyOff", highDeptUserApplyOff);
        handyflow.parse(root, attributes);
        
        return (HandyFlow) handyflow;
    }
}
