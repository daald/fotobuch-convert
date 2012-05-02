package org.alder.fotobuchconvert.ifolorconvert;

import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;

public class BookPicture extends BookElement {
	// apImDesigner.selectXPath("@designer");// ="0"
	final String origFile;// apImOrigFilePath.selectXPath("OrigFilePath");//
							// <OrigFilePath><![CDATA[data\9ac23b2ee4f146678aaed92ab627d786]]
	final double cropX, cropY, cropW, cropH;
	private Image image;

	public BookPicture(int left, int top, int width, int height,
			int angleDegrees, boolean dragable, IfolorDock dock,
			String origFile, double cropX, double cropY, double cropW,
			double cropH) {
		super(left, top, width, height, angleDegrees, dragable, dock);

		this.origFile = origFile;

		this.cropX = cropX;
		this.cropY = cropY;
		this.cropW = cropW;
		this.cropH = cropH;

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
