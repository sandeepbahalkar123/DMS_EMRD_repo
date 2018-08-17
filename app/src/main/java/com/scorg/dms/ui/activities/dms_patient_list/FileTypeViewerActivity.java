package com.scorg.dms.ui.activities.dms_patient_list;


//----------- IMPLEMENTED CODE : 16 AUGUST 2018 with differnt pdfview, lenthy code

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.scorg.dms.R;
import com.scorg.dms.helpers.patient_list.DMSPatientsHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.Common;
import com.scorg.dms.model.dms_models.requestmodel.archive.GetArchiveRequestModel;
import com.scorg.dms.model.dms_models.requestmodel.showfile_data.GetEncryptedPDFRequestModel;
import com.scorg.dms.model.dms_models.responsemodel.episode_list.PatientEpisodeFileData;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.ArchiveDatum;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.FileTreeResponseData;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.FileTreeResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.LstDateFileType;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.LstDateFolderType;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.LstDocCategory;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.LstDocType;
import com.scorg.dms.model.dms_models.responsemodel.getpdfdataresponsemodel.GetPdfDataResponseModel;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.singleton.Device;
import com.scorg.dms.ui.activities.ProfileActivity;
import com.scorg.dms.ui.customesViews.treeViewHolder.arrow_expand.ArrowExpandIconTreeItemHolder;
import com.scorg.dms.ui.customesViews.treeViewHolder.arrow_expand.ArrowExpandSelectableHeaderHolder;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;
import com.shockwave.pdfium.PdfDocument;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.github.barteksc.pdfviewer.PDFView.DEFAULT_MID_SCALE;
import static com.github.barteksc.pdfviewer.PDFView.DEFAULT_MIN_SCALE;

/**
 * Created by jeetal on 14/3/17.
 */


public class FileTypeViewerActivity extends AppCompatActivity implements HelperResponse, OnLoadCompleteListener, OnErrorListener, OnDrawListener, TreeNode.TreeNodeClickListener {

    private static final long ANIMATION_DURATION = 500; // in milliseconds

    private static final String TAG = FileTypeViewerActivity.class.getName();
    private static final int REQUEST_CODE_WRITE_FILE_ONE_PERMISSIONS = 101;
    private static final int REQUEST_CODE_WRITE_FILE_TWO_PERMISSIONS = 102;

    private Integer mPageNumber = 0;
    private boolean isFirstPdf = true;
    private float mCurrentXOffset = -1;
    private float mCurrentYOffset = -1;
    // End

    private Context mContext;


    //------
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    //------compare dialog box obj initalize
    @BindView(R.id.compareDialogParentLayout)
    LinearLayout mCompareDialogParentLayout;
    @BindView(R.id.compareDialogSwitch)
    Switch mOpenCompareDialogSwitch;
    @BindView(R.id.compareDialog)
    RelativeLayout mCompareDialogLayout;
    @BindView(R.id.fileOneRemoveButton)
    AppCompatImageView mFileOneRemoveButton;
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
    //---------------------------------------


    @BindView(R.id.archivedPreferenceSpinner)
    Spinner mArchivedPreferenceSpinner;
    @BindView(R.id.preferenceLayout)
    LinearLayout mPreferenceLayout;

    //---------
    @BindView(R.id.firstPdfView)
    PDFView mFirstPdfView;
    @BindView(R.id.secondPdfView)
    PDFView mSecondPdfView;

    @BindView(R.id.firstPdfViewFrameLayout)
    FrameLayout mFirstFileTypePdfViewLayout;
    @BindView(R.id.secondPdfViewFrameLayout)
    FrameLayout mSecondFileTypePdfViewLayout;
    //---------

    @BindView(R.id.messageForFirstFile)
    TextView mMessageForFirstFile;
    @BindView(R.id.messageForSecondFile)
    TextView mMessageForSecondFile;
    @BindView(R.id.openRightDrawer)
    ImageView mOpenRightDrawer;
    // End


    @BindView(R.id.fileTypeOneDoctorName)
    TextView mDoctorNameOne;
    @BindView(R.id.fileTypeTwoDoctorName)
    TextView mDoctorNameTwo;
    @BindView(R.id.tvPatientLocation)
    TextView mPatientAddress;

    @BindView(R.id.fileTypeOneRefID)
    TextView mFileOneRefId;
    @BindView(R.id.fileTypeTwoRefID)
    TextView mFileTwoRefId;
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
    //----------
    @BindView(R.id.fileTypeTreeViewContainer)
    RelativeLayout mFileTypeOneTreeViewContainer;
    @BindView(R.id.loadPreviousArchiveDataList)
    AppCompatImageButton mLoadPreviousArchiveDataList;
    @BindView(R.id.loadNextArchiveDataList)
    AppCompatImageButton mLoadNextArchiveDataList;
    @BindView(R.id.loadedArchiveDataMessage)
    TextView loadedArchiveDataMessage;
    //----------
    @BindView(R.id.et_uhid)
    Switch mCompareSwitch;
    @BindView(R.id.rowScrollBoth)
    TableRow mRowScrollBoth;

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

    @BindView(R.id.imageCloseDrawer)
    AppCompatImageButton imageCloseDrawer;


    DrawerLayout mDrawer;

    private DMSPatientsHelper mPatientsHelper;

    private boolean isCompareChecked = true;
    //---------
    ArrayList<PatientEpisodeFileData> mSelectedFileTypeDataToCompare;
    String respectivePatientID;
    String respectiveRecordID;
    int getArchivedPageNumber = 1;
    String patientName;
    String doctorName;
    String patientAddress;
    //---------
    private FileTreeResponseData mFileTreeResponseData;

    private RelativeLayout mFirstFileTypeProgressDialogLayout;
    private RelativeLayout mSecondFileTypeProgressDialogLayout;
    private LinkedHashMap<String, String> mPreviousClickedTreeElement = null;
    private String fileOneData;
    private String fileTwoData;
    private boolean isComparingBetweenSameFileType = false;
    private String mLoadedPDFFileDataPath = null;
    private String mArchivedSelectedPreference = DMSConstants.ArchivedPreference.FOLDER;
    private boolean isCompareDialogCollapsed;

    private ArrayList<GetEncryptedPDFRequestModel> mGetEncryptedPDFRequestModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare_file_type_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {

        Bundle extra = getIntent().getBundleExtra(DMSConstants.DATA);

        if (extra != null) {
            mSelectedFileTypeDataToCompare = (ArrayList<PatientEpisodeFileData>) extra.getSerializable(getString(R.string.compare));
            respectivePatientID = extra.getString(DMSConstants.PATIENT_ID);
            respectiveRecordID = extra.getString(DMSConstants.RECORD_ID);
            patientName = extra.getString(DMSConstants.PATIENT_LIST_PARAMS.PATIENT_NAME);
            patientAddress = extra.getString(DMSConstants.PATIENT_ADDRESS);
            doctorName = extra.getString(DMSConstants.DOCTOR_NAME);
        }

        mContext = getApplicationContext();
        //-------------
        mPatientsHelper = new DMSPatientsHelper(this, this);
        mPreviousClickedTreeElement = new LinkedHashMap<>();

        bindView();

        //-----------
        mDrawer.openDrawer(GravityCompat.END);
        mArchivedPreferenceSpinnerListener();
       if(CommonMethods.isTablet(this))
           layoutCompareSwitch.setVisibility(View.VISIBLE);

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

        doBindHeaderViews();
        //-------Listeners-----

        mCompareSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCompareChecked = isChecked;
            }
        });
        mCompareSwitch.setChecked(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDFViewCache/");
                file.delete();
                finish();
            }
        });
        //-----------
        mFirstFileTypePdfViewLayout.addView(CommonMethods.loadView(R.layout.mydialog, this));
        mFirstFileTypeProgressDialogLayout = mFirstFileTypePdfViewLayout.findViewById(R.id.progressBarContainerLayout);
        mFirstFileTypeProgressDialogLayout.setVisibility(View.VISIBLE);
        mSecondFileTypePdfViewLayout.addView(CommonMethods.loadView(R.layout.mydialog, this));
        mSecondFileTypeProgressDialogLayout = mSecondFileTypePdfViewLayout.findViewById(R.id.progressBarContainerLayout);
        mSecondFileTypeProgressDialogLayout.setVisibility(View.VISIBLE);

        //------RightNavigationView initialize---------

        ViewGroup.LayoutParams layoutParams = mRightNavigationView.getLayoutParams();
        layoutParams.width = width;
        mRightNavigationView.setLayoutParams(layoutParams);

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
        mPatientAddress.setText(patientAddress);

    }

    @OnClick({R.id.imageCloseDrawer,R.id.openRightDrawer, R.id.loadPreviousArchiveDataList, R.id.loadNextArchiveDataList, R.id.compareButton, R.id.compareLabel, R.id.fileOneRemoveButton})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openRightDrawer:
                mDrawer.openDrawer(GravityCompat.END);
                doCreateTreeStructure();
                break;
            case R.id.loadPreviousArchiveDataList:
                getArchivedPageNumber = getArchivedPageNumber - 1;
                if (getArchivedPageNumber == 0) {
                    CommonMethods.showToast(this, "No Previous data to load");
                } else {
                    mFileTreeResponseData = null;
                    doCreateTreeStructure();
                }
                break;
            case R.id.loadNextArchiveDataList:
                if (!mFileTreeResponseData.isPagination()) {
                    CommonMethods.showToast(this, "No Next data to load");
                } else {
                    getArchivedPageNumber = getArchivedPageNumber + 1;
                    mFileTreeResponseData = null;
                    doCreateTreeStructure();
                }

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
                mPreviousClickedTreeElement.remove(fileToRemove);
                mFileOneFileName.setText("");
                mFileOnePatientID.setText("");

                break;
            case R.id.fileTwoRemoveButton:
                String fileTwoRemove = mFileTwoFileName.getText().toString().trim().replace(getString(R.string.file), "").trim();
                mPreviousClickedTreeElement.remove(fileTwoRemove);
                mFileTwoFileName.setText("");
                mFileTwoPatientID.setText("");
                break;
            case R.id.imageCloseDrawer:
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
        //---------------
        GetArchiveRequestModel model = new GetArchiveRequestModel();
        model.setPatId(respectivePatientID);
        //model.setPatId("" + 200156);
        //model.setPatId("" + 143369);
        model.setRecordId(respectiveRecordID);
        model.setPageNumber(getArchivedPageNumber);
        model.setPreference(mArchivedSelectedPreference);
        mPatientsHelper.doGetArchivedList(model);
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (String.valueOf(mOldDataTag).equalsIgnoreCase("" + DMSConstants.TASK_GET_ARCHIVED_LIST)) {
            FileTreeResponseModel fileTreeResponseModel = (FileTreeResponseModel) customResponse;
            mFileTreeResponseData = fileTreeResponseModel.getFileTreeResponseData();

            if (mFileTypeOne.getText().toString().equalsIgnoreCase(mFileTypeTwo.getText().toString())) {
                isComparingBetweenSameFileType = true;
            }

            doCreateTreeStructure();
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
        }
    }


    private void doValidateReceivedEncryptedFilePath(String filePath, String mOldDataTag) {
        //------

        //  String fileData = CommonMethods.decryptPDFFilePathUsingAESAlgo(filePath);
        String fileData = filePath;
        if (fileData != null) {
            if (mOldDataTag.endsWith("0")) {
                mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
                fileOneData = fileData;
                askWriteExtenralStoragePermission(REQUEST_CODE_WRITE_FILE_ONE_PERMISSIONS);
            } else if (mOldDataTag.endsWith("1")) {
                mSecondFileTypeProgressDialogLayout.setVisibility(View.GONE);
                fileTwoData = fileData;
                askWriteExtenralStoragePermission(REQUEST_CODE_WRITE_FILE_TWO_PERMISSIONS);
            }
        } else
            Toast.makeText(mContext, "Document not available", Toast.LENGTH_SHORT).show();


        //----

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


    private void createAnnotationTreeWithFolderPreferences(FileTreeResponseData fileTreeResponseData, boolean isExpanded) {

        mFileTypeOneTreeViewContainer.removeAllViews();

        TreeNode treeRoot = TreeNode.root();

        int lstDocCategoryObjectLeftPadding = (int) (getResources().getDimension(R.dimen.dp30) / getResources().getDisplayMetrics().density);
        int lstDocTypeChildLeftPadding = (int) (getResources().getDimension(R.dimen.dp50) / getResources().getDisplayMetrics().density);
        int lstDateFolderTypeChildLeftPadding = (int) (getResources().getDimension(R.dimen.dp100) / getResources().getDisplayMetrics().density);
        int textColor = ContextCompat.getColor(this, R.color.black);

        List<ArchiveDatum> archiveData = fileTreeResponseData.getArchiveData();
        int size = archiveData.size();

        // For archived data list
        for (int i = 0; i < size; i++) {
            ArchiveDatum archiveDatumObject = archiveData.get(i);

            ArrowExpandSelectableHeaderHolder selectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded);
            selectableHeaderHolder.setOnlyOneNodeExpanded(true);
            selectableHeaderHolder.setNodeValueColor(textColor);

            //---- To bold clicked text in tree
            //  if (archiveDatumObject.getFileType().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
            if (archiveDatumObject.getFileType().equalsIgnoreCase(mPreviousClickedTreeElement.get(archiveDatumObject.getFileType())))
                selectableHeaderHolder.setTreeLabelBold(true);

            // Label(pageCount)|NA
            String dataToShow = archiveDatumObject.getFileType() + " (" + archiveDatumObject.getTotalLstDateFolderTypePageCount() + ")" + "|NA";

            TreeNode archiveDatumObjectFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, archiveDatumObject, i))
                    .setViewHolder(selectableHeaderHolder);

            //---- For list lstDateFolderType loop
            List<LstDateFolderType> lstDateFolderTypeList = archiveDatumObject.getLstDateFolderTypeList();

            for (int l = 0; l < lstDateFolderTypeList.size(); l++) {
                LstDateFolderType lstDateFolderType = lstDateFolderTypeList.get(l);

                //-------NODE LstDateFolderType--------------
                // Label(pageCount)|id
                dataToShow = lstDateFolderType.getDateFolderType() + " (" + lstDateFolderType.getPageCount() + ")" + "|NA";
                ArrowExpandSelectableHeaderHolder lstDateFolderTypeSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded, lstDocTypeChildLeftPadding);
                lstDateFolderTypeSelectableHeaderHolder.setOnlyOneNodeExpanded(true);

                lstDateFolderTypeSelectableHeaderHolder.setNodeValueColor(textColor);

                //---- To bold clicked text in tree
                // if (lstDateFolderType.getDateFolderType().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                if (lstDateFolderType.getDateFolderType().equalsIgnoreCase(mPreviousClickedTreeElement.get(lstDateFolderType.getDateFolderType())))
                    lstDateFolderTypeSelectableHeaderHolder.setTreeLabelBold(true);

                TreeNode lstDateFolderTypeObjectFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstDateFolderType, i))
                        .setViewHolder(lstDateFolderTypeSelectableHeaderHolder);
                //---

                //--------------------

                //---- For list categories loop
                List<LstDocCategory> lstDocCategories = lstDateFolderType.getLstDocCategories();

                for (int j = 0; j < lstDocCategories.size(); j++) {
                    LstDocCategory lstDocCategoryObject = lstDocCategories.get(j);

                    // Label(pageCount)|id
                    dataToShow = lstDocCategoryObject.getCategoryName() + " (" + lstDocCategoryObject.getTotalDocTypePageCount() + ")" + "|" + lstDocCategoryObject.getCategoryId();
                    ArrowExpandSelectableHeaderHolder docCatSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded, lstDateFolderTypeChildLeftPadding);
                    docCatSelectableHeaderHolder.setOnlyOneNodeExpanded(true);

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

                        //-------
                        ArrowExpandSelectableHeaderHolder lstDocTypeChildSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded, lstDateFolderTypeChildLeftPadding);
                        lstDocTypeChildSelectableHeaderHolder.setOnlyOneNodeExpanded(true);
                        lstDocTypeChildSelectableHeaderHolder.setNodeValueColor(textColor);

                        //---- To bold clicked text in tree
                        // if (lstDocTypeChild.getTypeName().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                        if (lstDocTypeChild.getTypeName().equalsIgnoreCase(mPreviousClickedTreeElement.get(lstDocTypeChild.getTypeName())))
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
            treeRoot.addChildren(archiveDatumObjectFolder);
        }

        AndroidTreeView mAndroidTreeView = new AndroidTreeView(this, treeRoot);
        mAndroidTreeView.setDefaultAnimation(true);
        mAndroidTreeView.setUse2dScroll(true);
        mAndroidTreeView.setDefaultNodeClickListener(this);
        mAndroidTreeView.setUseAutoToggle(false);
        mFileTypeOneTreeViewContainer.addView(mAndroidTreeView.getView());

    }

    private void createAnnotationTreeWithDatePreferences(FileTreeResponseData fileTreeResponseData, boolean isExpanded) {

        mFileTypeOneTreeViewContainer.removeAllViews();

        TreeNode treeRoot = TreeNode.root();

        int lstDocCategoryObjectLeftPadding = (int) (getResources().getDimension(R.dimen.dp30) / getResources().getDisplayMetrics().density);
        int lstDocTypeChildLeftPadding = (int) (getResources().getDimension(R.dimen.dp50) / getResources().getDisplayMetrics().density);
        int lstDateFolderTypeChildLeftPadding = (int) (getResources().getDimension(R.dimen.dp100) / getResources().getDisplayMetrics().density);
        int textColor = ContextCompat.getColor(this, R.color.black);

        List<ArchiveDatum> archiveData = fileTreeResponseData.getArchiveData();
        int size = archiveData.size();

        // For archived data list
        for (int i = 0; i < size; i++) {
            ArchiveDatum archiveDatumObject = archiveData.get(i);

            ArrowExpandSelectableHeaderHolder selectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded);
            selectableHeaderHolder.setOnlyOneNodeExpanded(true);
            selectableHeaderHolder.setNodeValueColor(textColor);

            //---- To bold clicked text in tree
            //  if (archiveDatumObject.getFileType().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
            if (archiveDatumObject.getFileType().equalsIgnoreCase(mPreviousClickedTreeElement.get(archiveDatumObject.getFileType())))
                selectableHeaderHolder.setTreeLabelBold(true);

            // Label(pageCount)|NA
            String dataToShow = archiveDatumObject.getFileType() + " (" + archiveDatumObject.getTotalArchiveDataLstDocCategoriesPageCount() + ")" + "|NA";

            TreeNode archiveDatumObjectFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, archiveDatumObject, i))
                    .setViewHolder(selectableHeaderHolder);

            //---- For list lstDateFolderType loop
            List<LstDocCategory> archiveDataLstDocCategoriesList = archiveDatumObject.getArchiveDataLstDocCategories();

            for (int l = 0; l < archiveDataLstDocCategoriesList.size(); l++) {
                LstDocCategory lstDocCategory = archiveDataLstDocCategoriesList.get(l);

                //-------NODE LstDateFolderType--------------
                // Label(pageCount)|id
                dataToShow = lstDocCategory.getCategoryName() + " (" + lstDocCategory.getPageCount() + ")" + "|NA";
                ArrowExpandSelectableHeaderHolder lstDateFolderTypeSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded, lstDocTypeChildLeftPadding);
                lstDateFolderTypeSelectableHeaderHolder.setOnlyOneNodeExpanded(true);

                lstDateFolderTypeSelectableHeaderHolder.setNodeValueColor(textColor);

                //---- To bold clicked text in tree
                // if (lstDateFolderType.getDateFolderType().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                if (lstDocCategory.getCategoryName().equalsIgnoreCase(mPreviousClickedTreeElement.get(lstDocCategory.getCategoryName())))
                    lstDateFolderTypeSelectableHeaderHolder.setTreeLabelBold(true);

                TreeNode lstDateFolderTypeObjectFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstDocCategory, i))
                        .setViewHolder(lstDateFolderTypeSelectableHeaderHolder);
                //---

                //--------------------

                //for lstDocTypes loop
                List<LstDocType> lstDocTypesCategoriesChildList = lstDocCategory.getLstDocTypes();
                for (int k = 0; k < lstDocTypesCategoriesChildList.size(); k++) {
                    LstDocType lstDocTypeChild = lstDocTypesCategoriesChildList.get(k);

                    // Label(pageCount)|id
                    dataToShow = lstDocTypeChild.getTypeName() + " (" + lstDocTypeChild.getPageCount() + ")" + "|" + lstDocTypeChild.getTypeId();

                    //-------
                    ArrowExpandSelectableHeaderHolder lstDocTypeChildSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded, lstDateFolderTypeChildLeftPadding);
                    lstDocTypeChildSelectableHeaderHolder.setOnlyOneNodeExpanded(true);
                    lstDocTypeChildSelectableHeaderHolder.setNodeValueColor(textColor);

                    //---- To bold clicked text in tree
                    // if (lstDocTypeChild.getTypeName().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                    if (lstDocTypeChild.getTypeName().equalsIgnoreCase(mPreviousClickedTreeElement.get(lstDocTypeChild.getTypeName())))
                        lstDocTypeChildSelectableHeaderHolder.setTreeLabelBold(true);

                    TreeNode lstDocTypeChildFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstDocTypeChild, i))
                            .setViewHolder(lstDocTypeChildSelectableHeaderHolder);
                    //-------
                    lstDateFolderTypeObjectFolder.addChildren(lstDocTypeChildFolder);
                }
                archiveDatumObjectFolder.addChildren(lstDateFolderTypeObjectFolder);
            }
            treeRoot.addChildren(archiveDatumObjectFolder);
        }

        AndroidTreeView mAndroidTreeView = new AndroidTreeView(this, treeRoot);
        mAndroidTreeView.setDefaultAnimation(true);
        mAndroidTreeView.setUse2dScroll(true);
        mAndroidTreeView.setDefaultNodeClickListener(this);
        mAndroidTreeView.setUseAutoToggle(false);
        mFileTypeOneTreeViewContainer.addView(mAndroidTreeView.getView());

    }

    private void createAnnotationTreeWithFilePreferences(FileTreeResponseData fileTreeResponseData, boolean isExpanded) {

        mFileTypeOneTreeViewContainer.removeAllViews();

        TreeNode treeRoot = TreeNode.root();

        int lstDocCategoryObjectLeftPadding = (int) (getResources().getDimension(R.dimen.dp30) / getResources().getDisplayMetrics().density);
        int lstDocTypeChildLeftPadding = (int) (getResources().getDimension(R.dimen.dp50) / getResources().getDisplayMetrics().density);
        int lstDateFolderTypeChildLeftPadding = (int) (getResources().getDimension(R.dimen.dp100) / getResources().getDisplayMetrics().density);
        int textColor = ContextCompat.getColor(this, R.color.black);

        List<ArchiveDatum> archiveData = fileTreeResponseData.getArchiveData();
        int size = archiveData.size();

        // For archived data list
        for (int i = 0; i < size; i++) {
            ArchiveDatum archiveDatumObject = archiveData.get(i);

            ArrowExpandSelectableHeaderHolder selectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded);
            selectableHeaderHolder.setOnlyOneNodeExpanded(true);
            selectableHeaderHolder.setNodeValueColor(textColor);

            //---- To bold clicked text in tree
            //  if (archiveDatumObject.getFileType().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
            if (archiveDatumObject.getFileType().equalsIgnoreCase(mPreviousClickedTreeElement.get(archiveDatumObject.getFileType())))
                selectableHeaderHolder.setTreeLabelBold(true);

            // Label(pageCount)|NA
            String dataToShow = archiveDatumObject.getFileType() + " (" + archiveDatumObject.getTotalArchiveDataLstDocCategoriesPageCount() + ")" + "|NA";

            TreeNode archiveDatumObjectFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, archiveDatumObject, i))
                    .setViewHolder(selectableHeaderHolder);

            //---- For list categories loop
            List<LstDocCategory> lstDocCategories = archiveDatumObject.getArchiveDataLstDocCategories();

            for (int j = 0; j < lstDocCategories.size(); j++) {
                LstDocCategory lstDocCategoryObject = lstDocCategories.get(j);

                // Label(pageCount)|id
                dataToShow = lstDocCategoryObject.getCategoryName() + " (" + lstDocCategoryObject.getTotalDocTypePageCount() + ")" + "|" + lstDocCategoryObject.getCategoryId();
                ArrowExpandSelectableHeaderHolder docCatSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded, lstDocTypeChildLeftPadding);
                docCatSelectableHeaderHolder.setOnlyOneNodeExpanded(true);

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

                    //-------
                    ArrowExpandSelectableHeaderHolder lstDocTypeChildSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded, lstDateFolderTypeChildLeftPadding);
                    lstDocTypeChildSelectableHeaderHolder.setOnlyOneNodeExpanded(true);
                    lstDocTypeChildSelectableHeaderHolder.setNodeValueColor(textColor);

                    //---- To bold clicked text in tree
                    // if (lstDocTypeChild.getTypeName().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                    if (lstDocTypeChild.getTypeName().equalsIgnoreCase(mPreviousClickedTreeElement.get(lstDocTypeChild.getTypeName())))
                        lstDocTypeChildSelectableHeaderHolder.setTreeLabelBold(true);

                    TreeNode lstDocTypeChildFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstDocTypeChild, i))
                            .setViewHolder(lstDocTypeChildSelectableHeaderHolder);
                    //-----------------------------------------------------------------
                    //for lstDocTypes loop
                    ArrayList<LstDateFileType> lstDateFileTypeList = lstDocTypeChild.getLstDateFileTypeList();
                    for (int l = 0; l < lstDateFileTypeList.size(); l++) {
                        LstDateFileType lstDateFileTypeLastChild = lstDateFileTypeList.get(l);

                        // Label(pageCount)|id
                        dataToShow = lstDateFileTypeLastChild.getTypeName() + " (" + lstDateFileTypeLastChild.getPageCount() + ")" + "|" + lstDateFileTypeLastChild.getTypeId();

                        //-------
                        ArrowExpandSelectableHeaderHolder lstDateFileTypeLastChildSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded, lstDateFolderTypeChildLeftPadding);
                        lstDateFileTypeLastChildSelectableHeaderHolder.setOnlyOneNodeExpanded(true);
                        lstDateFileTypeLastChildSelectableHeaderHolder.setNodeValueColor(textColor);

                        //---- To bold clicked text in tree
                        // if (lstDocTypeChild.getTypeName().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                        if (lstDateFileTypeLastChild.getTypeName().equalsIgnoreCase(mPreviousClickedTreeElement.get(lstDateFileTypeLastChild.getTypeName())))
                            lstDateFileTypeLastChildSelectableHeaderHolder.setTreeLabelBold(true);

                        TreeNode lstDateFileTypeLastChildSelectableHeaderFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstDateFileTypeLastChild, i))
                                .setViewHolder(lstDateFileTypeLastChildSelectableHeaderHolder);
                        //-------
                        lstDocTypeChildFolder.addChildren(lstDateFileTypeLastChildSelectableHeaderFolder);
                    }
                    //-----------------------------------------------------------------
                    lstDocCategoryObjectFolder.addChildren(lstDocTypeChildFolder);
                }
                archiveDatumObjectFolder.addChildren(lstDocCategoryObjectFolder);
            }

            treeRoot.addChildren(archiveDatumObjectFolder);
        }

        AndroidTreeView mAndroidTreeView = new AndroidTreeView(this, treeRoot);
        mAndroidTreeView.setDefaultAnimation(true);
        mAndroidTreeView.setUse2dScroll(true);
        mAndroidTreeView.setDefaultNodeClickListener(this);
        mAndroidTreeView.setUseAutoToggle(false);
        mFileTypeOneTreeViewContainer.addView(mAndroidTreeView.getView());

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
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
            if (v == mFirstPdfView) isFirstPdf = true;
            else
                isFirstPdf = false;
        }
    }

    @Override
    public void onError(PDFView pdfView, Throwable t) {
        if (mFirstPdfView == pdfView) {
            mMessageForFirstFile.setVisibility(View.VISIBLE);
            mMessageForFirstFile.setText(getString(R.string.filenotfound));
        } else if (mSecondPdfView == pdfView) {
            mMessageForSecondFile.setVisibility(View.VISIBLE);
            mMessageForSecondFile.setText(getString(R.string.filenotfound));
        }
    }

    @Override
    public void loadComplete(PDFView pdfView, int nbPages) {
        if (pdfView == mFirstPdfView)
            mMessageForFirstFile.setVisibility(View.GONE);

        if (pdfView == mSecondPdfView)
            mMessageForSecondFile.setVisibility(View.GONE);

        try {
            PdfDocument.Meta meta = null;
            if (pdfView == mFirstPdfView) {
                meta = mFirstPdfView.getDocumentMeta();
                printBookmarksTree(mFirstPdfView.getTableOfContents(), "-");
            } else if (pdfView == mSecondPdfView) {
                meta = mSecondPdfView.getDocumentMeta();
                printBookmarksTree(mSecondPdfView.getTableOfContents(), "-");
            }
            if (meta != null) {
                String dataToShow = "title = " + meta.getTitle()
                        + "author = " + meta.getAuthor()
                        + "subject = " + meta.getSubject()
                        + "keywords = " + meta.getKeywords()
                        + "creator = " + meta.getCreator()
                        + "producer = " + meta.getProducer()
                        + "creationDate = " + meta.getCreationDate()
                        + "modDate = " + meta.getModDate();
                CommonMethods.Log(TAG, "title = " + dataToShow);
            }
        } catch (Exception e) {
            e.getStackTrace();

        }
        setPDFScale(getResources().getConfiguration());
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {
            CommonMethods.Log(TAG, String.format("%s %s, p %d", Locale.US, sep, b.getTitle(), b.getPageIdx()));
            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    // End

    //-- For treeview annotations
    @Override
    public void onClick(TreeNode node, Object value, View nodeView) {
        //---To invisible pdf error message pdf view
        mMessageForFirstFile.setVisibility(View.GONE);
        mMessageForSecondFile.setVisibility(View.GONE);
        //---
        mDrawer.closeDrawer(GravityCompat.END);

        if (value instanceof ArrowExpandIconTreeItemHolder.IconTreeItem) {
            ArrowExpandIconTreeItemHolder.IconTreeItem value1 = (ArrowExpandIconTreeItemHolder.IconTreeItem) value;

            //----- THIS IS TO FIND OUT, WHICH level of treeView clicked
            // mClickedTreeStructureLevel = value1.level;

            //----- Get Object of clicked Element and create map to send  : Start------
            if (value1.objectData instanceof LstDocType) {
                LstDocType clickedLstDocTypeElement = (LstDocType) value1.objectData;

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
                        mGetEncryptedPDFRequestModelList.add(getEncryptedPDFRequestModel);
                        mPreviousClickedTreeElement.put(clickedLstDocTypeElement.getTypeName(), clickedLstDocTypeElement.getTypeName().trim());

                        ArrayList<String> tempClickedElements = new ArrayList<>(mPreviousClickedTreeElement.values());

                        expandCompareDialog();
                        switch (mGetEncryptedPDFRequestModelList.size()) {
                            case 1:
                                //-----
                                mFileOnePatientID.setText(getString(R.string.patient_id) + respectivePatientID);
                                mFileOneFileName.setText(getString(R.string.file) + tempClickedElements.get(0));
                                mFirstFileTypeProgressDialogLayout.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                //-----
                                mFileOnePatientID.setText(getString(R.string.patient_id) + respectivePatientID);
                                mFileOneFileName.setText(getString(R.string.file) + tempClickedElements.get(0));
                                mFirstFileTypeProgressDialogLayout.setVisibility(View.VISIBLE);
                                //-------
                                mFileTwoPatientID.setText(getString(R.string.patient_id) + respectivePatientID);
                                mFileTwoFileName.setText(getString(R.string.file) + tempClickedElements.get(1));
                                mSecondFileTypePdfViewLayout.setVisibility(View.VISIBLE);
                                mSecondFileTypeProgressDialogLayout.setVisibility(View.VISIBLE);
                                break;
                        }
                    }

                } else {
                    mGetEncryptedPDFRequestModelList.clear();
                    mGetEncryptedPDFRequestModelList.add(getEncryptedPDFRequestModel);

                    mPreviousClickedTreeElement.clear();
                    mPreviousClickedTreeElement.put(clickedLstDocTypeElement.getTypeName(), clickedLstDocTypeElement.getTypeName().trim());

                    mFirstFileTypeProgressDialogLayout.setVisibility(View.VISIBLE);
                    //--------
                    mSecondFileTypePdfViewLayout.setVisibility(View.GONE);
                    mSecondFileTypeProgressDialogLayout.setVisibility(View.GONE);
                    //--------
                    mPatientsHelper.getPdfData(mGetEncryptedPDFRequestModelList.get(0), DMSConstants.TASK_GET_PDF_DATA + "_0");
                }
            }
            //----- Get Object of clicked Element and create map to send  : End------
        }
    }


    private void loadPDFFromServer(String pdfFileURL, PDFView pdfViewToLoad) {

        String baseUrl = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext);

        pdfFileURL = baseUrl + pdfFileURL.replace("~", "").trim();

        CommonMethods.Log(TAG, "PDF URL:==-->> " + pdfFileURL);

        if (pdfViewToLoad == mFirstPdfView) {
            mFirstPdfView.fromUrl(pdfFileURL)
                    .defaultPage(mPageNumber)
                    .onError(this)
                    .onDraw(this)
                    .onLoad(this)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .loadFromUrl();
        } else if (pdfViewToLoad == mSecondPdfView) {
            mSecondPdfView.fromUrl(pdfFileURL)
                    .defaultPage(mPageNumber)
                    .onError(this)
                    .onDraw(this)
                    .onLoad(this)
                    .enableAnnotationRendering(true)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .loadFromUrl();
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
        } else {
            mSecondPdfView.setVisibility(View.VISIBLE);
            mSecondFileTypeProgressDialogLayout.setVisibility(View.VISIBLE);
            mSecondFileTypePdfViewLayout.setVisibility(View.VISIBLE);
            mSecondFileTypePdfViewLayout.setBackgroundResource(R.drawable.pdfdecoration);
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
                mFirstPdfView.zoomTo(DEFAULT_MID_SCALE);
                mSecondPdfView.zoomTo(DEFAULT_MID_SCALE);
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
        mArchivedPreferenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mArchivedSelectedPreference = array[position];
                mFileTreeResponseData = null;
                getArchivedPageNumber = 1;
                doCreateTreeStructure();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void doCreateTreeStructure() {

        if (respectiveRecordID != null) {
            mArchivedSelectedPreference = DMSConstants.ArchivedPreference.DATE;
            mArchivedPreferenceSpinner.setVisibility(View.GONE);
            mPreferenceLayout.setVisibility(View.GONE);
        }

        if (mFileTreeResponseData == null)
            getLoadArchivedList();
        else {
            switch (mArchivedSelectedPreference) {
                case DMSConstants.ArchivedPreference.FOLDER:
                    createAnnotationTreeWithFolderPreferences(mFileTreeResponseData, true);

                    break;
                case DMSConstants.ArchivedPreference.FILE:
                    createAnnotationTreeWithFilePreferences(mFileTreeResponseData, true);

                    break;
                case DMSConstants.ArchivedPreference.DATE:
                    createAnnotationTreeWithDatePreferences(mFileTreeResponseData, true);

                    break;
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
}
