package org.alder.fotobuchconvert.ifolorconvert;

import java.util.Vector;

public class BookPage {

	Vector<BookPicture> pics = new Vector<BookPicture>();

	public void add(BookPicture pic) {
		pics.add(pic);
	}

}
