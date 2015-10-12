package com.zhy;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.zhy.view.BinarySlidingMenu;
import com.zhy.view.BinarySlidingMenu.OnMenuOpenListener;

public class SlidingMenuActivity extends ListActivity {
	private BinarySlidingMenu mMenu;
	private List<String> mDatas = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sliding_menu);
		mMenu = (BinarySlidingMenu) findViewById(R.id.id_menu);
		mMenu.setOnMenuOpenListener(new OnMenuOpenListener() {
			@Override
			public void onMenuOpen(boolean isOpen, int flag) {
				if (isOpen) {
					Toast.makeText(getApplicationContext(),
							flag == 0 ? "LeftMenu Open" : "RightMenu Open",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),
							flag == 0 ? "LeftMenu Close" : "RightMenu Close",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		// 初始化数据
		for (int i = 'A'; i <= 'Z'; i++) {
			mDatas.add((char) i + "");
		}
		// 设置适配器
		setListAdapter(new ArrayAdapter<String>(this, R.layout.item, mDatas));
	}


}
