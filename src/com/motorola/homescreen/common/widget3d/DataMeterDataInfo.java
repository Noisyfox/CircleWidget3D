package com.motorola.homescreen.common.widget3d;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import com.motorola.homelib.R.string;
import java.text.DecimalFormat;

public class DataMeterDataInfo
{
  public static final int DIVIDE_NUMBER = 1000;
  public static final int GB = 2;
  public static final int INVALID_DATA = -1000;
  public static final int KB = 0;
  private static final boolean LOGD = true;
  public static final int MB = 1;
  private static final String TAG = "DataInfo";
  public Float data;
  public String dataStr;
  public int unit;
  public String unitStr;

  public static DataMeterDataInfo convertDataInfo(Context paramContext, DataMeterDataInfo paramDataMeterDataInfo, int paramInt)
  {
    Log.d("DataInfo", "convertDataInfo input : " + paramDataMeterDataInfo.data + " type: " + paramInt + " needed type: " + paramInt);
    if (paramInt != paramDataMeterDataInfo.unit)
    {
      if (paramInt != 2)
        break label218;
      if (paramDataMeterDataInfo.unit != 0)
        break label190;
      paramDataMeterDataInfo.data = Float.valueOf(paramDataMeterDataInfo.data.floatValue() / 1000000.0F);
      paramDataMeterDataInfo.unit = 2;
      label88: paramDataMeterDataInfo.unitStr = paramContext.getResources().getStringArray(com.motorola.homelib.R.array.data_units)[paramDataMeterDataInfo.unit];
      if (paramDataMeterDataInfo.data.floatValue() >= 0.1D)
        break label266;
    }
    label266: for (paramDataMeterDataInfo.dataStr = paramContext.getResources().getString(R.string.less_then_1); ; paramDataMeterDataInfo.dataStr = getFormattedLimitStr(paramDataMeterDataInfo.data, paramDataMeterDataInfo.unit))
    {
      Log.d("DataInfo", "convertDataInfo output: " + paramDataMeterDataInfo.data + " type: " + paramDataMeterDataInfo.unit + " str: " + paramDataMeterDataInfo.dataStr);
      return paramDataMeterDataInfo;
      label190: if (paramDataMeterDataInfo.unit != 1)
        break;
      paramDataMeterDataInfo.data = Float.valueOf(paramDataMeterDataInfo.data.floatValue() / 1000.0F);
      break;
      label218: if (paramInt != 1)
        break label88;
      if (paramDataMeterDataInfo.unit == 0)
        paramDataMeterDataInfo.data = Float.valueOf(paramDataMeterDataInfo.data.floatValue() / 1000.0F);
      while (true)
      {
        paramDataMeterDataInfo.unit = 1;
        break;
        if (paramDataMeterDataInfo.unit != 2);
      }
    }
  }

  public static DataMeterDataInfo getDataInfo(Context paramContext, Float paramFloat)
  {
    Log.d("DataInfo", "getDataInfo : " + paramFloat);
    DataMeterDataInfo localDataMeterDataInfo = new DataMeterDataInfo();
    if (paramFloat.floatValue() != -1.0F)
    {
      paramFloat = Float.valueOf(paramFloat.floatValue() / 1000.0F);
      localDataMeterDataInfo.unit = 0;
      if (paramFloat.floatValue() >= 1000.0F)
      {
        localDataMeterDataInfo.unit = 1;
        paramFloat = Float.valueOf(paramFloat.floatValue() / 1000.0F);
        if (paramFloat.floatValue() >= 1000.0F)
        {
          localDataMeterDataInfo.unit = 2;
          paramFloat = Float.valueOf(paramFloat.floatValue() / 1000.0F);
        }
      }
    }
    localDataMeterDataInfo.unitStr = paramContext.getResources().getStringArray(com.motorola.homelib.R.array.data_units)[localDataMeterDataInfo.unit];
    localDataMeterDataInfo.data = paramFloat;
    if (localDataMeterDataInfo.data.floatValue() != -1.0F)
      localDataMeterDataInfo.dataStr = getFormattedLimitStr(localDataMeterDataInfo.data, localDataMeterDataInfo.unit);
    return localDataMeterDataInfo;
  }

  private static String getFormattedLimitStr(Float paramFloat, int paramInt)
  {
    if ((paramFloat.floatValue() >= 100.0F) || (paramInt == 1));
    for (String str = Integer.toString(paramFloat.intValue()); ; str = new DecimalFormat("#.##").format(paramFloat))
    {
      Log.d("DataInfo", "getFormattedLimitStr: limit: " + paramFloat + " limitStr: " + str);
      return str;
    }
  }
}

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.homescreen.common.widget3d.DataMeterDataInfo
 * JD-Core Version:    0.6.2
 */