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

import java.util.ArrayList;

import android.content.Context;
import at.ac.univie.nappingplayer.R;
import at.ac.univie.nappingplayer.views.VideoButtonView;

public class VideoGroup {
	
	// Colors from Color Brewer http://colorbrewer2.org/
	private static final String[] COLOR_ARRAY = {
		"#8DD3C7",
		"#FFFFB3",
		"#BEBADA",
		"#FB8072",
		"#80B1D3",
		"#FDB462",
		"#B3DE69",
		"#FCCDE5",
		"#BC80BD",
		"#CCEBC5",
		"#FFED6F",
		"#8DD3C7",
		"#FFFFB3",
		"#BEBADA",
		"#FB8072",
		"#80B1D3",
		"#FDB462",
		"#B3DE69",
		"#FCCDE5",
		"#BC80BD",
		"#CCEBC5",
		"#FFED6F",
	};
	
	private int id;
	private ArrayList<String> mKeywords;
	private ArrayList<VideoButtonView> mVideos;
	private Context mContext;
	private String mColor;
	
	public VideoGroup(int id, Context ctx) {
		this.id = id;
		this.mContext = ctx;
		mKeywords = new ArrayList<String>();
		mVideos = new ArrayList<VideoButtonView>();
		this.mColor = getColorForId(id);
	}
	
	/**
	 * Adds a keyword to this VideoGroup
	 */
	public void addKeyword(String keyword) {
		// strip leading and trailing whitespace
		keyword = keyword.trim();
		// check if list doesn't already contain string
		if (!mKeywords.contains(keyword)) {
			this.mKeywords.add(keyword);			
		}
	}
	
	public ArrayList<String> getKeywords() {
		return mKeywords;
	}
	
	public void removeKeyword(String keyword) {
		this.mKeywords.remove(keyword);
	}
	
	public String getKeywordsAsString() {
		String ret = "";
		if (mKeywords.isEmpty()) {
			return "";
		} else {
			for (String keyword : mKeywords) {
				ret += keyword;
				ret += ", ";
			}
			return ret;			
		}
	}
	
	public void addVideo(VideoButtonView video) {
		this.mVideos.add(video);
	}
	
	public void removeVideo(VideoButtonView video) {
		this.mVideos.remove(video);
	}
	
	public ArrayList<VideoButtonView> getVideoButtons() {
		return mVideos;
	}
	
	public String getVideoListAsString() {
		// TODO
		return "";
	}
	
	public String getColor() {
		return mColor;
	}
	
	public int getId() {
		return id;
	}
	
	public static String getColorForId(int id) {
		if (id < COLOR_ARRAY.length) {
			return COLOR_ARRAY[id];			
		} else {
			return COLOR_ARRAY[0]; // safeguard for when ID is too high
		}
	}

	
	/**
	 * Textual representation of the group for the navigation menu
	 */
	@Override
	public String toString() {
		String gp = (String) mContext.getString(R.string.group);
		return gp + " " + (this.id + 1);
	}

}
