package com.tradevan.pkis.web.bean.apiRequest;

import java.util.List;

/**
 * 作 業 代 碼 ：GetSupplierIdRequest<br>
 * 作 業 名 稱 ：取得供應商資料API Request<br>
 * 程 式 代 號 ：GetSupplierIdRequest<br>
 * 描             述 ：<br>
 * 公             司 ：Tradevan Co.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Yuan<br>
 * @version  : 1.0.0 2021/1/19<P>
 */
public class GetSupplierIdRequest {

	private List<GetSupplierIdDataRequest> data;

	public List<GetSupplierIdDataRequest> getData() {
		return data;
	}

	public void setData(List<GetSupplierIdDataRequest> data) {
		this.data = data;
	}
}
