package com.scorg.dms.adapters.patient_detail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.model.case_details.PatientHistory;
import com.scorg.dms.model.case_details.Range;
import com.scorg.dms.model.case_details.VisitCommonData;
import com.scorg.dms.model.case_details.Vital;
import com.scorg.dms.util.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 5/2/18.
 */

public class PatientDetailAdapter extends BaseExpandableListAdapter {
    private int mPosition = 0;
    private Context mContext;
    private static final String CHILD_TYPE_1 = "vitals";
    private List<PatientHistory> mListDataHeader = new ArrayList<>(); // header titles
    List<VisitCommonData> mVisitDetailList = new ArrayList<>();
    List<VisitCommonData> mCommonDataVisitList = new ArrayList<>();
    public static final int TEXT_LIMIT = 36;

    public PatientDetailAdapter(Context context, List<PatientHistory> listDataHeader) {
        this.mContext = context;
        //  case details with no data are not added in the list
        for (int i = 0; i < listDataHeader.size(); i++) {
            List<VisitCommonData> commonData = listDataHeader.get(i).getCommonData();
            List<Vital> commonDatasVitals = listDataHeader.get(i).getVitals();
            if (!(commonData == null)) {
                if (commonData.size() > 0) {
                    mListDataHeader.add(listDataHeader.get(i));
                }
            } else if (!(commonDatasVitals == null)) {
                if (commonDatasVitals.size() > 0) {
                    VisitCommonData commonVitals = new VisitCommonData();
                    commonVitals.setId(0);
                    commonVitals.setVitalValue(listDataHeader.get(i).getVitals().get(0).getUnitValue());
                    commonVitals.setName(listDataHeader.get(i).getVitals().get(0).getUnitName());
                    mCommonDataVisitList.add(commonVitals);
                    listDataHeader.get(i).setCommonData(mCommonDataVisitList);
                    mListDataHeader.add(listDataHeader.get(i));
                }
            }
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return (this.mListDataHeader.get(groupPosition).getCommonData())
                .get(childPosititon);
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        // Onclick of vitals UI is different from those of other case details
        final List<VisitCommonData> childObject = mListDataHeader.get(groupPosition).getCommonData();
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        String headerName = mListDataHeader.get(groupPosition).getCaseDetailName();
        switch (headerName) {
            case CHILD_TYPE_1:
                //set data and UI for vitals
                convertView = inflater.inflate(R.layout.case_details_vitals_item_layout, null);
                convertView.setTag(headerName);
                TableLayout tableLayout = (TableLayout) convertView.findViewById(R.id.table);
                View divider = convertView.findViewById(R.id.adapter_divider);
                tableLayout.removeAllViews();
                mPosition = 0;
                List<Vital> vital = new ArrayList<>();
                int size = mListDataHeader.get(groupPosition).getVitals().size();
                int count = 1;
                int tempSize = size - (size % 3);
                for (int i = 0; i < size; i++) {
                    vital.add(mListDataHeader.get(groupPosition).getVitals().get(i));
                    if (tempSize > i) {
                        if (count == 3) {
                            tableLayout.addView(addTableRow(vital, groupPosition));
                            vital.clear();
                            count = 1;
                        } else
                            count++;
                    } else if (count == size % 3) {
                        tableLayout.addView(addTableRow(vital, groupPosition));
                        vital.clear();
                        count = 1;
                    } else count++;
                }

                if (isLastChild)
                    divider.setVisibility(View.VISIBLE);
                else
                    divider.setVisibility(View.GONE);

                break;

            default:
                // set data and UI for other case study
                convertView = inflater.inflate(R.layout.vital_item_row, null);
                convertView.setTag(headerName);
                TextView txtListChild = (TextView) convertView.findViewById(R.id.textView_name);
                View dividerLine = convertView.findViewById(R.id.adapter_divider_bottom);
                txtListChild.setText(childObject.get(childPosition).getName());

                if (isLastChild) {
                    dividerLine.setVisibility(View.VISIBLE);
                } else {
                    dividerLine.setVisibility(View.GONE);
                }
                break;
        }

        return convertView;
    }

    // Created dynamic grid function to list of  vitals
    private View addTableRow(final List<Vital> vital, final int groupPosition) {
        int i;
        String categoryForBpMax = "";
        String categoryForBpMin = "";
        TableRow tableRow = new TableRow(mContext);
        for (i = 0; i < vital.size(); i++) {
            View item = LayoutInflater.from(mContext)
                    .inflate(R.layout.vital_item_row, tableRow, false);
            LinearLayout vitalLinearlayout = (LinearLayout) item.findViewById(R.id.vitalCellLinearLayout);
            ImageView vitalImage = (ImageView) item.findViewById(R.id.vitalImage);
            TextView vital_name = (TextView) item.findViewById(R.id.vital_name);
            TextView noOfVitals = (TextView) item.findViewById(R.id.noOfVitals);
            LinearLayout unitValuesLayout = (LinearLayout) item.findViewById(R.id.unitValuesLayout);
            final int finali = mPosition;
            //dialog is opened to see info of vitals , Note : BpMin and BpMax is together shown as Bp
            vitalLinearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vital vitalClickedObject = mListDataHeader.get(groupPosition).getVitals().get(finali);
                    showVitalDialog(mContext, vitalClickedObject.getUnitName(),
                            vitalClickedObject.getUnitValue(),
                            vitalClickedObject.getRanges(),
                            CommonMethods.getVitalIcons(vitalClickedObject.getIcon()),
                            vitalClickedObject.getCategory(),
                            vitalClickedObject.getDisplayName());
                }
            });

            if (mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitName().equals(mContext.getString(R.string.bp))) {
                String category = mListDataHeader.get(groupPosition).getVitals().get(mPosition).getCategory();
                String[] categoryForBp = category.split(":");
                categoryForBpMax = categoryForBp[0];
                categoryForBpMin = categoryForBp.length == 2 ? categoryForBp[1] : "";

                String unit = mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitValue();
                String[] unitForBp = unit.split("/");
                String unitForBpMax = unitForBp[0];
                String unitForBpMin = unitForBp.length == 2 ? unitForBp[1] : "";
                vitalImage.setImageResource(CommonMethods.getVitalIcons(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getIcon()));

                vital_name.setText(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitName());

                noOfVitals.setText(Html.fromHtml(getUnitValueforBp(categoryForBpMin, categoryForBpMax, unitForBpMin, unitForBpMax)));

            } else {
                //TextColor of vital unit is set according to category
                vitalImage.setImageResource(CommonMethods.getVitalIcons(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getIcon()));

                //---*************** Show vaital_display_name instead of unitName (EXCEPT BP CASE) : START
                //  vital_name.setText(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitName());
                vital_name.setText(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getDisplayName());
                //---*************** Show vaital_display_name instead of unitName : END

                noOfVitals.setText(mListDataHeader.get(groupPosition).getVitals().get(mPosition).getUnitValue());
                if (mListDataHeader.get(groupPosition).getVitals().get(mPosition).getCategory().equalsIgnoreCase(mContext.getResources().getString(R.string.severeRange))) {
                    noOfVitals.setTextColor(ContextCompat.getColor(mContext, R.color.Red));
                } else if (mListDataHeader.get(groupPosition).getVitals().get(mPosition).getCategory().equalsIgnoreCase(mContext.getResources().getString(R.string.normalRange))) {
                    noOfVitals.setTextColor(ContextCompat.getColor(mContext, R.color.range_green));
                } else if (mListDataHeader.get(groupPosition).getVitals().get(mPosition).getCategory().equalsIgnoreCase(mContext.getResources().getString(R.string.moderateRange))) {
                    noOfVitals.setTextColor(ContextCompat.getColor(mContext, R.color.range_yellow));
                } else {
                    noOfVitals.setTextColor(ContextCompat.getColor(mContext, R.color.view_detail_color));
                }
            }
            tableRow.addView(item);
            mPosition++;
        }
        return tableRow;
    }

    //set Unit Value of Bp according to category
    private String getUnitValueforBp(String categoryForBpMin, String categoryForBpMax, String forBpMin, String forBpMax) {
        String unitValue = "";
        if (categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.select_all)) && categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.severeRange))) {
            String bpMax = "<font color='#FF0000'>" + forBpMax + "</font>";
            String bpMin = "<font color='#FF0000'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;


        } else if (categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.severeRange)) && categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.moderateRange))) {
            String bpMax = "<font color='#ff9500'>" + forBpMax + "</font>";
            String bpMin = "<font color='#FF0000'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.severeRange)) && categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.normalRange))) {
            String bpMax = "<font color='#178a00'>" + forBpMax + "</font>";
            String bpMin = "<font color='#FF0000'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.normalRange)) && categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.severeRange))) {
            String bpMax = "<font color='#FF0000'>" + forBpMax + "</font>";
            String bpMin = "<font color='#178a00'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.normalRange)) && categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.moderateRange))) {
            String bpMax = "<font color='#ff9500'>" + forBpMax + "</font>";
            String bpMin = "<font color='#178a00'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.normalRange)) && categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.normalRange))) {
            String bpMax = "<font color='#178a00'>" + forBpMax + "</font>";
            String bpMin = "<font color='#178a00'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) && categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.severeRange))) {
            String bpMax = "<font color='#FF0000'>" + forBpMax + "</font>";
            String bpMin = "<font color='#ff9500'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) && categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.moderateRange))) {
            String bpMax = "<font color='#ff9500'>" + forBpMax + "</font>";
            String bpMin = "<font color='#ff9500'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) && categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.normalRange))) {
            String bpMax = "<font color='#178a00'>" + forBpMax + "</font>";
            String bpMin = "<font color='#ff9500'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.severeRange)) && categoryForBpMin.equalsIgnoreCase("")) {
            String bpMax = "<font color='#FF0000'>" + forBpMax + "</font>";
            String bpMin = "<font color='#737373'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) && categoryForBpMin.equalsIgnoreCase("")) {
            String bpMax = "<font color='#ff9500'>" + forBpMax + "</font>";
            String bpMin = "<font color='#737373'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMax.equalsIgnoreCase(mContext.getString(R.string.normalRange)) && categoryForBpMin.equalsIgnoreCase("")) {
            String bpMax = "<font color='#178a00'>" + forBpMax + "</font>";
            String bpMin = "<font color='#737373'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMax.equalsIgnoreCase("") && categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.severeRange))) {
            String bpMax = "<font color='#737373'>" + forBpMax + "</font>";
            String bpMin = "<font color='#FF0000'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMax.equalsIgnoreCase("") && categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.moderateRange))) {
            String bpMax = "<font color='#737373'>" + forBpMax + "</font>";
            String bpMin = "<font color='#ff9500'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else if (categoryForBpMax.equalsIgnoreCase("") && categoryForBpMin.equalsIgnoreCase(mContext.getString(R.string.normalRange))) {
            String bpMax = "<font color='#737373'>" + forBpMax + "</font>";
            String bpMin = "<font color='#178a00'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;

        } else {
            String bpMax = "<font color='#737373'>" + forBpMax + "</font>";
            String bpMin = "<font color='#737373'>" + forBpMin + "</font>";
            String slash = "<font color='#737373'>" + "/" + "</font>";
            unitValue = bpMax + slash + bpMin;
        }
        return unitValue;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (this.mListDataHeader.get(groupPosition).getCommonData())
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mListDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        final GroupViewHolder groupViewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.case_details_group_item_layout, parent, false);

            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        if (isExpanded) {
            groupViewHolder.mDetailFirstPoint.setVisibility(View.GONE);
            groupViewHolder.mHeadergroupDivider.setVisibility(View.VISIBLE);
            groupViewHolder.mDivider.setVisibility(View.GONE);
        } else {
            groupViewHolder.mDetailFirstPoint.setVisibility(View.VISIBLE);
            groupViewHolder.mHeadergroupDivider.setVisibility(View.GONE);
            groupViewHolder.mDivider.setVisibility(View.VISIBLE);
        }

        String s1 = mListDataHeader.get(groupPosition).getCaseDetailName();
        groupViewHolder.lblListHeader.setText(s1.substring(0, 1).toUpperCase() + s1.substring(1));
        groupViewHolder.mViewDetailIcon.setImageResource(CommonMethods.getCaseStudyIcons(mListDataHeader.get(groupPosition).getCaseDetailName()));
        if (mListDataHeader.get(groupPosition).getCommonData() != null) {
            mVisitDetailList = mListDataHeader.get(groupPosition).getCommonData();

            if (mListDataHeader.get(groupPosition).getCaseDetailName().equalsIgnoreCase("vitals")) {
                if (!mListDataHeader.get(groupPosition).getVitals().isEmpty())
                    if(mVisitDetailList.get(0).getName().equalsIgnoreCase("bp")){
                        String bpValue = mVisitDetailList.get(0).getVitalValue();
                        String[] unitDataObject = bpValue.split("/");
                        String unitBpMax = unitDataObject[0];
                        String unitBpMin = unitDataObject.length == 2 ? unitDataObject[1] : "";
                        groupViewHolder.mDetailFirstPoint.setText(mVisitDetailList.get(0).getName() + " "+unitBpMax+"...");

                    }else{
                        groupViewHolder.mDetailFirstPoint.setText(mVisitDetailList.get(0).getName() + "...");

                    }
            } else if (mVisitDetailList.size() > 1) {
                groupViewHolder.mDetailFirstPoint.setText(mVisitDetailList.get(0).getName() + "...");
            } else {
//                SpannableString s = CommonMethods.addTextToStringAtLast(mVisitDetailList.get(0).getSenderName(), 5, "...", ContextCompat.getColor(mContext, R.color.view_detail_color));
                String text = mVisitDetailList.get(0).getName();
                if (text.length() > TEXT_LIMIT)
                    groupViewHolder.mDetailFirstPoint.setText(text.substring(0, TEXT_LIMIT - 1) + "...");
                else groupViewHolder.mDetailFirstPoint.setText(text);
            }
            //groupViewHolder.mDetailFirstPoint.setText(setStringLength(mVisitDetailList.get(0).getSenderName()));// + ".......");

        }
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

    static class ChildViewHolder {
        //---------

        @BindView(R.id.textView_name)
        TextView txtListChild;
        @BindView(R.id.adapter_divider_bottom)
        View mDividerLine;
        @BindView(R.id.expandVisitDetailsLayout)
        LinearLayout mExpandVisitDetailsLayout;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class GroupViewHolder {
        //---------

        @BindView(R.id.viewDetailHeaderLabel)
        TextView lblListHeader;
        @BindView(R.id.viewDetailIcon)
        ImageView mViewDetailIcon;
        @BindView(R.id.headergroupDivider)
        View mHeadergroupDivider;
        @BindView(R.id.adapter_divider_top)
        View mDivider;
        @BindView(R.id.detailFirstPoint)
        TextView mDetailFirstPoint;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    public String setStringLength(String t) {
        String o = "";
        if (t.length() >= 30) {
            o = t.substring(0, 30);
            System.out.println(o);
            return o;
        } else {
            System.out.println(t);
            return t;
        }
    }

    public Dialog showVitalDialog(Context context, String unitName, String unitValue, List<Range> rangeList, Integer drawable, String category, String vitalDisplayName) {

        final Dialog dialog = new Dialog(context);
        String categoryForBpMax = "";
        String categoryBpMin = "";
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.vitals_dialog_layout);
        String normal = "";
        String moderate = "";
        String severe = "";
        String moderateBpmin = "";
        String normalBpmin = "";
        String severeBpmin = "";


        LinearLayout vitalsDialogLayout = (LinearLayout) dialog.findViewById(R.id.vitalsDialogLayout);
        LinearLayout normalBpMaxRangeLayout = (LinearLayout) dialog.findViewById(R.id.normalRangeLayout);
        LinearLayout showVitalUnitNameIconLayout = (LinearLayout) dialog.findViewById(R.id.showVitalUnitNameIconLayout);
        LinearLayout moderateBpMaxRangeLayout = (LinearLayout) dialog.findViewById(R.id.moderateRangeLayout);
        LinearLayout severeBpMaxRangeLayout = (LinearLayout) dialog.findViewById(R.id.severeRangeLayout);
        LinearLayout showVitalNameLayout = (LinearLayout) dialog.findViewById(R.id.showVitalNameLayout);
        TextView normalBpMaxRange = (TextView) dialog.findViewById(R.id.normalRange);
        TextView moderateBpMaxRange = (TextView) dialog.findViewById(R.id.moderateRange);
        TextView severeBpMaxRange = (TextView) dialog.findViewById(R.id.severeRange);
        LinearLayout showVitalRangeLayout = (LinearLayout) dialog.findViewById(R.id.showVitalRangeLayout);
        TextView vitalTypeNameDialog = (TextView) dialog.findViewById(R.id.vitalTypeNameDialog);
        TextView noOfVitalsTypeDialog = (TextView) dialog.findViewById(R.id.noOfVitalsTypeDialog);
        TextView normalRange = (TextView) dialog.findViewById(R.id.normalSubTypeRange);
        TextView moderateRange = (TextView) dialog.findViewById(R.id.moderateSubTypeRange);
        TextView severeRange = (TextView) dialog.findViewById(R.id.severeSubTypeRange);
        TextView noOfVitalsDialog = (TextView) dialog.findViewById(R.id.noOfVitalsDialog);
        TextView vitalName = (TextView) dialog.findViewById(R.id.vitalNameDialog);
        LinearLayout bpMinLayout = (LinearLayout) dialog.findViewById(R.id.bpMinLayout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        for (int i = 0; i < rangeList.size(); i++) {
            if (rangeList.get(i).getNameOfVital() != null) {
                if (rangeList.get(i).getNameOfVital().equalsIgnoreCase(context.getString(R.string.bp_max))) {
                    if (rangeList.get(i).getCategory().equalsIgnoreCase(context.getString(R.string.normalRange))) {
                        if (normal.equals("")) {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            normal += finalString;
                            normalBpMaxRange.setText(normal);
                        } else {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            normal += ", " + finalString;
                            normalBpMaxRange.setText(normal);
                        }
                    } else if (rangeList.get(i).getCategory().equalsIgnoreCase(context.getString(R.string.moderateRange))) {
                        if (moderate.equals("")) {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            moderate += finalString;
                            moderateBpMaxRange.setText(moderate);
                        } else {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            moderate += ", " + finalString;
                            moderateBpMaxRange.setText(moderate);
                        }
                    } else if (rangeList.get(i).getCategory().equalsIgnoreCase(context.getString(R.string.severeRange))) {
                        if (severe.equals("")) {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            severe += finalString;
                            severeBpMaxRange.setText(severe);
                        } else {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            severe += ", " + finalString;
                            severeBpMaxRange.setText(severe);

                        }
                    }
                } else if (rangeList.get(i).getNameOfVital().equalsIgnoreCase(context.getString(R.string.bp_min))) {

                    if (rangeList.get(i).getCategory().equalsIgnoreCase(context.getString(R.string.normalRange))) {
                        if (normal.equals("")) {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            normal += finalString;
                            normalRange.setText(normal);
                        } else {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            normal += ", " + finalString;
                            normalRange.setText(normal);
                        }
                    } else if (rangeList.get(i).getCategory().equalsIgnoreCase(context.getString(R.string.moderateRange))) {
                        if (moderate.equals("")) {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            moderate += finalString;
                            moderateRange.setText(moderate);
                        } else {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            moderate += ", " + finalString;
                            moderateRange.setText(moderate);
                        }
                    } else if (rangeList.get(i).getCategory().equalsIgnoreCase(context.getString(R.string.severeRange))) {
                        if (severe.equals("")) {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            severe += finalString;
                            severeRange.setText(severe);
                        } else {
                            String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                            severe += ", " + finalString;
                            severeRange.setText(severe);
                        }
                    }

                }
            } else if (rangeList.get(i).getNameOfVital() == null) {
                if (rangeList.get(i).getCategory().equalsIgnoreCase(context.getString(R.string.normalRange))) {
                    if (normalBpmin.equals("")) {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                        normalBpmin += finalString;
                        normalRange.setText(normalBpmin);
                    } else {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                        normalBpmin += ", " + finalString;
                        normalRange.setText(normalBpmin);
                    }
                } else if (rangeList.get(i).getCategory().equalsIgnoreCase(context.getString(R.string.moderateRange))) {
                    if (moderateBpmin.equals("")) {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                        moderateBpmin += finalString;
                        moderateRange.setText(moderateBpmin);
                    } else {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                        moderateBpmin += ", " + finalString;
                        moderateRange.setText(moderateBpmin);
                    }
                } else if (rangeList.get(i).getCategory().equalsIgnoreCase(context.getString(R.string.severeRange))) {
                    if (severeBpmin.equals("")) {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                        severeBpmin += finalString;
                        severeRange.setText(severeBpmin);
                    } else {
                        String finalString = getSortedRangeValues(rangeList.get(i).getCategory(), rangeList.get(i).getOperator(), rangeList.get(i).getValue(), rangeList.get(i).getMin(), rangeList.get(i).getMax());
                        severeBpmin += ", " + finalString;
                        severeRange.setText(severeBpmin);

                    }
                }
            }
        }
        if (unitName.equals(context.getString(R.string.bp))) {
            String[] unitDataObject = unitValue.split("/");
            String unitBpMax = unitDataObject[0];
            String unitBpMin = unitDataObject.length == 2 ? unitDataObject[1] : "";
            showVitalNameLayout.setVisibility(View.VISIBLE);
            showVitalRangeLayout.setVisibility(View.VISIBLE);
            vitalName.setText(context.getString(R.string.systolic_pressure));
            if (!category.equals(":")) {
                String[] categoryForBp = category.split(":");
                categoryForBpMax = categoryForBp[0];
                categoryBpMin = categoryForBp.length == 2 ? categoryForBp[1] : "";
            }
            if (categoryForBpMax.equalsIgnoreCase(context.getString(R.string.severeRange))) {
                noOfVitalsDialog.setTextColor(ContextCompat.getColor(context, R.color.Red));
            } else if (categoryForBpMax.equalsIgnoreCase(context.getString(R.string.normalRange))) {
                noOfVitalsDialog.setTextColor(ContextCompat.getColor(context, R.color.range_green));
            } else if (categoryForBpMax.equalsIgnoreCase(context.getString(R.string.moderateRange))) {
                noOfVitalsDialog.setTextColor(ContextCompat.getColor(context, R.color.range_yellow));
            } else {
                noOfVitalsDialog.setTextColor(ContextCompat.getColor(context, R.color.view_detail_color));
            }

            if (categoryBpMin.equalsIgnoreCase(context.getString(R.string.severeRange))) {
                noOfVitalsTypeDialog.setTextColor(ContextCompat.getColor(context, R.color.Red));
            } else if (categoryBpMin.equalsIgnoreCase(context.getString(R.string.normalRange))) {
                noOfVitalsTypeDialog.setTextColor(ContextCompat.getColor(context, R.color.range_green));
            } else if (categoryBpMin.equalsIgnoreCase(context.getString(R.string.moderateRange))) {
                noOfVitalsTypeDialog.setTextColor(ContextCompat.getColor(context, R.color.range_yellow));
            } else {
                noOfVitalsTypeDialog.setTextColor(ContextCompat.getColor(context, R.color.view_detail_color));
            }
            noOfVitalsDialog.setText(unitBpMax);
            noOfVitalsTypeDialog.setText(unitBpMin);
            vitalTypeNameDialog.setText(context.getString(R.string.diastolic_pressure));
            if (normalBpMaxRange.getText().toString().trim().length() == 0) {
                normalBpMaxRangeLayout.setVisibility(View.GONE);
            }
            if (moderateBpMaxRange.getText().toString().trim().length() == 0) {
                moderateBpMaxRangeLayout.setVisibility(View.GONE);
            }
            if (severeBpMaxRange.getText().toString().trim().length() == 0) {
                severeBpMaxRangeLayout.setVisibility(View.GONE);
            }
            if (normalBpMaxRange.getText().toString().trim().length() == 0 && moderateBpMaxRange.getText().toString().trim().length() == 0 && severeBpMaxRange.getText().toString().trim().length() == 0) {
                showVitalRangeLayout.setVisibility(View.GONE);
                showVitalNameLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.vitals_curve_white_bg));
                bpMinLayout.setVisibility(View.GONE);
                showVitalUnitNameIconLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.vitals_curve_grey_bg));
            }
            if (normalRange.getText().toString().trim().length() == 0) {
                LinearLayout normalRangeLayout = (LinearLayout) dialog.findViewById(R.id.normalSubTypeRangeLayout);
                normalRangeLayout.setVisibility(View.GONE);
            }
            if (moderateRange.getText().toString().trim().length() == 0) {
                LinearLayout moderateRangeLayout = (LinearLayout) dialog.findViewById(R.id.moderateSubTypeRangeLayout);
                moderateRangeLayout.setVisibility(View.GONE);
            }
            if (severeRange.getText().toString().trim().length() == 0) {
                LinearLayout severeRangeLayout = (LinearLayout) dialog.findViewById(R.id.severeSubTypeRangeLayout);
                severeRangeLayout.setVisibility(View.GONE);
            }
            if (normalRange.getText().toString().trim().length() == 0 && moderateRange.getText().toString().trim().length() == 0 && severeRange.getText().toString().trim().length() == 0) {
                bpMinLayout.setVisibility(View.GONE);
                showVitalUnitNameIconLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.vitals_curve_grey_bg));
            }

        } else {

            showVitalNameLayout.setVisibility(View.GONE);
            showVitalRangeLayout.setVisibility(View.GONE);
            noOfVitalsDialog.setText(unitValue);
            //---*************** Show vaital_display_name instead of unitName : START
            //vitalName.setText(unitName);
            vitalName.setText(vitalDisplayName);
            //---*************** Show vaital_display_name instead of unitName : END
            if (category.equalsIgnoreCase(context.getString(R.string.severeRange))) {
                noOfVitalsDialog.setTextColor(ContextCompat.getColor(context, R.color.Red));
            } else if (category.equalsIgnoreCase(context.getString(R.string.normalRange))) {
                noOfVitalsDialog.setTextColor(ContextCompat.getColor(context, R.color.range_green));
            } else if (category.equalsIgnoreCase(context.getString(R.string.moderateRange))) {
                noOfVitalsDialog.setTextColor(ContextCompat.getColor(context, R.color.range_yellow));
            }
            if (normalRange.getText().toString().trim().length() == 0) {
                LinearLayout normalRangeLayout = (LinearLayout) dialog.findViewById(R.id.normalSubTypeRangeLayout);
                normalRangeLayout.setVisibility(View.GONE);
            }
            if (moderateRange.getText().toString().trim().length() == 0) {
                LinearLayout moderateRangeLayout = (LinearLayout) dialog.findViewById(R.id.moderateSubTypeRangeLayout);
                moderateRangeLayout.setVisibility(View.GONE);
            }
            if (severeRange.getText().toString().trim().length() == 0) {
                LinearLayout severeRangeLayout = (LinearLayout) dialog.findViewById(R.id.severeSubTypeRangeLayout);
                severeRangeLayout.setVisibility(View.GONE);
            }
            if (normalRange.getText().toString().trim().length() == 0 && moderateRange.getText().toString().trim().length() == 0 && severeRange.getText().toString().trim().length() == 0) {
                bpMinLayout.setVisibility(View.GONE);
                showVitalUnitNameIconLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.vital_curve_allcorners_grey));
            }
        }

        ((ImageView) dialog.findViewById(R.id.vitalImageDialog)).setImageResource(drawable);

        vitalsDialogLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(true);
        dialog.show();

        return dialog;
    }

    private String getSortedRangeValues(String category, String operator, String value, String min, String max) {
        String range = "";
        if (category.equalsIgnoreCase(mContext.getString(R.string.normalRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.less))) {
            range = mContext.getString(R.string.less_than_sign) + value;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.normalRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.equal))) {
            range = min + mContext.getString(R.string.dash) + max;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.normalRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.greater))) {
            range = mContext.getString(R.string.greater_than_sign) + value;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.less))) {
            range = mContext.getString(R.string.less_than_sign) + value;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.equal))) {
            range = min + mContext.getString(R.string.dash) + max;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.moderateRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.greater))) {
            range = mContext.getString(R.string.greater_than_sign) + value;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.severeRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.less))) {
            range = mContext.getString(R.string.less_than_sign) + value;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.severeRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.equal))) {
            range = min + mContext.getString(R.string.dash) + max;
        } else if (category.equalsIgnoreCase(mContext.getString(R.string.severeRange)) && operator.equalsIgnoreCase(mContext.getString(R.string.greater))) {
            range = mContext.getString(R.string.greater_than_sign) + value;
        }
        return range;
    }

    public List<PatientHistory> getListDataList() {
        return mListDataHeader;
    }


}
