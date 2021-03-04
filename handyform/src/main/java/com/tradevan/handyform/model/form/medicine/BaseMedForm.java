package com.tradevan.handyform.model.form.medicine;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class BaseMedForm {
	
	protected String flow; // 審核流程
	protected String comments; // 各關卡審核意見
	
	@JsonIgnore
	public BigDecimal sumCustFormScore() { // Cust表單-[初核]加總分數欄位(SF)給合計欄位(TF)
		return null;
	}
	
	@JsonIgnore
	public BigDecimal sumCustFormScore2() { // Cust表單-[覆核]加總分數欄位(SF)給合計欄位(TF)
		return null;
	}
	
	@JsonIgnore
	public BigDecimal calcScore() { // 受評人分數
		return null;
	}
	
	@JsonIgnore
	public BigDecimal calcScore2() { // 評量人分數
		return null;
	}
	
	@JsonIgnore
	public Integer calcSatisfaction() { // 受評人滿意度
		return null;
	}
	
	@JsonIgnore
	public Integer calcSatisfaction2() { // 評量人滿意度
		return null;
	}
	
	@JsonIgnore
	public String getPassStatus() { // Cust表單結果-通過狀態
		return null;
	}
	
	@JsonIgnore
	public String getEvalSubject() { // 評量主題(MiniCex)
		return null;
	}
	
	@JsonIgnore
	public List<EduMedFormReportBean> getReportBeanList() {
		return Collections.emptyList();
	}
	
	@JsonIgnore
	public int getTotalCount() { // 總項目
		return 1;
	}
	
	@JsonIgnore
	public int getPassCount() { // 通過項目
		return 1;
	}
	
	@JsonIgnore
	public int getFinalPassCount() { // 學習項目/評量計算的通過項目
		return 1;
	}

	public String getFlow() {
		return flow;
	}

	@JsonIgnore
	public void renewFlow(String flow) {
		this.flow = flow;
	}

	public String getComments() {
		return comments;
	}

	@JsonIgnore
	public void addComment(String comment) {
		if (this.comments != null) {
			if (this.comments.length() > 0) {
				this.comments += "^";
			}
			this.comments += comment;
		}
		else {
			this.comments = comment;
		}
	}
	
	@JsonIgnore
	public void addExcelColsTitle(Row row) throws Exception {
		// Default do nothing
	}
	
	@JsonIgnore
	public void addExcelCells(Row row, int colIndex, CellStyle intCellStyle, CellStyle floatCellStyle) throws Exception {
		// Default do nothing
	}
	
	@JsonIgnore
	public boolean hasForReview() {
		return false;
	}
}
