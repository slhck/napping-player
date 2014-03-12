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

package at.ac.univie.nappingplayer.views;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;
import at.ac.univie.nappingplayer.StartVideoListener;
import at.ac.univie.nappingplayer.grouping.SelectVideoListener;

public class VideoButtonView extends android.widget.Button {
	private static final String TAG = VideoButtonView.class.getSimpleName();
	private static final int CLICK_DURATION 	= 100;
	private static final int VIBRATE_DURATION 	= 50;
	private static final int STATUS_BAR_HEIGHT 	= 35;
	
	private static final String COLOR_NEUTRAL  = "#AAAAAA";
	
	private String mLabel;
	private int mVideoId;
	private boolean mSelected;
	private int mMode;
	
	public static final int MODE_MOVE 	= 0;
	public static final int MODE_GROUP 	= 1;

	public VideoButtonView(Context context, int videoId) {
		super(context);
        this.setId(videoId + 1);
		this.setVideoId(videoId);
		this.setLabel(" " + (getVideoId() + 1) + " ");
		super.setBackgroundColor(Color.parseColor(COLOR_NEUTRAL));
		super.setText(getLabel());
		super.setTextColor(Color.parseColor("#000000"));
        // disable for now since it's set in main activity
		//setLayout();
		addClickListeners();
		
		this.mMode = MODE_MOVE;
		this.mSelected = false;
	}

	private void setLayout() {
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		p.addRule(RelativeLayout.CENTER_HORIZONTAL);
		p.addRule(RelativeLayout.CENTER_VERTICAL);
		// TODO: More intelligent placement?
		super.setLayoutParams(p);
	}

	private void addClickListeners() {
		this.setOnTouchListener(new View.OnTouchListener() {
			long touchDownTime;
			int elapsedTime;
			boolean dragStarted = false;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
				
				case MotionEvent.ACTION_DOWN:
					Log.d(TAG, "Button DOWN press.");
					touchDownTime = SystemClock.elapsedRealtime();
					break;
					
				case MotionEvent.ACTION_MOVE:
					// record no movement if we're not napping
					if (mMode == MODE_GROUP) {
						break;
					}
					
					elapsedTime = (int) (SystemClock.elapsedRealtime() - touchDownTime);
					Log.d(TAG, "Button MOVE event. Elapsed time since touch: " + elapsedTime);
					if (SystemClock.elapsedRealtime() - touchDownTime > CLICK_DURATION) {
						// if we haven't vibrated yet
						if (!dragStarted) {
							Vibrator vibrator = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
							vibrator.vibrate(VIBRATE_DURATION);
							dragStarted = true;
						} else {
							// http://stackoverflow.com/a/10075146
							MarginLayoutParams marginParams = new MarginLayoutParams(v.getLayoutParams());
							int left = (int) event.getRawX() - (v.getWidth() / 2);
							int top  = (int) event.getRawY() - (v.getHeight());
							marginParams.setMargins(left, top - STATUS_BAR_HEIGHT, 0, 0);
							RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
                            // ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(marginParams);
							v.setLayoutParams(layoutParams);
						}
					}
					break;
					
				case MotionEvent.ACTION_UP:
					elapsedTime = (int) (SystemClock.elapsedRealtime() - touchDownTime);
					Log.d(TAG, "Button UP event, elapsed time since touch: " + elapsedTime);
					if (elapsedTime <= CLICK_DURATION) {
						Log.d(TAG, "Button time less than click_duration, click registered");
						if (mMode == MODE_MOVE) {
							Log.d(TAG, "Button in play mode. Playing video.");
							// a real click if the touchdown time was short enough and no movement
							Context context = v.getContext();
							StartVideoListener listener = (StartVideoListener) context;
							listener.onStartVideoRequest((VideoButtonView) v);							
						} else if (mMode == MODE_GROUP) {
							Log.d(TAG, "Button in group mode. Selecting video.");
							Context context = v.getContext();
							SelectVideoListener listener = (SelectVideoListener) context;
							listener.onSelectVideoRequest((VideoButtonView) v);
						}
					} else {
						// cancel drag, do nothing
						dragStarted = false;
					}
					break;
				}
				
				return false;
			}
		});
	}
	
	public void showAsSelected(String color) {
		super.setBackgroundColor(Color.parseColor(color));
		this.mSelected = true;
	}
	
	public void showAsDeselected() {
		super.setBackgroundColor(Color.parseColor(COLOR_NEUTRAL));
		this.mSelected = false;
	}
		
	public boolean isSelected() {
		return mSelected;
	}

	public int getMode() {
		return mMode;
	}
	
	public void setMode(int mode) {
		this.mMode = mode;
	}
	
	public int getVideoId() {
		return mVideoId;
	}

	public void setVideoId(int mVideoId) {
		this.mVideoId = mVideoId;
	}

	public String getLabel() {
		return mLabel;
	}

	public void setLabel(String mLabel) {
		this.mLabel = mLabel;
	}

}
