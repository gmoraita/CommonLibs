package net.allwebdesign.common.lib.ui.export;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.CellStyle;

import net.allwebdesign.common.lib.db.DataResults;
 
/**
 * 
 * @author George Moraitakis
 *
 */
public class XLFillManager {
 
	 /**
	  * Fills the report with content
	  *
	  * @param worksheet
	  * @param startRowIndex starting row offset
	  * @param startColIndex starting column offset
	  * @param datasource the data source
	  */
	 public static void fillReport(HSSFSheet worksheet, int startRowIndex, int startColIndex, DataResults data) {
		 // Row offset
		 startRowIndex += 2;
	   
		 // Create cell style for the body
		 HSSFCellStyle bodyCellStyle = worksheet.getWorkbook().createCellStyle();
		 bodyCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		 bodyCellStyle.setWrapText(true);
	   
		 for (int i=0; i< data.size();i++){
		  
			 // 	Create a new row
			 HSSFRow row = worksheet.createRow(startRowIndex+i+1);
			 int col = 0;
			 for (Map.Entry<String, Object> e: data.get(i).getFields().entrySet()){
				 HSSFCell cell = row.createCell(startColIndex+col);
				 cell.setCellValue((String)e.getValue());
				 cell.setCellStyle(bodyCellStyle);
				 col++;
			 }
	   
	  
		 }
	 }
}
