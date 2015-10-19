package com.zhy.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zhy.R;

import java.util.ArrayList;
import java.util.List;

public class StickyNavTabFragment extends Fragment {
	public static final String TITLE = "title";
	private String mTitle = "Defaut Value";
	private ListView mListView;
	private List<String> mDatas = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mTitle = getArguments().getString(TITLE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_stickynav, container, false);
		mListView = (ListView) view
				.findViewById(R.id.id_stickynavlayout_innerscrollview);
		for (int i = 0; i < 50; i++) {
			mDatas.add(mTitle + " -> " + i);
		}
		mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.item_stickynav, R.id.id_info, mDatas) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				Log.e("tag", "convertView = " + convertView);
				return super.getView(position, convertView, parent);
			}
		});
		return view;

	}

	public static StickyNavTabFragment newInstance(String title) {
		StickyNavTabFragment tabFragment = new StickyNavTabFragment();
		Bundle bundle = new Bundle();
		bundle.putString(TITLE, title);
		tabFragment.setArguments(bundle);
		return tabFragment;
	}

}
