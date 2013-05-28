package net.allwebdesign.common.lib.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * Bank Default implementation of Spring's RowMapper.
 * @author George Moraitakis
 *
 */
public class BDDataMapper implements RowMapper<DataRow> {

	/**
	 * Maps a row to a DataRow. The DataRow is agnostic to the fields etc. 
	 * @param rs the open resultset
	 * @param rowNum the total row numbers
	 * @return the object mapped
	 * @throws an SQL exception if there is a problem while mapping
	 *
	 */
	public DataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
		
	
		DataRow row = new DataRow();
		//if (rs.isClosed()){return null;}
		int cols = rs.getMetaData().getColumnCount();
		
		for (int i=1 ; i <= cols; i++){
			row.setValue(rs.getMetaData().getColumnName(i), rs.getString(i));
		}
	
		return row;
	}

}
