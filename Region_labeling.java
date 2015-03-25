import java.util.Stack;

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Region_labeling implements PlugInFilter {

	@Override
	public int setup(String arg, ImagePlus imp) {
		
		return DOES_8G;
	}

	 
	public void run(ImageProcessor ip) {
		int width = ip.getWidth();
		int height = ip.getHeight();
		int label = 20;
		
		for(int u=0; u<width; u++){
			for(int v=0; v<height; v++){
				if(ip.getPixel(u,v) == 255){
					floodFill(ip, u, v, label);
					drawing(ip,label);
					label = label+10;
				}				
			}
		}
		
	}

	void floodFill(ImageProcessor ip, int x, int y, int label) {
		int width = ip.getWidth();
		int height = ip.getHeight();
		Stack<Node> s = new Stack<Node>();// stack
		s.push(new Node(x,y));
		while (!s.isEmpty()){
			Node n = s.pop();
			if ((n.x>=0) && (n.x<width) && (n.y>=0) && (n.y<height)
					&& ip.getPixel(n.x,n.y)==255) {
				ip.putPixel(n.x,n.y,label);
				s.push(new Node(n.x+1,n.y));
				s.push(new Node(n.x,n.y+1));
				s.push(new Node(n.x,n.y-1));
				s.push(new Node(n.x-1,n.y));
		 	}
		}
	}
	
	void drawing(ImageProcessor ip, int label){
	
		double m00 = 0;
		double xc = 0; 
		double yc = 0;
		
		
		Moments mom = new Moments();
		
		m00 = mom.moment(ip, 0, 0, label);
		
		xc=mom.moment(ip, 1, 0, label)/m00;
		yc=mom.moment(ip, 0, 1, label)/m00;
		
		double u11 = mom.centralMoment(ip, 1, 1, label);
		double u20 = mom.centralMoment(ip, 2, 0, label);
		double u02 = mom.centralMoment(ip, 0, 2, label);
		
		double a1=u20+u02+Math.sqrt(((u20-u02)*(u20-u02)+4*u11*u11));
		double a2=u20+u02-Math.sqrt(((u20-u02)*(u20-u02)+4*u11*u11));
		
		double ra=Math.sqrt((2*a1/m00));
		double rb=Math.sqrt((2*a2/m00));
		
		double theta = Math.atan((2*u11/(u20-u02)))/2;
		
		for(double t=0; t<6.28; t=t+0.05){
			int x = (int)(xc+Math.cos(theta)*ra*Math.cos(t)-Math.sin(theta)*rb*Math.sin(t));
			int y = (int)(yc+Math.sin(theta)*ra*Math.cos(t)+Math.cos(theta)*rb*Math.sin(t));
			ip.putPixel(x,y,199);
		}
		
		for(int t=0; t<50; t++){
			int x = (int)(xc + t*Math.cos(theta));
			int y = (int)(yc + t*Math.sin(theta));
			ip.putPixel(x, y, 201);
		}
				
		
	}

			
}
