package com.motorola.widget.circlewidget3d;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class CircleBattery extends Circle
{
  public static final long BATTERY_LEVEL_ANIM_DURATION = 800L;
  public static final String BATTERY_LEVEL_GRAY_ID = "battery_level_gray";
  public static final String BATTERY_LEVEL_GREEN_ID = "battery_level_green";
  public static final int BATTERY_LEVEL_ORANGE = 15;
  public static final String BATTERY_LEVEL_ORANGE_ID = "battery_level_orange";
  public static final int BATTERY_LEVEL_RED = 5;
  public static final String BATTERY_LEVEL_RED_ID = "battery_level_red";
  public static final int THRESOLD_ID_GRAY = 4;
  public static final int THRESOLD_ID_GREEN = 1;
  public static final int THRESOLD_ID_ORANGE = 2;
  public static final int THRESOLD_ID_RED = 3;
  private static CircleBattery mInstance;
  int mBatteryLevel;
  ImageView mBatteryStatusImage;
  int mChargeInfo;
  TextView mLevelText;
  int mThresold;

  private CircleBattery(Context paramContext)
  {
    this.mCurrentId = 0;
    this.mContext = paramContext;
    prepareCircle(2130903042, CircleConsts.BATTERY_BITMAP_SIZE.intValue());
    try
    {
      Intent localIntent = paramContext.getApplicationContext().registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
      this.mBatteryLevel = localIntent.getIntExtra("level", -1);
      this.mChargeInfo = localIntent.getIntExtra("plugged", 0);
      this.mThresold = getEquivalentThresold(this.mBatteryLevel);
      this.mIsFlipped = retrieveSidePref("battery_side_front");
      return;
    }
    catch (Exception localException)
    {
      while (true)
        Log.e("Circle", "registration for battery failed");
    }
  }

  public static CircleBattery getInstance(Context paramContext)
  {
    synchronized (syncObject)
    {
      if (mInstance == null)
        mInstance = new CircleBattery(paramContext);
      return mInstance;
    }
  }

  private Bitmap getPercentageScreen()
  {
    this.mLevelText.setVisibility(0);
    TextView localTextView = this.mLevelText;
    String str = this.mContext.getString(2131230767);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(this.mBatteryLevel);
    localTextView.setText(String.format(str, arrayOfObject));
    int i = 2130837547;
    if ((this.mChargeInfo == 0) && (this.mBatteryLevel <= 5))
      i = 2130837549;
    while (true)
    {
      this.mBatteryStatusImage.setImageResource(i);
      this.mFrontLayout.setVisibility(0);
      this.mBackLayout.setVisibility(8);
      this.mBitmap.eraseColor(0);
      this.mLayout.draw(this.mCanvas);
      return this.mBitmap;
      if (this.mChargeInfo != 0)
        i = 2130837548;
    }
  }

  public Bitmap getBackTexture(Bundle paramBundle)
  {
    return null;
  }

  public int getEquivalentThresold(int paramInt)
  {
    if (paramInt > 15)
      return 1;
    if ((paramInt <= 15) && (paramInt > 5))
      return 2;
    return 3;
  }

  public boolean getFlipValue()
  {
    return this.mIsFlipped;
  }

  public Bitmap getFrontTexture(Bundle paramBundle)
  {
    return null;
  }

  public String getLevelIdString()
  {
    String str = "battery_level_green";
    if (this.mBatteryLevel <= 5)
      str = "battery_level_red";
    while (this.mBatteryLevel > 15)
      return str;
    return "battery_level_orange";
  }

  public float getLevelTranslation()
  {
    return 0.25F * (100 - this.mBatteryLevel) / 50.0F;
  }

  public Bitmap getSettingScreen()
  {
    this.mFrontLayout.setVisibility(8);
    this.mBackLayout.setVisibility(0);
    this.mBitmap.eraseColor(0);
    this.mLayout.draw(this.mCanvas);
    return this.mBitmap;
  }

  public void handleAnimationComplete()
  {
    Utility.moveTexture(null, "circle_battery/level", 0, getLevelTranslation(), 0.0F, 0.0F, 800L);
  }

  public void handleDestroy()
  {
    super.handleDestroy();
    mInstance = null;
  }

  public boolean handleFling(Messenger paramMessenger, Message paramMessage, Float paramFloat)
  {
    if ((CircleData.isDataCircleDisplayed()) || (shouldDisplayDataCircle()))
    {
      CircleData.getInstance(this.mContext).handleFling(paramMessenger, paramMessage, paramFloat);
      return false;
    }
    int i = 1;
    int j = 1;
    if ((CircleData.isDataCircleEnable()) && ((CircleData.isHasCachedData()) || (CircleData.isHasErrorData()) || ((Config.isDeviceDataSupported()) && (CircleData.isSetupScreenDisplayed()))))
    {
      if (this.mIsFlipped)
        break label222;
      if (paramFloat.floatValue() < 0.0F)
      {
        CircleData.getInstance(this.mContext).updateCircle();
        i = 0;
      }
    }
    label94: float f2;
    label172: long l;
    if (i != 0)
    {
      Utility.flipCircle(paramMessenger, "circle_battery", paramFloat.floatValue(), this.mIsFlipped);
      float f1 = getLevelTranslation();
      if ((CircleData.isDataCircleDisplayed()) && ((CircleData.isHasCachedData()) || (CircleData.isHasErrorData())))
        f1 = CircleData.getInstance(this.mContext).getDataUsageTranslation();
      if (j != 0)
      {
        Messenger localMessenger = paramMessage.replyTo;
        if (!this.mIsFlipped)
          break label254;
        f2 = f1;
        if (!this.mIsFlipped)
          break label262;
        l = 800L;
        label184: Utility.moveTexture(localMessenger, "circle_battery/level", 0, f2, 0.0F, 0.0F, l);
      }
      if (this.mIsFlipped)
        break label268;
    }
    label262: label268: for (boolean bool = true; ; bool = false)
    {
      this.mIsFlipped = bool;
      storeSidePref("battery_side_front");
      return false;
      label222: if (paramFloat.floatValue() < 0.0F)
        updateCircle();
      while (true)
      {
        j = 0;
        break label94;
        break;
        CircleData.getInstance(this.mContext).updateCircle();
      }
      label254: f2 = 0.5F;
      break label172;
      l = 0L;
      break label184;
    }
  }

  public boolean handleSingleTap(Bundle paramBundle)
  {
    if ((shouldDisplayDataCircle()) || (CircleData.isDataCircleDisplayed()))
    {
      CircleData.getInstance(this.mContext).handleSingleTap(paramBundle);
      return false;
    }
    if (!this.mIsFlipped)
    {
      startBatteryApp();
      return false;
    }
    Utility.startCircleSettings(this.mContext);
    return false;
  }

  public boolean isDeviceDataValid()
  {
    return (Config.isDeviceDataSupported()) && (CircleData.isDataCircleEnable());
  }

  public void moveTexture()
  {
    Utility.moveTexture(null, "circle_battery/level", "circle_battery", 0, 0.5F, 0.0F, 0.0F, 1L);
  }

  public View prepareCircle(int paramInt1, int paramInt2)
  {
    View localView = super.prepareCircle(paramInt1, paramInt2);
    this.mLevelText = ((TextView)localView.findViewById(2131427336));
    this.mFrontLayout = localView.findViewById(2131427334);
    this.mBackLayout = localView.findViewById(2131427335);
    this.mBatteryStatusImage = ((ImageView)localView.findViewById(2131427337));
    return localView;
  }

  public void setFlipValue(boolean paramBoolean)
  {
    this.mIsFlipped = paramBoolean;
  }

  public void setTheme(String paramString)
  {
  }

  public boolean shouldDisplayDataCircle()
  {
    return (CircleWidget3DProvider.isDataServiceAvail()) && (CircleData.isDataCircleEnable()) && (CircleData.isDataCircleDisplayed()) && ((CircleData.isHasCachedData()) || (CircleData.isHasErrorData()));
  }

  public void startBatteryApp()
  {
    Intent localIntent = new Intent();
    localIntent.setFlags(346030080);
    localIntent.setAction("android.intent.action.POWER_USAGE_SUMMARY");
    try
    {
      this.mContext.startActivity(localIntent);
      return;
    }
    catch (Exception localException)
    {
      Log.e("Circle", "Couldn't start Clock activity" + localException);
    }
  }

  public void updateCircle()
  {
    if (shouldDisplayDataCircle())
      return;
    Utility.updateTexture(null, "circle_battery/circlefront", getPercentageScreen());
    Utility.updateTexture(null, "circle_battery/circleback", getSettingScreen());
    final int i = getEquivalentThresold(this.mBatteryLevel);
    if ((i != this.mThresold) || (CircleData.isFrontSideIsDataCircle()))
    {
      new Thread(new Runnable()
      {
        public void run()
        {
          try
          {
            CircleBattery.this.mThresold = i;
            Utility.updateBatteryLevelTexture(i);
            Thread.sleep(30L);
            CircleBattery.this.moveTexture();
            return;
          }
          catch (InterruptedException localInterruptedException)
          {
            localInterruptedException.printStackTrace();
          }
        }
      }).start();
      return;
    }
    moveTexture();
  }

  public void updateCircle(Context paramContext, Intent paramIntent)
  {
    int i;
    int j;
    if (paramIntent != null)
    {
      i = paramIntent.getIntExtra("level", -1);
      j = paramIntent.getIntExtra("plugged", 0);
      if ((i != this.mBatteryLevel) || (j != this.mChargeInfo))
        break label39;
    }
    label39: 
    do
    {
      return;
      this.mBatteryLevel = i;
      this.mChargeInfo = j;
    }
    while (shouldDisplayDataCircle());
    updateCircle();
  }

  public void updateValues(Context paramContext, Intent paramIntent)
  {
    int i = paramIntent.getIntExtra("level", -1);
    int j = paramIntent.getIntExtra("plugged", 0);
    if ((i != this.mBatteryLevel) || (j != this.mChargeInfo))
    {
      this.mBatteryLevel = i;
      this.mChargeInfo = j;
      this.mRefreshNeeded = true;
    }
  }
}

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.CircleBattery
 * JD-Core Version:    0.6.2
 */