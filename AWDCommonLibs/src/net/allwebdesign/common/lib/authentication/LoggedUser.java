package net.allwebdesign.common.lib.authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import java.util.logging.Logger;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import net.allwebdesign.common.lib.db.DataResults;

/**
 * Extended User class to include custom variables
 * @author George Moraitakis
 *
 */
public class LoggedUser extends User {

	private static final long serialVersionUID = 1L;
	private String fullname;
	private String employeeCode;
	private LoggedUser simulatedBy = null;
	private Collection<GrantedAuthority> authorities;
	private DataResults atomoInfo;
	
	
	/**
	 * Constructor that sets a logged user
	 * @param username
	 * @param password
	 * @param enabled
	 * @param accountNonExpired
	 * @param credentialsNonExpired
	 * @param accountNonLocked
	 * @param authorities
	 * @param fullname
	 * @param employeeCode
	 * @throws IllegalArgumentException
	 */
	public LoggedUser(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked, Collection<GrantedAuthority> authorities, 
			String fullname, String employeeCode)
			throws IllegalArgumentException {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		
		this.fullname = fullname;
		this.authorities = authorities;
		this.employeeCode = employeeCode;
	}
	



	/**
	 * Get the full name of the user
	 * @return the full name
	 */
	public String getFullname() {
		return fullname;
	}
	
	/**
	 * Set the full name of the user
	 * @param fullname 
	 */
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	
	/**
	 * Get all the fields coming from atomo
	 * @return the fields from atomo
	 */
	public DataResults getAtomoInfo() {
		return atomoInfo;
	}
	
	/**
	 * Get all the fields coming from atomo but only the first row
	 * @return the fields from atomo in the first row of the result set
	 */
	public Map<String,Object> getFirstAtomoInfo() {
		if (atomoInfo == null){return null;}
		return atomoInfo.getFirstRowFields();
	}



	/**
	 * Set the atomo fields
	 * @param atomoInfo the fields coming from atomo
	 */
	public void setAtomoInfo(DataResults atomoInfo) {
		this.atomoInfo = atomoInfo;
	}




	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}


	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	
	/**
	 * Returns an Object Array of the user roles
	 * @return the user roles
	 */
	public Object[] getAuthoritiesAsArray(){
		ArrayList<String> roles = new ArrayList<String>(); 
		for (Iterator<GrantedAuthority> it = authorities.iterator(); it.hasNext();){
		
			roles.add(it.next().getAuthority());
		
		}
		return roles.toArray();
	}
	/**
	 * Returns a comma delimited string with the user roles
	 * @return the user roles
	 */
	public String getAuthoritiesAsString(){
		return getAuthoritiesAsString(",");
	}
	
	/**
	 * Returns a custom delimited string with the user roles 
	 * @param delim the custom delimiter
	 * @return the user roles
	 */
	public String getAuthoritiesAsString(String delim){
		String roles = ""; 
		for (Iterator<GrantedAuthority> it = authorities.iterator(); it.hasNext();){
			roles += it.next().getAuthority() + delim;
			
		
		}
		return roles;
	}

	/**
	 * Convenience method, to retrieve a specific atomo field
	 * @param field the field to retrieve
	 * @return the atomo info to show
	 */
	public String showAtomoInfo(String field){
		return (String) this.atomoInfo.get(0).getValue(field); 
	}
	
	/**
	 * Generates a new Logged User based on the roles passed to it from the database
	 * <BR>If passed null or no user found then it creates a null user
	 * @param atomoInfo the result set from the database containing the extra info from atomo
	 * @param if we do authentication from database then this will be retrieved otherwise a default password is assigned
	 * @return a new LoggedUser object
	 */
	public static LoggedUser getInstance(DataResults atomoInfo, boolean databaseAuth) {

		if (atomoInfo == null || atomoInfo.size() == 0){
			Logger.getLogger("No authorisation found");
			return new LoggedUser("not_found_user","nopass",true, true, true, true, getAuthorities("ROLE_NOTFOUND"), "No User", "No User");
		}
		
		
		
		// Get the first row which contains the common fields plus its role
		String username = atomoInfo.getValueString("Username");
		String fullname = atomoInfo.getValueString("FullName");
		String userCode = atomoInfo.getValueString("EmployeeCode");
		String passwordEncoded = atomoInfo.getValueString("PasswordEncoded");
		String password = "password";
		if (databaseAuth){
			password = atomoInfo.getValueString("Password");
			if (passwordEncoded.equals("1")){
				try{
					password = PasswordService.decrypt(password);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
		Collection<GrantedAuthority> auth = LoggedUser.getAuthorities((String) atomoInfo.get(0).getValue("Role"));
		
		
		// start from the second row if it exists
		for(int i = 1; i < atomoInfo.size(); i++){
			auth.addAll(LoggedUser.getAuthorities((String) atomoInfo.get(i).getValue("Role")));
		}
		LoggedUser user = new LoggedUser(username, password, true, true, true, true, auth, fullname, userCode);
		user.setAtomoInfo(atomoInfo);
		
		return user;
	}
	
	/**
	 * Get the authorities
	 * @param authority the authority string e.g. ROLE_USER
	 * @return a collection of the granted authorities
	 */
	public static Collection<GrantedAuthority> getAuthorities(String authority) {
		ArrayList<GrantedAuthority> g = new ArrayList<GrantedAuthority>();
		g.add(new SimpleGrantedAuthority(authority));
		return g;
	}

	/**
	 * Checks if this user is simulated
	 * @return whether is simulated or not
	 */
	public boolean isSimulated(){
		if (simulatedBy != null){
			return true;
		}
		return false;
	}

	/**
	 * Get the user that simulates this user
	 * @return the user of the user that simulates this one
	 */
	public LoggedUser getSimulatedBy() {
		return simulatedBy;
	}
	
	/**
	 * Convenience method to get the full name of the user that simulates this user
	 * @return the full name of the user that simulates this one
	 */
	public String getSimulatedByFullName() {
		if (simulatedBy== null){ return null;}
				
		return simulatedBy.getFullname();
	}
	
	/**
	 * Convenience method to get the username of the user that simulates this user
	 * @return the username of the user that simulates this one
	 */
	public String getSimulatedByUsername() {
		if (simulatedBy== null){ return null;}
		
		return simulatedBy.getUsername();
	}
	

	/**
	 * Set the user that simulates this user
	 * @param simulatedBy the username of the user that simulates this one
	 */
	public void setSimulatedBy(LoggedUser simulatedBy) {
		this.simulatedBy = simulatedBy;
	}



	/**
	 * Get the Employee Code
	 * @return the Employee Code
	 */
	public String getEmployeeCode() {
		return employeeCode;
	}



	/**
	 * Set the Employee Code
	 * @param employeeCode the Employee Code
	 */
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}	
	
	
	


}
