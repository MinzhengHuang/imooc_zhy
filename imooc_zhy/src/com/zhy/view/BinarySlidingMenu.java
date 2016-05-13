package com.zhy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.zhy.R;
import com.zhy.utils.ScreenUtils;

/**
 * http://blog.csdn.net/lmj623565791/article/details/39670935
 * 
 * @author zhy
 * 
 */
public class BinarySlidingMenu extends HorizontalScrollView {
	/**
	 * 菜单的宽度
	 */
	private int mMenuWidth;
	private int mHalfMenuWidth;
	private boolean isOperateRight;
	private boolean isOperateLeft;
	private boolean once;
	private ViewGroup mLeftMenu;
	private ViewGroup mContent;
	private ViewGroup mRightMenu;
	private ViewGroup mWrapper;
	private boolean isLeftMenuOpen;
	private boolean isRightMenuOpen;
	private int mScreenWidth;//屏幕宽度
	private int mMenuRightPadding;//dp 菜单距离屏幕的右边距

	/**
	 * 回调的接口
	 */
	public interface OnMenuOpenListener {
		/**
		 * 
		 * @param isOpen
		 *            true打开菜单，false关闭菜单
		 * @param flag
		 *            0 左侧， 1右侧
		 */
		void onMenuOpen(boolean isOpen, int flag);
	}

	public OnMenuOpenListener mOnMenuOpenListener;

	public void setOnMenuOpenListener(OnMenuOpenListener mOnMenuOpenListener) {
		this.mOnMenuOpenListener = mOnMenuOpenListener;
	}

	public BinarySlidingMenu(Context context) {
		this(context, null, 0);
	}

	public BinarySlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BinarySlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScreenWidth = ScreenUtils.getScreenWidth(context);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.BinarySlidingMenu, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.BinarySlidingMenu_rightPadding:
				// 默认50
				mMenuRightPadding = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50f,
								getResources().getDisplayMetrics()));// 默认为10DP
				break;
			}
		}
		a.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/**
		 * 显示的设置一个宽度
		 */
		if (!once) {
			mWrapper = (LinearLayout) getChildAt(0);
			mLeftMenu = (ViewGroup) mWrapper.getChildAt(0);
			mContent = (ViewGroup) mWrapper.getChildAt(1);
			mRightMenu = (ViewGroup) mWrapper.getChildAt(2);

			mMenuWidth = mScreenWidth - mMenuRightPadding;
			mHalfMenuWidth = mMenuWidth / 2;
			mLeftMenu.getLayoutParams().width = mMenuWidth;
			mContent.getLayoutParams().width = mScreenWidth;
			mRightMenu.getLayoutParams().width = mMenuWidth;

		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			// 将菜单隐藏
			this.scrollTo(mMenuWidth, 0);
			once = true;
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		// Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
		case MotionEvent.ACTION_UP:
			int scrollX = getScrollX();
			// 如果是操作左侧菜单
			if (isOperateLeft) {
				// 如果影藏的区域大于菜单一半，则影藏菜单
				if (scrollX > mHalfMenuWidth) {
					this.smoothScrollTo(mMenuWidth, 0);
					// 如果当前左侧菜单是开启状态，且mOnMenuOpenListener不为空，则回调关闭菜单
					if (isLeftMenuOpen && mOnMenuOpenListener != null) {
						// 第一个参数true：打开菜单，false：关闭菜单;第二个参数 0 代表左侧；1代表右侧
						mOnMenuOpenListener.onMenuOpen(false, 0);
					}
					isLeftMenuOpen = false;

				} else { // 关闭左侧菜单
					this.smoothScrollTo(0, 0);
					// 如果当前左侧菜单是关闭状态，且mOnMenuOpenListener不为空，则回调打开菜单
					if (!isLeftMenuOpen && mOnMenuOpenListener != null) {
						mOnMenuOpenListener.onMenuOpen(true, 0);
					}
					isLeftMenuOpen = true;
				}
			}

			// 操作右侧
			if (isOperateRight) {
				// 打开右侧侧滑菜单
				if (scrollX > mHalfMenuWidth + mMenuWidth) {
					this.smoothScrollTo(mMenuWidth + mMenuWidth, 0);
				} else { // 关闭右侧侧滑菜单
					this.smoothScrollTo(mMenuWidth, 0);
				}
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (l > mMenuWidth) {
			isOperateRight = true;
			isOperateLeft = false;
		} else {
			isOperateRight = false;
			isOperateLeft = true;

		}
	}

}
