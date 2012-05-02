package org.alder.fotobuchconvert.ifolorconvert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

class Loader {
	private final Log log = LogFactory.getLog(Loader.class);

	Book load(ProjectPath path) throws Exception {
		log.info("Loading Layout for " + path);

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
		// AutoPilot apImVisiblePart = new AutoPilot();
		AutoPilot apImVpLeft = new AutoPilot();
		AutoPilot apImVpTop = new AutoPilot();
		AutoPilot apImVpWidth = new AutoPilot();
		AutoPilot apImVpHeight = new AutoPilot();

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
		apImDock.selectXPath("@dock");// ="middle"
		apImDesigner.selectXPath("@designer");// ="0"
		apImOrigFilePath.selectXPath("OrigFilePath");// <OrigFilePath><![CDATA[data\9ac23b2ee4f146678aaed92ab627d786]]
		// apImVisiblePart.selectXPath("VisiblePart"); // <VisiblePart
		apImVpLeft.selectXPath("VisiblePart/@left");// left="0"
		apImVpTop.selectXPath("VisiblePart/@top");// top="2.333334"
		apImVpWidth.selectXPath("VisiblePart/@width");// width="100"
		apImVpHeight.selectXPath("VisiblePart/@height");// height="90.66666"

		System.out.println(path.projectFile.getAbsolutePath());
		System.out.println(path.projectFile.exists());
		if (!vg.parseFile(path.projectFile.getAbsolutePath(), false)) {
			throw new RuntimeException("Invalid XML structure");
		}

		Book book = new Book(path);

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
		// apImVisiblePart.bind(vn);
		apImVpLeft.bind(vn);
		apImVpTop.bind(vn);
		apImVpWidth.bind(vn);
		apImVpHeight.bind(vn);
		System.out.println("start Pages");
		apPg.resetXPath();
		while (apPg.evalXPath() != -1) {
			System.out.println("  start Images");

			BookPage page = new BookPage();
			book.add(page);

			apGo.resetXPath();
			while (apGo.evalXPath() != -1) {
				String type = vn.toString(vn.getCurrentIndex());
				System.out.printf("    T:%s\n", type);

				int left = atoi(apImLeft.evalXPathToString());
				int top = atoi(apImTop.evalXPathToString());
				int width = atoi(apImWidth.evalXPathToString());
				int height = atoi(apImHeight.evalXPathToString());
				int angleDegrees = atoi(apImRotateAngle.evalXPathToString());
				boolean dragable = apImDragable.evalXPathToString().equals("1");

				IfolorDock dock = s2dock(apImDock.evalXPathToString());

				if ("Image".equals(type)) {
					String origFile = apImOrigFilePath.evalXPathToString();
					double cropX = apImVpLeft.evalXPathToNumber() / 100d;
					double cropY = apImVpTop.evalXPathToNumber() / 100d;
					double cropW = apImVpWidth.evalXPathToNumber() / 100d;
					double cropH = apImVpHeight.evalXPathToNumber() / 100d;

					BookPicture pic = new BookPicture(left, top, width, height,
							angleDegrees, dragable, dock, origFile, cropX,
							cropY, cropW, cropH);
					page.add(pic);

					System.out.printf("    %d,%d\t%s\t\t%f %f %f %f\n", left,
							top, origFile, cropX, cropY, cropW, cropH);
				} else if ("ColorRectangle".equals(type)) {
					BookShape pic = new BookShape(left, top, width, height,
							angleDegrees, dragable, dock);
					page.add(pic);

					System.out.printf("    %d,%d\n", left, top);
				} else if ("Text".equals(type)) {
					String origFile = apImOrigFilePath.evalXPathToString();
					double cropX = apImVpLeft.evalXPathToNumber() / 100d;
					double cropY = apImVpTop.evalXPathToNumber() / 100d;
					double cropW = apImVpWidth.evalXPathToNumber() / 100d;
					double cropH = apImVpHeight.evalXPathToNumber() / 100d;

					BookText pic = new BookText(left, top, width, height,
							angleDegrees, dragable, dock);
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

		return book;
	}

	private IfolorDock s2dock(String s) {
		if ("top".equals(s))
			return IfolorDock.top;
		else if ("middle".equals(s))
			return IfolorDock.middle;
		else if ("bottom".equals(s))
			return IfolorDock.bottom;
		else
			throw new RuntimeException("Invalid value for dock: " + s);
	}

	private int atoi(String s) {
		return Integer.valueOf(s);
	}

}