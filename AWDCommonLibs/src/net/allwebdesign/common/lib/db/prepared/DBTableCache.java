package net.allwebdesign.common.lib.db.prepared;

import net.allwebdesign.common.lib.cache.Cache;


public class DBTableCache extends Cache {
	

	/**
	 * Default constructor that sets up the cache.
	 */
	public DBTableCache(){
		super("TableDefCache");

	}
	
	
	
	/**
	 * This overridden version returns table definition
	 */
	@Override
	public DBTableDefinition getFromCache(String tableName){
		DBTableDefinition tableDef = (DBTableDefinition)super.getFromCache(tableName);
		
		return tableDef;
		
	}


}
