package org.alder.fotobuchconvert.scribus;

import java.awt.Color;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ImageIcon;

public class ScribusWriter {

	private final PrintStream out;

	double pageW;// A4=595.28;
	double pageH;// A4=841.89;
	String pageFormat;// A4
	double pagePosX = 100;
	double pagePosY = 20;
	double margin;
	double bleed;
	double vgap = 100;

	private static final String FRTYPE = "FRTYPE";// Resize complex shapes

	private final Vector<PageDims> pageDims = new Vector<PageDims>();

	private final XmlBuilder doc;

	private final HashMap<String, Integer> masterPages = new HashMap<String, Integer>();

	private final XmlBuilder root;

	public ScribusWriter(File file, double margin, double bleed,
			String pageFormat, double pageW, double pageH)
			throws FileNotFoundException {

		out = new PrintStream(file);

		this.margin = margin;
		this.bleed = bleed;
		this.pageFormat = pageFormat;
		this.pageW = pageW;
		this.pageH = pageH;

		root = new XmlBuilder("SCRIBUSUTF8NEW").set("Version", "1.4.0.rc3");
		doc = createIntro();
	}

	private XmlBuilder createIntro() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("  Compatible file format for Scribus\n");
		sb.append("\n");
		sb.append("  This format was tested with Scribus 1.4.0");
		sb.append("\n");
		sb.append("  Because the format is very complex, it might be a good idea,\n");
		sb.append("  once loaded, to save the file again within Scribus to ensure\n");
		sb.append("  better compatibility to later versions.\n");
		sb.append("\n");
		root.comment(sb.toString());

		XmlBuilder doc;
		doc = root.add("DOCUMENT")
				.set("ANZPAGES", 3)
				.set("FIRSTNUM", "1")
				.set("BOOK", "1")
				// UNITS pt=0 cm=4
				.set("UNITS", "0").set("BleedTop", bleed)
				.set("BleedLeft", bleed).set("BleedRight", bleed)
				.set("BleedBottom", bleed).set("ORIENTATION", "0")
				.set("PAGESIZE", pageFormat);
		applyPageSettings(doc, true, pageW, pageH);

		doc.add("COLOR").set("NAME", "Black").set("CMYK", "#000000ff")
				.set("Spot", "0").set("Register", "0");
		doc.add("COLOR").set("NAME", "White").set("CMYK", "#00000000")
				.set("Spot", "0").set("Register", "0");

		doc.add("COLOR").set("NAME", "Blue").set("RGB", "#0000ff")
				.set("Spot", "0").set("Register", "0");
		doc.add("COLOR").set("NAME", "Cool Black").set("CMYK", "#990000ff")
				.set("Spot", "0").set("Register", "0");
		doc.add("COLOR").set("NAME", "Cyan").set("CMYK", "#ff000000")
				.set("Spot", "0").set("Register", "0");
		doc.add("COLOR").set("NAME", "Green").set("RGB", "#00ff00")
				.set("Spot", "0").set("Register", "0");
		doc.add("COLOR").set("NAME", "Magenta").set("CMYK", "#00ff0000")
				.set("Spot", "0").set("Register", "0");
		doc.add("COLOR").set("NAME", "Red").set("RGB", "#ff0000")
				.set("Spot", "0").set("Register", "0");
		doc.add("COLOR").set("NAME", "Rich Black").set("CMYK", "#996666ff")
				.set("Spot", "0").set("Register", "0");
		doc.add("COLOR").set("NAME", "Warm Black").set("CMYK", "#00994cff")
				.set("Spot", "0").set("Register", "0");
		doc.add("COLOR").set("NAME", "Yellow").set("CMYK", "#0000ff00")
				.set("Spot", "0").set("Register", "0");

		// seems to be necessary for scribus 1.4.0
		doc.add("LAYERS").set("NUMMER", "0").set("SICHTBAR", "1")
				.set("DRUCKEN", "1").set("EDIT", "1").set("FLOW", "1")
				.set("TRANS", "1");

		return doc;
	}

	public PageDims addPage(String masterName, double pageW, double pageH) {
		final int pgnum = pageDims.size();

		int left = (pgnum % 2);
		masterName = masterName + (left == 1 ? " Left" : " Right");

		PageDims pd = new PageDims(pageDims, pgnum, left == 1, pageW, pageH);
		pageDims.add(pd);

		if (!masterPages.containsKey(masterName)) {
			int mpgnum = masterPages.size();
			XmlBuilder pagemaster;
			pagemaster = doc.add("MASTERPAGE").set("NAM", masterName)
					.set("NUM", mpgnum).set("LEFT", left)
					.set("PAGEXPOS", pagePosX).set("PAGEYPOS", pagePosY);
			applyPageSettings(pagemaster, false, pageW, pageH);
			masterPages.put(masterName, mpgnum);
		}

		XmlBuilder page;

		page = doc.add("PAGE").set("MNAM", masterName).set("NUM", pgnum)
				.set("LEFT", left).set("PAGEXPOS", pd.docbaseX)
				.set("PAGEYPOS", pd.docbaseY);
		applyPageSettings(page, false, pageW, pageH);

		return pd;
	}

	public void finish() {
		root.output(out, 0);
		System.out.println("File written.");
	}

	private void applyPageSettings(XmlBuilder doc, boolean isDoc, double pageW,
			double pageH) {
		doc.set("PAGEWIDTH", pageW).set("PAGEHEIGHT", pageH)
				.set("BORDERLEFT", margin).set("BORDERRIGHT", margin)
				.set("BORDERTOP", margin).set("BORDERBOTTOM", margin)
				.set("PRESET", "0");

		if (isDoc)
			doc.set("GapHorizontal", "0").set("GapVertical", vgap);
		else
			doc.set("VerticalGuides", "").set("HorizontalGuides", "")
					.set("AGhorizontalAutoGap", "0")
					.set("AGverticalAutoGap", vgap)
					.set("AGhorizontalAutoCount", "0")
					.set("AGverticalAutoCount", "0")
					.set("AGhorizontalAutoRefer", "0")
					.set("AGverticalAutoRefer", "0")
					.set("AGSelection", "0 0 0 0").set("Size", pageFormat);
	}

	public ScribusShape makeRect(double x, double y, double w, double h,
			double rot) {
		ScribusShape si = new ScribusShape();
		si.setPosition(x, y, w, h, rot);
		si.setBorder();
		// si.element.set("PICART", "1");
		// cropping-shape NUMPO:

		return si;
	}

	public PageDims[] getPageDims() {
		return pageDims.toArray(new PageDims[0]);
	}

	public class PageDims {
		public final double docbaseX, docbaseY;
		private final double pageW, pageH;
		private final boolean left;

		// public PageDims(int num) {
		// // docbase bezeichnet die sichtbare Ecke (roter Rand)
		// docbaseX = pagePosX + ((num + 1) % 2) * pageW;// - 1;
		// docbaseY = pagePosY + (int) ((num + 1) / 2) * pageH;
		// }

		public PageDims(Vector<PageDims> pageDims, int pgnum, boolean left,
				double pageW, double pageH) {

			double x = pagePosX;
			double y = pagePosY;

			for (PageDims pd : pageDims)
				if (pd.left == left)
					y += pd.pageH + vgap;

			// for(int i=pgnum-1;i>=0 && )
			if (!left && pgnum > 0)
				x += pageDims.get(pgnum - 1).pageW;

			// HACK:
			x = pagePosX + ((pgnum + 1) % 2) * pageW;// - 1;
			y = pagePosY + (int) ((pgnum + 1) / 2) * (pageH + vgap);

			this.docbaseX = x;
			this.docbaseY = y;
			this.pageW = pageW;
			this.pageH = pageH;
			this.left = left;
		}
	}

	public ScribusImg addImage(String imagePath) throws IOException {
		ScribusImg si = new ScribusImg(imagePath);
		return si;
	}

	public ScribusLine addLine(double x1, double y1, double x2, double y2) {
		ScribusLine si = new ScribusLine();
		double w = x2 - x1, h = y2 - y1;
		si.setPosition(x1, y1, Math.sqrt(w * w + h * h), 1, Math.atan2(h, w)
				/ Math.PI * 180);
		si.setBorder();

		si.element.set("PWIDTH", 1);
		return si;
	}

	public class ScribusObject {

		protected final XmlBuilder element;
		final private int type;

		protected double w, h;

		public ScribusObject(int type) {
			this.type = type;

			element = doc.add("PAGEOBJECT").set("OwnPage", 0)
					.set("PTYPE", type);

			// NEXTITEM/BACKITEM important!
			element.set("NEXTITEM", "-1").set("BACKITEM", "-1");

			// druckbar
			element.set("PRINTABLE", "1");
		}

		public final void setPositionCenterRot(double x, double y, double w,
				double h, double angleDegrees) {
			double rad = angleDegrees / 180 * Math.PI;

			double cX = x + w / 2, cY = y + h / 2;

			AffineTransform trans = new AffineTransform();
			trans.translate(cX, cY);
			trans.rotate(rad);
			trans.translate(-cX, -cY);

			double[] src = { x, y }, dst = new double[2];
			trans.transform(src, 0, dst, 0, 1);
			x = dst[0];
			y = dst[1];

			setPosition(x, y, w, h, angleDegrees);
		}

		public void setPosition(double x, double y, double w, double h,
				double angleDegrees) {
			this.w = w;
			this.h = h;

			element.set("XPOS", x).set("YPOS", y);
			element.set("WIDTH", w).set("HEIGHT", h);

			// ROT: rotation
			element.set("ROT", angleDegrees);

			ScribusPolyBuilder pb;
			pb = getPoly(false);
			if (pb != null) {
				element.set("NUMPO", pb.getNumber());
				element.set("POCOOR", pb.getCoordsStr());
			}
			pb = getPoly(true);
			if (pb != null) {
				element.set("NUMCO", pb.getNumber());
				element.set("COCOOR", pb.getCoordsStr());
			}
		}

		/**
		 * @param co
		 *            co=true=COCOORD, co=false=POCOORD
		 */
		protected ScribusPolyBuilder getPoly(boolean co) {

			if (type == 5 || co == true)
				return null;

			ScribusPolyBuilder pb = new ScribusPolyBuilder();

			// cropping-shape PO: (also needed for shapes)
			pb.add(0, 0);
			pb.add2(w, 0);
			pb.add2(w, h);
			pb.add2(0, h);
			pb.add(0, 0);

			return pb;
		}

		public void setBorder() {
			element.set("PCOLOR2", "Black");
			// SHADE2: Deckung Linie (100=full)
			element.set("SHADE2", "100");
		}

		public void setFill(String color) {
			element.set("PCOLOR", color);
			// SHADE2: Deckung Fläche (100=full)
			element.set("SHADE", "100");
		}

	}

	public class ScribusShape extends ScribusObject {
		public ScribusShape() {
			super(6);// auch Rechtecke
		}
	}

	public class ScribusText extends ScribusObject {
		public ScribusText() {
			super(4);
		}

		public void text(String text) {
			System.out.println(text);

			element.add("ITEXT").set("CH", "text");
			element.add("para");
			element.add("ITEXT").set("FONT", "Arial Bold").set("CH", "fett");
			element.add("para");
			element.add("ITEXT").set("FONT", "Arial Italic")
					.set("CH", "kursiv");
			element.add("para");
			element.add("ITEXT").set("FONT", "Arial Bold Italic")
					.set("CH", "fettkursiv");
			element.add("para");
			element.add("ITEXT").set("FONTSIZE", "16").set("CH", "gross");
		}

		public XmlBuilder getElement() {
			return element;
		}
	}

	public class ScribusImg extends ScribusObject {
		private final String imagePath;
		private final double imgW, imgH;

		public ScribusImg(String imagePath) throws IOException {
			super(2);

			this.imagePath = imagePath;

			if (imagePath != null) {
				Image img = new ImageIcon(imagePath).getImage();
				imgW = img.getWidth(null);
				imgH = img.getHeight(null);
				if (imgW <= 0 || imgH <= 0)
					throw new IOException("Image file not found");
				element.set("PFILE", imagePath);
			} else {
				imgW = 0;
				imgH = 0;
			}

			element.set("PICART", "1");
		}

		public void setCropPct(double cropX, double cropY, double cropW,
				double cropH) {
			assert w > 0 && h > 0;

			// SCALETYPE: 0=img-auto-resize, 1=manual
			element.set("SCALETYPE", "1");

			double localscx = w / (imgW * cropW);
			double localscy = h / (imgH * cropH);

			// ifolor Designer only allows proportionally scaled images.
			assert localscx / localscy > 0.999d && localscx / localscy < 1.001d;
			System.out.println("Scale h/v factor: " + localscx / localscy
					+ "  (1.000 is best)");

			// LOCALSCX: image scale [1/n]
			element.set("LOCALSCX", localscx);
			element.set("LOCALSCY", localscy);

			// double locx = -(cropX / cropW * w);
			// double locy = -(cropY / cropH * h);
			double locx = -(imgW * cropX);
			double locy = -(imgH * cropY);
			// LOCALX image offset [pt], wird mit LOCALSCX mult.
			element.set("LOCALX", locx);
			element.set("LOCALY", locy);

			System.out.printf(
					"CROP: %f/%f\t%f/%f\t%f %f %f %f   -> %f %f %f %f\n", w, h,
					imgW, imgH, cropX, cropY, cropW, cropH, localscx, localscy,
					locx, locy);
		}

		public void addPictureFrame(double x, double y, double w, double h,
				double angleDegrees) {
			ScribusImgFrame frame = new ScribusImgFrame();
			frame.setPositionCenterRot(x, y, w, h, angleDegrees);
			frame.setBorder();
			frame.setFill("Warm Black");
		}
	}

	public class ScribusImgFrame extends ScribusShape {
		public ScribusImgFrame() {
			super();

			element.set(FRTYPE, "3");
		}

		@Override
		protected ScribusPolyBuilder getPoly(boolean co) {
			double x0 = -5, y0 = -5;
			double x1 = 5, y1 = 5;
			double x2 = w - x1, y2 = h - y1;
			double x3 = w - x0, y3 = h - y0;
			// double xy_g = 999999;
			// String costr = x1 + " " + y1 + " " + x1 + " " + y1 + " " + x2 +
			// " "
			// + y1 + " " + x2 + " " + y1 + " " + x2 + " " + y1 + " " + x2
			// + " " + y1 + " " + x2 + " " + y2 + " " + x2 + " " + y2
			// + " " + x2 + " " + y2 + " " + x2 + " " + y2 + " " + x1
			// + " " + y2 + " " + x1 + " " + y2 + " " + x1 + " " + y2
			// + " " + x1 + " " + y2 + " " + x1 + " " + y1 + " " + x1
			// + " " + y1 + " " + xy_g + " " + xy_g + " " + xy_g + " "
			// + xy_g + " " + xy_g + " " + xy_g + " " + xy_g + " " + xy_g
			// + " " + x0 + " " + y0 + " " + x0 + " " + y0 + " " + x3
			// + " " + y0 + " " + x3 + " " + y0 + " " + x3 + " " + x0
			// + " " + x3 + " " + y0 + " " + x3 + " " + y3 + " " + x3
			// + " " + y3 + " " + x3 + " " + y3 + " " + x3 + " " + y3
			// + " " + x0 + " " + y3 + " " + x0 + " " + y3 + " " + y0
			// + " " + y3 + " " + y0 + " " + y3 + " " + x0 + " " + y0
			// + " " + x0 + " " + y0 + " ";

			ScribusPolyBuilder pb = new ScribusPolyBuilder();

			// cropping-shape PO: (also needed for shapes)
			pb.add(x1, y1);
			pb.add2(x2, y1);
			pb.add2(x2, y2);
			pb.add2(x1, y2);
			pb.add(x1, y1);
			pb.sep();
			pb.add(x0, y0);
			pb.add2(x0, y3);
			pb.add2(x3, y3);
			pb.add2(x3, y0);
			pb.add(x0, y0);

			// element.set("NUMPO", "36").set("POCOOR", costr).set("NUMCO",
			// "36")
			// .set("COCOOR", costr);

			return pb;

			// .set("OwnPage","3")
			// --------------
			// .set("RADRECT", "0")
		}
	}

	public class ScribusLine extends ScribusObject {
		public ScribusLine() {
			super(5);
		}
	}

	public ScribusText addText() {
		return new ScribusText();
	}

	public String getColorName(Color color) {
		String rgbcode = Integer.toHexString(color.getRGB()).substring(2);
		String key = "RGB." + rgbcode;

		HashMap<String, String> colortable = new HashMap<String, String>();
		String colname = colortable.get(key);
		if (colname == null) {
			colname = "color" + colortable.size();
			colortable.put(key, colname);
			doc.add("COLOR").set("NAME", colname).set("RGB", "#" + rgbcode)
					.set("Spot", "0").set("Register", "0");
		}

		return colname;
	}
}
