import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Laplacian_Edge_Detection implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {
		return DOES_32;
	}

	public void run(ImageProcessor ip) {
		Gradient_Magnitude image1 = new Gradient_Magnitude();
		Laplacian_ image2 = new Laplacian_();
		Zero_Crossings image3 = new Zero_Crossings();
		
		
		ImageProcessor copy1 = ip.duplicate();
		ImageProcessor copy2 = ip.duplicate();

		image1.run(copy1);
		image2.run(copy2);
		image3.run(copy2);
		
		int w = ip.getWidth();
		int h = ip.getHeight();
		
		for(int u=0; u<w; u++){
			for(int v=0; v<h; v++){
				float p1 = copy1.getPixelValue(u, v);
				float p2 = copy2.getPixelValue(u, v);
				if(p1<200) p1=0;    // 0 is the background
				else p1 = 1000f;    //1000 is the line
				float p = p1*p2;
				
				ip.putPixel(u, v, Float.floatToIntBits(p));
			}
		}
		
	}

}
