package com.tradevan.handyflow.model.flow;

import java.util.List;

import com.tradevan.aporg.model.DeptProf;
import com.tradevan.aporg.model.UserProf;
import com.tradevan.aporg.service.OrgService;
import com.tradevan.handyflow.model.form.DocState;
import com.tradevan.handyflow.model.form.DocStateLog;
import com.tradevan.handyflow.repository.DocStateRepository;
import com.tradevan.handyflow.repository.DocStateLogRepository;

/**
 * Title: SubFlow<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1.1
 */
public class SubFlow extends HandyFlow {
	
	public SubFlow(FlowComponent parent, String id, String name, String desc) {
		super(parent, id, name, desc, null);
	}
	
	public String getVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<DocState> processInternalAfter(DocState docState, DocStateRepository docStateRepository, DocStateLogRepository docStateLogRepository, OrgService orgService, 
			UserProf beRepresented, UserProf agent, List<UserProf> nextConcurrentUsers) {
		FlowEvent subBegEvent = getFlowEvent(FlowEvent.Type.SUB_BEGIN);
		FlowLink subNextLink = subBegEvent.getFlowLink(null);
		FlowTask subNextTask = getFlowTask(subNextLink.getTo());
		
		docState.setNowTaskId(subNextLink.getTo());
		docState.setFlowStatus(subBegEvent.getType().getValue());
		docState.setTaskName(getName());
		docState.setTaskDesc(subBegEvent.getDesc());
		docState.setLinkName(subNextLink.getName());
		docState.setLinkDesc(subNextLink.getDesc());
		
		DocStateLog docStateLog = new DocStateLog(docState, subBegEvent.getClass().getSimpleName(), subBegEvent.getId(), subNextTask.getId(), beRepresented, agent, null);
		
		docStateLogRepository.save(docStateLog);
		
		docState.setFlowStatus(DocState.FLOW_STATUS_PROCESS);
		docState.setTaskName(subNextTask.getName());
		docState.setTaskDesc(subNextTask.getDesc());
		
		docState.setTaskRoleIds(subNextTask.getRoles());
		docState.setTaskDeptId(subNextTask.getDept());
		docState.setTaskUserIds(subNextTask.getUsers());
		docState.setTaskExt(subNextTask.getTaskExt());
		
		if (subNextTask.getDept() != null) {
			DeptProf subFlowDept = orgService.fetchDeptByDeptId(subNextTask.getDept());
			docState.setSubFlowDept(subFlowDept);
		}
		return null;
	}
	
	@Override
	public List<UserProf> fetchCandidates(DocState docState, OrgService orgService) {
		FlowEvent subBegEvent = getFlowEvent(FlowEvent.Type.SUB_BEGIN);
		FlowLink subNextLink = subBegEvent.getFlowLink(null);
		FlowTask subNextTask = getFlowTask(subNextLink.getTo());
		
		return subNextTask.fetchCandidates(docState, orgService);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (getId() != null) {
			sb.append("subFlow: id=\"").append(getId()).append("\"");
		}
		if (getName() != null) {
			sb.append(" name=\"").append(getName()).append("\"");
		}
		if (getDesc() != null) {
			sb.append(" desc=\"").append(getDesc()).append("\"");
		}
		sb.append("\n");
		for (FlowComponent component : getChildren()) {
			sb.append(component.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
}
