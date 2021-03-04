package com.tradevan.handyflow.bean;

import com.tradevan.handyflow.model.flow.LinkAction;

/**
 * Title: DefaultFlowAction<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.2.1
 */
public enum DefaultFlowAction implements LinkAction {
	
	APPLY("Apply", "送出申請"),
	CANCEL("Cancel", "作廢申請"),
	SUBMIT("Submit", "送出"),
	APPROVE("Approve", "審查同意"),
	APPROVE2("Approve2", "審查同意"),
	FINAL_APPROVE("FinalApprove", "審查同意*"),
	RETURN("Return", "審查退回"),
	COUNTERSIGN("Countersign", "送出會簽"),
	PARALLEL_COUNTERSIGN("ParallelCountersign", "平行會簽"),
	
	//------ Irb 
	APPLY_NEW_PROJ_CASE_REPORT("ApplyCa", "Case Report"),
	APPLY_NEW_PROJ_GENERAL("ApplyGe", "一般案件"),
	APPLY_NEW_PROJ_SIMPLE("ApplySi", "簡審案件"),
	APPLY_NEW_PROJ_EXEMPT("ApplyEx", "免審案件"),
	FORCE_MEETING("forceMeeting", "會議送審"), //issue 239：複審行政審查時，多一條路可以將申請項目直接排入會議(by Sephiro) 
	REASSIGN("Reassign","重新分派");
	
	
	String action;
	String name;
	
	private DefaultFlowAction(String action, String name) {
		this.action = action;
		this.name = name;
	}

	@Override
	public String getAction() {
		return this.action;
	}
	
	public String getName() {
		return this.name;
	}
	
}
