package net.allwebdesign.common.lib.db;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.support.PagedListHolder;

/**
 * A List of DataRows that represents the result set.
 * It adds some additional functionalities including the conversion
 * of result set to a delimited string
 * @author George Moraitakis
 *
 */
public class DataResults extends ArrayList<DataRow> {
	
	
	private static int DEFAULT_PAGE_SIZE = 200; 
	private static final long serialVersionUID = 1L;
	private PagedListHolder<DataRow> pager;
	
	/**
	 * Initialise the data results with a list of rows. 
	 * It also initialises the data pager with default page size of 200
	 * @param datarows the query results
	 */
	public DataResults(List<DataRow> datarows) {
		super(datarows);
		pager = new PagedListHolder<DataRow>(datarows);
		pager.setPageSize(DEFAULT_PAGE_SIZE);
		
	}
	
	
	/**
	 * Creates an empty data result
	 */
	public DataResults() {
		// TODO Auto-generated constructor stub
	}



	/**
	 * Converts a specified field of a List containing data rows to a delimited string of data.
	 * This is a convenience method that defaults to first column use and pipe delimited output
	 * @return the delimited string of data or empty string if no data available 
	 */
	public String fieldToDelimString(){
		return fieldToDelimString(null,null);
	}
	
	/**
	 * Converts a specified field of a List containing data rows to a delimited string of data.
	 * If no field is specified then it will pick the first field   
	 * @param fieldName the field to use
	 * @param delim the delimiter of the output string. If null it defaults to pipe
	 * @return the delimited string of data or empty string if no data available 
	 */
	public String fieldToDelimString(String fieldName, String delim){
		
		
		StringBuffer strBuf = new StringBuffer();
		if (delim == null){
			delim = "|";
		}
		
		if (this.size()==0){
			return ""; // will return an empty string
		}
		
		for (int i = 0 ; i < this.size(); i++){
			if (fieldName == null){
				// will get the first available field
				strBuf.append(this.get(i).getValueString(0)).append(delim);
			}
			else{
				strBuf.append(this.get(i).getValueString(fieldName)).append(delim);
			}
			
		}
		
		return strBuf.toString();
	}
	/**
	 * Converts the result set to a hashmap of key/values for 2 selected columns. The ordering is
	 * based on the SQL so it should be handled there! (Before 13.4.18.1 it was alphabetic)
	 * @param keyColumn the column to act as a key
	 * @param valueColumn the column to act as value
	 * @return the hashmap with the key/value pairs
	 */
	public Map<String, Object> convertToHashMap(String keyColumn, String valueColumn){
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		for (int i=0; i<this.size();i++){
			DataRow d = this.get(i);
			String key = d.getValueString(keyColumn);
			Object value = d.getValue(valueColumn);
			
			map.put(key, value);
		}
		
		return map;
		
	}
	
	/**
	 * Makes a single map out of all rows prepended with a row flag. This is useful when returning to 
	 * a viewer that requires the id of each row e.g. row0_, row1_ etc. Also  includes the row count "_rowsCounterFld"
	 * and the "_elementsIds" with the Ids of all the rows
	 * This is a convenience overloaded method that passes the default flag "row" and delimiter "_" 
	 * @param rows the list of rows
	 * @param flag the flag for each row e.g. "row"
	 * @param delim the delimiter e.g. "_"
	 * @return the list of rows with the field names prepended
	 */
	public Map<String, Object> viewMapped(){
		return viewMapped(DataHandler.ROW_FLAG, DataHandler.ROW_DELIM);
	}	
	/**
	 * Makes a single map out of all rows prepended with a row flag. This is useful when returning to 
	 * a viewer that requires the id of each row e.g. row0_, row1_ etc. Also  includes the row count "_rowsCounterFld"
	 * and the "_elementsIds" with the Ids of all the rows
	 * @param rows the list of rows
	 * @param flag the flag for each row e.g. "row"
	 * @param delim the delimiter e.g. "_"
	 * @return the list of rows with the field names prepended and appended
	 */
	public Map<String, Object> viewMapped(String flag, String delim){
		if (this.size()==0){return null;}
		Map<String, Object> map = new TreeMap<String, Object>();
		for (int i = 0; i < this.size(); i++){
			DataRow d = this.get(i);
			d.prependFieldNames(flag + i + delim);
			if (map.size() == 0){
				map.put("_elementsIds", "0");
			} else {
				map.put("_elementsIds", map.get("_elementsIds")+","+Integer.toString(i));
			}
			map.putAll(d.getFields());
			//rows.set(i, d);
			
		}
		map.put("_rowsCounterFld", Integer.toString(this.size() - 1));
		
		return map;
		

	}
	
	/**
	 * Get all the fields of the first row in the result set
	 * @return the fields and their values of the first row or null if there are no results 
	 */
	public Map<String, Object> getFirstRowFields(){
		
		return this.getRowFields(0);
	}
	
	/**
	 * Get all the fields of a specific row in the result set based on the value of the first column.<BR/>
	 * Note: if the value occurs multiple times in the field then the first occurrence will only be return
	 * @param value the value of the first column that we want to use as key for this search
	 * @return the Map representation of the row.
	 */
	public DataRow getRow(String value){
		return this.getRow(0, value);
	}
	
	/**
	 * Get all the fields of a specific row in the result set based on the value of a particular field.<BR/>
	 * Note: if the value occurs multiple times in the field then the first occurrence will only be return
	 * @param field the field to search
	 * @param value the value of the field that we want to use as key for this search
	 * @return the Map representation of the row.
	 */
	public DataRow getRow(String field, String value){
		if (this.size()== 0 || !this.get(0).getFields().containsKey(field)){
			return null;
		}
		
		for (int i=0; i<this.size();i++){
			if (this.get(i).getValue(field).equals(value)){
				return this.get(i);
			}
		}
		
		return null;
		
	}
	
	/**
	 * Get all the fields of a specific row in the result set based on the value of a particular column.<BR/>
	 * Note: if the value occurs multiple times in the field then the first occurrence will only be return
	 * @param field the field to search
	 * @param value the value of the field that we want to use as key for this search
	 * @return the Map representation of the row.
	 */
	public DataRow getRow(int column, String value){
		if (this.size()== 0 || column >= this.get(0).getFields().size()){
			return null;
		}
		
		for (int i=0; i<this.size();i++){
			if (this.get(i).getValue(column).equals(value)){
				return this.get(i);
			}
		}
		
		return null;
		
	}
	
	/**
	 * Get all the fields of a specific row in the result set
	 * @param row the row to pick
	 * @return the fields and their values of the specific row or null if the result set does not have this row
	 */
	public Map<String, Object> getRowFields(int row){
		
		if (this.size()== 0 || row >= this.size()){
			return null;
		}
		
		return this.get(row).getFields();
		
	}
	
	/**
	 * Fetches the value of the specified column of the first row of the result set
	 * @param columnName the name of the column
	 * @return the value of the field or null if no rows
	 */
	public Object getValue(String columnName){
		if (size()==0){return null;}
		return this.get(0).getValue(columnName);
	}
	
	/**
	 * Fetches the value of the specified column of the first row of the result set
	 * @param columnName the index of the column
	 * @return the value of the field or null if no rows
	 */
	public Object getValue(int col){
		if (size()==0){return null;}
		return this.get(0).getValue(col);
	}
	
	/**
	 * Fetches the value of the specified column of the first row of the result set
	 * @param columnName the name of the column
	 * @return the value of the field as string or null if no rows
	 */
	public String getValueString(String columnName){
		if (size()==0){return null;}
		return this.get(0).getValueString(columnName);
	}
	
	/**
	 * Fetches the value of the specified column of the first row of the result set
	 * @param columnName the index of the column
	 * @return the value of the field as string or null if no rows
	 */
	public String getValueString(int col){
		if (size()==0){return null;}
		return this.get(0).getValueString(col);
	}
	
	
	
	/**
	 * Show the data paged
	 * @return the pager
	 */
	public PagedListHolder<DataRow> showPaged() {
		return pager;
	}

	/**
	 * Set the page. 
	 * @param page the page number. If page is not a valid number then default to 0
	 */
	public void setPage(String page) {
		int pageNum;
		try{
			pageNum = Integer.parseInt(page);
			assert pageNum>=0 :" page has to be a positive integer";
			assert pageNum<pager.getPageCount() :" page has to be smaller than the total pages";
		} catch(Exception e){
			e.printStackTrace();
			pageNum = 0;
		}
		
		
		if (this.pager != null){
			this.pager.setPage(pageNum);
		}
		
	}

	/**
	 * Set the size of the page in rows. 
	 * @param rows the rows to show per page. If rows is not a valid number then default to DEFAULT_PAGE_SIZE
	 */
	public void setPageSize(String rows) {
		int rowsNum;
		try{
			rowsNum = Integer.parseInt(rows);
			assert rowsNum>0:" rows has to be a positive integer";
		} catch(Exception e){
			e.printStackTrace();
			rowsNum = DEFAULT_PAGE_SIZE;
		}

		
		
		if (this.pager != null){
			this.pager.setPageSize(rowsNum);
		}
		
	}

	/**
	 * Creates a single row, single field artificial data result (e.g. for integrating error messages)
	 * @param fieldName the name of the artificial field
	 * @param value the value of the field
	 * @return an artificial data result
	 */
	public static DataResults createSingleFieldRow(String fieldName, String value){
		DataRow row = new DataRow();
		List<DataRow> rowList = new ArrayList<DataRow>();
		row.setValue(fieldName, value);
		rowList.add(row);
		
		return new DataResults(rowList);
	}
	
	/**
	 * Fetches the contents of a specific column as strings.
	 * @param column the numbered column to use 
	 * @return a String array of all the rows in the specific column
	 */
	public String[] getValues(int column){
		if (this.size()>0){
			return this.getValues(this.get(0).getKey(0));
		}
		return null;
	}
	
	/**
	 * Fetches the contents of a specific column as strings
	 * @param column  the column to use 
	 * @return a String array of all the rows in the specific column
	 */
	public String[] getValues(String column){
		
		List<String> fieldRows = new ArrayList<String>();
		for(int i = 0 ; i< this.size();i++ ){
			fieldRows.add(this.get(i).getValueString(column));
		}

		return fieldRows.toArray(new String[0]);
		
	}
	
	/**
	 * Fetches the contents of a specific column as strings but ignoring specific values
	 * @param column the numbered column to use
	 * @param ignoreValues the values to ignore
	 * @return a String array of all the rows in the specific column
	 */
	public String[] getValues(int column, String... ignoreValues){
		if (this.size()>0){
			return this.getValues(this.get(0).getKey(0),ignoreValues);
		}
		return null;
	}
	
	/**
	 * Fetches the contents of a specific column as strings but ignoring specific values
	 * @param column the column to use
	 * @param ignoreValues the values to ignore
	 * @return a String array of all the rows in the specific column
	 */
	public String[] getValues(String column, String... ignoreValues){
		
		List<String> fieldRows = new ArrayList<String>();
		for(int i = 0 ; i< this.size();i++ ){
			boolean doInclude = true;
			for(int j=0; j<ignoreValues.length;j++){
				if (this.get(i).getValueString(column).equals(ignoreValues[j])){
					doInclude = false;
				}
			}
			
			if(doInclude){
				fieldRows.add(this.get(i).getValueString(column));
			}
		}

		return fieldRows.toArray(new String[0]);
		
	}


	/**
	 * 
	 * DataResults factory method from List of {@link net.allwebdesign.common.lib.db.DataRow DataRows}
	 * @param dataResults the List of {@link net.allwebdesign.common.lib.db.DataRow DataRows}
	 * @return a new DataResults object
	 */
	public static DataResults parseResults(List<DataRow> dataResults) {
		return new DataResults(dataResults);
	}
	
	
}
