package at.ac.univie.nappingplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartActivity extends Activity {

	private static final String TAG = StartActivity.class.getSimpleName();
	
	Button mPreferencesButton;
	EditText mEditText;
	
	private static final int NAPPING_START_REQUEST = 0;
	
	/**
	 * Called when the activity is first started
	 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        View v = findViewById(R.id.layout_start);
        v.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
        
        mPreferencesButton = (Button) findViewById(R.id.button_preferences);
        mPreferencesButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				showPreferences();
			}
        });
        mEditText = (EditText) findViewById(R.id.et_person_name);
        
        try {
        	// initialize SD card
			Configuration.initialize(this);
			VideoPlaylist.initialize(Configuration.videos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }
    
    /**
     * Sets off the napping experiment
     */
    public void startNapping(View view) {
    	Intent startNapping = new Intent(this, NappingActivity.class);
    	startNapping.putExtra("userName", mEditText.getText().toString());
    	
    	if (mEditText.getText().toString().equals("")) {
    		Toast.makeText(this, getText(R.string.enter_name_prompt), Toast.LENGTH_SHORT).show();
    		mEditText.requestFocus();
    		// TODO disable this again
    		// return;
    	}
    	
    	startActivityForResult(startNapping, NAPPING_START_REQUEST);
    }
    
    /**
	 * Return callback for when another activity finishes (e.g. napping)
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "Returned from activity; request code " + requestCode);
		if (requestCode == NAPPING_START_REQUEST) {
			if (resultCode == RESULT_OK) {
				// if we return from napping and everything went as expected
				if (VideoPlaylist.getState() == VideoPlaylist.STATE_FINISHED) {
					resetExperiment();
		    		Toast.makeText(this, getText(R.string.finished_napping), Toast.LENGTH_LONG).show();
				}
			} else if (resultCode == RESULT_CANCELED) {
				// the user canceled the activity
				resetExperiment();
				Toast.makeText(this, getText(R.string.canceled_napping), Toast.LENGTH_LONG).show();
			}
		}
	}
    
	/**
	 * Resets experiment data (i.e. clears name and playlist state) for next observer
	 */
	private void resetExperiment() {
		VideoPlaylist.reset();
		mEditText.setText("");
	}
	
    public void showPreferences() {
    	Intent prefIntent = new Intent(this, PreferencesActivity.class);
    	startActivity(prefIntent);
    }
    
}
