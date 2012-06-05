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
