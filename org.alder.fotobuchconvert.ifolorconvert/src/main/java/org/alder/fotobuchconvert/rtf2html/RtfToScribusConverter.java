package org.alder.fotobuchconvert.rtf2html;

import java.awt.Color;
import java.io.CharArrayReader;
import java.io.IOException;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.rtf.RTFEditorKit;

import org.alder.fotobuchconvert.scribus.XmlBuilder;

public class RtfToScribusConverter {

	private static boolean debug = false;

	public void convert(XmlBuilder xml, String input) throws IOException,
			BadLocationException {
		if (input == null)
			return;

		CharArrayReader rd = new CharArrayReader(input.toCharArray());

		RTFEditorKit kit = new RTFEditorKit();
		DefaultStyledDocument doc = (DefaultStyledDocument) kit
				.createDefaultDocument();
		kit.read(rd, doc, 0);

		output(xml, doc);
	}

	void output(XmlBuilder xml, DefaultStyledDocument doc) {
		if (debug)
			doc.dump(System.out);

		try {
			Element section = doc.getDefaultRootElement();
			if (debug)
				System.out.println(section);
			assert section.getName().equals("section");

			final int jn = section.getElementCount();
			for (int j = 0; j < jn; j++) {
				Element paragraph = section.getElement(j);
				if (debug)
					System.out.println(paragraph);
				assert section.getName().equals("paragraph");

				boolean needParaPrint = (j > 0);
				boolean firstInPara = true;

				final int ni = paragraph.getElementCount();
				for (int i = 0; i < ni; i++) {
					Element content = paragraph.getElement(i);
					assert section.getName().equals("content");

					int start = content.getStartOffset();
					int end = content.getEndOffset();

					AttributeSet attr = content.getAttributes();
					Boolean italic = (Boolean) attr
							.getAttribute(StyleConstants.Italic);
					Boolean bold = (Boolean) attr
							.getAttribute(StyleConstants.Bold);
					Boolean underline = (Boolean) attr
							.getAttribute(StyleConstants.Underline);
					String family = (String) attr
							.getAttribute(StyleConstants.Family);
					Integer fontSize = (Integer) attr
							.getAttribute(StyleConstants.Size);
					Color color = (Color) attr
							.getAttribute(StyleConstants.ColorConstants.Foreground);

					String text = doc.getText(start, end - start);

					if (firstInPara && text.trim().isEmpty() && family == null
							&& fontSize == null)
						continue;
					else
						firstInPara = false;

					System.out.println(italic + " " + bold + " " + underline
							+ " " + family + " " + fontSize + " " + color
							+ "\t\"" + text + "\"");

					if (needParaPrint) {
						xml.add("para");
						needParaPrint = false;
					}

					XmlBuilder el = xml.add("ITEXT").set("CH", text);

					if (bold == Boolean.TRUE && italic == Boolean.TRUE)
						el.set("FONT", family + " Bold Italic");
					else if (bold == Boolean.TRUE)
						el.set("FONT", family + " Bold");
					else if (italic == Boolean.TRUE)
						el.set("FONT", family + " Italic");
					else
						el.set("FONT", family + " Regular");

					if (fontSize != null)
						el.set("FONTSIZE", fontSize);
				}
			}
		} catch (BadLocationException e) {
			throw new RuntimeException("This error should not occour", e);
		}

	}
}
