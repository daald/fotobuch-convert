package org.alder.fotobuchconvert.objects;

import java.util.Vector;

public class BookPage {

	public final Vector<BookElement> pics = new Vector<BookElement>();

	public void add(BookElement pic) {
		pics.add(pic);
	}

}
