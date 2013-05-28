package net.allwebdesign.common.lib.db;

import java.util.TreeMap;

/**
 * 
 * @author George Moraitakis
 *
 */
public class QueryQualifierMap extends TreeMap<String, Object> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Initializes an emtpy qualifier 
	 */
	public QueryQualifierMap(){
		super();
	}
	
	/**
	 * Creates an instance of this class
	 * @return
	 */
	public static QueryQualifierMap create(){
		return new QueryQualifierMap();
	}
	
	/**
	 * Adds a qualifier to this holder
	 * @param key the name of the qualifer (i.e. the table field name)
	 * @param value the value of the qualifier (the value to pass to the field)
	 * @return the updated instance
	 */
	public QueryQualifierMap add(String key, Object value){
		this.put(key, value);
		return this;
		
	}
	
	/**
	 * Gets the list of keys in the form of a String array
	 * @return the list of keys (fields)
	 */
	public String[] keyArray(){
		return this.keySet().toArray(new String[0]);
	}


	
}
