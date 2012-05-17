package org.alder.fotobuchconvert.ifolor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.swing.text.BadLocationException;

import org.alder.fotobuchconvert.objects.Book;
import org.alder.fotobuchconvert.objects.BookElement;
import org.alder.fotobuchconvert.objects.BookPage;
import org.alder.fotobuchconvert.objects.BookPicture;
import org.alder.fotobuchconvert.objects.BookShape;
import org.alder.fotobuchconvert.objects.BookText;
import org.alder.fotobuchconvert.objects.Border;
import org.alder.fotobuchconvert.objects.Border.HeavyBorder;
import org.alder.fotobuchconvert.objects.Border.LineBorder;
import org.alder.fotobuchconvert.objects.Shadow;
import org.alder.fotobuchconvert.objects.Shadow.SoftShadow;
import org.alder.fotobuchconvert.scribus.RtfToScribusConverter;
import org.alder.fotobuchconvert.scribus.SVGShadowManager;
import org.alder.fotobuchconvert.scribus.ScribusWriter;
import org.alder.fotobuchconvert.scribus.ScribusWriter.PageDims;
import org.alder.fotobuchconvert.scribus.ScribusWriter.ScribusImg;
import org.alder.fotobuchconvert.scribus.ScribusWriter.ScribusImgFrame;
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

				double elX = oX + oF * el.left;
				double elY = oY + oF * el.top;
				double elW = oF * el.width;
				double elH = oF * el.height;

				if (el instanceof BookPicture) {
					BookPicture pic = (BookPicture) el;
					File imgFile = pic.getImageFile(book);

					if (pic.shadow != null)
						addShadow(wr, pic.shadow, elX, elY, elW, elH,
								el.angleDegrees);

					try {
						String imgFilePath = imgFile != null ? imgFile
								.getAbsolutePath() : null;
						ScribusImg scrimg = wr.addImage(imgFilePath);

						scrimg.setPositionCenterRot(elX, elY, elW, elH,
								el.angleDegrees);
						if (imgFilePath != null)
							scrimg.setCropPct(pic.cropX, pic.cropY, pic.cropW,
									pic.cropH);

						if (pic.border instanceof Border.LineBorder) {
							Border.LineBorder border = (LineBorder) pic.border;
							scrimg.setBorder(border.width, border.color);
						} else if (pic.border instanceof Border.HeavyBorder) {
							Border.HeavyBorder border = (HeavyBorder) pic.border;

							ScribusImgFrame frame = scrimg.addPictureFrame(elX,
									elY, elW, elH, el.angleDegrees,
									border.width, 0);

							frame.setBorder(2, Color.GRAY);
							frame.setFill(border.color);
							frame.setTransparency(border.transparency);
						}

						if (imgFile == null)
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

					scrtext.setPositionCenterRot(elX, elY, elW, elH,
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

					out.setPositionCenterRot(elX, elY, elW, elH,
							el.angleDegrees);
					if (shape.colors.length == 1)
						out.setFill(wr.colorManager
								.getColorName(shape.colors[0].color));
					else
						out.setGradient(shape.colors);

					placeHolder = false;
				}

				if (placeHolder) {
					ScribusShape shape = wr.addShape();
					shape.setPositionCenterRot(elX, elY, elW, elH,
							el.angleDegrees);
					shape.setBorder();

					final double bw = 5;

					shape = wr.addShape();
					shape.setPositionCenterRot(elX + bw, elY + bw,
							elW - 2 * bw, elH - 2 * bw, el.angleDegrees);
					shape.setBorder();
				}
			}

			// pageBorders(g);

			wrpg += 2;
		}

		wr.finish();
	}

	private void addShadow(ScribusWriter wr, Shadow shadow, double elX,
			double elY, double elW, double elH, int angleDegrees)
			throws IOException {

		final double cx = elX + elW / 2, cy = elY + elH / 2;

		elX = ((elX - cx) * shadow.scale + cx) + shadow.rx;
		elY = ((elY - cy) * shadow.scale + cy) + shadow.ry;
		elW = elW * shadow.scale;
		elH = elH * shadow.scale;

		if (shadow instanceof Shadow.HardShadow) {
			ScribusShape scshadow = wr.addShape();
			scshadow.setPosition(elX, elY, elW, elH, angleDegrees);
			scshadow.setFill(Color.BLACK);// , shadow.transparency);
		} else if (shadow instanceof Shadow.SoftShadow) {
			Shadow.SoftShadow sshadow = (SoftShadow) shadow;

			File file = SVGShadowManager.getInstance().get((int) elW,
					(int) elH, sshadow.softedge);
			System.out.println(file);
			ScribusImg scshadow = wr.addImage(file.getAbsolutePath());
			scshadow.setPosition(elX, elY, elW, elH, angleDegrees);
			scshadow.setAutoScale(false);
		}
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
