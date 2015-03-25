//engineer: chun yang
//time:     02/20/2014

import ij.*;
import ij.plugin.filter.PlugInFilter;
import java.util.Arrays;
import ij.process.ImageProcessor;

public class restore_img_a implements PlugInFilter {
	final int K=4;

public int setup (String arg, ImagePlus im) {
return DOES_RGB;
 }

public void run (ImageProcessor orig) {
	int w = orig.getWidth();	
	int h = orig.getHeight();
	
	ImageProcessor copy = orig.duplicate();
	
	int [] Pr = new int [2*K+1];
	int [] Pg = new int [2*K+1];
	int [] Pb = new int [2*K+1];
	
	
	for (int v = 1; v <= h-2; v++){
		for (int u =1; u<=w-2; u++) {
			int k=0;
			for (int j=-1; j<=1; j++){
				for (int i = -1; i<=1; i++){
				Pr[k] = (copy.getPixel(u+i, v+j) & 0xff0000) >> 16;
				Pg[k] = (copy.getPixel(u+i, v+j) & 0x00ff00) >> 8;
		        Pb[k] = (copy.getPixel(u+i, v+j) & 0x0000ff) ;
				k++;
				}
			}
			Arrays.sort(Pr);
			Arrays.sort(Pg);
			Arrays.sort(Pb);
			
			orig.putPixel(u, v, ((Pr[K] & 0xff)<<16) | ((Pg[K] & 0xff)<<8) | Pb[K] & 0xff);
		}
	}
	
}
}
