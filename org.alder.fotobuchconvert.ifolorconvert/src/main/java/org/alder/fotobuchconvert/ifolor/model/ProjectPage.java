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
