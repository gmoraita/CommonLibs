package net.allwebdesign.common.lib.service;

import java.util.Map;

/**
 * An interface for reading responses from a remote service 
 * @author George Moraitakis
 *
 */
public interface ServiceReader {

	
	
	/**
	 * Reads a message from a source passing a set of encoded params 
	 * @param source the source of the service
	 * @param params the params to pass to the server
	 * @param encoding the encoding to use for the values. If null then will use the system encoding
	 * @return the response of the service
	 * @throws Exception if the connection is not established 
	 */
	public String readMessage(String source, Map<String, Object> params, String encoding) throws Exception;
}
