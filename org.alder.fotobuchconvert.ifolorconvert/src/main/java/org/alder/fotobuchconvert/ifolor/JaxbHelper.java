/*******************************************************************************
 * Copyright (c) 2012 Daniel Alder.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Daniel Alder - initial API and implementation
 ******************************************************************************/
package org.alder.fotobuchconvert.ifolor;

import java.io.ByteArrayInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.alder.fotobuchconvert.ifolor.model.UserProject;

public class JaxbHelper {
	public UserProject loadXml(byte[] bytes) throws JAXBException {
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);

		JAXBContext jc = JAXBContext.newInstance(UserProject.class);
		Unmarshaller carMarshaller = jc.createUnmarshaller();

		Object o = carMarshaller.unmarshal(is);
		UserProject uo = (UserProject) o;

		return uo;
	}
}
