package com.motorola.widget.circlewidget3d;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.motorola.homescreen.common.anim.AnimUtils;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CircleAlert extends Circle
{
  public static final int ALERT_SIDE_BACK = 1;
  public static final int ALERT_SIDE_FRONT = 0;
  public static final int ALERT_SIDE_INVALID = -1;
  private static final int ALTERNATE_TEXTURE_UNIT = 3;
  private static final int DEFAULT_TEXTURE_UNIT = 0;
  public static final String JSON_ALERT_KEY_DESCRIPTION = "DESCRIPTION";
  public static final String JSON_ALERT_KEY_ID = "ID";
  public static final String JSON_ALERT_KEY_IMAGE_ID = "IMAGE_ID";
  public static final String JSON_ALERT_KEY_NAME = "NAME";
  public static final String JSON_ALERT_KEY_NUMBER = "NUMBER";
  public static final String JSON_ALERT_KEY_TIMESTAMP = "TIMESTAMP";
  public static final String JSON_ALERT_KEY_TYPE = "TYPE";
  private static CircleAlert mInstance;
  static boolean sAlertDisplayed = false;
  private static int sAlertSide;
  private static int sAlertTextureUnit = 3;
  private static BaseAlert.AlertInfo sCurrentAlertInfo;
  static boolean sEnableMissedCall;
  static boolean sEnableTextMsg;
  static boolean sEnableVoicemail;
  private static boolean sHelpDisplayAgain;
  private static long sLastAlertTimeStamp = 0L;
  TextView mDescriptionView;
  TextView mNameNumberView;
  TextView mTimeStampView;
  ImageView mTypeImageView;

  private CircleAlert(Context paramContext)
  {
    this.mContext = paramContext;
    this.mCurrentId = 0;
    sAlertSide = -1;
    prepareCircle(2130903040, CircleConsts.CLOCK_BITMAP_SIZE.intValue());
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
    sEnableTextMsg = localSharedPreferences.getBoolean("enable_text_notification", true);
    sEnableMissedCall = localSharedPreferences.getBoolean("enable_missed_call_notification", true);
    sEnableVoicemail = localSharedPreferences.getBoolean("enable_voicemail_notification", true);
  }

  static void addItem(BaseAlert.AlertInfo paramAlertInfo)
  {
    if ((paramAlertInfo == null) || ((sAlertDisplayed) && (sCurrentAlertInfo != null)))
      try
      {
        if (Long.parseLong(paramAlertInfo.timestamp) < Long.parseLong(sCurrentAlertInfo.timestamp))
        {
          Log.w("Circle", "Not displaying new alert as it's timestamps is older then current alert");
          return;
        }
      }
      catch (Exception localException2)
      {
      }
    try
    {
      l = Long.parseLong(paramAlertInfo.timestamp);
      if (sLastAlertTimeStamp == l)
      {
        Log.w("Circle", "Not displaying new alert as it was clear it before");
        return;
      }
    }
    catch (Exception localException1)
    {
      while (true)
      {
        long l;
        sCurrentAlertInfo = paramAlertInfo;
        saveAlert(paramAlertInfo);
        if (mInstance == null)
          break;
        sHelpDisplayAgain = CircleHelp.isHelpDisplayed();
        mInstance.updateCircle();
        return;
        sLastAlertTimeStamp = l;
      }
      Log.e("Circle", "Why mInstance is null??");
    }
  }

  private void displayHelpScreenAgainIfNeeded()
  {
    if (sHelpDisplayAgain)
    {
      Intent localIntent = new Intent();
      localIntent.setAction("com.motorola.widget.circlewidget3d.ACTION_DISPLAY_HELP_CIRCLE");
      localIntent.putExtra("force", true);
      this.mContext.sendBroadcast(localIntent);
      sHelpDisplayAgain = false;
    }
  }

  private static void fadeTextureUnits(int paramInt, long paramLong)
  {
    JSONArray localJSONArray = new JSONArray();
    float[] arrayOfFloat = new float[3];
    arrayOfFloat[0] = 0.0F;
    if (paramInt == 0);
    for (float f = 0.0F; ; f = 10.0F)
    {
      arrayOfFloat[1] = f;
      arrayOfFloat[2] = 0.0F;
      localJSONArray.put(AnimUtils.createJSONData3D("alert_alpha", "translation", null, arrayOfFloat, (float)paramLong, false));
      Utility.updatAnimation(null, localJSONArray);
      CircleClock.getInstance(mInstance.mContext).setCurrentTextureUnit(paramInt);
      return;
    }
  }

  public static int getAlertDisplayedSide()
  {
    return sAlertSide;
  }

  private Bitmap getAlertScreen(BaseAlert.AlertInfo paramAlertInfo)
  {
    String str1;
    label44: int i;
    label86: label112: Long localLong;
    String str3;
    label224: int m;
    if (paramAlertInfo != null)
      if (paramAlertInfo.number != null)
      {
        if (AlertMissedCall.isDialable(paramAlertInfo.number))
          paramAlertInfo.number = PhoneNumberUtils.formatNumber(paramAlertInfo.number);
      }
      else
      {
        if (paramAlertInfo.name == null)
          break label295;
        str1 = paramAlertInfo.name;
        this.mNameNumberView.setText(str1);
        if (paramAlertInfo.description != null)
          this.mDescriptionView.setText(paramAlertInfo.description);
        TextView localTextView1 = this.mDescriptionView;
        if (paramAlertInfo.description == null)
          break label303;
        i = 0;
        localTextView1.setVisibility(i);
        if (paramAlertInfo.type.intValue() != 2)
          break label309;
        this.mDescriptionView.setLines(1);
        this.mTypeImageView.setImageResource(paramAlertInfo.imageId.intValue());
        int j = paramAlertInfo.type.intValue();
        int k = 0;
        if (j != 3)
        {
          int n = paramAlertInfo.type.intValue();
          k = 0;
          if (n != 4)
          {
            String str2 = paramAlertInfo.timestamp;
            k = 0;
            if (str2 != null)
            {
              localLong = Long.valueOf(Long.parseLong(paramAlertInfo.timestamp));
              if (!Utility.is24HourFormat(this.mContext))
                break label320;
              Calendar localCalendar = Calendar.getInstance();
              localCalendar.setTimeInMillis(localLong.longValue());
              str3 = Utility.getTimeString(this.mContext, localCalendar);
              this.mTimeStampView.setText(str3);
              k = 1;
            }
          }
        }
        TextView localTextView2 = this.mTimeStampView;
        if (k == 0)
          break label344;
        m = 0;
        label250: localTextView2.setVisibility(m);
        this.mBitmap.eraseColor(0);
        this.mLayout.draw(this.mCanvas);
      }
    while (true)
    {
      return this.mBitmap;
      paramAlertInfo.name = AlertMissedCall.getPrivateNumberString(paramAlertInfo.number);
      break;
      label295: str1 = paramAlertInfo.number;
      break label44;
      label303: i = 4;
      break label86;
      label309: this.mDescriptionView.setLines(2);
      break label112;
      label320: str3 = DateFormat.getTimeInstance(3).format(new Date(localLong.longValue()));
      break label224;
      label344: m = 8;
      break label250;
      Log.e("Circle", "Info null in getAlertScreen");
    }
  }

  public static int getAlertType()
  {
    return sCurrentAlertInfo.type.intValue();
  }

  public static CircleAlert getInstance(Context paramContext)
  {
    synchronized (syncObject)
    {
      if (mInstance == null)
        mInstance = new CircleAlert(paramContext);
      return mInstance;
    }
  }

  private static Integer getIntegerSafely(JSONObject paramJSONObject, String paramString)
  {
    Object localObject = null;
    if (paramJSONObject != null)
    {
      localObject = null;
      if (paramString == null);
    }
    try
    {
      boolean bool = paramJSONObject.isNull(paramString);
      localObject = null;
      if (!bool)
      {
        Integer localInteger = Integer.valueOf(paramJSONObject.getInt(paramString));
        localObject = localInteger;
      }
      return localObject;
    }
    catch (JSONException localJSONException)
    {
    }
    return null;
  }

  private static String getStringSafely(JSONObject paramJSONObject, String paramString)
  {
    Object localObject = null;
    if (paramJSONObject != null)
    {
      localObject = null;
      if (paramString == null);
    }
    try
    {
      boolean bool = paramJSONObject.isNull(paramString);
      localObject = null;
      if (!bool)
      {
        String str = paramJSONObject.getString(paramString);
        localObject = str;
      }
      return localObject;
    }
    catch (JSONException localJSONException)
    {
    }
    return null;
  }

  public static boolean isAlertDisplayed()
  {
    return sAlertDisplayed;
  }

  static boolean isMissedCallEnabled()
  {
    return sEnableMissedCall;
  }

  static boolean isTextMsgEnabled()
  {
    return sEnableTextMsg;
  }

  static boolean isVoicemailEnabled()
  {
    return sEnableVoicemail;
  }

  private void launchAlertApp()
  {
    BaseAlert.AlertInfo localAlertInfo = sCurrentAlertInfo;
    Intent localIntent = null;
    if (localAlertInfo != null)
    {
      int i = sCurrentAlertInfo.type.intValue();
      localIntent = null;
      switch (i)
      {
      default:
      case 1:
      case 3:
      case 2:
      case 4:
      }
    }
    while (true)
      if (localIntent != null)
        try
        {
          this.mContext.startActivity(localIntent);
          clearAlert();
          return;
          localIntent = AlertMissedCall.getInstance(this.mContext).getAlertAppIntent();
          continue;
          localIntent = AlertMessages.getInstance(this.mContext).getAlertAppIntent();
          continue;
          localIntent = AlertVoicemailMoto.getInstance(this.mContext).getAlertAppIntent();
          continue;
          localIntent = AlertMMS.getInstance(this.mContext).getAlertAppIntent();
        }
        catch (Exception localException)
        {
          while (true)
            Log.e("Circle", "Error while launching app: " + localException);
        }
    Log.e("Circle", "Start intent not available");
  }

  // ERROR //
  public static void loadLastAlert()
  {
    // Byte code:
    //   0: getstatic 152	com/motorola/widget/circlewidget3d/CircleAlert:mInstance	Lcom/motorola/widget/circlewidget3d/CircleAlert;
    //   3: getfield 72	com/motorola/widget/circlewidget3d/CircleAlert:mContext	Landroid/content/Context;
    //   6: invokestatic 100	android/preference/PreferenceManager:getDefaultSharedPreferences	(Landroid/content/Context;)Landroid/content/SharedPreferences;
    //   9: ldc_w 434
    //   12: aconst_null
    //   13: invokeinterface 437 3 0
    //   18: astore_0
    //   19: aload_0
    //   20: ifnonnull +4 -> 24
    //   23: return
    //   24: new 126	com/motorola/widget/circlewidget3d/BaseAlert$AlertInfo
    //   27: dup
    //   28: invokespecial 438	com/motorola/widget/circlewidget3d/BaseAlert$AlertInfo:<init>	()V
    //   31: astore_1
    //   32: new 362	org/json/JSONObject
    //   35: dup
    //   36: aload_0
    //   37: invokespecial 441	org/json/JSONObject:<init>	(Ljava/lang/String;)V
    //   40: astore_2
    //   41: aload_1
    //   42: aload_2
    //   43: ldc 21
    //   45: invokestatic 443	com/motorola/widget/circlewidget3d/CircleAlert:getIntegerSafely	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/Integer;
    //   48: putfield 446	com/motorola/widget/circlewidget3d/BaseAlert$AlertInfo:id	Ljava/lang/Integer;
    //   51: aload_1
    //   52: aload_2
    //   53: ldc 27
    //   55: invokestatic 448	com/motorola/widget/circlewidget3d/CircleAlert:getStringSafely	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   58: putfield 247	com/motorola/widget/circlewidget3d/BaseAlert$AlertInfo:name	Ljava/lang/String;
    //   61: aload_1
    //   62: aload_2
    //   63: ldc 30
    //   65: invokestatic 448	com/motorola/widget/circlewidget3d/CircleAlert:getStringSafely	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   68: putfield 232	com/motorola/widget/circlewidget3d/BaseAlert$AlertInfo:number	Ljava/lang/String;
    //   71: aload_1
    //   72: aload_2
    //   73: ldc 33
    //   75: invokestatic 448	com/motorola/widget/circlewidget3d/CircleAlert:getStringSafely	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   78: putfield 129	com/motorola/widget/circlewidget3d/BaseAlert$AlertInfo:timestamp	Ljava/lang/String;
    //   81: aload_1
    //   82: aload_2
    //   83: ldc 18
    //   85: invokestatic 448	com/motorola/widget/circlewidget3d/CircleAlert:getStringSafely	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/String;
    //   88: putfield 258	com/motorola/widget/circlewidget3d/BaseAlert$AlertInfo:description	Ljava/lang/String;
    //   91: aload_1
    //   92: aload_2
    //   93: ldc 36
    //   95: invokestatic 443	com/motorola/widget/circlewidget3d/CircleAlert:getIntegerSafely	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/Integer;
    //   98: putfield 266	com/motorola/widget/circlewidget3d/BaseAlert$AlertInfo:type	Ljava/lang/Integer;
    //   101: aload_1
    //   102: aload_2
    //   103: ldc 24
    //   105: invokestatic 443	com/motorola/widget/circlewidget3d/CircleAlert:getIntegerSafely	(Lorg/json/JSONObject;Ljava/lang/String;)Ljava/lang/Integer;
    //   108: putfield 274	com/motorola/widget/circlewidget3d/BaseAlert$AlertInfo:imageId	Ljava/lang/Integer;
    //   111: aload_1
    //   112: getfield 129	com/motorola/widget/circlewidget3d/BaseAlert$AlertInfo:timestamp	Ljava/lang/String;
    //   115: ifnull +13 -> 128
    //   118: aload_1
    //   119: getfield 129	com/motorola/widget/circlewidget3d/BaseAlert$AlertInfo:timestamp	Ljava/lang/String;
    //   122: invokestatic 135	java/lang/Long:parseLong	(Ljava/lang/String;)J
    //   125: putstatic 64	com/motorola/widget/circlewidget3d/CircleAlert:sLastAlertTimeStamp	J
    //   128: aload_1
    //   129: putstatic 124	com/motorola/widget/circlewidget3d/CircleAlert:sCurrentAlertInfo	Lcom/motorola/widget/circlewidget3d/BaseAlert$AlertInfo;
    //   132: getstatic 152	com/motorola/widget/circlewidget3d/CircleAlert:mInstance	Lcom/motorola/widget/circlewidget3d/CircleAlert;
    //   135: invokevirtual 163	com/motorola/widget/circlewidget3d/CircleAlert:updateCircle	()V
    //   138: return
    //   139: astore_3
    //   140: aload_3
    //   141: invokevirtual 451	org/json/JSONException:printStackTrace	()V
    //   144: ldc 137
    //   146: ldc_w 453
    //   149: invokestatic 168	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   152: pop
    //   153: goto -42 -> 111
    //   156: astore_3
    //   157: goto -17 -> 140
    //
    // Exception table:
    //   from	to	target	type
    //   32	41	139	org/json/JSONException
    //   41	111	156	org/json/JSONException
  }

  public static void removeLastAlertKey(Context paramContext)
  {
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(paramContext);
    if (localSharedPreferences.getString("last_alert_values", null) != null)
      localSharedPreferences.edit().remove("last_alert_values").apply();
  }

  public static void saveAlert(BaseAlert.AlertInfo paramAlertInfo)
  {
    if (mInstance == null)
    {
      Log.e("Circle", "This method must be called after create a circle.");
      return;
    }
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("ID", paramAlertInfo.id).put("NAME", paramAlertInfo.name).put("NUMBER", paramAlertInfo.number).put("TIMESTAMP", paramAlertInfo.timestamp).put("DESCRIPTION", paramAlertInfo.description).put("TYPE", paramAlertInfo.type).put("IMAGE_ID", paramAlertInfo.imageId);
      label89: String str = localJSONObject.toString();
      SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(mInstance.mContext).edit();
      localEditor.putString("last_alert_values", str);
      localEditor.apply();
      return;
    }
    catch (JSONException localJSONException)
    {
      break label89;
    }
  }

  public static void setAlertState(boolean paramBoolean)
  {
    sAlertDisplayed = paramBoolean;
    sAlertTextureUnit = 3;
    fadeTextureUnits(0, 0L);
  }

  public static void setHelpDisplayAgain(boolean paramBoolean)
  {
    sHelpDisplayAgain = false;
  }

  public void clearAlert()
  {
    sCurrentAlertInfo = null;
    sAlertDisplayed = false;
    sAlertSide = -1;
    Utility.hideAlertScreen();
    Utility.showAnalogClock();
    displayHelpScreenAgainIfNeeded();
    removeLastAlertKey(this.mContext);
    new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          Thread.sleep(2000L);
          CircleClock localCircleClock = CircleClock.getInstance(CircleAlert.this.mContext);
          boolean bool1 = localCircleClock.isFlipped();
          Utility.flipCircle(null, "circle_time", 300.0F, bool1);
          if (!bool1);
          for (boolean bool2 = true; ; bool2 = false)
          {
            localCircleClock.setFlipped(bool2);
            return;
          }
        }
        catch (InterruptedException localInterruptedException)
        {
          localInterruptedException.printStackTrace();
        }
      }
    }).start();
  }

  public void clearNotification(Context paramContext, Intent paramIntent)
  {
    if (!isAlertDisplayed());
    int i;
    do
    {
      return;
      long l1 = paramIntent.getLongExtra("timestamp", -1L);
      long l2 = Long.parseLong(sCurrentAlertInfo.timestamp);
      long l3 = l1 / 100000L;
      boolean bool = l2 / 100000L < l3;
      i = 0;
      if (!bool)
        i = 1;
    }
    while (i == 0);
    clearAlert();
  }

  public Bitmap getBackTexture(Bundle paramBundle)
  {
    return null;
  }

  public Bitmap getFrontTexture(Bundle paramBundle)
  {
    return null;
  }

  public void handleDestroy()
  {
    super.handleDestroy();
    mInstance = null;
  }

  public boolean handleFling(Messenger paramMessenger, Message paramMessage, Float paramFloat)
  {
    return false;
  }

  public boolean handleSingleTap(Bundle paramBundle)
  {
    paramBundle.getString("shape_name");
    launchAlertApp();
    return false;
  }

  public View prepareCircle(int paramInt1, int paramInt2)
  {
    View localView = super.prepareCircle(paramInt1, paramInt2);
    this.mTypeImageView = ((ImageView)localView.findViewById(2131427329));
    this.mNameNumberView = ((TextView)localView.findViewById(2131427330));
    this.mDescriptionView = ((TextView)localView.findViewById(2131427331));
    this.mTimeStampView = ((TextView)localView.findViewById(2131427332));
    return localView;
  }

  public void restoreAlertState()
  {
    updateCircle();
  }

  public void setTheme(String paramString)
  {
  }

  public void updateCircle()
  {
    if (sCurrentAlertInfo == null)
    {
      Log.e("Circle", "Not updating alert because Notification setting is OFF");
      Utility.showAnalogClock();
      return;
    }
    if (!CircleWidget3DProvider.isScreenOn())
    {
      Log.e("Circle", "Screen is off so not displaying alert currently");
      this.mRefreshNeeded = true;
      return;
    }
    CircleClock localCircleClock = CircleClock.getInstance(this.mContext);
    boolean bool1 = localCircleClock.isFlipped();
    int i = 1;
    String str;
    int m;
    label77: label82: boolean bool2;
    if (!sAlertDisplayed)
      if (bool1)
      {
        str = "circle_time/circlefront";
        if (!bool1)
          break label226;
        m = 0;
        sAlertSide = m;
        if (i != 0)
        {
          Utility.flipCircle(null, "circle_time", 300.0F, bool1);
          if (bool1)
            break label322;
          bool2 = true;
          label104: localCircleClock.setFlipped(bool2);
        }
        if (sAlertSide == 1)
          Utility.hideAnalogClock();
        Utility.updateTexture(null, str + ":t1", "alert_front_mask");
        Utility.updateTexture(null, str + ":t" + sAlertTextureUnit, getAlertScreen(sCurrentAlertInfo));
        sAlertDisplayed = true;
        fadeTextureUnits(sAlertTextureUnit, 1000L);
        if (sAlertTextureUnit != 0)
          break label328;
      }
    label282: label293: label316: label322: label328: for (int k = 3; ; k = 0)
    {
      sAlertTextureUnit = k;
      CircleHelp.setHelpDisplayed(false);
      return;
      str = "circle_time/circleback";
      break;
      label226: m = 1;
      break label77;
      if (sAlertSide != -1)
      {
        if (sAlertSide == 0);
        for (str = "circle_time/circlefront"; ; str = "circle_time/circleback")
        {
          if (((sAlertSide != 0) || (bool1)) && ((sAlertSide != 1) || (!bool1)))
            break label282;
          i = 0;
          break;
        }
        break label82;
      }
      if (bool1)
      {
        str = "circle_time/circlefront";
        if (!bool1)
          break label316;
      }
      for (int j = 0; ; j = 1)
      {
        sAlertSide = j;
        break;
        str = "circle_time/circleback";
        break label293;
      }
      bool2 = false;
      break label104;
    }
  }

  public void updateCircle(Context paramContext, Intent paramIntent)
  {
    updateCircle();
  }

  public void updateSettingValues(Intent paramIntent)
  {
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
    boolean bool1 = localSharedPreferences.getBoolean("enable_text_notification", sEnableTextMsg);
    boolean bool2 = localSharedPreferences.getBoolean("enable_missed_call_notification", sEnableMissedCall);
    boolean bool3 = localSharedPreferences.getBoolean("enable_voicemail_notification", sEnableVoicemail);
    sEnableTextMsg = bool1;
    sEnableMissedCall = bool2;
    sEnableVoicemail = bool3;
  }

  public void updateValues(Context paramContext, Intent paramIntent)
  {
  }
}

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.CircleAlert
 * JD-Core Version:    0.6.2
 */