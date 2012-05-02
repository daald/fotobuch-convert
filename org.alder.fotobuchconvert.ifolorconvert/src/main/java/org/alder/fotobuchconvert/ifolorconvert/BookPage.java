package org.alder.fotobuchconvert.ifolorconvert;

import java.util.Vector;

public class BookPage {

	Vector<BookElement> pics = new Vector<BookElement>();

	public void add(BookElement pic) {
		pics.add(pic);
	}

}
