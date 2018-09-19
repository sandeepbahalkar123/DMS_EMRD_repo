package com.scorg.dms.singleton;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.scorg.dms.R;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

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

        String density = DMSConstants.HDPI;

        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        switch (metrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                density = DMSConstants.LDPI;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                density = DMSConstants.MDPI;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                density = DMSConstants.HDPI;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                density = DMSConstants.XHDPI;
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                density = DMSConstants.XXHDPI;
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                density = DMSConstants.XXXHDPI;
                break;
        }

        CommonMethods.Log(TAG, density);

        return density;
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
