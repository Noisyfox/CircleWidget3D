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
		this.mWeatherInfo = new HashMap();
		this.mWeatherOrder = new HashMap();
		this.mSetupFlipThreadStarted = false;
		retrieveWeatherInfo();
		prepareCircle(2130903062, CircleConsts.WEATHER_BITMAP_SIZE.intValue());
		startWeatherService();
		Resources localResources = this.mContext.getResources();
		this.mCurrentTempTextSize = localResources
				.getDimensionPixelSize(2131165198);
		this.mCurrentTempSmallTextSize = localResources
				.getDimensionPixelSize(2131165199);
	}

	private void fetchWeatherInfoFromJSON(JSONArray paramJSONArray) {
		int i = paramJSONArray.length();
		int j = 0;
		while (true)
			if (j < i)
				try {
					JSONObject localJSONObject = paramJSONArray
							.getJSONObject(j);
					Integer localInteger = Integer.valueOf(localJSONObject
							.optInt("id"));
					if (this.mWeatherInfo.put(localInteger, localJSONObject) == null)
						this.mWeatherOrder
								.put(Integer.valueOf(j), localInteger);
					j++;
				} catch (JSONException localJSONException) {
					while (true)
						Log.w("Circle",
								"fetchWeatherInfoFromJSON Info Exception : ",
								localJSONException);
				}
	}

	private void flipWeatherToCity(Messenger paramMessenger,
			Message paramMessage, Float paramFloat, long paramLong) {
		boolean bool1;
		float f;
		if (!this.mIsFlipped) {
			bool1 = true;
			this.mIsFlipped = bool1;
			Utility.changeVisibility(paramMessenger, new String[] {
					"circle_weather/weathereffects/pointLight",
					"circle_weather/weathereffects/planetfront",
					"circle_weather/weathereffects/conditionfront",
					"circle_weather/weathereffects/clouds2front" },
					new boolean[] { 0, 0, 0, 0 });
			f = paramFloat.floatValue();
			if (this.mIsFlipped)
				break label108;
		}
		label108: for (boolean bool2 = true;; bool2 = false) {
			Utility.flipCircle(paramMessenger, "circle_weather", f, paramLong,
					bool2, "weather_fling_id");
			preUpdateCircle();
			return;
			bool1 = false;
			break;
		}
	}

	private Bitmap getCityScreen(int paramInt) {
		int j;
		if (isCitiesAvailable())
			j = -1;
		while (true) {
			try {
				j = ((Integer) this.mWeatherOrder
						.get(Integer.valueOf(paramInt))).intValue();
				localJSONObject = (JSONObject) this.mWeatherInfo.get(Integer
						.valueOf(j));
				if (localJSONObject != null) {
					mTopCityId = j;
					this.mFrontLayout.setVisibility(0);
					this.mBackLayout.setVisibility(8);
					String str = localJSONObject.optString("curr");
					this.mCurrentTemp.setText(str);
					if (!TextUtils.isEmpty(str)) {
						int m = str.length();
						TextView localTextView2 = this.mCurrentTemp;
						if (m > 2) {
							f = this.mCurrentTempSmallTextSize;
							localTextView2.setTextSize(0, f);
						}
					} else {
						this.mCity.setText(localJSONObject.optString("city"));
						int k = localJSONObject.optInt("error_id");
						i = 0;
						if (k == 0) {
							if (this.mTodayHigh != null)
								this.mTodayHigh.setText(localJSONObject
										.optString("high") + '°');
							TextView localTextView1 = this.mTodayLow;
							i = 0;
							if (localTextView1 != null)
								this.mTodayLow.setText(localJSONObject
										.optString("low") + '°');
						}
						if (i != 0) {
							this.mFrontLayout.setVisibility(8);
							this.mBackLayout.setVisibility(0);
							this.mCurrentId = -1;
						}
						this.mBitmap.eraseColor(0);
						this.mLayout.draw(this.mCanvas);
						return this.mBitmap;
					}
				}
			} catch (Exception localException) {
				Log.e("Circle", "exception because no data: " + localException);
				JSONObject localJSONObject = null;
				continue;
				float f = this.mCurrentTempTextSize;
				continue;
				i = 1;
				continue;
			}
			int i = 1;
		}
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
		if (str != null)
			;
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
		localIntent.setFlags(337674240);
		localIntent.putExtra("city_id", mTopCityId);
		try {
			this.mContext.startActivity(localIntent);
			return;
		} catch (Exception localException) {
			do {
				Log.e("Circle", "Couldn't start Weather activity"
						+ localException);
				Toast.makeText(this.mContext,
						this.mContext.getResources().getString(2131230739), 1)
						.show();
			} while (!isCitiesAvailable());
			this.mWeatherInfo.clear();
			this.mWeatherOrder.clear();
			storeWeatherInfo(null);
			updateCircle();
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
			Toast.makeText(this.mContext,
					this.mContext.getResources().getString(2131230739), 1)
					.show();
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

	public void flingWeatherCircle(Messenger paramMessenger,
			Message paramMessage, Float paramFloat, long paramLong) {
		int i = this.mWeatherInfo.size();
		if (paramFloat.floatValue() > 0.0F)
			if (this.mCurrentId < i - 1)
				this.mCurrentId = (1 + this.mCurrentId);
		while (true) {
			flipWeatherToCity(paramMessenger, paramMessage, paramFloat,
					paramLong);
			return;
			this.mCurrentId = -1;
			continue;
			if (this.mCurrentId > -1)
				this.mCurrentId = (-1 + this.mCurrentId);
			else
				this.mCurrentId = (i - 1);
		}
	}

	public Bitmap getBackTexture(Bundle paramBundle) {
		return null;
	}

	public Bitmap getFrontTexture(Bundle paramBundle) {
		return null;
	}

	public boolean[] getShapeVisibilities(WeatherCondition paramWeatherCondition)
  {
    int i;
    int j;
    label22: int k;
    label36: int m;
    label46: int n;
    if (paramWeatherCondition.rainName != null)
    {
      i = 1;
      if ((this.mIsFlipped) || (i == 0))
        break label105;
      j = 1;
      if ((!this.mIsFlipped) || (i == 0))
        break label110;
      k = 1;
      if (paramWeatherCondition.conditionName == null)
        break label116;
      m = 1;
      if (paramWeatherCondition.condition2Name == null)
        break label122;
      n = 1;
      label56: if (paramWeatherCondition.planetName == null)
        break label128;
    }
    label128: for (int i1 = 1; ; i1 = 0)
    {
      return new boolean[] { m, n, j, k, i1, i1 };
      i = 0;
      break;
      label105: j = 0;
      break label22;
      label110: k = 0;
      break label36;
      label116: m = 0;
      break label46;
      label122: n = 0;
      break label56;
    }
  }

	public WeatherCondition getWeatherCondition() {
		WeatherCondition localWeatherCondition = new WeatherCondition();
		localWeatherCondition.dayNightName = "weather_day";
		if ((isCitiesAvailable()) && (this.mCurrentId != -1)) {
			try {
				int i = ((Integer) this.mWeatherOrder.get(Integer
						.valueOf(this.mCurrentId))).intValue();
				localJSONObject = (JSONObject) this.mWeatherInfo.get(Integer
						.valueOf(i));
				if (localJSONObject != null)
					;
				switch (localJSONObject.optInt("cond_id")) {
				default:
					return localWeatherCondition;
				case 0:
				case 6:
				case 1:
				case 7:
				case 2:
				case 8:
				case 3:
				case 9:
				case 4:
				case 10:
				case 5:
				case 11:
				}
			} catch (Exception localException) {
				while (true) {
					localWeatherCondition.planetName = CircleConsts.WEATHER_SUN_ID;
					localWeatherCondition.conditionName = CircleConsts.WEATHER_CLOUDS_ID;
					localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
					JSONObject localJSONObject = null;
				}
				localWeatherCondition.dayNightName = "weather_day";
				localWeatherCondition.planetName = CircleConsts.WEATHER_SUN_ID;
				return localWeatherCondition;
			}
			localWeatherCondition.dayNightName = "weather_night";
			localWeatherCondition.planetName = CircleConsts.WEATHER_MOON_ID;
			return localWeatherCondition;
			localWeatherCondition.dayNightName = "weather_day";
			localWeatherCondition.planetName = CircleConsts.WEATHER_SUN_ID;
			localWeatherCondition.conditionName = CircleConsts.WEATHER_CLOUDS_ID;
			localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
			return localWeatherCondition;
			localWeatherCondition.dayNightName = "weather_night";
			localWeatherCondition.planetName = CircleConsts.WEATHER_MOON_ID;
			localWeatherCondition.conditionName = CircleConsts.WEATHER_CLOUDS_ID;
			localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
			return localWeatherCondition;
			localWeatherCondition.dayNightName = "weather_day_cloudy";
			localWeatherCondition.conditionName = CircleConsts.WEATHER_CLOUDS_ID;
			localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
			return localWeatherCondition;
			localWeatherCondition.dayNightName = "weather_night_cloudy";
			localWeatherCondition.conditionName = CircleConsts.WEATHER_CLOUDS_ID;
			localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
			return localWeatherCondition;
			localWeatherCondition.dayNightName = "weather_day_cloudy";
			localWeatherCondition.conditionName = CircleConsts.WEATHER_DEFAULT_CLOUDS1_ID;
			localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
			localWeatherCondition.rainName = CircleConsts.WEATHER_RAIN_ID;
			return localWeatherCondition;
			localWeatherCondition.dayNightName = "weather_night_cloudy";
			localWeatherCondition.conditionName = CircleConsts.WEATHER_DEFAULT_CLOUDS1_ID;
			localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
			localWeatherCondition.rainName = CircleConsts.WEATHER_RAIN_ID;
			return localWeatherCondition;
			localWeatherCondition.dayNightName = "weather_day_cloudy";
			localWeatherCondition.conditionName = CircleConsts.WEATHER_DEFAULT_CLOUDS1_ID;
			localWeatherCondition.condition2Name = CircleConsts.WEATHER_CLOUDS_LIGHTNING_ID;
			localWeatherCondition.rainName = CircleConsts.WEATHER_RAIN_ID;
			return localWeatherCondition;
			localWeatherCondition.dayNightName = "weather_night_cloudy";
			localWeatherCondition.conditionName = CircleConsts.WEATHER_DEFAULT_CLOUDS1_ID;
			localWeatherCondition.condition2Name = CircleConsts.WEATHER_CLOUDS_LIGHTNING_ID;
			localWeatherCondition.rainName = CircleConsts.WEATHER_RAIN_ID;
			return localWeatherCondition;
			localWeatherCondition.dayNightName = "weather_day_cloudy";
			localWeatherCondition.conditionName = CircleConsts.WEATHER_DEFAULT_CLOUDS1_ID;
			localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
			localWeatherCondition.rainName = CircleConsts.WEATHER_SNOW_ID;
			return localWeatherCondition;
			localWeatherCondition.dayNightName = "weather_night_cloudy";
			localWeatherCondition.conditionName = CircleConsts.WEATHER_DEFAULT_CLOUDS1_ID;
			localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
			localWeatherCondition.rainName = CircleConsts.WEATHER_SNOW_ID;
			return localWeatherCondition;
		}
		localWeatherCondition.planetName = CircleConsts.WEATHER_SUN_ID;
		localWeatherCondition.conditionName = CircleConsts.WEATHER_CLOUDS_ID;
		localWeatherCondition.condition2Name = CircleConsts.WEATHER_DEFAULT_CLOUDS2_ID;
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

	public boolean handleAnimationComplete(String paramString) {
		boolean bool1 = paramString.equals("weather_fling_id");
		boolean bool2 = false;
		if (bool1) {
			bool2 = true;
			if (this.mIsFlipped)
				break label57;
		}
		label57: for (boolean bool3 = true;; bool3 = false) {
			Utility.flipCircle(null, "circle_weather/weathereffects", 0.0F, 0L,
					bool3, null);
			new Thread(new Runnable() {
				public void run() {
					try {
						CircleWeather.this.postUpdateCircle(true);
						Thread.sleep(100L);
						CircleWeather.this
								.topCityChanged(CircleWeather.this.mCurrentId);
						return;
					} catch (InterruptedException localInterruptedException) {
						localInterruptedException.printStackTrace();
					}
				}
			}).start();
			return bool2;
		}
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

	public boolean handleSingleTap(Bundle paramBundle) {
		if ((this.mWeatherOrder.isEmpty()) || (this.mCurrentId == -1))
			startWeatherSetup();
		while (true) {
			return true;
			startWeatherApp();
		}
	}

	public void handleTopCityChanged(Context paramContext, Intent paramIntent) {
		if (paramIntent.getBooleanExtra("from_circle", false))
			;
		int i;
		do {
			return;
			i = paramIntent.getIntExtra("city_id", mTopCityId);
		} while ((i == mTopCityId) || (!isCitiesAvailable()));
		int j = this.mWeatherInfo.size();
		for (int k = 0;; k++) {
			int m = 0;
			if (k < j) {
				if (((Integer) this.mWeatherOrder.get(Integer.valueOf(k)))
						.intValue() == i) {
					this.mCurrentId = k;
					m = 1;
				}
			} else {
				if (m == 0)
					break;
				flipWeatherToCity(null, null, Float.valueOf(1000.0F), 2000L);
				return;
			}
		}
	}

	public void postUpdateCircle(boolean paramBoolean) {
		WeatherDisplayIds localWeatherDisplayIds = this.mCurrentDisplayIds;
		WeatherCondition localWeatherCondition = mCurrentCondition;
		if ((localWeatherCondition == null) || (localWeatherDisplayIds == null))
			return;
		String str;
		float f1;
		if (paramBoolean) {
			Utility.playFrames(null, 120, 180, 2500L, "condition_id");
			if (localWeatherCondition.rainName != null) {
				str = localWeatherDisplayIds.rainId;
				f1 = localWeatherCondition.rainName[0];
				if (localWeatherCondition.rainName != CircleConsts.WEATHER_RAIN_ID)
					break label198;
			}
		}
		label198: for (float f2 = 3.0F;; f2 = 0.6F) {
			Utility.moveTexture(null, str, 0, f1 - f2,
					localWeatherCondition.rainName[1], 0.0F, 2500L);
			Utility.changeVisibility(null, VISIBILITY_SHAPE_NAMES,
					getShapeVisibilities(localWeatherCondition));
			if (localWeatherCondition.planetName != null)
				Utility.moveTexture(null, localWeatherDisplayIds.planetId, 0,
						localWeatherCondition.planetName[0],
						localWeatherCondition.planetName[1], 0.0F, 0L);
			if (localWeatherCondition.conditionName != null)
				Utility.moveTexture(null, localWeatherDisplayIds.conditionId,
						0, localWeatherCondition.conditionName[0],
						localWeatherCondition.conditionName[1], 0.0F, 0L);
			if (localWeatherCondition.condition2Name == null)
				break;
			Utility.moveTexture(null, localWeatherDisplayIds.condition2Id, 0,
					localWeatherCondition.condition2Name[0],
					localWeatherCondition.condition2Name[1], 0.0F, 0L);
			return;
		}
	}

	public void preUpdateCircle() {
		this.mCurrentDisplayIds = getWeatherDisplayIds();
		mCurrentCondition = getWeatherCondition();
		WeatherDisplayIds localWeatherDisplayIds = this.mCurrentDisplayIds;
		WeatherCondition localWeatherCondition = mCurrentCondition;
		Utility.updateTexture(null, this.mContext, this.mThemePackage,
				localWeatherDisplayIds.sideId,
				localWeatherCondition.dayNightName, true, true);
		String str = localWeatherDisplayIds.textId;
		if (this.mCurrentId != -1)
			;
		for (Bitmap localBitmap = getCityScreen(this.mCurrentId);; localBitmap = getSetupScreen()) {
			Utility.updateTexture(null, str, localBitmap);
			Utility.moveTexture(null, localWeatherDisplayIds.sideId, 0, -1.0F,
					0.0F, 0.0F, 2500L);
			if (localWeatherCondition.rainName != null)
				Utility.moveTexture(null, localWeatherDisplayIds.rainId, 0,
						localWeatherCondition.rainName[0],
						localWeatherCondition.rainName[1], 0.0F, 0L);
			return;
		}
	}

	public View prepareCircle(int paramInt1, int paramInt2)
  {
    int i = 1;
    View localView = super.prepareCircle(paramInt1, paramInt2);
    this.mFrontLayout = localView.findViewById(2131427390);
    this.mBackLayout = localView.findViewById(2131427391);
    this.mCurrentTemp = ((TextView)localView.findViewById(2131427393));
    this.mCity = ((TextView)localView.findViewById(2131427402));
    this.mTodayHigh = ((TextView)localView.findViewById(2131427398));
    this.mTodayLow = ((TextView)localView.findViewById(2131427401));
    this.mTodayHighLabel = ((TextView)localView.findViewById(2131427396));
    this.mTodayLowLabel = ((TextView)localView.findViewById(2131427399));
    this.mTodayHighImageView = ((ImageView)localView.findViewById(2131427397));
    this.mTodayLowImageView = ((ImageView)localView.findViewById(2131427400));
    Resources localResources = this.mContext.getResources();
    String str1 = localResources.getString(2131230735);
    String str2 = localResources.getString(2131230736);
    int j;
    label215: int k;
    label236: int m;
    label256: ImageView localImageView2;
    int n;
    if ((str1.length() > i) || (str2.length() > i))
    {
      TextView localTextView1 = this.mTodayLowLabel;
      if (i == 0)
        break label313;
      j = 8;
      localTextView1.setVisibility(j);
      TextView localTextView2 = this.mTodayHighLabel;
      if (i == 0)
        break label319;
      k = 8;
      localTextView2.setVisibility(k);
      ImageView localImageView1 = this.mTodayHighImageView;
      if (i == 0)
        break label325;
      m = 0;
      localImageView1.setVisibility(m);
      localImageView2 = this.mTodayLowImageView;
      n = 0;
      if (i == 0)
        break label332;
    }
    while (true)
    {
      localImageView2.setVisibility(n);
      if (i == 0)
      {
        this.mTodayHighLabel.setText(str1);
        this.mTodayLowLabel.setText(str2);
      }
      return localView;
      i = 0;
      break;
      label313: j = 0;
      break label215;
      label319: k = 0;
      break label236;
      label325: m = 8;
      break label256;
      label332: n = 8;
    }
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