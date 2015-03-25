//engineer: chun yang
//time:     02/05/2014
//discription: Implement the auto-contrast operation
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

public class AutoContrast_quantiles implements PlugInFilter {


	public int setup(String arg, ImagePlus imp) {
		//this plugin accepts 8-bit grayscale image
		return DOES_8G;
	}


	public void run(ImageProcessor ip) {
		int w=ip.getWidth();
		int h=ip.getHeight();
		int s=(int) (0.01*w*h);   //calculate the margin s
		int amax=255;
		int amin=0;
		int alow=0;
		int ahigh=0;
		int[] H=new int[256];
		int total1=0;
		int total2=0;
		int q=0;
		
		for (int u=0;u<w;u++){
			for(int v=0;v<h;v++){
				int p=ip.getPixel(u,v);
				H[p]=H[p]+1;  //the original histogram
			}
		}
		
		for (int i=0;i<=amax;i++){
			total1=total1+H[i];
			if (total1 >= s){    //if added number of pixels equal or bigger than s
				alow=i;          //find out the alow
				break;
			}
		}
		
		for (int j=amax;j>=0;j--){
			total2=total2+H[j];
			if (total2 >= s){  //if added number of pixels equal or bigger than s
				ahigh=j;       //find out ahigh
				break;
			}
		}
		
		for (int u=0;u<w;u++){
			for(int v=0;v<h;v++){
				int p=ip.getPixel(u,v);
				if (p<=alow){      //if intensity smaller than alow, set it to amin(0);
					q=amin;
				} else if (alow<p && p<ahigh){   //if intensity between alow and ahigh, set it to new intensity based
					q=(int) ((p-alow)*(amax-amin)/(ahigh-alow)+0.5);     //on the linear function
				} else if (p>ahigh){    //if the intensity bigger than ahigh, set it to amax(255)
					q=amax;
				}
				ip.putPixel(u, v, q);
			}
		}
		//the output information
		System.out.println("alow "+alow);
		System.out.println("ahigh "+ahigh);
		System.out.println("s "+s);
		System.out.println("h "+h);
		System.out.println("w "+w);
	}
	

}
