package com.scorg.dms.util;


/**
 * @author Sandeep Bahalkar
 */
public class DMSConstants {

    public static final String RESCRIBE_LOG_FOLDER = "Rescribe_LOG";
    public static final String RESCRIBE_LOG_FILE = "Rescribe_LOG_FILE.txt";



    public static final String MT_TABLET = "TABLET";
    public static final String MT_SYRUP = "SYRUP";
    public static final String MT_OINTMENT = "OINTMENT";
    public static final String YES = "yes";
    public static final String DOCUMENTS = "documents";
    public static final String ATTACHMENTS_LIST = "ATTACHMENTS_LIST";
    public static final String ALERT = "alert";
    public static final String ID = "_id";
    public static final String USER_ID = "User-ID";
    public static final String DEVICEID = "Device-Id";
    public static final String OS = "OS";
    public static final String OSVERSION = "OsVersion";
    public static final String DMS_OSVERSION = "OS-Version";
    public static final String DEVICE_TYPE = "DeviceType";
    public static final String APP_NAME = "appName";
    public static final String ACCESS_TOKEN = "accessToken";
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
    public static final String AUTHORIZATION_TOKEN = "authtoken";
    public static final String AUTH_KEY = "Auth-Key";
    public static final String CLIENT_SERVICE = "Client-Service";
    public static final String GRANT_TYPE_KEY = "grant_type";

    public static final String APPLICATION_FORM_DATA = "multipart/form-data";
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

    public static final String APPOINTMENT_TIME = "appointment_time";
    public static final String NOTIFICATION_TIME = "notification_time";
    public static final String APPOINTMENT_DATE = "appointment_date";
    public static final String NOTIFICATION_DATE = "notification_date";
    public static final String TIME = "time";

    // Connection codes
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String DATE = "date";
    public static final String MEDICINE_NAME = "medicine_name";
    public static final String MEDICINE_SLOT = "edicine_slot";
    public static final String MEDICINE_TYPE = "medicine_type";
    public static final String NOTIFICATION_ID = "notification_id";

    public static final String APPOINTMENT_NOTIFICATION_ID = "appointment_notification_id";
    public static final String APPOINTMENT_MESSAGE = "appointment_message";
    public static final String BLANK = "";
    //Click codes

    public static final String TASK_LOGIN = DMSConstants.BLANK + 1;
    public static final String TASK_SIGN_UP = DMSConstants.BLANK + 2;
    public static final String TASK_VERIFY_SIGN_UP_OTP = DMSConstants.BLANK + 3;
    public static final String TASK_LOGIN_WITH_PASSWORD = DMSConstants.BLANK + 4;
    public static final String TASK_LOGIN_WITH_OTP = DMSConstants.BLANK + 5;
    public static final String TASK_DOCTOR_CONNECT_CHAT = DMSConstants.BLANK + 6;
    public static final String TASK_DOCTOR_CONNECT = DMSConstants.BLANK + 7;
    public static final String TASK_DOCTOR_FILTER_DOCTOR_SPECIALITY_LIST = DMSConstants.BLANK + 8;
    public static final String TASK_GET_PATIENT_LIST = DMSConstants.BLANK + 9;
    public static final String SEND_MESSAGE = DMSConstants.BLANK + 10;
    public static final String CHAT_HISTORY = DMSConstants.BLANK + 11;
    public static final String CHAT_USERS = DMSConstants.BLANK + 12;
    public static final String GET_PATIENT_CHAT_LIST = DMSConstants.BLANK + 13;

    public static final String ACTIVE_STATUS = DMSConstants.BLANK + 14;
    public static final String LOGOUT = DMSConstants.BLANK + 15;
    public static final String TASK_GET_APPOINTMENT_DATA = BLANK + 16;
    public static final String TASK_GET_PATIENT_DATA = BLANK + 17;
    public static final String TASK_ONE_DAY_VISIT = BLANK + 18;
    public static final String MY_RECORDS_DOCTOR_LIST = DMSConstants.BLANK + 19;
    public static final String MY_RECORDS_ADD_DOCTOR = DMSConstants.BLANK + 20;
    public static final String TASK_PATIENT_HISTORY = DMSConstants.BLANK + 21;
    public static final String TASK_GET_DOCTOR_SMS_TEMPLATE = DMSConstants.BLANK + 22;
    public static final String TASK_REQUEST_SEND_SMS = DMSConstants.BLANK + 23;
    public static final String TASK_GET_WAITING_LIST = DMSConstants.BLANK + 24;
    public static final String TASK_GET_SEARCH_RESULT_MY_PATIENT = DMSConstants.BLANK + 25;
    public static final String TASK_ADD_TO_WAITING_LIST = DMSConstants.BLANK + 26;
    public static final String TASK_GET_LOCATION_LIST = DMSConstants.BLANK + 27;
    public static final String TASK_GET_DASHBOARD_RESPONSE = DMSConstants.BLANK + 28;
    public static final String TASK_DELETE_WAITING_LIST = DMSConstants.BLANK + 29;
    public static final String TASK_APPOINTMENT_CANCEL_OR_COMPLETE = DMSConstants.BLANK + 30;
    public static final String TASK_DARG_DROP = DMSConstants.BLANK + 31;
    public static final String TASK_GET_COMPLETED_OPD = DMSConstants.BLANK + 32;
    public static final String TASK_GET_NEW_PATIENT_LIST = DMSConstants.BLANK + 33;
    public static final String TASK_GET_DOCTOR_PATIENT_CITY = DMSConstants.BLANK + 34;
    public static final String TASK_ADD_NEW_PATIENT = DMSConstants.BLANK + 35;
    public static final String TASK_DELETE_PATIENT_OPD_ATTCHMENTS = DMSConstants.BLANK + 36;
    public static final String TASK_GET_TIME_SLOTS_TO_BOOK_APPOINTMENT = DMSConstants.BLANK + 37;
    public static final String TASK_CONFIRM_APPOINTMENT = DMSConstants.BLANK + 38;
    public static final String DATA = "DATA";
    public static final Integer SUCCESS = 200;

    //-----------
    public static final String TASK_CHECK_SERVER_CONNECTION = DMSConstants.BLANK + 39;
    public static final String TASK_PATIENT_LIST = DMSConstants.BLANK + 40;
    public static final String TASK_ANNOTATIONS_LIST = DMSConstants.BLANK + 41;
    public static final String TASK_GET_ARCHIVED_LIST = DMSConstants.BLANK + 42;
    public static final String TASK_GET_PDF_DATA = DMSConstants.BLANK + "PDF_DATA";
    public static final String TASK_GET_PATIENT_NAME_LIST = DMSConstants.BLANK + 43;
    public static final String TASK_LOGIN_CODE = DMSConstants.BLANK + 44;

    //-----------

    public static final String TAKEN_DATE = "takenDate";
    public static final String PATIENT_ADDRESS = "patientAddress";
    public static final String DOCTOR_NAME = "doctorName";
    public static final String TITLE = "title";
    public static final int MAX_RETRIES = 3;

    public static final String GMAIL_LOGIN = "gmail_login";
    public static final String FACEBOOK_LOGIN = "facebook_login";

    public static final String UPLOADING_STATUS = "uploading_status";
    public static final String INVESTIGATION_NOTIFICATION_TIME = "9:00 AM";
    public static final String APPOINTMENT_NOTIFICATION_TIME = "9:00 AM";
    public static final String GMAIL_PACKAGE = "com.google.android.gm";
    public static final String EMAIL = "email";
    public static final String OPD_ID = "opd_id";
    public static final String MYRECORDDATAMODEL = "myrecorddatamodel";
    public static final String CAPTION = "caption";
    public static final String TYPE_OF_LOGIN = "";
    public static final String SENDERID = "EMROTP";
    public static final String IS_URL = "isUrl";
    public static final String CHAT_REQUEST = "chat_request";
    public static final String SEARCH__REQUEST = "search_request";
    public static final String CONNECT_REQUEST = "connect_request";
    public static final String PATIENT_INFO = "patient_info";
    public static final int PLACE_PICKER_REQUEST = 99;
    public static final String APPOINTMENT_DATA = "appointment_data";
    public static final String DOCTOR_IMAGE_URL = "doctor_iamge_url";
    public static final String PATIENT_VISIT_DATE = "patient_visit_date";
    public static final String PATIENT_ID = "patient_id";
    public static final String NO = "no";
    public static final String DOCTORS_ID = "doctor_id";
    public static final String VISIT_DATE = "visitDate";
    public static final String PATIENT_OBJECT = "patient_object";
    public static final String PATIENT_NAME = "patient_name";
    public static final String PREMIUM = "Premium";
    public static final String LONGPRESSED = "long_pressed";
    public static final String DATES_LIST = "dates_list";
    public static final String DATES_INFO = "dates_info";
    public static final String FILTER_STATUS_LIST = "filter_status_list";
    public static final String FILTER_CLINIC_LIST = "filter_clinic_list";
    public static final String APPOINTMENT_LIST = "appointment_list";
    public static final String SMS_DETAIL_LIST = "sms_detail_list";
    public static final String WAITING_LIST_INFO = "waiting_list_info";
    public static final String SMS_PATIENT_LIST_TO_SHOW = "sms_list_to_show";
    public static final String PATIENT_HOS_PAT_ID = "hos_pat_id";
    public static final String PATIENT_OPDID = "patient_opdid";
    public static final String PATIENT_LIST = "patient_list";
    public static final String LOCATION_ID = "location_id";
    public static final String CITY_ID = "city_id";
    public static final String CITY_NAME = "city_name";
    public static final String TEMPLATE_OBJECT = "template_object";
    public static final String CLINIC_ID = "clinic_id";
    public static final String CLINIC_NAME = "clinic_name";
    public static final String ACTIVITY_LAUNCHED_FROM = "activity_launched_from";
    public static final String WAITING_LIST = "waiting_list";
    public static final String HOME_PAGE = "home_page";
    public static final String OPD_TIME = "opd_time";
    public static final String IS_CALL_FROM_MY_PATIENTS = "call_from_my_patients_chat";
    public static final String TEMPLATE_LIST = "template_list";
    public static final float ZOOM_CAMERA_VALUE = 7.0f;
    public static final String COMPLETE_OPD = "complete_opd";
    public static final String NOT_FROM_COMPLETE_OPD = "not_complete_opd";
    public static final String PATIENT_DETAILS = "patient_details";

    public static final String STARTFOREGROUND_ACTION = "com.rescribe.download";
    public static final int FOREGROUND_SERVICE = 21;
    public static final String MY_PATIENTS_DATA = "my_patient_data";
    public static final String MY_APPOINTMENTS = "my_appointments";
    public static final String PATIENT_CONNECT = "patient_connect";
    public static final String IS_APPOINTMENT_TYPE_RESHEDULE = "appointment_type_reschedule";
    public static final String DOCTOR = "doctor";
    public static final String APPOINTMENT_ID = "appointment_id";
    public static String HEADER_COLOR = "#E4422C";
    public static String BUTTON_TEXT_COLOR = "#FFFFFF";
    public static String TEXT_COLOR = "#000000";

    public static final String[] SALUTATION = {"Mr. ", "Mrs. ", "Miss ", ""};
    public static final String[] GENDER = {"MALE", "FEMALE", "TRANSGENDER"};
    public static String OT_AND_OPD = "ot_and_opd";
    public static String OPD = "opd";
    public static String OT = "ot";

    public interface APPOINTMENT_STATUS {
        int BOOKED = 1;
        int CONFIRM = 2;
        int COMPLETED = 3;
        int CANCEL = 4;
        int NO_SHOW = 5;
        int OTHER = 6;
        int IN_CONSULTATION = 7;
        int IN_QUEUE = 8;
        int COMING = 9;
    }

    public interface USER_STATUS {
        String ONLINE = "Online";
        String OFFLINE = "Offline";
        String TYPING = "Typing";
        String IDLE = "Idle";
    }

    // change
    public interface MESSAGE_STATUS {
        String SEEN = "seen";
        String REACHED = "reached";
        String SENT = "sent";
        String PENDING = "pending";

        int READ = 1;
        int UNREAD = 0;
    }

    public interface FILE_STATUS {
        int UPLOADING = 1;
        int DOWNLOADING = 1;
        int FAILED = 0;
        int COMPLETED = 3;
    }

    public interface PRESCRIPTION_LIST_PARAMS {
        String PATIENT_NAME = "User-ID";
        String FILE_TYPE = "fileType";
        String DATE_TYPE = "dateType";
        String FROM_DATE = "fromDate";
        String TO_DATE = "toDate";
        String ANNOTATION_TEXT = "annotationText";
        String DOC_TYPE_ID = "DocTypeId";
    }


    public interface DATE_PATTERN {
        String YYYY_MM_DD_hh_mm_a = "yyyy-MM-dd hh:mm a";
        String DD_MM = "dd/MM";
        String DD_MMM = "dd MMM";
        String MM = "MM";
        String MMM = "MMM";
        String UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        String YYYY_MM_DD = "yyyy-MM-dd";
        String DD_MM_YYYY = "dd-MM-yyyy";
        String DD_MM_YYYY_SLASH = "dd/MM/yyyy";
        String DD_MMMM_YYYY = "dd MMMM yyyy"; // 12-September-2017
        String hh_mm_a = "hh:mm a";
        String TOTIMEZONE = "Asia/Kolkata";
        String EEEE_dd_MMM_yyyy_hh_mm_a = "EEEE dd MMM yyyy | hh:mm a";
        String HH_mm_ss = "HH:mm:ss";
        String DD_MM_YYYY_hh_mm = "dd/MM/yyyy hh:mm aa";
        String HH_MM = "hh:mm";
        String HH_mm = "HH:mm";
        String MMM_YYYY = "MMM, yyyy";
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

    public interface FILE {
        String IMG = "img";
        String DOC = "doc";
        String VID = "vid";
        String AUD = "aud";
        String LOC = "loc";
    }

    public interface FILE_EMOJI {
        String IMG_FILE = "🏞 Image";
        String DOC_FILE = "📒 Document";
        String VID_FILE = "📹 Video";
        String AUD_FILE = "🔊 Audio";
        String LOC_FILE = "📍 Location";
    }

    public interface PATIENT_OPDS_STATUS {
        String OPD_COMPLETED = "opd completed";
        String OPD_SAVED = "opd saved";
        String ONLY_ATTACHMENTS = "only attachments";
        String NO_SHOW = "no show";
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

}


