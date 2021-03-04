package com.tradevan.handyflow.model.flow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.tradevan.aporg.model.DeptProf;
import com.tradevan.aporg.model.RoleProf;
import com.tradevan.aporg.model.UserDeptRole;
import com.tradevan.aporg.model.UserProf;
import com.tradevan.aporg.service.OrgService;
import com.tradevan.handyflow.exception.FlowSettingException;
import com.tradevan.handyflow.model.form.DocState;
import com.tradevan.handyflow.model.form.DocStateLog;
import com.tradevan.handyflow.repository.DocStateRepository;
import com.tradevan.handyflow.repository.DocStateLogRepository;

/**
 * Title: FlowTask<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.3.3
 */
public class FlowTask extends FlowComposite {

	private String roles;
	private String dept;
	private String users;
	private String sameUserAs;
	private String taskExt;
	private Boolean isReviewDeptRole;
	private Boolean isProjRole;
	private Boolean isSameRolePass;
	private Boolean isHighLevelPass;
	private Boolean highDeptUserApplyOff;
	
	public FlowTask(FlowComponent parent, String id, String name, String desc, String roles, String dept, String users, String sameUserAs, String taskExt,
			String isReviewDeptRole, String isProjRole, String isSameRolePass, String isHighLevelPass, String highDeptUserApplyOff) {
		super(parent, id, name, desc);
		this.roles = roles;
		this.dept = dept;
		this.users = users;
		this.sameUserAs = sameUserAs;
		this.taskExt = taskExt;
		if (isReviewDeptRole != null) {
			this.isReviewDeptRole = Boolean.valueOf(isReviewDeptRole);
		}
		if (isProjRole != null) {
			this.isProjRole = Boolean.valueOf(isProjRole);
		}
		if (isSameRolePass != null) {
			this.isSameRolePass = Boolean.valueOf(isSameRolePass);
		}
		if (isHighLevelPass != null) {
			this.isHighLevelPass = Boolean.valueOf(isHighLevelPass);
		}
		if (highDeptUserApplyOff != null) {
			this.highDeptUserApplyOff = Boolean.valueOf(highDeptUserApplyOff);
		}
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
		throw new FlowSettingException("FlowTask id:" + getId() + " has no FlowLink match action:" + action);
	}
	
	public boolean checkFlowLink(LinkAction action) {
		for (FlowComponent child : getChildren()) {
			if (child instanceof FlowConditions) {
				FlowConditions conditions = (FlowConditions) child;
				return conditions.checkFlowLink(action);
			}
			else if (child instanceof FlowLink) {
				FlowLink flowLink = (FlowLink) child;
				if (action == null || action.getAction() == null || action.getAction().equalsIgnoreCase(flowLink.getAction())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public FlowLink getFlowLinkByTo(String to) {
		for (FlowComponent child : getChildren()) {
			if (child instanceof FlowConditions) {
				FlowConditions conditions = (FlowConditions) child;
				return conditions.getFlowLinkByTo(to);
			}
			else if (child instanceof FlowLink) {
				return (FlowLink) child;
			}
		}
		throw new FlowSettingException("FlowTask id:" + getId() + " has no FlowLink match to:" + to);
	}
	
	public void processBefore(DocState docState, OrgService orgService) {
		String rolesId = getRoles();
		
		if (! docState.getNowUser().equals(docState.getApplicant())) {
			if (rolesId != null && getDept() == null) {
				for (String roleId : rolesId.split(",")) {
					RoleProf taskRole = orgService.fetchRoleByRoleId(roleId);
					if (taskRole != null && !taskRole.isDeptRole() && taskRole.getLevel() == 0) {
						docState.setLastReviewDept(docState.getNowUser().getDept());
						break;
					}
				}
			}
			else {
				docState.setLastReviewDept(docState.getNowUser().getDept());
			}
		}
	}
	
	@Override
	public FlowComponent processInternalBefore(DocState docState, OrgService orgService) {
		String rolesId = getRoles();
		
		if (getDept() == null && isProjRole == null && rolesId != null) {
			FlowComponent nextComp = processSameRolePass(docState, orgService, rolesId);
			if (nextComp != null) {
				return nextComp;
			}
			
			nextComp = processHighLevelPass(docState, orgService, rolesId);
			if (nextComp != null) {
				return nextComp;
			}
			
			nextComp = processHighDeptUserApply(docState, orgService, rolesId);
			if (nextComp != null) {
				return nextComp;
			}
		}
		return this;
	}
	
	private FlowComponent processSameRolePass(DocState docState, OrgService orgService, String rolesId) {
		if (isSameRolePass != null && isSameRolePass()) {
			for (String roleId : rolesId.split(",")) {
				RoleProf taskRole = orgService.fetchRoleByRoleId(roleId);
				if (taskRole != null && taskRole.getLevel() != 0) {
					Set<RoleProf> userRoles = docState.getApplicant().getRoles();
					for (UserDeptRole deptRole : orgService.fetchUserDeptRoles(docState.getApplicant().getUserId())) {
						RoleProf role = deptRole.getRoleProf();
						if (role.isDeptRole() && !userRoles.contains(role)) {
							userRoles.add(role);
						}
					}
					return compareRoleId(taskRole, userRoles);
				}
			}
		}
		return null;
	}
	
	private FlowComponent compareRoleId(RoleProf taskRole, Set<RoleProf> userRoles) {
		for (RoleProf userRole : userRoles) {
			if (taskRole.getRoleId().equalsIgnoreCase(userRole.getRoleId())) {
				FlowLink nextLink = getFlowLink(null); // Default first FlowLink
				return getRoot().getFlowComponent(nextLink.getTo());
			}
		}
		return null;
	}
	
	private FlowComponent processHighLevelPass(DocState docState, OrgService orgService, String rolesId) {
		if (isHighLevelPass != null && isHighLevelPass()) {
			for (String roleId : rolesId.split(",")) {
				RoleProf taskRole = orgService.fetchRoleByRoleId(roleId);
				if (taskRole != null && taskRole.getLevel() != 0) {
					Set<RoleProf> userRoles = docState.getApplicant().getRoles();
					for (UserDeptRole deptRole : orgService.fetchUserDeptRoles(docState.getApplicant().getUserId())) {
						RoleProf role = deptRole.getRoleProf();
						if (role.getLevel() != 0 && !userRoles.contains(role)) {
							userRoles.add(role);
						}
					}
					return compareRoleLevel(taskRole, userRoles);
				}
			}
		}
		return null;
	}
	
	private FlowComponent compareRoleLevel(RoleProf taskRole, Set<RoleProf> userRoles) {
		if (taskRole.getLevel() > 0) {
			for (RoleProf userRole : userRoles) {
				if (userRole.getLevel() > taskRole.getLevel()) {
					FlowLink nextLink = getFlowLink(null); // Default first FlowLink
					return getRoot().getFlowComponent(nextLink.getTo());
				}
			}
		}
		return null;
	}
	
	private FlowComponent processHighDeptUserApply(DocState docState, OrgService orgService, String rolesId) {
		if (highDeptUserApplyOff != null && !highDeptUserApplyOff) {
			// When non leaf Dept's applicant direct to their supervisor
			DeptProf applicatnDept = docState.getApplicant().getDept();
			if (applicatnDept != null) {				
				for (String roleId : rolesId.split(",")) {
					RoleProf taskRole = orgService.fetchRoleByRoleId(roleId);
					if (taskRole != null && taskRole.getLevel() != 0 && taskRole.isDeptRole()) {
						Integer deptLevel = orgService.getMaxDeptLevelByRole(taskRole);
						if (deptLevel != null) {
							FlowComponent nextComp = compareHighDeptUserApplyDeptLevel(applicatnDept.getLevel(), deptLevel, orgService);
							if (nextComp != null) {
								return nextComp;
							}
						}
					}
				}
			}
			else { // When no Dept's applicant direct to high level supervisor 
				for (String roleId : rolesId.split(",")) {
					RoleProf taskRole = orgService.fetchRoleByRoleId(roleId);
					if (taskRole != null && taskRole.getLevel() != 0 && taskRole.isDeptRole()) {
						FlowLink nextLink = getFlowLink(null); // Default first FlowLink
						return getRoot().getFlowComponent(nextLink.getTo());
					}
				}
			}
		}
		return null;
	}
	
	private FlowComponent compareHighDeptUserApplyDeptLevel(int applicantDeptLevel, int deptLevel, OrgService orgService) {
		if (applicantDeptLevel < deptLevel) {
			FlowLink nextLink = getFlowLink(null); // Default first FlowLink
			FlowComponent nextComp = getRoot().getFlowComponent(nextLink.getTo());
			if (nextComp instanceof FlowTask) {
				FlowTask flowTask = (FlowTask) nextComp;
				if (flowTask.getDept() == null && flowTask.isProjRole() == null && flowTask.getRoles() != null) { // When next task is dept role 
					for (String roleId : flowTask.getRoles().split(",")) {
						RoleProf taskRole = orgService.fetchRoleByRoleId(roleId);
						if (taskRole != null && taskRole.getLevel() != 0 && taskRole.isDeptRole()) {
							Integer nextDeptLevel = orgService.getMaxDeptLevelByRole(taskRole);
							if (nextDeptLevel != null && nextDeptLevel < deptLevel) {
								return flowTask;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public List<DocState> processInternalAfter(DocState docState, DocStateRepository docStateRepository, DocStateLogRepository docStateLogRepository, OrgService orgService, 
			UserProf beRepresented, UserProf agent, List<UserProf> nextConcurrentUsers) {
		docState.setTaskRoleIds(getRoles());
		docState.setTaskDeptId(getDept());
		docState.setTaskUserIds(getUsers());
		docState.setTaskExt(getTaskExt());
		if (getSameUserAs() != null) {
			if (processSameUserAs(docState, docState) == false && docState.getUpDocState() != null) {
				processSameUserAs(docState.getUpDocState(), docState);
			}
		}
		
		if (isReviewDeptRole != null && isReviewDeptRole()) {
			docState.setIsReviewDeptRole("Y");
		}
		else {
			docState.setIsReviewDeptRole("N");
		}
		
		if (isProjRole != null && isProjRole()) {
			docState.setIsProjRole("Y");
		}
		else {
			docState.setIsProjRole("N");
		}
		return null;
	}
	
	private boolean processSameUserAs(DocState docState, DocState docStateUpd) {
		for (DocStateLog docStateLog : docState.getDocStateLogs()) {
			if (getClass().getSimpleName().equals(docStateLog.getTaskType()) && getSameUserAs().equalsIgnoreCase(docStateLog.getFromTaskId())) {
				docStateUpd.setNowUser(docStateLog.getBeRepresented() != null ? docStateLog.getBeRepresented() : docStateLog.getUser());
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<UserProf> fetchCandidates(DocState docState, OrgService orgService) {
		
		List<UserProf> list = new ArrayList<UserProf>();
		
		String rolesId = getRoles();
		String deptId = getDept();
		String usersId = getUsers();
		if (deptId != null && rolesId != null) {
			for (String roleId : rolesId.split(",")) {
				for (UserProf user : orgService.fetchUsersInUserDeptRole(deptId, roleId)) {
					if (!list.contains(user) && "Y".equals(user.getStatus())) {
						list.add(user);
					}
				}
			}
		}
		else if (rolesId != null) {
			DeptProf userDept = (isReviewDeptRole != null && isReviewDeptRole() && docState.getLastReviewDept() != null) ? docState.getLastReviewDept() :
					((docState.getSubFlowDept() != null && isTaskInSubFlow()) ? docState.getSubFlowDept() : docState.getApplicant().getDept());
			
			for (String roleId : rolesId.split(",")) {
				if (isProjRole != null && isProjRole() && docState.getProjId() != null) {
					for (String userId : orgService.findProjRoleUserIdList(docState.getProjId(), roleId)) {
						UserProf user = orgService.fetchUserByUserId(userId);
						if (!list.contains(user) && "Y".equals(user.getStatus())) {
							list.add(user);
						}
					}
				}
				else {
					RoleProf taskRole = orgService.fetchRoleByRoleId(roleId);
					if (taskRole != null) {
						
						for (UserProf actor : taskRole.getActors()) {
							if(null!=actor) {
								if (!list.contains(actor) && "Y".equals(actor.getStatus())) {
									if (taskRole.isDeptRole() && !isSameDeptTree(userDept, actor.getDept())) {
										continue;
									}
									list.add(actor);
								}
							}
							
						}
						if (taskRole.isDeptRole() && userDept != null) {
							for (UserProf user : orgService.fetchUsersInUserDeptRole(userDept.getDeptId(), roleId)) {
								if (!list.contains(user) && "Y".equals(user.getStatus())) {
									list.add(user);
								}
							}
							addUpDeptRoleActors2List(userDept, roleId, list, orgService, userDept.getDeptId());
						}
					}
				}
			}
		}
		else if (deptId != null) {
			DeptProf taskDept = orgService.fetchDeptByDeptId(deptId);
			if (taskDept != null) {
				for (UserProf user : taskDept.getUsers()) {
					if(null!=user) {
						if (!list.contains(user) && "Y".equals(user.getStatus())) {
							list.add(user);
						}
					}
				}
				for (UserProf user : taskDept.getOtherUsers()) {
					if(null!=user) {
						if (!list.contains(user) && "Y".equals(user.getStatus())) {
							list.add(user);
						}
					}
				}
			}
		}
		else if (usersId != null) {
			for (String userId : usersId.split(",")) {
				UserProf user = orgService.fetchUserByUserId(userId);
				if (!list.contains(user) && "Y".equals(user.getStatus())) {
					list.add(user);
				}
			}
		}
		
		Collections.sort(list);
		
		return list;
	}
	
	private boolean isTaskInSubFlow() {
		return getRoot().isSubFlow(getParent());
	}
	
	private boolean isSameDeptTree(DeptProf userDept, DeptProf actorDept) {
		if (userDept != null && actorDept != null) {
			if (userDept.getId().equals(actorDept.getId())) {
				return true;
			}
			return isSameDeptTree(userDept.getUpDept(), actorDept);
		}
		return false;
	}
	
	private void addUpDeptRoleActors2List(DeptProf dept, String roleId, List<UserProf> list, OrgService orgService, String deptId) {
		DeptProf upDept = dept.getUpDept();
		if (upDept != null && !orgService.checkUserDeptRoleByDeptIdAndRoleId(deptId, roleId)) {
			for (UserProf user : orgService.fetchUsersInUserDeptRole(upDept.getDeptId(), roleId)) {
				if (!list.contains(user) && "Y".equals(user.getStatus())) {
					list.add(user);
				}
			}
			addUpDeptRoleActors2List(upDept, roleId, list, orgService, deptId);
		}
	}
	
	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}
	
	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public String getSameUserAs() {
		return sameUserAs;
	}

	public void setSameUserAs(String sameUserAs) {
		this.sameUserAs = sameUserAs;
	}
	
	public String getTaskExt() {
		return taskExt;
	}

	public void setTaskExt(String taskExt) {
		this.taskExt = taskExt;
	}

	public Boolean isReviewDeptRole() {
		return isReviewDeptRole;
	}

	public void setIsReviewDeptRole(Boolean isReviewDeptRole) {
		this.isReviewDeptRole = isReviewDeptRole;
	}
	
	public Boolean isProjRole() {
		return isProjRole;
	}

	public void setIsProjRole(Boolean isProjRole) {
		this.isProjRole = isProjRole;
	}

	public Boolean isSameRolePass() {
		return isSameRolePass;
	}

	public void setIsSameRolePass(Boolean isSameRolePass) {
		this.isSameRolePass = isSameRolePass;
	}
	
	public Boolean isHighLevelPass() {
		return isHighLevelPass;
	}

	public void setIsHighLevelPass(Boolean isHighLevelPass) {
		this.isHighLevelPass = isHighLevelPass;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("flowTask: id=\"").append(getId()).append("\"");
		if (getName() != null) {
			sb.append(" name=\"").append(getName()).append("\"");
		}
		if (getRoles() != null) {
			sb.append(" roles=\"").append(getRoles()).append("\"");
		}
		if (getDept() != null) {
			sb.append(" dept=\"").append(getDept()).append("\"");
		}
		if (getUsers() != null) {
			sb.append(" users=\"").append(getUsers()).append("\"");
		}
		if (getSameUserAs() != null) {
			sb.append(" sameUserAs=\"").append(getSameUserAs()).append("\"");
		}
		if (getTaskExt() != null) {
			sb.append(" taskExt=\"").append(getTaskExt()).append("\"");
		}
		if (isReviewDeptRole() != null) {
			sb.append(" isReviewDeptRole=\"").append(isReviewDeptRole()).append("\"");
		}
		if (isProjRole() != null) {
			sb.append(" isProjRole=\"").append(isProjRole()).append("\"");
		}
		if (isSameRolePass() != null) {
			sb.append(" isSameRolePass=\"").append(isSameRolePass()).append("\"");
		}
		if (isHighLevelPass() != null) {
			sb.append(" isHighLevelPass=\"").append(isHighLevelPass()).append("\"");
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
