package main;
import java.util.ArrayList;
import java.util.Scanner;

import WrapperClasses.Auto;
import WrapperClasses.Besitzer;
import WrapperClasses.Hersteller;

public class Main {

	public static void main(String[] args) {
		if(args.length==0 || 
			args[0].equals("") || 
			args[1].replaceAll("-n", "").equals("") ||
			args[2].replaceAll("-u", "").equals("") ){
			
			System.out.println("Es fehlen Argumente, erfordelich sind:\n1)Datenbanktyp 2)Datenbankname 3)Username 4)Password");
			System.exit(0);
		}
		if(args[0].toLowerCase().equals("sqlite") || args[0].toLowerCase().equals("mysql")){
			DBManager.getInstance(args[0], args[1].replaceAll("-n", ""),
					args[2].replaceAll("-u", ""), args[3].replaceAll("-p", ""));
		}
		else{
			System.out.println("Ungültiger Datenbank Typ - Das Programm wird beendet");
			System.exit(0);
		}
		Scanner scanner = new Scanner(System.in);
		boolean weiter = true;
		
		Besitzer bes = new Besitzer(1, "Joe", "Hellen", "10.10.1997", 1200, "USA", "Virginia", "Am Ass 7", false);
		Hersteller her = new Hersteller(12, "Ford", 1970, false);
		Hersteller her1 = new Hersteller(11, "BMW", 1871, false);
		Hersteller her2 = new Hersteller(10, "Fiat", 1971, false);
		Auto aut = new Auto(1, 12, "Ford", 1998, 210, false);
		Auto aut2 = new Auto(1, 11, "BMW", 2007, 218, false);
		Auto aut3 = new Auto(1, 10, "Fiat", 2012, 18, false);
		
		bes.saveBesitzer();
		her.saveHersteller();
		her1.saveHersteller();
		her2.saveHersteller();
		aut.saveAuto();
		aut2.saveAuto();
		aut3.saveAuto();
		
		ArrayList<Besitzer> arrbes = Besitzer.getBesitzer();
		ArrayList<Hersteller> arrher = Hersteller.getHersteller();
		ArrayList<Auto> arraut = Auto.getAutos();
		ArrayList<Hersteller> arrherJoin = bes.getHerstellerVonPerson();
		//Ausgabe der Erhaltenen Ergebnise
		System.out.println("Autos: ");
		for (Auto a : arraut) {
			System.out.println(a.toString());
		}
		System.out.println("\nHersteller: ");
		for (Hersteller h : arrher) {
			System.out.println(h.toString());
		}
		System.out.println("\nBesitzer: ");
		for (Besitzer b : arrbes) {
			System.out.println(b.toString());
		}
		System.out.println("JOIN Abfrage für Besitzer: " + bes.toString());
		for (Hersteller he : arrherJoin) {
			System.out.println(he.toString());
		}
		//Löschen aller Einträge, damit sie beim nächsten Ausführen wieder hinzugefügt werden können
		aut.deleteAuto();
		aut2.deleteAuto();
		aut3.deleteAuto();
		her.deleteHersteller();
		her1.deleteHersteller();
		her2.deleteHersteller();
		bes.deleteBesitzer();
		
		//Konsolenanwendung bzw. Eingabe Hersteller
		do{
			int hid=-1, gja = -1;
			String name="";
			String in = "";
			
			System.out.println("\nEigener Hersteller hinzufügen");
			System.out.println("===============================");
			do{
				System.out.print("HerstellerID (int): ");
				try{
					hid = Integer.valueOf(scanner.nextLine());
				}catch(Exception e){System.out.println("Fehler - Eine Integer Zahl ist erforderlich!");}
			}while(hid==-1);
			do{
				System.out.print("\nName: ");
				if((name = scanner.nextLine()).equals("")) System.out.println("Bitte einen Namen angeben!");
			}while(name.equals(""));
			do{
				try{
					System.out.print("\nGruendungsjahr(JJJJ): ");
					gja = Integer.valueOf(scanner.nextLine());
				}catch(Exception e){System.out.println("Fehler - Eine Integer Zahl ist erforderlich!");}
			}while(gja==-1);
			new Hersteller(hid, name, gja, true);
			System.out.println("--> Fertig");
			
			do{
				System.out.print("Noch einen anlegen? [j/n]: ");
				in = scanner.nextLine();
				if(in.equals("") | !(in.equals("n")|in.equals("j"))){
					System.out.println("Eingabe ungültig, bitte erneut Versuchen!");
				}
			}while(in.equals("") | !(in.equals("n") | in.equals("j")));
			if(in.equals("n"))weiter = false;
		}while(weiter);
		
		scanner.close();
		DBManager.getInstance().closeConnection();
	}
}
