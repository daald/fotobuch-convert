package org.alder.fotobuchconvert.scribus;

import java.awt.Color;
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
import org.alder.fotobuchconvert.objects.Border.ScratchBorder;
import org.alder.fotobuchconvert.objects.Shadow;
import org.alder.fotobuchconvert.objects.Shadow.SoftShadow;
import org.alder.fotobuchconvert.scribus.ScribusWriter.PageDims;
import org.alder.fotobuchconvert.scribus.ScribusWriter.ScribusImg;
import org.alder.fotobuchconvert.scribus.ScribusWriter.ScribusImgFrame;
import org.alder.fotobuchconvert.scribus.ScribusWriter.ScribusImgScratchFrame;
import org.alder.fotobuchconvert.scribus.ScribusWriter.ScribusShape;
import org.alder.fotobuchconvert.scribus.ScribusWriter.ScribusText;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ScribusExporter {

	protected final Log log = LogFactory.getLog(ScribusExporter.class);

	private final Book book;

	public ScribusExporter(final Book book) {
		this.book = book;
	}

	public void process(File outFile) throws IOException, BadLocationException {
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

						if (pic.alpha != null)
							scrimg.setPOCoords(pic.alpha.get(elW, elH));

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
						} else if (pic.border instanceof Border.ScratchBorder) {
							Border.ScratchBorder border = (ScratchBorder) pic.border;

							ScribusImgScratchFrame frame = scrimg
									.addScratchFrame(elX, elY, elW, elH,
											el.angleDegrees, border.innerWidth,
											border.outerWidth);

							frame.setBorder(0, Color.GRAY);
							frame.setFill(Color.WHITE);
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
					shape.setBorder(0, Color.BLACK);

					final double bw = 5;

					shape = wr.addShape();
					shape.setPositionCenterRot(elX + bw, elY + bw,
							elW - 2 * bw, elH - 2 * bw, el.angleDegrees);
					shape.setBorder(0, Color.BLACK);
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

}
