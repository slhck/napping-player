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
	
	public void addKeyword(String keyword) {
		this.mKeywords.add(keyword);
	}
	
	public ArrayList<String> getKeywords() {
		return mKeywords;
	}
	
	public void removeKeyword(String keyword) {
		this.mKeywords.remove(keyword);
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
