package com.motorola.widget.circlewidget3d;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.util.Calendar;
import java.util.Iterator;

public class CircleCheckin {
	private static final String ASSIGNMENT = "=";
	private static final String CLICKED = "times_clicked";
	private static final String COUNT = "count";
	private static final String CS_PREFERENCE_NAME = "cs_pref_name";
	private static final String DELIMITER = ";";
	private static final String END_SEGMENT = "]";
	private static final String EVENT_ACC_CLICK_CIRCLE = "accClickCircle";
	private static final int EVENT_ACC_CLICK_CIRCLE_ID = 1;
	private static final String EVENT_ACC_CS_SELECT_PREF = "accCircleSettingsPref";
	private static final int EVENT_ACC_CS_SELECT_PREF_ID = 3;
	private static final String EVENT_ACC_FLIP_CIRCLE = "accFlipCircle";
	private static final int EVENT_ACC_FLIP_CIRCLE_ID = 2;
	public static final String EVENT_ME_CLICK_CIRCLE = "clickCircle";
	private static final int EVENT_ME_WILDCARD_ID = 0;
	private static final String FLIPPED = "times_flipped";
	private static final String ID = "ID";
	private static final String LAST_UPDATE = "last_update";
	private static final String MARKER = "$";
	private static final String NAME = "name";
	private static final String START_SEGMENT = "[";
	private static final String TAG = "CircleCheckin";
	private static final String TAG_L1 = "MOT_HOME_STATS_L1";
	private static final String TAG_L2 = "MOT_HOME_STATS_L2";
	private static final String TIME = "time";
	private static final String VER = "ver";
	private static final String VERSION = "0.1";
	private static CircleCheckin mInstance;
	private static Object mLock = new Object();
	private Context mContext;
	private CheckinHandler mHandler;
	private HandlerThread mHandlerThread;
	private int mLastInsertion;
	private SharedPreferences mSharedPrefs;

	private CircleCheckin(Context paramContext) {
		this.mContext = paramContext;
		this.mSharedPrefs = this.mContext.getSharedPreferences(
				CircleCheckin.class.getName(), 0);
	}

	private void accCircleSettingsPreference(Object obj) {
		String s = (String) obj;
		String s1 = Integer.toString(concat(
				new Object[] { "accCircleSettingsPref", s }).hashCode());
		String s2 = mSharedPrefs.getString(s1, null);
		Event event;
		String s3;
		android.content.SharedPreferences.Editor editor;
		if (s2 == null) {
			event = new Event(3);
			event.add("cs_pref_name", s);
			event.add("count", Integer.valueOf(0));
		} else {
			event = new Event();
			event.setEventFromValue(s2);
		}
		event.increase("count");
		s3 = event.getValue();
		editor = mSharedPrefs.edit();
		editor.putString(s1, s3);
		editor.apply();
	}

	private void accClickCircle(Object obj) {
		String s = (String) obj;
		String s1 = Integer.toString(concat(
				new Object[] { "accClickCircle", s }).hashCode());
		String s2 = mSharedPrefs.getString(s1, null);
		Event event;
		String s3;
		android.content.SharedPreferences.Editor editor;
		if (s2 == null) {
			event = new Event(1);
			event.add("name", s);
			event.add("times_clicked", Integer.valueOf(0));
		} else {
			event = new Event();
			event.setEventFromValue(s2);
		}
		event.increase("times_clicked");
		s3 = event.getValue();
		editor = mSharedPrefs.edit();
		editor.putString(s1, s3);
		editor.apply();
	}

	private void accFlipCircle(Object obj) {
		String s = (String) obj;
		String s1 = Integer
				.toString(concat(new Object[] { "accFlipCircle", s })
						.hashCode());
		String s2 = mSharedPrefs.getString(s1, null);
		Event event;
		String s3;
		android.content.SharedPreferences.Editor editor;
		if (s2 == null) {
			event = new Event(2);
			event.add("name", s);
			event.add("times_flipped", Integer.valueOf(0));
		} else {
			event = new Event();
			event.setEventFromValue(s2);
		}
		event.increase("times_flipped");
		s3 = event.getValue();
		editor = mSharedPrefs.edit();
		editor.putString(s1, s3);
		editor.apply();
	}

	private void checkUpdateCheckinDb() {
		int i = Calendar.getInstance().get(6);
		if (this.mLastInsertion == 0)
			this.mLastInsertion = this.mSharedPrefs.getInt("last_update", 0);
		if (i != this.mLastInsertion) {
			this.mLastInsertion = i;
			updateCheckinDb();
		}
	}

	private static String concat(Object[] paramArrayOfObject) {
		StringBuilder localStringBuilder = new StringBuilder();
		if ((paramArrayOfObject != null) && (localStringBuilder != null))
			for (int i = 0; i < paramArrayOfObject.length; i++)
				localStringBuilder.append(paramArrayOfObject[i]);
		return localStringBuilder.toString();
	}

	private static StringBuilder concat(StringBuilder paramStringBuilder,
			Object[] paramArrayOfObject) {
		if ((paramArrayOfObject != null) && (paramStringBuilder != null))
			for (int i = 0; i < paramArrayOfObject.length; i++)
				paramStringBuilder.append(paramArrayOfObject[i]);
		return paramStringBuilder;
	}

	private void handleLogEvent(int paramInt, Object paramObject) {
		switch (paramInt) {
		default:
			return;
		case 0:
			((Event) paramObject).logEvent(this.mContext);
			return;
		case 1:
			accClickCircle(paramObject);
			checkUpdateCheckinDb();
			return;
		case 2:
			accFlipCircle(paramObject);
			checkUpdateCheckinDb();
			return;
		case 3:
		}
		accCircleSettingsPreference(paramObject);
		checkUpdateCheckinDb();
	}

	private boolean logAccEvent(int paramInt, Object paramObject) {
		startHandler();
		return this.mHandler.sendMessage(this.mHandler.obtainMessage(paramInt,
				paramObject));
	}

	static void logAccEventCircleSettingsPreference(String paramString) {
		try {
			CircleCheckin localCircleCheckin = peekInstance();
			if (localCircleCheckin == null)
				return;
			localCircleCheckin.logAccEvent(3, paramString);
			return;
		} catch (Throwable localThrowable) {
			Log.e("CircleCheckin",
					"Fail to log event - CircleSettings Preference - Exception="
							+ localThrowable);
			localThrowable.printStackTrace();
		}
	}

	public static void logAccEventFlipCircle(String paramString) {
		try {
			CircleCheckin localCircleCheckin = peekInstance();
			if (localCircleCheckin == null)
				return;
			localCircleCheckin.logAccEvent(2, paramString);
			return;
		} catch (Throwable localThrowable) {
			Log.e("CircleCheckin",
					"Fail to log event - Flip Circle - Exception="
							+ localThrowable);
			localThrowable.printStackTrace();
		}
	}

	private boolean logMeanEvent(Event paramEvent) {
		startHandler();
		return this.mHandler.sendMessage(this.mHandler.obtainMessage(0,
				paramEvent));
	}

	static void logMeanEventClickCircle(String paramString) {
		try {
			CircleCheckin localCircleCheckin = peekInstance();
			if (localCircleCheckin == null)
				return;
			localCircleCheckin.getClass();
			Event localEvent = localCircleCheckin.new Event("clickCircle");
			localEvent.add("name", paramString);
			localCircleCheckin.logMeanEvent(localEvent);
			localCircleCheckin.logAccEvent(1, paramString);
			return;
		} catch (Throwable localThrowable) {
			Log.e("CircleCheckin",
					"Fail to log event - Click Circle Exception="
							+ localThrowable);
			localThrowable.printStackTrace();
		}
	}

	private static CircleCheckin peekInstance() {
		return mInstance;
	}

	public static void startChecking(final Context paramContext) {
		new Thread() {
			public void run() {
				synchronized (CircleCheckin.mLock) {
					if (CircleCheckin.mInstance == null)
						CircleCheckin.mInstance = new CircleCheckin(
								paramContext);
					return;
				}
			}
		}.start();
	}

	private void startHandler() {
		if (this.mHandler == null) {
			this.mHandlerThread = new HandlerThread(
					CircleCheckin.class.getName());
			this.mHandlerThread.start();
			this.mHandler = new CheckinHandler(this.mHandlerThread.getLooper());
		}
	}

	public static void stopCheckin() {
		CircleCheckin localCircleCheckin = peekInstance();
		if (localCircleCheckin != null)
			localCircleCheckin.stopHandler();
	}

	private void stopHandler() {
		if (this.mHandlerThread != null) {
			this.mHandlerThread.quit();
			this.mHandlerThread = null;
			this.mHandler = null;
		}
	}

	private void updateCheckinDb() {
		Iterator localIterator = this.mSharedPrefs.getAll().values().iterator();
		while (localIterator.hasNext())
			try {
				String str = (String) localIterator.next();
				if (str != null) {
					Event localEvent = new Event();
					localEvent.setEventFromValue(str);
					localEvent.setHeader();
					localEvent.logEvent(this.mContext);
				}
			} catch (Throwable localThrowable) {
			}
		SharedPreferences.Editor localEditor = this.mSharedPrefs.edit();
		localEditor.clear();
		localEditor.putInt("last_update", this.mLastInsertion);
		localEditor.apply();
	}

	class CheckinHandler extends Handler {
		private CheckinHandler(Looper arg2) {
			super();
		}

		public void handleMessage(Message paramMessage) {
			try {
				CircleCheckin.this.handleLogEvent(paramMessage.what,
						paramMessage.obj);
				return;
			} catch (Throwable localThrowable) {
				Log.e("CircleCheckin", "Fail handleMessage - Exception="
						+ localThrowable);
				localThrowable.printStackTrace();
			}
		}
	}

	private class Event {
		static final int FIRST_FIELD_AFTER_TIMESTAMP = 7;
		static final int SEGMENT_NAME_VALUE_POSITION = 2;
		static final String SPLIT_REGEX = "[\\[\\]=;]";
		static final int TIMESTAMP_VALUE_POSITION = 6;
		static final int VERSION_VALUE_POSITION = 4;
		int mId = 0;
		String mTag = "MOT_HOME_STATS_L2";
		StringBuilder mValue = new StringBuilder("[");

		Event() {
		}

		Event(int i) {
			this.mValue.append("$");
			this.mValue.append(i);
			this.mValue.append(";");
		}

		Event(String s) {
			Long localLong = Long.valueOf(System.currentTimeMillis());
			add("ID", s);
			add("ver", "0.1");
			add("time", localLong.toString());
		}

		void add(Object paramObject1, Object paramObject2) {
			CircleCheckin.concat(this.mValue, new Object[] { paramObject1, "=",
					paramObject2, ";" });
		}

		void dencrease(String paramString) {
			operation(paramString, false, 1);
		}

		String getTag() {
			return this.mTag;
		}

		String getValue() {
			if (!"]".equals(this.mValue.substring(-1 + this.mValue.length(),
					this.mValue.length())))
				this.mValue.append("]");
			return this.mValue.toString();
		}

		void increase(String paramString) {
			operation(paramString, true, 1);
		}

		void logEvent(Context paramContext) {
			String[] arrayOfString = getValue().split("[\\[\\]=;]");
			if (arrayOfString.length > 6) {
				String str1 = getTag();
				String str2 = arrayOfString[2];
				String str3 = arrayOfString[4];
				Long localLong = Long.valueOf(Long.parseLong(arrayOfString[6]));
				if (CheckinEventWrapper.isInitialized()) {
					CheckinEventWrapper localCheckinEventWrapper = new CheckinEventWrapper();
					if (localCheckinEventWrapper.setHeader(str1, str2, str3,
							localLong.longValue())) {
						for (int i = 7; i < arrayOfString.length; i += 2)
							localCheckinEventWrapper.setValue(arrayOfString[i],
									arrayOfString[(i + 1)]);
						localCheckinEventWrapper.publish(paramContext
								.getContentResolver());
					}
				}
			}
		}

		void operation(String paramString, boolean paramBoolean, int paramInt) {
			StringBuilder localStringBuilder = new StringBuilder();
			CircleCheckin.concat(localStringBuilder, new Object[] {
					paramString, "=" });
			int i = this.mValue.indexOf(localStringBuilder.toString())
					+ localStringBuilder.length();
			int j = this.mValue.indexOf(";", i);
			Integer localInteger1 = Integer.valueOf(Integer
					.parseInt(this.mValue.substring(i, j)));
			Integer localInteger2;

			if (paramBoolean) {
				localInteger2 = Integer.valueOf(paramInt
						+ localInteger1.intValue());
			} else {

				localInteger2 = Integer.valueOf(localInteger1.intValue()
						- paramInt);
				if (localInteger2.intValue() < 0) {
					localInteger2 = Integer.valueOf(0);
				}
			}

			this.mValue = this.mValue.replace(i, j, localInteger2.toString());
		}

		void setEventFromValue(String paramString) {
			this.mId = Integer.parseInt(paramString.substring(
					1 + paramString.indexOf("$"), paramString.indexOf(";")));
			this.mValue = new StringBuilder(paramString);
		}

		boolean setHeader() {
			String str1;
			switch (this.mId) {
			default:
				return false;
			case 1:
				str1 = "accClickCircle";
				break;
			case 2:
				str1 = "accFlipCircle";
				break;
			case 3:
				str1 = "accCircleSettingsPref";
				break;
			}
			Long localLong = Long.valueOf(System.currentTimeMillis());
			Object[] arrayOfObject = new Object[12];
			arrayOfObject[0] = "ID";
			arrayOfObject[1] = "=";
			arrayOfObject[2] = str1;
			arrayOfObject[3] = ";";
			arrayOfObject[4] = "ver";
			arrayOfObject[5] = "=";
			arrayOfObject[6] = "0.1";
			arrayOfObject[7] = ";";
			arrayOfObject[8] = "time";
			arrayOfObject[9] = "=";
			arrayOfObject[10] = localLong.toString();
			arrayOfObject[11] = ";";
			String str2 = CircleCheckin.concat(arrayOfObject);
			int i = this.mValue.indexOf("$");
			int j = 1 + this.mValue.indexOf(";");
			this.mValue.replace(i, j, str2);

			return true;
		}
	}
}

/*
 * Location: J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.widget.circlewidget3d.CircleCheckin JD-Core Version: 0.6.2
 */