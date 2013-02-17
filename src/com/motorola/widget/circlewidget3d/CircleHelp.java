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

public class CircleHelp extends Circle {
	private static String HELP_COMPONENT;
	private static String HELP_PKG = "";
	private static CircleHelp mInstance;
	private static String sClockSideTextureName = null;
	private static boolean sDonotDisplayHelpAnyMore;
	private static boolean sHelpScreenIsDisplayed;

	static {
		HELP_COMPONENT = "";
		sHelpScreenIsDisplayed = false;
		sDonotDisplayHelpAnyMore = false;
	}

	private CircleHelp(Context paramContext) {
		mContext = paramContext;
		mCurrentId = 0;
		prepareCircle(0x7f03000f, CircleConsts.CLOCK_BITMAP_SIZE.intValue());
		sDonotDisplayHelpAnyMore = PreferenceManager
				.getDefaultSharedPreferences(mContext).getBoolean(
						"do_not_display_help", false);
	}

	private Bitmap getHelpScreen() {
		mBitmap.eraseColor(0);
		mLayout.draw(mCanvas);
		return mBitmap;
	}

	public static CircleHelp getInstance(Context paramContext) {
		synchronized (syncObject) {
			if (mInstance == null) {
				mInstance = new CircleHelp(paramContext);
			}
			return mInstance;
		}
	}

	public static String getTextureSideOfHelpScreen() {
		return sClockSideTextureName;
	}

	public static boolean isDoNotShowHelpEnabled() {
		return sDonotDisplayHelpAnyMore;
	}

	public static boolean isHelpDisplayed() {
		return sHelpScreenIsDisplayed;
	}

	public static void setHelpDisplayed(boolean paramBoolean) {
		sHelpScreenIsDisplayed = paramBoolean;
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

	public boolean handleSingleTap(Bundle bundle) {
		Intent intent = new Intent("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		intent.setComponent(new ComponentName(HELP_PKG, HELP_COMPONENT));
		intent.setFlags(0x14200000);
		try {
			mContext.startActivity(intent);
		} catch (Exception exception) {
			Log.e("Circle",
					(new StringBuilder())
							.append("Couldn't start Helpfor Circle activity")
							.append(exception).toString());
		}
		return true;
	}

	public void hideHelpScreen() {
		if (!sDonotDisplayHelpAnyMore) {
			SharedPreferences.Editor localEditor = PreferenceManager
					.getDefaultSharedPreferences(mContext).edit();
			localEditor.putBoolean("do_not_display_help", true);
			localEditor.apply();
			sDonotDisplayHelpAnyMore = true;
		}
		if (sHelpScreenIsDisplayed) {
			sHelpScreenIsDisplayed = false;
			Utility.showAnalogClock();
			return;
		}
		CircleAlert.setHelpDisplayAgain(false);
	}

	public void restoreHelpState() {
		updateCircle();
	}

	public void setTheme(String paramString) {
	}

	public void updateCircle() {
		if (!sDonotDisplayHelpAnyMore) {
			sClockSideTextureName = "circle_time/circlefront";
			if (CircleAlert.isAlertDisplayed()) {
				int i = CircleAlert.getAlertDisplayedSide();
				if (i != -1) {
					String s;
					if (i == 0)
						s = "circle_time/circlefront";
					else
						s = "circle_time/circleback";
					sClockSideTextureName = s;
				}
			}
			Utility.updateTexture(null, sClockSideTextureName, getHelpScreen());
			Utility.updateTexture(
					null,
					(new StringBuilder()).append(sClockSideTextureName)
							.append(":t1").toString(), "alert_front_mask");
			sHelpScreenIsDisplayed = true;
		}
	}

	public void updateCircle(Context paramContext, Intent paramIntent) {
		if (paramIntent.getBooleanExtra("force", false)) {
			sDonotDisplayHelpAnyMore = false;
		}
		updateCircle();
	}

	public void updateValues(Context paramContext, Intent paramIntent) {
	}
}

/*
 * Location: J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.widget.circlewidget3d.CircleHelp JD-Core Version: 0.6.2
 */