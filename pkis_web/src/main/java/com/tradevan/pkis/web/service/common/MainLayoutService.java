package com.tradevan.pkis.web.service.common;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tradevan.xauthframework.common.bean.ProcessResult;
import com.tradevan.xauthframework.core.utils.XauthPropUtils;
import com.tradevan.xauthframework.web.service.DefaultService;

@Service("MainLayoutService")
@Transactional(rollbackFor=Exception.class)
public class MainLayoutService extends DefaultService {
	
	private static String MANUAL_URL = XauthPropUtils.getKey("main.layout.manualurl");
	
	private Map<String, Object> fileMap = new HashMap<String, Object>();

	public ProcessResult getFiles() throws Exception {
		ProcessResult result = new ProcessResult();
		result.setStatus(ProcessResult.NG);
		File file = new File(MANUAL_URL);
		String[] files = file.list();
		fileMap = new HashMap<String, Object>();
		for(int i = 0 ; i < files.length ; i ++) {
			fileMap.put("li_" + i, files[i]);
		}
		result.addResult("fileData", fileMap);
		result.setStatus(ProcessResult.OK);
		
		return result;
	}
	
	public String getFilePath(String fileNameKey) {
		String fileName = MapUtils.getString(fileMap, fileNameKey);
		return MANUAL_URL + fileName;
	}
}
