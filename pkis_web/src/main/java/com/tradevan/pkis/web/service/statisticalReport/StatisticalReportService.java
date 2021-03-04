package com.tradevan.pkis.web.service.statisticalReport;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.tradevan.mapper.contract.dao.ContractMapper;
import com.tradevan.mapper.pkis.dao.CodelistMapper;
import com.tradevan.mapper.pkis.dao.ContractmasterMapper;
import com.tradevan.mapper.pkis.dao.ContractreviewMapper;
import com.tradevan.mapper.pkis.dao.DocstateMapper;
import com.tradevan.mapper.pkis.dao.FlowstepMapper;
import com.tradevan.mapper.pkis.dao.ReviewconfMapper;
import com.tradevan.mapper.pkis.dao.StatisticalReportMapper;
import com.tradevan.mapper.pkis.model.Codelist;
import com.tradevan.mapper.pkis.model.Contractmaster;
import com.tradevan.mapper.pkis.model.Docstate;
import com.tradevan.mapper.pkis.model.StatisticalReport;
import com.tradevan.mapper.xauth.dao.XauthSysCodeMapper;
import com.tradevan.mapper.xauth.dao.XauthUsersMapper;
import com.tradevan.mapper.xauth.model.XauthSysCode;
import com.tradevan.mapper.xauth.model.XauthUsers;
import com.tradevan.pkis.web.service.common.CommonService;
import com.tradevan.pkis.web.service.contract.ContractService;
import com.tradevan.pkis.web.util.ElasticSearchUtil;
import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.common.utils.JsonUtils;
import com.tradevan.xauthframework.core.common.LocaleMessage;
import com.tradevan.xauthframework.core.enums.USER_TYPE;
import com.tradevan.xauthframework.core.security.UserInfo;
import com.tradevan.xauthframework.dao.bean.GridResult;
import com.tradevan.xauthframework.web.service.DefaultService;

@Service("StatisticalReportService")
@Transactional(rollbackFor = Exception.class)
public class StatisticalReportService extends DefaultService {

	@Autowired
	ReviewconfMapper reviewconfMapper;

	@Autowired
	ContractMapper contractMapper;

	@Autowired
	StatisticalReportMapper statisticalReportMapper;

	@Autowired
	DocstateMapper docstateMapper;

	@Autowired
	ContractService contractService;

	@Autowired
	ContractmasterMapper contractmasterMapper;

	@Autowired
	XauthSysCodeMapper xauthSysCodeMapper;

	@Autowired
	CodelistMapper codelistMapper;

	@Autowired
	XauthUsersMapper xauthUsersMapper;

	@Autowired
	FlowstepMapper flowstepMapper;

	@Autowired
	CommonService commonService;

	@Autowired
	ContractreviewMapper contractreviewMapper;

	@Autowired
	ElasticSearchUtil elasticSearchUtil;

	/**
	 * 查詢電子合約紀錄
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult selectCDtList(Map<String, Object> params) throws Exception {
		String beginColumnName = "data.docdetail.resultdata";
		String indexName = "";
		String module = MapUtils.getString(params, "module");
		String todo = "";
		GridResult gridResult = null;
		String contractBgnDate = MapUtils.getString(params, "contractBgnDate");
		String contractEndDate = MapUtils.getString(params, "contractEndDate");
		String year = MapUtils.getString(params, "year");
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> resultMap = null;
		StringBuffer esSql = null;
		Map<String, Object> compParams = new HashMap<String, Object>();
		compParams.put("contractmodel", module);
		compParams.put("contractBgnDate", contractBgnDate);
		compParams.put("contractEndDate", contractEndDate);
		if (StringUtils.isNotBlank(year)) {
			compParams.put("indexname", year + "_" + module.toLowerCase() + "_qryrec_doc");
		}

		List<Map<String, Object>> contractList = contractMapper.selectContractReport(compParams);
		if (contractList.size() > 0) {
			// 根據查詢到的DATAID進行ELSEARCH
			for (Map<String, Object> data : contractList) {

				data.put("SENDDATE", data.get("SENDDATE") != null ? data.get("SENDDATE") : "");
				data.put("TASKDESC", data.get("TASKDESC") != null ? data.get("TASKDESC") : "");
				data.put("FLOWSTATUS", data.get("FLOWSTATUS") != null ? data.get("FLOWSTATUS") : "");
				data.put("TASKNAME", data.get("TASKNAME") != null ? data.get("TASKNAME") : "");
				data.put("APPLICANTID", data.get("APPLICANTID") != null ? data.get("APPLICANTID") : "");

				// 查詢合約名稱
				String dataid = MapUtils.getString(data, "DATAID");
				indexName = MapUtils.getString(data, "INDEXNAME");// year + "_" + module.toLowerCase() + "_qryrec_doc";
				esSql = new StringBuffer();
				esSql.append(" where " + "_id = '" + dataid + "'");
				List<String> resultJson = ElasticSearchUtil.searchForES(indexName, "todo," + beginColumnName,
						esSql.toString());

				if (resultJson != null && resultJson.size() > 0) {
					for (String json : resultJson) {
						ReadContext readContext = JsonPath.parse(json);
						todo = readContext.read("$.todo");
						List<Map<String, Object>> resultdataList = readContext.read("$..resultdata");
						if (resultdataList.size() > 0) {
							resultMap = resultdataList.get(0);

							String contractor = MapUtils.getString(resultMap,
									LocaleMessage.getMsg("contract.field.undertakeuserid"));
							if (StringUtils.isBlank(contractor)) {
								contractor = MapUtils.getString(resultMap,
										LocaleMessage.getMsg("contract.field.contractor"));
							}
							resultMap.put(LocaleMessage.getMsg("contract.field.contractor"), contractor);
							resultMap.put(LocaleMessage.getMsg("contract.field.name"),
									MapUtils.getString(resultMap, "合約名稱"));
						}
					}
				} else {
					logger.info("ElasticSearch異常");
					continue;
				}
				// 放入grid參數
				resultMap.put(LocaleMessage.getMsg("contract.field.module"),
						(module.equals("NSC") ? "非制式" : "制式") + "合約");
				resultMap.put(LocaleMessage.getMsg("contract.field.no"), dataid);
				// 設定狀態
				String flowStatus = "";
				if (!MapUtils.getString(data, "FLOWSTATUS").equals("")) {
					resultMap.put(LocaleMessage.getMsg("contract.field.submit"), MapUtils.getString(data, "SENDDATE"));
					resultMap.put(LocaleMessage.getMsg("contract.field.docstatus"),
							MapUtils.getString(data, "TASKDESC"));

					flowStatus = MapUtils.getString(data, "FLOWSTATUS");
					if (StringUtils.equals(flowStatus, "PROCESS")) {
						if (StringUtils.equals(todo, "駁回")) {
							if (MapUtils.getString(data, "TASKNAME").equals("申請人")) {
								flowStatus = "編輯中";
							} else {
								flowStatus = "退回重審";
							}
						} else {
							flowStatus = "審核中";
						}
					} else if (StringUtils.equals(flowStatus, "END")) {
						flowStatus = "歸檔";
					} else if (StringUtils.equals(flowStatus, "CANCEL")) {
						flowStatus = "作廢";
					} else {
						if (StringUtils.equals(todo, "作廢")) {
							flowStatus = "作廢";
						}
					}
				} else {
					if (todo.equals("新建") || todo.equals("暫存")) {
						flowStatus = "編輯中";
					} else {
						flowStatus = "作廢";
					}
				}
				resultMap.put("Flowstatus", flowStatus);
				if (flowStatus.equals("")) {
					continue;
				}
				resultList.add(resultMap);
			}
		}
		// 排序及分頁
		gridResult = this.grid(params);
		ElasticSearchUtil.sortPagination(gridResult, resultList, params);

		return gridResult;
	}

	/**
	 * 審核進度統計
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult selectRSList(Map<String, Object> params) throws Exception {
		GridResult gridResult = null;
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		String module = MapUtils.getString(params, "module");
		String beginColumnName = "data.docdetail.resultdata";
		String indexName = "";
		String contractBgnDate = MapUtils.getString(params, "contractBgnDate");
		String contractEndDate = MapUtils.getString(params, "contractEndDate");
		String year = MapUtils.getString(params, "year");

		if (StringUtils.isNotBlank(module)) {
			// 查詢符合條件的Contractmaster
			Map<String, Object> compParams = new HashMap<String, Object>();
			compParams.put("contractmodel", module);
			compParams.put("contractBgnDate", contractBgnDate);
			compParams.put("contractEndDate", contractEndDate);
			if (StringUtils.isNotBlank(year)) {
				compParams.put("indexname", year + "_" + module.toLowerCase() + "_qryrec_doc");
			}

			List<Map<String, Object>> contractmasterList = contractMapper.selectContractReport(compParams);
			Map<String, List<Map<String, Object>>> applynolistmap = new HashMap<String, List<Map<String, Object>>>();
			for (Map<String, Object> contractmasterLists : contractmasterList) {
				indexName = MapUtils.getString(contractmasterLists, "INDEXNAME");
				String applicantName = MapUtils.getString(contractmasterLists, "APPLICANTID");
				// 若為空值 代表合約為草稿階段
				if (StringUtils.isBlank(applicantName)) {
					applicantName = MapUtils.getString(contractmasterLists, "CREATEUSER").split(":")[1];
				}
				// 製作承辦人所持有的合約
				if (applynolistmap.get(applicantName) != null) {
					applynolistmap.get(applicantName).add(contractmasterLists);
				} else {
					List<Map<String, Object>> applynolist = new LinkedList<Map<String, Object>>();
					applynolist.add(contractmasterLists);
					applynolistmap.put(applicantName, applynolist);
				}
			}

			for (String key : applynolistmap.keySet()) {
				String contractor = "";
				Map<String, Object> todomap = new LinkedHashMap<String, Object>();
				todomap = initCheckPoint(todomap);
				// 遍立各List
				List<Map<String, Object>> applynolist = applynolistmap.get(key);
				contractor = getUserCName(key);
				// 根據合約編號查詢流程狀態
				for (Map<String, Object> applynomap : applynolist) {
					StringBuffer esSql = null;
					String todo = "";
					String dataid = MapUtils.getString(applynomap, "DATAID");
					String flowStatus = MapUtils.getString(applynomap, "FLOWSTATUS");
					String nowTaskId = MapUtils.getString(applynomap, "NOWTASKID");
					String taskName = MapUtils.getString(applynomap, "TASKNAME");
					String taskDesc = MapUtils.getString(applynomap, "TASKDESC");
					indexName = MapUtils.getString(applynomap, "INDEXNAME");
					String isFlowStatus = "Y";

					// flowStatus狀態為空值代表為草稿階段
					if (flowStatus == null) {
						isFlowStatus = "N";
					}
					// 未建立FLOW流程(未送出) - 新建 暫存 作廢
					if (isFlowStatus.equals("N")) {
						esSql = new StringBuffer();
						esSql.append(" where " + "_id = '" + dataid + "'");
						List<String> resultJson = ElasticSearchUtil.searchForES(indexName, "todo," + beginColumnName,
								esSql.toString());
						if (resultJson != null && resultJson.size() > 0) {
							for (String json : resultJson) {
								ReadContext readContext = JsonPath.parse(json);
								todo = readContext.read("$.todo");
							}
						}

						// 新建 與 暫存資料轉至 製作編輯中
						if (StringUtils.equals(todo, "新建") || StringUtils.equals(todo, "暫存")) {
							todo = "製作編輯中";
							todomap = mapcount(todomap, "件數");
						}
						todomap = mapcount(todomap, todo);
					} else {
						// 已結案與作廢
						if (StringUtils.equals(flowStatus, "END")) {
							todomap = mapcount(todomap, "歸檔");
							todomap = mapcount(todomap, "件數");
						} else if (StringUtils.equals(flowStatus, "CANCEL")) {
							todomap = mapcount(todomap, "作廢");
						} else {
							// 判斷適合為駁回
							esSql = new StringBuffer();
							esSql.append(" where " + "_id = '" + dataid + "'");
							List<String> resultJson = ElasticSearchUtil.searchForES(indexName, "todo," + beginColumnName, esSql.toString());
							if (resultJson != null && resultJson.size() > 0) {
								for (String json : resultJson) {
									ReadContext readContext = JsonPath.parse(json);
									todo = readContext.read("$.todo");
								}
							}

							if (StringUtils.equals(todo, "駁回")) {
								todomap = mapcount(todomap, "退件");
								if (StringUtils.equals(MapUtils.getString(applynomap, "TASKNAME"), "申請人")) {
									todomap = mapcount(todomap, "製作編輯中");
								} else {
									// 制式合約統計
									if (StringUtils.equals(module, "SC")) {
										switch (nowTaskId) {
										case "Task2":
											todomap = mapcount(todomap, nowTaskId);
											break;
										default:
											if (!StringUtils.equals(taskName, "法務")) {
												todomap = mapcount(todomap, "總審核數");
											}
											todomap = mapcount(todomap, nowTaskId);
											break;
										}
									}
									// 非制式統計
									else {
										if (StringUtils.isNotBlank(taskDesc) && taskDesc.indexOf("(組)") > -1) {
											todomap = mapcount(todomap, "組織最高主管");
											todomap = mapcount(todomap, "總審核數");
										} else {
											if (StringUtils.equals(nowTaskId, "Task2")) {
												todomap = mapcount(todomap, "法務審核中");
											} else if (StringUtils.equals(nowTaskId, "Task3")) {
												todomap = mapcount(todomap, "廠商簽核中");
											} else {
												List<String> filter = Arrays.asList("法務", "IT經理", "組織系統供應鏈總監", "財務總監",
														"總經理", "法律顧問", "法務長", "非商品採購經理", "人力資源總監", "便利購暨資產總監", "資產部經理"

												);
												if (!filter.contains(taskName)) {
													todomap = mapcount(todomap, "店經理/單位主管");
													todomap = mapcount(todomap, "總審核數");
												} else {
													if (StringUtils.equals(taskName, "法務")) {
														todomap = mapcount(todomap, "待法務簽章");
													} else {
														todomap = mapcount(todomap, taskName);
														todomap = mapcount(todomap, "總審核數");
													}
												}
											}
										}
									}
								}
								todomap = mapcount(todomap, "件數");
							} else {
								// 制式合約統計
								if (StringUtils.equals(module, "SC")) {
									switch (nowTaskId) {
									case "Task2":
										todomap = mapcount(todomap, nowTaskId);
										break;
									default:
										if (!StringUtils.equals(taskName, "法務")) {
											todomap = mapcount(todomap, "總審核數");
										}
										todomap = mapcount(todomap, nowTaskId);
										break;
									}
								}
								// 非制式統計
								else {
									if (StringUtils.isNotBlank(taskDesc) && taskDesc.indexOf("(組)") > -1) {
										todomap = mapcount(todomap, "組織最高主管");
										todomap = mapcount(todomap, "總審核數");
									} else {
										if (StringUtils.equals(nowTaskId, "Task2")) {
											todomap = mapcount(todomap, "法務審核中");
										} else if (StringUtils.equals(nowTaskId, "Task3")) {
											todomap = mapcount(todomap, "廠商簽核中");
										} else {
											List<String> filter = Arrays.asList("法務", "IT經理", "組織系統供應鏈總監", "財務總監",
													"總經理", "法律顧問", "法務長", "非商品採購經理", "人力資源總監", "便利購暨資產總監", "資產部經理");
											if (!filter.contains(taskName)) {
												todomap = mapcount(todomap, "店經理/單位主管");
												todomap = mapcount(todomap, "總審核數");
											} else {
												if (StringUtils.equals(taskName, "法務")) {
													todomap = mapcount(todomap, "法務簽章");
												} else {
													todomap = mapcount(todomap, taskName);
													todomap = mapcount(todomap, "總審核數");
												}
											}
										}
									}
								}
								todomap = mapcount(todomap, "件數");
							}
						}
					}
				}
				if (MapUtils.getInteger(todomap, "件數") == 0) {
					continue;
				}
				Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
				// 修改數字為百分比
				Map<String, Object> munList = new HashMap<String, Object>();

				resultMap.put("TotalReview", MapUtils.getInteger(todomap, "總審核數"));
				// 製作編輯中
				resultMap.put("todoEdit", MapUtils.getInteger(todomap, "製作編輯中"));
				// 根據合約模式分別統計
				if (StringUtils.equals(module, "SC")) {
					resultMap.put("Supplier", todomap.get("Task2"));
					resultMap.put("Supplier", todomap.get("Task2"));
					resultMap.put("Director", todomap.get("Task3"));
					resultMap.put("logisticalDirector", todomap.get("Task4"));
					resultMap.put("StoreManager", todomap.get("Task5"));
					resultMap.put("Officer", todomap.get("Task6"));
					resultMap.put(LocaleMessage.getMsg("statisticalreport.field.LegalSignature"), todomap.get("Task7"));
				} else {
					resultMap.put(LocaleMessage.getMsg("statisticalreport.field.Legalreview"), todomap.get("法務審核中"));
					resultMap.put("Supplier", todomap.get("廠商簽核中"));
					resultMap.put(LocaleMessage.getMsg("statisticalreport.field.StoreManager"),
							todomap.get(LocaleMessage.getMsg("statisticalreport.field.StoreManager")));
					resultMap.put("OSD", todomap.get("組織最高主管"));
					resultMap.put(LocaleMessage.getMsg("statisticalreport.field.Legalcounsel"), todomap.get("法務長"));
					resultMap.put(LocaleMessage.getMsg("statisticalreport.field.ChiefLegalOfficer"),
							todomap.get("法律顧問"));
					resultMap.put(LocaleMessage.getMsg("statisticalreport.field.Purchasing"), todomap.get("非商品採購經理"));
					resultMap.put(LocaleMessage.getMsg("statisticalreport.field.AssetManager"), todomap.get("資產部經理"));
					resultMap.put(LocaleMessage.getMsg("statisticalreport.field.ITmanager"), todomap.get("IT經理"));
					resultMap.put(LocaleMessage.getMsg("statisticalreport.field.CHO"), todomap.get("人力資源總監"));
					resultMap.put(LocaleMessage.getMsg("statisticalreport.field.DOS"), todomap.get("組織系統供應鏈總監"));
					resultMap.put(LocaleMessage.getMsg("statisticalreport.field.CMAssetDirector"),
							todomap.get("便利購暨資產總監"));
					resultMap.put(LocaleMessage.getMsg("statisticalreport.field.FinancialDirector"),
							todomap.get("財務總監"));
					resultMap.put(LocaleMessage.getMsg("statisticalreport.field.GeneralManager"), todomap.get("總經理"));
					resultMap.put(LocaleMessage.getMsg("statisticalreport.field.LegalSignature"), todomap.get("法務簽章"));
				}
				// munList存放用來轉換百分比
				munList.putAll(resultMap);
				resultMap.put("Count", todomap.get("件數"));
				resultMap.put("todoreject", todomap.get("退件"));
				resultMap.put("todoArchive", todomap.get("歸檔"));
				munList.put("todoArchive", MapUtils.getInteger(resultMap, "todoArchive"));
				munList.put("todoreject", MapUtils.getInteger(resultMap, "todoreject"));
				resultMap.put(LocaleMessage.getMsg("contract.field.contractor"), contractor);
				resultMap.put(LocaleMessage.getMsg("contract.field.module"), module.equals("NSC") ? "非制式合約" : "制式合約");
				int total = MapUtils.getInteger(resultMap, "Count");
				for (String resultListKey : munList.keySet()) {
					resultMap.put(resultListKey, (resultMap.get(resultListKey) + "("
							+ convertPercent(MapUtils.getInteger(resultMap, resultListKey), total) + ")"));
				}
				resultList.add(resultMap);
			}
		}
		// 排序及分頁
		gridResult = this.grid(params);
		ElasticSearchUtil.sortPagination(gridResult, resultList, params);

		return gridResult;
	}

	/**
	 * 查詢非制式案件比例
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult selectNTSList(Map<String, Object> params) throws Exception {
		GridResult gridResult = null;
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		String module = MapUtils.getString(params, "module");
		String beginColumnName = "data.docdetail.resultdata";
		String indexName = "";
		StringBuffer esSql = null;
		String contractBgnDate = MapUtils.getString(params, "contractBgnDate");
		String contractEndDate = MapUtils.getString(params, "contractEndDate");
		String year = MapUtils.getString(params, "year");

		Map<String, Object> compParams = new HashMap<String, Object>();
		compParams.put("contractmodel", module);
		compParams.put("contractBgnDate", contractBgnDate);
		compParams.put("contractEndDate", contractEndDate);

		if (StringUtils.isNotBlank(year)) {
			compParams.put("indexname", year + "_" + module.toLowerCase() + "_qryrec_doc");
		}

		List<Map<String, Object>> contractmasterList = contractMapper.selectContractReport(compParams);
		Map<String, Integer> countmap = new LinkedHashMap<String, Integer>();
		if (contractmasterList.size() > 0) {
			for (Map<String, Object> contractmasterLists : contractmasterList) {
				String todo = "";
				String dataid = MapUtils.getString(contractmasterLists, "DATAID");
				indexName = MapUtils.getString(contractmasterLists, "INDEXNAME");
				// TASKDESC 合約DB未有DOC處理
				contractmasterLists.put("TASKDESC",
						MapUtils.getString(contractmasterLists, "TASKDESC") != null
								? MapUtils.getString(contractmasterLists, "TASKDESC")
								: "");

				// 狀態無 做ELSEARCH搜尋 取得狀態
				if (MapUtils.getString(contractmasterLists, "FLOWSTATUS") == null) {
					esSql = new StringBuffer();
					esSql.append(" where " + "_id = '" + dataid + "'");
					List<String> resultJson = ElasticSearchUtil.searchForES(indexName, "todo," + beginColumnName,
							esSql.toString());
					if (resultJson != null && resultJson.size() > 0) {
						for (String json : resultJson) {
							ReadContext readContext = JsonPath.parse(json);
							todo = readContext.read("$.todo");
						}
					}
				}
				// 排除作廢
				if (todo.equals("作廢") || MapUtils.getString(contractmasterLists, "TASKDESC").equals("申請單作廢")) {
					continue;
				}
				String flowid = MapUtils.getString(contractmasterLists, "FLOWID");
				// 取得flowid前五碼查詢合約類型
				flowid = (String) flowid.subSequence(0, 5);
				if (countmap.containsKey(flowid)) {
					countmap.put(flowid, MapUtils.getInteger(countmap, flowid) + 1);
				} else {
					countmap.put(flowid, 1);
				}
			}
			Integer count = 0;
			// 遍歷統計結果至前端grid
			for (String key : countmap.keySet()) {
				Map<String, Object> resultMap = new LinkedHashMap<String, Object>();

				// 取得非制式合約類型中文名
				XauthSysCode xauthSysCode = commonService.getSysCodeData("NSC_FLOW_TEMPLATE", key);
				if (xauthSysCode != null) {
					resultMap.put("ContractType", xauthSysCode.getCname());
				}
				resultMap.put("count", countmap.get(key));
				resultList.add(resultMap);
				count += countmap.get(key);
			}
			Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
			resultMap.put("ContractType", "總計");
			resultMap.put("count", count);
			resultList.add(resultMap);
		}

		// 排序及分頁
		gridResult = this.grid(params);
		ElasticSearchUtil.sortPagination(gridResult, resultList, params);

		return gridResult;
	}

	/**
	 * 查詢代理簽審合約案件資料
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public GridResult selectACDList(Map<String, Object> params) throws Exception {
		UserInfo userInfo = userContext.getCurrentUser();
		String beginColumnName = "data.docdetail.resultdata";
		String module = MapUtils.getString(params, "module");
		String year = MapUtils.getString(params, "year");
		String contractBgnDate = MapUtils.getString(params, "contractBgnDate");
		String contractEndDate = MapUtils.getString(params, "contractEndDate");
		String indexName = year + "_" + module.toLowerCase() + "_qryrec_doc";
		String flowStatus = "";
		String todo = "";
		GridResult gridResult = null;

		SimpleDateFormat format0 = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		StringBuffer esSql = null;

		Map<String, Object> compParams = new HashMap<String, Object>();
		if (!StringUtils.equals(userContext.getCurrentUser().getUserType(), USER_TYPE.SYS_ADMIN.getCode())) {
			compParams.put("userid", userInfo.getUserId());
		}
		compParams.put("contractBgnDate", contractBgnDate);
		compParams.put("contractEndDate", contractEndDate);
		compParams.put("module", module);

		List<Map<String, Object>> contractreviewList = contractMapper.selectACD(compParams);

		contractMapper.selectACD(compParams);
		if (contractreviewList.size() > 0) {
			for (Map<String, Object> data : contractreviewList) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				String dataid = MapUtils.getString(data, "DATAID");

				resultMap.put(LocaleMessage.getMsg("contract.field.no"), dataid);
				resultMap.put(LocaleMessage.getMsg("contract.field.module"),
						(module.equals("NSC") ? "非制式" : "制式") + "合約");
				resultMap.put(LocaleMessage.getMsg("contract.field.principal"), MapUtils.getString(data, "USERIDS"));
				resultMap.put(LocaleMessage.getMsg("contract.field.undertakeTask"),
						MapUtils.getString(data, "STEPNAME"));
				resultMap.put(LocaleMessage.getMsg("contract.field.undertakeAction"),
						MapUtils.getString(data, "REVIEWRLT"));
//				String undertakeSenfDate = MapUtils.getString(data, "CREATEDATE");
				Date undertakeSenfDate = (Date) data.get("CREATEDATE");

				resultMap.put(LocaleMessage.getMsg("contract.field.undertakeSenfDate"),
						format1.format(undertakeSenfDate));

				// 取得送審日期
				Contractmaster contractmaster = commonService.getContractmasterData(dataid);
				// 放入送審日期
				resultMap.put(LocaleMessage.getMsg("contract.field.submit"),
						format0.format(contractmaster.getSendDate()));

				if (StringUtils.isBlank(year)) {
					indexName = contractmaster.getIndexname();
				}
				// EL查詢
				esSql = new StringBuffer();
				esSql.append(" where " + "_id = '" + dataid + "'");
				List<String> resultJson = ElasticSearchUtil.searchForES(indexName, "todo," + beginColumnName,
						esSql.toString());
				// 取得resultdata
				if (resultJson != null && resultJson.size() > 0) {
					for (String json : resultJson) {
						ReadContext readContext = JsonPath.parse(json);
						todo = readContext.read("$.todo");
						List<Map<String, Object>> resultdataList = readContext.read("$..resultdata");
						if (resultdataList.size() > 0) {
							Map<String, Object> resultdataMap = resultdataList.get(0);
							resultMap.put(LocaleMessage.getMsg("contract.field.name"),
									resultdataMap.get(LocaleMessage.getMsg("contract.field.name")));
							resultMap.put(LocaleMessage.getMsg("xauth.field.transfer.suppliergui"),
									resultdataMap.get(LocaleMessage.getMsg("suppliter.field.suppliergui")));
							resultMap.put(LocaleMessage.getMsg("suppliter.field.suppliercname"),
									resultdataMap.get(LocaleMessage.getMsg("suppliter.field.suppliercname")));
							// 合約承辦人 當有承接人以承接人為主
							String contractor = MapUtils.getString(resultdataMap,
									LocaleMessage.getMsg("contract.field.undertakeuserid"));
							if (StringUtils.isBlank(contractor)) {
								contractor = MapUtils.getString(resultdataMap,
										LocaleMessage.getMsg("contract.json.contractor"));
							}
							resultMap.put(LocaleMessage.getMsg("contract.json.contractor"), contractor);
						}
					}
				} else {
					continue;
				}
				// 取得合約狀態
				QueryWrapper<Docstate> contractmasterQueryWrapper = new QueryWrapper<Docstate>();
				contractmasterQueryWrapper.eq("APPLYNO", dataid);
				List<Docstate> contractmasterList = docstateMapper.selectList(contractmasterQueryWrapper);
				if (contractmasterList.size() > 0) {
					String taskname = contractmasterList.get(0).getTaskname();
					flowStatus = contractmasterList.get(0).getFlowstatus();
					if (StringUtils.equals(flowStatus, "PROCESS")) {
						if (StringUtils.equals(todo, "駁回")) {
							if (StringUtils.equals(taskname, "申請人")) {
								flowStatus = "編輯中";
							} else {
								flowStatus = "退回重審";
							}
						} else {
							flowStatus = "審核中";
						}
					} else if (StringUtils.equals(flowStatus, "END")) {
						flowStatus = "歸檔";
					} else if (StringUtils.equals(flowStatus, "CANCEL")) {
						flowStatus = "作廢";
					} else {
						if (StringUtils.equals(todo, "作廢")) {
							flowStatus = "作廢";
						}
					}
				}
				resultMap.put(LocaleMessage.getMsg("contract.field.contractStatus"), flowStatus);
				resultList.add(resultMap);
			}
		}
		// 排序及分頁
		gridResult = this.grid(params);
		ElasticSearchUtil.sortPagination(gridResult, resultList, params);

		return gridResult;
	}

	/**
	 * 回傳前端新增報表
	 * 
	 * @param params
	 * @return
	 */
	public ProcessResult insertStatisticalReportDate(Map<String, Object> params) {
		ProcessResult result = new ProcessResult();
		try {
			insertStatisticalReport(params);
			result.addMessage("新增成功 ");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 寫入資料庫
	 * 
	 * @param params
	 * @return
	 */
	public void insertStatisticalReport(Map<String, Object> params) throws Exception {
		Map<String, Object> Reportmap = new HashMap<String, Object>();
		try {
			logger.info("傳入參數" + params);
			String timeFrame = MapUtils.getString(params, "timeFrame");
			String jsonData = MapUtils.getString(params, "paramsData");
			List<Map<String, Object>> Datalist = JsonUtils.jsonArray2MapList(jsonData);
			String createuser = MapUtils.getString(params, "createuser");
			// 合約類型
			String module = MapUtils.getString(params, "module");
			// 報表類型
			String srt = MapUtils.getString(params, "statisticalReportType");
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			StringBuffer date = new StringBuffer();
			String years = Integer.toString(cal.get(Calendar.YEAR));
			Integer month = cal.get(Calendar.MONTH) + 1;
			Integer day = cal.get(Calendar.DAY_OF_MONTH);
			date.append(years);
			date.append(month < 10 ? "0" + month : Integer.toString(month));
			date.append(day < 10 ? "0" + day : Integer.toString(day));
			StatisticalReport statisticalReport = new StatisticalReport();
			boolean isLogr = false;
			String rptid = srt + "_" + getContractNo(isLogr, date.toString(), years, srt);
			date.setLength(0);
			date.append(years);
			date.append("/");
			date.append(month);
			date.append("/");
			date.append(day);
			Reportmap.put("srt", srt);
			Reportmap.put("rptid", rptid);
			Reportmap.put("timeFrame", timeFrame);
			Reportmap.put("module", module);
			Reportmap.put("createuser", createuser);
			Reportmap.put("date", date.toString());
			Datalist.add(Reportmap);
			Gson gson = new Gson();
			jsonData = gson.toJson(Datalist);

			// 寫入資料庫
			statisticalReport.setRptid(rptid);
			statisticalReport.setCreatedate(new Timestamp(new Date().getTime()));
			statisticalReport.setCreateuser(getCreOrUpdUser(null));
			statisticalReport.setDownloadpath("");
			statisticalReport.setJson(jsonData);
			statisticalReport.setUpdatedate(new Timestamp(new Date().getTime()));
			statisticalReportMapper.insert(statisticalReport);
		} catch (Exception e) {
			logger.info("insertStatisticalReport Exception : ", e, e);
		}
	}

	/**
	 * 統計報表狀態
	 * 
	 * @param map
	 * @param key
	 * @return
	 */
	private Map<String, Object> mapcount(Map<String, Object> map, String key) {
		if (map.get(key) != null) {
			if (key.equals("新建") || key.equals("暫存")) {
				map.put("製作編輯中", MapUtils.getInteger(map, "製作編輯中") + 1);
			} else {
				if (key.equals("OSD")) {
				}
				map.put(key, (MapUtils.getInteger(map, key) + 1));
			}
		} else {
			map.put(key, 1);
		}
		return map;
	}

	/**
	 * 初始化統計LIST
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, Object> initCheckPoint(Map<String, Object> map) {
		map.put("件數", 0);
		map.put("退件", 0);
		map.put("作廢", 0);
		map.put("歸檔", 0);
		map.put("製作編輯中", 0);
		map.put("總審核數", 0);
		map.put("Task2", 0);
		map.put("Task3", 0);
		map.put("Task4", 0);
		map.put("Task5", 0);
		map.put("Task6", 0);
		map.put("Task7", 0);

		// NSC
		map.put("廠商簽核中", 0);
		map.put("店經理/單位主管", 0);
		map.put("組織最高主管", 0);
		map.put("法務審核中", 0);
		map.put("廠商簽核中", 0);
		map.put(LocaleMessage.getMsg("statisticalreport.field.StoreManager"), 0);
		map.put("OSD", 0);
		map.put(LocaleMessage.getMsg("statisticalreport.field.Legalcounsel"), 0);
		map.put(LocaleMessage.getMsg("statisticalreport.field.ChiefLegalOfficer"), 0);
		map.put(LocaleMessage.getMsg("statisticalreport.field.Purchasing"), 0);
		map.put(LocaleMessage.getMsg("statisticalreport.field.AssetManager"), 0);
		map.put(LocaleMessage.getMsg("statisticalreport.field.ITmanager"), 0);
		map.put(LocaleMessage.getMsg("statisticalreport.field.CHO"), 0);
		map.put(LocaleMessage.getMsg("statisticalreport.field.DOS"), 0);
		map.put(LocaleMessage.getMsg("statisticalreport.field.CMAssetDirector"), 0);
		map.put(LocaleMessage.getMsg("statisticalreport.field.FinancialDirector"), 0);
		map.put(LocaleMessage.getMsg("statisticalreport.field.GeneralManager"), 0);
		map.put("法務簽章", 0);

		return map;
	}

	/**
	 * 取得統計報表序號
	 * 
	 * @param isLogr
	 * @param date
	 * @param years
	 * @param srt
	 * @return
	 */
	public String getContractNo(boolean isLogr, String date, String years, String srt) throws Exception {
		Codelist codelist = new Codelist();
		QueryWrapper<Codelist> queryWrapper = new QueryWrapper<Codelist>();

		String resultString = "";

		if (isLogr) {
			resultString += date;
			codelist.setClasstype(srt);
			queryWrapper.eq("CLASSTYPE", srt);
		} else {
			resultString += date;

			codelist.setClasstype(srt);
			queryWrapper.eq("CLASSTYPE", srt);
			queryWrapper.isNull("DEPTNO");
		}

		queryWrapper.eq("SYS", "SEQUENCE");
		queryWrapper.eq("YEARS", years);

		List<Codelist> codelistList = codelistMapper.selectList(queryWrapper);

		if (codelistList.size() == 0) {
			resultString += "000001";
			codelist.setSys("SEQUENCE");
			codelist.setYears(years);
			codelist.setAnumber(2);
			codelist.setNote("序號");

			codelistMapper.insert(codelist);
		} else {
			codelist = codelistList.get(0);
			Integer anumber = codelist.getAnumber();
			String mun = String.valueOf(anumber);
			mun = StringUtils.leftPad(mun, 6, '0');
			resultString += mun;
			codelist.setAnumber(anumber + 1);
			codelistMapper.update(codelist, queryWrapper);
		}
		return resultString;
	}

	/**
	 * 轉換百分比
	 * 
	 * @param fraction
	 * @param denominator
	 * @return
	 */
	private String convertPercent(int fraction, int denominator) {
		String percent = "";
		double baiy = fraction * 1.0;
		double baiz = denominator * 1.0;
		double fen = baiy / baiz;

		DecimalFormat df1 = new DecimalFormat("##.##%");

		percent = df1.format(fen);

		return percent;
	}

	/**
	 * 取得角色中文名
	 * 
	 * @param userid
	 * @return
	 */
	private String getUserCName(String userid) {
		String useCName = "";
		QueryWrapper<XauthUsers> queryWrapper = new QueryWrapper<XauthUsers>();
		queryWrapper.eq("USER_ID", userid);
		List<XauthUsers> xauthUserList = xauthUsersMapper.selectList(queryWrapper);
		if (xauthUserList.size() > 0) {
			useCName = xauthUserList.get(0).getUserCname();
		} else
			useCName = userid;

		return useCName;
	}
}
