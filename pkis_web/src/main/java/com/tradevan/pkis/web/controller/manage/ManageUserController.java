package com.tradevan.pkis.web.controller.manage;

import java.util.HashMap;
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
import com.tradevan.mapper.xauth.model.XauthUsers;
import com.tradevan.pkis.web.service.xauth.XauthService;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：MANAGE_USER<br>
 * 作 業 名 稱 ：使用者帳號管理<br>
 * 程 式 代 號 ：ManageUserController<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : penny<br>
 * @version  : 1.0.0 2018/3/12<P>
 */
@RestController
@RequestMapping("/manage/user")
public class ManageUserController extends DefaultController {
	
	@Autowired
	XauthService xauthService;

	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		return new ModelAndView("/manage/manage_user");
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
		return xauthService.searchUserPage(params);	
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
		ModelAndView mv = new ModelAndView("/manage/manage_user_data", "data", new HashMap<String, Object>());
		if (params != null && params.size() > 0) {
			String jsonData = MapUtils.getString(params, "keyData");
			if (StringUtils.isNotBlank(jsonData)) {
				Map<String, Object> keyData = new Gson().fromJson(jsonData, HashMap.class);
				XauthUsers xauthUsers = new XauthUsers();
				xauthUsers.setAppId(APP_ID);
				xauthUsers.setIdenId(keyData.get("idenId").toString());
				xauthUsers.setUserId(keyData.get("userId").toString());
				Map<String, Object> data = xauthService.getUser(xauthUsers);
				data.put("directManager", xauthService.getDirectManager(keyData));
				mv.addObject("data", data);
				
				List<Map<String, Object>> userRoleList = xauthService.getUserRoleList(xauthUsers);
				if (userRoleList != null && userRoleList.size() > 0) {
					mv.addObject("userRoleList", new Gson().toJson(userRoleList));
				}
			}
		}	
		return mv;
	}
	
	@RequestMapping("/getUserRoleList")
	public @ResponseBody Object getUserRoleList(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		ProcessResult result = xauthService.getUserRoleList(params);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/insert")
	public @ResponseBody Object insert(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = (Map<String, Object>) JsonUtils.json2Object(jsonData, Map.class, false);
		paramsData.put("roleId",  MapUtils.getString(params, "roleId"));
		ProcessResult result = xauthService.insertUser(paramsData);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/update")
	public @ResponseBody Object update(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = (Map<String, Object>) JsonUtils.json2Object(jsonData, Map.class, false);		
		paramsData.put("roleId",  MapUtils.getString(params, "roleId"));
		ProcessResult result = xauthService.updateUser(paramsData);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/delete")
	public @ResponseBody Object delete(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = (Map<String, Object>) JsonUtils.json2Object(jsonData, Map.class, false);
		ProcessResult result = xauthService.deleteUser(paramsData);
		return result;
	}
}
