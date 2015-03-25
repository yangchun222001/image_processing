package hm3;

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Circular_Hough implements PlugInFilter{
	public int setup(String arg, ImagePlus im){
		return DOES_8G+NO_CHANGES;
	}

	public void run(ImageProcessor ip){
		CircleHT cht = new CircleHT(ip);
		int rmax = cht.rmax;
		int[][][] acc = cht.houghArray;
		int w = ip.getWidth();
		int h = ip.getHeight();
		int threshold = 80;
		
		//clean image first if not the line will be appear
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
						int x = 0;
						int y = r;
						int d =3-2*r;
					
						while(x<y){
							if(u+x<w){
								if(v+y<h) ip.putPixel(u+x,v+y,255);   //confirm it is in the valid area
								if(v-y>0) ip.putPixel(u+x, v-y, 255);
							}
							
							if(u-x>0){
								if(v+y<h) ip.putPixel(u-x, v+y, 255);
								if(v-y>0) ip.putPixel(u-x, v-y, 255);
							}
							
							if(u+y<w){
								if(v+x<h) ip.putPixel(u+y, v+x, 255);
								if(v-x>0) ip.putPixel(u+y, v-x, 255);
							}
							
							if(u-y>0){
								if(v+x<h) ip.putPixel(u-y, v+x, 255);
								if(v-x>0) ip.putPixel(u-y, v-x, 255);
							}
							
							if(d<0) d=d+4*x+6;
							else{
								d = d+4*(x-y)+10;
								y--;
							}
							x++;
						}
					}	
										
				}	
					
			}
		}		
		
	}
	
}	


class CircleHT {
	ImageProcessor ip; 
	int step;
	int[][][] houghArray; 
	int rmax;
	//constructor method:
	CircleHT(ImageProcessor ip) {
		int u = ip.getWidth();
		int v = ip.getHeight();
		this.step = 1;
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
		
		for(int r=1; r<rmax; r++){
			int x = 0;
			int y = r;
			int d = 3-2*r;		
			while(y>x){
				
				if(u+x<w){
					if(v+y<h) houghArray[x+u][v+y][r]++;   //confirm it is in the valid area
					if(v-y>0) houghArray[x+u][v-y][r]++;
				}
				
				if(u-x>0){
					if(v+y<h) houghArray[u-x][v+y][r]++;
					if(v-y>0) houghArray[u-x][v-y][r]++;
				}
				
				if(u+y<w){
					if(v+x<h) houghArray[u+y][v+x][r]++;
					if(v-x>0) houghArray[u+y][v-x][r]++;
				}
				
				if(u-y>0){
					if(v+x<h) houghArray[u-y][v+x][r]++;
					if(v-x>0) houghArray[u-y][v-x][r]++;
				}
				
				if(d<0) d=d+4*x+6;
				else{
					d = d+4*(x-y)+10;
					y--;
				}
				x++;
			}
		}
	}
}	
	/*	
	int[][] draw_circle(int u,int v,int r, int x, int y){
		
		int[][] circle = new int[8][2];
		
			circle[0][0] = x+u;
			circle[0][1] = y+v;
			circle[1][0] = u-x;
			circle[1][1] = v+y;
			circle[2][0] = u+y;
			circle[2][1] = v-y;
			circle[3][0] = u-x;
			circle[3][1] = v-y;
			circle[4][0] = u+y;
			circle[4][1] = v+x;
			circle[5][0] = u-y;
		    circle[5][1] = v+x;
		    circle[6][0] = u+y;
		    circle[6][1] = v-x;
		    circle[7][0] = u-y;
		    circle[7][1] = v-x;
		    		    
		return circle;
		*/

	
	





