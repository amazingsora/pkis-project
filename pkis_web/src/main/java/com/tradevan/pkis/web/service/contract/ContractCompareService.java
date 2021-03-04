package com.tradevan.pkis.web.service.contract;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tradevan.xauthframework.web.service.DefaultService;

@Service("ContractCompareService")
@Transactional(rollbackFor = Exception.class)
public class ContractCompareService extends DefaultService {
	
	@Autowired
	ContractReportService contractReportService;
	
	List<String> filter = Arrays.asList(
			"基本資料",
			"明細資料",
			"附件資料",
			"審核評估",
			"審核意見"
	);

	Map<String, Object> editContentMap = new LinkedHashMap<String, Object> ();
	
	Map<String, Object> baseContentMap = new LinkedHashMap<String, Object> ();
	
	public static void main(String[] args) {
		ContractCompareService testCompare = new ContractCompareService();
		try {
			testCompare.build();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 測試
	public void build() throws Exception {
		ContractReportService reportService = new ContractReportService();
		String editJson = readJsonFile("C:\\Users\\im\\Desktop\\json_test.txt");
		String baseJson = readJsonFile("C:\\Users\\im\\Desktop\\json_lastVer_test.txt");
//		logger.info("editJson == " + editJson);
//		logger.info("baseJson == " + baseJson);
		editContentMap = new LinkedHashMap<String, Object> ();
		baseContentMap = new LinkedHashMap<String, Object> ();
		JsonObject editJsonObj = prepareJson(editJson, "edit", editContentMap);
		JsonObject baseJsonObj = prepareJson(baseJson, "base", baseContentMap);
		String resultJson = compare(baseJsonObj, editJsonObj);
		saveFile("json.txt", resultJson);
		reportService.build();
	}
	
	/**
	 * 主流程
	 * @param baseJson
	 * @param editJson
	 * @param resultPath
	 * @param resultFileName
	 * @throws Exception
	 */
	public void build(String baseJson, String editJson, String resultPath, String resultFileName) throws Exception {
		editContentMap = new LinkedHashMap<String, Object> ();
		baseContentMap = new LinkedHashMap<String, Object> ();
		JsonObject editJsonObj = prepareJson(editJson, "edit", editContentMap);
		JsonObject baseJsonObj = prepareJson(baseJson, "base", baseContentMap);
		String resultJson = compare(baseJsonObj, editJsonObj);
		contractReportService.createScPdf(resultPath, resultFileName, resultJson);
	}
	
	/**
	 * 讀取JSON
	 * @param jsonFilePath
	 * @return
	 * @throws Exception
	 */
	public String readJsonFile(String jsonFilePath) throws Exception {
		String result = "";
		String line = "";
		FileReader fileReader = new FileReader(jsonFilePath);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		while(StringUtils.isNotBlank(line = bufferedReader.readLine())) {
			result = result + line;
		}
		bufferedReader.close();
		fileReader.close();
		
		return result;
	}
	
	/**
	 * 取得dataType
	 * @param json
	 * @param dataType
	 * @return
	 */
	public JsonObject prepareJson(String json, String jsonType, Map<String, Object> contentMap) throws Exception {
		JsonObject root = new JsonParser().parse(json).getAsJsonObject();
		JsonArray jsons = root.get("data").getAsJsonArray();
		JsonObject jsonObj = null;
		String datatype = "";
		int jsonsSize = jsons.size();
		for(int i = 0 ; i < jsonsSize ; i ++) {
			datatype = ((JsonObject) jsons.get(i)).get("datatype").getAsString();
			if(!filter.contains(datatype)) {
				jsonObj = (JsonObject) jsons.get(i);
				if(StringUtils.equals(datatype, "0.前言")) {
					buildPrefaceGroupid(jsonObj, jsonType, contentMap);
				} else {
					buildGroupid(jsonObj, jsonType, contentMap);
				}
			}
		}
		
//		logger.info("editContentMap == " + new Gson().toJson(editContentMap));
//		logger.info("baseContentMap == " + new Gson().toJson(baseContentMap));
//		logger.info("json == " + new Gson().toJson(root));
		
		return root;
	}
	
	/**
	 * 組前言groupid
	 * @param jsonObj
	 * @param jsonType
	 */
	public void buildPrefaceGroupid(JsonObject jsonObj, String jsonType, Map<String, Object> contentMap) {
		JsonArray docdetail = jsonObj.get("docdetail").getAsJsonArray();
		JsonElement uitype = null;
		JsonElement displayname = null;
		JsonElement row = null;
		JsonElement col = null;
		String datatype = jsonObj.get("datatype").getAsString();
		StringBuffer content = new StringBuffer();
		int docdetailSize = docdetail.size();
		int groupNum = 0;
		String groupid = "";
		
		for(int i = 0 ; i < docdetailSize ; i ++) {
			uitype = ((JsonObject) docdetail.get(i)).get("uitype");
			displayname = ((JsonObject) docdetail.get(i)).get("displayname");
			row = ((JsonObject) docdetail.get(i)).get("row");
			col = ((JsonObject) docdetail.get(i)).get("col");
			if(uitype != null && (StringUtils.equals(uitype.getAsString(), "label") || StringUtils.equals(uitype.getAsString(), "checkbox"))) {
				if(col.getAsInt() != 0) {
					if(row.getAsInt() % 2 == 0) {
						groupNum ++;
						content = new StringBuffer();
					}
				}
				groupid = jsonType + "_" + datatype + "_" + groupNum;
				// 將條文組成一組groupid，放入節點
				((JsonObject) docdetail.get(i)).addProperty("groupid", groupid);
				// 組groupid放入MAP
				contentMap.put(groupid, content.append(displayname.getAsString()));
			}
		}
	}
	
	/**
	 * 準備JSON，組內文groupid
	 * @param jsonObj
	 * @param jsonType
	 * @throws Exception
	 */
	public void buildGroupid(JsonObject jsonObj, String jsonType, Map<String, Object> contentMap) throws Exception {
		JsonArray docdetail = jsonObj.get("docdetail").getAsJsonArray();
		JsonElement uitype = null;
		JsonElement displayname = null;
		String datatype = jsonObj.get("datatype").getAsString();
		StringBuffer content = new StringBuffer();
		
		// docdetail array
		int col = 0;
		int docdetailSize = docdetail.size();
		String item = "";
		String groupid = "";
		for(int i = 0 ; i < docdetailSize ; i ++) {
			uitype = ((JsonObject) docdetail.get(i)).get("uitype");
			if(uitype != null && (StringUtils.equals(uitype.getAsString(), "label") || StringUtils.equals(uitype.getAsString(), "checkbox")
					|| StringUtils.equals(uitype.getAsString(), "field") || StringUtils.equals(uitype.getAsString(), "text"))) {
				displayname = ((JsonObject) docdetail.get(i)).get("displayname");
				col = ((JsonObject) docdetail.get(i)).get("col").getAsInt();
				// 換下一組group
				if((col == 0 && isNumeric(displayname.getAsString())) || (col == 1 && isNumeric(displayname.getAsString()))) {
					item = displayname.getAsString();
					groupid = jsonType + "_" + datatype + "_" + item;
					if(!contentMap.containsKey(groupid)) {
						content = new StringBuffer();
					}
				} 
				if(StringUtils.isBlank(groupid)) groupid = jsonType + "_" + datatype + "_" + item;
				// 將條文組成一組groupid，放入節點
				((JsonObject) docdetail.get(i)).addProperty("groupid", groupid);
				// 組groupid放入MAP
				contentMap.put(groupid, content.append(displayname.getAsString()));
			}
		}
	}
	
	/**
	 * 判斷是否為數字
	 * @param strNum
	 * @return
	 */
	public boolean isNumeric(String strNum) throws Exception {
	    if (strNum == null) {
	        return false; 
	    }
	    strNum = StringUtils.replace(strNum, ".", "");
	    Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
	    return pattern.matcher(strNum).matches();
	}
	
	/**
	 * 進行比較差異
	 * @param baseJsonObj
	 * @param editJsonObj
	 * @return
	 * @throws Exception
	 */
	// 當「基底版」有資料且「編輯版」有資料：當無差異時，資料來源：以「基底版」為主
	// 比對主要分三種類 : 
	// type1：「基底版」有資料「編輯版」有資料，但內容不同
	// type2：「基底版」有資料「編輯版」無資料
	// type3：「基底版」無資料「編輯版」有資料
	// type4：「基底版」整大段(包含標題)無資料「編輯版」有資料
	public String compare(JsonObject baseJsonObj, JsonObject editJsonObj) throws Exception {
		JsonArray newArray = null;
		JsonArray datas = baseJsonObj.get("data").getAsJsonArray();
		JsonArray editDatas = editJsonObj.get("data").getAsJsonArray();
		JsonArray docdetails = null;
		JsonObject data = null;
		JsonObject docdetail = null;
		JsonObject docdetailCopy = null;
		JsonElement uitype = null;
		JsonElement groupid = null;
		JsonElement col = null;
		String datatype = "";
		String baseContent = "";
		String editContent = "";
		String groupidTemp = ""; // 紀錄比對有差異的條文groupid
		String editGroupid = ""; 
		String result = "";
		String type = ""; 
		boolean isDifferent = false; //判斷內容是否有異
		boolean isSubItem = false; // 判斷是否為子序號 ex: 8.2.3
		List<String> editGroupids = null;
		List<String> editSubItemGroupids = null;
		List<String> dataTypes = new ArrayList<String>();
		int row = 0;
		int nowRow = 0;
		int count = 0;
		int datasSize = datas.size();
		
		// data array
		for(int i = 0 ; i < datasSize ; i ++) {
			newArray = new JsonArray();
			data = ((JsonObject) datas.get(i));
			datatype = data.get("datatype").getAsString();
			if(!filter.contains(datatype)) {
				dataTypes.add(datatype);
				docdetails = data.get("docdetail").getAsJsonArray();
				editGroupids = new ArrayList<String>();
				editSubItemGroupids = new ArrayList<String>();
				count = 0;
				// docdetail array
				int docdetailSize = docdetails.size();
				for(int j = 0 ; j < docdetailSize ; j ++) {
					docdetail = (JsonObject) docdetails.get(j);
					uitype = docdetail.get("uitype");
					groupid = docdetail.get("groupid");
					col = docdetail.get("col");
					// 新增type1及type2內容
					if(StringUtils.isNotBlank(groupidTemp) && isDifferent && (groupid == null || !StringUtils.equals(groupidTemp, groupid.getAsString()))) {
						if(uitype != null && (StringUtils.equals(uitype.getAsString(), "label") || 
								StringUtils.equals(uitype.getAsString(), "checkbox") || StringUtils.equals(uitype.getAsString(), "button"))) {
							nowRow = ((JsonObject) newArray.get(newArray.size() - 1)).get("row").getAsInt();
							if(StringUtils.equals(type, "1")) {
								// type1
								addDifferentContent(newArray, editJsonObj, datatype, groupidTemp, nowRow);
							} else if(StringUtils.equals(type, "2")) {
								// type2
								addEditLakeContent(newArray, baseJsonObj, datatype, groupidTemp, nowRow);
							}
							isDifferent = false;
							groupidTemp = "";
						}
					}
					// 新增type3內容 - 子項目
					if(col != null && col.getAsInt() == 0 && isSubItem) {
						nowRow = ((JsonObject) newArray.get(newArray.size() - 1)).get("row").getAsInt();
						addEditContent(newArray, editJsonObj, datatype, editSubItemGroupids, nowRow, "Y");
						editSubItemGroupids = new ArrayList<String>();
						isSubItem = false;
					}
					// 新增主內容
					if(count == 0) {
						newArray.add(docdetail.deepCopy());
					} else {
						// 重新計算row
						nowRow = ((JsonObject) newArray.get(newArray.size() - 1)).get("row").getAsInt();
						docdetailCopy = docdetail.deepCopy();
						docdetailCopy.addProperty("row", nowRow + (docdetail.get("row").getAsInt() - row));
						newArray.add(docdetailCopy);
					}
					
					if(uitype != null && (StringUtils.equals(uitype.getAsString(), "label") || StringUtils.equals(uitype.getAsString(), "checkbox"))) {
						row = docdetail.get("row").getAsInt();
						if(baseContentMap != null && baseContentMap.size() > 0 && editContentMap != null && editContentMap.size() > 0) {
							if(baseContentMap.containsKey(groupid.getAsString())) {
								editGroupid = groupid.getAsString().replace("base", "edit");
								baseContent = MapUtils.getString(baseContentMap, groupid.getAsString());
								editContent = MapUtils.getString(editContentMap, editGroupid);
								if(!StringUtils.equals(baseContent, editContent)) {
									if(StringUtils.isBlank(editContent)) {
										type = "2";
									} else {
										type = "1";
									}
									isDifferent = true;
									groupidTemp = groupid.getAsString();
								}
								if(!editGroupids.contains(editGroupid)) {
									editGroupids.add(editGroupid);
								}
								// 判斷是否有子項目
								String[] groupidArray = StringUtils.split(groupid.getAsString(), "_");
								String subItem = groupidArray[groupidArray.length - 1];
								if(isNumeric(subItem) && StringUtils.split(subItem, ".").length > 2) {
									if(!editSubItemGroupids.contains(editGroupid)) {
										editSubItemGroupids.add(editGroupid);
									}
									isSubItem = true;
								}
							}
						} else {
							logger.error("資料準備發生錯誤");
							throw new Exception("資料準備發生錯誤");
						}
						count ++;
					}
				}
				// 新增type3內容
				nowRow = ((JsonObject) newArray.get(newArray.size() - 1)).get("row").getAsInt();
				addEditContent(newArray, editJsonObj, datatype, editGroupids, nowRow, "N");
				
				data.add("docdetail", newArray);
			}
		}
		
		// type4 編輯版有大標，發佈版沒有
		if(editDatas.size() >= datas.size()) {
			addEditTitleContent(datas, editJsonObj, dataTypes);
		}
		
		result = new Gson().toJson(baseJsonObj);
//		logger.info("resultJson == " + result);
		
		return result;
	}
	
	/**
	 * 從groupid取得Json資料
	 * @param jsonObj
	 * @param datatype
	 * @param groupid
	 * @return
	 */
	public JsonArray getAddJsonArray(JsonObject jsonObj, String datatype, String groupid) {
		JsonArray result = new JsonArray();
		JsonArray datas = jsonObj.get("data").getAsJsonArray();
		JsonArray docdetails = null;
		JsonObject data = null;
		JsonObject docdetail = null;
		JsonObject docdetailJsonObj = null;
		JsonElement remark = null;
		int datasSize = datas.size();
		
		for(int i = 0 ; i < datasSize ; i ++) {
			data = (JsonObject) datas.get(i);
			if(StringUtils.equals(data.get("datatype").getAsString(), datatype)) {
				docdetails = data.get("docdetail").getAsJsonArray();
				int docdetailSize = docdetails.size();
				for(int j = 0 ; j < docdetailSize ; j ++) {
					docdetail = (JsonObject) docdetails.get(j);
					remark = docdetail.get("remark");
					// StringUtils.contains(docdetail.get("groupid").getAsString(), groupid)
					if(docdetail.get("groupid") != null && StringUtils.equals(docdetail.get("groupid").getAsString(), groupid)) {
						docdetailJsonObj = docdetail.deepCopy();
						docdetailJsonObj.addProperty("fontcolor", "red");
						if(remark != null && remark.getAsString().contains("table")) {
							docdetailJsonObj.addProperty("remark", remark.getAsString() + "_compare");
						}
						result.add(docdetailJsonObj);
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * type1：「基底版」有資料「編輯版」有資料，但內容不同
	 * @param newArray
	 * @param editJsonObj
	 * @param datatype
	 * @param groupidTemp
	 * @param row
	 */
	public void addDifferentContent(JsonArray newArray, JsonObject editJsonObj, String datatype, String groupidTemp, int row) {
		JsonArray jsons = getAddJsonArray(editJsonObj, datatype, groupidTemp.replace("base", "edit"));
		JsonObject json = null;
		int oriRow = 0;
		int jsonsSize = jsons.size();
		String titleName = "編輯版差異部分：(與發佈版條文內容不同)";
		
		// 差異標題
		row = row + 1;
		json = new JsonObject();
		json.addProperty("id", "temptitleid");
		json.addProperty("displayname", titleName);
		json.addProperty("value", "");
		json.addProperty("uitype", "label");
		json.addProperty("row", row);
		json.addProperty("col", 1);
		json.addProperty("orgString", titleName);
		json.addProperty("groupid", groupidTemp.replace("base", "edit"));
		json.addProperty("fontcolor", "red_title");
		newArray.add(json);
		
		for(int i = 0 ; i < jsonsSize ; i ++) {
			json = (JsonObject) jsons.get(i);
			// 重新計算row
			if(i == 0) {
				oriRow = json.get("row").getAsInt();
				row = row + 1;
			} else {
				row = row + (json.get("row").getAsInt() - oriRow);
				oriRow = json.get("row").getAsInt();
			}
			json.addProperty("row", row);
			newArray.add(json);
		}
	}
	
	/**
	 * type2：「基底版」有資料「編輯版」無資料
	 * @param newArray
	 * @param baseJsonObj
	 * @param datatype
	 * @param groupidTemp
	 * @param row
	 */
	public void addEditLakeContent(JsonArray newArray, JsonObject baseJsonObj, String datatype, String groupidTemp, int row) {
		JsonArray jsons = getAddJsonArray(baseJsonObj, datatype, groupidTemp);
		JsonObject json = null;
		int jsonsSize = jsons.size();
		String titleName = "編輯版差異部分：(發佈版有，編輯版沒有)";
		
		// 差異標題
		row = row + 1;
		json = new JsonObject();
		json.addProperty("id", "temptitleid");
		json.addProperty("displayname", titleName);
		json.addProperty("value", "");
		json.addProperty("uitype", "label");
		json.addProperty("row", row);
		json.addProperty("col", 1);
		json.addProperty("orgString", titleName);
		json.addProperty("groupid", groupidTemp.replace("base", "edit"));
		json.addProperty("fontcolor", "red_title");
		newArray.add(json);
		
		if(jsonsSize > 0) {
			int col = 0;
			for(int i = 0 ; i < jsonsSize ; i ++) {
				json = (JsonObject) jsons.get(i);
				if(i == 0) {
					col = json.get("col").getAsInt();
					row = row + 1;
					json.addProperty("row", row);
					newArray.add(json);	
				}
			}
			json = new JsonObject();
			json.addProperty("id", "tempid");
			json.addProperty("displayname", "缺");
			json.addProperty("value", "");
			json.addProperty("uitype", "label");
			json.addProperty("row", row);
			json.addProperty("col", col + 1);
			json.addProperty("orgString", "缺");
			json.addProperty("groupid", groupidTemp.replace("base", "edit"));
			json.addProperty("fontcolor", "red");
			newArray.add(json);
		}
	}
	
	/**
	 * type3：「基底版」無資料「編輯版」有資料
	 * @param newArray
	 * @param editJsonObj
	 * @param datatype
	 * @param editGroupids
	 * @param row
	 * @throws Exception 
	 */
	public void addEditContent(JsonArray newArray, JsonObject editJsonObj, String datatype, List<String> editGroupids, int row, String isSubItem) throws Exception {
		JsonArray jsons = null;
		JsonObject json = null;
		int oriRow = 0;
		int jsonsSize = 0;
		String[] editContentKeyArray = null;
		String editContentKeySplit = "";
		String titleName = "編輯版差異部分：(編輯版有，發佈版沒有)";
		
		for(String editContentKey : editContentMap.keySet()) {
			if(StringUtils.contains(editContentKey, datatype)) {
				if(editGroupids.size() == 0) {
					editContentKeyArray = StringUtils.split(editContentKey, "_");
					editContentKeySplit = editContentKeyArray[editContentKeyArray.length - 1];
					// 若有子項目，判斷到回主項目時停止
					if(StringUtils.equals(isSubItem, "Y") && isNumeric(editContentKeySplit) && StringUtils.split(editContentKeySplit, ".").length <= 2) {
						break;
					}
					// 差異標題
					row = row + 1;
					json = new JsonObject();
					json.addProperty("id", "temptitleid");
					json.addProperty("displayname", titleName);
					json.addProperty("value", "");
					json.addProperty("uitype", "label");
					json.addProperty("row", row);
					json.addProperty("col", 1);
					json.addProperty("orgString", titleName);
					json.addProperty("groupid", editContentKey);
					json.addProperty("fontcolor", "red_title");
					newArray.add(json);
					jsons = getAddJsonArray(editJsonObj, datatype, editContentKey);
					jsonsSize = jsons.size();
					for(int i = 0 ; i < jsonsSize ; i ++) {
						json = (JsonObject) jsons.get(i);
						if(i == 0) {
							oriRow = json.get("row").getAsInt();
							row = row + 1;
						} else {
							row = row + (json.get("row").getAsInt() - oriRow);
							oriRow = json.get("row").getAsInt();
						}
						json.addProperty("row", row);
						newArray.add(json);
					}
				}
				for(String editGroupid : editGroupids) {
					if(StringUtils.equals(editContentKey, editGroupid)) {
						editGroupids.remove(editGroupid);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * type4：「基底版」整大段(包含標題)無資料「編輯版」有資料
	 * @param resultDatas
	 * @param editJsonObj
	 * @param dataTypes
	 */
	public void addEditTitleContent(JsonArray resultDatas, JsonObject editJsonObj, List<String> dataTypes) {
		JsonArray datas = editJsonObj.get("data").getAsJsonArray();
		JsonArray docdetails = null;
		JsonArray newArray = null;
		JsonObject editData = null;
		JsonObject docdetail = null;
		JsonObject json = null;
		int datasSize = datas.size();
		String datatype = "";
		String titleName = "編輯版差異部分：(編輯版有，發佈版沒有)";
		
		// data array
		for(int i = 0 ; i < datasSize ; i ++) {
			editData = ((JsonObject) datas.get(i));
			datatype = editData.get("datatype").getAsString();
			if(!filter.contains(datatype) && !dataTypes.contains(datatype)) {
				newArray = new JsonArray();
				// 差異標題
				json = new JsonObject();
				json.addProperty("id", "editMoreTitleid");
				json.addProperty("displayname", titleName);
				json.addProperty("value", "");
				json.addProperty("orgString", titleName);
				newArray.add(json);
				docdetails = editData.get("docdetail").getAsJsonArray();
				int docdetailSize = docdetails.size();
				for(int j = 0 ; j < docdetailSize ; j ++) {
					docdetail = (JsonObject) docdetails.get(j);
					docdetail.addProperty("fontcolor", "red");
					newArray.add(docdetail);
				}
				editData.add("docdetail", newArray);
				resultDatas.add(editData);
			}
		}
	}
	
	// 測試
	public void saveFile(String filePath, String json) throws Exception {
		File file = new File(filePath);
		BufferedWriter writter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
		writter.write(json);
		if(writter != null) {
			writter.close();
		}
	}
}
