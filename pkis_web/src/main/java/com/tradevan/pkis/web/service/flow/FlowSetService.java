package com.tradevan.pkis.web.service.flow;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.handyflow.dto.FlowConfDto;
import com.tradevan.handyflow.model.form.FlowStep;
import com.tradevan.handyflow.model.form.FlowStepLink;
import com.tradevan.handyflow.model.form.FormConf;
import com.tradevan.handyflow.model.form.SubFlowConf;
import com.tradevan.handyflow.repository.FlowStepLinkRepository;
import com.tradevan.handyflow.repository.FlowStepRepository;
import com.tradevan.handyflow.repository.FormConfRepository;
import com.tradevan.handyflow.repository.SubFlowConfRepository;
import com.tradevan.handyflow.service.FlowAdminService;
import com.tradevan.mapper.contract.dao.ContractMapper;
import com.tradevan.mapper.pkis.dao.ContractmasterMapper;
import com.tradevan.mapper.pkis.dao.DeptInfoExtMapper;
import com.tradevan.mapper.pkis.dao.FlowconfMapper;
import com.tradevan.mapper.pkis.dao.FlowstepMapper;
import com.tradevan.mapper.pkis.dao.FlowsteplinkMapper;
import com.tradevan.mapper.pkis.dao.FlowxmlMapper;
import com.tradevan.mapper.pkis.dao.FormconfMapper;
import com.tradevan.mapper.pkis.dao.ReviewsetdataconfMapper;
import com.tradevan.mapper.pkis.dao.SystemparamMapper;
import com.tradevan.mapper.pkis.model.Contractmaster;
import com.tradevan.mapper.pkis.model.DeptInfoExt;
import com.tradevan.mapper.pkis.model.Flowconf;
import com.tradevan.mapper.pkis.model.Flowstep;
import com.tradevan.mapper.pkis.model.Flowsteplink;
import com.tradevan.mapper.pkis.model.Flowxml;
import com.tradevan.mapper.pkis.model.Formconf;
import com.tradevan.mapper.pkis.model.Reviewconf;
import com.tradevan.mapper.pkis.model.Reviewsetdataconf;
import com.tradevan.mapper.pkis.model.Subflowconf;
import com.tradevan.mapper.pkis.model.Suppliermaster;
import com.tradevan.mapper.pkis.model.Systemparam;
import com.tradevan.mapper.xauth.dao.XauthDeptMapper;
import com.tradevan.mapper.xauth.dao.XauthRoleMapper;
import com.tradevan.mapper.xauth.dao.XauthRoleUserMapper;
import com.tradevan.mapper.xauth.dao.XauthUsersMapper;
import com.tradevan.mapper.xauth.model.XauthDept;
import com.tradevan.mapper.xauth.model.XauthRole;
import com.tradevan.pkis.web.service.common.CommonService;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.core.security.UserInfo;
import com.tradevan.xauthframework.web.service.DefaultService;

@Service("FlowSetService")
@Transactional(rollbackFor = Exception.class)
public class FlowSetService extends DefaultService {
	@Autowired
	XauthRoleUserMapper xauthRoleUserMapper;
	
	@Autowired
	XauthUsersMapper xauthUsersMapper;
	
	@Autowired
	XauthDeptMapper xauthDeptMapper;
	
	@Autowired
	XauthRoleMapper xauthRoleMapper;
	
	@Autowired
	DeptInfoExtMapper deptInfoExtMapper;
	
	@Autowired
	FlowstepMapper flowstepMapper;
	
	@Autowired
	ContractmasterMapper contractmasterMapper;
	
	@Autowired
	SystemparamMapper systemparamMapper;
	
	@Autowired
	ReviewsetdataconfMapper reviewsetdataconfMapper;
	
	@Autowired
	FlowconfMapper flowconfMapper;
	
	@Autowired 
	FlowAdminService flowAdminService;
	
	@Autowired
	FlowStepRepository flowStepRepository;
	
	@Autowired
	FlowStepLinkRepository flowStepLinkRepository;
	
	@Autowired
	FormConfRepository formConfRepository;
	
	@Autowired
	SubFlowConfRepository subFlowConfRepository;

	@Autowired
	FormconfMapper formconfMapper;
	
	@Autowired
	FlowsteplinkMapper flowsteplinkMapper;
	
	@Autowired
	FlowxmlMapper flowxmlMapper;
	
	@Autowired
	ContractMapper contractMapper;
	
	@Autowired
	CommonService commonService;
	
	/**
	 * Flow 前置設定處理 SC
	 * @param params
	 * @param reviewconf
	 * @param suppliermaster
	 */
	public ProcessResult setSCFlowStepAndLink(Map<String, Object> params, Reviewconf reviewconf, Suppliermaster suppliermaster) throws Exception {
		ProcessResult result = new ProcessResult();
		String module = MapUtils.getString(params, "module");
		String section = MapUtils.getString(params, "section");
		String flowname = MapUtils.getString(params, "dispName");
		String flowid = MapUtils.getString(params, "flowid");
		String idenId = MapUtils.getString(params, "idenId");
		String applicantid = MapUtils.getString(params, "applicantid");
		UserInfo userInfo = userContext.getCurrentUser();
		List<XauthDept> xauthDeptList = getAllDeptList(null, idenId, idenId);
		List<Flowstep> flowstepList = new ArrayList<Flowstep>();
		List<FlowStepLink> flowStepLinkList = new ArrayList<FlowStepLink>();
		List<DeptInfoExt> deptInfoExtList = new ArrayList<DeptInfoExt>();
		List<Systemparam> systemparamList = new ArrayList<Systemparam>();
		
		if(xauthDeptList.size() == 0) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("查無使用者上階層部門");
			return result;
		}
		
		if(reviewconf == null) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("查詢合約發生錯誤");
			return result;
		}
		
		if(suppliermaster == null) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("查無供應商資料");
			return result;
		}
		
		//刪除總經理
		xauthDeptList.remove(xauthDeptList.size() - 1);
		
		//刪除組織主管
		if(StringUtils.equals("N", reviewconf.getIsorgreview())) {
			int size = xauthDeptList.size();
			xauthDeptList.remove(size - 1);
		}
		
		//刪除單位主管
		if(StringUtils.equals("N", reviewconf.getIsdeptreview())) {
			if(StringUtils.equals("Y", reviewconf.getIsorgreview())) {
				int size = xauthDeptList.size();
				xauthDeptList.remove(size - 1);
				XauthDept xauthDept = xauthDeptList.get(size - 1);
				xauthDeptList = new ArrayList<XauthDept>();
				xauthDeptList.add(xauthDept);
			}else {
				int size = xauthDeptList.size();
				for(int i = size - 1; 0 <= i; i--) {
					xauthDeptList.remove(i);
				}
			}
		}
		
		//部門階層
		if(xauthDeptList.size() != 0) {
		
			for(XauthDept xauthDept : xauthDeptList) {
				QueryWrapper<DeptInfoExt> queryWrapper = new QueryWrapper<DeptInfoExt>();
				queryWrapper.eq("IDEN_ID", xauthDept.getIdenId());
				 List<DeptInfoExt> deptInfoExtDbList = deptInfoExtMapper.selectList(queryWrapper);
				 deptInfoExtList.add(deptInfoExtDbList.get(0));
			}
			
			if(deptInfoExtList.size() == 0 ) {
				result.setStatus(ProcessResult.NG);
				result.addMessage("查無使用者上階層部門主管角色資料");
				return result;
			}
		}
		
		//法務
		QueryWrapper<Systemparam> systemparamQueryWrapper = new QueryWrapper<Systemparam>();
		systemparamQueryWrapper.eq("DEPTNO", section);
		systemparamQueryWrapper.eq("CONTRACTMODEL", module);
		systemparamQueryWrapper.eq("ACTIONTYPE", "02");
		systemparamList = systemparamMapper.selectList(systemparamQueryWrapper);
		
		if(systemparamList.size() == 0) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("查無該課別合約法務簽章人員");
			return result;
		}
		
		long row = 1;
		int toRow = 2;
		int toBackRow = 0;
		
		Flowstep flowstep = new Flowstep();
		flowstep.setSteptype("C");
		flowstep.setFlowid(flowid);
		flowstep.setStepid("Task" + row);
		flowstep.setStepname("申請人");
		flowstep.setStepdesc("案件申請");
		flowstep.setDispord(row);
		flowstep.setUserids(applicantid);
		// 改成以roleids寫入
		flowstep.setDeptid(idenId);
//		flowstep.setRoleids(getRoleids(userInfo.getIdenId(), userInfo.getUserId()));
		flowstepList.add(flowstep);
		
		FlowStepLink flowStepLink = new FlowStepLink();
		flowStepLink.setFlowId(flowid);
		flowStepLink.setStepId("Task" + row);
		flowStepLink.setToStepId("Task" + toRow);
		flowStepLink.setAction("Apply");
		flowStepLink.setIsConcurrent(false);
		flowStepLink.setName("送審");
		flowStepLink.setDispOrd(10);
		flowStepLinkList.add(flowStepLink);
		
		flowStepLink = new FlowStepLink();
		flowStepLink.setFlowId(flowid);
		flowStepLink.setStepId("Task" + row);
		flowStepLink.setToStepId("Cancel");
		flowStepLink.setAction("Cancel");
		flowStepLink.setIsConcurrent(false);
		flowStepLink.setName("作廢");
		flowStepLink.setDispOrd(20);
		flowStepLinkList.add(flowStepLink);
		row++;
		toRow++;
		toBackRow++;
		
		flowstep = new Flowstep();
		flowstep.setSteptype("C");
		flowstep.setFlowid(flowid);
		flowstep.setStepid("Task" + row);
		flowstep.setStepname("供應商");
		flowstep.setStepdesc("供應商審核");
		flowstep.setDispord(row);
		flowstep.setUserids(suppliermaster.getSupplierid());
		flowstep.setDeptid(suppliermaster.getIdenid());

		flowstepList.add(flowstep);
		
		flowStepLink = new FlowStepLink();
		flowStepLink.setFlowId(flowid);
		flowStepLink.setStepId("Task" + row);
		flowStepLink.setToStepId("Task" + toRow);
		flowStepLink.setAction("Approve");
		flowStepLink.setIsConcurrent(false);
		flowStepLink.setName("送審");
		flowStepLink.setDispOrd(10);
		flowStepLinkList.add(flowStepLink);
		
		flowStepLink = new FlowStepLink();
		flowStepLink.setFlowId(flowid);
		flowStepLink.setStepId("Task" + row);
		flowStepLink.setToStepId("Task" + toBackRow);
		flowStepLink.setAction("Return");
		flowStepLink.setIsConcurrent(false);
		flowStepLink.setName("退回");
		flowStepLink.setDispOrd(20);
		flowStepLinkList.add(flowStepLink);
		row++;
		toRow++;
		toBackRow++;
		
		//修改
		int size = xauthDeptList.size();
		for(int i = 0 ; i < size ; i++) {
			String roleids = getRoleids(xauthDeptList.get(i).getIdenId(), getDeptUserId(xauthDeptList.get(i).getIdenId(), deptInfoExtList));
			if(i == 0) {// && StringUtils.equals(userInfo.getUserId(), userId)) {
				continue;
			}
			
			flowstep = new Flowstep();
			flowstep.setSteptype("C");
			flowstep.setFlowid(flowid);
			flowstep.setStepid("Task" + row);
			flowstep.setStepname(xauthDeptList.get(i).getCname());
			flowstep.setStepdesc(xauthDeptList.get(i).getCname() + "審核");
			flowstep.setDispord(row);
//			flowstep.setUserids(userId);
			//測試
			flowstep.setRoleids(roleids);
			flowstep.setDeptid(xauthDeptList.get(i).getIdenId());
			flowstepList.add(flowstep);
			//非部門階層最後一筆
			if(!((size - 1) == i)) {
				
				flowStepLink = new FlowStepLink();
				flowStepLink.setFlowId(flowid);
				flowStepLink.setStepId("Task" + row);
				flowStepLink.setToStepId("Task" + toRow);
				flowStepLink.setAction("Approve");
				flowStepLink.setIsConcurrent(false);
				flowStepLink.setName("送審");
				flowStepLink.setDispOrd(10);
				flowStepLinkList.add(flowStepLink);
				
				flowStepLink = new FlowStepLink();
				flowStepLink.setFlowId(flowid);
				flowStepLink.setStepId("Task" + row);
				
				//如果上一階層為供應商必須退回到供應商前一關
				if(row == 3) {
					flowStepLink.setToStepId("Task1");
				}else {
					flowStepLink.setToStepId("Task" + toBackRow);
				}
				
				flowStepLink.setAction("Return");
				flowStepLink.setIsConcurrent(false);
				flowStepLink.setName("退回");
				flowStepLink.setDispOrd(20);
				flowStepLinkList.add(flowStepLink);
				row++;
				toRow++;
				toBackRow++;
			}else {
				//部門階層最後一筆
				if(systemparamList.size() == 0) {
					flowStepLink = new FlowStepLink();
					flowStepLink.setFlowId(flowid);
					flowStepLink.setStepId("Task" + row);
					flowStepLink.setToStepId("End");
					flowStepLink.setAction("Approve");
					flowStepLink.setIsConcurrent(false);
					flowStepLink.setName("送審");
					flowStepLink.setDispOrd(10);
					flowStepLinkList.add(flowStepLink);
					
					flowStepLink = new FlowStepLink();
					flowStepLink.setFlowId(flowid);
					flowStepLink.setStepId("Task" + row);
					flowStepLink.setToStepId("Task" + toBackRow);
					flowStepLink.setAction("Return");
					flowStepLink.setIsConcurrent(false);
					flowStepLink.setName("退回");
					flowStepLink.setDispOrd(20);
					flowStepLinkList.add(flowStepLink);
				}else {
					//部門階層最後一筆
					if((size - 1) == i) {
						flowStepLink = new FlowStepLink();
						flowStepLink.setFlowId(flowid);
						flowStepLink.setStepId("Task" + row);
						flowStepLink.setToStepId("Task" + toRow);
						flowStepLink.setAction("Approve");
						flowStepLink.setIsConcurrent(false);
						flowStepLink.setName("送審");
						flowStepLink.setDispOrd(10);
						flowStepLinkList.add(flowStepLink);
						
						flowStepLink = new FlowStepLink();
						flowStepLink.setFlowId(flowid);
						flowStepLink.setStepId("Task" + row);
						flowStepLink.setToStepId("Task" + toBackRow);
						flowStepLink.setAction("Return");
						flowStepLink.setIsConcurrent(false);
						flowStepLink.setName("退回");
						flowStepLink.setDispOrd(20);
						flowStepLinkList.add(flowStepLink);
						row++;
						toRow++;
						toBackRow++;
					}
					//法務
					for(int j = 0 ; j < systemparamList.size() ; j ++) {
						flowstep = new Flowstep();
						flowstep.setSteptype("C");
						flowstep.setFlowid(flowid);
						flowstep.setStepid("Task" + row);
						flowstep.setStepname("法務");
						flowstep.setStepdesc("法務審核");
						flowstep.setDispord(row);
						if(StringUtils.isNoneBlank(systemparamList.get(j).getIdenid())) {
						flowstep.setDeptid(systemparamList.get(j).getIdenid());
						}
						if(StringUtils.isNoneBlank(systemparamList.get(j).getUserids())) {
							flowstep.setUserids(systemparamList.get(j).getUserids());
						}
						if(StringUtils.isNotBlank(systemparamList.get(j).getRoleids())) {
							flowstep.setRoleids(systemparamList.get(j).getRoleids());
						}
						
						flowstepList.add(flowstep);
						
						//非法務最後一筆
						if(!((systemparamList.size() - 1) == j)){
							flowStepLink = new FlowStepLink();
							flowStepLink.setFlowId(flowid);
							flowStepLink.setStepId("Task" + row);
							flowStepLink.setToStepId("Task" + toRow);
							flowStepLink.setAction("Approve");
							flowStepLink.setIsConcurrent(false);
							flowStepLink.setName("送審");
							flowStepLink.setDispOrd(10);
							flowStepLinkList.add(flowStepLink);
							
							flowStepLink = new FlowStepLink();
							flowStepLink.setFlowId(flowid);
							flowStepLink.setStepId("Task" + row);
							flowStepLink.setToStepId("Task" + toBackRow);
							flowStepLink.setAction("Return");
							flowStepLink.setIsConcurrent(false);
							flowStepLink.setName("退回");
							flowStepLink.setDispOrd(20);
							flowStepLinkList.add(flowStepLink);
							row++;
							toRow++;
							toBackRow++;
						}else {
							//法務最後一筆
							flowStepLink = new FlowStepLink();
							flowStepLink.setFlowId(flowid);
							flowStepLink.setStepId("Task" + row);
							flowStepLink.setToStepId("End");
							flowStepLink.setAction("Approve");
							flowStepLink.setIsConcurrent(false);
							flowStepLink.setName("送審");
							flowStepLink.setDispOrd(10);
							flowStepLinkList.add(flowStepLink);
							
							flowStepLink = new FlowStepLink();
							flowStepLink.setFlowId(flowid);
							flowStepLink.setStepId("Task" + row);
							flowStepLink.setToStepId("Task" + toBackRow);
							flowStepLink.setAction("Return");
							flowStepLink.setIsConcurrent(false);
							flowStepLink.setName("退回");
							flowStepLink.setDispOrd(20);
							flowStepLinkList.add(flowStepLink);
						}
					}
				}
			}
		}
		
		//儲存 FlowSetup
		for(Flowstep flowstepToDb : flowstepList) {
			setFlowSetup(flowstepToDb.getSteptype(), flowstepToDb.getFlowid(), flowstepToDb.getStepid(), flowstepToDb.getStepname(), flowstepToDb.getStepdesc(), Long.toString(flowstepToDb.getDispord()), flowstepToDb.getDeptid(), flowstepToDb.getRoleids(), flowstepToDb.getUserids(), flowstepToDb.getSubflowid());
		}
		
		//儲存 FlowStepLink
		for(FlowStepLink flowStepLinkToDb : flowStepLinkList) {
			setFlowStepLink(flowStepLinkToDb.getFlowId(), flowStepLinkToDb.getStepId(), flowStepLinkToDb.getToStepId(), flowStepLinkToDb.getAction(), Boolean.toString(flowStepLinkToDb.getIsConcurrent()), flowStepLinkToDb.getName(), flowStepLinkToDb.getDesc(), Integer.toString(flowStepLinkToDb.getDispOrd()));
		}
		
		//儲存 Flowconf
		QueryWrapper<Flowconf> queryWrapper = new QueryWrapper<Flowconf>();
		queryWrapper.eq("FLOWID", reviewconf.getFlowid());
		queryWrapper.eq("FLOWVERSION", reviewconf.getFlowversion());
		List<Flowconf> flowconfList = flowconfMapper.selectList(queryWrapper);
		Flowconf flowconfForDb = flowconfList.get(0);
		flowconfForDb.setSerno(null);
		flowconfForDb.setFlowid(flowid);
		logger.info("Flow == " + flowconfForDb.toString());
		flowconfMapper.insert(flowconfForDb);
		
		//產 Flow 流程 XML
		CreateUserDto dto = new CreateUserDto();
		Date now = new Date();
		dto.setCreateUserId(userInfo.getUserId());
		dto.setCreateTime(now);
		dto.setUpdateUserId(userInfo.getUserId());
		dto.setUpdateTime(now);
		dto.setCreateAgentId("");
		dto.setUpdateAgentId("");
		genflowXmlforFlowConfDb(flowid, dto);
		
		//建立表單
		FormConf formConf = formConfRepository.getEntityByProperty("formId", flowid);
		//沒有表單資料,建立對應表單資料
		if(null==formConf || StringUtils.isEmpty(formConf.getFormId())) {
			formConf = new FormConf();
			formConf.setFormId(flowid);
			formConf.setFormUrl("/contract/form" + flowid);
			formConf.setName(flowname);
			formConf.setSysId(APP_ID);
			formConf.setCreateUserId(userInfo.getUserId());
			formConf.setCreateTime(now);
			formConf.setUpdateUserId(userInfo.getUserId());
			formConf.setUpdateTime(now);
			formConfRepository.save(formConf);
		}
		
		result.setStatus(ProcessResult.OK);
		result.addMessage("制式合約流程建立成功");
		return result;
	}
	
	/**
	 * Flow 前置設定處理 NSC
	 * @param params
	 * @param reviewconf
	 * @param suppliermaster
	 */
	public ProcessResult setNSCFlowStepAndLink(Map<String, Object> params, Reviewconf reviewconf, Suppliermaster suppliermaster) throws Exception {
		ProcessResult result = new ProcessResult();
		String module = MapUtils.getString(params, "module");
		String flowname = MapUtils.getString(params, "dispName");
		String flowid = MapUtils.getString(params, "flowid");
		String idenId = MapUtils.getString(params, "idenId");
		String applicantid = MapUtils.getString(params, "applicantid");
		UserInfo userInfo = userContext.getCurrentUser();
		Flowstep flowstep = null;
		FlowStepLink flowStepLink = null;
		List<XauthDept> xauthDeptList = getAllDeptList(null, idenId, idenId);
		List<Flowstep> flowstepList = new ArrayList<Flowstep>();
		List<FlowStepLink> flowStepLinkList = new ArrayList<FlowStepLink>();
		List<DeptInfoExt> deptInfoExtList = new ArrayList<DeptInfoExt>();
		List<Systemparam> systemparamList = new ArrayList<Systemparam>();
		List<Map<String, Object>> reviewsetdataconfList = new ArrayList<Map<String, Object>>();
		
		if(xauthDeptList.size() == 0) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("查無使用者上階層部門");
			return result;
		}
		
		if(reviewconf == null) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("查詢合約發生錯誤");
			return result;
		}
		
		if(suppliermaster == null) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("查無供應商資料");
			return result;
		}
		
		//刪除總經理
		xauthDeptList.remove(xauthDeptList.size() - 1);
		
		//刪除組織主管
		if(StringUtils.equals("N", reviewconf.getIsorgreview())) {
			int size = xauthDeptList.size();
			xauthDeptList.remove(size - 1);
		}
		
		//刪除單位主管
		if(StringUtils.equals("N", reviewconf.getIsdeptreview())) {
			if(StringUtils.equals("Y", reviewconf.getIsorgreview())) {
				int size = xauthDeptList.size();
				xauthDeptList.remove(size - 1);
				XauthDept xauthDept = xauthDeptList.get(size - 1);
				xauthDeptList = new ArrayList<XauthDept>();
				xauthDeptList.add(xauthDept);
			}else {
				int size = xauthDeptList.size();
				for(int i = size - 1; 0 <= i; i--) {
					xauthDeptList.remove(i);
				}
			}
		}
		
		//部門階層
		if(xauthDeptList.size() != 0) {
		
			for(XauthDept xauthDept : xauthDeptList) {
				QueryWrapper<DeptInfoExt> queryWrapper = new QueryWrapper<DeptInfoExt>();
				queryWrapper.eq("IDEN_ID", xauthDept.getIdenId());
				 List<DeptInfoExt> deptInfoExtDbList = deptInfoExtMapper.selectList(queryWrapper);
				 deptInfoExtList.add(deptInfoExtDbList.get(0));
			}
			
			if(deptInfoExtList.size() == 0 ) {
				result.setStatus(ProcessResult.NG);
				result.addMessage("查無使用者上階層部門主管角色資料-001");
				return result;
			}
		}
		
		boolean systemparam01 = true;//法務審核
		boolean systemparam02 = true;//法務簽章
		List<Systemparam> systemparamResult = new ArrayList<Systemparam>();
		//法務審查
		QueryWrapper<Systemparam> systemparamQueryWrapper = new QueryWrapper<Systemparam>();
//		systemparamQueryWrapper.eq("CONTRACTMODEL", module).eq("ACTIONTYPE", "01").like("IDENIDS", userInfo.getIdenId());
		systemparamQueryWrapper.eq("CONTRACTMODEL", module).eq("ACTIONTYPE", "01").like("IDENIDS", idenId);
		systemparamQueryWrapper.or().eq("CONTRACTMODEL", module).eq("ACTIONTYPE", "01").eq("FLOWID", reviewconf.getFlowid());
		systemparamQueryWrapper.orderByDesc("FLOWID").orderByDesc("UPDATEDATE");
		systemparamList = systemparamMapper.selectList(systemparamQueryWrapper);
		if(systemparamList.size() > 0) {
			systemparamResult.add(systemparamList.get(0));
			systemparam01 = false;
		}
		
		systemparamQueryWrapper = new QueryWrapper<Systemparam>();
		systemparamQueryWrapper.eq("CONTRACTMODEL", module).eq("ACTIONTYPE", "02").like("IDENIDS", idenId);
		systemparamQueryWrapper.or().eq("CONTRACTMODEL", module).eq("ACTIONTYPE", "02").eq("FLOWID", reviewconf.getFlowid());
		systemparamQueryWrapper.orderByDesc("FLOWID").orderByDesc("UPDATEUSER");
		systemparamList = systemparamMapper.selectList(systemparamQueryWrapper);
		if(systemparamList.size() > 0) {
			systemparamResult.add(systemparamList.get(0));
			systemparam02 = false;
		}
		
		//勾選前置流程必須檢核該合約的法務審核人員
		if(StringUtils.equals("Y", reviewconf.getIsprereview())) {
			if(systemparam01) {
				result.setStatus(ProcessResult.NG);
				result.addMessage("查無該流程合約法務審查人員-01");
				return result;
			}
		}
		//法務簽章必要檢核
		if(systemparam02) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("查無該流程合約法務簽章人員-02");
			return result;
		}
		
		//勾選欄位
		boolean isSignUserBlank = false;
		Map<String, Object> reviewsetdataconfMap = new HashMap<String, Object>();
		reviewsetdataconfMap.put("flowid", reviewconf.getFlowid());
		reviewsetdataconfList = reviewsetdataconfMapper.selectFlowData(reviewsetdataconfMap);
		if(reviewsetdataconfList.size() == 0) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("查詢該合約應勾選欄位發生錯誤-001");
			return result;
		}
		
		// 檢核合約流程所有關卡都有簽核人員
		for(Map<String, Object> reviewsetdataconfListMap : reviewsetdataconfList) {
			if(StringUtils.isBlank(MapUtils.getString(reviewsetdataconfListMap, "REVIEWUSERIDS")) && StringUtils.isBlank(MapUtils.getString(reviewsetdataconfListMap, "REVIEWROLES"))) {
				result.setStatus(ProcessResult.NG);
				result.addMessage("關卡 : " + MapUtils.getString(reviewsetdataconfListMap, "REVIEWNAME") + "，查無此關簽核人員\r\n");
				isSignUserBlank = true;
			}
		}
		if(isSignUserBlank) {
			return result;
		}
		
		if(reviewsetdataconfList.size() != 0) {
			reviewsetdataconfList = sortOutReviewSetDataList(reviewsetdataconfList, null, 1);
			if(reviewsetdataconfList == null) {
				result.setStatus(ProcessResult.NG);
				result.addMessage("查詢該合約應勾選欄位發生錯誤-002");
				return result;
			}
		}
		
		QueryWrapper<Reviewsetdataconf> reviewsetdataconfWrapper = new QueryWrapper<Reviewsetdataconf>();
		reviewsetdataconfWrapper.eq("flowid", reviewconf.getFlowid());
		List<Reviewsetdataconf> reviewsetdataconfs = reviewsetdataconfMapper.selectList(reviewsetdataconfWrapper);
		if(reviewsetdataconfs.size() != reviewsetdataconfList.size()) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("審核關卡發生錯誤");
			return result;
		}
		
		long row = 1;
		int toRow = 2;
		int toBackRow = 0;
		
		//申請人
		flowstep = new Flowstep();
		flowstep.setSteptype("C");
		flowstep.setFlowid(flowid);
		flowstep.setStepid("Task" + row);
		flowstep.setStepname("申請人");
		flowstep.setStepdesc("案件申請");
		flowstep.setDispord(row);
		//改成以roleids寫入
		flowstep.setUserids(applicantid);
		flowstep.setDeptid(idenId);
//		flowstep.setRoleids(getRoleids(userInfo.getIdenId(), userInfo.getUserId()));
		flowstepList.add(flowstep);
		
		flowStepLink = new FlowStepLink();
		flowStepLink.setFlowId(flowid);
		flowStepLink.setStepId("Task" + row);
		flowStepLink.setToStepId("Task" + toRow);
		flowStepLink.setAction("Apply");
		flowStepLink.setIsConcurrent(false);
		flowStepLink.setName("送審");
		flowStepLink.setDispOrd(10);
		flowStepLinkList.add(flowStepLink);
		
		flowStepLink = new FlowStepLink();
		flowStepLink.setFlowId(flowid);
		flowStepLink.setStepId("Task" + row);
		flowStepLink.setToStepId("Cancel");
		flowStepLink.setAction("Cancel");
		flowStepLink.setIsConcurrent(false);
		flowStepLink.setName("作廢");
		flowStepLink.setDispOrd(20);
		flowStepLinkList.add(flowStepLink);
		row++;
		toRow++;
		toBackRow++;
		
		//法務審核
		if(systemparamResult.size() != 0) {
			for(Systemparam systemparam : systemparamResult) {
				if(StringUtils.equals("01", systemparam.getActiontype())) {
					flowstep = new Flowstep();
					flowstep.setSteptype("C");
					flowstep.setFlowid(flowid);
					flowstep.setStepid("Task" + row);
					flowstep.setStepname("法務");
					flowstep.setStepdesc("法務審核");
					flowstep.setDispord(row);
					if(StringUtils.isNoneBlank(systemparam.getIdenid())) {
						flowstep.setDeptid(systemparam.getIdenid());
					}
					if(StringUtils.isNoneBlank(systemparam.getUserids())) {
						flowstep.setUserids(systemparam.getUserids());
					}
					if(StringUtils.isNotBlank(systemparam.getRoleids())) {
						flowstep.setRoleids(systemparam.getRoleids());
					}
					
					flowstepList.add(flowstep);
					
					flowStepLink = new FlowStepLink();
					flowStepLink.setFlowId(flowid);
					flowStepLink.setStepId("Task" + row);
					flowStepLink.setToStepId("Task" + toRow);
					flowStepLink.setAction("Approve");
					flowStepLink.setIsConcurrent(false);
					flowStepLink.setName("送審");
					flowStepLink.setDispOrd(10);
					flowStepLinkList.add(flowStepLink);
					
					flowStepLink = new FlowStepLink();
					flowStepLink.setFlowId(flowid);
					flowStepLink.setStepId("Task" + row);
					flowStepLink.setToStepId("Task" + toBackRow);
					flowStepLink.setAction("Return");
					flowStepLink.setIsConcurrent(false);
					flowStepLink.setName("退回");
					flowStepLink.setDispOrd(20);
					flowStepLinkList.add(flowStepLink);
					row++;
					toRow++;
					toBackRow++;
				}
			}
		}
		
		//供應商
		flowstep = new Flowstep();
		flowstep.setSteptype("C");
		flowstep.setFlowid(flowid);
		flowstep.setStepid("Task" + row);
		flowstep.setStepname("供應商");
		flowstep.setStepdesc("供應商審核");
		flowstep.setDispord(row);
		flowstep.setUserids(suppliermaster.getSupplierid());
		flowstep.setDeptid(suppliermaster.getIdenid());
		flowstepList.add(flowstep);
		
		flowStepLink = new FlowStepLink();
		flowStepLink.setFlowId(flowid);
		flowStepLink.setStepId("Task" + row);
		flowStepLink.setToStepId("Task" + toRow);
		flowStepLink.setAction("Approve");
		flowStepLink.setIsConcurrent(false);
		flowStepLink.setName("送審");
		flowStepLink.setDispOrd(10);
		flowStepLinkList.add(flowStepLink);
		
		flowStepLink = new FlowStepLink();
		flowStepLink.setFlowId(flowid);
		flowStepLink.setStepId("Task" + row);
		flowStepLink.setToStepId("Task" + toBackRow);
		flowStepLink.setAction("Return");
		flowStepLink.setIsConcurrent(false);
		flowStepLink.setName("退回");
		flowStepLink.setDispOrd(20);
		flowStepLinkList.add(flowStepLink);
		row++;
		toRow++;
		toBackRow++;
		
		// 單位主管
		if(xauthDeptList.size() != 0) {
			int i = 1;
			for(XauthDept xauthDept : xauthDeptList) {
				String roleids="";
				String userId = getDeptUserId(xauthDept.getIdenId(), deptInfoExtList);
				if(row == 4 && StringUtils.equals(applicantid, userId)) {
					i++;
					continue;
				}
				//查詢Roleid
				logger.info("xauthDept.getIdenId() ==="+xauthDept.getIdenId()+"userId ==="+userId);
				roleids = getRoleids(xauthDept.getIdenId(), userId);
				flowstep = new Flowstep();
				flowstep.setSteptype("C");
				flowstep.setFlowid(flowid);
				flowstep.setStepid("Task" + row);
				flowstep.setStepname(xauthDept.getCname());
				if(StringUtils.equals("Y", reviewconf.getIsorgreview()) && i == xauthDeptList.size()) {
					flowstep.setStepdesc(xauthDept.getCname() + "審核(組)");
				}else {
					flowstep.setStepdesc(xauthDept.getCname() + "審核");
				}
				flowstep.setDispord(row);

				flowstep.setRoleids(roleids);
//				flowstep.setUserids(userId);

				flowstep.setDeptid(xauthDept.getIdenId());
				flowstepList.add(flowstep);
				
				flowStepLink = new FlowStepLink();
				flowStepLink.setFlowId(flowid);
				flowStepLink.setStepId("Task" + row);
				flowStepLink.setToStepId("Task" + toRow);
				flowStepLink.setAction("Approve");
				flowStepLink.setIsConcurrent(false);
				flowStepLink.setName("送審");
				flowStepLink.setDispOrd(10);
				flowStepLinkList.add(flowStepLink);
				
				flowStepLink = new FlowStepLink();
				flowStepLink.setFlowId(flowid);
				flowStepLink.setStepId("Task" + row);
				
				//如果上一階層為供應商必須退回到供應商前一關
				if(row == 3) {
					flowStepLink.setToStepId("Task1");
				}else if(row == 4){
					flowStepLink.setToStepId("Task2");
				}else {
					flowStepLink.setToStepId("Task" + toBackRow);
				}
				
				flowStepLink.setAction("Return");
				flowStepLink.setIsConcurrent(false);
				flowStepLink.setName("退回");
				flowStepLink.setDispOrd(20);
				flowStepLinkList.add(flowStepLink);
				row++;
				toRow++;
				toBackRow++;
				i++;
			}
		}else {
			result.setStatus(ProcessResult.NG);
			result.addMessage("查無使用者上階層部門主管角色資料-002");
			return result;
		}
		
		//勾選欄位
		if(reviewsetdataconfList.size() != 0) {
			//TODO 合約子流程（未完成) SubFlow
			
//			flowstep = new Flowstep();
//			flowstep.setSteptype("P");
//			flowstep.setFlowid(flowid);
//			flowstep.setStepid("Task" + row);
//			flowstep.setStepname("會簽");
//			flowstep.setStepdesc("會簽審核");
//			flowstep.setDispord(row);
//			flowstepList.add(flowstep);
//			
//			flowStepLink = new FlowStepLink();
//			flowStepLink.setFlowId(flowid);
//			flowStepLink.setStepId("Task" + row);
//			flowStepLink.setToStepId("Sub1");
//			flowStepLink.setAction("ParallelCountersign");
//			flowStepLink.setIsConcurrent(true);
//			flowStepLink.setName("送審");
//			flowStepLink.setDispOrd(10);
//			flowStepLinkList.add(flowStepLink);
//			
//			row++;
//			toRow++;
//			toBackRow++;
//			
//			setNSCSubFlowStepAndLink(reviewsetdataconfList, flowid, row, toBackRow);
			for(Map<String, Object> map : reviewsetdataconfList) {
				if(StringUtils.equals(MapUtils.getString(map, "REVIEWNAME"), "法律顧問")) {
					systemparamQueryWrapper = new QueryWrapper<Systemparam>();
//					systemparamQueryWrapper.eq("CONTRACTMODEL", module).eq("ACTIONTYPE", "03").like("IDENIDS", userInfo.getIdenId());
					systemparamQueryWrapper.eq("CONTRACTMODEL", module).eq("ACTIONTYPE", "03").like("IDENIDS", idenId);
					systemparamQueryWrapper.orderByDesc("FLOWID").orderByDesc("UPDATEDATE");
					systemparamList = systemparamMapper.selectList(systemparamQueryWrapper);
					if(systemparamList.size() > 0) {
						Systemparam systemparam = systemparamList.get(0);
						map.put("REVIEWROLES", systemparam.getRoleids());
						map.put("REVIEWUSERIDS", systemparam.getUserids());
						
					}
				}
				flowstep = new Flowstep();
				flowstep.setSteptype("C");
				flowstep.setFlowid(flowid);
				flowstep.setStepid("Task" + row);
				flowstep.setDeptid(MapUtils.getString(map, "IDENID"));
				flowstep.setStepname(MapUtils.getString(map, "REVIEWNAME"));				
				flowstep.setStepdesc(MapUtils.getString(map, "REVIEWNAME") + "審核");
				flowstep.setDispord(row);
				if(StringUtils.isNotBlank(MapUtils.getString(map, "REVIEWUSERIDS"))) {
					flowstep.setUserids(MapUtils.getString(map, "REVIEWUSERIDS"));
				}
				if(StringUtils.isNotBlank(MapUtils.getString(map, "REVIEWROLES"))) {
					flowstep.setRoleids(MapUtils.getString(map, "REVIEWROLES"));
				}
				
				flowstepList.add(flowstep);
				
				flowStepLink = new FlowStepLink();
				flowStepLink.setFlowId(flowid);
				flowStepLink.setStepId("Task" + row);
				flowStepLink.setToStepId("Task" + toRow);
				flowStepLink.setAction("Approve");
				flowStepLink.setIsConcurrent(false);
				flowStepLink.setName("送審");
				flowStepLink.setDispOrd(10);
				flowStepLinkList.add(flowStepLink);
				
				flowStepLink = new FlowStepLink();
				flowStepLink.setFlowId(flowid);
				flowStepLink.setStepId("Task" + row);
				flowStepLink.setToStepId("Task" + toBackRow);
				flowStepLink.setAction("Return");
				flowStepLink.setIsConcurrent(false);
				flowStepLink.setName("退回");
				flowStepLink.setDispOrd(20);
				flowStepLinkList.add(flowStepLink);
				row++;
				toRow++;
				toBackRow++;
			}
		}else {
			result.setStatus(ProcessResult.NG);
			result.addMessage("查詢該合約應勾選欄位發生錯誤-003");
			return result;
		}
		
		//法務簽章
		if(systemparamResult.size() != 0) {
			for(Systemparam systemparam : systemparamResult) {
				if(StringUtils.equals("02", systemparam.getActiontype())) {
					flowstep = new Flowstep();
					flowstep.setSteptype("C");
					flowstep.setFlowid(flowid);
					flowstep.setStepid("Task" + row);
					flowstep.setStepname("法務");
					flowstep.setStepdesc("法務簽章");
					flowstep.setDispord(row);
					if(StringUtils.isNoneBlank(systemparam.getIdenid())) {
						flowstep.setDeptid(systemparam.getIdenid());
					}
					if(StringUtils.isNoneBlank(systemparam.getUserids())) {
						flowstep.setUserids(systemparam.getUserids());
					}
					if(StringUtils.isNotBlank(systemparam.getRoleids())) {
						flowstep.setRoleids(systemparam.getRoleids());
					}
					
					flowstepList.add(flowstep);
					
					flowStepLink = new FlowStepLink();
					flowStepLink.setFlowId(flowid);
					flowStepLink.setStepId("Task" + row);
					flowStepLink.setToStepId("End");
					flowStepLink.setAction("Approve");
					flowStepLink.setIsConcurrent(false);
					flowStepLink.setName("送審");
					flowStepLink.setDispOrd(10);
					flowStepLinkList.add(flowStepLink);
					
					flowStepLink = new FlowStepLink();
					flowStepLink.setFlowId(flowid);
					flowStepLink.setStepId("Task" + row);
					flowStepLink.setToStepId("Task" + toBackRow);
					flowStepLink.setAction("Return");
					flowStepLink.setIsConcurrent(false);
					flowStepLink.setName("退回");
					flowStepLink.setDispOrd(20);
					flowStepLinkList.add(flowStepLink);
				}
			}
		}else {
			result.setStatus(ProcessResult.NG);
			result.addMessage("查無該課別法務簽章人員-002");
			return result;
		}
		
		//儲存 FlowSetup
		for(Flowstep flowstepToDb : flowstepList) {
			setFlowSetup(flowstepToDb.getSteptype(), flowstepToDb.getFlowid(), flowstepToDb.getStepid(), flowstepToDb.getStepname(), flowstepToDb.getStepdesc(), Long.toString(flowstepToDb.getDispord()), flowstepToDb.getDeptid(), flowstepToDb.getRoleids(), flowstepToDb.getUserids(), flowstepToDb.getSubflowid());
		}

		//儲存 FlowStepLink
		for(FlowStepLink flowStepLinkToDb : flowStepLinkList) {
			setFlowStepLink(flowStepLinkToDb.getFlowId(), flowStepLinkToDb.getStepId(), flowStepLinkToDb.getToStepId(), flowStepLinkToDb.getAction(), Boolean.toString(flowStepLinkToDb.getIsConcurrent()), flowStepLinkToDb.getName(), flowStepLinkToDb.getDesc(), Integer.toString(flowStepLinkToDb.getDispOrd()));
		}
		
		//儲存 Flowconf
		QueryWrapper<Flowconf> queryWrapper = new QueryWrapper<Flowconf>();
		queryWrapper.eq("FLOWID", reviewconf.getFlowid());
		queryWrapper.eq("FLOWVERSION", reviewconf.getFlowversion());
		List<Flowconf> flowconfList = flowconfMapper.selectList(queryWrapper);
		Flowconf flowconfForDb = flowconfList.get(0);
		flowconfForDb.setSerno(null);
		flowconfForDb.setFlowid(flowid);
		logger.info("Flow == " + flowconfForDb.toString());
		flowconfMapper.insert(flowconfForDb);
		
		//產 Flow 流程 XML
		CreateUserDto dto = new CreateUserDto();
		Date now = new Date();
		dto.setCreateUserId(userInfo.getUserId());
		dto.setCreateTime(now);
		dto.setUpdateUserId(userInfo.getUserId());
		dto.setUpdateTime(now);
		dto.setCreateAgentId("");
		dto.setUpdateAgentId("");
		genflowXmlforFlowConfDb(flowid, dto);
		
		//建立表單
		FormConf formConf = formConfRepository.getEntityByProperty("formId", flowid);
		//沒有表單資料,建立對應表單資料
		if(null == formConf || StringUtils.isEmpty(formConf.getFormId())) {
			formConf = new FormConf();
			formConf.setFormId(flowid);
			formConf.setFormUrl("/contract/form" + flowid);
			formConf.setName(flowname);
			formConf.setSysId(APP_ID);
			formConf.setCreateUserId(userInfo.getUserId());
			formConf.setCreateTime(now);
			formConf.setUpdateUserId(userInfo.getUserId());
			formConf.setUpdateTime(now);
			formConfRepository.save(formConf);
		}
		
		result.setStatus(ProcessResult.OK);
		result.addMessage("制式合約流程建立成功");
		return result;
	}
	
	/**
	 * 合約子流程（未完成）
	 * @param reviewsetdataconfList
	 * @param flowid
	 * @param row
	 * @param toBackRow
	 * @return
	 */
	public ProcessResult setNSCSubFlowStepAndLink(List<Map<String, Object>> reviewsetdataconfList, String flowid, long row, int toBackRow) {
		//TODO 合約子流程（未完成) SubFlow
		ProcessResult result = new ProcessResult();
		Flowstep flowstep = null;
		FlowStepLink flowStepLink = null;
		Subflowconf subflowconf = null;
		List<Flowstep> flowstepList = new ArrayList<Flowstep>();
		List<FlowStepLink> flowStepLinkList = new ArrayList<FlowStepLink>();
		List<Subflowconf> subflowconfList = new ArrayList<Subflowconf>();
		
		long subTaskRow = 1;
		int subTaskBackRow = 0;
		int subIdRow = 1;
		
		String subFlowId = "sub" + subIdRow;
		
		subflowconf = new Subflowconf();
		subflowconf.setFlowid(flowid);
		subflowconf.setSubflowid(subFlowId);
		subflowconf.setSubflowname("會簽子流程");
		subflowconf.setSubflowdesc("會簽");
		subflowconf.setFinishto("Task" + row);
		subflowconf.setReturnto("Task" + toBackRow);
		subflowconfList.add(subflowconf);
		
		if(reviewsetdataconfList.size() != 0) {
			for(Map<String, Object> map : reviewsetdataconfList) {
				String actionType = MapUtils.getString(map, "ACTIONTYPE");
				if(StringUtils.equals("00", actionType)) {
					
					flowstep = new Flowstep();
					flowstep.setSteptype("C");
					flowstep.setFlowid(flowid);
					flowstep.setStepid(subFlowId + "Task" + subTaskRow);
					flowstep.setStepname(MapUtils.getString(map, "REVIEWNAME"));
					flowstep.setStepdesc(MapUtils.getString(map, "REVIEWNAME") + "審核");
					flowstep.setDispord(subTaskRow);
					flowstep.setSubflowid(subFlowId);
					if(StringUtils.isNotBlank(MapUtils.getString(map, "REVIEWUSERIDS"))) {
						flowstep.setUserids(MapUtils.getString(map, "REVIEWUSERIDS"));
					}
					if(StringUtils.isNotBlank(MapUtils.getString(map, "REVIEWROLES"))) {
						flowstep.setRoleids(MapUtils.getString(map, "REVIEWROLES"));
					}
					flowstepList.add(flowstep);
					
					flowStepLink = new FlowStepLink();
					flowStepLink.setFlowId(flowid);
					flowStepLink.setStepId(subFlowId + "Task" + subTaskRow);
					flowStepLink.setToStepId("Sub1Finish");
					flowStepLink.setAction("Approve");
					flowStepLink.setIsConcurrent(false);
					flowStepLink.setName("送審");
					flowStepLink.setDispOrd(10);
					flowStepLinkList.add(flowStepLink);
					
					flowStepLink = new FlowStepLink();
					flowStepLink.setFlowId(flowid);
					flowStepLink.setStepId(subFlowId + "Task" + subTaskBackRow);
					flowStepLink.setToStepId("Sub1Return");
					flowStepLink.setAction("Return");
					flowStepLink.setIsConcurrent(false);
					flowStepLink.setName("退回");
					flowStepLink.setDispOrd(20);
					flowStepLinkList.add(flowStepLink);
					
					subTaskRow++;
					subTaskBackRow++;
				}
			}	
		}
		
		for(Flowstep flowstepToDb : flowstepList) {
			setFlowSetup(flowstepToDb.getSteptype(), flowstepToDb.getFlowid(), flowstepToDb.getStepid(), flowstepToDb.getStepname(), flowstepToDb.getStepdesc(), Long.toString(flowstepToDb.getDispord()), flowstepToDb.getDeptid(), flowstepToDb.getRoleids(), flowstepToDb.getUserids(), flowstepToDb.getSubflowid());
		}
		
		for(Subflowconf subflowconfToDb : subflowconfList) {
			setSubFlowConf(subflowconfToDb.getFlowid(), subflowconfToDb.getSubflowid(), subflowconfToDb.getSubflowname(), subflowconfToDb.getSubflowdesc(), subflowconfToDb.getFinishto(), subflowconfToDb.getReturnto());
		}
		
		for(FlowStepLink flowStepLinkToDb : flowStepLinkList) {
			setFlowStepLink(flowStepLinkToDb.getFlowId(), flowStepLinkToDb.getStepId(), flowStepLinkToDb.getToStepId(), flowStepLinkToDb.getAction(), Boolean.toString(flowStepLinkToDb.getIsConcurrent()), flowStepLinkToDb.getName(), flowStepLinkToDb.getDesc(), Integer.toString(flowStepLinkToDb.getDispOrd()));
		}
		
		return result;
	}
	
	/**
	 * 將取得的勾選欄位做排序回傳
	 * @param reviewsetdataconfList
	 * @param resultReviewSetDataList
	 * @param j
	 * @return
	 */
	public List<Map<String, Object>> sortOutReviewSetDataList(List<Map<String, Object>> reviewsetdataconfList, List<Map<String, Object>> resultReviewSetDataList, int j) {
		int size = reviewsetdataconfList.size();
		
		if(resultReviewSetDataList == null) {
			resultReviewSetDataList = new ArrayList<Map<String, Object>>();
			logger.info("SortOutReviewSetDataList Is Start !!!! ");
		}
		
		if(size != 0) {
			for(int i = 0 ; i < size ; i++) {
				if(StringUtils.isNotBlank(MapUtils.getString(reviewsetdataconfList.get(i), "IDENIDS"))) {
					continue;
				}else {
					resultReviewSetDataList.add(reviewsetdataconfList.get(i));
				}
			}
			return resultReviewSetDataList;
		}else {
			logger.info("SortOutReviewSetDataList Is Finish !!!! ");
			return resultReviewSetDataList;
		}
	}
	
	/**
	 * 取得部門階層 UserId
	 * @param deptCnameList
	 * @param nextIdenid
	 * @return
	 */
	public String getDeptUserId(String idenId, List<DeptInfoExt> deptInfoExtList) {
		for(DeptInfoExt deptInfoExt : deptInfoExtList) {
			if(StringUtils.equals(idenId, deptInfoExt.getIdenId())) {
				return deptInfoExt.getDirectManager();
			}
		}
		return null;
	}
	
	/**
	 * 取得當前使用者 部門所有階層Bean
	 * @param deptCnameList
	 * @param nextIdenid
	 * @return
	 */
	public List<XauthDept> getAllDeptList(List<XauthDept> deptCnameList, String nextIdenid, String firstIdenid){
		
		if(deptCnameList == null) {
			deptCnameList = new ArrayList<XauthDept>();
		}
		
		QueryWrapper<XauthDept> queryWrapper = new QueryWrapper<XauthDept>();
		queryWrapper.eq("IDEN_ID", nextIdenid);
		List<XauthDept> xauthDeptList = xauthDeptMapper.selectList(queryWrapper);
		String parentId = xauthDeptList.get(0).getParentId();
		String idenId = xauthDeptList.get(0).getIdenId();
		
		if(!StringUtils.equals("#", parentId)) {
			if(StringUtils.equals(firstIdenid, idenId)) {
				deptCnameList.add(xauthDeptList.get(0));
			}
			if(!StringUtils.equals(firstIdenid, idenId)) {
				deptCnameList.add(xauthDeptList.get(0));
			}
			return getAllDeptList(deptCnameList, parentId, firstIdenid);
		}else {
			return deptCnameList;
		}
	}
	
	/**
	 * 產生DB XML (JPA)
	 * @param flowId
	 */
	public void genflowXmlforFlowConfDb(String flowId, CreateUserDto dto) {
		
		try {
			if (! flowAdminService.hasFlowSteps(flowId)) {
				//產生XML前，請先新增關卡及連結！
				System.out.println("產生XML前，請先新增關卡及連結！");
			}
			if (flowAdminService.isFlowXmlShouldBeUpdated(flowId)) {
				FlowConfDto flowDto = flowAdminService.getFlowConfByFlowId(flowId);
				flowAdminService.addFlowXml(flowDto, null, dto, false);
				System.out.println("gen dbXML done");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 儲存 FlowStep (JPA)
	 * @param steptype
	 * @param flowId
	 * @param stepId
	 * @param stepName
	 * @param stepDesc
	 * @param dispOrd
	 * @param deptId
	 * @param roleIds
	 * @param userIds
	 */
	@SuppressWarnings("unlikely-arg-type")
	public void setFlowSetup(String steptype,String flowId, String stepId
			,  String stepName, String stepDesc, String dispOrd, String deptId, String roleIds, String userIds
			,  String subFlowId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("stepType", (String)steptype);
		params.put("flowId", (String)flowId);
		params.put("stepId", (String)stepId);

		//FlowStep
		List<FlowStep>  serchflowSteps = flowStepRepository.findEntityListByJPQL("SELECT fs FROM FlowStep fs WHERE fs.stepType = ?1 AND fs.flowId = ?2  AND fs.stepId = ?3 ",params.get(0),params.get(1),params.get(2) );
		FlowStep flowStep= new FlowStep(flowId,stepId, steptype, stepName, stepDesc, Integer.valueOf(dispOrd), "SYSTEM");
		flowStep.setDeptId(deptId);
		flowStep.setRoleIds(roleIds);
		flowStep.setUserIds(userIds);
		flowStep.setSubFlowId(subFlowId);
		if(serchflowSteps!=null && !serchflowSteps.isEmpty()) {
			FlowStep sflowStep  =  serchflowSteps.get(0);
			BeanUtils.copyProperties(flowStep, sflowStep, "id");
			flowStepRepository.save(flowStep);
		}else {
			flowStepRepository.save(flowStep);
		}
	}
	/**
	 *  儲存 FlowStepLink (JPA)
	 * @param flowId
	 * @param stepId
	 * @param toStepId
	 * @param action
	 * @param isConcurrent
	 * @param linkName
	 * @param linkDesc
	 * @param dispOrddispOrd
	 */
	@SuppressWarnings("unlikely-arg-type")
	public void setFlowStepLink(String flowId, String stepId, String toStepId, String action, String isConcurrent, String linkName
			, String linkDesc
			, String dispOrd) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("flowId", (String)flowId);
		params.put("stepId", (String)stepId);
		
		//FlowStep
		List<FlowStepLink>  serchFlowStepLinks = flowStepLinkRepository.findEntityListByJPQL("SELECT fs FROM FlowStepLink fs WHERE fs.flowId = ?1 AND fs.stepId = ?2  ",params.get(0),params.get(1) );
		FlowStepLink flowStepLink= new FlowStepLink(flowId, stepId, toStepId, action,
				Boolean.valueOf(isConcurrent), 
				linkName, linkDesc, Integer.valueOf(dispOrd),"SYSTEM");
	

		if(serchFlowStepLinks!=null && !serchFlowStepLinks.isEmpty()) {
			FlowStepLink sFlowStepLink  =  serchFlowStepLinks.get(0);
			BeanUtils.copyProperties(flowStepLink, sFlowStepLink, "id");
			flowStepLinkRepository.save(flowStepLink);
		}else {
			flowStepLinkRepository.save(flowStepLink);
		}
	}
	
	/**
	 * 儲存 SubFlowConf (JPA)
	 * @param flowId
	 * @param subFlowId
	 * @param subFlowName
	 * @param subFlowDesc
	 * @param finishTo
	 * @param returnTo
	 */
	@SuppressWarnings("unlikely-arg-type")
	public void setSubFlowConf(String flowId, String subFlowId, String subFlowName, String subFlowDesc, String finishTo, String returnTo) {
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("flowId", (String)flowId);
		params.put("subFlowId", (String)subFlowId);

		//FlowStep
		List<SubFlowConf>  serchSubFlowConfs = subFlowConfRepository.findEntityListByJPQL("SELECT fs FROM SubFlowConf fs WHERE fs.flowId = ?1 AND fs.subFlowId = ?2  ",params.get(0),params.get(1) );
		SubFlowConf subFlowConf= new SubFlowConf(flowId, subFlowId, subFlowName, subFlowDesc, "SYSTEM");
		subFlowConf.setFinishTo(finishTo);
		subFlowConf.setReturnTo(returnTo);

		if(serchSubFlowConfs!=null && !serchSubFlowConfs.isEmpty()) {
			SubFlowConf sSubFlowConf  =  serchSubFlowConfs.get(0);
			BeanUtils.copyProperties(subFlowConf, sSubFlowConf, "id");
			subFlowConfRepository.save(subFlowConf);
		}else {
			subFlowConfRepository.save(subFlowConf);
		}
	}
	
	/**
	 * 取得Flow所有步驟資料
	 * @param contractNo
	 * @return
	 */
	public List<Flowstep> getFlowstepList(String contractNo){
		String flowid = "";
		QueryWrapper<Flowstep> flowstepQw = null;
		List<Flowstep> flowList = new ArrayList<Flowstep>();
		// 查詢該合約的flowid
		Contractmaster contractmaster =  commonService.getContractmasterData(contractNo);
		if(contractmaster != null) {
			flowid = contractmaster.getFlowid();
			logger.info("flowid ==="+flowid);
			flowstepQw = new QueryWrapper<Flowstep>();
			flowstepQw.eq("FLOWID", flowid);
			flowstepQw.orderByAsc("DISPORD");
			flowList = flowstepMapper.selectList(flowstepQw);
		}
		
		return flowList;
	}
	
	/**
	 * 取得Flow部門流程人員姓名與部門
	 * @param contractNo
	 * @return
	 */
	public ProcessResult getFlowstepName(String contractNo){
		ProcessResult result = new ProcessResult();
		String flowid = "";
		QueryWrapper<Flowstep> flowstepQw = null;
		List<Flowstep> flowList = new ArrayList<Flowstep>();
		// 查詢該合約的flowid
		Contractmaster contractmaster = commonService.getContractmasterData(contractNo);
		
		if(contractmaster != null) {
			flowid = contractmaster.getFlowid();
			flowstepQw = new QueryWrapper<Flowstep>();
			flowstepQw.eq("FLOWID", flowid);
			flowstepQw.orderByAsc("DISPORD");
			flowList = flowstepMapper.selectList(flowstepQw);
			//取得人員姓名
			List<Map<String, Object>>resultlist=new LinkedList<Map<String,Object>>();
			flowList.forEach(action->{
				 Map<String ,Object> map=new HashMap<String ,Object>();
				 if(action.getUserids() == null) {
					 String name = "";
					 for(String roleid : action.getRoleids().split(",")) {
						 QueryWrapper<XauthRole> xauthRoleQw = new QueryWrapper<XauthRole>();
						 xauthRoleQw.eq("IDEN_ID", action.getDeptid());
						 xauthRoleQw.eq("ROLE_ID", roleid);
						 List<XauthRole> xauthRoleList = xauthRoleMapper.selectList(xauthRoleQw);
						 name = name + xauthRoleList.get(0).getRoleCname() + ","; 
					 }
					 map.put("rolename",name.substring(0, name.length()-1));
					 map.put("stepid", action.getStepid());
				 }else {
					 String name = "";
					 for(String userid : action.getUserids().split(",")) {
						 map.put("user_id", userid);
						 List<Map<String, Object>>list=contractMapper.selectRoleAndName(map);
						 for(Map<String, Object> dataMap :  list) {
							 name = name + MapUtils.getString(dataMap, "ROLE_CNAME") + ",";
						 }
					 }
					 map.put("rolename", name.substring(0, name.length()-1));
					 map.put("stepid", action.getStepid());
				 }
				 resultlist.add(map);	
			});
			result.addResult("dataList", resultlist);
		}
		return result;
	}
	
	public void deleteFlow(String flowId) {
		QueryWrapper<Formconf> formconfWrapper = new QueryWrapper<Formconf>();
		formconfWrapper.eq("FORMID", flowId);
		formconfMapper.delete(formconfWrapper);
		
		QueryWrapper<Flowconf> flowconfWrapper = new QueryWrapper<Flowconf>();
		flowconfWrapper.eq("FLOWID", flowId);
		flowconfMapper.delete(flowconfWrapper);
		
		QueryWrapper<Flowstep> flowstepWrapper = new QueryWrapper<Flowstep>();
		flowstepWrapper.eq("FLOWID", flowId);
		flowstepMapper.delete(flowstepWrapper);
		
		QueryWrapper<Flowsteplink> flowsteplinkWrapper = new QueryWrapper<Flowsteplink>();
		flowsteplinkWrapper.eq("FLOWID", flowId);
		flowsteplinkMapper.delete(flowsteplinkWrapper);
		
		QueryWrapper<Flowxml> flowxmlWrapper = new QueryWrapper<Flowxml>();
		flowxmlWrapper.eq("FLOWID", flowId);
		flowxmlMapper.delete(flowxmlWrapper);
		
	}
	
	public String getRoleids(String idenId,String userid) throws Exception {
		String roleids = "" ;
		List<String> xauthRoleUserList = commonService.getRoleDatas(userid, idenId);
		roleids = commonService.getDataByComma(xauthRoleUserList);
		
		return roleids;
	}

}
