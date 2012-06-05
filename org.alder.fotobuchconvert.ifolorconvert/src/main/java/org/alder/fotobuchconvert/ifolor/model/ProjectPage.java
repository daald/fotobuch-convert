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
package org.alder.fotobuchconvert.ifolor.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class ProjectPage {
	@XmlElement
	public Sheet Sheet;

	@XmlElement
	public String LeftPageEnabled, RightPageEnabled;

	@XmlElement
	public PageNumber LeftPageNumber, RightPageNumber;

	@XmlElementWrapper(name = "GuiObjects")
	@XmlElements({ @XmlElement(name = "Image", type = Image.class),
			@XmlElement(name = "Text", type = Text.class),
			@XmlElement(name = "ColorRectangle", type = ColorRectangle.class)

	})
	public List<AbstractElement> GuiObjects;

}
