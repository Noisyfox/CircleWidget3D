package com.motorola.widget.circlewidget3d;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class AlertVoicemail extends BaseAlert {
	public static final String[] PROJECTION_VOICEMAIL = { "_id", "number",
			"source_package", "is_read", "duration" };
	private static AlertVoicemail mInstance;
	public static ContentObserver mVoicemailObserver = new ContentObserver(
			new Handler()) {
		public void onChange(boolean paramAnonymousBoolean) {
			super.onChange(paramAnonymousBoolean);
			AlertVoicemail.lookVoicemailDB();
		}
	};

	private AlertVoicemail(Context paramContext) {
		mContext = paramContext;
	}

	public static AlertVoicemail getInstance(Context paramContext) {
		synchronized (mSyncObject) {
			if (mInstance == null) {
				mInstance = new AlertVoicemail(paramContext);
			}
			return mInstance;
		}
	}

	private static void lookVoicemailDB() {
		if (!CircleAlert.isVoicemailEnabled()) {
			return;
		}
		if (mInstance != null) {
			Cursor localCursor = mInstance.mContext.getContentResolver().query(
					android.provider.VoicemailContract.Voicemails.CONTENT_URI,
					PROJECTION_VOICEMAIL, null, null, "date DESC");
			if (localCursor != null) {
				if (localCursor.moveToFirst()) {
					String str1 = localCursor.getString(localCursor
							.getColumnIndex("number"));
					String str2 = localCursor.getString(localCursor
							.getColumnIndex("duration"));
					BaseAlert.AlertInfo localAlertInfo = new BaseAlert.AlertInfo();
					localAlertInfo.number = str1;
					localAlertInfo.name = null;
					localAlertInfo.description = str2;
					localAlertInfo.imageId = Integer.valueOf(R.drawable.ic_circle_widget_alert_voicemail);
					if (Utility.isVerizonCarrier(mInstance.mContext)) {
						localAlertInfo.imageId = Integer.valueOf(R.drawable.ic_circle_widget_alert_voicemail_vzw);
					}
					localAlertInfo.type = Integer.valueOf(2);
					localAlertInfo.timestamp = null;
					CircleAlert.addItem(localAlertInfo);
				} else {
					Log.e("Circle",
							"cursor can't move to first lookVoicemailDB");
				}
				localCursor.close();
				return;
			} else {
				Log.e("Circle", "cursor is null lookVoicemailDB");
				return;
			}
		} else {
			Log.e("Circle", "mInstance is null lookVoicemailDB");
			return;
		}
	}

	public static void registerVoicemailChange(Context paramContext) {
		paramContext.getContentResolver().registerContentObserver(
				android.provider.VoicemailContract.Voicemails.CONTENT_URI,
				false, mVoicemailObserver);
	}

	public static void unregisterVoicemailChange(Context paramContext) {
		paramContext.getContentResolver().unregisterContentObserver(
				mVoicemailObserver);
	}

	BaseAlert.AlertInfo addItem(Bundle paramBundle) {
		return null;
	}

	public Intent getAlertAppIntent() {
		return null;
	}
}

/*
 * Location: J:\鎶�湳鏂囨。\瀹夊崜鍥轰欢鐩稿叧\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.widget.circlewidget3d.AlertVoicemail JD-Core Version: 0.6.2
 */