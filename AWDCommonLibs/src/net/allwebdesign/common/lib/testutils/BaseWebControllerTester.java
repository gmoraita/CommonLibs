package net.allwebdesign.common.lib.testutils;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import static org.junit.Assert.*;

public abstract class BaseWebControllerTester {

	@Autowired
    protected ApplicationContext context;
    protected MockHttpServletRequest request;
    protected MockHttpServletResponse response;
    protected HandlerAdapter handlerAdapter;


    
    
    protected static DataSource prepareJndi(String appServer, String port, String jndiName) throws IllegalStateException, NamingException{
		Properties props = new Properties();
		props.setProperty(Context.PROVIDER_URL, "http://"+appServer+":"+port);
		props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
		//props.setProperty("java.naming.factory.url.pkgs","org.apache.naming");  
		//props.setProperty("java.naming.factory.url.pkgs.prefixes","org.apache.naming");  

		InitialContext ctx = new InitialContext(props);
		//Hashtable<?, ?> n = ctx.getEnvironment();

    	return (DataSource) ctx.lookup(jndiName);
    	
    	/*
    	SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
		
		DataSource dsMSSQLDev = DataSourceFactory.createDBConnection(DBType.MSSQL, "dev", "WebAppDev", "1433", "devldap", "password");
		builder.bind("java:comp/env/jdbc/webDB", dsMSSQLDev);
		builder.activate();
		*/
		

    	
    }
    
    protected void before() {
        handlerAdapter = context.getBean(HandlerAdapter.class);
    }
   
    protected void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final HandlerMapping handlerMapping = context.getBean(HandlerMapping.class);
        final HandlerExecutionChain handler = handlerMapping.getHandler(request);
        assertNotNull("No handler found for request, check you request mapping", handler);

        final Object controller = handler.getHandler();

        final HandlerInterceptor[] interceptors = handlerMapping.getHandler(request).getInterceptors();
        for (HandlerInterceptor interceptor : interceptors) {
            final boolean carryOn = interceptor.preHandle(request, response, controller);
            if (!carryOn) {
                return;
            }
        }

        handlerAdapter.handle(request, response, controller);
    }


}
