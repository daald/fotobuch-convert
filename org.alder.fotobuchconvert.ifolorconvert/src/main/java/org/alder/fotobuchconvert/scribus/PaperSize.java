package org.alder.fotobuchconvert.scribus;

public abstract class PaperSize {
	public final int ifolorDPI, ifolorDoubleWidth, ifolorHeight;
	public final double margin, bleed;

	public PaperSize(int ifolorDPI, int ifolorDoubleWidth, int ifolorHeight,
			double margin, double bleed) {
		this.ifolorDPI = ifolorDPI;
		this.ifolorDoubleWidth = ifolorDoubleWidth;
		this.ifolorHeight = ifolorHeight;
		this.margin = margin;
		this.bleed = bleed;
	}

	public static class A4_Landscape extends PaperSize {
		public A4_Landscape() {
			// int ifolorDoubleWidth = 7062;// width of a double page
			// int ifolorHeight = 2504;
			// int ifolorDPI = 300;
			super(
			/* dpi */300,
			/* width */7062, /* height */2504,
			/* margin */9.33d, /* bleed */9.33d);
		}
	}

	public static class A4_Portrait extends PaperSize {
		public A4_Portrait() {
			// int ifolorDoubleWidth = 7062;// width of a double page
			// int ifolorHeight = 2504;
			// int ifolorDPI = 300;
			super(
			/* dpi */300,
			/* width */2504 * 2,/* height */7062 / 2,
			/* margin */9.33d, /* bleed */9.33d);
		}
	}

}
