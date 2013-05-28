package net.allwebdesign.common.lib.db;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A class representing a row
 * 
 * @author George Moraitakis
 * @version 1.0.1
 */
public class DataRow implements Serializable{
	

	private static final long serialVersionUID = 1L;
	private LinkedHashMap<String,Object> fields = new LinkedHashMap<String,Object>();
	
	/**
	 * Adds a field typically coming from reflection
	 * @param declaredField the field to be used as key
	 * @param obj the obj from which to extract the value from that field
	 * @throws Exception if there has been a problem
	 */
	public <T> void addReflectedField(Field declaredField, T obj) throws Exception {
		this.setValue(declaredField.getName(), declaredField.get(obj) ==null? "":declaredField.get(obj).toString());
	}

	/**
	 * Sets a value to a specific field.
	 * @param fieldName the name of the field corresponding to the table column
	 * @param value the value to be assigned
	 */
	public void setValue(String fieldName, Object value){
		this.fields.put(fieldName, value);
	}	
	
	/**
	 * Gets the value of a field
	 * @param fieldName the field name
	 * @return the value of the field
	 */
	public Object getValue(String fieldName){
		return this.fields.get(fieldName);
	}
	
	/**
	 * Gets the value of the field appearing in the specified position
	 * @param fieldNumber the field of the specified position (0 based)
	 * @return the value of the field
	 */
	public Object getValue(int fieldNumber){
		return this.fields.values().toArray()[fieldNumber];
	}
	
	/**
	 * Gets the value of a field as a string
	 * @param fieldName the field name
	 * @return the value as string
	 */
	public String getValueString(String fieldName){
		return (String)this.fields.get(fieldName);
	}
	
	/**
	 * Gets the value of the field appearing in the specified position as a String
	 * @param fieldNumber the field of the specified position (0 based)
	 * @return the value of the field as a sString
	 */
	public String getValueString(int fieldNumber){
		return (String)this.fields.values().toArray()[fieldNumber];
	}
	
	/**
	 * Gets the key of the field appearing in the specified position
	 * @param fieldNumber the field of the specified position (0 based)
	 * @return the key of the field
	 */
	public String getKey(int fieldNumber){
		return (String) this.fields.keySet().toArray()[fieldNumber];
	}
	
	/**
	 * Gets a delimited list of values of fields
	 * @param delimiter the delimiter to use
	 * @param fieldNames the fields to get values of
	 * @return the delimited list of values
	 */
	public String getValueDelimitedList(String delimiter, String... fieldNames){
		
		StringBuffer b = new StringBuffer();
		for(int i=0; i< fieldNames.length;i++){
			b.append(getValueString(fieldNames[i]));
			if (i<fieldNames.length-1){b.append(delimiter);}
			
		}
		
		return b.toString();
	}
	
	
	/**
	 * Returns all fields of a row in the forma of a Linked HashMap
	 * @return the row
	 */
	public LinkedHashMap<String,Object> getFields(){
		return this.fields;
	}
	
	/**
	 * Set the fields of a row.
	 * @param fields the row fields in the form of a linked hash map
	 */
	public void setFields(LinkedHashMap<String,Object> fields){
		this.fields = fields;
	}
	
	/**
	 * Prepends the name of each field with a string. This is useful when returning to 
	 * a viewer that required the id of each row e.g. row0_, row1_ etc.
	 * @param prepender the prepending string
	 */
	public void prependFieldNames(String prepender){
		prependFieldNames(prepender, "");
	}

	/**
	 * Prepends the name of each field with a string and also appends it with another string<BR/>.
	 * This is useful when returning to a viewer that requires the id of each row e.g. row0_, row1_ etc.
	 * @param prepender the prepending string
	 * @param appender the appending string
	 */
	public void prependFieldNames(String prepender, String appender){
		LinkedHashMap<String,Object> localMap = new LinkedHashMap<String,Object>();
		Iterator<Entry<String, Object>> it = this.fields.entrySet().iterator();
		while (it.hasNext()) {
	        Map.Entry<String,Object> pairs = (Map.Entry<String,Object>)it.next();
	        String key = (String)pairs.getKey();
	        key = prepender + key + appender;
	        localMap.put(key, pairs.getValue());
		}
		
		this.setFields(localMap);
		
	}	
	
}
