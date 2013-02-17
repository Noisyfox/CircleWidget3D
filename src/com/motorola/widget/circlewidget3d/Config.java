package com.motorola.widget.circlewidget3d;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.util.Log;
import java.util.List;

public class Config
{
  public static final String ACTION_CIRCLE_CONFIG = "com.motorola.circlewidget3d.CONFIG";
  public static String sConfigPkg = null;
  public static int sEnableDataMeter = 1;
  public static int sEnableMissedCall;
  public static int sEnableTextMsg = 1;
  public static int sEnableVoicemail;
  public static int sSupportDeviceData = 0;
  public static int sSupportedDataMeter;
  public static int sSupportedMissedCall;
  public static int sSupportedTextMsg;
  public static int sSupportedVoicemail;
  
  static
  {
    sEnableMissedCall = 1;
    sEnableVoicemail = 1;
    sSupportedDataMeter = 1;
    sSupportedTextMsg = 1;
    sSupportedMissedCall = 1;
    sSupportedVoicemail = 1;
  } 
  
  private static Context getConfigContext(Context paramContext, String paramString)
  {
    try
    {
      Context localContext = paramContext.createPackageContext(paramString, 4);
      return localContext;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      Log.e("Circle", "Failed to load config context");
    } 
    return null;
  } 
  
  public static int getInteger(Context paramContext1, Context paramContext2, String paramString, int paramInt)
  {
    int i = paramInt;
    if (paramContext2 != null)
    {
      Resources localResources = paramContext2.getResources();
      int j = localResources.getIdentifier(paramString, "integer", sConfigPkg);
      if (j != 0) {
        i = localResources.getInteger(j);
      } 
    } 
    return i;
  } 
  
  public static String getString(Context paramContext1, Context paramContext2, String paramString)
  {
    String str = null;
    if (paramContext2 != null)
    {
      Resources localResources = paramContext2.getResources();
      int i = localResources.getIdentifier(paramString, "string", sConfigPkg);
      str = null;
      if (i != 0) {
        str = localResources.getString(i);
      } 
    } 
    return str;
  } 
  
  public static boolean isCircleConfigAvail(Context paramContext)
  {
    List localList = paramContext.getPackageManager().queryBroadcastReceivers(new Intent("com.motorola.circlewidget3d.CONFIG"), 0);
    int i = localList.size();
    if (i > 0)
    {
      ResolveInfo localResolveInfo = (ResolveInfo)localList.get(0);
      if (localResolveInfo != null) {
        sConfigPkg = packageName;
      } 
    } 
    boolean bool = false;
    if (i > 0) {
      bool = true;
    } 
    return bool;
  } 
  
  public static boolean isDeviceDataSupported()
  {
    return sSupportDeviceData == 1;
  } 
  
  public static void retrieveConfigValues(Context paramContext)
  {
    if (sConfigPkg != null)
    {
      Context localContext = getConfigContext(paramContext, sConfigPkg);
      sEnableDataMeter = getInteger(paramContext, localContext, "enable_data_meter", sEnableDataMeter);
      sEnableTextMsg = getInteger(paramContext, localContext, "enable_text_msg", sEnableTextMsg);
      sEnableMissedCall = getInteger(paramContext, localContext, "enable_missed_call", sEnableMissedCall);
      sEnableVoicemail = getInteger(paramContext, localContext, "enable_voicemail", sEnableVoicemail);
      sSupportedDataMeter = getInteger(paramContext, localContext, "supported_data_meter", sSupportedDataMeter);
      sSupportedTextMsg = getInteger(paramContext, localContext, "supported_text_msg", sSupportedTextMsg);
      sSupportedMissedCall = getInteger(paramContext, localContext, "supported_missed_call", sSupportedMissedCall);
      sSupportedVoicemail = getInteger(paramContext, localContext, "supported_voicemail", sSupportedVoicemail);
      sSupportDeviceData = getInteger(paramContext, localContext, "supported_device_data", sSupportDeviceData);
      CirclePreferenceActivity.updateDefaultValues(paramContext);
    } 
  } 
} 

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.Config
 * JD-Core Version:    0.6.2
 */