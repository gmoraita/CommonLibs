package net.allwebdesign.common.lib.db.prepared.unittest;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import net.allwebdesign.common.lib.db.DataHandler;
import net.allwebdesign.common.lib.db.GenericDataHandler;

import net.allwebdesign.common.lib.db.templates.DBType;
import net.allwebdesign.common.lib.db.templates.DataSourceFactory;
import net.allwebdesign.common.lib.utils.Utils;


public class DBExecTest {


	private static  String tableName;
	private static DataSource ds;
	private static DataHandler appDataHandler;
	
	@BeforeClass
	public static void oneOffSetup(){
		ds = DataSourceFactory.createDBConnection(DBType.MSSQL, "localhost", "WebApp", "1433", "webuser", "P@ssw0rd");

	    appDataHandler = new GenericDataHandler(ds,DBType.MSSQL);
	    appDataHandler.init();

	    tableName = "ReqHandlerRequest";
	}
	
	@AfterClass
	public static void oneOffTearDown(){
		
	}
	
	@Before
	public void setup(){
		
		
	}
	
	@Test
	public void testNotNull() {
		
		
		//make sure we have instantiated object
		assertNotNull("DBExec is null",appDataHandler.getDbExec());
	}
	@Test
	public void testLoadTableDefinition() {
	
		//load a table definition
		assertNotNull("problems while loading and caching the table",appDataHandler.getDbExec().getTableDef(tableName));
	
	}
	
	@Test
	public void testTableInCache() {
		// load from cache directly
		assertNotNull("problem while loading directly from cache",appDataHandler.getDbExec().getDbCache().getFromCache(tableName));
	}
	
	@Test
	public void testSpeedRetrievingFromCache() {
		assertNotNull("problem while retriving definition from cache",appDataHandler.getDbExec().getTableDef(tableName));
	}
	
	@Test
	public void testRemoveFromCacheAndAddAgain() {
		appDataHandler.getDbExec().getDbCache().removeFromCache(tableName);
		assertNotNull("problem while retriving definition from cache after removing it",appDataHandler.getDbExec().getTableDef(tableName));
	}
	
	@Test
	public void testInsertIntoTableWithDirectValues() {
		
		try{
			appDataHandler.runInsertTable(tableName,true, false, "A second test client risk","12","443",Utils.getCurrentTimestamp("yyyyMMdd hh:mm:ss"));
		}
		catch(Exception e){
			
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testInsertIntoTableWithDataMap() {
		
		Map<String,Object> m = new TreeMap<String,Object>();
		m.put("row0_RiskDescription", "mapped description");
		m.put("row0_RiskRangeMin", "28");
		m.put("row0_RiskRangeMax", "42342");
		m.put("row0_RecTimestamp", Utils.getCurrentTimestamp("yyyyMMdd hh:mm:ss"));
		
		m.put("test", "will not be parsed");
		try{
			appDataHandler.runInsertTable(tableName,m);
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
			
		}
		
	}
	
	@Test
	public void testUpdateIntoTableWithDataMap() {
		
		Map<String,Object> m = new TreeMap<String,Object>();
		m.put("row0_Id", "12");
		m.put("row0_RiskDescription", "updated mapped description with hist");
		m.put("row0_RiskRangeMin", "2128");
		m.put("row0_RiskRangeMax", "242342");
		m.put("row0_RecTimestamp", Utils.getCurrentTimestamp("yyyyMMdd hh:mm:ss"));
		
		m.put("test", "will not be parsed");
		try{
			appDataHandler.runUpdateTableWithHist(tableName,m);
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
			
		}
		
	}
	
	@Test
	public void testBatchInsertWithDataMap() {
		
		Map<String,Object> m = new TreeMap<String,Object>();

		m.put("row0_RiskDescription", "r0 updated mapped description with hist");
		m.put("row0_RiskRangeMin", "11");
		m.put("row0_RiskRangeMax", "110");
		m.put("row0_RecTimestamp", Utils.getCurrentTimestamp("yyyyMMdd hh:mm:ss"));

		m.put("row1_RiskDescription", "r1 updated mapped description with hist");
		m.put("row1_RiskRangeMin", "22");
		m.put("row1_RiskRangeMax", "220");
		m.put("row1_RecTimestamp", Utils.getCurrentTimestamp("yyyyMMdd hh:mm:ss"));
		m.put("test", "will not be parsed");
		try{
			appDataHandler.runBatchInsert(tableName,m);
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
			
		}
		
	} 
	
	@Test
	public void testBatchReplaceWithDataMap() {

		String delWhereSql = "RiskDescription LIKE ?+'%' AND RecTimestamp > ? ";
		
		Map<String,Object> m = new TreeMap<String,Object>();

		m.put("row0_RiskDescription", "r0 updated mapped description with hist");
		m.put("row0_RiskRangeMin", "11");
		m.put("row0_RiskRangeMax", "110");
		m.put("row0_RecTimestamp", Utils.getCurrentTimestamp("yyyyMMdd hh:mm:ss"));

		m.put("row1_RiskDescription", "r1 updated mapped description with hist");
		m.put("row1_RiskRangeMin", "22");
		m.put("row1_RiskRangeMax", "220");
		m.put("row1_RecTimestamp", Utils.getCurrentTimestamp("yyyyMMdd hh:mm:ss"));
		m.put("test", "will not be parsed");
		try{
			appDataHandler.runBatchReplaceWithHist(tableName,m,delWhereSql,"r","20121224" );
		}
		catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
			
		}
		
	} 
	
		//do an update to a table 

	
		// do a multiple update


}
