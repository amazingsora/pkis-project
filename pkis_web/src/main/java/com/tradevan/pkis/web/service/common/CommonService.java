package com.tradevan.pkis.web.service.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tradevan.mapper.pkis.dao.ContractmasterMapper;
import com.tradevan.mapper.pkis.dao.FlowstepMapper;
import com.tradevan.mapper.pkis.dao.SuppliermasterMapper;
import com.tradevan.mapper.pkis.model.Contractmaster;
import com.tradevan.mapper.pkis.model.Flowstep;
import com.tradevan.mapper.pkis.model.Suppliermaster;
import com.tradevan.mapper.xauth.dao.XauthRoleAgentUserMapper;
import com.tradevan.mapper.xauth.dao.XauthRoleUserMapper;
import com.tradevan.mapper.xauth.dao.XauthSysCodeMapper;
import com.tradevan.mapper.xauth.model.XauthRoleAgentUser;
import com.tradevan.mapper.xauth.model.XauthRoleUser;
import com.tradevan.mapper.xauth.model.XauthSysCode;
import com.tradevan.xauthframework.web.service.DefaultService;

@Service("CommonService")
@Transactional(rollbackFor = Exception.class)
public class CommonService extends DefaultService {

	@Autowired
	XauthRoleAgentUserMapper xauthRoleAgentUserMapper;
	
	@Autowired
	XauthRoleUserMapper xauthRoleUserMapper;
	
	@Autowired
	FlowstepMapper flowstepMapper;
	
	@Autowired
	SuppliermasterMapper suppliermasterMapper;
	
	@Autowired
	XauthSysCodeMapper xauthSysCodeMapper; 
	
	@Autowired
	ContractmasterMapper contractmasterMapper;
	
	/**
	 * 依據代理人部門及人員ID查出資訊
	 * TABLE : XAUTH_ROLE_AGENT_USER
	 * @param agentIdenId
	 * @param agentUserId
	 * @return
	 */
	public List<XauthRoleAgentUser> getAgentDatas(String agentIdenId, String agentUserId) throws Exception {
		QueryWrapper<XauthRoleAgentUser> roleAgentUserWrapper = new QueryWrapper<XauthRoleAgentUser>();
		roleAgentUserWrapper.eq("APP_ID", APP_ID);
		roleAgentUserWrapper.eq("AGENT_IDEN_ID", agentIdenId);
		roleAgentUserWrapper.eq("AGENT_USER_ID", agentUserId);
		roleAgentUserWrapper.eq("ENABLED", "1");
		
		return xauthRoleAgentUserMapper.selectList(roleAgentUserWrapper);
	}
	
	/**
	 * 依據代理人資訊及關卡資訊取得被代理人
	 * @param flowid
	 * @param stepid
	 * @param xauthRoleAgentUser
	 * @return
	 */
	public List<String> getAgentSetByUsers(String flowid, String stepid, XauthRoleAgentUser xauthRoleAgentUser) throws Exception {
		List<String> result = new ArrayList<String>();
		String flowRoleids = "";
		String flowUserids = "";
		String flowDeptid = "";
		
		Flowstep flowstep = (getFlowstepData(flowid, stepid));
		if(flowstep != null && xauthRoleAgentUser != null) {
			flowRoleids = flowstep.getRoleids();
			flowUserids = flowstep.getUserids();
			flowDeptid = flowstep.getDeptid();
			if(StringUtils.equals(flowDeptid, xauthRoleAgentUser.getIdenId())) {
				if(StringUtils.isBlank(flowUserids)) {
					// flowstep 有角色
					result.addAll(getUserOrRoleDatas(flowRoleids, xauthRoleAgentUser.getRoleId(), xauthRoleAgentUser));
				} else {
					result.addAll(getUserOrRoleDatas(flowUserids, xauthRoleAgentUser.getUserId(), xauthRoleAgentUser));
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 取得被代理人userids
	 * @param flowUserOrRoleDatas
	 * @param agentUserOrRoleData
	 * @param xauthRoleAgentUser
	 * @return
	 */
	public List<String> getUserOrRoleDatas(String flowUserOrRoleDatas, String agentUserOrRoleData, XauthRoleAgentUser xauthRoleAgentUser) throws Exception {
		List<String> result = new ArrayList<String>();
		
		if(xauthRoleAgentUser != null) {
			for(String flowUserOrRoleData : flowUserOrRoleDatas.split(",")) {
				if(StringUtils.equals(flowUserOrRoleData, agentUserOrRoleData)) {
					result.add(xauthRoleAgentUser.getUserId());
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 依據flowid及stepid取得flowstep資訊
	 * TABLE : FLOWSTEP
	 * @param flowid
	 * @param stepid
	 * @return
	 */
	public Flowstep getFlowstepData(String flowid, String stepid) throws Exception {
		Flowstep result = null;
		QueryWrapper<Flowstep> flowStepWrapper = new QueryWrapper<Flowstep>();
		flowStepWrapper.eq("FLOWID", flowid);
		flowStepWrapper.eq("STEPID", stepid);
		List<Flowstep> flowStepList = flowstepMapper.selectList(flowStepWrapper);
		if(flowStepList != null && flowStepList.size() > 0) {
			result = flowStepList.get(0);
		}
		
		return result;
	}
	
	/**
	 * 依據部門及角色查出相對應的多筆USER_ID
	 * TABLE : XAUTH_ROLE_USER
	 * @param roleId
	 * @param deptId
	 * @return
	 */
	public List<String> getUserDatas(String roleId, String deptId) throws Exception {
		List<String> result = new ArrayList<String>();
		List<XauthRoleUser> roleUsers = null;
		QueryWrapper<XauthRoleUser> queryWrapper = new QueryWrapper<XauthRoleUser>(); 
		
		queryWrapper.eq("IDEN_ID", deptId); 
		queryWrapper.eq("ROLE_ID", roleId); 
		roleUsers = xauthRoleUserMapper.selectList(queryWrapper);
		for(XauthRoleUser roleUser : roleUsers) {
			result.add(roleUser.getUserId());
		}
		 
		return result;
	} 
	
	/**
	 * 依據部門及使用者帳號查出相對應的多筆ROLE_ID
	 * TABLE : XAUTH_ROLE_USER
	 * @param userId
	 * @param deptId
	 * @return
	 */
	public List<String> getRoleDatas(String userId, String deptId) throws Exception { 
		List<String> result = new ArrayList<String>();
		List<XauthRoleUser> roleUsers = null;
		QueryWrapper<XauthRoleUser> queryWrapper = new QueryWrapper<XauthRoleUser>(); 
		
		queryWrapper.eq("IDEN_ID", deptId); 
		queryWrapper.eq("USER_ID", userId); 
		roleUsers = xauthRoleUserMapper.selectList(queryWrapper);
		for(XauthRoleUser roleUser : roleUsers) {
			result.add(roleUser.getRoleId());
		}
		
		return result;
	} 
	
	/**
	 * 依據多筆字串資料，以逗號分隔組成字串
	 * @param datas
	 * @return
	 */
	public String getDataByComma(List<String> datas) throws Exception {
		StringBuffer result = new StringBuffer();
		int dataSize;
		
		if(datas != null) {
			dataSize = datas.size();
			for(int i = 0 ; i < dataSize ; i ++) {
				if(i == (dataSize - 1)) {
					result.append(datas.get(i));
				} else {
					result.append(datas.get(i));
					result.append(",");
				}
			}
		}
		
		return result.toString();
	}
	
	/**
	 * 依據合約範本類型及課別查詢有啟用的供應商資料
	 * TABLE : SUPPLIERMASTER
	 * @param module
	 * @param deptno
	 * @return
	 * @throws Exception
	 */
	public List<Suppliermaster> getSupplierDatasByDeptno(String module, String deptno) throws Exception {
		QueryWrapper<Suppliermaster> suppliermasterWrapper = new QueryWrapper<Suppliermaster>();
		if(StringUtils.equals("NSC", module)) {
			suppliermasterWrapper.isNull("DEPTNO");
		} else {
			suppliermasterWrapper.eq("DEPTNO", deptno);
		}
		suppliermasterWrapper.eq("ENABLED", "Y");
		
		return suppliermasterMapper.selectList(suppliermasterWrapper);
	}
	
	/**
	 * 依據供應商id及課別取得單一筆有啟用的供應商資料，課別deptno可以為null
	 * TABLE : SUPPLIERMASTER
	 * @param supplierid
	 * @param deptno
	 * @return
	 */
	public Suppliermaster getSupplierData(String supplierid, String deptno) {
		Suppliermaster result = null;
		List<Suppliermaster> supplierDatas = null;
		QueryWrapper<Suppliermaster> suppliermasterWrapper = new QueryWrapper<Suppliermaster>();
		
		suppliermasterWrapper.eq("SUPPLIERID", supplierid);
		if(StringUtils.isBlank(deptno)) {
			suppliermasterWrapper.isNull("DEPTNO");
		} else {
			suppliermasterWrapper.eq("DEPTNO", deptno);
		}
		suppliermasterWrapper.eq("ENABLED", "Y");
		supplierDatas = suppliermasterMapper.selectList(suppliermasterWrapper);
		if(supplierDatas != null && supplierDatas.size() > 0) {
			result = supplierDatas.get(0);
		}
		
		return result;
	}
	
	/**
	 * 依據GP取得設定檔多筆有啟用的資料
	 * TABLE : XAUTH_SYS_CODE
	 * @param gp
	 * @return
	 * @throws Exception
	 */
	public List<XauthSysCode> getSysCodeDatasByGp(String gp) throws Exception {
		QueryWrapper<XauthSysCode> sysCodeWrapper = new QueryWrapper<XauthSysCode>();
		sysCodeWrapper.eq("APP_ID", APP_ID);
		sysCodeWrapper.eq("GP", gp);
		sysCodeWrapper.eq("ENABLED", "1");

		return xauthSysCodeMapper.selectList(sysCodeWrapper);
	}
	
	/**
	 * 依據GP及CODE取得單一筆設定檔有啟用的資料
	 * TABLE : XAUTH_SYS_CODE
	 * @param gp
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public XauthSysCode getSysCodeData(String gp, String code) throws Exception {
		XauthSysCode result = null;
		List<XauthSysCode> sysCodes = null;
		QueryWrapper<XauthSysCode> sysCodeWrapper = new QueryWrapper<XauthSysCode>();
		
		sysCodeWrapper.eq("APP_ID", APP_ID);
		sysCodeWrapper.eq("GP", gp);
		sysCodeWrapper.eq("CODE", code);
		sysCodeWrapper.eq("ENABLED", "1");
		sysCodes = xauthSysCodeMapper.selectList(sysCodeWrapper);
		if(sysCodes != null && sysCodes.size() > 0) {
			result = sysCodes.get(0);
		}
		
		return result;
	}
	
	/**
	 * 依據合約編號dataid取得單一筆合約主檔資料
	 * TABLE : CONTRACTMASTER
	 * @param dataid
	 * @return
	 */
	public Contractmaster getContractmasterData(String dataid) {
		Contractmaster result = null;
		List<Contractmaster> contractDatas = null;
		QueryWrapper<Contractmaster> contractmasterWrapper = new QueryWrapper<Contractmaster>();
		
		contractmasterWrapper.eq("DATAID", dataid);
		contractDatas = contractmasterMapper.selectList(contractmasterWrapper);
		if(contractDatas != null && contractDatas.size() > 0) {
			result = contractDatas.get(0);
		}
		
		return result;
	}
	
}
