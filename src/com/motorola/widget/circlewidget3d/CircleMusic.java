package com.motorola.widget.circlewidget3d;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class CircleMusic extends Circle {
	private static CircleMusic mInstance;
	private static final Uri sGoogleArtworkUri = Uri
			.parse("content://media/external/audio/albumart");
	public boolean ignorePlayState = false;
	private ImageView mAlbumArt;
	private TextView mAlbumNameTextView;
	private String mAlbumPodStationName;
	private String mArtistName;
	private boolean mIsMusicInFront = false;
	private boolean mIsPlaying = true;
	private String mTrackName;
	private TextView mTrackTxtView;

	private CircleMusic(Context paramContext) {
		this.mContext = paramContext;
		this.mCurrentId = 0;
		prepareCircle(R.layout.music_circle,
				CircleConsts.WEATHER_BITMAP_SIZE.intValue());
		this.mAlbumPodStationName = this.mContext.getResources().getString(
				R.string.unknown);
		// ((AudioManager)paramContext.getSystemService("audio"));
		this.mIsMusicInFront = false;
	}

	private long getAlbumIdFromTrackId(long paramLong) {
		long l1 = -1L;
		Uri localUri = null;
		Cursor localCursor = null;
		if (paramLong >= 1L) {
			long l2 = paramLong - 1L;
			localUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
					.buildUpon().appendQueryParameter("limit", l2 + ",1")
					.build();
			localCursor = null;
		}
		try {
			localCursor = this.mContext.getContentResolver().query(localUri,
					new String[] { "_id", "album_id" }, null, null, null);
			if (localCursor != null) {
				localCursor.moveToFirst();
				if (!localCursor.isAfterLast()) {
					l1 = localCursor.getLong(localCursor
							.getColumnIndex("album_id"));
				}
			}
		} catch (Exception localException) {
		} finally {
			if (localCursor != null)
				localCursor.close();
		}

		return l1;
	}

	private long getAlbumIdFromTrackName(String paramString) {
		long l = -1L;
		String str = "title LIKE " + DatabaseUtils.sqlEscapeString(paramString)
				+ "";
		Cursor localCursor = this.mContext.getContentResolver().query(
				android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { "album_id", "title" }, str, null, null);
		if (localCursor != null) {
			localCursor.moveToFirst();
			if (!localCursor.isAfterLast())
				l = localCursor.getLong(localCursor.getColumnIndex("album_id"));
		}
		return l;
	}

	private Bitmap getArtworkFromDB(Uri paramUri,
			ContentResolver paramContentResolver) {
		if (paramUri == null)
			return null;

		BitmapFactory.Options localOptions = new BitmapFactory.Options();
		localOptions.inSampleSize = 1;
		localOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
		localOptions.inDither = false;
		localOptions.inPurgeable = true;
		InputStream localInputStream = null;

		try {
			localInputStream = paramContentResolver.openInputStream(paramUri);
			return BitmapFactory.decodeStream(localInputStream, null,
					localOptions);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (localInputStream != null) {
				try {
					localInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	private static Bitmap getArtworkFromFile(Context paramContext,
			long paramLong1, long paramLong2) {

		if ((paramLong2 < 0L) && (paramLong1 < 0L)) {
			throw new IllegalArgumentException(
					"Must specify an album or a song id");
		}

		try {
			if (paramLong2 < 0L) {
				Uri localUri2 = Uri
						.parse("content://media/external/audio/media/"
								+ paramLong1 + "/albumart");
				ParcelFileDescriptor localParcelFileDescriptor2 = paramContext
						.getContentResolver()
						.openFileDescriptor(localUri2, "r");
				if (localParcelFileDescriptor2 != null) {
					return BitmapFactory
							.decodeFileDescriptor(localParcelFileDescriptor2
									.getFileDescriptor());
				}
			} else {
				Uri localUri1 = ContentUris.withAppendedId(sGoogleArtworkUri,
						paramLong2);
				ParcelFileDescriptor localParcelFileDescriptor1 = paramContext
						.getContentResolver()
						.openFileDescriptor(localUri1, "r");
				if (localParcelFileDescriptor1 != null) {
					return BitmapFactory
							.decodeFileDescriptor(localParcelFileDescriptor1
									.getFileDescriptor());
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static CircleMusic getInstance(Context paramContext) {
		synchronized (syncObject) {
			if (mInstance == null)
				mInstance = new CircleMusic(paramContext);
			return mInstance;
		}
	}

	public void bringCircleToFront(Message paramMessage) {
		if (!this.mIsMusicInFront) {
			Utility.changeVisibility(null, new String[] { "circle_music" },
					new boolean[] { true });
			Utility.playFrames(null, 120, 180, 2000L, "weather_to_music_id");
		}
	}

	public void bringToFrontSuccess() {
		this.mIsMusicInFront = true;
	}

	public Bitmap getBackTexture(Bundle paramBundle) {
		return null;
	}

	public Bitmap getFrontTexture(Bundle bundle) {
		if (bundle != null) {
			bundle.getString("command");
			mArtistName = bundle.getString("artist");
			mAlbumPodStationName = bundle.getString("album");
			if (mAlbumPodStationName != null)
				mAlbumNameTextView.setText(mArtistName);
			mTrackName = bundle.getString("track");
			if (mTrackName != null)
				mTrackTxtView.setText(mTrackName);
			long l = bundle.getLong("id", -1L);
			if (l > 0L) {
				long l1 = getAlbumIdFromTrackId(l);
				ContentResolver contentresolver = mContext.getContentResolver();
				if (l1 < 0L)
					l1 = l;
				Bitmap bitmap = getArtworkFromDB(
						ContentUris.withAppendedId(sGoogleArtworkUri, l1),
						contentresolver);
				if (bitmap != null) {
					mAlbumArt.setImageBitmap(bitmap);
				} else {
					Bitmap bitmap1 = getArtworkFromFile(mContext, l, -1L);
					if (bitmap1 != null) {
						mAlbumArt.setImageBitmap(bitmap1);
					} else {
						long l2 = getAlbumIdFromTrackName(mTrackName);
						if (l2 > 0L) {
							Bitmap bitmap2 = getArtworkFromDB(
									ContentUris.withAppendedId(
											sGoogleArtworkUri, l2),
									contentresolver);
							if (bitmap2 != null)
								mAlbumArt.setImageBitmap(bitmap2);
						} else {
							mAlbumArt
									.setImageResource(R.drawable.music_circle_mp3_no_artwork);
						}
					}
				}
			}
			if (bundle.getBoolean("playstate", true))
				mIsPlaying = true;
			else
				mIsPlaying = false;
		}
		mBitmap.eraseColor(0);
		mLayout.draw(mCanvas);
		return mBitmap;
	}

	public String[] getNamesOfShape() {
		return new String[] { "circle_music/buttonfronttouch",
				"circle_music/buttonbacktouch",
				"circle_music/buttonfrontpause",
				"circle_music/buttonbackpause", "circle_music/buttonfrontplay",
				"circle_music/buttonbackplay", "circle_music/dismissfront",
				"circle_music/dismissback" };
	}

	public boolean[] getShapeVisibilities() {
		boolean flag = false;
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		boolean flag4 = false;
		boolean flag5;
		if (isFlipped()) {
			if (mIsPlaying) {
				flag5 = true;
			} else {
				flag2 = true;
				flag4 = true;
				flag5 = false;
				flag3 = false;
				flag = false;
				flag1 = false;
			}
		} else if (mIsPlaying) {
			flag = true;
			flag5 = false;
			flag2 = false;
			flag4 = false;
			flag3 = false;
			flag1 = false;
		} else {
			flag1 = true;
			flag3 = true;
			flag5 = false;
			flag2 = false;
			flag4 = false;
			flag = false;
		}
		return (new boolean[] { false, false, flag, flag5, flag1, flag2, flag3,
				flag4 });
	}

	public void handleDestroy() {
		super.handleDestroy();
		mInstance = null;
	}

	public boolean handleFling(Messenger messenger, Message message,
			Float float1) {
		Utility.flipCircle(messenger, "circle_music", float1.floatValue(),
				mIsFlipped);
		boolean flag;
		Intent intent;
		if (!mIsFlipped)
			flag = true;
		else
			flag = false;
		mIsFlipped = flag;
		intent = new Intent();
		intent.setAction("com.android.music.musicservicecommand.togglepause");
		if (float1.floatValue() > 0.0F)
			intent.putExtra("command", "next");
		else
			intent.putExtra("command", "previous");
		mContext.sendBroadcast(intent);
		return false;
	}

	public boolean handleSingleTap(Bundle paramBundle) {
		String str1 = paramBundle.getString("shape_name");
		if (str1.startsWith("circle_music/albumfront")) {
			Intent localIntent1 = new Intent(
					"android.intent.action.MUSIC_PLAYER");
			localIntent1.setFlags(0x14200000);
			this.mContext.startActivity(localIntent1);
			return true;
		}
		if ((str1.startsWith("circle_music/dismissback"))
				|| (str1.startsWith("circle_music/dismissfront"))) {
			if (this.mIsPlaying) {
				Intent localIntent2 = new Intent();
				localIntent2
						.setAction("com.android.music.musicservicecommand.togglepause");
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
		Utility.changeVisibility(null, getNamesOfShape(),
				getShapeVisibilities());
		Intent localIntent3 = new Intent();
		localIntent3
				.setAction("com.android.music.musicservicecommand.togglepause");
		localIntent3.putExtra("command", "togglepause");
		this.mContext.sendBroadcast(localIntent3);
		boolean bool3 = this.mIsPlaying;
		boolean bool4 = false;
		if (!bool3)
			bool4 = true;
		this.mIsPlaying = bool4;
		return true;
	}

	public void hideCircle(Message paramMessage) {
		Utility.playFrames(null, 300, 360, 2000L, "music_to_weather_id");
		this.mIsMusicInFront = false;
	}

	public boolean isMusicInFront() {
		return this.mIsMusicInFront;
	}

	public boolean isPlaying() {
		return this.mIsPlaying;
	}

	public View prepareCircle(int paramInt1, int paramInt2) {
		View localView = super.prepareCircle(paramInt1, paramInt2);
		this.mAlbumArt = ((ImageView) localView.findViewById(R.id.album_art));
		this.mAlbumNameTextView = ((TextView) localView
				.findViewById(R.id.album_pod_station_name));
		this.mTrackTxtView = ((TextView) localView
				.findViewById(R.id.song_episode_name));
		return localView;
	}

	public void setMusicHidden() {
		this.mIsMusicInFront = false;
	}

	public void setPlayState(boolean paramBoolean) {
		this.mIsPlaying = paramBoolean;
	}

	public void setTheme(String paramString) {
	}

	public void updateCircle() {
	}

	public void updateCircle(Context paramContext, Intent paramIntent) {
		Bundle localBundle1 = paramIntent.getExtras();
		String str1 = "circle_music/albumfront";
		String str2 = "circle_music/buttonfront";
		Bundle localBundle2 = new Bundle();
		localBundle2.putString("shape_name", "circle_music");
		Message localMessage = Message.obtain(null, 9);
		localMessage.setData(localBundle2);

		// if ((paramIntent != null) && (paramIntent.getExtras() != null)) {
		// Iterator localIterator = paramIntent.getExtras().keySet()
		// .iterator();
		// while (localIterator.hasNext()) {
		// String str3 = (String) localIterator.next();
		// Object localObject = localBundle1.get(str3);
		// if (localObject != null)
		// Log.d("Circle",
		// "Key:" + str3 + " Val: " + localObject.toString());
		// }
		//
		// }

		if (paramIntent != null) {
			if ((paramIntent.getAction()
					.equals("com.android.music.playbackcomplete"))
					&& (isMusicInFront())) {
				this.ignorePlayState = true;
				hideCircle(localMessage);
				return;
			} else if ((paramIntent.getAction()
					.equals("com.android.music.playstatechanged"))
					&& (this.ignorePlayState)) {
				this.ignorePlayState = false;
				setPlayState(localBundle1.getBoolean("playstate"));
				return;
			}

		}

		if (isFlipped()) {
			str1 = "circle_music/albumback";
			str2 = "circle_music/buttonback";
		}
		Utility.updateTexture(null, str1, getFrontTexture(localBundle1));
		Utility.updateTexture(null, str2, "music_pause");
		Utility.changeVisibility(null, getNamesOfShape(),
				getShapeVisibilities());
		if (!this.ignorePlayState) {
			bringCircleToFront(localMessage);
		}
	}

	public void updateValues(Context paramContext, Intent paramIntent) {
	}
}

/*
 * Location: J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.widget.circlewidget3d.CircleMusic JD-Core Version: 0.6.2
 */