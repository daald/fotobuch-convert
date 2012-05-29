package org.alder.fotobuchconvert.ifolor.model;

import javax.xml.bind.annotation.XmlElement;

public class Sheet {
	@XmlElement
	double Width, Height;
	@XmlElement
	int Dpi = 300;
}
