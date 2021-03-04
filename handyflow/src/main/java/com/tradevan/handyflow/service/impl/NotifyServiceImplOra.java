package com.tradevan.handyflow.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tradevan.apcommon.util.StringUtil;
import com.tradevan.aporg.bean.UserDto;
import com.tradevan.aporg.enums.MailCategory;
import com.tradevan.aporg.model.DeptProf;
import com.tradevan.aporg.model.RoleProf;
import com.tradevan.aporg.model.UserDeptRole;
import com.tradevan.aporg.model.UserProf;
import com.tradevan.aporg.repository.UserProfRepository;
import com.tradevan.aporg.service.EmailQueueService;
import com.tradevan.aporg.service.OrgService;
import com.tradevan.handyflow.bean.DocStateBean;
import com.tradevan.handyflow.model.flow.FlowEvent;
import com.tradevan.handyflow.model.form.DocNoticeLog;
import com.tradevan.handyflow.model.form.DocState;
import com.tradevan.handyflow.model.form.DocToDo;
import com.tradevan.handyflow.repository.DocNoticeLogRepository;
import com.tradevan.handyflow.service.FlowQueryService;
import com.tradevan.handyflow.service.FlowService;
import com.tradevan.handyflow.service.NotifyService;

/**
 * Title: NotifyServiceImpl<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.3.3
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class NotifyServiceImplOra implements NotifyService {

	@Value("${flow.useEmailQueue}")
	private String useEmailQueue;
	
	@Autowired
	private MailSender mailSender;

	//@Autowired
	//private SimpleMailMessage toDoMessage;
	
	//@Autowired
	//private SimpleMailMessage endMessage;
	
	@Autowired
	private EmailQueueService emailQueueService;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	//@Qualifier("flowQueryServiceImplOra")
	private FlowQueryService flowQueryService;
	
	@Autowired
	//@Qualifier("flowServiceImplOra")
	private FlowService flowService;
	
	@Autowired
	//@Qualifier("jpaUserProfRepository")
	private UserProfRepository userProfRepository;
	
	@Autowired
	//@Qualifier("jpaDocNoticeLogRepository")
	private DocNoticeLogRepository docNoticeLogRepository;
	
	@Override
	public void sendMail(String to, String subject, String body) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(to);
			message.setSubject(subject);
			message.setText(body);
			mailSender.send(message);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void notifyToDoMessage(String sysId, String[] userIds, String[] to, String[] subjArgs, String[] textArgs, String applyNo, Integer serialNo) {
		try {
			if ("Y".equalsIgnoreCase(useEmailQueue) && to != null) {
				emailQueueService.add2EmailQueue(sysId, MailCategory.TODO_NOTICE, String.join(",", to), 
						new MessageFormat("[待辦提醒] - 您有一筆  {0} 單號：{1} 待處理\"").format(subjArgs), new MessageFormat("您有一筆  {0} 單號：{1} 待處理，請確認！").format(textArgs), "admin");
			}
			else if ("N".equalsIgnoreCase(useEmailQueue) && to != null) {
				SimpleMailMessage message = new SimpleMailMessage();
				message.setTo(to);
				message.setSubject(new MessageFormat(message.getSubject()).format(subjArgs));
				message.setText(new MessageFormat(message.getText()).format(textArgs));
				mailSender.send(message);
			}
			else if ("L".equalsIgnoreCase(useEmailQueue)) {
				updateDocNoticeLogStatus("Y", sysId, applyNo, serialNo);
				
				for (String userId : userIds) {
					docNoticeLogRepository.save(new DocNoticeLog(userId, sysId, applyNo, serialNo));
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateDocNoticeLogStatus(String status, String sysId, String applyNo, Integer serialNo) {
		docNoticeLogRepository.updateStatus(status, sysId, applyNo, serialNo);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void notifyEndMessage(String sysId, String[] to, String[] subjArgs, String[] textArgs, String applyNo) {
		try {
			if ("Y".equalsIgnoreCase(useEmailQueue) && to != null) {
				emailQueueService.add2EmailQueue(sysId, MailCategory.TODO_NOTICE, String.join(",", to), 
						new MessageFormat("[待辦提醒] - 您有一筆  {0} 單號：{1} 申請完成").format(subjArgs), new MessageFormat("您有一筆  {0} 單號：{1} 申請完成，請確認！").format(textArgs), "admin");
			}
			else if ("N".equalsIgnoreCase(useEmailQueue) && to != null) {
				SimpleMailMessage message = new SimpleMailMessage();
				message.setTo(to);
				message.setSubject(new MessageFormat(message.getSubject()).format(subjArgs));
				message.setText(new MessageFormat(message.getText()).format(textArgs));
				mailSender.send(message);
			}
			else if ("L".equalsIgnoreCase(useEmailQueue)) {
				updateDocNoticeLogStatus("Y", sysId, applyNo, null);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void processNotifications(DocState docState) {
		
		processNotificationsAndReplaceDisabledUserInternal(docState, false);
	}
	
	private void processNotificationsAndReplaceDisabledUserInternal(DocState docState, boolean bReplaceDisabledUser) {
		
		if (docState.getFlowStatus().equals(DocState.FLOW_STATUS_PROCESS)) {
			UserProf nowUser = docState.getNowUser();
			String[] subjArgs = new String[] { docState.getFormConf().getName(), docState.getApplyNo() };
			String[] textArgs = new String[] { docState.getFormConf().getName(), docState.getApplyNo() };
			
			if (nowUser != null) {
				
				notifyToDoMessage(docState.getSysId(), new String[] { nowUser.getUserId() }, StringUtil.isEmail(nowUser.getEmail()) ? new String[] { nowUser.getEmail() } : null, 
						subjArgs, textArgs, docState.getApplyNo(), docState.getSerialNo());
				
				if (bReplaceDisabledUser && isUserNotEnabled(nowUser)) {
					replaceNowUser2FlowAdmin(docState);
				}
			}
			else if (docState.getTaskDeptId() != null && docState.getTaskRoleIds() != null) {
				List<String> userIds = new ArrayList<String>();
				List<String> emails = new ArrayList<String>();
				
				boolean bValid = false;
				for (String roleId : docState.getTaskRoleIds().split(",")) {
					for (UserProf user : orgService.fetchUsersInUserDeptRole(docState.getTaskDeptId(), roleId)) {
						String email = user.getEmail();
						if (StringUtil.isEmail(email) && !emails.contains(email)) {
							userIds.add(user.getUserId());
							emails.add(email);
						}
						if (bReplaceDisabledUser && isUserEnabled(user)) {
							bValid = true;
						}
					}
				}
				
				notifyToDoMessage(docState.getSysId(), userIds.toArray(new String[0]), (emails.size() > 0) ? emails.toArray(new String[0]) : null, subjArgs, textArgs, 
						docState.getApplyNo(), docState.getSerialNo());
				
				if (bReplaceDisabledUser && bValid == false) {
					replaceNowUser2FlowAdmin(docState);
				}
			}
			else if (docState.getTaskRoleIds() != null && "Y".equals(docState.getIsProjRole())) {
				List<String> userIds = new ArrayList<String>();
				List<String> emails = new ArrayList<String>();
				String projId = docState.getProjId();
				StringTokenizer st = new StringTokenizer(docState.getTaskRoleIds(), ",");
				
				boolean bValid = false;
				if (projId != null) {
					while (st.hasMoreTokens()) {
						String roleId = st.nextToken();
						
						for (UserProf actor : orgService.fetchUsersByProjIdAndRoleId(projId, roleId)) {
							String email = actor.getEmail();
							if (StringUtil.isEmail(email) && !emails.contains(email)) {
								userIds.add(actor.getUserId());
								emails.add(email);
							}
							if (bReplaceDisabledUser && isUserEnabled(actor)) {
								bValid = true;
							}
						}
					}
				}
				
				notifyToDoMessage(docState.getSysId(), userIds.toArray(new String[0]), (emails.size() > 0) ? emails.toArray(new String[0]) : null, subjArgs, textArgs, 
						docState.getApplyNo(), docState.getSerialNo());
				
				if (bReplaceDisabledUser && bValid == false) {
					replaceNowUser2FlowAdmin(docState);
				}
			}
			else if (docState.getTaskRoleIds() != null) {
				List<String> userIds = new ArrayList<String>();
				List<String> emails = new ArrayList<String>();
				StringTokenizer st = new StringTokenizer(docState.getTaskRoleIds(), ",");
				
				boolean bValid = false;
				while (st.hasMoreTokens()) {
					RoleProf role = orgService.fetchRoleByRoleId(st.nextToken());
					
					if (role != null) {
						if (role.isDeptRole()) {
							DocStateBean docStateBean = flowQueryService.getDocStateById(docState.getId());
							for (UserDto user : flowService.fetchNowCandidates(docStateBean)) {
								String email = user.getEmail();
								if (StringUtil.isEmail(email) && !emails.contains(email)) {
									userIds.add(user.getUserId());
									emails.add(email);
								}
								if (bReplaceDisabledUser && isUserEnabled(user)) {
									bValid = true;
								}
							}
						}
						else {
							for (UserProf actor : role.getActors()) {								 
								if(null!= actor) {
									String email = actor.getEmail();
									if (StringUtil.isEmail(email) && !emails.contains(email)) {
										userIds.add(actor.getUserId());
										emails.add(email);
									}
									if (bReplaceDisabledUser && isUserEnabled(actor)) {
										bValid = true;
									}
								}
								
							}
						}
					}
				}
				
				notifyToDoMessage(docState.getSysId(), userIds.toArray(new String[0]), (emails.size() > 0) ? emails.toArray(new String[0]) : null, subjArgs, textArgs, 
						docState.getApplyNo(), docState.getSerialNo());
				
				if (bReplaceDisabledUser && bValid == false) {
					replaceNowUser2FlowAdmin(docState);
				}
			}
//			else if (docState.getTaskDeptId() != null) {
//				List<String> userIds = new ArrayList<String>();
//				List<String> emails = new ArrayList<String>();
//				DeptProf dept = orgService.fetchDeptByDeptId(docState.getTaskDeptId());
//				
//				boolean bValid = false;
//				if (dept != null) {
//					for (UserProf user : dept.getUsers()) {
//						if(user!= null) {
//							String email = user.getEmail();
//							if (StringUtil.isEmail(email) && !emails.contains(email)) {
//								userIds.add(user.getUserId());
//								emails.add(email);
//							}
//							if (bReplaceDisabledUser && isUserEnabled(user)) {
//								bValid = true;
//							}
//						}
//						
//					}
//				}
//				
//				notifyToDoMessage(docState.getSysId(), userIds.toArray(new String[0]), (emails.size() > 0) ? emails.toArray(new String[0]) : null, subjArgs, textArgs, 
//						docState.getApplyNo(), docState.getSerialNo());
//				
//				if (bReplaceDisabledUser && bValid == false) {
//					replaceNowUser2FlowAdmin(docState);
//				}
//			}
			else if (docState.getTaskUserIds() != null) {
				List<String> userIds = new ArrayList<String>();
				List<String> emails = new ArrayList<String>();
				
				boolean bValid = false;
				for (String userId : docState.getTaskUserIds().split(",")) {
					UserProf user = orgService.fetchUserByUserId(userId);
					String email = user.getEmail();
					if (StringUtil.isEmail(email) && !emails.contains(email)) {
						userIds.add(userId);
						emails.add(email);
					}
					if (bReplaceDisabledUser && isUserEnabled(user)) {
						bValid = true;
					}
				}
				
				notifyToDoMessage(docState.getSysId(), userIds.toArray(new String[0]), (emails.size() > 0) ? emails.toArray(new String[0]) : null, subjArgs, textArgs, 
						docState.getApplyNo(), docState.getSerialNo());
				
				if (bReplaceDisabledUser && bValid == false) {
					replaceNowUser2FlowAdmin(docState);
				}
			}
			else {
				if (bReplaceDisabledUser && isUserNotEnabled(nowUser)) {
					replaceNowUser2FlowAdmin(docState);
				}
			}
		}
		else if (docState.getFlowStatus().equals(FlowEvent.Type.END.getValue())) {
			UserProf applicant = docState.getApplicant();
			String[] subjArgs = new String[] { docState.getFormConf().getName(), docState.getApplyNo() };
			String[] textArgs = new String[] { docState.getFormConf().getName(), docState.getApplyNo() };
			
			if (applicant != null) {
				
				notifyEndMessage(docState.getSysId(), StringUtil.isEmail(applicant.getEmail()) ? new String[] { applicant.getEmail() } : null, subjArgs, textArgs, docState.getApplyNo());
			}
		}
	}
	
	private boolean isUserNotEnabled(UserProf user) {
		return !isUserEnabled(user);
	}
	
	private boolean isUserEnabled(UserProf user) {
		if (user != null && "1".equalsIgnoreCase(user.getStatus())) {
			return true;
		}
		return false;
	}
	
	private boolean isUserEnabled(UserDto user) {
		if (user != null && "1".equalsIgnoreCase(user.getStatus())) {
			return true;
		}
		return false;
	}
	
	private void replaceNowUser2FlowAdmin(DocState docState) {
		String flowAdminId = docState.getFlowAdminId();
		if (StringUtil.isBlank(flowAdminId)) {
			flowAdminId = "admin";
		}
		docState.setNowUser(refUser(flowAdminId));
		docState.setByFlowAdminId(flowAdminId);
	}
	
	private UserProf refUser(String userId) {
		if (userId != null) {
			return orgService.fetchUserByUserId(userId);
		}
		return null;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void processNotifications(DocState docState, List<DocState> subDocStates) {
		if (subDocStates == null) {
			processNotifications(docState);
		}
		else {
			for (DocState subDocState : subDocStates) {
				processNotifications(subDocState);
			}
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void processNotificationsAndReplaceDisabledUser(DocState docState, boolean bReplaceDisabledUser) {
		processNotificationsAndReplaceDisabledUserInternal(docState, true);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void processNotificationsAndReplaceDisabledUser(DocState docState, List<DocState> subDocStates) {
		if (subDocStates == null) {
			processNotificationsAndReplaceDisabledUserInternal(docState, true);
		}
		else {
			for (DocState subDocState : subDocStates) {
				processNotificationsAndReplaceDisabledUserInternal(subDocState, true);
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void processNotifications(DocToDo docToDo) {
		String email = (String) userProfRepository.getByProperty(new String[] { "email" }, "userId", docToDo.getUserId());
		notifyToDoMessage(docToDo.getSysId(), new String[] { docToDo.getUserId() }, (StringUtil.isEmail(email)) ? new String[] { email } : null, 
				(StringUtil.isEmail(email)) ? new String[] { docToDo.getSubject(), docToDo.getToDoNo() } : null, 
				(StringUtil.isEmail(email)) ? new String[] { docToDo.getSubject(), docToDo.getToDoNo() } : null, docToDo.getToDoNo(), docToDo.getSerialNo());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void processNotifications(List<DocToDo> docToDoList) {
		for (DocToDo docToDo : docToDoList) {
			processNotifications(docToDo);
		}
	}
	
}
