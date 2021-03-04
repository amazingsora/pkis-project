package com.tradevan.pkis.web.controller.xauth;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.tradevan.mapper.xauth.model.XauthRole;
import com.tradevan.pkis.web.service.xauth.XauthService;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.MapUtils;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：XAUTH_ROLE_MENU<br>
 * 作 業 名 稱 ：公司角色選單管理<br>
 * 程 式 代 號 ：XauthRoleMenuController<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : penny<br>
 * @version  : 1.0.0 2018/3/12<P>
 */
@RestController
@RequestMapping("/xauth/roleMenu")
public class XauthRoleMenuController extends DefaultController {
	
	@Autowired
	XauthService xauthService;

	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		return new ModelAndView("/xauth/xauth_role_menu");
	}
	
	@RequestMapping("/getRoleMenuTree")
	public @ResponseBody Object getRoleMenuTree(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		ProcessResult result = xauthService.getIdenRoleMenuTree(params);
		return result;
	}
	
	@RequestMapping("/getRoleList")
	public @ResponseBody Object getRoleList(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		XauthRole xauthRole = new XauthRole();
		xauthRole.setAppId(APP_ID);
		xauthRole.setIdenId(MapUtils.getString(params, "idenId"));
		List<Map<String, Object>> roleList = xauthService.getRoleList(xauthRole);
		ProcessResult result = new ProcessResult();
		result.addResult("dataList", roleList);
		return result;
	}
	
	@RequestMapping("/saveRoleMenu")
	public @ResponseBody Object saveIdenRoleMenu(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		ProcessResult result = xauthService.saveIdenRoleMenu(params);
		return result;
	}
}
