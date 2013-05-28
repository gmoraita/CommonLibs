package net.allwebdesign.common.lib.ui.export;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
 
/**
 * Writes the Excel report to the output stream
 *
 * @author George Moraitakis
 */
public class XLWriter {
 
	 private static Logger logger = Logger.getLogger("service");

	 /**
	  * Writes the report to the output stream
	  * @param response
	  * @param worksheet
	  */
	 public static void write(HttpServletResponse response, HSSFSheet worksheet) {
	   
		  logger.debug("Writing report to the stream");
		  try {
			   // Retrieve the output stream
			   ServletOutputStream outputStream = response.getOutputStream();
			   // Write to the output stream
			   worksheet.getWorkbook().write(outputStream);
			   // Flush the stream
			   outputStream.flush();
		 
		  } catch (Exception e) {
			  logger.error("Unable to write report to the output stream");
		  }
	 }
}
