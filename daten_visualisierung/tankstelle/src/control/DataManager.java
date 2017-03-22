package control;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import model.DataObject;

public class DataManager {
	private final long seconds_year = 31536000;
	private final long seconds_day = 86000;
	//Konstanten zum abfragen der Properties
	private final String props_dbname="dbname";
	private final String props_table_name="table_names";
	private final String props_table_data="table_data";
	
	private static DataManager instance;
	private Connection c;
	private Properties properties;
	
	private DataManager(){
		try(FileInputStream in = new FileInputStream("database.properties")) {
			properties = new Properties();
			properties.load(in);
			
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:db/"+properties.getProperty(props_dbname));
		    c.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static DataManager getInstance(){
		if(instance==null)instance = new DataManager();
		return instance;
	}
	//DATE_FORMAT(FROM_UNIXTIME(`sprit_data.datum`), '%e %b %Y') AS 'date_formatted'
	//strftime('%d - %m  - %Y ', datetime(1281353727, 'unixepoch'))
	public ArrayList<DataObject> getYearData(long start_time) throws SQLException{
		ArrayList<DataObject> tempRes = new ArrayList<>();
		String command = "SELECT name, AVG(value) as avg_price, datum FROM "+properties.getProperty(props_table_data)
				+ " JOIN "+properties.getProperty(props_table_name)+" USING (tankenr)"
				+ " WHERE datum < "+start_time+" AND datum > "+(start_time-seconds_year)
				+ " GROUP BY tankenr, strftime('%d-%m-%Y', datetime(datum, 'unixepoch'))";
		PreparedStatement stmt = c.prepareStatement(command);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			tempRes.add(new DataObject(rs.getString("name"), rs.getLong("datum")*1000, rs.getDouble("avg_price")));
		}
		return tempRes;
	}
	
	public ArrayList<DataObject> get24hData(long start_time) throws SQLException{
		ArrayList<DataObject> tempRes = new ArrayList<>();
		//TODO Abfrage anpassen
		String command ="SELECT name, MIN(avg_price) as avg_price, datum FROM" 
				+ " (SELECT name, AVG(value) as avg_price, datum FROM "+properties.getProperty(props_table_data)
				+ " JOIN "+properties.getProperty(props_table_name)+" USING (tankenr)"
				+ " WHERE datum < "+start_time+" AND datum > "+(start_time-seconds_day)
				+ " GROUP BY tankenr, strftime('%H', datetime(datum, 'unixepoch'))) as sq"
				+ " GROUP BY strftime('%H', datetime(datum, 'unixepoch'))";
		PreparedStatement stmt = c.prepareStatement(command);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()){
			tempRes.add(new DataObject(rs.getString("name"), rs.getLong("datum")*1000, rs.getDouble("avg_price")));
		}
		return tempRes;
	}
}
