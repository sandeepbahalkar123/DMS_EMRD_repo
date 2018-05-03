package com.rescribe.doctor.ui.fragments.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.rescribe.doctor.R;
import com.rescribe.doctor.helpers.database.AppDBHelper;
import com.rescribe.doctor.helpers.login.LoginHelper;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.login.ClinicList;
import com.rescribe.doctor.model.login.DocDetail;
import com.rescribe.doctor.model.login.LoginModel;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.singleton.RescribeApplication;
import com.rescribe.doctor.ui.activities.AppGlobalContainerActivity;
import com.rescribe.doctor.ui.activities.ForgotPasswordWebViewActivity;
import com.rescribe.doctor.ui.activities.HomePageActivity;
import com.rescribe.doctor.ui.customesViews.CustomTextView;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment implements HelperResponse {

    @BindView(R.id.editTextEmailID)
    EditText editTextEmailID;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.btnOtp)
    CustomTextView btnOtp;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.forgotPasswordView)
    CustomTextView forgotPasswordView;
    @BindView(R.id.signup)
    CustomTextView signup;
    Unbinder unbinder;
    @BindView(R.id.loginUpWithFacebook)
    ImageView loginUpWithFacebook;
    @BindView(R.id.loginUpWithGmail)
    ImageView loginUpWithGmail;
    private OnFragmentInteractionListener mListener;
    private final String TAG = this.getClass().getName();

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btnOtp, R.id.btn_login, R.id.forgotPasswordView, R.id.signup, R.id.loginUpWithFacebook, R.id.loginUpWithGmail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnOtp:
                //input mobile no and click on otp button ,TASK_LOGIN_WITH_OTP is used
                // TODO  NOT CONFIRMED ABOUT THIS IMPLEMENTATION RIGHT NOW, HENCE COMMENTED.
              /*  String mobile = editTextEmailID.getText().toString();
                if (!validatePhoneNo(mobile)) {
                    LoginHelper loginHelper = new LoginHelper(getActivity(), this);
                    loginHelper.doLoginByOTP(mobile);
                }*/
                break;
            case R.id.btn_login:
                // input mobileNo and password on click of Login buttton , TASK_LOGIN is used
                String email = editTextEmailID.getText().toString();
                String password = editTextPassword.getText().toString();
                if (!validate(email, password)) {
                    LoginHelper loginHelper = new LoginHelper(getActivity(), this);
                    loginHelper.doLogin(email, password);
                }
                break;
            case R.id.forgotPasswordView:

                Intent intent = new Intent(getActivity(), ForgotPasswordWebViewActivity.class);
                startActivity(intent);

                break;
            case R.id.signup:
                //on click of signup , Signup fragment is loaded here.
                SignUpFragment signupFragment = new SignUpFragment();
                FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, signupFragment);
                fragmentTransaction.addToBackStack("SignUpFragment");
                fragmentTransaction.commit();
                break;
            case R.id.loginUpWithFacebook:
                //Onclick of Facebook button logic immplemented in LoginSignupActivity
                mListener.onClickFacebook(getString(R.string.log_in));
                break;
            case R.id.loginUpWithGmail:
                //Onclick of gmail button logic immplemented in LoginSignupActivity
                mListener.onClickGoogle(getString(R.string.log_in));
                break;
        }
    }

    //validation for mobileNo on click of otp Button
    private boolean validate(String email, String password) {
        String message = null;
        String enter = getString(R.string.enter);
        if (email.isEmpty()) {
            message = enter + getString(R.string.enter_email_id).toLowerCase(Locale.US);
            editTextEmailID.setError(message);
            editTextEmailID.requestFocus();

        } else if (!CommonMethods.isValidEmail(email)) {
            message = getString(R.string.err_email_invalid);
            editTextEmailID.setError(message);
            editTextEmailID.requestFocus();

        } else if (password.isEmpty()) {
            message = enter + getString(R.string.enter_password).toLowerCase(Locale.US);
            editTextPassword.setError(message);
            editTextPassword.requestFocus();
        } else if (password.trim().length() < 6) {
            message = getString(R.string.error_too_small_password);
            editTextPassword.setError(message);
            editTextPassword.requestFocus();
        }
        if (message != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_LOGIN)) {
            //After login user navigated to HomepageActivity
            LoginModel receivedModel = (LoginModel) customResponse;
            if (receivedModel.getCommon().isSuccess()) {

                DocDetail docDetail = receivedModel.getDoctorLoginData().getDocDetail();
                String authToken = receivedModel.getDoctorLoginData().getAuthToken();

                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.AUTHTOKEN, authToken, getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.DOC_ID, String.valueOf(docDetail.getDocId()), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.USER_NAME, docDetail.getDocName(), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PROFILE_PHOTO, docDetail.getDocImgUrl(), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.EMAIL, docDetail.getDocEmail(), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.SPECIALITY, docDetail.getDocSpaciality(), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.ADDRESS, docDetail.getDocAddress(), getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, RescribeConstants.YES, getActivity());
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PASSWORD, editTextPassword.getText().toString(), getActivity());

                String doctorDetails = new Gson().toJson(docDetail);
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.DOC_INFO, doctorDetails, getActivity());

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
        //--- TODO  NOT CONFIRMED ABOUT THIS IMPLEMENTATION RIGHT NOW, HENCE COMMENTED.

       /* else if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_LOGIN_WITH_OTP)) {
            //After login user navigated to HomepageActivity
            LoginModel loginModel = (LoginModel) customResponse;
            if (loginModel.getCommon().isSuccess()) {
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER, editTextEmailID.getText().toString(), getActivity());
                Intent intent = new Intent(getActivity(), AppGlobalContainerActivity.class);
                intent.putExtra(getString(R.string.type), getString(R.string.enter_otp_for_login));
                intent.putExtra(getString(R.string.title), getString(R.string.enter_otp_for_login));
                startActivity(intent);
            } else {
                CommonMethods.showToast(getActivity(), loginModel.getCommon().getStatusMessage());
            }
        }*/

        //-------
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        CommonMethods.showToast(getActivity(), errorMessage);
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(getActivity(), serverErrorMessage);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(getActivity(), serverErrorMessage);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onClickGoogle(String login);

        void onClickFacebook(String login);
    }
}
