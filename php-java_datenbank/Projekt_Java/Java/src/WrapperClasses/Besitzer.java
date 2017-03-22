package WrapperClasses;
import java.util.ArrayList;

import main.DBManager;


public class Besitzer {
	
	public Besitzer(int personID, String vorname, String nachname,
			String gebdatum, int gehalt, String land, String ort,
			String addresse, boolean isDatabase) {
		super();
		this.personID = personID;
		this.vorname = vorname;
		this.nachname = nachname;
		this.gebdatum = gebdatum;
		this.gehalt = gehalt;
		this.land = land;
		this.ort = ort;
		this.addresse = addresse;
		if(isDatabase)allBesitzer.add(this);
	}
	
	private int personID;
	private String vorname;
	private String nachname;
	private String gebdatum;
	private int gehalt;
	private String land;
	private String ort;
	private String addresse;
	private static ArrayList<Besitzer> allBesitzer = new ArrayList<Besitzer>();
	
	public int getPersonID() {
		return personID;
	}
	public String getVorname() {
		return vorname;
	}
	public String getNachname() {
		return nachname;
	}
	public String getDatum() {
		return gebdatum;
	}
	public int getGehalt() {
		return gehalt;
	}
	public String getLand() {
		return land;
	}
	public String getOrt() {
		return ort;
	}
	public String getAddresse() {
		return addresse;
	}
	
	//Methoden der Klasse
	public void saveBesitzer(){
		DBManager.getInstance().saveBesitzer(this);
	}
	public void deleteBesitzer(){
		DBManager.getInstance().deleteBesitzer(this);
		if(allBesitzer.contains(this)){
			allBesitzer.remove(this);
		}
	}
	public ArrayList<Hersteller> getHerstellerVonPerson(){
		return DBManager.getInstance().getHerstellerVonPerson(this);
	}
	public static ArrayList<Besitzer> getBesitzer(){
		DBManager.getInstance().getBesitzer();
		return allBesitzer;
	}
	
	public String toString(){
		return vorname+", "+nachname+": "+gebdatum;
	}
}
