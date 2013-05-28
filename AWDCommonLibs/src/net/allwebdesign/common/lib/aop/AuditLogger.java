package net.allwebdesign.common.lib.aop;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import net.allwebdesign.common.lib.db.GenericDataHandler;


/**
 * Class AuditLogger
 * Uses AOP to log actions from the user.
 * @author George Moraitakis
 * @since v1.0
 */
@Aspect
public class AuditLogger {
	
	
	private String application ;
	private DataSource datasource;
	private Boolean isEnabled = false;
	private String insertAuditTrailSql;
	
	/**
	 * It intercepts calls to the jstlview class which is the standard 
	 * resolver of the spring pages. 
	 * @since v1.0
	 */
	@Pointcut("target(org.springframework.web.servlet.view.JstlView)")
	public void jstlPoint() {	}
	
	
	/**
	 * Advice to the point cut that does the logging. It requires that the
	 * logging is enabled and there is a datasource + sql supplied for writing to 
	 * the database 
	 * It logs the username, page and request objects used. 
	 * @param point the joint point representing the class containing the data
	 * to log 
	 */
	@SuppressWarnings("unchecked")
	@After("net.allwebdesign.common.lib.aop.AuditLogger.jstlPoint()")
	public void doJstlLog(JoinPoint point){
		org.springframework.web.servlet.view.JstlView jstl  = (org.springframework.web.servlet.view.JstlView)point.getTarget();
		String user = "";
		String url = jstl.getUrl();
		ArrayList<String> params = null;		
		//System.out.println(">>>>>>>>>>>> DO hello "+ point.getTarget());
		
			
		

		for (int i = 0 ; i < point.getArgs().length ; i++){
			Object o = point.getArgs()[i];
			if (o.getClass().getCanonicalName().contains("Map")){
				params = this.processDataMap((Map<String, Object>)o);
			}
			else if (o.getClass().getCanonicalName().contains("SecurityContextHolderAwareRequestWrapper")){
				org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper security =
					(org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper)o;
				
				user = security.getRemoteUser();
				
				//System.out.println("User: "+security.getRemoteUser());
			
			}
			else {
				//System.out.println("Other class: "+o);
			}
			
		}
		this.doAuditLog(application, user, url, params);
	}

	
	private ArrayList<String> processDataMap(Map<String, Object> map) {
	    TreeMap<String, Object> sortedMap = new TreeMap<String, Object>(map);
	    ArrayList<String> params = new ArrayList<String>();
	    Iterator<Entry<String, Object>> it = sortedMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Object> pairs = (Map.Entry<String,Object>)it.next();
	       	//System.out.println((pairs.getKey() + " = " + pairs.getValue()));
	       	params.add(pairs.getKey() + " = " + pairs.getValue());
	        

	    }
		return params;
	}
	
	private void doAuditLog(String application, String user, String url, ArrayList<String> params){
		String paramsStr = "";
		
		if (params != null){
			paramsStr = params.toString();
		}
		
		
		
		if (this.isEnabled){
			System.out.println("Application: "+application+"User:"+user+" page: "+url+ " data: "+paramsStr);
			GenericDataHandler dataHandler = new GenericDataHandler(datasource);
			try{
				dataHandler.doInsertSql(this.insertAuditTrailSql,application,user, url, paramsStr);
			}catch (Exception e){
				
				System.err.println("ERROR: Could not write to the DB the audit trail" );
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * See the datasource for logging
	 * @param datasource the javax.sql.DataSource type datasource
	 */
	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	/**
	 * Whether the logging is enabled or not
	 * @param isEnabled set the enabled flag
	 */
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * Sets the query to use for the audit trail table 
	 * @param insertAuditTrailSql The insert sql statement
	 */
	public void setInsertAuditTrailSql(String insertAuditTrailSql) {
		this.insertAuditTrailSql = insertAuditTrailSql;
	}

	/**
	 * Sets the application name which is added to the audit trail table
	 * @param application the name of the application 
	 */
	public void setApplication(String application) {
		this.application = application;
	}



	
	
}
