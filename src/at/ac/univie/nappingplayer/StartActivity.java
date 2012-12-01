package at.ac.univie.nappingplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import at.ac.univie.nappingplayer.util.Configuration;

public class StartActivity extends Activity {
	private static final String TAG = StartActivity.class.getSimpleName();
	
	EditText mEditName;
	
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
		
		mEditName = (EditText) findViewById(R.id.et_person_name);
		try {
			// initialize SD card
			Configuration.initialize(this);
			VideoPlaylist.initialize(Configuration.getVideos());
		} catch (Exception e) {
			Log.e(TAG, "Error while initializing SD card. Aborting");
			e.printStackTrace();
			finish();
		}
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
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
					Toast.makeText(this, getText(R.string.finished_napping),
							Toast.LENGTH_LONG).show();
				}
			} else if (resultCode == RESULT_CANCELED) {
				// the user canceled the activity
				resetExperiment();
				Toast.makeText(this, getText(R.string.canceled_napping),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_start, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_data:
			startDataExplorer();
			return true;
		case R.id.menu_settings:
			startPreferences();
			return true;
		case R.id.menu_credits:
			startCredits();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Resets experiment data (i.e. clears name and playlist state) for next
	 * observer
	 */
	private void resetExperiment() {
		VideoPlaylist.reset();
		mEditName.setText("");
	}
	
	/**
	 * Sets off the napping experiment
	 */
	public void startNapping(View view) {
		Intent startNapping = new Intent(this, NappingActivity.class);
		startNapping.putExtra("userName", mEditName.getText().toString());
//		if (mEditName.getText().toString().equals("")) {
//			Toast.makeText(this, getText(R.string.enter_name_prompt), Toast.LENGTH_SHORT).show();
//			mEditName.requestFocus();
//			return;
//		}
		startActivityForResult(startNapping, NAPPING_START_REQUEST);
	}

	public void startPreferences() {
		Intent prefIntent = new Intent(this, PreferencesActivity.class);
		startActivity(prefIntent);
	}
	
	public void startDataExplorer() {
		Intent dataIntent = new Intent(this, DataExplorerActivity.class);
		startActivity(dataIntent);
	}
	
	public void startCredits() {
		Intent creditsIntent = new Intent(this, CreditsActivity.class);
		startActivity(creditsIntent);
	}
	
	
}
