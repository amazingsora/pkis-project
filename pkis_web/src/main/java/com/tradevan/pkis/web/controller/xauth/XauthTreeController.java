package com.tradevan.pkis.web.controller.xauth;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tradevan.pkis.web.service.xauth.XauthService;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.core.enums.USER_TYPE;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：XAUTH_TREE<br>
 * 作 業 名 稱 ：選單樹狀圖<br>
 * 程 式 代 號 ：XauthTreeController<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : penny<br>
 * @version  : 1.0.0 2018/3/12<P>
 */
@RestController
@RequestMapping("/xauth/tree")
public class XauthTreeController extends DefaultController {
	
	@Autowired
	XauthService xauthService;

	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init(RedirectAttributes redirectAttributes) {
		ModelAndView mv = new ModelAndView("/xauth/xauth_tree");
		if (!userContext.getCurrentUser().getUserType().equals(USER_TYPE.SYS_ADMIN.getCode())) {
			mv.setViewName("redirect:/main/index");
			redirectAttributes.addFlashAttribute("msg", "您無權限使用此功能");
		}
		return mv;
	}
	
	@RequestMapping("/getMenuTree")
	public @ResponseBody Object getMenuTree(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		ProcessResult result = xauthService.getMenuTree(params);
		return result;
	}
	
	@RequestMapping("/saveMenuTree")
	public @ResponseBody Object saveMenuTree(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		ProcessResult result = xauthService.saveMenuTree(params);
		return result;
	}
}
