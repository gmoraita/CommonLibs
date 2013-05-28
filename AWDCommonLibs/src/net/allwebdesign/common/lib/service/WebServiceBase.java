package net.allwebdesign.common.lib.service;

import javax.annotation.PostConstruct;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * This class forms the base class for any web service built <BR/>
 * It provides the init method declared as @PostConstruct to enable deployment of Spring aware web services in Tomcat 7, Glassfish 3 and JBoss 
 * 
 * @author g_n_morait
 *
 */
public abstract class WebServiceBase{
	/**
	 * Enables autowiring in Tomcat 7, Glassfish 3 and JBoss 
	 */
	@PostConstruct
	public void init() {
	    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
}
