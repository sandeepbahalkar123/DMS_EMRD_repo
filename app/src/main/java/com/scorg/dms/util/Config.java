package com.scorg.dms.util;

import android.content.Context;

/**
 * Created by Sandeep Bahalkar
 */
public class Config {




    public static final String HTTP = "http://";
    public static final String API = "/api/";
    public static final String TOKEN_TYPE = "Bearer";
    public static final String LOGIN_URL = "authApi/authenticate/doctorLogin";
    public static final String VERIFY_SIGN_UP_OTP = "authApi/authenticate/verifyOTP";
    public static final String SIGN_UP_URL = "authApi/authenticate/signUp";
    public static final String ONE_DAY_VISIT_URL = "api/patient/getPatientOneDayVisit?opdId=";
    public static final String GET_SMS_TEMPLATE = "doctor/api/appointment/getDoctorSmsTemplate?docId=";
    public static final String LOGOUT = "api/doctors/logDoctorSignOut";


    public static final String GET_MY_PATIENTS_LIST = "doctor/api/patient/getPatientList";
    public static final String GET_PATIENT_HISTORY = "doctor/api/patient/getPatientOpdHistory";
    public static final String ADD_TO_WAITING_LIST = "doctor/api/appointment/addToWaitingList";
    public static final String GET_CLINIC_LOCATION_LIST = "doctor/api/appointment/getDoctorLocationList?docId=";

    public static final String DELETE_WAITING_LIST = "doctor/api/appointment/deleteFromWaitingList";
    public static final String CANCEL_OR_COMPLETE_APPOINTMENT = "api/patient/updateAppointmentStatus";
    public static final String DRAG_AND_DROP_API = "doctor/api/appointment/dragDropPatientWaiting";
    public static final String GET_COMPELTED_OPD_URL = "doctor/api/appointment/getCompletedOpd";
    public static final String GET_NEW_PATIENTS_URL = "doctor/api/appointment/getNewPatients";
    public static final String DELETE_PATIENT_OPD_ATTCHMENTS = "doctor/api/patient/deletePatientOpdAttachment";
    public static final String TIME_SLOT_TO_BOOK_APPOINTMENT = "api/doctors/getDocOpenTimeSlots?";
    public static final String CONFIRM_APPOINTMENT = "api/patient/bookAppointment";
    public static final String UPLOAD_PROFILE_PHOTO = "api/upload/uploadDoctorImage";

    //---------------LOCAL SERVER URL START-----------------------
    /*public static String BASE_URL = "http://172.16.100.219:3003/";
    public static final String FORGOT_PASSWORD_URL = "http://172.16.100.219:3003/app.html#/access/forgotPassword";
    public static final String ADD_NEW_PATIENT_WEB_URL = "http://172.16.100.219:3003/app.html#/addpatientmobile/";
    public static final String BROKER = "tcp://172.16.100.219:1883";//"tcp://52.66.154.249:1883"; // Dr Rescribe.com IP QA
    */
    //---------------LOCAL SERVER URL END-----------------------

    //---------------QA SERVER URL START-----------------------

    public static String BASE_URL = "http://drrescribe.com:3003/";
    public static final String FORGOT_PASSWORD_URL = "https://drrescribe.com/app.html#/access/forgotPassword";
    public static final String ADD_NEW_PATIENT_WEB_URL = "https://drrescribe.com/app.html#/addpatientmobile/";
    public static final String BROKER = "tcp://drrescribe.com:1883";//"tcp://52.66.154.249:1883"; // Dr Rescribe.com IP QA
    //---------------QA SERVER URL END-----------------------

    //---------------LIVE URL START-----------------------
//    public static String BASE_URL = "http://rescribe.in:3003/";
//    public static final String FORGOT_PASSWORD_URL = "https://rescribe.in/app.html#/access/forgotPassword";
//    public static final String ADD_NEW_PATIENT_WEB_URL = "https://rescribe.in/app.html#/addpatientmobile/";
//    public static final String BROKER = "tcp://rescribe.in:1883";// "tcp://52.66.154.249:1883"; // Rescribe.in IP LIVE
    //---------------LIVE URL ENDS-----------------------

    public static final String MY_RECORDS_DOCTOR_LIST = "api/doctors/getDoctorsWithPatientVisits";
    public static final String MY_RECORDS_ADD_DOCTOR = "api/doctors/addDoctor";
    public static final String REQUEST_SEND_SMS = "doctor/api/appointment/sendSmsToPatients";

    public Context mContext;
    //  Declared all URL used in app here
    public static final String LOGIN_WITH_OTP_URL = "authApi/authenticate/otpLogin";
    public static final String GET_PATIENT_LIST = "api/patient/getChatPatientList?docId=";

    public static final String SEND_MSG_TO_PATIENT = "api/chat/sendMsgToPatient";
    public static final String CHAT_HISTORY = "api/chat/getChatHistory?";

    public static final String CHAT_FILE_UPLOAD = "api/upload/chatDoc";
    public static final String GET_PATIENT_CHAT_LIST = "api/chat/getChatTabUsers?user1id=";

    public static final String ACTIVE = "api/doctors/logDoctorActivity";
    public static final String MY_RECORDS_UPLOAD = "api/upload/addOpdAttachments";
    public static final String ADD_NEW_PATIENT_WEB_URL_SUCCESS = "addpatientmobilesuccess";

    public static final String GET_DOCTOR_PATIENT_CITY = "doctor/api/patient/getDoctorPatientCity";
    public static final String GET_PATIENTS_SYNC = "doctor/api/patient/getPatientSyncList";
    public static final String ADD_PATIENTS_SYNC = "doctor/api/patient/addPatients";


    //--DMS URLs-
    //Declared all URL used in app here

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

}


