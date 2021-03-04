package com.tradevan.aporg.repository;

import java.util.List;
import java.util.Map;

import com.tradevan.apcommon.bean.PageResult;
import com.tradevan.apcommon.persistence.GenericRepository;
import com.tradevan.aporg.model.ProjProf;

/**
 * Title: ProjProfRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.1
 */
public interface ProjProfRepository extends GenericRepository<ProjProf, Long> {

	String getMaxProjIdSerial(String prefixVal, String dateFmtVal, int serialLen);
	
	String getMaxRschProjIdSerial(String year, String organCode, String projType, String hostType, int serialLen);
	
	String getMaxEduCourseIdSerial(String prefixVal, String dateFmtVal, int serialLen);
	
	String getMaxEduClassIdSerial(String courseId);
	
	String getMaxCoprApplyNoSerial(String sequenceName);
	
	String getMaxBulletinNo(String dateFmtVal, int serialLen);
	
	PageResult findProjs(Map<String, Object> params, Integer page, Integer pageSize);
	
	List<Map<String, Object>> listProjAuth(String projId);
	
	List<Map<String, Object>> listProjRole(String projId);
	
}
