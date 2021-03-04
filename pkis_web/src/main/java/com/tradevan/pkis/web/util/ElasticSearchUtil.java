package com.tradevan.pkis.web.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tradevan.xauthframework.core.utils.XauthPropUtils;
import com.tradevan.xauthframework.dao.bean.GridResult;

@Service("ElasticSearchUtil")
@Transactional(rollbackFor = Exception.class)
public class ElasticSearchUtil {

	private static RestHighLevelClient client = null;
	
	private static String ELASTICSEARCH_TYPE = "_doc";
	
	private static String ELASTICSEARCH_URL = XauthPropUtils.getKey("es.url");
	
	private static String ES_CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
	
	private static String ES_BUILD_SETTING = XauthPropUtils.getKey("es.build.setting");

	/***
	 * ping Elastisearch 是否有正常連線
	 */
	public boolean initESClinet(String ip, String port) {
		try {
			HttpHost host = new HttpHost(ip, Integer.valueOf(port), "http");
			client = new RestHighLevelClient(RestClient.builder(host));
			client.ping(RequestOptions.DEFAULT);

			System.out.println("Connect ElasticSearch Success!!!");

			return true;
		} catch (Exception e) {
			throw new ElasticsearchException("Connect ElasticSearch Exception:" + e.getMessage());
		}
	}

	/**
	 * 關閉連線
	 */
	public void closeESClient() {
		try {
			if (client != null) {
				client.close();
			}
		} catch (Exception e) {
			throw new ElasticsearchException("closeESClient Exception:" + e.getMessage());
		}
	}

	/**
	 * index是否存在
	 */
	public boolean existsIndex(String indexName) {
		boolean isExists = false;

		try {
			GetRequest request = new GetRequest(indexName, "0");
			request.fetchSourceContext(new FetchSourceContext(false));

			client.get(request, RequestOptions.DEFAULT);
			System.out.println("ElasticSearch index Exists!!!");
			isExists = true;
		} catch (Exception e) {
			System.out.println("ElasticSearch index Not Exists!!!");
		}

		return isExists;
	}

	/**
	 * 建立index
	 * 
	 * @param indexName
	 * @param indexMap  (欄位相關設定,可參考buildIndexMapping)
	 * @return
	 */
	public boolean createIndex(String indexName) {
		boolean isCreate = false;

		try {
			CreateIndexRequest request = new CreateIndexRequest(indexName);
			buildSetting(request); // 建立shards和replicas
			client.indices().create(request, RequestOptions.DEFAULT);
			
			System.out.println("createIndex Success!!!");
			isCreate = true;
		} catch (Exception e) {
			throw new ElasticsearchException("createIndex Exception:" + e.getMessage());
		}

		return isCreate;
	}

	public void buildIndexMapping(CreateIndexRequest request) {
		// 文字型別
		Map<String, Object> textMap = new HashMap<>();
		textMap.put("type", "text");

		Map<String, Object> docMap = new HashMap<>();
		Map<String, Object> proMap = new HashMap<>();
		Map<String, Object> msgMap = new HashMap<>();
		msgMap.put("message", textMap);
		proMap.put("properties", msgMap);
		docMap.put("_doc", proMap);

		request.mapping(docMap);
	}
	
	/**
	 * 新增 Json
	 * 
	 * @param indexName, contractNo, jsonData
	 * @param String 
	 * @return Map<String, Object> id && type(_doc)
	 */
	@SuppressWarnings("deprecation")
	public Map<String, Object> insertIndex(String indexName, String contractNo, String jsonData) {
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			IndexRequest request = new IndexRequest(indexName, ELASTICSEARCH_TYPE, contractNo);
			request.source(jsonData, XContentType.JSON);

			IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
			if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
				System.out.println("insertIndex Success!!!");
				result.put("id", indexResponse.getId());
				result.put("type", indexResponse.getType());
			}
		} catch (Exception e) {
			throw new ElasticsearchException("insertIndex Exception:" + e.getMessage());
		}
		return result;
	}

	private void buildSetting(CreateIndexRequest request) {
		request.settings(Settings.builder().put("index.number_of_shards", ES_BUILD_SETTING).put("index.number_of_replicas", 1));
	}

	/**
	 * 查詢 Json
	 * 
	 * @param id, indexName
	 * @param String 
	 * @return 單筆 Json
	 */
	@SuppressWarnings("deprecation")
	public static String serachById(String id, String indexName) {
		HttpClient client = HttpClients.custom().build();
		HttpPost httpPost = new HttpPost(ELASTICSEARCH_URL + indexName + "/_search");
		httpPost.addHeader("Content-Type", ES_CONTENT_TYPE_JSON);
		StringBuffer content = new StringBuffer();
		try {
			String search = "{\"query\":{\"bool\":{\"must\":[{\"query_string\":{\"default_field\":\"_all\",\"query\":\"_id:" + id + "\"}}],\"must_not\":[],\"should\":[]}},\"from\":0,\"size\":10,\"sort\":[],\"aggs\":{}}";

			StringEntity stringEntity = new StringEntity(search, "UTF-8");
			httpPost.setEntity(stringEntity);
			
			HttpResponse response = client.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				List<String> lines = IOUtils.readLines(entity.getContent());
				for (String line : lines) {
					content.append(line);
				}
			} else {
				return null ;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}
		
		return content.toString() ;
	}

	/**
	 * 更新 Json
	 * 
	 * @param id, indexName, jsonData
	 */
	public static void updateById(String id, String indexName, String jsonData) {
		HttpClient client = HttpClients.custom().build();
		HttpPost httpPost = new HttpPost(ELASTICSEARCH_URL + indexName + "/" + ELASTICSEARCH_TYPE + "/" + id + "/_update");
		System.out.println(" Updata URL == " + ELASTICSEARCH_URL + indexName + "/" + ELASTICSEARCH_TYPE + "/" + id + "/_update");
		httpPost.addHeader("Content-Type", ES_CONTENT_TYPE_JSON);
		
		try {
			StringEntity stringEntity = new StringEntity("{ \"doc\" : " + jsonData + " }", "UTF-8");
			System.out.println(" Updata Json == " + "{ \"doc\" : " + jsonData + " }");
			
			httpPost.setEntity(stringEntity);
			HttpResponse response = client.execute(httpPost);
			System.out.println(response.getStatusLine().getStatusCode());
			
			if (response.getStatusLine().getStatusCode() == 200) {
				System.out.println("ES Update Success");
			} else {
				System.out.println("ES Update Fail");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ES Update Fail");
		} finally {

		}
	}
	
	/**
	 * 從elasticsearch合約查詢
	 * @param indexName
	 * @param beginColumnName
	 * @param where
	 * @return
	 */
	public static List<String> searchForES(String indexName, String beginColumnName, String where) throws Exception {
		
		HttpClient client = null;
		HttpPost httpPost = null;
		HttpResponse response = null;
		StringBuffer url = new StringBuffer();
		String result = "";
		List<String> resultList = null;
		try {
			client = HttpClients.custom().build();
			url.append(ELASTICSEARCH_URL + "_sql?sql=");
			url.append("select " + beginColumnName);
			url.append("  from " + indexName.toLowerCase() + "/" + ELASTICSEARCH_TYPE );
			url.append(where);
			httpPost = new HttpPost(url.toString().replaceAll(" ", "%20"));
			httpPost.addHeader("Content-Type", ES_CONTENT_TYPE_JSON);
			response = client.execute(httpPost);
			if(response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "UTF-8");
				if(StringUtils.isNotBlank(result)) {
					resultList = JsonUtil.jsonSkipToList(result);
				}
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		
		return resultList;
	}
	
	/**
	 * 刪除ES資料，by _id
	 * @param indexName
	 * @param idValue
	 */
	public static void deleteById(String indexName, String idValue) {
		
		HttpClient client = null;
		HttpPost httpPost = null;
		HttpResponse response = null;
		StringBuffer url = new StringBuffer();
		try {
			client = HttpClients.custom().build();
			url.append(ELASTICSEARCH_URL + "_sql?sql=");
			url.append("delete from " + indexName.toLowerCase() + "/" + ELASTICSEARCH_TYPE);
			url.append(" where _id = '" + idValue + "'");
			
			httpPost = new HttpPost(url.toString().replaceAll(" ", "%20"));
			httpPost.addHeader("Content-Type", ES_CONTENT_TYPE_JSON);
			response = client.execute(httpPost);
			if(response.getStatusLine().getStatusCode() == 200) {
				System.out.println("ES delete Successful!");
			} else {
				System.out.println("ES delete Fail!");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	   *    清空ES
	 * @param indexName
	 * @param idValue
	 */
	public static void deleteES(String indexName) {
		
		HttpClient client = null;
		HttpPost httpPost = null;
		HttpResponse response = null;
		StringBuffer url = new StringBuffer();
		try {
			client = HttpClients.custom().build();
			url.append(ELASTICSEARCH_URL + "_sql?sql=");
			url.append("delete from " + indexName.toLowerCase() + "/" + ELASTICSEARCH_TYPE);
			
			httpPost = new HttpPost(url.toString().replaceAll(" ", "%20"));
			httpPost.addHeader("Content-Type", ES_CONTENT_TYPE_JSON);
			response = client.execute(httpPost);
			if(response.getStatusLine().getStatusCode() == 200) {
				System.out.println("ES delete Successful!");
			} else {
				System.out.println("ES delete Fail!");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sortPagination(GridResult gridResult, List<Map<String, Object>> datas, Map<String, Object> params) throws Exception {
		
		String sidx = MapUtils.getString(params, "sidx");
		String sord = MapUtils.getString(params, "sord");
		List<Map<String, Object>> pages = new ArrayList<Map<String, Object>>();
		
		// 排序
		if(StringUtils.isNotBlank(sidx) && StringUtils.isNotBlank(sord)) {
			Collections.sort(datas, new Comparator<Map<String, Object>>() {
				public int compare(Map<String, Object> data1, Map<String, Object> data2) {
					String dataSidx1 = MapUtils.getString(data1, sidx);
					String dataSidx2 = MapUtils.getString(data2, sidx);
					if(StringUtils.equals(sord.toLowerCase(), "asc")) {
						return dataSidx1.compareTo(dataSidx2);
					} else {
						return dataSidx2.compareTo(dataSidx1);
					}
				}
			});
		}
		
		// 分頁
		int size = datas.size();
		int total = (int)Math.ceil((double)size / 10);
		int nowPage = gridResult.getPage();
		for(int i = (nowPage - 1) * 10 ; i < size ; i ++) {
			if(i < (nowPage + 1) * 10) {
				pages.add(datas.get(i));
			}
		}
		gridResult.setRows(pages);
		gridResult.setTotal(total);
		gridResult.setRecords(size);
	}
}
