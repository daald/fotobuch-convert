package org.alder.fotobuchconvert.ifolorconvert;

import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;

public class BookPicture {
	// apImQuality.selectXPath("@quality");// ="Good"
	// apImEnhancement.selectXPath("@enhancement");// ="1"
	// apImModified.selectXPath("@modified");// ="0"
	// apImId.selectXPath("@id");// ="I01"
	final int left;
	final int top;
	final int width;
	final int height;
	final int angleDegrees;// rotateAngle
	// apImApplyOffset.selectXPath("@applyOffset");// ="0"
	// apImEditable.selectXPath("@editable");// ="1"
	final boolean dragable;
	final IfolorDock dock;
	// apImDesigner.selectXPath("@designer");// ="0"
	final String origFile;// apImOrigFilePath.selectXPath("OrigFilePath");//
							// <OrigFilePath><![CDATA[data\9ac23b2ee4f146678aaed92ab627d786]]
	final double cropX, cropY, cropW, cropH;
	private Image image;

	public BookPicture(int left, int top, int width, int height,
			int angleDegrees, boolean dragable, String origFile, double cropX,
			double cropY, double cropW, double cropH, IfolorDock dock) {
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
		this.angleDegrees = angleDegrees;
		this.dragable = dragable;
		this.origFile = origFile;

		this.cropX = cropX;
		this.cropY = cropY;
		this.cropW = cropW;
		this.cropH = cropH;

		this.dock = dock;
	}

	public Image getImage(Book book) {
		if (image != null)
			return image;

		int i = origFile.indexOf('\\');
		File file = new File(book.pathInfo.previewFolder,
				origFile.substring(i + 1));
		System.out.println(this + ": " + file);
		image = new ImageIcon(file.getAbsolutePath()).getImage();
		return image;
	}
}
