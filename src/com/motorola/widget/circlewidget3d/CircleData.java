package com.motorola.widget.circlewidget3d;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.motorola.datameter.proxyservice.IDataMeter;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

public class CircleData extends Circle {
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
	private ServiceConnection mDeviceDataConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName componentname,
				IBinder ibinder) {
			mIsDeviceDataServiceBound = true;
			mDeviceDataService = com.motorola.datameter.proxyservice.IDataMeter.Stub
					.asInterface(ibinder);
		}

		public void onServiceDisconnected(ComponentName componentname) {
			mIsDeviceDataServiceBound = false;
			mDeviceDataService = null;
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

	private CircleData(Context paramContext) {
		this.mCurrentId = 0;
		this.mContext = paramContext;
		prepareCircle(R.layout.data_circle, CircleConsts.BATTERY_BITMAP_SIZE.intValue());
		sIsDataCircleEnabled = retrieveDataPref("show_mobile_data");
		sIsFrontSideIsDataCircle = retrieveDataPref("front_side_data_circle");
		boolean bool1 = sIsDataCircleEnabled;
		boolean bool2 = false;
		if (bool1) {
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

	private DataInfo convertDataInfo(Context paramContext,
			DataInfo paramDataInfo, int paramInt) {
		if (paramInt != paramDataInfo.unit) {
			if (paramInt != 2) {
				if (paramInt == 1) {
					if (paramDataInfo.unit != 0) {
						if (paramDataInfo.unit != 2) {
						}
					} else {
						paramDataInfo.data = Float.valueOf(paramDataInfo.data
								.floatValue() / 1000.0F);
					}
					paramDataInfo.unit = 1;
				}
			} else {
				if (paramDataInfo.unit != 0) {
					if (paramDataInfo.unit == 1) {
						paramDataInfo.data = Float.valueOf(paramDataInfo.data
								.floatValue() / 1000.0F);
					}
				} else {
					paramDataInfo.data = Float.valueOf(paramDataInfo.data
							.floatValue() / 1000000.0F);
				}
				paramDataInfo.unit = 2;
			}

			paramDataInfo.unitStr = paramContext.getResources().getStringArray(
					R.array.data_units)[paramDataInfo.unit];
			if (paramDataInfo.data.floatValue() >= 0.1D) {
				paramDataInfo.dataStr = getFormattedLimitStr(
						paramDataInfo.data, paramDataInfo.unit);
			} else {
				paramDataInfo.dataStr = paramContext.getResources().getString(
						R.string.less_then_1);
			}
		}
		return paramDataInfo;
	}

	private Bitmap getCorrectDataScreen() {
		if (sMobileDataEnabled) {
			if (!hasCachedData()) {
				Utility.changeVisibility("circle_battery/level", false);
				sIsSetupScreenDisplayed = true;
				return getDataSetupScreen();
			}
			return getDataScreen();
		}
		return getDataOffScreen();
	}

	private Bitmap getDataErrorScreen() {
		mFrontLayout.setVisibility(8);
		mBackLayout.setVisibility(0);
		mLayoutDataOff.setVisibility(8);
		mBitmap.eraseColor(0);
		if (mMaxLimit != null) {
			String s;
			if (mMaxLimit.data.floatValue() == -1F)
				s = mCurrentUsage.unitStr;
			else
				s = mMaxLimit.unitStr;
			mErrorMaxLimitView.setText(s);
		}
		mLayout.draw(mCanvas);
		return mBitmap;
	}

	private DataInfo getDataInfo(Context paramContext, Float paramFloat) {
		DataInfo localDataInfo = new DataInfo();
		if (paramFloat.floatValue() != -1.0F) {
			paramFloat = Float.valueOf(paramFloat.floatValue() / 1000.0F);
			localDataInfo.unit = 0;
			if (paramFloat.floatValue() >= 1000.0F) {
				localDataInfo.unit = 1;
				paramFloat = Float.valueOf(paramFloat.floatValue() / 1000.0F);
				if (paramFloat.floatValue() >= 1000.0F) {
					localDataInfo.unit = 2;
					paramFloat = Float
							.valueOf(paramFloat.floatValue() / 1000.0F);
				}
			}
		}
		localDataInfo.unitStr = paramContext.getResources().getStringArray(
				R.array.data_units)[localDataInfo.unit];
		localDataInfo.data = paramFloat;
		if (localDataInfo.data.floatValue() != -1.0F)
			localDataInfo.dataStr = getFormattedLimitStr(localDataInfo.data,
					localDataInfo.unit);
		return localDataInfo;
	}

	private Bitmap getDataOffScreen() {
		this.mLayoutUsage.setVisibility(8);
		this.mLayoutSetup.setVisibility(8);
		this.mLayoutDataOff.setVisibility(0);
		this.mFrontLayout.setVisibility(0);
		this.mBackLayout.setVisibility(8);
		this.mBitmap.eraseColor(0);
		this.mLayout.draw(this.mCanvas);
		return this.mBitmap;
	}

	private Bitmap getDataScreen() {
		mLayoutUsage.setVisibility(0);
		mLayoutSetup.setVisibility(8);
		mLayoutDataOff.setVisibility(8);
		if (hasCachedData()) {
			mCurrentUsageView.setText(mCurrentUsage.dataStr);
			String s;
			if (mMaxLimit.data.floatValue() == -1F)
				s = mCurrentUsage.unitStr;
			else
				s = (new StringBuilder()).append(mMaxLimit.dataStr).append(" ")
						.append(mMaxLimit.unitStr).toString();
			mMaxLimitView.setText(s);
			if (mMaxLimit.data.floatValue() == -1F)
				mLineView.setVisibility(4);
			else
				mLineView.setVisibility(0);
			mFrontLayout.setVisibility(0);
			mBackLayout.setVisibility(8);
			mBitmap.eraseColor(0);
			mLayout.draw(mCanvas);
			return mBitmap;
		} else {
			return getDataErrorScreen();
		}
	}

	private Bitmap getDataSetupScreen() {
		this.mLayoutUsage.setVisibility(8);
		this.mLayoutSetup.setVisibility(0);
		this.mLayoutDataOff.setVisibility(8);
		this.mFrontLayout.setVisibility(0);
		this.mBackLayout.setVisibility(8);
		this.mBitmap.eraseColor(0);
		this.mLayout.draw(this.mCanvas);
		return this.mBitmap;
	}

	private String getFormattedLimitStr(Float paramFloat, int paramInt) {
		if ((paramFloat.floatValue() >= 100.0F) || (paramInt == 1))
			return Integer.toString(paramFloat.intValue());
		return new DecimalFormat("#.##").format(paramFloat);
	}

	public static CircleData getInstance(Context paramContext) {
		synchronized (syncObject) {
			if (mInstance == null)
				mInstance = new CircleData(paramContext);
			return mInstance;
		}
	}

	private int getNewThresold() {
		int i = 100 - mDataLeftInPercentage;
		byte byte0;
		if (i > mOrangeMaxLevel)
			byte0 = 3;
		else if (i > mGreenMaxLevel && i <= mOrangeMaxLevel)
			byte0 = 2;
		else
			byte0 = 1;
		if (mDataError)
			byte0 = 4;
		return byte0;
	}

	private void initGetDataEnableMethod() {
		try {
			sConnectivityMgr = (ConnectivityManager) this.mContext
					.getSystemService("connectivity");
			sGetMobileDataEnabledMethod = sConnectivityMgr.getClass()
					.getMethod("getMobileDataEnabled", new Class[0]);
			sMobileDataEnabled = isMobileDataEnabled();
			return;
		} catch (Exception localException) {
			Log.e("Circle", "InitgetDaataEnableMethod exception "
					+ localException);
		}
	}

	public static boolean isDataCircleDisplayed() {
		return sIsDataCircleDisplayed;
	}

	public static boolean isDataCircleEnable() {
		return sIsDataCircleEnabled;
	}

	public static boolean isFrontSideIsDataCircle() {
		return sIsFrontSideIsDataCircle;
	}

	public static boolean isHasCachedData() {
		return sHasCachedData;
	}

	public static boolean isHasErrorData() {
		return mDataError;
	}

	public static boolean isMobileDataEnabled() {
		boolean bool1 = true;
		if (sGetMobileDataEnabledMethod != null)
			;
		try {
			boolean bool2 = ((Boolean) sGetMobileDataEnabledMethod.invoke(
					sConnectivityMgr, new Object[0])).booleanValue();
			bool1 = bool2;
			return bool1;
		} catch (Exception localException) {
			Log.e("Circle", "error in isMobileDataEnable function "
					+ localException);
		}
		return bool1;
	}

	public static boolean isSetupScreenDisplayed() {
		return sIsSetupScreenDisplayed;
	}

	private void removeFractionDataFromString(DataInfo paramDataInfo) {
		if (paramDataInfo.data.floatValue() != -1.0F)
			paramDataInfo.dataStr = getFormattedLimitStr(
					Float.valueOf(paramDataInfo.data.intValue()),
					paramDataInfo.unit);
	}

	private void retrieveDataInfo() {
		SharedPreferences localSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this.mContext);
		float f1 = localSharedPreferences.getFloat("EXTRA_MAX_DATA", -1000.0F);
		float f2 = localSharedPreferences.getFloat("EXTRA_USED_DATA", -1000.0F);
		if ((f1 != -1000.0F) && (f2 != -1000.0F)) {
			this.mMaxLimit = getDataInfo(this.mContext, Float.valueOf(f1));
			removeFractionDataFromString(this.mMaxLimit);
			this.mCurrentUsage = getDataInfo(this.mContext, Float.valueOf(f2));
			this.mCurrentUsage = convertDataInfo(this.mContext,
					this.mCurrentUsage, this.mMaxLimit.unit);
			this.mDataLeftInPercentage = generateDataLeftInPercentage(
					this.mCurrentUsage, this.mMaxLimit);
			sHasCachedData = true;
			return;
		}
		Log.e("Circle", "no cached data for data circle");
		sHasCachedData = false;
	}

	private boolean retrieveDataPref(String paramString) {
		return PreferenceManager.getDefaultSharedPreferences(this.mContext)
				.getBoolean(paramString, true);
	}

	private void startDataService() {
		Intent localIntent = new Intent(
				"com.motorola.datameter.ACTION_START_DATA_METER_SERVICE");
		localIntent.putExtra("EXTRA_CLIENT_NAME",
				this.mContext.getPackageName());
		try {
			this.mContext.sendBroadcast(localIntent);
			return;
		} catch (Exception localException) {
			Log.e("Circle", "Couldn't start Data service" + localException);
		}
	}

	private void storeDataInfo(float paramFloat1, float paramFloat2) {
		SharedPreferences.Editor localEditor = PreferenceManager
				.getDefaultSharedPreferences(this.mContext).edit();
		localEditor.putFloat("EXTRA_MAX_DATA", paramFloat1);
		localEditor.putFloat("EXTRA_USED_DATA", paramFloat2);
		localEditor.apply();
	}

	public void bindDeviceDataService(Context paramContext) {
		if (this.mIsDeviceDataServiceBound) {
			Log.e("Circle", "Device data service is already bound so return");
			return;
		}
		Intent localIntent = new Intent();
		localIntent.setComponent(new ComponentName(
				"com.motorola.datameter.proxyservice",
				"com.motorola.datameter.proxyservice.DataMeterProxyService"));
		try {
			paramContext
					.bindService(localIntent, this.mDeviceDataConnection, 1);
			return;
		} catch (SecurityException localSecurityException) {
			this.mIsDeviceDataServiceBound = false;
		}
	}

	public void changeBatteryLevelTransition(final float paramFloat,
			final int paramInt) {
		new Thread(new Runnable() {
			public void run() {
				try {
					Utility.updateBatteryLevelTexture(paramInt);
					Thread.sleep(200L);
					Utility.moveTexture(null, "circle_battery/level", 0, 0.5F,
							0.0F, 0.0F, 1L);
					if (!CircleData.this.mIsFlipped) {
						Thread.sleep(30L);
						Utility.moveTexture(null, "circle_battery/level", 0,
								paramFloat, 0.0F, 0.0F, 800L);
					}
					return;
				} catch (InterruptedException localInterruptedException) {
					localInterruptedException.printStackTrace();
				}
			}
		}).start();
	}

	public void checkIfDataStateChanged() {
		boolean bool = isMobileDataEnabled();
		if (sMobileDataEnabled != bool) {
			sMobileDataEnabled = bool;
			if (sIsDataCircleDisplayed)
				updateCircle();
		}
	}

	public void fetchDataFromService() {
		Intent localIntent = new Intent(
				"com.motorola.datameter.ACTION_FETCH_DATA_USAGE");
		try {
			this.mContext.sendBroadcast(localIntent);
			return;
		} catch (Exception localException) {
			Log.e("Circle", "Couldn't send featch data msg to service"
					+ localException);
		}
	}

	public int generateDataLeftInPercentage(DataInfo datainfo,
			DataInfo datainfo1) {
		int i;
		if (datainfo1.data.floatValue() == -1F) {
			i = 100;
		} else {
			if (datainfo.unit != datainfo1.unit)
				datainfo = convertDataInfo(mContext, datainfo, datainfo1.unit);
			i = 100 - (int) ((100F * datainfo.data.floatValue()) / datainfo1.data
					.floatValue());
			if (i < 0)
				return 0;
		}
		return i;
	}

	public Bitmap getBackTexture(Bundle paramBundle) {
		return null;
	}

	public float getDataUsageTranslation() {
		return 0.25F * (100 - this.mDataLeftInPercentage) / 50.0F;
	}

	public Bitmap getFrontTexture(Bundle paramBundle) {
		return null;
	}

	public void handleDestroy() {
		unbindDeviceDataService(this.mContext);
		super.handleDestroy();
		mInstance = null;
	}

	public boolean handleFling(Messenger messenger, Message message,
			Float float1) {
		boolean flag = true;
		boolean flag1 = true;
		CircleBattery circlebattery = CircleBattery.getInstance(mContext);
		mIsFlipped = circlebattery.getFlipValue();
		if (mIsFlipped && float1.floatValue() < 0.0F || !mIsFlipped
				&& float1.floatValue() > 0.0F) {
			flag1 = false;
			sIsDataCircleDisplayed = false;
			if (sIsSetupScreenDisplayed)
				Utility.changeVisibility("circle_battery/level", flag);
			circlebattery.updateCircle();
			storeDataCircleFronSidePref(false);
		}
		if (flag1) {
			Utility.flipCircle(messenger, "circle_battery",
					float1.floatValue(), mIsFlipped);
			updateDataLevelBg();
			if (mIsFlipped)
				flag = false;
			mIsFlipped = flag;
			storeSidePref("battery_side_front");
			circlebattery.setFlipped(mIsFlipped);
		}
		return false;
	}

	public boolean handleSingleTap(Bundle bundle) {
		mIsFlipped = CircleBattery.getInstance(mContext).getFlipValue();
		if (!mIsFlipped)
			startDataUsageApp();
		else
			Utility.startCircleSettings(mContext);
		return false;
	}

	public boolean hasCachedData() {
		return (this.mCurrentUsage != null) && (this.mMaxLimit != null);
	}

	public void populateData() {
		if (sIsDataCircleEnabled && sIsFrontSideIsDataCircle)
			updateCircle();
		else if (sIsDataCircleEnabled && getNewThresold() != mPrevThresold) {
			sIsFrontSideIsDataCircle = true;
			updateCircle();
			mIsFlipped = CircleBattery.getInstance(mContext).getFlipValue();
			if (mIsFlipped) {
				Utility.flipCircle(null, "circle_battery", 300F, false);
				return;
			}
		}
	}

	public View prepareCircle(int paramInt1, int paramInt2) {
		View localView = super.prepareCircle(paramInt1, paramInt2);
		this.mFrontLayout = localView.findViewById(R.id.data_front);
		this.mBackLayout = localView.findViewById(R.id.data_error);
		this.mCurrentUsageView = ((TextView) localView.findViewById(R.id.data_current_usage));
		this.mMaxLimitView = ((TextView) localView.findViewById(R.id.data_max_limit));
		this.mLineView = localView.findViewById(R.id.data_line);
		this.mErrorMaxLimitView = ((TextView) localView
				.findViewById(R.id.data_error_max_limit));
		this.mLayoutSetup = localView.findViewById(R.id.layout_data_setup);
		this.mLayoutUsage = localView.findViewById(R.id.layout_data_usage);
		this.mLayoutDataOff = localView.findViewById(R.id.layout_data_off);
		return localView;
	}

	public void retrieveDeviceDataValues(Context paramContext) {
		if (mDeviceDataService == null) {
			Log.e("Circle", "Device data Service is null");
			return;
		}

		try {
			long l1 = this.mDeviceDataService.getPolicyLimitBytes();
			if (l1 != -1000L) {
				this.mMaxLimit = getDataInfo(paramContext,
						Float.valueOf((float) l1));
				removeFractionDataFromString(this.mMaxLimit);
			}
			long l2 = this.mDeviceDataService.getCurrentUsageBytes();
			if (l2 == -1000L) {
				this.mCurrentUsage = getDataInfo(paramContext,
						Float.valueOf((float) l2));
				this.mCurrentUsage = convertDataInfo(this.mContext,
						this.mCurrentUsage, this.mMaxLimit.unit);
			}
			if (l1 == -1000L || l2 == -1000L) {
				Log.e("Circle",
						"Data circle got no data from Device dataService");
				return;
			}
			this.mDataLeftInPercentage = generateDataLeftInPercentage(
					this.mCurrentUsage, this.mMaxLimit);
			this.mGreenMaxLevel = 65;
			this.mOrangeMaxLevel = 90;
			storeDataInfo((float) l1, (float) l2);
			sHasCachedData = true;
			sIsSetupScreenDisplayed = false;
			mDataError = false;
			Utility.changeVisibility("circle_battery/level", true);
			populateData();
		} catch (Exception localException) {
			Log.e("Circle", "Exception while fetching getPolicyLimitBytes "
					+ localException);
		}
	}

	public void setTheme(String paramString) {
	}

	public void showDummyDataScreen() {
		sIsDataCircleEnabled = true;
		sIsFrontSideIsDataCircle = true;
		sIsFrontSideIsDataCircle = true;
		sHasCachedData = true;
		mDataError = false;
		if (2.0E+009F != -1000.0F)
			this.mMaxLimit = getDataInfo(this.mContext,
					Float.valueOf(2.0E+009F));
		removeFractionDataFromString(this.mMaxLimit);
		this.mCurrentUsage = getDataInfo(this.mContext,
				Float.valueOf(1.03E+009F));
		this.mCurrentUsage = convertDataInfo(this.mContext, this.mCurrentUsage,
				this.mMaxLimit.unit);
		this.mDataTestMode = true;
		this.mDataLeftInPercentage = generateDataLeftInPercentage(
				this.mCurrentUsage, this.mMaxLimit);
		this.mGreenMaxLevel = 65;
		this.mOrangeMaxLevel = 90;
		populateData();
	}

	public void startDataUsageApp() {
		Intent intent = new Intent();
		intent.setFlags(0x14a08000);
		if (Config.isDeviceDataSupported()) {
			intent.setAction("com.motorola.datameter.ACTION_SHOW_DATA_USAGE_SETTINGS");
			if (sIsSetupScreenDisplayed) {
				intent.putExtra("config", true);
				bindDeviceDataService(mContext);
			}
		} else {
			intent.setAction("com.motorola.datameter.ACTION_SHOW_DATA_USAGE");
		}
		try {
			mContext.startActivity(intent);
			return;
		} catch (Exception exception) {
			Log.e("Circle",
					(new StringBuilder()).append("Couldn't start Data App")
							.append(exception).toString());
		}
	}

	public void stopDataService() {
		Intent localIntent = new Intent(
				"com.motorola.datameter.ACTION_STOP_DATA_METER_SERVICE");
		localIntent.putExtra("EXTRA_CLIENT_NAME",
				this.mContext.getPackageName());
		try {
			this.mContext.sendBroadcast(localIntent);
			return;
		} catch (Exception localException) {
			Log.e("Circle", "Couldn't stop Data service" + localException);
		}
	}

	public void storeDataCircleFronSidePref(boolean paramBoolean) {
		SharedPreferences.Editor localEditor = PreferenceManager
				.getDefaultSharedPreferences(this.mContext).edit();
		localEditor.putBoolean("front_side_data_circle", paramBoolean);
		localEditor.apply();
		sIsFrontSideIsDataCircle = paramBoolean;
	}

	public void unbindDeviceDataService(Context paramContext) {
		if (this.mIsDeviceDataServiceBound) {
			paramContext.unbindService(this.mDeviceDataConnection);
			this.mIsDeviceDataServiceBound = false;
		}
		this.mDeviceDataService = null;
	}

	public void updateCircle() {
		Bitmap bitmap;
		if (!mDataError)
			bitmap = getCorrectDataScreen();
		else
			bitmap = getDataErrorScreen();
		Utility.updateTexture(null, "circle_battery/circlefront", bitmap);
		Utility.updateTexture(null, "circle_battery/circleback", CircleBattery
				.getInstance(mContext).getSettingScreen());
		sIsDataCircleDisplayed = true;
		if (mDataError)
			changeBatteryLevelTransition(0.5F, 4);
		else
			updateDataLevelBg();
		storeDataCircleFronSidePref(true);
	}

	public void updateCircle(Context context, Intent intent) {
		if (intent != null) {
			mDataError = false;
			float f = intent.getFloatExtra("EXTRA_MAX_DATA", -1000F);
			if (f != -1000F) {
				mMaxLimit = getDataInfo(context, Float.valueOf(f));
				removeFractionDataFromString(mMaxLimit);
			}
			float f1 = intent.getFloatExtra("EXTRA_USED_DATA", -1000F);
			if (f1 != -1000F) {
				mCurrentUsage = getDataInfo(context, Float.valueOf(f1));
				mCurrentUsage = convertDataInfo(mContext, mCurrentUsage,
						mMaxLimit.unit);
			}
			if (f == -1000F || f1 == -1000F) {
				Log.e("Circle", "Data circle got no data from Service");
			} else {
				mDataLeftInPercentage = generateDataLeftInPercentage(
						mCurrentUsage, mMaxLimit);
				mGreenMaxLevel = intent.getIntExtra("EXTRA_GREEN_THRESHOLD",
						mGreenMaxLevel);
				mOrangeMaxLevel = intent.getIntExtra("EXTRA_YELLOW_THRESHOLD",
						mOrangeMaxLevel);
				storeDataInfo(f, f1);
				sHasCachedData = true;
				if (f == -1F) {
					SharedPreferences sharedpreferences = PreferenceManager
							.getDefaultSharedPreferences(mContext);
					if (!sharedpreferences.getBoolean("data_setting_changed",
							false)) {
						android.content.SharedPreferences.Editor editor = sharedpreferences
								.edit();
						editor.putBoolean("show_mobile_data", false);
						editor.apply();
						sIsDataCircleEnabled = false;
					}
				}
				if (isDataCircleEnable()) {
					populateData();
					return;
				}
			}
		}
	}

	public void updateCircleWithError(Context paramContext, Intent paramIntent) {
		if (paramIntent != null) {
			mDataError = true;
			populateData();
		}
	}

	public void updateDataLevelBg() {
		if (!sIsSetupScreenDisplayed) {
			int i = getNewThresold();
			changeBatteryLevelTransition(getDataUsageTranslation(), i);
			this.mPrevThresold = i;
		}
	}

	public void updateSettingValues(Intent paramIntent) {
		sIsDataCircleEnabled = PreferenceManager.getDefaultSharedPreferences(
				this.mContext).getBoolean("show_mobile_data",
				sIsDataCircleEnabled);
	}

	public void updateValues(Context paramContext, Intent paramIntent) {
	}

	public static class DataInfo {
		Float data;
		String dataStr;
		int unit;
		String unitStr;
	}
}

/*
 * Location: J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.widget.circlewidget3d.CircleData JD-Core Version: 0.6.2
 */