package com.motorola.widget.circlewidget3d;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.motorola.datameter.proxyservice.IDataMeter;
import com.motorola.datameter.proxyservice.IDataMeter.Stub;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

public class CircleData extends Circle
{
  public static final String ACTION_DATA_SETUP_COMPLETE = "com.motorola.datameter.ACTION_DATA_USAGE_SETTINGS_COMPLETE";
  public static final String ACTION_SHOW_DATA_USAGE_SETTINGS = "com.motorola.datameter.ACTION_SHOW_DATA_USAGE_SETTINGS";
  public static final long DATA_CONTENT_DELAY = 1600L;
  public static final int DIVIDE_NUMBER = 1000;
  public static final String EXTRA_DATA_USAGE_DEVICE_CONFIG = "config";
  public static final int GB = 2;
  public static final int INVALID_DATA = -1000;
  public static final int KB = 0;
  public static final int MB = 1;
  public static final String PROXY_SERVICE_COMPONENT = "com.motorola.datameter.proxyservice.DataMeterProxyService";
  public static final String PROXY_SERVICE_PKG = "com.motorola.datameter.proxyservice";
  private static boolean mDataError = false;
  private static CircleData mInstance;
  static ConnectivityManager sConnectivityMgr;
  private static Method sGetMobileDataEnabledMethod;
  private static boolean sHasCachedData;
  private static boolean sIsDataCircleDisplayed;
  private static boolean sIsDataCircleEnabled;
  private static boolean sIsFrontSideIsDataCircle;
  static boolean sIsSetupScreenDisplayed = false;
  static boolean sMobileDataEnabled;
  DataInfo mCurrentUsage = null;
  TextView mCurrentUsageView;
  int mDataLeftInPercentage;
  boolean mDataTestMode = false;
  private ServiceConnection mDeviceDataConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      CircleData.this.mIsDeviceDataServiceBound = true;
      CircleData.access$002(CircleData.this, IDataMeter.Stub.asInterface(paramAnonymousIBinder));
    }

    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
      CircleData.this.mIsDeviceDataServiceBound = false;
      CircleData.access$002(CircleData.this, null);
    }
  };
  private IDataMeter mDeviceDataService = null;
  TextView mErrorMaxLimitView;
  int mGreenMaxLevel = 65;
  boolean mIsDeviceDataServiceBound = false;
  View mLayoutDataOff;
  View mLayoutSetup;
  View mLayoutUsage;
  View mLineView;
  DataInfo mMaxLimit = null;
  TextView mMaxLimitView;
  int mOrangeMaxLevel = 90;
  int mPlanType;
  int mPrevThresold;

  private CircleData(Context paramContext)
  {
    this.mCurrentId = 0;
    this.mContext = paramContext;
    prepareCircle(2130903048, CircleConsts.BATTERY_BITMAP_SIZE.intValue());
    sIsDataCircleEnabled = retrieveDataPref("show_mobile_data");
    sIsFrontSideIsDataCircle = retrieveDataPref("front_side_data_circle");
    boolean bool1 = sIsDataCircleEnabled;
    boolean bool2 = false;
    if (bool1)
    {
      boolean bool3 = sIsFrontSideIsDataCircle;
      bool2 = false;
      if (bool3)
        bool2 = true;
    }
    sIsDataCircleDisplayed = bool2;
    this.mIsFlipped = retrieveSidePref("battery_side_front");
    startDataService();
    retrieveDataInfo();
    initGetDataEnableMethod();
    if (Config.isDeviceDataSupported())
      bindDeviceDataService(this.mContext);
  }

  private DataInfo convertDataInfo(Context paramContext, DataInfo paramDataInfo, int paramInt)
  {
    if (paramInt != paramDataInfo.unit)
    {
      if (paramInt != 2)
        break label118;
      if (paramDataInfo.unit != 0)
        break label90;
      paramDataInfo.data = Float.valueOf(paramDataInfo.data.floatValue() / 1000000.0F);
      paramDataInfo.unit = 2;
    }
    label90: label118: 
    while (paramInt != 1)
      while (true)
      {
        paramDataInfo.unitStr = paramContext.getResources().getStringArray(2131099648)[paramDataInfo.unit];
        if (paramDataInfo.data.floatValue() >= 0.1D)
          break;
        paramDataInfo.dataStr = paramContext.getResources().getString(2131230720);
        return paramDataInfo;
        if (paramDataInfo.unit == 1)
          paramDataInfo.data = Float.valueOf(paramDataInfo.data.floatValue() / 1000.0F);
      }
    if (paramDataInfo.unit == 0)
      paramDataInfo.data = Float.valueOf(paramDataInfo.data.floatValue() / 1000.0F);
    while (true)
    {
      paramDataInfo.unit = 1;
      break;
      if (paramDataInfo.unit != 2);
    }
    paramDataInfo.dataStr = getFormattedLimitStr(paramDataInfo.data, paramDataInfo.unit);
    return paramDataInfo;
  }

  private Bitmap getCorrectDataScreen()
  {
    if (sMobileDataEnabled)
    {
      if (!hasCachedData())
      {
        Utility.changeVisibility("circle_battery/level", false);
        sIsSetupScreenDisplayed = true;
        return getDataSetupScreen();
      }
      return getDataScreen();
    }
    return getDataOffScreen();
  }

  private Bitmap getDataErrorScreen()
  {
    this.mFrontLayout.setVisibility(8);
    this.mBackLayout.setVisibility(0);
    this.mLayoutDataOff.setVisibility(8);
    this.mBitmap.eraseColor(0);
    if (this.mMaxLimit != null)
      if (this.mMaxLimit.data.floatValue() != -1.0F)
        break label90;
    label90: for (String str = this.mCurrentUsage.unitStr; ; str = this.mMaxLimit.unitStr)
    {
      this.mErrorMaxLimitView.setText(str);
      this.mLayout.draw(this.mCanvas);
      return this.mBitmap;
    }
  }

  private DataInfo getDataInfo(Context paramContext, Float paramFloat)
  {
    DataInfo localDataInfo = new DataInfo();
    if (paramFloat.floatValue() != -1.0F)
    {
      paramFloat = Float.valueOf(paramFloat.floatValue() / 1000.0F);
      localDataInfo.unit = 0;
      if (paramFloat.floatValue() >= 1000.0F)
      {
        localDataInfo.unit = 1;
        paramFloat = Float.valueOf(paramFloat.floatValue() / 1000.0F);
        if (paramFloat.floatValue() >= 1000.0F)
        {
          localDataInfo.unit = 2;
          paramFloat = Float.valueOf(paramFloat.floatValue() / 1000.0F);
        }
      }
    }
    localDataInfo.unitStr = paramContext.getResources().getStringArray(2131099648)[localDataInfo.unit];
    localDataInfo.data = paramFloat;
    if (localDataInfo.data.floatValue() != -1.0F)
      localDataInfo.dataStr = getFormattedLimitStr(localDataInfo.data, localDataInfo.unit);
    return localDataInfo;
  }

  private Bitmap getDataOffScreen()
  {
    this.mLayoutUsage.setVisibility(8);
    this.mLayoutSetup.setVisibility(8);
    this.mLayoutDataOff.setVisibility(0);
    this.mFrontLayout.setVisibility(0);
    this.mBackLayout.setVisibility(8);
    this.mBitmap.eraseColor(0);
    this.mLayout.draw(this.mCanvas);
    return this.mBitmap;
  }

  private Bitmap getDataScreen()
  {
    this.mLayoutUsage.setVisibility(0);
    this.mLayoutSetup.setVisibility(8);
    this.mLayoutDataOff.setVisibility(8);
    if (hasCachedData())
    {
      this.mCurrentUsageView.setText(this.mCurrentUsage.dataStr);
      String str;
      if (this.mMaxLimit.data.floatValue() == -1.0F)
      {
        str = this.mCurrentUsage.unitStr;
        this.mMaxLimitView.setText(str);
        if (this.mMaxLimit.data.floatValue() != -1.0F)
          break label186;
        this.mLineView.setVisibility(4);
      }
      while (true)
      {
        this.mFrontLayout.setVisibility(0);
        this.mBackLayout.setVisibility(8);
        this.mBitmap.eraseColor(0);
        this.mLayout.draw(this.mCanvas);
        return this.mBitmap;
        str = this.mMaxLimit.dataStr + " " + this.mMaxLimit.unitStr;
        break;
        label186: this.mLineView.setVisibility(0);
      }
    }
    return getDataErrorScreen();
  }

  private Bitmap getDataSetupScreen()
  {
    this.mLayoutUsage.setVisibility(8);
    this.mLayoutSetup.setVisibility(0);
    this.mLayoutDataOff.setVisibility(8);
    this.mFrontLayout.setVisibility(0);
    this.mBackLayout.setVisibility(8);
    this.mBitmap.eraseColor(0);
    this.mLayout.draw(this.mCanvas);
    return this.mBitmap;
  }

  private String getFormattedLimitStr(Float paramFloat, int paramInt)
  {
    if ((paramFloat.floatValue() >= 100.0F) || (paramInt == 1))
      return Integer.toString(paramFloat.intValue());
    return new DecimalFormat("#.##").format(paramFloat);
  }

  public static CircleData getInstance(Context paramContext)
  {
    synchronized (syncObject)
    {
      if (mInstance == null)
        mInstance = new CircleData(paramContext);
      return mInstance;
    }
  }

  private int getNewThresold()
  {
    int i = 100 - this.mDataLeftInPercentage;
    int j;
    if (i > this.mOrangeMaxLevel)
      j = 3;
    while (true)
    {
      if (mDataError)
        j = 4;
      return j;
      if ((i > this.mGreenMaxLevel) && (i <= this.mOrangeMaxLevel))
        j = 2;
      else
        j = 1;
    }
  }

  private void initGetDataEnableMethod()
  {
    try
    {
      sConnectivityMgr = (ConnectivityManager)this.mContext.getSystemService("connectivity");
      sGetMobileDataEnabledMethod = sConnectivityMgr.getClass().getMethod("getMobileDataEnabled", new Class[0]);
      sMobileDataEnabled = isMobileDataEnabled();
      return;
    }
    catch (Exception localException)
    {
      Log.e("Circle", "InitgetDaataEnableMethod exception " + localException);
    }
  }

  public static boolean isDataCircleDisplayed()
  {
    return sIsDataCircleDisplayed;
  }

  public static boolean isDataCircleEnable()
  {
    return sIsDataCircleEnabled;
  }

  public static boolean isFrontSideIsDataCircle()
  {
    return sIsFrontSideIsDataCircle;
  }

  public static boolean isHasCachedData()
  {
    return sHasCachedData;
  }

  public static boolean isHasErrorData()
  {
    return mDataError;
  }

  public static boolean isMobileDataEnabled()
  {
    boolean bool1 = true;
    if (sGetMobileDataEnabledMethod != null);
    try
    {
      boolean bool2 = ((Boolean)sGetMobileDataEnabledMethod.invoke(sConnectivityMgr, new Object[0])).booleanValue();
      bool1 = bool2;
      return bool1;
    }
    catch (Exception localException)
    {
      Log.e("Circle", "error in isMobileDataEnable function " + localException);
    }
    return bool1;
  }

  public static boolean isSetupScreenDisplayed()
  {
    return sIsSetupScreenDisplayed;
  }

  private void removeFractionDataFromString(DataInfo paramDataInfo)
  {
    if (paramDataInfo.data.floatValue() != -1.0F)
      paramDataInfo.dataStr = getFormattedLimitStr(Float.valueOf(paramDataInfo.data.intValue()), paramDataInfo.unit);
  }

  private void retrieveDataInfo()
  {
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
    float f1 = localSharedPreferences.getFloat("EXTRA_MAX_DATA", -1000.0F);
    float f2 = localSharedPreferences.getFloat("EXTRA_USED_DATA", -1000.0F);
    if ((f1 != -1000.0F) && (f2 != -1000.0F))
    {
      this.mMaxLimit = getDataInfo(this.mContext, Float.valueOf(f1));
      removeFractionDataFromString(this.mMaxLimit);
      this.mCurrentUsage = getDataInfo(this.mContext, Float.valueOf(f2));
      this.mCurrentUsage = convertDataInfo(this.mContext, this.mCurrentUsage, this.mMaxLimit.unit);
      this.mDataLeftInPercentage = generateDataLeftInPercentage(this.mCurrentUsage, this.mMaxLimit);
      sHasCachedData = true;
      return;
    }
    Log.e("Circle", "no cached data for data circle");
    sHasCachedData = false;
  }

  private boolean retrieveDataPref(String paramString)
  {
    return PreferenceManager.getDefaultSharedPreferences(this.mContext).getBoolean(paramString, true);
  }

  private void startDataService()
  {
    Intent localIntent = new Intent("com.motorola.datameter.ACTION_START_DATA_METER_SERVICE");
    localIntent.putExtra("EXTRA_CLIENT_NAME", this.mContext.getPackageName());
    try
    {
      this.mContext.sendBroadcast(localIntent);
      return;
    }
    catch (Exception localException)
    {
      Log.e("Circle", "Couldn't start Data service" + localException);
    }
  }

  private void storeDataInfo(float paramFloat1, float paramFloat2)
  {
    SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
    localEditor.putFloat("EXTRA_MAX_DATA", paramFloat1);
    localEditor.putFloat("EXTRA_USED_DATA", paramFloat2);
    localEditor.apply();
  }

  public void bindDeviceDataService(Context paramContext)
  {
    if (this.mIsDeviceDataServiceBound)
    {
      Log.e("Circle", "Device data service is already bound so return");
      return;
    }
    Intent localIntent = new Intent();
    localIntent.setComponent(new ComponentName("com.motorola.datameter.proxyservice", "com.motorola.datameter.proxyservice.DataMeterProxyService"));
    try
    {
      paramContext.bindService(localIntent, this.mDeviceDataConnection, 1);
      return;
    }
    catch (SecurityException localSecurityException)
    {
      this.mIsDeviceDataServiceBound = false;
    }
  }

  public void changeBatteryLevelTransition(final float paramFloat, final int paramInt)
  {
    new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          Utility.updateBatteryLevelTexture(paramInt);
          Thread.sleep(200L);
          Utility.moveTexture(null, "circle_battery/level", 0, 0.5F, 0.0F, 0.0F, 1L);
          if (!CircleData.this.mIsFlipped)
          {
            Thread.sleep(30L);
            Utility.moveTexture(null, "circle_battery/level", 0, paramFloat, 0.0F, 0.0F, 800L);
          }
          return;
        }
        catch (InterruptedException localInterruptedException)
        {
          localInterruptedException.printStackTrace();
        }
      }
    }).start();
  }

  public void checkIfDataStateChanged()
  {
    boolean bool = isMobileDataEnabled();
    if (sMobileDataEnabled != bool)
    {
      sMobileDataEnabled = bool;
      if (sIsDataCircleDisplayed)
        updateCircle();
    }
  }

  public void fetchDataFromService()
  {
    Intent localIntent = new Intent("com.motorola.datameter.ACTION_FETCH_DATA_USAGE");
    try
    {
      this.mContext.sendBroadcast(localIntent);
      return;
    }
    catch (Exception localException)
    {
      Log.e("Circle", "Couldn't send featch data msg to service" + localException);
    }
  }

  public int generateDataLeftInPercentage(DataInfo paramDataInfo1, DataInfo paramDataInfo2)
  {
    int i;
    if (paramDataInfo2.data.floatValue() == -1.0F)
      i = 100;
    do
    {
      return i;
      if (paramDataInfo1.unit != paramDataInfo2.unit)
        paramDataInfo1 = convertDataInfo(this.mContext, paramDataInfo1, paramDataInfo2.unit);
      i = 100 - (int)(100.0F * paramDataInfo1.data.floatValue() / paramDataInfo2.data.floatValue());
    }
    while (i >= 0);
    return 0;
  }

  public Bitmap getBackTexture(Bundle paramBundle)
  {
    return null;
  }

  public float getDataUsageTranslation()
  {
    return 0.25F * (100 - this.mDataLeftInPercentage) / 50.0F;
  }

  public Bitmap getFrontTexture(Bundle paramBundle)
  {
    return null;
  }

  public void handleDestroy()
  {
    unbindDeviceDataService(this.mContext);
    super.handleDestroy();
    mInstance = null;
  }

  public boolean handleFling(Messenger paramMessenger, Message paramMessage, Float paramFloat)
  {
    boolean bool = true;
    int i = 1;
    CircleBattery localCircleBattery = CircleBattery.getInstance(this.mContext);
    this.mIsFlipped = localCircleBattery.getFlipValue();
    if (((this.mIsFlipped) && (paramFloat.floatValue() < 0.0F)) || ((!this.mIsFlipped) && (paramFloat.floatValue() > 0.0F)))
    {
      i = 0;
      sIsDataCircleDisplayed = false;
      if (sIsSetupScreenDisplayed)
        Utility.changeVisibility("circle_battery/level", bool);
      localCircleBattery.updateCircle();
      storeDataCircleFronSidePref(false);
    }
    if (i != 0)
    {
      Utility.flipCircle(paramMessenger, "circle_battery", paramFloat.floatValue(), this.mIsFlipped);
      updateDataLevelBg();
      if (this.mIsFlipped)
        break label140;
    }
    while (true)
    {
      this.mIsFlipped = bool;
      storeSidePref("battery_side_front");
      localCircleBattery.setFlipped(this.mIsFlipped);
      return false;
      label140: bool = false;
    }
  }

  public boolean handleSingleTap(Bundle paramBundle)
  {
    this.mIsFlipped = CircleBattery.getInstance(this.mContext).getFlipValue();
    if (!this.mIsFlipped)
      startDataUsageApp();
    while (true)
    {
      return false;
      Utility.startCircleSettings(this.mContext);
    }
  }

  public boolean hasCachedData()
  {
    return (this.mCurrentUsage != null) && (this.mMaxLimit != null);
  }

  public void populateData()
  {
    if ((sIsDataCircleEnabled) && (sIsFrontSideIsDataCircle))
      updateCircle();
    do
    {
      do
        return;
      while ((!sIsDataCircleEnabled) || (getNewThresold() == this.mPrevThresold));
      sIsFrontSideIsDataCircle = true;
      updateCircle();
      this.mIsFlipped = CircleBattery.getInstance(this.mContext).getFlipValue();
    }
    while (!this.mIsFlipped);
    Utility.flipCircle(null, "circle_battery", 300.0F, false);
  }

  public View prepareCircle(int paramInt1, int paramInt2)
  {
    View localView = super.prepareCircle(paramInt1, paramInt2);
    this.mFrontLayout = localView.findViewById(2131427344);
    this.mBackLayout = localView.findViewById(2131427345);
    this.mCurrentUsageView = ((TextView)localView.findViewById(2131427353));
    this.mMaxLimitView = ((TextView)localView.findViewById(2131427355));
    this.mLineView = localView.findViewById(2131427354);
    this.mErrorMaxLimitView = ((TextView)localView.findViewById(2131427348));
    this.mLayoutSetup = localView.findViewById(2131427349);
    this.mLayoutUsage = localView.findViewById(2131427352);
    this.mLayoutDataOff = localView.findViewById(2131427356);
    return localView;
  }

  public void retrieveDeviceDataValues(Context paramContext)
  {
    if (this.mDeviceDataService != null);
    while (true)
    {
      long l1;
      long l2;
      try
      {
        l1 = this.mDeviceDataService.getPolicyLimitBytes();
        if (l1 != -1000L)
        {
          this.mMaxLimit = getDataInfo(paramContext, Float.valueOf((float)l1));
          removeFractionDataFromString(this.mMaxLimit);
        }
        l2 = this.mDeviceDataService.getCurrentUsageBytes();
        if (l2 == -1000L)
          break label223;
        this.mCurrentUsage = getDataInfo(paramContext, Float.valueOf((float)l2));
        this.mCurrentUsage = convertDataInfo(this.mContext, this.mCurrentUsage, this.mMaxLimit.unit);
        break label223;
        Log.e("Circle", "Data circle got no data from Device dataService");
        return;
        this.mDataLeftInPercentage = generateDataLeftInPercentage(this.mCurrentUsage, this.mMaxLimit);
        this.mGreenMaxLevel = 65;
        this.mOrangeMaxLevel = 90;
        storeDataInfo((float)l1, (float)l2);
        sHasCachedData = true;
        sIsSetupScreenDisplayed = false;
        mDataError = false;
        Utility.changeVisibility("circle_battery/level", true);
        populateData();
        return;
      }
      catch (Exception localException)
      {
        Log.e("Circle", "Exception while fetching getPolicyLimitBytes " + localException);
        return;
      }
      Log.e("Circle", "Device data Service is null");
      return;
      label223: if (l1 != -1000L)
        if (l2 != -1000L);
    }
  }

  public void setTheme(String paramString)
  {
  }

  public void showDummyDataScreen()
  {
    sIsDataCircleEnabled = true;
    sIsFrontSideIsDataCircle = true;
    sIsFrontSideIsDataCircle = true;
    sHasCachedData = true;
    mDataError = false;
    if (2.0E+009F != -1000.0F)
      this.mMaxLimit = getDataInfo(this.mContext, Float.valueOf(2.0E+009F));
    removeFractionDataFromString(this.mMaxLimit);
    this.mCurrentUsage = getDataInfo(this.mContext, Float.valueOf(1.03E+009F));
    this.mCurrentUsage = convertDataInfo(this.mContext, this.mCurrentUsage, this.mMaxLimit.unit);
    this.mDataTestMode = true;
    this.mDataLeftInPercentage = generateDataLeftInPercentage(this.mCurrentUsage, this.mMaxLimit);
    this.mGreenMaxLevel = 65;
    this.mOrangeMaxLevel = 90;
    populateData();
  }

  public void startDataUsageApp()
  {
    Intent localIntent = new Intent();
    localIntent.setFlags(346062848);
    if (Config.isDeviceDataSupported())
    {
      localIntent.setAction("com.motorola.datameter.ACTION_SHOW_DATA_USAGE_SETTINGS");
      if (sIsSetupScreenDisplayed)
      {
        localIntent.putExtra("config", true);
        bindDeviceDataService(this.mContext);
      }
    }
    try
    {
      while (true)
      {
        this.mContext.startActivity(localIntent);
        return;
        localIntent.setAction("com.motorola.datameter.ACTION_SHOW_DATA_USAGE");
      }
    }
    catch (Exception localException)
    {
      Log.e("Circle", "Couldn't start Data App" + localException);
    }
  }

  public void stopDataService()
  {
    Intent localIntent = new Intent("com.motorola.datameter.ACTION_STOP_DATA_METER_SERVICE");
    localIntent.putExtra("EXTRA_CLIENT_NAME", this.mContext.getPackageName());
    try
    {
      this.mContext.sendBroadcast(localIntent);
      return;
    }
    catch (Exception localException)
    {
      Log.e("Circle", "Couldn't stop Data service" + localException);
    }
  }

  public void storeDataCircleFronSidePref(boolean paramBoolean)
  {
    SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
    localEditor.putBoolean("front_side_data_circle", paramBoolean);
    localEditor.apply();
    sIsFrontSideIsDataCircle = paramBoolean;
  }

  public void unbindDeviceDataService(Context paramContext)
  {
    if (this.mIsDeviceDataServiceBound)
    {
      paramContext.unbindService(this.mDeviceDataConnection);
      this.mIsDeviceDataServiceBound = false;
    }
    this.mDeviceDataService = null;
  }

  public void updateCircle()
  {
    Bitmap localBitmap;
    if (!mDataError)
    {
      localBitmap = getCorrectDataScreen();
      Utility.updateTexture(null, "circle_battery/circlefront", localBitmap);
      Utility.updateTexture(null, "circle_battery/circleback", CircleBattery.getInstance(this.mContext).getSettingScreen());
      sIsDataCircleDisplayed = true;
      if (!mDataError)
        break label68;
      changeBatteryLevelTransition(0.5F, 4);
    }
    while (true)
    {
      storeDataCircleFronSidePref(true);
      return;
      localBitmap = getDataErrorScreen();
      break;
      label68: updateDataLevelBg();
    }
  }

  public void updateCircle(Context paramContext, Intent paramIntent)
  {
    float f1;
    float f2;
    if (paramIntent != null)
    {
      mDataError = false;
      f1 = paramIntent.getFloatExtra("EXTRA_MAX_DATA", -1000.0F);
      if (f1 != -1000.0F)
      {
        this.mMaxLimit = getDataInfo(paramContext, Float.valueOf(f1));
        removeFractionDataFromString(this.mMaxLimit);
      }
      f2 = paramIntent.getFloatExtra("EXTRA_USED_DATA", -1000.0F);
      if (f2 != -1000.0F)
      {
        this.mCurrentUsage = getDataInfo(paramContext, Float.valueOf(f2));
        this.mCurrentUsage = convertDataInfo(this.mContext, this.mCurrentUsage, this.mMaxLimit.unit);
      }
      if ((f1 != -1000.0F) && (f2 != -1000.0F))
        break label134;
      Log.e("Circle", "Data circle got no data from Service");
    }
    label134: 
    do
    {
      return;
      this.mDataLeftInPercentage = generateDataLeftInPercentage(this.mCurrentUsage, this.mMaxLimit);
      this.mGreenMaxLevel = paramIntent.getIntExtra("EXTRA_GREEN_THRESHOLD", this.mGreenMaxLevel);
      this.mOrangeMaxLevel = paramIntent.getIntExtra("EXTRA_YELLOW_THRESHOLD", this.mOrangeMaxLevel);
      storeDataInfo(f1, f2);
      sHasCachedData = true;
      if (f1 == -1.0F)
      {
        SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        if (!localSharedPreferences.getBoolean("data_setting_changed", false))
        {
          SharedPreferences.Editor localEditor = localSharedPreferences.edit();
          localEditor.putBoolean("show_mobile_data", false);
          localEditor.apply();
          sIsDataCircleEnabled = false;
        }
      }
    }
    while (!isDataCircleEnable());
    populateData();
  }

  public void updateCircleWithError(Context paramContext, Intent paramIntent)
  {
    if (paramIntent != null)
    {
      mDataError = true;
      populateData();
    }
  }

  public void updateDataLevelBg()
  {
    if (!sIsSetupScreenDisplayed)
    {
      int i = getNewThresold();
      changeBatteryLevelTransition(getDataUsageTranslation(), i);
      this.mPrevThresold = i;
    }
  }

  public void updateSettingValues(Intent paramIntent)
  {
    sIsDataCircleEnabled = PreferenceManager.getDefaultSharedPreferences(this.mContext).getBoolean("show_mobile_data", sIsDataCircleEnabled);
  }

  public void updateValues(Context paramContext, Intent paramIntent)
  {
  }

  public static class DataInfo
  {
    Float data;
    String dataStr;
    int unit;
    String unitStr;
  }
}

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.CircleData
 * JD-Core Version:    0.6.2
 */