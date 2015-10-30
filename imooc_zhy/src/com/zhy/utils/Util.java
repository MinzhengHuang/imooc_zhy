package com.zhy.utils;

import android.content.res.Resources;
import android.util.TypedValue;

public class Util {

	public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				res.getDisplayMetrics());
	}

	/**
	 * dp 2 px
	 *
	 * @param dpVal
	 */
	public static int dp2px(Resources res,int dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, res.getDisplayMetrics());
	}

	/**
	 * sp 2 px
	 *
	 * @param spVal
	 * @return
	 */
	public static int sp2px(Resources res,int spVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spVal, res.getDisplayMetrics());

	}
}
