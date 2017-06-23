package com.zhy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zhy.R;
import com.zhy.xutils.ContentView;
import com.zhy.xutils.OnClick;
import com.zhy.xutils.ViewInject;
import com.zhy.xutils.ViewInjectUtils;

@ContentView(value = R.layout.activity_xutils)
public class XUtilsTestActivity extends Activity {
	
	@ViewInject(R.id.id_btn)
	private Button mBtn1;
	@ViewInject(R.id.id_btn02)
	private Button mBtn2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewInjectUtils.inject(this);
	}
	
	
	@OnClick({ R.id.id_btn, R.id.id_btn02 })
	public void clickBtnInvoked(View view) {
		switch (view.getId()) {
		case R.id.id_btn:
			Toast.makeText(this, "Inject Btn01 !", Toast.LENGTH_SHORT).show();
			break;
		case R.id.id_btn02:
			Toast.makeText(this, "Inject Btn02 !", Toast.LENGTH_SHORT).show();
			break;
		}
	}

}
