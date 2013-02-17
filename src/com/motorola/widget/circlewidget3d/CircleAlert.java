package com.motorola.widget.circlewidget3d;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.motorola.homescreen.common.anim.AnimUtils;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CircleAlert extends Circle {
	public static final int ALERT_SIDE_BACK = 1;
	public static final int ALERT_SIDE_FRONT = 0;
	public static final int ALERT_SIDE_INVALID = -1;
	public static final String JSON_ALERT_KEY_DESCRIPTION = "DESCRIPTION";
	public static final String JSON_ALERT_KEY_ID = "ID";
	public static final String JSON_ALERT_KEY_IMAGE_ID = "IMAGE_ID";
	public static final String JSON_ALERT_KEY_NAME = "NAME";
	public static final String JSON_ALERT_KEY_NUMBER = "NUMBER";
	public static final String JSON_ALERT_KEY_TIMESTAMP = "TIMESTAMP";
	public static final String JSON_ALERT_KEY_TYPE = "TYPE";
	private static CircleAlert mInstance;
	static boolean sAlertDisplayed = false;
	private static int sAlertSide;
	private static int sAlertTextureUnit = 3;
	private static BaseAlert.AlertInfo sCurrentAlertInfo;
	static boolean sEnableMissedCall;
	static boolean sEnableTextMsg;
	static boolean sEnableVoicemail;
	private static boolean sHelpDisplayAgain;
	private static long sLastAlertTimeStamp = 0L;
	TextView mDescriptionView;
	TextView mNameNumberView;
	TextView mTimeStampView;
	ImageView mTypeImageView;

	private CircleAlert(Context paramContext) {
		this.mContext = paramContext;
		this.mCurrentId = 0;
		sAlertSide = -1;
		prepareCircle(R.layout.alert_circle, CircleConsts.CLOCK_BITMAP_SIZE.intValue());
		SharedPreferences localSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this.mContext);
		sEnableTextMsg = localSharedPreferences.getBoolean(
				"enable_text_notification", true);
		sEnableMissedCall = localSharedPreferences.getBoolean(
				"enable_missed_call_notification", true);
		sEnableVoicemail = localSharedPreferences.getBoolean(
				"enable_voicemail_notification", true);
	}

	static void addItem(BaseAlert.AlertInfo paramAlertInfo) {
		if (paramAlertInfo == null) {
			return;
		}

		if (sAlertDisplayed && sCurrentAlertInfo != null) {
			if (Long.parseLong(paramAlertInfo.timestamp) < Long
					.parseLong(sCurrentAlertInfo.timestamp)) {
				Log.w("Circle",
						"Not displaying new alert as it's timestamps is older then current alert");
				return;
			}
		}
		long l = Long.parseLong(paramAlertInfo.timestamp);

		if (sLastAlertTimeStamp == l) {
			Log.w("Circle",
					"Not displaying new alert as it was clear it before");
			return;
		}

		sLastAlertTimeStamp = l;
		sCurrentAlertInfo = paramAlertInfo;
		saveAlert(paramAlertInfo);

		if (mInstance != null) {
			sHelpDisplayAgain = CircleHelp.isHelpDisplayed();
			mInstance.updateCircle();
		} else {
			Log.e("Circle", "Why mInstance is null??");
		}

	}

	private void displayHelpScreenAgainIfNeeded() {
		if (sHelpDisplayAgain) {
			Intent localIntent = new Intent();
			localIntent
					.setAction("com.motorola.widget.circlewidget3d.ACTION_DISPLAY_HELP_CIRCLE");
			localIntent.putExtra("force", true);
			this.mContext.sendBroadcast(localIntent);
			sHelpDisplayAgain = false;
		}
	}

	private static void fadeTextureUnits(int i, long l) {
		JSONArray jsonarray = new JSONArray();
		float af[] = new float[3];
		af[0] = 0.0F;
		float f;
		if (i == 0)
			f = 0.0F;
		else
			f = 10F;
		af[1] = f;
		af[2] = 0.0F;
		jsonarray.put(AnimUtils.createJSONData3D("alert_alpha", "translation",
				null, af, l, false));
		Utility.updatAnimation(null, jsonarray);
		CircleClock.getInstance(mInstance.mContext).setCurrentTextureUnit(i);
	}

	public static int getAlertDisplayedSide() {
		return sAlertSide;
	}

	private Bitmap getAlertScreen(BaseAlert.AlertInfo alertinfo) {
		if (alertinfo != null) {
			String s;
			TextView textview;
			int i;
			int j;
			boolean flag;
			int k;
			if (alertinfo.number != null) {
				if (AlertMissedCall.isDialable(alertinfo.number)) {
					alertinfo.number = PhoneNumberUtils
							.formatNumber(alertinfo.number);
				} else {
					alertinfo.name = AlertMissedCall
							.getPrivateNumberString(alertinfo.number);
				}
			}

			if (alertinfo.name != null) {
				s = alertinfo.name;
			} else {
				s = alertinfo.number;
			}

			mNameNumberView.setText(s);
			if (alertinfo.description != null) {
				mDescriptionView.setText(alertinfo.description);
			}

			textview = mDescriptionView;
			if (alertinfo.description != null) {
				i = 0;
			} else {
				i = 4;
			}
			textview.setVisibility(i);
			if (alertinfo.type.intValue() == 2) {
				mDescriptionView.setLines(1);
			} else {
				mDescriptionView.setLines(2);
			}
			mTypeImageView.setImageResource(alertinfo.imageId.intValue());
			j = alertinfo.type.intValue();
			flag = false;
			if (j != 3) {
				int l = alertinfo.type.intValue();
				flag = false;
				if (l != 4) {
					String s1 = alertinfo.timestamp;
					flag = false;
					if (s1 != null) {
						Long long1 = Long.valueOf(Long
								.parseLong(alertinfo.timestamp));
						String s2;
						if (Utility.is24HourFormat(mContext)) {
							Calendar calendar = Calendar.getInstance();
							calendar.setTimeInMillis(long1.longValue());
							s2 = Utility.getTimeString(mContext, calendar);
						} else {
							s2 = DateFormat.getTimeInstance(3).format(
									new Date(long1.longValue()));
						}
						mTimeStampView.setText(s2);
						flag = true;
					}
				}
			}

			TextView textview1 = mTimeStampView;
			if (flag)
				k = 0;
			else
				k = 8;
			textview1.setVisibility(k);
			mBitmap.eraseColor(0);
			mLayout.draw(mCanvas);
		} else {
			Log.e("Circle", "Info null in getAlertScreen");
		}
		return mBitmap;
	}

	public static int getAlertType() {
		return sCurrentAlertInfo.type.intValue();
	}

	public static CircleAlert getInstance(Context paramContext) {
		synchronized (syncObject) {
			if (mInstance == null) {
				mInstance = new CircleAlert(paramContext);
			}
			return mInstance;
		}
	}

	private static Integer getIntegerSafely(JSONObject paramJSONObject,
			String paramString) {
		if (paramJSONObject != null && paramString != null) {
			try {
				if (!paramJSONObject.isNull(paramString)) {
					return Integer.valueOf(paramJSONObject.getInt(paramString));
				}
			} catch (JSONException localJSONException) {
			}
		}
		return null;
	}

	private static String getStringSafely(JSONObject paramJSONObject,
			String paramString) {
		if (paramJSONObject != null && paramString != null) {
			try {
				if (!paramJSONObject.isNull(paramString)) {
					return paramJSONObject.getString(paramString);
				}
			} catch (JSONException localJSONException) {
			}
		}
		return null;
	}

	public static boolean isAlertDisplayed() {
		return sAlertDisplayed;
	}

	static boolean isMissedCallEnabled() {
		return sEnableMissedCall;
	}

	static boolean isTextMsgEnabled() {
		return sEnableTextMsg;
	}

	static boolean isVoicemailEnabled() {
		return sEnableVoicemail;
	}

	private void launchAlertApp() {
		BaseAlert.AlertInfo localAlertInfo = sCurrentAlertInfo;
		Intent localIntent = null;
		if (localAlertInfo != null) {
			int i = sCurrentAlertInfo.type.intValue();
			localIntent = null;
			switch (i) {
			case 0:
				localIntent = AlertMissedCall.getInstance(this.mContext)
						.getAlertAppIntent();
				break;
			case 1:
				localIntent = AlertMessages.getInstance(this.mContext)
						.getAlertAppIntent();
				break;
			case 2:
				localIntent = AlertVoicemailMoto.getInstance(this.mContext)
						.getAlertAppIntent();
				break;
			case 3:
				localIntent = AlertMMS.getInstance(this.mContext)
						.getAlertAppIntent();
				break;
			}
			if (localIntent == null) {
				Log.e("Circle", "Start intent not available");
			} else {
				try {
					this.mContext.startActivity(localIntent);
				} catch (Exception localException) {
					Log.e("Circle", "Error while launching app: "
							+ localException);
				} finally {
					clearAlert();
				}
			}
		}

	}

	public static void loadLastAlert() {
		String str = PreferenceManager.getDefaultSharedPreferences(
				mInstance.mContext).getString("last_alert_values", null);
		if (str == null) {
			return;
		}
		BaseAlert.AlertInfo localAlertInfo = new BaseAlert.AlertInfo();
		try {
			JSONObject localJSONObject = new JSONObject(str);

			localAlertInfo.id = getIntegerSafely(localJSONObject, "ID");
			localAlertInfo.name = getStringSafely(localJSONObject, "NAME");
			localAlertInfo.number = getStringSafely(localJSONObject, "NUMBER");
			localAlertInfo.timestamp = getStringSafely(localJSONObject,
					"TIMESTAMP");
			localAlertInfo.description = getStringSafely(localJSONObject,
					"DESCRIPTION");
			localAlertInfo.type = getIntegerSafely(localJSONObject, "TYPE");
			localAlertInfo.imageId = getIntegerSafely(localJSONObject,
					"IMAGE_ID");
			if (localAlertInfo.timestamp != null) {
				sLastAlertTimeStamp = Long.parseLong(localAlertInfo.timestamp);
			}
			sCurrentAlertInfo = localAlertInfo;
			mInstance.updateCircle();
		} catch (JSONException localJSONException1) {
			localJSONException1.printStackTrace();
			Log.e("Circle", "Error while creating JSON from shared prefs.");
		}

	}

	public static void removeLastAlertKey(Context paramContext) {
		SharedPreferences localSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(paramContext);
		if (localSharedPreferences.getString("last_alert_values", null) != null) {
			localSharedPreferences.edit().remove("last_alert_values").apply();
		}
	}

	public static void saveAlert(BaseAlert.AlertInfo paramAlertInfo) {
		if (mInstance == null) {
			Log.e("Circle", "This method must be called after create a circle.");
			return;
		}
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("ID", paramAlertInfo.id)
					.put("NAME", paramAlertInfo.name)
					.put("NUMBER", paramAlertInfo.number)
					.put("TIMESTAMP", paramAlertInfo.timestamp)
					.put("DESCRIPTION", paramAlertInfo.description)
					.put("TYPE", paramAlertInfo.type)
					.put("IMAGE_ID", paramAlertInfo.imageId);
			String str = localJSONObject.toString();
			SharedPreferences.Editor localEditor = PreferenceManager
					.getDefaultSharedPreferences(mInstance.mContext).edit();
			localEditor.putString("last_alert_values", str);
			localEditor.apply();
			return;
		} catch (JSONException localJSONException) {
			for (;;) {
			}
		}
	}

	public static void setAlertState(boolean paramBoolean) {
		sAlertDisplayed = paramBoolean;
		sAlertTextureUnit = 3;
		fadeTextureUnits(0, 0L);
	}

	public static void setHelpDisplayAgain(boolean paramBoolean) {
		sHelpDisplayAgain = false;
	}

	public void clearAlert() {
		sCurrentAlertInfo = null;
		sAlertDisplayed = false;
		sAlertSide = -1;
		Utility.hideAlertScreen();
		Utility.showAnalogClock();
		displayHelpScreenAgainIfNeeded();
		removeLastAlertKey(this.mContext);
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(2000L);
				} catch (InterruptedException localInterruptedException) {
					localInterruptedException.printStackTrace();
				}
				CircleClock localCircleClock = CircleClock
						.getInstance(CircleAlert.this.mContext);
				boolean bool1 = localCircleClock.isFlipped();
				Utility.flipCircle(null, "circle_time", 300.0F, bool1);
				localCircleClock.setFlipped(!bool1);
				return;
			}
		}).start();
	}

	public void clearNotification(Context context, Intent intent) {
		if (isAlertDisplayed()) {
			long l = intent.getLongExtra("timestamp", -1L);
			long l1 = Long.parseLong(sCurrentAlertInfo.timestamp);
			long l2 = l / 0x186a0L;
			boolean flag = l1 / 0x186a0L == l2;
			if (flag) {
				clearAlert();
			}
		}
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

	public boolean handleFling(Messenger paramMessenger, Message paramMessage,
			Float paramFloat) {
		return false;
	}

	public boolean handleSingleTap(Bundle paramBundle) {
		paramBundle.getString("shape_name");
		launchAlertApp();
		return false;
	}

	public View prepareCircle(int paramInt1, int paramInt2) {
		View localView = super.prepareCircle(paramInt1, paramInt2);
		this.mTypeImageView = ((ImageView) localView.findViewById(R.id.alert_type_image));
		this.mNameNumberView = ((TextView) localView.findViewById(R.id.alert_namenumber));
		this.mDescriptionView = ((TextView) localView.findViewById(R.id.alert_description));
		this.mTimeStampView = ((TextView) localView.findViewById(R.id.alert_timestamp));
		return localView;
	}

	public void restoreAlertState() {
		updateCircle();
	}

	public void setTheme(String paramString) {
	}

	public void updateCircle() {
		if (sCurrentAlertInfo == null) {
			Log.e("Circle",
					"Not updating alert because Notification setting is OFF");
			Utility.showAnalogClock();
			return;
		}
		if (!CircleWidget3DProvider.isScreenOn()) {
			Log.e("Circle", "Screen is off so not displaying alert currently");
			mRefreshNeeded = true;
			return;
		}
		CircleClock circleclock = CircleClock.getInstance(mContext);
		boolean flag = circleclock.isFlipped();
		boolean flag1 = true;
		String s;
		int j;
		if (!sAlertDisplayed) {
			int k;
			if (flag)
				s = "circle_time/circlefront";
			else
				s = "circle_time/circleback";
			if (flag)
				k = 0;
			else
				k = 1;
			sAlertSide = k;
		} else if (sAlertSide != -1) {
			if (sAlertSide == 0)
				s = "circle_time/circlefront";
			else
				s = "circle_time/circleback";
			if (sAlertSide == 0 && !flag || sAlertSide == 1 && flag)
				flag1 = false;
		} else {
			int i;
			if (flag)
				s = "circle_time/circlefront";
			else
				s = "circle_time/circleback";
			if (flag)
				i = 0;
			else
				i = 1;
			sAlertSide = i;
		}
		if (flag1) {
			Utility.flipCircle(null, "circle_time", 300F, flag);
			boolean flag2;
			if (!flag)
				flag2 = true;
			else
				flag2 = false;
			circleclock.setFlipped(flag2);
		}
		if (sAlertSide == 1)
			Utility.hideAnalogClock();
		Utility.updateTexture(null,
				(new StringBuilder()).append(s).append(":t1").toString(),
				"alert_front_mask");
		Utility.updateTexture(null, (new StringBuilder()).append(s)
				.append(":t").append(sAlertTextureUnit).toString(),
				getAlertScreen(sCurrentAlertInfo));
		sAlertDisplayed = true;
		fadeTextureUnits(sAlertTextureUnit, 1000L);
		if (sAlertTextureUnit == 0)
			j = 3;
		else
			j = 0;
		sAlertTextureUnit = j;
		CircleHelp.setHelpDisplayed(false);
	}

	public void updateCircle(Context paramContext, Intent paramIntent) {
		updateCircle();
	}

	public void updateSettingValues(Intent paramIntent) {
		SharedPreferences localSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this.mContext);
		boolean bool1 = localSharedPreferences.getBoolean(
				"enable_text_notification", sEnableTextMsg);
		boolean bool2 = localSharedPreferences.getBoolean(
				"enable_missed_call_notification", sEnableMissedCall);
		boolean bool3 = localSharedPreferences.getBoolean(
				"enable_voicemail_notification", sEnableVoicemail);
		sEnableTextMsg = bool1;
		sEnableMissedCall = bool2;
		sEnableVoicemail = bool3;
	}

	public void updateValues(Context paramContext, Intent paramIntent) {
	}
}

/*
 * Location: J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.widget.circlewidget3d.CircleAlert JD-Core Version: 0.6.2
 */