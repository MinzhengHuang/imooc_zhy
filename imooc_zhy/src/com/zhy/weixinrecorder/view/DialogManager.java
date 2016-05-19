package com.zhy.weixinrecorder.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.R;


public class DialogManager {
	private Dialog mDialog;
	private ImageView mivIcon;
	private ImageView mivVoice;
	private TextView mtvLable;
	private Context mContext;

	public DialogManager(Context mContext) {
		this.mContext = mContext;
	}

	public void showRecordingDialog() {
		mDialog=new Dialog(mContext, R.style.Theme_AudioDialog);
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_recorder, null);
		mivIcon = (ImageView) view.findViewById(R.id.iv_recorder_dialog_icon);
		mivVoice=(ImageView) view.findViewById(R.id.iv_recorder_dialog_voice);
		mtvLable=(TextView) view.findViewById(R.id.tv_recorder_dialog_lable);
		mDialog.setContentView(view);
		mDialog.show();
	}

	public void recording() {
		if(mDialog!=null&&mDialog.isShowing()){
			mivIcon.setVisibility(View.VISIBLE);
			mivVoice.setVisibility(View.VISIBLE);
			mtvLable.setVisibility(View.VISIBLE);
			mivIcon.setImageResource(R.drawable.recorder);
			mtvLable.setText("手指上滑，取消发送");
		}
	}
	public void wantToCancle() {
		if(mDialog!=null&&mDialog.isShowing()){
			mivIcon.setVisibility(View.VISIBLE);
			mivVoice.setVisibility(View.GONE);
			mtvLable.setVisibility(View.VISIBLE);
			
			mivIcon.setImageResource(R.drawable.cancel);
			mtvLable.setText("松开手指，取消发送");
		}
	}

	public void tooShort() {
		if(mDialog!=null&&mDialog.isShowing()){
			mivIcon.setVisibility(View.VISIBLE);
			mivVoice.setVisibility(View.GONE);
			mtvLable.setVisibility(View.VISIBLE);
			
			mivIcon.setImageResource(R.drawable.voice_to_short);
			mtvLable.setText("录音时间过短");
		}
	}

	public void dismissDialog() {
		if(mDialog!=null&&mDialog.isShowing()){
			mDialog.dismiss();
			mDialog=null;
		}
	}

	/**
	 * 通过level去更新voice上的图片
	 * @param level 1-7
	 */
	public void updateVoiceLevel(int level) {
		if(mDialog!=null&&mDialog.isShowing()){
//			mivIcon.setVisibility(View.VISIBLE);
//			mivVoice.setVisibility(View.VISIBLE);
//			mtvLable.setVisibility(View.VISIBLE);
			
			int resId=mContext.getResources().getIdentifier("v"+level, "drawable", mContext.getPackageName());
			mivIcon.setImageResource(resId);
		}
	}
}
