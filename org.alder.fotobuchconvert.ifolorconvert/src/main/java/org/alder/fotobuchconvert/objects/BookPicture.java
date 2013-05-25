/*******************************************************************************
 * Copyright (c) 2012 Daniel Alder.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Daniel Alder - initial API and implementation
 ******************************************************************************/
package org.alder.fotobuchconvert.objects;

import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;

import org.alder.fotobuchconvert.scribus.ImageCutCoords;

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
	public final ImageCutCoords alpha;

	public BookPicture(int left, int top, int width, int height,
			int angleDegrees, String origFile, String previewFile,
			String sourceFile, double cropX, double cropY, double cropW,
			double cropH, Border border, Shadow shadow, ImageCutCoords alpha) {
		super(left, top, width, height, angleDegrees);

		if ((origFile == null || origFile.isEmpty())
				&& (previewFile == null || previewFile.isEmpty())
				&& (sourceFile == null || sourceFile.isEmpty())) {
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
		this.alpha = alpha;// ex. heart

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

	public String getOrigName() {
		return origFile;
	}

	@Override
	public boolean isInternalObject() {
		return "Images::binding.png".equals(origFile);
	}

}
