package com.motorola.homescreen.common.widget3d;

import android.content.Context;
import android.util.Log;
import java.text.DecimalFormat;

public class DataMeterDataInfo {
	public static final int DIVIDE_NUMBER = 1000;
	public static final int GB = 2;
	public static final int INVALID_DATA = -1000;
	public static final int KB = 0;
	// private static final boolean LOGD = true;
	public static final int MB = 1;
	private static final String TAG = "DataInfo";
	public Float data;
	public String dataStr;
	public int unit;
	public String unitStr;

	public static DataMeterDataInfo convertDataInfo(Context context,
			DataMeterDataInfo datameterdatainfo, int i) {
		Log.d(TAG, (new StringBuilder()).append("convertDataInfo input : ")
				.append(datameterdatainfo.data).append(" type: ").append(i)
				.append(" needed type: ").append(i).toString());
		if (i != datameterdatainfo.unit) {
			if (i == 2) {
				if (datameterdatainfo.unit == 0)
					datameterdatainfo.data = Float
							.valueOf(datameterdatainfo.data.floatValue() / 1000000F);
				else if (datameterdatainfo.unit == 1)
					datameterdatainfo.data = Float
							.valueOf(datameterdatainfo.data.floatValue() / 1000F);
				datameterdatainfo.unit = 2;
			} else if (i == 1) {
				if (datameterdatainfo.unit == 0)
					datameterdatainfo.data = Float
							.valueOf(datameterdatainfo.data.floatValue() / 1000F);
				else if (datameterdatainfo.unit != 2)
					;
				datameterdatainfo.unit = 1;
			}
			datameterdatainfo.unitStr = context.getResources().getStringArray(
					com.motorola.homelib.R.array.data_units)[datameterdatainfo.unit];
			if ((double) datameterdatainfo.data.floatValue() < 0.10000000000000001D)
				datameterdatainfo.dataStr = context.getResources().getString(
						com.motorola.homelib.R.string.less_then_1);
			else
				datameterdatainfo.dataStr = getFormattedLimitStr(
						datameterdatainfo.data, datameterdatainfo.unit);
		}
		Log.d(TAG,
				(new StringBuilder()).append("convertDataInfo output: ")
						.append(datameterdatainfo.data).append(" type: ")
						.append(datameterdatainfo.unit).append(" str: ")
						.append(datameterdatainfo.dataStr).toString());
		return datameterdatainfo;
	}

	public static DataMeterDataInfo getDataInfo(Context paramContext,
			Float paramFloat) {
		Log.d(TAG, "getDataInfo : " + paramFloat);
		DataMeterDataInfo localDataMeterDataInfo = new DataMeterDataInfo();
		if (paramFloat.floatValue() != -1.0F) {
			paramFloat = Float.valueOf(paramFloat.floatValue() / 1000.0F);
			localDataMeterDataInfo.unit = 0;
			if (paramFloat.floatValue() >= 1000.0F) {
				localDataMeterDataInfo.unit = 1;
				paramFloat = Float.valueOf(paramFloat.floatValue() / 1000.0F);
				if (paramFloat.floatValue() >= 1000.0F) {
					localDataMeterDataInfo.unit = 2;
					paramFloat = Float
							.valueOf(paramFloat.floatValue() / 1000.0F);
				}
			}
		}
		localDataMeterDataInfo.unitStr = paramContext.getResources()
				.getStringArray(com.motorola.homelib.R.array.data_units)[localDataMeterDataInfo.unit];
		localDataMeterDataInfo.data = paramFloat;
		if (localDataMeterDataInfo.data.floatValue() != -1.0F)
			localDataMeterDataInfo.dataStr = getFormattedLimitStr(
					localDataMeterDataInfo.data, localDataMeterDataInfo.unit);
		return localDataMeterDataInfo;
	}

	private static String getFormattedLimitStr(Float float1, int i) {
		String s;
		if (float1.floatValue() >= 100F || i == 1)
			s = Integer.toString(float1.intValue());
		else
			s = (new DecimalFormat("#.##")).format(float1);
		Log.d(TAG, "getFormattedLimitStr: limit: " + float1 + " limitStr: " + s);
		return s;
	}
}

/*
 * Location: J:\鎶�湳鏂囨。\瀹夊崜鍥轰欢鐩稿叧\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.homescreen.common.widget3d.DataMeterDataInfo JD-Core Version:
 * 0.6.2
 */