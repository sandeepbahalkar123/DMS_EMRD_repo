package com.scorg.dms.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.broadcast_receivers.OtpReader;
import com.scorg.dms.helpers.database.AppDBHelper;
import com.scorg.dms.helpers.login.LoginHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.interfaces.OTPListener;
import com.scorg.dms.model.login.DocDetail;
import com.scorg.dms.model.login.LoginModel;
import com.scorg.dms.model.login.SignUpModel;
import com.scorg.dms.model.requestmodel.login.SignUpRequestModel;
import com.scorg.dms.model.requestmodel.login.SignUpVerifyOTPRequestModel;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.ui.activities.HomePageActivity;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link OTPConfirmationForSignUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OTPConfirmationForSignUp extends Fragment implements HelperResponse, OTPListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private CountDownTimer mCountDownTimer;
    private final long mStartTime = 30 * 1000;
    private final long mInterval = 1 * 1000;

    @BindView(R.id.otpEditText)
    EditText mOtpEditText;

    @BindView(R.id.submitBtn)
    Button mSubmitBtn;

    @BindView(R.id.resendOtpBtn)
    TextView mResendOtpBtn;
    @BindView(R.id.progressTime)
    TextView mProgressTime;
    @BindView(R.id.headerMessageForMobileOTP)
    TextView mHeaderMessageForMobileOTP;
    @BindView(R.id.resendOtpBtnLayout)
    LinearLayout mResendOtpBtnLayout;

    private SignUpRequestModel mSignUpRequestModel;

    private String mMobileNo;
    private int mResendOTPCount = 0;
//
//    @BindView(R.id.progressBar)
//    LinearLayout mProgressBar;

    public OTPConfirmationForSignUp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUp.
     */
    // TODO: Rename and change types and number of parameters
    public static OTPConfirmationForSignUp newInstance(String param1, String param2) {
        OTPConfirmationForSignUp fragment = new OTPConfirmationForSignUp();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.enter_generated_otp, container, false);
        ButterKnife.bind(this, inflate);

        OtpReader.bind(this, DMSConstants.SENDERID);
        mCountDownTimer = new MyCountDownTimer(mStartTime, mInterval);
        mCountDownTimer.start();

        if (getArguments() != null) {
            Bundle arguments = getArguments();
            mSignUpRequestModel = (SignUpRequestModel) arguments.getSerializable(getString(R.string.details));
            mHeaderMessageForMobileOTP.setText("" + String.format(getString(R.string.message_for_mobile_otp), mSignUpRequestModel.getMobileNumber()));
        }

        return inflate;
    }

    @Override
    public void otpReceived(String smsText) {
        // Automate sms read and navigate to HomepageActivty
        //Do whatever you want to do with the text
        CommonMethods.Log("otpReceived", "otpReceived:" + smsText);
        int value = Integer.parseInt(smsText.replaceAll("[^0-9]", ""));
        CommonMethods.Log("otpReceived", "otpReceived reformatted:" + value);
        mCountDownTimer.onFinish();
        mOtpEditText.setText(String.valueOf(value).substring(0, 4));
        mSubmitBtn.setVisibility(View.VISIBLE);
        onSubmitBtnClicked();
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            mResendOtpBtnLayout.setVisibility(View.GONE);
            mResendOtpBtn.setVisibility(View.VISIBLE);
            mOtpEditText.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTick(long millisUntilFinished) {
//			mProgressText.setText(" "+millisUntilFinished / 1000 + " secs" );
            if (getActivity() != null) {
                if (!getActivity().isFinishing()) {
                    String format = "" + (millisUntilFinished / 1000);
                    mProgressTime.setText(format);
                }
            }
        }
    }

    @OnClick(R.id.submitBtn)
    public void onSubmitBtnClicked() {
        if (mOtpEditText.getText().toString().trim().length() == 4) {
            SignUpVerifyOTPRequestModel model = new SignUpVerifyOTPRequestModel();
            model.setMobileNumber("" + mSignUpRequestModel.getMobileNumber());
            model.setOTP(mOtpEditText.getText().toString().trim());
            model.setPassword(mSignUpRequestModel.getPassword());
            model.setName(mSignUpRequestModel.getName());
            model.seteMailID(mSignUpRequestModel.getEmailId());

            LoginHelper loginHelper = new LoginHelper(getActivity(), this);
            loginHelper.doVerifyGeneratedSignUpOTP(model);
        } else {
            CommonMethods.showToast(getActivity(), getString(R.string.err_otp_invalid));
        }
    }

    @OnClick(R.id.resendOtpBtn)
    // Resend otp function
    public void resendOTP() {
        if (mResendOTPCount == 3) {
            CommonMethods.showToast(getActivity(), getString(R.string.err_maximum_otp_retries));
        } else {
            LoginHelper loginHelper = new LoginHelper(getActivity(), this);
            loginHelper.doSignUp(mSignUpRequestModel);
        }

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(DMSConstants.TASK_SIGN_UP)) {
            SignUpModel loginModel = (SignUpModel) customResponse;
            if (DMSConstants.RESPONSE_OK.equalsIgnoreCase(loginModel.getCommon().getSuccess())) {
                mResendOTPCount = mResendOTPCount + 1;
                mCountDownTimer = new MyCountDownTimer(mStartTime, mInterval);
                mCountDownTimer.start();
                mResendOtpBtnLayout.setVisibility(View.VISIBLE);
                mSubmitBtn.setVisibility(View.VISIBLE);
                mResendOtpBtn.setVisibility(View.GONE);
                mOtpEditText.setText("");
            } else {
                CommonMethods.showToast(getActivity(), loginModel.getCommon().getStatusMessage());
            }
        } else if (mOldDataTag.equalsIgnoreCase(DMSConstants.TASK_VERIFY_SIGN_UP_OTP)) {

            LoginModel receivedModel = (LoginModel) customResponse;
            if (DMSConstants.RESPONSE_OK.equalsIgnoreCase(receivedModel.getCommon().getSuccess())) {
                if(mSignUpRequestModel.isGmailLogin()){
                    DMSPreferencesManager.putString(DMSConstants.GMAIL_LOGIN,getString(R.string.login_with_gmail),getActivity());
                    DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.MOBILE_NUMBER_GMAIL,mSignUpRequestModel.getMobileNumber(),getActivity());
                    DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.PASSWORD_GMAIL,mSignUpRequestModel.getPassword(),getActivity());
                }
                if(mSignUpRequestModel.isFaceBookLogin()){
                    DMSPreferencesManager.putString(DMSConstants.FACEBOOK_LOGIN,getString(R.string.login_with_facebook),getActivity());
                    DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.MOBILE_NUMBER_FACEBOOK,mSignUpRequestModel.getMobileNumber(),getActivity());
                    DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.PASSWORD_FACEBOOK,mSignUpRequestModel.getPassword(),getActivity());
                }

                DocDetail docDetail = receivedModel.getDoctorLoginData().getDocDetail();
                String authToken = receivedModel.getDoctorLoginData().getAuthToken();

                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.AUTHTOKEN, authToken, getActivity());
                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, String.valueOf(docDetail.getDocId()), getActivity());
                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.USER_NAME, docDetail.getDocName(), getActivity());
                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.PROFILE_PHOTO, docDetail.getDocImgUrl(), getActivity());
                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.EMAIL, docDetail.getDocEmail(), getActivity());
                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SPECIALITY, docDetail.getDocSpaciality(), getActivity());
                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.ADDRESS, docDetail.getDocAddress(), getActivity());

                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.LOGIN_STATUS, DMSConstants.YES, getActivity());

                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.MOBILE_NUMBER, mSignUpRequestModel.getMobileNumber().toString(), getActivity());
                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.PASSWORD, mSignUpRequestModel.getPassword().toString(), getActivity());
                Intent intent = new Intent(getActivity(), HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                new AppDBHelper(getContext());
                getActivity().finish();
            } else {
                CommonMethods.showToast(getActivity(), receivedModel.getCommon().getStatusMessage());
            }
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
