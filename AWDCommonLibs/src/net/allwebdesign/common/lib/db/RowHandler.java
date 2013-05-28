package net.allwebdesign.common.lib.db;

import java.sql.ResultSet;

/**
 * This interface is used for classes that handle a data row. 
 * It has a single method whereby all the row handling takes place.
 * @author George Moraitakis
 * @version 1.0
 */
public interface RowHandler {
	/**
	 * Handle a row
	 * @param rs the open resultset
	 * @param rowNum the number of rows
	 * @return an Object representing this row
	 */
	public Object handle(ResultSet rs, int rowNum);
	
}
