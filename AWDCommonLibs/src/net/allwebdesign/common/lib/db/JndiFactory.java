package net.allwebdesign.common.lib.db;

import javax.naming.NamingException;


/**
 * Factory that creates JNDI connections
 * @author George Moraitakis
 *
 */
public class JndiFactory extends org.springframework.jndi.JndiObjectFactoryBean{

	/**
	 * Default constructor initialising the JndiObjectFactoryBean
	 */
	public JndiFactory(){
		super();
	}
	
	
	/**
	 * Checks the jndiName passed and tries with and without java:comp/env/
	 */
	@Override
	public void setJndiName(String jndiName){
		try {
			super.setJndiName(jndiName);
			lookupWithFallback();
			
			return;
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			logger.info("Trying with prefix java:comp/env/");
		}
		try {
			super.setJndiName("java:comp/env/"+jndiName);
			lookupWithFallback();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			logger.info("Could not find even with java:comp/env/");
			e.printStackTrace();
			
		}
		super.setJndiName("java:comp/env/"+jndiName);
	}

}
