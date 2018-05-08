package com.scorg.dms.ui.activities.add_records;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scorg.dms.R;
import com.scorg.dms.helpers.database.AppDBHelper;
import com.scorg.dms.model.investigation.Image;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.singleton.Device;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.Config;
import com.scorg.dms.util.DMSConstants;
import com.scorg.dms.util.KeyboardEvent;
import com.scorg.dms.util.NetworkUtil;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.scorg.dms.services.SyncOfflineRecords.getUploadConfig;

@RuntimePermissions
public class SelectedRecordsActivity extends AppCompatActivity {

    @BindView(R.id.gridLayout)
    GridLayout gridLayout;
    @BindView(R.id.uploadButton)
    Button uploadButton;
    @BindView(R.id.coachmark)
    ImageView coachmark;
    @BindView(R.id.backImageView)
    ImageView backImageView;
    @BindView(R.id.titleTextView)
    CustomTextView titleTextView;
    @BindView(R.id.userInfoTextView)
    CustomTextView userInfoTextView;
    @BindView(R.id.dateTextview)
    CustomTextView dateTextview;
    @BindView(R.id.addImageView)
    ImageView addImageView;
    @BindView(R.id.addImageViewRightFab)
    FloatingActionButton mAddImageViewRightFab;
    @BindView(R.id.mainRelativeLayout)
    RelativeLayout mainRelativeLayout;

    private static final int MAX_ATTACHMENT_COUNT = 10;

    private Context mContext;
    private ArrayList<Image> imagePaths = new ArrayList<>();
    private Dialog dialog;
    private String visitDate;
    private int docId;
    private String patientId;
    private String opdId;
    private AppDBHelper appDBHelper;
    private Device device;
    private String authorizationString;
    private String month;
    private String year;
    private String patientName = "";
    private String patientInfo = "";
    private String Url;
    private String mHospitalPatId;
    private String mLocationId;
    private int mHospitalId;
    private String mOpdtime;
    private String currentOpdTime;
    private boolean openCameraDirect;
    private int imageSize;
    private int dimension20PixelSize;
    private int mAptId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleted_records);
        ButterKnife.bind(this);

        init();

        new KeyboardEvent(mainRelativeLayout, new KeyboardEvent.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                uploadButton.setVisibility(View.GONE);
                mAddImageViewRightFab.setVisibility(View.GONE);
            }

            @Override
            public void onKeyboardClose() {
                uploadButton.setVisibility(View.VISIBLE);
                mAddImageViewRightFab.setVisibility(View.VISIBLE);
            }
        });

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        dimension20PixelSize = getResources().getDimensionPixelSize(R.dimen.dp20);
        imageSize = (widthPixels / 2) - getResources().getDimensionPixelSize(R.dimen.dp10);

        // Show two options for user

        dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_file_dialog);
        dialog.setCanceledOnTouchOutside(false);

        dialog.findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openCameraDirect = true;
                SelectedRecordsActivityPermissionsDispatcher.onPickPhotoWithCheck(SelectedRecordsActivity.this);
            }
        });

        dialog.findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openCameraDirect = false;
                SelectedRecordsActivityPermissionsDispatcher.onPickPhotoWithCheck(SelectedRecordsActivity.this);
            }
        });

        dialog.findViewById(R.id.files).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SelectedRecordsActivityPermissionsDispatcher.onPickDocWithCheck(SelectedRecordsActivity.this);
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (imagePaths.isEmpty())
                    onBackPressed();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @SuppressLint("CheckResult")
    private void addImage() {
        final View child = getLayoutInflater().inflate(R.layout.selected_records_item_layout, gridLayout, false);
        EditText addCaptionText = child.findViewById(R.id.addCaptionText);
        ImageView crossImageView = child.findViewById(R.id.crossImageView);
        ImageView iv_photo = child.findViewById(R.id.iv_photo);
        child.setLayoutParams(new LinearLayout.LayoutParams(imageSize, imageSize));
        gridLayout.addView(child);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.override(imageSize - dimension20PixelSize, imageSize - dimension20PixelSize);
        requestOptions.error(R.drawable.ic_file);
        requestOptions.placeholder(R.drawable.ic_file);

        Glide.with(mContext)
                .load(new File(imagePaths.get(gridLayout.indexOfChild(child)).getImagePath()))
                .apply(requestOptions).thumbnail(0.5f)
                .into(iv_photo);

        addCaptionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                imagePaths.get(gridLayout.indexOfChild(child)).setParentCaption(s.toString());
            }
        });

        crossImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePaths.remove(gridLayout.indexOfChild(child));
                gridLayout.removeViewAt(gridLayout.indexOfChild(child));
                gridLayout.invalidate();
            }
        });
    }

    private void init() {
        mContext = SelectedRecordsActivity.this;
        mHospitalPatId = getIntent().getStringExtra(DMSConstants.PATIENT_HOS_PAT_ID);
        mLocationId = getIntent().getStringExtra(DMSConstants.LOCATION_ID);
        patientId = getIntent().getStringExtra(DMSConstants.PATIENT_ID);
        mAptId = getIntent().getIntExtra(DMSConstants.APPOINTMENT_ID, 0);
        mHospitalId = getIntent().getIntExtra(DMSConstants.CLINIC_ID, 0);
        addImageView.setVisibility(View.GONE);
        mAddImageViewRightFab.setVisibility(View.VISIBLE);

        patientName = getIntent().getStringExtra(DMSConstants.PATIENT_NAME);
        patientInfo = getIntent().getStringExtra(DMSConstants.PATIENT_INFO);
        mOpdtime = getIntent().getStringExtra(DMSConstants.OPD_TIME);
        opdId = getIntent().getStringExtra(DMSConstants.OPD_ID);

        visitDate = getIntent().getStringExtra(DMSConstants.VISIT_DATE);

        docId = Integer.parseInt(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, mContext));

        appDBHelper = new AppDBHelper(SelectedRecordsActivity.this);
        device = Device.getInstance(SelectedRecordsActivity.this);

        Url = Config.BASE_URL + Config.MY_RECORDS_UPLOAD;
//        Url = "http://192.168.0.115:8000/" + Config.MY_RECORDS_UPLOAD;

        authorizationString = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.AUTHTOKEN, SelectedRecordsActivity.this);

        UploadService.UPLOAD_POOL_SIZE = 10;
        setDateInToolbar();
        userInfoTextView.setVisibility(View.VISIBLE);
        titleTextView.setText(patientName);
        userInfoTextView.setText(patientInfo);
    }

    private void setDateInToolbar() {
        //Set Date in Required Format i.e 13thJuly'18
        dateTextview.setVisibility(View.VISIBLE);
        String timeToShow = CommonMethods.formatDateTime(visitDate, DMSConstants.DATE_PATTERN.MMM_YY,
                DMSConstants.DATE_PATTERN.DD_MM_YYYY, DMSConstants.DATE).toLowerCase();
        String[] timeToShowSpilt = timeToShow.split(",");
        month = timeToShowSpilt[0].substring(0, 1).toUpperCase() + timeToShowSpilt[0].substring(1);
        year = timeToShowSpilt.length == 2 ? timeToShowSpilt[1] : "";
        Date date = CommonMethods.convertStringToDate(visitDate, DMSConstants.DATE_PATTERN.DD_MM_YYYY);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        timeToShow = timeToShow.substring(0, 1).toUpperCase() + timeToShow.substring(1);
        String toDisplay = cal.get(Calendar.DAY_OF_MONTH) + "<sup>" + CommonMethods.getSuffixForNumber(cal.get(Calendar.DAY_OF_MONTH)) + "</sup> " + month + "'" + year;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dateTextview.setText(Html.fromHtml(toDisplay, Html.FROM_HTML_MODE_LEGACY));
        } else {
            dateTextview.setText(Html.fromHtml(toDisplay));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_docs_menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_docs:
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onPickPhoto() {
        if (imagePaths.size() == MAX_ATTACHMENT_COUNT)
            Toast.makeText(this, "Cannot select more than " + MAX_ATTACHMENT_COUNT + " documents", Toast.LENGTH_SHORT).show();
        else {

            ArrayList photos = new ArrayList();
            for (Image photo : imagePaths) {
                if (photo.getType() == FilePickerConst.REQUEST_CODE_PHOTO)
                    photos.add(photo.getImagePath());
            }

            FilePickerBuilder.getInstance().setMaxCount(MAX_ATTACHMENT_COUNT)
                    .setSelectedFiles(photos)
                    .setActivityTheme(R.style.AppTheme)
                    .enableVideoPicker(false)
                    .enableCameraSupport(openCameraDirect)
                    .enableCameraMultiplePhotos(openCameraDirect)
                    .openCameraDirect(openCameraDirect)
                    .showGifs(false)
                    .showFolderView(true)
                    .enableOrientation(true)
                    .pickPhoto(this);
        }
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void onPickDoc() {
//        String[] documents = {".doc", ".docx", ".odt", ".pdf", ".xls", ".xlsx", ".ods", ".ppt", ".pptx"};
        String[] documents = {".pdf", ".png", ".jpeg", ".jpg"};
        if (imagePaths.size() == MAX_ATTACHMENT_COUNT)
            Toast.makeText(this, "Cannot select more than " + MAX_ATTACHMENT_COUNT + " documents", Toast.LENGTH_SHORT).show();
        else {
            ArrayList photos = new ArrayList();
            for (Image photo : imagePaths) {
                if (photo.getType() == FilePickerConst.REQUEST_CODE_DOC)
                    photos.add(photo.getImagePath());
            }

            FilePickerBuilder.getInstance().setMaxCount(MAX_ATTACHMENT_COUNT)
                    .setSelectedFiles(photos)
                    .setActivityTheme(R.style.AppTheme)
                    .addFileSupport(documents)
                    .enableDocSupport(false)
                    .enableOrientation(true)
                    .pickFile(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SelectedRecordsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
                if (data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA).size() == 0) {
                    if (imagePaths.isEmpty())
                        finish();
                } else
                    addFiles(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA), FilePickerConst.REQUEST_CODE_PHOTO);
            } else if (requestCode == FilePickerConst.REQUEST_CODE_DOC) {
                if (data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS).size() == 0) {
                    if (imagePaths.isEmpty())
                        finish();
                } else
                    addFiles(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS), FilePickerConst.REQUEST_CODE_DOC);
            }

        } else if (imagePaths.isEmpty())
            finish();
    }

    private void addFiles(ArrayList<String> data, int type) {
        for (String imagePath : data) {
            boolean isExist = false;
            for (Image imagePre : imagePaths) {
                if (imagePre.getImagePath().equals(imagePath))
                    isExist = true;
            }

            if (!isExist) {
                Image image = new Image();
                image.setImageId(patientId + "_" + UUID.randomUUID().toString());
                image.setImagePath(imagePath);
                image.setType(type);
                imagePaths.add(image);
                addImage();
            }
        }
    }

    @OnClick({R.id.coachmark, R.id.uploadButton, R.id.addImageViewRightFab, R.id.backImageView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.coachmark:
              /*  coachmark.setVisibility(View.GONE);
                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.COACHMARK, DMSConstants.YES, mContext);*/
                break;
            case R.id.uploadButton:
                if (imagePaths.isEmpty()) {
                    CommonMethods.showToast(SelectedRecordsActivity.this, getResources().getString(R.string.select_report));
                } else {

                    for (int parentIndex = 0; parentIndex < imagePaths.size(); parentIndex++) {
                        String uploadId = System.currentTimeMillis() + "_" + parentIndex + "_" + patientId;
                        if (NetworkUtil.isInternetAvailable(SelectedRecordsActivity.this))
                            uploadImage(uploadId, imagePaths.get(parentIndex));
                        else
                            CommonMethods.showToast(this, getString(R.string.records_will_upload_when_internet_available));

                        appDBHelper.insertRecordUploads(uploadId, patientId, docId, visitDate, mOpdtime, opdId, String.valueOf(mHospitalId), mHospitalPatId, mLocationId, imagePaths.get(parentIndex).getParentCaption(), imagePaths.get(parentIndex).getImagePath(), mAptId);
                    }
//                    CommonMethods.showToast(this, getString(R.string.uploading));

//                    Intent i = new Intent(this, MQTTService.class);
//                    startService(i);
                    onBackPressed();
                }
                break;
            case R.id.addImageViewRightFab:
                dialog.show();
                break;
            case R.id.backImageView:
                onBackPressed();
                break;
        }
    }

    public void uploadImage(String uploadId, Image image) {
        try {

            UploadNotificationConfig uploadConfig = getUploadConfig(this);

            if (mOpdtime.equals("")) {
                currentOpdTime = CommonMethods.getCurrentTimeStamp(DMSConstants.DATE_PATTERN.HH_mm_ss);
            } else {
                currentOpdTime = mOpdtime;
            }
            String visitDateToPass = CommonMethods.getFormattedDate(visitDate, DMSConstants.DATE_PATTERN.DD_MM_YYYY, DMSConstants.DATE_PATTERN.YYYY_MM_DD);

            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(SelectedRecordsActivity.this, uploadId, Url)
                    .setUtf8Charset()
                    .setMaxRetries(DMSConstants.MAX_RETRIES)
                    .addHeader(DMSConstants.AUTHORIZATION_TOKEN, authorizationString)
                    .addHeader(DMSConstants.DEVICEID, device.getDeviceId())
                    .addHeader(DMSConstants.OS, device.getOS())
                    .addHeader(DMSConstants.OSVERSION, device.getOSVersion())
                    .addHeader(DMSConstants.DEVICE_TYPE, device.getDeviceType())

                    .addHeader("patientid", patientId)
                    .addHeader("docid", String.valueOf(docId))
                    .addHeader("opddate", visitDateToPass)
                    .addHeader("opdtime", currentOpdTime)
                    .addHeader("opdid", opdId)
                    .addHeader("hospitalid", String.valueOf(mHospitalId))
                    .addHeader("hospitalpatid", mHospitalPatId)
                    .addHeader("locationid", mLocationId)
                    .addHeader("aptid", String.valueOf(mAptId))
                    .addFileToUpload(image.getImagePath(), "attachment");

            String docCaption;

            if (!image.getParentCaption().isEmpty()) {
                docCaption = image.getParentCaption();
            } else
                docCaption = CommonMethods.stripExtension(CommonMethods.getFileNameFromPath(image.getImagePath()));

            uploadRequest.addHeader("captionname", docCaption);

            uploadConfig.getProgress().message = uploadConfig.getProgress().message.replace("record", docCaption);
            uploadRequest.setNotificationConfig(uploadConfig);

            uploadRequest.startUpload();

        } catch (FileNotFoundException | MalformedURLException e) {
            e.printStackTrace();
        }

        //  appDBHelper.insertMyRecordsData(uploadId, DMSConstants.UPLOADING, new Gson().toJson(image), docId, opdId, visitDate);
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DOC_UPLOAD);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() != null) {
                if (intent.getAction().equals(DOC_UPLOAD)) {

                   *//*
                    String isFailed = intent.getStringExtra(STATUS);

                    if (imagePaths.size() == count) {
                        allDone();
                    }

                    if (!isFailed) {

                    } else {

                    }*//*
                }
            }
        }
    };*/


}
