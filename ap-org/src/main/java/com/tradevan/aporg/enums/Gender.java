package com.tradevan.aporg.enums;

/**
 * Title: Gender<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public enum Gender {

	M("男","M","male"),
	F("女","F","female");
	public static final String I18N_CODE="irbGlobal.option.";
	
	private String gender;
	private String code;
	private String value;
	
	private Gender(String gender, String code, String value) {
		this.gender = gender;
		this.code = code;
		this.value = value;
	}

	public String getGender() {
		return gender;
	}

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getI18Ncode() {
		return I18N_CODE+value;
	}

	
	
}
