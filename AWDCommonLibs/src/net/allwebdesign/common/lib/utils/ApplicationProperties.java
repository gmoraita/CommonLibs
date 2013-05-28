package net.allwebdesign.common.lib.utils;


import java.io.Serializable;

import java.util.TreeMap;

import java.util.logging.Logger;

import javax.sql.DataSource;


import net.allwebdesign.common.lib.db.DataResults;
import net.allwebdesign.common.lib.db.GenericDataHandler;

/**
 * A class holding Application Properties. The properties are retrieved from the database
 * 
 * @author George Moraitakis
 *
 */
public class ApplicationProperties extends TreeMap<String,String> implements Serializable{


	private static final long serialVersionUID = 1L;
	private String applicationName;
	private String applicationPropertiesSql;
	private DataSource datasource;
	private String roles ;
	private String roleNarratives ;

	/**
	 * Initializes the application properties with data retrieved from the database
	 * @param modulationPropertiesSql the query to run for retrieving the data
	 * @param applicationName the application name to use for querying the data
	 * @param datasource the data source
	 */
	public ApplicationProperties(String applicationName, String applicationPropertiesSql,  DataSource datasource){
		
		this.applicationName = applicationName;
		this.applicationPropertiesSql = applicationPropertiesSql;
		this.datasource = datasource;
		
		this.loadProperties();

		
	}
	
		
	
	public boolean loadProperties(){
		GenericDataHandler dataHandler = new GenericDataHandler(datasource);
		try{
			DataResults params = dataHandler.getDataList(applicationPropertiesSql, applicationName);
			
			for (int i = 0 ; i < params.size();i++){
				this.put(params.get(i).getValueString("Property"), params.get(i).getValueString("Value"));
				
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			Logger.getLogger("Problem with setting the properties. I will not set any property.");
			return false;
			
		}
		
		return true;
	}
	
	/**
	 * Convinience method to get properties for web flows
	 * @return the properties
	 */
	public TreeMap<String,String> getAllProperties(){
		TreeMap<String,String> newMap = new TreeMap<String,String>();
		newMap.putAll(this);
		
		return newMap;
		
		
		
	}

	/**
	 * Get the roles 
	 * @return the roles
	 */
	public String getRoles() {
		return roles;
	}


	/**
	 * Set the roles
	 * @param roles the roles to set
	 */
	public void setRoles(String roles) {
		this.roles = roles;
	}

	/**
	 * Get the roles narratives
	 * @return the roles narratives
	 */
	public String getRoleNarratives() {
		return roleNarratives;
	}


	/**
	 * Set the roles narratives
	 * @param roleNarratives the roles narratives to set
	 */
	public void setRoleNarratives(String roleNarratives) {
		this.roleNarratives = roleNarratives;
	}

	
	
	
	
	
	
}
