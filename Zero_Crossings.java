import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Zero_Crossings implements PlugInFilter {

	
	
	public int setup(String arg, ImagePlus imp) {		
		return DOES_32;
	}

	public void run(ImageProcessor ip) {
		int w = ip.getWidth();	
		int h = ip.getHeight();
		float p = 0;
		
		ImageProcessor copy = ip.duplicate();
		Gradient_Magnitude kernel = new Gradient_Magnitude();
		
		for (int v=0; v<h; v++){
			for(int u=0;u<w; u++){
				float dx1 = kernel.get_pixel_wrap(u-1,v,copy);
				float dx2 = kernel.get_pixel_wrap(u+1,v,copy);
				float dy1 = kernel.get_pixel_wrap(u,v-1,copy);
				float dy2 = kernel.get_pixel_wrap(u,v+1,copy);
				if(dx1*dx2<0 || dy1*dy2<0) p = 1000f;
				else p = 0;
				ip.putPixel(u, v,  Float.floatToIntBits(p));
			}
		}
		
		/*
		float[][] sum = new float[w][h];
		float d = 0;
		
		double sigma=5;
	
		Gradient_Magnitude kernel = new Gradient_Magnitude();
		float[][] gausskernel2x =  kernel.makeGaussKernel2d(4,sigma);
		float[][] gausskernel2y =  kernel.makeGaussKernel2d(5,sigma);
		
		int r = gausskernel2x.length;
		float[][] LoGkernel = new float[r][r];
		
		for(int i=0; i<r; i++){
			for(int j=0; j<r; j++){
				LoGkernel[i][j] = gausskernel2x[i][j]+gausskernel2y[i][j];
			}
		}
		
		ImageProcessor copy1 = ip.duplicate();
		float[][] zeropixel=new float[w][h];
		for (int v=0; v<h; v++){
			for(int u=0;u<w; u++){
				float sum2=0;
				for (int i=0; i<r;i++){
					for (int j=0; j<r; j++){
						d=kernel.get_pixel_wrap(u-i+(r-1)/2, v-j+(r-1)/2, copy1);
						sum2 = sum2+(d*LoGkernel[i][j]);												
					}
				}
				sum[u][v]=sum2;
				zeropixel[u][v]=sum[u][v];
			}
		}
		

		for (int v=1; v<h-1; v++){
			for(int u=1;u<w-1; u++){
				if(sum[u-1][v]*sum[u+1][v]<0 || sum[u][v-1]*sum[u][v+1]<0 ) zeropixel[u][v]=1000000000;
				else zeropixel[u][v] = 0;
				ip.putPixel(u, v,  Float.floatToIntBits(zeropixel[u][v]));				
			}
		}
		*/
	}
	
}

