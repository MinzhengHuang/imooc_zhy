package com.zhy;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhy.view.ZoomImageView;

public class ScaleGestureDetectorActivity extends Activity {
	private ViewPager mViewPager;
	private int[] mImgs = new int[] { R.drawable.tbug, R.drawable.a,
			R.drawable.xx };
	private ImageView[] mImageViews = new ImageView[mImgs.length];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scale_gesture_detector);
		
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		mViewPager.setAdapter(new PagerAdapter() {

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				ZoomImageView iv = new ZoomImageView(
						getApplicationContext());
				iv.setImageResource(mImgs[position]);
				container.addView(iv);
				mImageViews[position] = iv;
				return iv;
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(mImageViews[position]);
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return mImgs.length;
			}
		});
	}

}
