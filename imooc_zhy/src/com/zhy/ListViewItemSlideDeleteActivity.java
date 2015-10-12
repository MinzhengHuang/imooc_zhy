package com.zhy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.zhy.view.ListViewItemSlideDeleteView;
import com.zhy.view.ListViewItemSlideDeleteView.DelButtonClickListener;

public class ListViewItemSlideDeleteActivity extends Activity {
	private ListViewItemSlideDeleteView mListView;
	private ArrayAdapter<String> mAdapter;
	private List<String> mDatas;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view_item_slide_delete);
		
		mListView = (ListViewItemSlideDeleteView) findViewById(R.id.id_listview);
		// 不要直接Arrays.asList
		mDatas = new ArrayList<String>(Arrays.asList("HelloWorld", "Welcome",
				"Java", "Android", "Servlet", "Struts", "Hibernate", "Spring",
				"HTML5", "Javascript", "Lucene"));
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mDatas);
		mListView.setAdapter(mAdapter);

		mListView.setDelButtonClickListener(new DelButtonClickListener() {
			
			@Override
			public void clickHappend(int position) {
				Toast.makeText(ListViewItemSlideDeleteActivity.this,
						position + " : " + mAdapter.getItem(position), 1)
						.show();
				mAdapter.remove(mAdapter.getItem(position));
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(ListViewItemSlideDeleteActivity.this,
						position + " : " + mAdapter.getItem(position), 1)
						.show();
			}
		});
	}


}
