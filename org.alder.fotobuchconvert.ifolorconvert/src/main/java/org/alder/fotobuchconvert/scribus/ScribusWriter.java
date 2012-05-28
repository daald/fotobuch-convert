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

import org.alder.fotobuchconvert.objects.BookShape.ShapeColor;
import org.alder.fotobuchconvert.tools.XmlBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ScribusWriter {

	private static final String OUTPUT_DOC_VERSION = "1.4.0.rc3";

	private final Log log = LogFactory.getLog(ScribusWriter.class);

	private final PrintStream out;

	double pageW;// A4=595.28;
	double pageH;// A4=841.89;
	String pageFormat;// A4
	double pagePosX = 100;
	double pagePosY = 20;
	double margin;
	double bleed;
	double vgap = 100;

	private final Vector<PageDims> pageDims = new Vector<PageDims>();

	private final XmlBuilder doc;

	private final HashMap<String, Integer> masterPages = new HashMap<String, Integer>();

	private final XmlBuilder root;
	public final ColorManager colorManager;

	public int numgroups;

	public ScribusWriter(File file, double margin, double bleed,
			String pageFormat, double pageW, double pageH)
			throws FileNotFoundException {

		out = new PrintStream(file);

		this.margin = margin;
		this.bleed = bleed;
		this.pageFormat = pageFormat;
		this.pageW = pageW;
		this.pageH = pageH;

		root = new XmlBuilder(C.EL_SCRIBUSUTF8NEW).set(C.VERSION,
				OUTPUT_DOC_VERSION);
		doc = createIntro();

		colorManager = new ColorManager(doc);
		colorManager.initialize();
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
		doc = root.add(C.EL_DOCUMENT)
				.set(C.ANZPAGES, 3)
				.set(C.FIRSTNUM, 1)
				.set(C.BOOK, 1)
				// UNITS pt=0 cm=4
				.set(C.UNITS, 0).set(C.BLEED_TOP, bleed)
				.set(C.BLEED_LEFT, bleed).set(C.BLEED_RIGHT, bleed)
				.set(C.BLEED_BOTTOM, bleed).set(C.ORIENTATION, 0)
				.set(C.PAGESIZE, pageFormat);
		applyPageSettings(doc, true, pageW, pageH);

		// seems to be necessary for scribus 1.4.0
		doc.add(C.EL_LAYERS).set(C.NUMMER, 0).set(C.SICHTBAR, 1)
				.set(C.DRUCKEN, 1).set(C.EDIT, 1).set(C.FLOW, 1)
				.set(C.TRANS, 1);

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
			pagemaster = doc.addAfterSimilar(C.EL_MASTERPAGE)
					.set(C.NAM, masterName).set(C.NUM, mpgnum)
					.set(C.LEFT, left).set(C.PAGEXPOS, pagePosX)
					.set(C.PAGEYPOS, pagePosY);
			applyPageSettings(pagemaster, false, pageW, pageH);
			masterPages.put(masterName, mpgnum);
		}

		XmlBuilder page;

		page = doc.addAfterSimilar(C.EL_PAGE).set(C.MNAM, masterName)
				.set(C.NUM, pgnum).set(C.LEFT, left)
				.set(C.PAGEXPOS, pd.docbaseX).set(C.PAGEYPOS, pd.docbaseY);
		applyPageSettings(page, false, pageW, pageH);

		return pd;
	}

	public void finish() {
		root.output(out);
		log.info("File written.");
	}

	private void applyPageSettings(XmlBuilder doc, boolean isDoc, double pageW,
			double pageH) {
		doc.set(C.PAGEWIDTH, pageW).set(C.PAGEHEIGHT, pageH)
				.set(C.BORDERLEFT, margin).set(C.BORDERRIGHT, margin)
				.set(C.BORDERTOP, margin).set(C.BORDERBOTTOM, margin)
				.set(C.PRESET, 0);

		if (isDoc)
			doc.set(C.GAP_HORIZONTAL, 0).set(C.GAP_VERTICAL, vgap);
		else
			doc.set(C.VERTICAL_GUIDES, "").set(C.HORIZONTAL_GUIDES, "")
					.set(C.A_GHORIZONTAL_AUTO_GAP, 0)
					.set(C.A_GVERTICAL_AUTO_GAP, vgap)
					.set(C.A_GHORIZONTAL_AUTO_COUNT, 0)
					.set(C.A_GVERTICAL_AUTO_COUNT, 0)
					.set(C.A_GHORIZONTAL_AUTO_REFER, 0)
					.set(C.A_GVERTICAL_AUTO_REFER, 0)
					.set(C.AG_SELECTION, "0 0 0 0").set(C.SIZE, pageFormat);
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
		si.setBorder(0, Color.BLACK);
		return si;
	}

	public class ScribusObject {

		protected final XmlBuilder element;
		final private int type;

		protected double w, h;
		private int group;
		protected ScribusPolyBuilder pocoords;

		public ScribusObject(int type) {
			this.type = type;

			element = doc.add(C.EL_PAGEOBJECT).set(C.OWN_PAGE, 0)
					.set(C.PTYPE, type);

			// NEXTITEM/BACKITEM important!
			element.set(C.NEXTITEM, -1).set(C.BACKITEM, -1);

			// druckbar
			element.set(C.PRINTABLE, 1);
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

		public ScribusObject setPosition(double x, double y, double w,
				double h, double angleDegrees) {
			this.w = w;
			this.h = h;

			element.set(C.XPOS, x).set(C.YPOS, y);
			element.set(C.WIDTH, w).set(C.HEIGHT, h);

			// ROT: rotation
			element.set(C.ROT, angleDegrees);

			ScribusPolyBuilder pb;
			if (pocoords != null)
				pb = pocoords;
			else
				pb = getPoly(false);
			if (pb != null) {
				element.set(C.NUMPO, pb.getNumber());
				element.set(C.POCOOR, pb.getCoordsStr());
			}
			pb = getPoly(true);
			if (pb != null) {
				element.set(C.NUMCO, pb.getNumber());
				element.set(C.COCOOR, pb.getCoordsStr());
			}

			return this;
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

		/**
		 * Sets color and width of the border around the object
		 * 
		 * @param width
		 *            0=hairline; >1=width in points
		 * @param color
		 */
		public void setBorder(double width, Color color) {
			element.set(C.PCOLOR2, colorManager.getColorName(color));
			element.set(C.PWIDTH, width);
			element.set(C.SHADE2, 100);
		}

		public void setFill(String color) {
			element.set(C.PCOLOR, color);
			// SHADE2: Deckung Fläche (100=full)
			element.set(C.SHADE, 100);
		}

		public void setFill(Color color) {
			element.set(C.PCOLOR, colorManager.getColorName(color));
			// SHADE2: Deckung Fläche (100=full)
			element.set(C.SHADE, 100);
		}

		public void setTransparency(double transparency) {
			element.set(C.TRANSVALUE, transparency);
		}

		public int getGroup() {
			if (group > 0)
				return group;

			setGroup(++numgroups);
			return group;
		}

		public void setGroup(int group) {
			this.group = group;
			element.set(C.NUMGROUP, 1);
			element.set(C.GROUPS, group);
		}
	}

	public class ScribusShape extends ScribusObject {

		public ScribusShape() {
			super(C.PTYPE_SHAPE);// auch Rechtecke
		}

		public void setGradient(ShapeColor[] colors) {
			// GRSTARTX="0" GRSTARTY="196.08" GRENDX="108.96" GRENDY="196.08"
			element.set("GRSTARTX", 0).set("GRSTARTY", h).set("GRENDX", w)
					.set("GRENDY", h).set("GRTYP", 1);

			// Test: element.set("RATIO", "1").set("SHADE", "70").set("PCOLOR",
			// "None");

			// <CSTOP RAMP="0.640569395017794" NAME="Blue" SHADE="100"
			// TRANS="1"/>
			for (ShapeColor color : colors) {
				element.add(C.CSTOP)
						.set(C.RAMP, color.position)
						.set(C.NAME, colorManager.getColorName(color.color))
						.set(C.SHADE,
								(int) (color.color.getAlpha() / 256d * 100d))
						.set(C.TRANS, 1);
			}
		}
	}

	public class ScribusText extends ScribusObject {

		public ScribusText() {
			super(C.PTYPE_TEXT);
		}

		public XmlBuilder getElement() {
			return element;
		}

		/**
		 * outputs text in its simplest form (without formatting at character
		 * level)
		 * 
		 * @param txt
		 */
		public void setText(String text) {
			for (String line : text.trim().split("\n")) {
				element.add(C.EL_ITEXT).set(C.CH, line);
				element.add(C.EL_PARA).set(C.ALIGN, 1);
			}
		}
	}

	public class ScribusImg extends ScribusObject {
		private final String imagePath;

		public ScribusImg(String imagePath) {
			super(C.PTYPE_IMAGE);

			element.set(C.PICART, 1);
			if (imagePath != null)
				element.set(C.PFILE, imagePath);

			this.imagePath = imagePath;
		}

		public void setCropPct(double cropX, double cropY, double cropW,
				double cropH) throws IOException {
			assert w > 0 && h > 0;

			final double imgW, imgH;
			if (imagePath != null) {
				Image img = new ImageIcon(imagePath).getImage();
				imgW = img.getWidth(null);
				imgH = img.getHeight(null);
				if (imgW <= 0 || imgH <= 0)
					throw new IOException("Image file not found");
			} else {
				throw new RuntimeException(
						"Cropping a non-existing image not possible");
			}

			// SCALETYPE: 0=img-auto-resize, 1=manual
			element.set(C.SCALETYPE, 1);

			double localscx = w / (imgW * cropW);
			double localscy = h / (imgH * cropH);

			// ifolor Designer only allows proportionally scaled images.
			assert localscx / localscy > 0.999d && localscx / localscy < 1.001d;
			log.debug("Scale h/v factor: " + localscx / localscy
					+ "  (1.000 is best)");

			// LOCALSCX: image scale [1/n]
			element.set(C.LOCALSCX, localscx);
			element.set(C.LOCALSCY, localscy);

			// double locx = -(cropX / cropW * w);
			// double locy = -(cropY / cropH * h);
			double locx = -(imgW * cropX);
			double locy = -(imgH * cropY);
			// LOCALX image offset [pt], wird mit LOCALSCX mult.
			element.set(C.LOCALX, locx);
			element.set(C.LOCALY, locy);

			log.debug(String.format(
					"CROP: %f/%f\t%f/%f\t%f %f %f %f   -> %f %f %f %f\n", w, h,
					imgW, imgH, cropX, cropY, cropW, cropH, localscx, localscy,
					locx, locy));
		}

		public ScribusImgFrame addPictureFrame(double x, double y, double w,
				double h, double angleDegrees, double innerWidth,
				double outerWidth) {
			ScribusImgFrame frame = new ScribusImgFrame(innerWidth, outerWidth);
			frame.setPositionCenterRot(x, y, w, h, angleDegrees);
			frame.setGroup(getGroup());
			return frame;
		}

		public ScribusImgScratchFrame addScratchFrame(double x, double y,
				double w, double h, double angleDegrees, double innerWidth,
				double outerWidth) {
			ScribusImgScratchFrame frame = new ScribusImgScratchFrame(
					innerWidth, outerWidth);
			frame.setPositionCenterRot(x, y, w, h, angleDegrees);
			frame.setGroup(getGroup());
			return frame;
		}

		public void setAutoScale(boolean proportional) {
			element.set(C.SCALETYPE, 0);
			element.set(C.RATIO, proportional ? 1 : 0);
		}

		public void setPOCoords(ScribusPolyBuilder pocoords) {
			this.pocoords = pocoords;
		}

	}

	public class ScribusImgFrame extends ScribusShape {
		private final double innerWidth, outerWidth;

		public ScribusImgFrame(double innerWidth, double outerWidth) {
			super();

			element.set(C.FRTYPE, 3);

			this.innerWidth = innerWidth;
			this.outerWidth = outerWidth;
		}

		@Override
		protected ScribusPolyBuilder getPoly(boolean co) {
			double x0 = -outerWidth, y0 = -outerWidth;
			double x1 = innerWidth, y1 = innerWidth;
			double x2 = w - x1, y2 = h - y1;
			double x3 = w - x0, y3 = h - y0;

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

			return pb;

			// .set(C."OwnPage","3")
			// --------------
			// .set(C."RADRECT", 0)
		}
	}

	public class ScribusImgScratchFrame extends ScribusShape {
		private final double innerWidth, outerWidth;

		public ScribusImgScratchFrame(double innerWidth, double outerWidth) {
			super();

			element.set(C.FRTYPE, 3);

			this.innerWidth = innerWidth;
			this.outerWidth = outerWidth;
		}

		@Override
		protected ScribusPolyBuilder getPoly(boolean co) {
			ScribusPolyBuilder pb = new ScratchFrameCoords().get(w, h,
					innerWidth, outerWidth);

			pb.sep();
			pb.add(innerWidth, innerWidth);
			pb.add2(innerWidth, h - innerWidth);
			pb.add2(w - innerWidth, h - innerWidth);
			pb.add2(w - innerWidth, innerWidth);
			pb.add(innerWidth, innerWidth);

			return pb;
		}
	}

	public class ScribusLine extends ScribusObject {

		public ScribusLine() {
			super(C.PTYPE_LINE);
		}
	}

	public ScribusText addText() {
		return new ScribusText();
	}

	public ScribusShape addShape() {
		return new ScribusShape();
	}

}
