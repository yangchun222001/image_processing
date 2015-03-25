package regions;
import java.awt.Rectangle;

 import java.awt.geom.Point2D;

 public class BinaryRegion {
 int label;
 int numberOfPixels = 0;
 double xc = Double.NaN;
 double yc = Double.NaN;
 int left = Integer.MAX_VALUE;
 int right = -1;
 int top = Integer.MAX_VALUE;
 int bottom = -1;

 int x_sum = 0;
 int y_sum = 0;
 int x2_sum = 0;
 int y2_sum = 0;

 public BinaryRegion(int id){
 this.label = id;
 }

 public int getSize() {
	 return this.numberOfPixels;
	  }
	 
	  public Rectangle getBoundingBox() {
	  if (left == Integer.MAX_VALUE)
	  return null;
	  else
	  return new Rectangle
	  (left, top, right-left+1, bottom-top+1);
	  }
	 
	  public Point2D.Double getCenter(){
	  if (Double.isNaN(xc))
	  return null;
	  else
	  return new Point2D.Double(xc, yc);
	  }
	 
	  public void addPixel(int x, int y){
	 numberOfPixels = numberOfPixels + 1;
	  x_sum = x_sum + x;
	  y_sum = y_sum + y;
	  x2_sum = x2_sum + x*x;
	  y2_sum = y2_sum + y*y;
	  if (x<left) left = x;
	  if (y<top) top = y;
	  if (x>right) right = x;
	  if (y>bottom) bottom = y;
	  }
	 
	  public void update(){
	  if (numberOfPixels > 0){
	  xc = x_sum / numberOfPixels;
	  yc = y_sum / numberOfPixels;
	  }
	  }
	 
	  } // end of class BinaryRegion