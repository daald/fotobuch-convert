package org.alder.fotobuchconvert.ifolorconvert;

import java.util.Vector;

public class Book {

	public final Vector<BookPage> pages = new Vector<BookPage>();
	final ProjectPath pathInfo;

	public Book(ProjectPath pathInfo) {
		this.pathInfo = pathInfo;
	}

	public void add(BookPage pic) {
		pages.add(pic);
	}

}
