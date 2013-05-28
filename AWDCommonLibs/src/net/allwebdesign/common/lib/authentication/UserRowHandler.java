package net.allwebdesign.common.lib.authentication;


import java.sql.ResultSet;
import java.sql.SQLException;

import net.allwebdesign.common.lib.db.RowHandler;

/**
 * An implementation of RowHoandler that handles rows with user info
 * @author George Moraitakis
 */
public class UserRowHandler implements RowHandler  {

	/**
	 * Handles a user row and return a LoggedUser object
	 * @param rs the open resultset
	 * @param rowNum the number of rows
	 * @return a LoggedUsed Object 
	 */
	public LoggedUser handle(ResultSet rs, int rowNum) {
		// TODO Auto-generated method stub
		try {
			return new LoggedUser(rs.getString("Username"), "password", true, true, true, true, LoggedUser.getAuthorities(rs.getString("Role")), rs.getString("FullName"), rs.getString("EmployeeCode") );
		}
		catch(SQLException sqle){
			sqle.printStackTrace();
			return null;
		}
	}

}
