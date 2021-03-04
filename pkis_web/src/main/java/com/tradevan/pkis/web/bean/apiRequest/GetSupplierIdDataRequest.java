package com.tradevan.pkis.web.bean.apiRequest;

/**
 * 作 業 代 碼 ：GetSupplierIdDataRequest<br>
 * 作 業 名 稱 ：取得供應商資料API Request<br>
 * 程 式 代 號 ：GetSupplierIdDataRequest<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Yuan<br>
 * @version  : 1.0.0 2021/1/19<P>
 */
public class GetSupplierIdDataRequest {

	private String supplierGUI;
	
	private String supplierCode;
	
	private String lgcAccount;

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

	public String getLgcAccount() {
		return lgcAccount;
	}

	public void setLgcAccount(String lgcAccount) {
		this.lgcAccount = lgcAccount;
	}
}
