package vip.ruoyun.screen;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ScreenHelper {

    public interface ScreenMode {
        int WIDTH_DP = 0x25;
        int WIDTH_PT = 0x35;
        int HEIGHT_PT = 0x45;
    }

    private ScreenHelper() {
    }

    private static List<Field> sMetricsFields;

    public static Resources applyAdapt(final Resources resources, float size, int screenMode) {
        DisplayMetrics activityDm = resources.getDisplayMetrics();
        final DisplayMetrics systemDm = Resources.getSystem().getDisplayMetrics();
        change(screenMode, resources, activityDm, systemDm, size);
        //兼容其他手机
        if (sMetricsFields == null) {
            sMetricsFields = new ArrayList<>();
            Class resCls = resources.getClass();
//            Log.e("ScreenHelper", "resCls.getSimpleName: " + resCls.getSimpleName());
            Field[] declaredFields = resCls.getDeclaredFields();
//            Log.e("ScreenHelper", "declaredFields.length: " + declaredFields.length);
            while (declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.getType().isAssignableFrom(DisplayMetrics.class)) {
//                        Log.e("ScreenHelper", "field.getName: " + field.getName());
                        field.setAccessible(true);
                        DisplayMetrics tmpDm = getMetricsFromField(resources, field);
                        if (tmpDm != null) {
//                            Log.e("ScreenHelper", "tmpDm: " + tmpDm.toString());
                            sMetricsFields.add(field);
                            change(screenMode, resources, tmpDm, systemDm, size);
                        }
                    }
                }
                resCls = resCls.getSuperclass();
                if (resCls != null) {
                    declaredFields = resCls.getDeclaredFields();
                } else {
                    break;
                }
            }
        } else {
            for (Field metricsField : sMetricsFields) {
                try {
                    DisplayMetrics dm = (DisplayMetrics) metricsField.get(resources);
                    if (dm != null) change(screenMode, resources, dm, systemDm, size);
                } catch (Exception e) {
                    Log.e("ScreenHelper", "applyMetricsFields: " + e);
                }
            }
        }
        return resources;
    }


    private static void change(int screenMode, final Resources resources, DisplayMetrics activityDm, DisplayMetrics systemDm, float size) {
        switch (screenMode) {
            case ScreenMode.WIDTH_DP:
                adaptWidthPixels(resources, activityDm, systemDm, size);
                break;
            case ScreenMode.HEIGHT_PT:
                adaptHeightXdpi(resources, size);
                break;
            case ScreenMode.WIDTH_PT:
                adaptWidthXdpi(resources, size);
                break;
        }
    }


    private static void adaptWidthPixels(Resources resources, DisplayMetrics activityDm, DisplayMetrics systemDm, float designWidthPixels) {
        //确保设备在横屏和竖屏的显示大小,确保 dp 的大小值
        if (resources.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //横屏以屏幕的宽度来适配，确保 dp 的大小值
            activityDm.density = activityDm.widthPixels * 1.0f / designWidthPixels;
        } else {
            //竖屏以屏幕的高度来适配
            activityDm.density = activityDm.heightPixels * 1.0f / designWidthPixels;
        }
        //确保字体的显示大小
        activityDm.scaledDensity = activityDm.density * (systemDm.scaledDensity / systemDm.density);
        //确保设置的 dpi
        activityDm.densityDpi = (int) (160 * activityDm.density);
    }

    /**
     * Adapt for the horizontal screen, and call it in [android.app.Activity.getResources].
     */
    private static void adaptWidthXdpi(Resources resources, float designWidth) {
        resources.getDisplayMetrics().xdpi = (Resources.getSystem().getDisplayMetrics().widthPixels * 72f) / designWidth;
    }

    /**
     * Adapt for the vertical screen, and call it in [android.app.Activity.getResources].
     */
    private static void adaptHeightXdpi(Resources resources, float designHeight) {
        resources.getDisplayMetrics().xdpi = (Resources.getSystem().getDisplayMetrics().heightPixels * 72f) / designHeight;
    }


    /**
     * @param resources The resources.
     * @return the resource
     */
    public static Resources closeAdapt(Resources resources) {
        DisplayMetrics activityDm = resources.getDisplayMetrics();
        final DisplayMetrics systemDm = Resources.getSystem().getDisplayMetrics();
        resetResources(activityDm, systemDm);
        //兼容其他手机
        if (sMetricsFields == null) {
            sMetricsFields = new ArrayList<>();
            Class resCls = resources.getClass();
            Field[] declaredFields = resCls.getDeclaredFields();
            while (declaredFields.length > 0) {
                for (Field field : declaredFields) {
                    if (field.getType().isAssignableFrom(DisplayMetrics.class)) {
                        field.setAccessible(true);
                        DisplayMetrics tmpDm = getMetricsFromField(resources, field);
                        if (tmpDm != null) {
                            sMetricsFields.add(field);
                            resetResources(tmpDm, systemDm);
                        }
                    }
                }
                resCls = resCls.getSuperclass();
                if (resCls != null) {
                    declaredFields = resCls.getDeclaredFields();
                } else {
                    break;
                }
            }
        } else {
            for (Field metricsField : sMetricsFields) {
                try {
                    DisplayMetrics dm = (DisplayMetrics) metricsField.get(resources);
                    if (dm != null) resetResources(dm, systemDm);
                } catch (Exception e) {
                    Log.e("ScreenHelper", "applyMetricsFields: " + e);
                }
            }
        }
        return resources;
    }

    private static void resetResources(DisplayMetrics activityDm, DisplayMetrics systemDm) {
        //        resources.getDisplayMetrics().xdpi = Resources.getSystem().getDisplayMetrics().density * 72f;
        activityDm.xdpi = systemDm.xdpi;
        activityDm.density = systemDm.density;
        activityDm.scaledDensity = systemDm.scaledDensity; //确保字体的显示大小
        activityDm.densityDpi = systemDm.densityDpi;//确保设置的 dpi
    }

    /**
     * Value of pt/dp to value of px.
     *
     * @param value The value of pt.
     * @return value of px
     */
    public static int value2px(Context context, float value, int screenMode) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        switch (screenMode) {
            case ScreenMode.WIDTH_DP:
            default:
                return (int) (value * metrics.density);
            case ScreenMode.HEIGHT_PT:
            case ScreenMode.WIDTH_PT:
                return (int) (value * metrics.xdpi / 72f + 0.5);
        }
    }

    /**
     * Value of px to value of pt/dp.
     *
     * @param pxValue The value of px.
     * @return value of pt
     */
    public static int px2Value(Context context, float pxValue, int screenMode) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        switch (screenMode) {
            case ScreenMode.WIDTH_DP:
            default:
                return (int) (pxValue / metrics.density);
            case ScreenMode.HEIGHT_PT:
            case ScreenMode.WIDTH_PT:
                return (int) (pxValue * 72 / metrics.xdpi + 0.5);
        }
    }

    private static DisplayMetrics getMetricsFromField(final Resources resources, final Field field) {
        try {
            return (DisplayMetrics) field.get(resources);
        } catch (Exception e) {
            Log.e("ScreenHelper", "getMetricsFromField: " + e);
            return null;
        }
    }


}
