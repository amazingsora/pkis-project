package com.tradevan.pkis.web.controller.pending;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.tradevan.aporg.repository.AgentProfRepository;
import com.tradevan.aporg.repository.DeptProfRepository;
import com.tradevan.aporg.repository.RoleProfRepository;
import com.tradevan.aporg.repository.UserProfRepository;
import com.tradevan.aporg.service.OrgService;
import com.tradevan.handyflow.bean.FlowBean;
import com.tradevan.handyflow.repository.FlowConfRepository;
import com.tradevan.handyflow.repository.FlowXmlRepository;
import com.tradevan.handyflow.service.FlowService;
import com.tradevan.jpa.repository.XauthUsersRepository;
import com.tradevan.pkis.web.service.contract.ContractService;
import com.tradevan.pkis.web.service.pending.PendingService;
import com.tradevan.pkis.web.service.xauth.XauthService;
import com.tradevan.xauthframework.web.controller.DefaultController;

/**
 * 作 業 代 碼 ：CONTRACT<br>
 * 作 業 名 稱 ：待辦事項<br>
 * 程 式 代 號 ：ContractListController<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : bruce<br>
 * @version  : 1.0.0 2020/4/15<P>
 */
@RestController
@RequestMapping("/pending/list")
public class PendingListController extends DefaultController {
	
	String FLOW_ID = "EduPlanReviewFlow";  // 計畫送審流程代碼
	String FORM_ID = "EduPlanReviewForm";  // 計畫送審表單代碼
	
	String SYSTEM_EDU = "EDU";
	String SYSTEM_RESEARCH = "RESEARCH";
	String SYSTEM_IRB = "IRB";
	String SYSTEM_COPYRIGHT = "COPYRIGHT";
	
	String ROLE_ADM = "ADM"; // 系統管理者
	String ROLE_EDU_ADMIN = "eduAdmin"; // 教學系統管理員
	String ROLE_RSCH_ADMIN = "rschAdmin"; // 研究系統管理員
	String ROLE_IRB_ADMIN = "irbAdmin"; // IRB系統管理員
	String ROLE_COPR_ADMIN = "coprAdmin"; // 著作系統管理員
	String ROLE_TRAINING = "TRAINING"; // 教學系統-師培中心
	String ROLE_EDU_HR_GRP = "EDU_HR_GRP"; // 教學系統-人事室經辦
	String ROLE_EDU_TEACH_GRP = "EDU_TEACH_GRP"; // 教學系統-教學組經辦
	String ROLE_RSCH_HR_GRP = "RSCH_HR_GRP"; // 研究系統-人事室經辦
	String ROLE_RSCH_TEACH_GRP = "RSCH_TEACH_GRP"; // 研究系統-教學組經辦
	String ROLE_IRB_HR_GRP = "IRB_HR_GRP"; // IRB系統-人事室經辦
	String ROLE_IRB_TEACH_GRP = "IRB_TEACH_GRP"; // IRB系統-教學組經辦
	String ROLE_COPR_HR_GRP = "COPR_HR_GRP"; // 著作系統-人事室經辦
	String ROLE_COPR_TEACH_GRP = "COPR_TEACH_GRP"; // 著作系統-教學組經辦
	String ROLE_JOB_MGR = "JOB_MGR"; // 職類管理員
	String ROLE_LEADER = "LEADER"; // 部門or單位主管(流程與權限用)
	String ROLE_LEADER1 = "LEADER1"; // 單位主管(流程與權限用)
	String ROLE_LEADER2 = "LEADER2"; // 部門主管(流程與權限用)
	String ROLE_SUPERVISOR = "supervisor"; // 單位管理職(權限用)
	String ROLE_SUPERVISOR2 = "supervisor2"; // 部門管理職(權限用)
	
	String CATEGORY_MEMBER_TYPE = "MemType";
	
	@Autowired
	XauthService xauthService;
	@Autowired
	private FlowService flowService;
	@Autowired 
	XauthUsersRepository xauthUsersRepository;
	@Autowired 
	FlowConfRepository flowConfRepository;
	@Autowired 
	FlowXmlRepository flowXmlRepository;
	
	@Autowired 
	AgentProfRepository agentProfRepository;
	
	@Autowired 
	UserProfRepository userProfRepository;
	
	@Autowired
	RoleProfRepository roleProfRepository;
	
	@Autowired
	DeptProfRepository deptProfRepository;
	
	@Autowired
	OrgService orgService;
	
	@Autowired
	PendingService pendingService;
	
	@Autowired
	ContractService contractService;
	
	/**
	 * 初始化頁面
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView init() {
		return new ModelAndView("/pending/pending_list");		
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
		agentProfRepository.findAllEntityList();
		userProfRepository.findAllEntityList();
		roleProfRepository.findAllEntityList();
		deptProfRepository.findAllEntityList();

		FlowBean flowBean = new FlowBean();
		
		flowBean.setFormId(FORM_ID);                                      // 表單代碼
		flowBean.setApplyNo("11234555");                             // 申請單主檔單號
		flowBean.setFlowId(FLOW_ID);                                      // 流程代碼
		flowBean.setFlowVersion(flowService.getNowFlowVersion(FLOW_ID));  // 取得目前流程最新版本
		flowBean.setSysId(SYSTEM_EDU);                                         // 系統代碼
		flowBean.setSubject("訓練計畫送審");                               // 主旨
		flowBean.setMainFormId(Long.valueOf("11234555"));                // 申請單主檔ID
		
		return xauthService.searchDeptPage(params);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/view")
	public @ResponseBody Object view(HttpServletRequest request, @RequestParam Map<String, Object> params) throws Exception {
		ModelAndView mv = new ModelAndView("/pending/pending_edit", "data", new HashMap<String, Object>());
		try {
			if (params != null && params.size() > 0) {
				String keyData = MapUtils.getString(params, "keyData");
				if (StringUtils.isNotBlank(keyData)) {
					Map<String, Object> keyMap = new Gson().fromJson(keyData, HashMap.class);
					Map<String, Object> data = pendingService.getFlowVeiwData(keyMap);
					mv.addObject("data", data);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 撈 JSON 資料
	 * 
	 * @param request
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/detail")
	public @ResponseBody Object detail(HttpServletRequest request, @RequestBody Map<String, Object> params) throws Exception {
		Map<String, Object> result = null;
		try {
			if (params != null && params.size() > 0) {
				String jsonData = MapUtils.getString(params, "data");
				if (StringUtils.isNotBlank(jsonData)) {
					Map<String, Object> keyData = new Gson().fromJson(jsonData, HashMap.class);
					logger.info("Json Params == " + jsonData);
					contractService.getJsonByEs(keyData);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
