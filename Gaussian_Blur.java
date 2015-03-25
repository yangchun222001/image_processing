//engineer: chun yang
//time:     02/20/2014

import ij.plugin.filter.PlugInFilter;
import ij.process.*;
import ij.ImagePlus;


public class Gaussian_Blur implements PlugInFilter {
	 
	public int setup (String arg, ImagePlus im) {
		return DOES_32;// will let the ImageJ to process an 32-bit gray scale image
 	}

	public void run (ImageProcessor iP) {
		int w = iP.getWidth();	
		int h = iP.getHeight();
		float d=0;
		
		double sigma=20;
		float[] gausskernel =  makeGaussKernel1d(sigma);
		int r =gausskernel.length;
		
		ImageProcessor copy1 = iP.duplicate();

		for (int v=0; v<h; v++){
			for(int u=0;u<w; u++){
				float sum =0;
				for (int i=0; i<r;i++){
					if(u+i-r/2>w-1){						
						d=Float.intBitsToFloat(copy1.getPixel(w-1, v));						
					}
					else if(u+i-r/2<0){						
						d=Float.intBitsToFloat(copy1.getPixel(0, v));						
					}
					else{
						d=Float.intBitsToFloat(copy1.getPixel(u+i-r/2, v));
					}
					float c=gausskernel[i];
					sum = sum+(d*c);					
				}
				iP.putPixel(u, v,  Float.floatToIntBits(sum));
			}
		}
		
		ImageProcessor copy2 = iP.duplicate();
		
		for (int v=0; v<h; v++){
			for(int u=0;u<w; u++){
				float sum=0;
				for (int i=0; i<r;i++){
					if(v+i-r/2>h-1 ){
						d=Float.intBitsToFloat(copy2.getPixel(u, h-1));
					}
					else if(v+i-r/2<0){						
						d=Float.intBitsToFloat(copy2.getPixel(u, 0));
					}
					else{
						d=Float.intBitsToFloat(copy2.getPixel(u, v+i-r/2));
					}
					
					float c2=gausskernel[i];
					sum=sum+(d*c2);
				}
				iP.putPixel(u, v,  Float.floatToIntBits(sum));
			}
		}
		
	}
	
	public float[] makeGaussKernel1d(double sigma) {
		// create the kernel
			int center = (int) (3.0*sigma);
			float[] kernel = new float[2*center+1]; 
			float sum = 0;
			// fill the kernel
			double sigma2 = sigma * sigma; 
			for (int i=0; i<kernel.length; i++) {
				double r = center - i;
				kernel[i] = (float) Math.exp(-0.5 * (r*r) / sigma2);
				sum = sum + kernel[i];
			}
			for (int i=0; i<kernel.length; i++) {
				kernel[i] = kernel[i]/sum;
			}
			return kernel;
		}
}