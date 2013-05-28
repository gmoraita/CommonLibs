package net.allwebdesign.common.lib.db.prepared;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import net.allwebdesign.common.lib.db.DataResults;
import net.allwebdesign.common.lib.db.GenericDataHandler;

import net.allwebdesign.common.lib.db.templates.DBType;
import net.allwebdesign.common.lib.db.templates.SQLTemplateFactory;

public class DBExec {

	
	private DataSource datasource;

	private DBType dbType;
	private DBTableCache dbCache;

	/**
	 * Constructor that initializes with the data source and the dbType
	 * @param ds the datasource to use
	 * @param dbType the {@link net.allwebdesign.common.lib.db.templates.DBType}
	 */
	public DBExec(DataSource ds, DBType dbType){
		this.datasource = ds;
		this.dbType = dbType;
		this.dbCache = new DBTableCache();
		
	}
	
	/**
	 * Get the table definition either from db (if not in dbCache) or from dbCache. You must already
	 * have set the datasource or it will return null
	 * @param tableName the name of the table
	 * @param ignoreColumns the list of columns to exclude from the table definition. Leave empty if you want to include all
	 * @return the table definition or null if not found or if the datasource is null
	 */
	public DBTableDefinition getTableDef(String tableName)  {
		DBTableDefinition tableDef = null;
		
		// let's try the dbCache first
		try {
			tableDef = getTableDefFromCache(tableName);
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		// try the db if not in dbCache
		if (tableDef == null){
			if (this.datasource == null){return null;}
			GenericDataHandler dataHandler = new GenericDataHandler(this.datasource);
			DataResults tableColumns = dataHandler.getDataList(SQLTemplateFactory.getSQLTemplate(dbType).fetchTableColumns(tableName));
			tableDef = DBTableDefinition.getInstance(tableName, tableColumns);
			
			// and put in dbCache
			this.putTableDefToCache(tableDef);
			
			Logger.getLogger("AppLogger").log(Level.INFO, "Got table from db and added to dbCache");
		}
	
		return tableDef;
	}
	
	/**
	 * Puts a table definition into dbCache
	 * @param tableDef the table definition object
	 * @throws IllegalArgumentException if we have passed an illegal object
	 */
	private void putTableDefToCache(DBTableDefinition tableDef) throws IllegalArgumentException {
		this.dbCache.putInCache(tableDef);
		
	}
	
	/**
	 * Gets a table definition from dbCache
	 * @param tableName the name of the table
	 * @return the table definition
	 * @throws IllegalArgumentException if we have passed an illegal object
	 */
	private DBTableDefinition getTableDefFromCache(String tableName) throws IllegalArgumentException {
		return this.dbCache.getFromCache(tableName);
		
	}
	
	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	

	public DBType getDbType() {
		return dbType;
	}

	public void setDbType(DBType dbType) {
		this.dbType = dbType;
	}

	public DBTableCache getDbCache() {
		return dbCache;
	}

	public void setDbCache(DBTableCache dbCache) {
		this.dbCache = dbCache;
	}

	

	
	
	
	
}
