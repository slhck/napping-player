package at.ac.univie.nappingplayer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

public abstract class IOUtil {
	private static final String TAG = IOUtil.class.getSimpleName();
	private static final String[] RECV_MAIL = { "entertainment.computing@gmail.com" };
	
	/**
	 * Inverts a bitmap's colors
	 */
	public static Bitmap invert(Bitmap src) {
		Bitmap output = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
				src.getConfig());
		int A, R, G, B;
		int pixelColor;
		int height = src.getHeight();
		int width = src.getWidth();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixelColor = src.getPixel(x, y);
				A = Color.alpha(pixelColor);
				R = 255 - Color.red(pixelColor);
				G = 255 - Color.green(pixelColor);
				B = 255 - Color.blue(pixelColor);
				output.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}
		return output;
	}

	/**
	 * Saves the screenshot from a bitmap to a file on the SD card
	 * @return A reference to the saved file
	 */
	public static File saveScreenshot(Bitmap bmp, String name) {
		Log.d(TAG, "Logging screenshot for user " + name);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
		String date = dateFormat.format(new Date());
		String logName = date + "-" + name + ".jpg";
		File screenshotFile = new File(Configuration.sFolderLogs, logName);
		OutputStream fout = null;
		try {
			fout = new FileOutputStream(screenshotFile);
			bmp.compress(Bitmap.CompressFormat.JPEG, 80, fout);
			fout.flush();
			fout.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return screenshotFile;
	}

	public static void exportPositions(ArrayList<VideoButtonView> buttons,
			String name) {
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

	/**
	 * Sends the passed file per mail
	 */
	public static void sendFilePerMail(File file, String name, Context context) {
		Log.d(TAG, "Sending e-mail from file " + file.toString() + " for user "
				+ name);
		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(Intent.EXTRA_SUBJECT,
				"Napping Activity Result from " + name);
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Napping Activity Result from "
				+ name);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, RECV_MAIL);
		emailIntent.putExtra(Intent.EXTRA_STREAM,
				Uri.parse("file://" + file.toString()));
		// context.startActivity(Intent.createChooser(emailIntent,
		// "Select an app"));
		context.startActivity(emailIntent);
	}
}
