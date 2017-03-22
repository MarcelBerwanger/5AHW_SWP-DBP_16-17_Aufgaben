package interpolate_jss;

import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		ArrayList<MonthObj> list=null;
		
		try {
			list = Calculation.calculateAll(2016, "strom_verbrauch");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (MonthObj monthObj : list) {
			System.out.println(monthObj.getLabel());
			System.out.println(monthObj.getSumMonth());
			System.out.println(monthObj.getAvgMonth());
		}
	}

}
