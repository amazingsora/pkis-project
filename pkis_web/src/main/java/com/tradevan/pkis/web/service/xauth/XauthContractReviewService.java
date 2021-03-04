package com.tradevan.pkis.web.service.xauth;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tradevan.mapper.pkis.dao.FlowconfMapper;
import com.tradevan.mapper.pkis.dao.ReviewconfMapper;
import com.tradevan.mapper.pkis.dao.ReviewsetdataMapper;
import com.tradevan.mapper.pkis.dao.ReviewsetdataconfMapper;
import com.tradevan.mapper.pkis.dao.SystemparamMapper;
import com.tradevan.mapper.pkis.model.Flowconf;
import com.tradevan.mapper.pkis.model.Reviewconf;
import com.tradevan.mapper.pkis.model.Reviewsetdata;
import com.tradevan.mapper.pkis.model.Reviewsetdataconf;
import com.tradevan.mapper.xauth.dao.XauthSysCodeMapper;
import com.tradevan.mapper.xauth.model.XauthSysCode;
import com.tradevan.pkis.web.service.common.CommonService;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.core.enums.MSG_KEY;
import com.tradevan.xauthframework.dao.bean.GridResult;
import com.tradevan.xauthframework.web.service.DefaultService;

@Service("ContractReviewService")
@Transactional(rollbackFor = Exception.class)
public class XauthContractReviewService extends DefaultService {
	
	@Autowired
	XauthSysCodeMapper xauthSysCodeMapper;
	
	@Autowired
	SystemparamMapper systemparamMapper;
	
	@Autowired
	ReviewconfMapper reviewconfMapper;
	
	@Autowired
	FlowconfMapper flowconfMapper;
	
	@Autowired
	ReviewsetdataMapper reviewsetdataMapper;
	
	@Autowired
	ReviewsetdataconfMapper reviewsetdataconfMapper;
	
	@Autowired
	CommonService commonService;
	
	public GridResult selectContractReviewGridList(Map<String, Object> params) throws Exception {
		GridResult gridResult = this.grid(params);
		Map<String, Object> objParams = new HashMap<String, Object>();
		
		String flowversion = MapUtils.getString(params, "flowversion");
		String flowname = MapUtils.getString(params, "flowname");
		String contractmodel = MapUtils.getString(params, "contractmodel");
		
		objParams.put("flowversion", flowversion);
		objParams.put("flowname", flowname);
		objParams.put("contractmodel", contractmodel);
		
		commonDao.query(gridResult, "com.tradevan.mapper.pkis.dao.ReviewconfMapper.selectContractReviewList", objParams);
		return gridResult;
	}
	
	public List<Reviewconf> selectContractReviewList(Map<String, Object> params) throws Exception {
		String flowid = MapUtils.getString(params, "flowid");
		String flowversion = MapUtils.getString(params, "flowversion");
		String flowname = MapUtils.getString(params, "flowname");
		
		QueryWrapper<Reviewconf> queryWrapper = new QueryWrapper<Reviewconf>();
		queryWrapper.eq("FLOWID", flowid);
		queryWrapper.eq("FLOWVERSION", flowversion);
		queryWrapper.eq("FLOWNAME", flowname);
		List<Reviewconf> reviewconfList = reviewconfMapper.selectList(queryWrapper);
		
		return reviewconfList;
	}
	
	public List<Map<String, Object>>  selectReviewsetdataconfList(Map<String, Object> params) throws Exception {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		String flowid = MapUtils.getString(params, "flowid");
		String flowversion = MapUtils.getString(params, "flowversion");
		
		logger.info("flowid == " + flowid);
		logger.info("flowversion == " + flowversion);
		
		dataMap.put("flowid", flowid);
		dataMap.put("flowversion", flowversion);
		
		List<Map<String, Object>> reviewsetdataconfList = reviewsetdataconfMapper.selectReviewsetdataconf(dataMap);
		
		return reviewsetdataconfList;
	}
	
	/**
	 * 新增畫面 新增按鈕呼叫方法
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult insertContractReviewData(Map<String, Object> params) throws Exception {
		
		ProcessResult result = new ProcessResult();
		String conditionName = MapUtils.getString(params, "conditionName");
		String condition = MapUtils.getString(params, "condition");
		String flowName = MapUtils.getString(params, "flowname");
		String flowid = MapUtils.getString(params, "flowid");
				
		if(StringUtils.isNotBlank(condition) && StringUtils.isNotBlank(conditionName)) {
			flowid += condition;
			flowName += " - " + conditionName;
		}
		
		Reviewconf reviewconf = new Reviewconf();
		reviewconf.setFlowid(flowid);
		reviewconf.setFlowname(flowName);
		reviewconf.setCondition(condition);
		reviewconf.setContractmodel(MapUtils.getString(params, "contractmodel"));
		reviewconf.setIsprereview(MapUtils.getString(params, "isprereview"));
		reviewconf.setIsdeptreview(MapUtils.getString(params, "isdeptreview"));
		reviewconf.setIsorgreview(MapUtils.getString(params, "isorgreview"));
		reviewconf.setStatus(MapUtils.getString(params, "status"));
		reviewconf.setFlowversion(getFlowVersion(reviewconf, null));
		reviewconf.setCreateuser(getCreOrUpdUser(null));
		reviewconf.setCreatedate(new Timestamp(new Date().getTime()));
		
		Flowconf flowconf = new Flowconf();
		flowconf.setFlowid(flowid);
		flowconf.setFlowname(flowName);
		flowconf.setSysid(APP_ID);
		flowconf.setCreateuserid(getCreOrUpdUser(null));
		flowconf.setCreatetime(new Timestamp(new Date().getTime()));
		flowconf.setUpdateuserid(getCreOrUpdUser(null));
		flowconf.setUpdatetime(new Timestamp(new Date().getTime()));
		flowconf.setFlowversion(getFlowVersion(null, flowconf));
		
		ProcessResult setReviewsetdataconfResult = insOrUpdReviewsetdataconf(params, reviewconf.getFlowversion(), flowid);
		if(StringUtils.equals(ProcessResult.NG, setReviewsetdataconfResult.getStatus())) {
			result.setStatus(setReviewsetdataconfResult.getStatus());
			result.addMessage(MSG_KEY.INSERT_FAIL.getMessage());
			return result;
		}
		ProcessResult processResult = sendData(reviewconf, flowconf);
		if(StringUtils.equals(ProcessResult.OK, processResult.getStatus())) {
			result.setStatus(processResult.getStatus());
			result.addMessage(MSG_KEY.INSERT_OK.getMessage());
		}else {
			result.setStatus(processResult.getStatus());
			result.addMessage(MSG_KEY.INSERT_FAIL.getMessage());
		}
		
		return result;
	}
	
	/**
	 * 更新畫面 更新按鈕呼叫方法
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult updateContractReviewData(Map<String, Object> params) throws Exception {
		
		ProcessResult result = new ProcessResult();
		Reviewconf reviewconf = new Reviewconf();
		reviewconf.setFlowid(MapUtils.getString(params, "flowid"));
		reviewconf.setFlowname(MapUtils.getString(params, "flowname"));
		reviewconf.setContractmodel(MapUtils.getString(params, "contractmodel"));
		reviewconf.setCondition(MapUtils.getString(params, "condition"));
		reviewconf.setIsprereview(MapUtils.getString(params, "isprereview"));
		reviewconf.setIsdeptreview(MapUtils.getString(params, "isdeptreview"));
		reviewconf.setIsorgreview(MapUtils.getString(params, "isorgreview"));
		reviewconf.setFlowversion(MapUtils.getString(params, "flowversion"));
		reviewconf.setStatus(MapUtils.getString(params, "status"));
		reviewconf.setUpdateuser(getCreOrUpdUser(null));
		reviewconf.setUpdatedate(new Timestamp(new Date().getTime()));
		
		Flowconf flowconf = new Flowconf();
		flowconf.setFlowid(MapUtils.getString(params, "flowid"));
		flowconf.setFlowname(MapUtils.getString(params, "flowname"));
		flowconf.setUpdateuserid(getCreOrUpdUser(null));
		flowconf.setUpdatetime(new Timestamp(new Date().getTime()));
		flowconf.setFlowversion(MapUtils.getString(params, "flowversion"));
		
		ProcessResult setReviewsetdataconfResult = insOrUpdReviewsetdataconf(params, reviewconf.getFlowversion(), reviewconf.getFlowid());
		if(StringUtils.equals(ProcessResult.NG, setReviewsetdataconfResult.getStatus())) {
			result.setStatus(setReviewsetdataconfResult.getStatus());
			result.addMessage(MSG_KEY.UPDATE_FAIL.getMessage());
			return result;
		}
		ProcessResult processResult = sendData(reviewconf, flowconf);
		if(StringUtils.equals(ProcessResult.OK, processResult.getStatus())) {
			processResult.setStatus(processResult.getStatus());
			processResult.addMessage(MSG_KEY.UPDATE_OK.getMessage());
		}else {
			processResult.setStatus(processResult.getStatus());
			processResult.addMessage(MSG_KEY.UPDATE_FAIL.getMessage());
		}
		
		return processResult;
	}
	
	/**
	 * 合約流程啟用/停用
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public ProcessResult bgnOrStopSupplierData(Map<String, Object> params) throws Exception{
		Reviewconf reviewconf = new Reviewconf();
		reviewconf.setFlowid(MapUtils.getString(params, "flowid"));
		reviewconf.setFlowname(MapUtils.getString(params, "flowname"));
		reviewconf.setCondition(MapUtils.getString(params, "condition"));
		reviewconf.setIsprereview(MapUtils.getString(params, "isprereview"));
		reviewconf.setIsdeptreview(MapUtils.getString(params, "isdeptreview"));
		reviewconf.setIsorgreview(MapUtils.getString(params, "isorgreview"));
		reviewconf.setFlowversion(MapUtils.getString(params, "flowversion"));
		reviewconf.setUpdateuser(getCreOrUpdUser(null));
		reviewconf.setUpdatedate(new Timestamp(new Date().getTime()));
		
		if(StringUtils.isNotBlank(MapUtils.getString(params, "contractmodel"))) {
			QueryWrapper<XauthSysCode> queryWrapper = new QueryWrapper<XauthSysCode>();
			queryWrapper.eq("CNAME", MapUtils.getString(params, "contractmodel"));
			queryWrapper.eq("GP", "CONTRACT_MODE_CODE");
			List<XauthSysCode> xauthSysCodeList = xauthSysCodeMapper.selectList(queryWrapper);
			reviewconf.setContractmodel(xauthSysCodeList.get(0).getCode());
		}
		
		if(StringUtils.equals("N", MapUtils.getString(params, "status"))) {
			reviewconf.setStatus("Y");
		}else {
			reviewconf.setStatus("N");
		}
		
		ProcessResult processResult = sendData(reviewconf, null);
		if(StringUtils.equals(ProcessResult.OK, processResult.getStatus())) {
			processResult.addMessage(MSG_KEY.UPDATE_OK.getMessage());
		}else {
			processResult.addMessage(MSG_KEY.UPDATE_FAIL.getMessage());
		}
		
		return processResult;
	}
	
	/**
	 * 編輯頁面-下拉式選單
	 * @param contractmodel
	 * @return
	 * @throws Exception 
	 */
	public List<XauthSysCode> getFlowList(String contractmodel) throws Exception{
		List<XauthSysCode> result = null;
		String gp = "";
		
		if(StringUtils.equals("NSC", contractmodel)) {
			gp = "NSC_FLOW_TEMPLATE";
		}else if(StringUtils.equals("SC", contractmodel)) {
			gp = "SC_FLOW_TEMPLATE";
		}
		result = commonService.getSysCodeDatasByGp(gp);
		return result;
	}
	
	/**
	 * 取得當前最高版號
	 * @param reviewconf
	 * @param flowconf
	 * @return
	 */
	private String getFlowVersion(Reviewconf reviewconf, Flowconf flowconf) {
		int resultVersion = 1;
		
		if(reviewconf != null) {
			
			if(reviewconf.getFlowid() == null) {
				return null;
			}
			
			QueryWrapper<Reviewconf> queryWrapper = new QueryWrapper<Reviewconf>();
			
			queryWrapper.eq("FLOWID", reviewconf.getFlowid());
			List<Reviewconf> reviewconfList = reviewconfMapper.selectList(queryWrapper);
			
			int size = reviewconfList.size();
			if(size == 0) {
				return Integer.toString(resultVersion);
			}
			
			for(int i = 0 ; i < size ; i++) {
				int reviewconfVersion = Integer.valueOf(reviewconfList.get(i).getFlowversion());
				if(reviewconfVersion >= resultVersion) {
					resultVersion = reviewconfVersion + 1;
				}
			}
		}
		
		if(flowconf != null) {
			
			if(flowconf.getFlowid() == null) {
				return null;
			}
			
			QueryWrapper<Flowconf> queryWrapper = new QueryWrapper<Flowconf>();
			
			queryWrapper.eq("FLOWID", flowconf.getFlowid());
			List<Flowconf> flowconfList = flowconfMapper.selectList(queryWrapper);
			
			int size = flowconfList.size();
			if(size == 0) {
				return Integer.toString(resultVersion);
			}
			
			for(int i = 0 ; i < size ; i++) {
				int flowconfVersion = Integer.valueOf(flowconfList.get(i).getFlowversion());
				if(flowconfVersion >= resultVersion) {
					resultVersion = flowconfVersion + 1;
				}
			}
		}
		
		return Integer.toString(resultVersion);
	}
	
	/**
	 * 存檔/更新 Reviewconf && Flowconf
	 * @param reviewconf
	 * @param flowconf
	 * @return
	 */
	private ProcessResult sendData(Reviewconf reviewconf, Flowconf flowconf) {
		ProcessResult processResult = new ProcessResult();
		
		if(reviewconf != null) {
			QueryWrapper<Reviewconf> reviewConfWrapper = new QueryWrapper<Reviewconf>();
			reviewConfWrapper.eq("FLOWID", reviewconf.getFlowid());
			reviewConfWrapper.eq("FLOWVERSION", reviewconf.getFlowversion());
			List<Reviewconf> ReviewconfList = reviewconfMapper.selectList(reviewConfWrapper);
			
			if(ReviewconfList.size() == 0) {
				reviewconfMapper.insert(reviewconf);
			}else {
				reviewconfMapper.update(reviewconf, reviewConfWrapper);
			}
		}
		
		if(flowconf != null) {
			QueryWrapper<Flowconf> flowconfWrapper = new QueryWrapper<Flowconf>();
			flowconfWrapper.eq("FLOWID", flowconf.getFlowid());
			flowconfWrapper.eq("FLOWVERSION", flowconf.getFlowversion());
			List<Flowconf> flowconfList = flowconfMapper.selectList(flowconfWrapper);
			
			if(flowconfList.size() == 0) {
				flowconfMapper.insert(flowconf);
			}else {
				flowconfMapper.update(flowconf, flowconfWrapper);
			}
		}
		
		processResult.setStatus(ProcessResult.OK);
		
		return processResult;
	}
	
	/**
	 * 取得flow的checkbox
	 * @return
	 */
	public List<Reviewsetdata> getReviewsetdata() {
		
		QueryWrapper<Reviewsetdata> queryWrapper = new QueryWrapper<Reviewsetdata>();
		List<Reviewsetdata> reviewsetdataList = reviewsetdataMapper.selectList(queryWrapper);
		
		return reviewsetdataList;
	}
	
	/**
	 * 編輯Reviewsetdataconf
	 * @param params
	 * @param flowVersion
	 * @param flowId
	 * @return
	 * @throws Exception
	 */
	public ProcessResult insOrUpdReviewsetdataconf(Map<String, Object> params, String flowVersion, String flowId) throws Exception {
		
		logger.info("insOrUpdReviewsetdataconf#start");
		ProcessResult result = new ProcessResult();
		Reviewsetdataconf reviewsetdataconf = null;
		List<Reviewsetdata> reviewsetdataList = getReviewsetdata();
		String isUpdate = "N";
		String createUserId = "";
		Date createDate = null;
		
		QueryWrapper<Reviewsetdataconf> queryWrapper = new QueryWrapper<Reviewsetdataconf>();
		queryWrapper.eq("FLOWID", flowId);
		queryWrapper.eq("FLOWVERSION", flowVersion);
		List<Reviewsetdataconf> reviewsetdataconfList = reviewsetdataconfMapper.selectList(queryWrapper);
		if(reviewsetdataconfList.size() > 0) {
			isUpdate = "Y";
			createUserId = reviewsetdataconfList.get(0).getCreateuser();
			createDate = reviewsetdataconfList.get(0).getCreatedate();
			int deleteCount = reviewsetdataconfMapper.delete(queryWrapper);
			logger.info("deleteCount == " + deleteCount);
		}
		
		int i = 0;
		for(Reviewsetdata reviewsetdata : reviewsetdataList) {
			if(params.containsKey("cbReviewName_" + reviewsetdata.getSerno())) {
				i = i + 1;
				reviewsetdataconf = new Reviewsetdataconf();
				reviewsetdataconf.setFlowid(flowId);
				reviewsetdataconf.setSequence(Long.valueOf(i));
				reviewsetdataconf.setReviewname(reviewsetdata.getReviewname());
				reviewsetdataconf.setActiontype(reviewsetdata.getActiontype());
				reviewsetdataconf.setUpdateuser(getCreOrUpdUser(null));
				reviewsetdataconf.setUpdatedate(new Timestamp(new Date().getTime()));
				reviewsetdataconf.setFlowversion(flowVersion);
				if(StringUtils.equals(isUpdate, "Y")) {
					reviewsetdataconf.setCreateuser(createUserId);
					reviewsetdataconf.setCreatedate(createDate);
				} else {
					reviewsetdataconf.setCreateuser(getCreOrUpdUser(null));
					reviewsetdataconf.setCreatedate(new Timestamp(new Date().getTime()));
				}
				int insertCount = reviewsetdataconfMapper.insert(reviewsetdataconf);
				if(insertCount <= 0) {
					result.setStatus(ProcessResult.NG);
					return result;
				}
			}
		}
		result.setStatus(ProcessResult.OK);
		
		return result;
	}
	
}
