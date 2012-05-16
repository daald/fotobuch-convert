package org.alder.fotobuchconvert.scribus;

import java.io.File;
import java.io.IOException;

import org.alder.fotobuchconvert.ifolor.TestData;
import org.alder.fotobuchconvert.scribus.ScribusWriter.PageDims;

public class ScribusWriterTest {

	public static void main(String[] args) throws IOException {
		File file = TestData.getTestOutputPath();

		int margin = 20;
		int bleed = 20;
		String pageFormat = "Custom";// A4
		int pageW = 500;// A4=595.28;
		int pageH = 500;// A4=841.89;

		ScribusWriter wr = new ScribusWriter(file, margin, bleed, pageFormat,
				pageW, pageH);
		wr.addPage("Normal", pageW, pageH);
		wr.addPage("Normal", pageW, pageH);
		wr.addPage("Normal", pageW, pageH);
		PageDims[] pageDims = wr.getPageDims();

		wr.addImage(
				"/media/reverseengineer_ifolor/xml/Australia/files/Australia2-20120409 Data/preview/f804f95403e1414caa14c8091da326d7")
				.setPosition(400, 400, 100, 100, 0);

		// DEMO: Rotation
		// SHAPE(6)
		wr.addShape().setPosition(800, 300, 100, 100, 0).setBorder();
		wr.addShape().setPosition(800, 300, 100, 100, 5).setBorder();
		wr.addShape().setPosition(800, 300, 100, 100, 10).setBorder();

		/**
		 * PAGEXPOS="100" PAGEYPOS="20" PAGEWIDTH="595.28" PAGEHEIGHT="841.89"
		 */

		for (int pg = 0; pg < 3; pg++) {
			PageDims pd = pageDims[pg];
			// DEMO: Seitenrand
			// liegt auf Seitenecke:
			wr.addShape()
					.setPosition(pd.docbaseX - bleed, pd.docbaseY - bleed, 1,
							1, 0).setBorder();
			// liegt auf roter Ecke:
			wr.addShape().setPosition(pd.docbaseX, pd.docbaseY, 1, 1, 0)
					.setBorder();
			// liegt auf margin (blaue Ecke):
			wr.addShape()
					.setPosition(pd.docbaseX + margin, pd.docbaseY + margin, 1,
							1, 0).setBorder();
		}

		PageDims pd = pageDims[0];
		// DEMO: Anker ist die linke obere Ecke
		wr.addShape()
				.setPosition(pd.docbaseX + 100, pd.docbaseY + 100, 1, 1, 0)
				.setBorder();
		wr.addShape()
				.setPosition(pd.docbaseX + 100, pd.docbaseY + 100, 10, 10, 0)
				.setBorder();
		wr.addShape()
				.setPosition(pd.docbaseX + 100, pd.docbaseY + 100, 100, 100, 0)
				.setBorder();

		wr.finish();
	}

}
