package com.tradevan.pkis.backend.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tradevan.mapper.pkis.dao.ShortagepenaltyMapper;
import com.tradevan.mapper.pkis.model.Shortagepenalty;
import com.tradevan.pkis.backend.config.DefaultService;
import com.tradevan.pkis.backend.utils.ExcelUtil;

@Service("ImportShortagepenaltyService")
@Transactional(rollbackFor = Exception.class)
public class ImportShortagepenaltyService extends DefaultService {
	
	@Autowired
	ShortagepenaltyMapper shortagepenaltyMapper;

	public void save(String filePath, List<String> errorMessageList) throws Exception {
		
		String deptno = "";
		String suppliercode = "";
		Shortagepenalty shortagepenalty = null;
		List<Map<Integer, String>> dataList = ExcelUtil.read(filePath, 0, 2, 0, 12);
		
		deleteDeptnoAndSuppliercode(dataList);
		
		int successCount = 0;
		int failCount = 0;
		int totalCount = 0;
		logger.info("【開始新增】");
		for(Map<Integer, String> data : dataList) {
			try {
				deptno = MapUtils.getString(data, 0);
				suppliercode = MapUtils.getString(data, 1);
				
				shortagepenalty = new Shortagepenalty();
				shortagepenalty.setDeptno(deptno);
				shortagepenalty.setSuppliercode(suppliercode);
				shortagepenalty.setLastyear(getRoundingModeUpVal(MapUtils.getString(data, 4)));
				shortagepenalty.setLscale(getRoundingModeUpVal(MapUtils.getString(data, 7)));
				shortagepenalty.setUscale(getRoundingModeUpVal(MapUtils.getString(data, 8)));
				shortagepenalty.setLevelpenalty(getStrVal(MapUtils.getString(data, 10), deptno));
				shortagepenalty.setPenalty(getRoundingModeUpVal(MapUtils.getString(data, 11)));
				shortagepenalty.setCmp(getStrVal(MapUtils.getString(data, 12), deptno));
				
				shortagepenaltyMapper.insert(shortagepenalty);
				successCount ++;
			} catch(Exception e) {
				failCount ++;
				logger.error("Deptno : " + shortagepenalty.getDeptno() + "Suppliercode : " + shortagepenalty.getSuppliercode() + " 新增失敗 : " + e);
				errorMessageList.add("Deptno : " + shortagepenalty.getDeptno() + ", Suppliercode : " + shortagepenalty.getSuppliercode() + " 新增失敗 : " + e);
//				throw new Exception(e);
			} finally {
				totalCount ++;
			}
		}
		logger.info("【新增完成，總共筆數 : 】" + totalCount);
		logger.info("【新增完成，成功筆數 : 】" + successCount);
		logger.info("【新增完成，失敗筆數 : 】" + failCount);
		
	}
	
	private void deleteDeptnoAndSuppliercode(List<Map<Integer, String>> dataList) throws Exception {
		String key = "";
		String deptno = "";
		String suppliercode = "";
		List<String> groupKeyList = new ArrayList<String>();
		QueryWrapper<Shortagepenalty> shortagepenaltyWrapper = null;
		
		for(Map<Integer, String> data : dataList) {
			deptno = MapUtils.getString(data, 0, "");
			suppliercode = MapUtils.getString(data, 1, "");
			
			if(StringUtils.isBlank(deptno)) {
				throw new Exception("deptno is null");
			}
			
			if(StringUtils.isBlank(suppliercode)) {
				throw new Exception("suppliercode is null");
			}
			
			deptno = StringUtils.trimToEmpty(deptno);
			suppliercode = StringUtils.trimToEmpty(suppliercode);
			key = deptno + "," + suppliercode;
			
			if(!groupKeyList.contains(key)) {
				groupKeyList.add(key);
			}
		}
		logger.info("groupKeyList size : " + groupKeyList.size());
		
		for(String groupkey : groupKeyList) {
			deptno = groupkey.split(",")[0];
			suppliercode = groupkey.split(",")[1];
			// 刪除
			shortagepenaltyWrapper = new QueryWrapper<Shortagepenalty>();
			shortagepenaltyWrapper.eq("DEPTNO", deptno);
			shortagepenaltyWrapper.eq("SUPPLIERCODE", suppliercode);
			shortagepenaltyMapper.delete(shortagepenaltyWrapper);
		}
		
	}
	
	private String getStrVal(String value, String deptno) {
		String result = null;
		Double valueDou = null;
		if(isNumeric(value)) {
			valueDou = getRoundingModeUpVal(value);
			if(valueDou != null) {
				result = String.valueOf(valueDou);
			}
		} else {
			result = value;
		}
		
		return result;
	}
	
	private Double getRoundingModeUpVal(String value) {
		
		if(StringUtils.isBlank(value)) {
			return null;
		}
		double numericCellValue = (Double.valueOf(value) * 100);
		BigDecimal bg = new BigDecimal(numericCellValue).setScale(2, BigDecimal.ROUND_HALF_UP);
		numericCellValue = bg.doubleValue();
		
		return numericCellValue;
	}
	
	/**
	 * 判斷是否為數字
	 * @param strNum
	 * @return
	 */
	private boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false; 
	    }
	    strNum = StringUtils.replace(strNum, ".", "");
	    Pattern pattern = Pattern.compile("[0-9]+");
	    return pattern.matcher(strNum).matches();
	}
	
	public static void main(String[] args) {
		ImportShortagepenaltyService service = new ImportShortagepenaltyService();
		String num = "0.050";
		System.out.println(service.isNumeric(num));
	}
}
