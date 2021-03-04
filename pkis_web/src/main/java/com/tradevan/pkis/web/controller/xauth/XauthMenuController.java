package com.tradevan.pkis.web.controller.xauth;

import java.util.HashMap;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.tradevan.mapper.xauth.model.XauthMenu;
import com.tradevan.pkis.web.service.xauth.XauthService;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.core.enums.USER_TYPE;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：XAUTH_MENU<br>
 * 作 業 名 稱 ：選單設定管理<br>
 * 程 式 代 號 ：XauthMenuController<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : penny<br>
 * @version  : 1.0.0 2018/3/12<P>
 */
@RestController
@RequestMapping("/xauth/menu")
public class XauthMenuController extends DefaultController {
	
	@Autowired
	XauthService xauthService;

	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init(RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView("/xauth/xauth_menu");
		if (!userContext.getCurrentUser().getUserType().equals(USER_TYPE.SYS_ADMIN.getCode())) {
			mv.setViewName("redirect:/main/index");
			redirectAttributes.addFlashAttribute("msg", "您無權限使用此功能");
		}
		return mv;
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
		return xauthService.searchMenuPage(params);	
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
		ModelAndView mv = new ModelAndView("/xauth/xauth_menu_data", "data", new HashMap<String, Object>());
		if (params != null && params.size() > 0) {
			String jsonData = MapUtils.getString(params, "keyData");
			if (StringUtils.isNotBlank(jsonData)) {
				Map<String, Object> keyData = new Gson().fromJson(jsonData, HashMap.class);
				XauthMenu xauthMenu = new XauthMenu();
				xauthMenu.setAppId(APP_ID);
				xauthMenu.setMenuId(keyData.get("menuId").toString());
				Map<String, Object> data = xauthService.getMenu(xauthMenu);
				mv.addObject("data", data);
			}
		}	
		return mv;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/insert")
	public @ResponseBody Object insert(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = (Map<String, Object>) JsonUtils.json2Object(jsonData, Map.class, false);
		ProcessResult result = xauthService.insertMenu(paramsData);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/update")
	public @ResponseBody Object update(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = (Map<String, Object>) JsonUtils.json2Object(jsonData, Map.class, false);
		ProcessResult result = xauthService.updateMenu(paramsData);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/delete")
	public @ResponseBody Object delete(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		String jsonData = MapUtils.getString(params, "paramsData");
		Map<String, Object> paramsData = (Map<String, Object>) JsonUtils.json2Object(jsonData, Map.class, false);
		ProcessResult result = xauthService.deleteMenu(paramsData);
		return result;
	}
}
