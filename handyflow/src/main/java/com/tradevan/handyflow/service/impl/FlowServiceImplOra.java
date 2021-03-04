package com.tradevan.handyflow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tradevan.apcommon.bean.NameValuePair;
import com.tradevan.apcommon.util.StringUtil;
import com.tradevan.aporg.bean.UserDto;
import com.tradevan.aporg.model.UserProf;
import com.tradevan.aporg.service.OrgService;
import com.tradevan.handyflow.bean.DefaultFlowAction;
import com.tradevan.handyflow.bean.DefaultFlowEvent;
import com.tradevan.handyflow.bean.DocStateBean;
import com.tradevan.handyflow.bean.FlowBean;
import com.tradevan.handyflow.exception.FlowParseException;
import com.tradevan.handyflow.model.flow.FlowComponent;
import com.tradevan.handyflow.model.flow.FlowEvent;
import com.tradevan.handyflow.model.flow.FlowLink;
import com.tradevan.handyflow.model.flow.FlowTask;
import com.tradevan.handyflow.model.flow.HandyFlow;
import com.tradevan.handyflow.model.flow.LinkAction;
import com.tradevan.handyflow.model.form.DocState;
import com.tradevan.handyflow.model.form.DocStateLog;
import com.tradevan.handyflow.model.form.FormConf;
import com.tradevan.handyflow.parser.DbFlowParser;
import com.tradevan.handyflow.parser.FileFlowParser;
import com.tradevan.handyflow.parser.FlowParser;
import com.tradevan.handyflow.repository.DocStateLogRepository;
import com.tradevan.handyflow.repository.DocStateRepository;
import com.tradevan.handyflow.repository.FlowConfRepository;
import com.tradevan.handyflow.repository.FlowStepRepository;
import com.tradevan.handyflow.repository.FormConfRepository;
import com.tradevan.handyflow.service.FlowService;
import com.tradevan.handyflow.service.NotifyService;

/**
 * Title: FlowServiceImpl<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.8.11
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FlowServiceImplOra implements FlowService {
	
	@Value("${flow.xmlSrc}")
	private String flowXmlSrc;
	
	@Autowired
	//@Qualifier("jpaFlowConfRepository")
	private FlowConfRepository flowConfRepository;
	
	@Autowired
	//@Qualifier("jpaFormConfRepository")
	private FormConfRepository formConfRepository;
	
	@Autowired
	//@Qualifier("jpaDocStateRepository")
	private DocStateRepository docStateRepository;
	
	@Autowired
	//@Qualifier("jpaDocStateLogRepository")
	private DocStateLogRepository docStateLogRepository;
	
	@Autowired
	//@Qualifier("jpaFlowStepRepository")
	private FlowStepRepository flowStepRepository;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	@Qualifier("notifyServiceImplOra")
	private NotifyService notifyService;
	
	@Autowired
	private FileFlowParser fileFlowParser;
	
	@Autowired
	private DbFlowParser dbFlowPrser;
	
	@Override
	public String getNowFlowVersion(String flowId) {
		String flowVersion = (String) flowConfRepository.getByProperty(new String[] { "version" }, "flowId", flowId);
		return flowVersion != null ? flowVersion : "1";
	}
	
	@Override
	public String getFlowAdminId(String flowId) {
		String flowAdminId = (String) flowConfRepository.getByProperty(new String[] { "flowAdminId" }, "flowId", flowId);
		return flowAdminId != null ? flowAdminId : "admin";
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public DocStateBean startFlow(FlowBean flowBean, LinkAction action) {
		
		checkFlowBean(flowBean);
		
		try {
			HandyFlow flow = getFlowParser().parse(flowBean.getFlowId(), flowBean.getFlowVersion(), false);
			FlowEvent begEvent = flow.getFlowEvent(FlowEvent.Type.BEGIN);
			FlowLink nextLink = begEvent.getFlowLink(action);
			FlowTask nextTask = flow.getFlowTask(nextLink.getTo());
			
			FormConf formConf = formConfRepository.getEntityByProperty("formId", flowBean.getFormId());
			UserProf user = orgService.fetchUserByUserId(flowBean.getUserId());
			
			DocState docState = new DocState(formConf, flowBean.getApplyNo(), 0, flowBean.getFlowId(), flowBean.getFlowVersion(), begEvent.getType().getValue(), 
					user, flowBean.getAgentId(), flowBean.getProjId(), flowBean.getMainFormId());
			docState.setNowTaskId(nextLink.getTo());
			docState.setTaskName(flow.getName());
			docState.setTaskDesc(begEvent.getDesc());
			docState.setLinkName(nextLink.getName());
			docState.setLinkDesc(nextLink.getDesc());
			docState.setMainFormTable(flowBean.getMainFormTable());
			
			docState = docStateRepository.save(docState);
			
			DocStateLog docStateLog = new DocStateLog(docState, begEvent.getClass().getSimpleName(), begEvent.getId(), nextLink.getTo(), 
					refUser(flowBean.getBeRepresentedId()), refUser(flowBean.getAgentId()), null);
			
			docStateLog = docStateLogRepository.save(docStateLog);
			
			docState.setFlowStatus(DocState.FLOW_STATUS_DRAFT);
			docState.setTaskName(nextTask.getName());
			docState.setTaskDesc(nextTask.getDesc());
			
			docState.setFlowAdminId(getFlowAdminId(flowBean.getFlowId()));
			docState.setSysId(flowBean.getSysId());
			docState.setCanBatchReview(flowBean.getCanBatchReview());
			docState.setSubject(flowBean.getSubject());
			
			return new DocStateBean(docState, flowBean.getUserId(), flowBean.getBeRepresentedId(), flowBean.getAgentId(), false, docStateLog.getId());
		}
		catch (DocumentException e) {
			throw new FlowParseException("Flow id:" +  flowBean.getFlowId() + " version:" + flowBean.getFlowVersion() + " parse failed!!", e);
		}
	}

	private FlowParser getFlowParser() {
		if ("DB".equalsIgnoreCase(flowXmlSrc)) {
			return dbFlowPrser;
		}
		return fileFlowParser;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public DocStateBean apply(DocStateBean docStateBean, LinkAction action) {
		
		checkDocStateBean(docStateBean);
		
		DocState docState = docStateRepository.getEntityById(docStateBean.getId());
		
		checkDocState(docStateBean, docState, true);
		
		/* Remark because allowing return to non-original applicant
		if (!docState.getApplicant().getUserId().equals(docState.getNowUser().getUserId())) {
			throw new IllegalStateException(
					"When apply the formId:" + docStateBean.getFormId() + " applyNo:" + docStateBean.getApplyNo() + 
					" applicant:" + docState.getApplicant().getUserId() + " should same as now user:" + docState.getNowUser().getUserId());
		}*/
		
		claim(docStateBean);
		
		if (docStateBean.getApplyNo() != null) {
			// Update tmpApplyNo to applyNo if necessary
			docState.setApplyNo(docStateBean.getApplyNo());
		}
		
		if (docStateBean.getReviewDeptId() != null) {
			docState.setLastReviewDept(orgService.fetchDeptByDeptId(docStateBean.getReviewDeptId()));
		}
		
		renewSubjectOrFormId(docStateBean, docState);
		
		docState.setRtnToOrig(docStateBean.isRtnToOrig());
		docState.setNoParallelSubFlows(docStateBean.getNoParallelSubFlows());
		docState.setAssignParallelPassCount(docStateBean.getAssignParallelPassCount());
		
		return processInternal(docState, action, null, null, docStateBean.getBeRepresentedId(), docStateBean.getAgentId(), docStateBean.getMemo());
	}

	private void checkFlowBean(FlowBean flowBean) {
		if (flowBean == null) {
			throw new IllegalArgumentException("Parameter flowBean can not be null");
		}
	}
	
	private void checkDocStateBean(DocStateBean docStateBean) {
		if (docStateBean == null) {
			throw new IllegalArgumentException("Parameter docStateBean can not be null");
		}
	}
	
	private void checkDocState(DocStateBean docStateBean, DocState docState, boolean needCheckTaskIdAndFlowStatus) {
		if (docState == null) {
			throw new IllegalArgumentException(
					"DocState doesn't exist. (formId:" + docStateBean.getFormId() + " applyNo:" + docStateBean.getApplyNo() + " serialNo:" + docStateBean.getSerialNo() + ")");
		}
		else if (needCheckTaskIdAndFlowStatus) {
			if (StringUtil.isNotBlank(docStateBean.getTaskId()) && !docStateBean.getTaskId().equals(docState.getNowTaskId())) {
				throw new IllegalStateException(
						"To-do's taskId:" + docStateBean.getTaskId() + " is not match DocState's nowTaskId:" + docState.getNowTaskId() +
						" (formId:" + docStateBean.getFormId() + " applyNo:" + docStateBean.getApplyNo() + " serialNo:" + docStateBean.getSerialNo() + ")");
			}
			if (! (DocState.FLOW_STATUS_DRAFT.equals(docState.getFlowStatus()) || DocState.FLOW_STATUS_PROCESS.equals(docState.getFlowStatus()) || DocState.FLOW_STATUS_PARALLEL.equals(docState.getFlowStatus()))) {
				throw new IllegalStateException(
						"DocState is either end or canceled. flowStatus:" + docState.getFlowStatus() +
						" (formId:" + docStateBean.getFormId() + " applyNo:" + docStateBean.getApplyNo() + " serialNo:" + docStateBean.getSerialNo() + ")");
			}
		}
	}
	
	private void renewSubjectOrFormId(DocStateBean docStateBean, DocState docState) {
		if (docStateBean.getSubject() != null) {
			docState.setSubject(docStateBean.getSubject());
		}
		if (docStateBean.getNewFormId() != null) {
			FormConf formConf = formConfRepository.getEntityByProperty("formId", docStateBean.getNewFormId());
			if (formConf == null) {
				throw new IllegalArgumentException("Then newFormId:" + docStateBean.getNewFormId() + " can not be found");
			}
			docState.setFormConf(formConf);
		}
	}
	
	private DocStateBean processInternal(DocState docState, LinkAction action, UserProf nextUser, String[] concurrentUserIds, String beRepresentedId, String agentId, String memo) {
		try {
			HandyFlow flow = getFlowParser().parse(docState.getFlowId(), docState.getFlowVersion(), false);
			FlowTask nowTask = flow.getFlowTask(docState.getNowTaskId());
			
			nowTask.processBefore(docState, orgService);
			
			FlowLink nextLink = nowTask.getFlowLink(action);
			FlowComponent nextComp = flow.getFlowComponent(nextLink.getTo());
			
			FlowComponent lastNextComp = null;
			do {
				lastNextComp = nextComp;
				nextComp = nextComp.processInternalBefore(docState, orgService);
			} while (lastNextComp != nextComp);
			
			docState.setLinkName(nextLink.getName());
			docState.setLinkDesc(nextLink.getDesc());
			docState.setUpdateTime(new Date());
			
			DocStateLog docStateLog = new DocStateLog(docState, nowTask.getClass().getSimpleName(), nowTask.getId(), nextComp.getId(), 
					refUser(beRepresentedId), refUser(agentId), memo);
			
			docStateLog = docStateLogRepository.save(docStateLog);
			
			List<UserProf> nextConcurrentUsers = null;
			if (concurrentUserIds != null) {
				nextConcurrentUsers = new ArrayList<UserProf>();
				for (String userId : concurrentUserIds) {
					UserProf user = refUser(userId);
					if (user != null && !nextConcurrentUsers.contains(user)) {
						nextConcurrentUsers.add(user);
					}
				}
			}
			
			docState.setNowTaskId(nextComp.getId());
			docState.setFlowStatus(DocState.FLOW_STATUS_PROCESS);
			docState.setTaskName(nextComp.getName());
			docState.setTaskDesc(nextComp.getDesc());
			docState.setNowUser(nextUser);
			docState.setLinkName(null);
			docState.setLinkDesc(null);
			docState.setAssigned(nextUser != null ? true : false);
			
			List<DocState> subDocStates = nextComp.processInternalAfter(docState, docStateRepository, docStateLogRepository, orgService, refUser(beRepresentedId), refUser(agentId), 
					nextConcurrentUsers);
			
			if (docState.isAssigned() == false && docState.isRtnToOrig() == true) {
				for (DocStateLog stateLog : docState.getDocStateLogs()) {
					if (docState.getNowTaskId().equalsIgnoreCase(stateLog.getFromTaskId())) {
						docState.setNowUser(stateLog.getUser());
						break;
					}
				}
			}
			
			docState.setByFlowAdminId(null);
			if (subDocStates != null) {
				for (DocState subDocState : subDocStates) {
					subDocState.setByFlowAdminId(null);
				}
			}
			
			notifyService.processNotificationsAndReplaceDisabledUser(docState, subDocStates);
			
			return new DocStateBean(docState, null, beRepresentedId, agentId, false, docStateLog.getId());
		}
		catch (DocumentException e) {
			throw new FlowParseException("Flow id:" +  docState.getFlowId() + " version:" + docState.getFlowVersion() + " parse failed!!", e);
		}
	}
	
	private UserProf refUser(String userId) {
		if (userId != null) {
			return orgService.fetchUserByUserId(userId);
		}
		return null;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public DocStateBean claim(DocStateBean docStateBean) {
		
		checkDocStateBean(docStateBean);
		checkUserId(docStateBean.getUserId());
		
		DocState docState = docStateRepository.getEntityById(docStateBean.getId());
		checkDocState(docStateBean, docState, true);
		
		if (docState.getNowUser() != null && !docState.getNowUser().getUserId().equals(docStateBean.getUserId()) && docStateBean.isSuper() == false) {
			throw new IllegalStateException(
					"The formId:" + docStateBean.getFormId() + " applyNo:" + docStateBean.getApplyNo() + " was already been claimed by " + docState.getNowUser().getUserId());
		}
		
		if (docState.getNowUser() == null || docStateBean.isSuper() == true) {
			UserProf user = orgService.fetchUserByUserId(docStateBean.getUserId());
			docState.setNowUser(user);
			docState.setUpdateTime(new Date());
			
			docState.setLinkName("Claim");
			DocStateLog docStateLog = new DocStateLog(docState, FlowTask.class.getSimpleName(), docState.getNowTaskId(), docState.getNowTaskId(), 
					refUser(docStateBean.getBeRepresentedId()), refUser(docStateBean.getAgentId()), docStateBean.getMemo());
			docState.setLinkName(null);
			
			docStateLog = docStateLogRepository.save(docStateLog);
			
			return new DocStateBean(docState, docStateBean.getUserId(), docStateBean.getBeRepresentedId(), docStateBean.getAgentId(), docStateBean.isSuper(), docStateLog.getId());
		}
		
		return docStateBean;
	}
	
	private void checkUserId(String userId) {
		if (userId == null) {
			throw new IllegalArgumentException("Parameter userId can not be null");
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public DocStateBean process(DocStateBean docStateBean, LinkAction action) {
		
		checkDocStateBean(docStateBean);
		checkUserId(docStateBean.getUserId());
		
		DocState docState = docStateRepository.getEntityById(docStateBean.getId());
		checkDocState(docStateBean, docState, true);
		
		if (docState.getNowUser() == null) {
			if (docStateBean.isSuper() == false) {
				throw new IllegalStateException(
						"The formId:" + docStateBean.getFormId() + " applyNo:" + docStateBean.getApplyNo() + " must be claimed first.");
			}
			else {
				docState.setNowUser(refUser(docStateBean.getUserId()));
			}
		}
		else if (!docState.getNowUser().getUserId().equals(docStateBean.getUserId()) || docStateBean.isSuper() == true) {
			if (docStateBean.getAgentId() == null && docStateBean.isSuper() == false) {
				throw new IllegalStateException(
						"No-auth!! The formId:" + docStateBean.getFormId() + " applyNo:" + docStateBean.getApplyNo() + " now user is " + docState.getNowUser().getUserId());
			}
			else {
				docState.setNowUser(refUser(docStateBean.getUserId()));
			}
		}
		
		if (docStateBean.getReviewDeptId() != null) {
			docState.setLastReviewDept(orgService.fetchDeptByDeptId(docStateBean.getReviewDeptId()));
		}
		
		renewSubjectOrFormId(docStateBean, docState);
		
		docState.setRtnToOrig(docStateBean.isRtnToOrig());
		docState.setNoParallelSubFlows(docStateBean.getNoParallelSubFlows());
		docState.setAssignParallelPassCount(docStateBean.getAssignParallelPassCount());
		
		return processInternal(docState, action, null, null, docStateBean.getBeRepresentedId(), docStateBean.getAgentId(), docStateBean.getMemo());
	}
	
	@Override
	public Map<String, Boolean> fetchFlowActionAuth(DocStateBean docState, String userId) {
		Map<String, Boolean> rtnMap = new HashMap<String, Boolean>();
		try {
			if (docState != null) {
				HandyFlow flow = getFlowParser().parse(docState.getFlowId(), docState.getFlowVersion(), false);
				rtnMap.put(IS_FLOW_AUTH, isUserFlowActionAuth(docState, userId));
				for (DefaultFlowAction action : DefaultFlowAction.values()) {
					if (action == DefaultFlowAction.APPLY) {
						rtnMap.put("can" + action.getAction(), canApply(flow, docState));
					}
					else if (action == DefaultFlowAction.CANCEL) {
						rtnMap.put("can" + action.getAction(), canCancel(flow, docState));
					}
					else {
						rtnMap.put("can" + action.getAction(), hasLinkAction(flow, docState, action, false));
					}
				}
			}
			else { // New and unsaved application form
				rtnMap.put(IS_FLOW_AUTH, true);
				for (DefaultFlowAction action : DefaultFlowAction.values()) {
					if (action == DefaultFlowAction.APPLY) {
						rtnMap.put("can" + action.getAction(), true);
					}
					else {
						rtnMap.put("can" + action.getAction(), false);
					}
				}
			}
		}
		catch (DocumentException e) {
			throw new FlowParseException("Flow id:" +  docState.getFlowId() + " version:" + docState.getFlowVersion() + " parse failed!!", e);
		}
		return rtnMap;
	}
	
	private boolean isUserFlowActionAuth(DocStateBean docStateBean, String userId) {
		List<UserDto> list = fetchNowCandidates(docStateBean);
		for (UserDto user : list) {
			if (user.getUserId().equals(userId) || docStateBean.isSuper()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean canApply(HandyFlow flow, DocStateBean docState) throws DocumentException {
		return (isNew(docState) || hasLinkAction(flow, docState, DefaultFlowAction.APPLY, true)) ? true : false;
	}
	
	private boolean isNew(DocStateBean docState) {
		return StringUtil.isBlank(docState.getFlowStatus()) ? true : false;
	}
	
	private boolean hasLinkAction(HandyFlow flow, DocStateBean docState, LinkAction action, boolean incDraft) throws DocumentException {
		if (isNew(docState)) {
			return false;
		}
		FlowTask task = flow.getFlowTaskOrNull(docState.getNowTaskId());
		if (incDraft) {
			if (DocState.FLOW_STATUS_DRAFT.equals(docState.getFlowStatus()) || DocState.FLOW_STATUS_PROCESS.equals(docState.getFlowStatus())) {
				if (task != null && task.checkFlowLink(action)) {
					return true;
				}
			}
		}
		else {
			if (DocState.FLOW_STATUS_PROCESS.equals(docState.getFlowStatus())) {
				if (task != null && task.checkFlowLink(action)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean canCancel(HandyFlow flow, DocStateBean docState) throws DocumentException {
		return (isNotNew(docState) && hasLinkAction(flow, docState, DefaultFlowAction.CANCEL, true)) ? true : false;
	}
	
	private boolean isNotNew(DocStateBean docState) {
		return !isNew(docState);
	}
	
	@Override
	public List<UserDto> fetchNowCandidates(DocStateBean docStateBean) {
		
		checkDocStateBean(docStateBean);
		
		DocState docState = docStateRepository.getEntityById(docStateBean.getId());
		
		checkDocState(docStateBean, docState, false);
		
		List<UserDto> rtnList = new ArrayList<UserDto>();
		if (docState.getNowUser() != null) {
			rtnList.add(new UserDto(docState.getNowUser()));
		}
		else {
			try {
				HandyFlow flow = getFlowParser().parse(docState.getFlowId(), docState.getFlowVersion(), false);
				FlowTask nowTask = flow.getFlowTaskOrNull(docState.getNowTaskId());
				if (nowTask != null) {
					for (UserProf user : nowTask.fetchCandidates(docState, orgService)) {
						rtnList.add(new UserDto(user));
					}
				}
			}
			catch (DocumentException e) {
				throw new FlowParseException("Flow id:" +  docState.getFlowId() + " version:" + docState.getFlowVersion() + " parse failed!!", e);
			}
		}
		return rtnList;
	}

	@Override
	public List<UserDto> fetchNextCandidates(DocStateBean docStateBean, LinkAction action) {
		
		checkDocStateBean(docStateBean);
		
		DocState docState = docStateRepository.getEntityById(docStateBean.getId());
		
		checkDocState(docStateBean, docState, false);
		try {
			List<UserDto> rtnList = new ArrayList<UserDto>();
			
			if (action != DefaultFlowAction.CANCEL && action != DefaultFlowAction.FINAL_APPROVE && action != DefaultFlowAction.RETURN && action != DefaultFlowAction.PARALLEL_COUNTERSIGN) {
				HandyFlow flow = getFlowParser().parse(docState.getFlowId(), docState.getFlowVersion(), false);
				FlowTask nowTask = flow.getFlowTaskOrNull(docState.getNowTaskId());
				if (nowTask != null) {
					FlowLink nextLink = nowTask.getFlowLink(action);
					if (! DefaultFlowEvent.END.getEvent().equalsIgnoreCase(nextLink.getTo()) &&
							! DefaultFlowEvent.CANCEL.getEvent().equalsIgnoreCase(nextLink.getTo())) {
						FlowComponent nextComp = flow.getFlowCandidateComponent(nextLink.getTo());
						
						FlowComponent lastNextComp = null;
						do {
							lastNextComp = nextComp;
							nextComp = nextComp.processInternalBefore(docState, orgService);
						} while (lastNextComp != nextComp);
						
						for (UserProf user : nextComp.fetchCandidates(docState, orgService)) {
							rtnList.add(new UserDto(user));
						}
					}
				}
			}
			return rtnList;
		}
		catch (DocumentException e) {
			throw new FlowParseException("Flow id:" +  docState.getFlowId() + " version:" + docState.getFlowVersion() + " parse failed!!", e);
		}
	}

	@Override
	public List<NameValuePair> fetchNextCandidatesNameValuePair(DocStateBean docStateBean, LinkAction action) {
		
		checkDocStateBean(docStateBean);
		
		DocState docState = docStateRepository.getEntityById(docStateBean.getId());
		
		checkDocState(docStateBean, docState, false);
		try {
			List<NameValuePair> rtnList = new ArrayList<NameValuePair>();
			
			if (action != DefaultFlowAction.CANCEL && action != DefaultFlowAction.FINAL_APPROVE && action != DefaultFlowAction.RETURN && action != DefaultFlowAction.PARALLEL_COUNTERSIGN) {
				HandyFlow flow = getFlowParser().parse(docState.getFlowId(), docState.getFlowVersion(), false);
				FlowTask nowTask = flow.getFlowTaskOrNull(docState.getNowTaskId());
				if (nowTask != null) {
					FlowLink nextLink = nowTask.getFlowLink(action);
					if (! DefaultFlowEvent.END.getEvent().equalsIgnoreCase(nextLink.getTo()) &&
							! DefaultFlowEvent.CANCEL.getEvent().equalsIgnoreCase(nextLink.getTo())) {
						FlowComponent nextComp = flow.getFlowCandidateComponent(nextLink.getTo());
						
						FlowComponent lastNextComp = null;
						do {
							lastNextComp = nextComp;
							nextComp = nextComp.processInternalBefore(docState, orgService);
						} while (lastNextComp != nextComp);
						
						for (UserProf user : nextComp.fetchCandidates(docState, orgService)) {
							rtnList.add(new NameValuePair(user.getName(), user.getUserId()));
						}
					}
				}
			}
			return rtnList;
		}
		catch (DocumentException e) {
			throw new FlowParseException("Flow id:" +  docState.getFlowId() + " version:" + docState.getFlowVersion() + " parse failed!!", e);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public DocStateBean applyTo(String nextUserId, DocStateBean docStateBean, LinkAction action) {
		
		DocState docState = doBeforeApplyTo(docStateBean);
		
		docState.setNoParallelSubFlows(docStateBean.getNoParallelSubFlows());
		docState.setAssignParallelPassCount(docStateBean.getAssignParallelPassCount());
		
		return processInternal(docState, action, refNextUser(nextUserId), null, docStateBean.getBeRepresentedId(), docStateBean.getAgentId(), docStateBean.getMemo());
	}
	
	private DocState doBeforeApplyTo(DocStateBean docStateBean) {
		checkDocStateBean(docStateBean);
		
		docStateBean = claim(docStateBean);
		
		DocState docState = docStateRepository.getEntityById(docStateBean.getId());
		
		checkDocState(docStateBean, docState, true);
		
		/* Remark because allowing return to non-original applicant
		if (!docState.getApplicant().getUserId().equals(docState.getNowUser().getUserId())) {
			throw new IllegalStateException(
					"When apply the formId:" + docStateBean.getFormId() + " applyNo:" + docStateBean.getApplyNo() + 
					" applicant:" + docState.getApplicant().getUserId() + " should same as now user:" + docState.getNowUser().getUserId());
		}*/
		
		if (docStateBean.getReviewDeptId() != null) {
			docState.setLastReviewDept(orgService.fetchDeptByDeptId(docStateBean.getReviewDeptId()));
		}
		
		renewSubjectOrFormId(docStateBean, docState);
		
		return docState;
	}
	
	private UserProf refNextUser(String nextUserId) {
		if (nextUserId == null) {
			throw new IllegalArgumentException("Parameter nextUserId can not be null");
		}
		return orgService.fetchUserByUserId(nextUserId);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public DocStateBean processTo(String nextUserId, DocStateBean docStateBean, LinkAction action) {
		
		DocState docState = doBeforeProcessTo(docStateBean, docStateBean.getUserId(), docStateBean.getAgentId(), docStateBean.isSuper());
		
		docState.setNoParallelSubFlows(docStateBean.getNoParallelSubFlows());
		docState.setAssignParallelPassCount(docStateBean.getAssignParallelPassCount());
		
		return processInternal(docState, action, refNextUser(nextUserId), null, docStateBean.getBeRepresentedId(), docStateBean.getAgentId(), docStateBean.getMemo());
	}
	
	private DocState doBeforeProcessTo(DocStateBean docStateBean, String userId, String agentId, boolean isSuper) {
		checkDocStateBean(docStateBean);
		checkUserId(userId);
		
		DocState docState = docStateRepository.getEntityById(docStateBean.getId());
		checkDocState(docStateBean, docState, true);
		
		if (docState.getNowUser() == null) {
			if (isSuper == false) {
				throw new IllegalStateException(
						"The formId:" + docStateBean.getFormId() + " applyNo:" + docStateBean.getApplyNo() + " must be claimed first.");
			}
			else {
				docState.setNowUser(refUser(userId));
			}
		}
		else if (!docState.getNowUser().getUserId().equals(userId) || isSuper == true) {
			if (docStateBean.getAgentId() == null  && isSuper == false) {
				throw new IllegalStateException(
						"No-auth!! The formId:" + docStateBean.getFormId() + " applyNo:" + docStateBean.getApplyNo() + " now user is " + docState.getNowUser().getUserId());
			}
			else {
				docState.setNowUser(refUser(userId));
			}
		}
		
		if (docStateBean.getReviewDeptId() != null) {
			docState.setLastReviewDept(orgService.fetchDeptByDeptId(docStateBean.getReviewDeptId()));
		}
		
		renewSubjectOrFormId(docStateBean, docState);
		
		return docState;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public DocStateBean applyConcurrentTo(String[] nextUserIds, DocStateBean docStateBean, LinkAction action) {
		
		DocState docState = doBeforeApplyTo(docStateBean);
		
		docState.setNoParallelSubFlows(docStateBean.getNoParallelSubFlows());
		docState.setAssignParallelPassCount(docStateBean.getAssignParallelPassCount());
		
		return processInternal(docState, action, null, nextUserIds, docStateBean.getBeRepresentedId(), docStateBean.getAgentId(), docStateBean.getMemo());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public DocStateBean processConcurrentTo(String[] nextUserIds, DocStateBean docStateBean, LinkAction action) {
		
		DocState docState = doBeforeProcessTo(docStateBean, docStateBean.getUserId(), docStateBean.getAgentId(), docStateBean.isSuper());
		
		docState.setNoParallelSubFlows(docStateBean.getNoParallelSubFlows());
		docState.setAssignParallelPassCount(docStateBean.getAssignParallelPassCount());
		
		return processInternal(docState, action, null, nextUserIds, docStateBean.getBeRepresentedId(), docStateBean.getAgentId(), docStateBean.getMemo());
	}
	
	@Override
	public boolean updateMainFormId(Long id,Long mainFormId) {
		DocState docState = docStateRepository.getEntityById(id);
		if (docState != null) {
			docState.setMainFormId(mainFormId);
			return true;
		}
		return false;
	}

	@Override
	public boolean isFinalFlowTask(DocStateBean docState, LinkAction action) {
		try {
			HandyFlow flow = getFlowParser().parse(docState.getFlowId(), docState.getFlowVersion(), false);
			FlowTask nowTask = flow.getFlowTask(docState.getNowTaskId());
			FlowLink nextLink = nowTask.getFlowLink(action);
			FlowComponent nextComp = flow.getFlowComponent(nextLink.getTo());
			if (nextComp instanceof FlowEvent) {
				FlowEvent flowEvent = (FlowEvent) nextComp;
				if (flowEvent.getType() == FlowEvent.Type.END) {
					return true;
				}
			}
		}
		catch (DocumentException e) {
			throw new FlowParseException("Flow id:" +  docState.getFlowId() + " version:" + docState.getFlowVersion() + " parse failed!!", e);
		}
		return false;
	}

	@Override
	public String getNextFlowTaskId(String flowId, LinkAction action) {
		
		return getNextFlowTaskIdInternal(flowId, null, null, action);
	}
	
	@Override
	public String getNextFlowTaskId(DocStateBean docState, LinkAction action) {
		
		return getNextFlowTaskIdInternal(docState.getFlowId(), docState.getFlowVersion(), docState.getNowTaskId(), action);
	}
	
	private String getNextFlowTaskIdInternal(String flowId, String flowVersion, String nowTaskId, LinkAction action) {
		if (StringUtil.isNotBlank(flowId) && !"End".equalsIgnoreCase(nowTaskId) && !"Cancel".equalsIgnoreCase(nowTaskId)) {
			try {
				HandyFlow flow = getFlowParser().parse(flowId, flowVersion != null ? flowVersion : getNowFlowVersion(flowId), false);
				FlowTask nowTask = flow.getFlowTask(nowTaskId != null ? nowTaskId : "Task1");
				FlowLink nextLink = nowTask.getFlowLink(action);
				FlowComponent nextComp = flow.getFlowComponent(nextLink.getTo());
				if (nextComp instanceof FlowTask) {
					FlowTask flowTask = (FlowTask) nextComp;
					return flowTask.getId();
				}
			}
			catch (DocumentException e) {
				throw new FlowParseException("Flow id:" +  flowId + " version:" + flowVersion + " parse failed!!", e);
			}
		}
		return "";
	}
	
	@Override
	public String getNextFlowTaskExt(String flowId, LinkAction action) {
		
		return getNextFlowTaskExtInternal(flowId, null, null, action);
	}
	
	@Override
	public String getNextFlowTaskExt(DocStateBean docState, LinkAction action) {
		
		return getNextFlowTaskExtInternal(docState.getFlowId(), docState.getFlowVersion(), docState.getNowTaskId(), action);
	}
	
	private String getNextFlowTaskExtInternal(String flowId, String flowVersion, String nowTaskId, LinkAction action) {
		if (StringUtil.isNotBlank(flowId) && !"End".equalsIgnoreCase(nowTaskId) && !"Cancel".equalsIgnoreCase(nowTaskId)) {
			try {
				HandyFlow flow = getFlowParser().parse(flowId, flowVersion != null ? flowVersion : getNowFlowVersion(flowId), false);
				FlowTask nowTask = flow.getFlowTask(nowTaskId != null ? nowTaskId : "Task1");
				FlowLink nextLink = nowTask.getFlowLink(action);
				FlowComponent nextComp = flow.getFlowComponent(nextLink.getTo());
				if (nextComp instanceof FlowTask) {
					FlowTask flowTask = (FlowTask) nextComp;
					return flowTask.getTaskExt();
				}
			}
			catch (DocumentException e) {
				throw new FlowParseException("Flow id:" +  flowId + " version:" + flowVersion + " parse failed!!", e);
			}
		}
		return "";
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public DocStateBean forceCancel(DocStateBean docStateBean) {
		if (docStateBean.getUpDocStateId() != 0) {
			throw new IllegalArgumentException(
					"Can't forceCancel by sub DocState id:" + docStateBean.getId() + " (upDocStateId:" + docStateBean.getUpDocStateId() + ")");
		}
		
		claim(docStateBean.superUser());
		DocState docState = docStateRepository.getEntityById(docStateBean.getId());
		try {
			HandyFlow flow = getFlowParser().parse(docState.getFlowId(), docState.getFlowVersion(), false);
			FlowComponent nowComp = flow.getFlowTaskOrNull(docState.getNowTaskId());
			if (nowComp == null) {
				nowComp = flow.getFlowParallel(docState.getNowTaskId());
			}
			FlowEvent cancelEvent = flow.getFlowEventOrNull(FlowEvent.Type.CANCEL);
			if (cancelEvent == null) {
				cancelEvent = new FlowEvent(flow, "Cancel", null, "申請單作廢", FlowEvent.Type.CANCEL);
			}
			
			docState.setLinkName("強制作廢");
			docState.setLinkDesc("強制作廢申請單");
			docState.setUpdateTime(new Date());
			
			UserProf beRepresented = refUser(docStateBean.getBeRepresentedId());
			UserProf agent = refUser(docStateBean.getAgentId());
			
			DocStateLog docStateLog = new DocStateLog(docState, nowComp.getClass().getSimpleName(), nowComp.getId(), cancelEvent.getId(), beRepresented, agent, docStateBean.getMemo());
			
			docStateLog = docStateLogRepository.save(docStateLog);
			
			docState.setNowTaskId(cancelEvent.getId());
			docState.setFlowStatus(DocState.FLOW_STATUS_PROCESS);
			docState.setTaskName(cancelEvent.getName());
			docState.setTaskDesc(cancelEvent.getDesc());
			docState.setNowUser(null);
			docState.setLinkName(null);
			docState.setLinkDesc(null);
			
			for (DocState subDocState : docState.getSubDocStates()) {
				if (DocState.FLOW_STATUS_PROCESS.equalsIgnoreCase(subDocState.getFlowStatus())) {
					subDocState.setFlowStatus(DocState.FLOW_STATUS_SUB_SUSPEND);
					
					subDocState.setTaskRoleIds(null);
					subDocState.setTaskDeptId(null);
					subDocState.setTaskUserIds(null);
					subDocState.setTaskExt(null);
					subDocState.setNowUser(null);
					
					docStateLog = new DocStateLog(subDocState, getClass().getSimpleName(), subDocState.getNowTaskId(), subDocState.getNowTaskId(), beRepresented, agent, null);
					
					docStateLogRepository.save(docStateLog);
				}
			}
			
			cancelEvent.processInternalAfter(docState, docStateRepository, docStateLogRepository, orgService, beRepresented, agent, null);
			
			notifyService.processNotificationsAndReplaceDisabledUser(docState, null);
			
			return new DocStateBean(docState, null, docStateBean.getBeRepresentedId(), docStateBean.getAgentId(), false, docStateLog.getId());
		}
		catch (DocumentException e) {
			throw new FlowParseException("Flow id:" +  docState.getFlowId() + " version:" + docState.getFlowVersion() + " parse failed!!", e);
		}
	}
	

	@Override 
	public Short getFlowTaskNum(String flowId) {
		if (StringUtils.isBlank(flowId)) {
			return 0;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("flowId", flowId);
		List<Map<String, Object>> flowStepList = flowStepRepository.findFlowSteps(params);
		
		if (CollectionUtils.isEmpty(flowStepList)) {
			return 0;
		}else {
			return new Short((short)(flowStepList.size()-1));
		}
	}
}
