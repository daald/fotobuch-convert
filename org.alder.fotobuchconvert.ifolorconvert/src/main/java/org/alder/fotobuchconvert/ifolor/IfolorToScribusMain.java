package org.alder.fotobuchconvert.ifolor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.swing.text.BadLocationException;

import org.alder.fotobuchconvert.ifolorconvert.Book;
import org.alder.fotobuchconvert.ifolorconvert.BookElement;
import org.alder.fotobuchconvert.ifolorconvert.BookPage;
import org.alder.fotobuchconvert.ifolorconvert.BookPicture;
import org.alder.fotobuchconvert.ifolorconvert.BookShape;
import org.alder.fotobuchconvert.ifolorconvert.BookText;
import org.alder.fotobuchconvert.ifolorconvert.Loader;
import org.alder.fotobuchconvert.ifolorconvert.ProjectPath;
import org.alder.fotobuchconvert.ifolorconvert.TestData;
import org.alder.fotobuchconvert.rtf2html.RtfToScribusConverter;
import org.alder.fotobuchconvert.scribus.ScribusWriter;
import org.alder.fotobuchconvert.scribus.ScribusWriter.PageDims;
import org.alder.fotobuchconvert.scribus.ScribusWriter.ScribusImg;
import org.alder.fotobuchconvert.scribus.ScribusWriter.ScribusShape;
import org.alder.fotobuchconvert.scribus.ScribusWriter.ScribusText;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IfolorToScribusMain {

	protected final Log log = LogFactory.getLog(getClass());

	private final int testLimit = 12;

	private Book book;

	public IfolorToScribusMain(final Book book) {
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

		// next is a fix formula. only change it if you know the unit of scribus
		// Pts
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

					try {
						String imgFilePath = imgFile != null ? imgFile
								.getAbsolutePath() : null;
						ScribusImg scrimg = wr.addImage(imgFilePath);
						scrimg.setPositionCenterRot(oX + oF * el.left, oY + oF
								* el.top, oF * el.width, oF * el.height,
								el.angleDegrees);
						scrimg.setCropPct(pic.cropX, pic.cropY, pic.cropW,
								pic.cropH);

						if (imgFile != null) {
							scrimg.addPictureFrame(oX + oF * el.left, oY + oF
									* el.top, oF * el.width, oF * el.height,
									el.angleDegrees);
						} else
							log.warn("Empty picture in page " + wrpg);

						placeHolder = false;
					} catch (IOException e) {
						System.err.println("Cannot load Image " + imgFile
								+ " (" + pic.getSourceName(book)
								+ "). Drawing not possible");
						e.printStackTrace();
					}

				} else if (el instanceof BookText) {
					BookText text = (BookText) el;
					String txt = text.getRtfText(book);

					ScribusText scrtext = wr.addText();

					scrtext.setPositionCenterRot(oX + oF * el.left, oY + oF
							* el.top, oF * el.width, oF * el.height,
							el.angleDegrees);
					if (txt != null) {
						RtfToScribusConverter rtfConv = new RtfToScribusConverter();

						rtfConv.convert(scrtext.getElement(), txt, wr);
					} else
						log.warn("Empty text in page " + wrpg);

					placeHolder = false;

				} else if (el instanceof BookShape) {
					BookShape shape = (BookShape) el;

					ScribusShape out = wr.addShape();

					out.setPositionCenterRot(oX + oF * el.left, oY + oF
							* el.top, oF * el.width, oF * el.height,
							el.angleDegrees);
					if (shape.colors.length == 1)
						out.setFill(wr.colorManager
								.getColorName(shape.colors[0].color));
					else
						out.setGradient(shape.colors);

					placeHolder = false;
				}

				if (placeHolder) {
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
				}
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
		ProjectPath path = TestData.getTestProject();

		Loader loader = new Loader();
		Book book = loader.load(path);

		IfolorToScribusMain f = new IfolorToScribusMain(book);

		File outFile = TestData.getTestOutputPath();

		f.process(outFile);
	}

}
