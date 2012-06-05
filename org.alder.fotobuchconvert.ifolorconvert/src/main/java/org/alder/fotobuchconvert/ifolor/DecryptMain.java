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

import java.io.File;
import java.io.IOException;


public class DecryptMain {
	// private static final String CHARSET_ISO_8859_1 = "ISO_8859_1";
	private static final String CHARSET_cp1252 = "Windows-1252";

	public static void main(String[] args) throws IOException {
		Decryptor decryptor = new Decryptor();
		File projectFile = TestData.getTestProject().projectFile;
		byte[] bytes = decryptor.loadBinaryFile(projectFile, "DPP");
		System.out.println();
		System.out.print(new String(bytes, 0, bytes.length, CHARSET_cp1252));
	}
}
