package org.alder.fotobuchconvert.ifolor;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.alder.fotobuchconvert.objects.Book;
import org.alder.fotobuchconvert.objects.BookPage;
import org.alder.fotobuchconvert.objects.BookPicture;
import org.alder.fotobuchconvert.objects.BookRtfText;
import org.alder.fotobuchconvert.objects.BookShape;
import org.alder.fotobuchconvert.objects.BookShape.ShapeColor;
import org.alder.fotobuchconvert.objects.Border;
import org.alder.fotobuchconvert.objects.PageNumberElement;
import org.alder.fotobuchconvert.objects.Shadow;
import org.alder.fotobuchconvert.scribus.ImageCutCoords;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

public class IfolorLoader {

	private final Log log = LogFactory.getLog(IfolorLoader.class);

	public Book load(ProjectPath path) throws Exception {
		log.info("Loading Layout for " + path);
		log.debug("File: " + path.projectFile.getAbsolutePath());
		log.debug("Exists: " + path.projectFile.exists());

		/******
		 * initialization
		 */

		VTDGen vg = new VTDGen();
		AutoPilot apPg = new AutoPilot();
		AutoPilot apPgLeftEnabled = new AutoPilot();
		AutoPilot apPgRightEnabled = new AutoPilot();
		AutoPilot apPgLeftPageNumberLabel = new AutoPilot();
		AutoPilot apPgRightPageNumberLabel = new AutoPilot();

		AutoPilot apPgLeftPageNumberLeft = new AutoPilot();
		AutoPilot apPgLeftPageNumberTop = new AutoPilot();
		AutoPilot apPgLeftPageNumberWidth = new AutoPilot();
		AutoPilot apPgLeftPageNumberHeight = new AutoPilot();
		AutoPilot apPgLeftPageNumberVisible = new AutoPilot();
		AutoPilot apPgLeftPageNumberValign = new AutoPilot();
		AutoPilot apPgLeftPageNumberFontName = new AutoPilot();
		AutoPilot apPgLeftPageNumberFontSize = new AutoPilot();
		AutoPilot apPgLeftPageNumberBold = new AutoPilot();
		AutoPilot apPgLeftPageNumberItalic = new AutoPilot();
		AutoPilot apPgLeftPageNumberUnderline = new AutoPilot();
		AutoPilot apPgLeftPageNumberColor = new AutoPilot();
		AutoPilot apPgRightPageNumberLeft = new AutoPilot();
		AutoPilot apPgRightPageNumberTop = new AutoPilot();
		AutoPilot apPgRightPageNumberWidth = new AutoPilot();
		AutoPilot apPgRightPageNumberHeight = new AutoPilot();
		AutoPilot apPgRightPageNumberVisible = new AutoPilot();
		AutoPilot apPgRightPageNumberValign = new AutoPilot();
		AutoPilot apPgRightPageNumberFontName = new AutoPilot();
		AutoPilot apPgRightPageNumberFontSize = new AutoPilot();
		AutoPilot apPgRightPageNumberBold = new AutoPilot();
		AutoPilot apPgRightPageNumberItalic = new AutoPilot();
		AutoPilot apPgRightPageNumberUnderline = new AutoPilot();
		AutoPilot apPgRightPageNumberColor = new AutoPilot();

		AutoPilot apGo = new AutoPilot();
		AutoPilot apIm = new AutoPilot();
		AutoPilot apImQuality = new AutoPilot();
		AutoPilot apImEnhancement = new AutoPilot();
		AutoPilot apImModified = new AutoPilot();
		AutoPilot apImId = new AutoPilot();
		AutoPilot apImLeft = new AutoPilot();
		AutoPilot apImTop = new AutoPilot();
		AutoPilot apImWidth = new AutoPilot();
		AutoPilot apImHeight = new AutoPilot();
		AutoPilot apImRotateAngle = new AutoPilot();
		AutoPilot apImApplyOffset = new AutoPilot();
		AutoPilot apImEditable = new AutoPilot();
		AutoPilot apImDragable = new AutoPilot();
		AutoPilot apImDock = new AutoPilot();
		AutoPilot apImDesigner = new AutoPilot();
		AutoPilot apImOrigFilePath = new AutoPilot();
		AutoPilot apImPreviewFilePath = new AutoPilot();
		AutoPilot apImSourceFilePath = new AutoPilot();
		AutoPilot apImVpLeft = new AutoPilot();
		AutoPilot apImVpTop = new AutoPilot();
		AutoPilot apImVpWidth = new AutoPilot();
		AutoPilot apImVpHeight = new AutoPilot();
		AutoPilot apImBorderType = new AutoPilot();
		AutoPilot apImAlphaType = new AutoPilot();
		AutoPilot apShapeColor = new AutoPilot();
		AutoPilot apShapeColorValue = new AutoPilot();
		AutoPilot apShapeColorPos = new AutoPilot();

		apPg.selectXPath("/UserProject/Pages/ProjectPage");
		apPgLeftEnabled.selectXPath("LeftPageEnabled");// <LeftPageEnabled>False</LeftPageEnabled>
		apPgRightEnabled.selectXPath("RightPageEnabled");// <RightPageEnabled>True</RightPageEnabled>

		apPgLeftPageNumberLeft.selectXPath("LeftPageNumber/@left");
		apPgLeftPageNumberTop.selectXPath("LeftPageNumber/@top");
		apPgLeftPageNumberWidth.selectXPath("LeftPageNumber/@width");
		apPgLeftPageNumberHeight.selectXPath("LeftPageNumber/@height");
		apPgLeftPageNumberVisible.selectXPath("LeftPageNumber/@visible");
		apPgLeftPageNumberValign.selectXPath("LeftPageNumber/@valign");
		apPgLeftPageNumberFontName.selectXPath("LeftPageNumber/@fontName");
		apPgLeftPageNumberFontSize.selectXPath("LeftPageNumber/@fontSize");
		apPgLeftPageNumberBold.selectXPath("LeftPageNumber/@bold");
		apPgLeftPageNumberItalic.selectXPath("LeftPageNumber/@italic");
		apPgLeftPageNumberUnderline.selectXPath("LeftPageNumber/@underline");
		apPgLeftPageNumberColor.selectXPath("LeftPageNumber/@color");
		apPgRightPageNumberLeft.selectXPath("RightPageNumber/@left");
		apPgRightPageNumberTop.selectXPath("RightPageNumber/@top");
		apPgRightPageNumberWidth.selectXPath("RightPageNumber/@width");
		apPgRightPageNumberHeight.selectXPath("RightPageNumber/@height");
		apPgRightPageNumberVisible.selectXPath("RightPageNumber/@visible");
		apPgRightPageNumberValign.selectXPath("RightPageNumber/@valign");
		apPgRightPageNumberFontName.selectXPath("RightPageNumber/@fontName");
		apPgRightPageNumberFontSize.selectXPath("RightPageNumber/@fontSize");
		apPgRightPageNumberBold.selectXPath("RightPageNumber/@bold");
		apPgRightPageNumberItalic.selectXPath("RightPageNumber/@italic");
		apPgRightPageNumberUnderline.selectXPath("RightPageNumber/@underline");
		apPgRightPageNumberColor.selectXPath("RightPageNumber/@color");
		// <RightPageNumber left="3631" top="2304" width="3331"
		// height="200" visible="1" align="outer" valign="bottom"
		// fontName="Arial"
		// fontSize="10" bold="0" italic="0" underline="0" color="Black" />

		apGo.selectXPath("GuiObjects/*"); // Image, ColorRectangle, Text
		apIm.selectXPath("GuiObjects/Image");
		apImQuality.selectXPath("@quality");// ="Good"
		apImEnhancement.selectXPath("@enhancement");// ="1"
		apImModified.selectXPath("@modified");// ="0"
		apImId.selectXPath("@id");// ="I01"
		apImLeft.selectXPath("@left");// ="3531"
		apImTop.selectXPath("@top");// ="0"
		apImWidth.selectXPath("@width");// ="3537"
		apImHeight.selectXPath("@height");// ="2525"
		apImRotateAngle.selectXPath("@rotateAngle");// ="0"
		apImApplyOffset.selectXPath("@applyOffset");// ="0"
		apImEditable.selectXPath("@editable");// ="1"
		apImDragable.selectXPath("@dragable");// ="1"
		// apImDock.selectXPath("@dock");// ="middle", (only "top" on cover
		// pages)
		apImDesigner.selectXPath("@designer");// ="0"
		apImOrigFilePath.selectXPath("OrigFilePath");// <OrigFilePath><![CDATA[data\9ac23b2ee4f146678aaed92ab627d786]]
		apImPreviewFilePath.selectXPath("PreviewFilePath");// <OrigFilePath><![CDATA[data\9ac23b2ee4f146678aaed92ab627d786]]
		apImSourceFilePath.selectXPath("SourceFilePath");// <OrigFilePath><![CDATA[data\9ac23b2ee4f146678aaed92ab627d786]]
		apImVpLeft.selectXPath("VisiblePart/@left");// left="0"
		apImVpTop.selectXPath("VisiblePart/@top");// top="2.333334"
		apImVpWidth.selectXPath("VisiblePart/@width");// width="100"
		apImVpHeight.selectXPath("VisiblePart/@height");// height="90.66666"
		apImBorderType.selectXPath("Border/@id");// <Border id="Elegant" />
		apImAlphaType.selectXPath("AlphaSet/@id"); // <AlphaSet
													// id="[ALPHAMASK01]Alpha10"
													// />

		// <Colors><Color value="7F000000" position="0" /></Colors>
		apShapeColor.selectXPath("Colors/Color");
		apShapeColorValue.selectXPath("@value");
		apShapeColorPos.selectXPath("@position");

		/******
		 * now load the file
		 */
		loadFile(vg, path.projectFile);
		vg.parse(false);

		/******
		 * more initialization
		 */
		VTDNav vn = vg.getNav();
		apPg.bind(vn);
		apPgLeftEnabled.bind(vn);
		apPgRightEnabled.bind(vn);
		apPgLeftPageNumberLabel.bind(vn);
		apPgRightPageNumberLabel.bind(vn);

		apPgLeftPageNumberLeft.bind(vn);
		apPgLeftPageNumberTop.bind(vn);
		apPgLeftPageNumberWidth.bind(vn);
		apPgLeftPageNumberHeight.bind(vn);
		apPgLeftPageNumberVisible.bind(vn);
		apPgLeftPageNumberValign.bind(vn);
		apPgLeftPageNumberFontName.bind(vn);
		apPgLeftPageNumberFontSize.bind(vn);
		apPgLeftPageNumberBold.bind(vn);
		apPgLeftPageNumberItalic.bind(vn);
		apPgLeftPageNumberUnderline.bind(vn);
		apPgLeftPageNumberColor.bind(vn);
		apPgRightPageNumberLeft.bind(vn);
		apPgRightPageNumberTop.bind(vn);
		apPgRightPageNumberWidth.bind(vn);
		apPgRightPageNumberHeight.bind(vn);
		apPgRightPageNumberVisible.bind(vn);
		apPgRightPageNumberValign.bind(vn);
		apPgRightPageNumberFontName.bind(vn);
		apPgRightPageNumberFontSize.bind(vn);
		apPgRightPageNumberBold.bind(vn);
		apPgRightPageNumberItalic.bind(vn);
		apPgRightPageNumberUnderline.bind(vn);
		apPgRightPageNumberColor.bind(vn);

		apGo.bind(vn);
		apImQuality.bind(vn);
		apImModified.bind(vn);
		apImId.bind(vn);
		apImLeft.bind(vn);
		apImTop.bind(vn);
		apImWidth.bind(vn);
		apImHeight.bind(vn);
		apImRotateAngle.bind(vn);
		apImApplyOffset.bind(vn);
		apImEditable.bind(vn);
		apImDragable.bind(vn);
		apImDock.bind(vn);
		apImDesigner.bind(vn);
		apImOrigFilePath.bind(vn);
		apImPreviewFilePath.bind(vn);
		apImSourceFilePath.bind(vn);
		apImVpLeft.bind(vn);
		apImVpTop.bind(vn);
		apImVpWidth.bind(vn);
		apImVpHeight.bind(vn);
		apImBorderType.bind(vn);
		apImAlphaType.bind(vn);
		apShapeColor.bind(vn);
		apShapeColorValue.bind(vn);
		apShapeColorPos.bind(vn);

		/******
		 * now read the file contents
		 */
		Book book = new Book(path);
		log.info("Loading Ifolor file");
		apPg.resetXPath();
		int pgnum = -2;
		while (apPg.evalXPath() != -1) {
			pgnum += 2;
			log.debug("Loading:   pages " + pgnum + "+" + (pgnum + 1));

			final boolean leftPageEnabled = "True".equals(apPgLeftEnabled
					.evalXPathToString());
			final boolean rightPageEnabled = "True".equals(apPgRightEnabled
					.evalXPathToString());

			BookPage page = new BookPage(pgnum, leftPageEnabled,
					rightPageEnabled);
			book.add(page);

			apGo.resetXPath();
			while (apGo.evalXPath() != -1) {
				String type = vn.toString(vn.getCurrentIndex());
				log.debug("Loading:     element:" + type);

				int left = atoi(apImLeft.evalXPathToString());
				int top = atoi(apImTop.evalXPathToString());
				int width = atoi(apImWidth.evalXPathToString());
				int height = atoi(apImHeight.evalXPathToString());
				int angleDegrees = atoi(apImRotateAngle.evalXPathToString());
				boolean dragable = apImDragable.evalXPathToString().equals("1");

				// IfolorDock dock = s2dock(apImDock.evalXPathToString());

				if ("Image".equals(type)) {
					String origFile = apImOrigFilePath.evalXPathToString();
					String previewFile = apImPreviewFilePath
							.evalXPathToString();
					String sourceFile = apImSourceFilePath.evalXPathToString();
					double cropX = apImVpLeft.evalXPathToNumber() / 100d;
					double cropY = apImVpTop.evalXPathToNumber() / 100d;
					double cropW = apImVpWidth.evalXPathToNumber() / 100d;
					double cropH = apImVpHeight.evalXPathToNumber() / 100d;
					BorderShadow bs = s2border(apImBorderType
							.evalXPathToString());
					if (bs == null)
						bs = new BorderShadow(null, null);
					ImageCutCoords alpha = s2alpha(apImAlphaType
							.evalXPathToString());

					log.trace(String.format(
							"    %d,%d\t%s\t\t%f %f %f %f\t%s %s", left, top,
							origFile, cropX, cropY, cropW, cropH, bs.border,
							bs.shadow));

					BookPicture pic = new BookPicture(left, top, width, height,
							angleDegrees, dragable, origFile, previewFile,
							sourceFile, cropX, cropY, cropW, cropH, bs.border,
							bs.shadow, alpha);
					page.add(pic);

				} else if ("ColorRectangle".equals(type)) {
					Vector<ShapeColor> colors = new Vector<ShapeColor>();

					apShapeColor.resetXPath();
					while (apShapeColor.evalXPath() != -1) {
						String value = apShapeColorValue.evalXPathToString();
						double pos = apShapeColorPos.evalXPathToNumber();

						int a = Integer.parseInt(value.substring(0, 2), 16);
						int r = Integer.parseInt(value.substring(2, 4), 16);
						int g = Integer.parseInt(value.substring(4, 6), 16);
						int b = Integer.parseInt(value.substring(6, 8), 16);

						colors.add(new ShapeColor(new Color(r, g, b, a), pos));
					}

					BookShape shape = new BookShape(left, top, width, height,
							angleDegrees, colors.toArray(new ShapeColor[0]));
					page.add(shape);
				} else if ("Text".equals(type)) {
					String origFile = apImOrigFilePath.evalXPathToString();

					BookRtfText pic = new BookRtfText(left, top, width, height,
							angleDegrees, origFile);
					page.add(pic);
				} else if ("MetaFile".equals(type)) {
					// keine Ahnung, was das ist. Kommt immer wieder vor
				} else {
					throw new RuntimeException("Typ " + type
							+ " nicht bekannt in XML");
				}
			}

			// page numbers
			if (apPgLeftPageNumberVisible.evalXPathToNumber() == 1) {
				int left = atoi(apPgLeftPageNumberLeft.evalXPathToString());
				int top = atoi(apPgLeftPageNumberTop.evalXPathToString());
				int width = atoi(apPgLeftPageNumberWidth.evalXPathToString());
				int height = atoi(apPgLeftPageNumberHeight.evalXPathToString());
				// TODO apPgLeftPageNumberAlign == outer|inner
				page.add(new PageNumberElement(left, top, width, height, pgnum));
			}
			if (apPgRightPageNumberVisible.evalXPathToNumber() == 1) {
				int left = atoi(apPgRightPageNumberLeft.evalXPathToString());
				int top = atoi(apPgRightPageNumberTop.evalXPathToString());
				int width = atoi(apPgRightPageNumberWidth.evalXPathToString());
				int height = atoi(apPgRightPageNumberHeight.evalXPathToString());
				// TODO apPgRightPageNumberAlign == outer|inner
				page.add(new PageNumberElement(left, top, width, height,
						pgnum + 1));
			}
		}
		apIm.resetXPath();

		log.info("Finished loading");
		return book;
	}

	// private IfolorDock s2dock(String s) {
	// if ("top".equals(s))
	// return IfolorDock.top;
	// else if ("middle".equals(s))
	// return IfolorDock.middle;
	// else if ("bottom".equals(s))
	// return IfolorDock.bottom;
	// else
	// throw new RuntimeException("Invalid value for dock: " + s);
	// }

	private ImageCutCoords s2alpha(String alpha) {
		if (alpha == null || alpha.isEmpty())
			return null;

		if (alpha.equals("[ALPHAMASK01]Alpha2"))// ok
			return new ImageCutCoords.HeartCoords();
		if (alpha.equals("[ALPHAMASK01]Alpha5"))// ok
			return new ImageCutCoords.HeartCoords();
		if (alpha.equals("[ALPHAMASK01]Alpha6"))// ok
			return new ImageCutCoords.OvalCoords();
		// if (alpha.equals("[ALPHAMASK01]Alpha1"))//ausgefranst
		// return null;
		// if (alpha.equals("[ALPHAMASK01]Alpha3"))//gepunkteter Rand
		// return null;
		// if (alpha.equals("[ALPHAMASK01]Alpha10"))// umblättern
		// return null;

		System.err.println("Unsupported: alpha layer in picture: " + alpha);

		return null;
	}

	private BorderShadow s2border(String input) {
		if (input == null || input.isEmpty())
			return null;

		if (input.endsWith(" Line")) {
			if (input.equals("Green Line"))
				return new BorderShadow(new Border.LineBorder(2, Color.GREEN),
						null);
			if (input.equals("Orange Line"))
				return new BorderShadow(new Border.LineBorder(2, Color.ORANGE),
						null);
			if (input.equals("Red Line"))
				return new BorderShadow(new Border.LineBorder(2, Color.RED),
						null);
			if (input.equals("White Line"))
				return new BorderShadow(new Border.LineBorder(2, Color.WHITE),
						null);
			if (input.equals("Blue Line"))
				return new BorderShadow(new Border.LineBorder(2, Color.BLUE),
						null);
			if (input.equals("Black Line"))
				return new BorderShadow(new Border.LineBorder(2, Color.BLACK),
						null);
		}
		if (input.equals("Classic")) // simple gray line
			return new BorderShadow(new Border.LineBorder(2, Color.GRAY), null);

		if (input.startsWith("Heavy ")) {
			if (input.equals("Heavy White"))
				return new BorderShadow(new Border.HeavyBorder(24, Color.WHITE,
						0.6), mkSoftShadow());
			if (input.equals("Heavy Black"))
				return new BorderShadow(new Border.HeavyBorder(24, Color.BLACK,
						0.6), mkSoftShadow());
		}

		if (input.equals("Elegant")) // no border, soft shadow
			return new BorderShadow(null, mkSoftShadow());

		if (input.equals("Basic")) // no border, no shadow
			return null;

		if (input.equals("Retro")) // white heavy border and soft shadow
			return new BorderShadow(new Border.HeavyBorder(12, Color.WHITE, 1),
					mkSoftShadow());

		if (input.equals("Solid")) // simple black border and hard shadow
			return new BorderShadow(new Border.LineBorder(2, Color.BLACK),
					mkHardShadow());

		// also: existing borders [ALPHAMASK01]Border01..06

		if (input.equals("[ALPHAMASK01]Border01"))// scratch frame
			return new BorderShadow(new Border.ScratchBorder(4, 4), null);
		//
		// if (input.equals("[ALPHAMASK01]Border02"))// double black/white frame
		// // without shadow
		// return new BorderShadow(new Border.LineBorder(20,
		// Color.BLUE.brighter()), null);
		//
		// if (input.equals("[ALPHAMASK01]Border03"))// fat heavy gray frame
		// // without shadow
		// return new BorderShadow(new Border.LineBorder(20,
		// Color.YELLOW.brighter()), null);
		//
		// if (input.equals("[ALPHAMASK01]Border04"))// fat white frame with
		// // centered shadow
		// return new BorderShadow(new Border.LineBorder(20,
		// Color.BLUE.brighter()), null);
		//
		// if (input.equals("[ALPHAMASK01]Border05"))// heavy white frame with
		// // centered shadow
		// return new BorderShadow(new Border.LineBorder(20,
		// Color.CYAN.brighter()), null);
		//
		// if (input.equals("[ALPHAMASK01]Border06"))// smaller heavy white
		// frame
		// // without shadow and round corners
		// return new BorderShadow(new Border.LineBorder(20,
		// Color.GRAY.brighter()), null);

		System.err.println("Unknown border: " + input);
		return null;
	}

	private Shadow mkSoftShadow() {
		return new Shadow.SoftShadow(5, 5, 1, .6, 5);
	}

	private Shadow mkHardShadow() {
		return new Shadow.HardShadow(5, 5, 1, .6);
	}

	private int atoi(String s) {
		return Integer.valueOf(s);
	}

	private void loadFile(VTDGen vg, File projectFile) throws IOException {
		Decryptor decryptor = new Decryptor();
		byte[] bytes = decryptor.loadBinaryFile(projectFile, "DPP");

		vg.setDoc(bytes);
	}

	public static final class BorderShadow {
		public final Border border;
		public final Shadow shadow;

		public BorderShadow(Border border, Shadow shadow) {
			this.border = border;
			this.shadow = shadow;
		}

	}
}
