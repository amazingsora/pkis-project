package com.tradevan.apcommon.util;

import java.math.BigInteger;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordUtil {
	
	protected static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

	public static final String DEFAULT_FONT_FAMILY = "標楷體";
	public static final int DEFAULT_FONT_SIZE = 12;
	public static final int DEFAULT_CELL_FONT_SIZE = 12;
	public static final int DEFAULT_CELL_WIDTH = 2880;
	
	public static void addText(XWPFParagraph paragraph,ParagraphAlignment alignment,String Text,boolean isBold) {
		addText(paragraph, alignment, Text , isBold , DEFAULT_FONT_FAMILY, DEFAULT_FONT_SIZE);
	}
	
	public static void addText(XWPFParagraph paragraph,ParagraphAlignment alignment,String Text,boolean isBold , int fontSize) {
		addText(paragraph, alignment, Text , isBold , DEFAULT_FONT_FAMILY, fontSize);
	}
	
	public static void addText(XWPFParagraph paragraph,ParagraphAlignment alignment,String Text,boolean isBold,String fontFamily , int fomtSize) {
	
		if (alignment != null) {
			paragraph.setAlignment(alignment);
		}
		XWPFRun run = paragraph.createRun();
		run.setBold(isBold);
		run.setFontFamily(fontFamily);
		run.setFontSize(fomtSize);
		run.setText(Text);
	}
	
	public static void addTableCellText(XWPFTable table,int rowIndex,int colIndex,ParagraphAlignment alignment,String text,boolean isBold) {
		
		addTableCellText(table, rowIndex, colIndex, alignment, text, isBold, DEFAULT_CELL_WIDTH);
	}
	
	public static void addTableCellText(XWPFTable table,int rowIndex,int colIndex,ParagraphAlignment alignment,String text,boolean isBold,int cellWidth) {
		XWPFTableCell cell = table.getRow(rowIndex).getCell(colIndex);
		cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(cellWidth));
		addText(cell.getParagraphs().get(0) , alignment , text , isBold, DEFAULT_CELL_FONT_SIZE);
	}
	
	//跨列合併儲存格 
    public static void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {    
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {    
            XWPFTableCell cell = table.getRow(row).getCell(cellIndex);    
            if ( cellIndex == fromCell ) {    
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);    
            } else {      
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);    
            }    
        }    
    } 
    //跨行合併儲存格  
    public void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {    
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {    
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);    
            if ( rowIndex == fromRow ) {    
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);    
            } else {      
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);    
            }    
        } 
    }
	public static void addTableCellText(XWPFTable table,int rowIndex,int colIndex,ParagraphAlignment alignment,String text,boolean isBold,int cellWidth, int fomtSize) {
		XWPFTableCell cell = table.getRow(rowIndex).getCell(colIndex);
		cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(cellWidth));
		addText(cell.getParagraphs().get(0) , alignment , text , isBold, fomtSize);
	}
	//換行(將需要換行的字串切成陣列)
	public static void addBreakToText(XWPFTable table,int rowIndex,int colIndex,ParagraphAlignment alignment,String[] texts,boolean isBold,int cellWidth, int fomtSize) {
	
		XWPFTableCell cell = table.getRow(rowIndex).getCell(colIndex);
		cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(cellWidth));

		if (alignment != null) {
			cell.getParagraphs().get(0).setAlignment(alignment);
		}
		XWPFRun run = cell.getParagraphs().get(0).createRun();
		run.setBold(isBold);
		run.setFontFamily(DEFAULT_FONT_FAMILY);
		run.setFontSize(fomtSize);
		for(String text : texts) {
			if(StringUtil.isNotBlank(text)) {
				run.setText(text);
				run.addBreak();		
			}		
		}
	}
}
