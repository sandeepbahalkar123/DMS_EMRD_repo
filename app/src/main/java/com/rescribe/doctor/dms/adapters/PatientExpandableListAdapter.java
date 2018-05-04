package com.rescribe.doctor.dms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rescribe.doctor.R;
import com.rescribe.doctor.dms.model.responsemodel.showsearchresultresponsemodel.PatientFileData;
import com.rescribe.doctor.dms.model.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.rescribe.doctor.dms.util.DmsConstants;
import com.rescribe.doctor.util.CommonMethods;

import java.util.ArrayList;
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

    private String TAG = this.getClass().getName();
    private Context _context;
    private OnPatientListener onPatientListener;

    private List<SearchResult> _listDataHeader = new ArrayList<>(); // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<PatientFileData>> _listDataChild = new HashMap<String, ArrayList<PatientFileData>>();

    private List<SearchResult> _originalListDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<PatientFileData>> _originalListDataChild;

    // @BindString(R.string.opd)
    private String opd;
    // @BindString(R.string.ipd)
    private String ipd;
    private String uhid;
    private int dataShowMaxValue = 2;
    private List<SearchResult> searchResultForPatientDetails;

    // Hashmap for keeping track of our checkbox check states
    private HashMap<Integer, boolean[]> mChildCheckStates = new HashMap<>();
    private String mCheckedBoxGroupName = null;
//    private int mGroupPosition = -1;

    public PatientExpandableListAdapter(Context context, List<SearchResult> searchResult) {
        this._context = context;

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

            listChildData.put(patientName, new ArrayList<PatientFileData>(patientFileData));

            try {
                this.onPatientListener = ((OnPatientListener) _context);
            } catch (ClassCastException e) {
                throw new ClassCastException("Activity must implement onPatientListener.");
            }

        }

        this.searchResultForPatientDetails = searchResult;
        this._originalListDataHeader = listDataHeader;
        this._originalListDataChild = listChildData;

        manageChild(null);

        opd = _context.getString(R.string.opd);
        ipd = _context.getString(R.string.ipd);
        uhid = _context.getString(R.string.uhid);


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
            childViewHolder.opd.setText(opd);
            childViewHolder.opdValue.setText(childElement.getReferenceId());
            //---------
            //-- TODO: visit date is not getting from API
            String s = CommonMethods.formatDateTime(childElement.getAdmissionDate(), DmsConstants.DATE_PATTERN.DD_MM_YYYY, DmsConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DmsConstants.DATE);
            childViewHolder.opdVisitDateValue.setText(s);
            //---------
            //+++++++----------

            childViewHolder.opdLayout.setVisibility(View.VISIBLE);
            childViewHolder.ipdLayout.setVisibility(View.GONE);

        } else {

            childViewHolder.ipd.setText(ipd);
            childViewHolder.ipdValue.setText(String.valueOf(childElement.getReferenceId()));

            String date = CommonMethods.formatDateTime(childElement.getAdmissionDate(), DmsConstants.DATE_PATTERN.DD_MM_YYYY, DmsConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DmsConstants.DATE);

            childViewHolder.ipdAdmissionDateValue.setText(date);
            //---
            date = CommonMethods.formatDateTime(childElement.getDischargeDate(), DmsConstants.DATE_PATTERN.DD_MM_YYYY, DmsConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DmsConstants.DATE);

            childViewHolder.ipdDischargeDateValue.setText(date);

            childViewHolder.ipdLayout.setVisibility(View.VISIBLE);
            childViewHolder.opdLayout.setVisibility(View.GONE);
        }

        //------------------
        int originalChildrenCount = getOriginalChildrenCount(groupPosition);
        int shownDataChildrenCount = getChildrenCount(groupPosition);
        // CommonMethods.Log(TAG, "childrenCount: " + groupPosition + ":" + childPosition + ":" + originalChildrenCount);
        if (dataShowMaxValue < originalChildrenCount && (childPosition + 1) == shownDataChildrenCount) {
            childViewHolder.moreOption.setVisibility(View.VISIBLE);
            childViewHolder.moreOption.setTag(getGroup(groupPosition).getPatientName());

            if (childElement.isShowCompleteList()) {
                childViewHolder.moreOption.setText(_context.getString(R.string.less));
            } else {
                childViewHolder.moreOption.setText(_context.getString(R.string.more));
            }
        } else {
            childViewHolder.moreOption.setVisibility(View.GONE);
        }

        //----------------
        if (isLastChild) {
            childViewHolder.mDividerLineMore.setVisibility(View.VISIBLE);
        } else {
            childViewHolder.mDividerLineMore.setVisibility(View.INVISIBLE);
        }
        //-----------------
        childViewHolder.ipdCheckBox.setOnCheckedChangeListener(null);
        childViewHolder.opdCheckBox.setOnCheckedChangeListener(null);

        if (mChildCheckStates.containsKey(groupPosition)) {
            boolean getChecked[] = mChildCheckStates.get(groupPosition);
            if (childViewHolder.ipdLayout.getVisibility() == View.VISIBLE) {
                childViewHolder.ipdCheckBox.setChecked(getChecked[childPosition]);
            } else if (childViewHolder.opdLayout.getVisibility() == View.VISIBLE) {
                childViewHolder.opdCheckBox.setChecked(getChecked[childPosition]);
            }
        } else {
            boolean getChecked[] = new boolean[_originalListDataChild.get(_originalListDataHeader.get(groupPosition).getPatientName())
                    .size()];

            mChildCheckStates.put(groupPosition, getChecked);

            // set the check state of this position's checkbox based on the
            // boolean value of getChecked[position]
            if (childViewHolder.ipdLayout.getVisibility() == View.VISIBLE) {
                if (getChildrenCount(groupPosition) > 1) {
                    childViewHolder.ipdCheckBox.setEnabled(true);
                }else {
                    childViewHolder.ipdCheckBox.setEnabled(false);
                }
                childViewHolder.ipdCheckBox.setChecked(false);
            } else if (childViewHolder.opdLayout.getVisibility() == View.VISIBLE) {
                if (getChildrenCount(groupPosition) > 1) {
                    childViewHolder.opdCheckBox.setEnabled(true);
                }else {
                    childViewHolder.opdCheckBox.setEnabled(false);
                }
                childViewHolder.opdCheckBox.setChecked(false);
            }
        }

        if (getChildrenCount(groupPosition) == 1) {
            childViewHolder.opdCheckBox.setEnabled(false);
            childViewHolder.ipdCheckBox.setEnabled(false);
        }else {
            childViewHolder.opdCheckBox.setEnabled(true);
            childViewHolder.ipdCheckBox.setEnabled(true);
        }

        childViewHolder.opdCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxClicked(isChecked, groupPosition, childPosition);
            }
        });
        childViewHolder.ipdCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkBoxClicked(isChecked, groupPosition, childPosition);
            }
        });

        childViewHolder.moreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lessText = _context.getString(R.string.less);
                String moreText = _context.getString(R.string.more);
                String textString = childViewHolder.moreOption.getText().toString();
                if (textString.equalsIgnoreCase(lessText)) {
                    manageChild(null);
                    childViewHolder.moreOption.setText(moreText);
                } else {
                    String groupName = (String) v.getTag();
                    manageChild(groupName);
                    childViewHolder.moreOption.setText(lessText);
                }

            }
        });

        //--------------------

        childViewHolder.rowLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onPatientListener.onPatientListItemClick(childElement, getGroup(groupPosition).getPatientName());

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
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_patient_list_header, parent, false);

            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);

        SearchResult groupHeader = getGroup(groupPosition);
//        int childrenCount = getChildrenCount(groupPosition);

        groupViewHolder.userName.setText(groupHeader.getPatientName());
        groupViewHolder.patientId.setText(groupHeader.getPatientId());
        groupViewHolder.uhid.setText(uhid);

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
        @BindView(R.id.userName)
        TextView userName;
        @BindView(R.id.uhid)
        TextView uhid;
        @BindView(R.id.patientId)
        TextView patientId;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ChildViewHolder {
        //---------

        @BindView(R.id.rowLay)
        LinearLayout rowLay;
        @BindView(R.id.dividerLineMore)
        View mDividerLineMore;
        @BindView(R.id.opd)
        TextView opd;
        @BindView(R.id.opdValue)
        TextView opdValue;
        @BindView(R.id.opdVisitDate)
        TextView opdVisitDate;
        @BindView(R.id.opdVisitDateValue)
        TextView opdVisitDateValue;
        @BindView(R.id.opdCheckBox)
        CheckBox opdCheckBox;
        //----
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
        //----
        @BindView(R.id.opdLayout)
        LinearLayout opdLayout;
        @BindView(R.id.ipdLayout)
        LinearLayout ipdLayout;
        @BindView(R.id.moreOption)
        TextView moreOption;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    private void manageChild(String groupName) {

        _listDataChild.clear();
        _listDataHeader.clear();

        _listDataHeader.addAll(_originalListDataHeader);
        //-------

        for (Map.Entry<String, ArrayList<PatientFileData>> pair : _originalListDataChild.entrySet()) {

            ArrayList<PatientFileData> value = pair.getValue();

            if ((pair.getKey()).equalsIgnoreCase(groupName)) {
                //----------
                for (PatientFileData dataObject :
                        value) {
                    dataObject.setShowCompleteList(true);
                }
                //---------
                _listDataChild.put(pair.getKey(), value);
            } else {
                //----------
                for (PatientFileData dataObject :
                        value) {
                    dataObject.setShowCompleteList(false);
                }
                //---------
                if (value.size() > dataShowMaxValue) {
                    ArrayList<PatientFileData> tempList = new ArrayList<>();
                    for (int i = 0; i < dataShowMaxValue; i++) {
                        tempList.add(value.get(i));
                    }
                    _listDataChild.put(pair.getKey(), tempList);
                } else {
                    _listDataChild.put(pair.getKey(), value);
                }
            }
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

}