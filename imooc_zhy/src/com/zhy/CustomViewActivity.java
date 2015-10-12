package com.zhy;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.view.CircleImageDrawable;
import com.zhy.view.RoundImageDrawable;

public class CustomViewActivity extends Activity {

    protected String mTitleText;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        final TextView tv = (TextView) findViewById(R.id.tv);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTitleText = randomText();//获得随机数
				tv.setText(mTitleText);
			}
		});
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.mv);
		ImageView iv1 = (ImageView) findViewById(R.id.id_one);
		iv1.setImageDrawable(new CircleImageDrawable(bitmap));
		ImageView iv2 = (ImageView) findViewById(R.id.id_two);
		iv2.setImageDrawable(new RoundImageDrawable(bitmap));
		
		findViewById(R.id.btn_customimg).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(CustomViewActivity.this, CustomImageActivity.class);
				startActivity(intent);
			}
		});
    }

	private String randomText() {
		Random random = new Random();
		Set<Integer> set = new HashSet<Integer>();
		while (set.size() < 4) {
			int randomInt = random.nextInt(10);
			set.add(randomInt);
		}
		StringBuffer sb = new StringBuffer();
		for (Integer i : set) {
			sb.append("" + i);
		}

		return sb.toString();
	}
}
