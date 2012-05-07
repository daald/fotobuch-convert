package org.alder.fotobuchconvert.scribus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import org.alder.fotobuchconvert.ifolorconvert.TestData;

public class ScribusPrototype {

	private final PrintStream out;

	public static void main(String[] args) throws IOException {
		FileOutputStream os = null;
		try {
			File file = TestData.getTestOutputPath();
			System.out.println("Save data into " + file + "    " + new Date());

			os = new FileOutputStream(file);

			ScribusPrototype test = new ScribusPrototype(new PrintStream(os));

			test.writeFile();

		} finally {
			if (os != null)
				os.close();
		}
	}

	public ScribusPrototype(PrintStream printStream) {
		this.out = printStream;
	}

	int pageW = 500;// A4=595.28;
	int pageH = 500;// A4=841.89;
	String pageFormat = "Custom";// A4
	int pagePosX = 100;
	int pagePosY = 20;
	int margin = 20;
	int bleed = 20;

	public class PageDims {
		private int docbaseX, docbaseY;

		public PageDims(int num) {
			// docbase bezeichnet die sichtbare Ecke (roter Rand)
			docbaseX = pagePosX + ((num + 1) % 2) * pageW;// - 1;
			docbaseY = pagePosY + (int) ((num + 1) / 2) * pageH;
		}
	}

	private void writeFile() {
		XmlBuilder root = new XmlBuilder("SCRIBUSUTF8NEW").set("Version",
				"1.4.0.rc3");

		XmlBuilder doc = root.add("DOCUMENT")
				.set("ANZPAGES", 3)
				.set("FIRSTNUM", "1")
				.set("BOOK", "1")
				// UNITS pt=0 cm=4
				.set("UNITS", "0").set("BleedTop", bleed)
				.set("BleedLeft", bleed).set("BleedRight", bleed)
				.set("BleedBottom", bleed).set("ORIENTATION", "0")
				.set("PAGESIZE", pageFormat);
		applyPageSettings(doc, true);

		doc.add("COLOR").set("NAME", "Black").set("CMYK", "#000000ff")
				.set("Spot", "0").set("Register", "0");
		doc.add("COLOR").set("NAME", "White").set("CMYK", "#00000000")
				.set("Spot", "0").set("Register", "0");

		// seems to be necessary for scribus 1.4.0
		doc.add("LAYERS").set("NUMMER", "0").set("SICHTBAR", "1")
				.set("DRUCKEN", "1").set("EDIT", "1").set("FLOW", "1")
				.set("TRANS", "1");

		for (int pg = 0; pg < 2; pg++) {
			XmlBuilder pagemaster;
			pagemaster = doc.add("MASTERPAGE")
					.set("NAM", "Normal " + (pg == 0 ? "Right" : "Left"))
					.set("NUM", pg).set("LEFT", (pg % 2))
					.set("PAGEXPOS", pagePosX).set("PAGEYPOS", pagePosY);
			applyPageSettings(pagemaster, false);
		}

		PageDims[] pageDims = new PageDims[3];
		for (int pg = 0; pg < pageDims.length; pg++) {
			XmlBuilder page;
			pageDims[pg] = new PageDims(pg);

			page = doc.add("PAGE")
					.set("MNAM", "Normal " + (pg % 2 == 0 ? "Right" : "Left"))
					.set("NUM", pg).set("LEFT", (pg % 2))
					.set("PAGEXPOS", pageDims[pg].docbaseX)
					.set("PAGEYPOS", pageDims[pg].docbaseY);
			applyPageSettings(page, false);
		}

		int w = pageW / 2, h = 150;
		// IMAGE
		XmlBuilder el = doc
				.add("PAGEOBJECT")
				.set("OwnPage", 0)
				.set("PTYPE", "2")

				// NEXTITEM/BACKITEM important!
				.set("NEXTITEM", "-1")
				.set("BACKITEM", "-1")

				.set("XPOS", "665.28")
				// XPOS achtung: 0=links aussen
				.set("YPOS", "0")
				.set("WIDTH", w)
				.set("HEIGHT", h)
				.set("ROT", "1")
				// ROT: rotation
				.set("LOCALSCX", "1")
				.set("LOCALSCY", "2")
				// LOCALSCX: image scale [1/n]
				.set("LOCALX", "20")
				.set("LOCALY", "10")
				// LOCALX image offset [pt], wird mit LOCALSCX mult.

				.set("PICART", "1")

				.set("SCALETYPE", "1")
				// SCALETYPE: 0=img-auto-resize, 1=manual

				.set("PFILE",
						"/media/reverseengineer_ifolor/xml/Australia/files/Australia2-20120409 Data/preview/f804f95403e1414caa14c8091da326d7")

				// cropping-shape NUMPO:
				.set("NUMPO", "16")
				.set("POCOOR",
						"0 0 0 0 " + w + " 0 " + w + " 0 " + w + " 0 " + w
								+ " 0 " + w + " " + h + " " + w + " " + h + " "
								+ w + " " + h + " " + w + " " + h + " 0 " + h
								+ " 0 " + h + " 0 " + h + " 0 " + h
								+ " 0 0 0 0 ");

		// .set("RADRECT", 0) .set("FRTYPE", "0")
		// .set("CLIPEDIT", "0") .set("PWIDTH", "1") .set("PCOLOR", "None")
		// .set("PCOLOR2", "None") .set("COLUMNS", "1")
		// .set("COLGAP", "0") .set("NAMEDLST", "") .set("SHADE", "100")
		// .set("SHADE2", "100") .set("GRTYP", "0")
		// .set("PLINEART", "1") .set("PLINEEND", "0") .set("PLINEJOIN", "0")
		// .set("PLTSHOW", "0") .set("BASEOF", "0")
		// .set("textPathType", "0") .set("textPathFlipped", "0")
		// .set("FLIPPEDH", "0") .set("FLIPPEDV", "0")
		// .set("RATIO", "1") .set("PRINTABLE", "1") .set("ANNOTATION", "0")
		// .set("ANNAME", "") .set("TEXTFLOWMODE", "0")
		// .set("TEXTFLOW", "0") .set("TEXTFLOW2", "0") .set("TEXTFLOW3", "0")
		// .set("AUTOTEXT", "0") .set("EXTRA", "0")
		// .set("TEXTRA", "0") .set("BEXTRA", "0") .set("REXTRA", "0")
		// .set("FLOP", "0")
		// .set("PFILE2", "") .set("PFILE3", "") .set("PRFILE", "")
		// .set("EPROF", "") .set("IRENDER", "0") .set("EMBEDDED", "0")
		// .set("LOCK", "0") .set("LOCKR", "0") .set("REVERS", "0")
		// .set("TransValue", "0") .set("TransValueS", "0") .set("TransBlend",
		// "0")
		// .set("TransBlendS", "0") .set("isTableItem", "0") .set("TopLine",
		// "0") .set("LeftLine", "0") .set("RightLine", "0") .set("BottomLine",
		// "0")
		// .set("isGroupControl", "0") .set("NUMDASH", "0") .set("DASHS", "")
		// .set("DASHOFF", "0")
		// unbekannt, bleibt auch in Vorlage konstant:
		// .set("NUMCO", "16")
		// .set("COCOOR",
		// "0 0 0 0 " + w + " 0 " + w + " 0 " + w + " 0 " + w
		// + " 0 " + w + " " + h + " " + w + " " + h + " "
		// + w + " " + h + " " + w + " " + h + " 0 " + h
		// + " 0 " + h + " 0 " + h + " 0 " + h
		// + " 0 0 0 0 ")

		// .set("NUMGROUP", "0")
		// .set("GROUPS", "")
		// .set("startArrowIndex", "0")
		// .set("endArrowIndex", "0")
		// .set("OnMasterPage", "")
		// .set("ImageClip", "")
		// .set("ImageRes", "1")
		// .set("Pagenumber", "0")
		// .set("isInline", "0")
		// .set("fillRule", "1")
		// .set("doOverprint", "0")
		// .set("gXpos", "0")
		// .set("gYpos", "0")
		// .set("gWidth", "0")
		// .set("gHeight", "0")
		// .set("LAYER", "0")
		// .set("BOOKMARK", "0")
		;

		// DEMO: Rotation
		// SHAPE(6)
		makeRect(doc, 800, 300, 100, 100, 0);
		makeRect(doc, 800, 300, 100, 100, 5);
		makeRect(doc, 800, 300, 100, 100, 10);

		/**
		 * PAGEXPOS="100" PAGEYPOS="20" PAGEWIDTH="595.28" PAGEHEIGHT="841.89"
		 */

		for (int pg = 0; pg < 3; pg++) {
			PageDims pd = pageDims[pg];
			// DEMO: Seitenrand
			// liegt auf Seitenecke:
			makeRect(doc, pd.docbaseX - bleed, pd.docbaseY - bleed, 1, 1, 0);
			// liegt auf roter Ecke:
			makeRect(doc, pd.docbaseX, pd.docbaseY, 1, 1, 0);
			// liegt auf margin (blaue Ecke):
			makeRect(doc, pd.docbaseX + margin, pd.docbaseY + margin, 1, 1, 0);
		}

		PageDims pd = pageDims[0];
		// DEMO: Anker ist die linke obere Ecke
		makeRect(doc, pd.docbaseX + 100, pd.docbaseY + 100, 1, 1, 0);
		makeRect(doc, pd.docbaseX + 100, pd.docbaseY + 100, 10, 10, 0);
		makeRect(doc, pd.docbaseX + 100, pd.docbaseY + 100, 100, 100, 0);

		root.output(out, 0);
	}

	private void applyPageSettings(XmlBuilder doc, boolean isDoc) {
		doc.set("PAGEWIDTH", pageW).set("PAGEHEIGHT", pageH)
				.set("BORDERLEFT", margin).set("BORDERRIGHT", margin)
				.set("BORDERTOP", margin).set("BORDERBOTTOM", margin)
				.set("PRESET", "0");

		if (isDoc)
			doc.set("GapHorizontal", "0").set("GapVertical", "0");
		else
			doc.set("VerticalGuides", "").set("HorizontalGuides", "")
					.set("AGhorizontalAutoGap", "0")
					.set("AGverticalAutoGap", "0")
					.set("AGhorizontalAutoCount", "0")
					.set("AGverticalAutoCount", "0")
					.set("AGhorizontalAutoRefer", "0")
					.set("AGverticalAutoRefer", "0")
					.set("AGSelection", "0 0 0 0").set("Size", pageFormat);
	}

	private void makeRect(XmlBuilder doc, int x, int y, int w, int h, int rot) {
		XmlBuilder el;
		el = doc.add("PAGEOBJECT")
				.set("OwnPage", 0)
				.set("PTYPE", "6")

				// NEXTITEM/BACKITEM important!
				.set("NEXTITEM", "-1")
				.set("BACKITEM", "-1")

				.set("XPOS", x)
				// XPOS achtung: 0=links aussen
				.set("YPOS", y)
				.set("WIDTH", w)
				.set("HEIGHT", h)
				// .set("RADRECT", 0)
				// .set("FRTYPE", "0")
				// .set("CLIPEDIT", "0")
				// .set("PWIDTH", "1")
				// .set("PCOLOR", "None")
				// PCOLOR2: Linienfarbe
				.set("PCOLOR2", "Black")
				// .set("COLUMNS", "1")
				// .set("COLGAP", "0")
				// .set("NAMEDLST", "")
				// .set("SHADE", "100")
				// SHADE2: Deckung Linie (100=full)
				.set("SHADE2", "100")
				// .set("GRTYP", "0")
				.set("ROT", rot)
				// ROT: rotation
				// .set("PLINEART", "1")
				// .set("PLINEEND", "0")
				// .set("PLINEJOIN", "0")
				.set("LOCALSCX", "1")
				.set("LOCALSCY", "2")
				// LOCALSCX: image scale [1/n]
				.set("LOCALX", "20")
				.set("LOCALY", "10")
				// LOCALX image offset [pt], wird mit LOCALSCX mult.

				.set("PICART", "1")
				// .set("PLTSHOW", "0")
				// .set("BASEOF", "0")
				// .set("textPathType", "0")
				// .set("textPathFlipped", "0")
				// .set("FLIPPEDH", "0")
				// .set("FLIPPEDV", "0")

				.set("SCALETYPE", "1")
				// SCALETYPE: 0=img-auto-resize, 1=manual
				// .set("RATIO", "1")
				// .set("PRINTABLE", "1")
				// .set("ANNOTATION", "0")
				// .set("ANNAME", "")
				// .set("TEXTFLOWMODE", "0")
				// .set("TEXTFLOW", "0")
				// .set("TEXTFLOW2", "0")
				// .set("TEXTFLOW3", "0")
				// .set("AUTOTEXT", "0")
				// .set("EXTRA", "0")
				// .set("TEXTRA", "0")
				// .set("BEXTRA", "0")
				// .set("REXTRA", "0")
				// .set("FLOP", "0")
				// .set("PFILE2", "")
				// .set("PFILE3", "")
				// .set("PRFILE", "")
				// .set("EPROF", "")
				// .set("IRENDER", "0")
				// .set("EMBEDDED", "0")
				// .set("LOCK", "0")
				// .set("LOCKR", "0")
				// .set("REVERS", "0")
				// .set("TransValue", "0")
				// .set("TransValueS", "0")
				// .set("TransBlend", "0")
				// .set("TransBlendS", "0")
				// .set("isTableItem", "0")
				// .set("TopLine", "0")
				// .set("LeftLine", "0")
				// .set("RightLine", "0")
				// .set("BottomLine", "0")
				// .set("isGroupControl", "0")
				// .set("NUMDASH", "0")
				// .set("DASHS", "")
				// .set("DASHOFF", "0")

				// cropping-shape NUMPO:
				.set("NUMPO", "16")
				.set("POCOOR",
						"0 0 0 0 " + w + " 0 " + w + " 0 " + w + " 0 " + w
								+ " 0 " + w + " " + h + " " + w + " " + h + " "
								+ w + " " + h + " " + w + " " + h + " 0 " + h
								+ " 0 " + h + " 0 " + h + " 0 " + h
								+ " 0 0 0 0 ");

		// unbekannt, bleibt auch in Vorlage konstant (contour box?):
		// .set("NUMCO", "16")
		// .set("COCOOR",
		// "0 0 0 0 " + w + " 0 " + w + " 0 " + w + " 0 " + w
		// + " 0 " + w + " " + h + " " + w + " " + h + " "
		// + w + " " + h + " " + w + " " + h + " 0 " + h
		// + " 0 " + h + " 0 " + h + " 0 " + h
		// + " 0 0 0 0 ")

		// .set("startArrowIndex", "0")
		// .set("endArrowIndex", "0")
		// .set("OnMasterPage", "")
		// .set("ImageClip", "")
		// .set("ImageRes", "1")
		// .set("Pagenumber", "0")
		// .set("isInline", "0")
		// .set("fillRule", "1")
		// .set("doOverprint", "0")
		// .set("LAYER", "0")
		// .set("BOOKMARK", "0")

		// GROUPING:
		// .set("NUMGROUP", "0")
		// .set("GROUPS", "")
		// .set("gXpos", "0")
		// .set("gYpos", "0")
		// .set("gWidth", "0")
		// .set("gHeight", "0")

		// el.add("trail");
		// el.add("PageItemAttributes");
	}
}