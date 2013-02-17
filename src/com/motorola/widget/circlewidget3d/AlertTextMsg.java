package com.motorola.widget.circlewidget3d;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

public class AlertTextMsg extends BaseAlert
{
  public static final String ACTION_SMS_RECEIVE = "android.provider.Telephony.SMS_RECEIVED";
  private static final String EXTRA_SMS_PUD = "pdus";
  public static final String MESSAGING_COMPONENT = "com.motorola.messaging.activity.MessagingActivity";
  public static final String MESSAGING_PACKAGE = "com.motorola.messaging";
  private static final String[] PHONELOOKUP_PROJECTION = { "_id", "display_name", "number" };
  private static AlertTextMsg mInstance;

  private AlertTextMsg(Context paramContext)
  {
    this.mContext = paramContext;
  }

  public static AlertTextMsg getInstance(Context paramContext)
  {
    synchronized (mSyncObject)
    {
      if (mInstance == null)
        mInstance = new AlertTextMsg(paramContext);
      return mInstance;
    }
  }

  BaseAlert.AlertInfo addItem(Bundle paramBundle)
  {
    BaseAlert.AlertInfo localAlertInfo;
    if (!CircleAlert.isTextMsgEnabled())
      localAlertInfo = null;
    Object[] arrayOfObject;
    do
    {
      return localAlertInfo;
      localAlertInfo = new BaseAlert.AlertInfo();
      arrayOfObject = (Object[])paramBundle.get("pdus");
    }
    while (arrayOfObject == null);
    SmsMessage[] arrayOfSmsMessage = new SmsMessage[arrayOfObject.length];
    int i = 0;
    label45: if (i < arrayOfSmsMessage.length)
    {
      arrayOfSmsMessage[i] = SmsMessage.createFromPdu((byte[])(byte[])arrayOfObject[i]);
      if (arrayOfSmsMessage[i] == null)
        break label197;
      String str1 = arrayOfSmsMessage[i].getMessageBody().toString();
      String str2 = arrayOfSmsMessage[i].getOriginatingAddress();
      Long localLong = Long.valueOf(arrayOfSmsMessage[i].getTimestampMillis());
      localAlertInfo.description = str1;
      localAlertInfo.number = str2;
      localAlertInfo.name = getContactName(str2);
      localAlertInfo.timestamp = Long.toString(localLong.longValue());
      localAlertInfo.type = Integer.valueOf(3);
      localAlertInfo.imageId = Integer.valueOf(2130837541);
      if (Utility.isVerizonCarrier(mInstance.mContext))
        localAlertInfo.imageId = Integer.valueOf(2130837542);
      CircleAlert.addItem(localAlertInfo);
    }
    while (true)
    {
      i++;
      break label45;
      break;
      label197: Log.e("Circle", "Msg is NULL");
    }
  }

  public void clearTextMsgAlert(Context paramContext)
  {
    if (CircleAlert.isAlertDisplayed())
    {
      int i = CircleAlert.getAlertType();
      if ((i == 3) || (i == 4))
        CircleAlert.getInstance(mInstance.mContext).clearAlert();
    }
  }

  public Intent getAlertAppIntent()
  {
    Intent localIntent = new Intent();
    localIntent.setComponent(new ComponentName("com.motorola.messaging", "com.motorola.messaging.activity.MessagingActivity"));
    localIntent.setFlags(337641472);
    return localIntent;
  }

  public String getContactName(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      Log.i("Circle", "Get contact name returns becuase number is null");
    String str;
    while (true)
    {
      return null;
      str = null;
      try
      {
        Uri localUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(paramString));
        Cursor localCursor = this.mContext.getContentResolver().query(localUri, PHONELOOKUP_PROJECTION, null, null, null);
        str = null;
        if (localCursor != null)
        {
          boolean bool = localCursor.moveToFirst();
          str = null;
          if (bool)
            str = localCursor.getString(localCursor.getColumnIndex("display_name"));
          localCursor.close();
          return str;
        }
      }
      catch (Exception localException)
      {
        Log.e("Circle", "Exception " + localException + " number: " + paramString);
      }
    }
    return str;
  }
}

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.AlertTextMsg
 * JD-Core Version:    0.6.2
 */