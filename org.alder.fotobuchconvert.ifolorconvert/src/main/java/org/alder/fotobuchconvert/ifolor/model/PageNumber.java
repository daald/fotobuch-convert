package org.alder.fotobuchconvert.ifolor.model;

import javax.xml.bind.annotation.XmlAttribute;

public class PageNumber {
	@XmlAttribute
	public int left, top, width, height;
	@XmlAttribute
	public int visible;
	@XmlAttribute
	public String align, valign;
	@XmlAttribute
	public String fontName;
	@XmlAttribute
	public float fontSize;
	@XmlAttribute
	public int bold, italic, underline;
	@XmlAttribute
	public String color;

	// <RightPageNumber left="3631" top="2304" width="3331"
	// height="200" visible="1" align="outer" valign="bottom"
	// fontName="Arial"
	// fontSize="10" bold="0" italic="0" underline="0" color="Black" />

}
