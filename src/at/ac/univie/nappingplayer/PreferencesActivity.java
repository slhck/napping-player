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
	
	public static final String LIST_SCALE 			= "list_scale";
	public static final String VIDEO_START_DELAY 	= "video_start_delay";
	public static final String SEND_EMAIL 			= "send_email";
	public static final String SEND_EMAIL_ADDRESS	= "send_email_address";
	public static final String ENABLE_GROUPING		= "enable_grouping";
	public static final String ENABLE_MOVEMENT 		= "enable_movement";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		EditTextPreference videoStartPref = (EditTextPreference)findPreference(VIDEO_START_DELAY);
		videoStartPref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
		
		EditTextPreference emailAddressPref = (EditTextPreference)findPreference(SEND_EMAIL_ADDRESS);
		emailAddressPref.getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(LIST_SCALE)) {
			ListPreference listScalePref = (ListPreference) findPreference(LIST_SCALE);
			listScalePref.setSummary(listScalePref.getEntry());
		} else if (key.equals(VIDEO_START_DELAY)) {
			EditTextPreference videoStartPref = (EditTextPreference) findPreference(VIDEO_START_DELAY);
			videoStartPref.setSummary(videoStartPref.getText());
		} else if (key.equals(SEND_EMAIL_ADDRESS)) {
			EditTextPreference emailAddressPref = (EditTextPreference) findPreference(SEND_EMAIL_ADDRESS);
			emailAddressPref.setSummary(emailAddressPref.getText());
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
