package org.alder.fotobuchconvert.ifolor;

import java.io.File;
import java.io.IOException;

import org.alder.fotobuchconvert.ifolorencryption.Decryptor;

public class DecryptMain {
	// private static final String CHARSET_ISO_8859_1 = "ISO_8859_1";
	private static final String CHARSET_cp1252 = "Windows-1252";

	final static String testfile1 = "/media/reverseengineer_ifolor/ifolorFiles/permut/05_Text_ABC/test_permut Data/data/bb34f746004943cc87b4a1a05e8ea32f";
	final static String testfile2 = "/media/reverseengineer_ifolor/ifolorFiles/permut/06_Text_Size24/test_permut Data/data/00a8ce71a05946e0876c74108dd201c3";
	final static String testfile3 = "/media/reverseengineer_ifolor/ifolorFiles/permut2_03_texte/permut2 Data/data/bc828f1f06fe4bd19278dcfd9f7fcfb4";
	final static String testdppfile1 = "/media/reverseengineer_ifolor/ifolorFiles/permut/02_Text_verschoben/test_permut.dpp";
	final static String testdppfile2 = "/media/reverseengineer_ifolor/ifolorFiles/test2.dpp";
	final static String testdppfile3 = "/daten/Photo/Album/2011/australia-fotobuch/FINAL Fotobuch 2012/Australia2-20120409.dpp";

	public static void main(String[] args) throws IOException {
		Decryptor decryptor = new Decryptor();
		File projectFile = new File(testdppfile3);
		byte[] bytes = decryptor.loadBinaryFile(projectFile, "DPP");
		System.out.print(new String(bytes, 0, bytes.length, CHARSET_cp1252));
	}
}
