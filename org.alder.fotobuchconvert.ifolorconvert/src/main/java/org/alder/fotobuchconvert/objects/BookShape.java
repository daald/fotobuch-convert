package org.alder.fotobuchconvert.objects;

import java.awt.Color;

public class BookShape extends BookElement {

	public final ShapeColor[] colors;

	public BookShape(int left, int top, int width, int height,
			int angleDegrees, ShapeColor[] colors) {
		super(left, top, width, height, angleDegrees);

		this.colors = colors;
	}

	public final static class ShapeColor {
		public final Color color;
		public final double position;

		public ShapeColor(Color color, double position) {
			this.color = color;
			this.position = position;
		}
	}

}
