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

import java.util.HashSet;

import org.alder.fotobuchconvert.objects.Book;
import org.alder.fotobuchconvert.objects.BookElement;
import org.alder.fotobuchconvert.objects.BookPage;
import org.alder.fotobuchconvert.objects.BookPicture;

public class ListFilesMain {
	public static void main(String[] args) throws Exception {

		IfolorLoader loader = new IfolorLoader();
		ProjectPath path = TestData.getTestProject();
		Book book = loader.load(path);

		HashSet<String> duplicateFind = new HashSet<String>();

		int doubles = 0;
		for (BookPage pg : book.pages)
			for (BookElement el : pg.pics)
				if (el instanceof BookPicture) {
					BookPicture pic = (BookPicture) el;
					String name = pic.getSourceName(book);
					if (name == null || name.isEmpty()
							|| name.startsWith("Images::"))
						continue;

					if (duplicateFind.contains(name)) {
						System.out.println("Double use of file: " + name);
						doubles++;
					}
					System.out.println(name);
					duplicateFind.add(name);
				}

		System.out.println();
		System.out.printf("%s duplicate warnings\n", doubles);
		System.out.printf("%s unique files\n", duplicateFind.size());
	}
}
