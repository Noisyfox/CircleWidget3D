package com.motorola.widget.circlewidget3d;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import java.util.ArrayList;

public class CircleTest extends Circle
{
  private static CircleTest mInstance;
  private ArrayList<BaseAlert.AlertInfo> mAlerts;
  private int mNextAlertId = 0;

  private CircleTest(Context paramContext)
  {
    this.mContext = paramContext;
    this.mCurrentId = 0;
  }

  private BaseAlert.AlertInfo getAlert(int paramInt)
  {
    ArrayList localArrayList = getAlerts();
    if ((paramInt < 0) || (paramInt >= localArrayList.size()))
      paramInt = this.mNextAlertId;
    for (this.mNextAlertId = (paramInt + 1); ; this.mNextAlertId = (paramInt + 1))
    {
      if (this.mNextAlertId >= localArrayList.size())
        this.mNextAlertId = 0;
      return (BaseAlert.AlertInfo)localArrayList.get(paramInt);
    }
  }

  private ArrayList<BaseAlert.AlertInfo> getAlerts()
  {
    if (this.mAlerts == null)
    {
      this.mAlerts = new ArrayList();
      BaseAlert.AlertInfo localAlertInfo1 = new BaseAlert.AlertInfo();
      localAlertInfo1.name = "Navin Dabhi";
      localAlertInfo1.number = "408-421-3633";
      localAlertInfo1.timestamp = "342424234";
      localAlertInfo1.description = "This is demo of text msg";
      localAlertInfo1.imageId = Integer.valueOf(2130837541);
      localAlertInfo1.type = Integer.valueOf(3);
      int i = 0 + 1;
      localAlertInfo1.id = Integer.valueOf(0);
      this.mAlerts.add(localAlertInfo1);
      BaseAlert.AlertInfo localAlertInfo2 = new BaseAlert.AlertInfo();
      localAlertInfo2.name = "Charles Mcbrian";
      localAlertInfo2.number = "510-342-3423";
      localAlertInfo2.timestamp = "342424234";
      localAlertInfo2.imageId = Integer.valueOf(2130837541);
      localAlertInfo2.type = Integer.valueOf(3);
      localAlertInfo2.description = "You need to work harder.";
      int j = i + 1;
      localAlertInfo2.id = Integer.valueOf(i);
      this.mAlerts.add(localAlertInfo2);
      BaseAlert.AlertInfo localAlertInfo3 = new BaseAlert.AlertInfo();
      localAlertInfo3.name = "Michael Jackson";
      localAlertInfo3.number = "510-342-3423";
      localAlertInfo3.timestamp = "342424234";
      localAlertInfo3.imageId = Integer.valueOf(2130837541);
      localAlertInfo3.type = Integer.valueOf(3);
      localAlertInfo3.description = "Wanna watch my dance?";
      int k = j + 1;
      localAlertInfo3.id = Integer.valueOf(j);
      this.mAlerts.add(localAlertInfo3);
      BaseAlert.AlertInfo localAlertInfo4 = new BaseAlert.AlertInfo();
      localAlertInfo4.name = "Nate Fortin";
      localAlertInfo4.number = "510-342-3423";
      localAlertInfo4.timestamp = "342424234";
      localAlertInfo4.imageId = Integer.valueOf(2130837541);
      localAlertInfo4.type = Integer.valueOf(3);
      localAlertInfo4.description = "Do you think we are ready for demo?";
      int m = k + 1;
      localAlertInfo4.id = Integer.valueOf(k);
      this.mAlerts.add(localAlertInfo4);
      BaseAlert.AlertInfo localAlertInfo5 = new BaseAlert.AlertInfo();
      localAlertInfo5.name = "Lady Gaga";
      localAlertInfo5.number = "510-342-3423";
      localAlertInfo5.timestamp = "342424234";
      localAlertInfo5.imageId = Integer.valueOf(2130837541);
      localAlertInfo5.type = Integer.valueOf(3);
      localAlertInfo5.description = "Don't miss my conert this weekend...";
      int n = m + 1;
      localAlertInfo5.id = Integer.valueOf(m);
      this.mAlerts.add(localAlertInfo5);
      BaseAlert.AlertInfo localAlertInfo6 = new BaseAlert.AlertInfo();
      localAlertInfo6.name = "IRS says..";
      localAlertInfo6.number = "510-342-3423";
      localAlertInfo6.timestamp = "342424234";
      localAlertInfo6.imageId = Integer.valueOf(2130837541);
      localAlertInfo6.type = Integer.valueOf(3);
      localAlertInfo6.description = "You will be paying more taxes this year...";
      (n + 1);
      localAlertInfo6.id = Integer.valueOf(n);
      this.mAlerts.add(localAlertInfo6);
    }
    return this.mAlerts;
  }

  public static CircleTest getInstance(Context paramContext)
  {
    synchronized (syncObject)
    {
      if (mInstance == null)
        mInstance = new CircleTest(paramContext);
      return mInstance;
    }
  }

  private void sendDataLevelChangeIntent()
  {
    Intent localIntent = new Intent("com.motorola.datameter.ACTION_DATA_METER_USAGE_DATA");
    localIntent.putExtra("EXTRA_MAX_DATA", 2.0E+009F);
    localIntent.putExtra("EXTRA_USED_DATA", 1.9E+009F);
    localIntent.putExtra("EXTRA_GREEN_THRESHOLD", 65);
    localIntent.putExtra("EXTRA_YELLOW_THRESHOLD", 90);
    this.mContext.sendBroadcast(localIntent);
  }

  public Bitmap getBackTexture(Bundle paramBundle)
  {
    return null;
  }

  public Bitmap getFrontTexture(Bundle paramBundle)
  {
    return null;
  }

  public void handleDestroy()
  {
  }

  public boolean handleFling(Intent paramIntent)
  {
    String str = paramIntent.getStringExtra("shape_name");
    Bundle localBundle = new Bundle();
    localBundle.putString("shape_name", str);
    localBundle.putFloat("velocity_x", 303.0F);
    localBundle.putFloat("velocity_y", 800.0F);
    Message localMessage = new Message();
    localMessage.setData(localBundle);
    CircleWidget3DProvider.handleFling(null, localMessage);
    return false;
  }

  public boolean handleFling(Messenger paramMessenger, Message paramMessage, Float paramFloat)
  {
    return false;
  }

  public boolean handleSingleTap(Bundle paramBundle)
  {
    return false;
  }

  public void setTheme(String paramString)
  {
  }

  public void showAlert(Intent paramIntent)
  {
    String str = paramIntent.getStringExtra("alertid");
    int i = -1;
    if (str != null);
    try
    {
      int j = Integer.parseInt(str);
      i = j;
      label22: CircleAlert.addItem(getAlert(i));
      return;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      break label22;
    }
  }

  public void updateBattery(Intent paramIntent)
  {
    String str = paramIntent.getStringExtra("level");
    if (str != null)
      paramIntent.putExtra("level", Integer.parseInt(str));
    CircleBattery.getInstance(null).updateCircle(null, paramIntent);
  }

  public void updateCircle()
  {
  }

  public void updateCircle(Context paramContext, Intent paramIntent)
  {
  }

  public void updateDataLevels(Intent paramIntent)
  {
    CircleData.getInstance(this.mContext).showDummyDataScreen();
    new Thread(new Runnable()
    {
      public void run()
      {
        try
        {
          Thread.sleep(10000L);
          CircleTest.this.sendDataLevelChangeIntent();
          return;
        }
        catch (Exception localException)
        {
          localException = localException;
          localException.printStackTrace();
          return;
        }
        finally
        {
        }
      }
    }).start();
  }

  public void updateValues(Context paramContext, Intent paramIntent)
  {
  }
}

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.CircleTest
 * JD-Core Version:    0.6.2
 */