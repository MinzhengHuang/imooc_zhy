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

import com.zhy.BounceScrollViewActivity;
import com.zhy.EventBusActivity;
import com.zhy.Game2048Activity;
import com.zhy.GamePintuActivity;
import com.zhy.GestureLockActivity;
import com.zhy.ImageLoaderActivity;
import com.zhy.ListViewItemSlideDeleteActivity;
import com.zhy.NetworkingImageLoaderActivity;
import com.zhy.R;
import com.zhy.SlidingPanelLayoutActivity;
import com.zhy.SwipeRefreshActivity;
import com.zhy.VerticalLinearLayoutActivity;
import com.zhy.view.ListViewItemSlideDeleteView;

public class SecondFragment extends Fragment implements OnClickListener{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_second, container, false);
		view.findViewById(R.id.btn_VerticalLinearLayout).setOnClickListener(this);
		view.findViewById(R.id.btn_ListViewItemSlideDelete).setOnClickListener(this);
		view.findViewById(R.id.btn_eventbus).setOnClickListener(this);
		view.findViewById(R.id.btn_SwipeRefresh).setOnClickListener(this);
		view.findViewById(R.id.btn_imageloader).setOnClickListener(this);
		view.findViewById(R.id.btn_networkingimageloader).setOnClickListener(this);
		view.findViewById(R.id.btn_BounceScrollView).setOnClickListener(this);
		view.findViewById(R.id.btn_GamePintu).setOnClickListener(this);
		view.findViewById(R.id.btn_Game2048).setOnClickListener(this);
		return view;

	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		Intent intent=null;
		switch (v.getId()) {
		case R.id.btn_VerticalLinearLayout:
			intent=new Intent(getActivity(), VerticalLinearLayoutActivity.class);
			break;
		case R.id.btn_ListViewItemSlideDelete:
			intent=new Intent(getActivity(), ListViewItemSlideDeleteActivity.class);
			break;
		case R.id.btn_eventbus:
			intent=new Intent(getActivity(), EventBusActivity.class);
			break;
		case R.id.btn_SwipeRefresh:
			intent=new Intent(getActivity(), SwipeRefreshActivity.class);
			break;
		case R.id.btn_imageloader:
			intent=new Intent(getActivity(), ImageLoaderActivity.class);
			break;
		case R.id.btn_networkingimageloader:
			intent=new Intent(getActivity(), NetworkingImageLoaderActivity.class);
			break;
		case R.id.btn_BounceScrollView:
			intent=new Intent(getActivity(), BounceScrollViewActivity.class);
			break;
		case R.id.btn_GamePintu:
			intent=new Intent(getActivity(), GamePintuActivity.class);
			break;
		case R.id.btn_Game2048:
			intent=new Intent(getActivity(), Game2048Activity.class);
			break;

		default:
			break;
		}
		startActivity(intent);
	}
}
