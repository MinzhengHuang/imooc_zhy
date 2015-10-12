package com.zhy.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhy.GestureLockActivity;
import com.zhy.SlidingPanelLayoutActivity;

public class TabFragment extends Fragment {
	private String mTitle = "Default";

	public static final String TITLE = "title";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (getArguments() != null) {
			mTitle = getArguments().getString(TITLE);
		}

		TextView tv = new TextView(getActivity());
		tv.setTextSize(20);
		tv.setBackgroundColor(Color.parseColor("#ffffffff"));
		tv.setText(mTitle);
		tv.setGravity(Gravity.CENTER);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent intent=new Intent(getActivity(), CustomViewActivity.class);
//				Intent intent=new Intent(getActivity(), RolateAnimActivity.class);
//				Intent intent=new Intent(getActivity(), ChouJiangActivity.class);
//				Intent intent=new Intent(getActivity(), CircleMenuActivity.class);
//				Intent intent=new Intent(getActivity(), SlidingMenuActivity.class);
//				Intent intent=new Intent(getActivity(), CustomPargressBarActivity.class);
//				Intent intent=new Intent(getActivity(), GestureLockActivity.class);
//				Intent intent=new Intent(getActivity(), SlidingPanelLayoutActivity.class);
				
//				startActivity(intent);
			}
		});
		return tv;

	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
}
