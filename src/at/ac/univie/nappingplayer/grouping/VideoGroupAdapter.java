// Copyright (C) 2013 Werner Robitza
//
// This file is part of NappingPlayer.
//
// NappingPlayer is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version. 
//
// NappingPlayer is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with NappingPlayer.  If not, see <http://www.gnu.org/licenses/>.
//
// NappingPlayer was written at the University of Vienna by Werner Robitza.

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
