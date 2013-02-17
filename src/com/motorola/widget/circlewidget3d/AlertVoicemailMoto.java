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

public class AlertVoicemailMoto extends BaseAlert
{
  private static final int INVALID_COUNT = 255;
  private static AlertVoicemailMoto mInstance;
  private static Method sGetVoicemailCountMethod;
  private static Method sGetVoicemailNumberMethod;
  private static Constructor sMotoTelephonyContstructor;
  private static Object sMotoTelephonyInstance;
  String mDialString;
  String mNewVoicemailString;
  String mUnknownString;
  String mVoicemailNumber;

  private AlertVoicemailMoto(Context paramContext)
  {
    this.mContext = paramContext;
    retrieveStrings(paramContext);
    initMotoTelephony();
  }

  private static void clearVoicemailAlert()
  {
    if ((CircleAlert.isAlertDisplayed()) && (CircleAlert.getAlertType() == 2))
      CircleAlert.getInstance(mInstance.mContext).clearAlert();
  }

  public static AlertVoicemailMoto getInstance(Context paramContext)
  {
    synchronized (mSyncObject)
    {
      if (mInstance == null)
        mInstance = new AlertVoicemailMoto(paramContext);
      return mInstance;
    }
  }

  private static Object getMotoTelephonyInstance(Context paramContext)
  {
    if (sMotoTelephonyInstance == null);
    try
    {
      sMotoTelephonyInstance = sMotoTelephonyContstructor.newInstance(new Object[] { paramContext });
      return sMotoTelephonyInstance;
    }
    catch (Exception localException)
    {
      while (true)
        Log.e("Circle", "Error while creating Moto Telephony Instance");
    }
  }

  private static int getVoicemailCount(Context paramContext)
  {
    Object localObject = getMotoTelephonyInstance(paramContext);
    int i = 0;
    if (localObject != null);
    try
    {
      int j = ((Integer)sGetVoicemailCountMethod.invoke(localObject, new Object[0])).intValue();
      i = j;
      return i;
    }
    catch (Exception localException)
    {
      Log.e("Circle", "Error while getting voicemail count: " + localException);
    }
    return 0;
  }

  private static String getVoicemailNumber(Context paramContext)
  {
    Object localObject = getMotoTelephonyInstance(paramContext);
    String str = null;
    if (localObject != null);
    try
    {
      str = (String)sGetVoicemailNumberMethod.invoke(localObject, new Object[0]);
      return str;
    }
    catch (Exception localException)
    {
      Log.e("Circle", "Error while getting voicemail count: " + localException);
    }
    return null;
  }

  private void initMotoTelephony()
  {
    try
    {
      Class localClass = Class.forName("com.motorola.android.telephony.MotoTelephonyManager");
      sMotoTelephonyContstructor = localClass.getConstructor(new Class[] { Context.class });
      sGetVoicemailCountMethod = localClass.getMethod("getVoiceMessageCount", new Class[0]);
      sGetVoicemailNumberMethod = localClass.getMethod("getVoiceMailNumber", new Class[0]);
      return;
    }
    catch (Exception localException)
    {
      Log.e("Circle", "No Moto Telephony on device");
    }
  }

  BaseAlert.AlertInfo addItem(Bundle paramBundle)
  {
    if (!CircleAlert.isVoicemailEnabled())
    {
      Log.w("Circle", "Voicemail option is disabled so don't add alert");
      return null;
    }
    BaseAlert.AlertInfo localAlertInfo = new BaseAlert.AlertInfo();
    localAlertInfo.number = null;
    int i = getVoicemailCount(this.mContext);
    this.mVoicemailNumber = getVoicemailNumber(this.mContext);
    if (i == 255)
    {
      localAlertInfo.name = this.mNewVoicemailString;
      if (this.mVoicemailNumber == null)
        break label209;
    }
    label209: for (String str = this.mDialString + " " + this.mVoicemailNumber; ; str = this.mUnknownString)
    {
      localAlertInfo.description = str;
      localAlertInfo.timestamp = Long.toString(new Date().getTime());
      localAlertInfo.type = Integer.valueOf(2);
      localAlertInfo.imageId = Integer.valueOf(2130837543);
      if (Utility.isVerizonCarrier(mInstance.mContext))
        localAlertInfo.imageId = Integer.valueOf(2130837544);
      if (i != 0)
        break label218;
      clearVoicemailAlert();
      return null;
      localAlertInfo.name = (this.mNewVoicemailString + " (" + i + ")");
      break;
    }
    label218: CircleAlert.addItem(localAlertInfo);
    return null;
  }

  public Intent getAlertAppIntent()
  {
    Intent localIntent = new Intent("android.intent.action.CALL_PRIVILEGED", Uri.parse("voicemail:" + this.mVoicemailNumber));
    localIntent.setFlags(337641472);
    return localIntent;
  }

  public void retrieveStrings(Context paramContext)
  {
    Resources localResources = paramContext.getResources();
    this.mNewVoicemailString = localResources.getString(2131230751);
    this.mDialString = localResources.getString(2131230753);
    this.mUnknownString = localResources.getString(2131230733);
  }
}

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.AlertVoicemailMoto
 * JD-Core Version:    0.6.2
 */