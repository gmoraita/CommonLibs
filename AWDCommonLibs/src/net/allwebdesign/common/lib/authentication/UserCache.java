package net.allwebdesign.common.lib.authentication;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.security.core.userdetails.cache.EhCacheBasedUserCache;

/**
 * Inheriting from EhCacheBasedUserCache, it enables the construction of a default cache 
 * of users with non-expiring elements. 
 *   
 * @author George Moraitakis
 *
 */
public class UserCache extends EhCacheBasedUserCache {
	
	private static final Log logger = LogFactory.getLog(EhCacheBasedUserCache.class);
	public static final String SIMULATED ="simulated";
	/**
	 * Default constructor that sets up the cache.
	 */
	public UserCache(){
		
		EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
		EhCacheFactoryBean b = new EhCacheFactoryBean();
		
		b.setCacheManager(cmfb.getObject());
		b.setCacheName("AppCache");
		b.setEternal(true);
		try {
			b.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		Ehcache cache = b.getObject();
		this.setCache(cache );
	}
	
	/**
	 * Puts a user in Cache
	 * @param user the user to add
	 */
	public void putUserInCache(LoggedUser user){
		Element element = new Element(user.getUsername().toLowerCase(), user);

		
        if (logger.isDebugEnabled()) {
            logger.debug("Cache put: " + element.getObjectKey());
        }

        this.getCache().put(element);
	}
	
	/**
	 * This overridden version additionally checks if simulated user
	 */
	public LoggedUser getUserFromCache(String username){
		// first check if user is in simulation mode
		LoggedUser user = (LoggedUser)super.getUserFromCache(SIMULATED+username.toLowerCase());
		// if null then try to retrive the normal.
		if (user ==null){
			user = (LoggedUser)super.getUserFromCache(username.toLowerCase());
		}
		return user;
		
	}

	/**
	 * Adds a simulated user in cache
	 * @param username the current user
	 * @param simulatedUser the user t be simulated
	 */
	public void putSimulatedUserInCache(String username, LoggedUser simulatedUser){
		Element element = new Element(SIMULATED+username.toLowerCase(), simulatedUser);

		
        if (logger.isDebugEnabled()) {
            logger.debug("Cache put: " + element.getObjectKey());
        }

        this.getCache().put(element);
	}
	
	/**
	 * Gets a simulated user
	 * @param username
	 * @return the user simulated
	 */
	public LoggedUser getSimulatedUserFromCache(String username){
		
		return (LoggedUser)super.getUserFromCache(SIMULATED+username.toLowerCase());
		
		
	}
	
	/**
	 * Removes a simulated user
	 * @param username the current user that does a simulation
	 */
	public void removeSimulatedUserFromCache(String username){
		this.removeUserFromCache(SIMULATED+username);
	}
	
	public void resetCache(){
		try{
			this.getCache().removeAll();
		} catch (Exception e){
			UserCache.logger.error("CACHE ERROR: There was a problem removing all from the cache");
		}
		
	}
	
}
