package com.tradevan.handyflow.parser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tradevan.handyflow.model.flow.HandyFlow;
import com.tradevan.handyflow.repository.FlowXmlRepository;

/**
 * Title: DbFlowParser<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0.1
 */
@Component
public class DbFlowParser implements FlowParser {

	@Value("${flow.highDeptUserApplyOff}")
	protected String highDeptUserApplyOff;
	
	@Autowired
//	@Qualifier("jpaFlowXmlRepository")
	private FlowXmlRepository flowXmlRepository;
	
	@Override
	public HandyFlow parse(String flowId, String version, boolean validate) throws DocumentException {
		String flowXml = (String) flowXmlRepository.getByProperties(new String[] { "flowXml" }, new String[] { "flowId", "flowVersion" }, new Object[] { flowId, version});
        SAXReader reader = new SAXReader();
        reader.setValidation(validate);
        Document doc = reader.read(new StringReader(flowXml));
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
