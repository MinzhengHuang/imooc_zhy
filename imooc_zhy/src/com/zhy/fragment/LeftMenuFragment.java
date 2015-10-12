package com.zhy.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zhy.R;

/**
 * 
 * @author zhy http://blog.csdn.net/lmj623565791/
 */
public class LeftMenuFragment extends Fragment {
	private ListView mMenus;
	private String[] mMenuItemStr = { "Bear", "Bird", "Cat", "Tigers", "Panda" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_left_menu, container,
				false);
		mMenus = (ListView) view.findViewById(R.id.id_left_menu_lv);
		mMenus.setAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.item_left_menu, mMenuItemStr));
		return view;
	}
}
