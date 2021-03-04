package com.tradevan.handyflow.service;

/**
 * Title: ApplyNoService<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.2
 */
public interface ApplyNoService {

	String genApplyNo(String prefix, String dateFmt, int serialLen);
	
	String genApplyNo(String formId, String prefix, String dateFmt, int serialLen);
	
	String genToDoNo(String formId, String prefix, String dateFmt, int serialLen);
}
