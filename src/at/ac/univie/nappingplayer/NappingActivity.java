package at.ac.univie.nappingplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NappingActivity extends Activity {

	private static final String TAG = NappingActivity.class.getSimpleName();

	Button mButtonPlayNext;
	int mCurrentVideoId;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_napping);
        mButtonPlayNext = (Button)findViewById(R.id.button_play_next);
        mButtonPlayNext.setOnClickListener(mButtonPlayNextListener);
        
        Log.d(TAG, "Files to play: " + VideoPlaylist.sFiles.toString());
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
    
    @Override
    public void onResume() {
    	super.onResume();
	    Log.d(TAG, "onResume called");
	    
	    // if we have already started watching, increment to next
	    if (VideoPlaylist.getState() != VideoPlaylist.STATE_INITIALIZED) {
	    	Log.d(TAG, "Incrementing playlist.");
	    	VideoPlaylist.incrementToNext();
	    } 
	    // if not, we'll just get the current video ID (could be 0 to start)
	    if (VideoPlaylist.getState() != VideoPlaylist.STATE_FINISHED) {
	    	mCurrentVideoId = VideoPlaylist.getCurrentVideoId();	    	
	    	Log.d(TAG, "Setting current video ID to " + mCurrentVideoId);
	    } else {
	    	Log.d(TAG, "Playlist finished");
	    	// disable button or something
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
    
    public void playNext() {
    	Intent showVideo = new Intent(this, ViewActivity.class);
    	showVideo.putExtra("videoID", mCurrentVideoId);
    	Log.d(TAG, "User pressed button. Playing next video with id " + mCurrentVideoId);
    	startActivity(showVideo);
    }
}
