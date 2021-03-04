package com.tradevan.pkis.web.controller.xauth;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.tradevan.mapper.xauth.model.XauthRole;
import com.tradevan.mapper.xauth.model.XauthRoleAgentUser;
import com.tradevan.mapper.xauth.model.XauthRoleUser;
import com.tradevan.mapper.xauth.model.XauthUsers;
import com.tradevan.pkis.web.service.xauth.XauthService;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.core.security.UserInfo;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：XAUTH_ROLE_AGENT_USER<br>
 * 作 業 名 稱 ：角色代理人管理<br>
 * 程 式 代 號 ：XauthRoleAgentUserController<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : penny<br>
 * @version  : 1.0.0 2019/5/12<P>
 */
@RestController
@RequestMapping("/xauth/roleAgentUser")
public class XauthRoleAgentUserController extends DefaultController {

	@Autowired
	XauthService xauthService;

	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		return new ModelAndView("/xauth/xauth_role_agent_user");		
	}
	
	/**
	 * 分頁查詢
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/query")
	public @ResponseBody Object query(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		return xauthService.searchRoleAgentUser(params);		
	}
	
	/**
	 * 詳細資料頁面
	 * @param request
	 * @param params
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/data")
	public ModelAndView toDetailView(HttpServletRequest request, @RequestParam Map<String, Object> params, Model model) throws Exception {
		ModelAndView mv = new ModelAndView("/xauth/xauth_role_agent_user_data", "data", new HashMap<String, Object>());

		if (params != null && params.size() > 0) {
			
			UserInfo userInfo = userContext.getCurrentUser();
			String jsonData = MapUtils.getString(params, "keyData");
			if (StringUtils.isNotBlank(jsonData)) {
				Map<String, Object> keyData = new Gson().fromJson(jsonData, HashMap.class);
				XauthRoleAgentUser xauthRoleAgentUser = new XauthRoleAgentUser();
				xauthRoleAgentUser.setAppId(MapUtils.getString(keyData, "appId"));
				xauthRoleAgentUser.setIdenId(MapUtils.getString(keyData, "idenId"));
				xauthRoleAgentUser.setRoleId("ROLE_" + MapUtils.getString(keyData, "roleId"));
				xauthRoleAgentUser.setAgentAppId(MapUtils.getString(keyData, "agentAppId"));
				xauthRoleAgentUser.setAgentIdenId(MapUtils.getString(keyData, "agentIdenId"));
				xauthRoleAgentUser.setAgentUserId(MapUtils.getString(keyData, "agentUserId"));
				xauthRoleAgentUser.setUserId(MapUtils.getString(keyData, "userId"));

				Map<String, Object> data = xauthService.getRoleAgentUser(xauthRoleAgentUser);
				
				mv.addObject("data", data);
			} else {
				String userRoleId="";
				List<XauthRoleUser> xauthRoleUserList=xauthService.getUserRole(userInfo.getUserId());
				if(xauthRoleUserList.size()>0) {
					userRoleId=xauthRoleUserList.get(0).getRoleId().trim();
				}
				if(userRoleId.length()>3) {
					StringBuilder bstr=new StringBuilder();

					bstr.append(userRoleId.substring(userRoleId.length()-3,userRoleId.length()));	
					
					userRoleId=bstr.toString();
				}
				mv.addObject("userRoleId", userRoleId);
			}
		}	

		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/insert")
	public @ResponseBody Object insert(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {		
		
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = (Map<String, Object>) JsonUtils.json2Object(jsonData, Map.class, false);
		ProcessResult result = xauthService.insertRoleAgentUser(paramsData);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/update")
	public @ResponseBody Object update(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = (Map<String, Object>) JsonUtils.json2Object(jsonData, Map.class, false);
		ProcessResult result = xauthService.updateRoleAgentUser(paramsData);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/delete")
	public @ResponseBody Object delete(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = (Map<String, Object>) JsonUtils.json2Object(jsonData, Map.class, false);
		ProcessResult result = xauthService.deleteRoleAgentUser(paramsData);
		return result;
	}
	
	/**
	 * 取得部門角色選單
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getRoleList")
	public @ResponseBody Object getRoleList(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		XauthRole xauthRole = new XauthRole();
		xauthRole.setAppId(APP_ID);
		xauthRole.setIdenId(MapUtils.getString(params, "idenId"));
		UserInfo userInfo = userContext.getCurrentUser();
		List<Map<String, Object>> roleList=new LinkedList<Map<String, Object>>();
		

		if("admin".equals(userInfo.getUserId())) 		
			roleList = xauthService.getRoleList(xauthRole);
		
		else {
			Map<String,Object> xauthRolemap=new HashMap<String,Object>();
			xauthRolemap.put("idenId", MapUtils.getString(params, "idenId"));
			roleList = xauthService.getRoleList(xauthRolemap);

		}
		
		logger.info("roleList==="+roleList);
		ProcessResult result = new ProcessResult();
		result.addResult("dataList", roleList);
		return result;
	}
	
	/**
	 * 取得使用者選單
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getUserList")
	public @ResponseBody Object getUserList(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		XauthUsers xauthUsers = new XauthUsers();
		xauthUsers.setAppId(APP_ID);
		xauthUsers.setIdenId(MapUtils.getString(params, "idenId"));
		String action="";
		if(MapUtils.getString(params, "action")!=null) {
			action=MapUtils.getString(params, "action");
		}
		else action="";
		List<Map<String, Object>> userList = xauthService.getUserList(xauthUsers);
		UserInfo userInfo = userContext.getCurrentUser();
		//用於新增代理人時自己本身移除
		if(userList != null && userList.size() > 0) {
			if("insert".equals(action)) {
				for(int i=0;i<userList.size();i++) {
					if(userList.get(i).get("userId").equals(userInfo.getUserId())){
					userList.remove(i);
						i--;
					}
				}
			}
		}
		ProcessResult result = new ProcessResult();
		result.addResult("dataList", userList);
		return result;
	}
	/**
	 * 取得使用者
	 * @param request
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getUserListByIdenAndRole")
	public @ResponseBody Object getUserListByIdenAndRole(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		
		List<Map<String, Object>> userList = xauthService.getUserListByIdenAndRole(params);
		ProcessResult result = new ProcessResult();
		result.addResult("dataList", userList);
		return result;
	}
}
