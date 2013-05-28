package net.allwebdesign.common.lib.db;


import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import net.allwebdesign.common.lib.db.prepared.DBExec;
import net.allwebdesign.common.lib.db.prepared.DBTableDefinition;
import net.allwebdesign.common.lib.db.templates.DBType;
import net.allwebdesign.common.lib.db.templates.MSSQLServerSQLTemplate;
import net.allwebdesign.common.lib.db.templates.MySQLDBSQLTemplate;
import net.allwebdesign.common.lib.db.templates.ORACLESQLTemplate;
import net.allwebdesign.common.lib.db.templates.SQLTemplate;
import net.allwebdesign.common.lib.utils.Utils;

/**
 * Datahandler is an abstract class that needs to be extended in the applications
 * It acts as a wrapper class for accessing the core DAO.
 * @author George Moraitakis
 * @version 1.0
 */
public abstract class DataHandler {
	
	/**
	 * Qualifier for the action() method to do Select statements
	 */
	protected static final int SELECT = 1;
	/**
	 * Qualifier for the action() method to do Delete statements
	 */	
	protected static final int DELETE = 2;
	/**
	 * Qualifier for the action() method to do Update statements
	 */	
	protected static final int UPDATE = 3;
	/**
	 * Qualifier for the action() method to do Insert statements
	 */	
	protected static final int INSERT = 4;
	/**
	 * Qualifier for the action() method to do Sproc statements
	 */	
	protected static final int SPROC = 5;	
	
	
	/**
	 * The default row flag: "row"
	 */
	protected static final String ROW_FLAG = "row";
	
	/**
	 * the default id flag: id
	 */
	protected static final String ROW_ID = "Id";
	
	/**
	 * the default delimiter
	 */
	protected static final String ROW_DELIM = "_";
	
	/**
	 * the default flag for the total rows
	 */
	protected static final String ROW_NUM_FLAG = "numRows";
	
	/**
	 * the default flag for the total rows
	 */
	protected static final String HISTORYTABLE_SUFFIX = "Hist";
	
	
	// Below are for file upload stats and can be public
	public static final String LINES_LOADED = "linesLoaded";
	public static final String LINES_LOADING_TIME = "linesLoadingTime";
	public static final String LINES_SKIPPED = "linesSkipped";
	public static final String LINES_READ = "linesRead";
	public static final String LINES_MSG = "linesMessage";
	public static final String QUERY_ERROR = "QUERY_ERROR";

	private Properties queries;
	private Resource[] queryFiles;
	
	private SQLTemplate sqlTemplate;
	private DBType dbType = DBType.NODB;

	private String queryNameFilter;
	
	protected DataDao dao;
	private String selectSql;
	private String insertSql;
	private String updateSql;
	private String deleteSql;
	
	private DBExec dbExec;
	
	/**
	 * Default constructor
	 */
	public DataHandler(){}
	
	/**
	 * Loads the dao
	 * @param dao
	 */
	public DataHandler(DataDao dao){
		this.dao = dao;
	}
	
	
	/**
	 * Loads the DAO and the query files
	 * @param dao
	 * @param queryFiles
	 */
	public DataHandler(DataDao dao, Resource[] queryFiles){
		this.dao = dao;
		try{
			this.queries = Utils.loadProperties(queryFiles);
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Initialises the data handler by reading all resources containing queries and assigning them into a registry. 
	 */
	public void init(){
		// Parse the query files
		try{
			if (queryFiles != null){
				this.queries = Utils.filterProperties(Utils.loadProperties(queryFiles), this.queryNameFilter);
			}
			
		}catch (Exception e){
			e.printStackTrace();
		}
		// Set the db exec for advanced queries
		if (this.dbExec == null && dbType != DBType.NODB){
			dbExec = new DBExec(this.getDatasource(), dbType);
		}
		
	}
	
	/**
	 * Get a result set in list form  after running a select query
	 * @return The list of data
	 */
	protected DataResults getDataList() {
		if (dao == null){return null;}
		return dao.execSQL(selectSql);
	}
	
	/**
	 * Get a result set in list form  after running a select query. It passes a
	 * custom sql query
	 * @param sql the custom sql query
	 * @return the list of data
	 */
	protected DataResults getDataList(String sql) {
		if (dao == null){return null;}
		return dao.execSQL(sql);
	}	
	
	/**
	 * Get a result set in list form  after running a select query. It passes a
	 * custom sql query with args as a Map with the default row flag id i.e. "rowX_"
	 * @param sql the custom sql query
	 * @param args the arguments to pass
	 * @return the list of data
	 */
	protected DataResults getDataListFromMap(String sql, Map<String,Object> args) {
		
		return getDataList(sql, DataProcessor.processDataMapIncludingId(args));
	}
	
		

	/**
	 * Get a result set in list form  after running a select query. It passes a
	 * custom sql query
	 * @param sql the custom sql query
	 * @param args the arguments to pass
	 * @return the list of data
	 */
	protected DataResults getDataList(String sql, Object... args) {
		if (dao == null){return null;}
		return dao.execSQL(sql, args);
	}	
	
	/**
	 * Get the first data entry after running a select query. It passes a
	 * custom sql query
	 * @param sql the custom sql query
	 * @param args the arguments to pass
	 * @return the first data entry
	 */
	protected DataRow getDataEntry(String sql, Object... args) {
		
		DataResults results =  this.getDataList(sql, args);
		if (results.size()==0){return null;}
		
		return results.get(0);
	}	
	
	/**
	 * Get the first data entry  after running a select query. It passes a
	 * custom sql query with args as a Map with the default row flag id i.e. "row9_"
	 * @param sql the custom sql query
	 * @param args the arguments to pass
	 * @return the first data entry
	 */
	protected DataRow getDataEntry(String sql, Map<String,Object> args) {
		DataResults results =  this.getDataList(sql, DataProcessor.processDataMap(args));
		if (results.size()==0){return null;}
		
		return results.get(0);
	}
	
	/**
	 * It carries out a select SQL. 
	 * @param sql the sql to run. Parameters should be in ?
	 * @param args the arguments to pass. They replace the ? of the SQL
	 */
	protected DataResults doSelectSql(String sql, Object... args) {
		if (dao == null){return null;}
		return dao.execSQL(sql, args);
	}	
	
	
	/**
	 * It carries out an insertion SQL. 
	 * @param sql the sql to run. Parameters should be in ?
	 * @param args the arguments to pass. They replace the ? of the SQL
	 */
	protected void doInsertSql(String sql, Object... args) {
		if (dao == null){return;}
		dao.updateSQL(sql, args);
	}	
	
	/**
	 * It carries out an insertion SQL. It assumes the insertSQL 
	 * of the class has been set
	 * @param args the arguments to pass. They replace the ? of the SQL
	 */
	protected void doInsert(Object... args) {
		if (dao == null){return;}
		dao.updateSQL(insertSql, args);
	}
	
	/**
	 * It carries out an update SQL.
	 * @param sql the sql to run. Parameters should be in ?
	 * @param args the arguments to pass. They replace the ? of the SQL
	 */
	protected void doUpdateSql(String sql, Object... args) {
		if (dao == null){return;}
		dao.updateSQL(sql, args);
	}	
	
	/**
	 * It carries out an update SQL.
	 * @param sql the sql to run. Parameters should be in ?
	 * @param map a hashmap of values to pass. They replace the ? of the SQL
	 */
	protected void doUpdateSql(String sql, Map<String,Object> map) {
		Object[] args = DataProcessor.processDataMapIncludingId(map);
		doUpdateSql(sql, args);
		
	}	
	
	/**
	 * It carries out an update SQL. For convinience you can pass explicitely
	 * the id of the row to update
	 * @param id the id of the row to update  
	 * @param args the rest of the arguments to pass. They replace the ? of the SQL
	 */
	protected void doUpdate(String id, Object... args) {
		doUpdate(Utils.concatArgs(id, args));
	}	
	
	/**
	 * It carries out an update SQL. It assumes the updateSQL 
	 * of the class has been set
	 * @param args the arguments to pass. They replace the ? of the SQL
	 */
	protected void doUpdate(Object... args) {
		if (dao == null){return;}
		dao.updateSQL(updateSql, args);
	}
	
	/**
	 * It carries out a delete SQL for a specific id
	 * @param sql the sql to run
	 * @param id the id of the row to delete
	 */
	protected void doDeleteSql(String sql, String id) {
		if (dao == null){return;}
		dao.updateSQL(sql, id);
	}	
	
	/**
	 * It carries out a delete SQL for a specific id. Assumes the deleteSql 
	 * of the class has been set
	 * @param id the id of the row to delete
	 */
	protected void doDelete(String id) {
		if (dao == null){return;}
		dao.updateSQL(deleteSql, id);
	}		
	
	
	

	
	/**
	 * Processes a request containing multiple rows to be updated.
	 * @param map the map that contains the request parameters
	 */
	protected void processRequest(Map<String, Object> map){
		
		this.action(UPDATE, map);
		
	}

	/**
	 * Get the SQL for insert statements
	 * @return the insert sql 
	 */
	protected String getInsertSql() {
		return insertSql;
	}



	/**
	 * Sets the insert SQL
	 * @param insertSql the sql for insert
	 */
	protected void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}


	/**
	 * Get the SQL for update statements
	 * @return the update sql 
	 */
	protected String getUpdateSql() {
		return updateSql;
	}


	/**
	 * Sets the update SQL
	 * @param updateSql the sql for update
	 */
	protected void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}



	/**
	 * Get the SQL for delete statements
	 * @return
	 */
	protected String getDeleteSql() {
		return deleteSql;
	}



	/**
	 * Sets the delete SQL
	 * @param deleteSql the sql for delete
	 */
	protected void setDeleteSql(String deleteSql) {
		this.deleteSql = deleteSql;
	}


	/**
	 * Get the SQL for select statements
	 * @return
	 */

	protected String getSelectSql() {
		return selectSql;
	}

	/**
	 * Sets the select SQL
	 * @param selectSql the sql for select
	 */

	protected void setSelectSql(String selectSql) {
		this.selectSql = selectSql;
	}



	/**
	 * Sets the Database Access Object (DAO) that contains 
	 * instructions for connecting to the database
	 * @param dao the Database Access Object
	 */
	public void setDao(DataDao dao) {
		this.dao = dao;
	}


	/**
	 * Get the Database Access Object (DAO) that contains 
	 * instructions for connecting to the database. 
	 * @return the Database Access Object
	 */
	public DataDao getDao() {
		return dao;
	}
	
	
	/**
	 * Get the queries that were loaded
	 * @return the queries
	 */
	protected Properties getQueries() {
		return queries;
	}

	/**
	 * set the queries
	 * @param queries the queries
	 */
	protected void setQueries(Properties queries) {
		this.queries = queries;
	}

	/**
	 * The loaded property files
	 * @return the list of files to load
	 */
	public Resource[] getQueryFiles() {
		return queryFiles;
	}

	/**
	 * Set the query files 
	 * @param queryFiles the query files
	 */
	public void setQueryFiles(Resource[] queryFiles) {
		this.queryFiles = queryFiles;
	}
	

	
	/**
	 * Get the filter of the query names 
	 * @return the filter used
	 */
	public String getQueryNameFilter() {
		return queryNameFilter;
	}

	/**
	 * 
	 * @param queryNameFilter
	 */
	public void setQueryNameFilter(String queryNameFilter) {
		this.queryNameFilter = queryNameFilter;
		if (this.queries!= null){
			Utils.filterProperties(queries, queryNameFilter);
		}
	}

	/**
	 * Retrieve a query from the queries loaded
	 * @param queryName the name of the query
	 * @return the query or empty string if not found
	 */
	public String query(String queryName){
		if (queries!= null){
			return queries.getProperty(queryName);
		}
		return "";
	}
	
	/**
	 * Runs an data updating query (update, insert, delete) 
	 * @param queryName the query name which holds the query
	 * @param args the arguments of the query in the form of plain text
	 */
	public void runUpdate(String queryName, Object... args){
		this.doUpdateSql(this.query(queryName), args);
	}
	
	/**
	 * Runs an data updating query (update, insert, delete) 
	 * @param queryName the query name which holds the query
	 * @param args the arguments of the query in the form of a map of keys/values
	 */
	public void runUpdate(String queryName, Map<String, Object> args){
		this.doUpdateSql(this.query(queryName), args);
	}
	
	/**
	 * Runs a query and returns its results 
	 * @param queryName the query name which holds the query
	 * @param args the arguments of the query in the form of plain text
	 * @return the data results.  
	 */
	public DataResults runSql(String sqlStatement, Object... args){
		return this.doSelectSql(sqlStatement, args);
	}
	
	/**
	 * Runs a query and returns its results 
	 * @param queryName the query name which holds the query
	 * @param args the arguments of the query in the form of plain text
	 * @return the data results.  
	 */
	public DataResults runQuery(String queryName, Object... args){
		return this.getDataList(this.query(queryName), args);
	}
	/**
	 * Runs a query and returns its results 
	 * @param queryName the query name which holds the query
	 * @param args the arguments of the query in the form of a map of keys/values
	 * @return the data results.  
	 */
	public DataResults runQuery(String queryName, Map<String, Object> args){
		return this.getDataListFromMap(this.query(queryName), args);
	}
	
	/**
	 * Runs a sproc with arguments constructed from the keys of the arguments passed (typically form params) and additional args.
	 * It assumes the form params are of the type rowXX_<i>paramName</i>.
	 * @param sprocName the sproc name
	 * @param args the map of arguments
	 * @param staticArgs static args (e.g. \@param = 1234)  
	 * @return the results of the sproc
	 */
	public DataResults runSproc(String sprocName, Map<String, Object> args, Object... staticArgs ){
		return runSproc(sprocName, ROW_FLAG, ROW_DELIM, null, "@", args, staticArgs);

	}
	
	/**
	 * Runs a sproc with arguments constructed from the keys of the arguments passed (typically form params) and additional args.
	 * It assumes the form params are of the type <b>flag</b>0<b>delim</b><i>paramName</i> (e.g. testrow0_).
	 * It also assumes that the param symbol is @
	 * @param sprocName the sproc name
	 * @param flag the flag of the rows to use (e.g. row)
	 * @param delim the delim of the rows to use (e.g. _)	 
	 * @param argSymbol the symbol to use for the sproc param (typically @)
	 * @param args the map of arguments
	 * @param staticArgs static args (e.g. \@param = 1234)  
	 * @return the results of the sproc
	 */
	public DataResults runSproc(String sprocName, String flag, String delim, String row, Map<String, Object> args, Object... staticArgs ){
		
		return  runSproc(sprocName, flag, delim, null, "@", args, staticArgs );
	}
	
	
	/**
	 * Runs a sproc with arguments constructed from the keys of the arguments passed (typically form params) and additional args.
	 * It assumes the form params are of the type <b>flag</b>row<b>delim</b><i>paramName</i>.
	 * @param sprocName the sproc name
	 * @param flag the flag of the rows to use (e.g. row)
	 * @param delim the delim of the rows to use (e.g. _)
	 * @param row a specific row number to use. If null then assumes 0 (e.g. row0_). 
	 * @param argSymbol the symbol to use for the sproc param (typically @)
	 * @param args the map of arguments
	 * @param staticArgs static args (e.g. \@param = 1234)  
	 * @return the results of the sproc
	 */
	public DataResults runSproc(String sprocName, String flag, String delim, String row, String argSymbol, Map<String, Object> args, Object... staticArgs ){
		String sql = DataProcessor.constructSprocFromArgs(sprocName, flag, delim, row, argSymbol, args, staticArgs);
		Object[] dataRow = DataProcessor.processDataMap(args, flag, null, delim, row, false, false) ;
		return this.getDataList(sql, dataRow);
	}
	
	/**
	 * Runs an updating sproc with arguments constructed from the keys of the arguments passed (typically form params) and additional args.
	 * It assumes the form params are of the type rowXX_<i>paramName</i>.
	 * @param sprocName the sproc name
	 * @param args the map of arguments
	 * @param staticArgs static args (e.g. \@param = 1234)  
	 */
	public void runUpdateSproc(String sprocName, Map<String, Object> args, Object... staticArgs ){
		runUpdateSproc(sprocName, ROW_FLAG, ROW_DELIM, null, "@", args, staticArgs);

	}	
	
	/**
	 * Runs an updating sproc with arguments constructed from the keys of the arguments passed (typically form params) and additional args.
	 * It assumes the form params are of the type <b>flag</b>row<b>delim</b><i>paramName</i>.
	 * @param sprocName the sproc name
	 * @param flag the flag of the rows to use (e.g. row)
	 * @param delim the delim of the rows to use (e.g. _)
	 * @param row a specific row number to use. If null then assumes 0  (e.g. row0_).
	 * @param argSymbol the symbol to use for the sproc param (typically @)
	 * @param args the map of arguments
	 * @param staticArgs static args (e.g. \@param = 1234)  
	 */
	public void runUpdateSproc(String sprocName, String flag, String delim, String row, String argSymbol, Map<String, Object> args, Object... staticArgs ){
		String sql = DataProcessor.constructSprocFromArgs(sprocName, flag, delim, row, argSymbol, args, staticArgs);
		
		this.doUpdateSql(sql, args);
	}
	
	/**
	 * Runs a batch of update sprocs based on how many data rows are included in the args.
	 * It assumes the form params are of the type rowXX_<i>paramName</i>.
	 * @param sprocName the sproc name
	 * @param args the map of arguments 
	 * @param staticArgs static args (e.g. \@param = 1234)  
	 */
	public void runBatchUpdateSproc(String sprocName, Map<String, Object> args, Object... staticArgs ){
		runBatchUpdateSproc(sprocName, ROW_FLAG, ROW_DELIM, "@", args, staticArgs);

	}	
	
	/**
	 * Runs a batch of update sprocs based on how many data rows are included in the args.
	 * It assumes the form params are of the type <b>flag</b>row<b>delim</b><i>paramName</i>.
	 * @param sprocName the sproc name
	 * @param flag the flag of the rows to use (e.g. row)
	 * @param delim the delim of the rows to use (e.g. _)
	 * @param argSymbol the symbol to use for the sproc param (typically @)
	 * @param args the map of arguments
	 * @param staticArgs static args (e.g. \@param = 1234)  
	 */
	public void runBatchUpdateSproc(String sprocName, String flag, String delim, String argSymbol, Map<String, Object> args, Object... staticArgs ){
		
		Object[] rows = DataProcessor.parseDistinctRows(args, flag,delim); 
		for (int ri = 0 ; ri < rows.length; ri++){
			String row = (String)rows[ri]; 
			Object[] dataRow = DataProcessor.processDataMap(args, flag, null, delim, row, false, false) ;
			String sproc = DataProcessor.constructSprocFromArgs(sprocName, flag, delim, row, argSymbol, args, staticArgs);
			this.doUpdateSql(sproc, dataRow);
		}
		
	}
	
	/**
	 * It directly inserts into a table without the need to write sql. The table definition is loaded once from the db and stored into cache<BR>
	 * Also, for convenience you can ignore the id column (if it is an identity column) and the rectimestamp (if it inserts default timestamps). <BR/>
	 * It will not insert an explicit Id and a RecTimestamp values (assuming these fields exist) <BR/>
	 * Your Bean MUST have the fields in the SAME order as in the table
	 * @param tableName the table to do insert into
	 * @param bean the Bean (POJO) the current state (set of instance values) to use
	 * @throws Exception
	 */
	public <T> void runInsertTable(String tableName, T bean) throws Exception {
		
		
		this.runInsertTable(tableName, true, true, bean);
	
	}
	
	/**
	 * It directly inserts into a table without the need to write sql. The table definition is loaded once from the db and stored into cache<BR>
	 * Also, for convenience you can ignore the id column (if it is an identity column) and the rectimestamp (if it inserts default timestamps). <BR/>
	 * Your Bean MUST have the fields in the SAME order as in the table
	 * @param tableName the table to do insert into
	 * @param ignoreId to ignore the Id column (you must set to true if this is Identity or Sequence column)
	 * @param ignoreRecTimestamp to ignore the RecTimestamp if this is what you want (e.g. when this column has a default)
	 * @param bean the Bean (POJO) the current state (set of instance values) to use
	 * @throws Exception
	 */
	public <T> void runInsertTable(String tableName, boolean ignoreId, boolean ignoreRecTimestamp, T bean) throws Exception {
		
		
		this.runInsertTable(tableName, ignoreId, ignoreRecTimestamp, Utils.getBeanState(bean));
	
	}
	
	/**
	 * It directly inserts into a table without the need to write sql. The table definition is loaded once from the db and stored into cache<BR>
	 * Also, for convenience you can ignore the id column (if it is an identity column) and the rectimestamp (if it inserts default timestamps)
	 * @param tableName the table to do insert into
	 * @param ignoreId to ignore the Id column (you must set to true if this is Identity or Sequence column)
	 * @param ignoreRecTimestamp to ignore the RecTimestamp if this is what you want (e.g. when this column has a default)
	 * @param values the values to insert into each field. WARNING! The have to be in the same order as the table definition 
	 * @throws Exception if it was unable to do insert
	 */
	public void runInsertTable(String tableName, boolean ignoreId, boolean ignoreRecTimestamp, Object... values) throws Exception {
		
		
		DBTableDefinition tableDef = this.dbExec.getTableDef(tableName);
		
		if (tableDef == null){throw new Exception("Table "+tableName+" not found where I am looking!");}
		
		String sql = this.sqlTemplate.insertRecordSql(tableName, tableDef.getTableColumns(ignoreId, ignoreRecTimestamp));
		
		this.doInsertSql(sql, values);
	
	}
	
	/**
	 * Inserts the data of map into a table. Assumes the rows have the format rowX_[column name]
	 * @param tableName  the table to insert into
	 * @param dataMap the map the keys of which are the columns to restrict insert. The rest will be null or default (WARNING! do not omit any non-null column)
	 * @throws Exception if it was unable to do insert 
	 */
	public void runInsertTable(String tableName, Map<String,Object> dataMap) throws Exception {
		runInsertTable(tableName, ROW_FLAG, ROW_DELIM, dataMap);
	}
	
	/**
	 * Runs an automated insert to a table with arguments constructed from the keys of the datamap passed (typically form params).
	 * The method uses the flag and delim to parse the arguments required and sorts them alphabetically.<BR> 
	 * Following that, the table columns are sorted alphabetically and the two are combined to create the insert statement   
	 * By default it will ignore the Id column
	 * @param tableName the table to insert into
	 * @param flag the row flag e.g. row
	 * @param delim the delimiter e.g. _
	 * @param dataMap the map the keys of which are the columns to restrict insert. The rest will be null or default (WARNING! do not omit any non-null column)
	 * @throws Exception if it was unable to do insert
	 */
	public void runInsertTable(String tableName, String flag, String delim, Map<String,Object> dataMap) throws Exception {
		runInsertTable(tableName, ROW_FLAG, ROW_DELIM, null, dataMap);
	}
	
	/**
	 * Runs an automated insert to a table with arguments constructed from the keys of the datamap passed (typically form params).
	 * The method uses the flag and delim to parse the arguments required and sorts them alphabetically.<BR> 
	 * Following that, the table columns are sorted alphabetically and the two are combined to create the insert statement   
	 * By default it will ignore the Id column
	 * @param tableName the table to insert into
	 * @param flag the row flag e.g. row
	 * @param delim the delimiter e.g. _
	 * @param the row to grab
	 * @param dataMap the map the keys of which are the columns to restrict insert. The rest will be null or default (WARNING! do not omit any non-null column)
	 * @throws Exception if it was unable to do insert
	 */
	public void runInsertTable(String tableName, String flag, String delim, String row, Map<String,Object> dataMap) throws Exception {
		
		
		DBTableDefinition tableDef = this.dbExec.getTableDef(tableName);
		
		if (tableDef == null){throw new Exception("Table "+tableName+" not found where I am looking!");}
		
		String sql = this.sqlTemplate.insertRecordSql(tableName, tableDef.getTableColumns(dataMap.keySet().toArray(new String[0]), flag, delim,row,true,true));
		
		Object[] dataRow = DataProcessor.processDataMap(dataMap, flag, null, delim, row, false, false) ;
		this.doInsertSql(sql, dataRow);
	
		
	}	
	/**
	 * Runs a simple select * from a table using a custom where clause
	 * @param tableName the table to use
	 * @param selectWhereSql the custom where clause. If null it reverts to {@link dbShowRecords} and shows all records
	 * @param selectWhereParams the params to pass to where clause. If the latter is null there have no effect
	 * @return the records of the table selected
	 * @throws Exception if there was a problem to run the query
	 */
	public DataResults runTableSelect(String tableName, String selectWhereSql, Object... selectWhereParams) {
		
		if (selectWhereSql == null){
			return this.dbShowRecords(tableName);
		}
		
		String sql = "SELECT * FROM "+tableName+" WHERE "+ selectWhereSql; 
		
		return this.getDataList(sql, selectWhereParams);
	
		
	}
	
	
	/**
	 * Will run a table update for a row included in the  dataMap. You have the option
	 * to put the data in a history table named [tableName]Hist before updating.
	 * @param tableName the name of the table to update
	 * @param dataMap dataMap the map containing the data
	 * @throws Exception if it was unable to do the update
	 */
	public void runUpdateTableWithHist(String tableName, Map<String, Object> dataMap) throws Exception{
		runUpdateTable(tableName, tableName+DataHandler.HISTORYTABLE_SUFFIX,  ROW_FLAG, ROW_DELIM, dataMap);
	}
	
	/**
	 * Will run a table update for a row included in the  dataMap with the name rowX_Id. Assumes the row has the format rowX_<colName>
	 * @param tableName the name of the table to update
	 * @param dataMap the map containing the data
	 * @throws Exception if it was unable to do the update
	 */
	public void runUpdateTable(String tableName, Map<String, Object> dataMap) throws Exception{
		runUpdateTable(tableName, null, ROW_FLAG, ROW_DELIM, dataMap);
	}
	
	/**
	 * Will run a table update for a row included in the  dataMap. You have the option
	 * to put the data in a history table before updating.
	 * @param tableName the name of the table to update
	 * @param historyTableName the name of the history table name
	 * @param flag the row flag e.g. row
	 * @param delim the row delimiter e.g. _
	 * @param dataMap the map containing the data
	 * @throws Exception if it was unable to do the update
	 */
	public void runUpdateTable(String tableName, String historyTableName, String flag, String delim,  Map<String, Object> dataMap) throws Exception {
	
		DBTableDefinition tableDef = this.dbExec.getTableDef(tableName);
		
		
		if (tableDef == null){throw new Exception("Table "+tableName+" not found where I am looking!");}
		if (historyTableName != null){
			DBTableDefinition tableDefHist = this.dbExec.getTableDef(historyTableName);
			if (tableDefHist == null){throw new Exception("Table "+historyTableName+" not found where I am looking!");}
			
			String sqlHist = this.sqlTemplate.copyToTableSql(tableName,historyTableName, DataHandler.ROW_ID);
			this.doInsertSql(sqlHist,this.extractIdValue(dataMap, ROW_ID, flag, delim));
		}
		
		
		String sql = this.sqlTemplate.updateRecordSql(tableName, tableDef.getTableColumns(dataMap.keySet().toArray(new String[0]), flag, delim,true,true));
		Object[] dataRow = DataProcessor.processDataMapIncludingId(dataMap, true) ;
		
		this.doUpdateSql(sql, dataRow);
		
		
	}	
	
	/**
	 * Batch Insert data into a table from a data map containing fields in the form 
	 * row[0-9]_[fieldName] 
	 * @param tableName the table name to insert into 
	 * @param dataMap the data map containing the values. The keys must be of the form row[0-9]_[fieldName] and the fieldName match the table fields
	 * @throws Exception if not able to insert
	 */
	public void runBatchInsert(String tableName, Map<String, Object> dataMap) throws Exception {
		this.runBatchInsert(tableName, ROW_FLAG, ROW_DELIM, dataMap);
		
	}
	
	/**
	 * Batch Insert data into a table from a data map containing fields in a specifiv format
	 * [flag][0-9][delim][fieldName]
	 * @param tableName the table name to insert into 
	 * @param flag the row flag e.g. row
	 * @param delim the row delimiter e.g. _
	 * @param dataMap the data map containing the values. The keys must be of the form [flag][0-9][delim][fieldName] and the fieldName match the table fields
	 * @throws Exception  if not able to insert
	 */
	public void runBatchInsert(String tableName, String flag, String delim, Map<String,Object> dataMap) throws Exception{
		Object[] rows = DataProcessor.parseDistinctRows(dataMap, flag,delim); 
		for (int ri = 0 ; ri < rows.length; ri++){
			String row = (String)rows[ri]; 
			
			this.runInsertTable(tableName, flag, delim, row, dataMap);
			
			
		}
	}
	/**
	 * Replaces the data of a table with new one based on criteria. 
	 * It will copy the replaced data to a history table named [tableName]Hist  
	 * @param tableName the table to use
	 * @param dataMap the data map containing the new data (typically the data of a form). Must contain keys of the form row[0-9]_[dataField]
	 * @param delWhereSql a partial sql without the where keyword to select the data to replace and to which the values are passed
	 * @param delCriteria the fields passed to the delWhereClause 
	 * @throws Exception if the replace does not work
	 */
	public void runBatchReplaceWithHist(String tableName, Map<String, Object> dataMap, String delWhereSql, Object... delCriteria) throws Exception {
		this.runBatchReplace(tableName, tableName+HISTORYTABLE_SUFFIX, dataMap, ROW_FLAG, ROW_DELIM, delWhereSql, delCriteria);
	}
	
	/**
	 * Replaces the data of a table with new one based on criteria. The replaced data are lost
	 * @param tableName the table to use
	 * @param dataMap the data map containing the new data (typically the data of a form). Must contain keys of the form row[0-9]_[dataField]
	 * @param delWhereSql a partial sql without the where keyword to select the data to replace and to which the values are passed
	 * @param delCriteria the fields passed to the delWhereClause 
	 * @throws Exception if the replace does not work
	 */
	public void runBatchReplace(String tableName, Map<String, Object> dataMap, String delWhereSql, Object... delCriteria) throws Exception {
		this.runBatchReplace(tableName, null, dataMap, ROW_FLAG, ROW_DELIM, delWhereSql, delCriteria);
	}
		
	
	/**
	 * Replaces the data of a table with new one based on criteria. Optionally it can copy the replaced data 
	 * to a history table  
	 * @param tableName the table to use
	 * @param historyTableName the history table to put data in. If null then it will not store the replaced data
	 * @param dataMap the data map containing the new data (typically the data of a form)
	 * @param flag the row flag in the data map e.g. row
	 * @param delim the delimiter of the rows e.g. _
	 * @param delWhereSql a partial sql without the where keyword to select the data to replace and to which the values are passed
	 * @param delCriteria the fields passed to the delWhereClause 
	 * @throws Exception if the replace does not work
	 */
	public void runBatchReplace(String tableName, String historyTableName, Map<String, Object> dataMap, String flag, String delim, String delWhereSql, Object... delCriteria) throws Exception {
		
		String histSql = this.sqlTemplate.copyToTableCustomSql(tableName, tableName+HISTORYTABLE_SUFFIX, delWhereSql);
		String delSql = this.sqlTemplate.deleteCustomSql(tableName, delWhereSql);

		// archive data to delete
		if (historyTableName != null){
			this.doInsertSql(histSql, delCriteria);
		}
		
		// delete the data
		this.doUpdateSql(delSql, delCriteria);
		
		// Insert the new data
		this.runBatchInsert(tableName, dataMap);
		
		
		
	}
	
	/**
	 * Run an action of specific type, for a set of rows as defined from a 
	 * map containing the request. Each row must have the parameter names starting with 
	 * a tag "row" followed by a number e.g. row0_surname. Also the id parameter 
	 * must be named rowX_Id e.g. row0_Id. By default the sql that will run is that declared by 
	 * the insertSql field.
	 * @param type the type of SQL. Possible values: INSERT, UPDATE and SPROC. All will exclude the Id column except from the sproc
	 * @param map the map containing the data
	 */
	protected void action(int type, Map<String, Object> map){
		this.action(type, map, null);
	}
	
	/**
	 * Run an action of specific type, for a SET OF ROWS as defined from a 
	 * map containing the request. Each row must have the parameter names starting with 
	 * a tag "row" followed by a number e.g. row0_surname. Also the id parameter 
	 * must be named rowX_Id e.g. row0_Id.  
	 * NOTE: For single rows use directly the doUpdate and doInsert
	 * @param type the type of SQL. Possible values: INSERT, UPDATE and SPROC. All will exclude the Id column except from the sproc
	 * @param map the map containing the data
	 * @param sql the custom SQL to run
	 */
	protected void action(int type, Map<String, Object> map, String sql){

		
		if (sql == null){
			sql = this.insertSql;
		}
		
		Object[] rows = DataProcessor.parseDistinctRows(map, ROW_FLAG,ROW_DELIM); 
		for (int ri = 0 ; ri < rows.length; ri++){
			String i = (String)rows[ri]; 
			
			switch (type){
	
				case UPDATE:
				{ 
					Object[] dataRow = DataProcessor.processDataMapOfRow(i, map);
					if (dataRow == null || dataRow.length == 0){continue;}
					if (sql == null){
						this.doUpdate(
							(String)map.get(ROW_FLAG+i+ROW_DELIM+ROW_ID), 
							dataRow
						);
					}else{
						this.doUpdateSql(
								sql,
								(String)map.get(ROW_FLAG+i+ROW_DELIM+ROW_ID), 
								dataRow
							);
					}
					
				}
				break;
				case INSERT: {
					Object[] dataRow = DataProcessor.processDataMapOfRow(i, map);
					if (dataRow == null || dataRow.length == 0){continue;}
					if (sql == null){
						this.doInsert(dataRow);
					}
					else{
						this.doInsertSql(sql, dataRow);
					}
				}
				break;

				case SPROC: {
					Object[] dataRow = DataProcessor.processDataMapOfRowIncludingId(i, map);
					if (dataRow == null || dataRow.length == 0){continue;}
					this.doUpdateSql(sql, dataRow);
				}
				break;
				default:{}
			}
			
			
		}
			
		
		
	}
	
	
	/**
	 * Execute an SQL and return a list of data of type datarow that contains a blob
	 * The data returned should be purely blobs
	 * @param the SQL to execute
	 * @return a list of type DataRow
	 */
	protected DataResults getBlob(String sql){
		
		return (this.getDao()).execBlobSQL(sql);
		
		
	}
	
	/**
	 * Execute an SQL and return a list of data of type datarow that contains a blob
	 * The data returned should be purely blobs
	 * @param the SQL to execute
	 * @param the arguments to pass
	 * @return a list of type DataRow
	 */
	protected DataResults getBlob(String sql, Object... args){
		return (this.getDao()).execBlobSQL(sql,args);
		
	}
	
	/**
	 * Gets an output stream and loads it with a blob
	 * @param out the output stream to use
	 * @param sql the sql to use
	 */
	protected void loadBlobStream(OutputStream out, String sql){
		DataProcessor.renderBlob(this.getDao().execBlobSQL(sql),out);
		
		
	}
	
	/**
	 * Gets an output stream and loads it with a blob
	 * @param out the output stream to use
	 * @param sql the sql to use
	 * @param args the sql arguments
	 */	
	protected void loadBlobStream(OutputStream out, String sql, Object... args){
		DataProcessor.renderBlob(this.getDao().execBlobSQL(sql,args),out);
		
	}	
	
	
	/**
	 * Uploads data that contain a blob. You have to ensure the first argument of the query is the blob 
	 * @param sql the sql to use
	 * @param fileName the filename 
	 * @param is the input stream
	 * @param size the size of the file
	 * @param args the rest of the data
	 */
	protected int uploadBlob(String sql, InputStream is, long size, Object... args){
		return this.getDao().execBlobUpdateSQL(sql, is, size, args);
		
	}	
	
	/**
	 * Convenience method to execute a cursor sproc in Oracle with a single output cursor 
	 * the elements of which are returned in a list of DataRows
	 * @param sql the sproc SQL 
	 * @param args the input arguments. They should be in the same order as in sproc
	 * @return the elements of cursor are returned in a list of DataRows
	 */
	@SuppressWarnings("unchecked")
	protected DataResults execSingleCursorSproc(String sql, Object... args){
		return DataResults.parseResults((List<DataRow>) execCursorSproc(sql, 1, args).get("cursor_0"));
	}
	
	/**
	 * Executes a cursor sproc in Oracle. Takes as input string arguments and returns a map of cursors.<B/> 
	 * The number of cursors returned is defined as a parameter <P/>
	 * The returned cursors are mapped as cursor_0, cursor_1 etc.
	 * @param sql the sproc SQL
	 * @param outputCursors the number of output cursors
	 * @param args the input arguments. They should be in the same order as in sproc
	 * @return a map with the returned cursors. They are in the same order as in sproc
	 */
	protected Map<String, Object> execCursorSproc(String sql, int outputCursors, Object... args){
	
		GenericStoredProcedure sproc = new GenericStoredProcedure(this.getDatasource(),sql);
		
		Map<String, Object> input = new HashMap<String, Object>();
				
		// assign the params
		for (int i = 0 ; i < args.length; i++){
			sproc.addInStringParam("p"+i);
			input.put("p"+i, args[i] );
		}
	
		// now add the output cursor
		for (int i = 0 ; i < outputCursors; i++){
			sproc.addOutCursorParam("cursor_"+i);
		}
		
		return sproc.execute(input);
		
		
	}
	
	
	

	
	
	/**
	 * Convinience method that returns the DAOs datasource. 
	 * @return the DAOs datasource
	 */
	protected DataSource getDatasource(){
		return this.dao.getDataSource();
	}

	
	/**
	 * Uploads file data row by row. 
	 * @param loadSql the sql to use for each line to do the upload
	 * @param deleteSql run this to delete previous data. Leave null to not to do anything
	 * @param is the input stream containing the data
	 * @param startLine the parsing will start from this line
	 * @param fieldDelimiter the delimiter that separates fields in the line
	 * @param expectedFieldsNum a safety variable that is used to validate that each line has the right number of fields 
	 * @param doRollBackOnError if true, upon encountering an error all data loaded will be rolled back
	 */
	protected Map<String,String> uploadDataFile(String loadSql, String preProcessSql, InputStream is, int startLine, String fieldDelimiter, int expectedFieldsNum, boolean doRollBackOnError){
		
		Map<String, String> loadStats = new TreeMap<String,String>();
		Scanner scanner = new Scanner(is, "ISO-8859-7");
		int line = 0;
		int totalLines = 0;
		int skippedLinesDueToErrors = 0;
		List<String[]> rows = new ArrayList<String[]>();
		long t0 = System.currentTimeMillis();
		try{
			
			
			
			// first let's read the file and load the liens read to a list of rows
			while (scanner.hasNextLine()){
				
				// skip the first lines
				String[] fields = scanner.nextLine().split(fieldDelimiter);
				line++;
			    if (line < startLine){continue;}
			    
			    // start counting
			    totalLines++;
				if (fields.length != expectedFieldsNum){
					org.apache.log4j.Logger.getRootLogger().info("Ignoring line since the fields number found ("+fields.length+") does not match the fields expected ("+expectedFieldsNum+")");
					skippedLinesDueToErrors++;
					if (doRollBackOnError){
						loadStats.put(DataHandler.LINES_READ, Integer.toString(totalLines));
						loadStats.put(DataHandler.LINES_SKIPPED, Integer.toString(0));
						loadStats.put(DataHandler.LINES_LOADED, Integer.toString(0));
						loadStats.put(DataHandler.LINES_MSG, "Line "+totalLines+" has incorrect number of fields. Expected "+expectedFieldsNum+" but found "+fields.length+" !");
						return loadStats;
					}
					else{
						continue;
					}
				}
				
				rows.add(fields);
				

				

			}
			
			
		}
		catch(Exception e){
			
			e.printStackTrace();
		}
		finally{
			if (scanner != null){
				scanner.close();
			}
		}
	
		loadStats.put(DataHandler.LINES_READ, Integer.toString(totalLines));
		loadStats.put(DataHandler.LINES_SKIPPED, Integer.toString(skippedLinesDueToErrors));
		
		// now let's load the file
		
		dbLoad(loadSql, preProcessSql, rows, loadStats, doRollBackOnError);

		long t1 = System.currentTimeMillis();
		double totalTime = (t1 - t0) / 60;
		
		loadStats.put(LINES_LOADING_TIME, Double.toString(totalTime));
		
		//add now the return with the stats
		return loadStats;
		
		
	}
	
	///////////////////////////////////////////////////
	///////////////////////////////////////////////////
	//Here start the static methods	
	///////////////////////////////////////////////////
	///////////////////////////////////////////////////
	
	
	private boolean dbLoad(String loadSql, String preProcessSql, final List<String[]> dataRows, Map<String, String> loadStats, boolean doRollBackOnError){
		
		boolean isSuccessfull = false;
		DataSourceTransactionManager tran = new DataSourceTransactionManager();
		tran.setDataSource(dao.getDataSource());
		DefaultTransactionDefinition td = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
		td.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
		td.setTimeout(1000);
		TransactionStatus status = tran.getTransaction(td); 

		//SimpleJdbcTemplate jt = new SimpleJdbcTemplate(dataSource);
		int rowsInserted = 0;
		try{
			
			if (preProcessSql != null){
				dao.updateSQL(preProcessSql);
			}
			
			
			
			

			for (int i = 0; i < dataRows.size(); i++){
				try{
					dao.updateSQL(loadSql, (Object[])dataRows.get(i));
					rowsInserted ++;
				}catch(Exception e){
					e.printStackTrace();
					Logger.getAnonymousLogger().severe("Could not insert row "+i);
					if (doRollBackOnError){
						throw e;
					}
					
				}
				if (rowsInserted%500 == 0){
					Logger.getAnonymousLogger().info("Loaded "+rowsInserted+" rows");
				}
				
			}
			
			
			isSuccessfull = true;
			loadStats.put(DataHandler.LINES_LOADED, Integer.toString(rowsInserted));
			
		} catch(Exception e){
			Logger.getAnonymousLogger().severe("Problem loading to the database target");
			e.printStackTrace();
			
			if (doRollBackOnError){
				tran.rollback(status);
				loadStats.put(LINES_MSG, "Unable to load line "+(rowsInserted+1)+". All changes have been rolled back");
				loadStats.put(LINES_LOADED, Integer.toString(0));
				return false;
			}
			else{
				loadStats.put(LINES_MSG, loadStats.get(LINES_MSG)+"<BR/>Unable to load line "+(rowsInserted+1)+".<BR/>");
				isSuccessfull = false;
			}
			
			
		}
		tran.commit(status);
		if (isSuccessfull){
			loadStats.put(LINES_MSG, "Successfully processed all data in the file.");
		}
		return isSuccessfull;
		
	}
	
	
	
	
	/**
	 * Extracts the id value from a map
	 * @param dataMap the data Map
	 * @param idCol the ID column to use
	 * @param flag the flag of the row e.g. row
	 * @param delim the delimiter of the row e.g. row
	 * @return the value of the ID col
	 */
	protected Object extractIdValue(Map<String,Object> dataMap, String idCol, String flag, String delim){
		Iterator<Entry<String, Object>> it = dataMap.entrySet().iterator();
		String matchPattern = "^"+flag+"[0-9]+"+delim;

		while (it.hasNext()) {
			 Map.Entry<String,Object> pairs = (Map.Entry<String,Object>)it.next();
			 if (idCol != null && pairs.getKey().matches(matchPattern+idCol+"$")){
				 return pairs.getValue();
			 }
			 
		}
		return null;
		
		
		
	}
	

	
	
	/*************************************************
	 * NEW STYLE
	 *************************************************/
	
	
	/**
	 * Shows all records of a table
	 * @param table the table name
	 * @return the data
	 */
	public DataResults dbShowRecords(String table){
		if (sqlTemplate == null){
			return null;
		}
		return this.getDataList(sqlTemplate.showRecordsSql(table));
		
	}

	/**
	 * Fetches a record by its Id
	 * @param table the table name
	 * @param id the id of the table (the field must be named Id)
	 * @return the data
	 */
	public DataResults dbFetchRecordById(String table, String id){
		if (sqlTemplate == null){
			return null;
		}
		String[] idParam = {"Id"};
		
		return this.getDataList(sqlTemplate.selectRecordsSql(table, idParam), id);
		
	}
	
	/**
	 * Fetches records by specific criteria
	 * @param table the table name
	 * @param criteria a map of criteria and values
	 * @return the data
	 */
	public DataResults dbFetchSpecificRecords(String table, QueryQualifierMap criteria){
		if (sqlTemplate == null){
			return null;
		}
		return this.getDataList(sqlTemplate.selectRecordsSql(table, criteria.keyArray()), criteria.values().toArray());
		
		
	}
	
	
	/**
	 * Shows all records of a table
	 * @param table the table name
	 * @return table
	 */
	public String dbInsertRecord(String table, Map<String,Object> dataToInsert, String flag, String delim){
		if (sqlTemplate == null){
			return "ERROR: No db type set";
		}
		
		Map<String,Object> cleanData = DataProcessor.filterData(dataToInsert, flag, delim);
		this.doUpdateSql(sqlTemplate.insertRecordSql(table, cleanData.keySet().toArray(new String[0])), cleanData);
		
		return "SUCCESS";
	}
	
	/**
	 * Shows all records of a table
	 * @param table the table name
	 * @return table
	 */
	public String dbUpdateRecord(String table, Map<String,Object> dataToInsert, String flag, String delim, String id){
		if (sqlTemplate == null){
			return "ERROR: No db type set";
		}
		
		Map<String,Object> cleanData = DataProcessor.filterData(dataToInsert, flag, delim);
		this.doUpdateSql(sqlTemplate.updateRecordSql(table, cleanData.keySet().toArray(new String[0])), cleanData);
		
		return "SUCCESS";
	}
	
	
	
	/**
	 * 
	 * @return the dbType
	 */
	public DBType getDbType() {
		return dbType;
	}

	/**
	 * @param dbType the dbType to set
	 */
	public void setDbType(DBType dbType) {
		this.dbType = dbType;
		
		switch(dbType){
		
		case MSSQL:
			this.sqlTemplate = new MSSQLServerSQLTemplate();
			break;
		case MYSQL:
			this.sqlTemplate = new MySQLDBSQLTemplate();
			break;
		case ORACLE:
			this.sqlTemplate = new ORACLESQLTemplate();
			
		case NODB:
			this.sqlTemplate = null;
			break;
		}
		
		
		
	}
	

	
	
	

	/**
	 * @return the sqlTemplate
	 */
	public SQLTemplate getSqlTemplate() {
		return sqlTemplate;
	}

	/**
	 * @param sqlTemplate the sqlTemplate to set
	 */
	public void setSqlTemplate(SQLTemplate sqlTemplate) {
		this.sqlTemplate = sqlTemplate;
	}

	/**
	 * @return the dbExec
	 */
	public DBExec getDbExec() {
		return dbExec;
	}

	/**
	 * @param dbExec the dbExec to set
	 */
	public void setDbExec(DBExec dbExec) {
		this.dbExec = dbExec;
	}
	
	
	
	
	
}
