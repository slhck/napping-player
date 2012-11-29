package at.ac.univie.nappingplayer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import at.ac.univie.nappingplayer.util.IOUtil;
import at.ac.univie.nappingplayer.views.VideoButtonView;

/**
 * Main activity for the napping experiment
 * @author werner
 *
 */
public class NappingActivity extends Activity implements StartVideoListener {
	private static final String TAG = NappingActivity.class.getSimpleName();
	public static final int VIDEO_NEXT_REQUEST = 0;
	public static final int VIDEO_SINGLE_REQUEST = 1;
	
	MenuItem mMenuPlayNext;
	MenuItem mMenuGroup;
	MenuItem mMenuFinish;
	
	TextView mInfoText;
	String mName;
	int mCurrentVideoId;
	ArrayList<VideoButtonView> mVideoButtons;

	/**
	 * Called when the activity is first started
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_napping);
		
		View v = findViewById(R.id.layout_napping);
        v.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
		
		Intent intent = getIntent();
		mName = intent.getStringExtra("userName");
		
//		mButtonPlayNext = (Button) findViewById(R.id.button_play_next);
//		mButtonPlayNext.setOnClickListener(mButtonPlayNextListener);
//		mButtonFinish = (Button) findViewById(R.id.button_finish);
//		mButtonFinish.setOnClickListener(mButtonFinishListener);
		
		mInfoText = (TextView) findViewById(R.id.tv_info_message);
		
		mVideoButtons = new ArrayList<VideoButtonView>();
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart called");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause called");
	}
	
	/**
	 * Called when the activity resumes, just reassign current video ID in case we need it or finish up
	 */
	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume called");
		// if we haven't stopped yet just get the current video ID (could be 0
		// to start)
		if (VideoPlaylist.getState() != VideoPlaylist.STATE_FINISHED) {
			if (VideoPlaylist.getState() != VideoPlaylist.STATE_INITIALIZED) {
				showMessage(getText(R.string.drag_around));				
			} else {
				showMessage(getText(R.string.click_play_to_start));
			}
			mCurrentVideoId = VideoPlaylist.getCurrentVideoId();
			Log.d(TAG, "Setting current video ID to " + mCurrentVideoId);
		} else {
			// playlist has finished, we should probably disable the "play next" button
			Log.d(TAG, "Playlist finished.");
			showMessage(getText(R.string.seen_all));
//			mButtonPlayNext.setVisibility(View.GONE);
//			mButtonFinish.setVisibility(View.VISIBLE);
			mMenuPlayNext.setEnabled(false);
			mMenuGroup.setEnabled(true);
			mMenuFinish.setEnabled(true);
			
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.menu_play_next:
				playNext();
				return true;
			case R.id.menu_finish:
				finishExperiment();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Defines what the "play next" button does                                                      
	 */
	private OnClickListener mButtonPlayNextListener = new OnClickListener() {
		public void onClick(View v) {
			playNext();
		}
	};
	
	/**
	 * Defines what the "finish" button does                                                      
	 */
	private OnClickListener mButtonFinishListener = new OnClickListener() {
		public void onClick(View v) {
//			mButtonFinish.setVisibility(View.GONE);
			finishExperiment();
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
//			String date = dateFormat.format(new Date());
//			
//			// create a screenshot
//			View activity = findViewById(R.id.layout_napping);
//			activity.setDrawingCacheEnabled(true);
//			Bitmap bmp = Bitmap.createBitmap(activity.getDrawingCache());
//			activity.setDrawingCacheEnabled(false);
//			File screenshotFile = IOUtil.saveScreenshot(bmp, mName, date);
//			
//			// export positions from the current view
//			File positionsFile = IOUtil.exportPositions(mVideoButtons, mName, date);
//			
//			// export configuration
//			File configurationFile = IOUtil.saveConfiguration(mName, date);
//			
//			// send the image per mail
//			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//			if (prefs.getBoolean(PreferencesActivity.SEND_EMAIL, true)) {
//				ArrayList<File> files = new ArrayList<File>();
//				files.add(configurationFile);
//				files.add(screenshotFile);
//				files.add(positionsFile);
//				IOUtil.sendFilePerMail(files, mName, v.getContext());				
//			}
//			
//			setResult(Activity.RESULT_OK);
//			finish();
		}
	};

	/**
	 * Finishes the experiment and sends the data
	 */
	private void finishExperiment() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
		String date = dateFormat.format(new Date());
		
		// create a screenshot
		View activity = findViewById(R.id.layout_napping);
		activity.setDrawingCacheEnabled(true);
		Bitmap bmp = Bitmap.createBitmap(activity.getDrawingCache());
		activity.setDrawingCacheEnabled(false);
		File screenshotFile = IOUtil.saveScreenshot(bmp, mName, date);
		
		// export positions from the current view
		File positionsFile = IOUtil.exportPositions(mVideoButtons, mName, date);
		
		// export configuration
		File configurationFile = IOUtil.saveConfiguration(mName, date);
		
		// send the image per mail
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		if (prefs.getBoolean(PreferencesActivity.SEND_EMAIL, true)) {
			ArrayList<File> files = new ArrayList<File>();
			files.add(configurationFile);
			files.add(screenshotFile);
			files.add(positionsFile);
			IOUtil.sendFilePerMail(files, mName, this);				
		}
		
		setResult(Activity.RESULT_OK);
		finish();
	}
	
	/**
	 * Creates a button for a given video ID in the napping view
	 */
	private void addButtonForVideo(int videoId) {
		Log.d(TAG, "Adding button for video " + videoId);
		VideoButtonView button = new VideoButtonView(this, videoId);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout_napping);
		layout.addView(button);
		mVideoButtons.add(button);
	}

	/**
	 * Plays the next video from the playlist
	 */
	public void playNext() {
		Intent showVideo = new Intent(this, ViewActivity.class);
		showVideo.putExtra("videoId", mCurrentVideoId);
		Log.d(TAG, "User pressed button. Playing next video with id " + mCurrentVideoId);
		startActivityForResult(showVideo, VIDEO_NEXT_REQUEST);
	}
	
	/**
	 * Play a single video from the playlist
	 * @param id The video id to be played
	 */
	public void playSingle(int id) {
		Intent showVideo = new Intent(this, ViewActivity.class);
		showVideo.putExtra("videoId", id);
		Log.d(TAG, "User pressed button. Playing single video with id " + id);
		startActivityForResult(showVideo, VIDEO_SINGLE_REQUEST);
	}

	/**
	 * Return callback for when another activity finishes (e.g. video playing)
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VIDEO_NEXT_REQUEST) {
			// if we return from a single video play and it played successfully
			if (resultCode == RESULT_OK) {
				int finishedId = data.getIntExtra("videoId", -1);
				// increment playlist if it hasn't finished yet
				if (VideoPlaylist.getState() != VideoPlaylist.STATE_FINISHED) {
					VideoPlaylist.incrementToNext();
				}
				// add the corresponding button for the video
				addButtonForVideo(finishedId);
			}
		} else if (requestCode == VIDEO_SINGLE_REQUEST) {
			// do nothing
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    mMenuGroup  = menu.findItem(R.id.menu_group);
	    mMenuFinish = menu.findItem(R.id.menu_finish);
	    mMenuPlayNext = menu.findItem(R.id.menu_play_next);
	    
	    mMenuGroup.setEnabled(false);
	    mMenuFinish.setEnabled(false);

	    super.onPrepareOptionsMenu(menu);
	    return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_napping, menu);
	    return true;
	}
	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(getText(R.string.dialog_close_header))
        .setMessage(getText(R.string.dialog_close_body))
        .setPositiveButton(getText(R.string.yes), new DialogInterface.OnClickListener()
        	{
        		@Override
    			public void onClick(DialogInterface dialog, int which) {
        			setResult(Activity.RESULT_CANCELED);
    				finish();
    			}

        	})
        .setNegativeButton(getText(R.string.no), null)
        .show();
	}

	private void showMessage(CharSequence charSequence) {
		//mInfoText.setText(charSequence);
		Toast.makeText(this, charSequence, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onStartVideoRequest(VideoButtonView btn) {
		playSingle(btn.getVideoId());
	}
}
