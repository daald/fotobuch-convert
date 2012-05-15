package org.alder.fotobuchconvert.objects;

import java.util.Vector;

import org.alder.fotobuchconvert.ifolor.ProjectPath;

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