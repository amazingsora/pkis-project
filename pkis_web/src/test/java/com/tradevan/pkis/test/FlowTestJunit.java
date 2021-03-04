package com.tradevan.pkis.test;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SealedObject;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tradevan.apcommon.bean.CreateUserDto;
import com.tradevan.apcommon.bean.UpdateUserDto;
import com.tradevan.apcommon.util.BeanUtil;
import com.tradevan.apcommon.util.DateUtil;
import com.tradevan.aporg.enums.UserType;
import com.tradevan.aporg.model.DeptProf;
import com.tradevan.aporg.model.RoleProf;
import com.tradevan.aporg.model.UserDeptRole;
import com.tradevan.aporg.model.UserProf;
import com.tradevan.aporg.repository.AgentProfRepository;
import com.tradevan.aporg.repository.DeptProfRepository;
import com.tradevan.aporg.repository.RoleProfRepository;
import com.tradevan.aporg.repository.UserDeptRoleRepository;
import com.tradevan.aporg.repository.UserProfRepository;
import com.tradevan.aporg.service.OrgService;
import com.tradevan.handyflow.bean.DefaultFlowAction;
import com.tradevan.handyflow.bean.DocStateBean;
import com.tradevan.handyflow.bean.FlowBean;
import com.tradevan.handyflow.dto.FlowConfDto;
import com.tradevan.handyflow.model.form.DocState;
import com.tradevan.handyflow.model.form.FlowConf;
import com.tradevan.handyflow.model.form.FlowStep;
import com.tradevan.handyflow.model.form.FlowStepLink;
import com.tradevan.handyflow.model.form.FlowXml;
import com.tradevan.handyflow.model.form.SubFlowConf;
import com.tradevan.handyflow.repository.DocStateRepository;
import com.tradevan.handyflow.repository.FlowConfRepository;
import com.tradevan.handyflow.repository.FlowStepLinkRepository;
import com.tradevan.handyflow.repository.FlowStepRepository;
import com.tradevan.handyflow.repository.FlowXmlRepository;
import com.tradevan.handyflow.repository.FormConfRepository;
import com.tradevan.handyflow.repository.SubFlowConfRepository;
import com.tradevan.handyflow.service.FlowAdminService;
import com.tradevan.handyflow.service.FlowQueryService;
import com.tradevan.handyflow.service.FlowService;
import com.tradevan.jpa.repository.XauthUsersRepository;
import com.tradevan.mapper.pkis.model.DeptInfoExt;
import com.tradevan.mapper.pkis.model.UserInfoExt;
import com.tradevan.pkis.web.PKISApplication;
import com.tradevan.pkis.web.service.xauth.XauthService;
import com.tradevan.xauthframework.common.utils.DateUtils;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.common.utils.MapUtils;
import com.tradevan.xauthframework.common.utils.SensitiveUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PKISApplication.class)
public class FlowTestJunit {
	
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
	
    /**
     * "流程代碼(flowId)"	
     * "流程名稱(flowName)"	
     * "最新流程版本(flowVersion)"	
     * "流程描述(flowDesc)"	
     * "流程管理員帳號(flowAdminId)"	
     * "系統代號(sysId)"	
     * "建立者(createUserId)"
     */
	String[][] flowConfArry = new String[][]{
		{"f0020","開發不動產約賃合約","1","含士地及房屋","admin","APPKIS","admin"},
		{"f0023","其他工程合約-500萬以上","2","包含水電、消防、空調、冷凍冷藏設備、貨架、燈具安裝等","admin","APPKIS","admin"}

	};
	
	
	/**
	 * 0"流程代碼(flowId)"	
	 * 1"子流程代碼(subFlowId)"	
	 * 2"表單名稱(subFlowName)"	
	 * 3"流程描述(subFlowDesc)"	
	 * 4"會簽完成To流程步驟(finishTo)"	
	 * 5"會簽退回To流程步驟(returnTo)"
	 */
	String[][] subFlowConfArry = new String[][]{
		{"f0023","sub1","法律顧問簽核","會簽","Task8","Task1"},
		{"f0023","sub2","財務控管經理簽核","會簽","Task8","Task1"}
	};
	
	/**
	 * 0"步驟類型(steptype)"	
	 * 1"流程代碼(flowId)"	
	 * 2"步驟代碼(stepId)"	
	 * 3"步驟名稱(stepName)"	
	 * 4"步驟描述(stepDesc)"	
	 * 5"顯示順序(dispOrd)"	
	 * 6"審核部門(deptId)"	
	 * 7"審核角色(roleIds)"	
	 * 8"審核使用者(userIds)"
	 */
	String[][] flowStepArry = new String[][]{
		{"C","f0020","Task1","申請人","案件申請","1","","","",""},
		{"C","f0020","Task2","單位主管","單位主管審核","2","","","F000022552",""},
		{"C","f0020","Task3","物流食品主管(Logistic-Food)","物流食品主管審核","3","","","F000001644",""},
		{"C","f0020","Task4","後勤單位主管(Logistic)","後勤單位主管審核","4","","","F000001637",""},
		{"C","f0020","Task5","OSSC供應鏈主管(OSSC Supply Chain)","OSSC供應鏈主管審核","5","","","F000000137",""},
		{"C","f0020","Task6","OSD分區主管(OSD-Division)","OSD分區主管審核","6","","","F000002099",""},
		{"C","f0020","Task7","法律顧問","法律顧問簽核","7","","法律顧問","",""},
		{"C","f0020","Task8","法務長","法務長簽核","8","","法務長","",""},
		{"C","f0020","Task9","財務總監","財務總監簽核","9","","財務總監","",""},
		{"C","f0020","Task10","總經理","總經理簽核","10","","總經理","",""},
		
		{"C","f0023","Task1","申請人","案件申請","1","","","",""},
		{"C","f0023","Task2","單位主管","單位主管審核","2","","","F000022552",""},
		{"C","f0023","Task3","物流食品主管(Logistic-Food)","物流食品主管審核","3","","","F000001644",""},
		{"C","f0023","Task4","後勤單位主管(Logistic)","後勤單位主管審核","4","","","F000001637",""},
		{"C","f0023","Task5","OSSC供應鏈主管(OSSC Supply Chain)","OSSC供應鏈主管審核","5","","","F000000137",""},
		{"C","f0023","Task6","OSD分區主管(OSD-Division)","OSD分區主管審核","6","","","F000002099",""},
		{"P","f0023","Task7","財務會簽","財務會簽審查","7","","","",""},
		{"C","f0023","Task8","財務總監","財務總監簽核","8","","財務總監","",""},
		{"C","f0023","SubTask1","法律顧問","法律顧問簽核","1","","法律顧問","","sub1"},
		{"C","f0023","SubTask2","財務控管經理","財務控管經理簽核","1","","財務控管經理","","sub2"},
	};
	
	
	/*
	 * 0"流程代碼(flowId)"	
	 * 1"步驟代碼(stepId)"	
	 * 2"To流程步驟(toStepId)"	
	 * 3"動作類型(action)"	
	 * 4"是否多人並行會簽(isConcurrent)"	
	 * 5"步驟連結名稱(linkName)"	
	 * 6"步驟連結描述(linkDesc)"	
	 * 7"顯示順序(dispOrd)"
	 */
	
	String[][] flowStepLinkArry = new String[][]{
		{"f0020","Task1","Task2","Apply","0","送審","","10",""},
		{"f0020","Task1","Cancel","Cancel","0","作廢","","20",""},
		{"f0020","Task2","Task3","Approve","0","核准","","10",""},
		{"f0020","Task2","Task1","Return","0","退回","","20",""},
		{"f0020","Task3","Task4","Approve","0","核准","","10",""},
		{"f0020","Task3","Task1","Return","0","退回","","20",""},
		{"f0020","Task4","Task5","Approve","0","核准","","10",""},
		{"f0020","Task4","Task1","Return","0","退回","","20",""},
		{"f0020","Task5","Task6","Approve","0","核准","","10",""},
		{"f0020","Task5","Task1","Return","0","退回","","20",""},
		{"f0020","Task6","Task7","Approve","0","核准","","10",""},
		{"f0020","Task6","Task1","Return","0","退回","","20",""},
		{"f0020","Task7","Task8","Approve","0","核准","","10",""},
		{"f0020","Task7","Task1","Return","0","退回","","20",""},
		{"f0020","Task8","Task9","Approve","0","核准","","10",""},
		{"f0020","Task8","Task1","Return","0","退回","","20",""},
		{"f0020","Task9","Task10","Approve","0","核准","","10",""},
		{"f0020","Task9","Task1","Return","0","退回","","20",""},
		{"f0020","Task10","End","Approve","0","核准","","10",""},
		{"f0020","Task10","Task1","Return","0","退回","","20",""},
		
		{"f0023","Task1","Task2","Apply","0","送審","","10",""},
		{"f0023","Task1","Cancel","Cancel","0","作廢","","20",""},
		{"f0023","Task2","Task3","Approve","0","核准","","10",""},
		{"f0023","Task2","Task1","Return","0","退回","","20",""},
		{"f0023","Task3","Task4","Approve","0","核准","","10",""},
		{"f0023","Task3","Task1","Return","0","退回","","20",""},
		{"f0023","Task4","Task5","Approve","0","核准","","10",""},
		{"f0023","Task4","Task1","Return","0","退回","","20",""},
		{"f0023","Task5","Task6","Approve","0","核准","","10",""},
		{"f0023","Task5","Task1","Return","0","退回","","20",""},
		{"f0023","Task6","Task7","Approve","0","核准","","10",""},
		{"f0023","Task6","Task1","Return","0","退回","","20",""},
		{"f0023","Task7","Sub1","","0","會簽","","10",""},
		{"f0023","Task7","Sub2","","0","會簽","","20",""},
		{"f0023","Task8","End","Approve","0","核准","","10",""},
		{"f0023","Task8","Task1","Return","0","退回","","20",""},
		{"f0023","Sub1Task1","Sub1Finish","Approve","0","核准","","10",""},
		{"f0023","Sub1Task1","Sub1Return","Return","0","退回","","20",""},
		{"f0023","Sub2Task1","Sub2Finish","Approve","0","核准","","10",""},
		{"f0023","Sub2Task1","Sub2Return","Return","0","退回","","20",""}
	};
	String[][] formConfs = new String[][]{
		{"f0020","開發不動產約賃合約","/contract/formf0020","APPKIS","admin","admin"},
		{"f0023","其他工程合約-500萬以上","/contract/formf0023","APPKIS","admin","admin"},
	};
	
	Map<String,String>  rmaps = new HashMap();
	String CATEGORY_MEMBER_TYPE = "MemType";
	Map<String,String>  roleMap= new HashMap<String,String>(); //jobCode/jobTitle
	Map<String,String>  deptMap= new HashMap<String,String>(); //orgCode/orgName
	
	@Autowired
	DocStateRepository docStateRepository;
	
	@Autowired
	XauthService xauthService;
	
	@Autowired
	private FlowService flowService;
	
	@Autowired
	private FlowQueryService flowQueryService;
	
	@Autowired 
	FlowAdminService flowAdminService;
	
	@Autowired
	SubFlowConfRepository subFlowConfRepository;
	
	@Autowired
	FlowStepLinkRepository flowStepLinkRepository;
	
	@Autowired
	FlowStepRepository flowStepRepository;
	
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
	UserDeptRoleRepository  userDeptRoleRepository;
	
	@Autowired
	OrgService orgService;
	@Autowired
	FormConfRepository formConfRepository;
	

	@Autowired
	private XauthUsersRepository xauthUsersRepository;
	
	//3. DB TO XML (FLOW)
	//@Test
	//@Transactional
	//@Rollback(false)
	public void testFlowDbGenXmlToDB() throws Exception {
		//將儲存於db的flowConf 寫入 flowxml(專案將由讀取flow db 處裡)		
	//	List<Map<String, Object>> stepsNotBySubFlow = flowStepRepository.findFlowStepsNotBySubFlow("f0020");
		//System.out.println("::::"+  stepsNotBySubFlow.size());
		//for (Map<String, Object> map : stepsNotBySubFlow) {
		//	String stepType = (String)map.get("stepType".toUpperCase());
		//	System.out.println("stepType::"+ stepType);
			//System.out.println(JsonUtils.obj2json(map));
		//}
		this.genflowXmlforFlowConfDb("f0020");
		this.genflowXmlforFlowConfDb("f0023");
	}
	public String getSysId() {
		return "APPKIS";
	}
	
	//4. 測流程 
//	@Test
//	@Transactional
//	@Rollback(false)
	public void testFlowControl()  {
		//admin 送審
		String formId="f0020"; //合約表單對應  
		List<DocStateBean> result= null;
		
		//需指定合約單號
		String applyNo ="k"+DateUtil.getSysDateStr(DateUtil.FMT_YYYYMMDDHHMMSS);
		try {
//			FormConf formConf = formConfRepository.getEntityByProperty("formId", formId);
//			if(null==formConf || StringUtils.isEmpty(formConf.getFormId())) { //沒有表單資料,建立對應表單資料
//				for (String[] strings : formConfs) {
//					 	formConf  = new FormConf(strings[0], strings[1],  strings[2],strings[3], "admin");
//						formConfRepository.save(formConf);
//				}
//				
//			}
//		
			
			this.applyForm(formId, applyNo, 
					formId, DateUtil.getSysDateStr(DateUtil.FMT_YYYYMMDDHHMMSS),getCreateUserDto("admin", "admin")) ;
			
			result = flowQueryService.fetchToDoListBy("admin", getSysId(), false);
			for (DocStateBean docStateBean : result) {
				if(//applyNo.equals(docStateBean.getApplyNo()) 
					//	&&
						"f0020".equals(docStateBean.getFlowId())){
					System.out.println(JsonUtils.obj2json(docStateBean));
					flowService.apply(docStateBean.nowUser(getCreateUserDto("admin", "admin"), "Task1"), null);
					
					
				};
				
			}
//			DocStateBean docState = flowQueryService.getDocStateById(dto.getDocStateId());
			// 認領案件
			//docState = flowService.claim(docState.nowUser(updateUserDto, dto.getTaskId()));
			//F000022552登入, 找單, 審單, 
			result = flowQueryService.fetchToDoListBy("F000022552", getSysId(), false);
			for (DocStateBean docStateBean : result) {
				if(//applyNo.equals(docStateBean.getApplyNo())  &&
						"f0020".equals(docStateBean.getFlowId())){
					System.out.println("====================");
					System.out.println(JsonUtils.obj2json(docStateBean));
					this.returnForm(String.valueOf(docStateBean.getId()), docStateBean.getTaskId(), "F000022552", "退回測試");
				};
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
			
		
	}
	
	public void approveForm(String docStateId,String taskId,String userId ,String flowComment ) {
		DocStateBean docState = flowQueryService.getDocStateById(Long.valueOf(docStateId));
		
//		FormConf formConf = formConfRepository.getEntityByProperty("formId", flowBean.getFormId());
		// 認領案件
		docState = flowService.claim(docState.nowUser(getUpdateUserDto(userId, "admin"), userId));
		//	docState = flowService.process(docState.nowUser(updateUserDto, dto.getTaskId()).comment(dto.getFlowComment()), action);
		// 送出審核案件
		docState = flowService.process(docState.nowUser(getUpdateUserDto(userId, "admin"), taskId).comment(flowComment), DefaultFlowAction.APPROVE);

	}
	
	public void cancelForm(String docStateId,String taskId,String userId ,String flowComment ) {
		DocStateBean docState = flowQueryService.getDocStateById(Long.valueOf(docStateId));
		// 認領案件
		docState = flowService.claim(docState.nowUser(getUpdateUserDto(userId, "admin"), userId));
		//	docState = flowService.process(docState.nowUser(updateUserDto, dto.getTaskId()).comment(dto.getFlowComment()), action);
		// 送出審核案件
		docState = flowService.process(docState.nowUser(getUpdateUserDto(userId, "admin"), taskId).comment(flowComment), DefaultFlowAction.CANCEL);

	}

	public void returnForm(String docStateId,String taskId,String userId ,String flowComment ) {
		DocStateBean docState = flowQueryService.getDocStateById(Long.valueOf(docStateId));
		try {
			System.out.println(JsonUtils.obj2json(docState));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 認領案件
		docState = flowService.claim(docState.nowUser(getUpdateUserDto(userId, "admin"), taskId));
		//	docState = flowService.process(docState.nowUser(updateUserDto, dto.getTaskId()).comment(dto.getFlowComment()), action);
		// 送出審核案件
		docState = flowService.process(docState.nowUser(getUpdateUserDto(userId, "admin"), taskId).comment(flowComment), DefaultFlowAction.RETURN);

	}
	
	//2. FLOW 設定
	//@Test
	//@Transactional
	//@Rollback(false)
	public void testFlowConfig() throws Exception {
		//this.changePassword();
		//1.建立CRC流程樣版
		//
	//	String flowId="f0020";
	//	String flowName="開發不動產約賃合約";
	//	String flowDesc="含士地及房屋";
	//	String subFlowId="";
	//	flowService.f
		//f0020
		//f0021
		//f0022
//		for (String[] strings : formConfs) {
//			FormConf formConf  = new FormConf(strings[0], strings[1],  strings[2],strings[3], "admin");
//			formConfRepository.save(formConf);
//		}
//		
//        //flowConfArry
//		for (int i = 0; i < flowConfArry.length; i++) {
//			String[] strings = flowConfArry[i];
//			setFlowConf(strings[0], strings[1], strings[3]);
//		}
//		
		//flowStepArry
//		for (int i = 0; i < flowStepArry.length; i++) {
//			String[] strings = flowStepArry[i];
//			int j =0;
//			setFlowSetup( strings[j++],strings[j++],  strings[j++]
//					,   strings[j++], strings[j++],   strings[j++], 
//					strings[j++], strings[j++],   strings[j++],strings[j++]);
//		}
		
		//subFlowConfArry
		for (int i = 0; i < subFlowConfArry.length; i++) {
			String[] strings = subFlowConfArry[i];
			int j =0;
			setSubFlowConf(strings[j++],strings[j++], strings[j++], strings[j++], strings[j++],strings[j++]) ;
		}
		
		//flowStepLinkArry
		for (int i = 0; i < flowStepLinkArry.length; i++) {
			String[] strings = flowStepLinkArry[i];
			int j =0;
			setFlowStepLink(strings[j++], strings[j++], strings[j++], strings[j++], strings[j++], strings[j++], strings[j++], strings[j++]);
		}
		
		 //.getEntityBySQL("select * from XAUTH_USERS  where USER_ID = ? ", "admin");
		
		
		//2.設定db資料
		
		//3.設定
	}
	
	
	private DocStateBean startFlow(String formId,String keyId,String flowId,String key2Id, CreateUserDto createUserDto) {
		FlowBean flowBean = new FlowBean();
		
		flowBean.setFormId(formId);                                      // 表單代碼
		flowBean.setApplyNo(keyId);                             // 申請單主檔單號
		flowBean.setFlowId(flowId);                                      // 流程代碼
		flowBean.setFlowVersion(flowService.getNowFlowVersion(flowId));  // 取得目前流程最新版本
		flowBean.setSysId("APPKIS");                                         // 系統代碼
		flowBean.setSubject("測試流程:"+ flowId);                               // 主旨
		flowBean.setMainFormId(Long.valueOf(key2Id));                              // 申請單主檔ID
		
		// 啟動申請單流程
		return flowService.startFlow(flowBean.nowUser(createUserDto), null);
	}
	
	public void applyForm(String formId,String keyId,String flowId,String key2Id, CreateUserDto createUserDto) {
		DocStateBean docStateBean = null;
		docStateBean = this.startFlow(formId, keyId, flowId, key2Id,  createUserDto) ;
//		flowService.apply(docStateBean.nowUser(createUserDto, "Task1"), null);

		//docStateBean = this.getDocStateById(Long.valueOf("123"));
	}
	
	
	/**
	 * 產生db xml
	 * @param flowId
	 */
	public void genflowXmlforFlowConfDb(String flowId) {
		
		try {
			if (! flowAdminService.hasFlowSteps(flowId)) {
				//產生XML前，請先新增關卡及連結！
				System.out.println("產生XML前，請先新增關卡及連結！");
			}
			if (flowAdminService.isFlowXmlShouldBeUpdated(flowId)) {
				FlowConfDto flowDto = flowAdminService.getFlowConfByFlowId(flowId);
				flowAdminService.addFlowXml(flowDto, null, getCreateUserDto("admin",""), false);
				System.out.println("gen dbXML done");
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public DocStateBean getDocStateById(Long docStateId) {
		DocState state = docStateRepository.getEntityById(docStateId);
		if (state != null) {
			return new DocStateBean(state, null, null, null, false, null);
		}
		return null;
	}
	
	/**
	 * 取得使用者
	 * @param userId
	 * @param agentId
	 * @return
	 */
	protected CreateUserDto getCreateUserDto(String userId,String agentId) {
		CreateUserDto dto = new CreateUserDto();
		Date now = new Date();
		dto.setCreateUserId(userId);
		dto.setCreateTime(now);
		dto.setUpdateUserId(userId);
		dto.setUpdateTime(now);
		dto.setCreateAgentId(agentId);
		dto.setUpdateAgentId(agentId);
		return dto;
	}
	
 	protected UpdateUserDto getUpdateUserDto(String userId,String agentId) {
		UpdateUserDto dto = new UpdateUserDto();
		dto.setUpdateUserId(userId);
		dto.setUpdateTime(new Date());
		dto.setUpdateAgentId(agentId);
		return dto;
	}

	
	/**
	 * 設定flwConf
	 * @param flowId
	 * @param flowName
	 * @param flowDesc
	 */
	public void setFlowConf(String flowId, String flowName, String flowDesc) {
		FlowConfDto serchflowConfDto= flowAdminService.getFlowConfByFlowId(flowId);
		
		FlowConfDto flowConfDto = new FlowConfDto();
		flowConfDto.setCreateUserId("SYSTEM");
		flowConfDto.setFlowId(flowId);
		flowConfDto.setFlowAdminId("admin");
		flowConfDto.setSysId("APPKIS");
		flowConfDto.setCreateTime(DateUtil.getSysDateStr(DateUtil.FMT_YYYYMMDDHHMMSS));
		flowConfDto.setUpdateTime(DateUtil.getSysDateStr(DateUtil.FMT_YYYYMMDDHHMMSS));
		flowConfDto.setName(flowName);
		flowConfDto.setDesc(flowDesc);
		flowConfDto.setUpdateUserId("SYSTEM");
		
		CreateUserDto createUserDto = getCreateUserDto("admin","");
		
		if(null!=serchflowConfDto && StringUtils.isNoneBlank(serchflowConfDto.getFlowId())) {
			FlowConf flowConf = flowConfRepository.getEntityById(serchflowConfDto.getId());
			BeanUtil.copyPropertiesIgnoreNull(flowConfDto, flowConf, "id", "createUserId", "createTime");
			BeanUtils.copyProperties(createUserDto, flowConf);
			flowConfRepository.save(flowConf);
		}else {
			flowAdminService.addFlowConf(flowConfDto, createUserDto, false, null);
		}
	}
	/**
	 * 
	 * @param steptype
	 * @param flowId
	 * @param stepId
	 * @param stepName
	 * @param stepDesc
	 * @param dispOrd
	 * @param deptId
	 * @param roleIds
	 * @param userIds
	 */
	public void setFlowSetup(String steptype,String flowId, String stepId
			,  String stepName, String stepDesc, String dispOrd, String deptId, String roleIds, String userIds
			,  String subFlowId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("stepType", (String)steptype);
		params.put("flowId", (String)flowId);
		params.put("stepId", (String)stepId);

		//FlowStep
		List<FlowStep>  serchflowSteps = flowStepRepository.findEntityListByJPQL("SELECT fs FROM FlowStep fs WHERE fs.stepType = ?1 AND fs.flowId = ?2  AND fs.stepId = ?3 ",params.get(0),params.get(1),params.get(2) );
		FlowStep flowStep= new FlowStep(flowId,stepId, steptype, stepName, stepDesc, Integer.valueOf(dispOrd), "SYSTEM");
		flowStep.setDeptId(deptId);
		flowStep.setRoleIds(roleIds);
		flowStep.setUserIds(userIds);
		flowStep.setSubFlowId(subFlowId);
		if(serchflowSteps!=null && !serchflowSteps.isEmpty()) {
			FlowStep sflowStep  =  serchflowSteps.get(0);
			BeanUtils.copyProperties(flowStep, sflowStep, "id");
			flowStepRepository.save(flowStep);
		}else {
			flowStepRepository.save(flowStep);
		}
		

	}
	/**
	 * 
	 * @param flowId
	 * @param subFlowId
	 * @param subFlowName
	 * @param subFlowDesc
	 * @param finishTo
	 * @param returnTo
	 */
	public void setSubFlowConf(String flowId, String subFlowId, String subFlowName, String subFlowDesc, String finishTo, String returnTo) {
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("flowId", (String)flowId);
		params.put("subFlowId", (String)subFlowId);
		

		//FlowStep
		List<SubFlowConf>  serchSubFlowConfs = subFlowConfRepository.findEntityListByJPQL("SELECT fs FROM SubFlowConf fs WHERE fs.flowId = ?1 AND fs.subFlowId = ?2  ",params.get(0),params.get(1) );
		SubFlowConf subFlowConf= new SubFlowConf(flowId, subFlowId, subFlowName, subFlowDesc, "SYSTEM");
		subFlowConf.setFinishTo(finishTo);
		subFlowConf.setReturnTo(returnTo);

		if(serchSubFlowConfs!=null && !serchSubFlowConfs.isEmpty()) {
			SubFlowConf sSubFlowConf  =  serchSubFlowConfs.get(0);
			BeanUtils.copyProperties(subFlowConf, sSubFlowConf, "id");
			subFlowConfRepository.save(subFlowConf);
		}else {
			subFlowConfRepository.save(subFlowConf);
		}
	}
	
	/**
	 * 
	 * @param flowId
	 * @param stepId
	 * @param toStepId
	 * @param action
	 * @param isConcurrent
	 * @param linkName
	 * @param linkDesc
	 * @param dispOrddispOrd
	 */
	public void setFlowStepLink(String flowId, String stepId, String toStepId, String action, String isConcurrent, String linkName
			, String linkDesc
			, String dispOrd) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("flowId", (String)flowId);
		params.put("stepId", (String)stepId);
		

		//FlowStep
		List<FlowStepLink>  serchFlowStepLinks = flowStepLinkRepository.findEntityListByJPQL("SELECT fs FROM FlowStepLink fs WHERE fs.flowId = ?1 AND fs.stepId = ?2  ",params.get(0),params.get(1) );
		FlowStepLink flowStepLink= new FlowStepLink(flowId, stepId, toStepId, action,
				Boolean.valueOf(isConcurrent), 
				linkName, linkDesc, Integer.valueOf(dispOrd),"SYSTEM");
	

		if(serchFlowStepLinks!=null && !serchFlowStepLinks.isEmpty()) {
			FlowStepLink sFlowStepLink  =  serchFlowStepLinks.get(0);
			BeanUtils.copyProperties(flowStepLink, sFlowStepLink, "id");
			flowStepLinkRepository.save(flowStepLink);
		}else {
			flowStepLinkRepository.save(flowStepLink);
		}
	}
	/**
	 * 讀取 部門,使用者,職稱(role)
	 * @throws Exception
	 */
	@Test
	@Transactional
	@Rollback(false)
	public void readEmpOrgTitle() throws Exception {
		
		     //載入 readTitle role map 
//			this.readTitle();
		     //建立部門資訊
//			this.readOrg();
		     //建立emp資料
			this.readEmp();

	}
	public void readEmp() throws Exception {
		//read emp file  to object
				try {
					List<UserProf>  users = new ArrayList<UserProf>();
					List<RoleProf>  roles = new ArrayList<RoleProf>();
					List<UserDeptRole> udrs = new ArrayList<UserDeptRole>();
					List<String> liset = FileUtils.readLines(new File("D:\\Project\\L285\\REF_EMP_DATA_2.csv"),"Big5");
					for (int i = 1; i < liset.size(); i++) {
						String[]  values = liset.get(i).split(",");
						System.out.println(values[36]);
						Emp emp = new Emp(values[0], values[1],  values[2],  values[3],  values[8],  values[20], deptMap.get(values[8]), values[11],  values[18], roleMap.get(values[18]),values[36] );
						users.add(emp.converUserProf());
						RoleProf role= emp.genRole(roleMap,rmaps);
						if(role!=null && StringUtils.isNoneBlank(role.getName())){
							System.out.println("has role" +role.getRoleId());
							roles.add(role);
						}
						UserDeptRole udr  = emp.genRoleUser();
						if(udr!=null) {
							System.out.println("has udr" +udr.getRoleId()+","+udr.getUserId());
							udrs.add(udr);
						}
						
					}
					Date ts = new Timestamp(new Date().getTime());
					Map<String,String> params= new HashMap<String,String>();
					DateUtil.formatDate(new Date(), DateUtil.FMT_YYYY_MM_DD);
					for (UserProf uu : users) {
						UserInfoExt userInfoExt = new UserInfoExt();
						QueryWrapper<UserInfoExt> userInfoExtWrapper = new QueryWrapper<UserInfoExt>();
						userInfoExtWrapper.eq("APP_ID", uu.getAppId());
						userInfoExtWrapper.eq("IDEN_ID", uu.getDeptId());
						userInfoExtWrapper.eq("USER_ID", uu.getUserId());
						userInfoExt = userInfoExt.selectOne(userInfoExtWrapper);
						if (userInfoExt != null) {				

							userInfoExt.setBgnDate(DateUtils.converStr2Date(DateUtil.formatDate(new Date(), DateUtil.FMT_YYYY_MM_DD), "yyyy/MM/dd"));			
							userInfoExt.setEndDate(DateUtils.converStr2Date(MapUtils.getEmptyString(params, "endDate"), "yyyy/MM/dd"));
							userInfoExt.setUpdUser("SYSTEM");
							userInfoExt.setUpdDate(ts);
							userInfoExt.update(userInfoExtWrapper);
						}
						else {
							userInfoExt = new UserInfoExt();
							userInfoExt.setAppId(uu.getAppId());
							userInfoExt.setIdenId( uu.getDeptId());
							userInfoExt.setUserId(uu.getUserId());
							userInfoExt.setBgnDate(DateUtils.converStr2Date(DateUtil.formatDate(new Date(), DateUtil.FMT_YYYY_MM_DD), "yyyy/MM/dd"));			
							userInfoExt.setEndDate(DateUtils.converStr2Date(MapUtils.getEmptyString(params, "endDate"), "yyyy/MM/dd"));
							userInfoExt.setCreUser("SYSTEM");
							userInfoExt.setCreDate(ts);
							userInfoExt.insert();
						}
					}
					System.out.println("run userProfRepository");
					userProfRepository.saveInBatch(users, users.size());
					users.clear();
					users = null;
					
					System.out.println("run roleProfRepository");
					roleProfRepository.saveInBatch(roles, roles.size());
					roles.clear();
					roles = null;
					
					System.out.println("run userDeptRoleRepository");
					userDeptRoleRepository.saveInBatch(udrs, udrs.size());
					udrs.clear();
					udrs = null;
					
					liset.clear();
					liset = null ;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	public void readOrg() {
		//read org file to object
				try {
					List<DeptProf>  depts = new ArrayList<DeptProf>();
					List<String> liset = FileUtils.readLines(new File("D:\\Project\\L285\\REF_ORG_DATA _1.csv"),"Big5");
					for (int i = 1; i < liset.size(); i++) {
						String[]  values = liset.get(i).split(",");
						deptMap.put(values[3], values[4]);
						depts.add((new Dept("APPKIS",values[3], values[5], "", "SYSTEM", values[4], 0, 0)).converDeptProf());
					}
					Date currentDate = new Timestamp(new Date().getTime());
					for (DeptProf dd : depts) {
						DeptInfoExt deptInfoExt = new DeptInfoExt();
						QueryWrapper<DeptInfoExt> wrapper = new QueryWrapper<DeptInfoExt>();
						wrapper.eq("APP_ID", dd.getAppId());
						wrapper.eq("IDEN_ID", dd.getDeptId());
						deptInfoExt = deptInfoExt.selectOne(wrapper);
						if (deptInfoExt != null) {
							deptInfoExt.setIdenType("01");  //01通路,02供應商
							deptInfoExt.setUpdUser("SYSTEM");
							deptInfoExt.setUpdDate(currentDate);
							deptInfoExt.update(wrapper);
						}
						else {
							deptInfoExt = new DeptInfoExt();
							deptInfoExt.setAppId(dd.getAppId());
							deptInfoExt.setIdenId( dd.getDeptId());
							deptInfoExt.setIdenType("01"); //01通路,02供應商
							deptInfoExt.setCreUser("SYSTEM");
							deptInfoExt.setCreDate(currentDate);
							deptInfoExt.insert();
						}
					}
					deptProfRepository.saveInBatch(depts, depts.size());
					depts.clear();
					depts = null;
					liset.clear();
					liset = null ;
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	}
	public void readTitle() {
		//read title file to object
		try {
			List<String> liset =FileUtils.readLines(new File("D:\\Project\\L285\\REF_TITLE.csv"),"UTF-8");
			for (int i = 1; i < liset.size(); i++) {
				String[]  values = liset.get(i).split(",");
				roleMap.put(values[0], values[2]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void changePassword() {
		
		//sql.append(" select u from UserProf u where status = 'Y' and sysId in ('ALL', ?) and userId = ? ");
		//return getEntityByJPQL(sql.toString(), sysId, userId);			
		UserProf   u=  userProfRepository
				.getEntityBySQL("select * from XAUTH_USERS  where USER_ID = ? ", "admin");
		        System.out.println("==================================");
				//findListMapBySQL("select * from FlowConf where flowId=?0 ", new Object[] {"SampleFlow"});
		if(null!= u ) {
			//UserProf 	u = s.get(0);
			SensitiveUtils sens = new SensitiveUtils();
			try {
				SealedObject sealObject = sens.encrypt("1234",u.getUserId());
				u.setPwdHash("1234");
				u.setUserPw(new BCryptPasswordEncoder().encode(sens.decrypt(sealObject,u.getUserId()).toString()));
				userProfRepository.save(u);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		}
		
	}
	//@Test
	public void test1() throws Exception {
//		XauthUsersKey key = new XauthUsersKey();
//		key.setAppId("APPKIS");
//		key.setIdenId("00000000");
//		key.setUserId("admin");		
//		XauthUsers users = xauthUsersRepository.getOne(key);
//		if (users != null) {
//			System.out.println(JsonUtils.obj2json(users));			
//		}
//		else {
//			System.out.println("no data");
//		}
		String flowId="SampleFlow";

		List<com.tradevan.jpa.entity.XauthUsers> xlist= xauthUsersRepository.findAll();
		for (com.tradevan.jpa.entity.XauthUsers xauthUsers : xlist) {
			System.out.println("KKKK::" +xauthUsers.getUserId());
		}
		//List<FlowXml> s= (List<FlowXml>) flowXmlRepository.getByNamedQuery("FlowXml.findAll", null);
		List<FlowXml>  s=flowXmlRepository.findAllEntityList();
				//flowXmlRepository.findByJPQL("select * FlowXml", null);
		for (FlowXml flowXml : s) {
			System.out.println("flowXml::" +flowXml.getFlowXml());
		}
	//	FlowConfDto docState;
		//Object[]  o=null;
		List<Map<String, Object>> ff =  flowConfRepository.findListMapBySQL("select * from FlowConf where flowId=?0 ", new Object[] {"SampleFlow"});
		for (Map<String, Object> a : ff) {
			
			System.out.println("nnn::-" +a.get("FLOWNAME"));
		}
		agentProfRepository.findAllEntityList();
		userProfRepository.findAllEntityList();
		roleProfRepository.findAllEntityList();
		deptProfRepository.findAllEntityList();
	//	orgService.fetchAllDepts(true);
		
		//orgService.fetchAllRoles(true);
	//	orgService.fetchAllSystems();
	//	orgService.fetchUsersInUserDeptRole("", "");
		
		
		
		//docState = flowAdminService.getFlowConfByFlowId(flowId);
	
		//if(docState!=null) {
		//	System.out.println("::::abc:::: " +JsonUtils.obj2json(docState));
		//}else {
		//	System.out.println("not found");
		//}

//		FlowBean flowBean = new FlowBean();
//		
//		flowBean.setFormId(FORM_ID);                                      // 表單代碼
//		flowBean.setApplyNo("11234555");                             // 申請單主檔單號
//		flowBean.setFlowId(FLOW_ID);                                      // 流程代碼
//		flowBean.setFlowVersion(flowService.getNowFlowVersion(FLOW_ID));  // 取得目前流程最新版本
//		flowBean.setSysId(SYSTEM_EDU);                                         // 系統代碼
//		flowBean.setSubject("訓練計畫送審");                               // 主旨
//		flowBean.setMainFormId(Long.valueOf("11234555"));                              // 申請單主檔ID
		

	}
	
}
class Emp {
	String empNo;
	String loginName;
	String empName;
	String mailInternal;
	String orgCode;
	String noJob;
	String orgName;
	String directManager;
	String jobCode;
	String jobTitle;
	String sex;
	public Emp(String empNo,
			String loginName,
			String empName,
			String mailInternal,
			String orgCode,
			String noJob,
			String orgName,
			String directManager,
			String jobCode,
			String jobTitle,
			String sex) {
		this.empNo			=empNo;
		this.loginName     =loginName;
		this.empName       =empName;
		this.mailInternal  =mailInternal;
		this.orgCode       =orgCode;
		this.noJob         =noJob;
		this.orgName       =orgName;
		this.directManager =directManager;
		this.jobCode       =jobCode;
		this.jobTitle      =jobTitle;
		this.sex           =sex;
	}
	public UserProf converUserProf() {
		UserProf  u= new  UserProf();
		
		u.setAppId("APPKIS");
		u.setDeptId(this.orgCode);
		u.setUserId(this.empNo);
		u.setCreateTime(new Date());
		u.setName(this.empName);
		u.setEmail(this.mailInternal);
		u.setStatus("1");
		u.setUserType(UserType.APCRC02.getCode());
		u.setCreateUserId("SYSTEM");
		u.setIsFirst("1");
		
		SensitiveUtils sens = new SensitiveUtils();
		try {
			SealedObject sealObject = sens.encrypt("1234",u.getUserId());
			u.setPwdHash("1234");
			u.setUserPw(new BCryptPasswordEncoder().encode(sens.decrypt(sealObject,u.getUserId()).toString()));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return  u;
	}
	public UserDeptRole genRoleUser() {
		return new UserDeptRole("APPKIS",this.empNo,this.orgCode,"ROLE_"+this.jobCode,"SYSTEM");
	}
	public RoleProf genRole(Map<String,String> m,Map<String,String> rm) {
		
		if((m.get(this.jobCode)==this.jobTitle ) && rm.get("APACR"+this.jobCode+this.orgCode)==null) {
			RoleProf r= new RoleProf();
			r.setAppId("APPKIS");
			r.setRoleId("ROLE_"+this.jobCode);
			r.setName(this.jobTitle); 
			r.setLevel(0);
			//r.isDeptRole = isDeptRole;
			r.setSysId("APPKIS");
			r.setDeptId(this.orgCode);
			r.setIsDeptRole(false);
			//r.category = category;
			r.setStatus("1");// = "Y";
			r.setCreateTime(new Date());
			r.setCreateUserId("SYSTEM");
			rm.put("APACR"+this.jobCode+this.orgCode, "APACR"+this.jobCode+this.orgCode);
			//r.createUserId = createUserId;
			//r.updateUserId = createUserId;
			//r now = new Date();
			//r.createTime = now;
			//r.updateTime = now;
			return r;
		}
		
		return null;
	}
	
} 
class Dept{
	  String appId;
	  String idenId;
	  String upDeptId;
	  String ban;
	  String createDate;
	  String createUser;
	  String cname;
	  int seqNo;
	  int parentSeq;
	  
	  public Dept(String appId,
			  String idenId,
			  String upDeptId,
			  String ban,
			  String createUser,
			  String cname,
			  int seqNo,
			  int parentSeq) {
		  
		    this.appId=appId;
			this.idenId=idenId;
			this.upDeptId=upDeptId;
			this.ban=ban;
			this.createUser=createUser;
			this.cname=cname;
			this.seqNo=seqNo;
			this.parentSeq=parentSeq;
	  }
	  public DeptProf converDeptProf() {
			DeptProf  d= new  DeptProf();
			
			d.setAppId("APPKIS");
			d.setStatus("1");
			d.setLevel(0);
			d.setDeptId(this.idenId);
			d.setParentSeq(this.parentSeq);
			if("984".equals(this.idenId) && StringUtils.isBlank(this.upDeptId)) {
				d.setUpDeptId("00000000");
			}else {
				d.setUpDeptId(this.upDeptId);
			}
			d.setName(this.cname);
			d.setSeqNo(seqNo);
			d.setCreateTime(new Date());
			d.setCreateUserId("SYSTEM");
			
			
			return  d;
		}
}
class role {
	   String roleId;
	   String roleName;
	   
}
