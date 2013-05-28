package net.allwebdesign.common.lib.ui.tags;
import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.Tag;

import net.allwebdesign.common.lib.db.DataResults;
import net.allwebdesign.common.lib.db.DataRow;

public class DataTable extends GenericTag
{
    private String classTag;
    private String id;
    private PageContext pageContext;
    private Tag parent;
    private DataResults data;
    private String drillDownColumn;
    private String drillDownUrl;
    private boolean sortable;
    private boolean doZebra;
    private boolean doExcel;
    private boolean doPrint;
    private String pageSizes;
    private boolean doPaging;
    private boolean showPagingAtBottom;
    private String alignContent ="left";

    public DataTable()
    {
        id = null;
        classTag = null;
        
    }

    public void setId(String s)
    {
        id = s;
    }

    public String getId()
    {
        return id;
    }


    public int doStartTag()
        throws JspException
    {
        return 0;
    }

    public int doEndTag()
        throws JspException
    {
        StringBuffer stringbuffer = new StringBuffer("");
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        
        // Always need to have a an id in case there is an excel/print
        if (id == null){
        	Random r = new Random();
        	int tR = r.nextInt(100000000);
        	id = "dataTable_"+ tR;
        }
        
        if(sortable){
        	stringbuffer.append("<script type=\"text/javascript\" >\n$(document).ready(function() {\n$(\"#"+id+"\")\n  	.tablesorter({widthFixed: true");
        	if (doZebra){
        		stringbuffer.append(",widgets: ['zebra']");
        	}
        	stringbuffer.append("}) \n");
        	if (!doPaging){stringbuffer.append(" });</script>");}
        	else{
        		stringbuffer.append(".tablesorterPager({container: $(\"#pager\")})");
        		if (this.showPagingAtBottom){
        			stringbuffer.append(".tablesorterPager({container: $(\"#pagerBottom\")})");
        		}
        		
        		stringbuffer.append("; });\n</script>"); 
        		
        	}
        }
        if (doPaging){
        	stringbuffer.append(tablePaging(id, req.getContextPath(), false));	
        }
        
        stringbuffer.append("<DIV id=\"ttdt_"+(id!=null?id:"")+"\">\n");
       
        if (this.doExcel || this.doPrint){
        	
        	String exportableTableId = id; 
        	if (this.doPaging){
        		exportableTableId = exportableTableId+"_exportable";
        		stringbuffer.append(this.buildTable(exportableTableId, true));
        	}
        	stringbuffer.append("\n<form name=\"formExcel\"  method=\"post\" >");
        	if (this.doPrint){
        		stringbuffer.append("\n<a href=\"#\" onClick=\"Print('"+exportableTableId+"','"+req.getContextPath()+"', 'Αποτελέσματα αναζήτησης')\"><img src=\""+req.getContextPath()+"/images/print.jpg\" border=\"0\" align=\"absmiddle\" /></a> ");
        	}
        	if (this.doExcel){
        		stringbuffer.append("\n<a href=\"#\" onClick=\"Excel('formExcel', '"+exportableTableId+"','Αποτελέσματα αναζήτησης')\"><img src=\""+req.getContextPath()+"/images/excel.jpg\" border=\"0\" align=\"absmiddle\" /></a>");
        		stringbuffer.append("\n<input type=\"hidden\" name=\"excelData\" value=\"false\" />");
        	}
        	stringbuffer.append("</form>\n");
			
        	
        	

        }
        
        stringbuffer.append(this.buildTable(id, false));
        if (doPaging && showPagingAtBottom){
        	stringbuffer.append(tablePaging(id, req.getContextPath(), true));	
        }
       
        stringbuffer.append("</div>\n");
        try
        {
            pageContext.getOut().write(stringbuffer.toString());
        }
        catch(IOException ioexception)
        {
            throw new JspException((new StringBuilder()).append("IO Error: ").append(ioexception.getMessage()).toString());
        }
        dropData();
        return 6;
    }

    
    private String tablePaging(String id, String contextPath, boolean isBottomPager){
    	
    	StringBuffer stringbuffer = new StringBuffer();
    	String pagerId = "pager";
    	if (isBottomPager){pagerId = "bottomPager";}
    	stringbuffer.append("<div id=\""+pagerId+"\" class=\"pager\"> \n<form>Επιλέξτε σελίδα:\n<img src=\""+contextPath+"/images/first.png\" class=\"first\"/>\n<img src=\""+contextPath+"/images/prev.png\" class=\"prev\"/>\n<input type=\"text\" class=\"pagedisplay\" readonly size=\"3\"/>\n<img src=\""+contextPath+"/images/next.png\" class=\"next\"/>\n<img src=\""+contextPath+"/images/last.png\" class=\"last\"/> &nbsp;&nbsp;&nbsp;\n	Eγγραφές / σελίδα:\n<select class=\"pagesize\">\n");

        
        
        /** TODO
         * fix issue with paging that needs to fix the default.
         */
    	if (pageSizes == null || !pageSizes.contains(",")){
    		pageSizes = "50,100,150,200";
    	}
		
    	String[] pageSizesArray = pageSizes.split(",");
		
		for (int p = 0 ; p < pageSizesArray.length; p++){
			stringbuffer.append("<option value=\""+pageSizesArray[p]+"\">"+pageSizesArray[p]+"</option>\n");
		}
		
		stringbuffer.append("\n</select>\n</form>\n</div>");
    	
    	return stringbuffer.toString();
    	
    	
    }
    
    private String buildTable(String id, boolean isHidden){
    	
    	StringBuffer stringbuffer = new StringBuffer();
    	
    	// TABLE TAG
        stringbuffer.append("<TABLE cellspacing=\"1\"");
        stringbuffer.append(" id=\"").append(id).append("\"");
        
        if (isHidden){
        	
        	stringbuffer.append(" style=\"display: none\" ");
        }
        
        if (classTag != null ){
	        stringbuffer.append(" class=\"").append(classTag).append("\"");
	    }
        
        
        
        
        stringbuffer.append("/>");
        ///////////////////////////
        
        // THEAD
        stringbuffer.append("<thead>\n");
        stringbuffer.append("<TR>\n");
        for (int i=0; i<data.get(0).getFields().size();i++){
        	String key = data.get(0).getKey(i);
        	if (key != null && key.equals("Id")) continue;
        	stringbuffer.append("<TH>").append(key).append("</TH>");
        }
        stringbuffer.append("<TR/>\n");
        stringbuffer.append("</thead>\n");
        ///////////////////////////
        
        
        
        // BODY
        stringbuffer.append("<tbody>\n");
        for (int i=0; i<data.size();i++){
        	stringbuffer.append("<TR>\n");
        	DataRow dataRow = data.get(i);
        	String rowId = null;
        	for (int j=0; j<dataRow.getFields().size();j++){
        		String key = dataRow.getKey(j);
        		String value = dataRow.getValueString(j);
        		
            	if (key != null && key.equals("Id")){
            		rowId = value;
            	}
            	else{
            		stringbuffer.append("<TD align=\""+alignContent+"\">");
            		if (key != null && this.drillDownColumn != null && key.equals(drillDownColumn)){
            			stringbuffer.append("<a href=\"").append(drillDownUrl).append(rowId).append("\">").append(value).append("</a>");
            		} else {
            			stringbuffer.append(value);
            		}
            		stringbuffer.append("</TD>\n");
            	}
            	
            		
            }
        	stringbuffer.append("</TR>\n");
        		
        }
        	
        stringbuffer.append("</tbody>\n");
        stringbuffer.append("</table>\n");
        
        return stringbuffer.toString();
    	
    	
    }
    
    
    public void release()
    {
        dropData();
    }

    private void dropData()
    {

        id = null;
    }
    
    @Override
    public void setPageContext(PageContext pagecontext)
    {
        pageContext = pagecontext;
    }

    public void setParent(Tag tag)
    {
        parent = tag;
    }

    public Tag getParent()
    {
        return parent;
    }

	/**
	 * @return the classTag
	 */
	public String getClassTag() {
		return classTag;
	}

	/**
	 * @param classTag the classTag to set
	 */
	public void setClassTag(String classTag) {
		this.classTag = classTag;
	}

	/**
	 * @return the data
	 */
	public DataResults getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(DataResults data) {
		this.data = data;
	}

	/**
	 * @return the drillDownColumn
	 */
	public String getDrillDownColumn() {
		return drillDownColumn;
	}

	/**
	 * @param drillDownColumn the drillDownColumn to set
	 */
	public void setDrillDownColumn(String drillDownColumn) {
		this.drillDownColumn = drillDownColumn;
	}

	/**
	 * @return the drillDownUrl
	 */
	public String getDrillDownUrl() {
		return drillDownUrl;
	}

	/**
	 * @param drillDownUrl the drillDownUrl to set
	 */
	public void setDrillDownUrl(String drillDownUrl) {
		this.drillDownUrl = drillDownUrl;
	}

	/**
	 * @return the sortable
	 */
	public boolean isSortable() {
		return sortable;
	}

	/**
	 * @param sortable the sortable to set
	 */
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
	}

	public boolean isDoZebra() {
		return doZebra;
	}

	public void setDoZebra(boolean doZebra) {
		this.doZebra = doZebra;
	}

	public boolean isDoExcel() {
		return doExcel;
	}

	public void setDoExcel(boolean doExcel) {
		this.doExcel = doExcel;
	}

	public boolean isDoPrint() {
		return doPrint;
	}

	public void setDoPrint(boolean doPrint) {
		this.doPrint = doPrint;
	}

	public String getPageSizes() {
		return pageSizes;
	}

	public void setPageSizes(String pageSizes) {
		this.pageSizes = pageSizes;
	}

	public boolean isDoPaging() {
		return doPaging;
	}

	public void setDoPaging(boolean doPaging) {
		this.doPaging = doPaging;
	}

	public boolean isShowPagingAtBottom() {
		return showPagingAtBottom;
	}

	public void setShowPagingAtBottom(boolean showPagingAtBottom) {
		this.showPagingAtBottom = showPagingAtBottom;
	}


	public String getAlignContent() {
		return alignContent;
	}


	public void setAlignContent(String alignContent) {
		this.alignContent = alignContent;
	}

	
    
	

	
}

