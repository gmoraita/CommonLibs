package net.allwebdesign.common.lib.db;

import net.allwebdesign.common.lib.db.templates.SQLTemplate;

/**
 * Creates a repository of the SQL to generate
 * @author g_n_morait
 *
 */
public class SQLSession {
	
	private SQLTemplate sqlTemplate;
	private StringBuffer sqlGenerated; 
	
	/**
	 * Sets a new session
	 * @param sqlTemplate the sql template to use
	 */
	public SQLSession(SQLTemplate sqlTemplate){
		this.sqlTemplate = sqlTemplate;
		sqlGenerated = new StringBuffer();
	}

	/**
	 * Shows all records of a table 
	 * @param table the table to use
	 */
	public void showRecordsSql(String table){
		this.sqlGenerated.append(sqlTemplate.showRecordsSql(table)).append("\n");
	}
	
	/**
	 * Select a specific record from a table. The id column must be named Id.
	 * @param table
	 * @param fields the fields to use in the where clause
	 */
	public void selectRecordsSql(String table, String[] criteriaFields){
		this.sqlGenerated.append(sqlTemplate.selectRecordsSql(table, criteriaFields)).append("\n");
	}
	
	/**
	 * Insert a record in a table
	 * @param table
	 * @param fields the fields to use
	 */
	public void insertRecordSql(String table, String[] fields){
		this.sqlGenerated.append(sqlTemplate.insertRecordSql(table, fields)).append("\n");
	}
	
	/**
	 * Update a record
	 * @param table
	 * @param fields
	 * @param id the Id of the table must be declared in the table as "Id"
	 * @param flag the flag of fields to parse (e.g. row)
	 * @param the delimiter from the field name (e.g. _)
	 */
	public void updateRecordSql(String table,  String[] fields){
		this.sqlGenerated.append(sqlTemplate.updateRecordSql(table, fields)).append("\n");
	}
	
	/**
	 * Delete a record. The Id of the table must be declared in the table as "Id"
	 * @param table
	 * @param id t
	 */
	public void deleteRecordSql(String table){
		this.sqlGenerated.append(sqlTemplate.deleteRecordSql(table)).append("\n");
	}
	
	/**
	 * Run a sproc
	 * @param sproc the name of a sproc
	 * @param fields the dynamic fields
	 * @param flag the flag of fields to parse (e.g. row)
	 * @param the delimiter from the field name (e.g. _)
	 * @param staticArgs static args to pass e.g. "@xyz = '123'" 
	 */
	public void execSprocSql(String sproc, String[] fields){
		this.sqlGenerated.append(sqlTemplate.execSprocSql(sproc, fields)).append("\n");
	}
	
	/**
	 * Open a transaction SQL
	 */
	public void openTranSql(){
		this.sqlGenerated.append(sqlTemplate.openTranSql()).append("\n");
	}
	
	/**
	 * Commits a Transaction
	 */
	public void commitTranSql(){
		this.sqlGenerated.append(sqlTemplate.commitTranSql()).append("\n");
	}
	
	/**
	 * Rollback a Transaction
	 */
	public void rollbackTranSql(){
		this.sqlGenerated.append(sqlTemplate.rollbackTranSql()).append("\n");
	}
	
	/**
	 * Get the id of the inserted column
	 */
	public void getLastId(){
		this.sqlGenerated.append(sqlTemplate.getLastId()).append("\n");
	}
	
	/**
	 * Assigns to a variable the last Id inserted
	 */
	public void assignToVarLastId(String varName){
		this.sqlGenerated.append(sqlTemplate.assignToVarLastId(varName)).append("\n");
	}
	
	/**
	 * Copies all fields from one table to another
	 * @param tableOrigin
	 * @param tableTarget
	 * @param field the field to use to filter the rows to copy. Use null to copy all records 
	 */
	public void copyToTableSql(String tableOrigin, String tableTarget, String field)
	{
		this.sqlGenerated.append(sqlTemplate.copyToTableSql(tableOrigin, tableTarget, field)).append("\n");
	}
	
	/**
	 * Generate the SQL deposited in this session
	 * @return the sql
	 */
	public String generateSql(){
		return this.sqlGenerated.toString();
	}
	
	
	/**
	 * Get the SQL Template Object assigned
	 * @return the sqlTemplate
	 */
	public SQLTemplate getSqlTemplate() {
		return sqlTemplate;
	}

	/**
	 * Set the SQL Template
	 * @param sqlTemplate the sqlTemplate to set
	 */
	public void setSqlTemplate(SQLTemplate sqlTemplate) {
		this.sqlTemplate = sqlTemplate;
	}

	
	
	
}
