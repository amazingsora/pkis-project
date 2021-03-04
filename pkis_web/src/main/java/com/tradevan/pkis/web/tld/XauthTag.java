package com.tradevan.pkis.web.tld;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tradevan.mapper.pkis.dao.BatchparamsetMapper;
import com.tradevan.mapper.pkis.model.Batchparamset;
import com.tradevan.pkis.web.enums.IDEN_TYPE;
import com.tradevan.xauthframework.core.enums.USER_TYPE;
import com.tradevan.xauthframework.core.security.UserContext;
import com.tradevan.xauthframework.core.security.UserInfo;
import com.tradevan.xauthframework.core.utils.XauthPropUtils;
import com.tradevan.xauthframework.dao.CommonDao;
import com.tradevan.xauthframework.web.utils.SpringContext;


public class XauthTag extends SimpleTagSupport implements DynamicAttributes {
	
	final Logger logger = LogManager.getLogger(XauthTag.class);
		
	private String APP_ID = XauthPropUtils.getKey("xauth.appId");
	
	final String XAUTH_DAO_PACKAGE = "com.tradevan.mapper.xauth.dao.XauthMapper.";
	
	final String SUPPLIERMASTER_DAO_PACKAGE = "com.tradevan.mapper.pkis.dao.SuppliermasterMapper.";
	
	final String REVIEWCONF_DAO_PACKAGE = "com.tradevan.mapper.pkis.dao.ReviewconfMapper.";

	Map<String, Object> tagAttributes = new HashMap<String, Object>();
	
	String xauthType;
	
	String idenId;
	
	String gp;
	
	String listKey ;
	
	String listValue ;
	
	String headerKey;
	
	String headerValue;
	
	String required;	
	
	String value;
	
	String disabled;
	
	@SuppressWarnings("serial")
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter out = pageContext.getOut();

		out.print("<select ");
		for (String attrName : tagAttributes.keySet()) {
			out.print(attrName);
			out.print("='");
			out.print(tagAttributes.get(attrName));
			out.print('\'');
		}
		
		if (StringUtils.isNotBlank(disabled)) {
			   out.println(" disabled=\"" + disabled + "\"");
			  }
		out.println(" " + required + ">");

		try {
			CommonDao commonDao = SpringContext.getApplicationContext().getBean(CommonDao.class);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("appId", APP_ID);
			params.put("ids", null);
			List<Map<String, Object>> dataList = null;
			
			UserContext userContext = new UserContext();
			UserInfo userInfo = userContext.getCurrentUser();
			
			if (xauthType.equals("x-menu")) {
				dataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectMenu", params);
			}
			
			if (xauthType.equals("x-iden-qry")) {				
				if (!userInfo.getUserType().equals(USER_TYPE.SYS_ADMIN.getCode())) {
					params.put("idenId", userInfo.getIdenId());
					params.put("userId", userInfo.getUserId());
				}
				if (userInfo.getUserType().equals(USER_TYPE.ADMIN.getCode())) {
					dataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectDeptTree", params);
				}
				else {
					dataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectDept", params);
				}				
			}
			if (xauthType.equals("ALL-x-iden-qry")) {				
				if (!userInfo.getUserType().equals(USER_TYPE.SYS_ADMIN.getCode())) {
					params.put("idenId","00000000" );
					params.put("userId","admin");
				}
				logger.info(userInfo.getIdenId()+"----+++"+userInfo.getUserId());
					dataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectDeptTree", params);				
			}
			
			if (xauthType.equals("x-iden")) {				
				if (!userInfo.getUserType().equals(USER_TYPE.SYS_ADMIN.getCode())) {
					params.put("idenId", userInfo.getIdenId());
					params.put("userId", userInfo.getUserId());
					// 取得登入者公司的上層公司ID
					List<Map<String, Object>> compDataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectDept", params);
					String parentId = compDataList.get(0).get("parentId").toString();
					
					// 取得上層公司, 若有資料,則併在自己公司上層
					params.put("idenId", parentId);
					compDataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectDept", params);
					if (compDataList != null && compDataList.size() > 0) {
						if (userInfo.getUserType().equals(USER_TYPE.ADMIN.getCode())) {
							dataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectDept", params);
							compDataList.addAll(dataList);
							dataList = compDataList;
						}
						else {
							dataList = compDataList;
						}
					}					
					
				}
				else {					
					dataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectDeptTree", params);
				}		
				
				if (dataList != null && dataList.size() == 1) {
					out.print("<option value=''>請選擇</option>");
				}				
			}
			
			if (xauthType.equals("x-iden-i")) {
				if (userInfo.getUserType().equals(USER_TYPE.SYS_ADMIN.getCode())) {					
					dataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectDeptTree", params);
				}
				else if (userInfo.getUserType().equals(USER_TYPE.ADMIN.getCode())) {
					params.put("idenId", userInfo.getIdenId());
					params.put("userId", userInfo.getUserId());
					dataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectDeptTree", params);
				}
				
				if (dataList != null && dataList.size() == 1) {
					out.print("<option value=''>請選擇</option>");
				}
			}
			
			if (xauthType.equals("x-iden-u")) {				
				if (userInfo.getUserType().equals(USER_TYPE.SYS_ADMIN.getCode())) {	
					dataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectDeptTree", params);
				}
				else {
					params.put("idenId", userInfo.getIdenId());
					params.put("userId", userInfo.getUserId());
					if (userInfo.isDeptRoot()) {
						dataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectDeptTree", params);
					}
					else {
						// 取得登入者公司的上層公司ID
						List<Map<String, Object>> compDataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectDept", params);
						String parentId = compDataList.get(0).get("parentId").toString();
						
						// 取得上層公司, 若有資料,則併在自己公司上層
						params.put("idenId", parentId);
						List<Map<String, Object>> parentDataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectDept", params);
						if (parentDataList != null && parentDataList.size() > 0) {
							if (userInfo.getUserType().equals(USER_TYPE.ADMIN.getCode())) {
								params.put("idenId", userInfo.getIdenId());
								dataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectDept", params);
								parentDataList.addAll(dataList);
								dataList = parentDataList;
							}
							else {
								dataList = parentDataList;
							}
						}					
						else {
							dataList = compDataList;
						}
					}
				}
								
				if (dataList != null && dataList.size() == 1) {
					out.print("<option value=''>請選擇</option>");
				}				
			}
			
			if (xauthType.equals("x-iden-root")) {				
				if (userInfo.getUserType().equals(USER_TYPE.SYS_ADMIN.getCode())) {
					dataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectDeptTreeRoot", params);
				}
				else if (userInfo.getUserType().equals(USER_TYPE.ADMIN.getCode()) && userInfo.isDeptRoot()) {
					params.put("idenId", userInfo.getIdenId());
					dataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectDeptTreeRoot", params);
				}
				else {
					dataList = null;
				}				
			}
			
			if (xauthType.equals("x-role")) {
				if (!userInfo.getUserType().equals(USER_TYPE.SYS_ADMIN.getCode())) {
					params.put("idenId", userInfo.getIdenId());					
				}				
				dataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectRole", params);
			}
			
			if (xauthType.equals("x-user-type")) {
				dataList = new ArrayList<Map<String, Object>>();
				if (userInfo.getUserType().equals(USER_TYPE.SYS_ADMIN.getCode())) {
					dataList.add(new HashMap<String, Object>(){ { put("key", USER_TYPE.SYS_ADMIN.getCode()); put("value", USER_TYPE.SYS_ADMIN.getMessage()); } });
					dataList.add(new HashMap<String, Object>(){ { put("key", USER_TYPE.ADMIN.getCode()); put("value", USER_TYPE.ADMIN.getMessage()); } });
				}
				if (userInfo.getUserType().equals(USER_TYPE.ADMIN.getCode())) {
					dataList.add(new HashMap<String, Object>(){ { put("key", USER_TYPE.ADMIN.getCode()); put("value", USER_TYPE.ADMIN.getMessage()); } });
				}				
				dataList.add(new HashMap<String, Object>(){ { put("key", USER_TYPE.USER.getCode()); put("value", USER_TYPE.USER.getMessage()); } });			
			}
			
			if (xauthType.equals("x-iden-type")) {
				dataList = new ArrayList<Map<String, Object>>();							
				IDEN_TYPE[] idenTypes = IDEN_TYPE.values();
				for (IDEN_TYPE idenType : idenTypes) {
					dataList.add(new HashMap<String, Object>(){ { put("key", idenType.getCode()); put("value", idenType.getMessage()); } });
				}
			}
			
			if (xauthType.equals("x-iden-root-ip")) {				
				if (userInfo.getUserType().equals(USER_TYPE.SYS_ADMIN.getCode())) {	
					dataList = new ArrayList<Map<String, Object>>();
					dataList.add(new HashMap<String, Object>(){ { put("idenId", "*"); put("struct", "不限制"); } });
					dataList.addAll(commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectDeptTree", params));
				}
				else if (userInfo.getUserType().equals(USER_TYPE.ADMIN.getCode()) && userInfo.isDeptRoot()) {
					params.put("idenId", userInfo.getIdenId());
					params.put("userId", userInfo.getUserId());
					dataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectDeptTree", params);
				}
				else {
					dataList = null;
				}				
			}
			
			if (xauthType.equals("x-sys-code")) {
				params.put("idenId", idenId);	
				params.put("gp", gp);	
				params.put("sortColumnName", "ORDER_SEQ");
				params.put("sortOrder", "ASC");
				dataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectSysCode", params);
			}

			if(xauthType.equals("x-contract-module")){
				dataList = new ArrayList<Map<String, Object>>();
				dataList.add(new HashMap<String, Object>(){ { put("moduleKey", "SC"); put("moduleValue", "制式"); } });
				dataList.add(new HashMap<String, Object>(){ { put("moduleKey", "NSC"); put("moduleValue", "非制式"); } });
			}
			
			if(xauthType.equals("x-contract-disp")) {
				dataList = new ArrayList<Map<String, Object>>();
				dataList.add(new HashMap<String, Object>(){ { put("dispKey", "disp"); put("dispValue", "後勤全國性合約"); } });
				dataList.add(new HashMap<String, Object>(){ { put("dispKey", "基本資料"); put("dispValue", "基本資料"); } });
			}
			
			if(xauthType.equals("x-contract-suppliercode")) {
				dataList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> data = commonDao.queryListByClass(Map.class, SUPPLIERMASTER_DAO_PACKAGE + "selectSuppliercodeList", params);
				for(Map<String, Object> dataMap : data) {
					String supplierCode = MapUtils.getString(dataMap, "suppliercode");
					dataList.add(new HashMap<String, Object>(){ { put("supplierCode", supplierCode); put("supplierValue", supplierCode); } });
				}
			}
			
			if(xauthType.equals("x-reviewconf")) {
				dataList = commonDao.queryListByClass(Map.class, REVIEWCONF_DAO_PACKAGE + "selectContractReviewList", null);
				
			}
			//報表統計選單
			if (xauthType.equals("x-statisticalreport-type")) {
				params.put("gp", gp);	
				params.put("sortColumnName", "ORDER_SEQ");
				params.put("sortOrder", "ASC");
				dataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectSysCode", params);
			}
			
			if(xauthType.equals("x-batch-param")) {
				logger.info("x-batch-param#start");
				dataList = commonDao.queryListByClass(Map.class, XAUTH_DAO_PACKAGE + "selectBatch", null);
				logger.info("dataList === " + dataList);
			}
			
			
			if (dataList != null && dataList.size() > 0) {
				if (StringUtils.isNotBlank(headerKey)) {
					if (StringUtils.isBlank(headerValue)) {
						headerValue = "";
					}
					out.print("<option value=" + headerValue + ">" + headerKey + "</option>");
				}
				else if (dataList.size() > 1) {
					out.print("<option value=''>請選擇</option>");
				}
				for (Map<String, Object> data : dataList) {
					out.print("<option value='" + data.get(listKey) + "' ");
					if (StringUtils.isNotBlank(value)) {
						if (value.equals(data.get(listKey))) {
							out.print("selected=\"selected\"");
						}
					}					
					out.print(">");	

					if(gp!=null&&gp.equals("DEPT_CODE")) {
						out.print(data.get(listKey)+"-");

						}
					out.print(data.get(listValue));

					out.print("</option>");
				}
			}	
			else {
				if (StringUtils.isNotBlank(headerKey)) {
					if (StringUtils.isBlank(headerValue)) {
						headerValue = "";
					}
					out.print("<option value=" + headerValue + ">" + headerKey + "</option>");
				}
				else {
					out.print("<option value=''>請選擇</option>");
				}				
			}
		} catch (Exception e) {
			logger.error(e, e);
		}

		out.println("</select>");
	}

	@Override
	public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
		if (!localName.equalsIgnoreCase("mapper") 
				|| !localName.equalsIgnoreCase("sql") 
				|| !localName.equalsIgnoreCase("listKey")
				|| !localName.equalsIgnoreCase("listValue")
				|| !localName.equalsIgnoreCase("headerKey")
				|| !localName.equalsIgnoreCase("headerValue")
				|| !localName.equalsIgnoreCase("method")) {
			tagAttributes.put(localName, value);
		}	
	}
	
	public String getXauthType() {
		return xauthType;
	}

	public void setXauthType(String xauthType) {
		this.xauthType = xauthType;
	}
	
	public String getIdenId() {
		return idenId;
	}

	public void setIdenId(String idenId) {
		this.idenId = idenId;
	}

	public String getGp() {
		return gp;
	}

	public void setGp(String gp) {
		this.gp = gp;
	}

	public String getListKey() {
		return listKey;
	}

	public void setListKey(String listKey) {
		this.listKey = listKey;
	}

	public String getListValue() {
		return listValue;
	}

	public void setListValue(String listValue) {
		this.listValue = listValue;
	}

	public String getHeaderKey() {
		return headerKey;
	}

	public void setHeaderKey(String headerKey) {
		this.headerKey = headerKey;
	}

	public String getHeaderValue() {
		return headerValue;
	}

	public void setHeaderValue(String headerValue) {
		this.headerValue = headerValue;
	}

	public String getRequired() {
		return required;
	}

	public void setRequired(String required) {
		this.required = required;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDisabled() {
		return disabled;
	}

	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}	
	
	
	
}
