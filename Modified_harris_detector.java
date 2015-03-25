
import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class Modified_harris_detector implements PlugInFilter {
	ImagePlus im;
	static float alpha = HarrisCornerDetector.DEFAULT_ALPHA;
	static int threshold = HarrisCornerDetector.DEFAULT_THRESHOLD;
	static int nmax = 0; //points to show
	
	public int setup(String arg, ImagePlus im) {
		this.im = im;
		if (arg.equals("about")) {
			showAbout();
			return DONE;
		}
		return DOES_8G + NO_CHANGES;
	}
	
	public void run(ImageProcessor ip) {
		if (!showDialog()) return;//dialog canceled or error
		HarrisCornerDetector hcd = new HarrisCornerDetector(ip,alpha);
		hcd.findCorners();
		ImageProcessor result = hcd.showCornerPoints(ip);
		ImagePlus win = new ImagePlus("Corners from " + im.getTitle(),result);
		win.show();
	}
	
	void showAbout() {
		String cn = getClass().getName();
		IJ.showMessage("About "+cn+" ...","Harris Corner Detector");
	}
	
	private boolean showDialog() {
	// display dialog, and return false if canceled or in error.
		GenericDialog dlg = new GenericDialog("Harris CornerDetector", IJ.getInstance());
		float def_alpha = HarrisCornerDetector.DEFAULT_ALPHA;
		dlg.addNumericField("Alpha (default: "+def_alpha+")",alpha, 3);
		//int def_threshold = HarrisCornerDetector.DEFAULT_THRESHOLD;
		//dlg.addNumericField("Threshold (default: "+def_threshold+")", threshold, 0);
		dlg.addNumericField("Max. points (0 = show all)", nmax,0);
		dlg.showDialog();
		if(dlg.wasCanceled())
			return false;
		if(dlg.invalidNumber()) {
			IJ.showMessage("Error", "Invalid input number");
			return false;
		}
		alpha = (float) dlg.getNextNumber();
		//threshold = (int) dlg.getNextNumber();
		nmax = (int) dlg.getNextNumber();
		return true;
	}
}// end of classHarris_Corner_Plugin