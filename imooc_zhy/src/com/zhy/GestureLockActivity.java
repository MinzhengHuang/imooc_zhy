package com.zhy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.zhy.view.GestureLockViewGroup;
import com.zhy.view.GestureLockViewGroup.OnGestureLockViewListener;

/**
 * Android 手势锁的实现 让自己的应用更加安全吧
 *
 * http://blog.csdn.net/lmj623565791/article/details/36236113
 *
 */
public class GestureLockActivity extends Activity {
	private GestureLockViewGroup mGestureLockViewGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_lock);
		mGestureLockViewGroup = (GestureLockViewGroup) findViewById(R.id.id_gestureLockViewGroup);
		mGestureLockViewGroup.setAnswer(new int[] { 1, 2, 3, 4, 5 });
		mGestureLockViewGroup
				.setOnGestureLockViewListener(new OnGestureLockViewListener() {

					@Override
					public void onUnmatchedExceedBoundary() {
						Toast.makeText(GestureLockActivity.this, "错误5次...",
								Toast.LENGTH_SHORT).show();
						mGestureLockViewGroup.setUnMatchExceedBoundary(5);
					}
					
					@Override
					public void onGestureEvent(boolean matched) {
						Toast.makeText(GestureLockActivity.this, matched + "",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onBlockSelected(int cId) {
					}
				});
	}

}
