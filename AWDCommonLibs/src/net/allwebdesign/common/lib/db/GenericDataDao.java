package net.allwebdesign.common.lib.db;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;


import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

public class GenericDataDao extends JdbcDaoSupport implements DataDao {


	
	@Override
	public DataResults execSQL(String sql, Object... args) {
		
		DataResults dataList = new DataResults(getJdbcTemplate().query(sql, new BDDataMapper(), args));
		return dataList;
	}	
	
	/**
	 * Execute an SQL and return a list of data of type datarow. It allows parametrised arguments
	 * It maps the data to a rowhandler and returns this as an object
	 * @param sql the sql to execute
	 * @param args the args to pass
	 * @param the row handler to use
	 * @return the object with the data
	 */	
	public Object execSQL(String sql, RowHandler handler, Object... args) throws DataAccessException {
		
		return getJdbcTemplate().queryForObject(sql, new DataMapper(handler), args);
		
	}	
	
	
	@Override
	public void updateSQL(String sql, Object... args) {
		
		this.getJdbcTemplate().update(sql, args);
	}


	
	
	@Override
	public DataResults execSproc(String sprocSql, Object... args) {

		return this.execSQL(sprocSql, args);
	}
	
	
	
	@Override
	public DataResults execBlobSQL(String sql, Object... args) {
		
		DataResults dataList = new DataResults(getJdbcTemplate().query(sql, new BlobDataMapper(), args));
		return dataList;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int execBlobUpdateSQL(String sql, final InputStream is, final long size, final Object... args){
		JdbcTemplate jt = this.getJdbcTemplate();
		try{
			


			jt.execute(     
					sql,     
					new PreparedStatementCallback() {         
						public Object doInPreparedStatement(PreparedStatement ps) throws SQLException{ 
							
							LobHandler lobHandler = new DefaultLobHandler();
							LobCreator lobCreator = lobHandler.getLobCreator();             
							lobCreator.setBlobAsBinaryStream(ps, 1, is, (int)size);   
							
							for(int i =0 ; i<args.length; i++){
								ps.setString(i+2, (String)args[i]);
							}
							int rows = ps.executeUpdate(); 
							
							return rows;
						}
					}
			); 

		}catch(Exception e){
			e.printStackTrace();
			Logger.getAnonymousLogger().severe("Could not insert file ");
			
		}
		return 0;
		
	}

}
