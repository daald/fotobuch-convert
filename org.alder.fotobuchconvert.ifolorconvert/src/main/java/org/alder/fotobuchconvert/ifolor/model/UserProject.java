package org.alder.fotobuchconvert.ifolor.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UserProject")
public class UserProject {

	@XmlElement
	public Cover Cover;

	@XmlElement
	public Pages Pages;

}
