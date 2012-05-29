package org.alder.fotobuchconvert.ifolor.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class Pages {
	@XmlElement(name = "ProjectPage")
	public List<ProjectPage> pages;
}
