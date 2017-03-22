package interpolate_jss;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class DBManager {
	private static DBManager instance;
	private Connection c;
	private static String table;
	
	private DBManager(){
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:db/verbrauch.db");
		    c.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static DBManager getInstance(String pTable){
		if(instance==null)instance = new DBManager();
		table=pTable;
		return instance;
	}
	
	public ArrayList<DayObj> getData(int fromYear) throws SQLException{
		ArrayList<DayObj> tempRes = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		
		//Grenzen des Jahres festlegen
		cal.set(fromYear, 0, 1); //0 & 1 sind Standardwerte
		String lower = String.valueOf(cal.getTimeInMillis()/1000);
		cal.set(fromYear, 11, 31); //0 & 1 sind Standardwerte
		String upper = String.valueOf(cal.getTimeInMillis()/1000);
		
		//Tabellen-Name durch Config ersetzten
		String command = "SELECT DISTINCT datum, value FROM "+table+" WHERE datum>="+lower+" AND datum <="+upper+" ORDER BY datum ASC"; //Holen des Timestamps
		PreparedStatement stmt = c.prepareStatement(command);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			tempRes.add(new DayObj(rs.getLong("datum"), rs.getDouble("value")));
		}
		return tempRes;
	}
}
