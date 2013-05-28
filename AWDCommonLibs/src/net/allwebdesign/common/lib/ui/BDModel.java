package net.allwebdesign.common.lib.ui;


import org.springframework.ui.ModelMap;


import net.allwebdesign.common.lib.authentication.CustomUserDetailsService;
import net.allwebdesign.common.lib.authentication.LoggedUser;
import net.allwebdesign.common.lib.authentication.SecurityUtils;
import net.allwebdesign.common.lib.utils.ApplicationProperties;
/**
 * Extends the Model Map passed to view, to add some custom fields
 * @author George Moraitakis
 * @deprecated use instead the AppModel
 * 
 */
public class BDModel extends ModelMap  {


	private static final long serialVersionUID = 1L;
	
	/**
	 * Initialises the Model by adding two specific fields 
	 * - the Full Name of the user
	 * - the Roles of the user
	 * @param jdbcUserService The security user service
	 */
	public BDModel(CustomUserDetailsService jdbcUserService){
		initModel(this,jdbcUserService);
	}
	
	
	/**
	 * Initialises the Model by adding two specific fields 
	 * - the Full Name of the user
	 * - the Roles of the user
	 * - the application params
	 * @param jdbcUserService The security user service
	 */
	public BDModel(CustomUserDetailsService jdbcUserService, ApplicationProperties appProps){
		initModel(this,jdbcUserService, appProps);
	}
	
	/**
	 * Initialises an existing ModelMap by adding the following implicit variables 
	 * - the Full Name of the user => userFN
	 * - the Roles of the user as array => userRoles
	 * - the Roles of the user as comma delimited string => userRolesStr
	 * - the username of the user => username
	 * - the simulated by user => simulatedByUsername
	 * - the simulated by full name = simulatedBy
	 * @param model the ModelMap to init
	 * @param jdbcUserService The security user service
	 */
	public static void initModel(ModelMap model, CustomUserDetailsService jdbcUserService){
		initModel(model, jdbcUserService, null);
	}
	
	
	/**
	 * Initialises an existing ModelMap by adding the following implicit variables 
	 * - the Full Name of the user => userFN
	 * - the Roles of the user as array => userRoles
	 * - the Roles of the user as comma delimited string => userRolesStr
	 * - the username of the user => username
	 * - the simulated by user => simulatedByUsername
	 * - the simulated by full name => simulatedBy
	 * - the application params => appProps
	 * @param model the ModelMap to init
	 * @param appProps the Application Parameters object
	 * @param jdbcUserService The security user service
	 */
	public static void initModel(ModelMap model, CustomUserDetailsService jdbcUserService, ApplicationProperties appProps){
		LoggedUser user = SecurityUtils.getLoggedUser(jdbcUserService);
		model.addAttribute("username", user.getUsername());
		model.addAttribute("userFN", user.getFullname());
		model.addAttribute("userRoles", user.getAuthoritiesAsArray());
		model.addAttribute("userRolesStr", user.getAuthoritiesAsString());
		
		if (user.isSimulated()){
			model.addAttribute("simulatedBy", user.getSimulatedBy().getFullname());
			model.addAttribute("simulatedByUsername", user.getSimulatedBy().getUsername());
		}
		
		if (appProps != null){

			model.addAttribute("appProps", appProps);
			
		}
		
	}



	
	

}
