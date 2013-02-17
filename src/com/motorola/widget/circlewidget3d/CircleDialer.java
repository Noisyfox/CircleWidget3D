package com.motorola.widget.circlewidget3d;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CircleDialer extends Circle {
	private static CircleDialer mInstance;
	ImageView mCallerImageView;
	TextView mCallerNameNumberView;
	Bitmap mContactImage;
	String mContactName;
	String mPhoneNumber;
	String mUnknownContact;

	private CircleDialer(Context paramContext) {
		this.mContext = paramContext;
		this.mCurrentId = 0;
		prepareCircle(R.layout.dialer_circle, CircleConsts.WEATHER_BITMAP_SIZE.intValue());
		this.mUnknownContact = this.mContext.getResources().getString(
				R.string.unknown);
	}

	private Bitmap getDialerScreen() {
		boolean flag = isKnownContact(mPhoneNumber);
		TextView textview = mCallerNameNumberView;
		String s;
		if (flag)
			s = mContactName;
		else
			s = mPhoneNumber;
		textview.setText(s);
		if (mContactImage != null)
			mCallerImageView.setImageBitmap(mContactImage);
		else
			mCallerImageView.setImageResource(R.drawable.ic_circle_widget_phone);
		mBitmap.eraseColor(0);
		mLayout.draw(mCanvas);
		return mBitmap;
	}

	public static CircleDialer getInstance(Context paramContext) {
		synchronized (syncObject) {
			if (mInstance == null)
				mInstance = new CircleDialer(paramContext);
			return mInstance;
		}
	}

	private void hideDialerCircle() {
		Log.d("Circle", "hideDialerCircle");
		Utility.playFrames(null, 420, 480, 2000L, "dialer_to_weather_id");
	}

	private boolean isKnownContact(String s) {
		Uri uri = Uri
				.withAppendedPath(
						android.provider.ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
						Uri.encode(s));
		ContentResolver contentresolver = mContext.getContentResolver();
		Cursor cursor = contentresolver.query(uri,
				CircleConsts.PROJECTION_CONTACT, null, null, null);
		boolean flag = false;
		if (cursor != null) {
			boolean flag1 = cursor.moveToFirst();
			flag = false;
			if (flag1) {
				mContactName = cursor.getString(cursor
						.getColumnIndex("display_name"));
				long l = cursor.getLong(cursor.getColumnIndex("_id"));
				java.io.InputStream inputstream = android.provider.ContactsContract.Contacts
						.openContactPhotoInputStream(
								contentresolver,
								ContentUris
										.withAppendedId(
												android.provider.ContactsContract.Contacts.CONTENT_URI,
												l));
				if (inputstream != null)
					mContactImage = BitmapFactory.decodeStream(inputstream,
							null, mBitmapOptions);
				else
					mContactImage = null;
				flag = true;
			}
			cursor.close();
		}
		return flag;
	}

	private void showDialerCircle() {
		Utility.changeVisibility(null, new String[] { "circle_dialer" },
				new boolean[] { true });
		Log.d("Circle", "showDialerCircle");
		updateCircle();
	}

	public Bitmap getBackTexture(Bundle paramBundle) {
		return null;
	}

	public Bitmap getFrontTexture(Bundle paramBundle) {
		return null;
	}

	public void handleDestroy() {
		super.handleDestroy();
		mInstance = null;
	}

	public boolean handleFling(Messenger paramMessenger, Message paramMessage,
			Float paramFloat) {
		return false;
	}

	public boolean handleSingleTap(Bundle bundle) {
		Intent intent = new Intent("android.intent.action.DIAL");
		try {
			mContext.startActivity(intent);
		} catch (Exception exception) {
			Log.e("Circle",
					(new StringBuilder()).append("Couldn't start Dialer")
							.append(exception).toString());
		}
		return true;
	}

	public View prepareCircle(int paramInt1, int paramInt2) {
		View localView = super.prepareCircle(paramInt1, paramInt2);
		this.mCallerImageView = ((ImageView) localView.findViewById(R.id.caller_image));
		this.mCallerNameNumberView = ((TextView) localView
				.findViewById(R.id.caller_namenumber));
		this.mFrontLayout = localView.findViewById(R.id.dialer_front);
		return localView;
	}

	public void setTheme(String paramString) {
	}

	public void updateCircle() {
		Utility.updateTexture(null, "circle_dialer/circlefront",
				getDialerScreen());
		Utility.playFrames(null, 360, 420, 2000L, "weather_to_dialer_id");
	}

	public void updateCircle(Context paramContext, Intent paramIntent) {
		int i = paramIntent.getIntExtra("call_type", 1);
		String str;
		if (i == 0) {
			str = paramIntent.getStringExtra("state");
			Log.d("Circle", "state: " + str);
			if (str.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
				this.mPhoneNumber = paramIntent
						.getStringExtra("incoming_number");
			} else {
				if (str.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
					hideDialerCircle();
				} else if (str.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
					showDialerCircle();
				}
			}
		} else {
			this.mPhoneNumber = paramIntent
					.getStringExtra("android.intent.extra.PHONE_NUMBER");
		}

		Log.d("Circle", "Type: " + i + " number: " + this.mPhoneNumber);

	}

	public void updateValues(Context paramContext, Intent paramIntent) {
	}
}

/*
 * Location: J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.widget.circlewidget3d.CircleDialer JD-Core Version: 0.6.2
 */