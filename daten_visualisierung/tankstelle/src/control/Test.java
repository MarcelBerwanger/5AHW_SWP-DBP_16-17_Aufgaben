package control;

import javax.swing.JFrame;
import model.DataObject;
import view.ChartView;

public class Test {

	public static void main(String[] args) {
		JFrame frame;
		ChartView chartView;
		DataManager instance;
		
		try {
			instance = DataManager.getInstance(); 
			chartView = new ChartView(DataObject.getSeries(instance.getYearData(1485649861)), instance.get24hData(1485649861));
			frame = chartView.showInFrame();
			chartView.saveAsPicture(frame);
			
			//Test von Abfragen
//			System.out.println("Erg");
//			for (DataObject d : DataManager.getInstance().get24hData(1485649861)) {
//				System.out.println(d.toString());
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
