package com.tradevan.pkis.web.bean;

import java.util.Date;

/**
 * 作 業 代 碼 ：UserData<br>
 * 作 業 名 稱 ：使用者擴充資料<br>
 * 程 式 代 號 ：UserData<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : penny<br>
 * @version  : 1.0.0 2020/3/16<P>
 */
public class UserData {

	private String idenType;
	
	private Date bgnDate;
	
	private Date endDate;

	public String getIdenType() {
		return idenType;
	}

	public void setIdenType(String idenType) {
		this.idenType = idenType;
	}

	public Date getBgnDate() {
		return bgnDate;
	}

	public void setBgnDate(Date bgnDate) {
		this.bgnDate = bgnDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
