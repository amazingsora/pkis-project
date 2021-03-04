package com.tradevan.handyflow.bean;

/**
 * Title: DefaultFlowEvent<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Sephiro Huang
 * @version 1.1
 */
public enum DefaultFlowEvent{
	BEGIN("Begin", "申請開始", "BEGIN"),
	END("End", "申請完成", "END"),
	CANCEL("Cancel", "申請單作廢", "CANCEL"),
	SUB_BEGIN("Begin", "會簽", "SUB_BEGIN"),
	SUB_FINISH("Finish", "會簽完成", "SUB_FINISH"),
	SUB_RETURN("Return", "會簽退回", "SUB_RETURN");
	
	String event;
	String name;
	String type;
	
	private DefaultFlowEvent(String event, String name, String type) {
		this.event = event;
		this.name = name;
		this.type = type;
	}

	public String getEvent() {
		return this.event;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getType() {
		return this.type;
	}
	
}
