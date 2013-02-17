package com.motorola.widget.circlewidget3d;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ThemeInfo {
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

	public static Bitmap getBitmap(Context context, String s, String s1) {
		Context context1;
		Resources resources;
		int i;
		Bitmap bitmap;
		try {
			context1 = context.createPackageContext(s, 4);
		} catch (android.content.pm.PackageManager.NameNotFoundException namenotfoundexception) {
			Log.e("Circle", "Failed to load theme context");
			return null;
		}
		resources = context1.getResources();
		i = resources.getIdentifier(s1, "drawable", s);
		if (i != 0) {
			mBitmapOptions.inPreferredConfig = android.graphics.Bitmap.Config.ARGB_8888;
			bitmap = BitmapFactory.decodeResource(resources, i, mBitmapOptions);
		} else {
			Log.e("Circle", "Back image id not found");
			bitmap = null;
		}
		return bitmap;
	}

	public static Drawable getDrawable(Context paramContext,
			String paramString1, String paramString2) {
		Bitmap localBitmap = getBitmap(paramContext, paramString1, paramString2);
		if (localBitmap != null)
			return new BitmapDrawable(localBitmap);
		Log.e("Circle", "Bitmap not decoded");
		return null;
	}

	public static String getText(Context context, String s, String s1) {
		Context context1;
		Resources resources;
		int i;
		String s2;
		try {
			context1 = context.createPackageContext(s, 4);
		} catch (android.content.pm.PackageManager.NameNotFoundException namenotfoundexception) {
			Log.e("Circle", "Failed to load theme context");
			return null;
		}
		resources = context1.getResources();
		i = resources.getIdentifier(s1, "string", s);
		if (i != 0) {
			s2 = resources.getString(i);
		} else {
			Log.e("Circle", "Text Id not found");
			s2 = null;
		}
		return s2;
	}

	public static int getTextColor(Context context, String s, String s1) {
		int i = -1;
		Context context1;
		Resources resources;
		int j;
		try {
			context1 = context.createPackageContext(s, 4);
		} catch (android.content.pm.PackageManager.NameNotFoundException namenotfoundexception) {
			Log.e("Circle", "Failed to load theme context");
			return i;
		}
		resources = context1.getResources();
		j = resources.getIdentifier(s1, "integer", s);
		if (j != 0)
			i = resources.getInteger(j);
		else
			Log.e("Circle", "Color id not found");
		return i;
	}
}

/*
 * Location: J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.widget.circlewidget3d.ThemeInfo JD-Core Version: 0.6.2
 */