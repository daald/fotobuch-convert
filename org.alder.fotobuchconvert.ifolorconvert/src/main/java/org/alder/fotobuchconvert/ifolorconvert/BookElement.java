package org.alder.fotobuchconvert.ifolorconvert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BookElement {

	protected final Log log = LogFactory.getLog(getClass());

	protected final int left;
	protected final int top;
	protected final int width;
	protected final int height;
	protected final int angleDegrees;
	protected final boolean dragable;
	protected final IfolorDock dock;

	public BookElement(int left, int top, int width, int height,
			int angleDegrees, boolean dragable, IfolorDock dock) {
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
		this.angleDegrees = angleDegrees;
		this.dragable = dragable;
		this.dock = dock;
	}

	public boolean isInternalObject() {
		return false;
	}

}