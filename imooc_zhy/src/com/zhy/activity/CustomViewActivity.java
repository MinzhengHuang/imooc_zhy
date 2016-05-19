package com.zhy.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.R;
import com.zhy.utils.HmzUtils;
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
                mTitleText = HmzUtils.randomNumStr(4);//获得随机数
                tv.setText(mTitleText);
            }
        });
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.meinv);
        ImageView iv1 = (ImageView) findViewById(R.id.id_one);
        iv1.setImageDrawable(new CircleImageDrawable(bitmap));

        ImageView iv2 = (ImageView) findViewById(R.id.id_two);
        iv2.setImageDrawable(new RoundImageDrawable(bitmap));

        findViewById(R.id.btn_customimg).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomViewActivity.this, CustomImageActivity.class);
                startActivity(intent);
            }
        });
    }


}
