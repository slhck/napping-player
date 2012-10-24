package at.ac.univie.nappingplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

public class VideoButtonView extends android.widget.Button {
	private static final String TAG = VideoButtonView.class.getSimpleName();
	
	private static final int SINGLE_VIDEO_PLAY_REQUEST = 1;
	private static final int VIBRATE_DURATION = 100;
	
	String mLabel;
	int mVideoId;

	public VideoButtonView(Context context, int videoId) {
		super(context);
		this.mVideoId = videoId;
		this.mLabel = "  " + (mVideoId + 1) + "  ";
		super.setText(mLabel);
		super.setTextColor(Color.parseColor("#ffffff"));
		setLayout();
		addClickListeners();
	}

	private void setLayout() {
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		p.addRule(RelativeLayout.BELOW, R.id.button_play_next);
		p.addRule(RelativeLayout.CENTER_HORIZONTAL);
		super.setLayoutParams(p);
	}

	private void addClickListeners() {
		this.setOnTouchListener(new View.OnTouchListener() {
			private static final int CLICK_DURATION = 100;
			long touchDownTime;
			int elapsedTime;
			boolean dragStarted = false;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				boolean handled = false;
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					Log.d(TAG, "Button DOWN press.");
					touchDownTime = SystemClock.elapsedRealtime();
					break;
				case MotionEvent.ACTION_MOVE:
					elapsedTime = (int) (SystemClock.elapsedRealtime() - touchDownTime);
					Log.d(TAG, "Button MOVE event. Elapsed time since touch: " + elapsedTime);
					if (SystemClock.elapsedRealtime() - touchDownTime > CLICK_DURATION) {
						// if we haven't vibrated yet
						if (!dragStarted) {
							Vibrator vibrator = (Vibrator) v.getContext()
									.getSystemService(Context.VIBRATOR_SERVICE);
							vibrator.vibrate(VIBRATE_DURATION);
							dragStarted = true;
						} else {
							MarginLayoutParams marginParams = new MarginLayoutParams(v.getLayoutParams());
							int left = (int) event.getRawX() - (v.getWidth() / 2);
							int top = (int) event.getRawY() - (v.getHeight());
							marginParams.setMargins(left, top, 0, 0);
							RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
							v.setLayoutParams(layoutParams);
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					elapsedTime = (int) (SystemClock.elapsedRealtime() - touchDownTime);
					Log.d(TAG, "Button UP event, elapsed time since touch: " + elapsedTime);
					if (elapsedTime <= CLICK_DURATION) {
						Log.d(TAG, "Button time less than 100ms, click");
						// a real click if the touchdown time was short enough
						// and no movement
						Context context = v.getContext();
						Intent showVideo = new Intent(context,
								ViewActivity.class);
						showVideo.putExtra("videoId", mVideoId);
						Log.d(TAG,
								"User pressed button. Playing video associated with it: "
										+ mVideoId);
						context.startActivity(showVideo);
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
}
