
//engineer: chun yang
//time:     02/05/2014
//discription£ºmodifies the values of an 8©\bit gray scale input image 
//            according to the function s = 16 sqrt(r), where r is
//            the input intensity and s is the processed intensity.
//            16 guarantees that the result will be in the range 0 to 255.

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

public class Gray_level_modification implements PlugInFilter{

	public int setup(String arg, ImagePlus imp) {
		return DOES_8G; 
		//this plugin accepts 8-bit grayscale image
	}

	public void run(ImageProcessor ip) {
		//find the width and height information of picture
		int w=ip.getWidth();
		int h=ip.getHeight();
		
		//iterate over all image coordinates
		for (int u=0;u<w;u++){
			for(int v=0;v<h;v++){
				int p = ip.getPixel(u, v);
				//put the transformed number into the image
				ip.putPixel(u, v, 16*(int) Math.sqrt(p));
			}
		}
		
	}
	

}
