package com.motorola.widget.circlewidget3d;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;

public class CircleExpand extends Circle {
	private static final String PREF_EXPAND_STATE = "expand_state";
	private static CircleExpand mInstance;
	private static boolean sExpanded = true;
	ImageView mExpandImage;

	private CircleExpand(Context paramContext) {
		this.mCurrentId = 0;
		this.mContext = paramContext;
		prepareCircle(R.layout.expand_circle, CircleConsts.GUIDE_BITMAP_SIZE.intValue());
		this.mIsFlipped = true;
		sExpanded = retrieveExpandState();
	}

	private Bitmap getCircleScreen(int paramInt) {
		this.mExpandImage.setImageResource(paramInt);
		this.mBitmap.eraseColor(0);
		this.mLayout.draw(this.mCanvas);
		return this.mBitmap;
	}

	public static CircleExpand getInstance(Context paramContext) {
		synchronized (syncObject) {
			if (mInstance == null)
				mInstance = new CircleExpand(paramContext);
			return mInstance;
		}
	}

	public static boolean isCircleExpanded() {
		return sExpanded;
	}

	private boolean retrieveExpandState() {
		return PreferenceManager.getDefaultSharedPreferences(this.mContext)
				.getBoolean(PREF_EXPAND_STATE, false);
	}

	public static void setCircleExpandedState(boolean paramBoolean) {
		sExpanded = paramBoolean;
	}

	private void storeExpandState(boolean paramBoolean) {
		SharedPreferences.Editor localEditor = PreferenceManager
				.getDefaultSharedPreferences(this.mContext).edit();
		localEditor.putBoolean(PREF_EXPAND_STATE, paramBoolean);
		localEditor.apply();
	}

	public Bitmap getBackTexture(Bundle paramBundle) {
		return null;
	}

	public Bitmap getFrontTexture(Bundle paramBundle) {
		return null;
	}

	public boolean handleFling(Messenger paramMessenger, Message paramMessage,
			Float paramFloat) {
		return false;
	}

	public boolean handleSingleTap(Bundle paramBundle) {
		boolean flag;
		if (!sExpanded)
			flag = true;
		else
			flag = false;
		sExpanded = flag;
		updateCircle();
		storeExpandState(sExpanded);
		return false;
	}

	public View prepareCircle(int paramInt1, int paramInt2) {
		View localView = super.prepareCircle(paramInt1, paramInt2);
		this.mExpandImage = ((ImageView) localView.findViewById(R.id.expand_image));
		return localView;
	}

	public void setTheme(String paramString) {
	}

	public void updateCircle() {
		int i;
		if (sExpanded)
			i = R.drawable.ic_circle_widget_collapse;
		else
			i = R.drawable.ic_circle_widget_expand;
		Utility.updateTexture(null, "circle_expand/circlefront",
				getCircleScreen(i));
	}

	public void updateCircle(int paramInt) {
		Utility.updateTexture(null, "circle_expand/circlefront",
				getCircleScreen(paramInt));
	}

	public void updateCircle(Context paramContext, Intent paramIntent) {
		updateCircle();
	}

	public void updateValues(Context paramContext, Intent paramIntent) {
	}
}

/*
 * Location: J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.widget.circlewidget3d.CircleExpand JD-Core Version: 0.6.2
 */