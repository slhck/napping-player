package at.ac.univie.nappingplayer;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.text.InputType;

public class PreferencesActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {
	
	public static final String LIST_SCALE = "list_scale";
	public static final String VIDEO_START_DELAY = "video_start_delay";
	public static final String SEND_EMAIL = "send_email";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		EditTextPreference pref = (EditTextPreference)findPreference(VIDEO_START_DELAY);
		pref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(LIST_SCALE)) {
			ListPreference listScalePref = (ListPreference) findPreference(LIST_SCALE);
			listScalePref.setSummary(listScalePref.getEntry());
		} else if (key.equals(VIDEO_START_DELAY)) {
			EditTextPreference videoStartPref = (EditTextPreference) findPreference(VIDEO_START_DELAY);
			videoStartPref.setSummary(videoStartPref.getText());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}
	
}
