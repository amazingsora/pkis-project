package com.tradevan.handyflow.service;

import java.util.List;

import com.tradevan.handyflow.model.form.DocState;
import com.tradevan.handyflow.model.form.DocToDo;

/**
 * Title: NotifyService<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.3.1
 */
public interface NotifyService {

	void sendMail(String to, String subject, String body);
	
	void notifyToDoMessage(String sysId, String[] userIds, String[] to, String[] subjArgs, String[] textArgs, String applyNo, Integer serialNo);
	
	void notifyEndMessage(String sysId, String[] to, String[] subjArgs, String[] textArgs, String applyNo);
	
	void processNotifications(DocState docState);
	
	void processNotifications(DocState docState, List<DocState> subDocStates);
	
	void processNotificationsAndReplaceDisabledUser(DocState docState, boolean bReplaceDisabledUser);
	
	void processNotificationsAndReplaceDisabledUser(DocState docState, List<DocState> subDocStates);
	
	void processNotifications(DocToDo docToDo);
	
	void processNotifications(List<DocToDo> docToDoList);
}
