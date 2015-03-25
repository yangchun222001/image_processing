//engineer: chun yang
//time:     02/20/2014


import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

public class Laplacian_Filter_with_c implements PlugInFilter {
	
	public int setup(String arg, ImagePlus imp) {
		//this plugin accepts 8-bit grayscale image
		return DOES_8G;		
	}

	
	public void run(ImageProcessor ip) {
		int w=ip.getWidth();
		int h=ip.getHeight();
		double c=-7;
		
		double[][] filter={
				{0,   c/4, 0  },
				{c/4, 1-c, c/4},
				{0,   c/4, 0  }
		};
		
		ImageProcessor copy = ip.duplicate();
		for(int v=1; v<h-1; v++){
			for(int u=1; u<= w-2; u++){
				double sum = 0;
				for(int j=-1;j<=1;j++){
					for(int i=-1;i<=1;i++){
						int p=copy.getPixel(u+i, v+j);
						double a =filter[j+1][i+1];
						sum = sum+a*p;
					    //System.out.println(sum);
					}
				}
				int q =(int)Math.round(sum);
				ip.putPixel(u, v, q);
			}
		}
		
	}
}
