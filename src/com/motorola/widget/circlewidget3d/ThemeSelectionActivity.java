package com.motorola.widget.circlewidget3d;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ThemeSelectionActivity extends Activity
  implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener
{
  public static final String THEME_ACTION = "com.motorola.circlewidget3d.THEME";
  public String mSelectedThemePkg;
  final ArrayList<String> mThemePackages = new ArrayList();
  ImageView mThemePreviewView;

  private void changeTheme(String paramString)
  {
    Intent localIntent = new Intent("com.motorola.widget.circlewidget3d.ACTION_CHANGE_THEME");
    if (!TextUtils.isEmpty(paramString))
      localIntent.putExtra("theme_pkg", paramString);
    sendBroadcast(localIntent);
    SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
    localEditor.putString("current_theme", paramString);
    localEditor.apply();
  }

  private void createGalleryItems()
  {
    Gallery localGallery = (Gallery)findViewById(2131427384);
    localGallery.setCallbackDuringFling(false);
    localGallery.setOnItemSelectedListener(this);
    localGallery.setAdapter(new ThemeImageAdapter(this));
  }

  private void prepareThemeInfo()
  {
    final PackageManager localPackageManager = getPackageManager();
    Comparator local3 = new Comparator()
    {
      public int compare(ResolveInfo paramAnonymousResolveInfo1, ResolveInfo paramAnonymousResolveInfo2)
      {
        return paramAnonymousResolveInfo1.loadLabel(localPackageManager).toString().compareTo(paramAnonymousResolveInfo2.loadLabel(localPackageManager).toString());
      }
    };
    List localList = localPackageManager.queryBroadcastReceivers(new Intent("com.motorola.circlewidget3d.THEME"), 0);
    int i = localList.size();
    this.mThemePackages.add("com.motorola.widget.circlewidget3d");
    if (i > 0)
    {
      Collections.sort(localList, local3);
      for (int j = 0; j < i; j++)
      {
        ResolveInfo localResolveInfo = (ResolveInfo)localList.get(j);
        if (localResolveInfo != null)
        {
          String str = localResolveInfo.activityInfo.packageName;
          this.mThemePackages.add(str);
        }
      }
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903061);
    this.mThemePreviewView = ((ImageView)findViewById(2131427389));
    prepareThemeInfo();
    createGalleryItems();
    ((Button)findViewById(2131427387)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        ThemeSelectionActivity.this.changeTheme(ThemeSelectionActivity.this.mSelectedThemePkg);
        ThemeSelectionActivity.this.finish();
      }
    });
    ((Button)findViewById(2131427386)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        ThemeSelectionActivity.this.finish();
      }
    });
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
  }

  public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    String str = (String)this.mThemePackages.get(paramInt);
    this.mSelectedThemePkg = str;
    Drawable localDrawable = ThemeInfo.getDrawable(this, str, "theme_preview_circle_widget");
    if (localDrawable != null)
      this.mThemePreviewView.setImageDrawable(localDrawable);
  }

  public void onNothingSelected(AdapterView<?> paramAdapterView)
  {
  }

  private class ThemeImageAdapter extends BaseAdapter
  {
    Context mContext;
    private LayoutInflater mLayoutInflater;

    ThemeImageAdapter(Activity arg2)
    {
      Object localObject;
      this.mLayoutInflater = localObject.getLayoutInflater();
      this.mContext = localObject;
    }

    public int getCount()
    {
      return ThemeSelectionActivity.this.mThemePackages.size();
    }

    public Object getItem(int paramInt)
    {
      return Integer.valueOf(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      View localView;
      String str2;
      TextView localTextView;
      if (paramView == null)
      {
        localView = this.mLayoutInflater.inflate(2130903053, paramViewGroup, false);
        ImageView localImageView = (ImageView)localView.findViewById(2131427364);
        String str1 = (String)ThemeSelectionActivity.this.mThemePackages.get(paramInt);
        Drawable localDrawable = ThemeInfo.getDrawable(this.mContext, str1, "theme_preview_circle_widget");
        if (localDrawable != null)
          localImageView.setImageDrawable(localDrawable);
        str2 = ThemeInfo.getText(this.mContext, str1, "theme_name");
        localTextView = (TextView)localView.findViewById(2131427365);
        if (str2 == null)
          break label116;
      }
      while (true)
      {
        localTextView.setText(str2);
        return localView;
        localView = paramView;
        break;
        label116: str2 = "";
      }
    }
  }
}

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.ThemeSelectionActivity
 * JD-Core Version:    0.6.2
 */