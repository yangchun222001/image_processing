
import java.util.ArrayList;
import java.util.List;

import regions.BinaryRegion;
import regions.RegionLabeling;
//import contours.Contour;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
 
public class Chain_codes implements PlugInFilter
{
	 ImagePlus origImage = null;
	 String origTitle = null;
	 static boolean verbose = true;
	 
	 
	 public int setup(String arg, ImagePlus im) {
		 origImage = im;
		 origTitle = im.getTitle();
		 RegionLabeling.setVerbose(verbose);
	
		 return DOES_8G + NO_CHANGES;
	 }
 
	@SuppressWarnings("null")
	public void run(ImageProcessor ip) 
	 {
		 ImageProcessor ip2 = ip.duplicate();
 
		 // label regions and trace contours
		 ContourTracer tracer = new ContourTracer(ip2);
 
		 // extract contours and regions
		 List<Contour> outerContours = tracer.getOuterContours();
		 List<Contour> innerContours = tracer.getInnerContours();
		 
		 List<BinaryRegion> regions = tracer.getRegions();
		 
		 System.out.println(outerContours.get(1).points.get(1));
		 
		 if (verbose) printRegions(regions);
 
		 // change lookup table to show gray regions
		 ip2.setMinAndMax(0,512);
		 // create an image with overlay to show the contours
		 ImagePlus im2 = new ImagePlus("Contours of " + origTitle,
				 ip2);
		 ContourOverlay cc = new ContourOverlay(im2, outerContours,
				 innerContours);
		 new ImageWindow(im2, cc);
		 
		/*********************************
		 * outerContour 
		 *********************************/
		 List<Integer> code = null;
		 List<List> suppercode = null; 
		 
		 int x,y,xx, yy;
		 int lengthContour = outerContours.size();
		 suppercode = new ArrayList<List>();
		 for(int i=0; i<lengthContour; i++){
			 code = new ArrayList<Integer>();
			 for(int j=0; j<outerContours.get(i).points.size(); j++){
				 x= outerContours.get(i).points.get(j).x;
				 y= outerContours.get(i).points.get(j).y;
				 if(j==outerContours.get(i).points.size()-1){
					 xx = outerContours.get(i).points.get(0).x;
					 yy = outerContours.get(i).points.get(0).y;
				 }
				 else{
					 xx = outerContours.get(i).points.get(j+1).x;
					 yy = outerContours.get(i).points.get(j+1).y;
				 }
				 
				 int xxx = xx-x;
				 int yyy = yy-y;
				 int chaincode = 0;
				 
				 if(xxx==1&&yyy==0)        chaincode = 0;			 
				 else if(xxx==1&&yyy==1)   chaincode = 1;
				 else if(xxx==0&&yyy==1)   chaincode = 2;
				 else if(xxx==-1&&yyy==1)  chaincode = 3;
				 else if(xxx==-1&&yyy==0)  chaincode = 4;
				 else if(xxx==-1&&yyy==-1) chaincode = 5;
				 else if(xxx==0&&yyy==-1)  chaincode = 6;
				 else chaincode = 7;
				 
				 code.add(chaincode);	
			 }
			 suppercode.add(code);
			 System.out.println(suppercode.get(i).size());
			 System.out.println("       "+suppercode.get(i).get(1)); 
			 
			 
		 }
	 
		 for(int u=0; u<ip.getWidth(); u++){
			 for(int v=0; v<ip.getHeight(); v++){
				ip.putPixel(u, v, 0); 
			 }
		 }
		 
		 for(int i=0; i<suppercode.size(); i++){
			 int point_x = outerContours.get(i).points.get(0).x;
			 int point_y = outerContours.get(i).points.get(0).y;
			 
			 
			 for(int j=0; j<suppercode.get(i).size(); j++){
				 
				 ip.putPixel(point_x, point_y, 255);
				 int u = (int) suppercode.get(i).get(j);
				 
				 if(u==0)  {
					 point_x = point_x + 1;
					 //point_y = point_y;
				 }
				 else if(u==1){
					 point_x = point_x + 1;
					 point_y = point_y + 1;
				 }
				 else if(u==2){
					 //point_x = point_x;
					 point_y = point_y + 1;
				 }
				 else if(u==3){
					 point_x = point_x - 1;
					 point_y = point_y + 1;
				 }
				 else if(u==4){
					 point_x = point_x - 1;
					 //point_y = point_y;
				 }
				 else if(u==5){
					 point_x = point_x - 1;
					 point_y = point_y - 1;
				 }
				 else if(u==6){
					 //point_x = point_x ;
					 point_y = point_y - 1;
				 }
				 else{
					 point_x = point_x + 1;
					 point_y = point_y - 1;
				 }
				 					 
			 }
		 }
		 
		 
		 /***************************************
		  * innerContour
		  **************************************/
		 List<Integer> code_i = null;
		 List<List> suppercode_i = null; 
		 
		 int x_i,y_i,xx_i, yy_i;
		 int lengthContour_i = innerContours.size();
		 suppercode_i = new ArrayList<List>();
		 for(int i=0; i<lengthContour_i; i++){
			 code_i = new ArrayList<Integer>();
			 for(int j=0; j<innerContours.get(i).points.size(); j++){
				 x_i= innerContours.get(i).points.get(j).x;
				 y_i= innerContours.get(i).points.get(j).y;
				 if(j==innerContours.get(i).points.size()-1){
					 xx_i = innerContours.get(i).points.get(0).x;
					 yy_i = innerContours.get(i).points.get(0).y;
				 }
				 else{
					 xx_i = innerContours.get(i).points.get(j+1).x;
					 yy_i = innerContours.get(i).points.get(j+1).y;
				 }
				 
				 int xxx_i = xx_i-x_i;
				 int yyy_i = yy_i-y_i;
				 int chaincode_i = 0;
				 
				 if(xxx_i==1&&yyy_i==0)        chaincode_i = 0;			 
				 else if(xxx_i==1&&yyy_i==1)   chaincode_i = 1;
				 else if(xxx_i==0&&yyy_i==1)   chaincode_i = 2;
				 else if(xxx_i==-1&&yyy_i==1)  chaincode_i = 3;
				 else if(xxx_i==-1&&yyy_i==0)  chaincode_i = 4;
				 else if(xxx_i==-1&&yyy_i==-1) chaincode_i = 5;
				 else if(xxx_i==0&&yyy_i==-1)  chaincode_i = 6;
				 else chaincode_i = 7;
				 
				 code_i.add(chaincode_i);	
			 }
			 suppercode_i.add(code_i);
			 System.out.println(suppercode_i.get(i).size());
			 System.out.println("YOYO     "+suppercode_i.get(i).get(1)); 
			 
			 
		 }
	 
		 
		 for(int i=0; i<suppercode_i.size(); i++){
			 int point_x = innerContours.get(i).points.get(0).x;
			 int point_y = innerContours.get(i).points.get(0).y;
			 
			 
			 for(int j=0; j<suppercode_i.get(i).size(); j++){
				 
				 ip.putPixel(point_x, point_y, 100);
				 int u = (int) suppercode_i.get(i).get(j);
				 
				 if(u==0)  {
					 point_x = point_x + 1;
					 //point_y = point_y;
				 }
				 else if(u==1){
					 point_x = point_x + 1;
					 point_y = point_y + 1;
				 }
				 else if(u==2){
					 //point_x = point_x;
					 point_y = point_y + 1;
				 }
				 else if(u==3){
					 point_x = point_x - 1;
					 point_y = point_y + 1;
				 }
				 else if(u==4){
					 point_x = point_x - 1;
					 //point_y = point_y;
				 }
				 else if(u==5){
					 point_x = point_x - 1;
					 point_y = point_y - 1;
				 }
				 else if(u==6){
					 //point_x = point_x ;
					 point_y = point_y - 1;
				 }
				 else{
					 point_x = point_x + 1;
					 point_y = point_y - 1;
				 }
				 					 
			 }
		 }
		 
		 //////////////////////////////////////////
		 
		 
		 
	 }
 
	 
	 
	 @SuppressWarnings("deprecation")
	 void printRegions(List<BinaryRegion> regions) {
		 for (BinaryRegion r: regions) {
			 IJ.write("" + r);
		 }
	 }
 
} // end of class Contour_Tracing_Plugin