package com.scorg.dms.adapters.dms_adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scorg.dms.R;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.PatientFileData;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by riteshpandhurkar on 24/2/17.
 */

public class PatientExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private OnPatientListener onPatientListener;

    private List<SearchResult> _listDataHeader = new ArrayList<>(); // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<PatientFileData>> _listDataChild = new HashMap<String, ArrayList<PatientFileData>>();

    private List<SearchResult> _originalListDataHeader = new ArrayList<>(); // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<PatientFileData>> _originalListDataChild = new HashMap<>();

    // @BindString(R.string.opd)
    private String opd;
    // @BindString(R.string.ipd)
    private String ipd;
    private String uhid;
    private List<SearchResult> searchResultForPatientDetails = new ArrayList<>();

    // Hashmap for keeping track of our checkbox check states
    private HashMap<Integer, boolean[]> mChildCheckStates = new HashMap<>();
    private String mCheckedBoxGroupName = null;
    private int lastExpanded = -1;
    private boolean mIsGroupExpanded;

    public PatientExpandableListAdapter(Context context, List<SearchResult> searchResult) {
        this._context = context;

        doCreateDataListMap(searchResult);
        opd = _context.getString(R.string.opd);
        ipd = _context.getString(R.string.ipd);
        uhid = _context.getString(R.string.uhid);

    }

    private void doCreateDataListMap(List<SearchResult> searchResult) {

        List<SearchResult> listDataHeader = new ArrayList<>();
        HashMap<String, ArrayList<PatientFileData>> listChildData = new HashMap<String, ArrayList<PatientFileData>>();

        for (SearchResult dataObject :
                searchResult) {
            String patientName = dataObject.getPatientName();
            String patientAddress = dataObject.getPatientAddress();
            String id = dataObject.getPatientId();
            listDataHeader.add(dataObject);

            //--------
            // This is done to set getPatientId in child (PatientFileData)
            List<PatientFileData> patientFileData = dataObject.getPatientFileData();
            for (PatientFileData temp :
                    patientFileData) {
                temp.setRespectiveParentPatientID(id);
            }
            //--------

            ArrayList<PatientFileData> formattedList = new ArrayList<>(patientFileData);

            //Collections.sort(formattedList, new DischargeDateWiseComparator());

            listChildData.put(patientName, formattedList);

            try {
                this.onPatientListener = ((OnPatientListener) _context);
            } catch (ClassCastException e) {
                throw new ClassCastException("Activity must implement onPatientListener.");
            }
        }

        this.searchResultForPatientDetails.addAll(searchResult);
        this._originalListDataHeader.addAll(listDataHeader);
        this._originalListDataChild.putAll(listChildData);

        manageChild(null);

    }


    @Override
    public PatientFileData getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getPatientName())
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ChildViewHolder childViewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_patient_content, parent, false);

            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        final PatientFileData childElement = getChild(groupPosition, childPosition);
        //---
        if (opd.equalsIgnoreCase(childElement.getFileType())) {
            // Label

            childViewHolder.ipdAdmissionDate.setText(_context.getString(R.string.visit_date));

            // Label End

            childViewHolder.ipd.setText(opd);
            childViewHolder.ipdValue.setText(childElement.getReferenceId());

            String s = CommonMethods.formatDateTime(childElement.getAdmissionDate(), DMSConstants.DATE_PATTERN.DD_MMM_YYYY, DMSConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DMSConstants.DATE);
            childViewHolder.ipdAdmissionDateValue.setText(s);

        } else {

            // Label

            childViewHolder.ipdAdmissionDate.setText(_context.getString(R.string.admission_date));
            childViewHolder.ipdDischargeDate.setText(_context.getString(R.string.discharge_date));

            // Label End

            childViewHolder.ipd.setText(ipd);
            childViewHolder.ipdValue.setText(String.valueOf(childElement.getReferenceId()));

            String date = CommonMethods.formatDateTime(childElement.getAdmissionDate(), DMSConstants.DATE_PATTERN.DD_MMM_YYYY, DMSConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DMSConstants.DATE);
            childViewHolder.ipdAdmissionDateValue.setText(date);

            date = CommonMethods.formatDateTime(childElement.getDischargeDate(), DMSConstants.DATE_PATTERN.DD_MMM_YYYY, DMSConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DMSConstants.DATE);

            childViewHolder.ipdDischargeDateValue.setText(date);
        }

        if (CommonMethods.isTablet(_context))
            childViewHolder.ipdCheckBox.setVisibility(View.VISIBLE);
        else
            childViewHolder.ipdCheckBox.setVisibility(View.GONE);

        if (mChildCheckStates.containsKey(groupPosition)) {
            boolean getChecked[] = mChildCheckStates.get(groupPosition);
            childViewHolder.ipdCheckBox.setChecked(getChecked[childPosition]);
        } else {
            boolean getChecked[] = new boolean[_originalListDataChild.get(_originalListDataHeader.get(groupPosition).getPatientName())
                    .size()];

            mChildCheckStates.put(groupPosition, getChecked);

            // set the check state of this position's checkbox based on the
            // boolean value of getChecked[position]
            childViewHolder.ipdCheckBox.setEnabled(getChildrenCount(groupPosition) > 1);

        }

        childViewHolder.ipdCheckBox.setEnabled(getChildrenCount(groupPosition) != 1);
        childViewHolder.ipdCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxClicked(childViewHolder.ipdCheckBox.isChecked(), groupPosition, childPosition);
            }
        });

        //--------------------

        manageChild(null);

        childViewHolder.rowLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPatientListener.onPatientListItemClick(childElement, getGroup(groupPosition).getPatientName());
            }
        });

        if (isLastChild) {
            childViewHolder.childCardView.setBackgroundResource(R.drawable.round_background_and_square_top_view);
            childViewHolder.childItemExpandCollapseButton.setImageResource(R.drawable.ic_expand_less_black_24dp);
            childViewHolder.childItemCollapseButton.setVisibility(View.VISIBLE);
        } else {
            childViewHolder.childCardView.setBackgroundResource(R.drawable.round_background_and_square_side_view);
            childViewHolder.childItemCollapseButton.setVisibility(View.GONE);
        }

        final ExpandableListView mExpandableListView = (ExpandableListView) parent;
        childViewHolder.childItemExpandCollapseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandableListView.collapseGroup(groupPosition);
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getPatientName())
                .size();
    }

    @Override
    public SearchResult getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final GroupViewHolder groupViewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_patient_list_header, parent, false);

            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        final ExpandableListView mExpandableListView = (ExpandableListView) parent;
//        mExpandableListView.expandGroup(groupPosition);

        SearchResult groupHeader = getGroup(groupPosition);
//        int childrenCount = getChildrenCount(groupPosition);

        groupViewHolder.userName.setText(groupHeader.getPatientName());
        groupViewHolder.patientId.setText(groupHeader.getPatientId());
        groupViewHolder.uhid.setText(uhid + ":");

        if (isExpanded) {
            groupViewHolder.cardView.setBackground(ContextCompat.getDrawable(_context, R.drawable.round_background_and_square_bottom_view));

            groupViewHolder.groupItemCollapseButton.setVisibility(View.GONE);
            groupViewHolder.groupItemExpandCollapseButton.setImageResource(R.drawable.ic_expand_less_black_24dp);
            //   groupViewHolder.divider.setVisibility(View.GONE);
        } else {
            groupViewHolder.cardView.setBackground(ContextCompat.getDrawable(_context, R.drawable.round_background_full_view));

            groupViewHolder.groupItemCollapseButton.setVisibility(View.VISIBLE);
            groupViewHolder.groupItemExpandCollapseButton.setImageResource(R.drawable.ic_expand_more_black_24dp);
            //  groupViewHolder.divider.setVisibility(View.VISIBLE);
        }

        //-------------
        TextDrawable textDrawable = CommonMethods.getTextDrawable(groupViewHolder.patientImageView.getContext(), groupHeader.getPatientName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.override(groupViewHolder.patientImageView.getResources().getDimensionPixelSize(R.dimen.dp67));
        requestOptions.circleCrop();
        requestOptions.placeholder(textDrawable);
        requestOptions.error(textDrawable);

        Glide.with(groupViewHolder.patientImageView.getContext())
                .load(groupHeader.getPatientImageURL())
                .apply(requestOptions)
                .into(groupViewHolder.patientImageView);
        //-------------

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PatientFileData child = getChild(groupPosition, 0);

                onPatientListener.onPatientListItemClick(child, getGroup(groupPosition).getPatientName());


            }
        });

        groupViewHolder.groupItemExpandCollapseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIsGroupExpanded = isExpanded;

                if (isExpanded) {
                    mExpandableListView.collapseGroup(groupPosition);
                } else {

                    if (lastExpanded != -1 && lastExpanded != groupPosition)
                        mExpandableListView.collapseGroup(lastExpanded);

                    mExpandableListView.expandGroup(groupPosition, true);
                    lastExpanded = groupPosition;


                }
            }
        });


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        @BindView(R.id.cardView)
        LinearLayout cardView;
        @BindView(R.id.userName)
        TextView userName;
        @BindView(R.id.uhid)
        TextView uhid;
        @BindView(R.id.patientId)
        TextView patientId;
        @BindView(R.id.groupItemExpandCollapseButton)
        AppCompatImageButton groupItemExpandCollapseButton;
        @BindView(R.id.groupItemCollapseButton)
        LinearLayout groupItemCollapseButton;
        @BindView(R.id.patientImageView)
        ImageView patientImageView;

        //@BindView(R.id.divider)
        //View divider;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        //---------

        @BindView(R.id.cardView)
        LinearLayout childCardView;
        @BindView(R.id.rowLay)
        LinearLayout rowLay;
        @BindView(R.id.ipd)
        TextView ipd;
        @BindView(R.id.ipdValue)
        TextView ipdValue;
        @BindView(R.id.ipdAdmissionDate)
        TextView ipdAdmissionDate;
        @BindView(R.id.ipdAdmissionDateValue)
        TextView ipdAdmissionDateValue;
        @BindView(R.id.ipdDischargeDate)
        TextView ipdDischargeDate;
        @BindView(R.id.ipdDischargeDateValue)
        TextView ipdDischargeDateValue;
        @BindView(R.id.ipdCheckBox)
        CheckBox ipdCheckBox;
        @BindView(R.id.childItemCollapseButton)
        LinearLayout childItemCollapseButton;
        @BindView(R.id.childItemExpandCollapseButton)
        AppCompatImageButton childItemExpandCollapseButton;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    private void manageChild(String groupName) {

        _listDataChild.clear();
        _listDataHeader.clear();

        _listDataHeader.addAll(_originalListDataHeader);

        for (Map.Entry<String, ArrayList<PatientFileData>> pair : _originalListDataChild.entrySet()) {
            ArrayList<PatientFileData> value = pair.getValue();
            _listDataChild.put(pair.getKey(), value);
        }

        this.notifyDataSetChanged();
    }


    private int getOriginalChildrenCount(int groupPosition) {
        return this._originalListDataChild.get(this._originalListDataHeader.get(groupPosition).getPatientName())
                .size();
    }

    private void checkBoxClicked(boolean isChecked, int mGroupPosition, int mChildPosition) {

        //------------------
        int counterToCheckValues = 0;

//        this.mGroupPosition = mGroupPosition;

        // To make arrayList of elements
        ArrayList<Boolean> tempStatusList = new ArrayList<>();
        for (Map.Entry<Integer, boolean[]> entries : mChildCheckStates.entrySet()) {
            boolean[] value = entries.getValue();
            for (boolean tempData :
                    value) {
                tempStatusList.add(tempData);
            }
        }

        // Increment counter if value ==> false
        for (Boolean dataValue :
                tempStatusList) {
            if (!dataValue) {
                counterToCheckValues = counterToCheckValues + 1;
            }
        }

        // This is done to check that, all elements in list is ==>false then reset mCheckedBoxGroupName
        if (counterToCheckValues == tempStatusList.size()) {
            mCheckedBoxGroupName = null;
        }

        boolean flag = true; // TO handle operation when selected for same group.
        String tempName = getGroup(mGroupPosition).getPatientName();
        if (mCheckedBoxGroupName == null) {
            mCheckedBoxGroupName = tempName;
            flag = true;
        } else if (!tempName.equalsIgnoreCase(mCheckedBoxGroupName)) {
            CommonMethods.showToast(_context, _context.getString(R.string.error_compare_patient));
            goPreviousPosition();
            flag = false;
        }

        if (flag) {
            //------
            boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
            getChecked[mChildPosition] = isChecked;
            mChildCheckStates.put(mGroupPosition, getChecked);
            //------
            ArrayList<Integer> tempCheckedDataToCompare = new ArrayList<>();
            boolean[] checkedValues = mChildCheckStates.get(mGroupPosition);

            for (int i = 0; i < checkedValues.length; i++) {
                boolean checkedValue = checkedValues[i];
                if (checkedValue) {
                    tempCheckedDataToCompare.add(i);
                }
            }
            //------
            if (tempCheckedDataToCompare.size() == 1) {
                PatientFileData child = getChild(mGroupPosition, tempCheckedDataToCompare.get(0));

                onPatientListener.onCompareDialogShow(null, null, null, null, true);
                onPatientListener.onCompareDialogShow(child, null, mCheckedBoxGroupName, tempName, true);

            } else if (tempCheckedDataToCompare.size() == 2) {
                PatientFileData child = getChild(mGroupPosition, tempCheckedDataToCompare.get(0));
                PatientFileData child_1 = getChild(mGroupPosition, tempCheckedDataToCompare.get(1));

                onPatientListener.onCompareDialogShow(null, null, null, null, true);
                onPatientListener.onCompareDialogShow(child, child_1, mCheckedBoxGroupName, tempName, true);

            } else if (tempCheckedDataToCompare.size() > 2) {
                getChecked[mChildPosition] = false;
                mChildCheckStates.put(mGroupPosition, getChecked);
                CommonMethods.showToast(_context, _context.getString(R.string.error_max_two_reports));
                goPreviousPosition();
            } else {
                onPatientListener.onCompareDialogShow(null, null, null, null, false);
            }
        }
        this.notifyDataSetChanged();

    }

    private void goPreviousPosition() {
        Iterator it = mChildCheckStates.entrySet().iterator();
        int childPos = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            boolean selected[] = (boolean[]) pair.getValue();

            for (boolean select : selected) {
                childPos += 1;
                if (select) {
                    int prePosition = (Integer) pair.getKey();
                    manageChild(_originalListDataHeader.get(prePosition).getPatientName());
                    onPatientListener.smoothScrollToPosition(prePosition + childPos);
                    break;
                }
            }
        }
    }

    public SearchResult searchPatientInfo(String patientId) {
        SearchResult searchResult = null;
        for (SearchResult searchResultPatientInfo : searchResultForPatientDetails) {
            if (searchResultPatientInfo.getPatientId().equals(patientId)) {
                searchResult = searchResultPatientInfo;
            }
        }

        return searchResult;
    }

    public interface OnPatientListener {
        void onCompareDialogShow(PatientFileData patientFileData1, PatientFileData patientFileData2, String mCheckedBoxGroupName, String tempName, boolean b);

        void onPatientListItemClick(PatientFileData childElement, String patientName);

        void smoothScrollToPosition(int previousPosition);
    }

    private class DischargeDateWiseComparator implements Comparator<PatientFileData> {

        public int compare(PatientFileData m1, PatientFileData m2) {
            Date m1Date = CommonMethods.convertStringToDate(m1.getDischargeDate(), DMSConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm);
            Date m2Date = CommonMethods.convertStringToDate(m2.getDischargeDate(), DMSConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm);
            return m2Date.compareTo(m1Date);
        }
    }


    public void addNewItems(List<SearchResult> searchResult) {
        doCreateDataListMap(searchResult);
    }


}