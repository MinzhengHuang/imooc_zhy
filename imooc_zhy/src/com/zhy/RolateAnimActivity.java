package com.zhy;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.zhy.view.MyImageView;

public class RolateAnimActivity extends Activity {

	MyImageView joke;
	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rolate_anim);
		mContext = this;
		initview();
	}

	private void initview() {
		joke = (MyImageView) findViewById(R.id.c_joke);
		joke.setOnClickIntent(new MyImageView.OnViewClickListener() {

			@Override
			public void onViewClick(MyImageView view) {
				Toast.makeText(mContext, "Joke", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
