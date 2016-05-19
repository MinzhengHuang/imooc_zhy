package com.zhy.weixinrecorder;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.zhy.R;
import com.zhy.weixinrecorder.view.AudioRecorderButton;

import java.util.ArrayList;
import java.util.List;

public class WeixinRecorderActivity extends Activity {
	
	private ListView mlv;
	private RecorderAdapter mAdapter;
	private List<Recorder> mDatas=new ArrayList<Recorder>();
	private AudioRecorderButton mbtnAudioRecorder;
	protected View mAnimView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weixin_recorder);
		mlv=(ListView) findViewById(R.id.lv);
		mbtnAudioRecorder=(AudioRecorderButton) findViewById(R.id.btn_audio_recorder);
		mbtnAudioRecorder.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
			
			@Override
			public void onFinish(float seconds, String filePath) {
				// TODO Auto-generated method stub
				Recorder recorder=new Recorder(seconds, filePath);
				mDatas.add(recorder);
				mAdapter.notifyDataSetChanged();
				mlv.setSelection(mDatas.size()-1);
			}
		});
		
		mAdapter=new RecorderAdapter(this, mDatas);
		mlv.setAdapter(mAdapter);
		mlv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(mAnimView!=null){
					mAnimView.setBackgroundResource(R.drawable.adj);
					mAnimView=null;
				}
				// 播放动画
				mAnimView=view.findViewById(R.id.view_recorder_anim);
				mAnimView.setBackgroundResource(R.drawable.play_anim);
				AnimationDrawable anim = (AnimationDrawable) mAnimView.getBackground();
				anim.start();
				// 播放音频
				MediaManager.playSound(mDatas.get(position).getFilePath(),new MediaPlayer.OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
						mAnimView.setBackgroundResource(R.drawable.adj);
					}
				});
			}
		});
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MediaManager.pause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MediaManager.resume();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MediaManager.release();
	}
	
}
