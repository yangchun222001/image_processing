
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import regions.BinaryRegion;
import ij.process.ImageProcessor;

 public class ContourTracer {
	  static final byte FOREGROUND = 1;
	  static final byte BACKGROUND = 0;
	  static boolean beVerbose = true;
	 
	  List<Contour> outerContours = null;
	  List<Contour> innerContours = null;
	  List<BinaryRegion> allRegions = null;
	  int regionId = 0;
	 
	  ImageProcessor ip = null;
	  int width;
	  int height;
	  byte[][] pixelArray;
	  int[][] labelArray;
	 

	  public ContourTracer (ImageProcessor ip) {
		  this.ip = ip;
		  this.width = ip.getWidth();
		  this.height = ip.getHeight();
		  makeAuxArrays();
		  findAllContours();
		  collectRegions();
	  }
	 
	 public static void setVerbose(boolean verbose) {
		 beVerbose = verbose;
	 }
	 
	  public List<Contour> getOuterContours() {
		  return outerContours;
	  }
	 
	  public List<Contour> getInnerContours() {
		  return innerContours;
	  }
	 
	  public List<BinaryRegion> getRegions() {
		  return allRegions;
	  }
	 
	  // nonpublic methods
	 
	  void makeAuxArrays() {
		  int h = ip.getHeight();
		  int w = ip.getWidth();
		  pixelArray = new byte[h+2][w+2];
		  labelArray = new int[h+2][w+2];
		  // initialize auxiliary arrays
		  for (int v = 0; v < h+2; v++) {
			  for (int u = 0; u < w+2; u++) {
				  if (ip.getPixel(u-1,v-1) == 0)
					  pixelArray[v][u] = BACKGROUND;
				  else
					  pixelArray[v][u] = FOREGROUND;
			  }
		  }
	  }
	  
	   Contour traceOuterContour (int cx, int cy, int label) {
		   Contour cont = new Contour(label);
		   traceContour(cx, cy, label, 0, cont);
		   return cont;
	   }
	  
	   Contour traceInnerContour(int cx, int cy, int label) {
		   Contour cont = new Contour(label);
		   traceContour(cx, cy, label, 1, cont);
		   return cont;
	   }
	  
	   // trace one contour starting at ( xS,yS) in direction dS
	  Contour traceContour (int xS, int yS, int label, int dS,
			  Contour cont) {
		  int xT, yT; // T = successor of starting point ( xS,yS)
		  int xP, yP; // P = previous contour point
		  int xC, yC; // C = current contour point
		  Point pt = new Point(xS, yS);
		  int dNext = findNextPoint(pt, dS);
		  cont.addPoint(pt);
		  xP = xS; yP = yS;
		  xC = xT = pt.x;
		  yC = yT = pt.y;
	  
		  boolean done = (xS==xT && yS==yT); // true if isolated pixel
	  
		  while (!done) {
			  labelArray[yC][xC] = label;
			  pt = new Point(xC, yC);
			  int dSearch = (dNext + 6) % 8;
			  dNext = findNextPoint(pt, dSearch);
			  xP = xC; yP = yC;
			  xC = pt.x; yC = pt.y;
			  // are we back at the starting position?
			  done = (xP==xS && yP==yS && xC==xT && yC==yT);
			  if (!done) {
				  cont.addPoint(pt);
			  }
		  }
		  return cont;
	  }
	  
	  int findNextPoint (Point pt, int dir) {
		  // starts at Point pt in direction dir, returns the
		  // final tracing direction, and modifies pt
		  final int[][] delta = {
				  { 1,0}, { 1, 1}, {0, 1}, {-1, 1},
				  {-1,0}, {-1,-1}, {0,-1}, { 1,-1}};
		  for (int i = 0; i < 7; i++) {
			  int x = pt.x + delta[dir][0];
			  int y = pt.y + delta[dir][1];
			  if (pixelArray[y][x] == BACKGROUND) {
				  // mark surrounding background pixels
				  labelArray[y][x] = -1;
				  dir = (dir + 1) % 8;
			  }
			  else { // found a nonbackground pixel
				  pt.x = x; pt.y = y;
				  break;
			  }
		  }
		  return dir;
	  }
		   
	  void findAllContours() {
		  outerContours = new ArrayList<Contour>(50);
		  innerContours = new ArrayList<Contour>(50);
		  int label = 0; // current label
		   
		  // scan top to bottom, left to right
		  for (int v = 1; v < pixelArray.length-1; v++) {
			  label = 0; // no label
			  for (int u = 1; u < pixelArray[v].length-1; u++) {
		   
				  if (pixelArray[v][u] == FOREGROUND) {
					  if (label != 0) { // keep using the same label
						  labelArray[v][u] = label;
					  }
					  else {
						  label = labelArray[v][u];
						  if (label == 0) {
							  // unlabeled¡ªnew outer contour
							  regionId = regionId + 1;
							  label = regionId;
							  Contour oc = traceOuterContour(u, v, label);
							  outerContours.add(oc);
							  labelArray[v][u] = label;
						  }
					  }
				  }
				  else { // background pixel
					  if (label != 0) {
						  if (labelArray[v][u] == 0) {
							  // unlabeled¡ªnew inner contour
							  Contour ic = traceInnerContour(u-1, v, label);
							  innerContours.add(ic);
						  }
						  label = 0;
					  }
				  }
			  }
		  }
		  // shift back to original coordinates
		  Contour.moveContoursBy (outerContours, -1, -1);
		  Contour.moveContoursBy (innerContours, -1, -1);
	  }
		    
		   
		    // creates a container of BinaryRegion objects
		     // collects the region pixels from the label image
		    // and computes the statistics for each region
	  void collectRegions() {
		  int maxLabel = this.regionId;
		  int startLabel = 1;
		  BinaryRegion[] regionArray =
				  new BinaryRegion[maxLabel + 1];
		  for (int i = startLabel; i <= maxLabel; i++) {
			  regionArray[i] = new BinaryRegion(i);
		  }
		  for (int v = 0; v < height; v++) {
			  for (int u = 0; u < width; u++) {
				  int lb = labelArray[v][u];
				  if (lb >= startLabel && lb <= maxLabel
						  && regionArray[lb]!=null) {
					  regionArray[lb].addPixel(u, v);
				  }
			  }
		  }
		    
		   // create a list of regions to return, collect nonempty regions
		  List<BinaryRegion> regionList =
				  new LinkedList<BinaryRegion>();
		  for (BinaryRegion r: regionArray) {
			  if (r != null && r.getSize()>0) {
				  r.update(); // compute the statistics for this region
				  regionList.add(r);
			  }
		  }
		  allRegions = regionList;
	  }
		     
 }