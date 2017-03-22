package WrapperClasses;
import java.util.ArrayList;
import java.util.Iterator;

import main.DBManager;

public class Auto {
	
	public Auto(int personenID, int herstellerID, String marke, int baujahr, int motorleistung, boolean isDatabase) {
		this.personenID = personenID;
		this.herstellerID = herstellerID;
		this.marke = marke;
		this.baujahr = baujahr;
		this.motorleistung = motorleistung;
		if(isDatabase)allAuto.add(this);
	}
	
	private int personenID;
	private int herstellerID;
	private String marke;
	private int baujahr;
	private int motorleistung;
	private static ArrayList<Auto> allAuto = new ArrayList<Auto>();
	
	public int getPersonenID() {
		return personenID;
	}
	public int getHerstellerID() {
		return herstellerID;
	}
	public String getMarke() {
		return marke;
	}
	public int getBaujahr() {
		return baujahr;
	}
	public int getMotorleistung() {
		return motorleistung;
	}
	
	//Methoden der Klasse
	public void saveAuto(){
		DBManager.getInstance().saveAuto(this);
	}
	public void deleteAuto(){
		DBManager.getInstance().deleteAuto(this);
		Iterator<Auto> it = allAuto.iterator();
		while (it.hasNext()) {
		  Auto a = it.next();
		  if (a.herstellerID==this.herstellerID && a.personenID==this.personenID) {
		    it.remove();
		  }
		}
	}
	public static ArrayList<Auto> getAutos(){
		DBManager.getInstance().getAutos();
		return allAuto;
	}
	
	public String toString(){
		return marke+": BJ"+baujahr+" PS: "+motorleistung;
	}
}
