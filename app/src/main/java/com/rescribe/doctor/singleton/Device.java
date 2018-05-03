package com.rescribe.doctor.singleton;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.rescribe.doctor.R;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

/**
 * Created by Sandeep Bahalkar
 */

public class Device {

    private  final String TAG = this.getClass().getName();
    private Context context;
    private WindowManager windowManager;

    public Device(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    public Device(Context context) {
        this.context = context;
    }

    /**
     * Create a static method to get instance.
     */

    public static Device getInstance(Context context) {

        return new Device(context);
    }

    public static Device getInstance(WindowManager windowManager) {

        return new Device(windowManager);
    }

    public String getDensity() {

        String density = RescribeConstants.HDPI;

        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                density = RescribeConstants.LDPI;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                density = RescribeConstants.MDPI;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                density = RescribeConstants.HDPI;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                density = RescribeConstants.XHDPI;
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                density = RescribeConstants.XXHDPI;
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                density = RescribeConstants.XXXHDPI;
                break;
        }

        CommonMethods.Log(TAG, density);

        return density;
    }

    public String getDeviceType() {
        String what = "";
        boolean tabletSize = context.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            what = RescribeConstants.TABLET;
        } else {
            what = RescribeConstants.PHONE;
        }
        return what;
    }

    public String getDeviceId() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getOSVersion() {
        return Build.VERSION.RELEASE + "(" + Build.BRAND + ")";
    }

    public String getOS() {
        return "Android";
    }
}
