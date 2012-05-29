package org.alder.fotobuchconvert.ifolor.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class ColorRectangle extends AbstractElement {
	@XmlElementWrapper(name = "Colors")
	@XmlElement(name = "Color")
	public List<Color> colors;
}
