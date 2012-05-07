package org.alder.fotobuchconvert.ifolorconvert;

import java.io.File;

public class TestData {
	public static String getTestPath() {
		String path, name;
		File file;

		if (false) {
			path = "/media/reverseengineer_ifolor/ifolorFiles/permut2_03_texte/";
			name = "permut2";
			file = new File(path, name + ".dpp");
			if (file.exists())
				return path + name;
		}

		path = "/media/reverseengineer_ifolor/xml/Australia/files/";
		name = "Australia2-20120409";
		file = new File(path, name + ".dpp");
		if (file.exists())
			return path + name;

		path = "G:\\ifolor\\tmpwin\\";
		file = new File(path, name + ".dpp");
		if (file.exists())
			return path + name;

		path = "P:\\ifolor\\fb\\Australia2-20120409\\";
		file = new File(path, name + ".dpp");
		if (file.exists())
			return path + name;

		throw new RuntimeException("Unknown environment");
	}

	public static File getTestOutputPath() {
		if (isWindows())
			return new File("C:\\Temp\\scribustest.sla");
		else
			return new File("/tmp/scribustest.sla");
	}

	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("win") >= 0);
	}
}
