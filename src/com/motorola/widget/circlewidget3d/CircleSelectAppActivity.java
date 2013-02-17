package com.motorola.widget.circlewidget3d;

import android.app.ListActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class CircleSelectAppActivity extends ListActivity
{
  String[] mAppList;

  private void setupAdapter()
  {
    Resources localResources = getResources();
    this.mAppList = localResources.getStringArray(2131099649);
    int i = this.mAppList.length;
    ArrayList localArrayList = new ArrayList();
    for (int j = 0; j < i; j++)
    {
      String str = this.mAppList[j];
      int k = 1;
      if ((str.equals(localResources.getString(2131230761))) && (!CircleWidget3DProvider.isDataServiceAvail()))
        k = 0;
      if (k != 0)
        localArrayList.add(str);
    }
    this.mAppList = new String[localArrayList.size()];
    this.mAppList = ((String[])localArrayList.toArray(this.mAppList));
    setListAdapter(new ArrayAdapter(this, 17367043, this.mAppList));
  }

  public void onClickCancel(View paramView)
  {
    finish();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903060);
    setupAdapter();
  }

  public void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    String str = this.mAppList[paramInt];
    Resources localResources = getResources();
    if (str.equals(localResources.getString(2131230758)))
      CircleClock.getInstance(this).startClockApp();
    while (true)
    {
      finish();
      return;
      if (str.equals(localResources.getString(2131230759)))
        CircleWeather.getInstance(this).handleSingleTap(null);
      else if (str.equals(localResources.getString(2131230760)))
        CircleBattery.getInstance(this).startBatteryApp();
      else if (str.equals(localResources.getString(2131230741)))
        Utility.startCircleSettings(this);
      else if (str.equals(localResources.getString(2131230761)))
        CircleData.getInstance(this).startDataUsageApp();
    }
  }
}

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.CircleSelectAppActivity
 * JD-Core Version:    0.6.2
 */