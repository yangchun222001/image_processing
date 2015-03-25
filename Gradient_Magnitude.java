import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

public class Gradient_Magnitude implements PlugInFilter{

	public int setup(String arg, ImagePlus imp) {
		return DOES_32;
	}

	
	public void run(ImageProcessor ip) {
		int w = ip.getWidth();	
		int h = ip.getHeight();
		float d = 0;
		
		double sigma=2;
		float[][] gausskernel_1x =  makeGaussKernel2d(2,sigma);
		float[][] gausskernel_1y =  makeGaussKernel2d(3,sigma);
		int r = gausskernel_1x.length;
		
		ImageProcessor copy1 = ip.duplicate();
		for (int v=0; v<h; v++){
			for(int u=0;u<w; u++){
				float sum_1x =0;
				float sum_1y =0;
				float p = 0;
				for (int i=0; i<r;i++){
					for (int j=0; j<r; j++){
						d=get_pixel_wrap(u-i+(r-1)/2, v-j+(r-1)/2, copy1);
						float c_1x=gausskernel_1x[i][j];
						float c_1y=gausskernel_1y[i][j];
						sum_1x = sum_1x+(d*c_1x);
						sum_1y = sum_1y+(d*c_1y);
						p = (float) Math.sqrt(sum_1y*sum_1y+sum_1x*sum_1x);
						
						ip.putPixel(u, v,  Float.floatToIntBits(p));
					}
				}
			}
		}
		
	}
	
	public float get_pixel_wrap(int u,int v,ImageProcessor ip){
		int w = ip.getWidth();
		int h = ip.getHeight();
		float pixel = 0;
		if(u<w && v<h && u>=0 && v>=0){
			pixel=Float.intBitsToFloat(ip.getPixel(u, v));
		} else if(u>w-1 && v>=0 && v<h){
			pixel=Float.intBitsToFloat(ip.getPixel((u-(w-1)), v));
		} else if(u<0 && v>=0 && v<h){
			pixel=Float.intBitsToFloat(ip.getPixel((u+w), v));
		} else if (u>=0 && u<w && v>-1){
			pixel=Float.intBitsToFloat(ip.getPixel(u, (v-(h-1))));
		} else if (u>=0 && u<w && v<0){
			pixel=Float.intBitsToFloat(ip.getPixel(u, (v+h)));
		} else if (u<0 && v<0){
			pixel=Float.intBitsToFloat(ip.getPixel((u+w), (v+h)));
		} else if (u>w-1 && v<0){
			pixel=Float.intBitsToFloat(ip.getPixel((u-(w-1)), v+h));
		} else if (u<0 && v>h-1){
			pixel=Float.intBitsToFloat(ip.getPixel(u+w, (v-(h-1))));
		} else if (u>w-1 && v>h-1){
			pixel=Float.intBitsToFloat(ip.getPixel(u-(w-1), (v-(h-1))));
			//System.out.println("erro2");
		}
		else System.out.println("erro");
		return pixel;
		
	}

	public float[][] makeGaussKernel2d(int type, double sigma) {
		// create the kernel
			int center = (int) (3.0*sigma);
			float[][] kernel = new float[2*center+1][2*center+1]; // odd size
			// fill the kernel
			double sigma2 = sigma * sigma; // ¦Ò2
			for (int i=0; i<2*center+1; i++) {
				for(int j=0; j<2*center+1;j++){
					double x = center - i;
					double y = center - j;
					float pie = 3.14f;
					float guass = (float) Math.exp(-0.5*(x*x+y*y)/sigma2);
					if(type == 1) kernel[i][j] = (float)( (1/(2*pie*sigma2))*guass); //2d Gauss
					else if(type == 2) kernel[i][j] = (float)((-x/(sigma2)* guass)); //2d 1st x
					else if(type == 3) kernel[i][j] = (float)((-y/(sigma2)* guass)); //2d 1st y
					else if(type == 4) kernel[i][j] = (float)((-1/(sigma2))*(1-x*x/sigma2)* guass); //2d 2st x
					else kernel[i][j] = (float)((-1/(sigma2))*(1-y*y/sigma2)* guass); //2d 2st y
				}
			}
			return kernel;
		}
}
