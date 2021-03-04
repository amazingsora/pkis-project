package com.tradevan.pkis.web.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.tradevan.mapper.pkis.model.Suppliermaster;
import com.tradevan.xauthframework.core.common.LocaleMessage;

public class JsonUtil {
	
	/**
	 * ES 帶出的 JSON 去掉 hits 這兩個階層 只保留 _source 內的內容 轉 String
	 * @param json
	 * @return
	 */
	public static String jsonSkipToString(String json) {
		String result = "";
		JsonArray jsonArray = null;
		JsonObject jsonObj = new JsonParser().parse(json).getAsJsonObject();
		
		if(jsonObj.has("hits")) {
			jsonArray = jsonObj.get("hits").getAsJsonObject().get("hits").getAsJsonArray();
		}else {
			return json;
		}
		
		if(jsonArray != null) {
			int size = jsonArray.size();
			for(int i = 0 ; i < size ; i++) {
				result += jsonArray.get(i).getAsJsonObject().get("_source").toString();
			}
		}
		return result;
	}

	/**
	 * ES 帶出的 JSON 去掉 hits 這兩個階層 只保留 _source 內的內容 轉 List<String>
	 * @param json
	 * @return
	 */
	public static List<String> jsonSkipToList(String json){
		List<String> result = null;
		JsonArray jsonArray = null;
		JsonObject jsonObj = new JsonParser().parse(json).getAsJsonObject();
		
		if(jsonObj.has("hits")) {
			jsonArray = jsonObj.get("hits").getAsJsonObject().get("hits").getAsJsonArray();
		}
		
		if(jsonArray != null) {
			result = new ArrayList<String>();
			int size = jsonArray.size();
			for(int i = 0 ; i < size ; i++) {
				result.add(jsonArray.get(i).getAsJsonObject().get("_source").toString());
			}
		}
		return result;
	}
	
	/**
	 * JSON 塞值
	 * @param json
	 * @return
	 */
	public static String jsonSetValueByKey(String json, String key, String value) {
		
		String result = null;
		Gson gson = new Gson();
		JsonElement jsonElement = new JsonParser().parse(json);
		JsonElement valueElement = new JsonParser().parse(gson.toJson(value));
		
		if (jsonElement.isJsonObject()) {
            JsonObject jsonObj = jsonElement.getAsJsonObject();
            if(jsonObj.has(key)) {
            	jsonObj.add(key, valueElement);
                result = jsonObj.toString();
            }else {
            	System.out.println(" This Json Have Not This Key - " + key);
            }
        }
		
		return result;
	}
	
	public static String setSupplierMasterToJsonByJsonPath(String json, Suppliermaster suppliermaster) {
		DocumentContext documentContext = JsonPath.parse(json);
		JsonPath jsonPath = null;
//		jsonPath = JsonPath.compile("$.data[?(@.datatype == '基本資料' )].docdetail[?(@.resultdata)].resultdata.供應商廠編");
//		documentContext.set(jsonPath, suppliermaster.getSuppliercode() != null ? suppliermaster.getSuppliercode() : "");
//		jsonPath = JsonPath.compile("$.data[?(@.datatype == '基本資料' )].docdetail[?(@.displayname == '統一編號：' && @.value == '')].value");
//		documentContext.set(jsonPath, suppliermaster.getSuppliergui() != null ? suppliermaster.getSuppliergui() : "");
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '基本資料' )].docdetail[?(@.displayname == '公司名稱(中)：' && @.value == '')].value");
		documentContext.set(jsonPath, suppliermaster.getSuppliercname() != null ? suppliermaster.getSuppliercname() : "");
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '基本資料' )].docdetail[?(@.displayname == '公司名稱(英)：' && @.value == '')].value");
		documentContext.set(jsonPath, suppliermaster.getSupplierename() != null ? suppliermaster.getSupplierename() : "");
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '基本資料' )].docdetail[?(@.displayname == '法定代理人：' && @.value == '')].value");
		documentContext.set(jsonPath, suppliermaster.getPicuser() != null ? suppliermaster.getPicuser() : "");
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '基本資料' )].docdetail[?(@.displayname == '公司地址(中)：' && @.value == '')].value");
		documentContext.set(jsonPath, suppliermaster.getSuppliercaddr() != null ? suppliermaster.getSuppliercaddr() : "");
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '基本資料' )].docdetail[?(@.displayname == '公司地址(英)：' && @.value == '')].value");
		documentContext.set(jsonPath, suppliermaster.getSuppliereaddr() != null ? suppliermaster.getSuppliereaddr() : "");
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '基本資料' )].docdetail[?(@.displayname == 'TEL：' && @.value == '')].value");
		documentContext.set(jsonPath, suppliermaster.getSupplieretel() != null ? suppliermaster.getSupplieretel() : "");
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '基本資料' )].docdetail[?(@.displayname == 'FAX：' && @.value == '')].value");
		documentContext.set(jsonPath, "");
		//聯絡人
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '基本資料' )].docdetail[?(@.displayname == '聯絡人：' && @.value == '')].value");
		documentContext.set(jsonPath, suppliermaster.getContacruser() != null ? suppliermaster.getContacruser() : "");
		//電子郵件
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '基本資料' )].docdetail[?(@.displayname == '聯絡人電子郵件：' && @.value == '')].value");
		documentContext.set(jsonPath, suppliermaster.getSupplieremail() != null ? suppliermaster.getSupplieremail() : "");
		//客製化en 中英混合
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"' )].docdetail[?(@.displayname == '"+LocaleMessage.getMsg("contractEn.filed.CompanyNameCh")+"：' && @.value == '')].value");
		documentContext.set(jsonPath, suppliermaster.getSuppliercname() != null ? suppliermaster.getSuppliercname() : "");
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"' )].docdetail[?(@.displayname == '"+LocaleMessage.getMsg("contractEn.filed.CompanyNameEn")+"：' && @.value == '')].value");
		documentContext.set(jsonPath, suppliermaster.getSupplierename() != null ? suppliermaster.getSupplierename() : "");
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"' )].docdetail[?(@.displayname == '"+LocaleMessage.getMsg("contractEn.filed.Representative")+"：' && @.value == '')].value");
		documentContext.set(jsonPath, suppliermaster.getPicuser() != null ? suppliermaster.getPicuser() : "");
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"' )].docdetail[?(@.displayname == '"+LocaleMessage.getMsg("contractEn.filed.CompanyAddressCn")+"：' && @.value == '')].value");
		documentContext.set(jsonPath, suppliermaster.getSuppliercaddr() != null ? suppliermaster.getSuppliercaddr() : "");
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"' )].docdetail[?(@.displayname == '"+LocaleMessage.getMsg("contractEn.filed.CompanyAddressEn")+"：' && @.value == '')].value");
		documentContext.set(jsonPath, suppliermaster.getSuppliereaddr() != null ? suppliermaster.getSuppliereaddr() : "");
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"' )].docdetail[?(@.displayname == 'TEL：' && @.value == '')].value");
		documentContext.set(jsonPath, suppliermaster.getSupplieretel() != null ? suppliermaster.getSupplieretel() : "");
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"' )].docdetail[?(@.displayname == 'FAX：' && @.value == '')].value");
		documentContext.set(jsonPath, "");
		//聯絡人
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"' )].docdetail[?(@.displayname == '"+LocaleMessage.getMsg("contractEn.filed.Email")+"：' && @.value == '')].value");
		documentContext.set(jsonPath, suppliermaster.getSupplieremail() != null ? suppliermaster.getSupplieremail() : "");
		//電子郵件
		jsonPath = JsonPath.compile("$.data[?(@.datatype == '"+LocaleMessage.getMsg("contractEn.filed.Basic_Information")+"' )].docdetail[?(@.displayname == '"+LocaleMessage.getMsg("contractEn.filed.ContactPerson")+"：' && @.value == '')].value");
		documentContext.set(jsonPath, suppliermaster.getContacruser() != null ? suppliermaster.getContacruser() : "");
		
		json = documentContext.jsonString();
		return json;
	}
	
	public static String setDefaultValue(String json) {
		DocumentContext documentContext = JsonPath.parse(json);
		JsonPath jsonPath = null;
		jsonPath = JsonPath.compile("$..docdetail[?(@.displayname == ' E-Order and E-Invoice 電子訂單、電子發票')].value");
		documentContext.set(jsonPath, "True");
		json = documentContext.jsonString();
		return json;
	}
	
	/**
	 * 設定10.3合約起日寫入json
	 * @param json
	 * @return
	 */
	public static String setdcFeesConDateValue(String json, String contractBgnDate) {
		DocumentContext documentContext = JsonPath.parse(json);
		JsonPath jsonPath = JsonPath.compile("$.data[?(@.datatype == '10.DCFees物流費用' )].docdetail[?(@._ref=='合約起日')].value");
		documentContext.set(jsonPath, contractBgnDate);
		json = documentContext.jsonString();
		return json;
	}
	
	public static String setSupplierMasterToJson(String json, Suppliermaster suppliermaster) {
		String result = "";
		Gson gson = new Gson();
		JsonObject jsonObj = null;
		JsonElement jsonElement = new JsonParser().parse(json);
		
		if(suppliermaster == null) {
			return null;
		}
		
		if (jsonElement.isJsonObject()) {
			JsonObject jsonDataObj = jsonElement.getAsJsonObject();
			if(jsonDataObj.has("data")) {
				JsonArray jsonArray = jsonDataObj.get("data").getAsJsonArray();
				for(JsonElement dataElement : jsonArray) {
					jsonObj = dataElement.getAsJsonObject();
					jsonArray = jsonObj.get("docdetail").getAsJsonArray();
					for(JsonElement docdetailElement : jsonArray) {
						jsonObj = docdetailElement.getAsJsonObject();
						if(jsonObj.has("displayname") && jsonObj.has("value")) {
							String displayname = jsonObj.get("displayname").getAsString();
							String value = jsonObj.get("value").getAsString();
							if(StringUtils.isBlank(value) && StringUtils.isNoneBlank(displayname) && ("供應商廠編：, 統一編號：, 公司名稱(中)：, 公司名稱(英)：, 法定代理人：, 公司地址(中)：, 公司地址(英)：, TEL：, FAX：".indexOf(displayname) > -1)) {
								
								if(StringUtils.equals("供應商廠編：", displayname)) {
									JsonElement valueElement = new JsonParser().parse(gson.toJson(suppliermaster.getSuppliercode()));
									jsonObj.add("value", valueElement);
								}else if(StringUtils.equals("統一編號：", displayname)) {
									JsonElement valueElement = new JsonParser().parse(gson.toJson(suppliermaster.getSuppliergui()));
									jsonObj.add("value", valueElement);
								}else if(StringUtils.equals("公司名稱(中)：", displayname)) {
									JsonElement valueElement = new JsonParser().parse(gson.toJson(suppliermaster.getSuppliercname()));
									jsonObj.add("value", valueElement);
								}else if(StringUtils.equals("公司名稱(英)：", displayname)) {
									JsonElement valueElement = new JsonParser().parse(gson.toJson(suppliermaster.getSupplierename()));
									jsonObj.add("value", valueElement);
								}else if(StringUtils.equals("法定代理人：", displayname)) {
									JsonElement valueElement = new JsonParser().parse(gson.toJson(suppliermaster.getContacruser()));
									jsonObj.add("value", valueElement);
								}else if(StringUtils.equals("公司地址(中)：", displayname)) {
									JsonElement valueElement = new JsonParser().parse(gson.toJson(suppliermaster.getSuppliercaddr()));
									jsonObj.add("value", valueElement);
								}else if(StringUtils.equals("公司地址(英)：", displayname)) {
									JsonElement valueElement = new JsonParser().parse(gson.toJson(suppliermaster.getSuppliereaddr()));
									jsonObj.add("value", valueElement);
								}else if(StringUtils.equals("TEL：", displayname)) {
									JsonElement valueElement = new JsonParser().parse(gson.toJson(suppliermaster.getSupplieretel()));
									jsonObj.add("value", valueElement);
								}else if(StringUtils.equals("FAX：", displayname)) {
									JsonElement valueElement = new JsonParser().parse(gson.toJson(""));
									jsonObj.add("value", valueElement);
								}
							}
						}
					}
				}
				result = jsonDataObj.toString();
            }else {
            	System.out.println(" This Json Have Not data ");
            }
		}
		
		return result;
	}
	
	public static List<String> getSingle(List<String> list) {
		List<String> tempList = new ArrayList<String>();          //1,建立新集合
		Iterator<String> it = list.iterator();              	  //2,根據傳入的集合(老集合)獲取迭代器
		while(it.hasNext()) {                					  //3,遍歷老集合
			String str = it.next();               				  //記錄住每一個元素
			if(!tempList.contains(str)) {           			  //如果新集合中不包含老集合中的元素
				tempList.add(str);                				  //將該元素新增
			}
		}  
		
		for(int i = 0 ; i < tempList.size() ; i ++) {
			if(tempList.get(i) != null && tempList.get(i).indexOf("fix0_0") > -1) {
				System.out.println(tempList.get(i));
				tempList.remove(i);
			}
		}
		
		return tempList;
	}
	
	/**
	 * 用json表達式取得值
	 * @param jsonData
	 * @param jsonPath
	 * @return
	 */
	public static String getJsonPathVal(String jsonData, String jsonPath) {
		
		ReadContext json = JsonPath.parse(jsonData);
		List<String> valueList = json.read(jsonPath);
		String result = "";
		
		if(valueList.size() > 0) {
			result = valueList.get(0);
		}
		
		return result;
	}
	
	/**
	 * 用json表達式取得數值形式值
	 * @param jsonData
	 * @param jsonPath
	 * @return
	 */
	public static List<Integer> getJsonPathRowVal(String jsonData, String jsonPath) {
		
		ReadContext json = JsonPath.parse(jsonData);
		List<Integer> valueList = json.read(jsonPath);
		
		return valueList;
	}
	
}