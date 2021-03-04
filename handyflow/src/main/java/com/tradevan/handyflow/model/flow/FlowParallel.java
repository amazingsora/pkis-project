package com.tradevan.handyflow.model.flow;

import java.util.ArrayList;
import java.util.List;

import com.tradevan.apcommon.util.StringUtil;
import com.tradevan.aporg.model.DeptProf;
import com.tradevan.aporg.model.UserProf;
import com.tradevan.aporg.service.OrgService;
import com.tradevan.handyflow.exception.FlowSettingException;
import com.tradevan.handyflow.model.form.DocState;
import com.tradevan.handyflow.model.form.DocStateLog;
import com.tradevan.handyflow.repository.DocStateRepository;
import com.tradevan.handyflow.repository.DocStateLogRepository;

/**
 * Title: FlowParallel<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1.2
 */
public class FlowParallel extends FlowComposite {
	
	private Integer parallelPassCount;
	
	public FlowParallel(FlowComponent parent, String id, String name, String desc, String parallelPassCount) {
		super(parent, id, name, desc);
		if (parallelPassCount != null) {
			this.parallelPassCount = Integer.valueOf(parallelPassCount);
		}
	}
	
	public List<FlowLink> getParallelLinks() {
		List<FlowLink> flowLinks = new ArrayList<FlowLink>();
		for (FlowComponent child : getChildren()) {
			if (child instanceof FlowLink) {
				flowLinks.add((FlowLink) child);
			}
			
		}
		return flowLinks;
	}

	public Integer getParallelPassCount() {
		return parallelPassCount;
	}

	public void setParallelPassCount(Integer parallelPassCount) {
		this.parallelPassCount = parallelPassCount;
	}
	
	@Override
	public List<DocState> processInternalAfter(DocState docState, DocStateRepository docStateRepository, DocStateLogRepository docStateLogRepository, OrgService orgService, 
			UserProf beRepresented, UserProf agent, List<UserProf> nextConcurrentUsers) {
		
		HandyFlow flow = getRoot();
		List<FlowLink> parallelLinks = getParallelLinks();
		
		if (parallelLinks.size() == 0) {
			throw new FlowSettingException("Flow id:" + flow.getId() + " version:" + flow.getVersion() + " FlowParallel has no parallelLinks");
		}
		docState.setFlowStatus(DocState.FLOW_STATUS_PARALLEL);
		docState.setParallelPassCount(parallelPassCount);
		
		int serial = docStateRepository.getMaxSerialNo(docState);
		
		List<DocState> subDocStates = new ArrayList<DocState>();
		for (FlowLink flowLink : parallelLinks) {
			SubFlow subFlow = flow.getSubFlow(flowLink.getTo());
			FlowEvent subBegEvent = subFlow.getFlowEvent(FlowEvent.Type.SUB_BEGIN);
			FlowLink subNextLink = subBegEvent.getFlowLink(null);
			FlowTask subNextTask = subFlow.getFlowTask(subNextLink.getTo());
			
			if (StringUtil.isNotBlank(docState.getNoParallelSubFlows())) {
				if (docState.getNoParallelSubFlows().contains(subFlow.getId())) {
					continue;
				}
			}
			
			if (flowLink.isConcurrent() != null && flowLink.isConcurrent()) {
				List<UserProf> users = nextConcurrentUsers;
				if (nextConcurrentUsers == null || nextConcurrentUsers.size() == 0) {
					users = subFlow.fetchCandidates(docState, orgService);
				}
				if (parallelLinks.size() == 1 && users.size() == 0) {
					throw new IllegalStateException("Flow id:" + flow.getId() + " version:" + flow.getVersion() + 
							" FlowParallel's only one FlowLink(isConcurrent=true) but next users size is 0");
				}
				for (UserProf user : users) {
					serial = addSubDocState(subDocStates, subFlow, subBegEvent, subNextLink, subNextTask, docState, docStateRepository, docStateLogRepository, orgService, serial, user);
				}
			}
			else {
				serial = addSubDocState(subDocStates, subFlow, subBegEvent, subNextLink, subNextTask, docState, docStateRepository, docStateLogRepository, orgService, serial, null);
			}
		}
		return subDocStates;
	}
	
	private int addSubDocState(List<DocState> subDocStates, SubFlow subFlow, FlowEvent subBegEvent, FlowLink subNextLink, FlowTask subNextTask, DocState docState, DocStateRepository docStateRepository, 
			DocStateLogRepository docStateLogRepository, OrgService orgService, int serial, UserProf user) {
		DocState subDocState = new DocState(docState, ++serial);
		
		subDocState.setNowTaskId(subNextLink.getTo());
		subDocState.setFlowStatus(subBegEvent.getType().getValue());
		subDocState.setTaskName(subFlow.getName());
		subDocState.setTaskDesc(subBegEvent.getDesc());
		subDocState.setLinkName(subNextLink.getName());
		subDocState.setLinkDesc(subNextLink.getDesc());
		
		subDocState = docStateRepository.save(subDocState);
		
		DocStateLog docStateLog = new DocStateLog(subDocState, subBegEvent.getClass().getSimpleName(), subBegEvent.getId(), subNextTask.getId(), null, null, null);
		
		docStateLogRepository.save(docStateLog);
		
		subDocState.setFlowStatus(DocState.FLOW_STATUS_PROCESS);
		subDocState.setTaskName(subNextTask.getName());
		subDocState.setTaskDesc(subNextTask.getDesc());
		
		subDocState.setTaskRoleIds(subNextTask.getRoles());
		subDocState.setTaskDeptId(subNextTask.getDept());
		subDocState.setTaskUserIds(subNextTask.getUsers());
		subDocState.setTaskExt(subNextTask.getTaskExt());
		
		subDocState.setNowUser(user);
		
		if (subNextTask.getDept() != null) {
			DeptProf subFlowDept = orgService.fetchDeptByDeptId(subNextTask.getDept());
			subDocState.setSubFlowDept(subFlowDept);
		}
		
		subDocStates.add(subDocState);
		
		return serial;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("flowParallel:");
		if (getId() != null) {
			sb.append(" id=\"").append(getId()).append("\"");
		}
		if (getName() != null) {
			sb.append(" name=\"").append(getName()).append("\"");
		}
		if (getParallelPassCount() != null) {
			sb.append(" parallelPassCount=\"").append(getParallelPassCount()).append("\"");
		}
		if (getDesc() != null) {
			sb.append(" desc=\"").append(getDesc()).append("\"");
		}
		
		for (FlowComponent component : getChildren()) {
			sb.append("\n");
			sb.append(component.toString());
		}
		return sb.toString();
	}
}
