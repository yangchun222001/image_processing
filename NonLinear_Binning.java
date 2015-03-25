//engineer: chun yang
//time:     02/05/2014
//discription:. Create a table of 10 arbitrary ranges and pick appropriate intervals of the 
//              histogram.

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

public class NonLinear_Binning implements PlugInFilter {


	public int setup(String arg, ImagePlus imp) {
		//this plugin accepts 8-bit grayscale image
		return DOES_8G;
	}

	
	public void run(ImageProcessor ip) {
		int w=ip.getWidth();
		int h=ip.getHeight();
		int K=256;
		int amax=255;
		int B=10;
		int[] H=new int[B];  //just 10 ranges
		double gamma=1.5;
		
		//iterate over all image coordinates
		for (int u=0;u<w;u++){
			for(int v=0;v<h;v++){
				int p=ip.getPixel(u,v);
				double scale_p=(double) p/amax;
				double gamma_scale_p=Math.pow(scale_p, gamma);  //use the nonlinear function: gamma
				int gamma_p=(int) Math.round(gamma_scale_p*amax); //apply the original intensity to gamma function
				int i= gamma_p*B/K;                               //then divide them into equal intervals
				ip.putPixel(u, v, i*10);     //i will be too small, multiply 10 will be better and it will not out of 255
				H[i]=H[i]+1;                 //output the transformed image's histogram
			}
		}
		
		for(int j=0;j<B;j++){
			System.out.println(H[j]);
		}
		
	}
}
