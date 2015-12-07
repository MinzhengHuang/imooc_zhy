package com.zhy.imageloader;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

import java.util.List;

public abstract class BasePopupWindowForListView<T> extends PopupWindow {
    protected View mContentView;//布局文件的最外层View
    protected Context context;
    protected List<T> mDatas;//ListView的数据集

    public BasePopupWindowForListView(View contentView, int width, int height,
                                      boolean focusable) {
        this(contentView, width, height, focusable, null);
    }

    public BasePopupWindowForListView(View contentView, int width, int height,
                                      boolean focusable, List<T> mDatas) {
        this(contentView, width, height, focusable, mDatas, new Object[0]);

    }

    public BasePopupWindowForListView(View contentView, int width, int height,
                                      boolean focusable, List<T> mDatas, Object... params) {
        super(contentView, width, height, focusable);
        this.mContentView = contentView;
        context = contentView.getContext();
        if (mDatas != null)
            this.mDatas = mDatas;

        if (params != null && params.length > 0) {
            beforeInitWeNeedSomeParams(params);
        }

        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        setOutsideTouchable(true);
        setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        initViews();
        initEvents();
        init();
    }

    protected abstract void beforeInitWeNeedSomeParams(Object... params);

    public abstract void initViews();

    public abstract void initEvents();

    public abstract void init();

    public View findViewById(int id) {
        return mContentView.findViewById(id);
    }

    protected static int dpToPx(Context context, int dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

}
