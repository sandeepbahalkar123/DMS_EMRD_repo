package com.scorg.dms.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.scorg.dms.R;
import com.scorg.dms.bottom_menus.BottomMenu;
import com.scorg.dms.bottom_menus.BottomMenuActivity;
import com.scorg.dms.bottom_menus.BottomMenuAdapter;
import com.scorg.dms.model.doctor_location.DoctorLocationModel;
import com.scorg.dms.model.login.ClinicList;
import com.scorg.dms.model.login.DocDetail;
import com.scorg.dms.model.profile_photo.ProfilePhotoResponse;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.singleton.Device;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.ui.activities.dashboard.SettingsActivity;
import com.scorg.dms.ui.activities.dashboard.SupportActivity;
import com.scorg.dms.ui.customesViews.BottomSheetDialog;
import com.scorg.dms.ui.customesViews.CircularImageView;
import com.scorg.dms.ui.customesViews.CustomProgressDialog;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.Config;
import com.scorg.dms.util.DMSConstants;
import com.scorg.dms.util.Imageutils;
import com.theartofdev.edmodo.cropper.CropImage;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.scorg.dms.ui.activities.MapActivityShowDoctorLocation.ADDRESS;
import static com.scorg.dms.util.Imageutils.FILEPATH;

/**
 * Created by jeetal on 16/2/18.
 */

public class ProfileActivity extends BottomMenuActivity implements BottomMenuAdapter.OnBottomMenuClickListener, Imageutils.ImageAttachmentListener {
    private static final String TAG = "Profile";
    @BindView(R.id.backImageView)
    ImageView backImageView;
    @BindView(R.id.titleTextView)
    CustomTextView titleTextView;
    @BindView(R.id.userInfoTextView)
    CustomTextView userInfoTextView;
    @BindView(R.id.dateTextview)
    CustomTextView dateTextview;
    @BindView(R.id.year)
    Spinner year;
    @BindView(R.id.addImageView)
    ImageView addImageView;
    @BindView(R.id.premiumType)
    CustomTextView premiumType;
    @BindView(R.id.doctorFees)
    CustomTextView doctorFees;
    @BindView(R.id.ruppeeShadow)
    ImageView ruppeeShadow;
    @BindView(R.id.rupeesLayout)
    LinearLayout rupeesLayout;
    @BindView(R.id.profileImage)
    CircularImageView profileImage;
    @BindView(R.id.clinicName)
    CustomTextView clinicName;
    @BindView(R.id.doctorName)
    CustomTextView doctorName;
    @BindView(R.id.doctorSpecialization)
    CustomTextView doctorSpecialization;
    @BindView(R.id.docRating)
    CustomTextView docRating;
    @BindView(R.id.docRatingBar)
    RatingBar docRatingBar;
    @BindView(R.id.docRatingBarLayout)
    LinearLayout docRatingBarLayout;
    @BindView(R.id.doChat)
    ImageView doChat;
    @BindView(R.id.favorite)
    ImageView favorite;
    @BindView(R.id.docPracticesLocationCount)
    CustomTextView docPracticesLocationCount;
    @BindView(R.id.viewAllClinicsOnMap)
    ImageView viewAllClinicsOnMap;
    @BindView(R.id.allClinicPracticeLocationMainLayout)
    LinearLayout allClinicPracticeLocationMainLayout;
    @BindView(R.id.clinicNameSpinner)
    Spinner clinicNameSpinner;
    @BindView(R.id.clinicNameSpinnerParentLayout)
    LinearLayout clinicNameSpinnerParentLayout;
    @BindView(R.id.selectClinicLine)
    View selectClinicLine;
    @BindView(R.id.countDoctorExperience)
    CustomTextView countDoctorExperience;
    @BindView(R.id.doctorExperience)
    CustomTextView doctorExperience;
    @BindView(R.id.doctorExperienceLayout)
    LinearLayout doctorExperienceLayout;
    @BindView(R.id.yearsExperienceLine)
    View yearsExperienceLine;
    @BindView(R.id.servicesHeaderView)
    CustomTextView servicesHeaderView;
    @BindView(R.id.servicesListView)
    ListView servicesListView;
    @BindView(R.id.readMoreDocServices)
    CustomTextView readMoreDocServices;
    @BindView(R.id.servicesLayout)
    LinearLayout servicesLayout;
    @BindView(R.id.servicesLine)
    View servicesLine;
    @BindView(R.id.aboutDoctor)
    CustomTextView aboutDoctor;
    @BindView(R.id.aboutDoctorDescription)
    CustomTextView aboutDoctorDescription;
    @BindView(R.id.aboutLayout)
    LinearLayout aboutLayout;

    private Context mContext;
    private BottomSheetDialog mBottomSheetDialog;

    private ArrayList<DoctorLocationModel> mArrayListDoctorLocationModel = new ArrayList<>();
    private DoctorLocationModel doctorLocationModel;
    private ArrayList<String> mServices = new ArrayList<>();
    private Imageutils imageutils;
    private String Url = Config.BASE_URL + Config.UPLOAD_PROFILE_PHOTO;
    private String authorizationString;
    private Device device;
    private String docId;
    CustomProgressDialog mCustomProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_base_layout);
        ButterKnife.bind(this);
        setCurrentActivityTab(getString(R.string.profile));
        initialize();
    }

    @SuppressLint("CheckResult")
    private void initialize() {
        mContext = ProfileActivity.this;
        ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
        imageutils = new Imageutils(this);
        device = Device.getInstance(ProfileActivity.this);
        docId = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, mContext);
        authorizationString = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.AUTHTOKEN, ProfileActivity.this);
        mArrayListDoctorLocationModel = DMSApplication.getDoctorLocationModels();
        int size = mArrayListDoctorLocationModel.size();
        titleTextView.setText(getString(R.string.profile));
        backImageView.setVisibility(View.GONE);

        String doctorNameToDisplay;
        if (DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.USER_NAME, mContext).toLowerCase().contains("Dr."))
            doctorNameToDisplay = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.USER_NAME, mContext);
         else
            doctorNameToDisplay = "Dr. " + DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.USER_NAME, mContext);

        doctorName.setText(doctorNameToDisplay);

        String doctorDetails = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_INFO, this);
        final DocDetail docDetail = new Gson().fromJson(doctorDetails, DocDetail.class);
        if(docDetail.getDocInfo().isEmpty()){
            aboutLayout.setVisibility(View.INVISIBLE);
        }else {
            aboutLayout.setVisibility(View.VISIBLE);
            aboutDoctorDescription.setText(docDetail.getDocInfo());
        }

        countDoctorExperience.setText(docDetail.getDocExperience());
        doctorExperience.setText(docDetail.getDocExperience() + " years of experience");
        doctorSpecialization.setText(docDetail.getDocDegree());

        if (docDetail.isPremium()) {
            premiumType.setText(DMSConstants.PREMIUM);
            premiumType.setVisibility(View.VISIBLE);
        } else {
            premiumType.setVisibility(View.INVISIBLE);
        }

        if (size > 0) {
            allClinicPracticeLocationMainLayout.setVisibility(View.VISIBLE);

            String mainString = getString(R.string.practices_at_locations);
            if (size == 1) {
                mainString = mainString.substring(0, mainString.length() - 1);
            }
            String updatedString = mainString.replace("$$", "" + size);
            SpannableString contentExp = new SpannableString(updatedString);
            contentExp.setSpan(new ForegroundColorSpan(
                            ContextCompat.getColor(mContext, R.color.tagColor)),
                    13, 13 + String.valueOf(size).length(),//hightlight mSearchString
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            docPracticesLocationCount.setText(contentExp);
        } else {
            allClinicPracticeLocationMainLayout.setVisibility(View.GONE);
        }

        String mDoctorName = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.USER_NAME, mContext);
        if (mDoctorName.contains("Dr. ")) {
            mDoctorName = mDoctorName.replace("Dr. ", "");
        }
        int color2 = mColorGenerator.getColor(mDoctorName);
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(Math.round(getResources().getDimension(R.dimen.dp40))) // width in px
                .height(Math.round(getResources().getDimension(R.dimen.dp40))) // height in px
                .endConfig()
                .buildRound(("" + mDoctorName.charAt(0)).toUpperCase(), color2);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.placeholder(drawable);
        requestOptions.error(drawable);

        Glide.with(mContext)
                .load(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.PROFILE_PHOTO, mContext))
                .apply(requestOptions).thumbnail(0.5f)
                .into(profileImage);

        if (mArrayListDoctorLocationModel.size() > 0) {
            ArrayList<String> mClinicname = new ArrayList<>();
            for (int i = 0; i < mArrayListDoctorLocationModel.size(); i++) {
                mClinicname.add(mArrayListDoctorLocationModel.get(i).getClinicName() + ", " + mArrayListDoctorLocationModel.get(i).getArea() + ", " + mArrayListDoctorLocationModel.get(i).getCity());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, R.layout.clinic_spinner_layout, mClinicname);
            clinicNameSpinner.setAdapter(arrayAdapter);

            clinicNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    doctorLocationModel = mArrayListDoctorLocationModel.get(position);

                    for (ClinicList clinicList : docDetail.getClinicList()) {
                        if (clinicList.getLocationId().equals(doctorLocationModel.getLocationId())) {
                            setServicesInView(clinicList.getServices());
                            break;
                        }
                    }

                    if (doctorLocationModel.getClinicName().equals("")) {
                        clinicName.setVisibility(View.GONE);
                    } else {
                        clinicName.setVisibility(View.VISIBLE);
                        clinicName.setText("" + doctorLocationModel.getClinicName());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            if (mArrayListDoctorLocationModel.size() == 1) {
                clinicNameSpinner.setEnabled(false);
                clinicNameSpinner.setClickable(false);
                clinicNameSpinner.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
            } else {
                clinicNameSpinner.setEnabled(true);
                clinicNameSpinner.setClickable(true);
                clinicNameSpinner.setBackground(ContextCompat.getDrawable(mContext, R.drawable.spinner_bg_profile));
            }
        } else {
            clinicNameSpinnerParentLayout.setVisibility(View.GONE);
        }


    }


    private void setServicesInView(ArrayList<String> receivedDocService) {
        //---------

        mServices = receivedDocService;

        int receivedDocServiceSize = receivedDocService.size();
        if (receivedDocServiceSize > 0) {
            servicesLine.setVisibility(View.VISIBLE);
            servicesLayout.setVisibility(View.VISIBLE);
            ArrayList<String> docListToSend = new ArrayList<>();
            if (receivedDocServiceSize > 4) {
                docListToSend.addAll(receivedDocService.subList(0, 4));
                readMoreDocServices.setVisibility(View.VISIBLE);
            } else {
                docListToSend.addAll(receivedDocService);
                readMoreDocServices.setVisibility(View.GONE);
            }
            DocServicesListAdapter mServicesAdapter = new DocServicesListAdapter(mContext, docListToSend);
            servicesListView.setAdapter(mServicesAdapter);
            CommonMethods.setListViewHeightBasedOnChildren(servicesListView);
        } else {
            servicesLine.setVisibility(View.GONE);
            servicesLayout.setVisibility(View.GONE);
        }

        //---------
    }

    @Override
    public void onBottomMenuClick(BottomMenu bottomMenu) {

        if (bottomMenu.getMenuName().equalsIgnoreCase(getString(R.string.support))) {
            Intent intent = new Intent(this, SupportActivity.class);
            startActivity(intent);
            finish();
        } else if (bottomMenu.getMenuName().equalsIgnoreCase(getString(R.string.home))) {
            finish();
        } else if (bottomMenu.getMenuName().equalsIgnoreCase(getString(R.string.settings))) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            finish();
        }
        super.onBottomMenuClick(bottomMenu);
    }

    @OnClick({R.id.profileImage, R.id.backImageView, R.id.titleTextView, R.id.userInfoTextView, R.id.readMoreDocServices, R.id.viewAllClinicsOnMap})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                finish();
                break;
            case R.id.titleTextView:
                break;
            case R.id.userInfoTextView:
                break;
            case R.id.readMoreDocServices:
                showServiceDialog(mServices);
                break;

            case R.id.viewAllClinicsOnMap: // on view-all location clicked
                //-----Show all doc clinic on map, copied from BookAppointFilteredDoctorListFragment.java----
                //this list is sorted for plotting map for each clinic location, the values of clinicName and doctorAddress are set in string here, which are coming from arraylist.

                Intent intentObjectMap = new Intent(this, MapActivityShowDoctorLocation.class);

                ArrayList<String> locations = new ArrayList<>();
                for (DoctorLocationModel doctorLocationM : mArrayListDoctorLocationModel)
                    locations.add(doctorLocationM.getArea() + ", " + doctorLocationM.getCity());

                intentObjectMap.putExtra(ADDRESS, locations);
                startActivity(intentObjectMap);
                break;
            case R.id.profileImage:
                //onclick of profile image imagepicker dialog called.
                imageutils.imagepicker(1);
                break;
        }
    }

    public void showServiceDialog(ArrayList<String> mServiceslist) {
        mBottomSheetDialog = new BottomSheetDialog(mContext, R.style.Material_App_BottomSheetDialog);
        View v = getLayoutInflater().inflate(R.layout.services_dialog_modal, null);
        ///  CommonMethods.setBackground(v, new ThemeDrawable(R.array.bg_window));
        mBottomSheetDialog.setTitle("Services");
        mBottomSheetDialog.heightParam(ViewGroup.LayoutParams.MATCH_PARENT);
        ListView mServicesListView = (ListView) v.findViewById(R.id.servicesListView);
        DialogServicesListAdapter mServicesAdapter = new DialogServicesListAdapter(mContext, mServiceslist);
        mServicesListView.setAdapter(mServicesAdapter);
        AppCompatImageView closeButton = (AppCompatImageView) v.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog.contentView(v)
                .show();

    }

    class DialogServicesListAdapter extends BaseAdapter {
        Context mContext;
        private ArrayList<String> mDocServiceList;

        DialogServicesListAdapter(Context context, ArrayList<String> items) {
            this.mContext = context;
            this.mDocServiceList = items;
        }

        @Override
        public int getCount() {
            return mDocServiceList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = convertView;
            String data = mDocServiceList.get(position);

            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                view = layoutInflater.inflate(R.layout.services_item_textview, null);
            }

            CustomTextView dataView = (CustomTextView) view.findViewById(R.id.text);
            dataView.setText(data);

            Drawable leftDrawable = AppCompatResources.getDrawable(mContext, R.drawable.bullet_dot);
            dataView.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);

            return view;
        }
    }


    public class DocServicesListAdapter extends BaseAdapter {
        Context mContext;
        private ArrayList<String> mDocServiceList;

        DocServicesListAdapter(Context context, ArrayList<String> items) {
            this.mContext = context;
            mDocServiceList = items;
        }

        @Override
        public int getCount() {
            return mDocServiceList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            View view = convertView;


            if (convertView == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(mContext);
                view = layoutInflater.inflate(R.layout.services_item_textview, null);
            }
            CustomTextView dataView = (CustomTextView) view.findViewById(R.id.text);
            dataView.setText("" + mDocServiceList.get(position));
            return view;
        }
    }

    @Override
    public void image_attachment(int from, Bitmap file, Uri uri) {
        //file path is given below to generate new image as required i.e jpg format
        String path = Environment.getExternalStorageDirectory() + File.separator + "DrRescribe" + File.separator + "ProfilePhoto" + File.separator;
        imageutils.createImage(file, path, false);
        mCustomProgressDialog = new CustomProgressDialog(this);
        uploadProfileImage(FILEPATH);


    }

    public void uploadProfileImage(final String filePath) {
        try {
            mCustomProgressDialog.show();
            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(ProfileActivity.this, System.currentTimeMillis() + docId, Url)
                    .setUtf8Charset()
                    .setMaxRetries(DMSConstants.MAX_RETRIES)
                    .addHeader(DMSConstants.AUTHORIZATION_TOKEN, authorizationString)
                    .addHeader(DMSConstants.DEVICEID, device.getDeviceId())
                    .addHeader(DMSConstants.OS, device.getOS())
                    .addHeader(DMSConstants.OSVERSION, device.getOSVersion())
                    .addHeader(DMSConstants.DEVICE_TYPE, device.getDeviceType())
                    .addHeader("docid", String.valueOf(docId))
                    .addFileToUpload(filePath, "docImage");

            uploadRequest.setNotificationConfig(new UploadNotificationConfig());

            uploadRequest.setDelegate(new UploadStatusDelegate() {
                @Override
                public void onProgress(Context context, UploadInfo uploadInfo) {
                    // your code here
                }

                @Override
                public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse,
                                    Exception exception) {
                    // your code here
                    mCustomProgressDialog.dismiss();
                    Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }

                @SuppressLint("CheckResult")
                @Override
                public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                    //On Profile Image Upload on Server is completed that event is captured in this function.

                    String bodyAsString = serverResponse.getBodyAsString();
                    CommonMethods.Log(TAG, bodyAsString);

                    ProfilePhotoResponse profilePhotoResponse = new Gson().fromJson(bodyAsString, ProfilePhotoResponse.class);
                    if (DMSConstants.RESPONSE_OK.equalsIgnoreCase(profilePhotoResponse.getCommon().getSuccess())) {
                        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.PROFILE_PHOTO, profilePhotoResponse.getData().getDocImgUrl(), mContext);
                        Toast.makeText(context, profilePhotoResponse.getCommon().getStatusMessage(), Toast.LENGTH_SHORT).show();
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.dontAnimate();
                        requestOptions.skipMemoryCache(true);
                        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);

                        Glide.with(mContext)
                                .load(filePath)
                                .apply(requestOptions).thumbnail(0.5f)
                                .into(profileImage);
                        mCustomProgressDialog.dismiss();
                    } else {
                        mCustomProgressDialog.dismiss();
                        Toast.makeText(context, profilePhotoResponse.getCommon().getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(Context context, UploadInfo uploadInfo) {
                    // your code here
                    mCustomProgressDialog.dismiss();
                }
            });

            uploadRequest.startUpload();

        } catch (FileNotFoundException | MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //get image URI and set to create image of jpg format.
                Uri resultUri = result.getUri();
//                String path = Environment.getExternalStorageDirectory() + File.separator + "DrRescribe" + File.separator + "ProfilePhoto" + File.separator;
                imageutils.callImageCropMethod(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
            }
        } else {
            imageutils.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }
}
