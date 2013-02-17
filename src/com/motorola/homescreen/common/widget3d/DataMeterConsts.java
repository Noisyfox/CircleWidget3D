package com.motorola.homescreen.common.widget3d;

public abstract interface DataMeterConsts {
	public static final String ACTION_DATA_METER_ERROR = "com.motorola.datameter.ACTION_DATA_METER_ERROR";
	public static final String ACTION_DATA_METER_USAGE_DATA = "com.motorola.datameter.ACTION_DATA_METER_USAGE_DATA";
	public static final String ACTION_FETCH_DATA_USAGE = "com.motorola.datameter.ACTION_FETCH_DATA_USAGE";
	public static final String ACTION_SHOW_DATA_USAGE = "com.motorola.datameter.ACTION_SHOW_DATA_USAGE";
	public static final String ACTION_START_DATA_METER_SERVICE = "com.motorola.datameter.ACTION_START_DATA_METER_SERVICE";
	public static final String ACTION_STOP_DATA_METER_SERVICE = "com.motorola.datameter.ACTION_STOP_DATA_METER_SERVICE";
	public static final int ERROR_CRITICAL = 2;
	public static final int ERROR_WARNING = 1;
	public static final String EXTRA_CLIENT_NAME = "EXTRA_CLIENT_NAME";
	public static final String EXTRA_ERROR = "EXTRA_ERROR";
	public static final String EXTRA_GREEN_THRESHOLD = "EXTRA_GREEN_THRESHOLD";
	public static final String EXTRA_MAX_DATA = "EXTRA_MAX_DATA";
	public static final String EXTRA_USED_DATA = "EXTRA_USED_DATA";
	public static final String EXTRA_YELLOW_THRESHOLD = "EXTRA_YELLOW_THRESHOLD";
	public static final float MAX_DATA_UNLIMITED = -1.0F;
	public static final String PERMISSION_READ_DATA_METER = "com.motorola.datameter.PERMISSION_READ_DATA_METER";
}

/*
 * Location: J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.homescreen.common.widget3d.DataMeterConsts JD-Core Version:
 * 0.6.2
 */