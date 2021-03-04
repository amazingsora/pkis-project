package com.tradevan.aporg.enums;

/**
 * Title: MailCategory<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public enum MailCategory {

	SYSTEM_MONITOR("系統監控"),
	SYSTEM_BATCH("批次轉檔"),
	TODO_NOTICE("待辦提醒"),
	EDU_AFTER_ALERT("課後試、問卷、報告提醒"),
	EDU_ASSESSMENT("360度評量通知"),
	EDU_BEFORE_ALERT("課前問卷提醒"),
	EDU_BULLETIN_ALERT("課程公告通知"),
	EDU_CANCEL_ALERT("取消報名通知"),
	EDU_CLASS_ALERT("報名人數不足提醒"),
	EDU_COURSE_ALERT("上課提醒"),
	EDU_CREDIT_ALERT("院內學分不足提醒"),
	EDU_DEDUCT_ALERT("受訓報告扣假提醒"),
	EDU_EXAM_ACTIVITY("試卷調查活動通知"),
	EDU_IC6HR_ALERT("感控6小時未完成提醒"),
	EDU_MENTOR_RESIGN("導師更換提醒"),
	EDU_MOHW_ALERT("衛福部繼續教育積分不足提醒"),
	EDU_PASSPORT_OVERDUE("學習項目/評量逾期提醒"),
	EDU_PASSPORT_UNDONE("學習項目/評量未完成提醒"),
	EDU_PAYMENT_ALERT("報名繳費通知"),
	EDU_PREEMP_ALERT("職前人員課程通知"),
	EDU_PREEMPEXCEPTION_ALERT("準員工職前訓練上課類別異常提醒"),
	EDU_PREEMPSTAT_ALERT("新進人員職前訓練課程未完成提醒"),
	EDU_QUES_ACTIVITY("問卷調查活動通知"),
	EDU_REPORT_ALERT("受訓報告提醒"),
	EDU_SIGNUP_ALERT("報名完成通知"),
	EDU_TARGET_ALERT("指定上課提醒"),
	EDU_TEACHPOINT_ALERT("師培點數不足提醒"),
	IRB_OUTSTAFF("外部帳號申請"),
	ALL_FORGOT_PWD("忘記密碼申請"),
	IRB_VISIT("實地訪查通知"),
	IRB_STATECHANGECONTINUE_NOTI("中止繼續申請審查通知"),//Title暫記
	IRB_STATECHANGEWD_NOTI("撤案申請審查通知"),//Title暫記
	IRB_F_PAPER_ALERT("結案報告提醒"),
	IRB_F_PAPER_D_ALERT("結案報告超過期限未繳交警告"),
	IRB_I_PAPER_ALERT("期中報告提醒"),
	IRB_I_PAPER_D_ALERT("期中報告超過期限未繳交警告"),
	IRB_REVIEW_ALERT("審查通知提醒"),
	IRB_DEVIA_NOTI_ALERT("偏差通報複審通知提醒"),
	IRB_EXPENSES_ALERT("經費不足通知"),
	IRB_REVIEW_DONE("審查完成通知"),
	IRB_MEETING_NOTICE("開會通知"), //issue 363：會議議程執行秘書複審完成，寄發會議議程通知給審查委員(by Sephiro)
	RSCH_IMPORT("院內計畫匯入通知"),
	RSCH_HOSTPAY("主持費計算通知"),
	RSCH_REVIEW_DONE("審查完成通知"),
	RSCH_BUILD_PLAN("外部計畫建立通知"),
	RSCH_ASSISTANT_HIRE("助理聘用通知"),
	RSCH_ASSISTANT_FIRE("助理解聘通知"),
	RSCH_PAPER_SUBMIT_ALERT("報告繳交提醒"),
	RSCH_FUND_USAGE_ALERT("經費支用提醒"),
	COPR_REVIEW_DONE("審查結果通知"),
	COPR_BONUS_APPROVE("獎勵金核准通知"),;
	
	private String catetory;
	
	private MailCategory(String catetory) {
		this.catetory = catetory;
	}

	public String getCatetory() {
		return catetory;
	}

	public void setCatetory(String catetory) {
		this.catetory = catetory;
	}
	
}
