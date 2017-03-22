package interpolate_jss;

import java.util.ArrayList;
import java.util.Calendar;

public class DayObj {
	private long timestamp;
	private double stand;
	private double kprev;
	private double kaft;
	private static ArrayList<DayObj> allDays=new ArrayList<>();
	
	public DayObj(long timestamp, double stand) {
		this.timestamp = timestamp;
		this.stand = stand;
		allDays.add(this);
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	public double getStand() {
		return stand;
	}
	public double getKprev() {
		return kprev;
	}
	public double getKaft() {
		return kaft;
	}
	public Calendar getDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp*1000);
		return cal;
	}
	public static int getListSize(){
		return allDays.size();
	}
	
	public void setKprev(double kprev) {
		this.kprev = kprev;
	}
	public void setKaft(double kaft) {
		this.kaft = kaft;
	}
	
	public static double getSum(long d1, long d2){
		double ku=0;
		double su=0;
		long tu=0;
		double ko=0;
		double so=0;
		long to=0;
		for (int i =0; i<allDays.size(); i++){
			if(allDays.get(i).getTimestamp()>d1){
				if(i-1<0){
					ku=allDays.get(i).getKaft();
					tu=allDays.get(i).getTimestamp();
					su=allDays.get(i).getStand();
				}else if(allDays.get(i-1).getKaft()>0){
					ku=allDays.get(i-1).getKaft();
					tu=allDays.get(i-1).getTimestamp();
					su=allDays.get(i-1).getStand();
				}else{
					ku=allDays.get(i-2).getKaft();
					tu=allDays.get(i-2).getTimestamp();
					su=allDays.get(i-2).getStand();
				}
				su=(((d1-tu)/86400)*ku)+su;
				break;
			}
		}
		for (int i =0; i<allDays.size(); i++){
			if(allDays.get(i).getTimestamp()>d2){
				if(allDays.get(i).getKprev()>0){
					ko=allDays.get(i).getKprev();
					to=allDays.get(i).getTimestamp();
					so=allDays.get(i).getStand();
				}else{
					ko=allDays.get(i+1).getKprev();
					to=allDays.get(i+1).getTimestamp();
					so=allDays.get(i+1).getStand();
				}
				so=so-(((to-d2)/86400)*ko);
				break;
			}
		}
		if(so==0){ //Wenn der letzte Wert höher als der letzte Wert im Array ist
			ko=allDays.get(allDays.size()-1).getKaft();
			to=allDays.get(allDays.size()-1).getTimestamp();
			so=allDays.get(allDays.size()-1).getStand();
			so=so+(((d2-to)/86400)*ko);
		}
		return so-su;
	}
}
