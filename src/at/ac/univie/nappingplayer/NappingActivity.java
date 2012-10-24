package at.ac.univie.nappingplayer;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Main activity for the napping experiment
 * @author werner
 *
 */
public class NappingActivity extends Activity {
	private static final String TAG = NappingActivity.class.getSimpleName();
	private static final int VIDEO_PLAY_REQUEST = 0;
	Button mButtonPlayNext;
	int mCurrentVideoId;
	ArrayList<VideoButtonView> mVideoButtons;

	/**
	 * Called when the activity is first started
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_napping);
		mButtonPlayNext = (Button) findViewById(R.id.button_play_next);
		mButtonPlayNext.setOnClickListener(mButtonPlayNextListener);
		mVideoButtons = new ArrayList<VideoButtonView>();
		Log.d(TAG, "Files to play: " + VideoPlaylist.sFiles.toString());
		
		// TODO debug
		// addButtonForVideo(0);
		
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
	 * Called when the activity resumes, just reassign current video ID in case we need it
	 */
	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume called");
		// if we haven't stopped yet just get the current video ID (could be 0
		// to start)
		if (VideoPlaylist.getState() != VideoPlaylist.STATE_FINISHED) {
			mCurrentVideoId = VideoPlaylist.getCurrentVideoId();
			Log.d(TAG, "Setting current video ID to " + mCurrentVideoId);
		} else {
			// playlist has finished, we should probably disable the "play next" button
			Log.d(TAG, "Playlist finished.");
			mButtonPlayNext.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_napping, menu);
		return true;
	}

	private OnClickListener mButtonPlayNextListener = new OnClickListener() {
		public void onClick(View v) {
			playNext();
		}
	};

	/**
	 * Creates a button for a given video ID
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
		Log.d(TAG, "User pressed button. Playing next video with id "
				+ mCurrentVideoId);
		// startActivity(showVideo);
		startActivityForResult(showVideo, VIDEO_PLAY_REQUEST);
	}

	/**
	 * Return callback for when another activity finishes (e.g. video playing)
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VIDEO_PLAY_REQUEST) {
			if (resultCode == RESULT_OK) {
				int finishedId = data.getIntExtra("videoId", -1);
				// increment playlist
				if (VideoPlaylist.getState() != VideoPlaylist.STATE_FINISHED) {
					VideoPlaylist.incrementToNext();
				}
				// add the corresponding button
				addButtonForVideo(finishedId);
			}
		}
	}
}
