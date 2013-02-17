package com.motorola.widget.circlewidget3d;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import java.util.ArrayList;

import com.motorola.widget.circlewidget3d.BaseAlert.AlertInfo;

public class CircleTest extends Circle {
	private static CircleTest mInstance;
	private ArrayList<BaseAlert.AlertInfo> mAlerts;
	private int mNextAlertId = 0;

	private CircleTest(Context paramContext) {
		this.mContext = paramContext;
		this.mCurrentId = 0;
	}

	private BaseAlert.AlertInfo getAlert(int i) {
		ArrayList<AlertInfo> arraylist = getAlerts();
		if (i < 0 || i >= arraylist.size()) {
			i = mNextAlertId;
			mNextAlertId = i + 1;
		} else {
			mNextAlertId = i + 1;
		}
		if (mNextAlertId >= arraylist.size())
			mNextAlertId = 0;
		return (BaseAlert.AlertInfo) arraylist.get(i);
	}

	private ArrayList<AlertInfo> getAlerts() {
		if (this.mAlerts == null) {
			mAlerts = new ArrayList<AlertInfo>();
			BaseAlert.AlertInfo alertinfo = new BaseAlert.AlertInfo();
			alertinfo.name = "Navin Dabhi";
			alertinfo.number = "408-421-3633";
			alertinfo.timestamp = "342424234";
			alertinfo.description = "This is demo of text msg";
			alertinfo.imageId = Integer.valueOf(R.drawable.ic_circle_widget_alert_text);
			alertinfo.type = Integer.valueOf(3);
			int i = 0 + 1;
			alertinfo.id = Integer.valueOf(0);
			mAlerts.add(alertinfo);
			BaseAlert.AlertInfo alertinfo1 = new BaseAlert.AlertInfo();
			alertinfo1.name = "Charles Mcbrian";
			alertinfo1.number = "510-342-3423";
			alertinfo1.timestamp = "342424234";
			alertinfo1.imageId = Integer.valueOf(R.drawable.ic_circle_widget_alert_text);
			alertinfo1.type = Integer.valueOf(3);
			alertinfo1.description = "You need to work harder.";
			int j = i + 1;
			alertinfo1.id = Integer.valueOf(i);
			mAlerts.add(alertinfo1);
			BaseAlert.AlertInfo alertinfo2 = new BaseAlert.AlertInfo();
			alertinfo2.name = "Michael Jackson";
			alertinfo2.number = "510-342-3423";
			alertinfo2.timestamp = "342424234";
			alertinfo2.imageId = Integer.valueOf(R.drawable.ic_circle_widget_alert_text);
			alertinfo2.type = Integer.valueOf(3);
			alertinfo2.description = "Wanna watch my dance?";
			int k = j + 1;
			alertinfo2.id = Integer.valueOf(j);
			mAlerts.add(alertinfo2);
			BaseAlert.AlertInfo alertinfo3 = new BaseAlert.AlertInfo();
			alertinfo3.name = "Nate Fortin";
			alertinfo3.number = "510-342-3423";
			alertinfo3.timestamp = "342424234";
			alertinfo3.imageId = Integer.valueOf(R.drawable.ic_circle_widget_alert_text);
			alertinfo3.type = Integer.valueOf(3);
			alertinfo3.description = "Do you think we are ready for demo?";
			int l = k + 1;
			alertinfo3.id = Integer.valueOf(k);
			mAlerts.add(alertinfo3);
			BaseAlert.AlertInfo alertinfo4 = new BaseAlert.AlertInfo();
			alertinfo4.name = "Lady Gaga";
			alertinfo4.number = "510-342-3423";
			alertinfo4.timestamp = "342424234";
			alertinfo4.imageId = Integer.valueOf(R.drawable.ic_circle_widget_alert_text);
			alertinfo4.type = Integer.valueOf(3);
			alertinfo4.description = "Don't miss my conert this weekend...";
			int i1 = l + 1;
			alertinfo4.id = Integer.valueOf(l);
			mAlerts.add(alertinfo4);
			BaseAlert.AlertInfo alertinfo5 = new BaseAlert.AlertInfo();
			alertinfo5.name = "IRS says..";
			alertinfo5.number = "510-342-3423";
			alertinfo5.timestamp = "342424234";
			alertinfo5.imageId = Integer.valueOf(R.drawable.ic_circle_widget_alert_text);
			alertinfo5.type = Integer.valueOf(3);
			alertinfo5.description = "You will be paying more taxes this year...";
			// int _tmp = i1 + 1;
			alertinfo5.id = Integer.valueOf(i1);
			mAlerts.add(alertinfo5);
		}
		return this.mAlerts;
	}

	public static CircleTest getInstance(Context paramContext) {
		synchronized (syncObject) {
			if (mInstance == null)
				mInstance = new CircleTest(paramContext);
			return mInstance;
		}
	}

	private void sendDataLevelChangeIntent() {
		Intent localIntent = new Intent(
				"com.motorola.datameter.ACTION_DATA_METER_USAGE_DATA");
		localIntent.putExtra("EXTRA_MAX_DATA", 2.0E+009F);
		localIntent.putExtra("EXTRA_USED_DATA", 1.9E+009F);
		localIntent.putExtra("EXTRA_GREEN_THRESHOLD", 65);
		localIntent.putExtra("EXTRA_YELLOW_THRESHOLD", 90);
		this.mContext.sendBroadcast(localIntent);
	}

	public Bitmap getBackTexture(Bundle paramBundle) {
		return null;
	}

	public Bitmap getFrontTexture(Bundle paramBundle) {
		return null;
	}

	public void handleDestroy() {
	}

	public boolean handleFling(Intent paramIntent) {
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

	public boolean handleFling(Messenger paramMessenger, Message paramMessage,
			Float paramFloat) {
		return false;
	}

	public boolean handleSingleTap(Bundle paramBundle) {
		return false;
	}

	public void setTheme(String paramString) {
	}

	public void showAlert(Intent paramIntent) {
		String str = paramIntent.getStringExtra("alertid");
		if (str != null) {
			try {
				int j = Integer.parseInt(str);
				CircleAlert.addItem(getAlert(j));
				return;
			} catch (NumberFormatException localNumberFormatException) {
			}
		}
	}

	public void updateBattery(Intent paramIntent) {
		String str = paramIntent.getStringExtra("level");
		if (str != null)
			paramIntent.putExtra("level", Integer.parseInt(str));
		CircleBattery.getInstance(null).updateCircle(null, paramIntent);
	}

	public void updateCircle() {
	}

	public void updateCircle(Context paramContext, Intent paramIntent) {
	}

	public void updateDataLevels(Intent paramIntent) {
		CircleData.getInstance(this.mContext).showDummyDataScreen();
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(10000L);
					CircleTest.this.sendDataLevelChangeIntent();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void updateValues(Context paramContext, Intent paramIntent) {
	}
}

/*
 * Location: J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.widget.circlewidget3d.CircleTest JD-Core Version: 0.6.2
 */