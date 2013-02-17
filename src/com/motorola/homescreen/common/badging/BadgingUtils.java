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

public class BadgingUtils {
	public static final int CODE_GET_SHORTCUTS = 1;
	private static final String TAG = "BadgingUtils";

	public static GetShortcutsResult getShortcuts(Context paramContext,
			String paramString1, String paramString2, String paramString3,
			List<Intent> paramList) {
		return new BadgeProxy(paramContext).getShortcuts(paramString1,
				paramString2, paramString3, paramList);
	}

	static class BadgeProxy implements ServiceConnection {
		private volatile boolean mBound;
		private final Context mContext;
		private volatile IBinder mService;

		public BadgeProxy(Context paramContext) {
			this.mContext = paramContext;
		}

		private synchronized IBinder getService() {
			if (this.mService == null) {
				Intent localIntent = new Intent();
				localIntent.setComponent(new ComponentName(
						"com.motorola.homescreen",
						"com.motorola.homescreen.BadgeService"));
				this.mBound = this.mContext.bindService(localIntent, this, 1);
				if (this.mBound) {
					try {
						wait(10000L);
					} catch (InterruptedException localInterruptedException) {
						Log.w(TAG, "Interrupted waiting to bind",
								localInterruptedException);
					}
				}

				if (this.mService == null)
					Log.w(TAG, "Couldn't bind to BadgeService");

			}

			return this.mService;
		}

		public GetShortcutsResult getShortcuts(String s, String s1, String s2,
				List<Intent> list) {
			GetShortcutsResult getshortcutsresult = GetShortcutsResult.OK;
			IBinder ibinder = getService();
			Parcel parcel = null;
			Parcel parcel1 = null;

			if (ibinder == null) {
				Log.e(TAG, "Couldn't bind to BadgeService");
				getshortcutsresult = GetShortcutsResult.ERR_REMOTE;
			} else {
				try {
					parcel = Parcel.obtain();
					parcel1 = Parcel.obtain();
					parcel.writeString(s);
					parcel.writeString(s1);
					parcel.writeString(s2);
					ibinder.transact(1, parcel, parcel1, 0);
					getshortcutsresult = GetShortcutsResult.valueOf(parcel1
							.readString());
					if (getshortcutsresult == GetShortcutsResult.OK) {
						int i = parcel1.readInt();
						int j = 0;

						while (j < i) {
							list.add((Intent) parcel1.readParcelable(null));
							j++;
						}
					}

				} catch (RemoteException localRemoteException) {
					Log.e("BadgingUtils", "Couldn't transact BadgeService",
							localRemoteException);
					getshortcutsresult = BadgingUtils.GetShortcutsResult.ERR_REMOTE;
				} finally {
					if (parcel != null) {
						parcel.recycle();
					}
					if (parcel1 != null) {
						parcel1.recycle();
					}
					if (this.mBound) {
						this.mContext.unbindService(this);
					}
				}

			}
			return getshortcutsresult;
		}

		public void onServiceConnected(ComponentName paramComponentName,
				IBinder paramIBinder) {
			this.mService = paramIBinder;
			notify();
		}

		public void onServiceDisconnected(ComponentName paramComponentName) {
			this.mBound = false;
		}

	}

	public static enum GetShortcutsResult {
		OK, ERR_REMOTE, ERR_PERMISSION, ERR_DB, ERR_REGEX
	}
}

/*
 * Location: J:\鎶�湳鏂囨。\瀹夊崜鍥轰欢鐩稿叧\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.homescreen.common.badging.BadgingUtils JD-Core Version: 0.6.2
 */