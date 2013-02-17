// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.motorola.widget.circlewidget3d;

import android.content.*;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;

// Referenced classes of package com.motorola.widget.circlewidget3d:
//            BaseAlert, CircleAlert

public class AlertMissedCall extends BaseAlert {

	private AlertMissedCall(Context context) {
		mContext = context;
	}

	private static void clearMissedCallAlert() {
		if (CircleAlert.isAlertDisplayed() && CircleAlert.getAlertType() == 1)
			CircleAlert.getInstance(mInstance.mContext).clearAlert();
	}

	public static AlertMissedCall getInstance(Context context) {
		synchronized (mSyncObject) {
			if (mInstance == null)
				mInstance = new AlertMissedCall(context);
		}
		return mInstance;
	}

	public static String getPrivateNumberString(String s) {
		Resources resources = mInstance.mContext.getResources();
		if (s.equals("-1"))
			return resources.getString(R.string.unknown);
		if (s.equals("-3"))
			return resources.getString(R.string.pay_phone);
		else
			return resources.getString(R.string.private_number);
	}

	public static boolean isDialable(String s) {
		char ac[] = s.toCharArray();
		int i = ac.length;
		for (int j = 0; j < i; j++)
			if (!PhoneNumberUtils.isDialable(ac[j]))
				return false;

		return true;
	}

	private static void lookCallLogDB() {
		if (!CircleAlert.isMissedCallEnabled())
			return;
		if (mInstance != null) {
			Cursor cursor = mInstance.mContext.getContentResolver().query(
					android.provider.CallLog.Calls.CONTENT_URI,
					PROJECTION_CALL_LOG, null, null, "date DESC");
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					if (cursor.getInt(cursor.getColumnIndex("type")) == 3) {
						String s = cursor.getString(cursor
								.getColumnIndex("number"));
						String s1 = cursor.getString(cursor
								.getColumnIndex("name"));
						String s2 = cursor.getString(cursor
								.getColumnIndex("date"));
						int i = cursor.getInt(cursor
								.getColumnIndex("numbertype"));
						int j = cursor.getInt(cursor.getColumnIndex("is_read"));
						boolean flag = TextUtils.isEmpty(s1);
						CharSequence charsequence = null;
						if (!flag)
							charsequence = android.provider.ContactsContract.CommonDataKinds.Phone
									.getTypeLabel(
											mInstance.mContext.getResources(),
											i, null);
						if (j == 1) {
							clearMissedCallAlert();
							cursor.close();
							return;
						}
						if (!isDialable(s))
							s1 = getPrivateNumberString(s);
						BaseAlert.AlertInfo alertinfo = new BaseAlert.AlertInfo();
						alertinfo.number = s;
						if (TextUtils.isEmpty(s1))
							s1 = null;
						alertinfo.name = s1;
						String s3 = null;
						if (charsequence != null)
							s3 = charsequence.toString();
						alertinfo.description = s3;
						alertinfo.imageId = Integer.valueOf(R.drawable.ic_circle_widget_alert_missed);
						alertinfo.type = Integer.valueOf(1);
						alertinfo.timestamp = s2;
						CircleAlert.addItem(alertinfo);
					}
				} else {
					Log.e("Circle", "Cursor can't find first element");
					clearMissedCallAlert();
				}
				cursor.close();
				return;
			} else {
				Log.e("Circle", "Cursor is null in lookCallLogDB");
				return;
			}
		} else {
			Log.e("Circle", "mInstance is NULL in lookcallLogDB");
			return;
		}
	}

	public static void registerForCallLogChange(Context context) {
		context.getContentResolver().registerContentObserver(
				android.provider.CallLog.Calls.CONTENT_URI, false,
				mCallLogObserver);
	}

	public static void unregisterCallLogChange(Context context) {
		context.getContentResolver()
				.unregisterContentObserver(mCallLogObserver);
	}

	BaseAlert.AlertInfo addItem(Bundle bundle) {
		return null;
	}

	public Intent getAlertAppIntent() {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setType("vnd.android.cursor.dir/calls");
		intent.setComponent(new ComponentName("com.android.contacts",
				"com.android.contacts.activities.DialtactsActivity"));
		intent.setFlags(0x14200000);
		return intent;
	}

	public static final String CALL_LOG_COMPONENT = "com.android.contacts.activities.DialtactsActivity";
	public static final String CALL_LOG_PACKAGE = "com.android.contacts";
	public static final String CALL_TYPE_INTENT = "vnd.android.cursor.dir/calls";
	public static final String PAYPHONE_NUMBER = "-3";
	public static final String PRIVATE_NUMBER = "-2";
	public static final String PROJECTION_CALL_LOG[] = { "_id", "number",
			"type", "name", "date", "numbertype", "is_read" };
	public static final String UNKNOWN_NUMBER = "-1";
	public static ContentObserver mCallLogObserver = new ContentObserver(
			new Handler()) {

		public void onChange(boolean flag) {
			super.onChange(flag);
			AlertMissedCall.lookCallLogDB();
		}

	};
	private static AlertMissedCall mInstance;

}
