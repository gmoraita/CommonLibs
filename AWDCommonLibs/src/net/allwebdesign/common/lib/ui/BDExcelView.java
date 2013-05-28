package net.allwebdesign.common.lib.ui;

import java.util.Iterator;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import net.allwebdesign.common.lib.db.DataResults;
import net.allwebdesign.common.lib.db.DataRow;

/**
 * View Excels (work in progress)
 * @author George Moraitakis
 *
 */
public class BDExcelView extends AbstractExcelView{
	 public static final String WIDGET_LIST_KEY = "widgetList";  
	 protected static final int WIDGET_NAME_COLUMN = 0;  
	 protected static final int WIDGET_SIZE_COLUMN = 1;  

	 protected void buildExcelDocument(Map<String, Object> map, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) 
	 {  
		//CREATE THE SHEET    
	 	HSSFSheet sheet = workbook.createSheet("Data List");  
		sheet.setDefaultColumnWidth(12);    
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		
		while (it.hasNext()) {
			Map.Entry<String,Object> pairs = (Map.Entry<String,Object>)it.next();
			DataResults rows = (DataResults)pairs.getValue();
			
			for(int i=0; i<rows.size(); i++){
				HSSFRow excelRow = sheet.createRow(i);
				//write headers
				if (i == 0){this.writeHeader(rows.get(i), excelRow);}
				else{this.writeRow(rows.get(i), excelRow);}
			}
		}

	 }
	 
	 private void writeHeader(DataRow row, HSSFRow excelRow){
		 this.writeCells(row, excelRow, true);
	 }
	 private void writeRow(DataRow row, HSSFRow excelRow){
		 this.writeCells(row, excelRow, false);
	 }
	
	 private void writeCells(DataRow row, HSSFRow excelRow, boolean isHeader){
		 int cell = 0;
		 Iterator<Entry<String, Object>> fieldIterator = row.getFields().entrySet().iterator();
		 while(fieldIterator.hasNext()){
			 Map.Entry<String,Object> pairs = (Map.Entry<String,Object>)fieldIterator.next();
			 Object content = isHeader? pairs.getKey():pairs.getValue();
			 
			 excelRow.createCell(cell).setCellValue(content.toString());
			 cell++;
		 }
	 }


	 
}
