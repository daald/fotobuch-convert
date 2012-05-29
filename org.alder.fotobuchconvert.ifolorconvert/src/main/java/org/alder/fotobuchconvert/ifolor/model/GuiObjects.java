package org.alder.fotobuchconvert.ifolor.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class GuiObjects {
	// @XmlMixed
	@XmlElementRefs({ @XmlElementRef(name = "Image", type = Image.class),
			@XmlElementRef(name = "Text", type = Text.class) })
	List<AbstractElement> e;
}
