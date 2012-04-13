package org.alder.fotobuchconvert.ifolorconvert;

import java.io.File;

public class ProjectPath {
	public final File projectFile;
	public final File projectFolder;
	public final File dataFolder, previewFolder, thumbsFolder;

	public ProjectPath(String path) {
		projectFile = new File(path + ".dpp");
		projectFolder = new File(path + " Data");
		dataFolder = new File(projectFolder, "data");
		previewFolder = new File(projectFolder, "preview");
		thumbsFolder = new File(projectFolder, "thumb");
	}
}
