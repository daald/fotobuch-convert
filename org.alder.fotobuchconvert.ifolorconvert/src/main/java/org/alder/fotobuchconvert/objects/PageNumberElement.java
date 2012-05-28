package org.alder.fotobuchconvert.objects;

public class PageNumberElement extends BookText {

	private final int pageNumer;

	public PageNumberElement(int left, int top, int width, int height,
			int pageNumber) {
		super(left, top, width, height, 0);

		this.pageNumer = pageNumber;
	}

	@Override
	public String getText(Book book) {
		return String.valueOf(pageNumer);
	}
}
