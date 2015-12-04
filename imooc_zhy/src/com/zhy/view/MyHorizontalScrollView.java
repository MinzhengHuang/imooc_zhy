package com.zhy.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.zhy.adapter.HorizontalScrollViewAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * 思想：
 1、首先根据屏幕的大小和Item的大小，计算可以一个屏幕最多可以加载多少个Item，然后加载该数量Item。
 2、当用户右滑（从右向左），滑动到一定距离时，加载下一张，删除第一张
 3、当用户左滑（从左向右），滑动到一定距离时，加载上一张，删除最后一张
 */
public class MyHorizontalScrollView extends HorizontalScrollView implements
		OnClickListener {

	/**
	 * 图片滚动时的回调接口
	 */
	public interface CurrentImageChangeListener {
		void onCurrentImgChanged(int position, View viewIndicator);
	}

	/**
	 * 条目点击时的回调
	 */
	public interface OnItemClickListener {
		void onClick(View view, int pos);
	}

	private CurrentImageChangeListener mListener;
	private OnItemClickListener mOnClickListener;
	private static final String TAG = "MyHorizontalScrollView";
	private LinearLayout mContainer;//HorizontalListView中的LinearLayout
	private int mChildWidth;//子元素的宽度
	private int mChildHeight;//子元素的高度
	private int mCurrentIndex;//当前最后一张图片的index
	private int mFristIndex;//当前第一张图片的下标
	private View mFirstView;//当前第一个View
	private HorizontalScrollViewAdapter mAdapter;//数据适配器
	private int mCountOneScreen;//每屏幕最多显示的个数
	private int mScreenWitdh;//屏幕的宽度
	private Map<View, Integer> mViewPos = new HashMap<View, Integer>();//保存View与位置的键值对

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获得屏幕宽度
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWitdh = outMetrics.widthPixels;
	}

	public void setOnItemClickListener(OnItemClickListener mOnClickListener) {
		this.mOnClickListener = mOnClickListener;
	}

	public void setCurrentImageChangeListener(
			CurrentImageChangeListener mListener) {
		this.mListener = mListener;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mContainer = (LinearLayout) getChildAt(0);
	}

	/**
	 * 加载下一张图片
	 */
	protected void loadNextImg() {
		// 数组边界值计算
		if (mCurrentIndex == mAdapter.getCount() - 1) {
			return;
		}
		// 移除第一张图片，且将水平滚动位置置0
		scrollTo(0, 0);
		mViewPos.remove(mContainer.getChildAt(0));
		mContainer.removeViewAt(0);

		// 获取下一张图片，并且设置onclick事件，且加入容器中
		View view = mAdapter.getView(++mCurrentIndex, null, mContainer);
		view.setOnClickListener(this);
		mContainer.addView(view);
		mViewPos.put(view, mCurrentIndex);

		// 当前第一张图片小标
		mFristIndex++;
		// 如果设置了滚动监听则触发
		if (mListener != null) {
			notifyCurrentImgChanged();
		}

	}

	/**
	 * 加载前一张图片
	 */
	protected void loadPreImg() {
		// 如果当前已经是第一张，则返回
		if (mFristIndex == 0)
			return;
		// 获得当前应该显示为第一张图片的下标
		int index = mCurrentIndex - mCountOneScreen;
		if (index >= 0) {
			// mContainer = (LinearLayout) getChildAt(0);
			// 移除最后一张
			int oldViewPos = mContainer.getChildCount() - 1;
			mViewPos.remove(mContainer.getChildAt(oldViewPos));
			mContainer.removeViewAt(oldViewPos);

			// 将此View放入第一个位置
			View view = mAdapter.getView(index, null, mContainer);
			mViewPos.put(view, index);
			mContainer.addView(view, 0);
			view.setOnClickListener(this);
			// 水平滚动位置向左移动view的宽度个像素
			scrollTo(mChildWidth, 0);
			// 当前位置--，当前第一个显示的下标--
			mCurrentIndex--;
			mFristIndex--;
			// 回调
			if (mListener != null) {
				notifyCurrentImgChanged();

			}
		}
	}

	/**
	 * 滑动时的回调
	 */
	public void notifyCurrentImgChanged() {
		// 先清除所有的背景色，点击时会设置为蓝色
		for (int i = 0; i < mContainer.getChildCount(); i++) {
			mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
		}

		mListener.onCurrentImgChanged(mFristIndex, mContainer.getChildAt(0));

	}

	/**
	 * 初始化数据，设置数据适配器
	 * 
	 * @param mAdapter
	 */
	public void initDatas(HorizontalScrollViewAdapter mAdapter) {
		this.mAdapter = mAdapter;
		mContainer = (LinearLayout) getChildAt(0);
		// 获得适配器中第一个View
		final View view = mAdapter.getView(0, null, mContainer);
		mContainer.addView(view);

		// 强制计算当前View的宽和高
		if (mChildWidth == 0 && mChildHeight == 0) {
			int w = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			view.measure(w, h);
			mChildHeight = view.getMeasuredHeight();
			mChildWidth = view.getMeasuredWidth();
			Log.e(TAG, view.getMeasuredWidth() + "," + view.getMeasuredHeight());
			mChildHeight = view.getMeasuredHeight();
			// 计算每次加载多少个View
			mCountOneScreen = (mScreenWitdh / mChildWidth == 0) ? mScreenWitdh
					/ mChildWidth + 1 : mScreenWitdh / mChildWidth + 2;

			Log.e(TAG, "mCountOneScreen = " + mCountOneScreen
					+ " ,mChildWidth = " + mChildWidth);

		}
		// 初始化第一屏幕的元素
		initFirstScreenChildren(mCountOneScreen);
	}

	/**
	 * 加载第一屏的View
	 * 
	 * @param mCountOneScreen
	 */
	public void initFirstScreenChildren(int mCountOneScreen) {
		mContainer = (LinearLayout) getChildAt(0);
		mContainer.removeAllViews();
		mViewPos.clear();

		for (int i = 0; i < mCountOneScreen; i++) {
			View view = mAdapter.getView(i, null, mContainer);
			view.setOnClickListener(this);
			mContainer.addView(view);
			mViewPos.put(view, i);
			mCurrentIndex = i;
		}

		if (mListener != null) {
			notifyCurrentImgChanged();
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_MOVE:
			// Log.e(TAG, getScrollX() + "");

			int scrollX = getScrollX();
			// 如果当前scrollX为view的宽度，加载下一张，移除第一张
			if (scrollX >= mChildWidth) {
				loadNextImg();
			}
			// 如果当前scrollX = 0， 往前设置一张，移除最后一张
			if (scrollX == 0) {
				loadPreImg();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void onClick(View v) {
		if (mOnClickListener != null) {
			for (int i = 0; i < mContainer.getChildCount(); i++) {
				mContainer.getChildAt(i).setBackgroundColor(Color.WHITE);
			}
			mOnClickListener.onClick(v, mViewPos.get(v));
		}
	}

}
