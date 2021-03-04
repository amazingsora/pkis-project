package com.tradevan.handyflow.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tradevan.handyflow.repository.DocStateRepository;
import com.tradevan.handyflow.repository.DocToDoRepository;
import com.tradevan.handyflow.service.ApplyNoService;

/**
 * Title: ApplyNoServiceImpl<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.2
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ApplyNoServiceImplOra implements ApplyNoService {

	@Autowired
	//@Qualifier("jpaDocStateRepositoryOra")
	private DocStateRepository docStateRepository;
	
	@Autowired
	//@Qualifier("jpaDocToDoRepositoryOra")
	private DocToDoRepository docToDoRepository;
	
	@Override
	public String genApplyNo(String prefix, String dateFmt, int serialLen) {
		String prefixVal = getPrefixVal(prefix);
		String dateFmtVal = getDateFmtVal(dateFmt);
		
		String maxSerial = docStateRepository.getMaxApplyNoSerial(prefixVal, dateFmtVal, serialLen);
		
		return formatApplyNo(null, prefixVal, dateFmtVal, serialLen, maxSerial);
	}
	
	@Override
	public String genApplyNo(String formId, String prefix, String dateFmt, int serialLen) {
		String prefixVal = getPrefixVal(prefix);
		String dateFmtVal = getDateFmtVal(dateFmt);
		
		String maxSerial = docStateRepository.getMaxApplyNoSerial(formId, prefixVal, dateFmtVal, serialLen);
		
		return formatApplyNo(formId, prefixVal, dateFmtVal, serialLen, maxSerial);
	}
	
	private String getPrefixVal(String prefix) {
		return (prefix != null) ? prefix : "";
	}
	
	private String getDateFmtVal(String dateFmt) {
		return (dateFmt != null) ? (new SimpleDateFormat(dateFmt).format(new Date())) : "";
	}
	
	private String formatApplyNo(String formId, String prefixVal, String dateFmtVal, int serialLen, String maxSerial) {
		String serial = String.valueOf((maxSerial != null) ? Integer.parseInt(maxSerial) + 1 : 1);
		
		if (serial.length() > serialLen) {
			new IllegalStateException("Serial overflows. (formId:" + formId + " applyNo:" + prefixVal + dateFmtVal + serial + ")");
		}
		
		StringBuilder buf = new StringBuilder();
		int len = serialLen - serial.length();
		for (int x = 0; x < len; x++) {
			buf.append("0");
		}
		buf.append(serial);
		
		return prefixVal + dateFmtVal + buf.toString();
	}
	
	@Override
	public String genToDoNo(String formId, String prefix, String dateFmt, int serialLen) {
		String prefixVal = getPrefixVal(prefix);
		String dateFmtVal = getDateFmtVal(dateFmt);
		
		String maxSerial = docToDoRepository.getMaxToDoNoSerial(formId, prefixVal, dateFmtVal, serialLen);
		
		return formatApplyNo(formId, prefixVal, dateFmtVal, serialLen, maxSerial);
	}
	
}
