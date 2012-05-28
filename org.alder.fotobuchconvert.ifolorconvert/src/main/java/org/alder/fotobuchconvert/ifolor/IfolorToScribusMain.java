package org.alder.fotobuchconvert.ifolor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;

import org.alder.fotobuchconvert.objects.Book;
import org.alder.fotobuchconvert.scribus.ScribusExporter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IfolorToScribusMain {

	protected final Log log = LogFactory.getLog(getClass());

	public static void main(String[] args) throws Exception {
		ProjectPath path = TestData.getTestProject();

		IfolorLoader loader = new IfolorLoader();
		Book book = loader.load(path);

		book.reducePagesForTesting();

		ScribusExporter f = new ScribusExporter(book);

		File outFile = TestData.getTestOutputPath();

		f.process(outFile);
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
