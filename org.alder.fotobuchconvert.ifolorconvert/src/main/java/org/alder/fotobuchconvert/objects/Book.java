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

import org.alder.fotobuchconvert.ifolor.ProjectPath;

public class Book {

	public final Vector<BookPage> pages = new Vector<BookPage>();
	final ProjectPath pathInfo;
	public BookPage cover;

	public Book(ProjectPath pathInfo) {
		this.pathInfo = pathInfo;
	}

	public void add(BookPage pic) {
		pages.add(pic);
	}

	public void reducePagesForTesting() {
		while (pages.size() > 10)
			pages.remove(pages.size() - 1);
	}

}
