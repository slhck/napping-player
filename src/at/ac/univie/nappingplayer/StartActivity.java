package at.ac.univie.nappingplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartActivity extends Activity {

	Button mPreferencesButton;
	EditText mEditText;
	
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
     * When we return to this app from somewhere else and an experiment was finished, clear playlist
     * TODO maybe refactor this as a callback from the napping intent?
     */
    @Override
    public void onResume() {
    	super.onResume();
    	if (VideoPlaylist.getState() == VideoPlaylist.STATE_FINISHED) {
    		VideoPlaylist.reset();
    		mEditText.setText("");
    	}
    }
    
    /**
     * Sets off the napping experiment
     */
    public void startNapping(View view) {
    	Intent startNapping = new Intent(this, NappingActivity.class);
    	startNapping.putExtra("userName", mEditText.getText().toString());
    	
    	if (mEditText.getText().toString().equals("")) {
    		Toast.makeText(this, "You need to enter a name!", Toast.LENGTH_SHORT).show();
    		mEditText.requestFocus();
    		// TODO disable this again
    		// return;
    	}
    	
    	startActivity(startNapping);
    }
    
    public void showPreferences() {
    	Intent prefIntent = new Intent(this, PreferencesActivity.class);
    	startActivity(prefIntent);
    }
    
}
