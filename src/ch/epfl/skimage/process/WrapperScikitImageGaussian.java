package ch.epfl.skimage.process;

import java.awt.FlowLayout;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.epfl.skimage.ExchangeDocuments;
import ch.epfl.skimage.VirtualEnvironmentRunner;
import ch.epfl.skimage.components.GridPanel;
import ch.epfl.skimage.tools.Log;
import ij.IJ;
import ij.ImagePlus;

@SuppressWarnings("serial")
public class WrapperScikitImageGaussian extends WrapperSkimageAbstract {

	private String python;
	private JTextField txtSigma;
	private JComboBox<String> cmbMode;
	
	public WrapperScikitImageGaussian() {
		super("scikit-image-gaussian");
		python = "skimage_filters_gaussian.py";
	}
	
	public String getSyntax() {
		return "out = skimage.filters.gaussian(image, sigma, mode)";
	}
	
	public JPanel getParametersPanel() {
		txtSigma = new JTextField("3.0", 5);
		cmbMode = new JComboBox<String>(new String[] {"reflect", "constant", "nearest", "mirror", "wrap"});

		GridPanel pn = new GridPanel("Parameters", 8);
		pn.place(0, 1, new JLabel("sigma"));
		pn.place(0, 2, txtSigma);
		pn.place(0, 3, new JLabel("mode"));
		pn.place(0, 4, cmbMode);
		pn.place(0, 5, getRunButton());
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(pn);
		return panel;
	}

	public void process(VirtualEnvironmentRunner venv) {
		
		ImagePlus imp = IJ.getImage();
		if (imp == null) {
			IJ.error("No open image.");
			return;
		}

		ArrayList<String> init = ExchangeDocuments.cleanExchangeDirectory();
		Log.write(init);
		
		ExchangeDocuments.sendImagePlus(imp, "temp_input.tif");
	
        try {
        	String currentPath = new java.io.File(".").getCanonicalPath();
        	IJ.log("Current dir:" + currentPath);

        	String currentDir = System.getProperty("user.dir");
        	IJ.log("Current dir using System:" + currentDir);
            InputStream is = this.getClass().getResourceAsStream(python);
            if (is == null) {
            	System.out.println(python + " not found");
            	return;
            }

            Files.copy(is, Paths.get(ExchangeDocuments.path(python)), StandardCopyOption.REPLACE_EXISTING);
            	
            ArrayList<String> args = new ArrayList<String>();
            args.add(python);
            args.add(ExchangeDocuments.path("temp_input.tif"));
            args.add(ExchangeDocuments.path("temp_output.tif"));
            args.add(txtSigma.getText());
            args.add((String)cmbMode.getSelectedItem());
                
            ArrayList<String> log = venv.runCommand(args);
            Log.write(log);
            
            ImagePlus out = ExchangeDocuments.getImagePlus("temp_output.tif");
            out.setTitle("skimage-filters-gaussian " + imp.getTitle());
            out.show();
            is.close();
		}
        catch(IOException | InterruptedException ex) {
        	ex.printStackTrace();
        }

	}

}
