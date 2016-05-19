package com.zhy.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

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
		tv.setTextSize(60);
		Random r = new Random();
//		tv.setBackgroundColor(Color.parseColor("#ffffffff"));
		tv.setBackgroundColor(Color.argb(r.nextInt(120), r.nextInt(255),
				r.nextInt(255), r.nextInt(255)));
		tv.setText(mTitle);
		tv.setGravity(Gravity.CENTER);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "hmz", Toast.LENGTH_SHORT).show();
			}
		});
		return tv;

	}

	public static TabFragment newInstance(String title) {
		TabFragment tabFragment = new TabFragment();
		Bundle bundle = new Bundle();
		bundle.putString(TITLE, title);
		tabFragment.setArguments(bundle);
		return tabFragment;
	}
}
