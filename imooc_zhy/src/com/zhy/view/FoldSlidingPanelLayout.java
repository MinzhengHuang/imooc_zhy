package com.zhy.view;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author zhy http://blog.csdn.net/lmj623565791/
 */
public class FoldSlidingPanelLayout extends SlidingPaneLayout {
	public FoldSlidingPanelLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		View child = getChildAt(0);
		if (child != null) {

			removeView(child);
			final FoldLayout foldLayout = new FoldLayout(getContext());
			// foldLayout.setAnchor(0);
			foldLayout.addView(child);
			ViewGroup.LayoutParams layPar = child.getLayoutParams();
			addView(foldLayout, 0, layPar);

			setPanelSlideListener(new PanelSlideListener() {

				@Override
				public void onPanelSlide(View arg0, float arg1) {
					foldLayout.setFactor(arg1);
				}

				@Override
				public void onPanelOpened(View arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onPanelClosed(View arg0) {

				}
			});

		}
	}
}
