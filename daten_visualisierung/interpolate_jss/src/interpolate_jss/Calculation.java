package interpolate_jss;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Calculation {
	public static ArrayList<MonthObj> calculateAll(int year, String table) throws SQLException{
		calculateK(DBManager.getInstance(table).getData(year));
		return getAllMonthsData(year);
	}
	
	private static void calculateK(ArrayList<DayObj> tempDayList){
		DayObj prev = null;
		double diffDay=0;
		double diffVal=0;
		double tempPrev=0;
		double tempAft=0;
		
		//Alle Werte die gefunden wurden durchgehen und Steigungen berechnen
		for(int i=0; i<tempDayList.size(); i++){
			if(prev!=null){
				//Aktuelles - vergangenes; unterschied der Tage
				diffDay = tempDayList.get(i).getTimestamp()-prev.getTimestamp(); //Erster Tag hat 1
				diffVal = tempDayList.get(i).getStand()-prev.getStand();
				tempPrev = diffVal/(((diffDay/60)/60)/24);
			}if(i<(tempDayList.size()-1)){ //Wenn sich noch einer vor dem Element befindet
				diffDay = tempDayList.get(i+1).getTimestamp()-tempDayList.get(i).getTimestamp();
				diffVal = tempDayList.get(i+1).getStand()-tempDayList.get(i).getStand();
				tempAft = diffVal/(((diffDay/60)/60)/24);
			}else{
				tempAft=tempPrev;
			}if(prev==null){tempPrev=tempAft;}
			//Zuweisen der Errechneten Werte
			tempDayList.get(i).setKaft(tempAft);
			tempDayList.get(i).setKprev(tempPrev);
			//Aktualisieren des Nächsten Objektes
			prev = tempDayList.get(i);
		}
	}
	
	private static ArrayList<MonthObj> getAllMonthsData(int year){
		ArrayList<MonthObj> tempRes = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,year);
		long d1=0;
		long d2=0;
		
		for(int i=0; i<12; i++){
			cal.set(Calendar.MONTH, i);
			cal.set(Calendar.DAY_OF_MONTH,1);
			d1=cal.getTimeInMillis()/1000;
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			d2=cal.getTimeInMillis()/1000;
			tempRes.add(new MonthObj(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US), DayObj.getSum(d1, d2), d2-d1));
		}
		return tempRes;
	}
}
