package com.scorg.dms.util;

import android.content.Context;

/**
 * Created by Sandeep Bahalkar
 */
public class Config {
    public static final String HTTP = "http://";
    public static final String API = "/api/";
    public Context mContext;
    //Declared all DMS URL used in app here
    public static final String URL_LOGIN = "/api/userLogin";
    public static final String URL_ANNOTATIONS_LIST = "/api/documenttype/getAnnotations";
    public static final String URL_PATIENT_NAME_LIST = "/api/Patient";
    public static final String URL_GET_ARCHIVED_LIST = "/api/getArchived";
    public static final String URL_GET_PDF_DATA = "/api/showfile";
    public static final String URL_CHECK_SERVER_CONNECTION = "/api/connectionCheck";
    public static final String GET_EPISODE_LIST = "/api/EpisodeList";
    public static final String GET_DASHBOARD_DATA = "/api/DoctorDashboard";
    public static final String GET_WAITING_LIST = "/api/WaitingList";
    public static final String GET_MY_APPOINTMENTS_LIST = "/api/Appointment";
    public static final String URL_PATIENT_LIST = "/api/Result";
    public static final String URL_PENDING_APPROVAL_LIST = "/api/PendingApprovalList";
    public static final String URL_RAISE_REQUEST_CONFIDENTIAL = "/api/RaiseRequest";
    public static final String URL_CANCEL_REQUEST_CONFIDENTIAL = "/api/UpdateRequest";

}


