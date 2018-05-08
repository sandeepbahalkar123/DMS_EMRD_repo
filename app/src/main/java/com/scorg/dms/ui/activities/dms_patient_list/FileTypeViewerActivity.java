package com.scorg.dms.ui.activities.dms_patient_list;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.scorg.dms.model.dms_models.requestmodel.filetreerequestmodel.FileTreeRequestModel;
import com.scorg.dms.model.dms_models.requestmodel.filetreerequestmodel.LstSearchParam;
import com.scorg.dms.model.dms_models.requestmodel.getpdfdatarequestmodel.GetPdfDataRequestModel;
import com.scorg.dms.model.dms_models.requestmodel.getpdfdatarequestmodel.LstDocTypeRequest;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.ArchiveDatum;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.FileTreeResponseData;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.FileTreeResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.LstDocCategory;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.LstDocType;
import com.scorg.dms.model.dms_models.responsemodel.getpdfdataresponsemodel.GetPdfDataResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.PatientFileData;
import com.scorg.dms.ui.customesViews.treeViewHolder.arrow_expand.ArrowExpandIconTreeItemHolder;
import com.scorg.dms.ui.customesViews.treeViewHolder.arrow_expand.ArrowExpandSelectableHeaderHolder;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;
import com.shockwave.pdfium.PdfDocument;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.github.barteksc.pdfviewer.PDFView.DEFAULT_MID_SCALE;
import static com.github.barteksc.pdfviewer.PDFView.DEFAULT_MIN_SCALE;

/**
 * Created by jeetal on 14/3/17.
 */


public class FileTypeViewerActivity extends AppCompatActivity implements View.OnClickListener, HelperResponse, OnLoadCompleteListener, OnErrorListener, OnDrawListener, TreeNode.TreeNodeClickListener {

    private static final String TAG = FileTypeViewerActivity.class.getName();
    private static final int REQUEST_CODE_WRITE_FILE_ONE_PERMISSIONS = 101;
    private static final int REQUEST_CODE_WRITE_FILE_TWO_PERMISSIONS = 102;
    private Integer mPageNumber = 0;
    private boolean isFirstPdf = true;
    private float mCurrentXOffset = -1;
    private float mCurrentYOffset = -1;
    // End

    private Context mContext;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

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
    @BindView(R.id.fileTypeTreeViewContainer)
    RelativeLayout mFileTypeOneTreeViewContainer;
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

    DrawerLayout mDrawer;

    private DMSPatientsHelper mPatientsHelper;

    private boolean isCompareChecked = false;
    //---------
    ArrayList<PatientFileData> mSelectedFileTypeDataToCompare;
    String respectivePatientID;
    String patientName;
    String doctorName;
    String patientAddress;
    //---------
    private int mClickedTreeStructureLevel;
    private String mMergedRequestCalled = DMSConstants.BLANK;
    private FileTreeResponseData mFileTreeResponseData;

    private RelativeLayout mFirstFileTypeProgressDialogLayout;
    private RelativeLayout mSecondFileTypeProgressDialogLayout;
    private HashMap<Integer, String> mPreviousClickedTreeElement = null;
    private String fileOneData;
    private String fileTwoData;
    private boolean isComparingBetweenSameFileType = false;

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
            mSelectedFileTypeDataToCompare = (ArrayList<PatientFileData>) extra.getSerializable(getString(R.string.compare));
            respectivePatientID = extra.getString(DMSConstants.ID);
            patientName = extra.getString(DMSConstants.PATIENT_LIST_PARAMS.PATIENT_NAME);
            patientAddress = extra.getString(DMSConstants.PATIENT_ADDRESS);
            doctorName = extra.getString(DMSConstants.DOCTOR_NAME);
        }

        mContext = getApplicationContext();
        //-------------
        mPatientsHelper = new DMSPatientsHelper(this, this);
        mPreviousClickedTreeElement = new HashMap<>();

        bindView();

        //-----------
        mDrawer.openDrawer(GravityCompat.END);
        if (mFileTreeResponseData == null)
            getLoadArchivedList();
        else
            createAnnotationTreeStructure(mFileTreeResponseData, true);
        //-----------
    }

    private void bindView() {
        int width = (int) (getResources().getDisplayMetrics().widthPixels / (CommonMethods.isTablet(mContext) ? 1.6 : 1.2));

        //---------
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        mOpenRightDrawer.setOnClickListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //-----------
        mFirstFileTypePdfViewLayout.addView(CommonMethods.loadView(R.layout.mydialog, this));
        mFirstFileTypeProgressDialogLayout = (RelativeLayout) mFirstFileTypePdfViewLayout.findViewById(R.id.progressBarContainerLayout);
        mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
        mSecondFileTypePdfViewLayout.addView(CommonMethods.loadView(R.layout.mydialog, this));
        mSecondFileTypeProgressDialogLayout = (RelativeLayout) mSecondFileTypePdfViewLayout.findViewById(R.id.progressBarContainerLayout);
        mSecondFileTypeProgressDialogLayout.setVisibility(View.GONE);

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
        } else
            super.onBackPressed();
    }

    private void doBindHeaderViews() {

        mPatientName.setText(patientName);
        mDoctorNameTwo.setText(doctorName);
        mDoctorNameOne.setText(doctorName);
        mPatientAddress.setText(patientAddress);

        //----------
        PatientFileData patientFileData = mSelectedFileTypeDataToCompare.get(0);
        mFileOneRefId.setText(String.valueOf(patientFileData.getReferenceId()));
        mAdmissionDateOne.setText(String.valueOf(patientFileData.getAdmissionDate()));
        mDischargeDateOne.setText(String.valueOf(patientFileData.getDischargeDate()));
        mFileTypeOne.setText(String.valueOf(patientFileData.getFileType()));
        mPatientId.setText(String.valueOf(patientFileData.getRespectiveParentPatientID()));
        //----------

        if (String.valueOf(patientFileData.getFileType()).equalsIgnoreCase(getResources().getString(R.string.opd)))
            dischargeDateRow.setVisibility(View.GONE);
        else dischargeDateRow.setVisibility(View.VISIBLE);

        if (mSelectedFileTypeDataToCompare.size() == 2) {

            mSecondFileTypePdfViewLayout.setVisibility(View.VISIBLE);
            mRowScrollBoth.setVisibility(View.VISIBLE);
            mFileTwoDrawerLayout.setVisibility(View.VISIBLE);

            patientFileData = mSelectedFileTypeDataToCompare.get(1);
            mFileTwoRefId.setText(String.valueOf(patientFileData.getReferenceId()));
            mAdmissionDateTwo.setText(String.valueOf(patientFileData.getAdmissionDate()));
            mDischargeDateTwo.setText(String.valueOf(patientFileData.getDischargeDate()));
            mFileTypeTwo.setText(String.valueOf(patientFileData.getFileType()));

            if (String.valueOf(patientFileData.getFileType()).equalsIgnoreCase(getResources().getString(R.string.opd)))
                dischargeDateRowTwo.setVisibility(View.GONE);
            else dischargeDateRowTwo.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openRightDrawer:
                mDrawer.openDrawer(GravityCompat.END);
                if (mFileTreeResponseData == null)
                    getLoadArchivedList();
                else
                    createAnnotationTreeStructure(mFileTreeResponseData, true);

                break;
        }
    }

    // To create JSON and get archived list of data.
    private void getLoadArchivedList() {
        //---------------
        FileTreeRequestModel fileTreeRequestModel = new FileTreeRequestModel();

        List<LstSearchParam> lstSearchParamList = new ArrayList<>();
        if (mSelectedFileTypeDataToCompare != null) {
            for (PatientFileData tempObject :
                    mSelectedFileTypeDataToCompare) {
                LstSearchParam lstSearchParam = new LstSearchParam();
                lstSearchParam.setPatientId(tempObject.getRespectiveParentPatientID());
                lstSearchParam.setFileType(tempObject.getFileType());
                lstSearchParam.setFileTypeRefId("" + tempObject.getReferenceId());
                lstSearchParamList.add(lstSearchParam);
            }
            fileTreeRequestModel.setLstSearchParam(lstSearchParamList);
            mPatientsHelper.doGetArchivedList(fileTreeRequestModel);
        }
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        //Changed from switch to if..else, due to dynamically comparing received mOldDataTag.
        if (mOldDataTag.startsWith(DMSConstants.TASK_GET_PDF_DATA + mMergedRequestCalled)) {
            GetPdfDataResponseModel getPdfDataResponseModel = (GetPdfDataResponseModel) customResponse;
            if (getPdfDataResponseModel.getCommon().getStatusCode().equals(DMSConstants.SUCCESS)) {
                String fileData = getPdfDataResponseModel.getGetPdfDataResponseData().getFileData();
                if (fileData != null) {
                    if (mMergedRequestCalled.equalsIgnoreCase("0") || mOldDataTag.endsWith("0")) {
                        mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
                        fileOneData = fileData;
                        askWriteExtenralStoragePermission(REQUEST_CODE_WRITE_FILE_ONE_PERMISSIONS);
                    } else if (mMergedRequestCalled.equalsIgnoreCase(("1")) || mOldDataTag.endsWith("1")) {
                        mSecondFileTypeProgressDialogLayout.setVisibility(View.GONE);
                        fileTwoData = fileData;
                        askWriteExtenralStoragePermission(REQUEST_CODE_WRITE_FILE_TWO_PERMISSIONS);
                    }
                } else
                    Toast.makeText(mContext, "Document not available", Toast.LENGTH_SHORT).show();
            }

        } else if (String.valueOf(mOldDataTag).equalsIgnoreCase("" + DMSConstants.TASK_GET_ARCHIVED_LIST)) {
            FileTreeResponseModel fileTreeResponseModel = (FileTreeResponseModel) customResponse;
            mFileTreeResponseData = fileTreeResponseModel.getFileTreeResponseData();

            if (mFileTypeOne.getText().toString().equalsIgnoreCase(mFileTypeTwo.getText().toString())) {
                isComparingBetweenSameFileType = true;
                doGetMergeArchiveList(mFileTreeResponseData, mFileTypeOne.getText().toString());
            }
            createAnnotationTreeStructure(mFileTreeResponseData, true);
            //------

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


    private void createAnnotationTreeStructure(FileTreeResponseData fileTreeResponseData, boolean isExpanded) {

        mFileTypeOneTreeViewContainer.removeAllViews();

        TreeNode treeRoot = TreeNode.root();

        int lstDocCategoryObjectLeftPadding = (int) (getResources().getDimension(R.dimen.dp30) / getResources().getDisplayMetrics().density);
        int lstDocTypeChildLeftPadding = (int) (getResources().getDimension(R.dimen.dp50) / getResources().getDisplayMetrics().density);
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
            if (archiveDatumObject.getFileType().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                selectableHeaderHolder.setTreeLabelBold(true);

            // Label(pageCount)|NA
            String dataToShow = archiveDatumObject.getFileType() + " (" + archiveDatumObject.getTotalDocCategoryPageCount() + ")" + "|NA";

            TreeNode archiveDatumObjectFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, archiveDatumObject, i))
                    .setViewHolder(selectableHeaderHolder);

            //---- For list categories loop
            List<LstDocCategory> lstDocCategories = archiveDatumObject.getLstDocCategories();

            for (int j = 0; j < lstDocCategories.size(); j++) {
                LstDocCategory lstDocCategoryObject = lstDocCategories.get(j);

                // Label(pageCount)|id
                dataToShow = lstDocCategoryObject.getCategoryName() + " (" + lstDocCategoryObject.getTotalDocTypePageCount() + ")" + "|" + lstDocCategoryObject.getCategoryId();
                ArrowExpandSelectableHeaderHolder docCatSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded, lstDocTypeChildLeftPadding);
                docCatSelectableHeaderHolder.setOnlyOneNodeExpanded(true);

                docCatSelectableHeaderHolder.setNodeValueColor(textColor);

                //---- To bold clicked text in tree
                if (lstDocCategoryObject.getCategoryName().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
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
                    ArrowExpandSelectableHeaderHolder lstDocTypeChildSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded, lstDocTypeChildLeftPadding);
                    lstDocTypeChildSelectableHeaderHolder.setOnlyOneNodeExpanded(true);
                    lstDocTypeChildSelectableHeaderHolder.setNodeValueColor(textColor);

                    //---- To bold clicked text in tree
                    if (lstDocTypeChild.getTypeName().equalsIgnoreCase(mPreviousClickedTreeElement.get(i)))
                        lstDocTypeChildSelectableHeaderHolder.setTreeLabelBold(true);

                    TreeNode lstDocTypeChildFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, lstDocTypeChild, i))
                            .setViewHolder(lstDocTypeChildSelectableHeaderHolder);
                    //-------
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
            if (v == mFirstPdfView) {
                isFirstPdf = true;
            } else {
                isFirstPdf = false;
            }
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
        String idToFetch[] = null;

        GetPdfDataRequestModel getPdfDataRequestModel = new GetPdfDataRequestModel();
        getPdfDataRequestModel.setPatientId(respectivePatientID);

        if (value instanceof ArrowExpandIconTreeItemHolder.IconTreeItem) {
            ArrowExpandIconTreeItemHolder.IconTreeItem value1 = (ArrowExpandIconTreeItemHolder.IconTreeItem) value;

            //----- THIS IS TO FIND OUT, WHICH level of treeView clicked
            mClickedTreeStructureLevel = value1.level;
            mMergedRequestCalled = DMSConstants.BLANK;

            //----- Get Object of clicked Element and create map to send  : Start------
            if (value1.objectData instanceof ArchiveDatum) {
                ArchiveDatum tempData = (ArchiveDatum) value1.objectData;
                List<LstDocCategory> lstDocCategories = tempData.getLstDocCategories();
                mPreviousClickedTreeElement.put(mClickedTreeStructureLevel, tempData.getFileType());
                getPdfDataRequestModel.setLstDocTypeRequests(createLstDocTypeRequest(lstDocCategories));

                //----
                if (tempData.getMergedFileCompareCustomID().length != 2) {
                    mMergedRequestCalled = tempData.getMergedFileCompareCustomID()[0];
                } else {
                    idToFetch = tempData.getMergedFileCompareCustomID();
                }
                //----

            } else if (value1.objectData instanceof LstDocCategory) {
                LstDocCategory objectData = (LstDocCategory) value1.objectData;
                List<LstDocCategory> lstDocCategories = new ArrayList<>();
                lstDocCategories.add(objectData);
                mPreviousClickedTreeElement.put(mClickedTreeStructureLevel, objectData.getCategoryName());
                getPdfDataRequestModel.setLstDocTypeRequests(createLstDocTypeRequest(lstDocCategories));

                //----
                if (objectData.getMergedFileCompareCustomID().length != 2) {
                    mMergedRequestCalled = objectData.getMergedFileCompareCustomID()[0];
                } else {
                    idToFetch = objectData.getMergedFileCompareCustomID();
                }
                //----

            } else if (value1.objectData instanceof LstDocType) {
                LstDocType clickedLstDocTypeElement = (LstDocType) value1.objectData;
                mPreviousClickedTreeElement.put(mClickedTreeStructureLevel, clickedLstDocTypeElement.getTypeName());
                //----
                List<LstDocType> lstDocType = new ArrayList<>();
                lstDocType.add(clickedLstDocTypeElement);
                //-----
                List<LstDocCategory> lstDocCategories = new ArrayList<>();
                LstDocCategory temp = new LstDocCategory();
                temp.setLstDocTypes(lstDocType);
                lstDocCategories.add(temp);

                getPdfDataRequestModel.setLstDocTypeRequests(createLstDocTypeRequest(lstDocCategories));

                //--------
                if (clickedLstDocTypeElement.getMergedFileCompareCustomID().length != 2) {
                    mMergedRequestCalled = clickedLstDocTypeElement.getMergedFileCompareCustomID()[0];
                } else {
                    idToFetch = clickedLstDocTypeElement.getMergedFileCompareCustomID();
                }
                //--------
            }
            //----- Get Object of clicked Element and create map to send  : End------

            List<LstDocTypeRequest> lstDocTypeRequestsToFetchFromServer = getPdfDataRequestModel.getLstDocTypeRequests();

            switch (mMergedRequestCalled) {

                case "0":
                    //--- Set fileType and refID based on mClickedTreeStructureLevel
                    getPdfDataRequestModel.setFileType(mSelectedFileTypeDataToCompare.get(Integer.parseInt(mMergedRequestCalled)).getFileType());
                    getPdfDataRequestModel.setFileTypeRefId("" + mSelectedFileTypeDataToCompare.get(Integer.parseInt(mMergedRequestCalled)).getReferenceId());

                    //-- To Grayed out alternate views based on selection :START----
                    if (isComparingBetweenSameFileType) {
                        mSecondPdfView.setVisibility(View.GONE);
                        mSecondFileTypePdfViewLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.Gray));
                    }
                    //-- To Grayed out alternate views based on selection : END ----

                    doCallPDFDataService(getPdfDataRequestModel, lstDocTypeRequestsToFetchFromServer.size(), mFirstPdfView, mFirstFileTypeProgressDialogLayout, mFirstFileTypePdfViewLayout, mMergedRequestCalled);
                    break;
                case "1":
                    //--- Set fileType and refID based on mClickedTreeStructureLevel
                    getPdfDataRequestModel.setFileType(mSelectedFileTypeDataToCompare.get(Integer.parseInt(mMergedRequestCalled)).getFileType());
                    getPdfDataRequestModel.setFileTypeRefId("" + mSelectedFileTypeDataToCompare.get(Integer.parseInt(mMergedRequestCalled)).getReferenceId());

                    //-- To Grayed out alternate views based on selection :START----
                    if (isComparingBetweenSameFileType) {
                        mFirstPdfView.setVisibility(View.GONE);
                        mFirstFileTypePdfViewLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.Gray));
                    }
                    //-- To Grayed out alternate views based on selection : END ----

                    doCallPDFDataService(getPdfDataRequestModel, lstDocTypeRequestsToFetchFromServer.size(), mSecondPdfView, mSecondFileTypeProgressDialogLayout, mSecondFileTypePdfViewLayout, mMergedRequestCalled);
                    break;
                default:
                    if (idToFetch != null) {
                        for (int i = 0; i < idToFetch.length; i++) {
                            //--- Set fileType and refID based on mClickedTreeStructureLevel
                            getPdfDataRequestModel.setFileType(mSelectedFileTypeDataToCompare.get(Integer.parseInt(idToFetch[i])).getFileType());
                            getPdfDataRequestModel.setFileTypeRefId("" + mSelectedFileTypeDataToCompare.get(Integer.parseInt(idToFetch[i])).getReferenceId());
                            if (i == 0)
                                doCallPDFDataService(getPdfDataRequestModel, lstDocTypeRequestsToFetchFromServer.size(), mFirstPdfView, mFirstFileTypeProgressDialogLayout, mFirstFileTypePdfViewLayout, i + "");
                            else
                                doCallPDFDataService(getPdfDataRequestModel, lstDocTypeRequestsToFetchFromServer.size(), mSecondPdfView, mSecondFileTypeProgressDialogLayout, mSecondFileTypePdfViewLayout, i + "");
                        }
                    }
            }
        }
    }

    // TO create object to pass to helper
    private List<LstDocTypeRequest> createLstDocTypeRequest(List<LstDocCategory> lstDocCategories) {

        List<LstDocTypeRequest> docList = new ArrayList<>();

        for (LstDocCategory tempCat :
                lstDocCategories) {
            List<LstDocType> lstDocTypes = tempCat.getLstDocTypes();

            for (LstDocType tempDocObject :
                    lstDocTypes) {
                LstDocTypeRequest lstDocTypeRequest = new LstDocTypeRequest();
                lstDocTypeRequest.setTypeId(tempDocObject.getTypeId());
                lstDocTypeRequest.setTypeName(tempDocObject.getTypeName());
                lstDocTypeRequest.setAbbreviation(tempDocObject.getAbbreviation());
                lstDocTypeRequest.setCreatedDate(tempDocObject.getCreatedDate());
                lstDocTypeRequest.setPageCount(tempDocObject.getPageCount());
                lstDocTypeRequest.setPageNumber(tempDocObject.getPageNumber());
                docList.add(lstDocTypeRequest);
            }
        }
        return docList;
    }

    private void askWriteExtenralStoragePermission(int requestCode) {
        int hasWriteContactsPermissionCamera = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasWriteContactsPermissionCamera != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
            }
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_WRITE_FILE_ONE_PERMISSIONS:
                loadPDFFromServer(mFirstPdfView, fileOneData, "file1", "pdf");
                break;

            case REQUEST_CODE_WRITE_FILE_TWO_PERMISSIONS:
                loadPDFFromServer(mSecondPdfView, fileTwoData, "file2", "pdf");
                break;

            default:
                CommonMethods.Log(TAG, String.valueOf(requestCode));
        }

    }

    private void loadPDFFromServer(PDFView pdfViewToLoad, String base64Pdf, String fileName, String extension) {
        pdfViewToLoad.fromFile(CommonMethods.storeAndGetDocument(this, base64Pdf, fileName, extension))
                .defaultPage(mPageNumber)
                .onError(this)
                .onDraw(this)
                .onLoad(this)
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_WRITE_FILE_ONE_PERMISSIONS:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    loadPDFFromServer(mFirstPdfView, fileOneData, "file1", "pdf");
                } else {
                    // Permission Denied
                    CommonMethods.showToast(mContext, getString(R.string.denied_permission_read_document));
                }
                break;

            case REQUEST_CODE_WRITE_FILE_TWO_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    loadPDFFromServer(mSecondPdfView, fileTwoData, "file2", "pdf");
                } else {
                    // Permission Denied
                    CommonMethods.showToast(mContext, getString(R.string.denied_permission_read_document));
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void doCallPDFDataService(GetPdfDataRequestModel getPdfDataRequestModel, int size, PDFView pdfView, RelativeLayout progressBarLayout, FrameLayout pdfContainerLayout, String mTagID) {
        //-----TO grayed out pdfview based on no element in that view -----
        if (size != 0) {
            pdfView.setVisibility(View.VISIBLE);
            progressBarLayout.setVisibility(View.VISIBLE);
            pdfContainerLayout.setBackgroundResource(R.drawable.pdfdecoration);
        } else {
            pdfView.setVisibility(View.GONE);
            pdfContainerLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.Gray));
        }
        mPatientsHelper.getPdfData(getPdfDataRequestModel, (DMSConstants.TASK_GET_PDF_DATA + mTagID));
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


    private void doGetMergeArchiveList(FileTreeResponseData originalFileTreeResponseData, String fileType) {
        List<LstDocCategory> mergedALstDocCategoryListData = new ArrayList<LstDocCategory>();

        List<ArchiveDatum> originalArchiveDataList = originalFileTreeResponseData.getArchiveData();
        //---Merge LstDocCategory into single list-
        for (ArchiveDatum originalArchiveDataObject :
                originalArchiveDataList) {
            if (originalArchiveDataObject.getFileType().equalsIgnoreCase(fileType)) {
                List<LstDocCategory> lstDocCategories = originalArchiveDataObject.getLstDocCategories();
                for (LstDocCategory tempObject :
                        lstDocCategories) {
                    mergedALstDocCategoryListData.add(tempObject);
                }
            }
        }
        CommonMethods.Log(TAG, "MERGED mergedALstDocCategoryListData :" + mergedALstDocCategoryListData.toString());
        //-----------
        HashMap<LstDocCategory, List<LstDocType>> mergedLstDocTypeHashMap = new HashMap<LstDocCategory, List<LstDocType>>();

        for (int i = 0; i < mergedALstDocCategoryListData.size(); i++) {
            boolean flag = false;
            LstDocCategory iLoopLstDocCategory = mergedALstDocCategoryListData.get(i);

            boolean mapInsertedStatus = false;

            for (Map.Entry<LstDocCategory, List<LstDocType>> entry : mergedLstDocTypeHashMap.entrySet()) {
                LstDocCategory key = entry.getKey();
                if (iLoopLstDocCategory.getCategoryId() == key.getCategoryId()) {
                    mapInsertedStatus = true;
                    break;
                } else {
                    mapInsertedStatus = false;
                }
            }
            //---

            //--if already in map, then don't iterate
            if (!mapInsertedStatus) {
                for (int j = i + 1; j < mergedALstDocCategoryListData.size(); j++) {
                    LstDocCategory jLoopLstDocCategory = mergedALstDocCategoryListData.get(j);
                    if (iLoopLstDocCategory.getCategoryId() == jLoopLstDocCategory.getCategoryId()) {
                        // got the duplicate element

                        //---***************
                        List<LstDocType> firstLstDocTypes = iLoopLstDocCategory.getLstDocTypes();
                        // THIS IS DONE BZCZ, WE ARE ADDING List<LstDocType> into hashmap,
                        // hence to avoid data redundancy
                        iLoopLstDocCategory.setLstDocTypes(null);


                        //---
                        List<LstDocType> secondLstDocTypes = jLoopLstDocCategory.getLstDocTypes();

                        List<LstDocType> tempLocalListToAddIntoMap = new ArrayList<>();

                        tempLocalListToAddIntoMap.addAll(firstLstDocTypes);
                        tempLocalListToAddIntoMap.addAll(secondLstDocTypes);

                        //-- To Merge getMergedFileCompareCustomID--
                        String[] iLoopMergedFileCompareCustomID = iLoopLstDocCategory.getMergedFileCompareCustomID();
                        String[] jLoopMergedFileCompareCustomID = jLoopLstDocCategory.getMergedFileCompareCustomID();

                        String[] tempTotal = new String[iLoopMergedFileCompareCustomID.length + jLoopMergedFileCompareCustomID.length];

                        System.arraycopy(iLoopMergedFileCompareCustomID, 0, tempTotal, 0, iLoopMergedFileCompareCustomID.length);
                        System.arraycopy(jLoopMergedFileCompareCustomID, 0, tempTotal, iLoopMergedFileCompareCustomID.length, jLoopMergedFileCompareCustomID.length);

                        iLoopLstDocCategory.setMergedFileCompareCustomID(tempTotal);
                        //----

                        HashSet<LstDocType> lstDocTypes = mergeLstDocTypeElement(tempLocalListToAddIntoMap);

                        mergedLstDocTypeHashMap.put(iLoopLstDocCategory, new ArrayList<LstDocType>(lstDocTypes));
                        //---***************
                        flag = true;
                    }
                }

                if (!flag) {
                    LstDocCategory firstLstDocCategory = mergedALstDocCategoryListData.get(i);
                    List<LstDocType> firstLstDocTypes = firstLstDocCategory.getLstDocTypes();
                    // THIS IS DONE BZCZ, WE ARE ADDING List<LstDocType> into hashmap,
                    // hence to avoid data redundancy
                    firstLstDocCategory.setLstDocTypes(null);

                    mergedLstDocTypeHashMap.put(firstLstDocCategory, firstLstDocTypes);
                }
            }
        }
        CommonMethods.Log(TAG, "COMBINED HASHMAP LIST: " + mergedLstDocTypeHashMap.toString());

        //----final object creation
        ArchiveDatum archiveDatum = new ArchiveDatum();
        ArrayList<LstDocCategory> lstDocCategories = new ArrayList<LstDocCategory>();
        if (mergedLstDocTypeHashMap.isEmpty()) {
            archiveDatum.setFileType(fileType);
        } else {
            for (Map.Entry<LstDocCategory, List<LstDocType>> entry : mergedLstDocTypeHashMap.entrySet()) {
                LstDocCategory key = entry.getKey();
                archiveDatum.setFileType(fileType);
                key.setLstDocTypes(entry.getValue());
                lstDocCategories.add(key);
            }
        }
        archiveDatum.setLstDocCategories(lstDocCategories);
        //-- This is done to maintain parent element pattern for both first & second file view.
        archiveDatum.setMergedFileCompareCustomID(new String[]{"0", "1"});

        ArrayList<ArchiveDatum> lstArchiveDatum = new ArrayList<ArchiveDatum>();
        lstArchiveDatum.add(archiveDatum);

        mFileTreeResponseData.setArchiveData(lstArchiveDatum);
        CommonMethods.Log(TAG, "FINAL COMBINED HASHMAP LIST: " + mFileTreeResponseData.toString());
        //-----

    }


    private HashSet<LstDocType> mergeLstDocTypeElement(List<LstDocType> tempLstDocTypeList) {

        HashSet<LstDocType> hashSet = new HashSet<>();
        for (int i = 0; i < tempLstDocTypeList.size(); i++) {
            boolean flag = false;
            LstDocType iLoopLstDocType = tempLstDocTypeList.get(i);
            for (int j = i + 1; j < tempLstDocTypeList.size(); j++) {
                LstDocType jLoopLstDocType = tempLstDocTypeList.get(j);
                if (iLoopLstDocType.getTypeId() == jLoopLstDocType.getTypeId()) {
                    LstDocType lstDocType = new LstDocType();
                    lstDocType.setTypeId(iLoopLstDocType.getTypeId());
                    lstDocType.setTypeName(iLoopLstDocType.getTypeName());
                    lstDocType.setAbbreviation(iLoopLstDocType.getAbbreviation());
                    lstDocType.setCreatedDate(iLoopLstDocType.getCreatedDate());
                    lstDocType.setPageCount(iLoopLstDocType.getPageCount() + jLoopLstDocType.getPageCount());
                    lstDocType.setPageNumber(iLoopLstDocType.getPageNumber());

                    //-- To Merge getMergedFileCompareCustomID--
                    String[] iLoopMergedFileCompareCustomID = iLoopLstDocType.getMergedFileCompareCustomID();
                    String[] jLoopMergedFileCompareCustomID = jLoopLstDocType.getMergedFileCompareCustomID();

                    String[] tempTotal = new String[iLoopMergedFileCompareCustomID.length + jLoopMergedFileCompareCustomID.length];

                    System.arraycopy(iLoopMergedFileCompareCustomID, 0, tempTotal, 0, iLoopMergedFileCompareCustomID.length);
                    System.arraycopy(jLoopMergedFileCompareCustomID, 0, tempTotal, iLoopMergedFileCompareCustomID.length, jLoopMergedFileCompareCustomID.length);

                    lstDocType.setMergedFileCompareCustomID(tempTotal);
                    //----
                    //-------
                    boolean tempFlag = true;
                    for (LstDocType tempData :
                            hashSet) {
                        if (tempData.getTypeId() == iLoopLstDocType.getTypeId()) {
                            tempFlag = false;
                            break;
                        }
                    }
                    if (tempFlag)
                        hashSet.add(lstDocType);
                    //----
                    flag = true;
                }
            }

            if (!flag) {
                LstDocType lstDocType = new LstDocType();
                lstDocType.setTypeId(iLoopLstDocType.getTypeId());
                lstDocType.setTypeName(iLoopLstDocType.getTypeName());
                lstDocType.setAbbreviation(iLoopLstDocType.getAbbreviation());
                lstDocType.setCreatedDate(iLoopLstDocType.getCreatedDate());
                lstDocType.setPageCount(iLoopLstDocType.getPageCount());
                lstDocType.setPageNumber(iLoopLstDocType.getPageNumber());
                lstDocType.setMergedFileCompareCustomID(iLoopLstDocType.getMergedFileCompareCustomID());
                //-------
                boolean tempFlag = true;
                for (LstDocType tempData :
                        hashSet) {
                    if (tempData.getTypeId() == iLoopLstDocType.getTypeId()) {
                        tempFlag = false;
                        break;
                    }
                }
                if (tempFlag)
                    hashSet.add(lstDocType);
                //----
            }
        }
        return hashSet;
    }

}
