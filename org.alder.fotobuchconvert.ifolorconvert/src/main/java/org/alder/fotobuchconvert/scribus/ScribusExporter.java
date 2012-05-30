package org.alder.fotobuchconvert.scribus;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.text.BadLocationException;

import org.alder.fotobuchconvert.objects.Book;
import org.alder.fotobuchconvert.objects.BookElement;
import org.alder.fotobuchconvert.objects.BookPage;
import org.alder.fotobuchconvert.objects.BookPicture;
import org.alder.fotobuchconvert.objects.BookRtfText;
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
import org.alder.fotobuchconvert.scribus.ScribusWriter.ScribusObject;
import org.alder.fotobuchconvert.scribus.ScribusWriter.ScribusShape;
import org.alder.fotobuchconvert.scribus.ScribusWriter.ScribusText;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ScribusExporter {

	private final Log log = LogFactory.getLog(ScribusExporter.class);

	public void export(File outFile, Book _book) throws IOException,
			BadLocationException {
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
		double ifolorPt2scribusPtFactor = 1 / (7062d / 2d / 847.44d * 300d / ifolorDPI);

		double pageW = (ifolorDoubleWidth / 2) * ifolorPt2scribusPtFactor;
		double pageH = ifolorHeight * ifolorPt2scribusPtFactor;
		double oF = ifolorPt2scribusPtFactor;

		// adjust for bleeding
		pageW -= bleed;
		pageH -= bleed * 2;

		ScribusWriter wr = new ScribusWriter(outFile, margin, bleed,
				pageFormat, pageW, pageH);

		wr.addPage("Front", pageW, pageH);

		for (BookPage _page : _book.pages) {
			int wrpg = _page.lowerPageNumber;

			log.info("Exporting page " + wrpg);

			PageDims pd = wr.addPage("Normal", pageW, pageH);// left
			wr.addPage("Normal", pageW, pageH);// right

			double oX = pd.docbaseX - bleed;
			double oY = pd.docbaseY - bleed;

			exportPage(wr, _book, oF, _page, wrpg, oX, oY);
		}

		{
			log.info("Exporting cover page");

			PageDims pd = wr.addPage("Cover", pageW, pageH);// left
			wr.addPage("Cover", pageW, pageH);// right

			double oX = pd.docbaseX - bleed;
			double oY = pd.docbaseY - bleed;

			exportPage(wr, _book, oF, _book.cover, -1, oX, oY);
		}

		wr.finish();
	}

	private void exportPage(ScribusWriter wr, Book _book, double oF,
			BookPage _page, int wrpg, double oX, double oY) throws IOException,
			BadLocationException {
		for (BookElement _el : _page.pics) {
			if (_el.isInternalObject())
				continue;

			log.info("  Exporting element " + _el);

			boolean placeHolder = true;

			final double elX = oX + oF * _el.left;
			final double elY = oY + oF * _el.top;
			final double elW = oF * _el.width;
			final double elH = oF * _el.height;

			if (_el instanceof BookPicture) {
				BookPicture _pic = (BookPicture) _el;

				File imgFile = _pic.getImageFile(_book);

				ScribusObject group = null;

				if (_pic.shadow != null)
					group = addShadow(wr, _pic.shadow, elX, elY, elW, elH,
							_el.angleDegrees);

				try {
					String imgFilePath = imgFile != null ? imgFile
							.getAbsolutePath() : null;
					ScribusImg scrimg = wr.addImage(imgFilePath);

					if (group != null)
						scrimg.setGroup(group.getGroup());

					if (_pic.alpha != null)
						scrimg.setPOCoords(_pic.alpha.get(elW, elH));

					scrimg.setPositionCenterRot(elX, elY, elW, elH,
							_el.angleDegrees);
					if (imgFilePath != null)
						scrimg.setCropPct(_pic.cropX, _pic.cropY, _pic.cropW,
								_pic.cropH);

					if (_pic.border instanceof Border.LineBorder) {
						Border.LineBorder border = (LineBorder) _pic.border;
						scrimg.setBorder(border.width, border.color);
					} else if (_pic.border instanceof Border.HeavyBorder) {
						Border.HeavyBorder border = (HeavyBorder) _pic.border;

						ScribusImgFrame frame = scrimg.addPictureFrame(elX,
								elY, elW, elH, _el.angleDegrees, border.width,
								0);

						frame.setBorder(2, Color.GRAY);
						frame.setFill(border.color);
						frame.setTransparency(border.transparency);
					} else if (_pic.border instanceof Border.ScratchBorder) {
						Border.ScratchBorder border = (ScratchBorder) _pic.border;

						ScribusImgScratchFrame frame = scrimg.addScratchFrame(
								elX, elY, elW, elH, _el.angleDegrees,
								border.innerWidth, border.outerWidth);

						frame.setBorder(0, Color.GRAY);
						frame.setFill(Color.WHITE);
					}

					if (imgFile == null)
						log.warn("Empty picture in page " + wrpg);

					placeHolder = false;
				} catch (IOException e) {
					log.error(
							"Cannot load Image " + imgFile + " ("
									+ _pic.getSourceName(_book)
									+ "). Drawing not possible", e);
				}

			} else if (_el instanceof BookText) {
				BookText _text = (BookText) _el;

				ScribusText scrtext = wr.addText();

				scrtext.setPositionCenterRot(elX, elY, elW, elH,
						_el.angleDegrees);

				if (_el instanceof BookRtfText) {
					String rtftxt = ((BookRtfText) _text).getRtfText(_book);
					if (rtftxt != null) {
						RtfToScribusConverter rtfConv = new RtfToScribusConverter();
						rtfConv.convert(scrtext.getElement(), rtftxt, wr);
					} else
						log.warn("Empty RTF text in page " + wrpg);
				} else {
					String txt = _text.getText(_book);
					scrtext.setText(txt);
				}

				placeHolder = false;

			} else if (_el instanceof BookShape) {
				BookShape _shape = (BookShape) _el;

				ScribusShape out = wr.addShape();

				out.setPositionCenterRot(elX, elY, elW, elH, _el.angleDegrees);
				if (_shape.colors.length == 1)
					out.setFill(wr.colorManager
							.getColorName(_shape.colors[0].color));
				else
					out.setGradient(_shape.colors);

				placeHolder = false;
			}

			if (placeHolder) {
				ScribusShape shape = wr.addShape();
				shape.setPositionCenterRot(elX, elY, elW, elH, _el.angleDegrees);
				shape.setBorder(0, Color.BLACK);

				final double bw = 5;

				shape = wr.addShape();
				shape.setPositionCenterRot(elX + bw, elY + bw, elW - 2 * bw,
						elH - 2 * bw, _el.angleDegrees);
				shape.setBorder(0, Color.BLACK);
			}
		}
	}

	private ScribusObject addShadow(ScribusWriter wr, Shadow _shadow,
			double elX, double elY, double elW, double elH, int angleDegrees)
			throws IOException {

		final double cx = elX + elW / 2, cy = elY + elH / 2;

		elX = ((elX - cx) * _shadow.scale + cx) + _shadow.rx;
		elY = ((elY - cy) * _shadow.scale + cy) + _shadow.ry;
		elW = elW * _shadow.scale;
		elH = elH * _shadow.scale;

		if (_shadow instanceof Shadow.HardShadow) {
			ScribusShape scshadow = wr.addShape();
			scshadow.setPositionCenterRot(elX, elY, elW, elH, angleDegrees);
			scshadow.setFill(Color.BLACK);// , shadow.transparency);

			return scshadow;
		} else if (_shadow instanceof Shadow.SoftShadow) {
			Shadow.SoftShadow sshadow = (SoftShadow) _shadow;

			File file = SVGShadowManager.getInstance().get((int) elW,
					(int) elH, sshadow.softedge);
			log.debug("shadow file " + file);
			ScribusImg scshadow = wr.addImage(file.getAbsolutePath());
			scshadow.setPositionCenterRot(elX, elY, elW, elH, angleDegrees);
			scshadow.setAutoScale(false);

			return scshadow;
		}

		return null;
	}

}
