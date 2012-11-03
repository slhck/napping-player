package at.ac.univie.nappingplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import at.ac.univie.nappingplayer.util.Configuration;
import at.ac.univie.nappingplayer.util.IOUtil;
import at.ac.univie.nappingplayer.views.DataExplorerAdapter;
import at.ac.univie.nappingplayer.views.DataExplorerChild;
import at.ac.univie.nappingplayer.views.DataExplorerGroup;

public class DataExplorerActivity extends Activity {

	private static final String TAG = DataExplorerActivity.class.getSimpleName();
	
	private DataExplorerAdapter mExpAdapter;
	private ArrayList<DataExplorerGroup> mExpListItems;
	private ExpandableListView mExpandList;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_explorer);
        
        View v = findViewById(R.id.layout_data_explorer);
        v.setSystemUiVisibility(View.STATUS_BAR_HIDDEN);
        
        mExpandList = (ExpandableListView) findViewById(R.id.data_explorer_list);
        mExpListItems = getItems();
        mExpAdapter = new DataExplorerAdapter(DataExplorerActivity.this, mExpListItems);
        mExpandList.setAdapter(mExpAdapter);
        //mExpandList.setGroupIndicator(null);
        
        mExpandList.setOnChildClickListener(mOnChildClickListener);
    }
    
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_data_explorer, menu);
		return true;
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_clear:
			showClearDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
    
    /**
     * What happens when a child item is clicked
     */
    private ExpandableListView.OnChildClickListener mOnChildClickListener = new ExpandableListView.OnChildClickListener() {
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			DataExplorerChild c = (DataExplorerChild) mExpAdapter.getChild(groupPosition, childPosition);
	        // perform work on child object here
	        Log.d(TAG, "Clicked on " + c.getName());
	        if (c.getTag() == DataExplorerChild.TAG_TEXT) {
	        	StringBuilder text = IOUtil.readFile(c.getFile()); 
	        	showTextDialog(text); 	
	        } else if (c.getTag() == DataExplorerChild.TAG_VIDEO) { 
	        	playVideo(c.getFile());
	        } else {
	        	showUnknownDialog();
	        }
	        return true;
		}
	};
	
	/**
     * Refreshes the adapter's view
     */
    private void refreshView() {
    	mExpListItems = getItems();
        mExpAdapter = new DataExplorerAdapter(DataExplorerActivity.this, mExpListItems);
        mExpandList.setAdapter(mExpAdapter);
    }
    
    private void showTextDialog(StringBuilder text) {
		LayoutInflater inflater = getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.dialog_text_view, null);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(dialogView);
		builder.setPositiveButton("OK", null);
		builder.show();
		
		TextView tv  = (TextView) dialogView.findViewById(R.id.tv_dialog_text);
		tv.setText(text);
		
	}


	/**
     * Deletes all log files from the device
     */
    private void showClearDialog() {
    	new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(getText(R.string.dialog_clear_header))
        .setMessage(getText(R.string.dialog_clear_body))
        .setPositiveButton(getText(R.string.yes), new DialogInterface.OnClickListener()
        	{
        		@Override
    			public void onClick(DialogInterface dialog, int which) {
    				IOUtil.deleteLogData();
    				refreshView();
    			}

        	})
        .setNegativeButton(getText(R.string.no), null)
        .show();
    }
    
    /**
     * Shows a dialog telling the user the file type is unknown
     */
    private void showUnknownDialog() {
    	new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(getText(R.string.dialog_unknown_header))
        .setMessage(getText(R.string.dialog_unknown_body))
        .setPositiveButton("OK", null)
        .show();
    }
    
    /**
     * Plays a single video
     */
    private void playVideo(File f) {
    	int videoId = VideoPlaylist.getId(f);
    	Intent showVideo = new Intent(this, ViewActivity.class);
		showVideo.putExtra("videoId", videoId);
		startActivity(showVideo);
	}
    
    /**
     * Populates an array of item groups from the SD card folders
     */
    private ArrayList<DataExplorerGroup> getItems() {
    	
    	ArrayList<DataExplorerGroup> list = new ArrayList<DataExplorerGroup>();
    	
    	ArrayList<File> logFiles = new ArrayList<File>(Arrays.asList(Configuration.getLogFolder().listFiles()));
    	ArrayList<File> videoFiles = Configuration.getVideos();
    	
    	// collect logs
    	DataExplorerGroup logFileGroup = new DataExplorerGroup();
    	logFileGroup.setName(getText(R.string.log_files).toString());
    	ArrayList<DataExplorerChild> logFileList = new ArrayList<DataExplorerChild>();
    	for (File f : logFiles) {
    		DataExplorerChild c = new DataExplorerChild();
    		c.setName(f.getName());
    		
    		String name = f.getName().toLowerCase();
    		if (name.endsWith(".csv") || name.endsWith(".txt")) {
    			c.setTag(DataExplorerChild.TAG_TEXT);    			
    		} else if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif")) {
    			c.setTag(DataExplorerChild.TAG_IMAGE);
    		} else {    			
    			c.setTag(DataExplorerChild.TAG_UNKNOWN);
    		}
    		
    		c.setFile(f);
    		logFileList.add(c);
    	}
    	logFileGroup.setItems(logFileList);
    	list.add(logFileGroup);
    	
    	// collect videos
    	DataExplorerGroup videoFileGroup = new DataExplorerGroup();
    	videoFileGroup.setName(getText(R.string.video_files).toString());
    	ArrayList<DataExplorerChild> videoFileList = new ArrayList<DataExplorerChild>();
    	for (File f : videoFiles) {
    		DataExplorerChild c = new DataExplorerChild();
    		c.setName(f.getName() + " (ID " + VideoPlaylist.getId(f) + ")");
    		
    		String name = f.getName().toLowerCase();
    		if (name.endsWith(".mp4") || name.endsWith(".mov") || name.endsWith(".flv")) {
    			c.setTag(DataExplorerChild.TAG_VIDEO);    			
    		} else {
    			c.setTag(DataExplorerChild.TAG_UNKNOWN);
    		}
    		
    		c.setFile(f);
    		videoFileList.add(c);
    	}
    	videoFileGroup.setItems(videoFileList);
    	list.add(videoFileGroup);
    	
        
        return list;
    }

}
