package org.alder.fotobuchconvert.ifolorencryption;

import java.util.HashSet;

import org.alder.fotobuchconvert.ifolorconvert.Book;
import org.alder.fotobuchconvert.ifolorconvert.BookElement;
import org.alder.fotobuchconvert.ifolorconvert.BookPage;
import org.alder.fotobuchconvert.ifolorconvert.BookPicture;
import org.alder.fotobuchconvert.ifolorconvert.Loader;
import org.alder.fotobuchconvert.ifolorconvert.ProjectPath;
import org.alder.fotobuchconvert.ifolorconvert.TestData;

public class ListFiles {
	public static void main(String[] args) throws Exception {

		Loader loader = new Loader();
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
