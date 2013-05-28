package net.allwebdesign.common.lib.ui;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;

import com.lowagie.text.Rectangle;

import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

public class BDPDFView extends AbstractPdfView {
	
	
	@Override
	protected void buildPdfDocument(
			Map<String, Object> model, 
			Document pdfDoc,
			PdfWriter pdfWriter, 
			HttpServletRequest request, 
			HttpServletResponse response)
			throws Exception {
		
				
			byte[] pdfData = (byte[]) model.get("pdfData");
			
		 	this.loadPdf(pdfData, pdfDoc, pdfWriter);
	}
		/*
		DataTable table = new DataTable(2);
		table.addCell("Month");
		table.addCell("Revenue");
 
		for (Map.Entry<String, String> entry : revenueData.entrySet()) {
			table.addCell(entry.getKey());
			table.addCell(entry.getValue());
                }
 
		Element element = new Element()
		
		pdfDoc.
		*/
	

	
	
	private void loadPdf(byte[] pdfIn, Document document, PdfWriter pdfWriter){
	    
		PdfReader pdfReader;
		try {
			pdfReader = new PdfReader(pdfIn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		int n = pdfReader.getNumberOfPages(); 
	    Rectangle psize = pdfReader.getPageSize(1);
          
	    document.setPageSize(psize);
	    PdfContentByte cb = pdfWriter.getDirectContent();
	    
	    //int p = 0;
	    for (int i=0; i<n ; i++) {
	       document.newPage();
	       //p++;

	       PdfImportedPage page = pdfWriter.getImportedPage(pdfReader, i);
	       cb.addTemplate(page, .5f, 0, 0, .5f, 60, 120);
	      
	       
	    }
	    
	     
	}

}
