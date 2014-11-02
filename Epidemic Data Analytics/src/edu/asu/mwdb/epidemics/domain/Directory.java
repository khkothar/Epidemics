package edu.asu.mwdb.epidemics.domain;

import java.io.File;
import java.io.IOException;

public class Directory {

	private int index;
	private String[] files;
	private String path;
	public Directory(String dirPath) throws IOException {
		if (dirPath != null) {
			File dir = new File(dirPath);
			if (!dir.exists()) {
				throw new IOException("Invalid directory path");
			} else {
				this.path = dir.getAbsolutePath();
				init(dir);
			}
		} else {
			throw new NullPointerException("Directory path is NULL");
		}
	}
	
	private void init(File dir) {
		index = 0;
		files = dir.list();
	}

	public synchronized String getNextFile() {
		String fileName = null;
		if(index < files.length)
			fileName = files[index++];
		return fileName;
	}
	
	public String absolutePath() {
		return path;
	}

}
