package org.alder.fotobuchconvert.ifolor;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.alder.fotobuchconvert.objects.Book;
import org.alder.fotobuchconvert.objects.BookPage;
import org.alder.fotobuchconvert.objects.BookPicture;
import org.alder.fotobuchconvert.objects.BookShape;
import org.alder.fotobuchconvert.objects.BookShape.ShapeColor;
import org.alder.fotobuchconvert.objects.BookText;
import org.alder.fotobuchconvert.objects.Border;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

public class Loader {
	private final Log log = LogFactory.getLog(Loader.class);

	public Book load(ProjectPath path) throws Exception {
		log.info("Loading Layout for " + path);
		log.debug("File: " + path.projectFile.getAbsolutePath());
		log.debug("Exists: " + path.projectFile.exists());

		/******
		 * initialization
		 */

		VTDGen vg = new VTDGen();
		AutoPilot apPg = new AutoPilot();
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
		AutoPilot apShapeColor = new AutoPilot();
		AutoPilot apShapeColorValue = new AutoPilot();
		AutoPilot apShapeColorPos = new AutoPilot();

		apPg.selectXPath("/UserProject/Pages/ProjectPage");
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
		apShapeColor.bind(vn);
		apShapeColorValue.bind(vn);
		apShapeColorPos.bind(vn);

		/******
		 * now read the file contents
		 */
		Book book = new Book(path);
		log.trace("Doc root");
		apPg.resetXPath();
		while (apPg.evalXPath() != -1) {
			System.out.println("  page");

			BookPage page = new BookPage();
			book.add(page);

			apGo.resetXPath();
			while (apGo.evalXPath() != -1) {
				String type = vn.toString(vn.getCurrentIndex());
				System.out.printf("    element:%s\n", type);

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
					Border border = s2border(apImBorderType.evalXPathToString());

					System.out.printf("    %d,%d\t%s\t\t%f %f %f %f\t%s\n",
							left, top, origFile, cropX, cropY, cropW, cropH,
							border);

					BookPicture pic = new BookPicture(left, top, width, height,
							angleDegrees, dragable, origFile, previewFile,
							sourceFile, cropX, cropY, cropW, cropH, border);
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
							angleDegrees, dragable,
							colors.toArray(new ShapeColor[0]));
					page.add(shape);

					System.out.printf("    %d,%d\n", left, top);
				} else if ("Text".equals(type)) {
					String origFile = apImOrigFilePath.evalXPathToString();

					BookText pic = new BookText(left, top, width, height,
							angleDegrees, dragable, origFile);
					page.add(pic);

					System.out.printf("    %d,%d\n", left, top);
				} else if ("MetaFile".equals(type)) {
					// keine Ahnung, was das ist. Kommt immer wieder vor
				} else {
					throw new RuntimeException("Typ " + type
							+ " nicht bekannt in XML");
				}
			}
		}
		apIm.resetXPath();

		System.out.println();
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

	private Border s2border(String input) {
		if (input == null)
			return null;

		if (input.endsWith(" Line")) {
			if (input.equals("Green Line"))
				return new Border.LineBorder(2, Color.GREEN);
			if (input.equals("Orange Line"))
				return new Border.LineBorder(2, Color.ORANGE);
			if (input.equals("Red Line"))
				return new Border.LineBorder(2, Color.RED);
			if (input.equals("White Line"))
				return new Border.LineBorder(2, Color.WHITE);
			if (input.equals("Blue Line"))
				return new Border.LineBorder(2, Color.BLUE);
			if (input.equals("Black Line"))
				return new Border.LineBorder(2, Color.BLACK);
		}
		if (input.equals("Classic")) // simple gray line
			return new Border.LineBorder(2, Color.GRAY);

		if (input.startsWith("Heavy ")) {
			if (input.equals("Heavy White"))
				return new Border.HeavyBorder(24, Color.WHITE, 0.6, true);
			if (input.equals("Heavy Black"))
				return new Border.HeavyBorder(24, Color.BLACK, 0.6, true);
		}

		if (input.equals("Elegant")) {// no border, soft shadow
			System.err.println("Unsupported border: " + input);
			return new Border.LineBorder(20, Color.GREEN);
			// return null;
		}
		if (input.equals("Basic")) // no border, no shadow
			return null;

		if (input.equals("Retro")) {// white heavy border and soft shadow
			System.err.println("Unsupported border: " + input);
			return new Border.LineBorder(20, Color.BLUE);
			// return null;
		}
		if (input.equals("Solid")) {// simple black border and hard shadow
			System.err.println("Unsupported border: " + input);
			return new Border.LineBorder(20, Color.GRAY);
			// return null;
		}

		// also: [ALPHAMASK01]Border01..06

		System.err.println("Unknown border: " + input);
		return new Border.LineBorder(20, Color.GREEN.brighter());
	}

	private int atoi(String s) {
		return Integer.valueOf(s);
	}

	private void loadFile(VTDGen vg, File projectFile) throws IOException {
		Decryptor decryptor = new Decryptor();
		byte[] bytes = decryptor.loadBinaryFile(projectFile, "DPP");

		vg.setDoc(bytes);
	}

}
