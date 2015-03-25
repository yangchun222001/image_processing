
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

 public class Contour {
	 static int INITIAL_SIZE = 50;
	 int label;
	 List<Point> points;

	 Contour (int label, int size) {
		 this.label = label;
		 points = new ArrayList<Point>(size);
	 }

	 Contour (int label) {
		 this.label = label;
		 points = new ArrayList<Point>(INITIAL_SIZE);
	 }

	 void addPoint (Point n) {
		 points.add(n);
	 }

	 Shape makePolygon() {
		 int m = points.size();
		 if (m>1) {
			 int[] xPoints = new int[m];
			 int[] yPoints = new int[m];
			 int k = 0;
			 Iterator<Point> itr = points.iterator();
			 while (itr.hasNext() && k < m) {
				 Point cpt = itr.next();
				 xPoints[k] = cpt.x;
				 yPoints[k] = cpt.y;
				 k = k + 1;
			 }
			 return new Polygon(xPoints, yPoints, m);
		 }
		 else { // use circles for isolated pixels
			 Point cpt = points.get(0);
			 return new Ellipse2D.Double
					 (cpt.x-0.1, cpt.y-0.1, 0.2, 0.2);
		 }
	 }

	 static Shape[] makePolygons(List<Contour> contours) {
		 if (contours == null)
			 return null;
		 else {
			 Shape[] pa = new Shape[contours.size()];
			 int i = 0;
			 for (Contour c: contours) {
				 pa[i] = c.makePolygon();
				 i = i + 1;
			 }
			 return pa;
		 }
	 }
	 void moveBy (int dx, int dy) {
		 for (Point pt: points) {
			 pt.translate(dx,dy);
		 }
	 }

	 static void moveContoursBy
	 (List<Contour> contours, int dx, int dy) {
		 for (Contour c: contours) {
			 c.moveBy(dx, dy);
		 }
	 }

 } // end of class Contour