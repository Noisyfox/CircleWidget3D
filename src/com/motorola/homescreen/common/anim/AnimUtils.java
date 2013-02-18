package com.motorola.homescreen.common.anim;

import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.animation.ObjectAnimator;
import android.appwidget.AppWidgetHostView;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AnimUtils {
	public static final String ACTION_HOME_ONPAUSE = "com.motorola.homescreen.ACTION_HOME_ONPAUSE";
	public static final String ACTION_HOME_ONRESUME = "com.motorola.homescreen.ACTION_HOME_ONRESUME";
	public static final String ACTION_PANEL_FOCUS_CHANGE = "com.motorola.homescreen.ACTION_PANEL_FOCUS_CHANGE";
	public static final String ADDITIVE = "additive";
	//private static final boolean DEBUG = false;
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

	public static AnimatorSet inflateAnimations(View view, String animState,
			JSONObject anims) {
		AnimatorSet animSet = null;
		Builder animBuilder = null;
		JSONArray animsArray = null;

		try {
			animsArray = anims.getJSONArray(animState);
		} catch (JSONException e) {
			Log.e("AnimUtils", "JSON Exception : " + animsArray);
			return null;
		}

		int numAnims = animsArray.length();
		for (int i = 0; i < numAnims; i++) {
			try {
				JSONObject data = animsArray.getJSONObject(i);
				long duration = data.optLong("duration", 1000L);
				int repeat = data.optInt("repeat", 0);
				int repeatMode = data.optInt("repeat_mode", 2);
				String type = data.getString("property_name");
				int targetType = data.optInt("target_type", 1);
				long startDelay = data.optLong("start_delay", 0L);
				int animValueType = data.optInt("value_type", 2);
				int interpolator = data.optInt("interpolator", -9999);
				float interpolatorFactor = (float) data.optDouble(
						"interpolator_factor", 1.0D);

				int targetId = 0;
				String targetName = null;

				if (targetType != 1) {
					targetName = data.getString("target");
				} else {
					targetId = data.getInt("target");
				}

				ObjectAnimator anim = null;
				View v = getTargetView(view, targetType, targetId, targetName);

				if (v == null) {
					Log.d("AnimUtils", "animView not found: " + v);
				} else {

					if (animValueType != 1) {
						// :cond_4
						float startValue = (float) data.optDouble(
								"start_value", -9999D);
						float endValue = (float) data.getDouble("end_value");

						if (startValue != -9999F) {
							anim = ObjectAnimator.ofFloat(v, type, new float[] {
									startValue, endValue });
						} else {
							anim = ObjectAnimator.ofFloat(v, type,
									new float[] { endValue });
						}
					} else {

						int startValue = data.optInt("start_value", -9999);
						int endValue = data.getInt("end_value");

						if (startValue != -9999) {
							anim = ObjectAnimator.ofInt(v, type, new int[] {
									startValue, endValue });
						} else {
							anim = ObjectAnimator.ofInt(v, type,
									new int[] { endValue });
						}
					}
					anim.setDuration(duration);

					if (repeat > 0) {
						anim.setRepeatCount(repeat);
						anim.setRepeatMode(repeatMode);
					}

					if (interpolator != -9999) {
						setInterpolatorForThisObject(anim, interpolator,
								interpolatorFactor);
					}

					anim.setStartDelay(startDelay);
					if (animSet != null) {
						animBuilder.with(anim);
					}
					animSet = new AnimatorSet();

					animBuilder = animSet.play(anim);
				}
			} catch (JSONException e) {
				Log.e("AnimUtils", "JSON Exception : " + animsArray);
				break;
			}
		}

		return animSet;
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