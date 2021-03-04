package com.tradevan.aporg.enums;

/**
 * Title: UserType<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public enum UserType {

	IN("院內", "IN", "in"),
	OUT("院外","OUT", "out"),
	APCRC00("系統管理員","00", "00"),
	APCRC01("管理員","01", "01"),
	APCRC02("使用者","02", "02");
	
	private String type;
	private String code;
	private String value;
	
	public static final String I18N_CODE="irbGlobal.option.";
	
	private UserType(String type, String code, String value) {
		this.type = type;
		this.code = code;
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getI18Ncode() {
		return I18N_CODE+value;
	}
}
