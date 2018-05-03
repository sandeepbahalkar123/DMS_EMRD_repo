package com.rescribe.doctor.dms.util;

import android.content.Context;

/**
 * Created by Sandeep Bahalkar
 */
public class Config {
    private static final String TAG = "DMS/Config";
    public static final String HTTP = "http://";
    public static final String API = "/api/";
    public static final String TOKEN_TYPE = "Bearer";
    public static boolean DEV_BUILD = true;
    public static String BASE_URL = "";
    public Context mContext;

   /* static {
        if (DEV_BUILD) {

            *//*BASE_URL = "http://192.168.0.25:81/api/";*//*
            BASE_URL = DmsPreferencesManager.getString(DmsPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext);
            Log.e(TAG, "BASE_URL: " + BASE_URL);
        } else {
//            TODO: production base url will be added once its ready
            BASE_URL = "";
        }

    }*/

    //Declared all URL used in app here

    public static final String URL_LOGIN = "userLogin";
    public static final String URL_PATIENT_LIST = "result/ShowSearchResults";
    public static final String URL_ANNOTATIONS_LIST = "documenttype/getAnnotations";
    public static final String URL_PATIENT_NAME_LIST = "Patient";

    public static final String URL_GET_ARCHIVED_LIST = "getArchived";
    public static final String URL_GET_PDF_DATA = "showfile";
    public static final String URL_CHECK_SERVER_CONNECTION = "connectionCheck";


}
