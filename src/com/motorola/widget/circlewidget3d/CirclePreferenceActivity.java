package com.motorola.widget.circlewidget3d;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.MenuItem;

public class CirclePreferenceActivity extends PreferenceActivity
{
  public static final String KEY_CIRCLE_FIRST_TIME_LOAD = "first_time_circle_load";
  public static final String KEY_DO_NOT_DISPLAY_GUIDE_TIP_IMAGE = "do_not_display_guide_tip_image";
  public static final String KEY_DO_NOT_DISPLAY_HELP = "do_not_display_help";
  public static final String KEY_ENABLE_DATA_CIRCLE = "show_mobile_data";
  public static final String KEY_ENABLE_GUIDE_ME_CIRCLE = "show_guide_me";
  public static final String KEY_ENABLE_MISSED_CALL = "enable_missed_call_notification";
  public static final String KEY_ENABLE_NOTIFICATIONS = "enable_notifications";
  public static final String KEY_ENABLE_TEXT_MSG = "enable_text_notification";
  public static final String KEY_ENABLE_VOICE_MAIL = "enable_voicemail_notification";
  public static final String KEY_THEME_TITLE = "selected_theme";

  private void adjustOptionsFromConfig()
  {
    PreferenceScreen localPreferenceScreen = getPreferenceScreen();
    if ((localPreferenceScreen != null) && (Config.sSupportedDataMeter == 0))
    {
      Preference localPreference4 = findPreference("show_mobile_data");
      if (localPreference4 != null)
        localPreferenceScreen.removePreference(localPreference4);
    }
    PreferenceCategory localPreferenceCategory = (PreferenceCategory)findPreference("enable_notifications");
    if (localPreferenceCategory != null)
    {
      Log.d("Circle", "Notification group found");
      if (Config.sSupportedTextMsg == 0)
      {
        Preference localPreference3 = findPreference("enable_text_notification");
        if (localPreference3 != null)
          localPreferenceCategory.removePreference(localPreference3);
      }
      if (Config.sSupportedMissedCall == 0)
      {
        Preference localPreference2 = findPreference("enable_missed_call_notification");
        if (localPreference2 != null)
          localPreferenceCategory.removePreference(localPreference2);
      }
      if (Config.sSupportedVoicemail == 0)
      {
        Preference localPreference1 = findPreference("enable_voicemail_notification");
        if (localPreference1 != null)
          localPreferenceCategory.removePreference(localPreference1);
      }
    }
  }

  private void applyCarrierSpecificIcons()
  {
    if (Utility.isVerizonCarrier(this))
    {
      Preference localPreference1 = findPreference("enable_text_notification");
      if (localPreference1 != null)
        localPreference1.setIcon(2130837542);
      Preference localPreference2 = findPreference("enable_voicemail_notification");
      if (localPreference2 != null)
        localPreference2.setIcon(2130837544);
    }
  }

  private void hideDataCircleOptionsIfNeeded()
  {
    Preference localPreference = findPreference("show_mobile_data");
    PreferenceScreen localPreferenceScreen = getPreferenceScreen();
    if ((localPreferenceScreen != null) && (localPreference != null))
    {
      if (!CircleWidget3DProvider.isDataServiceAvail())
        localPreferenceScreen.removePreference(localPreference);
    }
    else
      return;
    localPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
    {
      public boolean onPreferenceChange(Preference paramAnonymousPreference, Object paramAnonymousObject)
      {
        CirclePreferenceActivity.this.setDataMobileSettingValueChanged();
        return true;
      }
    });
  }

  private void setDataMobileSettingValueChanged()
  {
    SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
    localEditor.putBoolean("data_setting_changed", true);
    localEditor.apply();
  }

  private void settingScreenFinish()
  {
    sendBroadcast(new Intent("com.motorola.widget.circlewidget3d.ACTION_SETTING_FINISH"));
  }

  public static void updateDefaultValues(Context paramContext)
  {
    int i = 1;
    SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
    if (Config.sEnableDataMeter == i)
    {
      int k = i;
      localEditor.putBoolean("show_mobile_data", k);
      if (Config.sEnableTextMsg != i)
        break label102;
      int n = i;
      label41: localEditor.putBoolean("enable_text_notification", n);
      if (Config.sEnableMissedCall != i)
        break label108;
      int i2 = i;
      label62: localEditor.putBoolean("enable_missed_call_notification", i2);
      if (Config.sEnableVoicemail != i)
        break label114;
    }
    while (true)
    {
      localEditor.putBoolean("enable_voicemail_notification", i);
      localEditor.apply();
      return;
      int m = 0;
      break;
      label102: int i1 = 0;
      break label41;
      label108: int i3 = 0;
      break label62;
      label114: int j = 0;
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903044);
    addPreferencesFromResource(2130968576);
    if (!Config.isDeviceDataSupported())
      hideDataCircleOptionsIfNeeded();
    if (CircleWidget3DProvider.isConfigAvail())
      adjustOptionsFromConfig();
    getActionBar().setDisplayHomeAsUpEnabled(true);
    applyCarrierSpecificIcons();
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    default:
      return false;
    case 16908332:
    }
    finish();
    return true;
  }

  public boolean onPreferenceTreeClick(PreferenceScreen paramPreferenceScreen, Preference paramPreference)
  {
    CircleCheckin.logAccEventCircleSettingsPreference(paramPreference.getTitle().toString());
    return super.onPreferenceTreeClick(paramPreferenceScreen, paramPreference);
  }

  public void onStop()
  {
    super.onStop();
    settingScreenFinish();
  }
}

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.CirclePreferenceActivity
 * JD-Core Version:    0.6.2
 */