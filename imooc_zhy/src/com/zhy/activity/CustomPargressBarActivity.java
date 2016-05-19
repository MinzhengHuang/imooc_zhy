package com.zhy.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.zhy.R;
import com.zhy.view.HorizontalProgressBarWithNumber;
import com.zhy.view.RoundProgressBarWidthNumber;

/**
 * Android 打造形形色色的进度条 实现可以如此简单
 *
 * http://blog.csdn.net/lmj623565791/article/details/43371299
 *
 */
public class CustomPargressBarActivity extends Activity {

	private RoundProgressBarWidthNumber mRoundProgressBar;

	private HorizontalProgressBarWithNumber mProgressBar;
	private static final int MSG_PROGRESS_UPDATE = 0x110;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			int progress = mProgressBar.getProgress();
			int roundProgress = mRoundProgressBar.getProgress();
			mProgressBar.setProgress(++progress);
			mRoundProgressBar.setProgress(++roundProgress);
			if (progress >= 100) {
				mHandler.removeMessages(MSG_PROGRESS_UPDATE);
			}
			mHandler.sendEmptyMessageDelayed(MSG_PROGRESS_UPDATE, 100);
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_pargress_bar);
		mProgressBar = (HorizontalProgressBarWithNumber) findViewById(R.id.id_progressbar01);
		mRoundProgressBar = (RoundProgressBarWidthNumber) findViewById(R.id.id_progress02);
		mHandler.sendEmptyMessage(MSG_PROGRESS_UPDATE);
	}

}
