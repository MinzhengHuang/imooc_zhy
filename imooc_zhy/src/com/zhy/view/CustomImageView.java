package com.zhy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.zhy.R;

/**
 * Android 自定义View (二) 进阶
 *
 * http://blog.csdn.net/lmj623565791/article/details/24300125
 *
 *
 * 步骤:
 *1、自定义View的属性
 *2、在View的构造方法中获得我们自定义的属性
 *[ 3、重写onMesure ]
 *4、重写onDraw
 *
 */
public class CustomImageView extends View {
	/**
	 * 控件的宽
	 */
	private int mWidth;
	/**
	 * 控件的高
	 */
	private int mHeight;
	/**
	 * 控件中的图片
	 */
	private Bitmap mImage;
	/**
	 * 图片的缩放模式
	 */
	private int mImageScale;
	private static final int IMAGE_SCALE_FITXY = 0;
	private static final int IMAGE_SCALE_CENTER = 1;
	/**
	 * 图片的介绍
	 */
	private String mTitle;
	/**
	 * 字体的颜色
	 */
	private int mTextColor;
	/**
	 * 字体的大小
	 */
	private int mTextSize;

	private Paint mPaint;
	/**
	 * 对文本的约束
	 */
	private Rect mTextBound;
	/**
	 * 控制整体布局
	 */
	private Rect rect;

	public CustomImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomImageView(Context context) {
		this(context, null);
	}

	/**
	 * 初始化所特有自定义类型
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.CustomImageView, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.CustomImageView_image:
				mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
				break;
			case R.styleable.CustomImageView_imageScaleType:
				mImageScale = a.getInt(attr, 0);
				break;
			case R.styleable.CustomImageView_titleText:
				mTitle = a.getString(attr);
				break;
			case R.styleable.CustomImageView_titleTextColor:
				mTextColor = a.getColor(attr, Color.BLACK);
				break;
			case R.styleable.CustomImageView_titleTextSize:
				mTextSize = a.getDimensionPixelSize(attr, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
								getResources().getDisplayMetrics()));
				break;

			}
		}
		a.recycle();//回收

		rect = new Rect();
		mPaint = new Paint();
		mTextBound = new Rect();
		mPaint.setTextSize(mTextSize);
		// 计算了描绘字体需要的范围
		mPaint.getTextBounds(mTitle, 0, mTitle.length(), mTextBound);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/**
		 * 设置宽度
		 */
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);

		if (specMode == MeasureSpec.EXACTLY) {// match_parent , accurate
			Log.e("main", "EXACTLY");
			mWidth = specSize;
		} else {
			// 由图片决定的宽
			int desireByImg = getPaddingLeft() + getPaddingRight() + mImage.getWidth();
			// 由字体决定的宽
			int desireByTitle = getPaddingLeft() + getPaddingRight() + mTextBound.width();

			if (specMode == MeasureSpec.AT_MOST) {// wrap_content
				int desire = Math.max(desireByImg, desireByTitle);
				mWidth = Math.min(desire, specSize);
				Log.e("main", "AT_MOST");
			}
		}

		/***
		 * 设置高度
		 */
		specMode = MeasureSpec.getMode(heightMeasureSpec);
		specSize = MeasureSpec.getSize(heightMeasureSpec);
		if (specMode == MeasureSpec.EXACTLY) {// match_parent , accurate
			mHeight = specSize;
		} else {
			int desire = getPaddingTop() + getPaddingBottom()
					+ mImage.getHeight() + mTextBound.height();
			if (specMode == MeasureSpec.AT_MOST) {// wrap_content
				mHeight = Math.min(desire, specSize);
			}
		}
		setMeasuredDimension(mWidth, mHeight);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		/**
		 * 边框
		 */
		mPaint.setStrokeWidth(4);//边框宽度
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(Color.CYAN);
		canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

		rect.left = getPaddingLeft();
		rect.right = mWidth - getPaddingRight();
		rect.top = getPaddingTop();
		rect.bottom = mHeight - getPaddingBottom();

		mPaint.setColor(mTextColor);
		mPaint.setStyle(Style.FILL);
		/**
		 * 当前设置的宽度小于字体需要的宽度，将字体改为xxx...
		 * ellipsize：当文字长度超过textview宽度时的显示方式
		 */
		if (mTextBound.width() > mWidth) {
			TextPaint paint = new TextPaint(mPaint);
			String msg = TextUtils.ellipsize(mTitle, paint,
					(float) mWidth - getPaddingLeft() - getPaddingRight(),
					TextUtils.TruncateAt.END).toString();
			canvas.drawText(msg, getPaddingLeft(),
					mHeight - getPaddingBottom(), mPaint);

		} else {
			// 正常情况，将字体居中
			canvas.drawText(mTitle, mWidth / 2 - mTextBound.width() * 1.0f / 2,
					mHeight - getPaddingBottom(), mPaint);
		}

		// 取消使用掉的快
		rect.bottom -= mTextBound.height();

		if (mImageScale == IMAGE_SCALE_FITXY) {
			canvas.drawBitmap(mImage, null, rect, mPaint);
		} else {
			// 计算居中的矩形范围
			rect.left = mWidth / 2 - mImage.getWidth() / 2;
			rect.right = mWidth / 2 + mImage.getWidth() / 2;
			rect.top = (mHeight - mTextBound.height()) / 2 - mImage.getHeight() / 2;
			rect.bottom = (mHeight - mTextBound.height()) / 2 + mImage.getHeight() / 2;

			canvas.drawBitmap(mImage, null, rect, mPaint);
		}

	}

}

