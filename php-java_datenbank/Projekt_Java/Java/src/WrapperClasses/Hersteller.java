package WrapperClasses;
import java.util.ArrayList;

import main.DBManager;


public class Hersteller {
	
	
	public Hersteller(int herstellerID, String name, int gruendungsjahr, boolean isDatabase) {
		super();
		this.herstellerID = herstellerID;
		this.name = name;
		this.gruendungsjahr = gruendungsjahr;
		if(isDatabase)allHersteller.add(this);
	}

	private int herstellerID;
	private String name;
	private int gruendungsjahr;
	private static ArrayList<Hersteller> allHersteller = new ArrayList<Hersteller>();
	
	public int getHerstellerID() {
		return herstellerID;
	}
	public String getName() {
		return name;
	}
	public int getGruendungsjahr() {
		return gruendungsjahr;
	}
	
	//Methoden der Klasse
	public void saveHersteller(){
		DBManager.getInstance().saveHersteller(this);
	}
	
	public void deleteHersteller(){
		DBManager.getInstance().deleteHersteller(this);
		if(allHersteller.contains(this)){
			allHersteller.remove(this);
		}
	}
	
	public static ArrayList<Hersteller> getHersteller(){
		DBManager.getInstance().getHersteller();
		return allHersteller;
	}
	
	public String toString(){
		return name+": "+gruendungsjahr;
	}
}
