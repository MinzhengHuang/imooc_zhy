package com.zhy.activity;

import android.support.v4.app.Fragment;

import com.zhy.R;
import com.zhy.networkimageloader.AbsSingleFragmentActivity;
import com.zhy.networkimageloader.ListImgsFragment;

public class NetworkingImageLoaderActivity extends AbsSingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return new ListImgsFragment();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_single_fragment;
	}
    
}
