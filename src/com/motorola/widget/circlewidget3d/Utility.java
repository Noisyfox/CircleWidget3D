package com.motorola.widget.circlewidget3d;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import com.motorola.homescreen.common.anim.AnimUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Utility
{
  private static int mBitmapIndex = 0;

  public static void change2DViewVisibility(Messenger paramMessenger, String paramString, boolean paramBoolean)
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("visibility_shapes", paramString);
    localBundle.putBoolean("visibility", paramBoolean);
    Message localMessage = Message.obtain(null, 17);
    localMessage.setData(localBundle);
    CircleWidget3DProvider.sendMessage(paramMessenger, localMessage);
  }

  private static void change2DViewVisibilityWithDelay(Messenger paramMessenger, final String paramString, final boolean paramBoolean, long paramLong)
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          Thread.sleep(this.val$delay);
          Utility.change2DViewVisibility(paramString, paramBoolean, this.val$visibility);
          return;
        }
        catch (InterruptedException localInterruptedException)
        {
          localInterruptedException.printStackTrace();
        }
      }
    }).start();
  }

  public static void changeVisibility(Messenger paramMessenger, String[] paramArrayOfString, boolean[] paramArrayOfBoolean)
  {
    if ((paramArrayOfString != null) && (paramArrayOfBoolean != null))
    {
      if (paramArrayOfString.length != paramArrayOfBoolean.length)
        Log.e("Circle", "length different : " + paramArrayOfString.length + " " + paramArrayOfBoolean.length);
    }
    else
      return;
    Bundle localBundle = new Bundle();
    localBundle.putStringArray("visibility_shapes", paramArrayOfString);
    localBundle.putBooleanArray("visibility", paramArrayOfBoolean);
    Message localMessage = Message.obtain(null, 10);
    localMessage.setData(localBundle);
    CircleWidget3DProvider.sendMessage(paramMessenger, localMessage);
  }

  public static void changeVisibility(String paramString, boolean paramBoolean)
  {
    changeVisibility(null, new String[] { paramString }, new boolean[] { paramBoolean });
  }

  public static void flipCircle(Messenger paramMessenger, String paramString1, float paramFloat, long paramLong, boolean paramBoolean, String paramString2)
  {
    float f;
    if (paramBoolean)
      f = 0.0F;
    while (true)
    {
      JSONArray localJSONArray = new JSONArray();
      float[] arrayOfFloat = new float[3];
      arrayOfFloat[0] = 0.0F;
      if (paramFloat > 0.0F)
        f = -f;
      arrayOfFloat[1] = f;
      arrayOfFloat[2] = 0.0F;
      JSONObject localJSONObject1 = AnimUtils.createJSONData3D(paramString1, "rotation", arrayOfFloat, (float)paramLong, 7, 2.0F);
      if (paramString2 != null);
      try
      {
        localJSONObject1.put("anim_id", paramString2);
        localJSONArray.put(localJSONObject1);
        localJSONObject2 = new JSONObject();
      }
      catch (Exception localException2)
      {
        try
        {
          JSONObject localJSONObject2;
          localJSONObject2.put("new_data", localJSONArray);
          Bundle localBundle = new Bundle();
          localBundle.putString("anim_data", localJSONObject2.toString());
          Message localMessage = Message.obtain(null, 9);
          localMessage.setData(localBundle);
          CircleWidget3DProvider.sendMessage(paramMessenger, localMessage);
          return;
          f = 180.0F;
          continue;
          localException2 = localException2;
          Log.e("Circle", "Exception adding animation ID" + localException2);
        }
        catch (Exception localException1)
        {
          while (true)
            Log.e("Circle", "Exception json" + localException1);
        }
      }
    }
  }

  public static void flipCircle(Messenger paramMessenger, String paramString, float paramFloat, boolean paramBoolean)
  {
    flipCircle(paramMessenger, paramString, paramFloat, 300L, paramBoolean, null);
  }

  public static String getTimeString(Context paramContext, Calendar paramCalendar)
  {
    int i = 1;
    boolean bool = is24HourFormat(paramContext);
    int j;
    int k;
    int m;
    int n;
    label41: String str1;
    if (bool)
    {
      j = 11;
      k = paramCalendar.get(j);
      m = paramCalendar.get(12);
      if (m >= 10)
        break label140;
      n = i;
      if (n == 0)
        break label146;
      str1 = "0";
      label50: if ((k != 0) || (!bool))
        break label153;
      label59: if (i == 0)
        break label158;
    }
    label140: label146: label153: label158: for (String str2 = "0"; ; str2 = "")
    {
      if ((k == 0) && (!bool))
        k = 12;
      String str3 = paramContext.getResources().getString(2131230731);
      return str2 + Integer.toString(k) + str3 + str1 + Integer.toString(m);
      j = 10;
      break;
      n = 0;
      break label41;
      str1 = "";
      break label50;
      i = 0;
      break label59;
    }
  }

  public static void handlePanelFocus(Message paramMessage)
  {
    playFrames(null, 120, 180, 2500L, "condition_id");
  }

  public static void hideAlertScreen()
  {
    CircleAlert.setAlertState(false);
  }

  public static void hideAnalogClock()
  {
    boolean[] arrayOfBoolean = { 0, 0, 0, 0 };
    changeVisibility(null, CircleClock.CLOCK_HANDS_SHAPES, arrayOfBoolean);
  }

  public static void initCircleInCaseLoadingFirstTime()
  {
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(CircleWidget3DProvider.CircleService.getServiceContext());
    if (localSharedPreferences.getBoolean("first_time_circle_load", true))
    {
      CircleAlert.setAlertState(false);
      boolean[] arrayOfBoolean = { 1, 1, 1, 1 };
      changeVisibility(null, CircleClock.CLOCK_HANDS_SHAPES, arrayOfBoolean);
      updateTexture(null, "circle_time/circlefront:t1", "clock_front_mask");
      updateTexture(null, "circle_time/circleback:t1", "clock_back_mask");
      changeVisibility("circle_battery/level", true);
      SharedPreferences.Editor localEditor = localSharedPreferences.edit();
      localEditor.putBoolean("first_time_circle_load", false);
      localEditor.apply();
    }
  }

  public static boolean is24HourFormat(Context paramContext)
  {
    return DateFormat.is24HourFormat(paramContext);
  }

  public static boolean isDataServiceAvail(Context paramContext)
  {
    Intent localIntent = new Intent("com.motorola.datameter.ACTION_START_DATA_METER_SERVICE");
    List localList = paramContext.getPackageManager().queryBroadcastReceivers(localIntent, 0);
    boolean bool = false;
    if (localList != null)
    {
      int i = localList.size();
      bool = false;
      if (i > 0)
        bool = true;
    }
    return bool;
  }

  public static boolean isVerizonCarrier(Context paramContext)
  {
    TelephonyManager localTelephonyManager = (TelephonyManager)paramContext.getSystemService("phone");
    boolean bool1 = false;
    if (localTelephonyManager != null)
    {
      String str = localTelephonyManager.getNetworkOperatorName();
      bool1 = false;
      if (str != null)
      {
        boolean bool2 = str.contains("Verizon");
        bool1 = false;
        if (bool2)
          bool1 = true;
      }
    }
    return bool1;
  }

  public static void moveTexture(Messenger paramMessenger, String paramString, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, long paramLong)
  {
    moveTexture(paramMessenger, paramString, null, paramInt, paramFloat1, paramFloat2, paramFloat3, paramLong);
  }

  public static void moveTexture(Messenger paramMessenger, String paramString1, String paramString2, int paramInt, float paramFloat1, float paramFloat2, float paramFloat3, long paramLong)
  {
    JSONArray localJSONArray = new JSONArray();
    float[] arrayOfFloat = { paramFloat1, paramFloat2, paramFloat3 };
    localJSONArray.put(AnimUtils.createJSONData3D(paramString1 + ":t" + paramInt, "translation", paramString2, arrayOfFloat, (float)paramLong, false));
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("new_data", localJSONArray);
      Bundle localBundle = new Bundle();
      localBundle.putString("anim_data", localJSONObject.toString());
      Message localMessage = Message.obtain(null, 9);
      localMessage.setData(localBundle);
      CircleWidget3DProvider.sendMessage(paramMessenger, localMessage);
      return;
    }
    catch (Exception localException)
    {
      while (true)
        Log.e("Circle", "Exception json" + localException);
    }
  }

  public static void playFrames(Messenger paramMessenger, int paramInt1, int paramInt2, long paramLong, String paramString)
  {
    Message localMessage = Message.obtain(null, 11);
    Bundle localBundle = new Bundle();
    localBundle.putInt("start_frame_index", paramInt1);
    localBundle.putInt("end_frame_index", paramInt2);
    localBundle.putLong("frame_anim_duration", paramLong);
    localBundle.putString("anim_id", paramString);
    localMessage.setData(localBundle);
    CircleWidget3DProvider.sendMessage(paramMessenger, localMessage);
  }

  public static void prepareCircles(Context paramContext)
  {
    CircleBattery.getInstance(paramContext).prepareCircle(2130903042, CircleConsts.BATTERY_BITMAP_SIZE.intValue());
    CircleClock.getInstance(paramContext).prepareCircle(2130903046, CircleConsts.CLOCK_BITMAP_SIZE.intValue());
    CircleWeather.getInstance(paramContext).prepareCircle(2130903062, CircleConsts.WEATHER_BITMAP_SIZE.intValue());
    CircleAlert.getInstance(paramContext).prepareCircle(2130903040, CircleConsts.CLOCK_BITMAP_SIZE.intValue());
    if ((CircleAlert.isAlertDisplayed()) && (CircleAlert.getAlertType() == 2))
    {
      AlertVoicemailMoto localAlertVoicemailMoto = AlertVoicemailMoto.getInstance(paramContext);
      localAlertVoicemailMoto.retrieveStrings(paramContext);
      localAlertVoicemailMoto.addItem(null);
    }
    AlertMMS.getInstance(paramContext).retrieveStrings(paramContext);
    if ((CircleWidget3DProvider.isDataServiceAvail()) || (Config.isDeviceDataSupported()))
      CircleData.getInstance(paramContext).prepareCircle(2130903048, CircleConsts.BATTERY_BITMAP_SIZE.intValue());
  }

  public static void refreshCircles()
  {
    Context localContext = CircleWidget3DProvider.CircleService.getServiceContext();
    CircleClock.getInstance(localContext).refreshCircleIfNeeded();
    CircleBattery.getInstance(localContext).refreshCircleIfNeeded();
    CircleAlert.getInstance(localContext).refreshCircleIfNeeded();
  }

  public static String retrieveCurrentThemePkg(Context paramContext)
  {
    return PreferenceManager.getDefaultSharedPreferences(paramContext).getString("current_theme", null);
  }

  public static boolean saveBitmapAsImage(Context paramContext, Bitmap paramBitmap)
  {
    boolean bool = false;
    File localFile1;
    if (paramBitmap != null)
    {
      localFile1 = Environment.getExternalStorageDirectory();
      if (!localFile1.exists())
        Log.d("Circle", "SD Card error");
    }
    else
    {
      return false;
    }
    File localFile2 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Circle3D");
    Log.d("Circle", "dirPath: " + localFile2.toString());
    String str = "/Image" + mBitmapIndex + ".jpg";
    File localFile3;
    if (localFile2.exists())
      localFile3 = new File(localFile2.getAbsolutePath() + str);
    while (true)
    {
      Log.d("Circle", "File Path: " + localFile3.toString());
      try
      {
        bool = paramBitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(localFile3));
        mBitmapIndex = 1 + mBitmapIndex;
        return bool;
      }
      catch (IOException localIOException)
      {
        Log.e("Circle", "IOException " + localIOException);
        return bool;
      }
      if (localFile2.mkdir())
      {
        Log.d("Circle", "Dir created");
        localFile3 = new File(localFile2.getAbsolutePath() + str);
      }
      else
      {
        Log.e("Circle", "Dir creation failed");
        localFile3 = new File(localFile1.getAbsolutePath() + str);
      }
    }
  }

  public static void sendAllTextures()
  {
    initCircleInCaseLoadingFirstTime();
    playFrames(null, 0, 120, 1L, "start_anim_id");
    CircleBattery localCircleBattery = CircleBattery.getInstance(null);
    if ((!localCircleBattery.shouldDisplayDataCircle()) && ((!Config.isDeviceDataSupported()) || (!CircleData.isFrontSideIsDataCircle())))
    {
      localCircleBattery.updateCircle();
      if (!localCircleBattery.isFlipped())
        break label162;
      flipCircle(null, "circle_battery", 300.0F, false);
    }
    while (true)
    {
      CircleClock localCircleClock = CircleClock.getInstance(null);
      localCircleClock.setAnalogClockState(-1);
      if (CircleHelp.isHelpDisplayed())
        CircleHelp.getInstance(null).restoreHelpState();
      localCircleClock.getAlarmCondition();
      localCircleClock.updateCircle();
      if (localCircleClock.isFlipped())
        flipCircle(null, "circle_time", 300.0F, false);
      CircleWeather localCircleWeather = CircleWeather.getInstance(null);
      CircleAlert.loadLastAlert();
      localCircleWeather.setFlipped(false);
      localCircleWeather.preUpdateCircle();
      new Thread(new Runnable()
      {
        public void run()
        {
          try
          {
            Thread.sleep(1400L);
            this.val$weather.postUpdateCircle(true);
            return;
          }
          catch (InterruptedException localInterruptedException)
          {
            localInterruptedException.printStackTrace();
          }
        }
      }).start();
      return;
      if (!Config.isDeviceDataSupported())
        break;
      CircleData.getInstance(null).updateCircle();
      break;
      label162: if ((CircleWidget3DProvider.isDataServiceAvail()) && (CircleData.isDataCircleEnable()) && (CircleData.isHasCachedData()))
        CircleData.getInstance(null).populateData();
    }
  }

  public static void showAlertScreen()
  {
    CircleAlert.setAlertState(true);
  }

  public static void showAnalogClock()
  {
    CircleAlert.setAlertState(false);
    CircleClock localCircleClock = CircleClock.getInstance(null);
    localCircleClock.setAnalogClockState(-1);
    localCircleClock.updateCircle();
    boolean[] arrayOfBoolean = { 1, 1, 1, 1 };
    changeVisibility(null, CircleClock.CLOCK_HANDS_SHAPES, arrayOfBoolean);
    updateTexture(null, "circle_time/circlefront:t1", "clock_front_mask");
    updateTexture(null, "circle_time/circleback:t1", "clock_back_mask");
  }

  public static void startCircleSettings(Context paramContext)
  {
    Intent localIntent = new Intent(paramContext, CirclePreferenceActivity.class);
    localIntent.setFlags(337641472);
    paramContext.startActivity(localIntent);
  }

  public static void startSelectAppActivity(Context paramContext)
  {
    Intent localIntent = new Intent(paramContext, CircleSelectAppActivity.class);
    localIntent.setFlags(337641472);
    paramContext.startActivity(localIntent);
  }

  public static void updatAnimation(Messenger paramMessenger, JSONArray paramJSONArray)
  {
    if (paramJSONArray == null)
    {
      Log.e("Circle", "No anim data");
      return;
    }
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("new_data", paramJSONArray);
      Bundle localBundle = new Bundle();
      localBundle.putString("anim_data", localJSONObject.toString());
      Message localMessage = Message.obtain(null, 9);
      localMessage.setData(localBundle);
      CircleWidget3DProvider.sendMessage(paramMessenger, localMessage);
      return;
    }
    catch (Exception localException)
    {
      while (true)
        Log.e("Circle", "exception json");
    }
  }

  public static void updateBatteryLevelTexture(int paramInt)
  {
    String str = null;
    switch (paramInt)
    {
    default:
    case 1:
    case 2:
    case 3:
    case 4:
    }
    while (true)
    {
      updateTexture(null, "circle_battery/level", str);
      return;
      str = "battery_level_green";
      continue;
      str = "battery_level_orange";
      continue;
      str = "battery_level_red";
      continue;
      str = "battery_level_gray";
    }
  }

  public static void updateTexture(Messenger paramMessenger, Context paramContext, String paramString1, String paramString2, String paramString3)
  {
    updateTexture(paramMessenger, paramContext, paramString1, paramString2, paramString3, false, false);
  }

  public static void updateTexture(Messenger paramMessenger, Context paramContext, String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramString1 == null)
      updateTexture(paramMessenger, paramString2, paramString3, paramBoolean1, paramBoolean2);
    Bitmap localBitmap;
    do
    {
      return;
      localBitmap = ThemeInfo.getBitmap(paramContext, paramString1, paramString3);
    }
    while (localBitmap == null);
    updateTexture(paramMessenger, paramString2, localBitmap, paramBoolean1, paramBoolean2);
  }

  public static void updateTexture(Messenger paramMessenger, String paramString, Bitmap paramBitmap)
  {
    updateTexture(paramMessenger, paramString, paramBitmap, false, false);
  }

  public static void updateTexture(Messenger paramMessenger, String paramString, Bitmap paramBitmap, boolean paramBoolean1, boolean paramBoolean2)
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("shape_name", paramString);
    if (paramBitmap != null)
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, localByteArrayOutputStream);
      localBundle.putByteArray("bitmap_stream", localByteArrayOutputStream.toByteArray());
      Message localMessage = Message.obtain(null, 4);
      localMessage.setData(localBundle);
      CircleWidget3DProvider.sendMessage(paramMessenger, localMessage);
      return;
    }
    Log.e("Circle", "No Bitmap for texture");
  }

  public static void updateTexture(Messenger paramMessenger, String paramString1, String paramString2)
  {
    updateTexture(paramMessenger, paramString1, paramString2, false, false);
  }

  public static void updateTexture(Messenger paramMessenger, String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2)
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("shape_name", paramString1);
    if (paramString2 != null)
    {
      localBundle.putString("resource_id_string", paramString2);
      if (paramBoolean1)
        localBundle.putBoolean("wrap_s", paramBoolean1);
      if (paramBoolean2)
        localBundle.putBoolean("wrap_t", paramBoolean2);
      Message localMessage = Message.obtain(null, 4);
      localMessage.setData(localBundle);
      CircleWidget3DProvider.sendMessage(paramMessenger, localMessage);
      return;
    }
    Log.e("Circle", "No Bitmap or ResourceId for texture");
  }
}

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.Utility
 * JD-Core Version:    0.6.2
 */