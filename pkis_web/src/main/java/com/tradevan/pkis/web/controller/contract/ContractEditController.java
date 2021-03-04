package com.tradevan.pkis.web.controller.contract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.tradevan.pkis.web.service.contract.ContractService;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：CONTRACT<br>
 * 作 業 名 稱 ：合約編輯<br>
 * 程 式 代 號 ：ContractEditController<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : bruce<br>
 * @version  : 1.0.0 2020/4/17<P>
 */
@RestController
@RequestMapping("/contract/edit")
public class ContractEditController extends DefaultController {
	
	@Autowired
	ContractService contractService;
	
	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		return new ModelAndView("/contract/contract_edit_bak");		
	}
	
	/**
	 * 編輯
	 * @return
	 */
	@RequestMapping("/editSingle")
	public ModelAndView editSingle() {
		return new ModelAndView("/contract/contract_edit_single");		
	}
	
}
