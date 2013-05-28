package net.allwebdesign.common.lib.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

public class DataValidator extends HashMap<String,String>{

	private static final long serialVersionUID = 1L;
	private final static String VALIDATION_EMPTY = "Empty";
	private Properties properties = new Properties();
	
	public DataValidator(String fileName){
		this.readPropertiesFile(fileName);
		
	}
	
	
	public void readPropertiesFile(String fileName){
		try {
			
		    properties.load(new FileInputStream(fileName ));
		} catch (IOException e) {
		}

	}
	
	public void doValidation(Map<String,String> data){
		
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		
		while (it.hasNext()) {
	        Map.Entry<String,String> pairs = (Map.Entry<String,String>)it.next();
	        checkField(pairs.getValue()); 
	        
	        
		}
		
	}
	private void checkField(String field){
		Iterator<Entry<Object, Object>> it = this.properties.entrySet().iterator();
		while (it.hasNext()) {
	        Map.Entry<Object,Object> pairs = (Map.Entry<Object,Object>)it.next();
	        if (field.contains(pairs.getKey().toString())){
	        	if (pairs.getValue().toString().contains(VALIDATION_EMPTY)){
		        	if (field.length() == 0){
		        		this.put(pairs.getKey().toString(), VALIDATION_EMPTY);
		        	}
		        }
	        }
		}
		
	}	
}
