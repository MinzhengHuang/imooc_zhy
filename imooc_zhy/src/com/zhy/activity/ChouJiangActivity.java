package com.zhy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.zhy.R;
import com.zhy.view.LuckyPanView;

/**
 * Android SurfaceView实战 打造抽奖转盘
 *
 * http://blog.csdn.net/lmj623565791/article/details/41722441
 *
 */
public class ChouJiangActivity extends Activity {

	private LuckyPanView mLuckyPanView;
	private ImageView mStartBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chou_jiang);
		mLuckyPanView = (LuckyPanView) findViewById(R.id.id_luckypan);
		mStartBtn = (ImageView) findViewById(R.id.iv_start);

		mStartBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mLuckyPanView.isStart()) {
					mStartBtn.setImageResource(R.drawable.stop);
					mLuckyPanView.luckyStart(1);
				} else {
					if (!mLuckyPanView.isShouldEnd()) {
						mStartBtn.setImageResource(R.drawable.start);
						mLuckyPanView.luckyEnd();
					}
				}
			}
		});
	}

}
