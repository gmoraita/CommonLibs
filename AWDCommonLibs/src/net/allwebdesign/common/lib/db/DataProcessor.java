package net.allwebdesign.common.lib.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.springframework.util.FileCopyUtils;

/**
 * Contains a collection of static methods whcih are helpers for data processing
 * @author George Moraitakis
 *
 */
public class DataProcessor {
	/**
	 * Utility method to put all fields in alphabetical order. It includes the Id column 
	 * Will match: rowX_ e.g. row10_
	 * Will include any id column (regardless of its name)
	 * @param map the map containing the data
	 * @return the processed map with alphabetical order keys
	 */
	public static Object[] processDataMapIncludingId(Map<String, Object> map) {
		return processDataMap(map,DataHandler.ROW_FLAG,null,DataHandler.ROW_DELIM);
	}	
	
	/**
	 * Utility method to put all fields in alphabetical order. It includes the Id column 
	 * Will match: rowX_ e,g, row10_
	 * Will include the row id: rowX_Id
	 * @param map the map containing the data
	 * @param putIdColLast if true then it will add the value of the Id last (useful in update statements) 
	 * @return the processed map with alphabetical order keys except from the id column if this has been requested
	 */
	public static Object[] processDataMapIncludingId(Map<String, Object> map,  boolean putIdColLast) {
		return processDataMap(map,DataHandler.ROW_FLAG,DataHandler.ROW_ID,DataHandler.ROW_DELIM,null, false, putIdColLast);
	}	
	
	/**
	 * Utility method to put all fields in alphabetical order. It includes the Id column 
	 * Will match: rowX_ e,g, row10_
	 * Will include the row id: rowX_<idCol>
	 * @param map the map containing the data
	 * @param idCol the name of the Id col
	 * @param putIdColLast if true then it will add the value of the Id last (useful in update statements) 
	 * @return the processed map with alphabetical order keys except from the id column if this has been requested
	 */
	public static Object[] processDataMapIncludingId(Map<String, Object> map, String idCol, boolean putIdColLast) {
		return processDataMap(map,DataHandler.ROW_FLAG,idCol,DataHandler.ROW_DELIM,null, false, putIdColLast);
	}	

	/**
	 * Utility method to put all fields in alphabetical orderfor a specific row. 
	 * Will match: rowX_ e,g, row10_
	 * Will include the row id: rowX_Id
	 * @param map the map containing the data
	 * @return the processed map with alphabetical order keys
	 */
	public static Object[] processDataMapOfRow(String row, Map<String, Object> map) {
		return processDataMap(map,DataHandler.ROW_FLAG,DataHandler.ROW_ID,DataHandler.ROW_DELIM, row,true,false);
	}	
	

	
	
	/**
	 * Utility method to put all fields in alphabetical order. It includes the Id column (any name)
	 * Will match: rowX_ e.g. row10_
	 * Will include the row id: rowX_Id
	 * @param row the row to identify and process
	 * @param map the map containing the data
	 * @return the processed map with alphabetical order keys
	 */
	public static Object[] processDataMapOfRowIncludingId(String row, Map<String, Object> map) {
		return processDataMap(map,DataHandler.ROW_FLAG,null,DataHandler.ROW_DELIM, row, true, false);
	}	
	
	
	/**
	 * Utility method to put all fields in alphabetical order. It includes the Id column (any name)
	 * This is intentionally private
	 * Will match: rowX_ e.g. row10_
	 * Will include the row id: rowX_<idCol>
	 * @param row the row to identify and process
	 * @param map the map containing the data
	 * @param idCol the name of the id column
	 * @param putIdColLast if true then it will add the value of the Id last (useful in update statements) 
	 * @return the processed map with alphabetical order keys
	 */
	/*
	private static Object[] processDataMapOfRowIncludingId(String row, Map<String, Object> map, String idCol, boolean putIdColLast) {
		return processDataMap(map,ROW_FLAG,idCol,ROW_DELIM, row, false, putIdColLast);
	}
	*/
	
	/**
	 * Utility method to put all fields in alphabetical order. 
	 * Will match: rowX_ e,g, row10_
	 * Will exclude the row id: rowX_Id
	 * @param map the map containing the data
	 * @return the processed map with alphabetical order keys
	 */
	public static Object[] processDataMap(Map<String, Object> map) {
		return processDataMap(map,DataHandler.ROW_FLAG,DataHandler.ROW_ID,DataHandler.ROW_DELIM);
	}	
	
	/**
	 * Utility method to put all fields in alphabetical order. 
	 * Will match: <your custom flag>X_ e.g. myFlag10_ 
	 * Will exclude the row id: e.g. myFlag10_Id
	 * @param map the map containing the data
	 * @param flag the tag to match in order to parse data
	 * @return the processed map with alphabetical order keys 
	 */
	public static Object[] processDataMap(Map<String, Object> map, String flag) {
		return processDataMap(map,flag,DataHandler.ROW_ID, DataHandler.ROW_DELIM);
	}


	/**
	 * Utility method to put all fields in alphabetical order. It also filters out the
	 * ID column which is convenient in update statements unless the idCol passed is null
	 * By default, the idCol will be excluded if defined. If null then it will be included  
	 * @param map the map containing the data
	 * @param flag the tag to match in order to parse data
	 * @param idCol the Id column tag to filter out. If null it will not be filtered.
	 * @param delim the delimiter to be used
	 * @return the processed map with alphabetical order keys
	 */
	public static Object[] processDataMap(Map<String, Object> map, String flag, String idCol, String delim) {
		return processDataMap(map, flag, idCol, delim, null, true, false);
	}
	
	/**
	 * Utility method to put all fields in alphabetical order. It also filters out the
	 * ID column which is convenient in update statements unless the idCol passed is null
	 * @param map the map containing the data
	 * @param flag the tag to match in order to parse data
	 * @param idCol the Id column tag to filter out. If null it will not be filtered out.
	 * @param delim the delimiter to be used
	 * @param row grabs for a specific row. Use null to ignore
	 * @param excludeIdCol if true then it will exclude the Id column value defined in idCol (if idCol = null it will include the value regardless)
	 * @param putIdColLast if true it will put the Id column value defined in idCol last (if idCol = null or excludeIdCol = true then this has no effect)
	 * @return the processed map with alphabetical order keys
	 */
	public static Object[] processDataMap(Map<String, Object> map, String flag, String idCol, String delim, String row, boolean excludeIdCol, boolean putIdColLast) {
	    if (flag == null ||  delim == null){return null;}
		
		TreeMap<String, Object> sortedMap = new TreeMap<String, Object>(map);
		ArrayList<Object> a = new ArrayList<Object>();
	    Iterator<Entry<String, Object>> it = sortedMap.entrySet().iterator();
	    String matchPattern = "^"+flag+"[0-9]+"+delim;
	    if (row != null){
	    	matchPattern = "^"+flag+row+delim;
	    }
	    Object idColumnValue = null;
	    while (it.hasNext()) {
	        Map.Entry<String,Object> pairs = (Map.Entry<String,Object>)it.next();
	        if (((String)pairs.getKey()).matches(matchPattern+"\\S+$")){
	        	// Special treatment for the Id column
	        	if (idCol != null && pairs.getKey().matches(matchPattern+idCol+"$")){
	        		
	        		if (!excludeIdCol){
	        			if (putIdColLast){
	        				idColumnValue = pairs.getValue();
	        			} else {
	        				a.add(pairs.getValue());
	        			}
	        			
	        		}
	        		continue;
	        	}

	        	a.add(pairs.getValue());
	        	
	        }
	    }
	    
	    if (!excludeIdCol && putIdColLast && idColumnValue!= null){
	    	a.add(idColumnValue);
	    }
	    
		return a.toArray();
	}
	
	/**
	 * Utility method to add an identifier to each element in the map<BR/>
	 * This is useful if we want each element to be correlated to the same id <BR/>
	 * It requires that each element ha key starting with "row"
	 * @deprecated Please use addFieldToRows
	 * @param map the map of elements
	 * @param id the id to add
	 * @return the updated map
	 */
	public static Map<String, Object> addIdToElements(Map<String, Object> map, String id){
		
		return addFieldToRows(map,id,id);
	    
		
		
	}	
	
	/**
	 * Utility method to add a specific field to all rows with a default value<BR/>
	 * This is useful if we want each element to be correlated to the same field<BR/>
	 * It requires that each element having the format rowNN_ where NN is the number
	 * @param map the map of elements
	 * @param field the field to add
	 * @param value the default value of the field
	 * @return the updated map
	 */
	public static Map<String, Object> addFieldToRows(Map<String, Object> map, String field, String value){

	    return addFieldToRows(map, field, value, DataHandler.ROW_FLAG, DataHandler.ROW_DELIM);
		
	}	

	/**
	 * Utility method to add a specific field to all rows with a default value<BR/>
	 * This is useful if we want each element to be correlated to the same field<BR/>
	 * It requires that each element having the format xxxxNNd where xxxx the flag of the row and NN the number and d the delimiter
	 * @param map the map of elements
	 * @param field the field to add
	 * @param value the default value of the field
	 * @param flag the signature to use (e.g. row)
	 * @param delim the delimiter to use (e.g. _)
	 * @return the updated map
	 */
	public static Map<String, Object> addFieldToRows(Map<String, Object> map, String field, String value, String flag, String delim){

	    Iterator<Entry<String, Object>> it = map.entrySet().iterator();
	    TreeMap<String, Object> mapNew = new TreeMap<String, Object>();
	    while (it.hasNext()) {
	        Map.Entry<String,Object> pairs = (Map.Entry<String,Object>)it.next();
	        mapNew.put(pairs.getKey(), pairs.getValue());
	        if (((String)pairs.getKey()).startsWith(flag)){
	        	
	        	mapNew.put( ((String)pairs.getKey()).substring(0, ((String)pairs.getKey()).indexOf(delim))+delim+field, value);
	        	

	        }
	    } 

		return mapNew;
		
	}		
	
	
	/**
	 * Utility method to fill any empty fields with "-"
	 * @param map the map of elements
	 * @return the filled map
	 */
	public static Map<String,Object> fillEmptyFields(Map<String,Object> map){
		return fillEmptyFields(map, "-");
	}
	
	/**
	 * Utility method to fill any empty fields with "-"
	 * @param map the map of elements
	 * @param the text to put in empty elements
	 * @return the filled map
	 */	
	public static Map<String,Object> fillEmptyFields(Map<String,Object> map, String filler){
		TreeMap<String,Object> mapNew = new TreeMap<String,Object>();
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String,Object> pairs = (Map.Entry<String,Object>)it.next();
			if (((String)pairs.getValue()).length() == 0 ){
				pairs.setValue(filler);
			}
			mapNew.put(pairs.getKey(), pairs.getValue());
		}
		return map;
	}
	
	/**
	 * Get a list with the distinct number of the rows submitted. E.g. row0, row1, row4, will return 0, 1, 4
	 * @param map the map containing the request
	 * @param flag the row flag
	 * @param delim the delimiter separating row identification from fields
	 * @return the distinct list
	 */
	public static Object[] parseDistinctRows(Map<String,Object> map, String flag, String delim){
		ArrayList<String> rows = new ArrayList<String>();

	    Iterator<Entry<String, Object>> it = map.entrySet().iterator();
	    String rowKey = "";
	    while (it.hasNext()) {
	        Map.Entry<String,Object> pairs = (Map.Entry<String,Object>)it.next();
	        
	        String key = pairs.getKey();
	        if (key.startsWith(flag)){
	        	
	        	String id = key.substring(flag.length(), key.indexOf(delim));
	        	if (!id.equals(rowKey)){
	        		rowKey = id;
	        		rows.add(rowKey);
	        	}
	        }
	    }
		return rows.toArray();		
		
		
		
	}

	

	/**
	 * Puts all data in a single row and appends the increasing number <BR/>
	 * e.g. row0_xxxxxx1, row0_xxxxxx2 etc.		
	 * By default it will be prepended with row) and start the appending with 1
	 * @param rows the list of rows
	 * @param firstAppendingNumber the number to start the appending from (e.g. 1 then next will be 2 etc.)
	 * @return the list of rows with the field names prepended (with the same prepender) and appended (with increasing number)
	 */
	public static Map<String, Object> viewSingleRowMapped(DataResults rows){
		return viewSingleRowMapped(rows, 1, 0 , DataHandler.ROW_FLAG, DataHandler.ROW_DELIM );
	}	
	
	
	/**
	 * Puts all data in a single row and appends the increasing number <BR/>
	 * e.g. row0_xxxxxx1, row0_xxxxxx2 etc.	
	 * By default it will be prepended with row0_
	 * @param rows the list of rows
	 * @param firstAppendingNumber the number to start the appending from (e.g. 1 then next will be 2 etc.)
	 * @return the list of rows with the field names prepended (with the same prepender) and appended (with increasing number)
	 */
	public static Map<String, Object> viewSingleRowMapped(DataResults rows, int firstAppendingNumber){
		return viewSingleRowMapped(rows, firstAppendingNumber, 0 , DataHandler.ROW_FLAG, DataHandler.ROW_DELIM );
	}
	
	/**
	 * Puts all data in a single row and appends the increasing number <BR/>
	 * e.g. row0_xxxxxx1, row0_xxxxxx2 etc.		
	 * @param rows the list of rows
	 * @param firstAppendingNumber the number to start the appending from (e.g. 1 then next will be 2 etc.)
	 * @param prependingNumber the number of the prepender (e.g. 0 then row0_)
	 * @param flag the flag for each row e.g. "row"
	 * @param delim the delimiter e.g. "_"
	 * @return the list of rows with the field names prepended (with the same prepender) and appended (with increasing number)
	 */
	public static Map<String, Object> viewSingleRowMapped(DataResults rows, int firstAppendingNumber, int prependingNumber, String flag, String delim){
		if (rows == null || rows.size() == 0 ){return null;}
		Map<String, Object> map = null;
		for (int i = 0; i < rows.size(); i++){
			DataRow d = rows.get(i);
			d.prependFieldNames(flag + Integer.toBinaryString(prependingNumber) + delim, Integer.toString(firstAppendingNumber+i));
			if (map == null){
				map = d.getFields();
				
			} else {
				map.putAll(d.getFields());
				
			}
		
		}
		map.put("rows", Integer.toString(rows.size()));
		
		return map;
		

	}	
	
	/**
	 * Renders the data into plain text format with tab delimiters to separate fields and new line 
	 * characters to separate rows. The format is Excel ready
	 * @param rows the list of rows
	 * @param hideId if the first column needs to be hidden
	 * @return the plain text of data
	 */
	public static String renderToText(DataResults rows, boolean hideId){
		
		String text ="";
		if (rows == null || rows.size() == 0 ){return "";}
		
		for (int i = 0; i < rows.size(); i++){
			DataRow row = rows.get(i);
			Iterator<Entry<String, Object>> fieldIterator = row.getFields().entrySet().iterator();
			if (i==0){
				int j = 0;
				String row0 = "";
				while(fieldIterator.hasNext()){
					Map.Entry<String,Object> pairs = (Map.Entry<String,Object>)fieldIterator.next();
					if (hideId && j==0){j++;continue;}
					text += pairs.getKey()+"\t";
					row0 += pairs.getValue()+"\t";
					
				}
				text+="\n"+row0+"\n";
				
			}
			else{
				int j = 0;
				while(fieldIterator.hasNext()){
					Map.Entry<String,Object> pairs = (Map.Entry<String,Object>)fieldIterator.next();
					if (hideId && j==0){j++;continue;}
					text += pairs.getValue()+"\t";
					
					
				}	 
				text+="\n";
			}
		}
		return text;
		
	}
	
	/**
	 * Renders the blob extracted from the database and attaches it to the output stream
	 * If multiple rows returned, then the blobs are concatenated into the output stream
	 * @param rows the resultset with the blobs 
	 * @param out the output stream on which the blob will be attached
	 */
	public static void renderBlob(DataResults rows, OutputStream out){
		

		if (rows == null  ){return;}
		
		for (int i = 0; i < rows.size(); i++){
			DataRow row = rows.get(i);
			Iterator<Entry<String, Object>> fieldIterator = row.getFields().entrySet().iterator();
			while(fieldIterator.hasNext()){
				Map.Entry<String,Object> pairs = (Map.Entry<String,Object>)fieldIterator.next();
				try {
					Object is = pairs.getValue();
					if (is != null){
					
						FileCopyUtils.copy((InputStream)is, out);
					}
					
				} catch (IOException e) {
					Logger.getAnonymousLogger().severe("Problem in rendering the blob");
					e.printStackTrace();
				}
				
				
				
			}
		}
		
		
	}
	
	
	/**
	 * Creates a sproc from args
	 * @param sprocName
	 * @param flag
	 * @param delim
	 * @param row
	 * @param argSymbol
	 * @param args
	 * @param staticArgs
	 * @return
	 */
	public static String constructSprocFromArgs(String sprocName, String flag, String delim, String row, String argSymbol, Map<String,Object> args, Object... staticArgs){
		
		if (flag == null ||  delim == null){return null;}
		
		String sproc = "EXEC "+sprocName+" ";
		StringBuffer sprocBuf = new StringBuffer(sproc);

	    Iterator<Entry<String, Object>> it = args.entrySet().iterator();
	    String matchPattern = "^"+flag+"[0-9]+"+delim;
	    if (row != null){
	    	matchPattern = "^"+flag+row+delim;
	    }
	    String comma = "";// only for first arg is empty
	    while (it.hasNext()) {
	        Map.Entry<String,Object> pairs = (Map.Entry<String,Object>)it.next();
	        if (((String)pairs.getKey()).matches(matchPattern+"\\S+$")){
	        	
	        	sprocBuf.append(comma).append(argSymbol).append(pairs.getKey().substring(pairs.getKey().indexOf(delim)+1)).append("= ?");
	        	comma = ",";
	        }
	    }
	    for (int i=0 ; i < staticArgs.length; i++){
	    	sprocBuf.append(comma).append(staticArgs[i]);
	    }
	    
		return sprocBuf.toString();
		
	}
	
	/**
	 * Filters data based on whether they match a pattern
	 * @param data the mapped data
	 * @param flag the flag to identify them (e.g. row)
	 * @param delim delim the delimiter (e.g. _)
	 * @return the filtered map
	 */
	protected static Map<String, Object> filterData(Map<String, Object> data, String flag, String delim){
		return filterData(data, null, flag, delim);
	}
		
	
	/**
	 * Filters data based on whether they match a pattern
	 * @param data the mapped data
	 * @param row the specific row to pick
	 * @param flag the flag to identify them (e.g. row) 
	 * @param delim the delimiter (e.g. _)
	 * @return the filtered map
	 */
	protected static Map<String, Object> filterData(Map<String, Object> data, String row, String flag, String delim){
		
		String matchPattern = "^"+flag+"[0-9]+"+delim;
		if (row != null){
	    	matchPattern = "^"+flag+row+delim;
	    }
		
		Map<String, Object> filteredMap = new TreeMap<String, Object>();
		for (Map.Entry<String, Object> entry: data.entrySet()){
			if (entry.getKey().matches(matchPattern)){
				filteredMap.put(entry.getKey(), entry.getValue());
			}
			
		}
		
		return filteredMap;
	
	}
}
