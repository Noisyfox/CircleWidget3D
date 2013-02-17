package com.motorola.widget.circlewidget3d;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.motorola.homescreen.common.anim.AnimUtils;
import java.util.Calendar;
import org.json.JSONArray;

public class CircleClock extends Circle {
	private static final char CHAR_DAY = 'd';
	private static final char CHAR_MONTH = 'M';
	public static String[] CLOCK_HANDS_SHAPES = { "circle_time/hourhand",
			"circle_time/hourhandshadow", "circle_time/minutehand",
			"circle_time/minutehandshadow" };
	private static final String FONT_NAME = "fonts/Chulho_Neue_v09.ttf";
	private static CircleClock mInstance;
	ImageView mAlarmImageView;
	private int mAnalogClockState = -1;
	private int mCurrentTextureUnit = 0;
	TextView mDateView;
	private boolean mIsAlarmset;
	private String mNextAlarmTime;
	String mTimeFormat;
	TextView mTimeView;

	private CircleClock(Context paramContext) {
		this.mContext = paramContext;
		this.mCurrentId = 0;
		this.mAnalogClockState = -1;
		prepareCircle(0x7f030006, CircleConsts.CLOCK_BITMAP_SIZE.intValue());
		this.mIsFlipped = retrieveSidePref("clock_side_front");
	}

	private Bitmap getAnalogClock() {
		this.mFrontLayout.setVisibility(0);
		this.mBackLayout.setVisibility(8);
		this.mBitmap.eraseColor(0);
		this.mLayout.draw(this.mCanvas);
		return this.mBitmap;
	}

	private Bitmap getDigitalClock() {
		Calendar calendar = Calendar.getInstance();
		long l = System.currentTimeMillis();
		calendar.setTimeInMillis(l);
		mTimeView.setText(Utility.getTimeString(mContext, calendar));
		ImageView imageview = mAlarmImageView;
		int i;
		String s;
		if (mIsAlarmset)
			i = 0x7f020021;
		else
			i = 0x7f020022;
		imageview.setImageResource(i);
		s = DateUtils.formatDateTime(mContext, l, 0x8001a);
		mDateView.setText(s);
		mFrontLayout.setVisibility(8);
		mBackLayout.setVisibility(0);
		mBitmap.eraseColor(0);
		mLayout.draw(mCanvas);
		return mBitmap;
	}

	public static CircleClock getInstance(Context paramContext) {
		synchronized (syncObject) {
			if (mInstance == null)
				mInstance = new CircleClock(paramContext);
			return mInstance;
		}
	}

	private void retrieveAlarmInfo(Intent paramIntent) {
		if (paramIntent.getAction().equals(
				"android.intent.action.ALARM_CHANGED")) {
			this.mIsAlarmset = paramIntent.getBooleanExtra("alarmSet", false);
			return;
		}
		getAlarmCondition();
	}

	public boolean getAlarmCondition() {
		mNextAlarmTime = android.provider.Settings.System.getString(
				mContext.getContentResolver(), "next_alarm_formatted");
		if (mNextAlarmTime == null || TextUtils.isEmpty(mNextAlarmTime))
			mIsAlarmset = false;
		else
			mIsAlarmset = true;
		return mIsAlarmset;
	}

	public JSONArray getAnalogClockHandAnimation(Context paramContext,
			long paramLong) {
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.setTimeInMillis(System.currentTimeMillis());
		int i = localCalendar.get(12);
		int j = localCalendar.get(11);
		if (j == 12)
			j = 0;
		if (i == 60)
			i = 0;
		JSONArray localJSONArray = new JSONArray();
		float[] arrayOfFloat = new float[3];
		arrayOfFloat[0] = (-(i * 6));
		arrayOfFloat[1] = 0.0F;
		arrayOfFloat[2] = 0.0F;
		localJSONArray.put(AnimUtils.createJSONData3D("circle_time/minutehand",
				"rotation", null, arrayOfFloat, (float) paramLong, false));
		localJSONArray.put(AnimUtils.createJSONData3D(
				"circle_time/minutehandshadow", "rotation", null, arrayOfFloat,
				(float) paramLong, false));
		arrayOfFloat[0] = (-(j * 30 + i / 2));
		arrayOfFloat[1] = 0.0F;
		arrayOfFloat[2] = 0.0F;
		localJSONArray.put(AnimUtils.createJSONData3D("circle_time/hourhand",
				"rotation", null, arrayOfFloat, (float) paramLong, false));
		localJSONArray.put(AnimUtils.createJSONData3D(
				"circle_time/hourhandshadow", "rotation", null, arrayOfFloat,
				(float) paramLong, false));
		return localJSONArray;
	}

	public Bitmap getBackTexture(Bundle paramBundle) {
		return null;
	}

	public Bitmap getFrontTexture(Bundle paramBundle) {
		return null;
	}

	public void handleDestroy() {
		super.handleDestroy();
		mInstance = null;
	}

	public boolean handleFling(Messenger messenger, Message message,
			Float float1) {
		Utility.flipCircle(messenger, "circle_time", float1.floatValue(),
				mIsFlipped);
		boolean flag;
		if (!mIsFlipped)
			flag = true;
		else
			flag = false;
		mIsFlipped = flag;
		storeSidePref("clock_side_front");
		return false;
	}

	public boolean handleSingleTap(Bundle paramBundle) {
		int i = 1;
		if (CircleAlert.isAlertDisplayed()) {
			String str2 = paramBundle.getString("shape_name");
			int j = CircleAlert.getAlertDisplayedSide();
			if (((j == 0) && (str2.equals("circle_time/circlefront")))
					|| ((j == 1) && (str2.equals("circle_time/circleback")))) {
				CircleAlert.getInstance(this.mContext).handleSingleTap(
						paramBundle);
				i = 0;
			}
		}
		if (CircleHelp.isHelpDisplayed()) {
			String str1 = paramBundle.getString("shape_name");
			if (CircleHelp.getTextureSideOfHelpScreen().equals(str1)) {
				Log.d("Circle", "Not doint anything as help side was clicked");
				i = 0;
			}
		}
		if (i != 0)
			startClockApp();
		return true;
	}

	public View prepareCircle(int paramInt1, int paramInt2) {
		View localView = super.prepareCircle(paramInt1, paramInt2);
		this.mDateView = ((TextView) localView.findViewById(2131427340));
		this.mTimeView = ((TextView) localView.findViewById(2131427341));
		this.mAlarmImageView = ((ImageView) localView.findViewById(2131427342));
		this.mFrontLayout = localView.findViewById(2131427338);
		this.mBackLayout = localView.findViewById(2131427339);
		this.mTimeFormat = this.mContext.getResources().getString(2131230730);
		Typeface localTypeface = Typeface.createFromAsset(
				this.mContext.getAssets(), "fonts/Chulho_Neue_v09.ttf");
		if (localTypeface != null)
			this.mTimeView.setTypeface(localTypeface);
		return localView;
	}

	public void setAnalogClockState(int paramInt) {
		this.mAnalogClockState = paramInt;
	}

	public void setCurrentTextureUnit(int i) {
		boolean flag = true;
		boolean flag1 = true;
		mCurrentTextureUnit = i;

		if (CircleAlert.isAlertDisplayed()) {
			int j = CircleAlert.getAlertDisplayedSide();
			if (j == 0) {
				flag = false;
			} else if (j == 1) {
				flag1 = false;
			}
		}

		if (flag)
			Utility.updateTexture(null,
					(new StringBuilder()).append("circle_time/circlefront:t")
							.append(i).toString(), getDigitalClock());
		if (flag1)
			Utility.updateTexture(null,
					(new StringBuilder()).append("circle_time/circleback:t")
							.append(i).toString(), getAnalogClock());
	}

	public void setTheme(String s) {
		mThemePackage = s;
		if (s.equals("com.motorola.widget.circlewidget3d")) {
			mFrontLayout.setBackgroundResource(0x7f020012);
			mBackLayout.setBackgroundResource(0x7f020010);
			mDateView.setTextColor(-1);
			mTimeView.setTextColor(-1);
			Utility.updateTexture(null, "circle_time/hourhand", "clock_hands");
			Utility.updateTexture(null, "circle_time/minutehand", "clock_hands");
		} else {
			android.graphics.drawable.Drawable drawable = ThemeInfo
					.getDrawable(mContext, s, "clock_front");
			android.graphics.drawable.Drawable drawable1 = ThemeInfo
					.getDrawable(mContext, s, "clock_back");
			android.graphics.drawable.Drawable drawable2 = ThemeInfo
					.getDrawable(mContext, s, "clock_hands");
			if (drawable != null)
				mBackLayout.setBackgroundDrawable(drawable);
			if (drawable1 != null)
				mFrontLayout.setBackgroundDrawable(drawable1);
			if (drawable2 != null) {
				Bitmap bitmap = ((BitmapDrawable) drawable2).getBitmap();
				Utility.updateTexture(null, "circle_time/hourhand", bitmap);
				Utility.updateTexture(null, "circle_time/minutehand", bitmap);
			}
			int i = ThemeInfo.getTextColor(mContext, s, "text_color");
			if (i != -1) {
				mDateView.setTextColor(i);
				mTimeView.setTextColor(i);
			}
		}
	}

	public void startClockApp() {
		Intent intent = new Intent("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		Resources resources = mContext.getResources();
		intent.setComponent(new ComponentName(resources.getString(0x7f080004),
				resources.getString(0x7f080005)));
		intent.setFlags(0x14200000);
		try {
			mContext.startActivity(intent);
			return;
		} catch (Exception exception) {
			Log.e("Circle",
					(new StringBuilder())
							.append("Couldn't start Clock activity")
							.append(exception).toString());
		}
	}

	public void updateCircle() {
		int i;
		int j;
		boolean flag;
		boolean flag1;
		boolean flag2;
		boolean flag3;
		if (mIsAlarmset)
			i = 1;
		else
			i = 0;
		j = mAnalogClockState;
		flag = false;
		if (i != j) {
			flag = true;
			mAnalogClockState = i;
		}
		flag1 = true;
		flag2 = true;
		flag3 = true;
		if (CircleAlert.isAlertDisplayed()) {
			int k = CircleAlert.getAlertDisplayedSide();
			if (k == 0)
				flag1 = false;
			else if (k == 1) {
				flag3 = false;
				flag2 = false;
			}
		}
		if (CircleHelp.isHelpDisplayed()) {
			String s = CircleHelp.getTextureSideOfHelpScreen();
			if (s != null)
				if (s.equals("circle_time/circlefront"))
					flag1 = false;
				else if (s.equals("circle_time/circleback")) {
					flag3 = false;
					flag2 = false;
				}
		}
		if (flag1)
			Utility.updateTexture(null,
					(new StringBuilder()).append("circle_time/circlefront:t")
							.append(mCurrentTextureUnit).toString(),
					getDigitalClock());
		if (flag2 && flag)
			Utility.updateTexture(null,
					(new StringBuilder()).append("circle_time/circleback:t")
							.append(mCurrentTextureUnit).toString(),
					getAnalogClock());
		if (flag3)
			Utility.updatAnimation(null,
					getAnalogClockHandAnimation(mContext, 500L));
	}

	public void updateCircle(Context paramContext, Intent paramIntent) {
		retrieveAlarmInfo(paramIntent);
		updateCircle();
	}

	public void updateValues(Context paramContext, Intent paramIntent) {
		retrieveAlarmInfo(paramIntent);
		this.mRefreshNeeded = true;
	}
}

/*
 * Location: J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.widget.circlewidget3d.CircleClock JD-Core Version: 0.6.2
 */