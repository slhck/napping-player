package at.ac.univie.nappingplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class NappingActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_napping);
    }
    
    public void playTest(View view) {
    	Intent showVideo = new Intent(this, ViewActivity.class);
    	showVideo.putExtra("videoID", 0);
    	startActivity(showVideo);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_napping, menu);
        return true;
    }
}
