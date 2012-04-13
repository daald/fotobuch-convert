package org.alder.fotobuchconvert.ifolorconvert;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) throws Exception {
		Loader loader = new Loader();
		ProjectPath path = new ProjectPath(
				"G:\\ifolor\\tmpwin\\Australia2-20120409");
		loader.load(path);
	}
}
