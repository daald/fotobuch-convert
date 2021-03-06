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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.alder.fotobuchconvert.ifolor.Decryptor;

public class BookRtfText extends BookText {
	private byte[] data;
	private String dataFile;

	public BookRtfText(int left, int top, int width, int height,
			int angleDegrees, String dataFile) {
		super(left, top, width, height, angleDegrees);

		if (dataFile == null || dataFile.isEmpty())
			dataFile = null;

		this.dataFile = dataFile;
	}

	public byte[] getRtf(Book book) throws IOException {
		if (data != null)
			return data;

		if (dataFile == null)
			return null;

		File file = new File(book.pathInfo.projectFolder, dataFile.replace(
				'\\', '/'));
		System.out.println(this + ": " + file);
		Decryptor dec = new Decryptor();
		data = dec.loadBinaryFile(file, "DPT");
		return data;
	}

	public String getRtfText(Book book) {
		try {
			byte[] rtf = getRtf(book);
			if (rtf == null)
				return null;

			return new String(rtf, Charset.forName(Decryptor.CHARSET_cp1252));
		} catch (IOException e) {
			log.warn("Error loading text file", e);
			return null;
		}

	}

	@Override
	public String getText(Book book) {
		return "- rtf encoded text -";
	}
}
