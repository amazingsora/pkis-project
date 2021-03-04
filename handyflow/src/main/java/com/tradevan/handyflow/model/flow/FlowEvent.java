package com.tradevan.handyflow.model.flow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tradevan.aporg.model.UserProf;
import com.tradevan.aporg.service.OrgService;
import com.tradevan.handyflow.exception.FlowSettingException;
import com.tradevan.handyflow.model.form.DocState;
import com.tradevan.handyflow.model.form.DocStateLog;
import com.tradevan.handyflow.repository.DocStateRepository;
import com.tradevan.handyflow.repository.DocStateLogRepository;

/**
 * Title: FlowEvent<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1.3
 */
public class FlowEvent extends FlowComposite {
	
	public enum Type {
		BEGIN("BEGIN", "~~B~~"),
		END("END", "~~E~~"),
		CANCEL("CANCEL", "~~C~~"),
		SUB_BEGIN("SUB_BEGIN", "~S.B~"),
		SUB_FINISH("SUB_FINISH", "~S.F~"),
		SUB_RETURN("SUB_RETURN", "~S.R~"),
		SUB_SUSPEND("SUB_SUSPEND", "~S.S~");
		
		String value;
		String symbol;

		private Type(String value, String symbol) {
			this.value = value;
			this.symbol = symbol;
		}
		
		public String getValue() {
			return value;
		}

		public String getSymbol() {
			return symbol;
		}
	}

	private Type type;
	
	public FlowEvent(FlowComponent parent, String id, String name, String desc, Type type) {
		super(parent, id, name, desc);
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public FlowLink getFlowLink(LinkAction action) {
		for (FlowComponent child : getChildren()) {
			if (child instanceof FlowConditions) {
				FlowConditions conditions = (FlowConditions) child;
				return conditions.getFlowLink(action);
			}
			else if (child instanceof FlowLink) {
				return (FlowLink) child;
			}
		}
		throw new FlowSettingException("FlowEvent id:" + getId() + " has no FlowLink match action:" + action);
	}
	
	@Override
	public List<DocState> processInternalAfter(DocState docState, DocStateRepository docStateRepository, DocStateLogRepository docStateLogRepository, OrgService orgService, 
			UserProf beRepresented, UserProf agent, List<UserProf> nextConcurrentUsers) {
		if (getType() == FlowEvent.Type.END || getType() == FlowEvent.Type.CANCEL) {
			
			processEndOrCancel(docState, docStateLogRepository);
		}
		else if (getType() == FlowEvent.Type.SUB_FINISH || getType() == FlowEvent.Type.SUB_RETURN) {
			DocState upDocState = docState.getUpDocState();
			if (upDocState == null) { 
				processSubFinishOrReturn(docState, docStateLogRepository, beRepresented, agent);
			}
			else { // FlowParallel
				processParallelSubFinishOrReturn(docState, upDocState, docStateRepository, docStateLogRepository, orgService, beRepresented, agent);
			}
		}
		else {
			throw new FlowSettingException(
					"Flow id:" + docState.getFlowId() + " version:" + docState.getFlowVersion() + " has no FlowEvent type END/CANCEL/SUB_FINISH/SUB_RETURN");
		}
		return null;
	}
	
	private void processEndOrCancel(DocState docState, DocStateLogRepository docStateLogRepository) {
		docState.setFlowStatus(getType().getValue());
		
		if (docState.getTaskName() == null) {
			docState.setTaskName(getRoot().getName());
		}
		
		DocStateLog docStateLog = new DocStateLog(docState, getClass().getSimpleName(), getId(), 
				(getType() == FlowEvent.Type.END) ? FlowEvent.Type.END.getSymbol() : FlowEvent.Type.CANCEL.getSymbol(), null, null, null);
		
		docStateLogRepository.save(docStateLog);
		
		docState.setTaskRoleIds(null);
		docState.setTaskDeptId(null);
		docState.setTaskUserIds(null);
		docState.setTaskExt(null);
		docState.setNowUser(null);
	}
	
	private void processSubFinishOrReturn(DocState docState, DocStateLogRepository docStateLogRepository, UserProf beRepresented, UserProf agent) {
		FlowLink nextLink2 = getFlowLink(null);
		FlowTask nextTask = getRoot().getFlowTask(nextLink2.getTo());
		
		docState.setFlowStatus(getType().getValue());
		
		if (docState.getTaskName() == null) {
			docState.setTaskName(getParent().getName());
		}
		
		docState.setLinkName(nextLink2.getName());
		
		DocStateLog docStateLog = new DocStateLog(docState, getClass().getSimpleName(), getId(), nextLink2.getTo(), beRepresented, agent, null);
		
		docStateLogRepository.save(docStateLog);
		
		String origTaskId = docState.getNowTaskId();
		docState.setNowTaskId(nextTask.getId());
		docState.setFlowStatus(DocState.FLOW_STATUS_PROCESS);
		docState.setTaskName(nextTask.getName());
		docState.setTaskDesc(nextTask.getDesc());
		docState.setTaskRoleIds(nextTask.getRoles());
		docState.setTaskDeptId(nextTask.getDept());
		docState.setTaskUserIds(nextTask.getUsers());
		docState.setTaskExt(nextTask.getTaskExt());
		
		if (!docState.isAssigned() && !origTaskId.equals(docState.getNowTaskId())) {
			docState.setNowUser(null);
		}
	}
	
	private void processParallelSubFinishOrReturn(DocState docState, DocState upDocState, DocStateRepository docStateRepository, DocStateLogRepository docStateLogRepository, 
			OrgService orgService, UserProf beRepresented, UserProf agent) {
		docState.setFlowStatus(getType().getValue());
		
		if (docState.getTaskName() == null) {
			docState.setTaskName(getParent().getName());
		}
		
		DocStateLog docStateLog = new DocStateLog(docState, getClass().getSimpleName(), getId(), 
				(getType() == FlowEvent.Type.SUB_FINISH) ? FlowEvent.Type.SUB_FINISH.getSymbol() : FlowEvent.Type.SUB_RETURN.getSymbol(), null, null, null);
		
		docStateLogRepository.save(docStateLog);
		
		docState.setTaskRoleIds(null);
		docState.setTaskDeptId(null);
		docState.setTaskUserIds(null);
		docState.setTaskExt(null);
		docState.setNowUser(null);
		
		boolean done = checkParallelDone(docState, upDocState);
		
		if (done == true || getType() == FlowEvent.Type.SUB_RETURN) {
			for (DocState subDocState : upDocState.getSubDocStates()) {
				if (DocState.FLOW_STATUS_PROCESS.equalsIgnoreCase(subDocState.getFlowStatus())) {
					subDocState.setFlowStatus(DocState.FLOW_STATUS_SUB_SUSPEND);
					
					subDocState.setTaskRoleIds(null);
					subDocState.setTaskDeptId(null);
					subDocState.setTaskUserIds(null);
					subDocState.setNowUser(null);
					
					docStateLog = new DocStateLog(subDocState, getClass().getSimpleName(), subDocState.getNowTaskId(), subDocState.getNowTaskId(), beRepresented, agent, null);
					
					docStateLogRepository.save(docStateLog);
				}
			}
			
			HandyFlow flow = getRoot();
			FlowParallel flowParallel = flow.getFlowParallel(upDocState.getNowTaskId());
			FlowLink nextLink = getFlowLink(null);
			FlowComponent nextComp = flow.getFlowComponent(nextLink.getTo());
			
			upDocState.setFlowStatus(done == true ? DocState.FLOW_STATUS_PARALLEL_FINISH : DocState.FLOW_STATUS_PARALLEL_RETURN);
			upDocState.setUpdateTime(new Date());
			
			docStateLog = new DocStateLog(upDocState, flowParallel.getClass().getSimpleName(), flowParallel.getId(), nextComp.getId(), beRepresented, agent, null);
			
			docStateLogRepository.save(docStateLog);
			
			String origTaskId = upDocState.getNowTaskId();
			upDocState.setNowTaskId(nextLink.getTo());
			upDocState.setFlowStatus(DocState.FLOW_STATUS_PROCESS);
			
			upDocState.setTaskName(nextComp.getName());
			upDocState.setTaskDesc(nextComp.getDesc());
			upDocState.setLinkName(null);
			upDocState.setLinkDesc(null);
			
			if (!docState.isAssigned() && !origTaskId.equals(upDocState.getNowTaskId())) {
				upDocState.setNowUser(null);
			}
			
			nextComp.processInternalAfter(upDocState, docStateRepository, docStateLogRepository, orgService, beRepresented, agent, null);
			
			if (docState.isRtnToOrig() == true) {
				for (DocStateLog stateLog : upDocState.getDocStateLogs()) {
					if (upDocState.getNowTaskId().equalsIgnoreCase(stateLog.getFromTaskId())) {
						upDocState.setNowUser(stateLog.getUser());
					}
				}
			}
		}
	}
	
	private boolean checkParallelDone(DocState docState, DocState upDocState) {
		if (getType() == FlowEvent.Type.SUB_FINISH) {
			int totalCount = 0;
			int count = 0;
			List<DocState> subDocStates = upDocState.getSubDocStates();
			for (DocState subDocState : subDocStates) {
				if (!FlowEvent.Type.SUB_RETURN.getValue().equalsIgnoreCase(subDocState.getFlowStatus()) && !FlowEvent.Type.SUB_SUSPEND.getValue().equalsIgnoreCase(subDocState.getFlowStatus())) {
					totalCount++;
				}
				if (FlowEvent.Type.SUB_FINISH.getValue().equalsIgnoreCase(subDocState.getFlowStatus())) {
					count++;
					if ((upDocState.getParallelPassCount() != null && count >= upDocState.getParallelPassCount()) ||
							(docState.getAssignParallelPassCount() != null && count >= docState.getAssignParallelPassCount())) {
						return true;
					}
				}
			}
			
			if (count >= totalCount) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<UserProf> fetchCandidates(DocState docState, OrgService orgService) {
		if (getType() == FlowEvent.Type.SUB_FINISH) {
			DocState upDocState = docState.getUpDocState();
			if (upDocState == null) { 
				FlowLink nextLink = getFlowLink(null);
				FlowTask nextTask = getRoot().getFlowTask(nextLink.getTo());
				
				return nextTask.fetchCandidates(docState, orgService);
			}
			else if (checkParallelDone(docState, upDocState)) {
				HandyFlow flow = getRoot();
				FlowLink nextLink = getFlowLink(null);
				FlowComponent nextComp = flow.getFlowCandidateComponent(nextLink.getTo());
				
				return nextComp.fetchCandidates(upDocState, orgService);
			}
		}
		
		return new ArrayList<UserProf>();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("flowEvent: id=\"").append(getId()).append("\"");
		if (getName() != null) {
			sb.append(" name=\"").append(getName()).append("\"");
		}
		if (getDesc() != null) {
			sb.append(" desc=\"").append(getDesc()).append("\"");
		}
		sb.append(" type=\"").append(type).append("\"");
		sb.append("\n");
		for (FlowComponent component : getChildren()) {
			sb.append(component.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
}
