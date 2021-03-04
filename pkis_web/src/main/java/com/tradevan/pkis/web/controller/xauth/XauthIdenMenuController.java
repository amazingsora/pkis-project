package com.tradevan.pkis.web.controller.xauth;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.tradevan.pkis.web.service.xauth.XauthService;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.web.controller.DefaultController;


/**
 * 作 業 代 碼 ：XAUTH_IDEN_MENU<br>
 * 作 業 名 稱 ：公司選單管理<br>
 * 程 式 代 號 ：XauthIdenMenuController<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : penny<br>
 * @version  : 1.0.0 2018/3/12<P>
 */
@RestController
@RequestMapping("/xauth/idenMenu")
public class XauthIdenMenuController extends DefaultController {
	
	@Autowired
	XauthService xauthService;

	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		return new ModelAndView("/xauth/xauth_iden_menu");
	}
	
	@RequestMapping("/getIdenMenuTree")
	public @ResponseBody Object getIdenMenuTree(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		ProcessResult result = xauthService.getIdenMenuTree(params);
		return result;
	}
	
	@RequestMapping("/saveIdenMenuTree")
	public @ResponseBody Object saveIdenMenuTree(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		ProcessResult result = xauthService.saveIdenMenuTree(params);
		return result;
	}
}
