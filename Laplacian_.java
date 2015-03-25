import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Laplacian_ implements PlugInFilter {

	
	
	
	
	public int setup(String arg, ImagePlus imp) {
		
		return DOES_32;
	}

	public void run(ImageProcessor ip) {
		int w = ip.getWidth();	
		int h = ip.getHeight();
		
		float d = 0;
		double sigma=2;
		
		Gradient_Magnitude kernel = new Gradient_Magnitude();
		float[][] gausskernel2x =  kernel.makeGaussKernel2d(4,sigma);
		float[][] gausskernel2y =  kernel.makeGaussKernel2d(5,sigma);
		
		int r = gausskernel2x.length;
		float[][] LoGkernel = new float[r][r];
		
		for(int i=0; i<r; i++){
			for(int j=0; j<r; j++){
				LoGkernel[i][j] = gausskernel2x[i][j]+gausskernel2y[i][j];
			}
		}
		
		ImageProcessor copy1 = ip.duplicate();
		for (int v=0; v<h; v++){
			for(int u=0;u<w; u++){
				float sum =0;

				for (int i=0; i<r;i++){
					for (int j=0; j<r; j++){
						d=kernel.get_pixel_wrap(u-i+(r-1)/2, v-j+(r-1)/2, copy1);
						
						sum = sum+(d*LoGkernel[i][j]);
						
					}
				}
				ip.putPixel(u, v,  Float.floatToIntBits(sum));
			}
		}
		
	}

}
