import java.awt.Image;

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

public class two_dimensionalDFT implements PlugInFilter {

	public int setup(String arg, ImagePlus imp) {
	
		return DOES_8G;
	}

	public void run(ImageProcessor ip) {
		int h = ip.getHeight();
		int w = ip.getWidth();
		Complex[] guall = new Complex[w];
		Complex[] gvall = new Complex[h];
		
		Complex[] gudft = new Complex[w];
		Complex[][] gudft2 = new Complex[w][h];
		Complex[] gvdft = new Complex[h];
		Complex[][] gvdft2 = new Complex[w][h];
		
		for(int v=0; v<h; v++){
			for(int u=0; u<w; u++){
				Complex gu = new Complex(ip.getPixel(u, v), 0);
				guall[u]=gu;
			}
		
			gudft = DFT(guall, true);
			for(int u=0; u<w; u++){
				gudft2[u][v] = gudft[u];
			}			
		}
				
		for(int u=0; u<w; u++){
			for(int v=0; v<h; v++){
				gvall[v]=gudft2[u][v];
			}
			gvdft = DFT(gvall, true);
			
			//int pixels= (int)(Math.sqrt((gvdft[u].im*gvdft[u].im+gvdft[u].re*gvdft[u].re)));
			
			for(int v=0; v<h; v++){
			//	ip.putPixel(u, v, pixels);
				gvdft2[u][v]=gvdft[v];
			}			
		}
		
		ImageProcessor ipdft = new FloatProcessor(w,h);
		for(int v=0;v<h;v++){
			for(int u=0;u<w;u++){
				float pixel = (float)Math.sqrt((gvdft2[u][v].im*gvdft2[u][v].im+gvdft2[u][v].re*gvdft2[u][v].re));
				float pixels = (float)Math.log((1+pixel));
				ipdft.putPixel(u, v, Float.floatToIntBits(pixels));
			}
		}		
		ImagePlus imdft = new ImagePlus("DFT", ipdft);
		imdft.show();
		
		/*************************
		 * inverse
		 *************************/
		Complex[] guinall = new Complex[w];
		Complex[] guindft = new Complex[w];
		Complex[][] guindft2 = new Complex[w][h];
		
		Complex[] gvinall = new Complex[h];
		Complex[] gvindft = new Complex[h];
		Complex[][] gvindft2 = new Complex[w][h];
		
		for(int v=0; v<h; v++){
			for(int u=0; u<w; u++){
				guinall[u]=gvdft2[u][v];
			}
		
			guindft = DFT(guinall, false);
			for(int u=0; u<w; u++){
				guindft2[u][v] = guindft[u];
			}			
		}
		
		for(int u=0; u<w; u++){
			for(int v=0; v<h; v++){
				gvinall[v]=guindft2[u][v];
			}
			gvindft = DFT(gvinall, false);
			
			//int pixels= (int)(Math.sqrt((gvdft[u].im*gvdft[u].im+gvdft[u].re*gvdft[u].re)));
			
			for(int v=0; v<h; v++){
			//	ip.putPixel(u, v, pixels);
				gvindft2[u][v]=gvindft[v];
			}			
		}
		
		ImageProcessor ipindft = new ByteProcessor(w,h);
		for(int v=0;v<h;v++){
			for(int u=0;u<w;u++){
				int pixel = (int)(gvindft2[u][v].re);
				ipindft.putPixel(u, v, pixel);
			}
		}		
		ImagePlus imindft = new ImagePlus("inDFT", ipindft);
		imindft.show();			
	}

	/***************************
	 * method DFT begin
	 * @param g
	 * @param forward
	 * @return
	 ***************************/
	Complex[] DFT(Complex[] g, boolean forward) {
		int M = g.length;
		double s = 1 / Math.sqrt(M);//common scale factor
		Complex[] G = new Complex[M];
		
		for (int m = 0; m < M; m++) {
			double sumRe = 0;
			double sumIm = 0;
			double phim = 2 * Math.PI * m / M;
				for (int u = 0; u < M; u++) {
					double gRe = g[u].re;
					double gIm = g[u].im;
					double cosw = Math.cos(phim * u);
					double sinw = Math.sin(phim * u);
					
					if (!forward) // inverse transform
						sinw = -sinw;
					//complex mult:[gRe+igIm]¡¤[cos(¦Ø)+isin(¦Ø)]
					sumRe += gRe * cosw + gIm * sinw;
					sumIm += gIm * cosw - gRe * sinw;
				}
				G[m] = new Complex(s * sumRe, s * sumIm);
		 }
		return G;
	}
	
	
}
