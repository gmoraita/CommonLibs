package net.allwebdesign.common.lib.authentication;

import java.io.Serializable;


import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import net.allwebdesign.common.lib.db.DataResults;
import net.allwebdesign.common.lib.db.GenericDataHandler;


/**
 * An implementation of UserDetailsService that gets authorisation data from 
 * the database
 * @author George Moraitakis
 *
 */
public class CustomUserDetailsService implements UserDetailsService, Serializable{
	
	private static final long serialVersionUID = 1L;	
	private DataSource datasource;
	private String applicationName; 
	private String userAuthorityQuery;
	private UserCache userCache;
	private boolean databaseAuth = false;
	

	public CustomUserDetailsService(){
		this.userCache = new UserCache();
		
	}
	


	/**
	 * Get details of a user. This methos checks the cache to see if the user <br/>
	 * is available. If the user is available it returns that otherwise
	 * it does a user search in the persistant layer and populates the cache if found.
	 * @param username the username for which to look detail
	 * @return the user details or null if the username passed is null
	 */
	public LoggedUser getCurrentUser(String username){
		if (username == null){return null;}
		LoggedUser user = this.userCache.getUserFromCache(username.toLowerCase());

		if ( user == null){
			user = this.loadUserByUsername(username.toLowerCase());
			if (!user.equals("not_found_user")){
				this.userCache.putUserInCache(user);
			}
		}
		return (LoggedUser)user; 
		
	}
	
	/**
	 * Wipes out the user cache
	 */
	public void clearCache(){
		this.userCache.resetCache();
	}
	
	
	/**
	 * Remove user from cache 
	 * @param username to remove from cache
	 */
	public void removeUserFromCache(String username){
		
		LoggedUser user = this.userCache.getUserFromCache(username.toLowerCase());

		if ( user != null){
			
			this.userCache.removeUserFromCache(username.toLowerCase());
			Logger.getAnonymousLogger().info("User "+username + " has been removen from cache.");
			
		}
		else{
			Logger.getAnonymousLogger().info("User "+username + " cannot be found in the cache.");
		}
		
	}

	/**
	 * Load a user by its username. It does a query in the database 
	 * @param username 
	 * @return the user details
	 */
	public LoggedUser loadUserByUsername(String Username)
			throws UsernameNotFoundException {
		
		LoggedUser user ;
		GenericDataHandler dataHandler = new GenericDataHandler(datasource);
		try{
			DataResults atomoInfo = dataHandler.getDataList(this.userAuthorityQuery,Username, this.applicationName);
			user = LoggedUser.getInstance(atomoInfo, databaseAuth);
			
			//this.user = (LoggedUser) dataHandler.getDao().execSQL(this.userAuthorityQuery, new UserRowHandler(), Username, this.applicationName);
		}
		catch(Exception e){
			e.printStackTrace();
			Logger.getLogger("Problem with searching authorisation");
			user = LoggedUser.getInstance(null, false); // returns a null user
		}
	
		return user;
	
 
	}
	
	/**
	 * Get details of a user. This methos checks the cache to see if the user <br/>
	 * is available. If the user is available it returns that otherwise
	 * it does a user search in the persistant layer and populates the cache if found.
	 * @param username the username for which to look detail
	 * @return the user details
	 */
	public LoggedUser addSimulatedUser(String username, String simulatedUsername){
		LoggedUser simulatedUser = this.loadUserByUsername(simulatedUsername);
		LoggedUser currentUser = this.userCache.getUserFromCache(username.toLowerCase());
		if (simulatedUser != null && !simulatedUser.getUsername().equals("not_found_user")){
			try{
				simulatedUser.setSimulatedBy(currentUser);
				this.userCache.putSimulatedUserInCache(username.toLowerCase(), simulatedUser);
			}catch (Exception e){
				e.printStackTrace();
				simulatedUser.setSimulatedBy(null);
			}
		}
		else{
			return currentUser;
		}
		
		return simulatedUser; 
		
	}
	
	/**
	 * Removes a user that has been simulated
	 * @param username the current user that simulates another one
	 */
	public void removeSimulatedUser(String username){
		
		
		UserDetails simulatedUser = userCache.getSimulatedUserFromCache(username.toLowerCase());
		if (simulatedUser != null && !simulatedUser.equals("not_found_user")){
			this.userCache.removeSimulatedUserFromCache(username.toLowerCase());
		}
		
	}
	
	/**
	 * Removes all users from cache
	 */
	public void removeAllUsers(){
		this.userCache.getCache().removeAll();
	}
	
	/**
	 * Set the user authority query
	 * @param userAuthorityQuery
	 */
	public void setUserAuthorityQuery(String userAuthorityQuery) {
		this.userAuthorityQuery = userAuthorityQuery;
	}	
	
	/**
	 * get the user authority query
	 * @return
	 */
	public String getUserAuthorityQuery() {
		return userAuthorityQuery;
	}

	/**
	 * Sets the datasource
	 * @param datasource 
	 */
	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}
	/**
	 * Set the application name that will be used to lookup authorities in db
	 * @param applicationName
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}


	/**
	 * Checks if we do database auth
	 * @return true if we do
	 */
	public boolean isDatabaseAuth() {
		return databaseAuth;
	}


	/**
	 * Set the database auth flag
	 * @param databaseAuth
	 */
	public void setDatabaseAuth(boolean databaseAuth) {
		this.databaseAuth = databaseAuth;
	}





	

}

