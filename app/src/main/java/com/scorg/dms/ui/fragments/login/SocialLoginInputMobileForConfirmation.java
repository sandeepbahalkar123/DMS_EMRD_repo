package com.scorg.dms.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.scorg.dms.R;
import com.scorg.dms.helpers.login.LoginHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.login.SignUpModel;
import com.scorg.dms.model.requestmodel.login.SignUpRequestModel;
import com.scorg.dms.ui.activities.AppGlobalContainerActivity;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SocialLoginInputMobileForConfirmation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SocialLoginInputMobileForConfirmation extends Fragment implements HelperResponse {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.socialLoginMobileNo)
    EditText mSocialLoginMobileNo;
    @BindView(R.id.socialLoginPassword)
    EditText mSocialLoginPassword;
    @BindView(R.id.socialLoginEmail)
    EditText mSocialLoginEmail;
    @BindView(R.id.emailLayout)
    LinearLayout mEmailLayout;
    @BindView(R.id.submitBtn)
    Button mSubmitBtn;
    private SignUpRequestModel mSignUpRequestModel;

    public SocialLoginInputMobileForConfirmation() {
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
    public static SocialLoginInputMobileForConfirmation newInstance(String param1, String param2) {
        SocialLoginInputMobileForConfirmation fragment = new SocialLoginInputMobileForConfirmation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //For Both gmail and login signup this fragment is loaded , By setting isGmailLogin or is FacebookLogin true functinality of social media login  works
        View inflate = inflater.inflate(R.layout.social_login_confirm_mobileno_password, container, false);
        ButterKnife.bind(this, inflate);
        if (getArguments() != null) {
            Bundle arguments = getArguments();
            mSignUpRequestModel = (SignUpRequestModel) arguments.getSerializable(getString(R.string.details));
            mEmailLayout.setVisibility(View.GONE);
            if (mSignUpRequestModel.getEmailId() == null || DMSConstants.BLANK.equalsIgnoreCase(mSignUpRequestModel.getEmailId())) {
                mEmailLayout.setVisibility(View.VISIBLE);
            } else {
                mSocialLoginEmail.setText("" + mSignUpRequestModel.getEmailId());
            }
        }
        return inflate;
    }

    /**
     * Return true if fields empty/validation failed, else false.
     *
     * @return
     */
    private boolean validate(String mobileNo, String password, String email) {
        String message = null;
        String enter = getString(R.string.enter);
        if (mobileNo.isEmpty()) {
            message = enter + getString(R.string.enter_mobile_no).toLowerCase(Locale.US);
        } else if ((mobileNo.trim().length() < 10) || !(mobileNo.trim().startsWith("7") || mobileNo.trim().startsWith("8") || mobileNo.trim().startsWith("9"))) {
            message = getString(R.string.err_invalid_mobile_no);
        } else if (password.isEmpty()) {
            message = enter + getString(R.string.enter_password).toLowerCase(Locale.US);
        } else if (password.trim().length() < 8) {
            message = getString(R.string.error_too_small_password);
        } else if (mEmailLayout.getVisibility() == View.VISIBLE) {
            if (email.isEmpty()) {
                message = enter + getString(R.string.enter_email_id).toLowerCase(Locale.US);
            } else if (!CommonMethods.isValidEmail(email)) {
                message = enter + getString(R.string.err_email_invalid);
            }
        }
        if (message != null) {
            CommonMethods.showToast(getActivity(), message);
            return true;
        } else {
            return false;
        }
    }

    @OnClick({R.id.submitBtn})
    public void onButtonClicked(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.submitBtn:
                String email = mSocialLoginEmail.getText().toString();
                String password = mSocialLoginPassword.getText().toString();
                String mobileNo = mSocialLoginMobileNo.getText().toString();
                if (!validate(mobileNo, password, email)) {
                    LoginHelper loginHelper = new LoginHelper(getActivity(), this);
                    mSignUpRequestModel.setMobileNumber(mobileNo);
                    mSignUpRequestModel.setEmailId(email);
                    mSignUpRequestModel.setPassword(password);
                    if(mSignUpRequestModel.isGmailLogin()){
                        mSignUpRequestModel.setGmailLogin(true);
                    }else if(mSignUpRequestModel.isFaceBookLogin()){
                        mSignUpRequestModel.setFaceBookLogin(true);
                    }
                    loginHelper.doSignUp(mSignUpRequestModel);
                }
                break;
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(DMSConstants.TASK_SIGN_UP)) {

            SignUpModel loginModel = (SignUpModel) customResponse;

            if (loginModel.getCommon().isSuccess()) {
                     AppGlobalContainerActivity activity = (AppGlobalContainerActivity) getActivity();
                     activity.loadFragment(getString(R.string.enter_otp), mSignUpRequestModel, getString(R.string.sign_up_confirmation));

            } else {
                CommonMethods.showToast(getActivity(), loginModel.getCommon().getStatusMessage());
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
