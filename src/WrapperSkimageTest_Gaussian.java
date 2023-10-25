import java.util.Random;

import ch.epfl.skimage.process.WrapperScikitImageGaussian;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.plugin.PlugIn;


public class WrapperSkimageTest_Gaussian implements PlugIn {

    public static void main(String... args ) throws Exception {
    	int nx = 200;
    	int ny = 300;
    	ImagePlus imp = IJ.createImage("input", nx, nx, 2, 32);
        Random random = new Random(1234);
        for ( int i = 0; i < 10000; i++ ) {
            int x = ( int ) ( random.nextFloat() * nx/2 );
            int y = ( int ) ( random.nextFloat() * ny );
            imp.getProcessor().putPixelValue(x, y, 255);
        }
        for ( int j = 0; j < ny; j++ ) {
        	imp.getProcessor().putPixelValue(nx/2, j, 255);
        }
        imp.show();
        new WrapperScikitImageGaussian();
    }

	@Override
	public void run(String arg) {
		new WrapperScikitImageGaussian();
	}
}