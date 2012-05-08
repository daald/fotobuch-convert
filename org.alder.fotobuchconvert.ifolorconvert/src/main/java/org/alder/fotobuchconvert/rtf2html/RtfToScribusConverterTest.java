package org.alder.fotobuchconvert.rtf2html;

import java.io.IOException;

import javax.swing.text.BadLocationException;

import org.alder.fotobuchconvert.scribus.XmlBuilder;

public class RtfToScribusConverterTest {
	static String input = "{\\rtf1\\ansi\\ansicpg1252\\deff0\\deflang2055{\\fonttbl{\\f0\\fnil\\fcharset0 Trebuchet MS;}}\n"
			+ "{\\colortbl ;\\red0\\green0\\blue0;}\n"
			+ "\\viewkind4\\uc1\\pard\\qc\\b\\f0\\fs32 Sydney City\\b0 . \\cf1\\i The big smoke\\par\n"
			+ "}";

	public static void main(String[] args) throws IOException,
			BadLocationException {

		System.out.println(input);
		RtfToScribusConverter converter = new RtfToScribusConverter();

		XmlBuilder xml = new XmlBuilder("____");
		converter.convert(xml, input, null);

		xml.output(System.out);
	}
}
