package com.rescribe.doctor.helpers.myrecords;

import android.content.Context;

import com.android.volley.Request;
import com.rescribe.doctor.R;
import com.rescribe.doctor.interfaces.ConnectionListener;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.my_records.MyRecordBaseModel;
import com.rescribe.doctor.model.my_records.MyRecordDataModel;
import com.rescribe.doctor.model.my_records.MyRecordInfoAndReports;
import com.rescribe.doctor.model.my_records.MyRecordInfoMonthContainer;
import com.rescribe.doctor.model.my_records.RequestAddDoctorModel;
import com.rescribe.doctor.model.my_records.new_pojo.NewMonth;
import com.rescribe.doctor.model.my_records.new_pojo.NewMyRecordBaseModel;
import com.rescribe.doctor.model.my_records.new_pojo.NewMyRecordDataModel;
import com.rescribe.doctor.model.my_records.new_pojo.NewOriginalData;
import com.rescribe.doctor.network.ConnectRequest;
import com.rescribe.doctor.network.ConnectionFactory;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.Config;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by riteshpandhurkar on 1/3/17.
 */

public class MyRecordsHelper implements ConnectionListener {

    String TAG = this.getClass().getName();
    Context mContext;
    HelperResponse mHelperResponseManager;

    private Map<String, Map<String, ArrayList<MyRecordInfoAndReports>>> yearWiseSortedMyRecordInfoAndReports = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);


    public MyRecordsHelper(Context context, HelperResponse mHelperResponseManager) {
        this.mContext = context;
        this.mHelperResponseManager = mHelperResponseManager;
    }


    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                CommonMethods.Log(TAG, customResponse.getClass() + " success");

                if (customResponse instanceof NewMyRecordBaseModel) {

                    NewMyRecordBaseModel newModel = (NewMyRecordBaseModel) customResponse;
                    MyRecordBaseModel model = new MyRecordBaseModel();
                    MyRecordDataModel myRecordDataModel = new MyRecordDataModel();
                    NewMyRecordDataModel newRecordMainDataModel = newModel.getData();
                    model.setCommon(newModel.getCommon());
                    model.setRecordMainDataModel(myRecordDataModel);
                    myRecordDataModel.setReceivedYearMap(newRecordMainDataModel.getYearsMonthsData());
                    MyRecordInfoMonthContainer myRecordInfoMonthContainerNew = new MyRecordInfoMonthContainer();
                    myRecordInfoMonthContainerNew.setYear(String.valueOf(newRecordMainDataModel.getOriginalData().getYear()));
                    NewOriginalData newOriginalData = newRecordMainDataModel.getOriginalData();

                    TreeMap<String, ArrayList<MyRecordInfoAndReports>> monthWiseSortedMyRecords = new TreeMap<String, ArrayList<MyRecordInfoAndReports>>(String.CASE_INSENSITIVE_ORDER);

                    for (NewMonth newMonth : newOriginalData.getMonths()) {
                        ArrayList<MyRecordInfoAndReports> docVisits = newMonth.getDocVisits();
                        Collections.sort(docVisits, new DateWiseComparator());
                        String month = newMonth.getMonth();
                        monthWiseSortedMyRecords.put(month, docVisits);
                    }

                    myRecordInfoMonthContainerNew.setMonthWiseSortedMyRecords(monthWiseSortedMyRecords);

                    myRecordDataModel.setMyRecordInfoMonthContainer(myRecordInfoMonthContainerNew);

                    MyRecordDataModel recordMainDataModel = model.getRecordMainDataModel();
                    if (recordMainDataModel.getMyRecordInfoMonthContainer() != null) {
                        MyRecordInfoMonthContainer myRecordInfoMonthContainer = recordMainDataModel.getMyRecordInfoMonthContainer();
                        Map<String, ArrayList<MyRecordInfoAndReports>> monthWiseSortedMyRecords1 = myRecordInfoMonthContainer.getMonthWiseSortedMyRecords();
                        yearWiseSortedMyRecordInfoAndReports.put(myRecordInfoMonthContainer.getYear(), monthWiseSortedMyRecords1);
                    }
                }

                mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                break;
            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, mContext.getString(R.string.parse_error));
                mHelperResponseManager.onParseError(mOldDataTag, mContext.getString(R.string.parse_error));
                break;
            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, mContext.getString(R.string.server_error));
                mHelperResponseManager.onServerError(mOldDataTag, mContext.getString(R.string.server_error));
                break;
            case ConnectionListener.NO_CONNECTION_ERROR:
                CommonMethods.Log(TAG, mContext.getString(R.string.no_connection_error));
                mHelperResponseManager.onNoConnectionError(mOldDataTag, mContext.getString(R.string.no_connection_error));
                break;
            default:
                CommonMethods.Log(TAG, mContext.getString(R.string.default_error));
                break;
        }
    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }

    public Map<String, Map<String, ArrayList<MyRecordInfoAndReports>>> getYearWiseSortedMyRecordInfoAndReports() {
        return yearWiseSortedMyRecordInfoAndReports;
    }


    public void getDoctorList(String patientId) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.MY_RECORDS_DOCTOR_LIST, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.MY_RECORDS_DOCTOR_LIST + "?patientId=" + 539842);
        mConnectionFactory.createConnection(RescribeConstants.MY_RECORDS_DOCTOR_LIST);
    }

    public void addDoctor(RequestAddDoctorModel requestAddDoctorModel) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.MY_RECORDS_ADD_DOCTOR, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(requestAddDoctorModel);
        mConnectionFactory.setUrl(Config.MY_RECORDS_ADD_DOCTOR);
        mConnectionFactory.createConnection(RescribeConstants.MY_RECORDS_ADD_DOCTOR);
    }

   /* public void doGetAllMyRecords(String year) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_GET_ALL_MY_RECORDS, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        String patientId = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PATIENT_ID, mContext);
        mConnectionFactory.setUrl(Config.LIST_ALL_MY_RECORD + patientId + "&year=" + year);
        mConnectionFactory.createConnection(RescribeConstants.TASK_GET_ALL_MY_RECORDS);
    }*/


    private class DateWiseComparator implements Comparator<MyRecordInfoAndReports> {

        public int compare(MyRecordInfoAndReports m1, MyRecordInfoAndReports m2) {

            //possibly check for nulls to avoid NullPointerException
            //  String s = CommonMethods.formatDateTime(m1, RescribeConstants.DATE_PATTERN.YYYY_MM_DD, RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE);
            Date m1Date = CommonMethods.convertStringToDate(m1.getMyRecordDoctorInfo().getDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
            Date m2Date = CommonMethods.convertStringToDate(m2.getMyRecordDoctorInfo().getDate(), RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
            return m2Date.compareTo(m1Date);
        }
    }
}
