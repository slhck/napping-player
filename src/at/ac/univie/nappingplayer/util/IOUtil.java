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

package at.ac.univie.nappingplayer.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import at.ac.univie.nappingplayer.VideoPlaylist;
import at.ac.univie.nappingplayer.grouping.VideoGroup;
import at.ac.univie.nappingplayer.views.VideoButtonView;

public abstract class IOUtil {
	private static final String TAG = IOUtil.class.getSimpleName();
	private static final String SEP = ",";

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
	 * Exports device configuration
	 */
	public static File saveConfiguration(String name, String date) {
		Log.d(TAG, "Logging configuration for user " + name + " at " + date);
		String logName = date + "-" + name + "-config" + ".csv";
		File configurationFile = new File(Configuration.getLogFolder(), logName);
		FileWriter fw = null;
		BufferedWriter bw = null;
		Log.d(TAG, "Trying to write to file " + configurationFile);
		try {
			configurationFile.createNewFile();
			fw = new FileWriter(configurationFile);
			bw = new BufferedWriter(fw);
			bw.write("name" + SEP + name);
			bw.newLine();
			bw.write("screen-width"  + SEP + Configuration.getWidth());
			bw.newLine();
			bw.write("screen-height"  + SEP + Configuration.getHeight());
			bw.newLine();
			bw.write("release"  + SEP + Build.VERSION.RELEASE);
			bw.newLine();
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
		return configurationFile;
	}

	/**
	 * Saves the positions of the videos on screen
	 */
	public static File exportPositions(ArrayList<VideoButtonView> buttons,
			String name, String date) {
		Log.d(TAG, "Logging results for user " + name + " at " + date);
		String logName = date + "-" + name + "-videos.csv";
		File logFile = new File(Configuration.getLogFolder(), logName);
		FileWriter fw = null;
		BufferedWriter bw = null;
		Log.d(TAG, "Trying to write to file " + logFile);
		try {
			logFile.createNewFile();
			fw = new FileWriter(logFile);
			bw = new BufferedWriter(fw);
			
			bw.write("video_id" + SEP + "file" + SEP + "x" + SEP + "y");
			bw.newLine();
			
			for (VideoButtonView button : buttons) {
				Log.d(TAG, "Writing button " + button.getLabel() + " with position " + button.getTop() + "/" + button.getLeft());
				String videoName = VideoPlaylist.getVideo(button.getVideoId()).toString();
				String line = "" + button.getVideoId() + SEP + videoName + SEP + button.getLeft() + SEP + button.getTop();
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
					Log.e(TAG, "Couldn't close file / buffered writer" + e.toString());
					e.printStackTrace();
				}
			}
		}
		return logFile;
	}

	/**
	 * Saves the group associations to a file on the SD card
	 *
	 * @return A reference to the saved file
	 */
	public static File exportGroups(ArrayList<VideoGroup> groups, String name, String date) {
		// TODO: 
		Log.d(TAG, "Logging results for user " + name + " at " + date);
		String logName = date + "-" + name + "-groups.csv";
		File logFile = new File(Configuration.getLogFolder(), logName);
		FileWriter fw = null;
		BufferedWriter bw = null;
		Log.d(TAG, "Trying to write to file " + logFile);
		try {
			logFile.createNewFile();
			fw = new FileWriter(logFile);
			bw = new BufferedWriter(fw);
			
			bw.write("group_id" + SEP + "video_id");
			bw.newLine();
			
			for (VideoGroup group : groups) {
				Log.d(TAG, "Writing group videos " + group.toString());
				for (VideoButtonView v : group.getVideoButtons()) {
					String line = group.getId() + SEP + v.getVideoId();
					bw.write(line);
					bw.newLine();
				}
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
		return logFile;
	}
	

	/**
	 * Saves the group keywords to a file on the SD card
	 *
	 * @return A reference to the saved file
	 */
	public static File exportKeywords(ArrayList<VideoGroup> groups, String name, String date) {
		// TODO: 
		Log.d(TAG, "Logging results for user " + name + " at " + date);
		String logName = date + "-" + name + "-keywords.csv";
		File logFile = new File(Configuration.getLogFolder(), logName);
		FileWriter fw = null;
		BufferedWriter bw = null;
		Log.d(TAG, "Trying to write to file " + logFile);
		try {
			logFile.createNewFile();
			fw = new FileWriter(logFile);
			bw = new BufferedWriter(fw);
			
			bw.write("group_id" + SEP + "keyword");
			bw.newLine();
			
			for (VideoGroup group : groups) {
				Log.d(TAG, "Writing group keywords" + group.toString());
				for (String keyword : group.getKeywords()) {
					String line = group.getId() + SEP + keyword;
					bw.write(line);
					bw.newLine();
				}
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
		return logFile;
	}
	
	
	
	/**
	 * Saves the screenshot from a bitmap to a file on the SD card
	 * 
	 * @return A reference to the saved file
	 */
	public static File saveScreenshot(Bitmap bmp, String name, String date) {
		Log.d(TAG, "Logging screenshot for user " + name + " at " + date);
		String logName = date + "-" + name + ".jpg";
		File screenshotFile = new File(Configuration.getLogFolder(), logName);
		OutputStream fout = null;
		try {
			fout = new FileOutputStream(screenshotFile);
			bmp.compress(Bitmap.CompressFormat.JPEG, 80, fout);
			fout.flush();
			fout.close();
		} catch (FileNotFoundException e) {
			Log.e(TAG, "Couldn't find file " + logName);
		} catch (IOException e) {
			Log.e(TAG, "Error while saving file " + logName);
			e.printStackTrace();
		}
		return screenshotFile;
	}

	/**
	 * Sends a number of files per mail to a specified address
	 * @param recipient The e-mail address of the recipient
	 * @param files An ArrayList of files
	 * @param name The name of the observer
	 * @param context The activity context
	 */
	public static void sendFilePerMail(String recipient, ArrayList<File> files, String name, Context context) {
		Log.d(TAG, "Sending e-mail for user " + name);
		
		Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		
		emailIntent.setType("plain/text");
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Napping Activity Result from " + name);
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Napping Activity Result from " + name);
		
		String[] recipientArray = new String[1];
		recipientArray[0] = recipient;
		emailIntent.putExtra(Intent.EXTRA_EMAIL, recipientArray);
		
		// merge all files into one list of URIs
		ArrayList<Uri> uris = new ArrayList<Uri>();
		for (File f : files) {
			uris.add(Uri.parse("file://" + f.toString()));
		}
		
		emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
		context.startActivity(emailIntent);
	}

	/**
	 * Reads a file into a StringBuilder
	 */
	public static StringBuilder readFile(File f) {
		// Read text from file
		StringBuilder text = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;
			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
			br.close();
		} catch (IOException e) {
			Log.e(TAG, "Error while reading file " + f + ": " + e.toString());
		}
		return text;
	}

	/**
	 * Deletes all file from the log folder
	 */
	public static void deleteLogData() {
		File logFolder = Configuration.getLogFolder();
		try {
			for (File f : logFolder.listFiles()) {
				f.delete();
			}
		} catch (Exception e) {
			Log.e(TAG, "Could not delete file from log folder: " + e.toString());
		}
	}
}
