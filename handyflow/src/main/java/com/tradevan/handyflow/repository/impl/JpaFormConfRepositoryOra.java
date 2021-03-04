package com.tradevan.handyflow.repository.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tradevan.apcommon.persistence.JpaGenericRepository;
import com.tradevan.handyflow.model.form.FormConf;
import com.tradevan.handyflow.repository.FormConfRepository;

/**
 * Title: JpaFormConfRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@Repository("jpaFormConfRepositoryOra")
public class JpaFormConfRepositoryOra extends JpaGenericRepository<FormConf, Long> implements FormConfRepository {

	@Override
	public List<Map<String, Object>> findFormConfs(Map<String, Object> params) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ROW_NUMBER() OVER (order by serno) AS rowsNum ");
		sql.append(" , serNo ");
		sql.append(" , formId ");
		sql.append(" , formName ");
		sql.append(" , formUrl ");
		sql.append(" , (SELECT sysName FROM SysProf WHERE SysProf.sysId = FormConf.sysId) as system ");
		sql.append(" , createUserId ");
		sql.append(" , TO_CHAR (createTime, 'yyyy/mm/dd HH24:MI') AS createTime  ");
		sql.append(" , updateUserId ");
		sql.append(" , TO_CHAR (updateTime, 'yyyy/mm/dd HH24:MI') AS updateTime ");
		sql.append(" FROM FormConf "); 
		if (params.containsKey("sysId")) {
			sql.append("WHERE sysId in ('ALL', :sysId) ");
		}
		else {
			sql.append("WHERE sysId = 'ALL' ");
		}
		return findListMapBySQL_map(sql.toString(), params);
	}

}
