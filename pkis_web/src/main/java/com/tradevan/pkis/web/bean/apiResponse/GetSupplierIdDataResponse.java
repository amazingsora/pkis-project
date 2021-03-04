package com.tradevan.pkis.web.bean.apiResponse;

/**
 * 作 業 代 碼 ：GetSupplierIdDataResponse<br>
 * 作 業 名 稱 ：取得供應商資料API Response<br>
 * 程 式 代 號 ：GetSupplierIdDataResponse<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Yuan<br>
 * @version  : 1.0.0 2021/1/19<P>
 */
public class GetSupplierIdDataResponse {

	private String supplierGUI;
	
	private String supplierCode;
	
	private String supplierID;

	public String getSupplierGUI() {
		return supplierGUI;
	}

	public void setSupplierGUI(String supplierGUI) {
		this.supplierGUI = supplierGUI;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}
}
