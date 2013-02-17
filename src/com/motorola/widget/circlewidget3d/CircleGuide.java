package com.motorola.widget.circlewidget3d;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

public class CircleGuide extends Circle {
	public static final String GUIDE_COMPONENT = "com.motorola.genie.app.GenieMainActivity";
	public static final String GUIDE_PKG = "com.motorola.genie";
	private static CircleGuide mInstance;
	private static boolean sIsGuidemeCircleEnabled;
	private static boolean sIsHideGuidemeImage;

	private CircleGuide(Context paramContext) {
		this.mCurrentId = 0;
		this.mContext = paramContext;
		prepareCircle(0x7f03000e, CircleConsts.GUIDE_BITMAP_SIZE.intValue());
		this.mIsFlipped = true;
		SharedPreferences localSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this.mContext);
		sIsGuidemeCircleEnabled = localSharedPreferences.getBoolean(
				"show_guide_me", true);
		sIsHideGuidemeImage = localSharedPreferences.getBoolean(
				"do_not_display_guide_tip_image", false);
	}

	public static Intent getGuideAppIntent() {
		Intent localIntent = new Intent("android.intent.action.MAIN");
		localIntent.addCategory("android.intent.category.LAUNCHER");
		localIntent.setComponent(new ComponentName("com.motorola.genie",
				"com.motorola.genie.app.GenieMainActivity"));
		localIntent.setFlags(337641472);
		return localIntent;
	}

	public static CircleGuide getInstance(Context paramContext) {
		synchronized (syncObject) {
			if (mInstance == null)
				mInstance = new CircleGuide(paramContext);
			return mInstance;
		}
	}

	public static boolean isGuidemeCircleEnabled() {
		return sIsGuidemeCircleEnabled;
	}

	public static boolean isHideGuidemeImage() {
		return sIsHideGuidemeImage;
	}

	public Bitmap getBackTexture(Bundle paramBundle) {
		return null;
	}

	public Bitmap getFrontTexture(Bundle paramBundle) {
		return null;
	}

	public Bitmap getGuideScreen() {
		this.mBitmap.eraseColor(0);
		this.mLayout.draw(this.mCanvas);
		return this.mBitmap;
	}

	public boolean handleFling(Messenger messenger, Message message,
			Float float1) {
		Utility.flipCircle(messenger, "circle_help", float1.floatValue(),
				mIsFlipped);
		boolean flag;
		if (!mIsFlipped)
			flag = true;
		else
			flag = false;
		mIsFlipped = flag;
		return true;
	}

	public boolean handleSingleTap(Bundle paramBundle) {
		startGenieApp();
		return true;
	}

	public View prepareCircle(int paramInt1, int paramInt2) {
		return super.prepareCircle(paramInt1, paramInt2);
	}

	public void setTheme(String paramString) {
	}

	public void startGenieApp() {
		Intent localIntent = getGuideAppIntent();
		try {
			this.mContext.startActivity(localIntent);
			if (!sIsHideGuidemeImage)
				updateHideGuideImageValue(true);
			return;
		} catch (Exception localException) {
			Log.e("Circle", "Couldn't start Guide activity" + localException);
		}
	}

	public void updateCircle() {
		Utility.updateTexture(null, "circle_help/circlefront", getGuideScreen());
		Utility.updateTexture(null, "circle_help/circleback", getGuideScreen());
	}

	public void updateCircle(Context paramContext, Intent paramIntent) {
		updateCircle();
	}

	public void updateHideGuideImageValue(boolean paramBoolean) {
		SharedPreferences.Editor localEditor = PreferenceManager
				.getDefaultSharedPreferences(this.mContext).edit();
		localEditor.putBoolean("do_not_display_guide_tip_image", paramBoolean);
		localEditor.apply();
		sIsHideGuidemeImage = paramBoolean;
	}

	public void updateSettingValues(Intent intent) {
		boolean flag = PreferenceManager.getDefaultSharedPreferences(mContext)
				.getBoolean("show_guide_me", sIsGuidemeCircleEnabled);
		if (flag != sIsGuidemeCircleEnabled) {
			sIsGuidemeCircleEnabled = flag;
			boolean flag1;
			if (sIsGuidemeCircleEnabled)
				flag1 = true;
			else
				flag1 = false;
			if (flag1) {
				updateCircle();
				boolean aflag[] = { true };
				Utility.changeVisibility(null, new String[] { "circle_help" },
						aflag);
			}
			if (!sIsGuidemeCircleEnabled)
				;
			if (!sIsGuidemeCircleEnabled)
				;
		}
	}

	public void updateValues(Context paramContext, Intent paramIntent) {
	}
}

/*
 * Location: J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.widget.circlewidget3d.CircleGuide JD-Core Version: 0.6.2
 */