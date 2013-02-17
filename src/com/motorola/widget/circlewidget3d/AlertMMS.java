package com.motorola.widget.circlewidget3d;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.util.Log;
import java.util.Date;

public class AlertMMS extends BaseAlert
{
  public static final String EXTRA_URI_ID = "URI";
  public static final int INVALID_ID = -1;
  private static final Uri MMS_URI = Uri.parse("content://mms/inbox");
  public static final String[] PROJECTIONS_MMS = { "_id", "thread_id", "date", "read" };
  private static AlertMMS mInstance;
  long mId = -1L;
  private String mMMSString;

  private AlertMMS(Context paramContext)
  {
    this.mContext = paramContext;
    retrieveStrings(paramContext);
  }

  private String getAddressNumber(ContentResolver paramContentResolver, long paramLong)
  {
    String[] arrayOfString = { "address" };
    Uri.Builder localBuilder = new Uri.Builder();
    localBuilder.scheme("content");
    localBuilder.authority("mms");
    localBuilder.appendPath(String.valueOf(paramLong));
    localBuilder.appendPath("addr");
    Uri localUri = localBuilder.build();
    String str = null;
    try
    {
      Cursor localCursor = paramContentResolver.query(localUri, arrayOfString, "msg_id=" + paramLong, null, null);
      str = null;
      if (localCursor != null)
      {
        boolean bool = localCursor.moveToFirst();
        str = null;
        if (bool)
          str = localCursor.getString(localCursor.getColumnIndex("address"));
        localCursor.close();
      }
      return str;
    }
    catch (Exception localException)
    {
      Log.d("Circle", "Couldn't find address for msg_id " + paramLong);
    }
    return str;
  }

  public static AlertMMS getInstance(Context paramContext)
  {
    synchronized (mSyncObject)
    {
      if (mInstance == null)
        mInstance = new AlertMMS(paramContext);
      return mInstance;
    }
  }

  BaseAlert.AlertInfo addItem(Bundle paramBundle)
  {
    if (!CircleAlert.isTextMsgEnabled())
      return null;
    this.mId = -1L;
    String str1 = null;
    ContentResolver localContentResolver;
    if (paramBundle != null)
      localContentResolver = mInstance.mContext.getContentResolver();
    try
    {
      Cursor localCursor = localContentResolver.query(MMS_URI, PROJECTIONS_MMS, "read=0", null, "date DESC");
      str1 = null;
      if (localCursor != null)
      {
        boolean bool = localCursor.moveToFirst();
        str1 = null;
        if (bool)
        {
          Long localLong = Long.valueOf(localCursor.getLong(localCursor.getColumnIndex("thread_id")));
          str1 = getAddressNumber(localContentResolver, Long.valueOf(localCursor.getLong(localCursor.getColumnIndex("_id"))).longValue());
          this.mId = localLong.longValue();
        }
        localCursor.close();
      }
      BaseAlert.AlertInfo localAlertInfo = new BaseAlert.AlertInfo();
      localAlertInfo.description = this.mMMSString;
      if (str1 != null)
      {
        str2 = str1;
        localAlertInfo.number = str2;
        localAlertInfo.name = AlertMessages.getInstance(this.mContext).getContactName(str1);
        localAlertInfo.timestamp = Long.toString(new Date().getTime());
        localAlertInfo.type = Integer.valueOf(4);
        localAlertInfo.imageId = Integer.valueOf(2130837541);
        if (Utility.isVerizonCarrier(mInstance.mContext))
          localAlertInfo.imageId = Integer.valueOf(2130837542);
        CircleAlert.addItem(localAlertInfo);
        return localAlertInfo;
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        Log.d("Circle", "Couldn't find MMS id");
        continue;
        String str2 = "    ";
      }
    }
  }

  public Intent getAlertAppIntent()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    if (this.mId != -1L)
    {
      localIntent.setClassName("com.motorola.messaging", "com.motorola.messaging.activity.ComposeMessageActivity");
      localIntent.setType("vnd.android-dir/mms-sms");
      localIntent.putExtra("thread_id", this.mId);
      localIntent.addCategory("android.intent.category.DEFAULT");
    }
    while (true)
    {
      localIntent.setFlags(337641472);
      return localIntent;
      localIntent.setComponent(new ComponentName("com.motorola.messaging", "com.motorola.messaging.activity.MessagingActivity"));
    }
  }

  public void retrieveStrings(Context paramContext)
  {
    this.mMMSString = paramContext.getResources().getString(2131230752);
  }
}

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.AlertMMS
 * JD-Core Version:    0.6.2
 */