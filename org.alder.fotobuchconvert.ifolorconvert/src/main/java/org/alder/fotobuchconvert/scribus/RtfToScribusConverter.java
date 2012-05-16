package org.alder.fotobuchconvert.scribus;

import java.awt.Color;
import java.io.CharArrayReader;
import java.io.IOException;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.rtf.RTFEditorKit;

import org.alder.fotobuchconvert.tools.XmlBuilder;


public class RtfToScribusConverter {

	private static boolean debug = true;

	public void convert(XmlBuilder xml, String input, ScribusWriter scribus)
			throws IOException, BadLocationException {
		if (input == null)
			return;

		CharArrayReader rd = new CharArrayReader(input.toCharArray());

		RTFEditorKit kit = new RTFEditorKit();
		DefaultStyledDocument doc = (DefaultStyledDocument) kit
				.createDefaultDocument();
		kit.read(rd, doc, 0);

		output(xml, doc, scribus);
	}

	void output(XmlBuilder xml, DefaultStyledDocument doc, ScribusWriter scribus) {
		if (debug)
			doc.dump(System.out);

		try {
			Element section = doc.getDefaultRootElement();
			if (debug)
				System.out.println(section);
			assert section.getName().equals("section");

			final int nj = section.getElementCount();
			for (int j = 0; j < nj; j++) {
				Element paragraph = section.getElement(j);
				if (debug)
					System.out.println(paragraph);
				assert section.getName().equals("paragraph");

				// boolean firstInPara = true;
				AttributeSet attr = paragraph.getAttributes();
				Integer alignment = (Integer) attr
						.getAttribute(StyleConstants.Alignment);

				boolean elementsInThisLine = false;
				final int ni = paragraph.getElementCount();
				for (int i = 0; i < ni; i++) {
					Element content = paragraph.getElement(i);
					assert section.getName().equals("content");

					int start = content.getStartOffset();
					int end = content.getEndOffset();

					attr = content.getAttributes();
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

					// if (firstInPara && text.trim().isEmpty() && family ==
					// null
					// && fontSize == null)
					// continue;
					// else
					// firstInPara = false;
					if (i == ni - 1 && text.trim().isEmpty()
							&& text.length() < 3)
						continue;
					elementsInThisLine = true;

					System.out.println(italic + " " + bold + " " + underline
							+ " " + family + " " + fontSize + " " + color
							+ "\t\"" + text + "\"");

					XmlBuilder el = xml.add(C.EL_ITEXT).set("CH", text);

					if (bold == Boolean.TRUE && italic == Boolean.TRUE)
						el.set(C.FONT, family + " Bold Italic");
					else if (bold == Boolean.TRUE)
						el.set(C.FONT, family + " Bold");
					else if (italic == Boolean.TRUE)
						el.set(C.FONT, family + " Italic");
					else
						el.set(C.FONT, family + " Regular");

					if (fontSize != null)
						el.set(C.FONTSIZE, fontSize);

					if (color != null && color.equals(Color.BLACK)
							&& scribus != null) {
						String colname = scribus.colorManager
								.getColorName(color);
						el.set(C.FCOLOR, colname);
					}
				}

				if (!elementsInThisLine && j == nj - 1)
					break; // don't convert last line if empty

				XmlBuilder el = xml.add(C.EL_PARA);
				if (alignment != null)
					switch (alignment) {
					case StyleConstants.ALIGN_LEFT:
						el.set(C.ALIGN, 0);
						break;
					case StyleConstants.ALIGN_CENTER:
						el.set(C.ALIGN, 1);
						break;
					case StyleConstants.ALIGN_RIGHT:
						el.set(C.ALIGN, 2);
						break;
					case StyleConstants.ALIGN_JUSTIFIED:
						el.set(C.ALIGN, 3);
						break;
					}
			}
		} catch (BadLocationException e) {
			throw new RuntimeException("This error should not occour", e);
		}

	}
}
