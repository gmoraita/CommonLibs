/**
 * 
 */
package net.allwebdesign.common.lib.db.templates;


/**
 * @author George Moraitakis
 *
 */
public interface SQLTemplate {
	/**
	 * Shows all records of a table 
	 * @param table the table to use
	 * @return the sql to run
	 */
	public String showRecordsSql(String table);
	
	/**
	 * Select a specific record from a table. The id column must be named Id.
	 * @param table
	 * @param fields the fields to use in the where clause
	 * @return the sql to run
	 */
	public String selectRecordsSql(String table, String[] criteriaFields);
	
	/**
	 * Insert a record in a table
	 * @param table
	 * @param fields the fields to use
	 * @param flag the flag of fields to parse (e.g. row)
	 * @param the delimiter from the field name (e.g. _)
	 * @return the sql to run
	 */
	public String insertRecordSql(String table, String[] fields);
	
	/**
	 * Update a record
	 * @param table the table to use
	 * @param fields the list of fields
	 * @return the update sql
	 */
	public String updateRecordSql(String table, String[] fields);
	
	/**
	 * Copies all fields from one table to another. If filter is not null then specific records will be copied 
	 * @param tableOrigin the origin table
	 * @param tableTarget  the target table
	 * @param field the field to use to filter the rows to copy. Use null to copy all records 
	 * @return the sql to run
	 */
	public String copyToTableSql(String tableOrigin, String tableTarget, String field);
	
	/**
	 * Copies all fields from one table to another. 
	 * The where clause is a partial sql without the where keyword to which the whereClauseFields are passed
	 * @param tableOrigin the origin table
	 * @param tableTarget  the target table
	 * @param whereClause a partial sql without the where keyword to which the whereClauseFields are passed
	 * @return the sql to run
	 */
	public String copyToTableCustomSql(String tableOrigin, String tableTarget, String whereClause);
	
	
	/**
	 * Delete a record. The Id of the table must be declared in the table as "Id"
	 * @param table the table to delete from 
	 * @return the sql to run
	 */
	public String deleteRecordSql(String table);
	
	/**
	 * Delete a record. The Id of the table must be declared in the table as "Id"
	 * @param table the table to delete from 
	 * @param whereClause a partial sql without the where keyword to which the whereClauseFields are passed
	 * @return the sql to run
	 */
	public String deleteCustomSql(String table, String whereClause);
	
	
	/**
	 * Run a sproc
	 * @param sproc the name of a sproc
	 * @param fields the dynamic fields
	 * @param flag the flag of fields to parse (e.g. row)
	 * @param the delimiter from the field name (e.g. _)
	 * @param staticArgs static args to pass e.g. "@xyz = '123'" 
	 * @return the sql to run
	 */
	public String execSprocSql(String sproc, String[] fields);
	
	/**
	 * Open a transaction SQL
	 * @return the open transaction statement
	 */
	public String openTranSql();
	
	/**
	 * Commits a Transaction
	 * @return the commit transaction SQL
	 */
	public String commitTranSql();
	
	/**
	 * Rollback a Transaction
	 * @return the rollback transaction SQL
	 */
	public String rollbackTranSql();
	
	/**
	 * Get the id of the inserted column
	 * @return the get the Id of the insterted record
	 */
	public String getLastId();
	
	/**
	 * Assigns to a variable the last Id inserted
	 * @return the last Id inserted
	 */
	public String assignToVarLastId(String varName);
	
	/**
	 * Fetches the table definition 
	 * @param tableName
	 * @return the table definition
	 */
	public String fetchTableDefinition(String tableName);
	
	/**
	 * Fetches the table columns 
	 * @param tableName
	 * @return the table columns
	 */
	public String fetchTableColumns(String tableName);
	
}
