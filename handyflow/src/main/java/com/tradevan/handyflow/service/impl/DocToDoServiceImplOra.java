package com.tradevan.handyflow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.util.DateUtil;
import com.tradevan.handyflow.bean.DocToDoBean;
import com.tradevan.handyflow.model.form.DocToDo;
import com.tradevan.handyflow.model.form.FormConf;
import com.tradevan.handyflow.repository.DocNoticeLogRepository;
import com.tradevan.handyflow.repository.DocToDoRepository;
import com.tradevan.handyflow.service.ApplyNoService;
import com.tradevan.handyflow.service.DocToDoService;
import com.tradevan.handyflow.service.NotifyService;

/**
 * Title: DocToDoServiceImpl<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.3.4
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DocToDoServiceImplOra implements DocToDoService {

	@Autowired
	//@Qualifier("applyNoServiceImplOra")
	private ApplyNoService applyNoService;
	
	@Autowired
	//@Qualifier("notifyServiceImplOra")
	private NotifyService notifyService;
	
	@Autowired
	//@Qualifier("jpaDocToDoRepository")
	private DocToDoRepository docToDoRepository;
	
	@Autowired
	//@Qualifier("jpaDocNoticeLogRepository")
	private DocNoticeLogRepository docNoticeLogRepository;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Map<String, Long> addDocToDo(FormConf formConf, Set<String> userIds, String sysId, String projId, String subject, Long mainFormId, String createUserId) {
		
		return saveDocToDoListInBatch(formConf, userIds, sysId, projId, subject, mainFormId, null, null, createUserId, null);
	}
	
	private Map<String, Long> saveDocToDoListInBatch(FormConf formConf, Set<String> userIds, String sysId, String projId, String subject, Long mainFormId, 
			String otherInfo, Long otherSerNo, String createUserId, CreateUserDto createUserDto) {
		Map<String, Long> rtnMap = new HashMap<String, Long>();
		
		String toDoNo = genToDoNo(formConf.getFormId());
		List<DocToDo> list = new ArrayList<DocToDo>();
		int serialNo = 0;
		for (String userId : userIds) {
			DocToDo docToDo = null;
			if (createUserDto != null) {
				docToDo = new DocToDo(formConf, toDoNo, serialNo++, userId, sysId, projId, subject, mainFormId, otherInfo, otherSerNo, createUserDto);
			}
			else {
				docToDo = new DocToDo(formConf, toDoNo, serialNo++, userId, sysId, projId, subject, mainFormId, otherInfo, otherSerNo, createUserId);
			}
			list.add(docToDo);
		}
		docToDoRepository.saveInBatch(list, 100);
		
		notifyService.processNotifications(list);
		
		for (DocToDo docToDo : docToDoRepository.findEntityListByProperties(new String[] { "formConf", "toDoNo" }, new Object[] { formConf, toDoNo }, null, null, null)) {
			rtnMap.put(docToDo.getUserId(), docToDo.getId());
		}
		return rtnMap;
	}
	
	private String genToDoNo(String formId) {
		return applyNoService.genToDoNo(formId, "todo", DateUtil.FMT_YYYYMMDD, 4) + DateUtil.getSysDateStr("-SSS"); // 取號增加毫秒數，降低取號重覆的機率 
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Map<String, Long> addDocToDo(FormConf formConf, Set<String> userIds, String sysId, String projId, String subject, Long mainFormId, CreateUserDto createUserDto) {
		
		return saveDocToDoListInBatch(formConf, userIds, sysId, projId, subject, mainFormId, null, null, null, createUserDto);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Long addDocToDo(FormConf formConf, String userId, String sysId, String projId, String subject, Long mainFormId, String createUserId) {
		
		return saveToDo(formConf, userId, sysId, projId, subject, mainFormId, null, null, createUserId, null);
	}

	private Long saveToDo(FormConf formConf, String userId, String sysId, String projId, String subject, Long mainFormId, 
			String otherInfo, Long otherSerNo, String createUserId, CreateUserDto createUserDto) {
		String toDoNo = genToDoNo(formConf.getFormId());
		DocToDo docToDo = null;
		if (createUserDto != null) {
			docToDo = docToDoRepository.save(new DocToDo(formConf, toDoNo, 0, userId, sysId, projId, subject, mainFormId, otherInfo, otherSerNo, createUserDto));
		}
		else {
			docToDo = docToDoRepository.save(new DocToDo(formConf, toDoNo, 0, userId, sysId, projId, subject, mainFormId, otherInfo, otherSerNo, createUserId));
		}
		
		notifyService.processNotifications(docToDo);
		
		return docToDo.getId();
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Long addDocToDo(FormConf formConf, String userId, String sysId, String projId, String subject, Long mainFormId, CreateUserDto createUserDto) {
		
		return saveToDo(formConf, userId, sysId, projId, subject, mainFormId, null, null, null, createUserDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Map<String, Long> addDocToDo(FormConf formConf, Set<String> userIds, String sysId, String projId, String subject, Long mainFormId, 
			String otherInfo, Long otherSerNo, String createUserId) {
		
		return saveDocToDoListInBatch(formConf, userIds, sysId, projId, subject, mainFormId, otherInfo, otherSerNo, createUserId, null);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Map<String, Long> addDocToDo(FormConf formConf, Set<String> userIds, String sysId, String projId, String subject, Long mainFormId, 
			String otherInfo, Long otherSerNo, CreateUserDto createUserDto) {
		
		return saveDocToDoListInBatch(formConf, userIds, sysId, projId, subject, mainFormId, otherInfo, otherSerNo, null, createUserDto);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Long addDocToDo(FormConf formConf, String userId, String sysId, String projId, String subject, Long mainFormId, String otherInfo, Long otherSerNo, String createUserId) {
		
		return saveToDo(formConf, userId, sysId, projId, subject, mainFormId, otherInfo, otherSerNo, createUserId, null);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Long addDocToDo(FormConf formConf, String userId, String sysId, String projId, String subject, Long mainFormId, String otherInfo, Long otherSerNo, CreateUserDto createUserDto) {
		
		return saveToDo(formConf, userId, sysId, projId, subject, mainFormId, otherInfo, otherSerNo, null, createUserDto);
	}
	
	@Override
	public DocToDoBean getDocToDoBean(Long id) {
		DocToDo docToDo = docToDoRepository.getEntityById(id);
		return new DocToDoBean(docToDo);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public boolean finishDocToDoStatus(Long id, String sysId) {
		
		return updateDocToDoStatus(id, sysId, "D", null, null);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public boolean finishDocToDoStatus(Long id, String sysId, String otherInfo, Long otherSerNo) {
		
		return updateDocToDoStatus(id, sysId, "D", otherInfo, otherSerNo);
	}
	
	private boolean updateDocToDoStatus(Long id, String sysId, String status, String otherInfo, Long otherSerNo) {
		DocToDo docToDo = docToDoRepository.getEntityById(id);
		if (docToDo != null) {
			docToDo.setOldStatus(docToDo.getStatus());
			docToDo.setStatus(status);
			if (otherInfo != null) {
				docToDo.setOtherInfo(otherInfo);
			}
			if (otherSerNo != null) {
				docToDo.setOtherSerNo(otherSerNo);
			}
			
			if ("D".equals(status) || "C".equals(status)) {
				docNoticeLogRepository.updateStatusWithDocToDoId("Y", sysId, id);
			}
			else if ("W".equals(status)) {
				docNoticeLogRepository.updateStatusWithDocToDoId("N", sysId, id);
			}
			return true;
		}
		return false;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public boolean revokeDocToDoStatus(Long id, String sysId) {
		
		return updateDocToDoStatus(id, sysId, "W", null, null);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public boolean revokeDocToDoStatus(Long id, String sysId, String otherInfo, Long otherSerNo) {
		
		return updateDocToDoStatus(id, sysId, "W", otherInfo, otherSerNo);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public boolean cancelDocToDoStatus(Long id, String sysId) {
		
		return updateDocToDoStatus(id, sysId, "C", null, null);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public boolean cancelDocToDoStatus(Long id, String sysId, String otherInfo, Long otherSerNo) {
		
		return updateDocToDoStatus(id, sysId, "C", otherInfo, otherSerNo);
	}
	
	@Override
	public boolean isDocToDoExists(String userId, String sysId, String projId) {
		Long cnt = docToDoRepository.countByProperties(new String[] { "userId", "sysId", "projId" },  new Object[] { userId, sysId, projId });
		return cnt > 0 ? true : false;
	}
	
	@Override
	public boolean updateMainFormId(Long id,Long mainFormId) {
		DocToDo docToDo = docToDoRepository.getEntityById(id);
		if (docToDo != null) {
			docToDo.setMainFormId(mainFormId);
			return true;
		}
		return false;
	}

	@Override
	public void pauseToDo(String projId, String sysId) {
		docToDoRepository.updateStatusByProjIdAndSysId(projId, sysId, "C");
	}
	
	@Override
	public void restartToDo(String projId, String sysId) {
		
		String sql = "UPDATE docToDo SET status = oldStatus WHERE sysId = ? AND projId = ? AND oldStatus IS NOT NULL";
		docToDoRepository.updateBySQL(sql,sysId,projId);
	}
}
