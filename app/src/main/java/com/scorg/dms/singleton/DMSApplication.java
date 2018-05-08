package com.scorg.dms.singleton;

import android.content.Context;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.scorg.dms.helpers.database.AppDBHelper;
import com.scorg.dms.model.doctor_location.DoctorLocationModel;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Sandeep Bahalkar
 */
public class DMSApplication extends MultiDexApplication {
    public final String TAG = this.getClass().getName();
    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();
    private static ArrayList<DoctorLocationModel> doctorLocationModels = new ArrayList<>();
    private static String SHOW_UPDATE_DIALOG_ON_SKIPPED = "";

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
        //------------
        MultiDex.install(this);
        AppDBHelper.getInstance(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
//        new NukeSSLCerts().nuke(); // disable all ssl certificates (dangerous)
        //--------------
    }

    public static ArrayList<DoctorLocationModel> getDoctorLocationModels() {
        return doctorLocationModels;
    }

    public static void setDoctorLocationModels(ArrayList<DoctorLocationModel> doctorLocationModels) {
        DMSApplication.doctorLocationModels = doctorLocationModels;
    }


}