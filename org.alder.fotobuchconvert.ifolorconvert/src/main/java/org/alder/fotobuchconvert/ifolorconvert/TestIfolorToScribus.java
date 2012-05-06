package org.alder.fotobuchconvert.ifolorconvert;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;

import org.alder.fotobuchconvert.scribus.ScribusWriter;
import org.alder.fotobuchconvert.scribus.ScribusWriter.PageDims;
import org.alder.fotobuchconvert.scribus.ScribusWriter.ScribusImg;
import org.alder.fotobuchconvert.scribus.ScribusWriter.ScribusLine;

public class TestIfolorToScribus {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws Exception {

		Loader loader = new Loader();
		ProjectPath path = new ProjectPath(TestData.getTestPath());
		Book book = loader.load(path);

		TestIfolorToScribus f = new TestIfolorToScribus(book);

		f.process();

	}

	private Book book;

	public TestIfolorToScribus(final Book book) {
		this.book = book;
	}

	void process() throws FileNotFoundException {
		File outFile = new File("/tmp/scribustest.sla");

		int f = 1;

		int margin = 20;
		int bleed = 20;
		String pageFormat = "Custom";
		int pageW = (int) (f * 3530);
		int pageH = (int) (f * 2500);
		ScribusWriter wr = new ScribusWriter(outFile, margin, bleed,
				pageFormat, pageW, pageH);

		wr.addPage("Front", pageW, pageH);

		ScribusLine ln = wr.addLine(200, 200, 300, 300);
		ln = wr.addLine(200, 200, 300, 250);
		ln = wr.addLine(200, 200, 300, 200);
		wr.makeRect(200, 200, 100, 100, 0);

		int wrpg = 1;
		for (BookPage page : book.pages) {
			if (wrpg > 6)
				break;

			PageDims pd = wr.addPage("Normal", pageW, pageH);// left
			wr.addPage("Normal", pageW, pageH);// right

			int oX = pd.docbaseX;
			int oY = pd.docbaseY;

			for (BookElement el : page.pics) {
				int centerX = el.left + el.width / 2;
				int centerY = el.top + el.height / 2;

				// int offX = el.width / 2, offY;
				// switch (el.dock) {
				// case top:
				// offY = el.height;
				// break;
				// case middle:
				// offY = el.height / 2;
				// break;
				// case bottom:
				// offY = 0;
				// break;
				// default:
				// throw new RuntimeException(
				// "This command should never be called");
				// }

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
						scrimg.setPosition(oX + f * el.left, oY + f * el.top, f
								* el.width, f * el.height, el.angleDegrees);
						scrimg.setCropPct(pic.cropX, pic.cropY, pic.cropW,
								pic.cropH);

						placeHolder = false;
					} catch (Exception e) {
						System.err.println("Cannot load Image " + imgFile
								+ ". Drawing not possible");
						e.printStackTrace();
					}

				}
				// else if (el instanceof BookText) {
				// BookText text = (BookText) el;
				// String txt = text.getRtfText(book);
				// if (txt != null) {
				// g.setFont(g.getFont().deriveFont(60f));
				// FontMetrics fm = g.getFontMetrics();
				// g.drawString(txt, 0, fm.getHeight());
				// }
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
					wr.makeRect(oX + f * el.left, oY + f * el.top,
							f * el.width, f * el.height, 0);
					wr.makeRect(oX + f * el.left + 4, oY + f * el.top + 4, f
							* el.width - 8, f * el.height - 8, 0);
					wr.addLine(oX + f * el.left, oY + f * el.top, oX + f
							* (el.left + el.width), oY + f
							* (el.top + el.height));
					wr.addLine(oX + f * (el.left + el.width), oY + f * el.top,
							oX + f * el.left, oY + f * (el.top + el.height));
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

}
