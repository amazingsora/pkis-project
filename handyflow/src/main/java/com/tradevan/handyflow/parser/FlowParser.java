package com.tradevan.handyflow.parser;

import org.dom4j.DocumentException;

import com.tradevan.handyflow.model.flow.HandyFlow;

/**
 * Title: FlowParser<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public interface FlowParser {

	HandyFlow parse(String flowId, String version, boolean validate) throws DocumentException;
	
}
