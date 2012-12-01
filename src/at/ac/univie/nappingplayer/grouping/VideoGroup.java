package at.ac.univie.nappingplayer.grouping;

import java.util.ArrayList;

import android.content.Context;
import at.ac.univie.nappingplayer.R;
import at.ac.univie.nappingplayer.views.VideoButtonView;

public class VideoGroup {
	
	private int id;
	private ArrayList<String> mKeywords;
	private ArrayList<VideoButtonView> mVideos;
	private Context mContext;
	
	public VideoGroup(int id, Context ctx) {
		this.id = id;
		this.mContext = ctx;
		mKeywords = new ArrayList<String>();
		mVideos = new ArrayList<VideoButtonView>();
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
	
	public int getId() {
		return id;
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
