package com.zhy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.zhy.R;
import com.zhy.activity.BinarySlidingMenuActivity;
import com.zhy.activity.ChouJiangActivity;
import com.zhy.activity.CircleMenuActivity;
import com.zhy.activity.CustomPargressBarActivity;
import com.zhy.activity.CustomViewActivity;
import com.zhy.activity.DrawerLayoutActivity;
import com.zhy.activity.GestureLockActivity;
import com.zhy.activity.RolateAnimActivity;
import com.zhy.activity.SlidingPanelLayoutActivity;
import com.zhy.activity.ViewDragHelperActivity;

public class FirstFragment extends Fragment implements OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        view.findViewById(R.id.btn_customview).setOnClickListener(this);
        view.findViewById(R.id.btn_rolateanim).setOnClickListener(this);
        view.findViewById(R.id.btn_chouJiang).setOnClickListener(this);
        view.findViewById(R.id.btn_CircleMenu).setOnClickListener(this);
        view.findViewById(R.id.btn_SlidingMenu).setOnClickListener(this);
        view.findViewById(R.id.btn_CustomPargressBar).setOnClickListener(this);
        view.findViewById(R.id.btn_GestureLock).setOnClickListener(this);
        view.findViewById(R.id.btn_SlidingPanelLayout).setOnClickListener(this);
        view.findViewById(R.id.btn_ViewDragHelper).setOnClickListener(this);
        view.findViewById(R.id.btn_Drawerlayout).setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_customview:
                intent = new Intent(getActivity(), CustomViewActivity.class);
                break;
            case R.id.btn_rolateanim:
                intent = new Intent(getActivity(), RolateAnimActivity.class);
                break;
            case R.id.btn_chouJiang:
                intent = new Intent(getActivity(), ChouJiangActivity.class);
                break;
            case R.id.btn_CircleMenu:
                intent = new Intent(getActivity(), CircleMenuActivity.class);
                break;
            case R.id.btn_SlidingMenu:
                intent = new Intent(getActivity(), BinarySlidingMenuActivity.class);
                break;
            case R.id.btn_CustomPargressBar:
                intent = new Intent(getActivity(), CustomPargressBarActivity.class);
                break;
            case R.id.btn_GestureLock:
                intent = new Intent(getActivity(), GestureLockActivity.class);
                break;
            case R.id.btn_SlidingPanelLayout:
                intent = new Intent(getActivity(), SlidingPanelLayoutActivity.class);
                break;
            case R.id.btn_ViewDragHelper:
                intent = new Intent(getActivity(), ViewDragHelperActivity.class);
                break;
            case R.id.btn_Drawerlayout:
                intent = new Intent(getActivity(), DrawerLayoutActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}
