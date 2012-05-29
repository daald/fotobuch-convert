package org.alder.fotobuchconvert.ifolor.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class Text extends AbstractElement {
	@XmlElement
	public String OrigFilePath;// <OrigFilePath><![CDATA[data\9ac23b2ee4f146678aaed92ab627d786]]
}
