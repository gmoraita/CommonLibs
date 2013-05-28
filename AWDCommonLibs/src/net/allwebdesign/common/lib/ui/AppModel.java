package net.allwebdesign.common.lib.ui;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.PropertyResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import net.allwebdesign.common.lib.authentication.CustomUserDetailsService;
import net.allwebdesign.common.lib.authentication.LoggedUser;
import net.allwebdesign.common.lib.authentication.SecurityUtils;
import net.allwebdesign.common.lib.utils.ApplicationProperties;
import net.allwebdesign.common.lib.utils.Utils;

/**
 * This class enriches Models with additional info used later on to e.g. in views
 * The info includes:<BR/>
 * a) User information retrieved from the database and carried on a {@link CustomUserDetailsService} object<BR/>
 * b) Application properties held in the database and carried on a {@link ApplicationProperties} object
 * @author George Moraitakis
 *
 */
public class AppModel extends SpringBeanAutowiringSupport{

	@Autowired
	private CustomUserDetailsService jdbcUserService;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	

	/**
	 * Loads into a model the following:<BR/>
	 * - <B>user</B>: a {@link CustomUserDetailsService} holding user info <BR/>
	 * - <B>appProps</B>: a {@link ApplicationProperties} holding application properties.<BR/> 
	 * - <B>atomo</B>: holds all employee info. Use to retrieve any attribute from the authorization <BR/>
	 * - <B>username</B>: the username that is logged in (if simulated it is the simulated)<BR/>
	 * - <B>userFN</B>: the full name of the logged in or simulated user <BR/> 
	 * - <B>userRoles</B>: Array holding the roles of the logged in or simulated  user <BR/>
	 * - <B>userRolesStr</B>: The roles of the logged in or simulated user in comma separated format <BR/>
	 * - <B>employeeCode</B>: The employeeCode of the logged in or simulated user (also contained in employee)</BR>
	 * - <B>employee</B>: The employee details of the logged in or simulated user (use directly its fields e.g. employee.SubDepartmentNumber)</BR>
	 * - <B>simulatedBy</B>: The full name of the user simulating this user</BR>
	 * - <B>employeeCode</B>: The username of the user simulating this user</BR>
	 * - <B>masterProps</B>: The master.properties properties (access by doing e.g. masterProps['application.title'])</BR>
	 * - <B><I>menu details</I></B>: Loads the menu. You will not need this usually (attributes: <b>titleList</B>, <B>urlList</B>, <B>rolesList</B>)</BR>
	 * @param model the model to enrich
	 */
	public void initModel(ModelMap model){
		initModel(model,null);
	}
	
	/**
	 * Loads into a model the following:<BR/>
	 * - <B>user</B>: a {@link CustomUserDetailsService} holding user info <BR/>
	 * - <B>atomo</B>: holds all employee info. Use to retrieve any attribute from the authorization <BR/> 
	 * - <B>appProps</B>: a {@link ApplicationProperties} holding application properties.<BR/> 
	 * - <B>username</B>: the username that is logged in (if simulated it is the simulated)<BR/>
	 * - <B>userFN</B>: the full name of the logged in or simulated user <BR/> 
	 * - <B>userRoles</B>: Array holding the roles of the logged in or simulated  user <BR/>
	 * - <B>userRolesStr</B>: The roles of the logged in or simulated user in comma separated format <BR/>
	 * - <B>employeeCode</B>: The employeeCode of the logged in or simulated user (also contained in employee)</BR>
	 * - <B>employee</B>: The employee details of the logged in or simulated user (use directly its fields e.g. employee.SubDepartmentNumber)</BR>
	 * - <B>simulatedBy</B>: The full name of the user simulating this user</BR>
	 * - <B>employeeCode</B>: The username of the user simulating this user</BR>
	 * - <B>masterProps</B>: The master.properties properties (access by doing e.g. masterProps['application.title'])</BR>
	 * - <B><I>menu details</I></B>: Loads the menu. You will not need this usually (attributes: <b>titleList</B>, <B>urlList</B>, <B>rolesList</B>)</BR>
	 * @param model the model to enrich
	 * @param request the request params to add in by default
	 */
	public void initModel(ModelMap model, HttpServletRequest request){
		LoggedUser user = SecurityUtils.getLoggedUser(jdbcUserService);
		if (user != null){
			model.addAttribute("user", user);
			model.addAttribute("username", user.getUsername());
			model.addAttribute("userFN", user.getFullname());
			model.addAttribute("userRoles", user.getAuthoritiesAsArray());
			model.addAttribute("userRolesStr", user.getAuthoritiesAsString());
			model.addAttribute("userCode", user.getEmployeeCode()); // kept for compatibility reasons
			model.addAttribute("employeeCode", user.getEmployeeCode());
			model.addAttribute("atomo", user.getFirstAtomoInfo());
			model.addAttribute("employee", user.getFirstAtomoInfo());
			model.addAttribute("simulatedBy", user.getSimulatedByFullName());
			model.addAttribute("simulatedByUsername", user.getSimulatedByUsername());
		}
		
		if (request != null){
			model.addAllAttributes(Utils.extractFormValues(request));
		}
		
		model.addAttribute("appProps", applicationProperties);
		
		loadMenu(model);
		loadMasterProps(model);
		
		
	}
	
	/**
	 * Loads into a model the following:<BR/>
	 * - <B>user</B>: a {@link CustomUserDetailsService} holding user info <BR/>
	 * - <B>appProps</B>: a {@link ApplicationProperties} holding application properties.<BR/> 
	 * - <B>atomo</B>: holds all employee info. Use to retrieve any attribute from the authorization <BR/>
	 * - <B>username</B>: the username that is logged in (if simulated it is the simulated)<BR/>
	 * - <B>userFN</B>: the full name of the logged in or simulated user <BR/> 
	 * - <B>userRoles</B>: Array holding the roles of the logged in or simulated  user <BR/>
	 * - <B>userRolesStr</B>: The roles of the logged in or simulated user in comma separated format <BR/>
	 * - <B>employeeCode</B>: The employeeCode of the logged in or simulated user (also contained in employee)</BR>
	 * - <B>employee</B>: The employee details of the logged in or simulated user (use directly its fields e.g. employee.SubDepartmentNumber)</BR>
	 * - <B>simulatedBy</B>: The full name of the user simulating this user</BR>
	 * - <B>employeeCode</B>: The username of the user simulating this user</BR>
	 * - <B>masterProps</B>: The master.properties properties (access by doing e.g. masterProps['application.title'])</BR>
	 * - <B><I>menu details</I></B>: Loads the menu. You will not need this usually (attributes: <b>titleList</B>, <B>urlList</B>, <B>rolesList</B>)</BR>
	 */
	public ModelMap create(){
		return create(null);
	}
	
	/**
	 * Loads into a model the following:<BR/>
	 * - <B>user</B>: a {@link CustomUserDetailsService} holding user info <BR/>
	 * - <B>appProps</B>: a {@link ApplicationProperties} holding application properties.<BR/> 
	 * - <B>atomo</B>: holds all employee info. Use to retrieve any attribute from the authorization <BR/>
	 * - <B>username</B>: the username that is logged in (if simulated it is the simulated)<BR/>
	 * - <B>userFN</B>: the full name of the logged in or simulated user <BR/> 
	 * - <B>userRoles</B>: Array holding the roles of the logged in or simulated  user <BR/>
	 * - <B>userRolesStr</B>: The roles of the logged in or simulated user in comma separated format <BR/>
	 * - <B>employeeCode</B>: The employeeCode of the logged in or simulated user (also contained in employee)</BR>
	 * - <B>employee</B>: The employee details of the logged in or simulated user (use directly its fields e.g. employee.SubDepartmentNumber)</BR>
	 * - <B>simulatedBy</B>: The full name of the user simulating this user</BR>
	 * - <B>employeeCode</B>: The username of the user simulating this user</BR>
	 * - <B>masterProps</B>: The master.properties properties (access by doing e.g. masterProps['application.title'])</BR>
	 * - <B><I>menu details</I></B>: Loads the menu. You will not need this usually (attributes: <b>titleList</B>, <B>urlList</B>, <B>rolesList</B>)</BR>
	 * @param request the request params to add in by default
	 */
	public ModelMap create(HttpServletRequest request){
		ModelMap model = new ModelMap();
		initModel(model,request);
		return model;
	}
	
	
	
	private void loadMasterProps(ModelMap model){
		InputStream in = getClass().getResourceAsStream("/master.properties");
		Reader r;
		PropertyResourceBundle bundle = null;
		
		try {
			r = new InputStreamReader(in, "UTF-8");
			bundle = new PropertyResourceBundle(r);

		} catch (Exception e1) {
			e1.printStackTrace();
		}    
		model.addAttribute("masterProps", bundle);
		
	}
	    
	private void loadMenu(ModelMap model){
		InputStream in = getClass().getResourceAsStream("/menu.properties");
		Reader r;
		PropertyResourceBundle bundle = null;
		
		try {
			r = new InputStreamReader(in, "UTF-8");
			bundle = new PropertyResourceBundle(r);


		} catch (Exception e1) {
			e1.printStackTrace();
		}    
		
		
		
		if (bundle == null){return;}

		SortedMap<String, String> mapTitle = new TreeMap<String,String>(); 
		SortedMap<String, String> mapUrl = new TreeMap<String,String>(); 
		SortedMap<String, String> mapRoles = new TreeMap<String,String>(); 
		Enumeration<String> e = bundle.getKeys();
	    while (e.hasMoreElements()) {
	    	String key = e.nextElement();
	    	String value = bundle.getString(key);
	    	String[] elements = value.split(",");
	    	mapTitle.put(key,elements[0]);
	    	
	    	if (elements.length ==2 && key.contains(".h") ){
	    		mapRoles.put(key,elements[1]);
	    	}
	    	if (elements.length >=2 && key.contains(".s.") ){
	    		mapUrl.put(key,elements[1]);
	    	}	
	    	if (elements.length ==2 && key.contains(".2h") ){
	    		mapRoles.put(key,elements[1]);
	    	}
	    	if (elements.length >=2 && key.contains(".2s.") ){
	    		mapUrl.put(key,elements[1]);
	    	}	
	    	if (elements.length ==3  ){
	    		mapRoles.put(key,elements[2]);
	    	}	    	
	    }
		
	
		
	   	model.addAttribute("titleList", mapTitle);
	   	model.addAttribute("urlList", mapUrl);
	   	model.addAttribute("rolesList", mapRoles);
		
	}

	
	
	
	
	
	
	
}
