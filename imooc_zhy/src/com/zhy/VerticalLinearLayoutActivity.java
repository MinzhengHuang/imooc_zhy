package com.zhy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.zhy.view.VerticalLinearLayout;
import com.zhy.view.VerticalLinearLayout.OnPageChangeListener;

public class VerticalLinearLayoutActivity extends Activity {
	private VerticalLinearLayout mMianLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vertical_linear_layout);
		
		mMianLayout = (VerticalLinearLayout) findViewById(R.id.id_main_ly);
		mMianLayout.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageChange(int currentPage) {
				// mMianLayout.getChildAt(currentPage);
				Toast.makeText(VerticalLinearLayoutActivity.this,
						"第" + (currentPage + 1) + "页", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

}
