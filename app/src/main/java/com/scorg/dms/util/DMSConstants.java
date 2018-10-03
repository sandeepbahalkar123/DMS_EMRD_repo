package com.scorg.dms.util;


/**
 * @author Sandeep Bahalkar
 */
public class DMSConstants {

    public interface Images {

        String FOLDER = "/Contents/android/";

        String IC_LOGIN_BACKGROUD  = "/ic_login_backgroud.png";
        String IC_LOGIN_LOGO  = "/ic_login_logo.png";
        String IC_ACTIONBAR_LOGO  = "/ic_actionbar_logo.png";
        String LOGO_SMALL  = "/logo_small.png";
        String DASHBOARD_BACKGROUND  = "/background_dashboard.png";
    }

    public static final String BUNDLE = "bundle";
    public static final String YES = "yes";
    public static final String ID = "_id";
    public static final String DEVICEID = "Device-Id";
    public static final String OS = "OS";
    public static final String DMS_OSVERSION = "OS-Version";
    public static final String DEVICE_TYPE = "DeviceType";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String TOKEN_TYPE = "token_type";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String LDPI = "/LDPI/";
    public static final String MDPI = "/MDPI/";
    public static final String HDPI = "/HDPI/";
    public static final String XHDPI = "/XHDPI/";
    public static final String XXHDPI = "/XXHDPI/";
    public static final String XXXHDPI = "/XXXHDPI/";
    public static final String TABLET = "Tablet";
    public static final String PHONE = "Phone";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String AUTHORIZATION = "Authorization";
    public static final String GRANT_TYPE_KEY = "grant_type";
    public static final String APPLICATION_URL_ENCODED = "application/x-www-form-urlencoded; charset=UTF-8";
    public static final String APPLICATION_JSON = "application/json; charset=utf-8";
    //--- Request Params
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String LOGIN_SUCCESS = "LOGIN_SUCCESS";
    public static final String CLIENT_ID_KEY = "client_id";
    public static final String CLIENT_ID_VALUE = "334a059d82304f4e9892ee5932f81425";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String TIME = "time";
    // Connection codes
    public static final String DATE = "date";
    public static final String BLANK = "";
    public static final String DATA = "DATA";
    public static final Integer SUCCESS = 200;
    public static final String RESPONSE_OK = "OK";

    //Click codes
    public static final String TASK_GET_APPOINTMENT_DATA = BLANK + 16;
    public static final String TASK_GET_WAITING_LIST = DMSConstants.BLANK + 24;
    public static final String TASK_GET_DASHBOARD_RESPONSE = DMSConstants.BLANK + 28;
    public static final String TASK_CHECK_SERVER_CONNECTION = DMSConstants.BLANK + 39;
    public static final String TASK_PATIENT_LIST = DMSConstants.BLANK + 40;
    public static final String TASK_ANNOTATIONS_LIST = DMSConstants.BLANK + 41;
    public static final String TASK_GET_ARCHIVED_LIST = DMSConstants.BLANK + 42;
    public static final String TASK_GET_PDF_DATA = DMSConstants.BLANK + "PDF_DATA";
    public static final String TASK_GET_PATIENT_NAME_LIST = DMSConstants.BLANK + 43;
    public static final String TASK_LOGIN_CODE = DMSConstants.BLANK + 44;
    public static final String TASK_GET_EPISODE_LIST = DMSConstants.BLANK + 45;
    public static final String TASK_PENDING_APPROVAL_LIST = DMSConstants.BLANK + 46;
    public static final String TASK_RAISE_REQUEST_CONFIDENTIAL = DMSConstants.BLANK + 47;
    public static final String TASK_CANCEL_REQUEST_CONFIDENTIAL = DMSConstants.BLANK + 48;
    public static final String TASK_ADMITTED_PATIENT_DATA = DMSConstants.BLANK + 49;

    //-----------

    public static final String PATIENT_ADDRESS = "patientAddress";
    public static final String DOCTOR_NAME = "doctorName";
    public static final String APPOINTMENT_DATA = "appointment_data";
    public static final String ADMITTED_PATIENT_DATA = "admitted_patient_data";
    public static final String PATIENT_ID = "patient_id";
    public static final String PAT_ID = "pat_id";
    public static final String NO = "no";
    public static final String RECORD_ID = "record_id";
    public static final String PATIENT_DETAILS = "patient_details";
    public interface APPOINTMENT_STATUS {
        int BOOKED = 1;
        int CONFIRM = 2;
        int COMPLETED = 3;
        int CANCEL = 4;
        int NO_SHOW = 5;
        int OTHER = 6;
        String BOOKED_STATUS = "Booked";
        String CONFIRM_STATUS = "Confirm";
        String CANCEL_STATUS = "Cancel";
        String COMPLETED_STATUS = "Completed";

    }



    public interface DATE_PATTERN {
        String YYYY_MM_DD_hh_mm_a = "yyyy-MM-dd hh:mm a";
        String DD_MM = "dd/MM";
        String DD_MMM = "dd MMM";
        String MM = "MM";
        String MMM = "MMM";
       String UTC_PATTERN_2ND = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        String YYYY_MM_DD = "yyyy-MM-dd";
        String DD_MM_YYYY = "dd-MM-yyyy";
        String DD_MM_YYYY_SLASH = "dd/MM/yyyy";
        String DD_MMMM_YYYY = "dd MMMM yyyy"; // 12-September-2017
        String hh_mm_a = "hh:mm a";
        String TOTIMEZONE = "Asia/Kolkata";
        String EEEE_dd_MMM_yyyy_hh_mm_a = "EEEE dd MMM yyyy | hh:mm a";
        String HH_mm_ss = "HH:mm:ss";
        String DD_MM_YYYY_hh_mm = "dd-MM-yyyy hh:mm aa";
        String HH_MM = "hh:mm";
        String HH_mm = "HH:mm";
        String DD_MMM_YYYY = "dd MMM, yyyy";
        String MMM_YY = "MMM, yy";
        String DD_MM_YYYY_hh_mm_ss = "dd-MM-yyyy hh:mm:ss";
        String YYYY_MM_DD_hh_mm_ss = "yyyy-MM-dd hh:mm:ss";
        String YYYY_MM_DD_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
        String d_M_YYYY = "d-M-yyyy";
    }

    public interface TIME_STAMPS {
        int FIVE_FIFTY = 500;
        int ONE_SECONDS = 1000;
        int TWO_SECONDS = 2000;
        int THREE_SECONDS = 3000;
    }

    public static class PATIENT_LIST_PARAMS {
        public static final String PATIENT_NAME = "patientName";
        public static final String FILE_TYPE = "fileType";
        public static final String DATE_TYPE = "dateType";
        public static final String FROM_DATE = "fromDate";
        public static final String TO_DATE = "toDate";
        public static final String ANNOTATION_TEXT = "annotationText";
        public static final String DOC_TYPE_ID = "DocTypeId";
    }

    public static class ArchivedPreference {
        public static final String FOLDER = "Folder";
        public static final String DATE = "Date";
        public static final String FILE = "File";
    }

}


