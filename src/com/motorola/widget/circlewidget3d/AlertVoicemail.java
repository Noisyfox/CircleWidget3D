package com.motorola.widget.circlewidget3d;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.VoicemailContract.Voicemails;
import android.util.Log;

public class AlertVoicemail
  extends BaseAlert
{
  public static final String[] PROJECTION_VOICEMAIL = { "_id", "number", "source_package", "is_read", "duration" };
  private static AlertVoicemail mInstance;
  public static ContentObserver mVoicemailObserver = new ContentObserver(new Handler())
  {
    public void onChange(boolean paramAnonymousBoolean)
    {
      super.onChange(paramAnonymousBoolean);
      AlertVoicemail.access$000();
    } 
  };
  
  private AlertVoicemail(Context paramContext)
  {
    mContext = paramContext;
  } 
  
  public static AlertVoicemail getInstance(Context paramContext)
  {
    synchronized (mSyncObject)
    {
      if (mInstance == null) {
        mInstance = new AlertVoicemail(paramContext);
      } 
      return mInstance;
    } 
  } 
  
  private static void lookVoicemailDB()
  {
    if (!CircleAlert.isVoicemailEnabled()) {
      return;
    } 
    if (mInstance != null)
    {
      Cursor localCursor = mContext.getContentResolver().query(VoicemailContract.Voicemails.CONTENT_URI, PROJECTION_VOICEMAIL, null, null, "date DESC");
      if (localCursor != null)
      {
        if (localCursor.moveToFirst())
        {
          String str1 = localCursor.getString(localCursor.getColumnIndex("number"));
          String str2 = localCursor.getString(localCursor.getColumnIndex("duration"));
          BaseAlert.AlertInfo localAlertInfo = new BaseAlert.AlertInfo();
          number = str1;
          name = null;
          description = str2;
          imageId = Integer.valueOf(2130837543);
          if (Utility.isVerizonCarrier(mContext)) {
            imageId = Integer.valueOf(2130837544);
          } 
          type = Integer.valueOf(2);
          timestamp = null;
          CircleAlert.addItem(localAlertInfo);
        } 
        for (;;)
        {
          localCursor.close();
          return;
          Log.e("Circle", "cursor can't move to first lookVoicemailDB");
        } 
      } 
      Log.e("Circle", "cursor is null lookVoicemailDB");
      return;
    } 
    Log.e("Circle", "mInstance is null lookVoicemailDB");
  } 
  
  public static void registerVoicemailChange(Context paramContext)
  {
    paramContext.getContentResolver().registerContentObserver(VoicemailContract.Voicemails.CONTENT_URI, false, mVoicemailObserver);
  } 
  
  public static void unregisterVoicemailChange(Context paramContext)
  {
    paramContext.getContentResolver().unregisterContentObserver(mVoicemailObserver);
  } 
  
  BaseAlert.AlertInfo addItem(Bundle paramBundle)
  {
    return null;
  } 
  
  public Intent getAlertAppIntent()
  {
    return null;
  } 
} 

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.AlertVoicemail
 * JD-Core Version:    0.6.2
 */