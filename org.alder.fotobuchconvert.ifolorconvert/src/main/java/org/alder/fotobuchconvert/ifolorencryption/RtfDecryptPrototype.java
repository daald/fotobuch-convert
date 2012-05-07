package org.alder.fotobuchconvert.ifolorencryption;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;

public class RtfDecryptPrototype {
	private static final String UNEXPECTED_END_OF_FILE = "Unexpected end of file";
	private static final String DIGEST_MD52 = "MD5";
	// private static final String CHARSET_ISO_8859_1 = "ISO_8859_1";
	private static final String CHARSET_cp1252 = "Windows-1252";

	final static String testfile1 = "/media/reverseengineer_ifolor/ifolorFiles/permut/05_Text_ABC/test_permut Data/data/bb34f746004943cc87b4a1a05e8ea32f";
	final static String testfile2 = "/media/reverseengineer_ifolor/ifolorFiles/permut/06_Text_Size24/test_permut Data/data/00a8ce71a05946e0876c74108dd201c3";
	final static String testfile3 = "/media/reverseengineer_ifolor/ifolorFiles/permut2_03_texte/permut2 Data/data/bc828f1f06fe4bd19278dcfd9f7fcfb4";
	final static String testdppfile1 = "/media/reverseengineer_ifolor/ifolorFiles/permut/02_Text_verschoben/test_permut.dpp";

	public static void main(String[] args) throws IOException {
		FileInputStream fis = new FileInputStream(testdppfile1);
		byte[] buf = new byte[3];
		int l = fis.read(buf, 0, buf.length);
		if (l != 3)
			throw new IOException(UNEXPECTED_END_OF_FILE);

		String firstChars = new String(buf, Charset.forName(CHARSET_cp1252));
		if ("DPT".equals(firstChars)) {
			// loadCompressedGZip();

			System.out.println();

			// load md5
			try {
				MessageDigest md = MessageDigest.getInstance(DIGEST_MD52);
				byte[] md5 = new byte[md.getDigestLength()];
				l = fis.read(md5, 0, md5.length);
				if (l != md.getDigestLength())
					throw new IOException("file too small");

				// load size
				buf = new byte[4];
				l = fis.read(buf, 0, buf.length);
				if (l != 4)
					throw new IOException("file too small");
				int size = (((int) buf[3] & 0xFF) << 0x1000)
						| (((int) buf[2] & 0xFF) << 0x100)
						| (((int) buf[1] & 0xFF) << 0x10)
						| ((int) buf[0] & 0xFF);
				System.out.println(size);

				GZIPInputStream gzipIs = new GZIPInputStream(fis);
				buf = new byte[0x2000];
				while ((l = gzipIs.read(buf)) > 0) {
					System.out.print(new String(buf, 0, l, CHARSET_cp1252));
					md.update(buf, 0, l);
				}
				System.out.println();

				buf = md.digest();
				for (int i = 0; i < buf.length; i++)
					if (buf[i] != md5[i])
						throw new IOException("md5 mismatch at " + i);
				System.out.println("md5 ok");

			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("Problem with md5", e);
			}
		} else if ("DPP".equals(firstChars)) {
			// loadCompressedGZip();

			// load md5
			try {
				MessageDigest md = MessageDigest.getInstance(DIGEST_MD52);
				byte[] md5 = new byte[md.getDigestLength()];
				l = fis.read(md5, 0, md5.length);
				if (l != md.getDigestLength())
					throw new IOException(UNEXPECTED_END_OF_FILE);

				// load size
				buf = new byte[4];
				l = fis.read(buf, 0, buf.length);
				if (l != 4)
					throw new IOException(UNEXPECTED_END_OF_FILE);
				int size = (((int) buf[3] & 0xFF) << 24)
						| (((int) buf[2] & 0xFF) << 16)
						| (((int) buf[1] & 0xFF) << 8) | ((int) buf[0] & 0xFF);
				System.out.println(size);

				GZIPInputStream gzipIs = new GZIPInputStream(fis);
				buf = new byte[0x2000];
				while ((l = gzipIs.read(buf)) > 0) {
					System.out.print(new String(buf, 0, l, CHARSET_cp1252));
					md.update(buf, 0, l);
				}
				System.out.println();

				buf = md.digest();
				for (int i = 0; i < buf.length; i++)
					if (buf[i] != md5[i])
						throw new IOException("md5 mismatch at " + i);
				System.out.println("md5 ok");

			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("Problem with md5", e);
			}
		} else if ("{\\r".equals(firstChars)) {
			// already uncompressed RTF
			System.out.println("already uncompressed RTF");
			buf = new byte[0x2000];
			System.out.print("{\\r");
			while ((l = fis.read(buf)) > 0) {
				System.out.print(new String(buf, 0, l, CHARSET_cp1252));
			}
			System.out.println();
		} else {
			throw new IOException("Unknown file format");
		}
	}
}
