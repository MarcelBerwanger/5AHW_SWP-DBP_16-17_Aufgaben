package interpolate_jss;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Properties;
import jcckit.GraphicsPlotCanvas;
import jcckit.data.DataCurve;
import jcckit.data.DataPlot;
import jcckit.data.DataPoint;
import jcckit.util.ConfigParameters;
import jcckit.util.PropertiesBasedConfigData;

public class AnimatedChart extends Applet {

	private static final long serialVersionUID = 1L;
	private double[] _data; // Daten, die auf dem Diagramm dargestellt werden
	private DataPlot _dataPlot;
	private String titleY; // Titel der y-Achse
	private String titleX; // Titel der x-Achse
	private int maximum;

	public AnimatedChart(String titleY, String titleX, double[] arr, int maximum) {
		_data = arr;
		this.titleX = titleX;
		this.titleY = titleY;
		this.maximum = maximum;
	}

	public void init(String labels) {
		GraphicsPlotCanvas plotCanvas = createPlotCanvas(labels);

		_dataPlot = new DataPlot();
		_dataPlot.addElement(new DataCurve(""));
		plotCanvas.connect(_dataPlot);

		setLayout(new BorderLayout());
		add(plotCanvas.getGraphicsCanvas(), BorderLayout.CENTER);
		add(createControlPanel(), BorderLayout.SOUTH);
	}

	// Alle Einstellung des Diagramms erfolgen in dieser Methode
	// es gibt keinen anderen Weg die Einstellungen vorzunehmen
	// weiters gibt es Probleme bei der Skalierung der y-Achse ->
	// diese kann keine Kommazahlen darstellen
	private GraphicsPlotCanvas createPlotCanvas(String lables) {
		Properties props = new Properties();
		ConfigParameters config = new ConfigParameters(new PropertiesBasedConfigData(props));
		props.put("plot/legendVisible", "false");
		// X-Achse Skalierung und Ticks
		props.put("plot/coordinateSystem/xAxis/minimum", "0.5");
		props.put("plot/coordinateSystem/xAxis/maximum", "11.5");
		props.put("plot/coordinateSystem/xAxis/ticLabelFormat/className", "jcckit.plot.TicLabelMap");
		// Label der X-Achse -> Labels aus der der Methode createLabels
		props.put("plot/coordinateSystem/xAxis/ticLabelFormat/map", lables);
		// Titel der Y-Achse
		props.put("plot/coordinateSystem/yAxis/axisLabel", titleY);
		// Maximum der Y-Achse
		props.put("plot/coordinateSystem/yAxis/maximum", String.valueOf(maximum));
		props.put("plot/coordinateSystem/yAxis/ticLabelFormat", "%d%");
		props.put("plot/curveFactory/definitions", "curve");
		props.put("plot/curveFactory/curve/withLine", "false");
		// Maximum der X-Achse
		props.put("plot/coordinateSystem/xAxis/axisLabel", titleX);
		// Bars anzeigen
		props.put("plot/curveFactory/curve/symbolFactory/className", "jcckit.plot.BarFactory");
		// Shape festlegen
		props.put("plot/curveFactory/curve/symbolFactory/attributes/className", "jcckit.graphic.ShapeAttributes");
		// Farbe festlegen
		props.put("plot/curveFactory/curve/symbolFactory/attributes/fillColor", "0xfe8000");
		props.put("plot/curveFactory/curve/symbolFactory/attributes/lineColor", "0");
		props.put("plot/curveFactory/curve/symbolFactory/size", "0.06");
		props.put("plot/initialHintForNextCurve/className", "jcckit.plot.PositionHint");
		props.put("plot/initialHintForNextCurve/position", "0 0.1");

		return new GraphicsPlotCanvas(config);
	}

	// Diese Methode fügt den Button den Diagramm hinzu
	private Panel createControlPanel() {
		// Panel mit Button erzeugen
		Panel controlPanel = new Panel();
		Button startButton = new Button("Balken animieren");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run() {
						animate();
					}
				}.start();
			}
		});
		controlPanel.add(startButton);

		return controlPanel;
	}

	private void animate() {
		// Balken animieren
		DataCurve curve = new DataCurve("");
		for (int i = 0; i < _data.length; i++) {
			curve.addElement(new DataPoint(i, 0));
		}
		_dataPlot.replaceElementAt(0, curve);

		for (int i = 0; i < _data.length; i++) {
			double x = i;
			double y = 0;
			while (y < _data[i]) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
				y = Math.min(_data[i], y + 5);
				curve.replaceElementAt(i, new DataPoint(x, y));
			}
		}
	}

	private static void createFrame(String titleY, String titleX, double[]param, String labels, int maximum) {
		// Frame erzeugen
		Frame frame = new Frame("Diagramm");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				System.exit(0);
			}
		});

		AnimatedChart applet = new AnimatedChart(titleY, titleX, param, maximum);
		applet.init(labels);
		frame.add(applet);
		frame.setSize(600, 500);
		frame.show();
	}

	public static void main(String[] args) {
		double paramSum[]=new double[12];
		double paramAvg[]=new double[12];
		int index = 0;
		int maxsum=0;
		int maxavg=0;
		String label="";
		
		try {
			for (MonthObj mobj : Calculation.calculateAll(2016, "strom_verbrauch")) {
				paramSum[index] = mobj.getSumMonth();
				label+=index+"="+mobj.getLabel()+";";
				if(mobj.getSumMonth()>maxsum)maxsum=(int)mobj.getSumMonth();
				paramAvg[index] = mobj.getAvgMonth();
				if(mobj.getAvgMonth()>maxavg)maxavg=(int)mobj.getAvgMonth();
				index++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Erzeugen der Fenster für die Durchschnittsverbraeuche
		createFrame("Verbrauchte Ressourcen", "Monate", paramSum, label, maxsum);
		createFrame("Durchscnitt", "Monate", paramAvg, label, maxavg);
	}

}
