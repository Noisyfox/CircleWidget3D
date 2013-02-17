package com.motorola.widget.circlewidget3d;

import android.app.ListActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class CircleSelectAppActivity extends ListActivity {
	String[] mAppList;

	private void setupAdapter() {
		Resources resources = getResources();
		mAppList = resources.getStringArray(0x7f060001);
		int i = mAppList.length;
		ArrayList<String> arraylist = new ArrayList<String>();
		for (int j = 0; j < i; j++) {
			String s = mAppList[j];
			boolean flag = true;
			if (s.equals(resources.getString(0x7f080029))
					&& !CircleWidget3DProvider.isDataServiceAvail())
				flag = false;
			if (flag)
				arraylist.add(s);
		}

		mAppList = new String[arraylist.size()];
		mAppList = (String[]) arraylist.toArray(mAppList);
		setListAdapter(new ArrayAdapter<String>(this, 0x1090003, mAppList));
	}

	public void onClickCancel(View paramView) {
		finish();
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(2130903060);
		setupAdapter();
	}

	public void onListItemClick(ListView paramListView, View paramView,
			int paramInt, long paramLong) {
		String str = this.mAppList[paramInt];
		Resources localResources = getResources();

		if (str.equals(localResources.getString(0x7f080026)))
			CircleClock.getInstance(this).startClockApp();
		else if (str.equals(localResources.getString(0x7f080027)))
			CircleWeather.getInstance(this).handleSingleTap(null);
		else if (str.equals(localResources.getString(0x7f080028)))
			CircleBattery.getInstance(this).startBatteryApp();
		else if (str.equals(localResources.getString(0x7f080015)))
			Utility.startCircleSettings(this);
		else if (str.equals(localResources.getString(0x7f080029)))
			CircleData.getInstance(this).startDataUsageApp();

		finish();
	}
}

/*
 * Location: J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.widget.circlewidget3d.CircleSelectAppActivity JD-Core Version:
 * 0.6.2
 */