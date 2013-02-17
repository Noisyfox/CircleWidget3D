package com.motorola.widget.circlewidget3d;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ThemeInfo
{
  public static final String DEFAULT_THEME_PACKAGE = "com.motorola.widget.circlewidget3d";
  public static final String ID_CIRCLE_ALERT_FRONT_MASK = "alert_front_mask";
  public static final String ID_CIRCLE_CLOCK_BACK = "clock_back";
  public static final String ID_CIRCLE_CLOCK_BACK_MASK = "clock_back_mask";
  public static final String ID_CIRCLE_CLOCK_FRONT = "clock_front";
  public static final String ID_CIRCLE_CLOCK_FRONT_MASK = "clock_front_mask";
  public static final String ID_CIRCLE_WEATHER_CLOUDY_DAY = "weather_day_cloudy";
  public static final String ID_CIRCLE_WEATHER_CLOUDY_NIGHT = "weather_night_cloudy";
  public static final String ID_CIRCLE_WEATHER_DAY = "weather_day";
  public static final String ID_CIRCLE_WEATHER_NIGHT = "weather_night";
  public static final String ID_CLOCK_HANDS = "clock_hands";
  public static final String ID_PREVIEW_IMAGE = "theme_preview_circle_widget";
  public static final String ID_TEXT_COLOR = "text_color";
  public static final String ID_THEME_NAME = "theme_name";
  static BitmapFactory.Options mBitmapOptions = new BitmapFactory.Options();

  public static Bitmap getBitmap(Context paramContext, String paramString1, String paramString2)
  {
    while (true)
    {
      try
      {
        Context localContext = paramContext.createPackageContext(paramString1, 4);
        Resources localResources = localContext.getResources();
        int i = localResources.getIdentifier(paramString2, "drawable", paramString1);
        if (i != 0)
        {
          mBitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
          localBitmap = BitmapFactory.decodeResource(localResources, i, mBitmapOptions);
          return localBitmap;
        }
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        Log.e("Circle", "Failed to load theme context");
        return null;
      }
      Log.e("Circle", "Back image id not found");
      Bitmap localBitmap = null;
    }
  }

  public static Drawable getDrawable(Context paramContext, String paramString1, String paramString2)
  {
    Bitmap localBitmap = getBitmap(paramContext, paramString1, paramString2);
    if (localBitmap != null)
      return new BitmapDrawable(localBitmap);
    Log.e("Circle", "Bitmap not decoded");
    return null;
  }

  public static String getText(Context paramContext, String paramString1, String paramString2)
  {
    while (true)
    {
      try
      {
        Context localContext = paramContext.createPackageContext(paramString1, 4);
        Resources localResources = localContext.getResources();
        int i = localResources.getIdentifier(paramString2, "string", paramString1);
        if (i != 0)
        {
          str = localResources.getString(i);
          return str;
        }
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        Log.e("Circle", "Failed to load theme context");
        return null;
      }
      Log.e("Circle", "Text Id not found");
      String str = null;
    }
  }

  public static int getTextColor(Context paramContext, String paramString1, String paramString2)
  {
    int i = -1;
    while (true)
    {
      try
      {
        Context localContext = paramContext.createPackageContext(paramString1, 4);
        Resources localResources = localContext.getResources();
        int j = localResources.getIdentifier(paramString2, "integer", paramString1);
        if (j != 0)
        {
          i = localResources.getInteger(j);
          return i;
        }
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        Log.e("Circle", "Failed to load theme context");
        return i;
      }
      Log.e("Circle", "Color id not found");
    }
  }
}

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.ThemeInfo
 * JD-Core Version:    0.6.2
 */