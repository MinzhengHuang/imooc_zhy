package com.zhy.weixinrecorder.view;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.zhy.R;


public class AudioRecorderButton extends Button {

	private static final int DISTANCE_Y_CANCLE = 50;
	private static final int STATE_NORMAL = 1;
	private static final int STATE_RECORDING = 2;
	private static final int STATE_WANT_TO_CANCLE = 3;
	private int mCurState = STATE_NORMAL;
	private boolean isRecording = false;// 是否已经开始录音
	private DialogManager mDialogManager;
	private AudioManager mAudioManager;
	protected float mTime;
	// 是否触发longclick;
	private boolean mReady;

	public AudioRecorderButton(Context context) {
		this(context, null);
	}

	public AudioRecorderButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mDialogManager = new DialogManager(getContext());
		String dir = Environment.getExternalStorageDirectory()
				+ "/imooc_recorder_audios";
		mAudioManager = AudioManager.getInstance(dir);
		mAudioManager.setOnAudioStateListener(new AudioManager.AudioStateListener() {

			@Override
			public void wellPrepared() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
			}
		});
		setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				mReady = true;
				mAudioManager.prepareAudio();
				return false;
			}
		});
	}

	/**
	 * 录音完成后的回调
	 * 
	 * @author Administrator
	 * 
	 */
	public interface AudioFinishRecorderListener {
		void onFinish(float seconds, String filePath);
	}

	private AudioFinishRecorderListener mListener;

	public void setAudioFinishRecorderListener(
			AudioFinishRecorderListener listener) {
		mListener = listener;
	}

	/**
	 * 获取音量大小
	 */
	private Runnable mGetVoicelevelRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isRecording) {
				try {
					Thread.sleep(100);
					mTime += 0.1f;
					mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	private static final int MSG_AUDIO_PREPARED = 0x110;
	private static final int MSG_VOICE_CHANGED = 0x111;
	private static final int MSG_DIALOG_DISMISS = 0x112;
	Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_AUDIO_PREPARED:
				// 显示应该在audio end prepare之后
				mDialogManager.showRecordingDialog();
				isRecording = true;
				new Thread(mGetVoicelevelRunnable).start();
				break;
			case MSG_VOICE_CHANGED:
				mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
				break;
			case MSG_DIALOG_DISMISS:
				mDialogManager.dismissDialog();
				break;

			default:
				break;
			}
		};
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			changeState(STATE_RECORDING);
			break;

		case MotionEvent.ACTION_MOVE:
			if (isRecording) {// 已经开始录音

			}
			// 根据想x,y的坐标，判断是否想要取消
			if (wantToCancle(x, y)) {
				changeState(STATE_WANT_TO_CANCLE);
			} else {
				changeState(STATE_RECORDING);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (!mReady) {
				reset();
				return super.onTouchEvent(event);
			}
			if (!isRecording || mTime < 0.6f) {
				mDialogManager.tooShort();
				mAudioManager.cancle();
				mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS, 1300);// 延迟
			} else if (mCurState == STATE_RECORDING) {// 正常录制结束
				mDialogManager.dismissDialog();
				mAudioManager.release();
				if (mListener != null) {
					mListener.onFinish(mTime,
							mAudioManager.getCurrentFilePath());
				}
			} else if (mCurState == STATE_WANT_TO_CANCLE) {
				// cancle
				mDialogManager.dismissDialog();
				mAudioManager.cancle();
			}
			reset();
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 恢复状态及标志位
	 */
	private void reset() {
		isRecording = false;
		mReady = false;
		mTime = 0;
		changeState(STATE_NORMAL);
	}

	private void changeState(int state) {
		if (mCurState != state) {
			mCurState = state;
			switch (state) {
			case STATE_NORMAL:
				this.setBackgroundResource(R.drawable.btn_recorder_normal);
				setText("按住 说话");
				break;
			case STATE_RECORDING:
				this.setBackgroundResource(R.drawable.btn_recording);
				setText("松开 结束");
				if (isRecording) {
					mDialogManager.recording();
				}
				break;
			case STATE_WANT_TO_CANCLE:
				this.setBackgroundResource(R.drawable.btn_recording);
				setText("松开手指，取消发送");
				mDialogManager.wantToCancle();
				break;
			default:
				break;
			}
		}
	}

	private boolean wantToCancle(int x, int y) {
		if (x < 0 || x > getWidth()) {
			return true;
		}
		if (y < -DISTANCE_Y_CANCLE || y > getHeight() + DISTANCE_Y_CANCLE) {
			return true;
		}
		return false;
	}
}
