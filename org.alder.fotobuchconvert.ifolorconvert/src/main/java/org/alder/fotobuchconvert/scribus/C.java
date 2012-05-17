package org.alder.fotobuchconvert.scribus;

public class C {

	/**
	 * XML Elements
	 */
	public static final String EL_SCRIBUSUTF8NEW = "SCRIBUSUTF8NEW";
	public static final String EL_DOCUMENT = "DOCUMENT";
	public static final String EL_LAYERS = "LAYERS";
	public static final String EL_COLOR = "COLOR";
	public static final String EL_MASTERPAGE = "MASTERPAGE";
	public static final String EL_PAGE = "PAGE";
	public static final String EL_PAGEOBJECT = "PAGEOBJECT";

	/**
	 * XML Values
	 */
	public static final int PTYPE_IMAGE = 2;
	public static final int PTYPE_TEXT = 4;
	public static final int PTYPE_LINE = 5;
	public static final int PTYPE_SHAPE = 6;

	/**
	 * XML Attributes
	 */

	public static final String RATIO = "RATIO";// 1=proportional auto-scale
	public static final String SCALETYPE = "SCALETYPE";// 0=img-auto-scale
	public static final String LOCALX = "LOCALX";
	public static final String LOCALY = "LOCALY";
	public static final String LOCALSCX = "LOCALSCX";
	public static final String LOCALSCY = "LOCALSCY";

	public static final String PTYPE = "PTYPE";

	public static final String PICART = "PICART";// 1=shape uses an image file
	public static final String PFILE = "PFILE";

	public static final String ROT = "ROT";
	public static final String XPOS = "XPOS";
	public static final String YPOS = "YPOS";
	public static final String WIDTH = "WIDTH";
	public static final String HEIGHT = "HEIGHT";

	public static final String GROUPS = "GROUPS";
	public static final String NUMGROUP = "NUMGROUP";

	public static final String BACKITEM = "BACKITEM";
	public static final String NEXTITEM = "NEXTITEM";

	public static final String OWN_PAGE = "OwnPage";

	public static final String PWIDTH = "PWIDTH";

	public static final String SIZE = "Size"; // e.g. A4
	public static final String PAGESIZE = "PAGESIZE";
	public static final String ORIENTATION = "ORIENTATION"; // page orientation

	public static final String LEFT = "LEFT";// 1=page is on left side (new
	// line)

	public static final String AG_SELECTION = "AGSelection";
	public static final String A_GHORIZONTAL_AUTO_GAP = "AGhorizontalAutoGap";
	public static final String A_GVERTICAL_AUTO_GAP = "AGverticalAutoGap";
	public static final String A_GHORIZONTAL_AUTO_COUNT = "AGhorizontalAutoCount";
	public static final String A_GVERTICAL_AUTO_COUNT = "AGverticalAutoCount";
	public static final String A_GHORIZONTAL_AUTO_REFER = "AGhorizontalAutoRefer";
	public static final String A_GVERTICAL_AUTO_REFER = "AGverticalAutoRefer";

	public static final String HORIZONTAL_GUIDES = "HorizontalGuides";
	public static final String VERTICAL_GUIDES = "VerticalGuides";

	public static final String GAP_HORIZONTAL = "GapHorizontal";
	public static final String GAP_VERTICAL = "GapVertical";

	public static final String PRESET = "PRESET";

	public static final String BORDERLEFT = "BORDERLEFT";
	public static final String BORDERRIGHT = "BORDERRIGHT";
	public static final String BORDERTOP = "BORDERTOP";
	public static final String BORDERBOTTOM = "BORDERBOTTOM";

	public static final String PAGEWIDTH = "PAGEWIDTH";
	public static final String PAGEHEIGHT = "PAGEHEIGHT";

	public static final String PAGEXPOS = "PAGEXPOS";
	public static final String PAGEYPOS = "PAGEYPOS";

	// Page / object number
	public static final String NUM = "NUM";

	// Page / Master Page names
	public static final String MNAM = "MNAM";
	public static final String NAM = "NAM";

	public static final String TRANS = "TRANS";

	public static final String FLOW = "FLOW";

	// protection flags
	public static final String PRINTABLE = "PRINTABLE";
	public static final String EDIT = "EDIT";
	public static final String DRUCKEN = "DRUCKEN";
	public static final String SICHTBAR = "SICHTBAR";

	public static final String NUMMER = "NUMMER";

	// color table
	public static final String NAME = "NAME";
	public static final String RGB = "RGB";
	public static final String CMYK = "CMYK";
	public static final String SPOT = "Spot";
	public static final String REGISTER = "Register";

	public static final String BLEED_LEFT = "BleedLeft";
	public static final String BLEED_RIGHT = "BleedRight";
	public static final String BLEED_TOP = "BleedTop";
	public static final String BLEED_BOTTOM = "BleedBottom";

	public static final String UNITS = "UNITS";// UNITS pt=0 cm=4

	public static final String BOOK = "BOOK";

	public static final String FIRSTNUM = "FIRSTNUM";

	public static final String ANZPAGES = "ANZPAGES";

	public static final String VERSION = "Version";

	public static final String FRTYPE = "FRTYPE";// Resize complex shapes

	public static final String NUMCO = "NUMCO";// number of COCOOR pairs
	public static final String COCOOR = "COCOOR";
	public static final String NUMPO = "NUMPO";// number of POCOOR pairs
	public static final String POCOOR = "POCOOR";

	public static final String SHADE = "SHADE";// fill opacity
	public static final String PCOLOR = "PCOLOR";// fill color
	public static final String SHADE2 = "SHADE2";// border opacity
	public static final String PCOLOR2 = "PCOLOR2";// border color

	public static final String TRANSVALUE = "TransValue";// transparency(0..1)

	/**
	 * CSTOP (gradient) specific
	 */
	// public static final String TRANS = "TRANS";
	// public static final String SHADE = "SHADE";
	// public static final String NAME = "NAME";
	public static final String RAMP = "RAMP";
	public static final String CSTOP = "CSTOP";

	/**
	 * Text specific
	 */
	public static final String EL_ITEXT = "ITEXT";
	public static final String EL_PARA = "para";

	public static final String ALIGN = "ALIGN";
	public static final String FCOLOR = "FCOLOR";
	public static final String FONTSIZE = "FONTSIZE";
	public static final String FONT = "FONT";

}
