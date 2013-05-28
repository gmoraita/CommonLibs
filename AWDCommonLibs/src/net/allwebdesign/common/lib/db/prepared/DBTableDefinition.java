package net.allwebdesign.common.lib.db.prepared;


import java.util.ArrayList;
import java.util.Collections;


import net.allwebdesign.common.lib.cache.Cacheable;
import net.allwebdesign.common.lib.db.DataResults;

public class DBTableDefinition implements Cacheable {

	
	private static final long serialVersionUID = 1L;
	private String tableName;
	private String tableDefinition;
	private String[] tableColumns;
	
	
	public static DBTableDefinition getInstance(String tableName, DataResults tableColumnsList){
		
		DBTableDefinition tableDef = new DBTableDefinition();
		
		tableDef.setTableName(tableName);
		tableDef.setTableColumns(tableColumnsList.getValues(0));
		
		return tableDef;
	}
	
	@Override
	public String getKey() {
		return tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableDefinition() {
		return tableDefinition;
	}

	public void setTableDefinition(String tableDefinition) {
		this.tableDefinition = tableDefinition;
	}

	public String[] getTableColumns() {
		return tableColumns;
	}
	
	/**
	 * Convenience method to retrieve all table columns except the Id and RecTimestamp
	 * @param ignoreId if you need to ignore Id
	 * @param ignoreRecTimestamp if you need to ignore RecTimestamp
	 * @return the refined list
	 */
	public String[] getTableColumns(boolean ignoreId, boolean ignoreRecTimestamp){
		return this.getTableColumns(ignoreId, ignoreRecTimestamp, false);
	}
	
	
	/**
	 * Convenience method to retrieve all table columns except the Id and RecTimestamp. You can also have the
	 * output sorted
	 * @param ignoreId if you need to ignore Id
	 * @param ignoreRecTimestamp if you need to ignore RecTimestamp
	 * @param doSorting Set to true if yu want the columns to be soted before they return
	 * @return the refined list
	 */
	public String[] getTableColumns(boolean ignoreId, boolean ignoreRecTimestamp, boolean doSorting) {
		
		ArrayList<String> a = new ArrayList<String>();
		for(int i=0; i<tableColumns.length;i++){
		
			if (tableColumns[i].equals("Id") && ignoreId){
				continue;
			}
			if (tableColumns[i].equals("RecTimestamp") && ignoreRecTimestamp){
				continue;
			}
			
			a.add(tableColumns[i]);
			
		}
		
		if (doSorting){
			Collections.sort(a);
		}
		
		return a.toArray(new String[0]);
	}
	
	/**
	 * Returns only the columns contained in the matched column set. 
	 * This set usually comes from a map of key/values and they represent the keys
	 * @param matchColumns the columns to use
	 * @param flag the flag of key. Put "" if does not exist any
	 * @param delim the delimiter of the key. Put "" if does not exist any
	 * @param doSorting whether to sort them or not
	 * @return the refined list
	 */
	public String[] getTableColumns(String[] matchColumns, String flag, String delim, boolean doSorting, boolean ignoreId) {
		return this.getTableColumns(matchColumns, flag, delim, null, doSorting, ignoreId);
	}
	/**
	 * Returns only the columns contained in the matched column set. 
	 * This set usually comes from a map of key/values and they represent the keys
	 * You can select the row to use
	 * @param matchColumns the columns to use
	 * @param flag the flag of key. Put "" if does not exist any
	 * @param delim the delimiter of the key. Put "" if does not exist any
	 * @param row grab a specific row or any if it is null
	 * @param doSorting whether to sort them or not
	 * @return the refined list
	 */
	public String[] getTableColumns(String[] matchColumns, String flag, String delim, String row, boolean doSorting, boolean ignoreId) {
		ArrayList<String> a = new ArrayList<String>();
		String matchPattern = "^"+flag+(row==null?"[0-9]+":row)+delim;
	
		for(int i=0; i<matchColumns.length;i++){
			if (matchColumns[i].matches(matchPattern+"\\S+$")){ 
				String clearedColumn =  matchColumns[i].substring(matchColumns[i].indexOf(delim)+1);
				
				for(int j=0; j<tableColumns.length;j++){

					if (tableColumns[j].equals("Id") && ignoreId){
						continue;
					}
					if (clearedColumn.equals(tableColumns[j])){
						a.add(tableColumns[j]);
					}
					
				}
			
			}
			
		}
		
		
		
		if (doSorting){
			Collections.sort(a);
		}
		
		return a.toArray(new String[0]);
	}
	
	
	
	public void setTableColumns(String[] tableColumns) {
		this.tableColumns = tableColumns;
	}

	
	
	

}
