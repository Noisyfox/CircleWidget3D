package com.motorola.widget.circlewidget3d;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ThemeSelectionActivity extends Activity implements
		AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
	public static final String THEME_ACTION = "com.motorola.circlewidget3d.THEME";
	public String mSelectedThemePkg;
	final ArrayList<String> mThemePackages = new ArrayList<String>();
	ImageView mThemePreviewView;

	private void changeTheme(String paramString) {
		Intent localIntent = new Intent(
				"com.motorola.widget.circlewidget3d.ACTION_CHANGE_THEME");
		if (!TextUtils.isEmpty(paramString))
			localIntent.putExtra("theme_pkg", paramString);
		sendBroadcast(localIntent);
		SharedPreferences.Editor localEditor = PreferenceManager
				.getDefaultSharedPreferences(this).edit();
		localEditor.putString("current_theme", paramString);
		localEditor.apply();
	}

	private void createGalleryItems() {
		Gallery localGallery = (Gallery) findViewById(R.id.theme_gallery);
		localGallery.setCallbackDuringFling(false);
		localGallery.setOnItemSelectedListener(this);
		localGallery.setAdapter(new ThemeImageAdapter(this));
	}

	private void prepareThemeInfo() {
		final PackageManager localPackageManager = getPackageManager();
		Comparator<ResolveInfo> local3 = new Comparator<ResolveInfo>() {
			public int compare(ResolveInfo paramAnonymousResolveInfo1,
					ResolveInfo paramAnonymousResolveInfo2) {
				return paramAnonymousResolveInfo1
						.loadLabel(localPackageManager)
						.toString()
						.compareTo(
								paramAnonymousResolveInfo2.loadLabel(
										localPackageManager).toString());
			}
		};
		List<ResolveInfo> localList = localPackageManager
				.queryBroadcastReceivers(new Intent(
						"com.motorola.circlewidget3d.THEME"), 0);
		int i = localList.size();
		this.mThemePackages.add("com.motorola.widget.circlewidget3d");
		if (i > 0) {
			Collections.sort(localList, local3);
			for (int j = 0; j < i; j++) {
				ResolveInfo localResolveInfo = (ResolveInfo) localList.get(j);
				if (localResolveInfo != null) {
					String str = localResolveInfo.activityInfo.packageName;
					this.mThemePackages.add(str);
				}
			}
		}
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.theme_selection);
		this.mThemePreviewView = ((ImageView) findViewById(R.id.theme_preview_image));
		prepareThemeInfo();
		createGalleryItems();
		((Button) findViewById(R.id.theme_select_button))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View paramAnonymousView) {
						ThemeSelectionActivity.this
								.changeTheme(ThemeSelectionActivity.this.mSelectedThemePkg);
						ThemeSelectionActivity.this.finish();
					}
				});
		((Button) findViewById(R.id.theme_cancel))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View paramAnonymousView) {
						ThemeSelectionActivity.this.finish();
					}
				});
	}

	public void onItemClick(AdapterView<?> paramAdapterView, View paramView,
			int paramInt, long paramLong) {
	}

	public void onItemSelected(AdapterView<?> paramAdapterView, View paramView,
			int paramInt, long paramLong) {
		String str = (String) this.mThemePackages.get(paramInt);
		this.mSelectedThemePkg = str;
		Drawable localDrawable = ThemeInfo.getDrawable(this, str,
				"theme_preview_circle_widget");
		if (localDrawable != null)
			this.mThemePreviewView.setImageDrawable(localDrawable);
	}

	public void onNothingSelected(AdapterView<?> paramAdapterView) {
	}

	private class ThemeImageAdapter extends BaseAdapter {
		Context mContext;
		private LayoutInflater mLayoutInflater;

		ThemeImageAdapter(Activity activity) {
			mLayoutInflater = activity.getLayoutInflater();
			mContext = activity;
		}

		public int getCount() {
			return ThemeSelectionActivity.this.mThemePackages.size();
		}

		public Object getItem(int paramInt) {
			return Integer.valueOf(paramInt);
		}

		public long getItemId(int paramInt) {
			return paramInt;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			View view1;
			ImageView imageview;
			String s;
			android.graphics.drawable.Drawable drawable;
			String s1;
			TextView textview;
			if (view == null)
				view1 = mLayoutInflater.inflate(R.layout.gallery_item, viewgroup, false);
			else
				view1 = view;
			imageview = (ImageView) view1.findViewById(R.id.gallery_image);
			s = (String) mThemePackages.get(i);
			drawable = ThemeInfo.getDrawable(mContext, s,
					"theme_preview_circle_widget");
			if (drawable != null)
				imageview.setImageDrawable(drawable);
			s1 = ThemeInfo.getText(mContext, s, "theme_name");
			textview = (TextView) view1.findViewById(R.id.gallery_text);
			if (s1 == null)
				s1 = "";
			textview.setText(s1);
			return view1;
		}
	}
}

/*
 * Location: J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.widget.circlewidget3d.ThemeSelectionActivity JD-Core Version:
 * 0.6.2
 */