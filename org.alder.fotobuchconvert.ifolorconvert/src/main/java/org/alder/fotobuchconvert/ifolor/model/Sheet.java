package org.alder.fotobuchconvert.ifolor.model;

import javax.xml.bind.annotation.XmlElement;

public class Sheet {
	@XmlElement
	public double Width, Height;
	@XmlElement
	public int Dpi = 300;
}
