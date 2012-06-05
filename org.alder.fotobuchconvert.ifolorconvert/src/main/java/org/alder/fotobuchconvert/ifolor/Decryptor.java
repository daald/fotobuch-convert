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
package org.alder.fotobuchconvert.ifolor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Decryptor {
	private static final String UNEXPECTED_END_OF_FILE = "Unexpected end of file";
	private static final String DIGEST_MD52 = "MD5";
	// private static final String CHARSET_ISO_8859_1 = "ISO_8859_1";
	public static final String CHARSET_cp1252 = "Windows-1252";

	private final Log log = LogFactory.getLog(Decryptor.class);

	private boolean dumpData = false;

	public byte[] loadBinaryFile(File projectFile, String type)
			throws IOException {
		assert type != null;

		InputStream is = null;
		try {
			// Variables
			is = new FileInputStream(projectFile);
			int bytesRead;

			// Detect if File is encrypted
			byte[] buffer = new byte[3];
			bytesRead = is.read(buffer, 0, 3);
			if (bytesRead != 3)
				throw new IOException(UNEXPECTED_END_OF_FILE);
			String firstChars = new String(buffer, 0, 3,
					Charset.forName(CHARSET_cp1252));
			if (type.equals(firstChars)) {
				// this is an encrypted file

				return loadCompressedBinaryData(is);

			} else {
				// this is an unencrypted file.
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				output.write(buffer, 0, 3);

				buffer = new byte[4096];

				// correct read alignment (for speed)
				bytesRead = is.read(buffer, 3, buffer.length - 3);
				if (bytesRead >= 0)
					output.write(buffer, 3, bytesRead);

				// now just load rest of it
				while ((bytesRead = is.read(buffer)) != -1)
					output.write(buffer, 0, bytesRead);

				return output.toByteArray();
			}
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
				}
			is = null;
		}
	}

	private byte[] loadCompressedBinaryData(InputStream is) throws IOException,
			UnsupportedEncodingException {

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];

		int bytesRead;
		// load md5 sum
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(DIGEST_MD52);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		byte[] md5 = new byte[md.getDigestLength()];
		bytesRead = is.read(md5, 0, md5.length);
		if (bytesRead != md.getDigestLength())
			throw new IOException(UNEXPECTED_END_OF_FILE);

		// load size
		bytesRead = is.read(buffer, 0, 4);
		if (bytesRead != 4)
			throw new IOException(UNEXPECTED_END_OF_FILE);
		int dataSize = (((int) buffer[3] & 0xFF) << 24)
				| (((int) buffer[2] & 0xFF) << 16)
				| (((int) buffer[1] & 0xFF) << 8) | ((int) buffer[0] & 0xFF);
		log.debug("uncompressed file size: " + dataSize);

		GZIPInputStream gzipIs = new GZIPInputStream(is);
		while ((bytesRead = gzipIs.read(buffer)) != -1) {
			md.update(buffer, 0, bytesRead);
			output.write(buffer, 0, bytesRead);
			if (dumpData)
				System.out.print(new String(buffer, 0, bytesRead,
						CHARSET_cp1252));
		}
		if (dumpData)
			System.out.println();

		buffer = md.digest();
		for (int i = 0; i < buffer.length; i++)
			if (buffer[i] != md5[i])
				throw new IOException("MD5 mismatch");

		if (output.size() != dataSize)
			throw new IOException(String.format(
					"File size mismatch: %d instead of %d", output.size(),
					dataSize));

		log.debug("MD5 ok");

		return output.toByteArray();
	}
}
