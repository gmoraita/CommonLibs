package net.allwebdesign.common.lib.authentication; 

import java.util.Collection;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Implementation of the LdapAuthoritiesPopulator that enables 
 * using for authentication the LDAP and authorisation the dadtabase
 * @author George Moraitakis
 *
 */
public class UserDetailsAuthoritiesPopulator implements LdapAuthoritiesPopulator  {
    
	private UserDetailsService userDetailsService;

	/**
	 * Constructor that populates the authorities object with user details
	 * @param userDetailsService the user details
	 */
    public UserDetailsAuthoritiesPopulator(UserDetailsService userDetailsService) {
            setUserDetailsService(userDetailsService);
    }
    
    /**
     * Gets the authorities for the specific user
     * @param userData holds the user data
     * @param username the user's username
     * @return a collection that holds the authorities of the user
     */
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData,	String username) {
		
        UserDetails ud = userDetailsService.loadUserByUsername(username);
        return ud.getAuthorities();
	}

	/**
	 * Set the user details 
	 * @param userDetailsService the object holding the user details
	 */
    public void setUserDetailsService(UserDetailsService userDetailsService) {
            this.userDetailsService = userDetailsService;
    }
}
