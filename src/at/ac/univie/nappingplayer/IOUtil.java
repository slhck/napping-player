package at.ac.univie.nappingplayer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.util.Log;

public abstract class IOUtil {
	private static final String TAG = IOUtil.class.getSimpleName();

	public static void exportPositions(ArrayList<VideoButtonView> buttons, String name) {
		Log.d(TAG, "Logging results for user " + name);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
		String date = dateFormat.format(new Date());
		String logName = date + "-" + name;
		File logFile = new File(Configuration.sFolderLogs, logName);
		FileWriter fw = null;
		BufferedWriter bw = null;
		Log.d(TAG, "Trying to write to file " + logFile);
		try {
			logFile.createNewFile();
			fw = new FileWriter(logFile);
			bw = new BufferedWriter(fw);
			for (VideoButtonView button : buttons) {
				Log.d(TAG,
						"Writing button " + button.mLabel + " with position "
								+ button.getTop() + "/" + button.getLeft());
				String sep = ";";
				String videoName = VideoPlaylist.getVideo(button.mVideoId)
						.toString();
				String line = videoName + sep + button.getLeft() + sep
						+ button.getTop();
				bw.write(line);
				bw.newLine();
			}
		} catch (IOException e) {
			Log.e(TAG, "Couldn't write log file: " + e.toString());
			e.printStackTrace();
		} finally {
			if ((fw != null) && (bw != null)) {
				try {
					bw.flush();
					bw.close();
				} catch (IOException e) {
					Log.e(TAG,
							"Couldn't close file / buffered writer"
									+ e.toString());
					e.printStackTrace();
				}
			}
		}
	}
}
