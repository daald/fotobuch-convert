package org.alder.fotobuchconvert.ifolor;

import java.io.File;

import org.alder.fotobuchconvert.objects.Book;
import org.alder.fotobuchconvert.scribus.ScribusExporter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IfolorToScribusMain {

	protected final Log log = LogFactory.getLog(IfolorToScribusMain.class);

	public static void main(String[] args) throws Exception {
		ProjectPath path = TestData.getTestProject();

		IfolorLoader loader = new IfolorLoader();
		Book book = loader.load(path);

		book.reducePagesForTesting();

		ScribusExporter f = new ScribusExporter();

		File outFile = TestData.getTestOutputPath();

		f.export(outFile, book);
	}

}
