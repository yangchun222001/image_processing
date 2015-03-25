import ij.process.ImageProcessor;

public class Moments {
//static final int BACKGROUND = 0;
	
	double moment(ImageProcessor ip,int p,int q, int label) {
		double Mpq = 0.0;
		for (int v = 0; v < ip.getHeight(); v++) {
			for (int u = 0; u < ip.getWidth(); u++) {
				if (ip.getPixel(u,v) == label) {
					Mpq += Math.pow(u, p) * Math.pow(v, q);
				}
			}
		}
		return Mpq;
	}
	
	
	double centralMoment(ImageProcessor ip,int p,int q, int label)
	{
		double m00 = moment(ip, 0, 0, label); // region area
		double xCtr = moment(ip, 1, 0, label) / m00;
		double yCtr = moment(ip, 0, 1, label) / m00;
		double cMpq = 0.0;
		for (int v = 0; v < ip.getHeight(); v++) {
			for (int u = 0; u < ip.getWidth(); u++) {
				if (ip.getPixel(u,v) == label) {
					cMpq +=
							Math.pow(u - xCtr, p) *
							Math.pow(v - yCtr, q);
				}
			}
		}
		return cMpq;
	}
	

}
