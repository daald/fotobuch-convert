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

		this.dataFile = dataFile;
	}

	public byte[] getRtf(Book book) throws IOException {
		if (data != null)
			return data;

		File file = new File(book.pathInfo.projectFolder, dataFile.replace(
				'\\', '/'));
		System.out.println(this + ": " + file);
		Decryptor dec = new Decryptor();
		data = dec.loadBinaryFile(file, "DPT");
		return data;
	}

	public String getRtfText(Book book) {
		try {
			return new String(getRtf(book),
					Charset.forName(Decryptor.CHARSET_cp1252));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
}
