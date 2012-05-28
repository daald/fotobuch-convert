package org.alder.fotobuchconvert.objects;

public abstract class BookText extends BookElement {

	public BookText(int left, int top, int width, int height, int angleDegrees) {
		super(left, top, width, height, angleDegrees);
	}

	public abstract String getText(Book book);

}
