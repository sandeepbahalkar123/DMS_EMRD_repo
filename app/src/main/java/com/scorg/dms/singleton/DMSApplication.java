package com.scorg.dms.singleton;

import android.content.Context;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Sandeep Bahalkar
 */
public class DMSApplication extends MultiDexApplication {
    public final String TAG = this.getClass().getName();
    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();
    private static String SHOW_UPDATE_DIALOG_ON_SKIPPED = "";
    public static String RESOLUTION = "xxhdpi";

    public static String getShowUpdateDialogOnSkipped() {
        return SHOW_UPDATE_DIALOG_ON_SKIPPED;
    }

    public static void setShowUpdateDialogOnSkipped(String showUpdateDialogOnSkipped) {
        SHOW_UPDATE_DIALOG_ON_SKIPPED = showUpdateDialogOnSkipped;
    }


    public static Typeface get(Context c, String name) {
        synchronized (cache) {
            if (!cache.containsKey(name)) {
                Typeface t = Typeface.createFromAsset(c.getAssets(), "fonts/"
                        + name);
                cache.put(name, t);
            }
            return cache.get(name);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
//        new NukeSSLCerts().nuke(); // disable all ssl certificates (dangerous)
        getDeviceResolution(this);
    }

    private void getDeviceResolution(Context mContext) {
        int density = mContext.getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_LOW:
                RESOLUTION = "ldpi";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                RESOLUTION = "mdpi";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                RESOLUTION = "hdpi";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                RESOLUTION = "xhdpi";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
            case DisplayMetrics.DENSITY_420:
                RESOLUTION = "xxhdpi";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                RESOLUTION = "xxxhdpi";
                break;
            default:
                RESOLUTION = "xxhdpi";
                break;
        }
    }

}