package main;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import WrapperClasses.Auto;
import WrapperClasses.Besitzer;
import WrapperClasses.Hersteller;



public class DBManager {
	
	private DBManager(){}
	private DBManager(String type, String name, String user, String password){
		this.databaseType = type;
		this.databaseName = name;
		this.username = user;
		this.password = password;
		
		if(databaseType.equals("mysql")){establishConnectionMySQL();}
		else{establishConnectionSqlite();}
	}
	
	private String databaseType;
	private String password;
	private String username;
	private String databaseName;
	private Connection con;
	
	private static DBManager instance;
	public static DBManager getInstance(){
		if(instance == null) instance = new DBManager();
		return instance;
	}
	public static DBManager getInstance(String type, String name, String user, String password){
		if(instance == null) instance = new DBManager(type, name, user, password);
		return instance;
	}
	
	private void establishConnectionMySQL(){
		Statement stmt;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//Verbiden zur Datenbank
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+databaseName, username, password);
		} catch (Exception e) {
			try {
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306", username, password);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			System.out.println("Die Datenbank existiert nicht\n Die Datenbank wird angeleget");
			try {
				stmt = con.createStatement();
			    FileReader fr = new FileReader("create_statement.txt");
			    BufferedReader br = new BufferedReader(fr);
			    String line;
			    while((line=br.readLine())!=null){
			    	stmt.addBatch(line);
			    }
			    br.close();
				stmt.executeBatch();
				System.out.println("Erfolgreich");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private void establishConnectionSqlite(){
		Statement stmt;
		try {
			Class.forName("org.sqlite.JDBC");
			//Verbiden zur Datenbank
			con = DriverManager.getConnection("jdbc:sqlite:"+databaseName+".db", username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(con!=null){
			try {
				stmt = con.createStatement();
			    FileReader fr = new FileReader("create_statementSqlite.txt");
			    BufferedReader br = new BufferedReader(fr);
			    String line;
			    while((line=br.readLine())!=null){
			    	stmt.addBatch(line);
			    }
			    br.close();
				stmt.executeBatch();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public boolean saveBesitzer(Besitzer obj){
		PreparedStatement stmt;
		String command = "SELECT * FROM besitzer WHERE personid=?";
		try {
			stmt = con.prepareStatement(command);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				rs.getInt("personid");
			}
			command = "UPDATE besitzer set personid=? vorname=?, nachname=?, geburtsdatum=?, gehalt=?, land=?, ort=?, adresse=?";
		} catch (Exception e1) {
			command = "INSERT INTO Besitzer VALUES (?,?,?,?,?,?,?,?)";
		}
		
		try{
			stmt = con.prepareStatement(command);
			stmt.setInt(1, obj.getPersonID());
			stmt.setString(2, obj.getVorname());
			stmt.setString(3, obj.getNachname());
			stmt.setString(4, obj.getDatum());
			stmt.setInt(5, obj.getGehalt());
			stmt.setString(6, obj.getLand());
			stmt.setString(7, obj.getOrt());
			stmt.setString(8, obj.getAddresse());
			stmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean deleteBesitzer(Besitzer obj){
		String command = "DELETE FROM Besitzer WHERE PersonID = ?";
		try{
			PreparedStatement stmt = con.prepareStatement(command);
			stmt.setInt(1, obj.getPersonID());
			stmt.executeUpdate();
		}catch(Exception e){return false;}
		return true;
	}
	public void getBesitzer(){
		String command = "SELECT * FROM Besitzer";
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			while(rs.next()){
				int pid = rs.getInt("personid");
				String vn = rs.getString("vorname");
				String nn = rs.getString("nachname");
				String geb = rs.getString("geburtsdatum");
				int gh = rs.getInt("gehalt");
				String lnd = rs.getString("land");
				String o = rs.getString("ort");
				String ad = rs.getString("adresse");
				new Besitzer(pid, vn, nn, geb, gh, lnd, o, ad, true);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean saveHersteller(Hersteller obj){
		PreparedStatement stmt;
		String command = "SELECT * FROM hersteller WHERE herstellerid=?";
		try {
			stmt = con.prepareStatement(command);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				rs.getInt("herstellerid");
			}
			command = "UPDATE besitzer set herstellerid=? name=?, gruendungsjahr=?";
		} catch (Exception e1) {
			command = "INSERT INTO hersteller (herstellerid, name, gruendungsjahr) VALUES (?,?,?)";
		}
		try{
			stmt = con.prepareStatement(command);
			stmt.setInt(1, obj.getHerstellerID());
			stmt.setString(2, obj.getName());
			stmt.setInt(3, obj.getGruendungsjahr());
			stmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean deleteHersteller(Hersteller obj){
		String command = "DELETE FROM hersteller WHERE HerstellerID = ?";
		try{
			PreparedStatement stmt = con.prepareStatement(command);
			stmt.setInt(1, obj.getHerstellerID());
			stmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public void getHersteller(){
		String command = "SELECT * FROM Hersteller";
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			while(rs.next()){
				int hid = rs.getInt("herstellerid");
				String n = rs.getString("name");
				int gj = rs.getInt("gruendungsjahr");
				new Hersteller(hid, n, gj, true);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
	public boolean saveAuto(Auto obj){
		PreparedStatement stmt;
		String command = "SELECT * FROM auto WHERE personenid=? AND herstellerid=?";
		try {
			stmt = con.prepareStatement(command);
			stmt.setInt(1, obj.getPersonenID());
			stmt.setInt(2, obj.getHerstellerID());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				rs.getInt("personid");
			}
			command = "UPDATE auto SET personid=?, herstellerid=? marke=? baujahr=?, motorleistung=?";
		} catch (Exception e1) {
			command = "INSERT INTO auto (PersonID, HerstellerID, Marke, Baujahr, Motorleistung) VALUES (?,?,?,?,?)";
		}
		try{
			stmt = con.prepareStatement(command);
			stmt.setInt(1, obj.getPersonenID());
			stmt.setInt(2, obj.getHerstellerID());
			stmt.setString(3, obj.getMarke());
			stmt.setInt(4, obj.getBaujahr());
			stmt.setInt(5, obj.getMotorleistung());
			stmt.executeUpdate();
			stmt.close();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean deleteAuto(Auto obj){
		String command = "DELETE FROM Auto WHERE PersonID = ? AND HerstellerID = ?";
		try{
			PreparedStatement stmt = con.prepareStatement(command);
			stmt.setInt(1, obj.getPersonenID());
			stmt.setInt(2, obj.getHerstellerID());
			stmt.executeUpdate();
			stmt.close();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public void getAutos(){
		String command = "SELECT * FROM Auto";
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			while(rs.next()){
				int pid = rs.getInt("personid");
				int hid = rs.getInt("herstellerid");
				String ma = rs.getString("marke");
				int bj = rs.getInt("baujahr");
				int ml = rs.getInt("motorleistung");
				new Auto(pid, hid, ma, bj, ml, true);
			}
			stmt.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//Abfrage mit JOIN-Verknüpfung. Sie liefert alle Hersteller von Autos, die
	//ein Besitzer hat. 
	public ArrayList<Hersteller> getHerstellerVonPerson(Besitzer pers){
		ArrayList<Hersteller> tempList = new ArrayList<>();
		String command = "SELECT herstellerid, hersteller.name as name, gruendungsjahr FROM besitzer JOIN auto USING(PersonID)"
						+"JOIN hersteller USING(HerstellerID) WHERE vorname = '" + pers.getVorname()+"'";
		try{
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(command);
			while(rs.next()){
				int hid = rs.getInt("herstellerid");
				String name = rs.getString("name");
				int jahr = rs.getInt("gruendungsjahr");
				tempList.add(new Hersteller(hid, name, jahr, false));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return tempList;
	}
	public void closeConnection(){
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
