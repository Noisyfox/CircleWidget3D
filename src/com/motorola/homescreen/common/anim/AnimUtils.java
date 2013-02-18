package com.motorola.homescreen.common.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import org.json.JSONObject;

public class AnimUtils {
	public static final String ACTION_HOME_ONPAUSE = "com.motorola.homescreen.ACTION_HOME_ONPAUSE";
	public static final String ACTION_HOME_ONRESUME = "com.motorola.homescreen.ACTION_HOME_ONRESUME";
	public static final String ACTION_PANEL_FOCUS_CHANGE = "com.motorola.homescreen.ACTION_PANEL_FOCUS_CHANGE";
	public static final String ADDITIVE = "additive";
	private static final boolean DEBUG = false;
	public static final String DURATION = "duration";
	public static final String END_VALUE = "end_value";
	public static final String EXTRA_FOCUSED_WIDGET_ID = "focused_widget_ids";
	public static final String FRAME_DELAY = "frame_delay";
	public static final String INTERPOLATOR = "interpolator";
	public static final int INTERPOLATOR_ACCELERATE = 2;
	public static final int INTERPOLATOR_ACCELERATE_DECELERATE = 1;
	public static final String INTERPOLATOR_FACTOR = "interpolator_factor";
	public static final int INTERPOlATOR_ANTICIPATE = 3;
	public static final int INTERPOlATOR_ANTICIPATE_OVERSHOOT = 4;
	public static final int INTERPOlATOR_BOUNCE = 5;
	public static final int INTERPOlATOR_CYCLE = 6;
	public static final int INTERPOlATOR_DECELERATE = 7;
	public static final int INTERPOlATOR_LINEAR = 8;
	public static final int INTERPOlATOR_OVERSHOOT = 9;
	public static final int INVALID_VAL = -9999;
	public static final String PROPERTY_ALPHA = "alpha";
	public static final String PROPERTY_NAME = "property_name";
	public static final String PROPERTY_ROTATION = "rotation";
	public static final String PROPERTY_ROTATIONX = "rotationX";
	public static final String PROPERTY_ROTATIONY = "rotationY";
	public static final String PROPERTY_SCALE = "scale";
	public static final String PROPERTY_SCALEX = "scaleX";
	public static final String PROPERTY_SCALEY = "scaleY";
	public static final String PROPERTY_TRANSLATION = "translation";
	public static final String PROPERTY_TRANSLATIONX = "translationX";
	public static final String PROPERTY_TRANSLATIONY = "translationY";
	public static final String REPEAT = "repeat";
	public static final String REPEAT_MODE = "repeat_mode";
	public static final String START_DELAY = "start_delay";
	public static final String START_VALUE = "start_value";
	public static final String STATE_HOME_RESUME = "home_resume";
	public static final String STATE_NEW_DATA = "new_data";
	public static final String STATE_PANEL_FOCUS = "panel_focus";
	public static final String STATE_QUICKVIEW_HIDE = "quickview_hide";
	public static final String STATE_QUICKVIEW_SHOW = "quickview_show";
	public static final String STATE_RESET = "reset";
	public static final String TAG = "AnimUtils";
	public static final String TAG_ANIM_DATA = "anim_data";
	public static final String TARGET = "target";
	public static final String TARGET_TYPE = "target_type";
	public static final int TARGET_TYPE_ID = 1;
	public static final int TARGET_TYPE_NAME = 2;
	public static final int TARGET_TYPE_TAG = 3;
	public static final String TARGET_VALUE = "target_value";
	public static final String VALUE_TYPE = "value_type";
	public static final int VALUE_TYPE_CUSTOM = 2;
	public static final int VALUE_TYPE_FLOAT = 2;
	public static final int VALUE_TYPE_INT = 1;

	public static JSONObject createJSON(int paramInt1, int paramInt2,
			String paramString1, String paramString2, long paramLong1,
			int paramInt3, int paramInt4, int paramInt5, float paramFloat1,
			float paramFloat2, int paramInt6, int paramInt7, long paramLong2,
			long paramLong3, int paramInt8, float paramFloat3) {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("target_type", paramInt1);
			if (paramInt1 == 1)
				paramString1 = String.valueOf(paramInt2);
			localJSONObject.put("target", paramString1);
			localJSONObject.put("property_name", paramString2);
			localJSONObject.put("duration", paramLong1);
			if (paramInt3 == 1) {
				if (paramInt4 > 0)
					localJSONObject.put("start_value", paramInt4);
				localJSONObject.put("end_value", paramInt5);
			}
			if (paramInt6 > 0) {
				localJSONObject.put("repeat", paramInt6);
				localJSONObject.put("repeat_mode", paramInt7);
			}
			if (paramLong2 > 0L)
				localJSONObject.put("start_delay", paramLong2);
			if (paramLong3 > 0L)
				localJSONObject.put("frame_delay", paramLong3);
			if (paramInt8 > 0)
				localJSONObject.put("interpolator", paramInt8);
			if (paramFloat3 > 0.0F)
				localJSONObject.put("interpolator_factor", paramFloat3);
			if (paramFloat1 > 0.0F)
				localJSONObject.put("start_value", paramFloat1);
			localJSONObject.put("end_value", paramFloat2);

		} catch (Exception localException) {
			Log.d("AnimUtils", "JSON Error: " + localException);
		}
		return localJSONObject;
	}

	public static JSONObject createJSONData(int paramInt, String paramString,
			long paramLong, float paramFloat) {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("target", paramInt);
			localJSONObject.put("property_name", paramString);
			localJSONObject.put("end_value", paramFloat);
			localJSONObject.put("duration", paramLong);
			localJSONObject.put("value_type", 2);
			return localJSONObject;
		} catch (Exception localException) {
			Log.d("AnimUtils", "JSON Error: " + localException);
		}
		return localJSONObject;
	}

	public static JSONObject createJSONData(int paramInt, String paramString,
			long paramLong, float paramFloat1, float paramFloat2) {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("target", paramInt);
			localJSONObject.put("property_name", paramString);
			localJSONObject.put("start_value", paramFloat1);
			localJSONObject.put("end_value", paramFloat2);
			localJSONObject.put("duration", paramLong);
			localJSONObject.put("value_type", 2);
			return localJSONObject;
		} catch (Exception localException) {
			Log.d("AnimUtils", "JSON Error: " + localException);
		}
		return localJSONObject;
	}

	public static JSONObject createJSONData(int paramInt, String paramString,
			long paramLong1, float paramFloat1, float paramFloat2,
			long paramLong2) {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("target", paramInt);
			localJSONObject.put("property_name", paramString);
			localJSONObject.put("start_value", paramFloat1);
			localJSONObject.put("end_value", paramFloat2);
			localJSONObject.put("duration", paramLong1);
			localJSONObject.put("value_type", 2);
			if (paramLong2 > 0L)
				localJSONObject.put("start_delay", paramLong2);
			return localJSONObject;
		} catch (Exception localException) {
			Log.d("AnimUtils", "JSON Error: " + localException);
		}
		return localJSONObject;
	}

	public static JSONObject createJSONData(int paramInt1, String paramString,
			long paramLong1, float paramFloat1, float paramFloat2,
			long paramLong2, int paramInt2, int paramInt3) {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("target", paramInt1);
			localJSONObject.put("property_name", paramString);
			localJSONObject.put("start_value", paramFloat1);
			localJSONObject.put("end_value", paramFloat2);
			localJSONObject.put("duration", paramLong1);
			if (paramInt2 > 0) {
				localJSONObject.put("repeat", paramInt2);
				localJSONObject.put("repeat_mode", paramInt3);
			}
			localJSONObject.put("value_type", 2);
			if (paramLong2 > 0L)
				localJSONObject.put("start_delay", paramLong2);
			return localJSONObject;
		} catch (Exception localException) {
			Log.d("AnimUtils", "JSON Error: " + localException);
		}
		return localJSONObject;
	}

	public static JSONObject createJSONData(int paramInt1, String paramString,
			long paramLong1, float paramFloat1, float paramFloat2,
			long paramLong2, int paramInt2, int paramInt3, int paramInt4,
			int paramInt5) {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("target", paramInt1);
			localJSONObject.put("property_name", paramString);
			localJSONObject.put("start_value", paramFloat1);
			localJSONObject.put("end_value", paramFloat2);
			localJSONObject.put("duration", paramLong1);
			if (paramInt2 > 0) {
				localJSONObject.put("repeat", paramInt2);
				localJSONObject.put("repeat_mode", paramInt3);
			}
			localJSONObject.put("value_type", 2);
			if (paramLong2 > 0L)
				localJSONObject.put("start_delay", paramLong2);
			localJSONObject.put("interpolator", paramInt4);
			localJSONObject.put("interpolator_factor", paramInt5);
			return localJSONObject;
		} catch (Exception localException) {
			Log.d("AnimUtils", "JSON Error: " + localException);
		}
		return localJSONObject;
	}

	public static JSONObject createJSONData(int paramInt, String paramString,
			long paramLong1, float paramFloat, long paramLong2) {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("target", paramInt);
			localJSONObject.put("property_name", paramString);
			localJSONObject.put("end_value", paramFloat);
			localJSONObject.put("duration", paramLong1);
			localJSONObject.put("value_type", 2);
			if (paramLong2 > 0L)
				localJSONObject.put("start_delay", paramLong2);
			return localJSONObject;
		} catch (Exception localException) {
			Log.d("AnimUtils", "JSON Error: " + localException);
		}
		return localJSONObject;
	}

	public static JSONObject createJSONData(int paramInt1, String paramString,
			long paramLong1, float paramFloat, long paramLong2, int paramInt2,
			int paramInt3) {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("target", paramInt1);
			localJSONObject.put("property_name", paramString);
			localJSONObject.put("end_value", paramFloat);
			localJSONObject.put("duration", paramLong1);
			if (paramInt2 > 0) {
				localJSONObject.put("repeat", paramInt2);
				localJSONObject.put("repeat_mode", paramInt3);
			}
			localJSONObject.put("value_type", 2);
			if (paramLong2 > 0L)
				localJSONObject.put("start_delay", paramLong2);
			return localJSONObject;
		} catch (Exception localException) {
			Log.d("AnimUtils", "JSON Error: " + localException);
		}
		return localJSONObject;
	}

	public static JSONObject createJSONData(int paramInt1, String paramString,
			long paramLong, int paramInt2) {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("target", paramInt1);
			localJSONObject.put("property_name", paramString);
			localJSONObject.put("end_value", paramInt2);
			localJSONObject.put("duration", paramLong);
			localJSONObject.put("value_type", 1);
			return localJSONObject;
		} catch (Exception localException) {
			Log.d("AnimUtils", "JSON Error: " + localException);
		}
		return localJSONObject;
	}

	public static JSONObject createJSONData(int paramInt1, String paramString,
			long paramLong, int paramInt2, int paramInt3) {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("target", paramInt1);
			localJSONObject.put("property_name", paramString);
			localJSONObject.put("start_value", paramInt2);
			localJSONObject.put("end_value", paramInt3);
			localJSONObject.put("duration", paramLong);
			localJSONObject.put("value_type", 1);
			return localJSONObject;
		} catch (Exception localException) {
			Log.d("AnimUtils", "JSON Error: " + localException);
		}
		return localJSONObject;
	}

	public static JSONObject createJSONData(int paramInt1, String paramString,
			long paramLong1, int paramInt2, int paramInt3, long paramLong2) {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("target", paramInt1);
			localJSONObject.put("property_name", paramString);
			localJSONObject.put("start_value", paramInt2);
			localJSONObject.put("end_value", paramInt3);
			localJSONObject.put("duration", paramLong1);
			localJSONObject.put("value_type", 1);
			if (paramLong2 > 0L)
				localJSONObject.put("start_delay", paramLong2);
			return localJSONObject;
		} catch (Exception localException) {
			Log.d("AnimUtils", "JSON Error: " + localException);
		}
		return localJSONObject;
	}

	public static JSONObject createJSONData(int paramInt1, String paramString,
			long paramLong1, int paramInt2, int paramInt3, long paramLong2,
			int paramInt4, int paramInt5) {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("target", paramInt1);
			localJSONObject.put("property_name", paramString);
			localJSONObject.put("start_value", paramInt2);
			localJSONObject.put("end_value", paramInt3);
			localJSONObject.put("duration", paramLong1);
			if (paramInt4 > 0) {
				localJSONObject.put("repeat", paramInt4);
				localJSONObject.put("repeat_mode", paramInt5);
			}
			localJSONObject.put("value_type", 1);
			if (paramLong2 > 0L)
				localJSONObject.put("start_delay", paramLong2);
			return localJSONObject;
		} catch (Exception localException) {
			Log.d("AnimUtils", "JSON Error: " + localException);
		}
		return localJSONObject;
	}

	public static JSONObject createJSONData(int paramInt1, String paramString,
			long paramLong1, int paramInt2, int paramInt3, long paramLong2,
			int paramInt4, int paramInt5, int paramInt6, int paramInt7) {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("target", paramInt1);
			localJSONObject.put("property_name", paramString);
			localJSONObject.put("start_value", paramInt2);
			localJSONObject.put("end_value", paramInt3);
			localJSONObject.put("duration", paramLong1);
			if (paramInt4 > 0) {
				localJSONObject.put("repeat", paramInt4);
				localJSONObject.put("repeat_mode", paramInt5);
			}
			localJSONObject.put("value_type", 1);
			if (paramLong2 > 0L)
				localJSONObject.put("start_delay", paramLong2);
			localJSONObject.put("interpolator", paramInt6);
			localJSONObject.put("interpolator_factor", paramInt7);
			return localJSONObject;
		} catch (Exception localException) {
			Log.d("AnimUtils", "JSON Error: " + localException);
		}
		return localJSONObject;
	}

	public static JSONObject createJSONData(int paramInt1, String paramString,
			long paramLong1, int paramInt2, long paramLong2) {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("target", paramInt1);
			localJSONObject.put("property_name", paramString);
			localJSONObject.put("end_value", paramInt2);
			localJSONObject.put("duration", paramLong1);
			localJSONObject.put("value_type", 1);
			if (paramLong2 > 0L)
				localJSONObject.put("start_delay", paramLong2);
			return localJSONObject;
		} catch (Exception localException) {
			Log.d("AnimUtils", "JSON Error: " + localException);
		}
		return localJSONObject;
	}

	public static JSONObject createJSONData(int paramInt1, String paramString,
			long paramLong1, int paramInt2, long paramLong2, int paramInt3,
			int paramInt4) {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("target", paramInt1);
			localJSONObject.put("property_name", paramString);
			localJSONObject.put("end_value", paramInt2);
			localJSONObject.put("duration", paramLong1);
			if (paramInt3 > 0) {
				localJSONObject.put("repeat", paramInt3);
				localJSONObject.put("repeat_mode", paramInt4);
			}
			localJSONObject.put("value_type", 1);
			if (paramLong2 > 0L)
				localJSONObject.put("start_delay", paramLong2);
			return localJSONObject;
		} catch (Exception localException) {
			Log.d("AnimUtils", "JSON Error: " + localException);
		}
		return localJSONObject;
	}

	public static JSONObject createJSONData(int paramInt1, String paramString1,
			String paramString2, long paramLong1, float paramFloat,
			long paramLong2, int paramInt2, int paramInt3) {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("target_type", paramInt1);
			localJSONObject.put("target", paramString1);
			localJSONObject.put("property_name", paramString2);
			localJSONObject.put("end_value", paramFloat);
			localJSONObject.put("duration", paramLong1);
			if (paramInt2 > 0) {
				localJSONObject.put("repeat", paramInt2);
				localJSONObject.put("repeat_mode", paramInt3);
			}
			localJSONObject.put("value_type", 2);
			return localJSONObject;
		} catch (Exception localException) {
			Log.d("AnimUtils", "JSON Error: " + localException);
		}
		return localJSONObject;
	}

	public static JSONObject createJSONData(int paramInt1, String paramString1,
			String paramString2, long paramLong1, int paramInt2,
			long paramLong2, int paramInt3, int paramInt4) {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("target_type", paramInt1);
			localJSONObject.put("target", paramString1);
			localJSONObject.put("property_name", paramString2);
			localJSONObject.put("end_value", paramInt2);
			localJSONObject.put("duration", paramLong1);
			if (paramInt3 > 0) {
				localJSONObject.put("repeat", paramInt3);
				localJSONObject.put("repeat_mode", paramInt4);
			}
			localJSONObject.put("value_type", 1);
			return localJSONObject;
		} catch (Exception localException) {
			Log.d("AnimUtils", "JSON Error: " + localException);
		}
		return localJSONObject;
	}

	public static JSONObject createJSONData3D(String paramString1,
			String paramString2, String paramString3,
			float[] paramArrayOfFloat, float paramFloat, boolean paramBoolean) {
		JSONObject localJSONObject = new JSONObject();
		try {
			localJSONObject.put("target", paramString1);
			localJSONObject.put("property_name", paramString2);
			for (int i = 0; i < paramArrayOfFloat.length; i++)
				localJSONObject.put("target_value" + i, paramArrayOfFloat[i]);
			localJSONObject.put("duration", paramFloat);
			localJSONObject.put("additive", paramBoolean);
			localJSONObject.put("value_type", 2);
			localJSONObject.put("anim_id", paramString3);
			return localJSONObject;
		} catch (Exception localException) {
			Log.d("AnimUtils", "JSON Error: " + localException);
		}
		return localJSONObject;
	}

	public static JSONObject createJSONData3D(String paramString1,
			String paramString2, float[] paramArrayOfFloat, float paramFloat1,
			int paramInt, float paramFloat2) {
		try {
			JSONObject localJSONObject = new JSONObject();
			try {
				localJSONObject.put("target", paramString1);
				localJSONObject.put("property_name", paramString2);
				for (int i = 0; i < paramArrayOfFloat.length; i++)
					localJSONObject.put("target_value" + i,
							paramArrayOfFloat[i]);
				localJSONObject.put("duration", paramFloat1);
				localJSONObject.put("value_type", 2);
				localJSONObject.put("interpolator", paramInt);
				localJSONObject.put("interpolator_factor", paramFloat2);
				return localJSONObject;
			} catch (Exception localException) {
				while (true)
					Log.d("AnimUtils", "JSON Error: " + localException);
			}
		} finally {
		}
	}

	private static View getTargetView(View view, int i, int j, String s) {
		if (i == 1) {
			return view.findViewById(j);
		} else if (i == 3) {
			return view.findViewWithTag(s);
		} else if (i != 2) {
			return null;
		}

		if (!(view instanceof AppWidgetHostView)) {
			return null;
		}

		AppWidgetHostView appwidgethostview = null;
		PackageManager packagemanager;
		ComponentName componentname;
		View view2;

		try {
			appwidgethostview = (AppWidgetHostView) view;
		} catch (Exception exception) {
			Log.e("AnimUtils",
					(new StringBuilder()).append("Error in TYPE_NAME")
							.append(exception).toString());
			return null;
		}

		packagemanager = appwidgethostview.getContext().getPackageManager();
		componentname = appwidgethostview.getAppWidgetInfo().provider;
		try {
			view2 = appwidgethostview.findViewById(packagemanager
					.getResourcesForApplication(
							packagemanager.getApplicationInfo(
									componentname.getPackageName(), 0))
					.getIdentifier(s, "id", componentname.getPackageName()));
			return view2;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	// ERROR //
	public static AnimatorSet inflateAnimations(View paramView,
			String paramString, JSONObject paramJSONObject) {
		// Byte code:
		// 0: aconst_null
		// 1: astore_3
		// 2: aconst_null
		// 3: astore 4
		// 5: aconst_null
		// 6: astore 5
		// 8: aload_2
		// 9: aload_1
		// 10: invokevirtual 295 org/json/JSONObject:getJSONArray
		// (Ljava/lang/String;)Lorg/json/JSONArray;
		// 13: astore 5
		// 15: aload 5
		// 17: invokevirtual 301 org/json/JSONArray:length ()I
		// 20: istore 8
		// 22: iconst_0
		// 23: istore 9
		// 25: aconst_null
		// 26: astore 10
		// 28: iload 9
		// 30: iload 8
		// 32: if_icmpge +448 -> 480
		// 35: aload 5
		// 37: iload 9
		// 39: invokevirtual 305 org/json/JSONArray:getJSONObject
		// (I)Lorg/json/JSONObject;
		// 42: astore 12
		// 44: aload 12
		// 46: ldc 23
		// 48: ldc2_w 306
		// 51: invokevirtual 311 org/json/JSONObject:optLong
		// (Ljava/lang/String;J)J
		// 54: invokestatic 316 java/lang/Long:valueOf (J)Ljava/lang/Long;
		// 57: astore 13
		// 59: aload 12
		// 61: ldc 95
		// 63: iconst_0
		// 64: invokevirtual 320 org/json/JSONObject:optInt
		// (Ljava/lang/String;I)I
		// 67: istore 14
		// 69: aload 12
		// 71: ldc 98
		// 73: iconst_2
		// 74: invokevirtual 320 org/json/JSONObject:optInt
		// (Ljava/lang/String;I)I
		// 77: istore 15
		// 79: aload 12
		// 81: ldc 65
		// 83: invokevirtual 324 org/json/JSONObject:getString
		// (Ljava/lang/String;)Ljava/lang/String;
		// 86: astore 16
		// 88: aload 12
		// 90: ldc 134
		// 92: iconst_1
		// 93: invokevirtual 320 org/json/JSONObject:optInt
		// (Ljava/lang/String;I)I
		// 96: istore 17
		// 98: aload 12
		// 100: ldc 101
		// 102: lconst_0
		// 103: invokevirtual 311 org/json/JSONObject:optLong
		// (Ljava/lang/String;J)J
		// 106: lstore 18
		// 108: aload 12
		// 110: ldc 143
		// 112: iconst_2
		// 113: invokevirtual 320 org/json/JSONObject:optInt
		// (Ljava/lang/String;I)I
		// 116: istore 20
		// 118: aload 12
		// 120: ldc 35
		// 122: sipush -9999
		// 125: invokevirtual 320 org/json/JSONObject:optInt
		// (Ljava/lang/String;I)I
		// 128: istore 21
		// 130: aload 12
		// 132: ldc 43
		// 134: dconst_1
		// 135: invokevirtual 328 org/json/JSONObject:optDouble
		// (Ljava/lang/String;D)D
		// 138: d2f
		// 139: fstore 22
		// 141: aconst_null
		// 142: astore 23
		// 144: iload 17
		// 146: iconst_1
		// 147: if_icmpne +169 -> 316
		// 150: aload 12
		// 152: ldc 131
		// 154: invokevirtual 332 org/json/JSONObject:getInt
		// (Ljava/lang/String;)I
		// 157: istore 24
		// 159: aload_0
		// 160: iload 17
		// 162: iload 24
		// 164: aload 23
		// 166: invokestatic 334
		// com/motorola/homescreen/common/anim/AnimUtils:getTargetView
		// (Landroid/view/View;IILjava/lang/String;)Landroid/view/View;
		// 169: astore 25
		// 171: aload 25
		// 173: ifnull +274 -> 447
		// 176: iload 20
		// 178: iconst_1
		// 179: if_icmpne +177 -> 356
		// 182: aload 12
		// 184: ldc 104
		// 186: sipush -9999
		// 189: invokevirtual 320 org/json/JSONObject:optInt
		// (Ljava/lang/String;I)I
		// 192: istore 27
		// 194: aload 12
		// 196: ldc 26
		// 198: invokevirtual 332 org/json/JSONObject:getInt
		// (Ljava/lang/String;)I
		// 201: istore 28
		// 203: iload 27
		// 205: sipush -9999
		// 208: if_icmpne +123 -> 331
		// 211: aload 25
		// 213: aload 16
		// 215: iconst_1
		// 216: newarray int
		// 218: dup
		// 219: iconst_0
		// 220: iload 28
		// 222: iastore
		// 223: invokestatic 340 android/animation/ObjectAnimator:ofInt
		// (Ljava/lang/Object;Ljava/lang/String;[I)Landroid/animation/ObjectAnimator;
		// 226: astore 29
		// 228: aload 29
		// 230: aload 13
		// 232: invokevirtual 344 java/lang/Long:longValue ()J
		// 235: invokevirtual 348 android/animation/ObjectAnimator:setDuration
		// (J)Landroid/animation/ObjectAnimator;
		// 238: pop
		// 239: iload 14
		// 241: ifle +17 -> 258
		// 244: aload 29
		// 246: iload 14
		// 248: invokevirtual 352
		// android/animation/ObjectAnimator:setRepeatCount (I)V
		// 251: aload 29
		// 253: iload 15
		// 255: invokevirtual 355 android/animation/ObjectAnimator:setRepeatMode
		// (I)V
		// 258: iload 21
		// 260: sipush -9999
		// 263: if_icmpeq +12 -> 275
		// 266: aload 29
		// 268: iload 21
		// 270: fload 22
		// 272: invokestatic 359
		// com/motorola/homescreen/common/anim/AnimUtils:setInterpolatorForThisObject
		// (Landroid/animation/ObjectAnimator;IF)V
		// 275: aload 29
		// 277: lload 18
		// 279: invokevirtual 363 android/animation/ObjectAnimator:setStartDelay
		// (J)V
		// 282: aload 10
		// 284: ifnonnull +149 -> 433
		// 287: new 365 android/animation/AnimatorSet
		// 290: dup
		// 291: invokespecial 366 android/animation/AnimatorSet:<init> ()V
		// 294: astore_3
		// 295: aload_3
		// 296: aload 29
		// 298: invokevirtual 370 android/animation/AnimatorSet:play
		// (Landroid/animation/Animator;)Landroid/animation/AnimatorSet$Builder;
		// 301: astore 31
		// 303: aload 31
		// 305: astore 4
		// 307: iinc 9 1
		// 310: aload_3
		// 311: astore 10
		// 313: goto -285 -> 28
		// 316: aload 12
		// 318: ldc 131
		// 320: invokevirtual 324 org/json/JSONObject:getString
		// (Ljava/lang/String;)Ljava/lang/String;
		// 323: astore 23
		// 325: iconst_0
		// 326: istore 24
		// 328: goto -169 -> 159
		// 331: aload 25
		// 333: aload 16
		// 335: iconst_2
		// 336: newarray int
		// 338: dup
		// 339: iconst_0
		// 340: iload 27
		// 342: iastore
		// 343: dup
		// 344: iconst_1
		// 345: iload 28
		// 347: iastore
		// 348: invokestatic 340 android/animation/ObjectAnimator:ofInt
		// (Ljava/lang/Object;Ljava/lang/String;[I)Landroid/animation/ObjectAnimator;
		// 351: astore 29
		// 353: goto -125 -> 228
		// 356: aload 12
		// 358: ldc 104
		// 360: ldc2_w 371
		// 363: invokevirtual 328 org/json/JSONObject:optDouble
		// (Ljava/lang/String;D)D
		// 366: d2f
		// 367: fstore 33
		// 369: aload 12
		// 371: ldc 26
		// 373: invokevirtual 376 org/json/JSONObject:getDouble
		// (Ljava/lang/String;)D
		// 376: d2f
		// 377: fstore 34
		// 379: fload 33
		// 381: ldc_w 377
		// 384: fcmpl
		// 385: ifne +23 -> 408
		// 388: aload 25
		// 390: aload 16
		// 392: iconst_1
		// 393: newarray float
		// 395: dup
		// 396: iconst_0
		// 397: fload 34
		// 399: fastore
		// 400: invokestatic 381 android/animation/ObjectAnimator:ofFloat
		// (Ljava/lang/Object;Ljava/lang/String;[F)Landroid/animation/ObjectAnimator;
		// 403: astore 29
		// 405: goto -177 -> 228
		// 408: aload 25
		// 410: aload 16
		// 412: iconst_2
		// 413: newarray float
		// 415: dup
		// 416: iconst_0
		// 417: fload 33
		// 419: fastore
		// 420: dup
		// 421: iconst_1
		// 422: fload 34
		// 424: fastore
		// 425: invokestatic 381 android/animation/ObjectAnimator:ofFloat
		// (Ljava/lang/Object;Ljava/lang/String;[F)Landroid/animation/ObjectAnimator;
		// 428: astore 29
		// 430: goto -202 -> 228
		// 433: aload 4
		// 435: aload 29
		// 437: invokevirtual 386 android/animation/AnimatorSet$Builder:with
		// (Landroid/animation/Animator;)Landroid/animation/AnimatorSet$Builder;
		// 440: pop
		// 441: aload 10
		// 443: astore_3
		// 444: goto -137 -> 307
		// 447: ldc 125
		// 449: new 178 java/lang/StringBuilder
		// 452: dup
		// 453: invokespecial 179 java/lang/StringBuilder:<init> ()V
		// 456: ldc_w 388
		// 459: invokevirtual 185 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 462: aload 25
		// 464: invokevirtual 188 java/lang/StringBuilder:append
		// (Ljava/lang/Object;)Ljava/lang/StringBuilder;
		// 467: invokevirtual 192 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 470: invokestatic 198 android/util/Log:d
		// (Ljava/lang/String;Ljava/lang/String;)I
		// 473: pop
		// 474: aload 10
		// 476: astore_3
		// 477: goto -170 -> 307
		// 480: aload 10
		// 482: areturn
		// 483: astore 6
		// 485: ldc 125
		// 487: new 178 java/lang/StringBuilder
		// 490: dup
		// 491: invokespecial 179 java/lang/StringBuilder:<init> ()V
		// 494: ldc_w 390
		// 497: invokevirtual 185 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 500: aload 5
		// 502: invokevirtual 188 java/lang/StringBuilder:append
		// (Ljava/lang/Object;)Ljava/lang/StringBuilder;
		// 505: invokevirtual 192 java/lang/StringBuilder:toString
		// ()Ljava/lang/String;
		// 508: invokestatic 289 android/util/Log:e
		// (Ljava/lang/String;Ljava/lang/String;)I
		// 511: pop
		// 512: aload_3
		// 513: areturn
		// 514: astore 11
		// 516: aload 10
		// 518: astore_3
		// 519: goto -34 -> 485
		//
		// Exception table:
		// from to target type
		// 8 22 483 java/lang/Exception
		// 295 303 483 java/lang/Exception
		// 35 141 514 java/lang/Exception
		// 150 159 514 java/lang/Exception
		// 159 171 514 java/lang/Exception
		// 182 203 514 java/lang/Exception
		// 211 228 514 java/lang/Exception
		// 228 239 514 java/lang/Exception
		// 244 258 514 java/lang/Exception
		// 266 275 514 java/lang/Exception
		// 275 282 514 java/lang/Exception
		// 287 295 514 java/lang/Exception
		// 316 325 514 java/lang/Exception
		// 331 353 514 java/lang/Exception
		// 356 379 514 java/lang/Exception
		// 388 405 514 java/lang/Exception
		// 408 430 514 java/lang/Exception
		// 433 441 514 java/lang/Exception
		// 447 474 514 java/lang/Exception
	}

	public static AnimatorSet retrieveAndSetAnimation(View paramView,
			CharSequence paramCharSequence, String paramString) {
		Log.d("AnimUtils", "animDatas: " + paramCharSequence);
		AnimatorSet localAnimatorSet = null;
		try {
			localAnimatorSet = inflateAnimations(paramView, paramString,
					new JSONObject(paramCharSequence.toString()));
			if (localAnimatorSet != null)
				localAnimatorSet.start();
			return localAnimatorSet;
		} catch (Exception localException) {
			Log.e("AnimUtils", "Unable to retrieve anim_state=" + paramString
					+ " animDatas=" + paramCharSequence + localException);
		}
		return localAnimatorSet;
	}

	public static JSONObject retrieveAnimations(String paramString) {
		try {
			JSONObject localJSONObject = new JSONObject(paramString);
			return localJSONObject;
		} catch (Exception localException) {
			Log.e("AnimUtils", "Unable to retrieve animations=" + paramString
					+ " ex=" + localException);
		}
		return null;
	}

	private static void setInterpolatorForThisObject(
			ObjectAnimator paramObjectAnimator, int paramInt, float paramFloat) {
		Log.d("AnimUtils", "setInterpolator Id: " + paramInt);
		switch (paramInt) {
		default:
			return;
		case 1:
			paramObjectAnimator
					.setInterpolator(new AccelerateDecelerateInterpolator());
			return;
		case 2:
			paramObjectAnimator.setInterpolator(new AccelerateInterpolator(
					paramFloat));
			return;
		case 3:
			paramObjectAnimator.setInterpolator(new AnticipateInterpolator(
					paramFloat));
			return;
		case 4:
			paramObjectAnimator
					.setInterpolator(new AnticipateOvershootInterpolator(
							paramFloat));
			return;
		case 5:
			paramObjectAnimator.setInterpolator(new BounceInterpolator());
			return;
		case 6:
			paramObjectAnimator.setInterpolator(new CycleInterpolator(
					paramFloat));
			return;
		case 7:
			paramObjectAnimator.setInterpolator(new DecelerateInterpolator(
					paramFloat));
			return;
		case 8:
			paramObjectAnimator.setInterpolator(new LinearInterpolator());
			return;
		case 9:
		}
		paramObjectAnimator.setInterpolator(new OvershootInterpolator(
				paramFloat));
	}
}

/*
 * Location: J:\鎶�湳鏂囨。\瀹夊崜鍥轰欢鐩稿叧\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.homescreen.common.anim.AnimUtils JD-Core Version: 0.6.2
 */