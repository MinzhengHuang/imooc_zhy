package com.zhy.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

@SuppressLint("HandlerLeak")
public class RolateAnimImageView extends ImageView {
    private static final int SCALE_REDUCE = 0;
    private static final int SCALING = 1;
    private static final int SCALE_ADD = 6;
    /**
     * 控件的宽
     */
    private int mWidth;
    /**
     * 控件的高
     */
    private int mHeight;
    /**
     * 控件的宽1/2
     */
    private int mCenterWidth;
    /**
     * 控件的高 1/2
     */
    private int mCenterHeight;
    /**
     * 设置一个缩放的常量
     */
    private float mMinScale = 0.85f;
    /**
     * 缩放是否结束
     */
    private boolean isFinish = true;

    public RolateAnimImageView(Context context) {
        this(context, null);
    }

    public RolateAnimImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RolateAnimImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 必要的初始化
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            mHeight = getHeight() - getPaddingTop() - getPaddingBottom();

            mCenterWidth = mWidth / 2;
            mCenterHeight = mHeight / 2;

            Drawable drawable = getDrawable();
            BitmapDrawable bd = (BitmapDrawable) drawable;
            bd.setAntiAlias(true);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mScaleHandler.sendEmptyMessage(SCALE_REDUCE);
                break;
            case MotionEvent.ACTION_UP:
                mScaleHandler.sendEmptyMessage(SCALE_ADD);
                break;
        }
        return true;
    }

    /**
     * 控制缩放的Handler
     */
    @SuppressLint("HandlerLeak")
    private Handler mScaleHandler = new Handler() {
        private Matrix matrix = new Matrix();
        private int count = 0;
        private float scale;
        /**
         * 是否已经调用了点击事件
         */
        private boolean isClicked;

        public void handleMessage(android.os.Message msg) {
            matrix.set(getImageMatrix());
            switch (msg.what) {
                case SCALE_REDUCE:
                    if (!isFinish) {
                        mScaleHandler.sendEmptyMessage(SCALE_REDUCE);
                    } else {
                        isFinish = false;
                        count = 0;
                        scale = (float) Math.sqrt(Math.sqrt(mMinScale));
                        beginScale(matrix, scale);//缩放
                        mScaleHandler.sendEmptyMessage(SCALING);
                    }
                    break;
                case SCALING:
                    beginScale(matrix, scale);
                    if (count < 4) {
                        mScaleHandler.sendEmptyMessage(SCALING);
                    } else {
                        isFinish = true;
                        if (RolateAnimImageView.this.mOnViewClickListener != null
                                && !isClicked) {
                            isClicked = true;
                            RolateAnimImageView.this.mOnViewClickListener.onViewClick(RolateAnimImageView.this);
                        } else {
                            isClicked = false;
                        }
                    }
                    count++;

                    break;
                case SCALE_ADD:
                    if (!isFinish) {
                        mScaleHandler.sendEmptyMessage(SCALE_ADD);
                    } else {
                        isFinish = false;
                        count = 0;
                        scale = (float) Math.sqrt(Math.sqrt(1.0f / mMinScale));
                        beginScale(matrix, scale);//缩放
                        mScaleHandler.sendEmptyMessage(SCALING);
                    }
                    break;
            }
        }
    };

    /**
     * 缩放
     *
     * @param matrix
     * @param scale
     */
    private synchronized void beginScale(Matrix matrix, float scale) {
        Log.d("main", "scale=" + scale + ",mCenterWidth=" + mCenterWidth + ",mCenterHeight=" + mCenterHeight);
        matrix.postScale(scale, scale, mCenterWidth, mCenterHeight);
        setImageMatrix(matrix);
    }

    /**
     * 回调接口
     */
    private OnViewClickListener mOnViewClickListener;

    public void setOnClickIntent(OnViewClickListener onViewClickListener) {
        this.mOnViewClickListener = onViewClickListener;
    }

    public interface OnViewClickListener {
        void onViewClick(RolateAnimImageView view);
    }

}

