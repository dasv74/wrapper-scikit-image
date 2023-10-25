package ch.epfl.skimage.process;

import java.awt.FlowLayout;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.epfl.skimage.ExchangeDocuments;
import ch.epfl.skimage.VirtualEnvironmentRunner;
import ch.epfl.skimage.components.GridPanel;
import ch.epfl.skimage.tools.Log;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.PolygonRoi;
import ij.gui.Roi;

@SuppressWarnings("serial")
public class WrapperScikitImageSnake extends WrapperSkimageAbstract {

	private String python;
	private JTextField wLine;
	private JTextField wEdge;
	
	public WrapperScikitImageSnake() {
		super("scikit-image-snake");
		python = "skimage_segmentation_snake.py";
	}
	
	public String getSyntax() {
		return "out = skimage.segmentation.active_contour(image, init)";
	}
	
	public JPanel getParametersPanel() {
		wLine = new JTextField("-1.0", 5);
		wEdge = new JTextField("1.0", 5);
		
		GridPanel pn = new GridPanel("Parameters", 8);
		pn.place(0, 1, new JLabel("w_line"));
		pn.place(0, 2, wLine);
		pn.place(0, 3, new JLabel("w_edge"));
		pn.place(0, 4, wEdge);
		pn.place(0, 5, bnRun);
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(pn);
		return panel;
	}

	public void process(VirtualEnvironmentRunner venv) {

		ArrayList<String> init = ExchangeDocuments.cleanExchangeDirectory();
		Log.write(init);
		
		ImagePlus imp = IJ.getImage();
		if (imp == null) {
			IJ.error("No open image.");
			return;
		}
		ArrayList<Point2D.Double> points = getOvalRoi(imp);

		ExchangeDocuments.sendListPoints(points, "temp_init_contour.csv");
		
		ExchangeDocuments.sendImagePlus(imp, "temp_input.tif");
	
        try {
            InputStream is = this.getClass().getResourceAsStream(python);
            if (is == null) {
            	System.out.println(python + " not found");
            	return;
            }

            Files.copy(is, Paths.get(ExchangeDocuments.path(python)), StandardCopyOption.REPLACE_EXISTING);
	
            ArrayList<String> args = new ArrayList<String>();
            args.add(python);
            args.add(ExchangeDocuments.path("temp_input.tif"));
            args.add(ExchangeDocuments.path("temp_output_contour.csv"));
            args.add(ExchangeDocuments.path("temp_init_contour.csv"));
            args.add(wLine.getText());
            args.add(wEdge.getText());;
                
            ArrayList<String> log = venv.runCommand(args);
            Log.write(log);
            
            ArrayList<Point2D.Double> list = ExchangeDocuments.getListPoints("temp_output_contour.csv");
            Polygon polygon = new Polygon();
            for(Point2D.Double pt : list) {
            	polygon.addPoint((int)Math.round(pt.y), (int)Math.round(pt.x));
            }
            imp.setRoi(new PolygonRoi(polygon, Roi.POLYGON));
        	is.close();
		}
        catch(IOException | InterruptedException ex) {
        	ex.printStackTrace();
        }
        thread=null;
        bnRun.setEnabled(true);
	}
	
	private ArrayList<Point2D.Double> getOvalRoi(ImagePlus imp) {
		ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();

		Roi roi = imp.getRoi();
		if (roi == null) {
			int w = imp.getWidth();
			int h = imp.getHeight();
			roi = new Roi(w/2-w/4, h/2-h/4, w/2, h/4);
			imp.setRoi(roi);
		}

		Rectangle rect = roi.getBounds();
		int w = rect.width / 2;
		int h = rect.height / 2;
		int xc = rect.x;
		int yc = rect.y;
		for(int a = 0; a < 360; a+=10) {
			double ar = Math.toRadians(a);
			double x = xc + w * Math.cos(ar);
			double y = yc + h * Math.sin(ar);
			points.add(new Point2D.Double(y + h, x + w));
		}
        Polygon polygon = new Polygon();
        for(Point2D.Double pt : points) {
        	polygon.addPoint((int)Math.round(pt.y), (int)Math.round(pt.x));
        }
        imp.setRoi(new PolygonRoi(polygon, Roi.POLYGON));
		return points;
	}
}
