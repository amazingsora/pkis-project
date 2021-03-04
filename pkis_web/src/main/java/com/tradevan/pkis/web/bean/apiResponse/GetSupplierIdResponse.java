package com.tradevan.pkis.web.bean.apiResponse;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 作 業 代 碼 ：GetSupplierIdResponse<br>
 * 作 業 名 稱 ：取得供應商資料API Response<br>
 * 程 式 代 號 ：GetSupplierIdResponse<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Yuan<br>
 * @version  : 1.0.0 2021/1/19<P>
 */
public class GetSupplierIdResponse {

	private String returnCode;
	
	private String returnMsg;
	
	@JsonInclude(Include.NON_NULL)
	private List<GetSupplierIdDataResponse> returnData;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public List<GetSupplierIdDataResponse> getReturnData() {
		return returnData;
	}

	public void setReturnData(List<GetSupplierIdDataResponse> returnData) {
		this.returnData = returnData;
	}
}
