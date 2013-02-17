package com.motorola.homescreen.common.widget3d;

public abstract interface CircleWeatherConsts {
	public static final String ACTION_NEED_WEATHER_INFO = "com.motorola.weather.action.NEED_WEATHER_INFO";
	public static final String ACTION_NEW_WEATHER_INFO = "com.motorola.weather.action.NEW_WEATHER_INFO";
	public static final String ACTION_START_WEATHER_APPLICATION = "com.motorola.weather.action.START_WEATHER_APPLICATION";
	public static final String ACTION_START_WEATHER_SERVICE = "com.motorola.weather.action.START_WEATHER_SERVICE";
	public static final String ACTION_START_WEATHER_SETTINGS = "com.motorola.weather.action.START_WEATHER_SETTINGS";
	public static final String ACTION_STOP_WEATHER_SERVICE = "com.motorola.weather.action.STOP_WEATHER_SERVICE";
	public static final String ACTION_TOP_CITY_CHANGED = "com.motorola.weather.action.TOP_CITY_CHANGED";
	public static final String CITY_ID = "id";
	public static final int CITY_ID_SETUP = -1;
	public static final String CITY_NAME = "city";
	public static final int COND_CLEAR_DAY = 0;
	public static final int COND_CLEAR_NIGHT = 6;
	public static final int COND_CLOUDY_DAY = 2;
	public static final int COND_CLOUDY_NIGHT = 8;
	public static final int COND_PARTLY_CLOUDY_DAY = 1;
	public static final int COND_PARTLY_CLOUDY_NIGHT = 7;
	public static final int COND_RAINING_DAY = 3;
	public static final int COND_RAINING_NIGHT = 9;
	public static final int COND_SNOWING_DAY = 5;
	public static final int COND_SNOWING_NIGHT = 11;
	public static final int COND_THUNDER_DAY = 4;
	public static final int COND_THUNDER_NIGHT = 10;
	public static final String CURRENT_CONDITION = "cond";
	public static final String CURRENT_CONDITION_ID = "cond_id";
	public static final String CURRENT_TEMP = "curr";
	public static final int ERROR_DATA = 2;
	public static final int ERROR_GPS = 1;
	public static final String ERROR_ID = "error_id";
	public static final int ERROR_NONE = 0;
	public static final String EXTRA_CITY_ID = "city_id";
	public static final String EXTRA_UPDATE_ALL = "update_all";
	public static final String EXTRA_WEATHER_INFO = "weather_info";
	public static final String TEMP_UNIT = "unit";
	public static final String TODAY_HIGH = "high";
	public static final String TODAY_LOW = "low";
	public static final int UNIT_CELSIUS = 0;
	public static final int UNIT_FAHRENHEIT = 1;
	public static final int VERSION = 2;
}

/*
 * Location: J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.homescreen.common.widget3d.CircleWeatherConsts JD-Core Version:
 * 0.6.2
 */