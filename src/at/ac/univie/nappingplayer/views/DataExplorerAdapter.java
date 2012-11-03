package at.ac.univie.nappingplayer.views;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;
import at.ac.univie.nappingplayer.R;
import at.ac.univie.nappingplayer.R.id;
import at.ac.univie.nappingplayer.R.layout;


public class DataExplorerAdapter extends BaseExpandableListAdapter {
	
	private Context context;
	private ArrayList<DataExplorerGroup> groups;
	
	public DataExplorerAdapter(Context context, ArrayList<DataExplorerGroup> groups) {
		this.context = context;
		this.groups = groups;
	}
	
	public void addItem(DataExplorerChild item, DataExplorerGroup group) {
		if (!groups.contains(group)) {
			groups.add(group);
		}
		int index = groups.indexOf(group);
		ArrayList<DataExplorerChild> ch = groups.get(index).getItems();
		ch.add(item);
		groups.get(index).setItems(ch);
	}
	public DataExplorerChild getChild(int groupPosition, int childPosition) {
		ArrayList<DataExplorerChild> chList = groups.get(groupPosition).getItems();
		return chList.get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		DataExplorerChild child = (DataExplorerChild) getChild(groupPosition, childPosition);
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.data_explorer_child_item, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.tv_child);
		tv.setText(child.getName().toString());
		tv.setTag(child.getTag());
		return view;
	}

	public int getChildrenCount(int groupPosition) {
		ArrayList<DataExplorerChild> chList = groups.get(groupPosition).getItems();
		return chList.size();

	}

	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	public int getGroupCount() {
		return groups.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) {
		DataExplorerGroup group = (DataExplorerGroup) getGroup(groupPosition);
		if (view == null) {
			LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.data_explorer_group_item, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.tv_group);
		tv.setText(group.getName());
		return view;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}


}
