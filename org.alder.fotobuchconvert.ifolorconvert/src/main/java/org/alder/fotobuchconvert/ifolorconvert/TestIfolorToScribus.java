package org.alder.fotobuchconvert.ifolorconvert;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.swing.text.BadLocationException;

import org.alder.fotobuchconvert.rtf2html.RtfToScribusConverter;
import org.alder.fotobuchconvert.scribus.ScribusWriter;
import org.alder.fotobuchconvert.scribus.ScribusWriter.PageDims;
import org.alder.fotobuchconvert.scribus.ScribusWriter.ScribusImg;
import org.alder.fotobuchconvert.scribus.ScribusWriter.ScribusText;

public class TestIfolorToScribus {

	private final int testLimit = 0;

	private Book book;

	public TestIfolorToScribus(final Book book) {
		this.book = book;
	}

	void process(File outFile) throws IOException, BadLocationException {
		// int f = 1;
		// int pageW = (int) (f * 3530);
		// int pageH = (int) (f * 2500);

		double margin = 9.33;
		double bleed = 9.33;
		String pageFormat = "Custom";

		int ifolorDoubleWidth = 7062;// width of a double page
		int ifolorHeight = 2504;
		int ifolorDPI = 300;

		// next is a fix formulare. only make it dynamic if you know the unit of
		// scribus Pts:
		double ifolorPt2scribusPtFactor = 1 / (7062d / 2d / 847.44d);

		double pageW = (ifolorDoubleWidth / 2) * ifolorPt2scribusPtFactor;
		double pageH = ifolorHeight * ifolorPt2scribusPtFactor;
		double oF = ifolorPt2scribusPtFactor;

		// adjust for bleeding
		pageW -= bleed;
		pageH -= bleed * 2;

		ScribusWriter wr = new ScribusWriter(outFile, margin, bleed,
				pageFormat, pageW, pageH);

		wr.addPage("Front", pageW, pageH);

		// ScribusLine ln = wr.addLine(200, 200, 300, 300);
		// ln = wr.addLine(200, 200, 300, 250);
		// ln = wr.addLine(200, 200, 300, 200);
		// wr.makeRect(200, 200, 100, 100, 0);

		int wrpg = 1;
		for (BookPage page : book.pages) {
			if (testLimit > 0 && wrpg > testLimit)
				break;

			PageDims pd = wr.addPage("Normal", pageW, pageH);// left
			wr.addPage("Normal", pageW, pageH);// right

			double oX = pd.docbaseX - bleed;
			double oY = pd.docbaseY - bleed;

			for (BookElement el : page.pics) {
				if (el.isInternalObject())
					continue;

				boolean placeHolder = true;

				if (el instanceof BookPicture) {
					BookPicture pic = (BookPicture) el;
					File imgFile = pic.getImageFile(book);

					// System.out.println(">" + w + " " + h + " " + pic.cropX
					// + " " + pic.cropY + " " + pic.cropW + " "
					// + pic.cropH);

					try {
						ScribusImg scrimg = wr.addImage(imgFile
								.getAbsolutePath());
						scrimg.setPositionCenterRot(oX + oF * el.left, oY + oF
								* el.top, oF * el.width, oF * el.height,
								el.angleDegrees);
						scrimg.setCropPct(pic.cropX, pic.cropY, pic.cropW,
								pic.cropH);

						placeHolder = false;
					} catch (Exception e) {
						System.err.println("Cannot load Image " + imgFile
								+ ". Drawing not possible");
						e.printStackTrace();
					}

				} else if (el instanceof BookText) {
					BookText text = (BookText) el;
					String txt = text.getRtfText(book);
					// if (txt != null) {
					// g.setFont(g.getFont().deriveFont(60f));
					// FontMetrics fm = g.getFontMetrics();
					// g.drawString(txt, 0, fm.getHeight());

					ScribusText scrtext = wr.addText();

					RtfToScribusConverter rtfConv = new RtfToScribusConverter();

					scrtext.setPositionCenterRot(oX + oF * el.left, oY + oF
							* el.top, oF * el.width, oF * el.height,
							el.angleDegrees);
					rtfConv.convert(scrtext.getElement(), txt, wr);
					placeHolder = false;
				}
				// // int w = img.getWidth(null);
				// // int h = img.getHeight(null);
				// // System.out.println(">" + w + " " + h + " " +
				// // text.cropX
				// // + " " + text.cropY + " " + text.cropW + " "
				// // + text.cropH);
				// // if (w > 0) {
				// // g.drawImage(img, 0, 0, el.width, el.height,
				// // r(text.cropX * w), r(text.cropY * h),
				// // r((text.cropX + text.cropW) * w),
				// // r((text.cropY + text.cropH) * h), null);
				// // } else
				// img = null;
				// } else {
				// img = null;
				// }
				//
				// if (img == null)
				if (placeHolder) {
					// g.setColor(Color.LIGHT_GRAY);
					wr.makeRect(oX + oF * el.left, oY + oF * el.top, oF
							* el.width, oF * el.height, 0);
					wr.makeRect(oX + oF * el.left + 4, oY + oF * el.top + 4, oF
							* el.width - 8, oF * el.height - 8, 0);
					wr.addLine(oX + oF * el.left, oY + oF * el.top, oX + oF
							* (el.left + el.width), oY + oF
							* (el.top + el.height));
					wr.addLine(oX + oF * (el.left + el.width),
							oY + oF * el.top, oX + oF * el.left, oY + oF
									* (el.top + el.height));
					// g.drawLine(0, 0, el.width, el.height);
					// g.drawLine(0, el.height, el.width, 0);
				}
				// g.drawImage(img, 0, 0, pic.width, pic.height, null);
			}

			// pageBorders(g);

			wrpg += 2;
		}

		wr.finish();
	}

	private void pageBorders(Graphics2D g) {
		g.setColor(Color.BLUE);

		// final int ox = 102;
		// final int oy = 102;
		// // <Width>7712</Width>
		// // <Height>2953</Height>
		// g.drawRect(ox + 0, oy + 0, ox + 7712, oy + 2953);
		// // <GuideLine x1="3778" y1="0" x2="3778" y2="2953" />
		// g.drawLine(ox + 3778, oy + 0, ox + 3778, oy + 2953);
		// // <GuideLine x1="3934" y1="0" x2="3934" y2="2953" />
		// g.drawLine(ox + 3934, oy + 0, ox + 3934, oy + 2953);
		// // <GuideBox left="220" top="220" width="7272" height="2513"
		// />
		// g.drawRect(ox + 220, oy + 220, ox + 7272, oy + 2513);

		final int ox = 0;
		final int oy = 0;
		// <LeftSideOffset>0</LeftSideOffset>
		// <RightSideOffset>3531</RightSideOffset>
		// <LeftPageOffset>-102</LeftPageOffset>
		// <RightPageOffset>102</RightPageOffset>
		// <Width>7062</Width>
		// <Height>2504</Height>
		g.drawRect(ox + 0, oy + 0, ox + 7062, oy + 2504);
		// <GuideLine x1="3531" y1="0" x2="3531" y2="2504" />
		g.drawLine(ox + 3531, oy + 0, ox + 3531, oy + 2504);
		// <GuideBox left="40" top="40" width="6982" height="2424" />
		g.drawRect(ox + 40, oy + 40, ox + 6982, oy + 2424);
	}

	public static void main(String[] args) throws Exception {
		ProjectPath path = new ProjectPath(TestData.getTestPath());

		Loader loader = new Loader();
		Book book = loader.load(path);

		TestIfolorToScribus f = new TestIfolorToScribus(book);

		File outFile = TestData.getTestOutputPath();

		f.process(outFile);
	}

}
