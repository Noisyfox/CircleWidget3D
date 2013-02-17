package com.motorola.homescreen.common.badging;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import java.util.List;

public class BadgingUtils
{
  public static final int CODE_GET_SHORTCUTS = 1;
  private static final String TAG = "BadgingUtils";

  public static GetShortcutsResult getShortcuts(Context paramContext, String paramString1, String paramString2, String paramString3, List<Intent> paramList)
  {
    return new BadgeProxy(paramContext).getShortcuts(paramString1, paramString2, paramString3, paramList);
  }

  static class BadgeProxy
    implements ServiceConnection
  {
    private static final String BADGE_SERVICE_CLASS_NAME = "com.motorola.homescreen.BadgeService";
    private static final String BADGE_SERVICE_PACKAGE_NAME = "com.motorola.homescreen";
    private volatile boolean mBound;
    private final Context mContext;
    private volatile IBinder mService;

    public BadgeProxy(Context paramContext)
    {
      this.mContext = paramContext;
    }

    private IBinder getService()
    {
      try
      {
        if (this.mService == null)
        {
          Intent localIntent = new Intent();
          localIntent.setComponent(new ComponentName("com.motorola.homescreen", "com.motorola.homescreen.BadgeService"));
          this.mBound = this.mContext.bindService(localIntent, this, 1);
          boolean bool = this.mBound;
          if (!bool);
        }
        try
        {
          wait(10000L);
          if (this.mService == null)
            Log.w("BadgingUtils", "Couldn't bind to BadgeService");
          IBinder localIBinder = this.mService;
          return localIBinder;
        }
        catch (InterruptedException localInterruptedException)
        {
          while (true)
            Log.w("BadgingUtils", "Interrupted waiting to bind", localInterruptedException);
        }
      }
      finally
      {
      }
    }

    public BadgingUtils.GetShortcutsResult getShortcuts(String paramString1, String paramString2, String paramString3, List<Intent> paramList)
    {
      IBinder localIBinder = getService();
      Parcel localParcel1 = null;
      Parcel localParcel2 = null;
      if (localIBinder != null);
      try
      {
        localParcel1 = Parcel.obtain();
        localParcel2 = Parcel.obtain();
        localParcel1.writeString(paramString1);
        localParcel1.writeString(paramString2);
        localParcel1.writeString(paramString3);
        localIBinder.transact(1, localParcel1, localParcel2, 0);
        localGetShortcutsResult = BadgingUtils.GetShortcutsResult.valueOf(localParcel2.readString());
        if (localGetShortcutsResult == BadgingUtils.GetShortcutsResult.OK)
        {
          int i = localParcel2.readInt();
          int j = 0;
          while (j < i)
          {
            paramList.add((Intent)localParcel2.readParcelable(null));
            j++;
            continue;
            Log.e("BadgingUtils", "Couldn't bind to BadgeService");
            localGetShortcutsResult = BadgingUtils.GetShortcutsResult.ERR_REMOTE;
          }
        }
        return localGetShortcutsResult;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("BadgingUtils", "Couldn't transact BadgeService", localRemoteException);
        BadgingUtils.GetShortcutsResult localGetShortcutsResult = BadgingUtils.GetShortcutsResult.ERR_REMOTE;
        return localGetShortcutsResult;
      }
      finally
      {
        if (localParcel1 != null)
          localParcel1.recycle();
        if (localParcel2 != null)
          localParcel2.recycle();
        if (this.mBound)
          this.mContext.unbindService(this);
      }
    }

    public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
    {
      try
      {
        this.mService = paramIBinder;
        notify();
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public void onServiceDisconnected(ComponentName paramComponentName)
    {
      this.mBound = false;
    }
  }

  public static enum GetShortcutsResult
  {
    static
    {
      ERR_REMOTE = new GetShortcutsResult("ERR_REMOTE", 1);
      ERR_PERMISSION = new GetShortcutsResult("ERR_PERMISSION", 2);
      ERR_DB = new GetShortcutsResult("ERR_DB", 3);
      ERR_REGEX = new GetShortcutsResult("ERR_REGEX", 4);
      GetShortcutsResult[] arrayOfGetShortcutsResult = new GetShortcutsResult[5];
      arrayOfGetShortcutsResult[0] = OK;
      arrayOfGetShortcutsResult[1] = ERR_REMOTE;
      arrayOfGetShortcutsResult[2] = ERR_PERMISSION;
      arrayOfGetShortcutsResult[3] = ERR_DB;
      arrayOfGetShortcutsResult[4] = ERR_REGEX;
    }
  }
}

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.homescreen.common.badging.BadgingUtils
 * JD-Core Version:    0.6.2
 */