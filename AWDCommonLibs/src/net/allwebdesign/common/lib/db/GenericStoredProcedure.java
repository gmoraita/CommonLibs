package net.allwebdesign.common.lib.db;

import java.util.Map;


import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

/**
 * An extension of Spring's StoredProcedure
 * @author George Moraitakis
 * @version 1.0
 */
public class GenericStoredProcedure extends StoredProcedure {

	
	/**
	 * Default constructor that adds the datasource and the sproc name
	 * @param dataSource
	 * @param sprocName
	 */
	public GenericStoredProcedure(DataSource dataSource, String sprocName){
		super(dataSource, sprocName);
		
	}
	
	/**
	 * Convenience method to add a VARCHAR param
	 * @param name
	 * @param sqlType
	 */
	public void addInStringParam(String name){
		this.addInParam(name, java.sql.Types.VARCHAR);
	}
	
	/**
	 * Convenience method to add a DATE param
	 * @param name
	 * @param sqlType
	 */
	public void addInDateParam(String name){
		this.addInParam(name, java.sql.Types.DATE);
	}
	
	/**
	 * Convenience method to add a INTEGER param
	 * @param name
	 * @param sqlType
	 */
	public void addInIntParam(String name){
		this.addInParam(name, java.sql.Types.INTEGER);
	}
	
	/**
	 * Convenience method to add a NUMERIC param
	 * @param name
	 * @param sqlType
	 */
	public void addInNumericParam(String name){
		this.addInParam(name, java.sql.Types.NUMERIC);
	}
	
	
	/**
	 * Adds an input param
	 * @param name
	 * @param sqlType
	 */
	public void addInParam(String name, int sqlType){
		this.declareParameter(new SqlParameter(name, sqlType));
	}
	
	/**
	 * Adds an output param
	 * @param name
	 * @param sqlType
	 */
	public void addOutParam(String name, int sqlType){
		this.declareParameter(new SqlOutParameter(name, sqlType, new BDDataMapper()));
	}
		
	/**
	 * Adds new cursor output parameter (Oracle only). 
	 * @param name 
	 */
	public void addOutCursorParam(String name){
		  
		addOutParam(name, OracleTypes.CURSOR);
		
	}
	
	/**
	 * Adds new cursor output parameter (Oracle only). 
	 * @param name 
	 */
	public void addInOutCursorParam(String name){
	
		this.declareParameter(new SqlInOutParameter(name, OracleTypes.CURSOR, new BDDataMapper()));
		
		
	}
	
	/**
	 * executes the sproc
	 * @param params 
	 */
	public Map<String, Object> execute(Object... params){
		compile(); // will only be executed the 1st time. Ignored otherwise
		return super.execute(params);
	}

	
	
	
	
}
