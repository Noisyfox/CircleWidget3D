package com.motorola.widget.circlewidget3d;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;

public class AlertMissedCall
  extends BaseAlert
{
  public static final String CALL_LOG_COMPONENT = "com.android.contacts.activities.DialtactsActivity";
  public static final String CALL_LOG_PACKAGE = "com.android.contacts";
  public static final String CALL_TYPE_INTENT = "vnd.android.cursor.dir/calls";
  public static final String PAYPHONE_NUMBER = "-3";
  public static final String PRIVATE_NUMBER = "-2";
  public static final String[] PROJECTION_CALL_LOG = { "_id", "number", "type", "name", "date", "numbertype", "is_read" };
  public static final String UNKNOWN_NUMBER = "-1";
  public static ContentObserver mCallLogObserver = new ContentObserver(new Handler())
  {
    public void onChange(boolean paramAnonymousBoolean)
    {
      super.onChange(paramAnonymousBoolean);
      AlertMissedCall.access$000();
    } 
  };
  private static AlertMissedCall mInstance;
  
  private AlertMissedCall(Context paramContext)
  {
    mContext = paramContext;
  } 
  
  private static void clearMissedCallAlert()
  {
    if ((CircleAlert.isAlertDisplayed()) && (CircleAlert.getAlertType() == 1)) {
      CircleAlert.getInstance(mContext).clearAlert();
    } 
  } 
  
  public static AlertMissedCall getInstance(Context paramContext)
  {
    synchronized (mSyncObject)
    {
      if (mInstance == null) {
        mInstance = new AlertMissedCall(paramContext);
      } 
      return mInstance;
    } 
  } 
  
  public static String getPrivateNumberString(String paramString)
  {
    Resources localResources = mContext.getResources();
    if (paramString.equals("-1")) {
      return localResources.getString(2131230733);
    } 
    if (paramString.equals("-3")) {
      return localResources.getString(2131230755);
    } 
    return localResources.getString(2131230754);
  } 
  
  public static boolean isDialable(String paramString)
  {
    char[] arrayOfChar = paramString.toCharArray();
    int i = arrayOfChar.length;
    for (int j = 0; j < i; j++) {
      if (!PhoneNumberUtils.isDialable(arrayOfChar[j])) {
        return false;
      } 
    } 
    return true;
  } 
  
  private static void lookCallLogDB()
  {
    if (!CircleAlert.isMissedCallEnabled()) {
      return;
    } 
    if (mInstance != null)
    {
      Cursor localCursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, PROJECTION_CALL_LOG, null, null, "date DESC");
      if (localCursor != null)
      {
        if (localCursor.moveToFirst()) {
          if (localCursor.getInt(localCursor.getColumnIndex("type")) == 3)
          {
            String str1 = localCursor.getString(localCursor.getColumnIndex("number"));
            String str2 = localCursor.getString(localCursor.getColumnIndex("name"));
            String str3 = localCursor.getString(localCursor.getColumnIndex("date"));
            int i = localCursor.getInt(localCursor.getColumnIndex("numbertype"));
            int j = localCursor.getInt(localCursor.getColumnIndex("is_read"));
            boolean bool = TextUtils.isEmpty(str2);
            CharSequence localCharSequence = null;
            if (!bool) {
              localCharSequence = ContactsContract.CommonDataKinds.Phone.getTypeLabel(mContext.getResources(), i, null);
            } 
            if (j == 1)
            {
              clearMissedCallAlert();
              localCursor.close();
              return;
            } 
            if (!isDialable(str1)) {
              str2 = getPrivateNumberString(str1);
            } 
            BaseAlert.AlertInfo localAlertInfo = new BaseAlert.AlertInfo();
            number = str1;
            if (TextUtils.isEmpty(str2)) {
              str2 = null;
            } 
            name = str2;
            String str4 = null;
            if (localCharSequence != null) {
              str4 = localCharSequence.toString();
            } 
            description = str4;
            imageId = Integer.valueOf(2130837540);
            type = Integer.valueOf(1);
            timestamp = str3;
            CircleAlert.addItem(localAlertInfo);
          } 
        } 
        for (;;)
        {
          localCursor.close();
          return;
          Log.e("Circle", "Cursor can't find first element");
          clearMissedCallAlert();
        } 
      } 
      Log.e("Circle", "Cursor is null in lookCallLogDB");
      return;
    } 
    Log.e("Circle", "mInstance is NULL in lookcallLogDB");
  } 
  
  public static void registerForCallLogChange(Context paramContext)
  {
    paramContext.getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI, false, mCallLogObserver);
  } 
  
  public static void unregisterCallLogChange(Context paramContext)
  {
    paramContext.getContentResolver().unregisterContentObserver(mCallLogObserver);
  } 
  
  BaseAlert.AlertInfo addItem(Bundle paramBundle)
  {
    return null;
  } 
  
  public Intent getAlertAppIntent()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setType("vnd.android.cursor.dir/calls");
    localIntent.setComponent(new ComponentName("com.android.contacts", "com.android.contacts.activities.DialtactsActivity"));
    localIntent.setFlags(337641472);
    return localIntent;
  } 
} 

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.AlertMissedCall
 * JD-Core Version:    0.6.2
 */