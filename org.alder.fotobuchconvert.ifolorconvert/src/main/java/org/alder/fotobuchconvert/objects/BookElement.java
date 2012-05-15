package org.alder.fotobuchconvert.objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BookElement {

	protected final Log log = LogFactory.getLog(getClass());

	public final int left, top, width, height;
	public final int angleDegrees;
	public final boolean dragable;

	public BookElement(int left, int top, int width, int height,
			int angleDegrees, boolean dragable) {
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
		this.angleDegrees = angleDegrees;
		this.dragable = dragable;
	}

	public boolean isInternalObject() {
		return false;
	}

}