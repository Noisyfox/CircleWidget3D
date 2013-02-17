package com.motorola.widget.circlewidget3d;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;

public abstract class Circle {
	public static final boolean LOGD = false;
	public static final String TAG = "Circle";
	public static final Object syncObject = new Object();
	public View mBackLayout;
	public Bitmap mBitmap;
	public final BitmapFactory.Options mBitmapOptions = new BitmapFactory.Options();
	public Canvas mCanvas;
	public Context mContext;
	public int mCurrentId;
	public View mFrontLayout;
	public boolean mIsFlipped = false;
	public FrameLayout mLayout;
	public boolean mRefreshNeeded = false;
	public int mTextColor;
	public String mThemePackage;

	public abstract Bitmap getBackTexture(Bundle paramBundle);

	public abstract Bitmap getFrontTexture(Bundle paramBundle);

	public boolean handleAnimationComplete(String paramString) {
		return false;
	}

	public void handleDestroy() {
		if (this.mBitmap != null) {
			this.mBitmap.recycle();
			this.mBitmap = null;
		}
	}

	public abstract boolean handleFling(Messenger paramMessenger,
			Message paramMessage, Float paramFloat);

	public abstract boolean handleSingleTap(Bundle paramBundle);

	public boolean isFlipped() {
		return this.mIsFlipped;
	}

	public View prepareCircle(int paramInt1, int paramInt2) {
		this.mBitmapOptions.inInputShareable = true;
		this.mBitmapOptions.inPurgeable = true;
		this.mBitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
		this.mBitmap = Bitmap.createBitmap(paramInt2, paramInt2,
				Bitmap.Config.ARGB_8888);
		this.mCanvas = new Canvas(this.mBitmap);
		this.mLayout = new FrameLayout(this.mContext);
		this.mLayout.setLayoutParams(new FrameLayout.LayoutParams(this.mBitmap
				.getWidth(), this.mBitmap.getHeight()));
		View localView = View.inflate(this.mContext, paramInt1, this.mLayout);
		this.mLayout.measure(View.MeasureSpec.makeMeasureSpec(
				this.mBitmap.getWidth(), 1073741824), View.MeasureSpec
				.makeMeasureSpec(this.mBitmap.getHeight(), 1073741824));
		this.mLayout.layout(0, 0, this.mLayout.getMeasuredWidth(),
				this.mLayout.getMeasuredHeight());
		return localView;
	}

	public void refreshCircleIfNeeded() {
		if (this.mRefreshNeeded) {
			updateCircle();
			this.mRefreshNeeded = false;
		}
	}

	public boolean retrieveSidePref(String paramString) {
		return PreferenceManager.getDefaultSharedPreferences(this.mContext)
				.getBoolean(paramString, false);
	}

	public void setFlipped(boolean paramBoolean) {
		this.mIsFlipped = paramBoolean;
	}

	public abstract void setTheme(String paramString);

	public void storeSidePref(String paramString) {
		SharedPreferences.Editor localEditor = PreferenceManager
				.getDefaultSharedPreferences(this.mContext).edit();
		localEditor.putBoolean(paramString, this.mIsFlipped);
		localEditor.apply();
	}

	public abstract void updateCircle();

	public abstract void updateCircle(Context paramContext, Intent paramIntent);

	public abstract void updateValues(Context paramContext, Intent paramIntent);
}

/*
 * Location: J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.widget.circlewidget3d.Circle JD-Core Version: 0.6.2
 */