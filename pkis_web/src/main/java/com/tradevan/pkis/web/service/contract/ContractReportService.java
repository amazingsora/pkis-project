package com.tradevan.pkis.web.service.contract;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfWriter;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.tradevan.mapper.xauth.dao.XauthSysCodeMapper;
import com.tradevan.xauthframework.common.utils.FileUtils;
import com.tradevan.xauthframework.web.service.DefaultService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service("ContractReportService")
@Transactional(rollbackFor = Exception.class)
public class ContractReportService extends DefaultService {
	
	@Autowired
	XauthSysCodeMapper xauthSysCodeMapper;
	
	@Autowired
	ContractService contractService;
	
	public static String tag="";
	static String suppliercode="";
	static String deptno="";

	List<String> filter = Arrays.asList(
			"基本資料",
			"明細資料",
			"附件資料",
			"審核評估",
			"審核意見",
			"0.前言",
			"Other_Agreements其他同意條款"
	);
	
//	List<String> filter = Arrays.asList("1.Definition定義", "2.Order_and_Delivery商品訂貨與交付", "3.SHORTAGE_PENALTY缺貨罰款",
//			"4.Return退貨", "5.Product_Bar_Code商品條碼之交付", "6.Approval_Standard驗收標準", "7.Receiving_Location收貨地點",
//			"8.Standard_Pallets標準棧板規格", "9.Payment付款", "10.DCFees物流費用", "11.Delivery_Optimization最佳化配送",
//			"12.Pick_Up_Service到倉取貨", "13.Supplier_Service_Scorecard", "14.Other_Conditions其他條款");
	JsonObject root;
	List<String>shortagepenalty=new LinkedList<String>();
	Map<String, Object> params = new HashMap<String, Object>();

	List<Map<String, Object>> details = new ArrayList<Map<String, Object>>();
	
	Document document;
	PdfWriter writer;
	Font font12 ;
	Font font10 ;
	Font font12Red;
	Font font10Red;
	
	public static void main(String[] args) throws JRException {
		ContractReportService report = new ContractReportService();
		try {
			report.build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 將新增合約內容總成pdf
	 * @param pdfValueMap
	 * @throws Exception 
	 */
	public void createPdf(Map<String, Object> pdfValueMap, String filePath, String resultFileName, String fileName, String modelName) throws Exception {
		
//		String[] fileNameOri = StringUtils.split(fileName, ".");
		String newFileName = filePath + "/" + resultFileName;
		logger.info("newFileName ==="+newFileName);
		
		// 變數資料準備
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("EN_MODULE_NAME", pdfValueMap.get("EN_MODULE_NAME"));	   	// 合約英文名稱
		dataMap.put("CH_MODULE_NAME", pdfValueMap.get("CH_MODULE_NAME"));      	// 合約中文名稱
		dataMap.put("MODULE_YEAR", pdfValueMap.get("MODULE_YEAR"));        		// 合約年度
		dataMap.put("LEGAL_REP", pdfValueMap.get("LEGAL_REP"));		    		// 法定代理人
		dataMap.put("EN_ADDRESS", pdfValueMap.get("EN_ADDRESS"));				// 英文地址
		dataMap.put("CH_ADDRESS", pdfValueMap.get("CH_ADDRESS"));				// 中文地址
		dataMap.put("PHONE_NUM", pdfValueMap.get("PHONE_NUM"));					// 電話
		dataMap.put("FAX_NUM", pdfValueMap.get("FAX_NUM"));						// 傳真
		dataMap.put("VALID_BGN_DATE", pdfValueMap.get("VALID_BGN_DATE"));		// 有效期間起日
		dataMap.put("VALID_END_DATE", pdfValueMap.get("VALID_END_DATE"));		// 有效期間訖日
		dataMap.put("DIVISION", pdfValueMap.get("DIVISION"));					// 處(類)別
		dataMap.put("DEPT_NO", pdfValueMap.get("DEPT_NO"));						// 課別
		dataMap.put("SUPP_NAME", pdfValueMap.get("SUPP_NAME"));					// 供應商全名
		dataMap.put("SUPP_CODE", pdfValueMap.get("SUPP_CODE"));					// 供應商編號
		dataMap.put("TAX_NUM", pdfValueMap.get("TAX_NUM"));						// 統一編號
		dataMap.put("EXE_YEAR", pdfValueMap.get("EXE_YEAR"));					// 簽約日 - 年
		dataMap.put("EXE_MON", pdfValueMap.get("EXE_MON"));						// 簽約日 - 月
		dataMap.put("EXE_DAY", pdfValueMap.get("EXE_DAY"));						// 簽約日 - 日
		dataList.add(dataMap);
		
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(dataList);
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String contractReportPath = request.getServletContext().getRealPath("/") + "/WEB-INF/report/cover/contractReport.jasper";
//		JasperReport rpt = (JasperReport)JRLoader.loadObject(this.getClass().getResourceAsStream("/report/cover/contractReport.jasper"));
		JasperPrint jasperPrint = JasperFillManager.fillReport(contractReportPath, new HashMap<String, Object>(), ds);
		JRPdfExporter  exporter = new JRPdfExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		SimpleOutputStreamExporterOutput output = new SimpleOutputStreamExporterOutput(filePath + "/temp.pdf");
		exporter.setExporterOutput(output);
		exporter.exportReport();
		
		String files[] = {filePath + "/temp.pdf", filePath + "/" + fileName};
		mergePdf(files, newFileName);
		
		if(output != null) {
			output.close();
		}
		File fileTtemp = new File(filePath + "/temp.pdf");	
		FileUtils.forceDelete(fileTtemp);
		if(StringUtils.equals(modelName, "制式合約")) {
			File fileSscTemp = new File(filePath + "/scTemp.pdf");	
			FileUtils.forceDelete(fileSscTemp);
		}
	}
	
	public void mergePdf(String[] files, String result) throws Exception {
		
		Document document = null;
		PdfSmartCopy pdfCopy = null;
		PdfReader reader = null;
		FileOutputStream outputStream = new FileOutputStream(result);
		
		document = new Document();
		pdfCopy = new PdfSmartCopy(document, outputStream);
		document.open();
		for(int i = 0 ; i < files.length ; i ++) {
			reader = new PdfReader(files[i]);
			pdfCopy.addDocument(reader);
			pdfCopy.freeReader(reader);
			if(reader != null) {
				reader.close();
			}
		}
		if(document != null) {
			document.close();
		}
		if(pdfCopy != null) {
			pdfCopy.close();
		}
		if(reader != null) {
			reader.close();
		}
		if(outputStream != null) {
			outputStream.close();
		}
	}
	
	public void createScPdf(String resultPath, String resultFileName, String jsonData, Map<String, Object> pdfValueMap, String modelName) throws Exception {
		logger.info("createScPdf#start");
		
		String tempFileName = "scTemp.pdf";
		try {
			read(jsonData);
			init(resultPath, tempFileName);
			buildTitle();
			buildDataType();
			buildOtherAgreements();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (document != null)
				document.close();
			String newFileName = resultPath + "/" + resultFileName;
			logger.info("newFileName"+newFileName);

			createPdf(pdfValueMap, resultPath, resultFileName, tempFileName, modelName);
		}
	}
	
	public void createScPdf(String resultPath, String resultFileName, String jsonData) throws Exception {
		try {
			read(jsonData);
			init(resultPath, resultFileName);
			buildTitle();
			buildDataType();
			buildOtherAgreements();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (document != null)
				document.close();
		}
	}
	
	
	public void read() throws Exception {
		try(BufferedReader br = new BufferedReader(new FileReader("json.txt"))) {
			root = new JsonParser().parse(br.readLine()).getAsJsonObject();
		}
	}
	
	public void read(String jsonData) throws Exception {
		root = new JsonParser().parse(jsonData).getAsJsonObject();
		ReadContext json = JsonPath.parse(jsonData);
		shortagepenalty = new LinkedList<String>();
		List<Map<String, Object>> basicDataList = json.read("$.data[?(@.datatype == '基本資料')].docdetail[?(@.resultdata)].resultdata");
		int i = 0;
		boolean flag=true;
		if(basicDataList.size() > 0) {
			Map<String, Object> map = basicDataList.get(0);
			while(flag) {
				if(map.containsKey("Shortagepenalty_" + i)) {
					shortagepenalty.add((String) map.get("Shortagepenalty_"+i));
					i++;
				}else {
					flag = false;
				}
			}
			suppliercode = MapUtils.getString(basicDataList.get(0), "供應商廠編");
			deptno = MapUtils.getString(basicDataList.get(0), "課別");
		}
	}
	
	public JsonObject findDataType(String datatype) {
		JsonArray jsons = root.get("data").getAsJsonArray();
		int len = jsons.size();
		for (int i = 0; i < len; i++) {
			if (((JsonObject) jsons.get(i)).get("datatype").getAsString().equals(datatype)) {
				return (JsonObject) jsons.get(i);
			}
		}
		return null;
	}

	void buildTitle() throws Exception {
		JsonObject json = findDataType("0.前言");
		JsonArray jsons = json.get("docdetail").getAsJsonArray();
		int len = jsons.size();
		
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);
		PdfPCell cell = new PdfPCell();
		cell.setPadding(5);
		
		for (int i = 0; i < len; i++) {
			JsonElement datatype = ((JsonObject) jsons.get(i)).get("uitype");
			JsonElement col = ((JsonObject) jsons.get(i)).get("col");
			JsonElement displayname = ((JsonObject) jsons.get(i)).get("displayname");
			JsonElement fontcolor = ((JsonObject) jsons.get(i)).get("fontcolor");
			if (datatype != null && (datatype.getAsString().equals("label") || datatype.getAsString().equals("field"))) {
				if (col.getAsInt() == 0) {
					if(fontcolor != null && (StringUtils.equals(fontcolor.getAsString(), "red") || StringUtils.equals(fontcolor.getAsString(), "red_title")))
						document.add(new Paragraph(displayname.getAsString().replaceAll("\\.n", "\\.\n"), font12Red));
					else
						document.add(new Paragraph(displayname.getAsString().replaceAll("\\.n", "\\.\n"), font12));
					document.add(new Paragraph("\n"));
				} else {
					if(fontcolor != null && StringUtils.equals(fontcolor.getAsString(), "red"))
						cell.addElement(new Paragraph(displayname.getAsString().replaceAll("\\.n", "\\.\n"), font10Red));
					else if(fontcolor != null && StringUtils.equals(fontcolor.getAsString(), "red_title"))
						document.add(new Paragraph(displayname.getAsString().replaceAll("\\.n", "\\.\n"), font12Red));
					else
						cell.addElement(new Paragraph(displayname.getAsString().replaceAll("\\.n", "\\.\n"), font10));
				}
			}
		}
		
		table.addCell(cell);
		document.add(table);
	}

	void  buildDataType() throws Exception {
		
		JsonArray jsons = root.get("data").getAsJsonArray();
		int len = jsons.size();
		for (int i = 0; i < len; i++) {
			JsonElement datatype = ((JsonObject) jsons.get(i)).get("datatype");
			//插入錨點3.2結束 3.3前插入表格
			if (datatype != null && !datatype.getAsString().equals("") && !filter.contains(datatype.getAsString())) {
				if(datatype.getAsString().contains("3.SHORTAGE_PENALTY缺貨罰款")&&!tag.equals("e")) {
					tag="3.2";
				}
				else tag="";

				document.add(new Paragraph("\n", font12));
				PdfPTable table = new PdfPTable(3);
				table.setWidthPercentage(100);
				table.setWidths(new float[]{1, 1.25f, 15});
				buildDetail(table, (JsonObject) jsons.get(i));
				document.add(table);
			}
		}
	}

	void buildDetail(PdfPTable table, JsonObject json) throws Exception {
		
		JsonArray jsons = json.get("docdetail").getAsJsonArray();
		int len = jsons.size();
		int pCol = 9 ;
		int pRow = 0 ;
		//		//
		String tableName = "" ;
		PdfPTable subTable = null ;
		boolean hasOne = false ;
		for(int i = 0; i < len; i++) {
			JsonElement segmentation = ((JsonObject) jsons.get(i)).get("segmentation");
			JsonElement data = ((JsonObject) jsons.get(i)).get("data");
			JsonElement uitype = ((JsonObject) jsons.get(i)).get("uitype");
			JsonElement row = ((JsonObject) jsons.get(i)).get("row");
			JsonElement col = ((JsonObject) jsons.get(i)).get("col");
			JsonElement value = ((JsonObject) jsons.get(i)).get("value");
			JsonElement remark = ((JsonObject) jsons.get(i)).get("remark");
			JsonElement id = ((JsonObject) jsons.get(i)).get("id");
			JsonElement fontcolor = ((JsonObject) jsons.get(i)).get("fontcolor");
			
			// 版本比對標題用
			if(id != null && StringUtils.equals(id.getAsString(), "editMoreTitleid")) {
				PdfPCell cell = new PdfPCell();
				cell.setColspan(3);
				cell.setBorderWidth(0);
//				cell.setPadding(5);
				cell.addElement(new Paragraph(((JsonObject)jsons.get(i)).get("orgString").getAsString(), font12Red));
				table.addCell(cell);
			}
			
			// 標題
			if(segmentation != null && StringUtils.equals(segmentation.getAsString(), "title")) {
				PdfPCell cell = new PdfPCell();
				cell.setColspan(3);
				cell.setPadding(5);
				//去除大標底線
				if(fontcolor != null && StringUtils.equals(fontcolor.getAsString(), "red")) {
					cell.addElement(new Paragraph(data.getAsString().replaceAll("_", " "), font12Red));
				} else {
					cell.addElement(new Paragraph(data.getAsString().replaceAll("_", " "), font12));
				}
				table.addCell(cell);
			}
			
			if (uitype != null && (uitype.getAsString().equals("label") || uitype.getAsString().equals("checkbox") || uitype.getAsString().equals("field"))) {
				int iRow = row.getAsInt();//9
				int iCol = col.getAsInt();//1
				
				// 改變字體顏色
				Font textFont = null;
				if(fontcolor != null && StringUtils.equals(fontcolor.getAsString(), "red")) {
					textFont = font10Red;
				} else if(fontcolor != null && StringUtils.equals(fontcolor.getAsString(), "red_title")){
					textFont = font12Red;
				} else {
					textFont = font10;
				}
				
				if(iRow != pRow || (iRow == pRow && iCol <= pCol ) ) {
					
					if(remark != null && StringUtils.isNotBlank(remark.getAsString()) && remark.getAsString().contains("table") ) {

						if(!StringUtils.equals(tableName, remark.getAsString())) {
							
							if( (hasOne == false || pCol == 1) && cells.size() == 3) {
								cells.get(1).setColspan(2);
								cells.get(1).setBorderWidthRight(0.5f);
								cells.remove(2);
							}
							for(int j = 0 ; j < cells.size() ; j ++) {
								table.addCell(cells.get(j));
							}
							cells.removeAll(cells);
							
							String subRemarkTemp = "";
							int cols = 1 ;
							for(int j = i + 1 ; j < len ; j ++) {
								JsonElement rowTmp = ((JsonObject) jsons.get(j)).get("row");
								JsonElement remarkTmp = ((JsonObject) jsons.get(j)).get("remark");
								if(remarkTmp != null && remarkTmp.getAsString().contains("table") ) {
									if(iRow != rowTmp.getAsInt() && subTable == null && !StringUtils.equals(subRemarkTemp, remarkTmp.getAsString()) ) {
										subTable = new PdfPTable(cols);
										PdfPCell cell = new PdfPCell();
										cell.addElement(new Chunk(((JsonObject)jsons.get(i)).get("orgString").getAsString(), textFont));
										if(fontcolor != null && StringUtils.equals(fontcolor.getAsString(), "red")) 
											cell.setBorderColor(BaseColor.RED);
										subTable.addCell(cell);
										subRemarkTemp = remarkTmp.getAsString();
									} else {
										cols ++ ;
									}
								}
							}
							
							for(int j = i + 1 ; j < len ; j ++) {
								JsonElement remarkTmp = ((JsonObject) jsons.get(j)).get("remark");
								if(remarkTmp != null && remarkTmp.getAsString().contains("table") && StringUtils.equals(subRemarkTemp, remarkTmp.getAsString()) ) {
									PdfPCell cell = new PdfPCell();
									cell.addElement(new Chunk(((JsonObject)jsons.get(j)).get("orgString").getAsString(), textFont));
									if(fontcolor != null && StringUtils.equals(fontcolor.getAsString(), "red")) 
										cell.setBorderColor(BaseColor.RED);
									subTable.addCell(cell);
									subRemarkTemp = remarkTmp.getAsString();
								}
							}
							
							PdfPCell cell = new PdfPCell();
							cell.setBorderWidthTop(0);
							cell.setBorderWidthBottom(0);
							cell.setColspan(3);
							cell.addElement(subTable);
							table.addCell(cell);
							subTable = null ;
							
						}
						tableName = remark.getAsString();
						continue ;
					}
					
					if( (hasOne == false || pCol == 1) && cells.size() == 3) {
						cells.get(1).setColspan(2);
						cells.get(1).setBorderWidthRight(0.5f);
						cells.remove(2);
					}
					for(int j = 0 ; j < cells.size() ; j ++) {
						table.addCell(cells.get(j));
					}
					buildRow();
					hasOne = false ;
				} 
				
				if(iCol == 2 || (iCol == 3 && pCol != 1)) {
					hasOne = true ;
				}
				
				if(id.getAsString().contains("btn0_")) {
					continue;
				}
				
				Chunk checkbox;
				
				//20201023-by Max
				if(StringUtils.isBlank(value.getAsString()) || value.getAsBoolean() == false) {
					checkbox = new Chunk("□", textFont);
				}else {
					checkbox = new Chunk("■", textFont);
				}
//				Chunk checkbox = new Chunk(StringUtils.isBlank(value.getAsString()) ? "□" : "■", font10);
				//尋找3.3插入前
				if(tag.equals("3.2")) {
					if(((JsonObject)jsons.get(i)).get("displayname").getAsString().contains("3.3")){
						tag="insert";
					}
				}
				//插入表格
				if(tag.equals("insert")) {
//					QueryWrapper<XauthSysCode> deptnoWrapper=new QueryWrapper<XauthSysCode>();
//					deptnoWrapper.eq("CODE", deptno);
					PdfPTable basicTable = new PdfPTable(2);
					PdfPCell oneCell=null;
//					Map<String, Object> params=new HashMap<String, Object>();
//					params.put("SUPPLIERCODE", suppliercode);
//					params.put("DEPTNO", deptno);
//					List <String> list=contractService.selectShortagepenalty(params,"轉換List");
					if (shortagepenalty.size() > 1) {
						oneCell=new PdfPCell();
						oneCell.setColspan(2);
						oneCell.setPhrase(new Paragraph("Department 課別："+deptno,font10));
						basicTable.addCell(oneCell);
						basicTable.addCell(new PdfPCell(new Paragraph("當月到貨率%" +"\n"+ "current month arrival rate %",font10)));
						basicTable.addCell(new PdfPCell(new Paragraph("懲罰性違約金%" + "\n"+ "shortage penalty%",font10)));
						for (int i2 = 0; i2 < shortagepenalty.size(); i2++) {
							String action = shortagepenalty.get(i2);
							String[] result = action.split("\\|");
							if (i2 != shortagepenalty.size() - 1) {
								if(result.length != 0) {
									if(i2 == 0) {
										basicTable.addCell(new PdfPCell(new Paragraph("≧ "+result[0]+"% ~ "+result[1]+"%",font10)));
									}else {
										basicTable.addCell(new PdfPCell(new Paragraph("≧ "+result[0]+"% ~ < "+result[1]+"%",font10)));
									}
									basicTable.addCell(new PdfPCell(new Paragraph(result[2]+"%",font10)));
								}else {
									if(i2 == 0) {
										basicTable.addCell(new PdfPCell(new Paragraph("≧   % ~   %",font10)));
									}else {
										basicTable.addCell(new PdfPCell(new Paragraph("≧   % ~ <   %",font10)));
									}
									basicTable.addCell(new PdfPCell(new Paragraph("  %",font10)));
								}
							} else {
								oneCell=new PdfPCell();
								oneCell.setColspan(2);
								oneCell.setPhrase(new Paragraph("雙方確認供應商前一年到貨率為"+result[0]+"%"+"\n" +"Both parties confirmed "+result[0]+"% as Supplier’s arrival rate in last year",font10));
								basicTable.addCell(oneCell);
							}
						}
						Chunk underline = new Chunk("  ", font10);
						underline.setUnderline(0.1f, -2f);
						cells.get(0).getColumn().addText(underline);
						cells.get(1).getColumn().addElement(basicTable);
						cells.get(2).getColumn().addText(underline);

					}
					tag = "e";
					i--;
					continue;
					
				}
				
				boolean isUnderline = isUnderline(json.toString(), id.getAsString());
				
				if(iCol >= 2 && hasOne == false) {
					if(uitype.getAsString().equals("checkbox")) {
						cells.get(1).getColumn().addText(checkbox);
						cells.get(1).getColumn().addText(new Chunk(getContentReplace(((JsonObject)jsons.get(i)).get("displayname").getAsString()), textFont));
					} else {
						if(!uitype.getAsString().equals("field")) {
							cells.get(1).getColumn().addText(new Chunk(getContentReplace(((JsonObject)jsons.get(i)).get("orgString").getAsString()), textFont));
						}
						if(value != null && StringUtils.isNotEmpty(value.getAsString())) {
							Chunk underline = new Chunk("  " + value.getAsString() + "  ", textFont);
							underline.setUnderline(0.1f, -2f);
							cells.get(1).getColumn().addText(underline);
						}else {
							if(isUnderline) {
								Chunk underline = new Chunk("____", textFont);
								cells.get(1).getColumn().addText(underline);
							}
						}
					}
				} else if(iCol >= 2) {
					if(uitype.getAsString().equals("checkbox")) {
						cells.get(2).getColumn().addText(checkbox);
						cells.get(2).getColumn().addText(new Chunk(getContentReplace(((JsonObject)jsons.get(i)).get("displayname").getAsString()), textFont));
					} else {
						if(!uitype.getAsString().equals("field")) {
							cells.get(2).getColumn().addText(new Chunk(getContentReplace(((JsonObject)jsons.get(i)).get("orgString").getAsString()), textFont));
						}
						if(value != null && StringUtils.isNotEmpty(value.getAsString())) {
							Chunk underline = new Chunk("  " + value.getAsString() + "  ", textFont);
							underline.setUnderline(0.1f, -2f);
							cells.get(2).getColumn().addText(underline);
						}else {
							if(isUnderline) {
								Chunk underline = new Chunk("____", textFont);
								cells.get(2).getColumn().addText(underline);
							}
						}
					}
				} else {
					if(uitype.getAsString().equals("checkbox")) {
						cells.get(iCol).getColumn().addText(checkbox);
						cells.get(iCol).getColumn().addText(new Chunk(getContentReplace(((JsonObject)jsons.get(i)).get("displayname").getAsString()), textFont));
					} else {
						if(!uitype.getAsString().equals("field")) {
							cells.get(iCol).getColumn().addText(new Chunk(getContentReplace(((JsonObject)jsons.get(i)).get("orgString").getAsString()), textFont));
						}
						if(value != null && StringUtils.isNotEmpty(value.getAsString())) {
							Chunk underline = new Chunk("  " + value.getAsString() + "  ", textFont);
							underline.setUnderline(0.1f, -2f);
							cells.get(iCol).getColumn().addText(underline);
						}else{
							if(isUnderline) {
								Chunk underline = new Chunk("____", textFont);
								cells.get(iCol).getColumn().addText(underline);
							}
						}
					}
				}
				pRow = iRow ;
				pCol = iCol;
			}
		}
		//for循環結束
		if( (hasOne == false || pCol == 1) && cells.size() == 3) {
			cells.get(1).setColspan(2);
			cells.get(1).setBorderWidthRight(0.5f);
			cells.remove(2);
		}
		for(int j = 0 ; j < cells.size() ; j ++) {
//			cells.get(j).setBorder(0);
			cells.get(j).setBorderWidthBottom(0.5f);
			table.addCell(cells.get(j));
		}
		buildRow();
		for(int j = 0 ; j < cells.size() ; j ++) {
//			cells.get(j).setBorder(0);
			cells.get(j).setBorderWidthBottom(0.5f);
			table.addCell(cells.get(j));
		}
	}
	
	List<PdfPCell> cells =  new ArrayList<PdfPCell>();
	void buildRow() {
		cells = new ArrayList<PdfPCell>();
		PdfPCell cell = new PdfPCell();
		cell.setBorderWidthRight(0);
		cell.setBorderWidthTop(0);
		cell.setPadding(2);
		cell.setBorderWidthBottom(0);
		cells.add(cell);
		
		cell = new PdfPCell();
		cell.setBorderWidthRight(0);
		cell.setBorderWidthLeft(0);
		cell.setBorderWidthTop(0);
		cell.setPadding(2);
		cell.setBorderWidthBottom(0);
		cells.add(cell);
		
		cell = new PdfPCell();
		cell.setBorderWidthLeft(0);
		cell.setBorderWidthTop(0);
		cell.setPadding(2);
		cell.setBorderWidthBottom(0);
		cells.add(cell);
	}
	
	void buildOtherAgreements() throws Exception {
		JsonArray jsons = root.get("data").getAsJsonArray();
		int len = jsons.size();
		for(int i = 0 ; i < len ; i ++) {
			JsonElement dataType = ((JsonObject)jsons.get(i)).get("datatype");
			JsonArray docdetailArray = ((JsonObject)jsons.get(i)).get("docdetail").getAsJsonArray();
			if(dataType != null && StringUtils.equals(dataType.getAsString(), "Other_Agreements其他同意條款")) {
				document.add(new Paragraph("\n", font12));
				PdfPTable table = new PdfPTable(2);
				table.setWidthPercentage(100);
				for(int j = 0 ; j < docdetailArray.size() ; j ++) {
					JsonElement segmentation = ((JsonObject) docdetailArray.get(j)).get("segmentation");
					JsonElement data = ((JsonObject) docdetailArray.get(j)).get("data");
					JsonElement uitype = ((JsonObject)docdetailArray.get(j)).get("uitype");
					// 標題
					if(segmentation != null && StringUtils.equals(segmentation.getAsString(), "title")) {
						PdfPCell titleCell = new PdfPCell();
						titleCell.setColspan(3);
						titleCell.setPadding(5);
						//去除大標底線
						titleCell.addElement(new Paragraph(data.getAsString().replaceAll("_", " "), font12));
						table.addCell(titleCell);
					}
					if(uitype != null && (StringUtils.equals(uitype.getAsString(), "label") || StringUtils.equals(uitype.getAsString(), "field"))) {
						PdfPCell subCell = new PdfPCell();
						subCell.setPadding(5);
						subCell.setMinimumHeight(250f);
						subCell.addElement(new Paragraph(((JsonObject)docdetailArray.get(j)).get("displayname").getAsString(), font12));
						subCell.addElement(new Paragraph(((JsonObject)docdetailArray.get(j)).get("value").getAsString(), font10));
						table.addCell(subCell);
					}
				}
				document.add(table);
			}
		}
		
	}

	public void init(String resultPath, String tempFileName) throws Exception {
		File file = new File(resultPath);
		if(!file.exists()) {
			file.mkdirs();
		}
		document = new Document(PageSize.A4);
		writer = PdfWriter.getInstance(document, new FileOutputStream(resultPath + "/" + tempFileName));
		document.open();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String fontPath = request.getServletContext().getRealPath("/") + "/WEB-INF/report/fonts/KAIU.TTF";
		BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		font12 = new Font(baseFont, 12, Font.NORMAL);
		font10 = new Font(baseFont, 10, Font.NORMAL);
		font12Red = new Font(baseFont, 12, Font.NORMAL, BaseColor.RED);
		font10Red = new Font(baseFont, 10, Font.NORMAL, BaseColor.RED);
	}
	
	public void init() throws Exception {
		document = new Document(PageSize.A4);
		writer = PdfWriter.getInstance(document, new FileOutputStream("itext.pdf"));
		document.open();
		
		BaseFont baseFont = BaseFont.createFont("C:\\Windows\\Fonts\\kaiu.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		
		font12 = new Font(baseFont, 12, Font.NORMAL);
		font10 = new Font(baseFont, 10, Font.NORMAL);
		font12Red = new Font(baseFont, 12, Font.NORMAL, BaseColor.RED);
		font10Red = new Font(baseFont, 10, Font.NORMAL, BaseColor.RED);
	}
	
	public void build() throws Exception {
		try {
			read();
			init();
			buildTitle();
			buildDataType();
			buildOtherAgreements();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (document != null)
				document.close();
		}

	}
	/**
	 * 取的瀏覽檔案位置
	 * 
	 * @param resultPath
	 * @param resultFileName
	 * @param jsonData
	 * @param pdfValueMap
	 * @param modelName
	 * @return
	 */
	public String getcreateScPdf(String resultPath, String resultFileName, String jsonData, Map<String, Object> pdfValueMap, String modelName) throws Exception {
		String downloadPath = "";
		logger.info("createScPdf#start");
		String tempFileName = "scTemp.pdf";
		try {
			read(jsonData);
			init(resultPath, tempFileName);
			buildTitle();
			buildDataType();
			buildOtherAgreements();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (document != null)
				document.close();
			downloadPath = resultPath + "/" + resultFileName;
			createPdf(pdfValueMap, resultPath, resultFileName, tempFileName, modelName);
		}
		return downloadPath;
	}
	
	/**
	 * 判斷是否加底線
	 * @param jsonData
	 * @param id
	 * @return
	 */
	public boolean isUnderline(String jsonData, String id) {
		boolean result = false;
		ReadContext json = JsonPath.parse(jsonData);
		List<Map<String, Object>> dataList = json.read("$.docdetail[?(@.data_id == '" + id + "')]");
		if(dataList != null && dataList.size() > 0) {
			result = true;
		}
		
		return result;
	}
	
	/**
	 * 處理合約內容多餘html文字
	 * @param content
	 * @return
	 */
	public String getContentReplace(String content) {
		content = content.replaceAll("&nbsp;", "");
		content = content.replaceAll("&nbsp", "");
		content = content.replaceAll("&amp;", "&");
		
		return content;
	}

}