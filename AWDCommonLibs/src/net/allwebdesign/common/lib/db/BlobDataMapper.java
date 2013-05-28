package net.allwebdesign.common.lib.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.DefaultLobHandler;

/**
 * Implementation of Spring's RowMapper for blobs usually images
 * @author George Moraitakis
 *
 */
public class BlobDataMapper implements RowMapper<DataRow> {

	/**
	 * Maps a row to a DataRow. The DataRow is agnostic to the fields etc. but is specialised to blobs
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
			DefaultLobHandler blob = new DefaultLobHandler();
			row.setValue(rs.getMetaData().getColumnName(i), blob.getBlobAsBinaryStream(rs, i));
		}
	
		return row;
	}

}
