package org.alder.fotobuchconvert.ifolor.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class Image extends AbstractElement {
	@XmlElement
	public String OrigFilePath;// <OrigFilePath><![CDATA[data\9ac23b2ee4f146678aaed92ab627d786]]
	@XmlElement
	public String PreviewFilePath;// <OrigFilePath><![CDATA[data\9ac23b2ee4f146678aaed92ab627d786]]
	@XmlElement
	public String SourceFilePath;// <OrigFilePath><![CDATA[data\9ac23b2ee4f146678aaed92ab627d786]]

	@XmlElement
	public VisiblePart VisiblePart;

	@XmlElement
	public Border Border;
	@XmlElement
	public AlphaSet AlphaSet;
}
