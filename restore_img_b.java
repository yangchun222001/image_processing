//engineer: chun yang
//time:     02/20/2014


import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class restore_img_b implements PlugInFilter {
	double c=-10;

	public int setup (String arg, ImagePlus im) {
		return DOES_RGB;
	}

	public void run (ImageProcessor I) {
	
		int w = I.getWidth();	
		int h = I.getHeight();	
		
		double [][] H = {
				{0.0f, c/4f, 0.0f},
				{c/4f, 1-c, c/4f},
				{0.0f, c/4f, 0.0f}
		};
	
		ImageProcessor copy = I.duplicate();
	
		for (int v = 1; v <= h-2; v++){
			for (int u =1; u<=w-2; u++) {
				double r=0;
				double g=0;
				double b=0;
				
				for (int j=-1; j<=1; j++){
					for (int i = -1; i<=1; i++){
					int rr = (copy.getPixel(u+i, v+j) & 0xff0000) >> 16;					
					int gg = (copy.getPixel(u+i, v+j) & 0x00ff00) >> 8;
			        int bb = (copy.getPixel(u+i, v+j) & 0x0000ff) ;
					double c = H[j+1][i+1];
					r=r+c*rr;
					g=g+c*gg;
					b=b+c*bb;
					}
				}
										
				if (r>255)       r=255;				 
				 else if (r<0)	 r=0;
				if (g>255)		 g=255;
				 else if (g<0)	 g=0;
				if (b>255)   	 b=255;
				 else if (b<0)	 b=0;
				
				 // round the value of converted pixels part of R, G, B
				 int rrr =(int)Math.round(r);
				 int ggg =(int)Math.round(g);
				 int bbb =(int)Math.round(b);
				 				 
				 I.putPixel(u, v, ((rrr & 0xff)<<16) | ((ggg & 0xff)<<8) | ((bbb & 0xff)) );														
			}
		}
	}
}

