package org.alder.fotobuchconvert.objects;

import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;

public class BookPicture extends BookElement {
	// apImDesigner.selectXPath("@designer");// ="0"
	final String origFile;// apImOrigFilePath.selectXPath("OrigFilePath");//
							// <OrigFilePath><![CDATA[data\9ac23b2ee4f146678aaed92ab627d786]]
	final String previewFile;
	final String sourceFile;
	public final double cropX, cropY, cropW, cropH;
	private Image image;
	public final Border border;
	public final Shadow shadow;

	public BookPicture(int left, int top, int width, int height,
			int angleDegrees, boolean dragable, String origFile,
			String previewFile, String sourceFile, double cropX, double cropY,
			double cropW, double cropH, Border border, Shadow shadow) {
		super(left, top, width, height, angleDegrees, dragable);

		if (origFile.isEmpty() && previewFile.isEmpty() && sourceFile.isEmpty()) {
			origFile = null;
			previewFile = null;
			sourceFile = null;
		}

		this.origFile = origFile;
		this.previewFile = previewFile;
		this.sourceFile = sourceFile;

		this.cropX = cropX;
		this.cropY = cropY;
		this.cropW = cropW;
		this.cropH = cropH;

		this.border = border;
		this.shadow = shadow;

	}

	public File getImageFile(Book book) {
		if (previewFile == null)
			return null;

		File file = new File(book.pathInfo.projectFolder, previewFile.replace(
				'\\', '/'));
		return file;
	}

	public Image getImage(Book book) {
		if (image != null)
			return image;

		File file = getImageFile(book);
		if (file == null)
			return null;
		System.out.println(this + ": " + file);
		image = new ImageIcon(file.getAbsolutePath()).getImage();
		return image;
	}

	public String getSourceName(Book book) {
		return sourceFile;
	}

	@Override
	public boolean isInternalObject() {
		return "Images::binding.png".equals(origFile);
	}

}
