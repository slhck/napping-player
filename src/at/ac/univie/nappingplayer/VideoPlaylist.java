package at.ac.univie.nappingplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.util.Log;

public abstract class VideoPlaylist {

	private static final String TAG = VideoPlaylist.class.getSimpleName();
	
	static ArrayList<File> sFiles;
	private static int sCurrentVideo;
	private static int sState;
	
	public static final int STATE_INITIALIZED = 1;
	public static final int STATE_PLAYING = 2;
	public static final int STATE_FINISHED = 3;
	
	public static void initialize(File[] files) {
		sFiles = new ArrayList<File>(Arrays.asList(files));
		sCurrentVideo = 0;
		sState = STATE_INITIALIZED;
	}
	
	/**
	 * Returns a file reference for the given ID, in the order the files are read (whatever that is)
	 */
	public static File getVideo(int id) {
		return sFiles.get(id);
	}
	
	public static boolean hasNext() {
		Log.d(TAG, "Has next? Current video id: " + sCurrentVideo + ", size: " + sFiles.size());
		if (sState == STATE_FINISHED) {
			return false;
		} else {
			return (sCurrentVideo < (sFiles.size() - 1));
		}
	}
	
	public static boolean incrementToNext() {
		sState = STATE_PLAYING;
		if (hasNext()) {
			sCurrentVideo++;
			return true;
		} else {
			sState = STATE_FINISHED;
			return false;
		}
	}
	
	public static File getCurrentVideo() {
		if (sState != STATE_FINISHED) {
			sState = STATE_PLAYING;
			return sFiles.get(sCurrentVideo);			
		} else {
			return null;
		}
	}
		
	public static int getCurrentVideoId() {
		if (sState != STATE_FINISHED) {
			sState = STATE_PLAYING;
			return sCurrentVideo;			
		} else {
			return -1;
		}
	}
	
	public static void reset() {
		sCurrentVideo = 0;
		sState = STATE_INITIALIZED;
	}
	
	public static int getState() {
		return sState;
	}
	
}
