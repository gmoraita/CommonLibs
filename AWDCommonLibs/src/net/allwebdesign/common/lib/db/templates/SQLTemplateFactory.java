package net.allwebdesign.common.lib.db.templates;

/**
 * A factory to generate the appropriate SQL template based on the {@link net.allwebdesign.common.lib.db.templates.DBType}
 * @author George Moraitakis
 *
 */
public class SQLTemplateFactory {

	/**
	 * Generates the appropriate SQL template based on the {@link net.allwebdesign.common.lib.db.templates.DBType}
	 * @param dbType the {@link net.allwebdesign.common.lib.db.templates.DBType} to use
	 * @return the approproate Template or null id not found
	 */
	public static SQLTemplate getSQLTemplate(DBType dbType){
		switch(dbType){
		case MSSQL:
			return new MSSQLServerSQLTemplate();

		case ORACLE:
			return new ORACLESQLTemplate();

		case MYSQL:
			return new MySQLDBSQLTemplate();

		default:
		
		}
		
		return null;
		
		
	}
	
}
