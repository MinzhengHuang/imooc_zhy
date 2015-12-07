package com.zhy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.zhy.FlowLayoutActivity;
import com.zhy.JazzyViewPagerActivity;
import com.zhy.LargeImageViewActivity;
import com.zhy.MyHorizontalScrollViewActivity;
import com.zhy.R;
import com.zhy.ScaleGestureDetectorActivity;
import com.zhy.StickyNavLayoutActivity;
import com.zhy.ViewPagerIndicatorActivity;
import com.zhy.clippic.ClipPicActivity;
import com.zhy.handle_runtime_change.HandRuntimeChangeActivity;

public class ThirdFragment extends Fragment implements OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.fragment_third, container, false);
        view.findViewById(R.id.btn_HandRuntimeChange).setOnClickListener(this);
        view.findViewById(R.id.btn_ScaleGestureDetector).setOnClickListener(this);
        view.findViewById(R.id.btn_ClipPic).setOnClickListener(this);
        view.findViewById(R.id.btn_StickyNavLayout).setOnClickListener(this);
        view.findViewById(R.id.btn_MyHorizontalScrollView).setOnClickListener(this);
        view.findViewById(R.id.btn_JazzyViewPager).setOnClickListener(this);
        view.findViewById(R.id.btn_FlowLayout).setOnClickListener(this);
        view.findViewById(R.id.btn_LargeImageView).setOnClickListener(this);
        view.findViewById(R.id.btn_FlowLayout2).setOnClickListener(this);
        view.findViewById(R.id.btn_ViewPagerIndicator).setOnClickListener(this);
        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_HandRuntimeChange:
                intent = new Intent(getActivity(), HandRuntimeChangeActivity.class);
                break;
            case R.id.btn_ScaleGestureDetector:
                intent = new Intent(getActivity(), ScaleGestureDetectorActivity.class);
                break;
            case R.id.btn_ClipPic:
                intent = new Intent(getActivity(), ClipPicActivity.class);
                break;
            case R.id.btn_StickyNavLayout:
                intent = new Intent(getActivity(), StickyNavLayoutActivity.class);
                break;
            case R.id.btn_MyHorizontalScrollView:
                intent = new Intent(getActivity(), MyHorizontalScrollViewActivity.class);
                break;
            case R.id.btn_JazzyViewPager:
                intent = new Intent(getActivity(), JazzyViewPagerActivity.class);
                break;
            case R.id.btn_FlowLayout:
                intent = new Intent(getActivity(), FlowLayoutActivity.class);
                break;
            case R.id.btn_LargeImageView:
                intent = new Intent(getActivity(), LargeImageViewActivity.class);
                break;
            case R.id.btn_FlowLayout2:
//                intent = new Intent(getActivity(), CategoryActivity.class);
                break;
            case R.id.btn_ViewPagerIndicator:
                intent = new Intent(getActivity(), ViewPagerIndicatorActivity.class);
                break;

            default:
                break;
        }
        if(intent!=null){
            startActivity(intent);
        }

    }
}
