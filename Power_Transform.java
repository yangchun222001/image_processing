//engineer: chun yang
//time:     02/05/2014
//discription:performs an power law transformation on an image. 
//            This function should an 8©\bit grayscale image and the gamma
//            value to transform the image.

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

public class Power_Transform implements PlugInFilter{
	public int setup(String arg, ImagePlus imp) {
		return DOES_8G;    //this plugin accepts 8-bit grayscale image
	}

	public void run(ImageProcessor ip) {
		int w=ip.getWidth();
		int h=ip.getHeight();
		int amax=255;
		double gamma=0.1;   //set the gamma value 
		
		//iterate over all image coordinates
		for (int u=0;u<w;u++){
			for(int v=0;v<h;v++){
				int p=ip.getPixel(u,v);
				//calculate the scale of intensity to calculate gamma function
				double scale_p=(double) p/amax;
				double gamma_scale_p=Math.pow(scale_p, gamma); //the gamma function
				int gamma_p=(int) Math.round(gamma_scale_p*amax);  //come back to the original scale
				ip.putPixel(u, v, gamma_p);
			}
		}		
	}
}


