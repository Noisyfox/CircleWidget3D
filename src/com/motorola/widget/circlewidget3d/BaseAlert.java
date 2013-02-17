package com.motorola.widget.circlewidget3d;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public abstract class BaseAlert {
	public static final Object mSyncObject = new Object();
	public Context mContext;

	abstract AlertInfo addItem(Bundle paramBundle);

	public abstract Intent getAlertAppIntent();

	public void retrieveStrings(Context paramContext) {
	}

	public static class AlertInfo {
		String description;
		Integer id;
		Integer imageId;
		String name;
		String number;
		String timestamp;
		Integer type;
	}
}

/*
 * Location: J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.widget.circlewidget3d.BaseAlert JD-Core Version: 0.6.2
 */