package com.tradevan.handyflow.service;

import java.util.Map;
import java.util.Set;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.handyflow.bean.DocToDoBean;
import com.tradevan.handyflow.model.form.FormConf;

/**
 * Title: DocToDoService<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.3.4
 */
public interface DocToDoService {
	
	Map<String, Long> addDocToDo(FormConf formConf, Set<String> userIds, String sysId, String projId, String subject, Long mainFormId, String createUserId);
	
	Map<String, Long> addDocToDo(FormConf formConf, Set<String> userIds, String sysId, String projId, String subject, Long mainFormId, CreateUserDto createUserDto);
	
	Long addDocToDo(FormConf formConf, String userId, String sysId, String projId, String subject, Long mainFormId, String createUserId);
	
	Long addDocToDo(FormConf formConf, String userId, String sysId, String projId, String subject, Long mainFormId, CreateUserDto createUserDto);
	
	Map<String, Long> addDocToDo(FormConf formConf, Set<String> userIds, String sysId, String projId, String subject, Long mainFormId, String otherInfo, Long otherSerNo, String createUserId);
	
	Map<String, Long> addDocToDo(FormConf formConf, Set<String> userIds, String sysId, String projId, String subject, Long mainFormId, String otherInfo, Long otherSerNo, CreateUserDto createUserDto);
	
	Long addDocToDo(FormConf formConf, String userId, String sysId, String projId, String subject, Long mainFormId, String otherInfo, Long otherSerNo, String createUserId);
	
	Long addDocToDo(FormConf formConf, String userId, String sysId, String projId, String subject, Long mainFormId, String otherInfo, Long otherSerNo, CreateUserDto createUserDto);
	
	DocToDoBean getDocToDoBean(Long id);
	
	boolean finishDocToDoStatus(Long id, String sysId);
	
	boolean finishDocToDoStatus(Long id, String sysId, String otherInfo, Long otherSerNo);
	
	boolean revokeDocToDoStatus(Long id, String sysId);
	
	boolean revokeDocToDoStatus(Long id, String sysId, String otherInfo, Long otherSerNo);
	
	boolean cancelDocToDoStatus(Long id, String sysId);
	
	boolean cancelDocToDoStatus(Long id, String sysId, String otherInfo, Long otherSerNo);
	
	boolean isDocToDoExists(String userId, String sysId, String projId);
	
	boolean updateMainFormId(Long id,Long mainFormId);
	
	void pauseToDo(String projId, String sysId);
	
	void restartToDo(String projId, String sysId);
}
