package com.rescribe.doctor.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rescribe.doctor.R;
import com.rescribe.doctor.helpers.database.AppDBHelper;
import com.rescribe.doctor.helpers.login.LoginHelper;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.login.DocDetail;
import com.rescribe.doctor.model.login.LoginModel;
import com.rescribe.doctor.model.requestmodel.login.SignUpRequestModel;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.ui.fragments.login.LoginFragment;
import com.rescribe.doctor.ui.fragments.login.SignUpFragment;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by jeetal on 18/8/17.
 */
@RuntimePermissions
public class LoginSignUpActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, LoginFragment.OnFragmentInteractionListener, SignUpFragment.OnFragmentInteractionListener, HelperResponse {
    private final String TAG = this.getClass().getName();
    @BindView(R.id.container)
    FrameLayout container;
    private Context mContext;
    Intent intent;
    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    private static final int REQUEST_PERMISSIONS = 001;
    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_signup_layout);
        ButterKnife.bind(this);
        LoginSignUpActivityPermissionsDispatcher.askToReadMessageWithCheck(LoginSignUpActivity.this);
        String key = CommonMethods.printKeyHash(LoginSignUpActivity.this);
        CommonMethods.Log(TAG, key);
        // Code for facebook and gmail login for both signup and login fragment is written in LoginSignUpActivity
        googleInitialize();
        facebookInitialize();
        init();


    }

    private void init() {
        mContext = LoginSignUpActivity.this;
        //Fragment  login is loaded in LoginSignUpActivity , Facebook and google Login click is handled in LoginSignUpActivity
        loginFragment = new LoginFragment();
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, loginFragment);
        fragmentTransaction.commit();
    }

    private void facebookInitialize() {
        FacebookSdk.sdkInitialize(this);
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(
                mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        requestUserInfo(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException error) {
                        // TODO Auto-generated method stub
                    }
                });
    }

    private void googleInitialize() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }
 //Request to facebook for user info
    private void requestUserInfo(AccessToken accessToken) {
        String FIELDS = "fields";
        String ID = "id";
        String NAME = "name";
        String PICTURE = "picture";
        String EMAIL = "email";
        String BIRTHDAY = "birthday";
        String GENDER = "gender";
        String KEY_USERNAME = "email_address";
        String KEY_PASSWORD = "password";
        String REQUEST_FIELDS = TextUtils.join(",", new String[]{ID, NAME, PICTURE, EMAIL, BIRTHDAY, GENDER});

        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {
                        // TODO Auto-generated method stub

                        JSONObject json = response.getJSONObject();
                        CommonMethods.Log("requestUserInfo", json.toString());

                        //-----------
                        SignUpRequestModel signUpRequest = new SignUpRequestModel();
                        signUpRequest.setMobileNumber(null);
                        signUpRequest.setName(json.optString("name"));
                        signUpRequest.setEmailId(json.optString("email"));
                        signUpRequest.setPassword(null);
                        signUpRequest.setFaceBookLogin(true);
                        //-----------
                        Intent intentObj = new Intent(mContext, AppGlobalContainerActivity.class);
                        intentObj.putExtra(getString(R.string.type), getString(R.string.login_with_facebook));
                        intentObj.putExtra(getString(R.string.details), signUpRequest);
                        if(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_OR_SIGNUP,mContext).equalsIgnoreCase(getString(R.string.sign_up))) {
                            intentObj.putExtra(getString(R.string.title), getString(R.string.sign_up_confirmation));
                        }else if(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_OR_SIGNUP,mContext).equalsIgnoreCase(getString(R.string.log_in))){
                            intentObj.putExtra(getString(R.string.title), getString(R.string.login_confirmation));
                        }
                        startActivity(intentObj);

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString(FIELDS, REQUEST_FIELDS);
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (requestCode != RC_SIGN_IN) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == REQUEST_PERMISSIONS) {

        }
    }
//Request to Google for user info
    private void handleSignInResult(GoogleSignInResult result) {
        CommonMethods.Log(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            CommonMethods.Log(TAG, "display name: " + acct.getDisplayName());

            //-----------
            SignUpRequestModel signUpRequest = new SignUpRequestModel();
            signUpRequest.setMobileNumber(null);
            signUpRequest.setName(acct.getDisplayName());
            signUpRequest.setEmailId(acct.getEmail());
            signUpRequest.setPassword(null);
            signUpRequest.setGmailLogin(true);
            //-----------
            Intent intentObj = new Intent(mContext, AppGlobalContainerActivity.class);
            intentObj.putExtra(getString(R.string.type), getString(R.string.login_with_gmail));
            intentObj.putExtra(getString(R.string.details), signUpRequest);
            if(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_OR_SIGNUP,mContext).equalsIgnoreCase(getString(R.string.sign_up))) {
                intentObj.putExtra(getString(R.string.title), getString(R.string.sign_up_confirmation));
            }else if(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_OR_SIGNUP,mContext).equalsIgnoreCase(getString(R.string.log_in))){
                intentObj.putExtra(getString(R.string.title), getString(R.string.login_confirmation));
            }
            startActivity(intentObj);


        } else {
            // Signed out, show unauthenticated UI.

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onClickGoogle(String loginOrSignup) {
        if (RescribePreferencesManager.getString(RescribeConstants.GMAIL_LOGIN, mContext).equalsIgnoreCase(getString(R.string.login_with_gmail))) {
            String mobileNo = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER_GMAIL, mContext);
            String password = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PASSWORD_GMAIL, mContext);
            RescribePreferencesManager.putString(RescribeConstants.TYPE_OF_LOGIN,getString(R.string.login_with_gmail),mContext);
            LoginHelper loginHelper = new LoginHelper(this, this);
            loginHelper.doLogin(mobileNo, password);

        } else {
            RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_OR_SIGNUP,loginOrSignup,mContext);
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @Override
    public void onClickFacebook(String loginOrSignup) {
        if (RescribePreferencesManager.getString(RescribeConstants.FACEBOOK_LOGIN, mContext).equalsIgnoreCase(getString(R.string.login_with_facebook))) {
            String mobileNo = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER_FACEBOOK, mContext);
            String password = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PASSWORD_FACEBOOK, mContext);
            RescribePreferencesManager.putString(RescribeConstants.TYPE_OF_LOGIN,getString(R.string.login_with_facebook),mContext);
            LoginHelper loginHelper = new LoginHelper(this, this);
            loginHelper.doLogin(mobileNo, password);
        } else {
            RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_OR_SIGNUP,loginOrSignup,mContext);
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_friends", "email", "public_profile"));
        }
    }

    @NeedsPermission(Manifest.permission.READ_SMS)
    public void askToReadMessage() {
        //Do nothing
    }

    @OnPermissionDenied({Manifest.permission.READ_SMS})
    void deniedReadSms() {
        //Do nothing
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LoginSignUpActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        // User can login through gmail or facebook
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_LOGIN)) {

            LoginModel receivedModel = (LoginModel) customResponse;
            if (receivedModel.getCommon().isSuccess()) {

                DocDetail docDetail = receivedModel.getDoctorLoginData().getDocDetail();
                String authToken = receivedModel.getDoctorLoginData().getAuthToken();

                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.AUTHTOKEN, authToken, mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.DOC_ID, String.valueOf(docDetail.getDocId()), mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.USER_NAME, docDetail.getDocName(), mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PROFILE_PHOTO, docDetail.getDocImgUrl(), mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.EMAIL, docDetail.getDocEmail(), mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.SPECIALITY, docDetail.getDocSpaciality(), mContext);
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.ADDRESS, docDetail.getDocAddress(), mContext);
                
                RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, RescribeConstants.YES, mContext);
               if (RescribePreferencesManager.getString(RescribeConstants.TYPE_OF_LOGIN, mContext).equalsIgnoreCase(getString(R.string.login_with_facebook))) {
                    RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER,RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER_FACEBOOK,mContext), mContext);
                    RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PASSWORD, RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PASSWORD_FACEBOOK,mContext), mContext);
                }
                if(RescribePreferencesManager.getString(RescribeConstants.TYPE_OF_LOGIN, mContext).equalsIgnoreCase(getString(R.string.login_with_gmail))){
                    RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER,RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.MOBILE_NUMBER_GMAIL,mContext), mContext);
                    RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PASSWORD, RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PASSWORD_GMAIL,mContext), mContext);
                }
                Intent intent = new Intent(this, HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                new AppDBHelper(mContext);
                finish();
            } else {
                CommonMethods.showToast(mContext, receivedModel.getCommon().getStatusMessage());
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        CommonMethods.Log(TAG, errorMessage);

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.Log(TAG, serverErrorMessage);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.Log(TAG, serverErrorMessage);
    }
}
