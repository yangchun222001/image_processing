//engineer: chun yang
//time:     02/05/2014
//description: sets the threshold value to the median of the histogram

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

public class Median_Threshold implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {
		//this plugin accepts 8-bit grayscale image
		return DOES_8G;
	}

	
	public void run(ImageProcessor ip) {
		//find the width and height information of picture
		//int struck H to hold the histogram, the length is from 0 to 255 totally 256
		int w=ip.getWidth();
		int h=ip.getHeight();
		int threshold=0;
		int total=0;
		int[] H=new int[256];
		int[] trans_h=new int[2];
		
		//iterate over all image coordinates
		for (int u=0;u<w;u++){
			for(int v=0;v<h;v++){
				//build the histogram
				int p = ip.getPixel(u, v);
				H[p]=H[p]+1;
			}
		}
		
		//iterate over the histogram to find the median m
		for (int i=0;i<256;i++){
			total = total +H[i];
			System.out.println(H[i]);
			//add the pixels from 0 to 255, if the number is equal or bigger than the half 
			//break the for loop, the i is the median number
			if(total >= w*h/2){
				threshold = i;
				break;
			}
		}
		
		for (int u=0;u<w;u++){
			for(int v=0;v<h;v++){
				int p = ip.getPixel(u, v);
				//set the intensity to 0 if the original intensity less than the threshold
				if(p<threshold){
					ip.putPixel(u, v, 0);
					trans_h[0]=trans_h[0]+1; //build the transformed histogram
				} else if(p>=threshold){
				//set the intensity to 255 if the original intensity bigger than the threshold
					ip.putPixel(u, v, 255);
					trans_h[1]=trans_h[1]+1; //build the transformed histogram
				}
			}
		}
		
		//output some information of the image
		System.out.println("the size of the picture is "+w+" and "+h);
		System.out.println("the total number of pixels of the picture is "+w*h);
		System.out.println("the threshould is " + threshold);
		System.out.println("the histogram after transform is "+trans_h[0]);
		System.out.println("                                 "+trans_h[1]);
	}
	
}
