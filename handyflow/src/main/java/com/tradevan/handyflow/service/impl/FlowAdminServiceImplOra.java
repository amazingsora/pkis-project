package com.tradevan.handyflow.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.handyflow.bean.DefaultFlowAction;
import com.tradevan.handyflow.bean.DefaultFlowEvent;
import com.tradevan.handyflow.dto.FlowConfDto;
import com.tradevan.handyflow.model.form.FlowConf;
import com.tradevan.handyflow.model.form.FlowStep;
import com.tradevan.handyflow.model.form.FlowStepLink;
import com.tradevan.handyflow.model.form.FlowXml;
import com.tradevan.handyflow.model.form.SubFlowConf;
import com.tradevan.handyflow.repository.FlowConfRepository;
import com.tradevan.handyflow.repository.FlowStepLinkRepository;
import com.tradevan.handyflow.repository.FlowStepRepository;
import com.tradevan.handyflow.repository.FlowXmlRepository;
import com.tradevan.handyflow.repository.SubFlowConfRepository;
import com.tradevan.handyflow.service.FlowAdminService;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FlowAdminServiceImplOra implements FlowAdminService {

	@Autowired
	//@Qualifier("jpaFlowConfRepository")
	private FlowConfRepository flowConfRepository;
	
	@Autowired
	//@Qualifier("jpaFlowStepRepository")
	private FlowStepRepository flowStepRepository;
	
	@Autowired
	//@Qualifier("jpaFlowStepLinkRepository")
	private FlowStepLinkRepository flowStepLinkRepository;
	
	@Autowired
	//@Qualifier("jpaSubFlowConfRepository")
	private SubFlowConfRepository subFlowConfRepository;
	
	@Autowired
	//@Qualifier("jpaFlowXmlRepository")
	private FlowXmlRepository flowXmlRepository;
	
	@Override
	public List<Map<String, Object>> fetchSimpleFlowSteps(String flowId) {
		return flowStepRepository.findSimpleFlowSteps(flowId);
	}
	
	@Override
	public String genFlowId(String prefix, String dateFmt, int serialLen) {
		String prefixVal = getPrefixVal(prefix);
		String dateFmtVal = getDateFmtVal(dateFmt);
		
		String maxSerial = flowConfRepository.getMaxFlowIdSerial(prefixVal, dateFmtVal, serialLen);
		
		return formatFlowId(null, prefixVal, dateFmtVal, serialLen, maxSerial);
	}
	
	private String getPrefixVal(String prefix) {
		return (prefix != null) ? prefix : "";
	}
	
	private String getDateFmtVal(String dateFmt) {
		return (dateFmt != null) ? (new SimpleDateFormat(dateFmt).format(new Date())) : "";
	}
	
	private String formatFlowId(String formId, String prefixVal, String dateFmtVal, int serialLen, String maxSerial) {
		String serial = String.valueOf((maxSerial != null) ? Integer.parseInt(maxSerial) + 1 : 1);
		
		if (serial.length() > serialLen) {
			new IllegalStateException("Serial overflows. (flowId:" + prefixVal + dateFmtVal + serial + ")");
		}
		
		StringBuilder buf = new StringBuilder();
		int len = serialLen - serial.length();
		for (int x = 0; x < len; x++) {
			buf.append("0");
		}
		buf.append(serial);
		
		return prefixVal + dateFmtVal + buf.toString();
	}
	
	@Override
	public FlowConfDto getFlowConfByFlowId(String flowId) {
		FlowConf flow = flowConfRepository.getEntityByProperty("flowId", flowId);
		if (flow != null) {
			return new FlowConfDto(flow);
		}
		return null;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addFlowConf(FlowConfDto flowConfDto, CreateUserDto createUserDto, boolean isSimpleFlow, String flowExt2) {
		FlowConf flowConf = new FlowConf(flowConfDto, createUserDto);
		flowConf.setVersion(isSimpleFlow ? "0" : "1"); //版本一開始為第一版
		flowConfRepository.save(flowConf);
		
		if (isSimpleFlow == true) { //簡易流程自動建立申請人步驟(Task1)
			if ("RESIDENT_ASSESS".equalsIgnoreCase(flowExt2)) { // 住院醫師考核表審核流程固定
				FlowStep flowStep1 = new  FlowStep(flowConf.getFlowId(), "Task1", "C", "初核人", "初核人回饋", 0, createUserDto.getCreateUserId());
				flowStepRepository.save(flowStep1);
				FlowStep flowStep2 = new  FlowStep(flowConf.getFlowId(), "Task2", "C", "複核人", "複核人回饋", 1, createUserDto.getCreateUserId());
				flowStep2.setRoleIds("LEADER1");
				flowStepRepository.save(flowStep2);
				FlowStep flowStep3 = new  FlowStep(flowConf.getFlowId(), "Task3", "C", "受評者", "受評者簽認", 2, createUserDto.getCreateUserId());
				flowStep3.setExtension("beEval"); // 受評者
				flowStepRepository.save(flowStep3);
				FlowStep flowStep4 = new  FlowStep(flowConf.getFlowId(), "Task4", "C", "部主任", "部主任", 3, createUserDto.getCreateUserId());
				flowStep4.setRoleIds("LEADER2");
				flowStepRepository.save(flowStep4);
			}
			else if ("CUSTOM_MULTI".equalsIgnoreCase(flowExt2)) { // 自訂表單多筆預設審核流程
				FlowStep flowStep1 = new  FlowStep(flowConf.getFlowId(), "Task1", "C", "學員", "學員填寫", 0, createUserDto.getCreateUserId());
				flowStepRepository.save(flowStep1);
				FlowStep flowStep2 = new  FlowStep(flowConf.getFlowId(), "Task2", "C", "教師", "教師審核", 1, createUserDto.getCreateUserId());
				flowStep2.setExtension("teacher");
				flowStepRepository.save(flowStep2);
			}
			else if ("NURSING_EVAL".equalsIgnoreCase(flowExt2)) { // 護理評值表審核流程固定
				FlowStep flowStep1 = new  FlowStep(flowConf.getFlowId(), "Task1", "C", "申請人", "申請人回饋", 0, createUserDto.getCreateUserId());
				flowStepRepository.save(flowStep1);
				FlowStep flowStep2 = new  FlowStep(flowConf.getFlowId(), "Task2", "C", "護理長", "護理長回饋", 1, createUserDto.getCreateUserId());
				flowStep2.setRoleIds("LEADER1");
				flowStepRepository.save(flowStep2);
				FlowStep flowStep3 = new  FlowStep(flowConf.getFlowId(), "Task3", "C", "護理督導", "護理督導回饋", 2, createUserDto.getCreateUserId());
				flowStep3.setRoleIds("NURSE_SUP");
				flowStepRepository.save(flowStep3);
				FlowStep flowStep4 = new  FlowStep(flowConf.getFlowId(), "Task4", "C", "護理教育委員會", "護理教育委員會回饋", 3, createUserDto.getCreateUserId());
				flowStep4.setRoleIds("NURSE_CMT");
				flowStepRepository.save(flowStep4);
			}
			else {
				FlowStep flowStep1 = new  FlowStep(flowConf.getFlowId(), "Task1", "C", "申請人", "申請人回饋", 0, createUserDto.getCreateUserId());
				flowStepRepository.save(flowStep1);
				FlowStep flowStep2 = new  FlowStep(flowConf.getFlowId(), "Task2", "C", "申請人主管", "申請人主管回饋", 1, createUserDto.getCreateUserId());
				flowStep2.setRoleIds("LEADER1");
				flowStepRepository.save(flowStep2);
			}
		}
	}

	@Override
	public boolean hasFlowSteps(String flowId) {
		return flowStepRepository.countByProperty("flowId", flowId) > 0 ? true : false;
	}
	
	@Override
	public boolean hasFlowLinks(String flowId) {
		return flowStepLinkRepository.countByProperty("flowId", flowId) > 0 ? true : false;
	}
	
	@Override
	public boolean isFlowXmlShouldBeUpdated(String flowId) {
		FlowConf flowConf = flowConfRepository.getEntityByProperty("flowId", flowId);
		if (flowConf != null) {
			Date flowXmlCreateTime = (Date) flowXmlRepository.getByProperties(new String[] { "createTime" }, 
					new String[] { "flowId", "flowVersion" }, new Object[] { flowId, flowConf.getVersion() });
			if (flowXmlCreateTime == null) {
				return true;
			}
			else {
				if (flowConf.getUpdateTime().after(flowXmlCreateTime)) {
					return true;
				}
				
				for (FlowStep flowStep : flowConf.getFlowSteps()) {
					if (flowStep.getUpdateTime().after(flowXmlCreateTime)) {
						return true;
					}
					for (FlowStepLink flowStepLink : flowStep.getFlowStepLinks()) {
						if (flowStepLink.getUpdateTime().after(flowXmlCreateTime)) {
							return true;
						}
					}
				}
				
				for (SubFlowConf subFlow : flowConf.getSubFlowConfs()) {
					for (FlowStep flowStep : subFlow.getFlowSteps()) {
						if (flowStep.getUpdateTime().after(flowXmlCreateTime)) {
							return true;
						}
						for (FlowStepLink flowStepLink : flowStep.getFlowStepLinks()) {
							if (flowStepLink.getUpdateTime().after(flowXmlCreateTime)) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addFlowXml(FlowConfDto dto, File file, CreateUserDto createUserDto, boolean isSimpleFlow) {
		// 簡易流程重新產生FlowStepLink
		if (isSimpleFlow) {
			flowStepLinkRepository.deleteByProperty("flowId", dto.getFlowId());
			List<FlowStep> flowSteps = flowStepRepository.findEntityListByProperty("flowId", dto.getFlowId(), "dispOrd");
			int size = flowSteps.size();
			for (int x = 0; x < size; x++) {
				FlowStep step = flowSteps.get(x);
				if (x == 0) { // 第一關卡
					flowStepLinkRepository.save(new FlowStepLink(dto.getFlowId(), step.getStepId(), 
							size > 1 ? flowSteps.get(x + 1).getStepId() : "End", DefaultFlowAction.APPLY.getAction(), false, "送出", "", 10, createUserDto.getUpdateUserId()));
					flowStepLinkRepository.save(new FlowStepLink(dto.getFlowId(), step.getStepId(), 
							"Cancel", DefaultFlowAction.CANCEL.getAction(), false, "作廢", "", 20, createUserDto.getUpdateUserId()));
				}
				else {
					if (x == (size-1)) { // 最後關卡
						flowStepLinkRepository.save(new FlowStepLink(dto.getFlowId(), step.getStepId(), 
								"End", DefaultFlowAction.APPROVE.getAction(), false, "審查完成", "", 10, createUserDto.getUpdateUserId()));
					}
					else {
						flowStepLinkRepository.save(new FlowStepLink(dto.getFlowId(), step.getStepId(), 
								flowSteps.get(x + 1).getStepId(), DefaultFlowAction.APPROVE.getAction(), false, "審查", "", 10, createUserDto.getUpdateUserId()));
					}
					flowStepLinkRepository.save(new FlowStepLink(dto.getFlowId(), step.getStepId(), 
							flowSteps.get(0).getStepId(), DefaultFlowAction.RETURN.getAction(), false, "退回申請人", "", 20, createUserDto.getUpdateUserId()));
					//flowStepLinkRepository.save(new FlowStepLink(dto.getFlowId(), step.getStepId(), 
					//		flowSteps.get(x - 1).getStepId(), DefaultFlowAction.RETURN.getAction(), false, "退回", "", 20, createUserDto.getUpdateUserId()));
				}
			}
		}
		
		//update flowVersion
		FlowConf flow = flowConfRepository.getEntityById(dto.getId());
		flow.setVersion(String.valueOf((Integer.parseInt(flow.getVersion())+1)));
		BeanUtils.copyProperties(createUserDto, flow);
		
		BeanUtils.copyProperties(flow, dto);
		Document document = convert2DocumentXml(dto);
		
		FlowXml flowXml = new FlowXml(flow.getFlowId(), flow.getVersion(), createUserDto.getUpdateUserId());
		flowXml.setFlowXml(document.asXML());
		flowXmlRepository.save(flowXml);
		
		if (file != null) {
			saveFlowXml2File(document, file);
		}
	}
	
	private Document convert2DocumentXml(FlowConfDto dto) {
        Document document = DocumentHelper.createDocument();
        addHandyFlow(document, dto);
        return document;
	}
	
	private void addHandyFlow(Document document, FlowConfDto dto) {
		//handyFlow
		Element handyFlow = document.addElement("handyflow")
			.addAttribute("id", dto.getFlowId())
			.addAttribute("name", dto.getName())
			.addAttribute("version", dto.getVersion());
		if(StringUtils.isNotBlank(dto.getDesc())) handyFlow.addAttribute("desc", dto.getDesc());
		document.addDocType("handyflow", null, null); //設置DOCTYPE
		
		//flowEvent : BEGIN
		List<Map<String, Object>> stepsNotBySubFlow = flowStepRepository.findFlowStepsNotBySubFlow(dto.getFlowId());
		Map<String, Object> firstStep = stepsNotBySubFlow.get(0);
		String firstStepId = "";
		if(firstStep.get("stepId".toUpperCase()) != null) firstStepId = (String)firstStep.get("stepId".toUpperCase());
		addFlowEvent(handyFlow, DefaultFlowEvent.BEGIN.getType(), firstStepId, null, null);
		
		//flowTask
		for(Map<String, Object> step : stepsNotBySubFlow) {
			addFlowTask(handyFlow, step);
		}
		
		//flowEvent : END/CANCEL
		addFlowEvent(handyFlow, DefaultFlowEvent.END.getType(), null, null, null);
		addFlowEvent(handyFlow, DefaultFlowEvent.CANCEL.getType(), null, null, null);
		
		for (SubFlowConf sub : subFlowConfRepository.findEntityListByProperty("flowId", dto.getFlowId(), "subFlowId")) {
			List<Map<String, Object>> stepsBySubFlow = flowStepRepository.findFlowStepsBySubFlow(dto.getFlowId(), sub.getSubFlowId());
			Map<String, Object> firstStepBySubFlow = stepsBySubFlow.get(0);
			//subFlow  
			Element subFlow = addSubFlow(handyFlow, sub);
			//flowEvent : SUB_BEGIN
			addFlowEvent(subFlow, DefaultFlowEvent.SUB_BEGIN.getType(), null, sub, firstStepBySubFlow);
			//flowTask
			for(Map<String, Object> step : stepsBySubFlow) {
				addFlowTask(subFlow, step);
			}			
			//flowEvent : SUB_FINISH
			addFlowEvent(subFlow, DefaultFlowEvent.SUB_FINISH.getType(), null, sub, firstStepBySubFlow);
			//flowEvent : SUB_RETURN
			addFlowEvent(subFlow, DefaultFlowEvent.SUB_RETURN.getType(), null, sub, firstStepBySubFlow);
		}
	}
	
	private void addFlowEvent(Element mainRootElement, String eventType, String firstStepId, SubFlowConf sub, Map<String, Object> firstStepBySubFlow) {
		String[] attrAry = parseFlowEventAttr(eventType, firstStepId, sub, firstStepBySubFlow);
		
		if(StringUtils.equals(eventType, DefaultFlowEvent.BEGIN.getType())
				|| StringUtils.equals(eventType, DefaultFlowEvent.SUB_BEGIN.getType())
				|| StringUtils.equals(eventType, DefaultFlowEvent.SUB_FINISH.getType())
				|| StringUtils.equals(eventType, DefaultFlowEvent.SUB_RETURN.getType()) ) { 
	        Element flowEvent = mainRootElement.addElement("flowEvent")
		    	.addAttribute("id", attrAry[0])	
		    	.addAttribute("type", attrAry[1])	
		    	.addAttribute("desc", attrAry[2]);	
		    flowEvent.addElement("flowLink")
		    	.addAttribute("name", attrAry[3])	
		    	.addAttribute("to", attrAry[4]);		
		}else { 
			mainRootElement.addElement("flowEvent")
        		.addAttribute("id", attrAry[0])	
        		.addAttribute("type", attrAry[1])	
        		.addAttribute("desc", attrAry[2]);					
		}
	}

	/**
	 * @return String[] : 0:id, 1:type, 2:desc, 3:name, 4:to
	 */
	private String[] parseFlowEventAttr(String eventType, String firstStepId, SubFlowConf sub, Map<String, Object> firstStepBySubFlow) {
		String[] attrAry = new String[5]; //0:id,1:type,2:desc,3:name,4:to
		if(StringUtils.equals(eventType, DefaultFlowEvent.BEGIN.getType())) {
			attrAry[0] = DefaultFlowEvent.BEGIN.getEvent();
			attrAry[1] = DefaultFlowEvent.BEGIN.getType();
			attrAry[2] = DefaultFlowEvent.BEGIN.getName();
			attrAry[3] = "建立";
			attrAry[4] = firstStepId;
		}else if(StringUtils.equals(eventType, DefaultFlowEvent.SUB_BEGIN.getType())) {
			attrAry[0] = (String)firstStepBySubFlow.get("subFlowId".toUpperCase()) + DefaultFlowEvent.SUB_BEGIN.getEvent();
			attrAry[1] = DefaultFlowEvent.SUB_BEGIN.getType();
			attrAry[2] = DefaultFlowEvent.SUB_BEGIN.getName();
			attrAry[3] = "開始";
			attrAry[4] = (String)firstStepBySubFlow.get("stepId".toUpperCase());					
		}else if(StringUtils.equals(eventType, DefaultFlowEvent.SUB_FINISH.getType())) {
			attrAry[0] = (String)firstStepBySubFlow.get("subFlowId".toUpperCase()) + DefaultFlowEvent.SUB_FINISH.getEvent();
			attrAry[1] = DefaultFlowEvent.SUB_FINISH.getType();
			attrAry[2] = DefaultFlowEvent.SUB_FINISH.getName();
			attrAry[3] = "完成";
			attrAry[4] = sub.getFinishTo();			
		}else if(StringUtils.equals(eventType, DefaultFlowEvent.SUB_RETURN.getType())) {
			attrAry[0] = (String)firstStepBySubFlow.get("subFlowId".toUpperCase()) + DefaultFlowEvent.SUB_RETURN.getEvent();
			attrAry[1] = DefaultFlowEvent.SUB_RETURN.getType();
			attrAry[2] = DefaultFlowEvent.SUB_RETURN.getName();
			attrAry[3] = "退回";
			attrAry[4] = sub.getReturnTo();					
		}else if(StringUtils.equals(eventType, DefaultFlowEvent.END.getType())){
			attrAry[0] = DefaultFlowEvent.END.getEvent();
			attrAry[1] = DefaultFlowEvent.END.getType();
			attrAry[2] = DefaultFlowEvent.END.getName();			
		}else if(StringUtils.equals(eventType, DefaultFlowEvent.CANCEL.getType())){
			attrAry[0] = DefaultFlowEvent.CANCEL.getEvent();
			attrAry[1] = DefaultFlowEvent.CANCEL.getType();
			attrAry[2] = DefaultFlowEvent.CANCEL.getName();			
		}
		return attrAry;
	}
	
	private void addFlowTask(Element handyFlow, Map<String, Object> step) {
		Element mainRootElement = null;
		String stepType = (String)step.get("stepType".toUpperCase());
		boolean isFlowParallel = false;
		if(StringUtils.equals(stepType, "C")) { //一般步驟
			mainRootElement = handyFlow.addElement("flowTask");
		}else { //平行會簽
			mainRootElement = handyFlow.addElement("flowParallel");
			isFlowParallel = true;
		}
		
		String flowId = (String)step.get("flowId".toUpperCase());
		String stepId = (String)step.get("stepId".toUpperCase());
		
		mainRootElement.addAttribute("id", (String)step.get("stepId".toUpperCase()));
		mainRootElement.addAttribute("name", (String)step.get("stepName".toUpperCase()));
		if(step.get("stepDesc".toUpperCase()) != null && !"".equals(step.get("stepDesc".toUpperCase()))) mainRootElement.addAttribute("desc", (String)step.get("stepDesc".toUpperCase()));
		if(step.get("deptId".toUpperCase()) != null && !"".equals(step.get("deptId".toUpperCase()))) mainRootElement.addAttribute("dept", (String)step.get("deptId".toUpperCase()));
		if(step.get("roleIds".toUpperCase()) != null && !"".equals(step.get("roleIds".toUpperCase()))) mainRootElement.addAttribute("roles", (String)step.get("roleIds".toUpperCase()));
		if(step.get("userIds".toUpperCase()) != null && !"".equals(step.get("userIds".toUpperCase()))) mainRootElement.addAttribute("users", (String)step.get("userIds".toUpperCase()));
		if(step.get("sameUserAs".toUpperCase()) != null && !"".equals(step.get("sameUserAs".toUpperCase()))) mainRootElement.addAttribute("sameUserAs", (String)step.get("sameUserAs".toUpperCase()));
		if(step.get("extension".toUpperCase()) != null && !"".equals(step.get("extension".toUpperCase()))) mainRootElement.addAttribute("taskExt", (String)step.get("extension".toUpperCase()));
		Boolean isReviewDeptRole = false;
		if(null!=step.get("isReviewDeptRole".toUpperCase()) ) {
			isReviewDeptRole = Boolean.valueOf(String.valueOf(step.get("isReviewDeptRole".toUpperCase())));
		}
		
		if(null!=isReviewDeptRole && isReviewDeptRole) {
			 mainRootElement.addAttribute("isReviewDeptRole".toUpperCase(), "true");
		}
		Boolean isProjRole = false;
		if(null!=step.get("isProjRole".toUpperCase()) ) {
			isProjRole = Boolean.valueOf(String.valueOf(step.get("isProjRole".toUpperCase())));
		}
		if(null!= isProjRole&& isProjRole) {
			mainRootElement.addAttribute("isProjRole", "true");
		}
		if(step.get("parallelPassCount".toUpperCase()) != null && !"".equals(step.get("parallelPassCount".toUpperCase()))) mainRootElement.addAttribute("parallelPassCount", String.valueOf((Integer)step.get("parallelPassCount".toUpperCase())));
		
		//flowConditions + flowLink
		List<FlowStepLink> linkList = flowStepLinkRepository.findEntityListByProperties(new String[] {"flowId", "stepId"}, new String[] {flowId, stepId}, new String[] {"dispOrd"}, null, null);
		addFlowLinks(mainRootElement, linkList, isFlowParallel);
	}
	
	private void addFlowLinks(Element mainRootElement, List<FlowStepLink> linkList, boolean isFlowParallel) {
		Element appendElement = null;
		if(linkList.size() > 1 && isFlowParallel == false) { //flowConditions ： 有兩個以上的flowLink才加flowConditions
			appendElement = mainRootElement.addElement("flowConditions");
		}else {
			appendElement = mainRootElement;
		}
		
		for(FlowStepLink link : linkList) {
			Element flowLink = appendElement.addElement("flowLink");
			if(StringUtils.isNotBlank(link.getAction())) flowLink.addAttribute("action", link.getAction());
			flowLink.addAttribute("name", link.getName());
			flowLink.addAttribute("to", link.getToStepId());
			if(StringUtils.isNotBlank(link.getDesc())) flowLink.addAttribute("desc", link.getDesc());
			if(link.getIsConcurrent()) flowLink.addAttribute("isConcurrent", "true");
		}		
	}
	
	private Element addSubFlow(Element handyFlow, SubFlowConf sub) {
		Element subFlow = handyFlow.addElement("subFlow");
		subFlow.addAttribute("id", sub.getSubFlowId());
		subFlow.addAttribute("name", sub.getName());
		if(StringUtils.isNotBlank(sub.getDesc())) subFlow.addAttribute("desc", sub.getDesc());
		return subFlow;
	}
	
	private void saveFlowXml2File(Document document, File file) {
		if(file.exists()) file.delete(); 

		FileOutputStream fos = null; 
		OutputStreamWriter osw = null; 
		XMLWriter writer = null; 
		try { 
			file.createNewFile(); 

			OutputFormat format = OutputFormat.createPrettyPrint();   
			fos = new FileOutputStream(file); 
			osw = new OutputStreamWriter(fos, Charset.forName("utf-8")); 
			writer = new XMLWriter(osw, format); 
			writer.write(document); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} finally { 
			try { 
				if(writer != null) writer.close(); 
				if(osw != null) osw.close(); 
				if(fos != null) fos.close(); 
			} catch (IOException e) {} 
		} 		
	}
	
}
