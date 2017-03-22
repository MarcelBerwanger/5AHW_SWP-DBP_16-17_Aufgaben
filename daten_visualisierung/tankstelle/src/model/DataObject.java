package model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataTable;

public class DataObject {
	private String name;
	private long date;
	private double price;
	
	public DataObject(String name, long date, double price) {
		this.name = name;
		this.date = date;
		this.price = price;
	}
	
	public String getName() {
		return name;
	}
	public long getDate() {
		return date;
	}
	public double getPrice() {
		return price;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<DataSeries> getSeries(ArrayList<DataObject> dataObjects){
		ArrayList<DataSeries> resSeries = new ArrayList<>();
		String tempName = dataObjects.get(0).getName();
		DataTable table = new DataTable(Long.class, Double.class);
		
		for (DataObject dataObject : dataObjects) {
			if(tempName.equals(dataObject.getName())){
				table.add(dataObject.getDate(), dataObject.getPrice());
			}else{
				resSeries.add(new DataSeries(dataObject.name, table, 0,1));
				table = new DataTable(Long.class, Double.class);
			}
			tempName=dataObject.getName();
		}
		return resSeries;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH");
		DecimalFormat df = new DecimalFormat("#.000"); ;
		return "Tanke: "+name+ "; " + sdf.format(date)+":00 Uhr UTC"+ "; " +df.format(price)+"€";
	}
}
