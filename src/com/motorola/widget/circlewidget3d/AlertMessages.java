package com.motorola.widget.circlewidget3d;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract.PhoneLookup;
import android.text.TextUtils;
import android.util.Log;

public class AlertMessages
  extends BaseAlert
{
  public static final String ADDRESS = "address";
  public static final String APP_LAUNCHER_ACTIVITY_NAME = "com.motorola.messaging.activity.MessagingActivity";
  public static final String APP_PACKAGE_NAME = "com.motorola.messaging";
  public static final String BODY = "body";
  private static final Uri COMPLETE_URI;
  public static final String DATE = "date";
  private static final Uri MESSAGE_URI = Uri.parse("content://mms-sms/");
  public static final String MMS_TYPE = "mms";
  public static final String OPEN_THREAD_ACTIVITY_NAME = "com.motorola.messaging.activity.ComposeMessageActivity";
  private static final String[] PHONELOOKUP_PROJECTION = { "_id", "display_name", "number" };
  public static final String[] PROJECTIONS_MESSAGES;
  public static final String READ = "read";
  public static final String RESOLVER_CLASS = "com.android.internal.app.ResolverActivity";
  public static final String RESOLVER_PKG = "android";
  public static final String SMS_TYPE = "sms";
  public static final String SUBJECT = "subject";
  public static final String SUB_CS = "sub_cs";
  public static final String THREAD_ID = "thread_id";
  public static final String TRANSPORT_TYPE = "transport_type";
  public static final String VND_ANDROID_DIR_MMS_SMS = "vnd.android-dir/mms-sms";
  private static AlertMessages mInstance;
  public static long mLastMsgDate;
  public static ContentObserver mMessageObserver = new ContentObserver(new Handler())
  {
    public void onChange(boolean paramAnonymousBoolean)
    {
      super.onChange(paramAnonymousBoolean);
      new Thread("MessageObserver::onChange")
      {
        public void run() {}
      }.start();
    } 
  };
  public static Long mThreadId;
  
  static
  {
    COMPLETE_URI = Uri.withAppendedPath(MESSAGE_URI, "complete-conversations");
    PROJECTIONS_MESSAGES = new String[] { "_id", "transport_type", "address", "body", "subject", "sub_cs", "thread_id", "date", "read" };
  } 
  
  private AlertMessages(Context paramContext)
  {
    mContext = paramContext;
  } 
  
  public static void clearMessagingAlert()
  {
    if (CircleAlert.isAlertDisplayed())
    {
      int i = CircleAlert.getAlertType();
      if ((i == 3) || (i == 4))
      {
        Log.d("Circle", "clearning messaging alert");
        CircleAlert.getInstance(mContext).clearAlert();
      } 
    } 
  } 
  
  public static AlertMessages getInstance(Context paramContext)
  {
    synchronized (mSyncObject)
    {
      if (mInstance == null) {
        mInstance = new AlertMessages(paramContext);
      } 
      return mInstance;
    } 
  } 
  
  static void lookMessageDB()
  {
    for (;;)
    {
      Cursor localCursor;
      try
      {
        boolean bool = CircleAlert.isTextMsgEnabled();
        if (!bool) {
          return;
        } 
        if (mInstance == null) {
          continue;
        } 
        localCursor = mContext.getContentResolver().query(COMPLETE_URI, PROJECTIONS_MESSAGES, null, null, "date DESC");
        if (localCursor == null) {
          continue;
        } 
        if (!localCursor.moveToFirst()) {
          break label348
        } 
        if (localCursor.getString(localCursor.getColumnIndex("transport_type")).equalsIgnoreCase("mms"))
        {
          localCursor.close();
          continue;
        } 
        str1 = localCursor.getString(localCursor.getColumnIndex("address"));
      }
      finally {}
      String str1;
      String str2 = localCursor.getString(localCursor.getColumnIndex("body"));
      Long localLong = Long.valueOf(localCursor.getLong(localCursor.getColumnIndex("thread_id")));
      long l = localCursor.getLong(localCursor.getColumnIndex("date"));
      Integer localInteger = Integer.valueOf(localCursor.getInt(localCursor.getColumnIndex("read")));
      if ((CircleAlert.isAlertDisplayed()) && (CircleAlert.getAlertType() == 3) && (localInteger.intValue() == 0) && (mLastMsgDate == l))
      {
        localCursor.close();
      }
      else if (localInteger.intValue() == 1)
      {
        if (localLong == mThreadId) {
          clearMessagingAlert();
        } 
        localCursor.close();
      }
      else
      {
        BaseAlert.AlertInfo localAlertInfo = new BaseAlert.AlertInfo();
        description = str2;
        number = str1;
        name = mInstance.getContactName(str1);
        timestamp = Long.toString(l);
        type = Integer.valueOf(3);
        imageId = Integer.valueOf(2130837541);
        if (Utility.isVerizonCarrier(mContext)) {
          imageId = Integer.valueOf(2130837542);
        } 
        CircleAlert.addItem(localAlertInfo);
        mThreadId = localLong;
        mLastMsgDate = l;
        label349:
        localCursor.close()
      } 
    } 
  } 
  
  public static void registerForMessageChange(Context paramContext)
  {
    paramContext.getContentResolver().registerContentObserver(MESSAGE_URI, false, mMessageObserver);
  } 
  
  public static void unregisterMessageChange(Context paramContext)
  {
    paramContext.getContentResolver().unregisterContentObserver(mMessageObserver);
  } 
  
  BaseAlert.AlertInfo addItem(Bundle paramBundle)
  {
    return null;
  } 
  
  public Intent getAlertAppIntent()
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    localIntent.setType("vnd.android-dir/mms-sms");
    localIntent.putExtra("thread_id", mThreadId);
    localIntent.addCategory("android.intent.category.DEFAULT");
    localIntent.addFlags(67108864);
    localIntent.addFlags(268435456);
    ResolveInfo localResolveInfo = mContext.getPackageManager().resolveActivity(localIntent, 65536);
    String str1 = null;
    String str2 = null;
    if (localResolveInfo != null)
    {
      ActivityInfo localActivityInfo = activityInfo;
      str1 = null;
      str2 = null;
      if (localActivityInfo != null) {
        if ("android".equals(packageName))
        {
          boolean bool = "com.android.internal.app.ResolverActivity".equals(name);
          str1 = null;
          str2 = null;
          if (bool) {}
        }
        else
        {
          str1 = packageName;
          str2 = name;
        } 
      } 
    } 
    if (TextUtils.isEmpty(str1)) {
      str1 = "com.motorola.messaging";
    } 
    if (TextUtils.isEmpty(str2)) {
      str2 = "com.motorola.messaging.activity.ComposeMessageActivity";
    } 
    localIntent.setClassName(str1, str2);
    return localIntent;
  } 
  
  public String getContactName(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      Log.i("Circle", "Get contact name returns becuase number is null");
    } 
    String str;
    for (;;)
    {
      return null;
      str = null;
      try
      {
        Uri localUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(paramString));
        Cursor localCursor = mContext.getContentResolver().query(localUri, PHONELOOKUP_PROJECTION, null, null, null);
        str = null;
        if (localCursor != null)
        {
          boolean bool = localCursor.moveToFirst();
          str = null;
          if (bool) {
            str = localCursor.getString(localCursor.getColumnIndex("display_name"));
          } 
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
 * Qualified Name:     com.motorola.widget.circlewidget3d.AlertMessages
 * JD-Core Version:    0.6.2
 */