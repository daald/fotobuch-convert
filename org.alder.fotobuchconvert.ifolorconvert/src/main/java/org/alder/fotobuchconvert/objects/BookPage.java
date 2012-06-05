/*******************************************************************************
 * Copyright (c) 2012 Daniel Alder.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Daniel Alder - initial API and implementation
 ******************************************************************************/
package org.alder.fotobuchconvert.objects;

import java.util.Vector;

public class BookPage {

	public final Vector<BookElement> pics = new Vector<BookElement>();

	public final int lowerPageNumber;
	public final boolean leftPageEnabled, rightPageEnabled;

	public final double width, height;
	public final int dpi;

	public BookPage(int lowerPageNumber, double width, double height, int dpi,
			boolean leftPageEnabled, boolean rightPageEnabled) {
		this.lowerPageNumber = lowerPageNumber;

		this.width = width;
		this.height = height;
		this.dpi = dpi;

		this.leftPageEnabled = leftPageEnabled;
		this.rightPageEnabled = rightPageEnabled;
	}

	public void add(BookElement pic) {
		pics.add(pic);
	}

}
