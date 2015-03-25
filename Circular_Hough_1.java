

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Circular_Hough_1 implements PlugInFilter{
	public int setup(String arg, ImagePlus im){
		return DOES_8G+NO_CHANGES;
	}

	public void run(ImageProcessor ip){
		
		CircleHT cht = new CircleHT(ip);
		int rmax = cht.rmax;
		int[][][] acc = cht.houghArray;		
		int w = ip.getWidth();
		int h = ip.getHeight();
		int threshold = 50;
				
		for(int u = 0 ; u<w; u++){
			for(int v = 0; v<h;v++){
				ip.putPixel(u,v,0);
			}
		}
		
		
		for(int u = 0 ; u<w; u++){
			for(int v = 0; v<h;v++){
				for(int r = 1; r<rmax;r++){
					int points = acc[u][v][r];
					if(points > threshold){
						for(int x = -r; x<=r; x++){
							int y = (int) Math.sqrt(r*r-x*x);
							if(u+x>=0 && u+x<w && v-y>=0 && v+y<h && v+y >=0 && v-y<h){
								ip.putPixel(u+x, v+y, 255);
								ip.putPixel(u+x, v-y, 255);
							}
						}
					}
				}	
			}
		}		
		
	}	
}

//////////////////////////////////////////////////////
class CircleHT {
	ImageProcessor ip; 
	int[][][] houghArray; 
	int rmax;
	//constructor method:
	CircleHT(ImageProcessor ip) {
		int u = ip.getWidth();
		int v = ip.getHeight();
		this.rmax = (int)Math.sqrt(u*u+v*v)/2;
		this.ip = ip;
		this.houghArray = new int[u][v][rmax];
		fillHoughAccumulator();
	 }

	 void fillHoughAccumulator() {
		 int w = ip.getWidth();
		 int h = ip.getHeight();
		 for (int v = 0; v < h; v++) {
			 for (int u = 0; u < w; u++) {
				 if (ip.get(u, v) > 50) {
					 doPixel(u, v);
				 }
			 }
		 }
	}

	void doPixel(int u, int v) {
		
		int w = ip.getWidth();
		int h = ip.getHeight();
		
		for(int r=10; r<rmax; r++){
			for(int x = -r; x<=r; x++){
				int y = (int) Math.sqrt(r*r-x*x);
				if(u+x>=0 && u+x<w && v-y>=0 && v+y<h && v+y >=0 && v-y<h){
					houghArray[u+x][v+y][r]++;
					houghArray[u+x][v-y][r]++;
				}
			}
		
		}
	}

 } 

