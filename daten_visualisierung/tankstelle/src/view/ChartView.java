package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.graphics.Label;
import de.erichseifert.gral.graphics.Location;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.ui.InteractivePanel;
import model.DataObject;
import de.erichseifert.gral.graphics.Insets2D;


public class ChartView extends JPanel{

	private static final long serialVersionUID = -7690520438618218078L;
	private static final Random random = new Random();
	private static final String[] tableHeader = {"Tanke", "Zeit in UTC", "Preis in €"};
	private Object[][]data24h_view;

	public ChartView(ArrayList<DataSeries> series, ArrayList<DataObject> data24h, long timestamp) {
		super(new BorderLayout());
		
		// Create new xy-plot
		XYPlot plot = new XYPlot();
		
		// Format plot
		plot.setInsets(new Insets2D.Double(40.0, 80.0, 120.0, 200.0));
		plot.setBackground(Color.WHITE);
		plot.getTitle().setText(getDescription());
		// Format plot area
		plot.getPlotArea().setBackground(new RadialGradientPaint(
			new Point2D.Double(0.5, 0.5),0.75f, new float[] { 0.6f, 0.8f, 1.0f },
			new Color[] {new Color(0, 0, 0, 0), new Color(0, 0, 0, 32), new Color(0, 0, 0, 128)}
		));
		plot.getPlotArea().setBorderStroke(null);

		// Format x-axis
		AxisRenderer axisRendererX = plot.getAxisRenderer(XYPlot.AXIS_X);
		axisRendererX.setLabel(new Label("Datum"));
		axisRendererX.setLabelDistance(5.0);
		axisRendererX.setTickLabelFormat(DateFormat.getDateInstance());
		axisRendererX.setTickLabelRotation(90);
		plot.setAxisRenderer(XYPlot.AXIS_X, axisRendererX);
		// Format y-axis
		AxisRenderer axisRendererY = plot.getAxisRenderer(XYPlot.AXIS_Y);
		Label linearAxisLabel = new Label("Preis in €");
		linearAxisLabel.setRotation(90);
		axisRendererY.setLabel(linearAxisLabel);
		axisRendererY.setLabelDistance(2.0);
		plot.setAxisRenderer(XYPlot.AXIS_Y, axisRendererY);
		
		//Format Line & Add Data Series
		for (DataSeries dataSeries : series) {
			plot.add(dataSeries);
			DefaultLineRenderer2D discreteRenderer = new DefaultLineRenderer2D();
			discreteRenderer.setStroke(new BasicStroke(
					1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
					10.0f, new float[] {3f, 6f}, 0.0f));
			discreteRenderer.setColor(new Color(random.nextFloat(),random.nextFloat(),random.nextFloat(),1f));
			plot.setLineRenderers(dataSeries, discreteRenderer);
		}
		
		//initalisieren der Daten für die textuelle Darstellung
		data24h_view = new Object[data24h.size()][3];
		int index=0;
		for (DataObject obj : data24h) {
			data24h_view[index][0]=obj.getName();
			data24h_view[index][1]=obj.getDateHours();
			data24h_view[index][2]=obj.getPriceString();
			index++;
		}
		
		//Legende Konfigurieren
		plot.setLegendLocation(Location.EAST);
		plot.setLegendVisible(true);
		
		//JPanel zur textuellen Darstellung
		JPanel jp = new JPanel();
		jp.setLayout(new BorderLayout());
		
		//Einbinden der textuellen Darstellung
		JTable table = new JTable(data24h_view, tableHeader);
		JScrollPane spane = new JScrollPane(table);
		spane.getViewport().setBackground(Color.WHITE);
		spane.setBackground(Color.WHITE);
		spane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		//Label, das über der Tabelle steht
		Timestamp stamp = new Timestamp(timestamp*1000);
		Date date = new Date(stamp.getTime());
		Font f = new Font(Font.SANS_SERIF, 1, 25);
		JLabel label = new JLabel("Billigste Tankstelle 24h("+date.toString()+")");
		label.setFont(f);
		
		//add Components
		add(new InteractivePanel(plot), BorderLayout.CENTER);
		add(jp, BorderLayout.EAST);
		jp.add(label, BorderLayout.NORTH);
		jp.add(spane, BorderLayout.CENTER);
		jp.setBackground(Color.WHITE);
	}
	
	public String getTitle() {
		return "x-y plot";
	}

	public String getDescription() {
		return "Tankstellenpreise";
	}
	
	public JFrame showInFrame() {
		JFrame frame = new JFrame(getTitle());
		frame.getContentPane().add(this, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1500, 600);
		frame.setVisible(true);
		return frame;
	}
	
	public void saveAsPicture(JFrame frame) throws IOException{
		BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = image.createGraphics();
		frame.paint(graphics2D);
		ImageIO.write(image,"jpeg", new File("./pics/pic.jpeg"));
	}
}