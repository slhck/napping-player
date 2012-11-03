package at.ac.univie.nappingplayer.views;

import java.io.File;

public class DataExplorerChild {
	
	public static final int TAG_UNKNOWN = -1;
	public static final int TAG_VIDEO 	= 0;
	public static final int TAG_TEXT 	= 1;
	public static final int TAG_IMAGE 	= 2;
	
	private String name;
	private int tag;
	private File file;
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
}
