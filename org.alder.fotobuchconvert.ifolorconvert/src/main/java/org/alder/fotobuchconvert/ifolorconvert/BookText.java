package org.alder.fotobuchconvert.ifolorconvert;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.alder.fotobuchconvert.ifolorencryption.Decryptor;

public class BookText extends BookElement {
	private byte[] data;
	private String dataFile;

	public BookText(int left, int top, int width, int height, int angleDegrees,
			boolean dragable, IfolorDock dock, String dataFile) {
		super(left, top, width, height, angleDegrees, dragable, dock);

		if (dataFile.isEmpty())
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
}
