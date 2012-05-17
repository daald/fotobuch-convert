package org.alder.fotobuchconvert.objects;

public class Shadow {
	public final double rx, ry;
	public final double scale;
	public final double transparency;

	public Shadow(double rx, double ry, double scale, double transparency) {
		this.rx = rx;
		this.ry = ry;
		this.scale = scale;
		this.transparency = transparency;
	}

	public static class SoftShadow extends Shadow {

		public final double softedge;

		public SoftShadow(double rx, double ry, double scale,
				double transparency, double softedge) {
			super(rx, ry, scale, transparency);

			this.softedge = softedge;
		}

	}

	public static class HardShadow extends Shadow {

		public HardShadow(double rx, double ry, double scale,
				double transparency) {
			super(rx, ry, scale, transparency);
		}
	}
}
