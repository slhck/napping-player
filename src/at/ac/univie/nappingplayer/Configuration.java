package at.ac.univie.nappingplayer;

import java.io.File;

import android.os.Environment;
import android.util.Log;

public abstract class Configuration {

	private static final String TAG = Configuration.class.getSimpleName();

	public static File sSDcard = null;
	public static File sFolderVideos = null;
	public static File sFolderLogs = null;


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
	public static void initialize() throws Exception {
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
	}
}
