package com.motorola.widget.circlewidget3d;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;

public class AlertVoicemailMoto extends BaseAlert {
	private static AlertVoicemailMoto mInstance;
	private static Method sGetVoicemailCountMethod;
	private static Method sGetVoicemailNumberMethod;
	private static Constructor<?> sMotoTelephonyContstructor;
	private static Object sMotoTelephonyInstance;
	String mDialString;
	String mNewVoicemailString;
	String mUnknownString;
	String mVoicemailNumber;

	private AlertVoicemailMoto(Context paramContext) {
		this.mContext = paramContext;
		retrieveStrings(paramContext);
		initMotoTelephony();
	}

	private static void clearVoicemailAlert() {
		if ((CircleAlert.isAlertDisplayed())
				&& (CircleAlert.getAlertType() == 2))
			CircleAlert.getInstance(mInstance.mContext).clearAlert();
	}

	public static AlertVoicemailMoto getInstance(Context paramContext) {
		synchronized (mSyncObject) {
			if (mInstance == null)
				mInstance = new AlertVoicemailMoto(paramContext);
			return mInstance;
		}
	}

	private static Object getMotoTelephonyInstance(Context paramContext) {
		if (sMotoTelephonyInstance == null)
			;
		try {
			sMotoTelephonyInstance = sMotoTelephonyContstructor
					.newInstance(new Object[] { paramContext });
			return sMotoTelephonyInstance;
		} catch (Exception localException) {
			while (true)
				Log.e("Circle", "Error while creating Moto Telephony Instance");
		}
	}

	private static int getVoicemailCount(Context paramContext) {
		Object localObject = getMotoTelephonyInstance(paramContext);
		int i = 0;
		if (localObject != null)
			;
		try {
			int j = ((Integer) sGetVoicemailCountMethod.invoke(localObject,
					new Object[0])).intValue();
			i = j;
			return i;
		} catch (Exception localException) {
			Log.e("Circle", "Error while getting voicemail count: "
					+ localException);
		}
		return 0;
	}

	private static String getVoicemailNumber(Context paramContext) {
		Object localObject = getMotoTelephonyInstance(paramContext);
		String str = null;
		if (localObject != null)
			;
		try {
			str = (String) sGetVoicemailNumberMethod.invoke(localObject,
					new Object[0]);
			return str;
		} catch (Exception localException) {
			Log.e("Circle", "Error while getting voicemail count: "
					+ localException);
		}
		return null;
	}

	private void initMotoTelephony() {
		try {
			Class<?> localClass = Class
					.forName("com.motorola.android.telephony.MotoTelephonyManager");
			sMotoTelephonyContstructor = localClass
					.getConstructor(new Class[] { Context.class });
			sGetVoicemailCountMethod = localClass.getMethod(
					"getVoiceMessageCount", new Class[0]);
			sGetVoicemailNumberMethod = localClass.getMethod(
					"getVoiceMailNumber", new Class[0]);
			return;
		} catch (Exception localException) {
			Log.e("Circle", "No Moto Telephony on device");
		}
	}

	BaseAlert.AlertInfo addItem(Bundle bundle) {
		if (!CircleAlert.isVoicemailEnabled()) {
			Log.w("Circle", "Voicemail option is disabled so don't add alert");
			return null;
		}
		BaseAlert.AlertInfo alertinfo = new BaseAlert.AlertInfo();
		alertinfo.number = null;
		int i = getVoicemailCount(mContext);
		mVoicemailNumber = getVoicemailNumber(mContext);
		String s;
		if (i == 255)
			alertinfo.name = mNewVoicemailString;
		else
			alertinfo.name = (new StringBuilder()).append(mNewVoicemailString)
					.append(" (").append(i).append(")").toString();
		if (mVoicemailNumber != null)
			s = (new StringBuilder()).append(mDialString).append(" ")
					.append(mVoicemailNumber).toString();
		else
			s = mUnknownString;
		alertinfo.description = s;
		alertinfo.timestamp = Long.toString((new Date()).getTime());
		alertinfo.type = Integer.valueOf(2);
		alertinfo.imageId = Integer.valueOf(R.drawable.ic_circle_widget_alert_voicemail);
		if (Utility.isVerizonCarrier(mInstance.mContext))
			alertinfo.imageId = Integer.valueOf(R.drawable.ic_circle_widget_alert_voicemail_vzw);
		if (i == 0) {
			clearVoicemailAlert();
			return null;
		} else {
			CircleAlert.addItem(alertinfo);
			return null;
		}
	}

	public Intent getAlertAppIntent() {
		Intent localIntent = new Intent(
				"android.intent.action.CALL_PRIVILEGED", Uri.parse("voicemail:"
						+ this.mVoicemailNumber));
		localIntent.setFlags(337641472);
		return localIntent;
	}

	public void retrieveStrings(Context paramContext) {
		Resources localResources = paramContext.getResources();
		this.mNewVoicemailString = localResources.getString(R.string.new_voicemail);
		this.mDialString = localResources.getString(R.string.dial);
		this.mUnknownString = localResources.getString(R.string.unknown);
	}
}

/*
 * Location: J:\鎶�湳鏂囨。\瀹夊崜鍥轰欢鐩稿叧\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.widget.circlewidget3d.AlertVoicemailMoto JD-Core Version: 0.6.2
 */