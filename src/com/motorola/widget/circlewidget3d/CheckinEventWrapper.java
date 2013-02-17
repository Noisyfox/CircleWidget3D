package com.motorola.widget.circlewidget3d;

import android.content.ContentResolver;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class CheckinEventWrapper
{
  static final String CLASS_CHECKINEVENT = "com.motorola.android.provider.CheckinEvent";
  static final String CLASS_EVENT = "com.motorola.data.event.api.Event";
  static final String METHOD_PUBLISH = "publish";
  static final String METHOD_SETVALUE = "setValue";
  private static String TAG = "CheckinEventWrapper";
  private static Constructor<?> sMethodConstructor;
  private static Method sMethodPublish;
  private static Method sMethodSetValue;
  private static boolean sSucessfullyInit;
  private Object mCheckinEvent;

  static
  {
    try
    {
      Class localClass1 = Class.forName("com.motorola.android.provider.CheckinEvent");
      Class localClass2 = Class.forName("com.motorola.data.event.api.Event");
      Class[] arrayOfClass = new Class[4];
      arrayOfClass[0] = String.class;
      arrayOfClass[1] = String.class;
      arrayOfClass[2] = String.class;
      arrayOfClass[3] = Long.TYPE;
      sMethodConstructor = localClass1.getDeclaredConstructor(arrayOfClass);
      sMethodSetValue = localClass2.getDeclaredMethod("setValue", new Class[] { String.class, String.class });
      sMethodPublish = localClass1.getDeclaredMethod("publish", new Class[] { Object.class });
      sSucessfullyInit = true;
      return;
    }
    catch (Throwable localThrowable)
    {
      Log.w(TAG, "Reflection failed");
      sMethodSetValue = null;
      sMethodPublish = null;
      sMethodConstructor = null;
      sSucessfullyInit = false;
    }
  }

  public static boolean isInitialized()
  {
    return sSucessfullyInit;
  }

  public void publish(ContentResolver paramContentResolver)
  {
    if ((sSucessfullyInit) && (this.mCheckinEvent != null));
    try
    {
      sMethodPublish.invoke(this.mCheckinEvent, new Object[] { paramContentResolver });
      return;
    }
    catch (Throwable localThrowable)
    {
      Log.w(TAG, "Reflection failed");
    }
  }

  public boolean setHeader(String paramString1, String paramString2, String paramString3, long paramLong)
  {
    if (sSucessfullyInit)
      try
      {
        Constructor localConstructor = sMethodConstructor;
        Object[] arrayOfObject = new Object[4];
        arrayOfObject[0] = paramString1;
        arrayOfObject[1] = paramString2;
        arrayOfObject[2] = paramString3;
        arrayOfObject[3] = Long.valueOf(paramLong);
        this.mCheckinEvent = localConstructor.newInstance(arrayOfObject);
        return true;
      }
      catch (Throwable localThrowable)
      {
        Log.w(TAG, "Reflection failed");
      }
    return false;
  }

  public void setValue(String paramString1, String paramString2)
  {
    if ((sSucessfullyInit) && (this.mCheckinEvent != null));
    try
    {
      sMethodSetValue.invoke(this.mCheckinEvent, new Object[] { paramString1, paramString2 });
      return;
    }
    catch (Throwable localThrowable)
    {
      Log.w(TAG, "Reflection failed");
    }
  }
}

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.CheckinEventWrapper
 * JD-Core Version:    0.6.2
 */