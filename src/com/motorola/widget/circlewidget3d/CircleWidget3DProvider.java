package com.motorola.widget.circlewidget3d;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Calendar;

public class CircleWidget3DProvider
  extends AppWidgetProvider
{
  static final ArrayList<Messenger> mClients;
  public static BroadcastReceiver mEventReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      String str = paramAnonymousIntent.getAction();
      if ((str.equals("android.intent.action.TIME_TICK")) || (str.equals("android.intent.action.DATE_CHANGED")) || (str.equals("android.intent.action.TIME_SET")) || (str.equals("android.intent.action.ALARM_CHANGED"))) {
        if (CircleWidget3DProvider.mIsScreenOn)
        {
          CircleClock.getInstance(paramAnonymousContext).updateCircle(paramAnonymousContext, paramAnonymousIntent);
          if ((str.equals("android.intent.action.TIME_TICK")) && ((CircleWidget3DProvider.isDataServiceAvail()) || (Config.isDeviceDataSupported()))) {
            CircleData.getInstance(paramAnonymousContext).checkIfDataStateChanged();
          } 
        } 
      } 
      label258:label497:do
      {
        do
        {
          do
          {
            do
            {
              return;
              CircleClock.getInstance(paramAnonymousContext).updateValues(paramAnonymousContext, paramAnonymousIntent);
              break;
              if (str.equals("android.intent.action.BATTERY_CHANGED"))
              {
                if (CircleWidget3DProvider.mIsScreenOn)
                {
                  CircleBattery.getInstance(paramAnonymousContext).updateCircle(paramAnonymousContext, paramAnonymousIntent);
                  return;
                } 
                CircleBattery.getInstance(paramAnonymousContext).updateValues(paramAnonymousContext, paramAnonymousIntent);
                return;
              } 
              if (!str.equals("com.motorola.weather.action.NEW_WEATHER_INFO")) {
                break label165
              } 
              CircleWeather.getInstance(paramAnonymousContext).updateCircle(paramAnonymousContext, paramAnonymousIntent);
            } while (!Config.isDeviceDataSupported());
            CircleData.getInstance(paramAnonymousContext).retrieveDeviceDataValues(paramAnonymousContext);
            return;
            if (str.equals("com.motorola.circle.test.FLING"))
            {
              CircleTest.getInstance(paramAnonymousContext).handleFling(paramAnonymousIntent);
              return;
            } 
            if (str.equals("com.motorola.circle.test.battery"))
            {
              CircleTest.getInstance(paramAnonymousContext).updateBattery(paramAnonymousIntent);
              return;
            } 
            if (str.equals("com.motorola.circle.test.alert"))
            {
              CircleTest.getInstance(paramAnonymousContext).showAlert(paramAnonymousIntent);
              return;
            } 
            if (!str.equals("com.motorola.widget.circlewidget3d.ACTION_SETTING_FINISH")) {
              break label258
            } 
            CircleAlert.getInstance(paramAnonymousContext).updateSettingValues(paramAnonymousIntent);
          } while ((!CircleWidget3DProvider.isDataServiceAvail()) && (!Config.isDeviceDataSupported()));
          CircleData.getInstance(paramAnonymousContext).updateSettingValues(paramAnonymousIntent);
          return;
          if (str.equals("com.motorola.widget.circlewidget3d.ACTION_DISPLAY_HELP_CIRCLE"))
          {
            CircleHelp.getInstance(paramAnonymousContext).updateCircle(paramAnonymousContext, paramAnonymousIntent);
            return;
          } 
          if (str.equals("com.motorola.android.intent.action.ACTION_MWI_CHANGED"))
          {
            AlertVoicemailMoto.getInstance(paramAnonymousContext).addItem(paramAnonymousIntent.getExtras());
            return;
          } 
        } while (str.equals("com.motorola.blur.messaging.MMS_RECEIVED_ACTION"));
        if (str.equals("com.motorola.contracts.messaging.intent.action.NO_MORE_UNSEEN_MESSAGES"))
        {
          AlertMessages.clearMessagingAlert();
          return;
        } 
        if (str.equals("com.motorola.weather.action.TOP_CITY_CHANGED"))
        {
          CircleWeather.getInstance(paramAnonymousContext).handleTopCityChanged(paramAnonymousContext, paramAnonymousIntent);
          return;
        } 
        if (str.equals("android.intent.action.LOCALE_CHANGED"))
        {
          Utility.prepareCircles(CircleWidget3DProvider.CircleService.getServiceContext());
          return;
        } 
        if (str.equals("com.motorola.datameter.ACTION_DATA_METER_USAGE_DATA"))
        {
          CircleData.getInstance(paramAnonymousContext).updateCircle(paramAnonymousContext, paramAnonymousIntent);
          return;
        } 
        if (str.equals("com.motorola.datameter.ACTION_DATA_METER_ERROR"))
        {
          CircleData.getInstance(paramAnonymousContext).updateCircleWithError(paramAnonymousContext, paramAnonymousIntent);
          return;
        } 
        if (str.equals("com.motorola.notification.CLEAR_NOTIFICATION_ACTION"))
        {
          CircleAlert.getInstance(paramAnonymousContext).clearNotification(paramAnonymousContext, paramAnonymousIntent);
          return;
        } 
        if (str.equals("com.motorola.circle.test.data"))
        {
          CircleWidget3DProvider.mIsDataServiceAvail = true;
          CircleData.getInstance(CircleWidget3DProvider.CircleService.getServiceContext()).showDummyDataScreen();
          return;
        } 
        if (str.equals("com.motorola.circle.test.data.level"))
        {
          CircleWidget3DProvider.mIsDataServiceAvail = true;
          CircleTest.getInstance(CircleWidget3DProvider.CircleService.getServiceContext()).updateDataLevels(paramAnonymousIntent);
          return;
        } 
        if (str.equals("android.intent.action.SCREEN_OFF"))
        {
          CircleWidget3DProvider.mIsScreenOn = false;
          return;
        } 
        if (str.equals("android.intent.action.SCREEN_ON"))
        {
          CircleWidget3DProvider.mIsScreenOn = true;
          Utility.refreshCircles();
          return;
        } 
      } while (!str.equals("com.motorola.datameter.ACTION_DATA_USAGE_SETTINGS_COMPLETE"));
      CircleData.getInstance(CircleWidget3DProvider.CircleService.getServiceContext()).retrieveDeviceDataValues(CircleWidget3DProvider.CircleService.getServiceContext());
    } 
  };
  static boolean mIsConfigAvail;
  static boolean mIsDataServiceAvail;
  static boolean mIsScreenOn;
  public static final Object mLock;
  public static BroadcastReceiver mMMSReceiver;
  static final Messenger mMessenger = new Messenger(new HomeMsgHandler());
  
  static
  {
    mClients = new ArrayList();
    mIsDataServiceAvail = false;
    mIsConfigAvail = false;
    mIsScreenOn = true;
    mLock = new Object();
    mMMSReceiver = new BroadcastReceiver()
    {
      public void onReceive(final Context paramAnonymousContext, final Intent paramAnonymousIntent)
      {
        paramAnonymousIntent.getAction();
        new Thread()
        {
          public void run()
          {
            AlertMMS.getInstance(paramAnonymousContext).addItem(paramAnonymousIntent.getExtras());
          } 
        }.start();
      } 
    };
  } 
  
  public static void handleFling(Messenger paramMessenger, Message paramMessage)
  {
    Bundle localBundle = paramMessage.getData();
    String str1 = localBundle.getString("shape_name");
    Float.valueOf(localBundle.getFloat("velocity_x", -1.0F));
    Float localFloat = Float.valueOf(localBundle.getFloat("velocity_y", -1.0F));
    if (Math.abs(localFloat.floatValue()) > 300.0F)
    {
      String str2 = str1.split("/")[0];
      if (str2.equals("circle_battery")) {
        CircleBattery.getInstance(null).handleFling(paramMessenger, paramMessage, localFloat);
      } 
      for (;;)
      {
        CircleCheckin.logAccEventFlipCircle(str2);
        CircleHelp.getInstance(null).hideHelpScreen();
        return;
        if (str2.equals("circle_time")) {
          CircleClock.getInstance(null).handleFling(paramMessenger, paramMessage, localFloat);
        }
        else if (str2.equals("circle_weather")) {
          CircleWeather.getInstance(null).handleFling(paramMessenger, paramMessage, localFloat);
        } 
      } 
    } 
    handleSingleTap(paramMessage);
  } 
  
  public static void handleKeyEvent(Message paramMessage)
  {
    Bundle localBundle = paramMessage.getData();
    localBundle.getString("shape_name");
    KeyEvent localKeyEvent = (KeyEvent)localBundle.getParcelable("key_event");
    if ((localKeyEvent != null) && (localKeyEvent.getAction() == 1))
    {
      int i = localKeyEvent.getKeyCode();
      if ((i == 66) || (i == 23)) {
        Utility.startSelectAppActivity(CircleService.getServiceContext());
      } 
    } 
  } 
  
  public static void handlePODComplete(Message paramMessage)
  {
    String str = paramMessage.getData().getString("anim_id");
    if (str != null)
    {
      if (!str.startsWith("weather_")) {
        break label33
      } 
      CircleWeather.getInstance(null).handleAnimationComplete(str);
    } 
    label39:while (!"circle_battery".equals(str)) {
      return;
    } 
    CircleBattery.getInstance(null).handleAnimationComplete();
  } 
  
  public static void handleSingleTap(Message paramMessage)
  {
    Bundle localBundle = paramMessage.getData();
    String str = localBundle.getString("shape_name");
    Context localContext;
    if (str != null)
    {
      localContext = CircleService.getServiceContext();
      if (!str.startsWith("circle_time")) {
        break label43
      } 
      CircleClock.getInstance(localContext).handleSingleTap(localBundle);
    } 
    for (;;)
    {
      CircleCheckin.logMeanEventClickCircle(str);
      return;
      label81:if (str.startsWith("circle_battery")) {
        CircleBattery.getInstance(localContext).handleSingleTap(localBundle);
      }
      else if (str.startsWith("circle_weather")) {
        CircleWeather.getInstance(localContext).handleSingleTap(localBundle);
      } 
    } 
  } 
  
  public static boolean isConfigAvail()
  {
    return mIsConfigAvail;
  } 
  
  public static boolean isDataServiceAvail()
  {
    return mIsDataServiceAvail;
  } 
  
  public static boolean isScreenOn()
  {
    return mIsScreenOn;
  } 
  
  public static void sendMessage(Messenger paramMessenger, Message paramMessage)
  {
    synchronized (mLock)
    {
      int i = -1 + mClients.size();
      if (i >= 0)
      {
        Messenger localMessenger = (Messenger)mClients.get(i);
        if (paramMessenger != null)
        {
          boolean bool = paramMessenger.equals(localMessenger);
          if (bool) {}
        } 
        for (;;)
        {
          i--;
          break;
          try
          {
            localMessenger.send(paramMessage);
          }
          catch (RemoteException localRemoteException)
          {
            try
            {
              mClients.remove(i);
            }
            catch (Exception localException)
            {
              Log.e("Circle", "Client removal failed");
            } 
          } 
        } 
      } 
    } 
  } 
  
  public void onDisabled(Context paramContext)
  {
    CircleWeather.getInstance(paramContext).stopWeatherService();
    if (isDataServiceAvail()) {
      CircleData.getInstance(paramContext).stopDataService();
    } 
    paramContext.stopService(new Intent(paramContext, CircleService.class));
    super.onDisabled(paramContext);
  } 
  
  public void onEnabled(Context paramContext)
  {
    paramContext.getPackageManager().setComponentEnabledSetting(new ComponentName(paramContext, CircleService.class), 1, 1);
    super.onEnabled(paramContext);
  } 
  
  public void onUpdate(Context paramContext, AppWidgetManager paramAppWidgetManager, int[] paramArrayOfInt)
  {
    paramContext.startService(new Intent(paramContext, CircleService.class));
    int i = paramArrayOfInt.length;
    for (int j = 0; j < i; j++) {
      paramAppWidgetManager.updateAppWidget(paramArrayOfInt[j], new RemoteViews(paramContext.getPackageName(), 2130903056));
    } 
  } 
  
  public static class CircleService
    extends Service
  {
    static Context mContext;
    
    private void checkForHelpCircle()
    {
      if (!CircleHelp.isDoNotShowHelpEnabled())
      {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.add(12, 60);
        PendingIntent localPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent("com.motorola.widget.circlewidget3d.ACTION_DISPLAY_HELP_CIRCLE"), 134217728);
        ((AlarmManager)getSystemService("alarm")).set(1, localCalendar.getTimeInMillis(), localPendingIntent);
      } 
    } 
    
    private void createCircles()
    {
      CircleBattery.getInstance(this);
      CircleClock.getInstance(this);
      CircleWeather.getInstance(this);
      CircleAlert.getInstance(this);
      AlertMissedCall.getInstance(this);
      AlertVoicemailMoto.getInstance(this);
      AlertMMS.getInstance(this);
      CircleHelp.getInstance(this);
      AlertMessages.getInstance(this);
      if ((CircleWidget3DProvider.isDataServiceAvail()) || (Config.isDeviceDataSupported())) {
        CircleData.getInstance(this);
      } 
    } 
    
    private void destroyCircles()
    {
      CircleBattery.getInstance(this).handleDestroy();
      CircleClock.getInstance(this).handleDestroy();
      CircleWeather.getInstance(this).handleDestroy();
    } 
    
    public static Context getServiceContext()
    {
      return mContext;
    } 
    
    private void registerForChanges()
    {
      IntentFilter localIntentFilter1 = new IntentFilter();
      localIntentFilter1.addAction("android.intent.action.TIME_TICK");
      localIntentFilter1.addAction("android.intent.action.DATE_CHANGED");
      localIntentFilter1.addAction("android.intent.action.TIME_SET");
      localIntentFilter1.addAction("android.intent.action.ALARM_CHANGED");
      localIntentFilter1.addAction("android.intent.action.BATTERY_CHANGED");
      localIntentFilter1.addAction("com.motorola.weather.action.NEW_WEATHER_INFO");
      localIntentFilter1.addAction("com.motorola.widget.circlewidget3d.ACTION_CHANGE_THEME");
      localIntentFilter1.addAction("com.motorola.circle.test.FLING");
      localIntentFilter1.addAction("com.motorola.circle.test.battery");
      localIntentFilter1.addAction("com.motorola.circle.test.alert");
      localIntentFilter1.addAction("com.motorola.circle.test.data");
      localIntentFilter1.addAction("com.motorola.circle.test.data.level");
      localIntentFilter1.addAction("com.motorola.widget.circlewidget3d.ACTION_SETTING_FINISH");
      localIntentFilter1.addAction("com.motorola.widget.circlewidget3d.ACTION_DISPLAY_HELP_CIRCLE");
      localIntentFilter1.addAction("com.motorola.android.intent.action.ACTION_MWI_CHANGED");
      localIntentFilter1.addAction("com.motorola.blur.messaging.MMS_RECEIVED_ACTION");
      localIntentFilter1.addAction("com.motorola.contracts.messaging.intent.action.NO_MORE_UNSEEN_MESSAGES");
      localIntentFilter1.addAction("com.motorola.weather.action.TOP_CITY_CHANGED");
      localIntentFilter1.addAction("android.intent.action.LOCALE_CHANGED");
      if (CircleWidget3DProvider.isDataServiceAvail())
      {
        localIntentFilter1.addAction("com.motorola.datameter.ACTION_DATA_METER_USAGE_DATA");
        localIntentFilter1.addAction("com.motorola.datameter.ACTION_DATA_METER_ERROR");
      } 
      localIntentFilter1.addAction("com.motorola.notification.CLEAR_NOTIFICATION_ACTION");
      localIntentFilter1.addAction("android.intent.action.SCREEN_OFF");
      localIntentFilter1.addAction("android.intent.action.SCREEN_ON");
      if (Config.isDeviceDataSupported()) {
        localIntentFilter1.addAction("com.motorola.datameter.ACTION_DATA_USAGE_SETTINGS_COMPLETE");
      } 
      registerReceiver(CircleWidget3DProvider.mEventReceiver, localIntentFilter1);
      IntentFilter localIntentFilter2 = new IntentFilter();
      localIntentFilter2.addAction("com.motorola.blur.messaging.MMS_RECEIVED_ACTION");
      try
      {
        localIntentFilter2.addDataType("*/*");
        registerReceiver(CircleWidget3DProvider.mMMSReceiver, localIntentFilter2);
        AlertMissedCall.registerForCallLogChange(this);
        AlertMessages.registerForMessageChange(this);
        return;
      }
      catch (IntentFilter.MalformedMimeTypeException localMalformedMimeTypeException)
      {
        for (;;) {}
      } 
    } 
    
    public IBinder onBind(Intent paramIntent)
    {
      return CircleWidget3DProvider.mMessenger.getBinder();
    } 
    
    public void onCreate()
    {
      super.onCreate();
      CircleWidget3DProvider.mIsDataServiceAvail = Utility.isDataServiceAvail(this);
      CircleWidget3DProvider.mIsConfigAvail = Config.isCircleConfigAvail(this);
      if (CircleWidget3DProvider.mIsConfigAvail) {
        Config.retrieveConfigValues(this);
      } 
      createCircles();
      registerForChanges();
      checkForHelpCircle();
      mContext = this;
      CircleCheckin.startChecking(mContext);
    } 
    
    public void onDestroy()
    {
      unregisterReceiver(CircleWidget3DProvider.mEventReceiver);
      unregisterReceiver(CircleWidget3DProvider.mMMSReceiver);
      AlertMissedCall.unregisterCallLogChange(this);
      AlertMessages.unregisterMessageChange(this);
      destroyCircles();
      super.onDestroy();
      CircleCheckin.stopCheckin();
      Process.killProcess(Process.myPid());
    } 
    
    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
    {
      return 1;
    } 
  } 
  
  public static class HomeMsgHandler
    extends Handler
  {
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      case 3: 
      case 4: 
      case 6: 
      case 8: 
      case 9: 
      case 10: 
      case 11: 
      case 12: 
      case 13: 
      default: 
        super.handleMessage(paramMessage);
        return;
      case 1: 
        synchronized (CircleWidget3DProvider.mLock)
        {
          CircleWidget3DProvider.mClients.add(replyTo);
          if (CircleWidget3DProvider.isDataServiceAvail()) {
            CircleData.getInstance(null).fetchDataFromService();
          } 
          Utility.sendAllTextures();
          return;
        } 
      case 2: 
        synchronized (CircleWidget3DProvider.mLock)
        {
          CircleWidget3DProvider.mClients.remove(replyTo);
          return;
        } 
      case 5: 
        CircleWidget3DProvider.handleSingleTap(paramMessage);
        return;
      case 7: 
        CircleWidget3DProvider.handleFling(replyTo, paramMessage);
        return;
      case 14: 
        CircleWidget3DProvider.handlePODComplete(paramMessage);
        return;
      case 15: 
        Utility.handlePanelFocus(paramMessage);
        return;
      } 
      CircleWidget3DProvider.handleKeyEvent(paramMessage);
    } 
  } 
} 

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.CircleWidget3DProvider
 * JD-Core Version:    0.6.2
 */