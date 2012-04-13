package org.alder.fotobuchconvert.ifolorconvert;

import java.io.File;

public class TestData {
	public static String getTestPath() {
		String path, name;
		File file;
		name = "Australia2-20120409";

		path = "/media/reverseengineer_ifolor/xml/Australia/files/";
		file = new File(path, name + ".dpp");
		if (file.exists())
			return path + name;

		path = "G:\\ifolor\\tmpwin\\";
		file = new File(path, name + ".dpp");
		if (file.exists())
			return path + name;

		throw new RuntimeException("Unknown environment");
	}
}
