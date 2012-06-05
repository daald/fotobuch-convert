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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class AbstractElement {
	@XmlAttribute
	public String quality;// ="Good"
	@XmlAttribute
	public int enhancement;// ="1"
	@XmlAttribute
	public int modified;// ="0"
	@XmlAttribute
	public String id;// ="I01"
	@XmlAttribute
	public int left, top, width, height, rotateAngle;
	@XmlAttribute
	public int applyOffset;// ="0"
	@XmlAttribute
	public int editable;// ="1"
	@XmlAttribute
	public int dragable;// ="1"
	@XmlAttribute
	public String dock;// ="middle", (only "top" on cover pages)
	@XmlAttribute
	public int designer;// ="0"

}
