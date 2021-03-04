package com.tradevan.pkis.web.service.xauth;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SealedObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.tradevan.mapper.SequenceMapper;
import com.tradevan.mapper.pkis.dao.BatchparamsetMapper;
import com.tradevan.mapper.pkis.dao.DeptInfoExtMapper;
import com.tradevan.mapper.pkis.dao.EmailqueueMapper;
import com.tradevan.mapper.pkis.dao.FlowstepMapper;
import com.tradevan.mapper.pkis.dao.ReviewconfMapper;
import com.tradevan.mapper.pkis.dao.SuppliermasterMapper;
import com.tradevan.mapper.pkis.dao.SystemparamMapper;
import com.tradevan.mapper.pkis.dao.UserInfoExtMapper;
import com.tradevan.mapper.pkis.model.Batchparamset;
import com.tradevan.mapper.pkis.model.DeptInfoExt;
import com.tradevan.mapper.pkis.model.Emailqueue;
import com.tradevan.mapper.pkis.model.Reviewconf;
import com.tradevan.mapper.pkis.model.Suppliermaster;
import com.tradevan.mapper.pkis.model.Systemparam;
import com.tradevan.mapper.pkis.model.UserInfoExt;
import com.tradevan.mapper.xauth.dao.XauthDeptMapper;
import com.tradevan.mapper.xauth.dao.XauthIdenMenuMapper;
import com.tradevan.mapper.xauth.dao.XauthIpGrantMapper;
import com.tradevan.mapper.xauth.dao.XauthMapper;
import com.tradevan.mapper.xauth.dao.XauthMenuMapper;
import com.tradevan.mapper.xauth.dao.XauthRoleAgentUserMapper;
import com.tradevan.mapper.xauth.dao.XauthRoleMapper;
import com.tradevan.mapper.xauth.dao.XauthRoleMenuMapper;
import com.tradevan.mapper.xauth.dao.XauthRoleUserMapper;
import com.tradevan.mapper.xauth.dao.XauthSysCodeMapper;
import com.tradevan.mapper.xauth.dao.XauthUsersMapper;
import com.tradevan.mapper.xauth.dao.XauthUsersPwdHistoryMapper;
import com.tradevan.mapper.xauth.model.XauthDept;
import com.tradevan.mapper.xauth.model.XauthIdenMenu;
import com.tradevan.mapper.xauth.model.XauthIpGrant;
import com.tradevan.mapper.xauth.model.XauthMenu;
import com.tradevan.mapper.xauth.model.XauthRole;
import com.tradevan.mapper.xauth.model.XauthRoleAgentUser;
import com.tradevan.mapper.xauth.model.XauthRoleMenu;
import com.tradevan.mapper.xauth.model.XauthRoleUser;
import com.tradevan.mapper.xauth.model.XauthSysCode;
import com.tradevan.mapper.xauth.model.XauthUsers;
import com.tradevan.mapper.xauth.model.XauthUsersPwdHistory;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.ConvertUtils;
import com.tradevan.xauthframework.common.utils.DateUtils;
import com.tradevan.xauthframework.common.utils.MapUtils;
import com.tradevan.xauthframework.common.utils.SensitiveUtils;
import com.tradevan.xauthframework.core.enums.MSG_KEY;
import com.tradevan.xauthframework.core.enums.USER_TYPE;
import com.tradevan.xauthframework.core.security.UserInfo;
import com.tradevan.xauthframework.core.service.CheckPwdService;
import com.tradevan.xauthframework.dao.bean.GridResult;
import com.tradevan.xauthframework.web.service.DefaultService;

@Service("XauthService")
@Transactional(rollbackFor=Exception.class)
public class XauthService extends DefaultService {
	@Autowired
	FlowstepMapper flowstepMapper;
	
	@Autowired
	CheckPwdService checkPwdService;
	
	@Autowired
	XauthMapper xauthMapper;
	
	@Autowired
	XauthDeptMapper xauthDeptMapper;
	
	@Autowired
	XauthMenuMapper xauthMenuMapper;
	
	@Autowired
	XauthIdenMenuMapper xauthIdenMenuMapper;
	
	@Autowired
	XauthRoleMapper xauthRoleMapper;
	
	@Autowired
	XauthRoleUserMapper xauthRoleUserMapper;
	
	@Autowired
	XauthRoleMenuMapper xauthRoleMenuMapper;
	
	@Autowired
	XauthUsersMapper xauthUsersMapper;
	
	@Autowired
	XauthIpGrantMapper xauthIpGrantMapper;
	
	@Autowired
	XauthUsersPwdHistoryMapper xauthUsersPwdHistoryMapper;
	
	@Autowired
	XauthSysCodeMapper xauthSysCodeMapper;
	
	@Autowired
	XauthRoleAgentUserMapper xauthRoleAgentUserMapper;
	
	@Autowired
	DeptInfoExtMapper deptInfoExtMapper;
	
	@Autowired
	UserInfoExtMapper userInfoExtMapper;
	
	@Autowired
	ReviewconfMapper reviewconfMapper;
	
	@Autowired
	SystemparamMapper systemparamMapper;
	
	@Autowired
	EmailqueueMapper emailqueueMapper;
	
	@Autowired
	SuppliermasterMapper suppliermasterMapper;
	
	@Autowired
	BatchparamsetMapper batchparamsetMapper;
	
	@Autowired
	SequenceMapper sequenceMapper;
	
	/**
	 * 部門資料分頁查詢
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult searchDeptPage(Map<String, Object> params) throws Exception {
		UserInfo userInfo = userContext.getCurrentUser();
		GridResult gridResult = this.grid(params);		
		Map<String, Object> objParams = new HashMap<String, Object>();
		objParams.put("appId", APP_ID);
		objParams.put("idenId", MapUtils.getString(params, "idenId"));
		objParams.put("ban", MapUtils.getString(params, "ban"));
		objParams.put("cname", MapUtils.getString(params, "cname"));
		objParams.put("idenType", MapUtils.getString(params, "idenType"));
		objParams.put("sortColumnName", MapUtils.getString(params, "sortColumnName"));
		objParams.put("sortOrder", MapUtils.getString(params, "sortOrder"));
		
		if (userInfo.getUserType().equals(USER_TYPE.USER.getCode())) {
			objParams.put("idenId", userInfo.getIdenId());
		}
		else if (userContext.getCurrentUser().getUserType().equals(USER_TYPE.ADMIN.getCode())) {
			Map<String, Object> compParams = new HashMap<String, Object>();
			compParams.put("appId", APP_ID);
			compParams.put("idenId", userInfo.getIdenId());
			compParams.put("userId", userInfo.getUserId());
			List<Map<String, Object>> dataList = xauthMapper.selectDeptTree(compParams);
			if (dataList != null && dataList.size() > 0) {
				List<String> list = new ArrayList<String>();
				for (Map<String, Object> map : dataList) {
					list.add(MapUtils.getString(map, "IDEN_ID"));
				}
				objParams.put("ids", list);
			}
		}
		
		commonDao.query(gridResult, "com.tradevan.mapper.xauth.dao.XauthMapper.searchDept", objParams);	
		return gridResult;
	}
	
	/**
	 * 取得單筆部門資料
	 * @param getDept
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getDept(XauthDept xauthDept) throws Exception {	
		String directManager = "";
		if (xauthDept != null) {
			QueryWrapper<DeptInfoExt> queryWrapper = new QueryWrapper<DeptInfoExt>();
			queryWrapper.eq("APP_ID", xauthDept.getAppId());
			queryWrapper.eq("IDEN_ID", xauthDept.getIdenId());
			List<DeptInfoExt> deptInfoExtList =  deptInfoExtMapper.selectList(queryWrapper);
			if(deptInfoExtList != null && deptInfoExtList.size() > 0) {
				directManager = deptInfoExtList.get(0).getDirectManager();
			}
			List<Map<String, Object>> dataList = xauthMapper.searchDept(ConvertUtils.Object2Map(xauthDept));
			if (dataList != null && dataList.size() > 0) {
				Map<String, Object> resultMap = ConvertUtils.Map2Camel(dataList.get(0));
				resultMap.put("directManager", directManager);
				return resultMap;
			}
		}		
		return null;
	}
	
	/**
	 * 取得流程資料
	 * @param getDept
	 * @return
	 * @throws Exception
	 */
	public List<Reviewconf> getFlowList(String contractmodel) throws Exception {	
		List<Reviewconf> result = null ;
		QueryWrapper<Reviewconf> queryWrapper = new QueryWrapper<Reviewconf>();
		if(StringUtils.isNotBlank(contractmodel)) {
			queryWrapper.eq("CONTRACTMODEL", contractmodel);
		}
		queryWrapper.eq("STATUS", "N");
		queryWrapper.orderByAsc("FLOWID");
		queryWrapper.eq("STATUS", "N");
		result = reviewconfMapper.selectList(queryWrapper);
		return result;
	}
	
	/**
	 * 新增部門資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult insertDept(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		if (userContext.getCurrentUser().getUserType().equals(USER_TYPE.USER.getCode())) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("您無權限新增部門資料");
			return result;
		}

		if (params != null && params.size() > 0) {
			ProcessResult rs = checkDeptData(params);
			if (rs.getStatus().equals(ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}	
			
			String idenId = MapUtils.getString(params, "idenId");
			
			XauthDept xauthDept = new XauthDept();
			xauthDept.setAppId(APP_ID);
			xauthDept.setIdenId(idenId);					
			xauthDept.setBan(MapUtils.getEmptyString(params, "ban"));						
			xauthDept.setCname(MapUtils.getString(params, "cname"));
			
			String parentId = "#";
			String parentIdData = MapUtils.getString(params, "parentId");
			if (StringUtils.isNotBlank(parentIdData)) {
                parentId = parentIdData;
            }
			
			if ("#".equals(parentId) && !userContext.getCurrentUser().getUserType().equals(USER_TYPE.SYS_ADMIN.getCode())) {
			    result.setStatus(ProcessResult.NG);
			    result.addMessage("請選擇上層部門");
			    return result;
			}
			
			String seqNo = "0";
			
			if (!parentId.equals("#")) {
				String[] pa = parentId.split("#");
				parentId = pa[0];
				seqNo = pa[1];
			}
						
			Date currentDate = new Timestamp(new Date().getTime());
			
			xauthDept.setParentId(parentId);
			xauthDept.setSeqNo(Integer.parseInt(seqNo));
			xauthDept.setEnabled(MapUtils.getString(params, "enabled"));
			xauthDept.setCreUser(getCreOrUpdUser(null));
			xauthDept.setCreDate(currentDate);
						
			if (parentId.equals("#") && seqNo.equals("0")) {
				xauthDept.setParentSeq(0);								
			}
			
			xauthMapper.insertDept(xauthDept);
			
			String idenType = MapUtils.getString(params, "idenType");	
			String directManager = MapUtils.getString(params, "directManager");
			QueryWrapper<DeptInfoExt> queryWrapper = new QueryWrapper<DeptInfoExt>();
			queryWrapper.eq("APP_ID", APP_ID);
			queryWrapper.eq("IDEN_ID", idenId);
			DeptInfoExt deptInfoExt = deptInfoExtMapper.selectOne(queryWrapper);
			if (deptInfoExt != null) {
				deptInfoExt.setIdenType(idenType);
				deptInfoExt.setDirectManager(directManager);
				deptInfoExt.setUpdUser(getCreOrUpdUser(null));
				deptInfoExt.setUpdDate(currentDate);
				deptInfoExtMapper.update(deptInfoExt, queryWrapper);
			}
			else {
				deptInfoExt = new DeptInfoExt();
				deptInfoExt.setAppId(APP_ID);
				deptInfoExt.setIdenId(idenId);
				deptInfoExt.setIdenType(idenType);
				deptInfoExt.setDirectManager(directManager);
				deptInfoExt.setCreUser(getCreOrUpdUser(null));
				deptInfoExt.setCreDate(currentDate);
				deptInfoExtMapper.insert(deptInfoExt);
			}

			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.INSERT_OK.getMessage());
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}			
		return result;
	}
	
	/**
	 * 更新部門資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult updateDept(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		if (userContext.getCurrentUser().getUserType().equals(USER_TYPE.USER.getCode())) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("您無權限更新部門資料");
			return result;
		}
		
		if (params != null && params.size() > 0) {
			ProcessResult rs = checkDeptData(params);
			if (rs.getStatus().equals(ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}	
			
			String idenId = MapUtils.getString(params, "idenId");
			
			params.put("appId", APP_ID);
			XauthDept xauthDept = new XauthDept();
			xauthDept.setAppId(APP_ID);
			xauthDept.setIdenId(idenId);
			Map<String, Object> data = getDept(xauthDept);
			if (data == null) {
				result.setStatus(ProcessResult.NG);
				result.addMessage(MSG_KEY.QUERY_EMPTY.getMessage());
				return result;
			}
			
			String parentId = "#";
			String parentIdData = MapUtils.getString(params, "parentId");
			if (StringUtils.isNotBlank(parentIdData)) {
                parentId = parentIdData;
            }
			
			String seqNo = "0";
			
			if (!parentId.equals("#")) {
				String[] pa = parentId.split("#");
				parentId = pa[0];
				seqNo = pa[1];
			}

			Date currentDate = new Timestamp(new Date().getTime());			
			params.put("updUser", getCreOrUpdUser(null));
			params.put("updDate", currentDate);
			
			if (!MapUtils.getString(data, "parentId").equals(parentId)) {
//				result.setStatus(ProcessResult.NG);
//				result.addMessage("上層部門不得異動");
//				return result;
				
				params.put("parentId", parentId);
				params.put("seqNo", seqNo);
				xauthMapper.updateDeptParent(params);
				
				params = new HashMap<String, Object>();
				params.put("appId", APP_ID);
				params.put("idenId", idenId);
				xauthMapper.updateDeptParentSeq(params);
			}
			else {
				params.remove("parentId");
				params.remove("seqNo");
				params.remove("parentSeq");
				xauthMapper.updateDept(params);
			}							
			
			String idenType = MapUtils.getString(params, "idenType");		
			String directManager = MapUtils.getString(params, "directManager");
			QueryWrapper<DeptInfoExt> queryWrapper = new QueryWrapper<DeptInfoExt>();
			queryWrapper.eq("APP_ID", APP_ID);
			queryWrapper.eq("IDEN_ID", idenId);
			DeptInfoExt deptInfoExt = deptInfoExtMapper.selectOne(queryWrapper);
			if (deptInfoExt != null) {
				deptInfoExt.setIdenType(idenType);
				deptInfoExt.setDirectManager(directManager);
				deptInfoExt.setUpdUser(getCreOrUpdUser(null));
				deptInfoExt.setUpdDate(currentDate);				
				deptInfoExtMapper.update(deptInfoExt, queryWrapper);
			}
			else {
				deptInfoExt = new DeptInfoExt();
				deptInfoExt.setAppId(APP_ID);
				deptInfoExt.setIdenId(idenId);
				deptInfoExt.setIdenType(idenType);
				deptInfoExt.setDirectManager(directManager);
				deptInfoExt.setCreUser(getCreOrUpdUser(null));
				deptInfoExt.setCreDate(currentDate);
				deptInfoExtMapper.insert(deptInfoExt);
			}
			
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.UPDATE_OK.getMessage());
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result;
	}
	
	/**
	 * 刪除部門資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult deleteDept(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		if (userContext.getCurrentUser().getUserType().equals(USER_TYPE.USER.getCode())) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("您無權限刪除部門資料");
			return result;
		}
		if (params != null && params.size() > 0) {
			String idenId = MapUtils.getString(params, "idenId");
			if (StringUtils.isNotBlank(idenId)) {						
				Map<String, Object> compParams = new HashMap<String, Object>();
				compParams.put("appId", APP_ID);
				compParams.put("idenId", idenId);
				List<Map<String, Object>> dataList = xauthMapper.selectDeptTree(compParams);
				if (dataList != null && dataList.size() > 0) {
					String iden = "";
					QueryWrapper<XauthDept> deptWrapper = null;
					QueryWrapper<XauthIdenMenu> idenMenuWrapper = null;
					QueryWrapper<XauthRole> roleWrapper = null;
					QueryWrapper<XauthRoleUser> roleUserWrapper = null;
					QueryWrapper<XauthRoleMenu> roleMenuWrapper = null;
					QueryWrapper<XauthUsers> usersWrapper = null;
					QueryWrapper<XauthIpGrant> ipGrantWrapper = null;
					QueryWrapper<DeptInfoExt> deptInfoExtWrapper = null;
					for (Map<String, Object> data : dataList) {
						iden = data.get("IDEN_ID").toString();
												
						deptWrapper = new QueryWrapper<XauthDept>();
						deptWrapper.eq("APP_ID", APP_ID);
						deptWrapper.eq("IDEN_ID", iden);						
						xauthDeptMapper.delete(deptWrapper);
												
						idenMenuWrapper = new QueryWrapper<XauthIdenMenu>();
						idenMenuWrapper.eq("APP_ID", APP_ID);
						idenMenuWrapper.eq("IDEN_ID", iden);						
						xauthIdenMenuMapper.delete(idenMenuWrapper);
												
						roleWrapper = new QueryWrapper<XauthRole>();
						roleWrapper.eq("APP_ID", APP_ID);
						roleWrapper.eq("IDEN_ID", iden);						
						xauthRoleMapper.delete(roleWrapper);
						
						roleUserWrapper = new QueryWrapper<XauthRoleUser>();
						roleUserWrapper.eq("APP_ID", APP_ID);
						roleUserWrapper.eq("IDEN_ID", iden);						
						xauthRoleUserMapper.delete(roleUserWrapper);
												
						roleMenuWrapper = new QueryWrapper<XauthRoleMenu>();
						roleMenuWrapper.eq("APP_ID", APP_ID);
						roleMenuWrapper.eq("IDEN_ID", iden);						
						xauthRoleMenuMapper.delete(roleMenuWrapper);
							
						usersWrapper = new QueryWrapper<XauthUsers>();
						usersWrapper.eq("APP_ID", APP_ID);
						usersWrapper.eq("IDEN_ID", iden);						
						xauthUsersMapper.delete(usersWrapper);
												
						ipGrantWrapper = new QueryWrapper<XauthIpGrant>();
						ipGrantWrapper.eq("APP_ID", APP_ID);
						ipGrantWrapper.eq("IDEN_ID", iden);						
						xauthIpGrantMapper.delete(ipGrantWrapper);
												
						deptInfoExtWrapper = new QueryWrapper<DeptInfoExt>();
						deptInfoExtWrapper.eq("APP_ID", APP_ID);
						deptInfoExtWrapper.eq("IDEN_ID", iden);						
						deptInfoExtMapper.delete(deptInfoExtWrapper);
					}
					result.setStatus(ProcessResult.OK);
					result.addMessage(MSG_KEY.DELETE_OK.getMessage());
				}
				else {
					result.setStatus(ProcessResult.NG);
					result.addMessage(MSG_KEY.QUERY_EMPTY.getMessage());
				}
			}
			else {
				result.setStatus(ProcessResult.NG);
				result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
			}			
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result;
	}
	
	/**
	 * 檢查部門資料
	 * @param params
	 * @return
	 */
	private ProcessResult checkDeptData(Map<String, Object> params) {
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);
		try {
			if (StringUtils.isBlank(MapUtils.getString(params, "idenId"))) {
				result.addMessage("請輸入識別碼");
				return result;
			}
			
			if (StringUtils.isBlank(MapUtils.getString(params, "cname"))) {
				result.addMessage("請輸入部門名稱");
				return result;
			}		
			
			if (StringUtils.isBlank(MapUtils.getString(params, "idenType"))) {
				result.addMessage("請選擇身份別");
				return result;
			}	
		}
		catch (Exception e) {
			e.printStackTrace();
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
			return result;
		}
		result.setStatus(ProcessResult.OK);
		return result;
	}
	
	/**
	 * 角色資料分頁查詢
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult searchRolePage(Map<String, Object> params) throws Exception {
		UserInfo userInfo = userContext.getCurrentUser();
		GridResult gridResult = this.grid(params);	
		Map<String, Object> objParams = new HashMap<String, Object>();
		objParams.put("appId", APP_ID);
		objParams.put("idenId", MapUtils.getString(params, "idenId"));
		objParams.put("roleId", MapUtils.getString(params, "roleId"));
		objParams.put("roleCname", MapUtils.getString(params, "roleCname"));
		if (MapUtils.getString(params, "sortColumnName") != null && MapUtils.getString(params, "sortOrder") != null) {			
			objParams.put("sortColumnName", "A." + MapUtils.getString(params, "sortColumnName"));
			objParams.put("sortOrder", MapUtils.getString(params, "sortOrder"));
		}
		else {
			objParams.put("sortColumnName", "A.IDEN_ID, A.ROLE_ID");
			objParams.put("sortOrder", "ASC");
		}
		
		if (userInfo.getUserType().equals(USER_TYPE.USER.getCode())) {
			objParams.put("idenId", userInfo.getIdenId());
		}
		else if (userContext.getCurrentUser().getUserType().equals(USER_TYPE.ADMIN.getCode())) {
			Map<String, Object> compParams = new HashMap<String, Object>();
			compParams.put("appId", APP_ID);
			compParams.put("idenId", userInfo.getIdenId());
			compParams.put("userId", userInfo.getUserId());
			List<Map<String, Object>> dataList = xauthMapper.selectDeptTree(compParams);
			if (dataList != null && dataList.size() > 0) {
				List<String> list = new ArrayList<String>();
				for (Map<String, Object> map : dataList) {
					list.add(MapUtils.getString(map, "IDEN_ID"));
				}
				objParams.put("ids", list);
			}
		}
		
		commonDao.query(gridResult, "com.tradevan.mapper.xauth.dao.XauthMapper.searchRole", objParams);
		return gridResult;
	}
	
	/**
	 * 取得單筆角色資料
	 * @param xauthRole
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getRole(XauthRole xauthRole) throws Exception {		
		if (xauthRole != null) {
			List<Map<String, Object>> dataList = xauthMapper.selectRole(ConvertUtils.Object2Map(xauthRole));
			if (dataList != null && dataList.size() > 0) {
				dataList = ConvertUtils.Map2Camel(dataList);
				return dataList.get(0);
			}
		}	
		return null;
	}
	
	/**
	 * 取得角色資料
	 * @param xauthRole
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getRoleList(XauthRole xauthRole) throws Exception {
		List<Map<String, Object>> roleList = xauthMapper.selectRole(ConvertUtils.Object2Map(xauthRole));
		roleList = ConvertUtils.Map2Camel(roleList);
		return roleList;
	}
	/**
	 * 取得角色資料
	 * @param xauthRole
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getRoleList(Map<String,Object> params) throws Exception {
		QueryWrapper<XauthRoleUser> queryWrapper=new QueryWrapper<XauthRoleUser>();
		UserInfo userInfo = userContext.getCurrentUser();
		queryWrapper.eq("USER_ID",userInfo.getUserId());
		List <XauthRoleUser> xauthRoleUserList=xauthRoleUserMapper.selectList(queryWrapper);
		params.put("roleId",xauthRoleUserList.get(0).getRoleId());
		params.put("appId", APP_ID);
		List<Map<String, Object>> roleList = xauthMapper.selectRole(params);
		roleList = ConvertUtils.Map2Camel(roleList);
		return roleList;
	}
	
	/**
	 * 新增角色資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult insertRole(Map<String, Object> params) throws Exception {	
		ProcessResult result = new ProcessResult();
		if (userContext.getCurrentUser().getUserType().equals(USER_TYPE.USER.getCode())) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("您無權限新增角色資料");
			return result;
		}
		
		if (params != null && params.size() > 0) {
			ProcessResult rs = checkRoleData(params);
			if (rs.getStatus().equals(ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}	
			
			String roleId = "ROLE_" + MapUtils.getString(params, "roleId");
//			String roleId = "ROLE_" + MapUtils.getString(params, "roleId").toUpperCase();
			
			XauthRole xauthRole = new XauthRole();
			xauthRole.setAppId(APP_ID);
			xauthRole.setIdenId(MapUtils.getString(params, "idenId"));
			xauthRole.setRoleId(roleId);
			xauthRole.setRoleCname(MapUtils.getString(params, "roleCname"));
			xauthRole.setCreUser(getCreOrUpdUser(null));
			xauthRole.setCreDate(new Timestamp(new Date().getTime()));			
			xauthRoleMapper.insert(xauthRole);
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.INSERT_OK.getMessage());	
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result;
	}
	
	/**
	 * 更新角色資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult updateRole(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		if (params != null && params.size() > 0) {
			ProcessResult rs = checkRoleData(params);
			if (rs.getStatus().equals(ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}	
			
			String idenId = MapUtils.getString(params, "idenId");
			String roleId = "ROLE_" + MapUtils.getString(params, "roleId");
//			String roleId = "ROLE_" + MapUtils.getString(params, "roleId").toUpperCase();
			
			XauthRole xauthRole = new XauthRole();
			xauthRole.setAppId(APP_ID);
			xauthRole.setIdenId(idenId);
			xauthRole.setRoleId(roleId);
			Map<String, Object> data = getRole(xauthRole);
			if (data == null) {
				result.setStatus(ProcessResult.NG);
				result.addMessage(MSG_KEY.QUERY_EMPTY.getMessage());
				return result;
			}
			
			xauthRole.setRoleCname(MapUtils.getString(params, "roleCname"));
			xauthRole.setUpdUser(getCreOrUpdUser(null));			
			xauthRole.setUpdDate(new Timestamp(new Date().getTime()));
			
			QueryWrapper<XauthRole> queryWrapper = new QueryWrapper<XauthRole>();
			queryWrapper.eq("APP_ID", APP_ID);
			queryWrapper.eq("IDEN_ID", idenId);
			queryWrapper.eq("ROLE_ID", roleId);
			xauthRole.update(queryWrapper);
			xauthRoleMapper.update(xauthRole, queryWrapper);
			
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.UPDATE_OK.getMessage());
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result;
	}
	
	/**
	 * 刪除角色資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult deleteRole(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		if (params != null && params.size() > 0) {	
			String idenId = MapUtils.getString(params, "idenId");
			String roleId = "ROLE_" + MapUtils.getString(params, "roleId");
//			String roleId = "ROLE_" + MapUtils.getString(params, "roleId").toUpperCase();
						
			QueryWrapper<XauthRole> roleWrapper = new QueryWrapper<XauthRole>();
			roleWrapper.eq("APP_ID", APP_ID);
			roleWrapper.eq("IDEN_ID", idenId);
			roleWrapper.eq("ROLE_ID", roleId);			
			xauthRoleMapper.delete(roleWrapper);
						
			QueryWrapper<XauthRoleMenu> roleMenuWrapper = new QueryWrapper<XauthRoleMenu>();
			roleMenuWrapper.eq("APP_ID", APP_ID);
			roleMenuWrapper.eq("IDEN_ID", idenId);
			roleMenuWrapper.eq("ROLE_ID", roleId);			
			xauthRoleMenuMapper.delete(roleMenuWrapper);
						
			QueryWrapper<XauthRoleUser> roleUserWrapper = new QueryWrapper<XauthRoleUser>();
			roleUserWrapper.eq("APP_ID", APP_ID);
			roleUserWrapper.eq("IDEN_ID", idenId);
			roleUserWrapper.eq("ROLE_ID", roleId);			
			xauthRoleUserMapper.delete(roleUserWrapper);
			
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.DELETE_OK.getMessage());
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result;
	}
	
	/**
	 * 檢查角色資料
	 * @param params
	 * @return
	 */
	private ProcessResult checkRoleData(Map<String, Object> params) {
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);
		try {
			if (StringUtils.isBlank(MapUtils.getString(params, "idenId"))) {
				result.addMessage("請選擇公司");
				return result;
			}
			
			if (StringUtils.isBlank(MapUtils.getString(params, "roleId"))) {
				result.addMessage("請輸入角色代號");
				return result;
			}		
			
			if (StringUtils.isBlank(MapUtils.getString(params, "roleCname"))) {
				result.addMessage("請輸入角色名稱");
				return result;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
			return result;
		}
		result.setStatus(ProcessResult.OK);
		return result;
	}
	
	/**
	 * 使用者資料分頁查詢
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult searchUserPage(Map<String, Object> params) throws Exception {
		UserInfo userInfo = userContext.getCurrentUser();
		GridResult gridResult = this.grid(params);	
		Map<String, Object> objParams = new HashMap<String, Object>();
		objParams.put("appId", APP_ID);
		objParams.put("idenId", MapUtils.getString(params, "idenId"));
		objParams.put("userId", MapUtils.getString(params, "userId"));
		objParams.put("userCname", MapUtils.getString(params, "userCname"));
		objParams.put("enabledName", MapUtils.getString(params, "enabledName"));
		
		if (MapUtils.getString(params, "sortColumnName") != null && MapUtils.getString(params, "sortOrder") != null) {			
			objParams.put("sortColumnName", "A." + MapUtils.getString(params, "sortColumnName"));
			objParams.put("sortOrder", MapUtils.getString(params, "sortOrder"));
		}
		else {
			objParams.put("sortColumnName", "A.USER_TYPE, A.USER_ID");
			objParams.put("sortOrder", "ASC");
		}
		
		if (userInfo.getUserType().equals(USER_TYPE.USER.getCode())) {
			objParams.put("userId", userInfo.getUsername());
		}
		else if (userInfo.getUserType().equals(USER_TYPE.ADMIN.getCode())) {
			Map<String, Object> compParams = new HashMap<String, Object>();
			compParams.put("appId", APP_ID);
			compParams.put("idenId", userInfo.getIdenId());
			compParams.put("userId", userInfo.getUserId());
			List<Map<String, Object>> dataList = xauthMapper.selectDeptTree(compParams);			
			if (dataList != null && dataList.size() > 0) {
				List<String> list = new ArrayList<String>();
				for (Map<String, Object> map : dataList) {
					list.add(MapUtils.getString(map, "IDEN_ID"));
				}
				objParams.put("ids", list);
			}
		}
		
		commonDao.query(gridResult, "com.tradevan.mapper.xauth.dao.XauthMapper.searchUser", objParams);
		return gridResult;
	}
	
	/**
	 * 取得使用者單筆資料
	 * @param xauthUsers
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getUser(XauthUsers xauthUsers) throws Exception {		
		if (xauthUsers != null) {
			List<Map<String, Object>> dataList = xauthMapper.searchUser(ConvertUtils.Object2Map(xauthUsers));
			if (dataList != null && dataList.size() > 0) {		
				dataList = ConvertUtils.Map2Camel(dataList);
				return dataList.get(0);
			}
		}		
		return null;
	}
	
	public String getDirectManager(Map<String, Object> keyData) throws Exception {
		
		List<UserInfoExt> dataList = null;
		String directManager = "";
		if(keyData.size() > 0){
			QueryWrapper<UserInfoExt> queryWrapper = new QueryWrapper<UserInfoExt>();
			queryWrapper.eq("APP_ID", APP_ID);
			queryWrapper.eq("IDEN_ID", MapUtils.getString(keyData, "idenId"));
			queryWrapper.eq("USER_ID", MapUtils.getString(keyData, "userId"));
			dataList = userInfoExtMapper.selectList(queryWrapper);
			if(dataList != null && dataList.size() > 0) {
				directManager = dataList.get(0).getDirectManager();
			}
		}
		
		return directManager;
	}
	
	/**
	 * 取得使用者資料
	 * @param xauthUsers
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getUserList(XauthUsers xauthUsers) throws Exception {
		if (xauthUsers != null) {
			List<Map<String, Object>> dataList = xauthMapper.searchUser(ConvertUtils.Object2Map(xauthUsers));
			if (dataList != null && dataList.size() > 0) {		
				dataList = ConvertUtils.Map2Camel(dataList);
				return dataList;
			}
		}		
		return null;
	}
	
	/**
	 * 取得使用者資料
	 * @param xauthUsers
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getUserListByIdenAndRole(Map<String, Object> params) throws Exception {
		if (params != null) {
			Map<String, Object> objParams = new HashMap<String, Object>();
			objParams.put("appId", APP_ID);
			objParams.put("idenId", MapUtils.getString(params, "idenId"));
			objParams.put("roleId", "ROLE_"+MapUtils.getString(params, "roleId"));
			List<Map<String, Object>> dataList = xauthMapper.searchUserRole(objParams);
			if (dataList != null && dataList.size() > 0) {		
				dataList = ConvertUtils.Map2Camel(dataList);
				return dataList;
			}
		}		
		return null;
	}

	/**
	 * 取得使用者的角色資料
	 * @param xauthUsers
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getUserRoleList(XauthUsers xauthUsers) throws Exception {	
		if (xauthUsers != null) {
			List<Map<String, Object>> dataList = xauthMapper.selectUserRole(xauthUsers);
			if (dataList != null && dataList.size() > 0) {		
				dataList = ConvertUtils.Map2Camel(dataList);
				return dataList;
			}
		}		
		return null;
	}
	
	/**
	 * 取得使用者的角色資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult getUserRoleList(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);
		String idenId = MapUtils.getString(params, "idenId");
		
		if (StringUtils.isBlank(idenId)) {
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
			return result;
		}
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		
		if (userContext.getCurrentUser().getUserType().equals(USER_TYPE.USER.getCode())) {
			XauthUsers xauthUsers = new XauthUsers();
			xauthUsers.setAppId(APP_ID);
			xauthUsers.setIdenId(idenId);
			xauthUsers.setUserId(userContext.getCurrentUser().getUserId());
			dataList = getUserRoleList(xauthUsers);
		}
		else {
			XauthRole xauthRole = new XauthRole();
			xauthRole.setAppId(APP_ID);
			xauthRole.setIdenId(idenId);
			dataList = getRoleList(xauthRole);
		}
		if (dataList != null && dataList.size() > 0) {
			result.setStatus(ProcessResult.OK);
			result.addResult("dataList", ConvertUtils.Map2Camel(dataList));
		}
		return result;
	}		
	
	/**
	 * 新增使用者資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult insertUser(Map<String, Object> params) throws Exception {
		UserInfo userInfo = userContext.getCurrentUser();
		ProcessResult result = new ProcessResult();
		if (params != null && params.size() > 0) {
			ProcessResult rs = checkUserData(params);
			if (rs.getStatus() == ProcessResult.NG) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}	
			
			if (userInfo.getUserType().equals(USER_TYPE.USER.getCode())) {
				result.setStatus(ProcessResult.NG);
				result.addMessage("您無權限新增使用者資料");
				return result;
			}
			
			XauthUsers xauthUsers = new XauthUsers();
			xauthUsers.setAppId(APP_ID);
			String idenId = MapUtils.getString(params, "idenId");
			String userId = MapUtils.getString(params, "userId");
			xauthUsers.setIdenId(idenId);
			xauthUsers.setUserId(userId);
			
			if (StringUtils.isNotBlank(MapUtils.getString(params, "userPw"))) {
				SensitiveUtils sens = new SensitiveUtils();
				SealedObject sealObject = sens.encrypt(MapUtils.getString(params, "userPw"), userInfo.getIdenId() + userInfo.getUserId());
				ProcessResult r = checkPwdService.checkPwd(idenId, userId, sens.decrypt(sealObject, userInfo.getIdenId() + userInfo.getUserId()).toString());
				if (r.getStatus().equals(ProcessResult.NG)) {
					result.setStatus(ProcessResult.NG);
					result.addMessage(r.getMessages().get(0));
					return result;
				}
				xauthUsers.setPwdhash(MapUtils.getString(params, "userPw"));
				xauthUsers.setUserPw(new BCryptPasswordEncoder().encode(sens.decrypt(sealObject, userInfo.getIdenId() + userInfo.getUserId()).toString()));
			}
			else {
				result.setStatus(ProcessResult.NG);
				result.addMessage("請輸入密碼");
				return result;
			}
			
			Date ts = new Timestamp(new Date().getTime());
			xauthUsers.setEnabled(MapUtils.getString(params, "enabled"));
			xauthUsers.setEmail(MapUtils.getString(params, "email"));
			xauthUsers.setUserCname(MapUtils.getString(params, "userCname"));
			xauthUsers.setUserType(MapUtils.getString(params, "userType"));
			xauthUsers.setIsFirst(MapUtils.getString(params, "isFirst"));
			xauthUsers.setCreUser(getCreOrUpdUser(null));
			xauthUsers.setCreDate(ts);			
			xauthUsersMapper.insert(xauthUsers);
			String[] roleIdArray = MapUtils.getString(params, "roleId").split(",");
			XauthRoleUser xauthRoleUser;
			for (String roleId : roleIdArray) {					
				xauthRoleUser = new XauthRoleUser();
				xauthRoleUser.setAppId(APP_ID);
				xauthRoleUser.setIdenId(idenId);
				xauthRoleUser.setRoleId("ROLE_" + roleId.trim());
//				xauthRoleUser.setRoleId("ROLE_" + roleId.trim().toUpperCase());
				xauthRoleUser.setUserId(userId);
				xauthRoleUser.setCreUser(getCreOrUpdUser(null));
				xauthRoleUser.setCreDate(ts);				
				xauthRoleUserMapper.insert(xauthRoleUser);
			}	
			
			insUserPdHis(idenId, userId, xauthUsers.getUserPw());
						
			QueryWrapper<UserInfoExt> queryWrapper = new QueryWrapper<UserInfoExt>();
			queryWrapper.eq("APP_ID", APP_ID);
			queryWrapper.eq("IDEN_ID", idenId);
			queryWrapper.eq("USER_ID", userId);			
			UserInfoExt userInfoExt = userInfoExtMapper.selectOne(queryWrapper);
			if (userInfoExt != null) {
				userInfoExt.setBgnDate(DateUtils.converStr2Date(MapUtils.getEmptyString(params, "bgnDate"), "yyyy/MM/dd"));			
				userInfoExt.setEndDate(DateUtils.converStr2Date(MapUtils.getEmptyString(params, "endDate"), "yyyy/MM/dd"));
				userInfoExt.setDirectManager(MapUtils.getEmptyString(params, "directManager"));
				userInfoExt.setUpdUser(getCreOrUpdUser(null));
				userInfoExt.setUpdDate(ts);				
				userInfoExtMapper.update(userInfoExt, queryWrapper);
			}
			else {
				userInfoExt = new UserInfoExt();
				userInfoExt.setAppId(APP_ID);
				userInfoExt.setIdenId(idenId);
				userInfoExt.setUserId(userId);
				userInfoExt.setBgnDate(DateUtils.converStr2Date(MapUtils.getEmptyString(params, "bgnDate"), "yyyy/MM/dd"));			
				userInfoExt.setEndDate(DateUtils.converStr2Date(MapUtils.getEmptyString(params, "endDate"), "yyyy/MM/dd"));
				userInfoExt.setDirectManager(MapUtils.getEmptyString(params, "directManager"));
				userInfoExt.setCreUser(getCreOrUpdUser(null));
				userInfoExt.setCreDate(ts);				
				userInfoExtMapper.insert(userInfoExt);
			}
						
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.INSERT_OK.getMessage());
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result;
	}
	
	/**
	 * 更新使用者資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult updateUser(Map<String, Object> params) throws Exception {
		UserInfo userInfo = userContext.getCurrentUser();
		ProcessResult result = new ProcessResult();
		if (params != null && params.size() > 0) {
			ProcessResult rs = checkUserData(params);
			if (rs.getStatus() == ProcessResult.NG) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}	
			
			String idenId = MapUtils.getString(params, "idenId");
			String userId = MapUtils.getString(params, "userId");
			
			XauthUsers xauthUsers = new XauthUsers();
			xauthUsers.setAppId(APP_ID);
			xauthUsers.setIdenId(idenId);
			xauthUsers.setUserId(userId);
			Map<String, Object> data = getUser(xauthUsers);
			if (data == null) {
				result.setStatus(ProcessResult.NG);
				result.addMessage(MSG_KEY.QUERY_EMPTY.getMessage());
				return result;
			}
			
			if (userInfo.getUserType().equals(USER_TYPE.USER.getCode())) {
				if (!userInfo.getUserId().equals(userId)) {
					result.setStatus(ProcessResult.NG);
					result.addMessage("您無權限修改使用者資料");
					return result;
				}
			}
			else if (userInfo.getUserType().equals(USER_TYPE.ADMIN.getCode())) {
				// 如果登入者與要異動的帳號為同公司，且要異動的帳號是管理者身份，且欲將帳號改為使用者
				if (userInfo.getIdenId().equals(idenId) 
						&& MapUtils.getString(data, "userType").equals(USER_TYPE.ADMIN.getCode()) 
						&& MapUtils.getString(data, "userType").equals(USER_TYPE.USER.getCode())) {
					result.setStatus(ProcessResult.NG);
					result.addMessage("您無權限修改此管理員帳號為使用者");
					return result;
				}
			}
			
			if (userInfo.getUserId().equals(userId) && "0".equals(MapUtils.getString(params, "enabled"))) {
				result.setStatus(ProcessResult.NG);
				result.addMessage("您無權限自行停用使用者帳號");
				return result;
			}
			
			if (StringUtils.isNotBlank(MapUtils.getString(params, "userPw"))) {
				SensitiveUtils sens = new SensitiveUtils();
				SealedObject sealObject = sens.encrypt(MapUtils.getString(params, "userPw"), userInfo.getIdenId() + userInfo.getUserId());
				ProcessResult r = checkPwdService.checkPwd(idenId, userId, sens.decrypt(sealObject, userInfo.getIdenId() + userInfo.getUserId()).toString());
				if (r.getStatus().equals(ProcessResult.NG)) {
					result.setStatus(ProcessResult.NG);
					result.addMessage(r.getMessages().get(0));
					return result;
				}
				xauthUsers.setUserPw(new BCryptPasswordEncoder().encode(sens.decrypt(sealObject, userInfo.getIdenId() + userInfo.getUserId()).toString()));
			}
			
			Date ts = new Timestamp(new Date().getTime());
			xauthUsers.setEnabled(MapUtils.getString(params, "enabled"));
			xauthUsers.setEmail(MapUtils.getEmptyString(params, "email"));
			xauthUsers.setUserCname(MapUtils.getString(params, "userCname"));
			xauthUsers.setUserType(MapUtils.getString(params, "userType"));
			xauthUsers.setIsFirst(MapUtils.getString(params, "isFirst"));
			xauthUsers.setUpdUser(getCreOrUpdUser(null));			
			xauthUsers.setUpdDate(ts);
			
			QueryWrapper<XauthUsers> usersWrapper = new QueryWrapper<XauthUsers>();
			usersWrapper.eq("APP_ID", APP_ID);
			usersWrapper.eq("IDEN_ID", idenId);
			usersWrapper.eq("USER_ID", userId);			
			xauthUsersMapper.update(xauthUsers, usersWrapper);
			
			// 刪除該使用者所有角色
			QueryWrapper<XauthRoleUser> roleUserWrapper = new QueryWrapper<XauthRoleUser>();
			roleUserWrapper.eq("APP_ID", APP_ID);
			roleUserWrapper.eq("IDEN_ID", idenId);
			roleUserWrapper.eq("USER_ID", userId);			
			xauthRoleUserMapper.delete(roleUserWrapper);
		
			// 新增所選角色
			String[] roleIdArray = MapUtils.getString(params, "roleId").split(",");
			for (String roleId : roleIdArray) {
				XauthRoleUser xauthRoleUser = new XauthRoleUser();
				xauthRoleUser.setAppId(APP_ID);
				xauthRoleUser.setIdenId(idenId);
				xauthRoleUser.setRoleId("ROLE_" + roleId.trim());
//				xauthRoleUser.setRoleId("ROLE_" + roleId.trim().toUpperCase());
				xauthRoleUser.setUserId(userId);
				xauthRoleUser.setCreUser(getCreOrUpdUser(null));
				xauthRoleUser.setCreDate(ts);				
				xauthRoleUserMapper.insert(xauthRoleUser);
			}			
			
			if (StringUtils.isNotBlank(MapUtils.getString(params, "userPw"))) {
				insUserPdHis(idenId, userId, xauthUsers.getUserPw());
			}
						
			QueryWrapper<UserInfoExt> userInfoExtWrapper = new QueryWrapper<UserInfoExt>();
			userInfoExtWrapper.eq("APP_ID", APP_ID);
			userInfoExtWrapper.eq("IDEN_ID", idenId);
			userInfoExtWrapper.eq("USER_ID", userId);			
			UserInfoExt userInfoExt = userInfoExtMapper.selectOne(userInfoExtWrapper);
			if (userInfoExt != null) {				
				userInfoExt.setBgnDate(DateUtils.converStr2Date(MapUtils.getEmptyString(params, "bgnDate"), "yyyy/MM/dd"));			
				userInfoExt.setEndDate(DateUtils.converStr2Date(MapUtils.getEmptyString(params, "endDate"), "yyyy/MM/dd"));
				userInfoExt.setUpdUser(getCreOrUpdUser(null));
				userInfoExt.setUpdDate(ts);	
				userInfoExt.setDirectManager(MapUtils.getEmptyString(params, "directManager"));
				
				userInfoExtMapper.update(userInfoExt, userInfoExtWrapper);
			}
			else {
				userInfoExt = new UserInfoExt();
				userInfoExt.setAppId(APP_ID);
				userInfoExt.setIdenId(idenId);
				userInfoExt.setUserId(userId);
				userInfoExt.setBgnDate(DateUtils.converStr2Date(MapUtils.getEmptyString(params, "bgnDate"), "yyyy/MM/dd"));			
				userInfoExt.setEndDate(DateUtils.converStr2Date(MapUtils.getEmptyString(params, "endDate"), "yyyy/MM/dd"));
				userInfoExt.setCreUser(getCreOrUpdUser(null));
				userInfoExt.setCreDate(ts);	
				userInfoExt.setDirectManager(MapUtils.getEmptyString(params, "directManager"));
				userInfoExtMapper.insert(userInfoExt);
			}
			
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.UPDATE_OK.getMessage());
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result;
	}
	
	/**
	 * 刪除使用者資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult deleteUser(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		if (params != null && params.size() > 0) {
			String idenId = MapUtils.getString(params, "idenId");
			String userId = MapUtils.getString(params, "userId");
						
			QueryWrapper<XauthUsers> usersWrapper = new QueryWrapper<XauthUsers>();
			usersWrapper.eq("APP_ID", APP_ID);
			usersWrapper.eq("IDEN_ID", idenId);
			usersWrapper.eq("USER_ID", userId);			
			xauthUsersMapper.delete(usersWrapper);
			
			QueryWrapper<XauthRoleUser> roleUserWrapper = new QueryWrapper<XauthRoleUser>();
			roleUserWrapper.eq("APP_ID", APP_ID);
			roleUserWrapper.eq("IDEN_ID", idenId);
			roleUserWrapper.eq("USER_ID", userId);			
			xauthRoleUserMapper.delete(roleUserWrapper);
						
			QueryWrapper<XauthUsersPwdHistory> usersPwdHistoryWrapper = new QueryWrapper<XauthUsersPwdHistory>();
			usersPwdHistoryWrapper.eq("APP_ID", APP_ID);
			usersPwdHistoryWrapper.eq("IDEN_ID", idenId);
			usersPwdHistoryWrapper.eq("USER_ID", userId);			
			xauthUsersPwdHistoryMapper.delete(usersPwdHistoryWrapper);
			
			QueryWrapper<UserInfoExt> userInfoExtWrapper = new QueryWrapper<UserInfoExt>();
			userInfoExtWrapper.eq("APP_ID", APP_ID);
			userInfoExtWrapper.eq("IDEN_ID", idenId);
			userInfoExtWrapper.eq("USER_ID", userId);			
			userInfoExtMapper.delete(userInfoExtWrapper);
			//刪除使用者為供應商
			if(StringUtils.equals("999999999", idenId)) {
				QueryWrapper<Suppliermaster> suppliermasterWrapper = new QueryWrapper<Suppliermaster>();
				suppliermasterWrapper.eq("IDENID", idenId);
				suppliermasterWrapper.eq("SUPPLIERID", userId);
				suppliermasterMapper.delete(suppliermasterWrapper);
			}
			
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.DELETE_OK.getMessage());
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result;
	}
	
	/**
	 * 新增使用者密碼歷史資料
	 * @param idenId
	 * @param userId
	 * @param p
	 * @throws Exception
	 */
	private void insUserPdHis(String idenId, String userId, String p) throws Exception {
		XauthUsersPwdHistory xauthUsersPwdHistory = new XauthUsersPwdHistory();
		xauthUsersPwdHistory.setAppId(APP_ID);
		xauthUsersPwdHistory.setIdenId(idenId);
		xauthUsersPwdHistory.setUserId(userId);
		xauthUsersPwdHistory.setUserPw(p);
		xauthUsersPwdHistory.setCreUser(getCreOrUpdUser(null));
		xauthUsersPwdHistory.setCreDate(new Timestamp(new Date().getTime()));		
		xauthUsersPwdHistoryMapper.insert(xauthUsersPwdHistory);
	}
	
	/**
	 * 檢查使用者資料
	 * @param params
	 * @return
	 */
	private ProcessResult checkUserData(Map<String, Object> params) {
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);
		try {
			if (StringUtils.isBlank(MapUtils.getString(params, "idenId"))) {
				result.addMessage("請選擇公司");
				return result;
			}
			
			if (StringUtils.isBlank(MapUtils.getString(params, "roleId"))) {
				result.addMessage("請選擇角色");
				return result;
			}		
			
			if (StringUtils.isBlank(MapUtils.getString(params, "userId"))) {
				result.addMessage("請輸入使用者帳號");
				return result;
			}	
			
			if (StringUtils.isBlank(MapUtils.getString(params, "userCname"))) {
				result.addMessage("請輸入使用者姓名");
				return result;
			}
			
			if (StringUtils.isBlank(MapUtils.getString(params, "bgnDate"))) {
				result.addMessage("請輸入起始日期");
				return result;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
			return result;
		}
		result.setStatus(ProcessResult.OK);
		return result;
	}		
	
	/**
	 * 選單資料分頁查詢
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult searchMenuPage(Map<String, Object> params) throws Exception {	
		GridResult gridResult = this.grid(params);	
		Map<String, Object> objParams = new HashMap<String, Object>();
		objParams.put("appId", APP_ID);
		objParams.put("menuId", MapUtils.getString(params, "menuId"));
		objParams.put("menuCname", MapUtils.getString(params, "menuCname"));
		objParams.put("sortColumnName", MapUtils.getString(params, "sortColumnName"));
		objParams.put("sortOrder", MapUtils.getString(params, "sortOrder"));
		commonDao.query(gridResult, "com.tradevan.mapper.xauth.dao.XauthMapper.searchMenuMain", objParams);
		return gridResult;
	}
	
	/**
	 * 取得單筆選單資料
	 * @param xauthMenu
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getMenu(XauthMenu xauthMenu) throws Exception {		
		if (xauthMenu != null) {
			List<Map<String, Object>> dataList = xauthMapper.selectMenuMain(ConvertUtils.Object2Map(xauthMenu));
			if (dataList != null && dataList.size() > 0) {				
				return ConvertUtils.Map2Camel(dataList.get(0));
			}
		}		
		return null;
	}
	
	/**
	 * 新增選單資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult insertMenu(Map<String, Object> params) throws Exception {	
		ProcessResult result = new ProcessResult();
		if (!userContext.getCurrentUser().getUserType().equals(USER_TYPE.SYS_ADMIN.getCode())) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("您無權限新增選單資料");
			return result;
		}
		if (params != null && params.size() > 0) {
			ProcessResult rs = checkMenuData(params);
			if (rs.getStatus().equals(ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}
			
			params.put("appId", APP_ID);
			
			String parentId = MapUtils.getEmptyString(params, "parentId");
			
			if (StringUtils.isBlank(parentId)) {
				result.setStatus(ProcessResult.NG);
				result.addMessage("請選擇上層選單編號");
				return result;
			}
			
			String[] pa = parentId.split("#");
			params.put("parentId", pa[0]);
			params.put("seqNo", pa[1]);
			params.put("creUser", getCreOrUpdUser(null));
			params.put("creDate", new Timestamp(new Date().getTime()));
			
			if (xauthMapper.insertMenu(params) > 0) {
				result.setStatus(ProcessResult.OK);
				result.addMessage(MSG_KEY.INSERT_OK.getMessage());
			}
			else {
				result.setStatus(ProcessResult.NG);
				result.addMessage(MSG_KEY.INSERT_FAIL.getMessage());
			}	
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}	
		return result;
	}
	
	/**
	 * 更新選單資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult updateMenu(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		if (!userContext.getCurrentUser().getUserType().equals(USER_TYPE.SYS_ADMIN.getCode())) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("您無權限更新選單資料");
			return result;
		}
		if (params != null && params.size() > 0) {
			ProcessResult rs = checkMenuData(params);
			if (rs.getStatus().equals(ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}
			
			String menuId = MapUtils.getString(params, "menuId");
			if (StringUtils.isNotBlank(menuId)) {
				XauthMenu xauthMenu = new XauthMenu();
				xauthMenu.setAppId(APP_ID);
				xauthMenu.setMenuId(menuId);
				Map<String, Object> data = getMenu(xauthMenu);
				if (data == null) {
					result.setStatus(ProcessResult.NG);
					result.addMessage(MSG_KEY.QUERY_EMPTY.getMessage());
					return result;
				}
				
				params.put("appId", APP_ID);
				
				String dataParendId = MapUtils.getEmptyString(data, "parentId");
				String paramParentId = MapUtils.getEmptyString(params, "parentId");
				
				if (!dataParendId.equals("#") && StringUtils.isBlank(paramParentId)) {
					result.setStatus(ProcessResult.NG);
					result.addMessage("請選擇上層選單編號");
					return result;
				}
				
				if (dataParendId.equals("#") && StringUtils.isNotBlank(paramParentId)) {
					result.setStatus(ProcessResult.NG);
					result.addMessage("ROOT無法移至其他的節點");
					return result;
				}
				
				String[] pa = paramParentId.split("#");
				String parentId = pa[0];
				
				if (StringUtils.isEmpty(parentId) && dataParendId.equals("#")) {
					params.remove("parentId");
					params.remove("seqNo");
					params.remove("parentSeq");
				}
				else if (!dataParendId.equals(parentId)) {
					params.put("parentId", parentId);
					
					// 查詢更換的父層功能代號及順序
					XauthMenu m = new XauthMenu();
					m.setAppId(APP_ID);
					m.setMenuId(parentId);
					Map<String, Object> menuData = getMenu(m);
					String parentSeqNo = menuData.get("seqNo").toString();
					params.put("parentSeq", parentSeqNo);
					
					// 查詢該父層功能代號下是否有其他的子功能
					// 若有則取最大一筆資料的順序+1
					String seqNo = "";
					m = new XauthMenu();
					m.setAppId(APP_ID);
					m.setParentId(parentId);
					List<Map<String, Object>> dataList = xauthMapper.selectMenuMain(ConvertUtils.Object2Map(m));	
					dataList = ConvertUtils.Map2Camel(dataList);
					if (dataList != null && dataList.size() > 0) {
						seqNo = String.valueOf(Integer.parseInt(dataList.get(dataList.size() - 1).get("seqNo").toString()) + 1);
						params.put("seqNo", seqNo);				
					}
					else {
						params.put("seqNo", "1");
					}
				}
				else {
					params.remove("parentId");
					params.remove("seqNo");
					params.remove("parentSeq");
				}
				
				params.put("updUser", getCreOrUpdUser(null));
				params.put("updDate", new Timestamp(new Date().getTime()));
							
				if (xauthMapper.updateMenu(params) > 0) {
					result.setStatus(ProcessResult.OK);
					result.addMessage(MSG_KEY.UPDATE_OK.getMessage());
				}
				else {
					result.setStatus(ProcessResult.NG);
					result.addMessage(MSG_KEY.UPDATE_FAIL.getMessage());
				}
			}	
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result;
	}
	
	/**
	 * 刪除選單資料
	 * @param result
	 * @param params
	 * @throws Exception
	 */
	public ProcessResult deleteMenu(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		if (!userContext.getCurrentUser().getUserType().equals(USER_TYPE.SYS_ADMIN.getCode())) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("您無權限刪除選單資料");
			return result;
		}
		if (params != null && params.size() > 0) {
			String menuId = MapUtils.getString(params, "menuId");
			if (StringUtils.isNotBlank(menuId)) {
				QueryWrapper<XauthMenu> menuWrapper = new QueryWrapper<XauthMenu>();
				menuWrapper.eq("APP_ID", APP_ID);
				menuWrapper.eq("MENU_ID", menuId);				
				xauthMenuMapper.delete(menuWrapper);
								
				QueryWrapper<XauthIdenMenu> idenMenuWrapper = new QueryWrapper<XauthIdenMenu>();
				idenMenuWrapper.eq("APP_ID", APP_ID);
				idenMenuWrapper.eq("MENU_ID", menuId);				
				xauthIdenMenuMapper.delete(idenMenuWrapper);
				
				QueryWrapper<XauthRoleMenu> roleMenuWrapper = new QueryWrapper<XauthRoleMenu>();
				roleMenuWrapper.eq("APP_ID", APP_ID);
				roleMenuWrapper.eq("MENU_ID", menuId);				
				xauthRoleMenuMapper.delete(roleMenuWrapper);
				
				result.setStatus(ProcessResult.OK);
				result.addMessage(MSG_KEY.DELETE_OK.getMessage());
			}
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result;
	}
	
	/**
	 * 檢查選單資料
	 * @param params
	 * @return
	 */
	private ProcessResult checkMenuData(Map<String, Object> params) {
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);
		try {
			if (StringUtils.isBlank(MapUtils.getString(params, "menuId"))) {
				result.addMessage("請輸入選單編號");
				return result;
			}
			
			if (StringUtils.isBlank(MapUtils.getString(params, "menuCname"))) {
				result.addMessage("請輸入選單名稱");
				return result;
			}								
		}
		catch (Exception e) {
			e.printStackTrace();
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
			return result;
		}
		result.setStatus(ProcessResult.OK);
		return result;
	}
	
	/**
	 * 選單樹狀圖
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult getMenuTree(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();	
		List<Map<String, Object>> dataList = xauthMapper.selectMenuTree(APP_ID);
		if (dataList != null && dataList.size() > 0) {
			dataList = ConvertUtils.Map2Camel(dataList);
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			String icon = "";
			for (Map<String, Object> rs : dataList) {
				if (rs.get("lv").toString().equals("1")) {
					icon = servletContext.getContextPath() + "/resources/jstree/tree_icon.png";	
				}	
				else if (rs.get("lv").toString().equals("2")) {					
					icon = servletContext.getContextPath() + "/resources/jstree/0810_Icon_01S.gif";		
				}
				else {					
					icon = servletContext.getContextPath() + "/resources/jstree/0810_Icon_03S.gif";
				}
				rs.put("icon", icon);
				resultList.add(rs);					
			}
			result.addResult("data", ConvertUtils.Map2Lower(dataList));
		}
		return result;
	}
	
	/**
	 * 儲存選單
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ProcessResult saveMenuTree(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();	
		if (params != null && params.size() > 0) {
			String parentId = MapUtils.getString(params, "parentId");
			XauthMenu xauthMenu = new XauthMenu();
			xauthMenu.setAppId(APP_ID);
			xauthMenu.setParentId(parentId);
			
			QueryWrapper<XauthMenu> queryWrapper = new QueryWrapper<XauthMenu>();
			queryWrapper.eq("APP_ID", APP_ID);
			queryWrapper.eq("MENU_ID", parentId);
			XauthMenu mData = xauthMenuMapper.selectOne(queryWrapper);
			xauthMenu.setParentSeq(mData.getSeqNo());
				
			String item = "";
			List<String> itemList = new Gson().fromJson(params.get("itemList").toString(), List.class);
			for (int i=0 ; i<itemList.size() ; i++) {
				item = itemList.get(i).toString();
				xauthMenu.setMenuId(item);
				xauthMenu.setSeqNo((i + 1));
				
				queryWrapper = new QueryWrapper<XauthMenu>();
				queryWrapper.eq("APP_ID", APP_ID);
				queryWrapper.eq("MENU_ID", item);				
				xauthMenuMapper.update(xauthMenu, queryWrapper);
			}
			
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.SUCCESS.getMessage());
		}	
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage("無效的操作，查無上層代號");
		}
		return result;
	}
	
	/**
	 * 公司選單樹狀圖
	 * @param result
	 * @param params
	 * @throws Exception
	 */
	public ProcessResult getIdenMenuTree(Map<String, Object> params) throws Exception {	
		ProcessResult result = new ProcessResult();
		if (userContext.getCurrentUser().getUserType().equals(USER_TYPE.USER.getCode())) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("您無權限取得資料");
			return result;
		}
		
		String idenId = MapUtils.getString(params, "idenId");
		if (StringUtils.isBlank(idenId)) {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
			return result;
		}
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		
		// 取得該公司現有的選單
		XauthIdenMenu xauthIdenMenu = new XauthIdenMenu();
		xauthIdenMenu.setAppId(APP_ID);
		xauthIdenMenu.setIdenId(idenId);
		List<Map<String, Object>> idenMenuList = xauthMapper.selectIdenMenuTree(xauthIdenMenu);
		idenMenuList = ConvertUtils.Map2Camel(idenMenuList);
		
		// 若為系統管理者，可取得所有選單
		List<Map<String, Object>> menuList = null;
		if (userContext.getCurrentUser().getUserType().equals(USER_TYPE.SYS_ADMIN.getCode())) {
			menuList = xauthMapper.selectMenuTree(APP_ID);
			menuList = ConvertUtils.Map2Camel(menuList);
		}
		else {
			xauthIdenMenu = new XauthIdenMenu();
			xauthIdenMenu.setAppId(APP_ID);
			xauthIdenMenu.setIdenId(userContext.getCurrentUser().getIdenId());
			menuList  = xauthMapper.selectIdenTree(xauthIdenMenu);
			menuList = ConvertUtils.Map2Camel(menuList);
		}
		
		if (menuList != null && idenMenuList != null) {
			String icon = "";
			Map<String, Object> state;
			for (Map<String, Object> rs : menuList) {
				for (Map<String, Object> ds : idenMenuList) {
					if (rs.get("id").equals(ds.get("id"))) {
						state = new HashMap<String, Object>();
						state.put("selected", "true");
						rs.put("state", state);
					}
				}
				if (rs.get("lv").toString().equals("1")) {
					icon = servletContext.getContextPath() + "/resources/jstree/tree_icon.png";	
				}	
				else if (rs.get("lv").toString().equals("2")) {					
					icon = servletContext.getContextPath() + "/resources/jstree/0810_Icon_01S.gif";		
				}
				else {					
					icon = servletContext.getContextPath() + "/resources/jstree/0810_Icon_03S.gif";
				}
				rs.put("icon", icon);
				resultList.add(rs);					
			}
		}
		else {
			String icon = "";
			if (idenMenuList != null && idenMenuList.size() > 0) {
				for (Map<String, Object> rs : idenMenuList) {
					if (rs.get("lv").toString().equals("0") || rs.get("lv").toString().equals("1")) {
						icon = servletContext.getContextPath() + "/resources/jstree/tree_icon.png";	
					}	
					else if (rs.get("lv").toString().equals("2")) {					
						icon = servletContext.getContextPath() + "/resources/jstree/0810_Icon_01S.gif";		
					}
					else {					
						icon = servletContext.getContextPath() + "/resources/jstree/0810_Icon_03S.gif";
					}
					rs.put("icon", icon);
					resultList.add(rs);
				}
			}			
		}
		
		result.setStatus(ProcessResult.OK);
		result.addResult("data", ConvertUtils.Map2Lower(resultList));
		return result;
	}
	
	/**
	 * 儲存公司選單
	 * @param result
	 * @param params
	 * @throws Exception
	 */
	public ProcessResult saveIdenMenuTree(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();		
		if (params != null && params.size() > 0) {
			String[] menuArray = params.get("menuArray").toString().split(",");
			if (menuArray != null && menuArray.length > 0) {
				String idenId = params.get("idenId").toString();				
				QueryWrapper<XauthIdenMenu> idenMenuWrapper = new QueryWrapper<XauthIdenMenu>();
				idenMenuWrapper.eq("APP_ID", APP_ID);
				idenMenuWrapper.eq("IDEN_ID", idenId);				
				xauthIdenMenuMapper.delete(idenMenuWrapper);
				
				List<XauthIdenMenu> idenMenuList = new ArrayList<XauthIdenMenu>();
				Date ts = new Timestamp(new Date().getTime());
				XauthIdenMenu xauthIdenMenu = null;
				for (String menuId : menuArray) {
					xauthIdenMenu = new XauthIdenMenu();
					xauthIdenMenu.setAppId(APP_ID);
					xauthIdenMenu.setIdenId(idenId);
					xauthIdenMenu.setMenuId(menuId);
					xauthIdenMenu.setCreUser(getCreOrUpdUser(null));
					xauthIdenMenu.setCreDate(ts);
					xauthIdenMenu.setUpdUser(getCreOrUpdUser(null));
					xauthIdenMenu.setUpdDate(ts);
					xauthIdenMenu.insert();
					idenMenuList.add(xauthIdenMenu);
				}
				
				List<Map<String, Object>> idenMenuMapList = ConvertUtils.Object2MapList(idenMenuList);
				
				List<XauthRoleMenu> delList = new ArrayList<XauthRoleMenu>();
				
				XauthRole xauthRole = new XauthRole();
				xauthRole.setAppId(APP_ID);
				xauthRole.setIdenId(idenId);
				List<Map<String, Object>> roleList = xauthMapper.searchRole(ConvertUtils.Object2Map(xauthRole));
				roleList = ConvertUtils.Map2Camel(roleList);
				if (roleList != null && roleList.size() > 0) {
					QueryWrapper<XauthRoleMenu> roleMenuWrapper;
					for (Map<String, Object> rmap : roleList) {
						String roleId = "ROLE_" + rmap.get("roleId").toString();															
						roleMenuWrapper = new QueryWrapper<XauthRoleMenu>();
						roleMenuWrapper.eq("APP_ID", APP_ID);
						roleMenuWrapper.eq("IDEN_ID", idenId);
						roleMenuWrapper.eq("ROLE_ID", roleId);						
						List<XauthRoleMenu> roleMenuList = xauthRoleMenuMapper.selectList(roleMenuWrapper);
						if (roleMenuList != null && roleMenuList.size() > 0) {
							boolean flag = false;
							for (XauthRoleMenu m : roleMenuList) {
								flag = false;
								for (Map<String, Object> n : idenMenuMapList) {
									if (m.getMenuId().equals(n.get("menuId"))) {
										flag = true;
										break;
									}
								}
								if (!flag) {
									delList.add(m);
								}
							}
						}
					}
					
					if (delList != null && delList.size() > 0) {
						QueryWrapper<XauthRoleMenu> wrapper = null;
						for (XauthRoleMenu d : delList) {
							wrapper = new QueryWrapper<XauthRoleMenu>();
							wrapper.eq("APP_ID", APP_ID);
							wrapper.eq("IDEN_ID", d.getIdenId());
							wrapper.eq("MENU_ID", d.getMenuId());
							wrapper.eq("ROLE_ID", d.getRoleId());							
							xauthRoleMenuMapper.delete(wrapper);
						}
					}
				}
				
				result.setStatus(ProcessResult.OK);
				result.addMessage(MSG_KEY.SUCCESS.getMessage());
			}
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result;
	}
	
	/**
	 * 公司角色選單樹狀圖
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult getIdenRoleMenuTree(Map<String, Object> params) throws Exception {	
		ProcessResult result = new ProcessResult();		
		if (userContext.getCurrentUser().getUserType().equals(USER_TYPE.USER.getCode())) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("您無權限取得資料");
			return result;
		}
		if (params != null && params.size() > 0) {
			String idenId = MapUtils.getString(params, "idenId");
			String roleId = MapUtils.getString(params, "roleId");
			if (StringUtils.isBlank(idenId) && StringUtils.isBlank(roleId)) {
				result.setStatus(ProcessResult.NG);
				result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
				result.addResult("data", "");
				return result;
			}
			
			roleId = "ROLE_" + roleId;
			
			List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
					
			XauthRoleMenu xauthRoleMenu = new XauthRoleMenu();
			xauthRoleMenu.setAppId(APP_ID);
			xauthRoleMenu.setIdenId(idenId);
			xauthRoleMenu.setRoleId(roleId);
			List<Map<String, Object>> roleMenuList = xauthMapper.selectRoleMenuTree(xauthRoleMenu);
			roleMenuList = ConvertUtils.Map2Camel(roleMenuList);
				
			XauthIdenMenu xauthIdenMenu = new XauthIdenMenu();
			xauthIdenMenu.setAppId(APP_ID);
			xauthIdenMenu.setIdenId(idenId);
			List<Map<String, Object>> menuList = xauthMapper.selectIdenTree(xauthIdenMenu);
			menuList = ConvertUtils.Map2Camel(menuList);
			
			if (menuList != null && roleMenuList != null) {
				String icon = "";
				Map<String, Object> state;
				for (Map<String, Object> rs : menuList) {
					for (Map<String, Object> ds : roleMenuList) {
						if (rs.get("id").equals(ds.get("id"))) {
							state = new HashMap<String, Object>();
							state.put("selected", "true");
							rs.put("state", state);
						}
					}
					if (rs.get("lv").toString().equals("1")) {
						icon = servletContext.getContextPath() + "/resources/jstree/tree_icon.png";	
					}	
					else if (rs.get("lv").toString().equals("2")) {					
						icon = servletContext.getContextPath() + "/resources/jstree/0810_Icon_01S.gif";		
					}
					else {					
						icon = servletContext.getContextPath() + "/resources/jstree/0810_Icon_03S.gif";
					}
					rs.put("icon", icon);
					resultList.add(rs);					
				}
			}
			else {
				String icon = "";
				if (roleMenuList != null && roleMenuList.size() > 0) {
					for (Map<String, Object> rs : roleMenuList) {
						if (rs.get("lv").toString().equals("0") || rs.get("lv").toString().equals("1")) {
							icon = servletContext.getContextPath() + "/resources/jstree/tree_icon.png";	
						}	
						else if (rs.get("lv").toString().equals("2")) {					
							icon = servletContext.getContextPath() + "/resources/jstree/0810_Icon_01S.gif";		
						}
						else {					
							icon = servletContext.getContextPath() + "/resources/jstree/0810_Icon_03S.gif";
						}
						rs.put("icon", icon);
						resultList.add(rs);
					}
				}				
			}
			
			result.addResult("data", ConvertUtils.Map2Lower(resultList));
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());			
		}
		return result;
	}
	
	/**
	 * 儲存公司角色選單
	 * @param result
	 * @param params
	 * @throws Exception
	 */
	public ProcessResult saveIdenRoleMenu(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();		
		if (params != null && params.size() > 0) {
			String idenId = MapUtils.getString(params, "idenId");
			String roleId = "ROLE_" + MapUtils.getString(params, "roleId");//.toUpperCase();
			QueryWrapper<XauthRoleMenu> roleMenuWrapper = new QueryWrapper<XauthRoleMenu>();
			roleMenuWrapper.eq("APP_ID", APP_ID);
			roleMenuWrapper.eq("IDEN_ID", idenId);
			roleMenuWrapper.eq("ROLE_ID", roleId);			
			xauthRoleMenuMapper.delete(roleMenuWrapper);
			
			String[] menuArray = params.get("menuArray").toString().split(",");
			if (menuArray != null && menuArray.length > 0) {
				Date ts = new Timestamp(new Date().getTime());
				XauthRoleMenu xauthRoleMenu = null;
				for (String menuId : menuArray) {
					xauthRoleMenu = new XauthRoleMenu();
					xauthRoleMenu.setAppId(APP_ID);
					xauthRoleMenu.setIdenId(idenId);
					xauthRoleMenu.setMenuId(menuId);
					xauthRoleMenu.setRoleId(roleId);
					xauthRoleMenu.setCreUser(getCreOrUpdUser(null));
					xauthRoleMenu.setCreDate(ts);
					xauthRoleMenu.setUpdUser(getCreOrUpdUser(null));
					xauthRoleMenu.setUpdDate(ts);					
					xauthRoleMenuMapper.insert(xauthRoleMenu);
				}
				result.setStatus(ProcessResult.OK);
				result.addMessage(MSG_KEY.SUCCESS.getMessage());
			}
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result;
	}
	
	/**
	 * IP授權資料分頁查詢
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult searchIpGrantPage(Map<String, Object> params) throws Exception {
		GridResult gridResult = this.grid(params);		
		Map<String, Object> objParams = new HashMap<String, Object>();
		UserInfo userInfo = userContext.getCurrentUser();
		String userType = userInfo.getUserType();
		String idenId = MapUtils.getString(params, "idenId");
		if (!userType.equals(USER_TYPE.SYS_ADMIN.getCode())) {
			idenId = userInfo.getIdenId();
		}
		objParams.put("appId", APP_ID);
		objParams.put("idenId", idenId);
		objParams.put("ipAddr", MapUtils.getString(params, "ipAddr"));
		objParams.put("sysType", MapUtils.getString(params, "sysType"));
		objParams.put("enabled", MapUtils.getString(params, "enabled"));
		objParams.put("sortColumnName", MapUtils.getString(params, "sortColumnName"));
		objParams.put("sortOrder", MapUtils.getString(params, "sortOrder"));
		commonDao.query(gridResult, "com.tradevan.mapper.xauth.dao.XauthMapper.searchIpGrant", objParams);		
		return gridResult;
	}
	
	/**
	 * 取得單筆IP授權資料
	 * @param xauthIpGrantParams
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getIpGrant(XauthIpGrant xauthIpGrantParams) throws Exception {		
		if (xauthIpGrantParams != null) {
			List<Map<String, Object>> dataList = xauthMapper.selectIpGrant(ConvertUtils.Object2Map(xauthIpGrantParams));
			if (dataList != null && dataList.size() > 0) {				
				return ConvertUtils.Map2Camel(dataList.get(0));
			}
		}		
		return null;
	}
	
	/**
	 * 新增IP授權資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult insertIpGrant(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		if (userContext.getCurrentUser().getUserType().equals(USER_TYPE.USER.getCode())) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("您無權限新增IP授權資料");
			return result;
		}

		if (params != null && params.size() > 0) {
			ProcessResult rs = checkIpGrantData(params);
			if (rs.getStatus().equals(ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}	
			
			XauthIpGrant xauthIpGrant = new XauthIpGrant();
			xauthIpGrant.setAppId(APP_ID);
			xauthIpGrant.setIdenId(MapUtils.getString(params, "idenId"));
			xauthIpGrant.setIpAddr(MapUtils.getString(params, "ipAddr"));
			xauthIpGrant.setSysType(MapUtils.getString(params, "sysType"));
			xauthIpGrant.setEnabled(MapUtils.getString(params, "enabled"));
			xauthIpGrant.setMemo(MapUtils.getString(params, "memo"));
			xauthIpGrant.setCreUser(getCreOrUpdUser(null));
			xauthIpGrant.setCreDate(new Timestamp(new Date().getTime()));			
			xauthIpGrantMapper.insert(xauthIpGrant);
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.INSERT_OK.getMessage());
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}			
		return result;
	}
	
	/**
	 * 更新IP授權資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult updateIpGrant(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		if (userContext.getCurrentUser().getUserType().equals(USER_TYPE.USER.getCode())) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("您無權限更新IP授權資料");
			return result;
		}
		
		if (params != null && params.size() > 0) {
			ProcessResult rs = checkIpGrantData(params);
			if (rs.getStatus().equals(ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}	
			
			XauthIpGrant xauthIpGrant = new XauthIpGrant();
			xauthIpGrant.setAppId(APP_ID);
			xauthIpGrant.setIdenId(MapUtils.getString(params, "idenId"));
			xauthIpGrant.setIpAddr(MapUtils.getString(params, "ipAddr"));
			xauthIpGrant.setSysType(MapUtils.getString(params, "sysType"));
			xauthIpGrant.setEnabled(MapUtils.getString(params, "enabled"));
			xauthIpGrant.setMemo(MapUtils.getEmptyString(params, "memo"));
			xauthIpGrant.setUpdUser(getCreOrUpdUser(null));
			xauthIpGrant.setUpdDate(new Timestamp(new Date().getTime()));	
			
			QueryWrapper<XauthIpGrant> quertWrapper = new QueryWrapper<XauthIpGrant>();
			quertWrapper.eq("APP_ID", APP_ID);
			quertWrapper.eq("IDEN_ID", MapUtils.getString(params, "idenId"));
			quertWrapper.eq("IP_ADDR", MapUtils.getString(params, "ipAddr"));
			quertWrapper.eq("SYS_TYPE", MapUtils.getString(params, "sysType"));			
			xauthIpGrantMapper.update(xauthIpGrant, quertWrapper);
			
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.UPDATE_OK.getMessage());
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result;
	}
	
	/**
	 * 刪除IP授權資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult deleteIpGrant(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		if (userContext.getCurrentUser().getUserType().equals(USER_TYPE.USER.getCode())) {
			result.setStatus(ProcessResult.NG);
			result.addMessage("您無權限刪除IP授權資料");
			return result;
		}
		
		if (params != null && params.size() > 0) {
			ProcessResult rs = checkIpGrantData(params);
			if (rs.getStatus().equals(ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}	
				
			QueryWrapper<XauthIpGrant> queryWrapper = new QueryWrapper<XauthIpGrant>();
			queryWrapper.eq("APP_ID", APP_ID);
			queryWrapper.eq("IDEN_ID", MapUtils.getString(params, "idenId"));
			queryWrapper.eq("IP_ADDR", MapUtils.getString(params, "ipAddr"));
			queryWrapper.eq("SYS_TYPE", MapUtils.getString(params, "sysType"));			
			xauthIpGrantMapper.delete(queryWrapper);
			
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.DELETE_OK.getMessage());
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result;
	}
	
	/**
	 * 檢查IP授權資料
	 * @param params
	 * @return
	 */
	private ProcessResult checkIpGrantData(Map<String, Object> params) {
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);
		try {
			if (StringUtils.isBlank(MapUtils.getString(params, "idenId"))) {
				result.addMessage("請輸入識別碼");
				return result;
			}
			
			if (StringUtils.isBlank(MapUtils.getString(params, "ipAddr"))) {
				result.addMessage("請輸入IP位址");
				return result;
			}		
			
			if (StringUtils.isBlank(MapUtils.getString(params, "sysType"))) {
				result.addMessage("請選擇系統別");
				return result;
			}	
		}
		catch (Exception e) {
			e.printStackTrace();
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
			return result;
		}
		result.setStatus(ProcessResult.OK);
		return result;
	}
	
	/**
	 * 系統代碼分頁查詢
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult searchSysCodePage(Map<String, Object> params) throws Exception {		
		GridResult gridResult = this.grid(params);		
		Map<String, Object> objParams = new HashMap<String, Object>();
		UserInfo userInfo = userContext.getCurrentUser();
		String userType = userInfo.getUserType();
		String idenId = MapUtils.getString(params, "idenId");
		if (!userType.equals(USER_TYPE.SYS_ADMIN.getCode())) {
			idenId = userInfo.getIdenId();
		}
		objParams.put("appId", APP_ID);
		objParams.put("idenId", idenId);
		objParams.put("gp", MapUtils.getString(params, "gp"));
		objParams.put("code", MapUtils.getString(params, "code"));
		objParams.put("sortColumnName", MapUtils.getString(params, "sortColumnName"));
		objParams.put("sortOrder", MapUtils.getString(params, "sortOrder"));
		commonDao.query(gridResult, "com.tradevan.mapper.xauth.dao.XauthMapper.searchSysCode", objParams);		
		return gridResult;
	}
	
	/**
	 * 取得單筆系統代碼資料
	 * @param xauthSysCodeParams
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSysCode(XauthSysCode xauthSysCodeParams) throws Exception {		
		if (xauthSysCodeParams != null) {
			List<Map<String, Object>> dataList = xauthMapper.selectSysCode(ConvertUtils.Object2Map(xauthSysCodeParams));
			if (dataList != null && dataList.size() > 0) {				
				return ConvertUtils.Map2Camel(dataList.get(0));
			}
		}		
		return null;
	}
	
	/**
	 * 新增系統代碼資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult insertSysCode(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		if (params != null && params.size() > 0) {
			ProcessResult rs = checkSysCodeData(params);
			if (rs.getStatus().equals(ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}	
			
			XauthSysCode xauthSysCode = new XauthSysCode();
			xauthSysCode.setAppId(APP_ID);
			xauthSysCode.setIdenId(MapUtils.getString(params, "idenId"));
			xauthSysCode.setGp(MapUtils.getString(params, "gp"));
			xauthSysCode.setCode(MapUtils.getString(params, "code"));
			xauthSysCode.setCname(MapUtils.getEmptyString(params, "cname"));
			xauthSysCode.setEname(MapUtils.getEmptyString(params, "ename"));
			xauthSysCode.setOrderSeq(MapUtils.getInteger(params, "orderSeq"));
			xauthSysCode.setEnabled(MapUtils.getEmptyString(params, "enabled"));			
			xauthSysCode.setBgnDate(DateUtils.converStr2Date(MapUtils.getEmptyString(params, "bgnDate"), "yyyy/MM/dd"));			
			xauthSysCode.setEndDate(DateUtils.converStr2Date(MapUtils.getEmptyString(params, "endDate"), "yyyy/MM/dd"));
			xauthSysCode.setMemo(MapUtils.getEmptyString(params, "memo"));
			xauthSysCode.setC01(MapUtils.getEmptyString(params, "c01"));
			xauthSysCode.setC02(MapUtils.getEmptyString(params, "c02"));
			xauthSysCode.setC03(MapUtils.getEmptyString(params, "c03"));
			xauthSysCode.setC04(MapUtils.getEmptyString(params, "c04"));
			xauthSysCode.setC05(MapUtils.getEmptyString(params, "c05"));
			xauthSysCode.setCreUser(getCreOrUpdUser(null));
			xauthSysCode.setCreDate(new Timestamp(new Date().getTime()));							
			xauthSysCodeMapper.insert(xauthSysCode);
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.INSERT_OK.getMessage());
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}			
		return result;
	}
	
	/**
	 * 更新系統代碼資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult updateSysCode(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		if (params != null && params.size() > 0) {
			ProcessResult rs = checkSysCodeData(params);
			if (rs.getStatus().equals(ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}	
			String idenId = MapUtils.getString(params, "idenId");
			String gp = MapUtils.getString(params, "gp");
			String code = MapUtils.getString(params, "code");
			XauthSysCode xauthSysCode = new XauthSysCode();
			xauthSysCode.setAppId(APP_ID);
			xauthSysCode.setIdenId(idenId);
			xauthSysCode.setGp(gp);
			xauthSysCode.setCode(code);
			Map<String, Object> data = getSysCode(xauthSysCode);
			if (data == null) {
				result.setStatus(ProcessResult.NG);
				result.addMessage(MSG_KEY.QUERY_EMPTY.getMessage());
				return result;
			}
			
			xauthSysCode.setCname(MapUtils.getEmptyString(params, "cname"));
			xauthSysCode.setEname(MapUtils.getEmptyString(params, "ename"));
			xauthSysCode.setOrderSeq(MapUtils.getInteger(params, "orderSeq"));
			xauthSysCode.setEnabled(MapUtils.getEmptyString(params, "enabled"));
			xauthSysCode.setBgnDate(DateUtils.converStr2Date(MapUtils.getEmptyString(params, "bgnDate"), "yyyy/MM/dd"));			
			xauthSysCode.setEndDate(DateUtils.converStr2Date(MapUtils.getEmptyString(params, "endDate"), "yyyy/MM/dd"));
			xauthSysCode.setMemo(MapUtils.getEmptyString(params, "memo"));
			xauthSysCode.setC01(MapUtils.getEmptyString(params, "c01"));
			xauthSysCode.setC02(MapUtils.getEmptyString(params, "c02"));
			xauthSysCode.setC03(MapUtils.getEmptyString(params, "c03"));
			xauthSysCode.setC04(MapUtils.getEmptyString(params, "c04"));
			xauthSysCode.setC05(MapUtils.getEmptyString(params, "c05"));
			xauthSysCode.setUpdUser(getCreOrUpdUser(null));
			xauthSysCode.setUpdDate(new Timestamp(new Date().getTime()));		
			
			QueryWrapper<XauthSysCode> queryWrapper = new QueryWrapper<XauthSysCode>();
			queryWrapper.eq("APP_ID", APP_ID);
			queryWrapper.eq("IDEN_ID", idenId);
			queryWrapper.eq("GP", gp);
			queryWrapper.eq("CODE", code);			
			xauthSysCodeMapper.update(xauthSysCode, queryWrapper);
			
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.UPDATE_OK.getMessage());
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result;
	}
	
	/**
	 * 刪除系統代碼資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult deleteSysCode(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		if (params != null && params.size() > 0) {
			ProcessResult rs = checkSysCodeData(params);
			if (rs.getStatus().equals(ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}	
				
			XauthSysCode xauthSysCode = new XauthSysCode();
			QueryWrapper<XauthSysCode> queryWrapper = new QueryWrapper<XauthSysCode>(xauthSysCode);
			queryWrapper.eq("APP_ID", APP_ID);
			queryWrapper.eq("IDEN_ID", MapUtils.getString(params, "idenId"));
			queryWrapper.eq("GP", MapUtils.getString(params, "gp"));
			queryWrapper.eq("CODE", MapUtils.getString(params, "code"));			
			xauthSysCodeMapper.delete(queryWrapper);
			
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.DELETE_OK.getMessage());
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result;
	}
	
	/**
	 * 檢查系統代碼資料
	 * @param params
	 * @return
	 */
	private ProcessResult checkSysCodeData(Map<String, Object> params) {
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);
		try {
			if (StringUtils.isBlank(MapUtils.getString(params, "idenId"))) {
				result.addMessage("請輸入識別碼");
				return result;
			}
			
			if (StringUtils.isBlank(MapUtils.getString(params, "gp"))) {
				result.addMessage("請輸入群組名稱");
				return result;
			}		
			
			if (StringUtils.isBlank(MapUtils.getString(params, "code"))) {
				result.addMessage("請輸入代碼");
				return result;
			}	
			
			if (StringUtils.isBlank(MapUtils.getString(params, "bgnDate"))) {
				result.addMessage("請輸入起始日期");
				return result;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
			return result;
		}
		result.setStatus(ProcessResult.OK);
		return result;
	}
	
	public Map<String, Object> parseMenu(List<Map<String, Object>> dataList, String rootId) throws Exception {
		Map<String, Object> menu = new HashMap<String, Object>();
		if (dataList != null && dataList.size() > 0) {
			menu = parseMenuRoot(dataList, rootId);
		}
		return menu;
	}
	
	public List<Map<String, Object>> parseMenuList(List<Map<String, Object>> dataList, String rootId) throws Exception {
		List<Map<String, Object>> menu = new ArrayList<Map<String, Object>>();
		if (dataList != null && dataList.size() > 0) {
			menu.add(parseMenuRoot(dataList, rootId));
		}
		return menu;
	}
	
	private Map<String, Object> parseMenuRoot(List<Map<String, Object>> dataList, String id) {
		Map<String, Object> data = new HashMap<String, Object>();
		for (Map<String, Object> m : dataList) {
			if (m.get("id").toString().equalsIgnoreCase(id)) {						
				m.put("children", parseMenuParent(dataList, m.get("id").toString()));
				data.putAll(m);
			}
		}
		return data;
	}
	
	private List<Map<String, Object>> parseMenuParent(List<Map<String, Object>> dataList, String parentId) {
		List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> m : dataList) {
			if (m.get("parent").toString().equalsIgnoreCase(parentId)) {		
				m.put("children", parseMenuParent(dataList, m.get("id").toString()));
				menuList.add(m);
			}
		}
		return menuList;
	}
	
	/**
	 * 角色代理人資料分頁查詢
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult searchRoleAgentUser(Map<String, Object> params) throws Exception {		
		GridResult gridResult = this.grid(params);
		Map<String, Object> objParams = new HashMap<String, Object>();
		objParams.put("appId", MapUtils.getString(params, "appId"));
		objParams.put("idenId", MapUtils.getString(params, "idenId"));
		if (!userContext.getCurrentUser().getUserType().equals(USER_TYPE.SYS_ADMIN.getCode())) {
			objParams.put("userId", userContext.getCurrentUser().getUserId());
		}
		String roleId = MapUtils.getString(params, "roleId");
		if (StringUtils.isNotBlank(roleId)) {
			objParams.put("roleId", "ROLE_" + roleId);
		}		
		if (MapUtils.getString(params, "sortColumnName") != null && MapUtils.getString(params, "sortOrder") != null) {			
			objParams.put("sortColumnName", MapUtils.getString(params, "sortColumnName"));
			objParams.put("sortOrder", MapUtils.getString(params, "sortOrder"));
		}
		else {
			objParams.put("sortColumnName", "APP_ID, IDEN_ID, ROLE_ID");
			objParams.put("sortOrder", "ASC");
		}
		commonDao.query(gridResult, "com.tradevan.mapper.xauth.dao.XauthMapper.searchRoleAgentUser", objParams);		
		return gridResult;
	}
	
	/**
	 * 取得單筆角色代理人資料
	 * @param xauthIpGrantParams
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getRoleAgentUser(XauthRoleAgentUser xauthRoleAgentUser) throws Exception {		
		if (xauthRoleAgentUser != null) {
			List<Map<String, Object>> dataList = xauthMapper.searchRoleAgentUser(ConvertUtils.Object2Map(xauthRoleAgentUser));
			if (dataList != null && dataList.size() > 0) {				
				return ConvertUtils.Map2Camel(dataList.get(0));
			}
		}		
		return null;
	}
	
	/**
	 * 新增角色代理人資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult insertRoleAgentUser(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		if (params != null && params.size() > 0) {
			ProcessResult rs = checkRoleAgentUser(params);
			if (rs.getStatus().equals(ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}	
			
			XauthRoleAgentUser xauthRoleAgentUser = new XauthRoleAgentUser();
			xauthRoleAgentUser.setAppId(APP_ID);
			xauthRoleAgentUser.setIdenId(MapUtils.getString(params, "idenId"));
			xauthRoleAgentUser.setRoleId("ROLE_" + MapUtils.getString(params, "roleId"));
			xauthRoleAgentUser.setUserId(MapUtils.getString(params, "userId"));
			xauthRoleAgentUser.setAgentAppId(APP_ID);
			xauthRoleAgentUser.setAgentIdenId(MapUtils.getString(params, "agentIdenId"));
			xauthRoleAgentUser.setAgentUserId(MapUtils.getString(params, "agentUserId"));
			xauthRoleAgentUser.setEnabled(MapUtils.getString(params, "enabled"));
			xauthRoleAgentUser.setCreUser(getCreOrUpdUser(null));
			xauthRoleAgentUser.setCreDate(new Timestamp(new Date().getTime()));						
			xauthRoleAgentUserMapper.insert(xauthRoleAgentUser);
			
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.INSERT_OK.getMessage());
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}			
		return result;
	}
	
	/**
	 * 更新角色代理人資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult updateRoleAgentUser(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		if (params != null && params.size() > 0) {
			ProcessResult rs = checkRoleAgentUser(params);
			if (rs.getStatus().equals(ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}	
			
			String idenId = MapUtils.getString(params, "idenId");
			String roleId = "ROLE_" + MapUtils.getString(params, "roleId");
			String agentIdenId = MapUtils.getString(params, "agentIdenId");
			String agentUserId = MapUtils.getString(params, "agentUserId");
			
			XauthRoleAgentUser xauthRoleAgentUser = new XauthRoleAgentUser();
			xauthRoleAgentUser.setAppId(APP_ID);
			xauthRoleAgentUser.setIdenId(idenId);
			xauthRoleAgentUser.setRoleId(roleId);
			xauthRoleAgentUser.setAgentAppId(APP_ID);
			xauthRoleAgentUser.setAgentIdenId(agentIdenId);
			xauthRoleAgentUser.setAgentUserId(agentUserId);
			Map<String, Object> data = getRoleAgentUser(xauthRoleAgentUser);
			if (data == null) {
				result.setStatus(ProcessResult.NG);
				result.addMessage(MSG_KEY.QUERY_EMPTY.getMessage());
				return result;
			}
			
			xauthRoleAgentUser.setEnabled(MapUtils.getString(params, "enabled"));
			xauthRoleAgentUser.setUpdUser(getCreOrUpdUser(null));
			xauthRoleAgentUser.setUpdDate(new Timestamp(new Date().getTime()));		
			
			QueryWrapper<XauthRoleAgentUser> queryWrapper = new QueryWrapper<XauthRoleAgentUser>();
			queryWrapper.eq("APP_ID", APP_ID);
			queryWrapper.eq("IDEN_ID", idenId);
			queryWrapper.eq("ROLE_ID", roleId);
			queryWrapper.eq("AGENT_APP_ID", APP_ID);
			queryWrapper.eq("AGENT_IDEN_ID", agentIdenId);
			queryWrapper.eq("AGENT_USER_ID", agentUserId);			
			xauthRoleAgentUserMapper.update(xauthRoleAgentUser, queryWrapper);

			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.UPDATE_OK.getMessage());
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result;
	}
	
	/**
	 * 刪除角色代理人資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult deleteRoleAgentUser(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		if (params != null && params.size() > 0) {
			ProcessResult rs = checkRoleAgentUser(params);
			if (rs.getStatus().equals(ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}		
			
			String idenId = MapUtils.getString(params, "idenId");
			String roleId = "ROLE_" + MapUtils.getString(params, "roleId");
			String agentIdenId = MapUtils.getString(params, "agentIdenId");
			String agentUserId = MapUtils.getString(params, "agentUserId");
			
			QueryWrapper<XauthRoleAgentUser> queryWrapper = new QueryWrapper<XauthRoleAgentUser>();
			queryWrapper.eq("APP_ID", APP_ID);
			queryWrapper.eq("IDEN_ID", idenId);
			queryWrapper.eq("ROLE_ID", roleId);
			queryWrapper.eq("AGENT_APP_ID", APP_ID);
			queryWrapper.eq("AGENT_IDEN_ID", agentIdenId);
			queryWrapper.eq("AGENT_USER_ID", agentUserId);			
			xauthRoleAgentUserMapper.delete(queryWrapper);
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.DELETE_OK.getMessage());
		}
		else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		return result;
	}
	
	/**
	 * 檢查角色代理人資料
	 * @param params
	 * @return
	 */
	private ProcessResult checkRoleAgentUser(Map<String, Object> params) {
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);
		try {
			if (StringUtils.isBlank(MapUtils.getString(params, "idenId"))) {
				result.addMessage("請輸入識別碼");
				return result;
			}	
			
			if (StringUtils.isBlank(MapUtils.getString(params, "roleId"))) {
				result.addMessage("請輸入角色代號");
				return result;
			}
			
			if (StringUtils.isBlank(MapUtils.getString(params, "agentIdenId"))) {
				result.addMessage("請輸入代理識別碼");
				return result;
			}	
			
			if (StringUtils.isBlank(MapUtils.getString(params, "agentUserId"))) {
				result.addMessage("請輸入代理使用者帳號");
				return result;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
			return result;
		}
		result.setStatus(ProcessResult.OK);
		return result;
	}
	
	/**
	 * 新增法務簽審管理
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult insertSystemparam(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		Date currentDate = new Timestamp(new Date().getTime());
		String contractmodel = MapUtils.getString(params, "contractmodel");
		String flowid = MapUtils.getString(params, "flowid");
		String deptno = MapUtils.getString(params, "deptno");
		String actiontype = MapUtils.getString(params, "actiontype");
		String idenIds = MapUtils.getString(params, "idenIds");
		
		if(params != null && params.size() > 0) {
			ProcessResult rs = checkLegalPerData(params);
			if(StringUtils.equals(rs.getStatus(), ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}
			
			//判斷法務審查與簽章01 02
			//判斷法律顧問 03 當非制式 流程名稱與多部門為空時判斷是否重複
			//判斷其他關卡 當非制式 流程名稱為空時判斷是否重複
			if("01|02".indexOf(actiontype) > -1) {
				if(StringUtils.equals(contractmodel, "NSC") && StringUtils.isNotBlank(flowid)) {
					if(getSystemparamData(params).size() > 0) {
						result.setStatus(ProcessResult.NG);
						result.addMessage("資料重複");
						return result;
					}
				}else if(StringUtils.equals(contractmodel, "SC") && StringUtils.isNotBlank(deptno)) {
					if(getSystemparamData(params).size() > 0) {
						result.setStatus(ProcessResult.NG);
						result.addMessage("資料重複");
						return result;
					}
				}
			}else if(StringUtils.equals(actiontype, "03")) {
				if(StringUtils.equals(contractmodel, "NSC") && StringUtils.isBlank(flowid) && StringUtils.isBlank(idenIds)) {
					if(getSystemparamData(params).size() > 0) {
						result.setStatus(ProcessResult.NG);
						result.addMessage("資料重複");
						return result;
					}
				}
			}else if(StringUtils.equals(actiontype, "00")) {
				if(StringUtils.isNotBlank(flowid)) {
					if(getSystemparamData(params).size() > 0) {
						result.setStatus(ProcessResult.NG);
						result.addMessage("資料重複");
						return result;
					}
				}
			}else {
				if(StringUtils.equals(contractmodel, "NSC") && StringUtils.isBlank(flowid)) {
					if(getSystemparamData(params).size() > 0) {
						result.setStatus(ProcessResult.NG);
						result.addMessage("資料重複");
						return result;
					}
				}
			}

			Systemparam systemparam = new Systemparam();
			systemparam.setAppid(APP_ID);
			systemparam.setContractmodel(contractmodel);
			systemparam.setFlowid(flowid);
			systemparam.setFlowname(MapUtils.getString(params, "flowname"));
			systemparam.setDeptno(MapUtils.getString(params, "deptno"));
			systemparam.setUserids(MapUtils.getString(params, "userId"));
			systemparam.setRoleids(MapUtils.getString(params, "roleId"));
			systemparam.setCreateuser(getCreOrUpdUser(null));
			systemparam.setCreatedate(currentDate);
			systemparam.setUpdateuser(getCreOrUpdUser(null));
			systemparam.setUpdatedate(currentDate);
			systemparam.setActiontype(MapUtils.getString(params, "actiontype"));
			systemparam.setIdenid(MapUtils.getString(params, "idenId"));
			systemparam.setIdenids(MapUtils.getString(params, "idenIds"));
			systemparamMapper.insert(systemparam);
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.INSERT_OK.getMessage());
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}

		return result;
	}
	
	/**
	 * 檢查法務簽審管理資料
	 * @param params
	 * @return
	 */
	private ProcessResult checkLegalPerData(Map<String, Object> params) {
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);
		String contractmodel = MapUtils.getString(params, "contractmodel");
		String deptno = MapUtils.getString(params, "deptno");
		String flowid = MapUtils.getString(params, "flowid");
		String userId = MapUtils.getString(params, "userId");
		String roleId = MapUtils.getString(params, "roleId");
		String actionType = MapUtils.getString(params, "actiontype");
		String idenIds = MapUtils.getString(params, "idenIds");
		try {
			if(StringUtils.isBlank(contractmodel)) {
				result.addMessage("請選擇合約模式");
				return result;
			}
			if(StringUtils.equals(contractmodel, "SC")) {
				if("00".indexOf(actionType) == -1 && StringUtils.isBlank(deptno)) {
					result.addMessage("請選擇課別");
					return result;
				}
			} else if(StringUtils.equals(contractmodel, "NSC")) {
				if("01|02".indexOf(actionType) > -1) {
					if(StringUtils.isBlank(flowid) && StringUtils.isBlank(idenIds)) {
						result.addMessage("請選擇流程編號或部門選取被簽審部門二擇一");
						return result;
					}
				}
			}
			
			if(StringUtils.equals(actionType, "00")) {
//				if(StringUtils.equals(contractmodel, "NSC")) {
					if(StringUtils.isBlank(flowid)) {
						result.addMessage("請選擇流程編號");
						return result;
					}
//				}
				if(StringUtils.isBlank(idenIds)) {
					result.addMessage("請選擇部門選取被簽審部門");
					return result;
				}
			} else {
				if(StringUtils.isBlank(userId) && StringUtils.isBlank(roleId)) {
					result.addMessage("請選擇使用者帳號或角色二擇一");
					return result;
				}
				
				if(StringUtils.isBlank(actionType)) {
					result.addMessage("請選擇用途");
					return result;
				}
			}
//			if(StringUtils.isBlank(userId) && StringUtils.isBlank(roleId)) {
//				result.addMessage("請選擇使用者帳號或角色二擇一");
//				return result;
//			}
//			
//			if(StringUtils.isBlank(actionType)) {
//				result.addMessage("請選擇用途");
//				return result;
//			}
			
		} catch(Exception e) {
			e.printStackTrace();
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
			return result;
		}
		result.setStatus(ProcessResult.OK);
		return result;
	}
	
	/**
	 * 分頁查詢
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult searchSystemparam(Map<String, Object> params) throws Exception {
		GridResult gridResult = this.grid(params);
		Map<String, Object> objParams = new HashMap<String, Object>();
		objParams.put("appId", APP_ID);
		objParams.put("contractmodel", MapUtils.getString(params, "contractmodel"));
		objParams.put("flowid", MapUtils.getString(params, "flowid"));
		objParams.put("deptno", MapUtils.getString(params, "deptno"));
		commonDao.query(gridResult, "com.tradevan.mapper.xauth.dao.XauthMapper.searchSystemparam", objParams);
		return gridResult;
	}
	
	/**
	 * 依條件取得Systemparam資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSystemparamData(Map<String, Object> params) throws Exception {
		
		QueryWrapper<Systemparam> queryWrapper = new QueryWrapper<Systemparam>();
		queryWrapper.eq("APPID", APP_ID);
		queryWrapper.eq("CONTRACTMODEL", MapUtils.getString(params, "contractmodel"));
		if(StringUtils.isNotBlank(MapUtils.getString(params, "flowid"))) {
			queryWrapper.eq("FLOWID", MapUtils.getString(params, "flowid"));
		}
		if(StringUtils.isNotBlank(MapUtils.getString(params, "deptno"))) {
			queryWrapper.eq("DEPTNO", MapUtils.getString(params, "deptno"));
		}
		queryWrapper.eq("ACTIONTYPE", MapUtils.getString(params, "actiontype"));//Arrays.asList("01", "02"));
		List<Map<String, Object>> systemparamList = systemparamMapper.selectMaps(queryWrapper);
		
		return systemparamList;
	}
	
	/**
	 * 取得Systemparam須更新的一筆資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSystemparamBySerno(Map<String, Object> params) throws Exception {
		
		QueryWrapper<Systemparam> queryWrapper = new QueryWrapper<Systemparam>();
		queryWrapper.eq("SERNO", MapUtils.getLong(params, "serno"));
		List<Map<String, Object>> systemparamList = systemparamMapper.selectMaps(queryWrapper);
		
		return systemparamList;
	}
	
	public List<Map<String, Object>> getSystemparamIdens(String idenids) {
		
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if(StringUtils.isNotBlank(idenids)) {
			QueryWrapper<XauthDept> queryWrapper = null;
			List<XauthDept> xauthDeptList = null;
			Map<String, Object> xauthDeptMap = null;
			String cName = "";
			String[] idenidArray = idenids.split(",");
			for(String idenid : idenidArray) {
				queryWrapper = new QueryWrapper<XauthDept>();
				queryWrapper.eq("IDEN_ID", idenid);
				xauthDeptList = xauthDeptMapper.selectList(queryWrapper);
				if(xauthDeptList != null && xauthDeptList.size() > 0) {
					xauthDeptMap = new HashMap<String, Object>();
					cName = xauthDeptList.get(0).getCname();
					xauthDeptMap.put("IDEN_ID", idenid);
					xauthDeptMap.put("CNAME", cName);
					result.add(xauthDeptMap);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 更新Systemparam
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult updateSystemparam(Map<String, Object> params) throws Exception {
		
		Date currentDate = new Timestamp(new Date().getTime());
		ProcessResult result = new ProcessResult();
		int updateCount = 0;
		Systemparam systemparam = null;
		String contractmodel = MapUtils.getString(params, "contractmodel");
		String flowid = MapUtils.getString(params, "flowid");
		
		if(params != null && params.size() > 0) {
			ProcessResult rs = checkLegalPerData(params);
			if(StringUtils.equals(rs.getStatus(), ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}
			QueryWrapper<Systemparam> queryWrapper = new QueryWrapper<Systemparam>();
			queryWrapper.eq("SERNO", MapUtils.getLong(params, "serno"));
			
			List<Systemparam> systemparamList = systemparamMapper.selectList(queryWrapper);
			if(systemparamList.size() > 0) {
				if(!StringUtils.equals(contractmodel, "NSC") || StringUtils.isNotBlank(flowid)) {
					if(systemparamList.size() > 1) {
						result.setStatus(ProcessResult.NG);
						result.addMessage("資料重複");
						return result;
					}
				}
				
				systemparam = systemparamList.get(0);
				if(systemparam != null) {
					systemparam.setAppid(APP_ID);
					systemparam.setContractmodel(contractmodel);
					systemparam.setFlowid(flowid);
					systemparam.setFlowname(MapUtils.getString(params, "flowname"));
					systemparam.setDeptno(StringUtils.isBlank(MapUtils.getString(params, "deptno")) ? "" : MapUtils.getString(params, "deptno"));
					systemparam.setUserids(StringUtils.isBlank(MapUtils.getString(params, "userId")) ? "" : MapUtils.getString(params, "userId"));
					systemparam.setRoleids(StringUtils.isBlank(MapUtils.getString(params, "roleId")) ? "" : MapUtils.getString(params, "roleId"));
					systemparam.setUpdateuser(getCreOrUpdUser(null));
					systemparam.setUpdatedate(currentDate);
					systemparam.setActiontype(MapUtils.getString(params, "actionType"));
					systemparam.setIdenid(MapUtils.getString(params, "idenId"));
					systemparam.setIdenids(StringUtils.isBlank(MapUtils.getString(params, "idenIds")) ? "" : MapUtils.getString(params, "idenIds"));
					updateCount = systemparamMapper.update(systemparam, queryWrapper);
					if(updateCount > 0) {
						result.setStatus(ProcessResult.OK);
						result.addMessage(MSG_KEY.UPDATE_OK.getMessage());
					} else {
						result.setStatus(ProcessResult.NG);
						result.addMessage(MSG_KEY.UPDATE_FAIL.getMessage());
					}
				}
			} else {
				result.setStatus(ProcessResult.NG);
				if(StringUtils.equals(MapUtils.getString(params, "contractmodel"), "SC")) {
					result.addMessage(MSG_KEY.UPDATE_FAIL.getMessage() + "合約模式及課別不可變更");
				} else {
					result.addMessage(MSG_KEY.UPDATE_FAIL.getMessage() + "合約模式及流程編號不可變更");
				}
			}
			
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.UPDATE_FAIL.getMessage());
		}

		return result;
	}
	
	public ProcessResult deleteSystemparam(Map<String, Object> params) throws Exception {
		
		ProcessResult result = new ProcessResult();
		int deleteCount = 0;
		if(params != null && params.size() > 0) {
			QueryWrapper<Systemparam> queryWrapper = new QueryWrapper<Systemparam>();
			queryWrapper.eq("SERNO", MapUtils.getLong(params, "serno"));
			deleteCount = systemparamMapper.delete(queryWrapper);
			if(deleteCount > 0) {
				result.setStatus(ProcessResult.OK);
				result.addMessage(MSG_KEY.DELETE_OK.getMessage());
			} else {
				result.setStatus(ProcessResult.NG);
				result.addMessage(MSG_KEY.DELETE_FAIL.getMessage());
			}
			
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		
		return result;
	}
	
	public List<Map<String, Object>> getActionTypeList(String contractmodel) {
		
		QueryWrapper<XauthSysCode> queryWrapper = new QueryWrapper<XauthSysCode>();
		queryWrapper.eq("GP", "REVIEW_PER_CODE");
		if(StringUtils.equals(contractmodel, "SC")) {
			queryWrapper.in("CODE", Arrays.asList("00", "02"));
		}
		List<Map<String, Object>> reviewPerList = xauthSysCodeMapper.selectMaps(queryWrapper);
		
		return reviewPerList;
	}
	
	public List<XauthRoleUser>  getUserRole(String userId) {
		QueryWrapper<XauthRoleUser> queryWrapper = new QueryWrapper<XauthRoleUser>();
		queryWrapper.eq("USER_ID", userId);
		List<XauthRoleUser> xauthRoleUserList = xauthRoleUserMapper.selectList(queryWrapper);
		
		return xauthRoleUserList;
	}	
	/**
	 * 取得被簽審部門
	 * @param idenId
	 * @return
	 */
	public List<Map<String, Object>> getBeSignDept(String idenId) {
		
		Map<String, Object> deptMap = new HashMap<String, Object>();
		deptMap.put("appId", APP_ID);
		deptMap.put("idenId", idenId);
		List<Map<String, Object>> deptList = xauthMapper.selectChildDept(deptMap);
		
		return deptList;
	}
	
	/**
	 * 忘記密碼
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult updatePwd(Map<String, Object> params) throws Exception {
		logger.info("params == " + params);
		ProcessResult result = new ProcessResult();
		Emailqueue emailqueue = null;
		XauthUsers xauthUsers = null;
		String userId = MapUtils.getString(params, "userId");
		String email = MapUtils.getString(params, "email");
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		SensitiveUtils sensitiveUtils = new SensitiveUtils();
		
		if (params != null && params.size() > 0) {
			ProcessResult rs = checkForgetPwdData(params);
			if (StringUtils.equals(rs.getStatus(), ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}
			QueryWrapper<XauthUsers> usersWrapper = new QueryWrapper<XauthUsers>();
			usersWrapper.eq("APP_ID", APP_ID);
			usersWrapper.eq("USER_ID", userId);
			usersWrapper.eq("EMAIL", email);
			List<XauthUsers> xauthUserList = xauthUsersMapper.selectList(usersWrapper);
			if(xauthUserList.size() > 0) {
				emailqueue = new Emailqueue();
				emailqueue.setCategory("RESET");
				emailqueue.setContent("您的帳號已恢復初始密碼，登入後請修改密碼。");
				emailqueue.setCreatetime(new Timestamp(new Date().getTime()));
				emailqueue.setCreateuserid("SYSTEM");
				emailqueue.setMailto(email);
				emailqueue.setPriority("1");
				emailqueue.setStatus("N");
				emailqueue.setSubject("密碼初始化");
				emailqueue.setSysid(APP_ID);
				emailqueue.setSysmemo(userId);
				emailqueue.setUpdatetime(new Timestamp(new Date().getTime()));
				emailqueue.setUpdateuserid("SYSTEM");
				emailqueueMapper.insert(emailqueue);
				
				xauthUsers = xauthUserList.get(0);
				xauthUsers.setIsFirst("1");
				xauthUsers.setPwdhash("1234");
				xauthUsers.setUserPw(bCryptPasswordEncoder.encode(sensitiveUtils.decrypt(sensitiveUtils.encrypt(xauthUsers.getPwdhash(), userId), userId).toString()));
				xauthUsers.setUpdDate(new Timestamp(new Date().getTime()));
				xauthUsers.setUpdUser("SYSTEM");
				xauthUsersMapper.update(xauthUsers, usersWrapper);
				result.setStatus(ProcessResult.OK);
				result.addMessage("已恢復初始密碼，請重新登入修改密碼");
			} else {
				result.setStatus(ProcessResult.NG);
				result.addMessage("請聯絡系統管理員");
			}
		}
		
		return result;
	}
	
	/**
	 * 忘記密碼欄位檢核
	 * @param params
	 * @return
	 */
	private ProcessResult checkForgetPwdData(Map<String, Object> params) {
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);
		try {
			if (StringUtils.isBlank(MapUtils.getString(params, "userId"))) {
				result.addMessage("請輸入使用者帳號");
				return result;
			}
			
			if (StringUtils.isBlank(MapUtils.getString(params, "email"))) {
				result.addMessage("請輸入電子郵件");
				return result;
			}		
		}
		catch (Exception e) {
			e.printStackTrace();
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
			return result;
		}
		result.setStatus(ProcessResult.OK);
		return result;
	}
	public List<Map<String,Object>> getuserid(Map<String, Object> params) {
		List<Map<String,Object>> dataList=null;
		Map<String, Object> objParams = new HashMap<String, Object>();
		objParams.put("appId", APP_ID);
		objParams.put("idenId", MapUtils.getString(params, "idenId"));
		objParams.put("roleId",MapUtils.getString(params, "roleId"));
		dataList = xauthMapper.searchUserRole(objParams);
		return dataList ;
	}
	
	/**
	 * 批次分頁查詢
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult selectBatch(Map<String, Object> params) throws Exception {
		GridResult gridResult = this.grid(params);
		Map<String, Object> objParams = new HashMap<String, Object>();
		objParams.put("serno", MapUtils.getString(params, "serno"));
		commonDao.query(gridResult, "com.tradevan.mapper.xauth.dao.XauthMapper.selectBatch", objParams);
		
		return gridResult;
	}
	
	/**
	 * 新增批次
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult insertBatch(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		
		if(params != null && params.size() > 0) {
			ProcessResult rs = checkBatch(params);
			if(StringUtils.equals(rs.getStatus(), ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}
			Batchparamset batchparamset = new Batchparamset();
			batchparamset.setSerno(sequenceMapper.selectBatchparamsetSeq());
			batchparamset.setBatchname(MapUtils.getString(params, "batchname"));
			batchparamset.setStatus(MapUtils.getString(params, "status"));
			batchparamset.setSleepsec(MapUtils.getLong(params, "sleepsec"));
			batchparamset.setCreateuser(getCreOrUpdUser(null));
			batchparamset.setCreatedate(new Timestamp(new Date().getTime()));							
			batchparamsetMapper.insert(batchparamset);
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.INSERT_OK.getMessage());
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 更新批次
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult updateBatch(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		
		if(params != null && params.size() > 0) {
			ProcessResult rs = checkBatch(params);
			if(StringUtils.equals(rs.getStatus(), ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}
			long serno = MapUtils.getLongValue(params, "serno");
			Batchparamset batchparamset = batchparamsetMapper.selectById(serno);
			if(batchparamset == null) {
				result.setStatus(ProcessResult.NG);
				result.addMessage(MSG_KEY.QUERY_EMPTY.getMessage());
				return result;
			}
			batchparamset.setBatchname(MapUtils.getString(params, "batchname"));
			batchparamset.setStatus(MapUtils.getString(params, "status"));
			batchparamset.setSleepsec(MapUtils.getLong(params, "sleepsec"));
			batchparamset.setUpdateuser(getCreOrUpdUser(null));
			batchparamset.setUpdatedate(new Timestamp(new Date().getTime()));
			batchparamsetMapper.updateById(batchparamset);
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.UPDATE_OK.getMessage());
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 刪除批次
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult deleteBatch(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		
		if(params != null && params.size() > 0) {
			ProcessResult rs = checkBatch(params);
			if(StringUtils.equals(rs.getStatus(), ProcessResult.NG)) {
				result.setStatus(rs.getStatus());
				result.setMessages(rs.getMessages());
				return result;
			}
			long serno = MapUtils.getLongValue(params, "serno");
			batchparamsetMapper.deleteById(serno);
			result.setStatus(ProcessResult.OK);
			result.addMessage(MSG_KEY.DELETE_OK.getMessage());
		} else {
			result.setStatus(ProcessResult.NG);
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 檢核批次
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private ProcessResult checkBatch(Map<String, Object> params) throws Exception {
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);
		try {
			if(StringUtils.isBlank(MapUtils.getString(params, "batchname"))) {
				result.addMessage("請輸入排程名稱");
				return result;
			}
			
			if(StringUtils.isBlank(MapUtils.getString(params, "sleepsec"))) {
				result.addMessage("請輸入休眠時間");
				return result;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			result.addMessage(MSG_KEY.PARAMS_EMPTY.getMessage());
			return result;
		}
		result.setStatus(ProcessResult.OK);
		
		return result;
	}
	
	/**
	 * 取得Batch一筆資料
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Batchparamset getBatchData(Map<String, Object> params) throws Exception {		
		if (params != null && params.size() > 0) {
			Batchparamset data = batchparamsetMapper.selectById(MapUtils.getLong(params, "serno"));
			if(data != null) {
				return data;
			}
		}		
		return null;
	}
	
}

