package com.scorg.dms.ui.activities.dms_patient_list;


//----------- IMPLEMENTED CODE : 16 AUGUST 2018 with differnt pdfview, lenthy code

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.DownloadUtil;
import com.scorg.dms.R;
import com.scorg.dms.adapters.dms_adapters.CustomPreferenceSpinAdapter;
import com.scorg.dms.helpers.patient_list.DMSPatientsHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.ErrorDialogCallback;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.dms_models.requestmodel.archive.GetArchiveRequestModel;
import com.scorg.dms.model.dms_models.requestmodel.archive.RaiseUnlockRequestModel;
import com.scorg.dms.model.dms_models.requestmodel.archive.UnlockRequestResponseBaseModel;
import com.scorg.dms.model.dms_models.requestmodel.showfile_data.GetEncryptedPDFRequestModel;
import com.scorg.dms.model.dms_models.responsemodel.episode_list.PatientEpisodeFileData;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.ArchiveDatum;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.FileTreeResponseData;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.FileTreeResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.LstDateFileType;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.LstDateFolderType;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.LstDocCategory;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.LstDocType;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.LstHideDocType;
import com.scorg.dms.model.dms_models.responsemodel.getpdfdataresponsemodel.GetPdfDataResponseModel;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.ui.activities.BaseActivity;
import com.scorg.dms.ui.customesViews.CustomProgressDialog;
import com.scorg.dms.ui.customesViews.treeViewHolder.arrow_expand.ArrowExpandIconTreeItemHolder;
import com.scorg.dms.ui.customesViews.treeViewHolder.arrow_expand.ArrowExpandSelectableHeaderHolder;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.github.barteksc.pdfviewer.PDFView.DEFAULT_MIN_SCALE;

/**
 * Created by jeetal on 14/3/17.
 */


public class FileTypeViewerActivity extends BaseActivity implements HelperResponse, OnLoadCompleteListener, OnErrorListener, OnDrawListener, TreeNode.TreeNodeClickListener, ArrowExpandSelectableHeaderHolder.ArrowExpandSelectableHeaderHolderLockIconClickListener {

    private static final long ANIMATION_DURATION = 500; // in milliseconds

    private static final String TAG = FileTypeViewerActivity.class.getName();
    private static final int REQUEST_CODE_WRITE_FILE_ONE_PERMISSIONS = 101;
    private static final int REQUEST_CODE_WRITE_FILE_TWO_PERMISSIONS = 102;
    //------
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    //------compare dialog box obj initalize
    @BindView(R.id.compareDialogParentLayout)
    LinearLayout mCompareDialogParentLayout;
    @BindView(R.id.compareDialog)
    RelativeLayout mCompareDialogLayout;
    @BindView(R.id.fileOneRemoveButton)
    AppCompatImageView mFileOneRemoveButton;
    // End
    @BindView(R.id.fileOnePatientID)
    TextView mFileOnePatientID;
    @BindView(R.id.fileOneFileName)
    TextView mFileOneFileName;
    @BindView(R.id.fileOneIcon)
    ImageView mFileOneIcon;
    @BindView(R.id.fileTwoRemoveButton)
    AppCompatImageView mFileTwoRemoveButton;
    @BindView(R.id.fileTwoPatientID)
    TextView mFileTwoPatientID;
    @BindView(R.id.fileTwoFileName)
    TextView mFileTwoFileName;
    @BindView(R.id.fileTwoIcon)
    ImageView mFileTwoIcon;
    @BindView(R.id.compareButton)
    Button mCompareButton;
    @BindView(R.id.archivedPreferenceSpinner)
    Spinner mArchivedPreferenceSpinner;
    @BindView(R.id.archivedPreferenceSpinnerBG)
    LinearLayout archivedPreferenceSpinnerBG;
    @BindView(R.id.preferenceLayout)
    LinearLayout mPreferenceLayout;
    //---------
    @BindView(R.id.firstPdfView)
    PDFView mFirstPdfView;
    @BindView(R.id.secondPdfView)
    PDFView mSecondPdfView;
    @BindView(R.id.firstPdfViewFrameLayout)
    FrameLayout mFirstFileTypePdfViewLayout;
    //---------------------------------------
    @BindView(R.id.secondPdfViewFrameLayout)
    FrameLayout mSecondFileTypePdfViewLayout;
    @BindView(R.id.messageForFirstFile)
    TextView mMessageForFirstFile;
    @BindView(R.id.messageForSecondFile)
    TextView mMessageForSecondFile;
    @BindView(R.id.openRightDrawer)
    ImageView mOpenRightDrawer;
    @BindView(R.id.fileTypeOneDoctorName)
    TextView mDoctorNameOne;
    @BindView(R.id.fileTypeTwoDoctorName)
    TextView mDoctorNameTwo;
    //---------
    @BindView(R.id.tvPatientLocation)
    TextView mPatientAddress;
    @BindView(R.id.fileTypeOneRefID)
    TextView mFileOneRefId;
    @BindView(R.id.fileTypeTwoRefID)
    TextView mFileTwoRefId;
    // End
    @BindView(R.id.fileTypeOneAdmissionDate)
    TextView mAdmissionDateOne;
    @BindView(R.id.fileTypeTwoAdmissionDate)
    TextView mAdmissionDateTwo;
    @BindView(R.id.fileTypeOneDischargeDate)
    TextView mDischargeDateOne;
    @BindView(R.id.fileTypeTwoDischargeDate)
    TextView mDischargeDateTwo;
    @BindView(R.id.tvPatientUHID)
    TextView mPatientId;
    @BindView(R.id.tvPatientName)
    TextView mPatientName;
    @BindView(R.id.fileTypeOneFileTypeName)
    TextView mFileTypeOne;
    @BindView(R.id.fileTypeTwoFileTypeName)
    TextView mFileTypeTwo;
    @BindView(R.id.compareLabel)
    TextView compareLabel;


    //----------
    @BindView(R.id.fileTypeTreeViewContainer)
    RelativeLayout mFileTypeOneTreeViewContainer;
    @BindView(R.id.loadPreviousArchiveDataList)
    AppCompatImageButton mLoadPreviousArchiveDataList;
    @BindView(R.id.loadNextArchiveDataList)
    AppCompatImageButton mLoadNextArchiveDataList;
    @BindView(R.id.loadedArchiveDataMessage)
    TextView loadedArchiveDataMessage;
    @BindView(R.id.labelSecondPdf)
    TextView labelSecondPdf;
    @BindView(R.id.labelFirstPdf)
    TextView labelFirstPdf;
    @BindView(R.id.fileOneLay)
    LinearLayout mFileOneDrawerLayout;
    @BindView(R.id.fileTwoLay)
    LinearLayout mFileTwoDrawerLayout;
    @BindView(R.id.nav_right_view)
    FrameLayout mRightNavigationView;
    @BindView(R.id.dischargeDateRow)
    TableRow dischargeDateRow;
    @BindView(R.id.dischargeDateRowTwo)
    TableRow dischargeDateRowTwo;
    @BindView(R.id.layoutCompareSwitch)
    LinearLayout layoutCompareSwitch;
    @BindView(R.id.compareDialogSwitch)
    SwitchCompat mOpenCompareDialogSwitch;
    @BindView(R.id.layoutCompareSwitch1)
    LinearLayout layoutCompareSwitch1;
    @BindView(R.id.compareDialogSwitch1)
    SwitchCompat mOpenCompareDialogSwitch1;
    @BindView(R.id.imageCloseDrawer)
    AppCompatImageButton imageCloseDrawer;
    @BindView(R.id.patientIcon)
    ImageView patientIcon;
    @BindView(R.id.uhidIcon)
    ImageView uhidIcon;
    @BindView(R.id.addressIcon)
    ImageView addressIcon;

    @BindView(R.id.deviderView)
    View deviderView;

    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;

    @BindView(R.id.imgNoRecordFound)
    ImageView imgNoRecordFound;
    @BindView(R.id.labelDrawerUDID)
    TextView labelDrawerUDID;

    @BindView(R.id.footerLayout)
    LinearLayout footerLayout;

    @BindView(R.id.layoutDrawerAddress)
    LinearLayout layoutDrawerAddress;

    DrawerLayout mDrawer;
    //---------
    ArrayList<PatientEpisodeFileData> mSelectedFileTypeDataToCompare;
    String respectivePatientID;
    String respectivePatID;
    String respectiveRecordID;
    int getArchivedPageNumber = 1;
    String patientName;
    String doctorName;
    String patientAddress;
    private boolean isFirstPdf = true;
    private float mCurrentXOffset = -1;
    private float mCurrentYOffset = -1;
    private Context mContext;
    private DMSPatientsHelper mPatientsHelper;
    private boolean isCompareChecked = false;
    //---------
    private FileTreeResponseData mFileTreeResponseData;

    private RelativeLayout mFirstFileTypeProgressDialogLayout;
    private RelativeLayout mSecondFileTypeProgressDialogLayout;
    private LinkedHashMap<Integer, String> mPreviousClickedTreeElement = null;
    private String fileOneData;
    private String fileTwoData;
    private String mArchivedSelectedPreference = DMSConstants.ArchivedPreference.FOLDER;
    private boolean isCompareDialogCollapsed;
    private int archiveCount = 0;
    private int archiveApiCount = 0;
    private int currentCount = 0;

    private ArrayList<GetEncryptedPDFRequestModel> mGetEncryptedPDFRequestModelList = new ArrayList<>();

    private CustomProgressDialog customProgressDialog;
    private boolean isTreeCreated;
    private boolean isBackPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare_file_type_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        findViewById(R.id.layoutMedicalRecordHeader).setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        initialize();
    }

    private void initialize() {
        mContext = this;
        customProgressDialog = new CustomProgressDialog(mContext);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        patientIcon.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        uhidIcon.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        addressIcon.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        deviderView.setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        mCompareButton.setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));

        mLoadPreviousArchiveDataList.setColorFilter(Color.parseColor(DMSApplication.COLOR_ACCENT));
        mLoadNextArchiveDataList.setColorFilter(Color.parseColor(DMSApplication.COLOR_ACCENT));

        GradientDrawable cardBackground = new GradientDrawable();
        cardBackground.setShape(GradientDrawable.RECTANGLE);
        cardBackground.setColor(Color.WHITE);
        cardBackground.setCornerRadius(mContext.getResources().getDimension(R.dimen.dp8));
        cardBackground.setStroke(mContext.getResources().getDimensionPixelSize(R.dimen.dp1), Color.parseColor(DMSApplication.COLOR_PRIMARY));
        mPatientId.setBackground(cardBackground);

        GradientDrawable spinnerBackground = new GradientDrawable();
        spinnerBackground.setShape(GradientDrawable.RECTANGLE);
        spinnerBackground.setColor(Color.WHITE);
        spinnerBackground.setCornerRadius(mContext.getResources().getDimension(R.dimen.dp8));
        spinnerBackground.setStroke(mContext.getResources().getDimensionPixelSize(R.dimen.dp1), Color.parseColor(DMSApplication.COLOR_PRIMARY));
        archivedPreferenceSpinnerBG.setBackground(spinnerBackground);

        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked},
        };

        int[] thumbColors = new int[]{
                Color.LTGRAY,
                Color.parseColor(DMSApplication.COLOR_ACCENT)
        };

        int[] trackColors = new int[]{
                Color.LTGRAY,
                Color.parseColor(DMSApplication.COLOR_ACCENT),
        };

        DrawableCompat.setTintList(DrawableCompat.wrap(mOpenCompareDialogSwitch.getThumbDrawable()), new ColorStateList(states, thumbColors));
        DrawableCompat.setTintList(DrawableCompat.wrap(mOpenCompareDialogSwitch.getTrackDrawable()), new ColorStateList(states, trackColors));

        DrawableCompat.setTintList(DrawableCompat.wrap(mOpenCompareDialogSwitch1.getThumbDrawable()), new ColorStateList(states, thumbColors));
        DrawableCompat.setTintList(DrawableCompat.wrap(mOpenCompareDialogSwitch1.getTrackDrawable()), new ColorStateList(states, trackColors));
        compareLabel.setBackgroundColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        mFileOneRemoveButton.setBackgroundColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        mFileTwoRemoveButton.setBackgroundColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        Bundle extra = getIntent().getBundleExtra(DMSConstants.DATA);

        if (extra != null) {
            mSelectedFileTypeDataToCompare = (ArrayList<PatientEpisodeFileData>) extra.getSerializable(getString(R.string.compare));
            respectivePatientID = extra.getString(DMSConstants.PATIENT_ID);
            respectiveRecordID = extra.getString(DMSConstants.RECORD_ID);
            patientName = extra.getString(DMSConstants.PATIENT_LIST_PARAMS.PATIENT_NAME);
            patientAddress = extra.getString(DMSConstants.PATIENT_ADDRESS);
            doctorName = extra.getString(DMSConstants.DOCTOR_NAME);
            respectivePatID = extra.getString(DMSConstants.PAT_ID);
        }


        //-------------
        mPatientsHelper = new DMSPatientsHelper(this, this);
        mPreviousClickedTreeElement = new LinkedHashMap<>();

        bindView();
        mFirstPdfView.fromAsset("NO_DOCUMENT_LOADED.pdf").load();
        mSecondPdfView.fromAsset("NO_DOCUMENT_LOADED.pdf").load();

        //-----------
        mDrawer.openDrawer(GravityCompat.END);

        if (respectiveRecordID != null) {
            mArchivedPreferenceSpinnerListenerForEpisode();
        } else {
            mArchivedPreferenceSpinnerListener();
        }
        if (CommonMethods.isTablet(this)) {
            layoutCompareSwitch.setVisibility(View.VISIBLE);
        }


    }

    public void expandCompareDialog() {

        isCompareDialogCollapsed = false;

        mCompareDialogParentLayout.setVisibility(View.VISIBLE);

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, (int) getResources().getDimension(R.dimen.compare_dialog));
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mCompareDialogLayout.getLayoutParams();
                params.height = CommonMethods.convertDpToPixel(value.intValue());
                mCompareDialogLayout.setLayoutParams(params);

            }
        });

        valueAnimator.start();
    }

    public void collapseCompareDialog() {
        isCompareDialogCollapsed = true;

        mCompareDialogParentLayout.setVisibility(View.GONE);

        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) getResources().getDimension(R.dimen.compare_dialog), 0);
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mCompareDialogLayout.getLayoutParams();
                params.height = CommonMethods.convertDpToPixel(value.intValue());
                mCompareDialogLayout.setLayoutParams(params);

            }
        });

        valueAnimator.start();

    }


    private void bindView() {
        int width = (int) (getResources().getDisplayMetrics().widthPixels / (CommonMethods.isTablet(mContext) ? 1.2 : 1.2));

        //---------
        mDrawer = findViewById(R.id.drawer_layout);
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back_arrow_a_01));
        toggle.syncState();
        mToolbar.setTitle(getString(R.string.pdf));
        doBindHeaderViews();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDFViewCache/");
                file.delete();
                finish();
            }
        });
        //-----------
        mFirstFileTypePdfViewLayout.addView(CommonMethods.progressDialogView(R.layout.mydialog, this));
        mFirstFileTypeProgressDialogLayout = mFirstFileTypePdfViewLayout.findViewById(R.id.progressBarContainerLayout);
        mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
        mSecondFileTypePdfViewLayout.addView(CommonMethods.progressDialogView(R.layout.mydialog, this));
        mSecondFileTypeProgressDialogLayout = mSecondFileTypePdfViewLayout.findViewById(R.id.progressBarContainerLayout);
        mSecondFileTypeProgressDialogLayout.setVisibility(View.GONE);

        //------RightNavigationView initialize---------

        ViewGroup.LayoutParams layoutParams = mRightNavigationView.getLayoutParams();
        layoutParams.width = width;
        mRightNavigationView.setLayoutParams(layoutParams);


        archiveApiCount = DMSPreferencesManager.getInt(DMSPreferencesManager.DMS_PREFERENCES_KEY.ARCHIVE_API_COUNT, this);
        currentCount = archiveApiCount;

        mOpenCompareDialogSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCompareChecked = isChecked;
            }
        });

        mOpenCompareDialogSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    mOpenCompareDialogSwitch1.setChecked(false);
                    layoutCompareSwitch1.setVisibility(View.GONE);
                    mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
                    //--------
                    mSecondFileTypePdfViewLayout.setVisibility(View.GONE);
                    mSecondFileTypeProgressDialogLayout.setVisibility(View.GONE);
                } else {
                    layoutCompareSwitch1.setVisibility(View.VISIBLE);
                    mSecondFileTypePdfViewLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (mDrawer != null) {
            if (mDrawer.isDrawerOpen(GravityCompat.END) && mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.closeDrawer(GravityCompat.END);
                mDrawer.closeDrawer(GravityCompat.START);
            } else if (mDrawer.isDrawerOpen(GravityCompat.END)) {
                mDrawer.closeDrawer(GravityCompat.END);
            } else if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.closeDrawer(GravityCompat.START);
            } else super.onBackPressed();
        } else {
            final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDFViewCache/");
            file.delete();
            super.onBackPressed();
        }

    }


    private void doBindHeaderViews() {
        mPatientName.setText(patientName);
        mPatientId.setText(respectivePatientID);
        mDoctorNameTwo.setText(doctorName);
        mDoctorNameOne.setText(doctorName);



        if(patientAddress!=null )
            mPatientAddress.setText(patientAddress);
        else
            layoutDrawerAddress.setVisibility(View.GONE);

        labelDrawerUDID.setText(DMSApplication.LABEL_UHID);
    }

    @OnClick({R.id.imageCloseDrawer, R.id.openRightDrawer, R.id.loadPreviousArchiveDataList, R.id.loadNextArchiveDataList, R.id.compareButton, R.id.compareLabel, R.id.fileOneRemoveButton, R.id.fileTwoRemoveButton})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openRightDrawer:
                archiveCount = 0;
                mDrawer.openDrawer(GravityCompat.END);
                if (!isTreeCreated)
                    doCreateTreeStructure();
                break;
            case R.id.loadPreviousArchiveDataList:
                archiveCount = 0;
                isBackPress = true;
                if (getArchivedPageNumber == 1) {
                    mLoadPreviousArchiveDataList.setVisibility(View.INVISIBLE);
                    CommonMethods.showToast(this, "No Previous data to load");
                } else {
                    getArchivedPageNumber = getArchivedPageNumber - 1;
                    currentCount = currentCount - archiveApiCount;
                    mFileTreeResponseData = null;
                    doCreateTreeStructure();
                    mLoadNextArchiveDataList.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.loadNextArchiveDataList:
                archiveCount = 0;
                isBackPress = false;
                if (mFileTreeResponseData != null) {
                    if (!mFileTreeResponseData.isPagination()) {
                        CommonMethods.showToast(this, "No Next data to load");
                        mLoadNextArchiveDataList.setVisibility(View.INVISIBLE);
                        mLoadPreviousArchiveDataList.setVisibility(View.VISIBLE);
                    } else {
                        currentCount = currentCount + archiveApiCount;
                        mFileTreeResponseData = null;
                        getArchivedPageNumber = getArchivedPageNumber + 1;
                        doCreateTreeStructure();
                        mLoadPreviousArchiveDataList.setVisibility(View.VISIBLE);
                    }
                } else CommonMethods.showToast(this, "No Next data to load");

                break;
            case R.id.compareButton:
                collapseCompareDialog();
                if (mGetEncryptedPDFRequestModelList.size() == 2) {
                    mPatientsHelper.getPdfData(mGetEncryptedPDFRequestModelList.get(0), DMSConstants.TASK_GET_PDF_DATA + "_0");
                    mPatientsHelper.getPdfData(mGetEncryptedPDFRequestModelList.get(1), DMSConstants.TASK_GET_PDF_DATA + "_1");
                }

                break;
            case R.id.compareLabel:
                if (isCompareDialogCollapsed)
                    expandCompareDialog();
                else collapseCompareDialog();
                break;
            case R.id.fileOneRemoveButton:
                String fileToRemove = mFileOneFileName.getText().toString().trim().replace(getString(R.string.file), "").trim();
                for (Integer key : mPreviousClickedTreeElement.keySet()) {
                    if (mPreviousClickedTreeElement.get(key).equalsIgnoreCase(fileToRemove)) {
                        mPreviousClickedTreeElement.remove(key);
                        break;
                    }
                }

                if (mGetEncryptedPDFRequestModelList.size() != 0)
                    mGetEncryptedPDFRequestModelList.remove(0);
                mFileOneFileName.setText("");
                mFileOnePatientID.setText("");

                break;
            case R.id.fileTwoRemoveButton:
                String fileTwoRemove = mFileTwoFileName.getText().toString().trim().replace(getString(R.string.file), "").trim();
                for (Integer key : mPreviousClickedTreeElement.keySet()) {
                    if (mPreviousClickedTreeElement.get(key).equalsIgnoreCase(fileTwoRemove)) {
                        mPreviousClickedTreeElement.remove(key);
                        break;
                    }
                }
                if (mGetEncryptedPDFRequestModelList.size() == 2)
                    mGetEncryptedPDFRequestModelList.remove(1);
                mFileTwoFileName.setText("");
                mFileTwoPatientID.setText("");
                break;
            case R.id.imageCloseDrawer:
                archiveCount = 0;
                mDrawer.closeDrawer(GravityCompat.END);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDFViewCache/");
        file.delete();
        super.onDestroy();
    }

    // To create JSON and get archived list of data.
    private void getLoadArchivedList() {
        customProgressDialog.show();
        //---------------
        GetArchiveRequestModel model = new GetArchiveRequestModel();
        model.setPatId(respectivePatID);
        //model.setPatId("" + 200156);
        //model.setPatId("" + 143369);
        model.setRecordId(respectiveRecordID);
        model.setPageNumber(getArchivedPageNumber);
        model.setPreference(mArchivedSelectedPreference);
        mPatientsHelper.doGetArchivedList(model);
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(DMSConstants.TASK_GET_ARCHIVED_LIST)) {
            hideProgressDialog();
            FileTreeResponseModel fileTreeResponseModel = (FileTreeResponseModel) customResponse;
            if (!fileTreeResponseModel.getCommon().getStatusCode().equals(DMSConstants.SUCCESS)) {

                if (emptyListView.getVisibility() != View.VISIBLE)
                    emptyListView.setVisibility(View.VISIBLE);


                setErrorDialog(fileTreeResponseModel.getCommon().getStatusMessage(), mOldDataTag, false, FileTypeViewerActivity.this);

                if (isBackPress) {
                    getArchivedPageNumber = getArchivedPageNumber + 1;
                } else {
                    getArchivedPageNumber = getArchivedPageNumber - 1;
                }

            } else {

                if (fileTreeResponseModel.getFileTreeResponseData().isPagination()) {
                    footerLayout.setVisibility(View.VISIBLE);
                    if (getArchivedPageNumber == 1)
                        mLoadPreviousArchiveDataList.setVisibility(View.INVISIBLE);
                } else {
                    mLoadPreviousArchiveDataList.setVisibility(View.VISIBLE);
                    mLoadNextArchiveDataList.setVisibility(View.INVISIBLE);
                }


                if (!fileTreeResponseModel.getFileTreeResponseData().getArchiveData().isEmpty()) {
                    if (emptyListView.getVisibility() == View.VISIBLE)
                        emptyListView.setVisibility(View.GONE);
                    mFileTreeResponseData = fileTreeResponseModel.getFileTreeResponseData();
                    doCreateTreeStructure();
                } else {
                    setErrorDialog("Records Not Found", mOldDataTag, false, FileTypeViewerActivity.this);
                }
            }

            //------

        } else if (String.valueOf(mOldDataTag.toLowerCase()).contains("" + DMSConstants.TASK_GET_PDF_DATA.toLowerCase())) {

            GetPdfDataResponseModel data = (GetPdfDataResponseModel) customResponse;

            if (String.valueOf(mOldDataTag).endsWith("1")) {
                CommonMethods.Log("SECONDVIEW", "mOldDataTag:" + mOldDataTag);
                doCallPDFDataService(data.getGetPdfDataResponseData().getFileData(), String.valueOf(mOldDataTag));
            } else if (String.valueOf(mOldDataTag).endsWith("0")) {
                CommonMethods.Log("FIRSTVIEW", "mOldDataTag:" + mOldDataTag);
                doCallPDFDataService(data.getGetPdfDataResponseData().getFileData(), String.valueOf(mOldDataTag));
            }
        } else if (String.valueOf(mOldDataTag).contains("" + DMSConstants.TASK_RAISE_REQUEST_CONFIDENTIAL)) {

            UnlockRequestResponseBaseModel unlockRequestResponseBaseMode = (UnlockRequestResponseBaseModel) customResponse;

            String msg = unlockRequestResponseBaseMode.getRequestResponseResultUnlock().getResult();
            CommonMethods.showToast(this, getResources().getString(R.string.request_raised_success));


        }
    }

    private void hideProgressDialog() {
        if (customProgressDialog.isShowing()) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms

                    customProgressDialog.dismiss();
                }
            }, 500);
        }

        if (mSecondFileTypeProgressDialogLayout.getVisibility() == View.VISIBLE)
            mSecondFileTypeProgressDialogLayout.setVisibility(View.GONE);

        if (mFirstFileTypeProgressDialogLayout.getVisibility() == View.VISIBLE)
            mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);


    }

    void setErrorDialog(String errorMessage, String mOldDataTag, boolean isTimeout, Context mContext) {
        hideProgressDialog();
        CommonMethods.showErrorDialog(errorMessage, mContext, isTimeout, new ErrorDialogCallback() {
            @Override
            public void ok() {

            }

            @Override
            public void retry() {
                if (mFileTreeResponseData == null)
                    getLoadArchivedList();
            }
        });
    }


    private void doValidateReceivedEncryptedFilePath(String filePath, String mOldDataTag) {
        //------

        //  String fileData = CommonMethods.decryptPDFFilePathUsingAESAlgo(filePath);
        String fileData = filePath;
        if (fileData != null) {
            if (mOldDataTag.endsWith("0")) {
                //  mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
                fileOneData = fileData;
                askWriteExtenralStoragePermission(REQUEST_CODE_WRITE_FILE_ONE_PERMISSIONS);
            } else if (mOldDataTag.endsWith("1")) {
                // mSecondFileTypeProgressDialogLayout.setVisibility(View.GONE);
                fileTwoData = fileData;
                askWriteExtenralStoragePermission(REQUEST_CODE_WRITE_FILE_TWO_PERMISSIONS);
            }
        } else
            Toast.makeText(mContext, "Document not available", Toast.LENGTH_SHORT).show();


        //----

    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        if (String.valueOf(mOldDataTag).equalsIgnoreCase("" + DMSConstants.TASK_GET_ARCHIVED_LIST)) {
            setErrorDialog(errorMessage, mOldDataTag, false, mContext);
            if (mFileTreeResponseData.getArchiveData().isEmpty())
                emptyListView.setVisibility(View.VISIBLE);
        } else {
            setErrorDialog(errorMessage, mOldDataTag, false, mContext);
        }

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        if (String.valueOf(mOldDataTag).equalsIgnoreCase("" + DMSConstants.TASK_GET_ARCHIVED_LIST)) {
            setErrorDialog(serverErrorMessage, mOldDataTag, false, mContext);
            if (mFileTreeResponseData.getArchiveData().isEmpty())
                emptyListView.setVisibility(View.VISIBLE);
        } else {
            setErrorDialog(serverErrorMessage, mOldDataTag, false, mContext);
        }
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        if (String.valueOf(mOldDataTag).equalsIgnoreCase("" + DMSConstants.TASK_GET_ARCHIVED_LIST)) {
            setErrorDialog(serverErrorMessage, mOldDataTag, false, mContext);
            if (mFileTreeResponseData.getArchiveData().isEmpty())
                emptyListView.setVisibility(View.VISIBLE);
        } else {
            setErrorDialog(serverErrorMessage, mOldDataTag, false, mContext);
        }
    }

    @Override
    public void onTimeOutError(String mOldDataTag, String timeOutErrorMessage) {
        if (String.valueOf(mOldDataTag).equalsIgnoreCase("" + DMSConstants.TASK_GET_ARCHIVED_LIST)) {
            setErrorDialog(timeOutErrorMessage, mOldDataTag, true, mContext);
            if (mFileTreeResponseData.getArchiveData().isEmpty())
                emptyListView.setVisibility(View.VISIBLE);
        } else {
            setErrorDialog(timeOutErrorMessage, mOldDataTag, false, mContext);
        }
    }


    private void askWriteExtenralStoragePermission(int requestCode) {
        int hasWriteContactsPermissionCamera = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasWriteContactsPermissionCamera != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
            }
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_WRITE_FILE_ONE_PERMISSIONS:
                loadPDFFromServer(fileOneData, mFirstPdfView);
                break;

            case REQUEST_CODE_WRITE_FILE_TWO_PERMISSIONS:
                loadPDFFromServer(fileTwoData, mSecondPdfView);
                break;

            default:
                CommonMethods.Log(TAG, String.valueOf(requestCode));
        }

    }

    private void createAnnotationTree(FileTreeResponseData fileTreeResponseData, boolean isExpanded) {
        mFileTypeOneTreeViewContainer.removeAllViews();

        TreeNode treeRoot = TreeNode.root();
        int confidentialState;
        int lstDocTypeChildLeftPadding = (int) (getResources().getDimension(R.dimen.level1) / getResources().getDisplayMetrics().density);
        int lstDateFolderTypeChildLeftPadding = (int) (getResources().getDimension(R.dimen.level2) / getResources().getDisplayMetrics().density);
        int lstFileLeftPadding = (int) (getResources().getDimension(R.dimen.level3) / getResources().getDisplayMetrics().density);
        int textColor = ContextCompat.getColor(this, R.color.black);

        List<ArchiveDatum> archiveData = fileTreeResponseData.getArchiveData();
        int size = archiveData.size();

        // For archived data list
        for (int i = 0; i < size; i++) {
            ArchiveDatum archiveDatumObject = archiveData.get(i);
            confidentialState = archiveDatumObject.getConfidentialState();
            ArrowExpandSelectableHeaderHolder selectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, i == 0, true, confidentialState, false);
            selectableHeaderHolder.setOnlyOneNodeExpanded(true);
            selectableHeaderHolder.setNodeValueColor(textColor);

            //---- To bold clicked text in tree
            //  if (archiveDatumObject.getFileType().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
            if (archiveDatumObject.getFileType().equalsIgnoreCase(mPreviousClickedTreeElement.get(archiveDatumObject.getFileType())))
                selectableHeaderHolder.setTreeLabelBold(true);


            // Label(pageCount)|NA
            String dataToShow = archiveDatumObject.getFileType() + " (" + archiveDatumObject.getPageCount() + ")" + "|NA";
            TreeNode archiveDatumObjectFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, archiveDatumObject, i))
                    .setViewHolder(selectableHeaderHolder);


            //////////////
            //          //
            //          //
            //          //
            //////////////

            // Hide Type ----------------------------------------------------------

            //---- For list lstDateFolderType loop
            List<LstHideDocType> lstHideDocTypes = archiveDatumObject.getLstHideDocTypes();

            if (!CommonMethods.isNullOrEmpty(lstHideDocTypes)) {

                archiveCount = archiveCount + lstHideDocTypes.size();

                for (int l = 0; l < lstHideDocTypes.size(); l++) {
                    LstHideDocType lstHideDocType = lstHideDocTypes.get(l);

                    //-------NODE LstDateFolderType--------------
                    // Label(pageCount)|id
                    confidentialState = lstHideDocType.getConfidentialState();
                    dataToShow = lstHideDocType.getTypeName() + " (" + lstHideDocType.getPageCount() + ")" + "|NA";
                    ArrowExpandSelectableHeaderHolder lstDateFolderTypeSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, (l == 0), lstDocTypeChildLeftPadding, false, confidentialState, false);
                    lstDateFolderTypeSelectableHeaderHolder.setOnlyOneNodeExpanded(false);

                    lstDateFolderTypeSelectableHeaderHolder.setNodeValueColor(textColor);

                    //---- To bold clicked text in tree
                    // if (lstDateFolderType.getDateFolderType().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                    if (lstHideDocType.getTypeName().equalsIgnoreCase(mPreviousClickedTreeElement.get(lstHideDocType.getRecordDetailId())))
                        lstDateFolderTypeSelectableHeaderHolder.setTreeLabelBold(true);

                    TreeNode lstDateFolderTypeObjectFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstHideDocType, i))
                            .setViewHolder(lstDateFolderTypeSelectableHeaderHolder);
                    //---

                    archiveDatumObjectFolder.addChildren(lstDateFolderTypeObjectFolder);
                }
            }

            // My Order ----------------------------------------------------------

            //---- For list lstDateFolderType loop
            List<LstDateFileType> lstOrderedDocTypeList = archiveDatumObject.getLstOrderedDocTypes();

            if (!CommonMethods.isNullOrEmpty(lstOrderedDocTypeList)) {

                archiveCount = archiveCount + lstOrderedDocTypeList.size();

                for (int l = 0; l < lstOrderedDocTypeList.size(); l++) {
                    LstDateFileType lstOrderedDocType = lstOrderedDocTypeList.get(l);

                    //-------NODE LstDateFolderType--------------
                    // Label(pageCount)|id
                    confidentialState = lstOrderedDocType.getConfidentialState();
                    dataToShow = lstOrderedDocType.getTypeName() + " (" + lstOrderedDocType.getPageCount() + ")" + "|NA";
                    ArrowExpandSelectableHeaderHolder lstDateFolderTypeSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, (l == 0), lstDocTypeChildLeftPadding, false, confidentialState, false);
                    lstDateFolderTypeSelectableHeaderHolder.setOnlyOneNodeExpanded(false);

                    lstDateFolderTypeSelectableHeaderHolder.setNodeValueColor(textColor);

                    //---- To bold clicked text in tree
                    // if (lstDateFolderType.getDateFolderType().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                    if (lstOrderedDocType.getTypeName().equalsIgnoreCase(mPreviousClickedTreeElement.get(lstOrderedDocType.getRecordDetailId())))
                        lstDateFolderTypeSelectableHeaderHolder.setTreeLabelBold(true);

                    TreeNode lstDateFolderTypeObjectFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstOrderedDocType, i))
                            .setViewHolder(lstDateFolderTypeSelectableHeaderHolder);
                    //---

                    archiveDatumObjectFolder.addChildren(lstDateFolderTypeObjectFolder);
                }
            }


            //////////////
            //          //
            //          //
            //          //
            //////////////

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////

            // Doc List -------------------------------------------------------------------------

            List<LstDocCategory> archiveDataLstDocCategoriesList = archiveDatumObject.getArchiveDataLstDocCategories();

            if (!CommonMethods.isNullOrEmpty(archiveDataLstDocCategoriesList)) {
                //---- For list lstDateFolderType loop
                archiveCount = archiveCount + archiveDataLstDocCategoriesList.size();

                for (int l = 0; l < archiveDataLstDocCategoriesList.size(); l++) {
                    LstDocCategory lstDocCategory = archiveDataLstDocCategoriesList.get(l);

                    //-------NODE LstDateFolderType--------------
                    // Label(pageCount)|id
                    confidentialState = lstDocCategory.getConfidentialState();
                    dataToShow = lstDocCategory.getCategoryName() + " (" + lstDocCategory.getPageCount() + ")" + "|NA";
                    ArrowExpandSelectableHeaderHolder lstDateFolderTypeSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, (l == 0), lstDocTypeChildLeftPadding, true, confidentialState, false);
                    lstDateFolderTypeSelectableHeaderHolder.setOnlyOneNodeExpanded(false);

                    lstDateFolderTypeSelectableHeaderHolder.setNodeValueColor(textColor);

                    //---- To bold clicked text in tree
                    // if (lstDateFolderType.getDateFolderType().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                    if (lstDocCategory.getCategoryName().equalsIgnoreCase(mPreviousClickedTreeElement.get(lstDocCategory.getCategoryName())))
                        lstDateFolderTypeSelectableHeaderHolder.setTreeLabelBold(true);

                    TreeNode lstDateFolderTypeObjectFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstDocCategory, i))
                            .setViewHolder(lstDateFolderTypeSelectableHeaderHolder);
                    //---

                    //////////////
                    //          //
                    //          //
                    //          //
                    //////////////

                    // Hide Type

                    /////////-------------------------------------------------------

                    List<LstHideDocType> lsthideDocCategory = lstDocCategory.getLsthideDocCategory();

                    for (int j = 0; j < lsthideDocCategory.size(); j++) {
                        LstHideDocType lstHideDocType = lsthideDocCategory.get(j);

                        // Label(pageCount)|id
                        confidentialState = lstHideDocType.getConfidentialState();

                        dataToShow = lstHideDocType.getTypeName() + " (" + lstHideDocType.getPageCount() + ")" + "|" + lstHideDocType.getTypeId();
                        ArrowExpandSelectableHeaderHolder docCatSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, (l == 0 && j == 0), lstFileLeftPadding, false, confidentialState, false);
                        docCatSelectableHeaderHolder.setOnlyOneNodeExpanded(false);

                        docCatSelectableHeaderHolder.setNodeValueColor(textColor);

                        //---- To bold clicked text in tree
                        // if (lstDocCategoryObject.getCategoryName().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                        if (lstHideDocType.getTypeName().equalsIgnoreCase(mPreviousClickedTreeElement.get(lstHideDocType.getTypeName())))
                            docCatSelectableHeaderHolder.setTreeLabelBold(true);

                        TreeNode lstDocCategoryObjectFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstHideDocType, i))
                                .setViewHolder(docCatSelectableHeaderHolder);

                        lstDateFolderTypeObjectFolder.addChildren(lstDocCategoryObjectFolder);
                    }

                    /////////-------------------------------------------------------

                    //--------------------

                    //for lstDocTypes loop
                    List<LstDocType> lstDocTypesCategoriesChildList = lstDocCategory.getLstDocTypes();
                    for (int k = 0; k < lstDocTypesCategoriesChildList.size(); k++) {
                        LstDocType lstDocTypeChild = lstDocTypesCategoriesChildList.get(k);

                        // Label(pageCount)|id
                        dataToShow = lstDocTypeChild.getTypeName() + " (" + lstDocTypeChild.getPageCount() + ")" + "|" + lstDocTypeChild.getTypeId();
                        confidentialState = lstDocTypeChild.getConfidentialState();
                        //-------
                        ArrowExpandSelectableHeaderHolder lstDocTypeChildSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, (l == 0 && k == 0), lstFileLeftPadding, false, confidentialState, false);
                        lstDocTypeChildSelectableHeaderHolder.setOnlyOneNodeExpanded(false);
                        lstDocTypeChildSelectableHeaderHolder.setNodeValueColor(textColor);

                        //---- To bold clicked text in tree
                        // if (lstDocTypeChild.getTypeName().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                        if (lstDocTypeChild.getTypeName().equalsIgnoreCase(mPreviousClickedTreeElement.get(lstDocTypeChild.getRecordDetailId())))
                            lstDocTypeChildSelectableHeaderHolder.setTreeLabelBold(true);

                        TreeNode lstDocTypeChildFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstDocTypeChild, i))
                                .setViewHolder(lstDocTypeChildSelectableHeaderHolder);
                        //-------
                        lstDateFolderTypeObjectFolder.addChildren(lstDocTypeChildFolder);
                    }
                    archiveDatumObjectFolder.addChildren(lstDateFolderTypeObjectFolder);
                }
            }

            //////////////
            //          //
            //          //
            //          //
            //////////////

            // Date Folder Type ------------------------------------------------------------------

            //---- For list lstDateFolderType loop
            List<LstDateFolderType> lstDateFolderTypeList = archiveDatumObject.getLstDateFolderTypeList();

            if (!CommonMethods.isNullOrEmpty(lstDateFolderTypeList)) {
                archiveCount = archiveCount + lstDateFolderTypeList.size();

                for (int l = 0; l < lstDateFolderTypeList.size(); l++) {
                    LstDateFolderType lstDateFolderType = lstDateFolderTypeList.get(l);

                    //-------NODE LstDateFolderType--------------
                    // Label(pageCount)|id
                    confidentialState = lstDateFolderType.getConfidentialState();
                    dataToShow = lstDateFolderType.getDateFolderType() + " (" + lstDateFolderType.getPageCount() + ")" + "|NA";
                    ArrowExpandSelectableHeaderHolder lstDateFolderTypeSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, (l == 0), lstDocTypeChildLeftPadding, true, confidentialState, false);
                    lstDateFolderTypeSelectableHeaderHolder.setOnlyOneNodeExpanded(false);

                    lstDateFolderTypeSelectableHeaderHolder.setNodeValueColor(textColor);

                    //---- To bold clicked text in tree
                    // if (lstDateFolderType.getDateFolderType().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                    if (lstDateFolderType.getDateFolderType().equalsIgnoreCase(mPreviousClickedTreeElement.get(lstDateFolderType.getDateFolderType())))
                        lstDateFolderTypeSelectableHeaderHolder.setTreeLabelBold(true);

                    TreeNode lstDateFolderTypeObjectFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstDateFolderType, i))
                            .setViewHolder(lstDateFolderTypeSelectableHeaderHolder);
                    //---


                    //////////////
                    //          //
                    //          //
                    //          //
                    //////////////

                    // Hide Type

                    /////////-------------------------------------------------------

                    List<LstHideDocType> lsthideDocCategory = lstDateFolderType.getLsthideDocCategory();

                    for (int j = 0; j < lsthideDocCategory.size(); j++) {
                        LstHideDocType lstHideDocType = lsthideDocCategory.get(j);

                        // Label(pageCount)|id
                        confidentialState = lstHideDocType.getConfidentialState();

                        dataToShow = lstHideDocType.getTypeName() + " (" + lstHideDocType.getPageCount() + ")" + "|" + lstHideDocType.getTypeId();
                        ArrowExpandSelectableHeaderHolder docCatSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, (j == 0), lstDateFolderTypeChildLeftPadding, false, confidentialState, false);
                        docCatSelectableHeaderHolder.setOnlyOneNodeExpanded(false);

                        docCatSelectableHeaderHolder.setNodeValueColor(textColor);

                        //---- To bold clicked text in tree
                        // if (lstDocCategoryObject.getCategoryName().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                        if (lstHideDocType.getTypeName().equalsIgnoreCase(mPreviousClickedTreeElement.get(lstHideDocType.getTypeName())))
                            docCatSelectableHeaderHolder.setTreeLabelBold(true);

                        TreeNode lstDocCategoryObjectFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstHideDocType, i))
                                .setViewHolder(docCatSelectableHeaderHolder);

                        lstDateFolderTypeObjectFolder.addChildren(lstDocCategoryObjectFolder);
                    }

                    /////////-------------------------------------------------------

                    //////////////
                    //          //
                    //          //
                    //          //
                    //////////////

                    //---- For list categories loop
                    List<LstDocCategory> lstDocCategories = lstDateFolderType.getLstDocCategories();

                    for (int j = 0; j < lstDocCategories.size(); j++) {
                        LstDocCategory lstDocCategoryObject = lstDocCategories.get(j);

                        // Label(pageCount)|id
                        confidentialState = lstDocCategoryObject.getConfidentialState();

                        dataToShow = lstDocCategoryObject.getCategoryName() + " (" + lstDocCategoryObject.getPageCount() + ")" + "|" + lstDocCategoryObject.getCategoryId();
                        ArrowExpandSelectableHeaderHolder docCatSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, (j == 0), lstDateFolderTypeChildLeftPadding, true, confidentialState, false);
                        docCatSelectableHeaderHolder.setOnlyOneNodeExpanded(false);

                        docCatSelectableHeaderHolder.setNodeValueColor(textColor);

                        //---- To bold clicked text in tree
                        // if (lstDocCategoryObject.getCategoryName().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                        if (lstDocCategoryObject.getCategoryName().equalsIgnoreCase(mPreviousClickedTreeElement.get(lstDocCategoryObject.getCategoryName())))
                            docCatSelectableHeaderHolder.setTreeLabelBold(true);

                        TreeNode lstDocCategoryObjectFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstDocCategoryObject, i))
                                .setViewHolder(docCatSelectableHeaderHolder);
                        //---

                        //for lstDocTypes loop
                        List<LstDocType> lstDocTypesCategoriesChildList = lstDocCategoryObject.getLstDocTypes();
                        for (int k = 0; k < lstDocTypesCategoriesChildList.size(); k++) {
                            LstDocType lstDocTypeChild = lstDocTypesCategoriesChildList.get(k);

                            // Label(pageCount)|id
                            dataToShow = lstDocTypeChild.getTypeName() + " (" + lstDocTypeChild.getPageCount() + ")" + "|" + lstDocTypeChild.getTypeId();

                            confidentialState = lstDocTypeChild.getConfidentialState();

                            //-------
                            ArrowExpandSelectableHeaderHolder lstDocTypeChildSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, (j == 0 && k == 0), lstFileLeftPadding, false, confidentialState, false);
                            lstDocTypeChildSelectableHeaderHolder.setOnlyOneNodeExpanded(false);
                            lstDocTypeChildSelectableHeaderHolder.setNodeValueColor(textColor);

                            //---- To bold clicked text in tree
                            // if (lstDocTypeChild.getTypeName().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                            if (lstDocTypeChild.getTypeName().equalsIgnoreCase(mPreviousClickedTreeElement.get(lstDocTypeChild.getRecordDetailId())))
                                lstDocTypeChildSelectableHeaderHolder.setTreeLabelBold(true);

                            TreeNode lstDocTypeChildFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstDocTypeChild, i))
                                    .setViewHolder(lstDocTypeChildSelectableHeaderHolder);
                            //-------
                            lstDocCategoryObjectFolder.addChildren(lstDocTypeChildFolder);
                        }
                        lstDateFolderTypeObjectFolder.addChildren(lstDocCategoryObjectFolder);
                    }
                    archiveDatumObjectFolder.addChildren(lstDateFolderTypeObjectFolder);
                }
            }


            if (archiveDatumObjectFolder != null) {
                if (!archiveDatumObjectFolder.getChildren().isEmpty())
                    treeRoot.addChildren(archiveDatumObjectFolder);
            }
        }

        AndroidTreeView mAndroidTreeView = new AndroidTreeView(this, treeRoot);
        mAndroidTreeView.setDefaultAnimation(false);
        mAndroidTreeView.setUse2dScroll(false);
        mAndroidTreeView.setDefaultNodeClickListener(this);
        mAndroidTreeView.setUseAutoToggle(false);
        mFileTypeOneTreeViewContainer.addView(mAndroidTreeView.getView());
        isTreeCreated = true;
    }

    @Override
    public void onLayerDrawn(PDFView pdfView, Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

        if (isCompareChecked) {

            if (pdfView.getCurrentXOffset() == mCurrentXOffset && pdfView.getCurrentYOffset() == mCurrentYOffset) {
                Log.d(TAG + " onLayerDrawnSame", displayedPage + " " + pdfView.getCurrentXOffset() + " " + pdfView.getCurrentYOffset());
            } else {

                Log.d(TAG + " onLayerDrawnDifferent", displayedPage + " " + pdfView.getCurrentXOffset() + " " + pdfView.getCurrentYOffset());

                if (isFirstPdf && mFirstPdfView == pdfView) {
                    mSecondPdfView.jumpTo(displayedPage);

                    mSecondPdfView.zoomWithAnimation(mFirstPdfView.getZoom());
                    mSecondPdfView.moveTo(mFirstPdfView.getCurrentXOffset(), mFirstPdfView.getCurrentYOffset());

                } else if (!isFirstPdf && mSecondPdfView == pdfView) {
                    mFirstPdfView.jumpTo(displayedPage);
                    mFirstPdfView.zoomWithAnimation(mSecondPdfView.getZoom());
                    mFirstPdfView.moveTo(mSecondPdfView.getCurrentXOffset(), mSecondPdfView.getCurrentYOffset());
                }
            }

            mCurrentXOffset = pdfView.getCurrentXOffset();
            mCurrentYOffset = pdfView.getCurrentYOffset();

        }
    }

    @Override
    public void onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP)
            isFirstPdf = v == mFirstPdfView;
    }

    @Override
    public void onError(PDFView pdfView, Throwable t) {
        if (mFirstPdfView == pdfView) {
            mMessageForFirstFile.setVisibility(View.VISIBLE);
            mMessageForFirstFile.setText(getString(R.string.filenotfound));
            mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
        } else if (mSecondPdfView == pdfView) {
            mMessageForSecondFile.setVisibility(View.VISIBLE);
            mMessageForSecondFile.setText(getString(R.string.filenotfound));
            mSecondFileTypeProgressDialogLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void loadComplete(PDFView pdfView, int nbPages) {
        if (pdfView == mFirstPdfView) {
            mMessageForFirstFile.setVisibility(View.GONE);
            mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
        }

        if (pdfView == mSecondPdfView) {
            mMessageForSecondFile.setVisibility(View.GONE);
            mSecondFileTypeProgressDialogLayout.setVisibility(View.GONE);

        }

        setPDFScale(getResources().getConfiguration());
    }

    // End

    //-- For treeview annotations
    @Override
    public void onClick(TreeNode node, Object value, View nodeView) {
        //---To invisible pdf error message pdf view
        mMessageForFirstFile.setVisibility(View.GONE);
        mMessageForSecondFile.setVisibility(View.GONE);
        //---

        if (value instanceof ArrowExpandIconTreeItemHolder.IconTreeItem) {
            ArrowExpandIconTreeItemHolder.IconTreeItem value1 = (ArrowExpandIconTreeItemHolder.IconTreeItem) value;

            //----- THIS IS TO FIND OUT, WHICH level of treeView clicked
            // mClickedTreeStructureLevel = value1.level;
            if (value1.objectData instanceof LstDocType) {
                LstDocType clickedLstDocTypeElement = (LstDocType) value1.objectData;
                doClickedOperationOnTreeItem(clickedLstDocTypeElement);
                mDrawer.closeDrawer(GravityCompat.END);
            } else if (value1.objectData instanceof LstDateFileType) {
                LstDateFileType clickedLstDocTypeElement = (LstDateFileType) value1.objectData;
                LstDocType tempClickedLstDocTypeElement = new LstDocType();
                tempClickedLstDocTypeElement.setTypeId(clickedLstDocTypeElement.getTypeId());
                tempClickedLstDocTypeElement.setTypeName(clickedLstDocTypeElement.getTypeName());
                tempClickedLstDocTypeElement.setPageCount(clickedLstDocTypeElement.getPageCount());
                tempClickedLstDocTypeElement.setRecordId(clickedLstDocTypeElement.getRecordId());
                tempClickedLstDocTypeElement.setRecordDetailId(clickedLstDocTypeElement.getRecordDetailId());
                tempClickedLstDocTypeElement.setPermission(clickedLstDocTypeElement.getPermission());
                tempClickedLstDocTypeElement.setConfidentialState(clickedLstDocTypeElement.getConfidentialState());
                doClickedOperationOnTreeItem(tempClickedLstDocTypeElement);
                mDrawer.closeDrawer(GravityCompat.END);
            } else if (value1.objectData instanceof LstHideDocType) {
                LstHideDocType hideDocType = (LstHideDocType) value1.objectData;
                LstDocType tempClickedLstDocTypeElement = new LstDocType();
                tempClickedLstDocTypeElement.setTypeId(hideDocType.getTypeId());
                tempClickedLstDocTypeElement.setTypeName(hideDocType.getTypeName());
                tempClickedLstDocTypeElement.setPageCount(hideDocType.getPageCount());
                tempClickedLstDocTypeElement.setRecordId(hideDocType.getRecordId());
                tempClickedLstDocTypeElement.setRecordDetailId(hideDocType.getRecordDetailId());
//                tempClickedLstDocTypeElement.setPermission(hideDocType.getPermission());
                tempClickedLstDocTypeElement.setConfidentialState(hideDocType.getConfidentialState());
                doClickedOperationOnTreeItem(tempClickedLstDocTypeElement);
                mDrawer.closeDrawer(GravityCompat.END);
            }
        }
    }

    private void doClickedOperationOnTreeItem(LstDocType clickedLstDocTypeElement) {
        //----- Get Object of clicked Element and create map to send  : Start------

        //----------
        GetEncryptedPDFRequestModel getEncryptedPDFRequestModel = new GetEncryptedPDFRequestModel();
        getEncryptedPDFRequestModel.setRecordId(String.valueOf(clickedLstDocTypeElement.getRecordId()));
        getEncryptedPDFRequestModel.getLstDocTypes().add(clickedLstDocTypeElement);
        //----------
        if (mOpenCompareDialogSwitch.isChecked()) {
            if (mGetEncryptedPDFRequestModelList.size() == 2) {
                expandCompareDialog();
                CommonMethods.showToast(this, "Can not compare more than 2 PDFs");
            } else {

                if (mPreviousClickedTreeElement.containsKey(clickedLstDocTypeElement.getRecordDetailId())) {
                    CommonMethods.showToast(this, "Can not compare same PDFs");

                } else {

                    mGetEncryptedPDFRequestModelList.add(getEncryptedPDFRequestModel);
                    mPreviousClickedTreeElement.put(clickedLstDocTypeElement.getRecordDetailId(), clickedLstDocTypeElement.getTypeName().trim());

                    ArrayList<String> tempClickedElements = new ArrayList<>(mPreviousClickedTreeElement.values());

                    expandCompareDialog();
                    switch (mGetEncryptedPDFRequestModelList.size()) {
                        case 1:
                            //-----
                            mFileOnePatientID.setText(getString(R.string.patient_id) + respectivePatientID);
                            mFileOneFileName.setText(getString(R.string.file) + tempClickedElements.get(0));
                            mFirstFileTypeProgressDialogLayout.setVisibility(View.VISIBLE);
                            labelFirstPdf.setText(tempClickedElements.get(0));
                            break;
                        case 2:
                            //-----
                            mFileOnePatientID.setText(getString(R.string.patient_id) + respectivePatientID);
                            mFileOneFileName.setText(getString(R.string.file) + tempClickedElements.get(0));
                            mFirstFileTypeProgressDialogLayout.setVisibility(View.VISIBLE);
                            labelFirstPdf.setText(tempClickedElements.get(0));
                            //-------
                            mFileTwoPatientID.setText(getString(R.string.patient_id) + respectivePatientID);
                            mFileTwoFileName.setText(getString(R.string.file) + tempClickedElements.get(1));
                            mSecondFileTypePdfViewLayout.setVisibility(View.VISIBLE);

                            mSecondFileTypeProgressDialogLayout.setVisibility(View.VISIBLE);
                            labelSecondPdf.setText(tempClickedElements.get(1));
                            break;
                    }
                }
            }

        } else {

            mGetEncryptedPDFRequestModelList.clear();
            mGetEncryptedPDFRequestModelList.add(getEncryptedPDFRequestModel);

            mPreviousClickedTreeElement.clear();
            mPreviousClickedTreeElement.put(clickedLstDocTypeElement.getRecordDetailId(), clickedLstDocTypeElement.getTypeName().trim());
            labelFirstPdf.setText(clickedLstDocTypeElement.getTypeName());
            mFirstFileTypeProgressDialogLayout.setVisibility(View.VISIBLE);
            //--------
            mSecondFileTypePdfViewLayout.setVisibility(View.GONE);
            mSecondFileTypeProgressDialogLayout.setVisibility(View.GONE);

            //--------
            mPatientsHelper.getPdfData(mGetEncryptedPDFRequestModelList.get(0), DMSConstants.TASK_GET_PDF_DATA + "_0");
        }

        //----- Get Object of clicked Element and create map to send  : End------
    }


    private void loadPDFFromServer(String pdfFileURL, final PDFView pdfViewToLoad) {
        String baseUrl = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext);
        if (pdfFileURL.contains(",")) {
            String[] separated = pdfFileURL.split(",");
            pdfFileURL = separated[0];
        }
        // this will contain PDF Path
        pdfFileURL = baseUrl + pdfFileURL.replace("~", "").trim();
        CommonMethods.Log(TAG, "PDF URL:==-->> " + pdfFileURL);

        final String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDFViewCache/";
        int index = pdfFileURL.lastIndexOf("/");
        String fileName = pdfFileURL.substring(index);
        final File file = new File(SDPath, fileName);
        Log.e("PDFVIEW", "LOAD_FROM_URL:_file.name():" + file.getName() + "|file.exists():>" + file.exists());
        if (file.exists()) {
            loadFile(pdfViewToLoad, file);
        } else {
            final String finalPdfFileURL = pdfFileURL;
            try {
                DownloadUtil.get().download(pdfFileURL, SDPath, new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess(final File file) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadFile(pdfViewToLoad, file);
                            }
                        });
                    }

                    @Override
                    public void onDownloading(int progress) {

                    }

                    @Override
                    public void onDownloadFailed() {
                        loadPDFFromServer(finalPdfFileURL, pdfViewToLoad);
                    }
                });
            } catch (Exception e) {
                e.fillInStackTrace();
                Toast.makeText(this, pdfFileURL, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void loadFile(PDFView pdfViewToLoad, File file) {
        CommonMethods.Log(TAG, "PDF URL1112222:==-->> " + file);

        Integer mPageNumber = 0;
        if (pdfViewToLoad == mFirstPdfView) {
            mFirstPdfView.fromFile(file)
                    .defaultPage(mPageNumber)
                    .onError(this)
                    .onDraw(this)
                    .onLoad(this)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .load();
            mFirstPdfView.zoomTo(DEFAULT_MIN_SCALE);

        } else if (pdfViewToLoad == mSecondPdfView) {
            mSecondPdfView.fromFile(file)
                    .defaultPage(mPageNumber)
                    .onError(this)
                    .onDraw(this)
                    .onLoad(this)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .load();
            mSecondPdfView.zoomTo(DEFAULT_MIN_SCALE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_WRITE_FILE_ONE_PERMISSIONS:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    loadPDFFromServer(fileOneData, mFirstPdfView);
                } else {
                    // Permission Denied
                    CommonMethods.showToast(mContext, getString(R.string.denied_permission_read_document));
                }
                break;

            case REQUEST_CODE_WRITE_FILE_TWO_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    // Permission Granted
                    loadPDFFromServer(fileTwoData, mSecondPdfView);
                else
                    // Permission Denied
                    CommonMethods.showToast(mContext, getString(R.string.denied_permission_read_document));

                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void doCallPDFDataService(String filePathToFetch, String mTagID) {
        //-----TO grayed out pdfview based on no element in that view -----
        if (String.valueOf(mTagID).endsWith("0")) {
            mFirstPdfView.setVisibility(View.VISIBLE);
            mFirstFileTypeProgressDialogLayout.setVisibility(View.VISIBLE);
            mFirstFileTypePdfViewLayout.setVisibility(View.VISIBLE);
            mFirstFileTypePdfViewLayout.setBackgroundResource(R.drawable.pdfdecoration);
            mFirstPdfView.zoomTo(DEFAULT_MIN_SCALE);
        } else {
            mSecondPdfView.setVisibility(View.VISIBLE);
            mSecondFileTypeProgressDialogLayout.setVisibility(View.VISIBLE);
            mSecondFileTypePdfViewLayout.setVisibility(View.VISIBLE);
            mSecondFileTypePdfViewLayout.setBackgroundResource(R.drawable.pdfdecoration);
            mSecondPdfView.zoomTo(DEFAULT_MIN_SCALE);
        }
        doValidateReceivedEncryptedFilePath(filePathToFetch, mTagID);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int width = getResources().getDisplayMetrics().widthPixels / 2;
        super.onConfigurationChanged(newConfig);
        ViewGroup.LayoutParams layoutParams = mRightNavigationView.getLayoutParams();
        layoutParams.width = width;
        mRightNavigationView.setLayoutParams(layoutParams);
        setPDFScale(newConfig);
    }

    private void setPDFScale(Configuration newConfig) {
        if (mSecondFileTypePdfViewLayout.getVisibility() == View.VISIBLE) {
            // Checks the orientation of the screen
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mFirstPdfView.zoomTo(5.0f);
                mSecondPdfView.zoomTo(5.0f);
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                mFirstPdfView.zoomTo(DEFAULT_MIN_SCALE);
                mSecondPdfView.zoomTo(DEFAULT_MIN_SCALE);
            }
        } else {
            // Checks the orientation of the screen
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mFirstPdfView.zoomTo(2.5f);
                mSecondPdfView.zoomTo(2.5f);
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                mFirstPdfView.zoomTo(DEFAULT_MIN_SCALE);
                mSecondPdfView.zoomTo(DEFAULT_MIN_SCALE);
            }
        }

        mFirstPdfView.moveTo(mFirstPdfView.getCurrentXOffset(), mFirstPdfView.getCurrentYOffset());
        mSecondPdfView.moveTo(mSecondPdfView.getCurrentXOffset(), mSecondPdfView.getCurrentYOffset());
    }

    private void mArchivedPreferenceSpinnerListener() {

        final String[] array = getResources().getStringArray(R.array.get_archived_preference_list);
        int[] prefImageList = new int[]{R.drawable.ic_pref_folder, R.drawable.ic_pref_date, R.drawable.ic_pref_file};

        CustomPreferenceSpinAdapter preferenceSpinAdapter = new CustomPreferenceSpinAdapter(this, array, prefImageList);
        mArchivedPreferenceSpinner.setAdapter(preferenceSpinAdapter);
        mArchivedPreferenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (array[position].equals("My Order"))
                    mArchivedSelectedPreference = "Order";
                else
                    mArchivedSelectedPreference = array[position];

                mFileTreeResponseData = null;
                getArchivedPageNumber = 1;
                archiveCount = 0;
                doCreateTreeStructure();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void mArchivedPreferenceSpinnerListenerForEpisode() {
        final String[] array = getResources().getStringArray(R.array.get_archived_preference_list_episode);
        int[] prefImageList = new int[]{R.drawable.ic_pref_folder, R.drawable.ic_pref_file};

        CustomPreferenceSpinAdapter preferenceSpinAdapter = new CustomPreferenceSpinAdapter(this, array, prefImageList);
        mArchivedPreferenceSpinner.setAdapter(preferenceSpinAdapter);
        mArchivedPreferenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (array[position].equals("My Order"))
                    mArchivedSelectedPreference = "Order";
                else
                    mArchivedSelectedPreference = array[position];

                mFileTreeResponseData = null;
                getArchivedPageNumber = 1;
                archiveCount = 0;
                doCreateTreeStructure();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void doCreateTreeStructure() {

        if (respectiveRecordID != null) {
//            mArchivedSelectedPreference = DMSConstants.ArchivedPreference.DATE;
//            mArchivedPreferenceSpinner.setVisibility(View.GONE);
//            mPreferenceLayout.setVisibility(View.GONE);
        }

        if (mFileTreeResponseData == null)
            getLoadArchivedList();
        else {
            createAnnotationTree(mFileTreeResponseData, false);
            if (mFileTreeResponseData.isPagination()) {
                loadedArchiveDataMessage.setText("" + currentCount + " " + getString(R.string.records_more));
            } else {
                loadedArchiveDataMessage.setText("" + archiveCount + " " + getString(R.string.records));
            }

            if (getArchivedPageNumber == 1) {
                if (mFileTreeResponseData.isPagination()) {
                    mLoadNextArchiveDataList.setEnabled(true);
                }
            } else if (getArchivedPageNumber > 1) {
                if (mFileTreeResponseData.isPagination()) {
                    mLoadNextArchiveDataList.setEnabled(true);
                    mLoadPreviousArchiveDataList.setEnabled(true);
                } else {
                    mLoadPreviousArchiveDataList.setEnabled(true);
                }
            }

        }

    }


    @Override
    public void onLockIconClick(Object value, boolean isLeaf) {


        if (value instanceof ArrowExpandIconTreeItemHolder.IconTreeItem) {
            ArrowExpandIconTreeItemHolder.IconTreeItem value1 = (ArrowExpandIconTreeItemHolder.IconTreeItem) value;

            JSONArray jsonArrayCheckList = new JSONArray();
            ArrayList<String> arrayCheckList = new ArrayList<>();
            RaiseUnlockRequestModel unlockRequestModel = new RaiseUnlockRequestModel();

            if (value1.objectData instanceof LstDocType) {
                LstDocType clickedLstDocTypeElement = (LstDocType) value1.objectData;
                String val = clickedLstDocTypeElement.getFileTypeRefId() + "_" + clickedLstDocTypeElement.getTypeId() + "_" + clickedLstDocTypeElement.getRecordId();
                arrayCheckList.add(val);
                jsonArrayCheckList.put(val);
                String[] stringArray = arrayCheckList.toArray(new String[arrayCheckList.size()]);
                unlockRequestModel.setRequestTypeId("3");
                unlockRequestModel.setCheckList(stringArray);
                showDialogRaiseRequest(unlockRequestModel);
            } else if (value1.objectData instanceof LstDateFileType) {
                LstDateFileType clickedLstDocTypeElement = (LstDateFileType) value1.objectData;
                String val = clickedLstDocTypeElement.getFileTypeRefId() + "_" + clickedLstDocTypeElement.getTypeId() + "_" + clickedLstDocTypeElement.getRecordId();
                arrayCheckList.add(val);
                Log.e("CheckList", "--" + arrayCheckList);
                String[] stringArray = arrayCheckList.toArray(new String[arrayCheckList.size()]);
                unlockRequestModel.setRequestTypeId("3");
                unlockRequestModel.setCheckList(stringArray);
                showDialogRaiseRequest(unlockRequestModel);
            } else if (value1.objectData instanceof LstDocCategory) {
                LstDocCategory lstDocCategory = (LstDocCategory) value1.objectData;
                for (int i = 0; i < lstDocCategory.getLstDocTypes().size(); i++) {
                    LstDocType data = lstDocCategory.getLstDocTypes().get(i);
                    //FileTypeRefId_typeID_RecordID
                    String val = data.getFileTypeRefId() + "_" + data.getTypeId() + "_" + data.getRecordId();
                    arrayCheckList.add(val);
                }
                Log.e("CheckList", "--" + arrayCheckList);
                String[] stringArray = arrayCheckList.toArray(new String[arrayCheckList.size()]);
                unlockRequestModel.setRequestTypeId("3");
                unlockRequestModel.setCheckList(stringArray);
                showDialogRaiseRequest(unlockRequestModel);
            } else if (value1.objectData instanceof LstHideDocType) {
                LstHideDocType lstHideDocType = (LstHideDocType) value1.objectData;

                //FileTypeRefId_typeID_RecordID
                String val = lstHideDocType.getFileTypeRefId() + "_" + lstHideDocType.getTypeId() + "_" + lstHideDocType.getRecordId();
                arrayCheckList.add(val);

                Log.e("CheckList", "--" + arrayCheckList);
                String[] stringArray = arrayCheckList.toArray(new String[arrayCheckList.size()]);
                unlockRequestModel.setRequestTypeId("3");
                unlockRequestModel.setCheckList(stringArray);
                showDialogRaiseRequest(unlockRequestModel);
            }


        }

    }


    private void showDialogRaiseRequest(final RaiseUnlockRequestModel unlockRequestModel) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_alert);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        ((TextView) dialog.findViewById(R.id.textview_sucess)).setText(getResources().getString(R.string.do_you_want_to_unlock));

        float[] bottomLeftRadius = {0, 0, 0, 0, getResources().getDimension(R.dimen.dp8), getResources().getDimension(R.dimen.dp8), 0, 0};
        float[] bottomRightRadius = {0, 0, 0, 0, 0, 0, getResources().getDimension(R.dimen.dp8), getResources().getDimension(R.dimen.dp8)};
        //getResources().getDimension(R.dimen.dp8)
        GradientDrawable buttonLeftBackground = new GradientDrawable();
        buttonLeftBackground.setShape(GradientDrawable.RECTANGLE);
        buttonLeftBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonLeftBackground.setCornerRadii(bottomRightRadius);

        GradientDrawable buttonRightBackground = new GradientDrawable();
        buttonRightBackground.setShape(GradientDrawable.RECTANGLE);
        buttonRightBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonRightBackground.setCornerRadii(bottomLeftRadius);

        Button buttonRight = dialog.findViewById(R.id.button_cancel);
        Button buttonLeft = dialog.findViewById(R.id.button_ok);
        ImageView dialogIcon = dialog.findViewById(R.id.dialogIcon);
        dialogIcon.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        buttonLeft.setBackground(buttonLeftBackground);
        buttonRight.setBackground(buttonRightBackground);

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mPatientsHelper.raiseUnlockRequestArchivedFile(unlockRequestModel);

            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }
}
