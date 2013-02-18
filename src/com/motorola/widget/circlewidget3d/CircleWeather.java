package com.motorola.widget.circlewidget3d;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CircleWeather extends Circle {
	public static final long DELAY_FIRST_TIME_FLIP = 1400L;
	private static final long DURATION_FIRST_TIME_FLIP = 2000L;
	public static final String EXTRA_FROMCIRCLE = "from_circle";
	public static final String[] VISIBILITY_SHAPE_NAMES = {
			"circle_weather/weathereffects/conditionfront",
			"circle_weather/weathereffects/clouds2front",
			"circle_weather/rainfront", "circle_weather/rainback",
			"circle_weather/weathereffects/planetfront",
			"circle_weather/weathereffects/pointLight" };
	private static WeatherCondition mCurrentCondition = null;
	private static CircleWeather mInstance;
	private static int mTopCityId;
	private final boolean DEBUG_WEATHER_CONDITION = false;
	TextView mCity;
	private WeatherDisplayIds mCurrentDisplayIds = null;
	TextView mCurrentTemp;
	int mCurrentTempSmallTextSize;
	int mCurrentTempTextSize;
	boolean mSetupFlipThreadStarted;
	TextView mTodayHigh;
	ImageView mTodayHighImageView;
	TextView mTodayHighLabel;
	TextView mTodayLow;
	ImageView mTodayLowImageView;
	TextView mTodayLowLabel;
	HashMap<Integer, JSONObject> mWeatherInfo = null;
	HashMap<Integer, Integer> mWeatherOrder = null;

	private CircleWeather(Context paramContext) {
		this.mContext = paramContext;
		this.mCurrentId = 0;
		mTopCityId = -1;
		this.mWeatherInfo = new HashMap<Integer, JSONObject>();
		this.mWeatherOrder = new HashMap<Integer, Integer>();
		this.mSetupFlipThreadStarted = false;
		retrieveWeatherInfo();
		prepareCircle(R.layout.weather_circle,
				CircleConsts.WEATHER_BITMAP_SIZE.intValue());
		startWeatherService();
		Resources localResources = this.mContext.getResources();
		this.mCurrentTempTextSize = localResources
				.getDimensionPixelSize(R.dimen.weather_current_temp_size);
		this.mCurrentTempSmallTextSize = localResources
				.getDimensionPixelSize(R.dimen.weather_current_temp_size_small);
	}

	private void fetchWeatherInfoFromJSON(JSONArray jsonarray) {
		int i = jsonarray.length();
		int j = 0;
		while (j < i) {
			try {
				JSONObject jsonobject = jsonarray.getJSONObject(j);
				Integer integer = Integer.valueOf(jsonobject.optInt("id"));
				if (mWeatherInfo.put(integer, jsonobject) == null)
					mWeatherOrder.put(Integer.valueOf(j), integer);
			} catch (JSONException jsonexception) {
				Log.w("Circle", "fetchWeatherInfoFromJSON Info Exception : ",
						jsonexception);
			}
			j++;
		}
	}

	private void flipWeatherToCity(Messenger messenger, Message message,
			float float1, long l) {
		boolean flag;
		boolean flag1;
		if (!mIsFlipped)
			flag = true;
		else
			flag = false;
		mIsFlipped = flag;
		Utility.changeVisibility(messenger, new String[] {
				"circle_weather/weathereffects/pointLight",
				"circle_weather/weathereffects/planetfront",
				"circle_weather/weathereffects/conditionfront",
				"circle_weather/weathereffects/clouds2front" }, new boolean[] {
				false, false, false, false });
		if (!mIsFlipped)
			flag1 = true;
		else
			flag1 = false;
		Utility.flipCircle(messenger, "circle_weather", float1, l, flag1,
				"weather_fling_id");
		preUpdateCircle();
	}

	private Bitmap getCityScreen(int i) {
		boolean flag;
		if (isCitiesAvailable()) {
			int j = -1;
			JSONObject jsonobject;
			try {
				j = ((Integer) mWeatherOrder.get(Integer.valueOf(i)))
						.intValue();
				jsonobject = (JSONObject) mWeatherInfo.get(Integer.valueOf(j));
			} catch (Exception exception) {
				Log.e("Circle",
						(new StringBuilder())
								.append("exception because no data: ")
								.append(exception).toString());
				jsonobject = null;
			}
			if (jsonobject != null) {
				mTopCityId = j;
				mFrontLayout.setVisibility(0);
				mBackLayout.setVisibility(8);
				String s = jsonobject.optString("curr");
				mCurrentTemp.setText(s);
				if (!TextUtils.isEmpty(s)) {
					int l = s.length();
					TextView textview1 = mCurrentTemp;
					int k;
					TextView textview;
					float f;
					if (l > 2)
						f = mCurrentTempSmallTextSize;
					else
						f = mCurrentTempTextSize;
					textview1.setTextSize(0, f);
					mCity.setText(jsonobject.optString("city"));
					k = jsonobject.optInt("error_id");
					flag = false;
					if (k == 0) {
						if (mTodayHigh != null)
							mTodayHigh.setText((new StringBuilder())
									.append(jsonobject.optString("high"))
									.append('\260').toString());
						textview = mTodayLow;
						flag = false;
						if (textview != null)
							mTodayLow.setText((new StringBuilder())
									.append(jsonobject.optString("low"))
									.append('\260').toString());
					}
				}
			} else {
				flag = true;
			}
		} else {
			flag = true;
		}
		if (flag) {
			mFrontLayout.setVisibility(8);
			mBackLayout.setVisibility(0);
			mCurrentId = -1;
		}
		mBitmap.eraseColor(0);
		mLayout.draw(mCanvas);
		return mBitmap;
	}

	public static CircleWeather getInstance(Context paramContext) {
		synchronized (syncObject) {
			if (mInstance == null)
				mInstance = new CircleWeather(paramContext);
			return mInstance;
		}
	}

	private Bitmap getSetupScreen() {
		this.mFrontLayout.setVisibility(8);
		this.mBackLayout.setVisibility(0);
		this.mBitmap.eraseColor(0);
		this.mLayout.draw(this.mCanvas);
		this.mCurrentId = -1;
		return this.mBitmap;
	}

	private WeatherDisplayIds getWeatherDisplayIds() {
		WeatherDisplayIds localWeatherDisplayIds = new WeatherDisplayIds();
		localWeatherDisplayIds.planetId = "circle_weather/weathereffects/planetfront";
		localWeatherDisplayIds.conditionId = "circle_weather/weathereffects/conditionfront";
		localWeatherDisplayIds.condition2Id = "circle_weather/weathereffects/clouds2front";
		if (!this.mIsFlipped) {
			localWeatherDisplayIds.sideId = "circle_weather/circlefront";
			localWeatherDisplayIds.textId = "circle_weather/textfront";
			localWeatherDisplayIds.rainId = "circle_weather/rainfront";
			return localWeatherDisplayIds;
		}
		localWeatherDisplayIds.sideId = "circle_weather/circleback";
		localWeatherDisplayIds.textId = "circle_weather/textback";
		localWeatherDisplayIds.rainId = "circle_weather/rainback";
		return localWeatherDisplayIds;
	}

	public static void hideAllTextures() {
		int i = VISIBILITY_SHAPE_NAMES.length;
		boolean[] arrayOfBoolean = new boolean[i];
		for (int j = 0; j < i; j++)
			arrayOfBoolean[j] = false;
		Utility.changeVisibility(null, VISIBILITY_SHAPE_NAMES, arrayOfBoolean);
		Utility.changeVisibility("circle_weather/textfront", false);
		Utility.changeVisibility("circle_weather/textback", false);
	}

	private boolean isCitiesAvailable() {
		return (this.mWeatherInfo != null) && (!this.mWeatherInfo.isEmpty())
				&& (this.mWeatherOrder != null)
				&& (!this.mWeatherOrder.isEmpty());
	}

	private void retrieveWeatherInfo() {
		SharedPreferences localSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this.mContext);
		String str = localSharedPreferences.getString("weather_info", null);
		if (str == null)
			return;
		try {
			JSONArray localJSONArray = new JSONArray(str);
			if (localJSONArray.length() > 0) {
				fetchWeatherInfoFromJSON(localJSONArray);
				this.mCurrentId = localSharedPreferences.getInt(
						"weather_top_city_id", -1);
			}
			return;
		} catch (Exception localException) {
			Log.w("Circle", "retrieveWeather Info Exception : ", localException);
		}
	}

	public static void showTextures() {
		Utility.changeVisibility(null, VISIBILITY_SHAPE_NAMES,
				mInstance.getShapeVisibilities(mCurrentCondition));
		Utility.changeVisibility("circle_weather/textfront", true);
		Utility.changeVisibility("circle_weather/textback", true);
	}

	private void startFirstTimeFlipThread() {
		new Thread(new Runnable() {
			public void run() {
				CircleWeather.this.mSetupFlipThreadStarted = true;
				try {
					Thread.sleep(1400L);
					CircleWeather.this.flingWeatherCircle(null, null,
							Float.valueOf(1000.0F), 2000L);
					CircleWeather.this.mSetupFlipThreadStarted = false;
					return;
				} catch (InterruptedException localInterruptedException) {
					localInterruptedException.printStackTrace();
				}
			}
		}).start();
	}

	private void startWeatherApp() {
		Intent localIntent = new Intent(
				"com.motorola.weather.action.START_WEATHER_APPLICATION");
		localIntent.setFlags(0x14208000);
		localIntent.putExtra("city_id", mTopCityId);
		try {
			this.mContext.startActivity(localIntent);
			return;
		} catch (Exception localException) {
			Log.e("Circle", "Couldn't start Weather activity" + localException);
			Toast.makeText(
					this.mContext,
					this.mContext.getResources().getString(
							R.string.weather_app_not_installed),
					Toast.LENGTH_LONG).show();
			if (isCitiesAvailable()) {
				this.mWeatherInfo.clear();
				this.mWeatherOrder.clear();
				storeWeatherInfo(null);
				updateCircle();
			}
		}
	}

	private void startWeatherSetup() {
		mTopCityId = -1;
		Intent localIntent = new Intent(
				"com.motorola.weather.action.START_WEATHER_SETTINGS");
		localIntent.setFlags(337641472);
		try {
			this.mContext.startActivity(localIntent);
			return;
		} catch (Exception localException) {
			Log.e("Circle", "Couldn't start Weather activity" + localException);
			Toast.makeText(
					this.mContext,
					this.mContext.getResources().getString(
							R.string.weather_app_not_installed),
					Toast.LENGTH_LONG).show();
		}
	}

	private void storeTopCityId(int paramInt) {
		SharedPreferences.Editor localEditor = PreferenceManager
				.getDefaultSharedPreferences(this.mContext).edit();
		localEditor.putInt("weather_top_city_id", paramInt);
		localEditor.apply();
	}

	private void storeWeatherInfo() {
		JSONArray localJSONArray = new JSONArray();
		if (isCitiesAvailable()) {
			int i = this.mWeatherOrder.size();
			for (int j = 0; j < i; j++) {
				int k = ((Integer) this.mWeatherOrder.get(Integer.valueOf(j)))
						.intValue();
				localJSONArray.put((JSONObject) this.mWeatherInfo.get(Integer
						.valueOf(k)));
			}
		}
		storeWeatherInfo(localJSONArray.toString());
	}

	private void storeWeatherInfo(String paramString) {
		SharedPreferences.Editor localEditor = PreferenceManager
				.getDefaultSharedPreferences(this.mContext).edit();
		localEditor.putString("weather_info", paramString);
		localEditor.apply();
	}

	public void flingWeatherCircle(Messenger messenger, Message message,
			Float float1, long l) {
		int i = mWeatherInfo.size();
		if (float1.floatValue() > 0.0F) {
			if (mCurrentId < i - 1)
				mCurrentId = 1 + mCurrentId;
			else
				mCurrentId = -1;
		} else if (mCurrentId > -1)
			mCurrentId = -1 + mCurrentId;
		else
			mCurrentId = i - 1;
		flipWeatherToCity(messenger, message, float1, l);
	}

	public Bitmap getBackTexture(Bundle paramBundle) {
		return null;
	}

	public Bitmap getFrontTexture(Bundle paramBundle) {
		return null;
	}

	public boolean[] getShapeVisibilities(WeatherCondition weathercondition) {
		boolean flag;
		boolean flag1;
		boolean flag2;
		boolean flag3;
		boolean flag4;
		boolean flag5;
		if (weathercondition.rainName != null)
			flag = true;
		else
			flag = false;
		if (!mIsFlipped && flag)
			flag1 = true;
		else
			flag1 = false;
		if (mIsFlipped && flag)
			flag2 = true;
		else
			flag2 = false;
		if (weathercondition.conditionName != null)
			flag3 = true;
		else
			flag3 = false;
		if (weathercondition.condition2Name != null)
			flag4 = true;
		else
			flag4 = false;
		if (weathercondition.planetName != null)
			flag5 = true;
		else
			flag5 = false;
		return (new boolean[] { flag3, flag4, flag1, flag2, flag5, flag5 });
	}

	public WeatherCondition getWeatherCondition() {
		WeatherCondition localWeatherCondition = new WeatherCondition();

		// 默认情况
		localWeatherCondition.dayNightName = "weather_day";
		localWeatherCondition.planetName = CircleConsts.WEATHER_SUN_ID;
		localWeatherCondition.conditionName = CircleConsts.WEATHER_CLOUDS_ID;
		localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;

		if ((isCitiesAvailable()) && (this.mCurrentId != -1)) {
			try {
				int i = ((Integer) this.mWeatherOrder.get(Integer
						.valueOf(this.mCurrentId))).intValue();
				JSONObject localJSONObject = (JSONObject) this.mWeatherInfo
						.get(Integer.valueOf(i));
				if (localJSONObject != null) {
					switch (localJSONObject.optInt("cond_id")) {
					default:
					case 0:
						break;
					case 1:
						localWeatherCondition.dayNightName = "weather_night";
						localWeatherCondition.planetName = CircleConsts.WEATHER_MOON_ID;
						break;
					case 2:
						localWeatherCondition.dayNightName = "weather_day";
						localWeatherCondition.planetName = CircleConsts.WEATHER_SUN_ID;
						localWeatherCondition.conditionName = CircleConsts.WEATHER_CLOUDS_ID;
						localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
						break;
					case 3:
						localWeatherCondition.dayNightName = "weather_night";
						localWeatherCondition.planetName = CircleConsts.WEATHER_MOON_ID;
						localWeatherCondition.conditionName = CircleConsts.WEATHER_CLOUDS_ID;
						localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
						break;
					case 4:
						localWeatherCondition.dayNightName = "weather_day_cloudy";
						localWeatherCondition.conditionName = CircleConsts.WEATHER_CLOUDS_ID;
						localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
						break;
					case 5:
						localWeatherCondition.dayNightName = "weather_night_cloudy";
						localWeatherCondition.conditionName = CircleConsts.WEATHER_CLOUDS_ID;
						localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
						break;
					case 6:
						localWeatherCondition.dayNightName = "weather_day_cloudy";
						localWeatherCondition.conditionName = CircleConsts.WEATHER_DEFAULT_CLOUDS1_ID;
						localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
						localWeatherCondition.rainName = CircleConsts.WEATHER_RAIN_ID;
						break;
					case 7:
						localWeatherCondition.dayNightName = "weather_night_cloudy";
						localWeatherCondition.conditionName = CircleConsts.WEATHER_DEFAULT_CLOUDS1_ID;
						localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
						localWeatherCondition.rainName = CircleConsts.WEATHER_RAIN_ID;
						break;
					case 8:
						localWeatherCondition.dayNightName = "weather_day_cloudy";
						localWeatherCondition.conditionName = CircleConsts.WEATHER_DEFAULT_CLOUDS1_ID;
						localWeatherCondition.condition2Name = CircleConsts.WEATHER_CLOUDS_LIGHTNING_ID;
						localWeatherCondition.rainName = CircleConsts.WEATHER_RAIN_ID;
						break;
					case 9:
						localWeatherCondition.dayNightName = "weather_night_cloudy";
						localWeatherCondition.conditionName = CircleConsts.WEATHER_DEFAULT_CLOUDS1_ID;
						localWeatherCondition.condition2Name = CircleConsts.WEATHER_CLOUDS_LIGHTNING_ID;
						localWeatherCondition.rainName = CircleConsts.WEATHER_RAIN_ID;
						break;
					case 10:
						localWeatherCondition.dayNightName = "weather_day_cloudy";
						localWeatherCondition.conditionName = CircleConsts.WEATHER_DEFAULT_CLOUDS1_ID;
						localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
						localWeatherCondition.rainName = CircleConsts.WEATHER_SNOW_ID;
						break;
					case 11:
						localWeatherCondition.dayNightName = "weather_night_cloudy";
						localWeatherCondition.conditionName = CircleConsts.WEATHER_DEFAULT_CLOUDS1_ID;
						localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
						localWeatherCondition.rainName = CircleConsts.WEATHER_SNOW_ID;
						break;
					}
				}
			} catch (Exception localException) {
				localException.printStackTrace();
			}
		}

		return localWeatherCondition;
	}

	public WeatherCondition getWeatherConditionDebug(
			WeatherCondition paramWeatherCondition) {
		paramWeatherCondition.planetName = CircleConsts.WEATHER_SUN_ID;
		paramWeatherCondition.dayNightName = "weather_day";
		paramWeatherCondition.rainName = CircleConsts.WEATHER_RAIN_ID;
		paramWeatherCondition.conditionName = CircleConsts.WEATHER_DEFAULT_CLOUDS1_ID;
		paramWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
		return paramWeatherCondition;
	}

	public boolean handleAnimationComplete(String s) {
		boolean flag = s.equals("weather_fling_id");
		boolean flag1 = false;
		if (flag) {
			flag1 = true;
			boolean flag2;
			if (!mIsFlipped)
				flag2 = true;
			else
				flag2 = false;
			Utility.flipCircle(null, "circle_weather/weathereffects", 0.0F, 0L,
					flag2, null);
			(new Thread(new Runnable() {

				public void run() {
					try {
						postUpdateCircle(true);
						Thread.sleep(100L);
						topCityChanged(mCurrentId);
						return;
					} catch (InterruptedException interruptedexception) {
						interruptedexception.printStackTrace();
					}
				}

			})).start();
		}
		return flag1;
	}

	public void handleDestroy() {
		super.handleDestroy();
		mInstance = null;
	}

	public boolean handleFling(Messenger paramMessenger, Message paramMessage,
			Float paramFloat) {
		flingWeatherCircle(paramMessenger, paramMessage, paramFloat, 300L);
		return false;
	}

	public boolean handleSingleTap(Bundle bundle) {
		if (mWeatherOrder.isEmpty() || mCurrentId == -1)
			startWeatherSetup();
		else
			startWeatherApp();
		return true;
	}

	public void handleTopCityChanged(Context paramContext, Intent paramIntent) {
		if (!paramIntent.getBooleanExtra("from_circle", false))
			return;

		int i = paramIntent.getIntExtra("city_id", mTopCityId);
		if (i == mTopCityId || !isCitiesAvailable())
			return;

		int j = this.mWeatherInfo.size();
		boolean found = false;
		for (int k = 0; k < j; k++) {
			if (((Integer) this.mWeatherOrder.get(Integer.valueOf(k)))
					.intValue() == i) {
				this.mCurrentId = k;
				found = true;
				break;
			}
		}

		if (found) {
			flipWeatherToCity(null, null, 1000.0F, 2000L);
		}
	}

	public void postUpdateCircle(boolean flag) {
		WeatherDisplayIds weatherdisplayids = mCurrentDisplayIds;
		WeatherCondition weathercondition = mCurrentCondition;
		if (weathercondition != null && weatherdisplayids != null) {
			if (flag) {
				Utility.playFrames(null, 120, 180, 2500L, "condition_id");
				if (weathercondition.rainName != null) {
					String s = weatherdisplayids.rainId;
					float f = weathercondition.rainName[0];
					float f1;
					if (weathercondition.rainName == CircleConsts.WEATHER_RAIN_ID)
						f1 = 3F;
					else
						f1 = 0.6F;
					Utility.moveTexture(null, s, 0, f - f1,
							weathercondition.rainName[1], 0.0F, 2500L);
				}
			}
			Utility.changeVisibility(null, VISIBILITY_SHAPE_NAMES,
					getShapeVisibilities(weathercondition));
			if (weathercondition.planetName != null)
				Utility.moveTexture(null, weatherdisplayids.planetId, 0,
						weathercondition.planetName[0],
						weathercondition.planetName[1], 0.0F, 0L);
			if (weathercondition.conditionName != null)
				Utility.moveTexture(null, weatherdisplayids.conditionId, 0,
						weathercondition.conditionName[0],
						weathercondition.conditionName[1], 0.0F, 0L);
			if (weathercondition.condition2Name != null) {
				Utility.moveTexture(null, weatherdisplayids.condition2Id, 0,
						weathercondition.condition2Name[0],
						weathercondition.condition2Name[1], 0.0F, 0L);
				return;
			}
		}
	}

	public void preUpdateCircle() {
		mCurrentDisplayIds = getWeatherDisplayIds();
		mCurrentCondition = getWeatherCondition();
		WeatherDisplayIds weatherdisplayids = mCurrentDisplayIds;
		WeatherCondition weathercondition = mCurrentCondition;
		Utility.updateTexture(null, mContext, mThemePackage,
				weatherdisplayids.sideId, weathercondition.dayNightName, true,
				true);
		String s = weatherdisplayids.textId;
		Bitmap bitmap;
		if (mCurrentId != -1)
			bitmap = getCityScreen(mCurrentId);
		else
			bitmap = getSetupScreen();
		Utility.updateTexture(null, s, bitmap);
		Utility.moveTexture(null, weatherdisplayids.sideId, 0, -1F, 0.0F, 0.0F,
				2500L);
		if (weathercondition.rainName != null)
			Utility.moveTexture(null, weatherdisplayids.rainId, 0,
					weathercondition.rainName[0], weathercondition.rainName[1],
					0.0F, 0L);
	}

	public View prepareCircle(int i, int j) {
		int k = 1;
		View view = super.prepareCircle(i, j);
		mFrontLayout = view.findViewById(R.id.weather_front);
		mBackLayout = view.findViewById(R.id.weather_setup);
		mCurrentTemp = (TextView) view.findViewById(R.id.weather_current);
		mCity = (TextView) view.findViewById(R.id.weather_city);
		mTodayHigh = (TextView) view.findViewById(R.id.weather_high);
		mTodayLow = (TextView) view.findViewById(R.id.weather_low);
		mTodayHighLabel = (TextView) view.findViewById(R.id.weather_high_label);
		mTodayLowLabel = (TextView) view.findViewById(R.id.weather_low_label);
		mTodayHighImageView = (ImageView) view
				.findViewById(R.id.weather_high_image);
		mTodayLowImageView = (ImageView) view
				.findViewById(R.id.weather_low_image);
		Resources resources = mContext.getResources();
		String s = resources.getString(R.string.weather_label_high);
		String s1 = resources.getString(R.string.weather_label_low);
		TextView textview;
		byte byte0;
		TextView textview1;
		byte byte1;
		ImageView imageview;
		int l;
		ImageView imageview1;
		int i1;
		if (s.length() <= k && s1.length() <= k)
			k = 0;
		textview = mTodayLowLabel;
		if (k != 0)
			byte0 = 8;
		else
			byte0 = 0;
		textview.setVisibility(byte0);
		textview1 = mTodayHighLabel;
		if (k != 0)
			byte1 = 8;
		else
			byte1 = 0;
		textview1.setVisibility(byte1);
		imageview = mTodayHighImageView;
		if (k != 0)
			l = 0;
		else
			l = 8;
		imageview.setVisibility(l);
		imageview1 = mTodayLowImageView;
		i1 = 0;
		if (k == 0)
			i1 = 8;
		imageview1.setVisibility(i1);
		if (k == 0) {
			mTodayHighLabel.setText(s);
			mTodayLowLabel.setText(s1);
		}
		return view;
	}

	public void sendIntentToGetWeatherInfo() {
		Intent localIntent = new Intent(
				"com.motorola.weather.action.NEED_WEATHER_INFO");
		this.mContext.sendBroadcast(localIntent);
	}

	public void setTheme(String paramString) {
	}

	public void startWeatherService() {
		Intent localIntent = new Intent(
				"com.motorola.weather.action.START_WEATHER_SERVICE");
		try {
			this.mContext.sendBroadcast(localIntent);
			return;
		} catch (Exception localException) {
			Log.e("Circle", "Couldn't stop Weather service" + localException);
		}
	}

	public void stopWeatherService() {
		Intent localIntent = new Intent(
				"com.motorola.weather.action.STOP_WEATHER_SERVICE");
		try {
			this.mContext.sendBroadcast(localIntent);
			return;
		} catch (Exception localException) {
			Log.e("Circle", "Couldn't start Weather service" + localException);
		}
	}

	public void topCityChanged(int paramInt) {
		if (isCitiesAvailable()) {
			Intent localIntent = new Intent(
					"com.motorola.weather.action.TOP_CITY_CHANGED");
			int i = paramInt;
			if (paramInt != -1)
				i = ((Integer) this.mWeatherOrder
						.get(Integer.valueOf(paramInt))).intValue();
			localIntent.putExtra("city_id", i);
			localIntent.putExtra("from_circle", true);
			this.mContext.sendBroadcast(localIntent);
			storeTopCityId(paramInt);
			return;
		}
		Log.e("Circle",
				"Not sending top cityChanged because no city in the map");
	}

	public void updateCircle() {
		updateCircle(false);
	}

	public void updateCircle(Context paramContext, Intent paramIntent) {
		String str = paramIntent.getStringExtra("weather_info");
		int i = paramIntent.getIntExtra("city_id", mTopCityId);
		boolean bool1 = paramIntent.getBooleanExtra("update_all", true);
		if ((str == null) || (bool1))
			;
		try {
			if (this.mWeatherInfo != null)
				this.mWeatherInfo.clear();
			if (this.mWeatherOrder != null)
				this.mWeatherOrder.clear();
			JSONArray localJSONArray = new JSONArray(str);
			if (localJSONArray.length() > 0)
				fetchWeatherInfoFromJSON(localJSONArray);
			storeWeatherInfo();
			if ((bool1) || ((i != -1) && (i != mTopCityId))) {
				boolean bool2 = isCitiesAvailable();
				j = 0;
				if (bool2) {
					int n = this.mWeatherInfo.size();
					i1 = 0;
					j = 0;
					if (i1 < n) {
						if (((Integer) this.mWeatherOrder.get(Integer
								.valueOf(i1))).intValue() != i)
							break label256;
						if ((i == mTopCityId) && (this.mCurrentId == i1)) {
							preUpdateCircle();
							return;
						}
					}
				}
			}
		} catch (Exception localException) {
			label256: do {
				int m;
				do {
					while (true) {
						int i1;
						Log.e("Circle", "Exception " + str);
						continue;
						this.mCurrentId = i1;
						int j = 1;
						if (j == 0)
							break;
						flipWeatherToCity(null, null, Float.valueOf(1000.0F),
								2000L);
						return;
						i1++;
					}
					if (i != -1)
						break;
					int k = this.mCurrentId;
					m = 0;
					if (k != -1) {
						m = 1;
						this.mCurrentId = -1;
					}
					mTopCityId = -1;
				} while (m == 0);
				flipWeatherToCity(null, null, Float.valueOf(1000.0F), 2000L);
				return;
				if (0 == 0) {
					updateCircle();
					return;
				}
			} while ((!isCitiesAvailable()) || (this.mSetupFlipThreadStarted));
			startFirstTimeFlipThread();
		}
	}

	public void updateCircle(boolean paramBoolean) {
		preUpdateCircle();
		postUpdateCircle(paramBoolean);
	}

	public void updateValues(Context paramContext, Intent paramIntent) {
	}

	public static class WeatherCondition {
		float[] condition2Name;
		float[] conditionName;
		String dayNightName;
		float[] planetName;
		float[] rainName;
	}

	public static class WeatherDisplayIds {
		String condition2Id;
		String conditionId;
		String planetId;
		String rainId;
		String sideId;
		String textId;
	}
}

/*
 * Location: J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.widget.circlewidget3d.CircleWeather JD-Core Version: 0.6.2
 */