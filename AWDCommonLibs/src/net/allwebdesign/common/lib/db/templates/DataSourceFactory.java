package net.allwebdesign.common.lib.db.templates;

import org.springframework.jdbc.datasource.SingleConnectionDataSource;

/**
 * Makes a {@link SingleConnectionDataSource} to use ad hoc e.g. for testing purposes
 * @author George Moraitakis
 *
 */
public class DataSourceFactory {

	
	
	//public static final int DB_POSTGRES = 4;
	
	/**
	 * Creates a {@link SingleConnectionDataSource}
	 * @param dbType the DB type one of the database types in {@link DBType}
	 * @param server the DB server
	 * @param database the the DB name
	 * @param port the DB port
	 * @param username the DB username
	 * @param password the DB password
	 * @return A {@link SingleConnectionDataSource}
	 */
	public static SingleConnectionDataSource createDBConnection(DBType dbType, String server, String database, String port, String username, String password){
		SingleConnectionDataSource ds = new SingleConnectionDataSource();
	    ds.setDriverClassName(fetchDBDriver(dbType));
	    ds.setUrl(fetchDBUrl(dbType, server, port, database));
	    ds.setUsername(username);
	    ds.setPassword(password);
	    return ds;
		
		
	}
	
	private static String fetchDBDriver(DBType dbType){
		
		switch(dbType){
		case MSSQL:
			return "net.sourceforge.jtds.jdbc.Driver";

		case ORACLE:
			return "oracle.jdbc.OracleDriver";

		case MYSQL:
			return "com.mysql.jdbc.Driver";

		default:
		
		}
		
		return null;
	}
	
	private static String fetchDBUrl(DBType dbType, String server, String port, String database){
		
		switch(dbType){
		case MSSQL:
			return "jdbc:jtds:sqlserver://"+server+":"+port+"/"+database;

		case ORACLE:
			return "jdbc:oracle:thin:@"+server+":"+port+":"+database;

		case MYSQL:
			return "jdbc:mysql://"+server+":"+port+":"+database;

		default:
		
		}
		
		return null;
	}
	
	
	
}
