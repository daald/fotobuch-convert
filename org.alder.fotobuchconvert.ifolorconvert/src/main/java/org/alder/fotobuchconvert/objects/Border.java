package org.alder.fotobuchconvert.objects;

import java.awt.Color;

public class Border {

	public static class LineBorder extends Border {
		public final double width;
		public final Color color;

		public LineBorder(double width, Color color) {
			this.width = width;
			this.color = color;
		}
	}

	public static class HeavyBorder extends Border {
		public final double width;
		public final Color color;
		public final double transparency;

		public HeavyBorder(double width, Color color, double transparency) {
			this.width = width;
			this.color = color;
			this.transparency = transparency;
		}
	}

	public static class ScratchBorder extends Border {
		public final double width;

		public ScratchBorder(double width) {
			this.width = width;
		}
	}
}
