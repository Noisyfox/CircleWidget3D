package com.motorola.widget.circlewidget3d;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.AudioManager;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;

public class CircleMusic extends Circle
{
  private static CircleMusic mInstance;
  private static final Uri sGoogleArtworkUri = Uri.parse("content://media/external/audio/albumart");
  public boolean ignorePlayState = false;
  private ImageView mAlbumArt;
  private TextView mAlbumNameTextView;
  private String mAlbumPodStationName;
  private String mArtistName;
  private boolean mIsMusicInFront = false;
  private boolean mIsPlaying = true;
  private String mTrackName;
  private TextView mTrackTxtView;

  private CircleMusic(Context paramContext)
  {
    this.mContext = paramContext;
    this.mCurrentId = 0;
    prepareCircle(2130903058, CircleConsts.WEATHER_BITMAP_SIZE.intValue());
    this.mAlbumPodStationName = this.mContext.getResources().getString(2131230733);
    ((AudioManager)paramContext.getSystemService("audio"));
    this.mIsMusicInFront = false;
  }

  private long getAlbumIdFromTrackId(long paramLong)
  {
    long l1 = -1L;
    Uri localUri;
    Cursor localCursor;
    if (paramLong >= 1L)
    {
      long l2 = paramLong - 1L;
      localUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.buildUpon().appendQueryParameter("limit", l2 + ",1").build();
      localCursor = null;
    }
    try
    {
      localCursor = this.mContext.getContentResolver().query(localUri, new String[] { "_id", "album_id" }, null, null, null);
      if (localCursor != null)
      {
        localCursor.moveToFirst();
        if (!localCursor.isAfterLast())
        {
          long l3 = localCursor.getLong(localCursor.getColumnIndex("album_id"));
          l1 = l3;
        }
      }
      return l1;
    }
    catch (Exception localException)
    {
      return l1;
    }
    finally
    {
      if (localCursor != null)
        localCursor.close();
    }
  }

  private long getAlbumIdFromTrackName(String paramString)
  {
    long l = -1L;
    String str = "title LIKE " + DatabaseUtils.sqlEscapeString(paramString) + "";
    Cursor localCursor = this.mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[] { "album_id", "title" }, str, null, null);
    if (localCursor != null)
    {
      localCursor.moveToFirst();
      if (!localCursor.isAfterLast())
        l = localCursor.getLong(localCursor.getColumnIndex("album_id"));
    }
    return l;
  }

  private Bitmap getArtworkFromDB(Uri paramUri, ContentResolver paramContentResolver)
  {
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    localOptions.inSampleSize = 1;
    localOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
    localOptions.inDither = false;
    localOptions.inPurgeable = true;
    Object localObject1 = null;
    if (paramUri == null);
    InputStream localInputStream;
    while (true)
    {
      return localObject1;
      localInputStream = null;
      try
      {
        localInputStream = paramContentResolver.openInputStream(paramUri);
        if (localInputStream != null)
        {
          Bitmap localBitmap = BitmapFactory.decodeStream(localInputStream, null, localOptions);
          localObject1 = localBitmap;
          if (localInputStream != null)
            try
            {
              localInputStream.close();
              return localObject1;
            }
            catch (IOException localIOException3)
            {
              return localObject1;
            }
        }
        else
        {
          localObject1 = null;
          if (localInputStream != null)
            try
            {
              localInputStream.close();
              return null;
            }
            catch (IOException localIOException4)
            {
              return null;
            }
        }
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        localObject1 = null;
        if (localInputStream != null)
          try
          {
            localInputStream.close();
            return null;
          }
          catch (IOException localIOException2)
          {
            return null;
          }
      }
      finally
      {
        if (localInputStream == null);
      }
    }
    try
    {
      localInputStream.close();
      label138: throw localObject2;
    }
    catch (IOException localIOException1)
    {
      break label138;
    }
  }

  private static Bitmap getArtworkFromFile(Context paramContext, long paramLong1, long paramLong2)
  {
    if ((paramLong2 < 0L) && (paramLong1 < 0L))
      throw new IllegalArgumentException("Must specify an album or a song id");
    if (paramLong2 < 0L);
    try
    {
      Uri localUri2 = Uri.parse("content://media/external/audio/media/" + paramLong1 + "/albumart");
      ParcelFileDescriptor localParcelFileDescriptor2 = paramContext.getContentResolver().openFileDescriptor(localUri2, "r");
      if (localParcelFileDescriptor2 != null)
      {
        return BitmapFactory.decodeFileDescriptor(localParcelFileDescriptor2.getFileDescriptor());
        Uri localUri1 = ContentUris.withAppendedId(sGoogleArtworkUri, paramLong2);
        ParcelFileDescriptor localParcelFileDescriptor1 = paramContext.getContentResolver().openFileDescriptor(localUri1, "r");
        if (localParcelFileDescriptor1 != null)
        {
          Bitmap localBitmap = BitmapFactory.decodeFileDescriptor(localParcelFileDescriptor1.getFileDescriptor());
          return localBitmap;
        }
      }
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      return null;
    }
    catch (IllegalStateException localIllegalStateException)
    {
    }
    return null;
  }

  public static CircleMusic getInstance(Context paramContext)
  {
    synchronized (syncObject)
    {
      if (mInstance == null)
        mInstance = new CircleMusic(paramContext);
      return mInstance;
    }
  }

  public void bringCircleToFront(Message paramMessage)
  {
    if (!this.mIsMusicInFront)
    {
      Utility.changeVisibility(null, new String[] { "circle_music" }, new boolean[] { true });
      Utility.playFrames(null, 120, 180, 2000L, "weather_to_music_id");
    }
  }

  public void bringToFrontSuccess()
  {
    this.mIsMusicInFront = true;
  }

  public Bitmap getBackTexture(Bundle paramBundle)
  {
    return null;
  }

  public Bitmap getFrontTexture(Bundle paramBundle)
  {
    long l1;
    ContentResolver localContentResolver;
    if (paramBundle != null)
    {
      paramBundle.getString("command");
      this.mArtistName = paramBundle.getString("artist");
      this.mAlbumPodStationName = paramBundle.getString("album");
      if (this.mAlbumPodStationName != null)
        this.mAlbumNameTextView.setText(this.mArtistName);
      this.mTrackName = paramBundle.getString("track");
      if (this.mTrackName != null)
        this.mTrackTxtView.setText(this.mTrackName);
      l1 = paramBundle.getLong("id", -1L);
      if (l1 > 0L)
      {
        long l2 = getAlbumIdFromTrackId(l1);
        localContentResolver = this.mContext.getContentResolver();
        if (l2 < 0L)
          l2 = l1;
        Bitmap localBitmap1 = getArtworkFromDB(ContentUris.withAppendedId(sGoogleArtworkUri, l2), localContentResolver);
        if (localBitmap1 == null)
          break label194;
        this.mAlbumArt.setImageBitmap(localBitmap1);
      }
      if (!paramBundle.getBoolean("playstate", true))
        break label287;
    }
    label287: for (this.mIsPlaying = true; ; this.mIsPlaying = false)
    {
      this.mBitmap.eraseColor(0);
      this.mLayout.draw(this.mCanvas);
      return this.mBitmap;
      label194: Bitmap localBitmap2 = getArtworkFromFile(this.mContext, l1, -1L);
      if (localBitmap2 != null)
      {
        this.mAlbumArt.setImageBitmap(localBitmap2);
        break;
      }
      long l3 = getAlbumIdFromTrackName(this.mTrackName);
      if (l3 > 0L)
      {
        Bitmap localBitmap3 = getArtworkFromDB(ContentUris.withAppendedId(sGoogleArtworkUri, l3), localContentResolver);
        if (localBitmap3 == null)
          break;
        this.mAlbumArt.setImageBitmap(localBitmap3);
        break;
      }
      this.mAlbumArt.setImageResource(2130837570);
      break;
    }
  }

  public String[] getNamesOfShape()
  {
    return new String[] { "circle_music/buttonfronttouch", "circle_music/buttonbacktouch", "circle_music/buttonfrontpause", "circle_music/buttonbackpause", "circle_music/buttonfrontplay", "circle_music/buttonbackplay", "circle_music/dismissfront", "circle_music/dismissback" };
  }

  public boolean[] getShapeVisibilities()
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    int i1;
    if (isFlipped())
      if (this.mIsPlaying)
        i1 = 1;
    while (true)
    {
      return new boolean[] { false, false, i, i1, j, k, m, n };
      k = 1;
      n = 1;
      i1 = 0;
      m = 0;
      i = 0;
      j = 0;
      continue;
      if (this.mIsPlaying)
      {
        i = 1;
        i1 = 0;
        k = 0;
        n = 0;
        m = 0;
        j = 0;
      }
      else
      {
        j = 1;
        m = 1;
        i1 = 0;
        k = 0;
        n = 0;
        i = 0;
      }
    }
  }

  public void handleDestroy()
  {
    super.handleDestroy();
    mInstance = null;
  }

  public boolean handleFling(Messenger paramMessenger, Message paramMessage, Float paramFloat)
  {
    Utility.flipCircle(paramMessenger, "circle_music", paramFloat.floatValue(), this.mIsFlipped);
    boolean bool;
    Intent localIntent;
    if (!this.mIsFlipped)
    {
      bool = true;
      this.mIsFlipped = bool;
      localIntent = new Intent();
      localIntent.setAction("com.android.music.musicservicecommand.togglepause");
      if (paramFloat.floatValue() <= 0.0F)
        break label87;
      localIntent.putExtra("command", "next");
    }
    while (true)
    {
      this.mContext.sendBroadcast(localIntent);
      return false;
      bool = false;
      break;
      label87: localIntent.putExtra("command", "previous");
    }
  }

  public boolean handleSingleTap(Bundle paramBundle)
  {
    String str1 = paramBundle.getString("shape_name");
    if (str1.startsWith("circle_music/albumfront"))
    {
      Intent localIntent1 = new Intent("android.intent.action.MUSIC_PLAYER");
      localIntent1.setFlags(337641472);
      this.mContext.startActivity(localIntent1);
      return true;
    }
    if ((str1.startsWith("circle_music/dismissback")) || (str1.startsWith("circle_music/dismissfront")))
    {
      if (this.mIsPlaying)
      {
        Intent localIntent2 = new Intent();
        localIntent2.setAction("com.android.music.musicservicecommand.togglepause");
        localIntent2.putExtra("command", "togglepause");
        this.mContext.sendBroadcast(localIntent2);
        boolean bool1 = this.mIsPlaying;
        boolean bool2 = false;
        if (!bool1)
          bool2 = true;
        this.mIsPlaying = bool2;
        this.ignorePlayState = true;
      }
      Bundle localBundle = new Bundle();
      paramBundle.putString("shape_name", "circle_music");
      Message localMessage = Message.obtain(null, 9);
      localMessage.setData(localBundle);
      hideCircle(localMessage);
      return true;
    }
    String str2 = "circle_music/buttonfront";
    if (this.mIsFlipped)
      str2 = "circle_music/buttonback";
    Utility.updateTexture(null, str2, "music_pause");
    Utility.changeVisibility(null, getNamesOfShape(), getShapeVisibilities());
    Intent localIntent3 = new Intent();
    localIntent3.setAction("com.android.music.musicservicecommand.togglepause");
    localIntent3.putExtra("command", "togglepause");
    this.mContext.sendBroadcast(localIntent3);
    boolean bool3 = this.mIsPlaying;
    boolean bool4 = false;
    if (!bool3)
      bool4 = true;
    this.mIsPlaying = bool4;
    return true;
  }

  public void hideCircle(Message paramMessage)
  {
    Utility.playFrames(null, 300, 360, 2000L, "music_to_weather_id");
    this.mIsMusicInFront = false;
  }

  public boolean isMusicInFront()
  {
    return this.mIsMusicInFront;
  }

  public boolean isPlaying()
  {
    return this.mIsPlaying;
  }

  public View prepareCircle(int paramInt1, int paramInt2)
  {
    View localView = super.prepareCircle(paramInt1, paramInt2);
    this.mAlbumArt = ((ImageView)localView.findViewById(2131427376));
    this.mAlbumNameTextView = ((TextView)localView.findViewById(2131427378));
    this.mTrackTxtView = ((TextView)localView.findViewById(2131427379));
    return localView;
  }

  public void setMusicHidden()
  {
    this.mIsMusicInFront = false;
  }

  public void setPlayState(boolean paramBoolean)
  {
    this.mIsPlaying = paramBoolean;
  }

  public void setTheme(String paramString)
  {
  }

  public void updateCircle()
  {
  }

  public void updateCircle(Context paramContext, Intent paramIntent)
  {
    Bundle localBundle1 = paramIntent.getExtras();
    String str1 = "circle_music/albumfront";
    String str2 = "circle_music/buttonfront";
    Bundle localBundle2 = new Bundle();
    localBundle2.putString("shape_name", "circle_music");
    Message localMessage = Message.obtain(null, 9);
    localMessage.setData(localBundle2);
    if ((paramIntent != null) && (paramIntent.getExtras() != null))
    {
      Iterator localIterator = paramIntent.getExtras().keySet().iterator();
      while (localIterator.hasNext())
      {
        String str3 = (String)localIterator.next();
        Object localObject = localBundle1.get(str3);
        if (localObject != null)
          Log.d("Circle", "Key:" + str3 + " Val: " + localObject.toString());
      }
    }
    if (paramIntent != null)
      if ((paramIntent.getAction().equals("com.android.music.playbackcomplete")) && (isMusicInFront()))
      {
        this.ignorePlayState = true;
        hideCircle(localMessage);
      }
    do
    {
      return;
      if ((paramIntent.getAction().equals("com.android.music.playstatechanged")) && (this.ignorePlayState))
      {
        this.ignorePlayState = false;
        setPlayState(localBundle1.getBoolean("playstate"));
        return;
      }
      if (isFlipped())
      {
        str1 = "circle_music/albumback";
        str2 = "circle_music/buttonback";
      }
      Utility.updateTexture(null, str1, getFrontTexture(localBundle1));
      Utility.updateTexture(null, str2, "music_pause");
      Utility.changeVisibility(null, getNamesOfShape(), getShapeVisibilities());
    }
    while (this.ignorePlayState);
    bringCircleToFront(localMessage);
  }

  public void updateValues(Context paramContext, Intent paramIntent)
  {
  }
}

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.widget.circlewidget3d.CircleMusic
 * JD-Core Version:    0.6.2
 */