package org.alder.fotobuchconvert.objects;

import java.util.Vector;

public class BookPage {

	public final Vector<BookElement> pics = new Vector<BookElement>();

	public final int lowerPageNumber;
	private final boolean leftPageEnabled, rightPageEnabled;

	public BookPage(int lowerPageNumber, boolean leftPageEnabled,
			boolean rightPageEnabled) {
		this.lowerPageNumber = lowerPageNumber;
		this.leftPageEnabled = leftPageEnabled;
		this.rightPageEnabled = rightPageEnabled;
	}

	public void add(BookElement pic) {
		pics.add(pic);
	}

}
