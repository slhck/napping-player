package at.ac.univie.nappingplayer;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.TextView;

public class ViewActivity extends Activity implements Callback,
OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener,
OnErrorListener{
	
	private static final String TAG = ViewActivity.class
	.getSimpleName();
	
	private MediaPlayer mPlayer;
	TextView videoIDview;
	private SurfaceView mPlayView;
	private SurfaceHolder mHolder;
	private String fileName;
	int videoID;
	int pauseduration;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
        setContentView(R.layout.showvideo);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        //catch Intent
        Intent intent= getIntent(); // gets the previously created intent
        videoID = intent.getIntExtra("videoID", -1);
        
        if (videoID == -1) {
        	Log.e(TAG, "No video ID passed, exiting");
        	finish();
        }
        
        videoIDview = (TextView) this.findViewById(R.id.text);
        videoIDview.setText("Video " + String.valueOf(videoID+1));
        
        
        try {
			mPlayView = (SurfaceView) findViewById(R.id.video_surface);
			mHolder = mPlayView.getHolder();
			mHolder.addCallback(this);
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		} catch (Exception e) {
			Log.e(TAG, "Error while creating Surface:" + e.toString());
		}
        
		
         
        
    }
    
    
    @Override
	/**
	 * Called when the activity pauses
	 */
    protected void onPause() {
		super.onPause();
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
    
    
    @Override
	/**
	 * Called when the activity is destroyed.
	 */
    protected void onDestroy() {
		super.onDestroy();
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
    
	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Called when the Media Player is finished preparing and ready to play.
	 */
	public void onPrepared(MediaPlayer player) {
		delayedStartVideo();
	}

	@Override
	public void onCompletion(MediaPlayer player) {
		// TODO Auto-generated method stub
		mPlayer.release();
		finish();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		try {

			mPlayer = new MediaPlayer();
			
			//resolve video ID
			fileName = Configuration.videos[videoID].getName();
			File videoFile = new File(Configuration.sFolderVideos, fileName);
			if ((!videoFile.exists()) || (!videoFile.canRead())) {
				throw new IOException("Video file " + fileName + "not found!");
			}
			
			mPlayer.setDataSource(videoFile.getPath());
			mPlayer.setDisplay(mHolder);
			mPlayer.setScreenOnWhilePlaying(true);
			mPlayer.setOnPreparedListener(this);
			mPlayer.setOnCompletionListener(this);
			mPlayer.setOnVideoSizeChangedListener(this);
			mPlayer.setOnErrorListener(this);
			mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mPlayer.prepare();
				
			//Get the dimensions of the video
		    int videoWidth 	= mPlayer.getVideoWidth();
		    int videoHeight = mPlayer.getVideoHeight();
		    
		    // Get the SurfaceView layout parameters
		    LayoutParams lp = mPlayView.getLayoutParams();
		    
		    
		    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		    String displaySetting = prefs.getString("list_scale", "");
		    
		    if (displaySetting.equalsIgnoreCase("1")) {
		    	// original size
			    lp.width = videoWidth;
			    lp.height = videoHeight;
		    } else if (displaySetting.equalsIgnoreCase("2")) {
		    	// scale to screen width
				// http://stackoverflow.com/q/4835060
			    // Get the width of the screen
			    int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
			    // Set the width of the SurfaceView to the width of the screen
			    lp.width = screenWidth;
			    // Set the height of the SurfaceView to match the aspect ratio of the video 
			    // be sure to cast these as floats otherwise the calculation will likely be 0
			    lp.height = (int) (((float)videoHeight / (float)videoWidth) * (float)screenWidth);
		    } else {
		    	// do nothing, will be fullscreen by default, I guess/hope/fear?
		    }
		    
		    // Commit the layout parameters
		    mPlayView.setLayoutParams(lp); 
			
		} catch (IllegalArgumentException e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	//Starts video after showing Video ID
	public void delayedStartVideo(){
		videoIDview.bringToFront();
		SystemClock.sleep(2000);
		videoIDview.setVisibility(View.INVISIBLE);
		mPlayer.start();
	}
}