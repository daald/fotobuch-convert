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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BookElement {

	protected final Log log = LogFactory.getLog(getClass());

	public final int left, top, width, height;
	public final int angleDegrees;

	public BookElement(int left, int top, int width, int height,
			int angleDegrees) {
		this.left = left;
		this.top = top;
		this.width = width;
		this.height = height;
		this.angleDegrees = angleDegrees;
	}

	public boolean isInternalObject() {
		return false;
	}

}
