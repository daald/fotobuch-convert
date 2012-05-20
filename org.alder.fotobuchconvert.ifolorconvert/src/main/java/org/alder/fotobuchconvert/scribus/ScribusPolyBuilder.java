package org.alder.fotobuchconvert.scribus;

import java.util.Vector;

public class ScribusPolyBuilder {
	Vector<Point> points = new Vector<Point>();

	public void add(double x, double y) {
		points.add(new Point(x, y, x, y));
	}

	public void add(double x1, double y1, double x2, double y2) {
		points.add(new Point(x1, y1, x2, y2));
	}

	public void sep() {
		add2(999999, 999999);
	}

	public String getCoordsStr() {
		StringBuilder sb = new StringBuilder();
		for (Point pt : points) {
			sb.append(pt.x);
			sb.append(' ');
			sb.append(pt.y);
			sb.append(' ');
			sb.append(pt.rx);
			sb.append(' ');
			sb.append(pt.ry);
			sb.append(' ');
		}
		return sb.toString();
	}

	public int getNumber() {
		return points.size() * 2;
	}

	static final class Point {
		public final double x, y;
		public final double rx, ry;

		public Point(double x, double y, double rx, double ry) {
			this.x = x;
			this.y = y;
			this.rx = rx;
			this.ry = ry;
		}
	}

	public void add2(double x, double y) {
		add(x, y);
		add(x, y);
	}

}
