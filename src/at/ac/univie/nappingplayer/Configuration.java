package at.ac.univie.nappingplayer;

import java.io.File;

import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public abstract class Configuration {

	private static final String TAG = Configuration.class.getSimpleName();

	private static File sSDcard = null;
	private static File sFolderVideos = null;
	private static File sFolderLogs = null;
	private static int mScreenWidth;
	private static int mScreenHeight;
	
	/**
	 * Path of the folder in which videos are stored, relative to the SD card
	 * root with a trailing slash. If it does not exist, it will be created
	 * automatically on the SD card.
	 */
	public static final String PATH_VIDEOS 	= new String("NappingMovies/");
	public static final String PATH_LOGS 	= new String("NappingLogs/");
	
	/**List of Files in Video Directory */
	static File[] videos;

	/**
	 * Tries to initialize the SD card, obtain the file handles and then create
	 * folders if they don't exist already.
	 */
	public static void initialize(Context ctx) throws Exception {
		if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			sSDcard = Environment.getExternalStorageDirectory();
			
			sFolderVideos = new File(sSDcard, PATH_VIDEOS);
			sFolderLogs = new File(sSDcard, PATH_LOGS);
			
			// create the data and video directory if they don't exist already
			sFolderVideos.mkdirs();
			sFolderLogs.mkdirs();
			
			//make file list
			videos = sFolderVideos.listFiles();
		} else {
			Log.e(TAG, "Could not initialize SD card");
			throw new Exception("Could not open SD card!");
		}
		
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		
//		TODO: Fix me for upper API levels
//		http://stackoverflow.com/q/1016896/
//		try { 
//			Point size = new Point();
//			display.getSize(size); 
//			mScreenWidth = size.x; 
//			mScreenHeight = size.y; 
//		} catch (NoSuchMethodError e) { 
			mScreenWidth = display.getWidth(); 
			mScreenHeight = display.getHeight(); 
//		}
		
		mScreenWidth = display.getWidth();
		mScreenHeight = display.getHeight();
		
	}
	
	public static File getLogFolder() {
		return sFolderLogs;
	}
	
	public static int getWidth() {
		return mScreenWidth;
	}
	
	public static int getHeight() {
		return mScreenHeight;
	}
	
	
	
	
}
