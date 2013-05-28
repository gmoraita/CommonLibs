package net.allwebdesign.common.lib.db.templates;

/**
 * Holds the various DB types <BR>
 * NODB - The default<BR>
 * MSSQL <BR>
 * ORACLE <BR>
 * MYSQL 
 * @author George Moraitakis 
 *
 */


public enum DBType {
	/** No DB - a default value */
	NODB,
	
	/** MS SQL Server Database*/
	MSSQL,
	
	/** ORACLE Database */
	ORACLE,
	
	/** MySQL Database */
	MYSQL
}
