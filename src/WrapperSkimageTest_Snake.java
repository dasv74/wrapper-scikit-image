import java.util.Random;

import ch.epfl.skimage.process.WrapperScikitImageSnake;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.OvalRoi;
import ij.plugin.PlugIn;

public class WrapperSkimageTest_Snake implements PlugIn {

    public static void main(String... args ) throws Exception {
    	int nx = 200;
    	int ny = 300;
    	ImagePlus imp = IJ.createImage("input", nx, ny, 1, 32);
        Random random = new Random(1234);
        for ( int j = 40; j < 100; j++) 
        for ( int i = 50; i < 100; i++) {
        	imp.getProcessor().putPixelValue(i, j, random.nextDouble()*255);
        }
        imp.show();
        imp.setRoi(new OvalRoi(35, 35, 30, 50));
        new WrapperScikitImageSnake();
    }

	@Override
	public void run(String arg) {
		new WrapperScikitImageSnake();
	}
}