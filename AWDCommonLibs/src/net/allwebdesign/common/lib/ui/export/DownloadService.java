package net.allwebdesign.common.lib.ui.export;

import javax.servlet.http.HttpServletResponse;
 
import org.apache.log4j.Logger;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.springframework.stereotype.Service;

import net.allwebdesign.common.lib.db.DataResults;

 
/**
 * Service for processing Apache POI-based reports
 *
 * @author George Moraitakis
 */
@Service("downloadService")
public class DownloadService {
 
	 private static Logger logger = Logger.getLogger("service");
	  
	
	 /**
	  * Processes the download for Excel format.
	  * It does the following steps:
	  * <pre>1. Create new workbook
	  * 2. Create new worksheet
	  * 3. Define starting indices for rows and columns
	  * 4. Build layout
	  * 5. Fill report
	  * 6. Set the HttpServletResponse properties
	  * 7. Write to the output stream
	  * </pre>
	  */
	
	 /**
	  * download Excel
	  * @param response
	  * @param data
	  * @param excelFileName
	  * @throws ClassNotFoundException
	  */
	 public void downloadXLS(HttpServletResponse response, DataResults data, String sheetTitle, String excelFileName) throws ClassNotFoundException {
		  logger.debug("Downloading Excel report");
		   
		  
		  // 1. Create new workbook
		  HSSFWorkbook workbook = new HSSFWorkbook();
		   
		  // 2. Create new worksheet
		  HSSFSheet worksheet = workbook.createSheet("Property Insurance");
		   
		  if (data != null && data.size() >0){
			  
			  
			  // 3. Define starting indices for rows and columns
			  int startRowIndex = 0;
			  int startColIndex = 0;
			   
			    
			  // 4. Build layout
			  // Build title, date, and column headers
			  
			  String[] a = new String[]{};
			  XLLayoutManager.buildReport(worksheet,sheetTitle, data.getFirstRowFields().keySet().toArray(a),startRowIndex, startColIndex);
			 
			  // 5. Fill report
			  XLFillManager.fillReport(worksheet, startRowIndex, startColIndex, data);
		  }
		  // 6. Set the response properties
		  String fileName = excelFileName+".xls";
		  response.setHeader("Content-Disposition", "inline; filename=" + fileName);
		  // Make sure to set the correct content type
		  response.setContentType("application/vnd.ms-excel");
		   
		  //7. Write to the output stream
		  XLWriter.write(response, worksheet);
	 }
	 
	 /**
	  * Not implemented yet
	  * @param response
	  * @param data
	  * @param sheetTitle
	  * @param excelFileName
	  */
	 public void downloadPDF(HttpServletResponse response, DataResults data, String sheetTitle, String excelFileName){
		 /**TODO Add methods */
	 }
  
 
}