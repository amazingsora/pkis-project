package com.tradevan.aporg.enums;

/**
 * Title: UserState<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public enum UserState {

	A("在職"),
	C("不在職");
	
	private String state;
	
	private UserState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}
