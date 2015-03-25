//engineer: chun yang
//time:     02/20/2014


import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.*;

public class histogram_spec_averages implements PlugInFilter {
	
	public int setup(String arg, ImagePlus imp) {
		//this plugin accepts 8-bit grayscale image
		return DOES_8G;		
	}
	
	public void run(ImageProcessor ip) {
		
		ImagePlus t1 = new ImagePlus("C:\\Users\\chunyang\\Desktop\\545\\hw_images\\boat.jpg");
		ImagePlus t2 = new ImagePlus("C:\\Users\\chunyang\\Desktop\\545\\hw_images\\runway1.jpg");
		ImagePlus t3 = new ImagePlus("C:\\Users\\chunyang\\Desktop\\545\\hw_images\\spine.jpg");
		//ImagePlus t4 = new ImagePlus("C:\\Users\\chunyang\\Desktop\\545\\hw_images\\spine.jpg");
		
		ImageProcessor ip1 = t1.getProcessor();
		ImageProcessor ip2 = t2.getProcessor();
		ImageProcessor ip3 = t3.getProcessor();
		//ImageProcessor ip4 = t4.getProcessor();
		
		int[] h1=ip1.getHistogram();
		int[] h2=ip2.getHistogram();
		int[] h3=ip3.getHistogram();
		//int[] h4=ip4.getHistogram();
		
		int[] hR=h1;
		for(int i = 1; i<256; i++){
			hR[i]=(int) ((h1[i]+h2[i]+h3[i])/3);
		}
		
		int[] hA=ip.getHistogram();
		
		int[] F = matchHistograms(hA,hR);//matchHistograms(hA,hR);
		ip.applyTable(F);		
	}
	
	
	int[] matchHistograms (int[] hA, int[] hR) {
		 //hA...histogramhAof target imageIA
		 //hR...reference histogramhR
		 // returns the mapping functionfhs()to be applied to image IA
		
		 int K = hA.length; //hA, hRmust be of lengthK
		 double[] PA = Cdf(hA); // get CDF of histogramhA
		 double[] PR = Cdf(hR); // get CDF of histogramhR
		 int[] F = new int[K]; // pixel mapping functionfhs()
		
		 // compute mapping functionfhs()
		 for (int a = 0; a < K; a++) {
			 int j = K-1;
			 do {
				 F[a] = j;
				 j--;
			 } while (j>=0 && PA[a]<=PR[j]);
		 }
		 	return F;
	}
	
	
	double[] Cdf (int[] h) {
		 // returns the cumulative distribution function for histogramh
		 int K = h.length;
		 int n = 0; // sum all histogram values
		 for (int i=0; i<K; i++) {
			 n += h[i];
		 }
		
		 double[] P = new double[K]; // create cdf tableP
		 int c = h[0]; // cumulate histogram values
		 P[0] = (double) c / n;
		 
		 
		 for (int i=1; i<K; i++) {
			 c += h[i];
			 P[i] = (double) c / n;
		 }
		 return P;
	}
	
}
