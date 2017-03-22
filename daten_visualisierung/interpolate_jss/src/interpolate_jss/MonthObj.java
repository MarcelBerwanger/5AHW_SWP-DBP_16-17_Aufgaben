package interpolate_jss;

/**
 * Stellt einen Monat dar
 * 
 * @author Marcel
 */
public class MonthObj {
	private String label;
	private double sumMonth;
	private double avgMonth;
	
	public MonthObj(String label, double sum, double days) {
		this.label = label;
		this.sumMonth = sum;
		this.avgMonth = sum/(days/86400); 
	}

	public String getLabel() {
		return label;
	}
	public double getSumMonth() {
		return sumMonth;
	}
	public double getAvgMonth(){
		return avgMonth;
	}
}
