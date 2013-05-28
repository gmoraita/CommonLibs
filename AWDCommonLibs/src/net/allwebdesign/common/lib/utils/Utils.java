package net.allwebdesign.common.lib.utils;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;


import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.springframework.core.io.Resource;
import org.springframework.ui.ModelMap;
import org.springframework.util.DefaultPropertiesPersister;

import org.springframework.util.PropertiesPersister;

import net.allwebdesign.common.lib.db.DataResults;
import net.allwebdesign.common.lib.db.DataRow;



/**
 * A class which serves as a toolkit of useful methods <BR/>
 * It includes the following: <BR/>
 * - normalize: sorts and converts a Map<String,String[]> to Map<String,String>
 * @author George Moraitakis
 *
 */
public class Utils {
	
	public static final String XML_FILE_EXTENSION = ".xml";

	/**
	 * Sorts alphabetically the form values of a request  based on the keys and 
	 * at the same time it converts it to a Map<String,String>. 
	 * It will then concatenate the array of values to a single comma separated value.  
	 * @param request the HttpServletRequest
	 * @return a comma separated value in a Map<String,String>
	 */
	public static Map<String, Object> extractFormValues(HttpServletRequest request){
		
		@SuppressWarnings("unchecked")
		Map<String,Object[]> map = request.getParameterMap();
		Map<String,Object> mapNew = new TreeMap<String,Object>();
		Iterator<Entry<String, Object[]>> fieldIterator = map.entrySet().iterator();
		while(fieldIterator.hasNext()){
			Map.Entry<String,Object[]> pairs = (Map.Entry<String,Object[]>)fieldIterator.next();
			String value = "";
			for (int i = 0; i < pairs.getValue().length ; i++){
				if (i>0){
					value+=","; // added when more than one enrty a , b , c etc.
				}
				
				value += pairs.getValue()[i];
			}
			
			mapNew.put(pairs.getKey(), value);
		}
		return mapNew;
		
	}
	
	
	/**
	 * Determines the fileType based on the extension of the filename
	 * @param fileName
	 * @return the fileType <BR/>
	 * word,pdf,powerpoint,excel,zip,png,gif,jpg,tif
	 */
	public static String determineFileType(String fileName){
		String fileType = "unknown";
		String fileNameSC = fileName.toLowerCase();
		if(fileNameSC.endsWith(".doc") || fileNameSC.endsWith(".docx") || fileNameSC.endsWith(".txt")|| fileNameSC.endsWith(".rtf")){
			fileType = "word";
		}
		if(fileNameSC.endsWith(".pdf")  ){
			fileType = "pdf";
		}
		
		if(fileNameSC.endsWith(".ppt")  ){
			fileType = "powerpoint";
		}
		
		if(fileNameSC.endsWith(".xls") || fileNameSC.endsWith(".xlsx") ){
			fileType = "excel";
		}
		if(fileNameSC.endsWith(".zip")  ){
			fileType = "zip";
		}
		if(fileNameSC.endsWith(".rar")  ){
			fileType = "rar";
		}
		
		if(fileNameSC.endsWith(".png") 
			|| fileNameSC.endsWith(".gif" )
			|| fileNameSC.endsWith(".png" )
			|| fileNameSC.endsWith(".jpg" )
			|| fileNameSC.endsWith(".tif" )
			|| fileNameSC.endsWith(".bmp" )
		)
		{
			fileType = fileName.toLowerCase().substring(fileName.length()-4);
		}
		return fileType;
	}
	
	/**
	 * Determines the content type to be returned to the request based on the extension of the filename
	 * @param fileName
	 * @return the content type
	 */
	public static String showContentType(String fileName){
		String contentType = "application/unknown";
		
		String fileNameSC = fileName.toLowerCase();
		
		if(fileNameSC.endsWith(".doc") || fileNameSC.endsWith(".docx") || fileNameSC.endsWith(".txt") || fileNameSC.endsWith(".rtf")){
			contentType = "application/msword";
		}
		if(fileNameSC.endsWith(".pdf")  ){
			contentType = "application/pdf";
		}
		if(fileNameSC.endsWith(".ppt")  ){
			contentType = "application/vnd.ms-powerpoint";
		}
		if(fileNameSC.endsWith(".xls") || fileNameSC.endsWith(".xlsx") ){
			contentType = "application/vnd.ms-excel";
		}
		if(fileNameSC.endsWith(".zip") ){
			contentType = "application/zip";
		}
		if(fileNameSC.endsWith(".rar")  ){
			contentType = "application/rar";
		}
		if(fileNameSC.endsWith(".png")
			|| fileNameSC.endsWith(".gif" )
			|| fileNameSC.endsWith(".bmp" )
			|| fileNameSC.endsWith(".jpg" )
			|| fileNameSC.endsWith(".tif" )
		)
		{
			contentType = "image/"+fileNameSC.substring(fileNameSC.length()-4);
		}
		return contentType;
	}

	/**
	 * Prepends zeroes to an integer to make it a string with 30 digits 
	 * @param num the integer to prepend zeroes to
	 * @return a 30 digit String integer prepended with zeroes
	 */
	public static String prependZeroes(int num){
		String intName = Integer.toString(num);		
		String target = "";
		for (int n = 0; n<30-intName.length(); n++){
			target+=target+"0";
		}
		target+=intName;
		return target;
		
	}
	

	
	/**
	 * Maps a raw string of values separated by a delimiter to a list of keys. 
	 * @param values the raw string of values separated by a delimiter
	 * @param delim the delimiter
	 * @param keys the keys to map the values to 
	 * @return the mapped values
	 * @throws Exception if the values do not match the number of keys
	 */
	public static Map<String, String> mapDelimitedValues(String values, String delim, String... keys) throws Exception {
		String[] valueArray = values.split(delim);
		TreeMap<String,String> map = new TreeMap<String,String>();
		if (valueArray.length != keys.length){
			throw new Exception ("The number of keys ("+keys.length+") do not match the number of values("+valueArray+")");
		}
		for (int i = 0 ; i < valueArray.length; i++){
			map.put(keys[i], valueArray[i]);
		}
		return map;
	}
	
	/**
	 * Adds to a map a raw string of values separated by a delimiter
	 * @param values the raw string of values separated by a delimiter
	 * @param regExDelim the delimiter which is a regular expression (e.g. for pipe delimited use \\| )
	 * @param map the map to add the values to
	 * @param keys the keys to map the values to 
	 * @return the mapped values
	 * @throws Exception if the values do not match the number of keys
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> addDelimitedValuesToMap(String values, String regExDelim, @SuppressWarnings("rawtypes") Map map, String... keys) throws Exception {
		
		String[] valuesList = values.split(regExDelim); 
		
		
		
		if (valuesList.length!= keys.length){
			throw new Exception ("Expected "+keys.length+" values but received only "+valuesList.length+" from input: "+values);
		}
		
		for (int i=0; i < valuesList.length;i++){
			map.put(keys[i], valuesList[i]);
		}
		
		return map;
	}
	
	/**
	 * Load properties with the default encoding
	 * @param the location of a single property file e.g. classpath:queries.xml
	 * @throws java.io.IOException in case of I/O errors
	 */
	public static Properties loadProperties(Resource location) throws IOException {
		return loadProperties(new Resource[]{location}, null);
	}
	
	
	/**
	 * Load properties with the default encoding
	 * @param the location of the property files e.g. classpath:queries.xml
	 * @throws java.io.IOException in case of I/O errors
	 */
	public static Properties loadProperties(Resource[] locations) throws IOException {
		return loadProperties(locations, null);
	}
	
	/**
	 * Load properties with the default encoding
	 * @param the location of the property files e.g. classpath:queries.xml
	 * @param the encoding of the property files
	 * @throws java.io.IOException in case of I/O errors
	 * @see #setLocations
	 */
	public static Properties loadProperties(Resource[] locations, String fileEncoding) throws IOException {
		Properties props = new Properties();
		if (locations != null) {
			PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
			for (Resource location : locations) {
				
				InputStream is = null;
				try {
					is = location.getInputStream();

					String filename = null;
					try {
						filename = location.getFilename();
					} catch (IllegalStateException ex) {
						// resource is not file-based. See SPR-7552.
					}

					if (filename != null && filename.endsWith(XML_FILE_EXTENSION)) {
						propertiesPersister.loadFromXml(props, is);
					}
					else {
						if (fileEncoding != null) {
							propertiesPersister.load(props, new InputStreamReader(is, fileEncoding));
						}
						else {
							propertiesPersister.load(props, is);
						}
					}
				}
				catch (IOException ex) {
					throw ex;
					
				}
				finally {
					if (is != null) {
						is.close();
					}
				}
			}
		}
		return props;
	}
	
	/**
	 * Filter out properties so it only keeps the ones that match the filter
	 * @param props the property list
	 * @param filter the filter to match
	 * @return the filtered properties
	 */
	public static Properties filterProperties (Properties props, String filter){
		
		if (filter == null){return props;}
		Properties propsFiltered = new Properties();
		Iterator<Entry<Object, Object>> fieldIterator = props.entrySet().iterator();
		while(fieldIterator.hasNext()){
			Map.Entry<Object,Object> pairs = (Map.Entry<Object,Object>)fieldIterator.next();
			if (((String)pairs.getKey()).startsWith(filter) ){
				String key = ((String)pairs.getKey()).substring(filter.length());
				Object value = pairs.getValue();
				propsFiltered.put(key, value);
				
			}
			
			
		}
		
		return propsFiltered;
	}
	
	/**
	 * Adds an argument to an array of arguments of type Object... 
	 * The argument will be appended to the end of the array
	 * @param arg the argument to add
	 * @param args the list of args
	 * @return the concatenated args
	 */
	public static Object[] concatArgs(String arg, Object... args){
		
		ArrayList<String> a = new ArrayList<String>();
		for (int i = 0; i < args.length; i++){
			a.add((String)args[i]);
		}
		a.add(arg);
		
		return a.toArray();
		
	}
	
	/**
	 * Gets the current timestamp using the specified format (standard java format)
	 * @param format the format to use e.g. dd/MM/yyyy hh:mm:ss
	 * @return the formatted current time
	 */
	public static String getCurrentTimestamp(String format){
		DateFormat df = new SimpleDateFormat(format);
		return df.format(new Date());
		
	}
	
	/**
	 * Get the remote IP address from the request
	 * @param request the http request
	 * @return the client's IP address.
	 */
	public static String getRemoteIpAddress(HttpServletRequest request){
		if (request == null){return null;}
		
		String ip = request.getHeader("X-Forwarded-For");
		
		if (ip == null){
			ip = request.getRemoteAddr();
		}
		
		return ip;
		
	}
	
		
	/**
	 * Converts decimals to amounts wuth greek notation
	 * @param amount the decimal amount xxxxxx.yy
	 * @return the amount in greek notation xxx.xxx,yy
	 */
	public static BigDecimal toBigDecimalAmounts(String amount){
		
		String deformatAmount = amount.replace(".", "").replace(",", "."); 
		
		return BigDecimal.valueOf(Double.parseDouble(deformatAmount));
	}
	
	/**
	 * Check if a variable called excelData exists and returns true. 
	 * It is a prerequisite to have the model already loaded with the request
	 * @param model
	 * @return true if the excelData param exists
	 */
	public static boolean doExcel(ModelMap model){
		
		if (model.get("excelData") != null && model.get("excelData").toString().length()>0){
			return true;
		}
		
		return false;
		
	}
	
	
	/**
	 * Creates a generic SSL Http Client. It sets max total connections to 30 and max connections per route to 30
	 * @param httpPort the http port to use
	 * @param httpsPort the https port to use
	 * @return the http client
	 */
	public static HttpClient genericSSLHttpClient(int httpPort, int httpsPort) {
		SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http",  httpPort, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", httpsPort, SSLSocketFactory.getSocketFactory()));

        PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
		// Increase max total connection to 30
		cm.setMaxTotal(30);
		// Increase default max connection per route to 30
		cm.setDefaultMaxPerRoute(30);
		
        
        HttpParams params = new BasicHttpParams();
        params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

        HttpClient client = new DefaultHttpClient(cm, params);

        return client;
    }
	
	/**
	 * Returns the current state of a POJO bean that is all the values of its instance fields
	 * @param bean the bean to use
	 * @return an array of Object containing the values of the bean's instance fields 
	 */
	public static <T> Object[] getBeanState(T bean){
		
		Field[] fieldsArray = bean.getClass().getDeclaredFields();
		Object[] objectArray = new Object[fieldsArray.length];
		for (int i = 0 ; i < fieldsArray.length; i++){
			Field f = fieldsArray[i];
			f.setAccessible(true);
			try {
				objectArray[i] = f.get(bean);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		return objectArray;
		
	}
	
	/**
	 * Creates DataResults from a List of Objects using reflection.
	 * This is typically used for Hibernate List results
	 * @param list the list of objects
	 * @param hasId if there is a class with Id that holds the data
	 * @return the generated Data Results
	 */
	public static <T> DataResults convertListToDataResults(java.util.List<T> list, boolean hasId){
		DataResults dRes = new DataResults();
		for (T listItem: list){
			String cName = listItem.getClass().getName();
			
			
			DataRow dr = new DataRow();
			try {
				Object o = listItem;
				if (hasId){
					cName = cName + "Id";
					o = listItem.getClass().getMethod("getId").invoke(listItem);
				}
				Class<?> c = Class.forName(cName);
				Field[] fA = c.getDeclaredFields();
				for (Field f: fA){
					f.setAccessible(true);

					dr.addReflectedField(f, o);

				}
				
			
			} catch (Exception e) {

				e.printStackTrace();
			}
			dRes.add(dr);
		}
		
		return dRes;
		
		
	}
	
}
