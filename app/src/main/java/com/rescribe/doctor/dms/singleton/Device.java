package com.rescribe.doctor.dms.singleton;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.rescribe.doctor.R;
import com.rescribe.doctor.dms.util.DmsConstants;

/**
 * Created by Sandeep Bahalkar
 */

public class Device {

    private static final String TAG = "Device";
    private Context context;
    private WindowManager windowManager;

    /**
     * Create a static method to get instance.
     */

    public static Device getInstance(Context context){

        return new Device(context);
    }

    public static Device getInstance(WindowManager windowManager){

        return new Device(windowManager);
    }

    public Device(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    public Device(Context context) {
        this.context = context;
    }

    public String getDensity() {

        String density = DmsConstants.HDPI;

        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                density = DmsConstants.LDPI;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                density = DmsConstants.MDPI;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                density = DmsConstants.HDPI;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                density = DmsConstants.XHDPI;
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                density = DmsConstants.XXHDPI;
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                density = DmsConstants.XXXHDPI;
                break;
        }

        Log.d(TAG, density);

        return density;
    }

    public String getDeviceType() {
        String what = "";
        boolean tabletSize = context.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            what = DmsConstants.TABLET;
        } else {
            what = DmsConstants.PHONE;
        }
        return what;
    }

    public String getDeviceId() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getOS() {
        return "Android (" + Build.BRAND + ")";
    }


}
