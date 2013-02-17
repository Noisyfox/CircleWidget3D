package com.motorola.widget.circlewidget3d;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.motorola.homescreen.common.anim.AnimUtils;
import java.util.Calendar;
import org.json.JSONArray;

public class CircleClock extends Circle
{
  private static final char CHAR_DAY = 'd';
  private static final char CHAR_MONTH = 'M';
  public static String[] CLOCK_HANDS_SHAPES = { "circle_time/hourhand", "circle_time/hourhandshadow", "circle_time/minutehand", "circle_time/minutehandshadow" };
  private static final String FONT_NAME = "fonts/Chulho_Neue_v09.ttf";
  private static CircleClock mInstance;
  ImageView mAlarmImageView;
  private int mAnalogClockState = -1;
  private int mCurrentTextureUnit = 0;
  TextView mDateView;
  private boolean mIsAlarmset;
  private String mNextAlarmTime;
  String mTimeFormat;
  TextView mTimeView;

  private CircleClock(Context paramContext)
  {
    this.mContext = paramContext;
    this.mCurrentId = 0;
    this.mAnalogClockState = -1;
    prepareCircle(2130903046, CircleConsts.CLOCK_BITMAP_SIZE.intValue());
    this.mIsFlipped = retrieveSidePref("clock_side_front");
  }

  private Bitmap getAnalogClock()
  {
    this.mFrontLayout.setVisibility(0);
    this.mBackLayout.setVisibility(8);
    this.mBitmap.eraseColor(0);
    this.mLayout.draw(this.mCanvas);
    return this.mBitmap;
  }

  private Bitmap getDigitalClock()
  {
    Calendar localCalendar = Calendar.getInstance();
    long l = System.currentTimeMillis();
    localCalendar.setTimeInMillis(l);
    this.mTimeView.setText(Utility.getTimeString(this.mContext, localCalendar));
    ImageView localImageView = this.mAlarmImageView;
    if (this.mIsAlarmset);
    for (int i = 2130837537; ; i = 2130837538)
    {
      localImageView.setImageResource(i);
      String str = DateUtils.formatDateTime(this.mContext, l, 524314);
      this.mDateView.setText(str);
      this.mFrontLayout.setVisibility(8);
      this.mBackLayout.setVisibility(0);
      this.mBitmap.eraseColor(0);
      this.mLayout.draw(this.mCanvas);
      return this.mBitmap;
    }
  }

  public static CircleClock getInstance(Context paramContext)
  {
    synchronized (syncObject)
    {
      if (mInstance == null)
        mInstance = new CircleClock(paramContext);
      return mInstance;
    }
  }

  private void retrieveAlarmInfo(Intent paramIntent)
  {
    if (paramIntent.getAction().equals("android.intent.action.ALARM_CHANGED"))
    {
      this.mIsAlarmset = paramIntent.getBooleanExtra("alarmSet", false);
      return;
    }
    getAlarmCondition();
  }

  public boolean getAlarmCondition()
  {
    this.mNextAlarmTime = Settings.System.getString(this.mContext.getContentResolver(), "next_alarm_formatted");
    if ((this.mNextAlarmTime == null) || (TextUtils.isEmpty(this.mNextAlarmTime)));
    for (this.mIsAlarmset = false; ; this.mIsAlarmset = true)
      return this.mIsAlarmset;
  }

  public JSONArray getAnalogClockHandAnimation(Context paramContext, long paramLong)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(System.currentTimeMillis());
    int i = localCalendar.get(12);
    int j = localCalendar.get(11);
    if (j == 12)
      j = 0;
    if (i == 60)
      i = 0;
    JSONArray localJSONArray = new JSONArray();
    float[] arrayOfFloat = new float[3];
    arrayOfFloat[0] = (-(i * 6));
    arrayOfFloat[1] = 0.0F;
    arrayOfFloat[2] = 0.0F;
    localJSONArray.put(AnimUtils.createJSONData3D("circle_time/minutehand", "rotation", null, arrayOfFloat, (float)paramLong, false));
    localJSONArray.put(AnimUtils.createJSONData3D("circle_time/minutehandshadow", "rotation", null, arrayOfFloat, (float)paramLong, false));
    arrayOfFloat[0] = (-(j * 30 + i / 2));
    arrayOfFloat[1] = 0.0F;
    arrayOfFloat[2] = 0.0F;
    localJSONArray.put(AnimUtils.createJSONData3D("circle_time/hourhand", "rotation", null, arrayOfFloat, (float)paramLong, false));
    localJSONArray.put(AnimUtils.createJSONData3D("circle_time/hourhandshadow", "rotation", null, arrayOfFloat, (float)paramLong, false));
    return localJSONArray;
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
    Utility.flipCircle(paramMessenger, "circle_time", paramFloat.floatValue(), this.mIsFlipped);
    if (!this.mIsFlipped);
    for (boolean bool = true; ; bool = false)
    {
      this.mIsFlipped = bool;
      storeSidePref("clock_side_front");
      return false;
    }
  }

  public boolean handleSingleTap(Bundle paramBundle)
  {
    int i = 1;
    if (CircleAlert.isAlertDisplayed())
    {
      String str2 = paramBundle.getString("shape_name");
      int j = CircleAlert.getAlertDisplayedSide();
      if (((j == 0) && (str2.equals("circle_time/circlefront"))) || ((j == 1) && (str2.equals("circle_time/circleback"))))
      {
        CircleAlert.getInstance(this.mContext).handleSingleTap(paramBundle);
        i = 0;
      }
    }
    if (CircleHelp.isHelpDisplayed())
    {
      String str1 = paramBundle.getString("shape_name");
      if (CircleHelp.getTextureSideOfHelpScreen().equals(str1))
      {
        Log.d("Circle", "Not doint anything as help side was clicked");
        i = 0;
      }
    }
    if (i != 0)
      startClockApp();
    return true;
  }

  public View prepareCircle(int paramInt1, int paramInt2)
  {
    View localView = super.prepareCircle(paramInt1, paramInt2);
    this.mDateView = ((TextView)localView.findViewById(2131427340));
    this.mTimeView = ((TextView)localView.findViewById(2131427341));
    this.mAlarmImageView = ((ImageView)localView.findViewById(2131427342));
    this.mFrontLayout = localView.findViewById(2131427338);
    this.mBackLayout = localView.findViewById(2131427339);
    this.mTimeFormat = this.mContext.getResources().getString(2131230730);
    Typeface localTypeface = Typeface.createFromAsset(this.mContext.getAssets(), "fonts/Chulho_Neue_v09.ttf");
    if (localTypeface != null)
      this.mTimeView.setTypeface(localTypeface);
    return localView;
  }

  public void setAnalogClockState(int paramInt)
  {
    this.mAnalogClockState = paramInt;
  }

  public void setCurrentTextureUnit(int paramInt)
  {
    this.mCurrentTextureUnit = paramInt;
    int i = 1;
    int j = 1;
    int k;
    if (CircleAlert.isAlertDisplayed())
    {
      k = CircleAlert.getAlertDisplayedSide();
      if (k != 0)
        break label92;
      i = 0;
    }
    while (true)
    {
      if (i != 0)
        Utility.updateTexture(null, "circle_time/circlefront:t" + paramInt, getDigitalClock());
      if (j != 0)
        Utility.updateTexture(null, "circle_time/circleback:t" + paramInt, getAnalogClock());
      return;
      label92: if (k == 1)
        j = 0;
    }
  }

  public void setTheme(String paramString)
  {
    this.mThemePackage = paramString;
    if (paramString.equals("com.motorola.widget.circlewidget3d"))
    {
      this.mFrontLayout.setBackgroundResource(2130837522);
      this.mBackLayout.setBackgroundResource(2130837520);
      this.mDateView.setTextColor(-1);
      this.mTimeView.setTextColor(-1);
      Utility.updateTexture(null, "circle_time/hourhand", "clock_hands");
      Utility.updateTexture(null, "circle_time/minutehand", "clock_hands");
    }
    int i;
    do
    {
      return;
      Drawable localDrawable1 = ThemeInfo.getDrawable(this.mContext, paramString, "clock_front");
      Drawable localDrawable2 = ThemeInfo.getDrawable(this.mContext, paramString, "clock_back");
      Drawable localDrawable3 = ThemeInfo.getDrawable(this.mContext, paramString, "clock_hands");
      if (localDrawable1 != null)
        this.mBackLayout.setBackgroundDrawable(localDrawable1);
      if (localDrawable2 != null)
        this.mFrontLayout.setBackgroundDrawable(localDrawable2);
      if (localDrawable3 != null)
      {
        Bitmap localBitmap = ((BitmapDrawable)localDrawable3).getBitmap();
        Utility.updateTexture(null, "circle_time/hourhand", localBitmap);
        Utility.updateTexture(null, "circle_time/minutehand", localBitmap);
      }
      i = ThemeInfo.getTextColor(this.mContext, paramString, "text_color");
    }
    while (i == -1);
    this.mDateView.setTextColor(i);
    this.mTimeView.setTextColor(i);
  }

  public void startClockApp()
  {
    Intent localIntent = new Intent("android.intent.action.MAIN");
    localIntent.addCategory("android.intent.category.LAUNCHER");
    Resources localResources = this.mContext.getResources();
    localIntent.setComponent(new ComponentName(localResources.getString(2131230724), localResources.getString(2131230725)));
    localIntent.setFlags(337641472);
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
    int i;
    int k;
    int m;
    int n;
    int i1;
    int i2;
    label56: String str;
    if (this.mIsAlarmset)
    {
      i = 1;
      int j = this.mAnalogClockState;
      k = 0;
      if (i != j)
      {
        k = 1;
        this.mAnalogClockState = i;
      }
      m = 1;
      n = 1;
      i1 = 1;
      if (CircleAlert.isAlertDisplayed())
      {
        i2 = CircleAlert.getAlertDisplayedSide();
        if (i2 != 0)
          break label188;
        m = 0;
      }
      if (CircleHelp.isHelpDisplayed())
      {
        str = CircleHelp.getTextureSideOfHelpScreen();
        if (str != null)
        {
          if (!str.equals("circle_time/circlefront"))
            break label203;
          m = 0;
        }
      }
    }
    while (true)
    {
      if (m != 0)
        Utility.updateTexture(null, "circle_time/circlefront:t" + this.mCurrentTextureUnit, getDigitalClock());
      if ((n != 0) && (k != 0))
        Utility.updateTexture(null, "circle_time/circleback:t" + this.mCurrentTextureUnit, getAnalogClock());
      if (i1 != 0)
        Utility.updatAnimation(null, getAnalogClockHandAnimation(this.mContext, 500L));
      return;
      i = 0;
      break;
      label188: if (i2 != 1)
        break label56;
      i1 = 0;
      n = 0;
      break label56;
      label203: if (str.equals("circle_time/circleback"))
      {
        i1 = 0;
        n = 0;
      }
    }
  }

  public void updateCircle(Context paramContext, Intent paramIntent)
  {
    retrieveAlarmInfo(paramIntent);
    updateCircle();
  }

  public void updateValues(Context paramContext, Intent paramIntent)
  {
    retrieveAlarmInfo(paramIntent);
    this.mRefreshNeeded = true;
  }
}

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.CircleClock
 * JD-Core Version:    0.6.2
 */