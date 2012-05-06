package org.alder.fotobuchconvert.ifolorconvert;

import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;

public class BookPicture extends BookElement {
	// apImDesigner.selectXPath("@designer");// ="0"
	final String origFile;// apImOrigFilePath.selectXPath("OrigFilePath");//
							// <OrigFilePath><![CDATA[data\9ac23b2ee4f146678aaed92ab627d786]]
	final String previewFile;
	final String sourceFile;
	final double cropX, cropY, cropW, cropH;
	private Image image;

	public BookPicture(int left, int top, int width, int height,
			int angleDegrees, boolean dragable, IfolorDock dock,
			String origFile, String previewFile, String sourceFile,
			double cropX, double cropY, double cropW, double cropH) {
		super(left, top, width, height, angleDegrees, dragable, dock);

		this.origFile = origFile;
		this.previewFile = previewFile;
		this.sourceFile = sourceFile;

		this.cropX = cropX;
		this.cropY = cropY;
		this.cropW = cropW;
		this.cropH = cropH;

	}

	public File getImageFile(Book book) {
		File file = new File(book.pathInfo.projectFolder, previewFile.replace(
				'\\', '/'));
		return file;
	}

	public Image getImage(Book book) {
		if (image != null)
			return image;

		File file = getImageFile(book);
		System.out.println(this + ": " + file);
		image = new ImageIcon(file.getAbsolutePath()).getImage();
		return image;
	}

	public String getSourceName(Book book) {
		return sourceFile;
	}
}
