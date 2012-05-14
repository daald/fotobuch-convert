package org.alder.fotobuchconvert.ifolorconvert;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestData {
	public static String getTestPath() {
		final String configFile = "testpaths.txt";
		try {
			InputStream is = TestData.class.getClassLoader()
					.getResourceAsStream(configFile);
			BufferedReader ir = new BufferedReader(new InputStreamReader(is));

			String line;
			while ((line = ir.readLine()) != null) {
				if (line.isEmpty() || line.startsWith("#"))
					continue;

				File file = new File(line);
				if (file.exists()) {
					if (line.endsWith(".xml"))
						line = line.substring(0, line.length() - 4);
					if (line.endsWith(".dpp"))
						line = line.substring(0, line.length() - 4);
					return line;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		throw new RuntimeException("No test file found (check " + configFile
				+ ")");
	}

	public static File getTestOutputPath() {
		if (isWindows())
			return new File("C:\\Temp\\scribustest.sla");
		else
			return new File("/tmp/scribustest.sla");
	}

	private static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("win") >= 0);
	}

	public static void main(String[] args) {
		System.out.println(getTestPath());
	}
}
