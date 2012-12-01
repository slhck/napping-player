package at.ac.univie.nappingplayer.grouping;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Adapter that lists the groups in the action bar
 * @author werner
 *
 */
public class VideoGroupAdapter extends ArrayAdapter<VideoGroup> {
	private Context mContext;
	private int mResourceId;
	private List<VideoGroup> mVideoGroups;
	
	public VideoGroupAdapter(Context context, int textViewResourceId,List<VideoGroup> objects) {
		super(context, textViewResourceId, objects);
		this.mVideoGroups = objects;
		this.mContext = context;
		this.mResourceId = textViewResourceId;
	}
	
	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		v.setBackgroundColor(Color.parseColor(mVideoGroups.get(position).getColor()));
		return v;
	}
	
	@Override
	public View getDropDownView (int position, View convertView, ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		// v.setBackgroundColor(Color.parseColor(VideoGroup.getColorForId(position)));
		v.setBackgroundColor(Color.parseColor(mVideoGroups.get(position).getColor()));
		return v;
	}
	
	
}
