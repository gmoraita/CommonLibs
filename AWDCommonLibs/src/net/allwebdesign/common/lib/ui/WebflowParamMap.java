package net.allwebdesign.common.lib.ui;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A special HashMap for webflows 
 * @author George Moraitakis
 *
 */
public class WebflowParamMap extends HashMap<String,String> implements
		Serializable {


	private static final long serialVersionUID = 1L;

	/**
	 * Init the hashmap with a map of params
	 * @param params the params to init with
	 */
	public WebflowParamMap(Map<String,String> params){
		super (params);
	}
	
	/**
	 * Default instantiate the class
	 * @param params the map to instatiate with
	 * @return a new WebflowParamMap
	 */
	public static WebflowParamMap getInstance(Map<String, String> params){
		return new WebflowParamMap(params);
		
	}
	
}
