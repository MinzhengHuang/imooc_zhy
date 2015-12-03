package com.zhy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.zhy.view.VerticalLinearLayout;
import com.zhy.view.VerticalLinearLayout.OnPageChangeListener;

/**
 * Andoird 自定义ViewGroup实现竖向引导界面
 *
 * http://blog.csdn.net/lmj623565791/article/details/23692439
 *
 */
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
