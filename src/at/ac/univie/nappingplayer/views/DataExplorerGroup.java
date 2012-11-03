package at.ac.univie.nappingplayer.views;

import java.util.ArrayList;

public class DataExplorerGroup {
	private String Name;
	private ArrayList<DataExplorerChild> Items;
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		this.Name = name;
	}
	public ArrayList<DataExplorerChild> getItems() {
		return Items;
	}
	public void setItems(ArrayList<DataExplorerChild> Items) {
		this.Items = Items;
	}
}
