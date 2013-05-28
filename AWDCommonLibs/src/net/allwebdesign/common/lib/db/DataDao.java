package net.allwebdesign.common.lib.db;

import java.io.InputStream;

import javax.sql.DataSource;

/**
 * An interface for the Data Access Objects. Classes that behave as such
 * implement this interface
 * @author George Moraitakis
 * @version 1.4
 */
public interface DataDao {

	
	/**
	 * Execute an SQL and return a list of data of type datarow. It allows parametrised arguments
	 * @param sql the sql to execute
	 * @param args the args to pass
	 * @return the list of data
	 */
	public DataResults execSQL(String sql, Object... args);
	
	/**
	 * Execute an update SQL (Insert, Update, Delete)
	 * @param sql the sql to execute
	 * @param args the args to pass
	 */
	public void updateSQL(String sql, Object... args);
	
	/**
	 * Exec a sproc that returns a map of output params. I there is result set
	 * the data is stored in the output map with the key "dataRows"
	 * @param sprocSql the store proc sql 
	 * @param args the args to pass to the sproc
	 * @return the output params
	 */
	public DataResults execSproc(String sprocSql, Object... args);
	
	/**
	 * Gets the datasource assigned to this DAO
	 * @return the dataSource of this DAO
	 */
	public DataSource getDataSource ();
	
	/**
	 * Sets the DataSource assigned to this DAO
	 * @param datasource the datasoutce assigned
	 */
	public void setDataSource (DataSource datasource);
	
	
	
	/**
	 * Execute an SQL and return a list of data of type datarow that contains a blob
	 * The data returned should be purely blobs
	 * @param the SQL to execute
	 * @return a list of type DataRow
	 */
	public DataResults execBlobSQL(String sql, Object... args);
	
	/**
	 * Inserts into the database, data that contain a blob
	 * @param sql the query to use
	 * @param fileName the filename
	 * @param is the inputstream
	 * @param size the file size
	 * @param args the rest of the arguments
	 * @return the rows inserted or 0 if no rows inserted due to problem
	 */
	public int execBlobUpdateSQL(String sql, final InputStream is, final long size, final Object... args);

	
	
}
