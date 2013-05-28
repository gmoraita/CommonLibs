package net.allwebdesign.common.lib.db.templates;

public class ORACLESQLTemplate implements SQLTemplate {



	@Override
	public String showRecordsSql(String table) {
		return "SELECT * FROM "+table;

	}

	@Override
	public String selectRecordsSql(String table, String[] criteriaFields) {
		StringBuffer sqlBuf = new StringBuffer("SELECT * FROM "+table+" WHERE ");
		String comma = "";
		for (int i = 0 ; i < criteriaFields.length; i++){
			sqlBuf.append(comma).append(criteriaFields[i]).append(" = ?");
			comma = ",";
		}		
		
		return sqlBuf.toString();
		
	}

	@Override
	public String updateRecordSql(String table,  String[] fields) {
		StringBuffer sqlBuf = new StringBuffer("UPDATE "+table+" SET ");
		String comma = "";
		for (int i = 0 ; i < fields.length; i++){
			sqlBuf.append(comma).append(fields[i]).append(" = ?");
			comma = ",";
		}	
	
		sqlBuf.append(" WHERE Id = ?");
		
		return sqlBuf.toString();
		

	}

	@Override
				  
	public String insertRecordSql(String table,  String[] fields) {
		StringBuffer sqlBuf = new StringBuffer("INSERT INTO "+table+" (");

		String comma = "";
		for (int i = 0 ; i < fields.length; i++){
			sqlBuf.append(comma).append(fields[i]);
			comma = ",";
		}	
		
		comma = ") VALUES (";
		for (int i = 0 ; i < fields.length; i++){
		
			sqlBuf.append(comma).append("?");
			comma = ",";
		}
		sqlBuf.append(")");
		
		return sqlBuf.toString();
		
		

	}

	@Override
	public String deleteRecordSql(String table) {
		return deleteCustomSql(table,"Id = ?");

	}
	
	@Override
	public String deleteCustomSql(String table, String whereClause) {
		return "DELETE FROM "+table+" WHERE "+whereClause;

	}

	@Override
	public String execSprocSql(String sproc,  String[] fields)  {
		
		StringBuffer sqlBuf = new StringBuffer("EXEC "+sproc+" ");

		String comma = "";
		for (int i = 0 ; i < fields.length; i++){
			sqlBuf.append(comma).append("@").append(fields[i]).append(" = ?");
			comma = ",";
		}	
		
		return sqlBuf.toString();

	}

	@Override
	/**
	 * Unsupported
	 */
	public String openTranSql() {
		return null;
	}

	@Override
	/**
	 * Unsupported
	 */
	public String commitTranSql() {
		return null;
	}

	@Override
	/**
	 * Unsupported
	 */
	public String rollbackTranSql() {
		return null;
	}

	@Override
	/**
	 * Unsupported
	 */
	public String getLastId() {
		return null;
	}

	@Override
	/**
	 * Unsupported
	 */
	public String assignToVarLastId(String varName) {
		return null;
	}

	@Override
	public String copyToTableSql(String tableOrigin, String tableTarget, String field) {
		
		return copyToTableCustomSql(tableOrigin,tableTarget,field+" = ?");
	}
	
	@Override
	public String copyToTableCustomSql(String tableOrigin, String tableTarget, String whereClause ) {
		
		String sql = "INSERT INTO "+tableTarget+"\n SELECT * FROM "+tableOrigin;
		if (whereClause != null){
			sql+="\n WHERE "+whereClause;
		}
		
		return sql;
	}

	@Override
	public String fetchTableDefinition(String tableName) {
		//TODO In future versions show full table definition
		String sql = "Select COLUMN_NAME from user_tab_columns where table_name='"+tableName+"'";
		return sql;
	}

	@Override
	public String fetchTableColumns(String tableName) {
		String sql = "Select COLUMN_NAME from user_tab_columns where table_name='"+tableName+"'";
		return sql;
	}

}
