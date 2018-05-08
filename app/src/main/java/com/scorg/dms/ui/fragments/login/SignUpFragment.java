package com.scorg.dms.ui.fragments.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.scorg.dms.R;
import com.scorg.dms.helpers.login.LoginHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.login.SignUpModel;
import com.scorg.dms.model.requestmodel.login.SignUpRequestModel;
import com.scorg.dms.ui.activities.AppGlobalContainerActivity;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

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
public class SignUpFragment extends Fragment implements HelperResponse{

    @BindView(R.id.editTextName)
    EditText editTextName;
    @BindView(R.id.editTextEmailID)
    EditText editTextEmailID;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.editTextMobileNo)
    EditText editTextMobileNo;
    @BindView(R.id.btnSignUp)
    Button btnSignUp;
    @BindView(R.id.login)
    CustomTextView login;
    @BindView(R.id.signUpWithFacebook)
    ImageView signUpWithFacebook;
    @BindView(R.id.signUpWithGoogle)
    ImageView signUpWithGoogle;
    Unbinder unbinder;
    private SignUpRequestModel mSignUpRequestModel;
    private OnFragmentInteractionListener mListener;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);

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

    @OnClick({R.id.btnSignUp, R.id.login, R.id.signUpWithFacebook, R.id.signUpWithGoogle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //Onclick of Signup button
            case R.id.btnSignUp:
                String name = editTextName.getText().toString();
                String email = editTextEmailID.getText().toString();
                String password = editTextPassword.getText().toString();
                String mobileNo = editTextMobileNo.getText().toString();
                if (!validate(name, email, password, mobileNo)) {
                    LoginHelper loginHelper = new LoginHelper(getActivity(), this);
                    mSignUpRequestModel = new SignUpRequestModel();
                    mSignUpRequestModel.setMobileNumber(mobileNo);
                    mSignUpRequestModel.setName(name);
                    mSignUpRequestModel.setEmailId(email);
                    mSignUpRequestModel.setPassword(password);
                    loginHelper.doSignUp(mSignUpRequestModel);
                }
                break;
            //Onclick of Login text
            case R.id.login:
                FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
                supportFragmentManager.popBackStack();
                break;
            //Onclick of Facebook button logic immplemented in LoginSignupActivity
            case R.id.signUpWithFacebook:
                mListener.onClickFacebook(getString(R.string.sign_up));
                break;
            //Onclick of gmail button logic immplemented in LoginSignupActivity
            case R.id.signUpWithGoogle:
                mListener.onClickGoogle(getString(R.string.sign_up));
                break;
        }
    }
    private boolean validate(String name, String email, String password, String mobileNo) {
        String message = null;
        String enter = getString(R.string.enter);
        if (name.isEmpty()) {
            message = enter + getString(R.string.enter_full_name).toLowerCase(Locale.US);
            editTextName.setError(message);
            editTextName.requestFocus();
        } else if (email.isEmpty()) {
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

        } else if (password.trim().length() < 8) {
            message = getString(R.string.error_too_small_password);
            editTextPassword.setError(message);
            editTextPassword.requestFocus();

        } else if (mobileNo.isEmpty()) {
            message = enter + getString(R.string.enter_mobile_no).toLowerCase(Locale.US);
            editTextMobileNo.setError(message);
            editTextMobileNo.requestFocus();

        } else if ((mobileNo.trim().length() < 10) || !(mobileNo.trim().startsWith("7") || mobileNo.trim().startsWith("8") || mobileNo.trim().startsWith("9"))) {

            message = getString(R.string.err_invalid_mobile_no);
            editTextMobileNo.setError(message);
            editTextMobileNo.requestFocus();

        }
        if (message != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(DMSConstants.TASK_SIGN_UP)) {

            SignUpModel loginModel = (SignUpModel) customResponse;

            if (loginModel.getCommon().isSuccess()) {
                if (loginModel.getCommon().getStatusMessage().equalsIgnoreCase(getString(R.string.profile_exists))) {
                    CommonMethods.showToast(getActivity(), loginModel.getCommon().getStatusMessage());
                } else {
                    Intent intentObj = new Intent(getActivity(), AppGlobalContainerActivity.class);
                    intentObj.putExtra(getString(R.string.type), getString(R.string.enter_otp));
                    intentObj.putExtra(getString(R.string.title), getString(R.string.sign_up_confirmation));
                    intentObj.putExtra(getString(R.string.details), mSignUpRequestModel);
                    startActivity(intentObj);
                }
            } else {
                CommonMethods.showToast(getActivity(), loginModel.getCommon().getStatusMessage());
            }
        }
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
        void onClickGoogle(String Signup);

        void onClickFacebook(String Signup);
    }
}
