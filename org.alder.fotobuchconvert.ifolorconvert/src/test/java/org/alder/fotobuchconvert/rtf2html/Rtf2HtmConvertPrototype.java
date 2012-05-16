package org.alder.fotobuchconvert.rtf2html;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.MinimalHTMLWriter;
import javax.swing.text.rtf.RTFEditorKit;

public class Rtf2HtmConvertPrototype {
	final static String testfile1 = "/media/reverseengineer_ifolor/ifolorFiles/permut2_04_umlaute/permut2 Data/data/b1cdcebf62be4814b176a7b9a8994cc0";
	final static String outfile = "/tmp/out.htm";

	/**
	 * grundsätzliches Problem:
	 * 
	 * DIE UMLAUTE
	 */

	public static void main(String[] args) throws IOException,
			BadLocationException {
		FileInputStream in = new FileInputStream(testfile1);
		FileWriter out = new FileWriter(outfile);

		// StyledDocument doc = new DefaultStyledDocument();

		// new RTFEditorKit().read(in, doc, 0);

		RTFEditorKit kit = new RTFEditorKit();
		DefaultStyledDocument doc = (DefaultStyledDocument) kit
				.createDefaultDocument();
		kit.read(in, doc, 0);

		new MinimalHTMLWriter(out, doc).write();
		out.close();
	}

	public static void main_v2(String[] args) throws IOException,
			BadLocationException {
		FileReader in = new FileReader(testfile1);
		FileWriter out = new FileWriter(outfile);
		StyledDocument doc = new DefaultStyledDocument();
		new RTFEditorKit().read(in, doc, 0);
		new MinimalHTMLWriter(out, doc).write();
		out.close();
	}
}
