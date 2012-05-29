package org.alder.fotobuchconvert.ifolor.model;

import javax.xml.bind.annotation.XmlAttribute;

public class Color {
	@XmlAttribute
	public String value;
	@XmlAttribute
	public double position;
	// <Colors><Color value="7F000000" position="0" /></Colors>
}
