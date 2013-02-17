package com.motorola.widget.circlewidget3d;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;

public class CircleDialer extends Circle
{
  private static CircleDialer mInstance;
  ImageView mCallerImageView;
  TextView mCallerNameNumberView;
  Bitmap mContactImage;
  String mContactName;
  String mPhoneNumber;
  String mUnknownContact;

  private CircleDialer(Context paramContext)
  {
    this.mContext = paramContext;
    this.mCurrentId = 0;
    prepareCircle(2130903051, CircleConsts.WEATHER_BITMAP_SIZE.intValue());
    this.mUnknownContact = this.mContext.getResources().getString(2131230733);
  }

  private Bitmap getDialerScreen()
  {
    boolean bool = isKnownContact(this.mPhoneNumber);
    TextView localTextView = this.mCallerNameNumberView;
    String str;
    if (bool)
    {
      str = this.mContactName;
      localTextView.setText(str);
      if (this.mContactImage == null)
        break label78;
      this.mCallerImageView.setImageBitmap(this.mContactImage);
    }
    while (true)
    {
      this.mBitmap.eraseColor(0);
      this.mLayout.draw(this.mCanvas);
      return this.mBitmap;
      str = this.mPhoneNumber;
      break;
      label78: this.mCallerImageView.setImageResource(2130837562);
    }
  }

  public static CircleDialer getInstance(Context paramContext)
  {
    synchronized (syncObject)
    {
      if (mInstance == null)
        mInstance = new CircleDialer(paramContext);
      return mInstance;
    }
  }

  private void hideDialerCircle()
  {
    Log.d("Circle", "hideDialerCircle");
    Utility.playFrames(null, 420, 480, 2000L, "dialer_to_weather_id");
  }

  private boolean isKnownContact(String paramString)
  {
    Uri localUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(paramString));
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    Cursor localCursor = localContentResolver.query(localUri, CircleConsts.PROJECTION_CONTACT, null, null, null);
    boolean bool1 = false;
    InputStream localInputStream;
    if (localCursor != null)
    {
      boolean bool2 = localCursor.moveToFirst();
      bool1 = false;
      if (bool2)
      {
        this.mContactName = localCursor.getString(localCursor.getColumnIndex("display_name"));
        long l = localCursor.getLong(localCursor.getColumnIndex("_id"));
        localInputStream = ContactsContract.Contacts.openContactPhotoInputStream(localContentResolver, ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, l));
        if (localInputStream == null)
          break label141;
      }
    }
    label141: for (this.mContactImage = BitmapFactory.decodeStream(localInputStream, null, this.mBitmapOptions); ; this.mContactImage = null)
    {
      bool1 = true;
      localCursor.close();
      return bool1;
    }
  }

  private void showDialerCircle()
  {
    Utility.changeVisibility(null, new String[] { "circle_dialer" }, new boolean[] { true });
    Log.d("Circle", "showDialerCircle");
    updateCircle();
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
    super.handleDestroy();
    mInstance = null;
  }

  public boolean handleFling(Messenger paramMessenger, Message paramMessage, Float paramFloat)
  {
    return false;
  }

  public boolean handleSingleTap(Bundle paramBundle)
  {
    Intent localIntent = new Intent("android.intent.action.DIAL");
    try
    {
      this.mContext.startActivity(localIntent);
      return true;
    }
    catch (Exception localException)
    {
      while (true)
        Log.e("Circle", "Couldn't start Dialer" + localException);
    }
  }

  public View prepareCircle(int paramInt1, int paramInt2)
  {
    View localView = super.prepareCircle(paramInt1, paramInt2);
    this.mCallerImageView = ((ImageView)localView.findViewById(2131427359));
    this.mCallerNameNumberView = ((TextView)localView.findViewById(2131427360));
    this.mFrontLayout = localView.findViewById(2131427358);
    return localView;
  }

  public void setTheme(String paramString)
  {
  }

  public void updateCircle()
  {
    Utility.updateTexture(null, "circle_dialer/circlefront", getDialerScreen());
    Utility.playFrames(null, 360, 420, 2000L, "weather_to_dialer_id");
  }

  public void updateCircle(Context paramContext, Intent paramIntent)
  {
    int i = paramIntent.getIntExtra("call_type", 1);
    String str;
    if (i == 0)
    {
      str = paramIntent.getStringExtra("state");
      Log.d("Circle", "state: " + str);
      if (str.equals(TelephonyManager.EXTRA_STATE_RINGING))
        this.mPhoneNumber = paramIntent.getStringExtra("incoming_number");
    }
    while (true)
    {
      Log.d("Circle", "Type: " + i + " number: " + this.mPhoneNumber);
      return;
      if (str.equals(TelephonyManager.EXTRA_STATE_IDLE))
      {
        hideDialerCircle();
      }
      else if (str.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
      {
        showDialerCircle();
        continue;
        this.mPhoneNumber = paramIntent.getStringExtra("android.intent.extra.PHONE_NUMBER");
      }
    }
  }

  public void updateValues(Context paramContext, Intent paramIntent)
  {
  }
}

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.CircleDialer
 * JD-Core Version:    0.6.2
 */