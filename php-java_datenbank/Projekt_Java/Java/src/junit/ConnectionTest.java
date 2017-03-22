package junit;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import WrapperClasses.Auto;
import WrapperClasses.Besitzer;
import WrapperClasses.Hersteller;
import main.DBManager;

public class ConnectionTest {
	//Felder der Klassenobjekte
	private static Besitzer b;
	private static Hersteller h;
	private static Auto a1;
	private static Auto a2;
	private static Auto a3;
	//Felder der ArrayLists
	private ArrayList<Besitzer> listB;
	private ArrayList<Hersteller> listH;
	private ArrayList<Auto> listA;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//Sqlite hat kein PRAGMA enthalten -> erster Test funktioniert nicht
		DBManager.getInstance("mysql", "autos", "root", "");
		b = new Besitzer(1, "Joe", "Hellen", "10.10.1997", 1200, "USA", "Virginia", "Elm Street 7", false);
		h = new Hersteller(12, "Ford", 1970, false);
		a1 = new Auto(1, 12, "Ford", 1998, 210, false);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		a1.deleteAuto();
		if(a2!=null)a2.deleteAuto();
		if(a3!=null)a3.deleteAuto();
		b.deleteBesitzer();
		h.deleteHersteller();
		DBManager.getInstance().closeConnection();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDBMethods() {
		//Speichern der Objekte in der Datenbank
		b.saveBesitzer();
		h.saveHersteller();
		a1.saveAuto();
		//Abfragen der gespeicherten Objekte
		listB = Besitzer.getBesitzer();
		listH = Hersteller.getHersteller();
		listA = Auto.getAutos();
		
		if(listB.get(0).getPersonID()==b.getPersonID() &&
				listH.get(0).getHerstellerID() == h.getHerstellerID() &&
				listA.get(0).getPersonenID() == a1.getPersonenID()){
			return;
		}
		else{fail("Test \"testDBMethods\" Fehlgeschlagen");}
	}
	
	@Test
	public void testFailInsert(){
		//Keine gültigen Personen-IDs
		a2 = new Auto(2, 2, "BMW", 1992, 230, false);
		a3 = new Auto(23, 3, "BMW", 1992, 230, false);
		a2.saveAuto();
		a3.saveAuto();
		listA = Auto.getAutos();
		for (Auto a : listA) {
			System.out.println(a.toString());
			if(a.getPersonenID() == 2 | a.getPersonenID()== 23){
				fail("Test \"testFailInsert\" fehlgeschlagen");
			}
		}
	}
	
	@Test
	public void testDelete(){
		a1.deleteAuto();
		a2.deleteAuto();
		a3.deleteAuto();
		b.deleteBesitzer();
		h.deleteHersteller();
		
		listB = Besitzer.getBesitzer();
		listH = Hersteller.getHersteller();
		listA = Auto.getAutos();
		
		if(listB.size()==0 && listH.size()==0 && listA.size()==0){
			return;
		}
		else{
			fail("Test \"testDelete\" fehlgeschlagen");
		}
	}
}
