package com.tradevan.handyflow.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.bean.UpdateUserDto;
import com.tradevan.handyflow.dto.FlowConfDto;

public interface FlowAdminService {

	String SIMPLE_FLOW_ID_PREFIX = "SIMP-";
	int SIMPLE_FLOW_SERIAL_LEN = 5;

	List<Map<String, Object>> fetchSimpleFlowSteps(String flowId);
	
	String genFlowId(String prefix, String dateFmt, int serialLen);
	
	FlowConfDto getFlowConfByFlowId(String flowId);
	
	void addFlowConf(FlowConfDto flowConfDto, CreateUserDto createUserDto, boolean isSimpleFlow, String flowExt2);
	
	boolean hasFlowSteps(String flowId);
	
	boolean hasFlowLinks(String flowId);
	
	boolean isFlowXmlShouldBeUpdated(String flowId);
	
	void addFlowXml(FlowConfDto dto, File file, CreateUserDto createUserDto, boolean isSimpleFlow);
	
}
