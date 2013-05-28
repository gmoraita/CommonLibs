package net.allwebdesign.common.lib.ui.export;

import java.util.Date;


import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
 
/**
 * Builds the report layout, the template, the design, the pattern or whatever synonym you may want to call it.
 *
 * @author George Moraitakis
 */
public class XLLayoutManager {
 
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger("service");
	  
	 /**
	  * Builds the report layout.
	  * <p>
	  * This doesn't have any data yet. This is your template.
	  */
	 public static void buildReport(HSSFSheet worksheet,  String sheetTitle, String[] colTitles, int startRowIndex, int startColIndex) {
		 // Set column widths
		 for(int i =0;i<colTitles.length;i++){
			 worksheet.setColumnWidth(i, 10000);
	
		 }
	  
	   
		  // Build the title and date headers
		  buildTitle(worksheet, sheetTitle,colTitles.length, startRowIndex, startColIndex);
		  // Build the column headers
		  buildHeaders(worksheet, colTitles, startRowIndex, startColIndex);
	 }
	  
	 /**
	  * Builds the report title and the date header
	  *
	  * @param worksheet
	  * @param startRowIndex starting row offset
	  * @param startColIndex starting column offset
	  */
	 public static void buildTitle(HSSFSheet worksheet, String sheetTitle,int colNumber, int startRowIndex, int startColIndex) {
		  // Create font style for the report title
		  Font fontTitle = worksheet.getWorkbook().createFont();
		  fontTitle.setBoldweight(Font.BOLDWEIGHT_BOLD);
		  fontTitle.setFontHeight((short) 280);
		   
		    // Create cell style for the report title
		    HSSFCellStyle cellStyleTitle = worksheet.getWorkbook().createCellStyle();
		    cellStyleTitle.setAlignment(CellStyle.ALIGN_LEFT);
		    cellStyleTitle.setWrapText(true);
		    cellStyleTitle.setFont(fontTitle);
		   
		        // Create report title
		  HSSFRow rowTitle = worksheet.createRow((short) startRowIndex);
		  rowTitle.setHeight((short) 500);
		  HSSFCell cellTitle = rowTitle.createCell(startColIndex);
		  cellTitle.setCellValue(sheetTitle);
		  cellTitle.setCellStyle(cellStyleTitle);
		 
		   
		  // Create merged region for the report title
		  worksheet.addMergedRegion(new CellRangeAddress(0,0,0,colNumber-1));
		   
		  // Create date header
		  HSSFRow dateTitle = worksheet.createRow((short) startRowIndex +1);
		  HSSFCell cellDate = dateTitle.createCell(startColIndex);
		  cellDate.setCellValue("This report was generated at " + new Date());
	 }
	  
	 /**
	  * Builds the column headers
	  *
	  * @param worksheet
	  * @param startRowIndex starting row offset
	  * @param startColIndex starting column offset
	  */
	 public static void buildHeaders(HSSFSheet worksheet, String[] colTitles, int startRowIndex, int startColIndex) {
		  // Create font style for the headers
		  Font font = worksheet.getWorkbook().createFont();
		        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		 
		        // Create cell style for the headers
		  HSSFCellStyle headerCellStyle = worksheet.getWorkbook().createCellStyle();
		  headerCellStyle.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
		  headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		  headerCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		  headerCellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		  headerCellStyle.setWrapText(true);
		  headerCellStyle.setFont(font);
		  headerCellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		   
		  // Create the column headers
		  HSSFRow rowHeader = worksheet.createRow((short) startRowIndex +2);
		  rowHeader.setHeight((short) 500);
		  
		  
		  for (int i=0; i< colTitles.length;i++){
		
			  HSSFCell cell1 = rowHeader.createCell(startColIndex+i);
			  cell1.setCellValue(colTitles[i]);
			  cell1.setCellStyle(headerCellStyle);
		
		
		
		  }
		  
		  
	}
}
