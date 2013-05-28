package net.allwebdesign.common.lib.db;

import java.util.Map;

import javax.sql.DataSource;

import net.allwebdesign.common.lib.db.templates.DBType;
import net.allwebdesign.common.lib.utils.Utils;




/**
 * GenericDataHandler is a convenience class that acts as an extension of the Abstract
 * DataHandler class. Use this class if required to instantiate a handler for db operations 
 * @author George Moraitakis
 * @version 1.1
 */
public class GenericDataHandler extends DataHandler {
	
	/**
	 * Default constructor
	 */
	public GenericDataHandler() {}
	
	/**
	 * Initializes with a dao
	 * @param dao the dao to pass
	 */
	public GenericDataHandler(DataDao dao) {
		super(dao);
		
	}
	
	/**
	 * Constructor that assigns the datasource to the handler
	 * @param dataSource the datasource to assign
	 */
	public GenericDataHandler(DataSource dataSource){
		this.dao = new GenericDataDao();
		dao.setDataSource(dataSource);
		super.setDao(dao);
	}
	
	/**
	 * Constructor that assigns the datasource to the handler
	 * @param dataSource the datasource to assign
	 * @param dbType the database type (see {@link net.allwebdesign.common.lib.db.templates.DBType})
	 */
	public GenericDataHandler(DataSource dataSource, DBType dbType){
		this.dao = new GenericDataDao();
		dao.setDataSource(dataSource);
		super.setDao(dao);
		super.setDbType(dbType);
	}
	
	/**
	 * Gets back the GenericData DAO of the object 
	 * @return get the GenericData DAO
	 */
	@Override
	public GenericDataDao getDao() {
		return (GenericDataDao)this.dao;
	}

	
	
	/**
	 * Get a result set in list form  after running a select query. It passes a
	 * custom sql query
	 * @param sql the custom sql query
	 * @return the list of data
	 */
	@Override
	public DataResults getDataList(String sql) {
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
	@Override
	public DataResults getDataListFromMap(String sql, Map<String,Object> args) {
		
		return getDataList(sql, DataProcessor.processDataMapIncludingId(args));
	}
	
		

	/**
	 * Get a result set in list form  after running a select query. It passes a
	 * custom sql query
	 * @param sql the custom sql query
	 * @param args the arguments to pass
	 * @return the list of data
	 */
	@Override
	public DataResults getDataList(String sql, Object... args) {
		if (dao == null){return null;}
		return dao.execSQL(sql, args);
	}		
	
	/**
	 * It carries out an insertion SQL. 
	 * @param sql the sql to run. Parameters should be in ?
	 * @param args the arguments to pass. They replace the ? of the SQL
	 */
	@Override
	public void doInsertSql(String sql, Object... args) {
		if (dao == null){return;}
		dao.updateSQL(sql, args);
	}	
	
	
	
	/**
	 * It carries out an update SQL.
	 * @param sql the sql to run. Parameters should be in ?
	 * @param args the arguments to pass. They replace the ? of the SQL
	 */
	@Override
	public void doUpdateSql(String sql, Object... args) {
		if (dao == null){return;}
		dao.updateSQL(sql, args);
	}	
	
	/**
	 * It carries out an update SQL. For convinience you can pass explicitely
	 * the id of the row to update
	 * @param id the id of the row to update  
	 * @param args the rest of the arguments to pass. They replace the ? of the SQL
	 */
	@Override
	public void doUpdate(String id, Object... args) {
		doUpdate(Utils.concatArgs(id, args));
	}	
	
	
	
	/**
	 * It carries out a delete SQL for a specific id
	 * @param sql the sql to run
	 * @param id the id of the row to delete
	 */
	@Override
	public void doDeleteSql(String sql, String id) {
		if (dao == null){return;}
		dao.updateSQL(sql, id);
	}

	

	
	
	

	
}
