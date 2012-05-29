package org.alder.fotobuchconvert.ifolor;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.alder.fotobuchconvert.ifolor.model.AbstractElement;
import org.alder.fotobuchconvert.ifolor.model.AlphaSet;
import org.alder.fotobuchconvert.ifolor.model.ColorRectangle;
import org.alder.fotobuchconvert.ifolor.model.Image;
import org.alder.fotobuchconvert.ifolor.model.PageNumber;
import org.alder.fotobuchconvert.ifolor.model.ProjectPage;
import org.alder.fotobuchconvert.ifolor.model.Text;
import org.alder.fotobuchconvert.ifolor.model.UserProject;
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

public class IfolorLoader {

	private final Log log = LogFactory.getLog(IfolorLoader.class);

	public Book load(ProjectPath path) throws Exception {
		log.info("Loading Layout for " + path);
		log.debug("File: " + path.projectFile.getAbsolutePath());
		log.debug("Exists: " + path.projectFile.exists());

		/******
		 * now load the file
		 */
		byte[] bytes = loadFile(path.projectFile);
		UserProject _project = new JaxbHelper().loadXml(bytes);

		/******
		 * now read the file contents
		 */
		Book book = new Book(path);
		log.info("Loading Ifolor file");
		int pgnum = 0;
		for (ProjectPage _page : _project.Pages.pages) {
			log.debug("Loading:   pages " + pgnum + "+" + (pgnum + 1));

			final boolean leftPageEnabled = "True"
					.equals(_page.LeftPageEnabled);
			final boolean rightPageEnabled = "True"
					.equals(_page.RightPageEnabled);

			BookPage page = new BookPage(pgnum, leftPageEnabled,
					rightPageEnabled);
			book.add(page);

			handlePage(page, _page, pgnum);

			pgnum += 2;
		}

		log.info("Finished loading");
		return book;
	}

	/**
	 * 
	 * @param page
	 *            output page
	 * @param _page
	 *            input page
	 * @param pgnum
	 *            page number
	 */
	private void handlePage(BookPage page, ProjectPage _page, int pgnum) {
		for (AbstractElement _go : _page.GuiObjects) {
			log.debug("Loading:     element:" + _go);

			int left = _go.left;
			int top = _go.top;
			int width = _go.width;
			int height = _go.height;
			int angleDegrees = _go.rotateAngle;
			// boolean dragable =
			// in_go.Dragable.evalXPathToString().equals("1");

			// IfolorDock dock = s2dock(apImDock.evalXPathToString());

			if (_go instanceof Image) {
				Image _image = (Image) _go;

				String origFile = _image.OrigFilePath;
				String previewFile = _image.PreviewFilePath;
				String sourceFile = _image.SourceFilePath;
				double cropX = _image.VisiblePart.left / 100d;
				double cropY = _image.VisiblePart.top / 100d;
				double cropW = _image.VisiblePart.width / 100d;
				double cropH = _image.VisiblePart.height / 100d;
				BorderShadow bs = s2border(_image.Border);
				if (bs == null)
					bs = new BorderShadow(null, null);
				ImageCutCoords alpha = s2alpha(_image.AlphaSet);

				log.trace(String.format("    %d,%d\t%s\t\t%f %f %f %f\t%s %s",
						left, top, origFile, cropX, cropY, cropW, cropH,
						bs.border, bs.shadow));

				BookPicture pic = new BookPicture(left, top, width, height,
						angleDegrees, origFile, previewFile, sourceFile, cropX,
						cropY, cropW, cropH, bs.border, bs.shadow, alpha);
				page.add(pic);

			} else if (_go instanceof ColorRectangle) {
				ColorRectangle _colorRect = (ColorRectangle) _go;

				Vector<ShapeColor> colors = new Vector<ShapeColor>();

				for (org.alder.fotobuchconvert.ifolor.model.Color _color : _colorRect.colors) {
					String value = _color.value;
					double pos = _color.position;

					int a = Integer.parseInt(value.substring(0, 2), 16);
					int r = Integer.parseInt(value.substring(2, 4), 16);
					int g = Integer.parseInt(value.substring(4, 6), 16);
					int b = Integer.parseInt(value.substring(6, 8), 16);

					colors.add(new ShapeColor(new Color(r, g, b, a), pos));
				}

				BookShape shape = new BookShape(left, top, width, height,
						angleDegrees, colors.toArray(new ShapeColor[0]));

				page.add(shape);
			} else if (_go instanceof Text) {
				Text _text = (Text) _go;

				String origFile = _text.OrigFilePath;

				BookRtfText pic = new BookRtfText(left, top, width, height,
						angleDegrees, origFile);

				page.add(pic);

				// } else if ("MetaFile".equals(type)) {
				// // keine Ahnung, was das ist. Kommt immer wieder vor
			} else {
				throw new RuntimeException("Typ " + _go
						+ " nicht bekannt in XML");
			}
		}

		// page numbers
		if (_page.LeftPageNumber != null)
			handlePageNumber(page, _page, _page.LeftPageNumber, pgnum);
		if (_page.RightPageNumber != null)
			handlePageNumber(page, _page, _page.RightPageNumber, pgnum + 1);
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

	private void handlePageNumber(BookPage page, ProjectPage _page,
			PageNumber _number, int pgnum) {
		if (_number.visible != 1)
			return;

		int left = _number.left;
		int top = _number.top;
		int width = _number.width;
		int height = _number.height;
		// TODO apPgRightPageNumberAlign == outer|inner

		page.add(new PageNumberElement(left, top, width, height, pgnum));
	}

	private ImageCutCoords s2alpha(AlphaSet alphaSet) {
		if (alphaSet == null || alphaSet.id == null)
			return null;

		if (alphaSet.id.equals("[ALPHAMASK01]Alpha2"))// ok
			return new ImageCutCoords.HeartCoords();
		if (alphaSet.id.equals("[ALPHAMASK01]Alpha5"))// ok
			return new ImageCutCoords.HeartCoords();
		if (alphaSet.id.equals("[ALPHAMASK01]Alpha6"))// ok
			return new ImageCutCoords.OvalCoords();
		// if (alpha.id.equals("[ALPHAMASK01]Alpha1"))//ausgefranst
		// return null;
		// if (alpha.id.equals("[ALPHAMASK01]Alpha3"))//gepunkteter Rand
		// return null;
		// if (alpha.id.equals("[ALPHAMASK01]Alpha10"))// umblättern
		// return null;

		System.err.println("Unsupported: alpha layer in picture: "
				+ alphaSet.id);

		return null;
	}

	private BorderShadow s2border(
			org.alder.fotobuchconvert.ifolor.model.Border border) {
		if (border == null || border.id == null)
			return null;

		final String id = border.id;
		if (id.endsWith(" Line")) {
			if (id.equals("Green Line"))
				return new BorderShadow(new Border.LineBorder(2, Color.GREEN),
						null);
			if (id.equals("Orange Line"))
				return new BorderShadow(new Border.LineBorder(2, Color.ORANGE),
						null);
			if (id.equals("Red Line"))
				return new BorderShadow(new Border.LineBorder(2, Color.RED),
						null);
			if (id.equals("White Line"))
				return new BorderShadow(new Border.LineBorder(2, Color.WHITE),
						null);
			if (id.equals("Blue Line"))
				return new BorderShadow(new Border.LineBorder(2, Color.BLUE),
						null);
			if (id.equals("Black Line"))
				return new BorderShadow(new Border.LineBorder(2, Color.BLACK),
						null);
		}
		if (id.equals("Classic")) // simple gray line
			return new BorderShadow(new Border.LineBorder(2, Color.GRAY), null);

		if (id.startsWith("Heavy ")) {
			if (id.equals("Heavy White"))
				return new BorderShadow(new Border.HeavyBorder(24, Color.WHITE,
						0.6), mkSoftShadow());
			if (id.equals("Heavy Black"))
				return new BorderShadow(new Border.HeavyBorder(24, Color.BLACK,
						0.6), mkSoftShadow());
		}

		if (id.equals("Elegant")) // no border, soft shadow
			return new BorderShadow(null, mkSoftShadow());

		if (id.equals("Basic")) // no border, no shadow
			return null;

		if (id.equals("Retro")) // white heavy border and soft shadow
			return new BorderShadow(new Border.HeavyBorder(12, Color.WHITE, 1),
					mkSoftShadow());

		if (id.equals("Solid")) // simple black border and hard shadow
			return new BorderShadow(new Border.LineBorder(2, Color.BLACK),
					mkHardShadow());

		// also: existing borders [ALPHAMASK01]Border01..06

		if (id.equals("[ALPHAMASK01]Border01"))// scratch frame
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

		System.err.println("Unknown border: " + id);
		return null;
	}

	private Shadow mkSoftShadow() {
		return new Shadow.SoftShadow(5, 5, 1, .6, 5);
	}

	private Shadow mkHardShadow() {
		return new Shadow.HardShadow(5, 5, 1, .6);
	}

	private byte[] loadFile(File projectFile) throws IOException {
		Decryptor decryptor = new Decryptor();
		byte[] bytes = decryptor.loadBinaryFile(projectFile, "DPP");

		return bytes;
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
