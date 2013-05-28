package net.allwebdesign.common.lib.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * An implementation of Spring's RowMapper. It has a single method
 * to handle the row by mapping 
 * @author George Moraitakis
 *
 */
public class DataMapper implements RowMapper<Object> {
	
	private RowHandler rowHandle;
	
	/**
	 * A constructor that takes as parameter the row handler and sets it.
	 * @param rowHandle the row handler.
	 */
	public DataMapper(RowHandler rowHandle){
		this.rowHandle = rowHandle;
	}
	
	/**
	 * Default constructor. It does nothing
	 */
	public DataMapper(){}	
	
	/**
	 * Maps a row to an Object. This object can be anything the developer decides
	 * @param rs the open resultset
	 * @param rowNum the total row numbers
	 * @return the object mapped
	 * @throws an SQL exception if there is a problem while mapping
	 */
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		return this.rowHandle.handle(rs, rowNum);
		
	}

	/**
	 * Gets the row handler
	 * @return the handler
	 */
	public RowHandler getRowHandle() {
		return rowHandle;
	}

	/**
	 * Sets the row handler
	 * @param rowHandle the row handler
	 */
	public void setRowHandle(RowHandler rowHandle) {
		this.rowHandle = rowHandle;
	}


	
}
