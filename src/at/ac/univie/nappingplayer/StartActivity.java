package at.ac.univie.nappingplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class StartActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        try {
			Configuration.initialize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }
    
    public void startNapping(View view) {
    	Intent startNapping = new Intent(this, NappingActivity.class);
    	EditText name = (EditText) findViewById(R.id.et_person_name);
    	startNapping.putExtra("username", name.getText());
    	startActivity(startNapping);
    }
    
    public void showPreferences() {
    	Intent prefIntent = new Intent(this, PreferencesActivity.class);
    	startActivity(prefIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_start, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_settings:
        	showPreferences();
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
