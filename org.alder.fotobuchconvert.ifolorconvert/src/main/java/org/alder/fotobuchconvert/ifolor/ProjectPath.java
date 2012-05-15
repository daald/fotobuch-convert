package org.alder.fotobuchconvert.ifolor;

import java.io.File;
import java.io.IOException;

public class ProjectPath {
	public final File projectFile;
	public final File projectFolder;
	public final File dataFolder, previewFolder, thumbsFolder;

	public ProjectPath(File file) throws IOException {
		this.projectFile = file;

		String baseName = file.getPath();
		if (baseName.endsWith(".xml"))
			baseName = baseName.substring(0, baseName.length() - 4);
		if (baseName.endsWith(".dpp"))
			baseName = baseName.substring(0, baseName.length() - 4);

		this.projectFolder = new File(baseName + " Data");

		if (!this.projectFolder.exists())
			throw new IOException("Project folder not found");

		dataFolder = new File(projectFolder, "data");
		previewFolder = new File(projectFolder, "preview");
		thumbsFolder = new File(projectFolder, "thumb");
	}

}
