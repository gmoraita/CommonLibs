package net.allwebdesign.common.lib.authentication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Used to get the currently logged user.<br/> 
 * Needs to be serializable so it can be used in the webflows 
 * 
 * @author George Moraitakis
 *
 */
public class SecurityUtils implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * Returns the current authenticated user
	 * @return the username of the authenticated user or null if no authentication
	 * info is available
	 */
	
	public static String getCurrentUser(){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null){
			return null;
		}
		return auth.getName();
	}
	
	
	/**
	 * Gets the user's full name
	 * @param userDetailsService the object holding the user details
	 * @return the fullname of the user
	 */
	public static LoggedUser getLoggedUser(CustomUserDetailsService userDetailsService){
		return userDetailsService.getCurrentUser(getCurrentUser() );
	}
	
	/**
	 * Gets the user's full name
	 * @param userDetailsService the object holding the user details
	 * @return the fullname of the user
	 */
	public static String getUserFullName(CustomUserDetailsService userDetailsService){
		return userDetailsService.getCurrentUser(getCurrentUser() ).getFullname();
	}
	
	/**
	 * Returns the roles of the authenticated user
	 * @return the list of roles
	 */
	
	public static Object[] getUserRoles(CustomUserDetailsService userDetailsService){
		ArrayList<String> roles = new ArrayList<String>(); 
		Collection<GrantedAuthority> authorities =  userDetailsService.getCurrentUser(getCurrentUser() ).getAuthorities();
		
		for (Iterator<GrantedAuthority> it = authorities.iterator(); it.hasNext();){
		
			roles.add(it.next().getAuthority());
		
		}
		return roles.toArray();
	}
	
}
