package com.zhy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhy.R;
import com.zhy.view.GestureLockView.Mode;

import java.util.ArrayList;
import java.util.List;

/**
 * 整体包含n*n个GestureLockView,每个GestureLockView间间隔mMarginBetweenLockView，
 * 最外层的GestureLockView与容器存在mMarginBetweenLockView的外边距
 * 
 * 关于GestureLockView的边长（n*n）: n * mGestureLockViewWidth + ( n + 1 ) *
 * mMarginBetweenLockView = mWidth ; 得：mGestureLockViewWidth = 4 * mWidth / ( 5
 * * mCount + 1 ) 注：mMarginBetweenLockView = mGestureLockViewWidth * 0.25 ;
 * 
 * @author zhy
 *
 * http://blog.csdn.net/lmj623565791/article/details/36236113
 *
 * 整体思路:
 * a、自定义了一个RelativeLayout(GestureLockViewGroup)在里面会根据传入的每行的个数，生成多个GestureLockView
 * （就是上面一个个小圈圈），然后会自动进行布局，里面的宽度，间距，内圆的直径，箭头的大小神马的都是百分比实现的，
 * 所以大胆的设置你喜欢的个数，只要你没有密集恐惧症~
 * b、GestureLockView有三个状态，没有手指触碰、手指触碰、和手指抬起，会根据这三个状态绘制不同的效果，以及抬起时的
 * 小箭头需要旋转的角度，会根据用户选择的GestureLockView，进行计算，在GestureLockViewGroup为每个GestureLockView设置
 * c、GestureLockViewGroup主要就是判断用户ACTION_MOVE，ACTION_DOWN ， ACTION_UP时改变选中的GestureLockView的状态，
 * 并且记录下来，提供一定的回调。
 *
 */
public class GestureLockViewGroup extends RelativeLayout {

	private static final String TAG = "GestureLockViewGroup";
	private GestureLockView[] mGestureLockViews;//保存所有的GestureLockView
	private int mCount = 4;//每个边上的GestureLockView的个数
	private int[] mAnswer = { 0, 1, 2, 5, 8 };//存储答案
	private List<Integer> mChoose = new ArrayList<Integer>();//保存用户选中的GestureLockView的id
	private Paint mPaint;
	private int mMarginBetweenLockView = 30;//每个GestureLockView中间的间距 设置为：mGestureLockViewWidth * 25%
	private int mGestureLockViewWidth;//GestureLockView的边长 4 * mWidth / ( 5 * mCount + 1 )
	private int mNoFingerInnerCircleColor = 0xFF939090;//GestureLockView无手指触摸的状态下内圆的颜色
	private int mNoFingerOuterCircleColor = 0xFFE0DBDB;//GestureLockView无手指触摸的状态下外圆的颜色
	private int mFingerOnColor = 0xFF378FC9;//GestureLockView手指触摸的状态下内圆和外圆的颜色
	private int mFingerUpColor = 0xFFFF0000;//GestureLockView手指抬起的状态下内圆和外圆的颜色
	private int mWidth;//宽度
	private int mHeight;//高度
	private Path mPath;
	private int mLastPathX;//指引线的开始位置x
	private int mLastPathY;//指引线的开始位置y
	private Point mTmpTarget = new Point();//指引下的结束位置
	private int mTryTimes = 4;//最大尝试次数
	private OnGestureLockViewListener mOnGestureLockViewListener;//回调接口

	public GestureLockViewGroup(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GestureLockViewGroup(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		/**
		 * 获得所有自定义的参数的值
		 */
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.GestureLockViewGroup, defStyle, 0);
		int n = a.getIndexCount();

		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.GestureLockViewGroup_color_no_finger_inner_circle:
				mNoFingerInnerCircleColor = a.getColor(attr,
						mNoFingerInnerCircleColor);
				break;
			case R.styleable.GestureLockViewGroup_color_no_finger_outer_circle:
				mNoFingerOuterCircleColor = a.getColor(attr,
						mNoFingerOuterCircleColor);
				break;
			case R.styleable.GestureLockViewGroup_color_finger_on:
				mFingerOnColor = a.getColor(attr, mFingerOnColor);
				break;
			case R.styleable.GestureLockViewGroup_color_finger_up:
				mFingerUpColor = a.getColor(attr, mFingerUpColor);
				break;
			case R.styleable.GestureLockViewGroup_count:
				mCount = a.getInt(attr, 3);
				break;
			case R.styleable.GestureLockViewGroup_tryTimes:
				mTryTimes = a.getInt(attr, 5);
			default:
				break;
			}
		}

		a.recycle();

		// 初始化画笔
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.STROKE);
		// mPaint.setStrokeWidth(20);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		// mPaint.setColor(Color.parseColor("#aaffffff"));
		mPath = new Path();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		mWidth = MeasureSpec.getSize(widthMeasureSpec);
		mHeight = MeasureSpec.getSize(heightMeasureSpec);

		// Log.e(TAG, mWidth + "");
		// Log.e(TAG, mHeight + "");

		mHeight = mWidth = mWidth < mHeight ? mWidth : mHeight;

		// setMeasuredDimension(mWidth, mHeight);

		// 初始化mGestureLockViews
		if (mGestureLockViews == null) {
			mGestureLockViews = new GestureLockView[mCount * mCount];
			// 计算每个GestureLockView的宽度
			mGestureLockViewWidth = (int) (4 * mWidth * 1.0f / (5 * mCount + 1));
			// 计算每个GestureLockView的间距
			mMarginBetweenLockView = (int) (mGestureLockViewWidth * 0.25);
			// 设置画笔的宽度为GestureLockView的内圆直径稍微小点（不喜欢的话，随便设）
			mPaint.setStrokeWidth(mGestureLockViewWidth * 0.29f);

			for (int i = 0; i < mGestureLockViews.length; i++) {
				// 初始化每个GestureLockView
				mGestureLockViews[i] = new GestureLockView(getContext(),
						mNoFingerInnerCircleColor, mNoFingerOuterCircleColor,
						mFingerOnColor, mFingerUpColor);
				mGestureLockViews[i].setId(i + 1);
				// 设置参数，主要是定位GestureLockView间的位置
				RelativeLayout.LayoutParams lockerParams = new RelativeLayout.LayoutParams(
						mGestureLockViewWidth, mGestureLockViewWidth);
				// 不是每行的第一个，则设置位置为前一个的右边
				if (i % mCount != 0) {
					lockerParams.addRule(RelativeLayout.RIGHT_OF,
							mGestureLockViews[i - 1].getId());
				}
				// 从第二行开始，设置为上一行同一位置View的下面
				if (i > mCount - 1) {
					lockerParams.addRule(RelativeLayout.BELOW,
							mGestureLockViews[i - mCount].getId());
				}
				// 设置右下左上的边距
				int rightMargin = mMarginBetweenLockView;
				int bottomMargin = mMarginBetweenLockView;
				int leftMagin = 0;
				int topMargin = 0;
				/**
				 * 每个View都有右外边距和底外边距 第一行的有上外边距 第一列的有左外边距
				 */
				if (i >= 0 && i < mCount) {// 第一行
					topMargin = mMarginBetweenLockView;
				}
				if (i % mCount == 0) {// 第一列
					leftMagin = mMarginBetweenLockView;
				}

				lockerParams.setMargins(leftMagin, topMargin, rightMargin,
						bottomMargin);
				mGestureLockViews[i].setMode(Mode.STATUS_NO_FINGER);
				addView(mGestureLockViews[i], lockerParams);
			}

			Log.e(TAG, "mWidth = " + mWidth + " ,  mGestureViewWidth = "
					+ mGestureLockViewWidth + " , mMarginBetweenLockView = "
					+ mMarginBetweenLockView);

		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// 重置
			reset();
			break;
		case MotionEvent.ACTION_MOVE:
			mPaint.setColor(mFingerOnColor);
			mPaint.setAlpha(50);
			GestureLockView child = getChildIdByPos(x, y);
			if (child != null) {
				int cId = child.getId();
				if (!mChoose.contains(cId)) {
					mChoose.add(cId);
					child.setMode(Mode.STATUS_FINGER_ON);
					if (mOnGestureLockViewListener != null)
						mOnGestureLockViewListener.onBlockSelected(cId);
					// 设置指引线的起点
					mLastPathX = child.getLeft() / 2 + child.getRight() / 2;
					mLastPathY = child.getTop() / 2 + child.getBottom() / 2;

					if (mChoose.size() == 1) {// 当前添加为第一个
						mPath.moveTo(mLastPathX, mLastPathY);
					} else {// 非第一个，将两者使用线连上
						mPath.lineTo(mLastPathX, mLastPathY);
					}

				}
			}
			// 指引线的终点
			mTmpTarget.x = x;
			mTmpTarget.y = y;
			break;
		case MotionEvent.ACTION_UP:

			mPaint.setColor(mFingerUpColor);
			mPaint.setAlpha(50);
			this.mTryTimes--;

			// 回调是否成功
			if (mOnGestureLockViewListener != null && mChoose.size() > 0) {
				mOnGestureLockViewListener.onGestureEvent(checkAnswer());
				if (this.mTryTimes == 0) {
					mOnGestureLockViewListener.onUnmatchedExceedBoundary();
				}
			}

			Log.e(TAG, "mUnMatchExceedBoundary = " + mTryTimes);
			Log.e(TAG, "mChoose = " + mChoose);
			// 将终点设置位置为起点，即取消指引线
			mTmpTarget.x = mLastPathX;
			mTmpTarget.y = mLastPathY;

			// 改变子元素的状态为UP
			changeItemMode();

			// 计算每个元素中箭头需要旋转的角度
			for (int i = 0; i + 1 < mChoose.size(); i++) {
				int childId = mChoose.get(i);
				int nextChildId = mChoose.get(i + 1);

				GestureLockView startChild = (GestureLockView) findViewById(childId);
				GestureLockView nextChild = (GestureLockView) findViewById(nextChildId);

				int dx = nextChild.getLeft() - startChild.getLeft();
				int dy = nextChild.getTop() - startChild.getTop();
				// 计算角度
				int angle = (int) Math.toDegrees(Math.atan2(dy, dx)) + 90;
				startChild.setArrowDegree(angle);
			}
			break;

		}
		invalidate();
		return true;
	}

	private void changeItemMode() {
		for (GestureLockView gestureLockView : mGestureLockViews) {
			if (mChoose.contains(gestureLockView.getId())) {
				gestureLockView.setMode(Mode.STATUS_FINGER_UP);
			}
		}
	}

	/**
	 * 
	 * 做一些必要的重置
	 */
	private void reset() {
		mChoose.clear();
		mPath.reset();
		for (GestureLockView gestureLockView : mGestureLockViews) {
			gestureLockView.setMode(Mode.STATUS_NO_FINGER);
			gestureLockView.setArrowDegree(-1);
		}
	}

	/**
	 * 检查用户绘制的手势是否正确
	 * 
	 * @return
	 */
	private boolean checkAnswer() {
		if (mAnswer.length != mChoose.size()){
			return false;
		}

		for (int i = 0; i < mAnswer.length; i++) {
			if (mAnswer[i] != mChoose.get(i)){
				return false;
			}
		}

		return true;
	}

	/**
	 * 检查当前左边是否在child中
	 * 
	 * @param child
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean checkPositionInChild(View child, int x, int y) {
		// 设置了内边距，即x,y必须落入下GestureLockView的内部中间的小区域中，可以通过调整padding使得x,y落入范围不变大，或者不设置padding
		int padding = (int) (mGestureLockViewWidth * 0.15);
		if (x >= child.getLeft() + padding && x <= child.getRight() - padding
				&& y >= child.getTop() + padding
				&& y <= child.getBottom() - padding) {
			return true;
		}
		return false;
	}

	/**
	 * 通过x,y获得落入的GestureLockView
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private GestureLockView getChildIdByPos(int x, int y) {
		for (GestureLockView gestureLockView : mGestureLockViews) {
			if (checkPositionInChild(gestureLockView, x, y)) {
				return gestureLockView;
			}
		}

		return null;
	}

	/**
	 * 设置回调接口
	 * 
	 * @param listener
	 */
	public void setOnGestureLockViewListener(OnGestureLockViewListener listener) {
		this.mOnGestureLockViewListener = listener;
	}

	/**
	 * 对外公布设置答案的方法
	 * 
	 * @param answer
	 */
	public void setAnswer(int[] answer) {
		this.mAnswer = answer;
	}

	/**
	 * 设置最大实验次数
	 * 
	 * @param boundary
	 */
	public void setUnMatchExceedBoundary(int boundary) {
		this.mTryTimes = boundary;
	}

	@Override
	public void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		// 绘制GestureLockView间的连线
		if (mPath != null) {
			canvas.drawPath(mPath, mPaint);
		}
		// 绘制指引线
		if (mChoose.size() > 0) {
			if (mLastPathX != 0 && mLastPathY != 0){
				canvas.drawLine(mLastPathX, mLastPathY, mTmpTarget.x,
						mTmpTarget.y, mPaint);
			}
				
		}

	}

	public interface OnGestureLockViewListener {
		/**
		 * 单独选中元素的Id
		 * @param cId
		 */
		public void onBlockSelected(int cId);

		/**
		 * 是否匹配
		 * @param matched
		 */
		public void onGestureEvent(boolean matched);

		/**
		 * 超过尝试次数
		 */
		public void onUnmatchedExceedBoundary();
	}
}
