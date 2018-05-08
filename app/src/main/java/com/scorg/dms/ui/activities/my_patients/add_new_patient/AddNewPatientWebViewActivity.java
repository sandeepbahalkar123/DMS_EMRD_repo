package com.scorg.dms.ui.activities.my_patients.add_new_patient;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.helpers.database.AppDBHelper;
import com.scorg.dms.helpers.myappointments.AppointmentHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.Common;
import com.scorg.dms.model.patient.doctor_patients.PatientList;
import com.scorg.dms.model.patient.doctor_patients.sync_resp.PatientUpdateDetail;
import com.scorg.dms.model.patient.doctor_patients.sync_resp.SyncPatientsModel;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.services.MQTTService;
import com.scorg.dms.ui.activities.my_patients.patient_history.PatientHistoryActivity;
import com.scorg.dms.ui.customesViews.CustomProgressDialog;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.Config;
import com.scorg.dms.util.DMSConstants;
import com.scorg.dms.util.NetworkUtil;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.scorg.dms.util.DMSConstants.SUCCESS;

public class AddNewPatientWebViewActivity extends AppCompatActivity implements HelperResponse {

    private static final String TAG = "AddPatient";
    public static final int ADD_PATIENT_REQUEST = 121;

    //---------
    @BindView(R.id.mainParentScrollViewLayout)
    ScrollView mMainParentScrollViewLayout;
    //----------
    @BindView(R.id.webViewLayout)
    WebView mWebViewObject;
    @BindView(R.id.backButton)
    AppCompatImageView backButton;
    @BindView(R.id.webViewTitle)
    TextView mWebViewTitle;
    //---------
    @BindView(R.id.firstName)
    EditText mFirstName;
    @BindView(R.id.middleName)
    EditText mMiddleName;
    @BindView(R.id.lastName)
    EditText mLastName;
    @BindView(R.id.mobNo)
    EditText mMobNo;
    @BindView(R.id.age)
    EditText mAge;
    @BindView(R.id.referenceID)
    EditText mReferenceID;
    @BindView(R.id.btnAddPatientSubmit)
    Button mSubmit;
    @BindView(R.id.genderRadioGroup)
    RadioGroup mGenderRadioGroup;

    private int hospitalId;
    private boolean isCalled = false;
    private String locationID;
    private int docID;
    private int cityID;
    private String cityName;
    private Context mContext;
    private boolean mAddPatientOfflineSetting;
    private PatientList mAddedPatientListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.add_new_patient);
        ButterKnife.bind(this);

        mContext = this;

        Bundle extras = getIntent().getBundleExtra(DMSConstants.PATIENT_DETAILS);
        hospitalId = extras.getInt(DMSConstants.CLINIC_ID);
        locationID = extras.getString(DMSConstants.LOCATION_ID);
        cityID = extras.getInt(DMSConstants.CITY_ID);
        cityName = extras.getString(DMSConstants.CITY_NAME);
        docID = Integer.valueOf(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, this));

        String urlData = Config.ADD_NEW_PATIENT_WEB_URL + docID + "/" +
                hospitalId + "/" + locationID + "/" + cityID;

        mWebViewTitle.setText(getString(R.string.patient_registration));

        mAddPatientOfflineSetting = DMSPreferencesManager.getBoolean(DMSPreferencesManager.DMS_PREFERENCES_KEY.ADD_PATIENT_OFFLINE_SETTINGS, this);

        boolean internetAvailable = NetworkUtil.isInternetAvailable(this);
        if (internetAvailable && !mAddPatientOfflineSetting) {
            mWebViewObject.setVisibility(View.VISIBLE);
            mMainParentScrollViewLayout.setVisibility(View.GONE);
            loadWebViewData(urlData);
        } else {
            if (!internetAvailable)
                CommonMethods.showToast(this, getString(R.string.add_patient_offline_msg));
            mWebViewObject.setVisibility(View.GONE);
            mMainParentScrollViewLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.backButton, R.id.btnAddPatientSubmit})
    public void back(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                onBackPressed();
                break;
            case R.id.btnAddPatientSubmit:
                mAddedPatientListData = validate();
                boolean internetAvailable = NetworkUtil.isInternetAvailable(this);
                if (mAddedPatientListData != null) {
                    if (internetAvailable && mAddPatientOfflineSetting) {
                        AppointmentHelper m = new AppointmentHelper(this, this);
                        m.addNewPatient(mAddedPatientListData);
                    } else {

                        addOfflinePatient();
                    }
                }
                break;
        }
    }

    private void addOfflinePatient() {

        if (AppDBHelper.getInstance(mContext).addNewPatient(mAddedPatientListData) != -1) {
            Bundle bundle = new Bundle();
            // this is done to replzce | with space, | used in case blank middle name.
            String replace = mAddedPatientListData.getPatientName().replace("|", "");
            bundle.putString(DMSConstants.PATIENT_NAME, replace);

            //------------
            String patientInfo = "";
            if (!mAddedPatientListData.getAge().isEmpty() && !mAddedPatientListData.getGender().isEmpty())
                patientInfo = mAddedPatientListData.getAge() + " yrs - " + mAddedPatientListData.getGender();
            else if (!mAddedPatientListData.getAge().isEmpty())
                patientInfo = mAddedPatientListData.getAge() + " yrs";
            else if (!mAddedPatientListData.getGender().isEmpty())
                patientInfo = mAddedPatientListData.getGender();

            bundle.putString(DMSConstants.PATIENT_INFO, patientInfo);
            //------------

            bundle.putInt(DMSConstants.CLINIC_ID, mAddedPatientListData.getClinicId());
            bundle.putString(DMSConstants.PATIENT_ID, String.valueOf(mAddedPatientListData.getPatientId()));
            bundle.putString(DMSConstants.PATIENT_HOS_PAT_ID, String.valueOf(mAddedPatientListData.getHospitalPatId()));
            Intent intent = new Intent(this, PatientHistoryActivity.class);
            intent.putExtra(DMSConstants.PATIENT_INFO, bundle);
            startActivity(intent);
            finish();
        } else
            CommonMethods.showToast(mContext, "Failed to store");
    }

    @Override
    public void onBackPressed() {
        if (mMainParentScrollViewLayout.getVisibility() == View.VISIBLE) {
            finish();
        } else {
            if (mWebViewObject.canGoBack()) {
                mWebViewObject.goBack();
            } else {
                super.onBackPressed();
            }
        }

    }

    private void loadWebViewData(String url) {

        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(this);
        customProgressDialog.show();

        if (url != null) {
            mWebViewObject.setVisibility(View.VISIBLE);

            WebSettings webSettings = mWebViewObject.getSettings();

            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(true);
            webSettings.setDefaultTextEncodingName("utf-8");

            mWebViewObject.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    // Activities and WebViews measure progress with different scales.
                    // The progress meter will automatically disappear when we reach 100%
                    setProgress(progress);
                    if (progress > 90)
                        customProgressDialog.dismiss();
                }
            });

            renderWebPage(url);

            mWebViewObject.loadUrl(url);
        }
    }

    // Custom method to render a web page
    protected void renderWebPage(String urlToRender) {
        mWebViewObject.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // Do something on page loading started
                Log.d(TAG + "Start", url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Do something when page loading finished
                Log.d(TAG + "Finish", url);

                // https://drrescribe.com/app.html#/addpatientmobilesuccess/541170

                if (url.toLowerCase().contains(Config.ADD_NEW_PATIENT_WEB_URL_SUCCESS) && !isCalled) {
                    String[] split = url.split("/");
                    String patientId = split[split.length - 2];
                    String hospitalPatId = split[split.length - 1];
                    callNextActivity(patientId, hospitalPatId);
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Log.d(TAG, "ShouldOverride " + url);

                if (url.toLowerCase().contains(Config.ADD_NEW_PATIENT_WEB_URL_SUCCESS) && !isCalled) {

                    String[] split = url.split("/");
                    String patientId = split[split.length - 2];
                    String hospitalPatId = split[split.length - 1];

                    callNextActivity(patientId, hospitalPatId);
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }


    private void callNextActivity(String patientId, String hospitalPatId) {

        isCalled = true;

        Bundle b = new Bundle();
        b.putString(DMSConstants.PATIENT_ID, patientId);
        b.putInt(DMSConstants.CLINIC_ID, hospitalId);
        b.putString(DMSConstants.PATIENT_HOS_PAT_ID, hospitalPatId);
        Intent intent = new Intent(this, PatientHistoryActivity.class);
        intent.putExtra(DMSConstants.PATIENT_INFO, b);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        startActivity(intent);
        finish();
    }

    private PatientList validate() {
        String message;
        PatientList patientList = null;
        String enter = getString(R.string.enter);
        String firstName = mFirstName.getText().toString().trim();
        String middleName = mMiddleName.getText().toString().trim();
        String lastName = mLastName.getText().toString().trim();
        String mob = mMobNo.getText().toString().trim();
        String age = mAge.getText().toString().trim();
        String refID = mReferenceID.getText().toString().trim();

        boolean enteredRefIDIsValid = isEnteredRefIDIsValid(refID);

        if (firstName.isEmpty()) {
            message = enter + getString(R.string.first_name).toLowerCase(Locale.US);
            CommonMethods.showToast(this, message);
        } else if (lastName.isEmpty()) {
            message = enter + getString(R.string.last_name);
            CommonMethods.showToast(this, message);
        } else if (mob.isEmpty() || mob.length() < 10) {
            message = enter + getString(R.string.enter_mobile_no);
            CommonMethods.showToast(this, message);
        } else if ((mob.trim().length() < 10) || !(mob.trim().startsWith("6") || mob.trim().startsWith("7") || mob.trim().startsWith("8") || mob.trim().startsWith("9"))) {
            message = getString(R.string.err_invalid_mobile_no);
            CommonMethods.showToast(this, message);
        } else if ((!age.isEmpty()) && Integer.parseInt(age) > 101) {
            message = getString(R.string.age_err_msg);
            CommonMethods.showToast(this, message);
        } else if (!enteredRefIDIsValid) {
            message = getString(R.string.reference_id_input_err_msg);
            CommonMethods.showToast(this, message);
        } else {
            patientList = new PatientList();
            int id = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
            patientList.setPatientId(id);
            if (middleName.trim().length() == 0) {
                middleName = "|";
            }
            patientList.setPatientName(firstName + " " + middleName + " " + lastName);
            patientList.setSalutation(0);
            patientList.setOutStandingAmount("0.00");
            patientList.setPatientImageUrl("");
            patientList.setPatientEmail("");
            patientList.setPatientPhone(mob);
            patientList.setAge(age);
            RadioButton viewById = (RadioButton) findViewById(mGenderRadioGroup.getCheckedRadioButtonId());
            if (viewById != null)
                patientList.setGender(viewById.getText().toString());
            else
                patientList.setGender("");

            patientList.setReferenceID(refID);
            patientList.setOfflinePatientSynced(false);
            patientList.setClinicId(hospitalId);
            patientList.setHospitalPatId(id + 1);
            patientList.setPatientCity(cityName);
            patientList.setPatientCityId(cityID);
            patientList.setCreationDate(CommonMethods.getCurrentTimeStamp(DMSConstants.DATE_PATTERN.UTC_PATTERN));
        }

         /*else if (middleName.isEmpty()) {
            message = enter + getString(R.string.middle_name).toLowerCase(Locale.US);
            CommonMethods.showToast(this, message);
        }
         else if (age.isEmpty() ) {
            message = enter + " valid " + getString(R.string.age);
            CommonMethods.showToast(this, message);
        }*/
        return patientList;
    }

    public static boolean isEnteredRefIDIsValid(String str) {
        boolean isValid = false;
        if (str.isEmpty()) {
            return true;
        } else {
            String expression = "^[a-z_A-Z0-9]*$";
            CharSequence inputStr = str;
            Pattern pattern = Pattern.compile(expression);
            Matcher matcher = pattern.matcher(inputStr);
            if (matcher.matches()) {
                isValid = true;
            }
        }
        return isValid;
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        switch (mOldDataTag) {
            case DMSConstants.TASK_ADD_NEW_PATIENT:
                SyncPatientsModel mSyncPatientsModel = (SyncPatientsModel) customResponse;
                Common common = mSyncPatientsModel.getCommon();
                if (common != null) {
                    CommonMethods.showToast(this, common.getStatusMessage());
                    if (common.getStatusCode().equals(SUCCESS)) {

                        //-----------
                        PatientUpdateDetail patientUpdateDetail = mSyncPatientsModel.getData().getPatientUpdateDetails().get(0);

                        mAddedPatientListData.setPatientId(patientUpdateDetail.getPatientId());
                        mAddedPatientListData.setHospitalPatId(patientUpdateDetail.getHospitalPatId());
                        mAddedPatientListData.setOfflinePatientSynced(true);
                        AppDBHelper.getInstance(mContext).addNewPatient(mAddedPatientListData);
                        //-----------

                        // start service if closed
                        Intent intentMQTT = new Intent(this, MQTTService.class);
                        startService(intentMQTT);

                        Bundle bundle = new Bundle();
                        String replace = mAddedPatientListData.getPatientName().replace("|", "");

                        bundle.putString(DMSConstants.PATIENT_NAME, replace);
                        //------------
                        String patientInfo = "";
                        if (!mAddedPatientListData.getAge().isEmpty() && !mAddedPatientListData.getGender().isEmpty())
                            patientInfo = mAddedPatientListData.getAge() + " yrs - " + mAddedPatientListData.getGender();
                        else if (!mAddedPatientListData.getAge().isEmpty())
                            patientInfo = mAddedPatientListData.getAge() + " yrs";
                        else if (!mAddedPatientListData.getGender().isEmpty())
                            patientInfo = mAddedPatientListData.getGender();

                        bundle.putString(DMSConstants.PATIENT_INFO, patientInfo);
                        //------------
                        bundle.putInt(DMSConstants.CLINIC_ID, mAddedPatientListData.getClinicId());
                        bundle.putString(DMSConstants.PATIENT_ID, String.valueOf(mAddedPatientListData.getPatientId()));
                        bundle.putString(DMSConstants.PATIENT_HOS_PAT_ID, String.valueOf(mAddedPatientListData.getHospitalPatId()));
                        Intent intent = new Intent(this, PatientHistoryActivity.class);
                        intent.putExtra(DMSConstants.PATIENT_INFO, bundle);
                        startActivity(intent);

                        finish();
                    }
                }


                break;
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {

    }
}
