package com.tradevan.handyflow.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tradevan.apcommon.util.StringUtil;
import com.tradevan.aporg.model.AgentProf;
import com.tradevan.aporg.model.DeptProf;
import com.tradevan.aporg.model.RoleProf;
import com.tradevan.aporg.model.UserDeptRole;
import com.tradevan.aporg.model.UserProf;
import com.tradevan.aporg.repository.AgentProfRepository;
import com.tradevan.aporg.service.OrgService;
import com.tradevan.handyflow.bean.DocStateBean;
import com.tradevan.handyflow.model.form.DocState;
import com.tradevan.handyflow.model.form.DocStateLog;
import com.tradevan.handyflow.model.form.DocToDo;
import com.tradevan.handyflow.repository.DocStateRepository;
import com.tradevan.handyflow.repository.DocToDoRepository;
import com.tradevan.handyflow.repository.DocStateLogRepository;
import com.tradevan.handyflow.service.FlowQueryService;

/**
 * Title: QueryServiceImpl<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.9
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FlowQueryServiceImplOra implements FlowQueryService {

	@Autowired
	private OrgService orgService;
	
	@Autowired
	//@Qualifier("jpaDocToDoRepository")
	private DocToDoRepository docToDoRepository;
	
	@Autowired
	//@Qualifier("jpaDocStateRepository")
	private DocStateRepository docStateRepository;
	
	@Autowired
	//@Qualifier("jpaDocStateLogRepository")
	private DocStateLogRepository docStateLogRepository;
	
	@Autowired
	//@Qualifier("jpaAgentProfRepository")
	private AgentProfRepository agentProfRepository;
	
	@Override
	public List<DocStateBean> fetchToDoListBy(String userId, String sysId, boolean sortDesc) {
		return fetchToDoListBy(userId, sysId, sortDesc, false);
	}

	@Override
	public List<DocStateBean> fetchToDoListBy(String userId, String formId, String sysId, boolean sortDesc) {
		return fetchToDoListBy(userId, formId, sysId, sortDesc, false);
	}
	
	@Override
	public List<DocStateBean> fetchToDoListBy(String userId, String sysId, boolean sortDesc, boolean includeAgent) {
		
		return fetchToDoListBy(userId, null, sysId, sortDesc, includeAgent);
	}
	
	@Override
	public List<DocStateBean> fetchToDoListBy(String userId, String formId, String sysId, boolean sortDesc, boolean includeAgent) {
		
		List<DocStateBean> list = new ArrayList<DocStateBean>();
		
		fetchToDoListInternal(list, userId, formId, sysId, sortDesc, null, null);
		
		if (includeAgent) {
			for (AgentProf agentProf : agentProfRepository.findActiveAgentRecords(userId)) {
				String beRepresentedId = agentProf.getUser().getUserId();
				fetchToDoListInternal(list, beRepresentedId, formId, sysId, sortDesc, beRepresentedId, userId);
			}
		}
		
		if (sortDesc) {
			Collections.sort(list, new Comparator<DocStateBean>() {
				@Override
				public int compare(DocStateBean bean1, DocStateBean bean2) {
					return bean2.getApplyNo().compareTo(bean1.getApplyNo());
				}
			});
		}
		else {
			Collections.sort(list, new Comparator<DocStateBean>() {
				@Override
				public int compare(DocStateBean bean1, DocStateBean bean2) {
					return bean1.getApplyNo().compareTo(bean2.getApplyNo());
				}
			});
		}
		return list;
	}
	
	private void fetchToDoListInternal(List<DocStateBean> list, String userId, String formId, String sysId, boolean sortDesc, String beRepresentedId, String agentId) {
		DocStateBean bean = null;
		
		for (DocToDo docToDo : docToDoRepository.findOwnedListByUser(userId, formId, sysId, sortDesc)) {
			bean = new DocStateBean(docToDo, beRepresentedId, agentId);
			if (! list.contains(bean)) {
				list.add(bean);
			}
		}
		
		for (DocState state : docStateRepository.findOwnedListByUser(userId, formId, sysId, sortDesc)) {
			bean = new DocStateBean(state, null, beRepresentedId, agentId, false, null);
			if (! list.contains(bean)) {
				list.add(bean);
			}
		}
		
		UserProf user = orgService.fetchUserByUserId(userId);
		if(user==null) {
			System.out.println("userId not found!!! "+ userId);
			return;
		}
		DeptProf userDept = user.getDept();
		Set<RoleProf> roles = user.getRoles();
		Set<DeptProf> depts = user.getDepts();
		List<UserDeptRole> deptRoles = orgService.fetchUserDeptRoles(userId);
		
		if (roles != null) {
			for (RoleProf role : roles) {
				if (role.isDeptRole()) {
					if (userDept != null) {
						for (DocState state : docStateRepository.findNoOwnerListByApplicantDeptRole(userDept.getDeptId(), role.getRoleId(), formId, sysId, sortDesc, false)) {
							bean = new DocStateBean(state, null, beRepresentedId, agentId, false, null);
							if (! list.contains(bean)) {
								list.add(bean);
							}
						}
						for (DocState state : docStateRepository.findNoOwnerListByReviewDeptRole(userDept.getDeptId(), role.getRoleId(), formId, sysId, sortDesc, false)) {
							bean = new DocStateBean(state, null, beRepresentedId, agentId, false, null);
							if (! list.contains(bean)) {
								list.add(bean);
							}
						}
						fetchToDoListAppliedOrReviewedBySubDepts(list, role.getRoleId(), formId, userDept, sysId, sortDesc, beRepresentedId, agentId);
					}
				}
				else {
					for (DocState state : docStateRepository.findNoOwnerListByRole(role.getRoleId(), formId, sysId, sortDesc)) {
						bean = new DocStateBean(state, null, beRepresentedId, agentId, false, null);
						if (! list.contains(bean)) {
							list.add(bean);
						}
					}
				}
				
				for (DocState state : docStateRepository.findNoOwnerListByUserProjRole(user.getUserId(), formId, sysId, sortDesc)) {
					bean = new DocStateBean(state, null, beRepresentedId, agentId, false, null);
					if (! list.contains(bean)) {
						list.add(bean);
					}
				}
			}
		}
		
		for (UserDeptRole deptRole : deptRoles) {
			RoleProf role = deptRole.getRoleProf();
			if (role.isDeptRole()) {
				for (DocState state : docStateRepository.findNoOwnerListByApplicantDeptRole(deptRole.getDeptId(), role.getRoleId(), formId, sysId, sortDesc, false)) {
					bean = new DocStateBean(state, null, beRepresentedId, agentId, false, null);
					if (! list.contains(bean)) {
						list.add(bean);
					}
				}
				for (DocState state : docStateRepository.findNoOwnerListByReviewDeptRole(deptRole.getDeptId(), role.getRoleId(), formId, sysId, sortDesc, false)) {
					bean = new DocStateBean(state, null, beRepresentedId, agentId, false, null);
					if (! list.contains(bean)) {
						list.add(bean);
					}
				}
				fetchToDoListAppliedOrReviewedBySubDepts(list, role.getRoleId(), formId, deptRole.getDeptProf(), sysId, sortDesc, beRepresentedId, agentId);
			}
			else {
				for (DocState state : docStateRepository.findNoOwnerListByDeptAndRole(deptRole.getDeptId(), deptRole.getRoleId(), formId, sysId, sortDesc)) {
					bean = new DocStateBean(state, null, beRepresentedId, agentId, false, null);
					if (! list.contains(bean)) {
						list.add(bean);
					}
				}
			}
		}
		
//		if (userDept != null) {
//			for (DocState state : docStateRepository.findNoOwnerListByDept(userDept.getDeptId(), formId, sysId, sortDesc)) {
//				bean = new DocStateBean(state, null, beRepresentedId, agentId, false, null);
//				if (! list.contains(bean)) {
//					list.add(bean);
//				}
//			}
//		}
//		
//		if (depts != null) {
//			for (DeptProf dept : depts) {
//				for (DocState state : docStateRepository.findNoOwnerListByDept(dept.getDeptId(), formId, sysId, sortDesc)) {
//					bean = new DocStateBean(state, null, beRepresentedId, agentId, false, null);
//					if (! list.contains(bean)) {
//						list.add(bean);
//					}
//				}
//			}
//		}

		for (DocState state : docStateRepository.findNoOwnerListByUser(userId, formId, sysId, sortDesc)) {
			bean = new DocStateBean(state, null, beRepresentedId, agentId, false, null);
			if (! list.contains(bean)) {
				list.add(bean);
			}
		}
	}
	
	private void fetchToDoListAppliedOrReviewedBySubDepts(List<DocStateBean> list, String roleId, String formId, DeptProf dept, String sysId, boolean sortDesc, 
			String beRepresentedId, String agentId) {
		DocStateBean bean = null;
		
		for (DeptProf subDept : dept.getSubDepts()) {
			for (DocState state : docStateRepository.findNoOwnerListByApplicantDeptRole(subDept.getDeptId(), roleId, formId, sysId, sortDesc, true)) {
				bean = new DocStateBean(state, null, beRepresentedId, agentId, false, null);
				if (! list.contains(bean)) {
					list.add(bean);
				}
			}
			for (DocState state : docStateRepository.findNoOwnerListByReviewDeptRole(subDept.getDeptId(), roleId, formId, sysId, sortDesc, true)) {
				bean = new DocStateBean(state, null, beRepresentedId, agentId, false, null);
				if (! list.contains(bean)) {
					list.add(bean);
				}
			}
			fetchToDoListAppliedOrReviewedBySubDepts(list, roleId, formId, subDept, sysId, sortDesc, beRepresentedId, agentId);
		}
	}

	@Override
	public DocStateBean getDocStateById(Long docStateId) {
		DocState state = docStateRepository.getEntityById(docStateId);
		if (state != null) {
			return new DocStateBean(state, null, null, null, false, null);
		}
		return null;
	}

	@Override
	public DocStateBean getDocStateBy(String formId, String applyNo, Integer serialNo) {
		DocState state = docStateRepository.getEntityByProperties(
				new String[] { "formConf.formId", "applyNo", "serialNo" }, new Object[] { formId, applyNo, (serialNo == null) ? 0 : serialNo });
		if (state != null) {
			return new DocStateBean(state, null, null, null, false, null);
		}
		return null;
	}
	
	@Override
	public List<DocStateLog> fetchDocStateLogsBy(String formId, String applyNo) {
		
		return docStateLogRepository.findListByFormIdAndApplyNo(formId, applyNo, false);
	}

	@Override
	public String getLatestCommentBy(String formId, String applyNo, String taskId) {
		String rtnStr = "";
		for (String memo : docStateLogRepository.findDocStateLogMemos(formId, applyNo, taskId)) {
			if (StringUtil.isNotBlank(memo)) {
				rtnStr = memo;
			}
		}
		return rtnStr;
	}
	
	@Override
	public String getLatestCommentBy(String formId, String applyNo) {
		String rtnStr = "";
		for (String memo : docStateLogRepository.findDocStateLogMemos(formId, applyNo)) {
			rtnStr = memo;
			break;
		}
		return rtnStr;
	}
	
	@Override
	public List<String> fetchCommentsBy(String formId, String applyNo, String taskId) {
		List<String> rtnList = new ArrayList<String>();
		for (String memo : docStateLogRepository.findDocStateLogMemos(formId, applyNo, taskId)) {
			if (StringUtil.isNotBlank(memo)) {
				rtnList.add(memo);
			}
		}
		return rtnList;
	}

	@Override
	public boolean isTaskIdsInDocStateLogs(Long docStateId, List<String> taskIds) {
		DocState docState = docStateRepository.getEntityById(docStateId);
		if (docState != null) {
			DocState upDocState = docState.getUpDocState();
			if (docStateLogRepository.countDocStateLogsBy(docState, taskIds) > 0) {
				return true;
			}
			else if (upDocState != null && docStateLogRepository.countDocStateLogsBy(upDocState, taskIds) > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getApplyNo(Long docStateId) {
		return (String) docStateRepository.getByProperty(new String[] { "applyNo" }, "id", docStateId);
	}

}
