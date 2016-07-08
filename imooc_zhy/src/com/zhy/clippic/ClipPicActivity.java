package com.zhy.clippic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.zhy.R;

import java.io.ByteArrayOutputStream;

/**
 * http://blog.csdn.net/lmj623565791/article/details/39761281
 * 
 * @author zhy
 *
 */
public class ClipPicActivity extends AppCompatActivity {
	private ClipImageLayout mClipImageLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clippic);

		mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.clippic, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.id_action_clip:
			Bitmap bitmap = mClipImageLayout.clip();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] datas = baos.toByteArray();

			Intent intent = new Intent(this, ShowImageActivity.class);
			intent.putExtra("bitmap", datas);
			startActivity(intent);

			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
