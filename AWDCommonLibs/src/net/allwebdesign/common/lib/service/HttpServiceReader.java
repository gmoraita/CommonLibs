package net.allwebdesign.common.lib.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Connects to a remote http service
 * @author George Moraitakis
 *
 */
public class HttpServiceReader implements ServiceReader {

	private String urlstr;
	private Map<String,Object> params;
	private String encoding;
	
	
	/**
	 * Default constructor
	 */
	public HttpServiceReader() {
		
	}
	
	
	
	/**
	 * Constructor that takes String params
	 * @param urlstr the URL of the service
	 * @param params the params to pass to the service
	 * @param encoding the encoding to use. Leave null for default encoding
	 */
	public HttpServiceReader(String urlstr, Map<String,Object> params, String encoding){ 
		this.urlstr = urlstr;
		this.params = params;
		this.encoding = encoding;
	}


	
	/**
	 * Reads a message from the http source 
	 */
	@Override
	public String readMessage(String urlstr, Map<String,Object> params, String encoding) throws Exception {
		return execRead(urlstr,params, encoding);
	}
	
	/**
	 * Convenience method to call the service after instantiating the class
	 * @return the message
	 * @throws Exception if the connection cannot be established or params are not set
	 */
	public String readMessage() throws Exception{
		return this.readMessage(urlstr, params, encoding);
	}
	
	
	/**
	 * Reads a message from the http source 
	 * @param urlstr the URL of the service
	 * @param params the params to pass to the service
	 * @param encoding the encoding to use. Leave null for default encoding
	 * @return the response of the service
	 * @throws Exception if the connection is not established or the params list is malformatted 
	 */
	public static String read(String urlstr, Map<String,Object> params, String encoding) throws Exception {
		return execRead(urlstr, params, encoding);
	}
	
	

	
	private static String execRead(String urlStr, Map<String, Object> params, String encoding) throws Exception {
		BufferedReader in = null;
		
		StringBuffer msgBuf = new StringBuffer();
		StringBuffer urlStrBuf = new StringBuffer(urlStr+"?");
		int i =0;
		Iterator<Entry<String, Object>> it = params.entrySet().iterator(); 
		while(it.hasNext()){
			Entry<String, Object> entry = it.next();
			
			urlStrBuf.append(entry.getKey()).append("=");
			
			if (encoding != null){
				urlStrBuf.append(URLEncoder.encode((String)entry.getValue(), encoding));
			}
			else{
				urlStrBuf.append((String)entry.getValue());
			}
			
			if (i<params.size()){urlStrBuf.append("&");}
			i++;
		}
	
		
		
		try{
			URL url = new URL(urlStrBuf.toString());
			in = new BufferedReader(	new InputStreamReader(	url.openStream(),encoding));
	
			String inputLine;
			
	
			while ((inputLine = in.readLine()) != null){
			    
			    msgBuf.append(inputLine);
			}
	
			
		}
		catch(Exception e){
			throw new Exception("Problem while reading the message from the remote server",e);
			
		}
		finally{
			if (in != null ){
				try{
					in.close();
				}
				catch(Exception e){
					throw new Exception("Problem while closing the connection to the remote server",e);

				}
			}
		}
		return msgBuf.toString();
	}

}
