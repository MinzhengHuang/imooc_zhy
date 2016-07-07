package com.zhy.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.zhy.R;
import com.zhy.adapter.HorizontalScrollViewAdapter;
import com.zhy.view.MyHorizontalScrollView;
import com.zhy.view.MyHorizontalScrollView.CurrentImageChangeListener;
import com.zhy.view.MyHorizontalScrollView.OnItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Android 自定义 HorizontalScrollView 打造再多图片（控件）也不怕 OOM 的横向滑动效果
 *
 * http://blog.csdn.net/lmj623565791/article/details/38140505
 *
 */
public class MyHorizontalScrollViewActivity extends Activity {

	private MyHorizontalScrollView mHorizontalScrollView;
	private HorizontalScrollViewAdapter mAdapter;
	private ImageView mImg;
	private List<Integer> mDatas = new ArrayList<Integer>(Arrays.asList(
			R.drawable.aaa, R.drawable.bbb, R.drawable.ccc, R.drawable.dddd,
			R.drawable.eeee, R.drawable.ffff, R.drawable.gggg, R.drawable.hhhh,
			R.drawable.l));

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_myhsv);

		mImg = (ImageView) findViewById(R.id.id_content);

		mHorizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.id_horizontalScrollView);
		mAdapter = new HorizontalScrollViewAdapter(this, mDatas);
		// 添加滚动回调
		mHorizontalScrollView
				.setCurrentImageChangeListener(new CurrentImageChangeListener() {
					@Override
					public void onCurrentImgChanged(int position,
							View viewIndicator) {
						mImg.setImageResource(mDatas.get(position));
						viewIndicator.setBackgroundColor(Color
								.parseColor("#AA024DA4"));
					}
				});
		// 添加点击回调
		mHorizontalScrollView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onClick(View view, int position) {
				mImg.setImageResource(mDatas.get(position));
				view.setBackgroundColor(Color.parseColor("#AA024DA4"));
			}
		});
		// 设置适配器
		mHorizontalScrollView.initDatas(mAdapter);
	}

}
