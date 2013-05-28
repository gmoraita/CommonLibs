package net.allwebdesign.common.lib.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class RestfulServiceReader implements ServiceReader {

	private String urlstr;
	private String encoding;
	
	/**
	 * Default constructor
	 */
	public RestfulServiceReader() {
		
	}
	
	
	
	/**
	 * Constructor that takes String params
	 * @param urlstr the URL of the service
	 * @param params the params to pass to the service
	 * @param encoding the encoding to use. Leave null for default encoding
	 */
	public RestfulServiceReader(String urlstr, String encoding){ 
		this.urlstr = urlstr;
		this.encoding = encoding;
	}
	
	/**
	 * Read a message from the service provided you setup already the url and encoding
	 * @param args the args to pass
	 * @return the message
	 */
	public String readMessage(Object... args){
		if (this.urlstr!= null && this.encoding != null){
			try {
				return execRead(urlstr,encoding,args);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	/**
	 * Converts the map to an array and uses only the values
	 */
	@Override
	public String readMessage(String urlstr, Map<String, Object> params, String encoding) throws Exception {
		//ArrayList<String> args = new ArrayList<String>();
		Object[] args = new String[params.size()];
		Iterator<Entry<String, Object>> it = params.entrySet().iterator(); 
		int i = 0;
		while(it.hasNext()){
			Entry<String, Object> entry = it.next();
			
			args[i] = entry.getValue();
			i++;
		}
		
		// TODO Auto-generated method stub
		return execRead(urlstr,encoding,args);
	}
	
	/**
	 * Connects to a Restful service and returns the output as a String
	 * @param urlStr the service url in the form http://[host]:[port] (without a / in the end)
	 * @param encoding the encoding to use
	 * @param args the args to pass so the resultant url is http://[host]:[port]/arg1/arg2/...
	 * @return the service message
	 * @throws Exception if there is a problem with the connection
	 */
	public static String read(String urlStr, String encoding, Object... args) throws Exception {
		return execRead(urlStr,encoding,args);
	}
	
	private static String execRead(String urlStr, String encoding, Object... args) throws Exception {
		BufferedReader in = null;
		
		StringBuffer msgBuf = new StringBuffer();
		StringBuffer urlStrBuf = new StringBuffer(urlStr);
		
		
		for (int i = 0; i<args.length; i++){
			
			urlStrBuf.append("/").append(args[i]);
			
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
