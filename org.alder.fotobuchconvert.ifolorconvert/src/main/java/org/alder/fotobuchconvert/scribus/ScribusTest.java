package org.alder.fotobuchconvert.scribus;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;

public class ScribusTest {

	private final PrintStream out;

	public ScribusTest(PrintStream printStream) {
		this.out = printStream;
	}

	public static void main(String[] args) throws IOException {
		FileOutputStream os = null;
		try {
			String name = "/tmp/scribustest.sla";
			System.out.println("Save data into " + name);

			os = new FileOutputStream(name);

			ScribusTest test = new ScribusTest(new PrintStream(os));

			test.writeFile();

		} finally {
			if (os != null)
				os.close();
		}
	}

	private void writeFile() {
		XmlBuilder root = new XmlBuilder("SCRIBUSUTF8NEW").set("Version",
				"1.4.0.rc3");

		XmlBuilder doc = root.add("DOCUMENT").set("ANZPAGES", 3)
				.set("PAGEWIDTH", "595.28").set("PAGEHEIGHT", "841.89")
				.set("BORDERLEFT", "40").set("BORDERRIGHT", "40")
				.set("BORDERTOP", "40").set("BORDERBOTTOM", "40")
				.set("PRESET", "0").set("BleedTop", "20")
				.set("BleedLeft", "20").set("BleedRight", "20")
				.set("BleedBottom", "20").set("ORIENTATION", "0")
				.set("PAGESIZE", "A4").set("FIRSTNUM", "1").set("BOOK", "1")
				.set("UNITS", "0");

		doc.add("COLOR").set("NAME", "Black").set("CMYK", "#000000ff")
				.set("Spot", "0").set("Register", "0");
		doc.add("COLOR").set("NAME", "White").set("CMYK", "#00000000")
				.set("Spot", "0").set("Register", "0");

		String n;
		int left;

		n = "Left";
		left = 1;

		doc.add("MASTERPAGE").set("PAGEXPOS", "100").set("PAGEYPOS", "20")
				.set("PAGEWIDTH", "595.28").set("PAGEHEIGHT", "841.89")
				.set("BORDERLEFT", "40").set("BORDERRIGHT", "40")
				.set("BORDERTOP", "40").set("BORDERBOTTOM", "40")
				.set("NUM", "0").set("NAM", "Normal " + n).set("MNAM", "")
				.set("Size", "A4").set("Orientation", "0").set("LEFT", left)
				.set("PRESET", "0").set("VerticalGuides", "")
				.set("HorizontalGuides", "").set("AGhorizontalAutoGap", "0")
				.set("AGverticalAutoGap", "0")
				.set("AGhorizontalAutoCount", "0")
				.set("AGverticalAutoCount", "0")
				.set("AGhorizontalAutoRefer", "0")
				.set("AGverticalAutoRefer", "0").set("AGSelection", "0 0 0 0");

		n = "Right";
		left = 0;
		doc.add("MASTERPAGE").set("PAGEXPOS", "100").set("PAGEYPOS", "20")
				.set("PAGEWIDTH", "595.28").set("PAGEHEIGHT", "841.89")
				.set("BORDERLEFT", "40").set("BORDERRIGHT", "40")
				.set("BORDERTOP", "40").set("BORDERBOTTOM", "40")
				.set("NUM", "0").set("NAM", "Normal " + n).set("MNAM", "")
				.set("Size", "A4").set("Orientation", "0").set("LEFT", left)
				.set("PRESET", "0").set("VerticalGuides", "")
				.set("HorizontalGuides", "").set("AGhorizontalAutoGap", "0")
				.set("AGverticalAutoGap", "0")
				.set("AGhorizontalAutoCount", "0")
				.set("AGverticalAutoCount", "0")
				.set("AGhorizontalAutoRefer", "0")
				.set("AGverticalAutoRefer", "0").set("AGSelection", "0 0 0 0");

		int pgnum = 0;

		for (int i = 0; i < 2; i++) {
			XmlBuilder page = doc
					.add("PAGE")
					.set("NUM", pgnum++)
					.set("MNAM",
							"Normal " + (pgnum % 2 == 0 ? "Left" : "Right"))
					.set("PAGEXPOS", 50 * i).set("PAGEYPOS", 50 * i)
					.set("PAGEWIDTH", "595.28").set("PAGEHEIGHT", "841.89")
					.set("Size", "A4");
		}

		int w = 200, h = 150;
		XmlBuilder el = doc
				.add("PAGEOBJECT")
				.set("OwnPage", 0)
				.set("PTYPE", "2")
				.set("XPOS", "600")
				// XPOS achtung: 0=links aussen
				.set("YPOS", "0")
				.set("WIDTH", w)
				.set("HEIGHT", h)
				.set("RADRECT", 0)
				.set("FRTYPE", "0")
				.set("CLIPEDIT", "0")
				.set("PWIDTH", "1")
				.set("PCOLOR", "None")
				.set("PCOLOR2", "None")
				.set("COLUMNS", "1")
				.set("COLGAP", "0")
				.set("NAMEDLST", "")
				.set("SHADE", "100")
				.set("SHADE2", "100")
				.set("GRTYP", "0")
				.set("ROT", "1")
				// ROT: rotation
				.set("PLINEART", "1")
				.set("PLINEEND", "0")
				.set("PLINEJOIN", "0")
				.set("LOCALSCX", "1")
				.set("LOCALSCY", "2")
				// LOCALSCX: image scale [1/n]
				.set("LOCALX", "20")
				.set("LOCALY", "10")
				// LOCALX image offset [pt], wird mit LOCALSCX mult.
				.set("PICART", "1")
				.set("PLTSHOW", "0")
				.set("BASEOF", "0")
				.set("textPathType", "0")
				.set("textPathFlipped", "0")
				.set("FLIPPEDH", "0")
				.set("FLIPPEDV", "0")
				.set("SCALETYPE", "1")
				// SCALETYPE: 0=img-auto-resize, 1=manual
				.set("RATIO", "1")
				.set("PRINTABLE", "1")
				.set("ANNOTATION", "0")
				.set("ANNAME", "")
				.set("TEXTFLOWMODE", "0")
				.set("TEXTFLOW", "0")
				.set("TEXTFLOW2", "0")
				.set("TEXTFLOW3", "0")
				.set("AUTOTEXT", "0")
				.set("EXTRA", "0")
				.set("TEXTRA", "0")
				.set("BEXTRA", "0")
				.set("REXTRA", "0")
				.set("FLOP", "0")
				.set("PFILE",
						"/media/reverseengineer_ifolor/xml/Australia/files/Australia2-20120409 Data/preview/f804f95403e1414caa14c8091da326d7")
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
								+ " 0 0 0 0 ")

				// unbekannt, bleibt auch in Vorlage konstant:
				// .set("NUMCO", "16")
				// .set("COCOOR",
				// "0 0 0 0 " + w + " 0 " + w + " 0 " + w + " 0 " + w
				// + " 0 " + w + " " + h + " " + w + " " + h + " "
				// + w + " " + h + " " + w + " " + h + " 0 " + h
				// + " 0 " + h + " 0 " + h + " 0 " + h
				// + " 0 0 0 0 ")

				.set("NUMGROUP", "0").set("GROUPS", "")
				.set("startArrowIndex", "0").set("endArrowIndex", "0")
				.set("OnMasterPage", "").set("ImageClip", "")
				.set("ImageRes", "1").set("Pagenumber", "0")
				.set("isInline", "0").set("fillRule", "1")
				.set("doOverprint", "0").set("gXpos", "0").set("gYpos", "0")
				.set("gWidth", "0").set("gHeight", "0").set("LAYER", "0")
				.set("BOOKMARK", "0").set("NEXTITEM", "-1")
				.set("BACKITEM", "-1");

		el.add("trail");
		el.add("PageItemAttributes");

		root.output(out, 0);
	}
}

class XmlBuilder {

	private final Vector<XmlBuilder> subElements = new Vector<XmlBuilder>();
	private final String name;
	private final HashMap<String, String> attributes = new HashMap<String, String>();

	public XmlBuilder(String name) {
		this.name = name;
	}

	public void output(PrintStream out, int indent) {
		if (indent == 0)
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		for (int i = 0; i < indent; i++)
			out.print("  ");
		out.print("<" + name);
		for (Entry<String, String> kv : attributes.entrySet())
			out.print(" " + kv.getKey() + "=\"" + kv.getValue() + "\"");
		if (subElements.isEmpty())
			out.println("/>");
		else {
			out.println(">");
			for (XmlBuilder el : subElements)
				el.output(out, indent + 1);
			for (int i = 0; i < indent; i++)
				out.print("  ");
			out.println("</" + name + ">");
		}
	}

	public XmlBuilder set(String key, int value) {
		attributes.put(key, String.valueOf(value));
		return this;
	}

	public XmlBuilder set(String key, String value) {
		attributes.put(key, value);
		return this;
	}

	public XmlBuilder add(String name) {
		XmlBuilder e = new XmlBuilder(name);
		subElements.add(e);
		return e;
	}

}
