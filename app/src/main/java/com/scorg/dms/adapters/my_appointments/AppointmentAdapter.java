package com.scorg.dms.adapters.my_appointments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.scorg.dms.R;
import com.scorg.dms.model.my_appointments.AppointmentList;
import com.scorg.dms.model.my_appointments.PatientList;
import com.scorg.dms.ui.customesViews.CircularImageView;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.ui.customesViews.swipeable_recyclerview.SwipeRevealLayout;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.scorg.dms.ui.fragments.my_appointments.MyAppointmentsFragment.isLongPressed;
import static com.scorg.dms.util.CommonMethods.toCamelCase;
import static com.scorg.dms.util.DMSConstants.APPOINTMENT_STATUS.BOOKED;
import static com.scorg.dms.util.DMSConstants.APPOINTMENT_STATUS.CANCEL;
import static com.scorg.dms.util.DMSConstants.APPOINTMENT_STATUS.COMPLETED;
import static com.scorg.dms.util.DMSConstants.APPOINTMENT_STATUS.CONFIRM;
import static com.scorg.dms.util.DMSConstants.APPOINTMENT_STATUS.NO_SHOW;
import static com.scorg.dms.util.DMSConstants.APPOINTMENT_STATUS.OTHER;

/**
 * Created by jeetal on 31/1/18.
 */

public class AppointmentAdapter extends BaseExpandableListAdapter implements Filterable {

    private OnDownArrowClicked mOnDownArrowClicked;
    private ArrayList<AppointmentList> mAppointmentListTemp;
    private Context mContext;
    private ArrayList<AppointmentList> mDataList;
    public String openedChildGroupPos = "";
    private Object slidingViewHolder;

    public AppointmentAdapter(Context context, ArrayList<AppointmentList> mAppointmentList, OnDownArrowClicked mOnDownArrowClicked) {
        this.mContext = context;
        this.mDataList = new ArrayList<>(mAppointmentList);
        this.mAppointmentListTemp = new ArrayList<>(mAppointmentList);
        this.mOnDownArrowClicked = mOnDownArrowClicked;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.mAppointmentListTemp.get(groupPosition).getPatientList().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ChildViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.my_appointment_child_item, parent, false);
            viewHolder = new ChildViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }

        final PatientList patientObject = mAppointmentListTemp.get(groupPosition).getPatientList().get(childPosition);
        final String cityName = mAppointmentListTemp.get(groupPosition).getCity();
        final String areaName = mAppointmentListTemp.get(groupPosition).getArea();
        bind(patientObject, groupPosition, childPosition, viewHolder, cityName, areaName);

        if (openedChildGroupPos.equals(childPosition + "_" + groupPosition))
            viewHolder.swipe_layout.open(true);
        else viewHolder.swipe_layout.close(true);

        viewHolder.swipe_layout.setSwipeListener(new SwipeRevealLayout.SwipeListener() {
            @Override
            public void onClosed(SwipeRevealLayout view) {
                if (slidingViewHolder != null && view.getTag() != null) {
                    if (view.getTag().equals(slidingViewHolder))
                        openedChildGroupPos = "";
                }
            }

            @Override
            public void onOpened(SwipeRevealLayout view) {
                openedChildGroupPos = childPosition + "_" + groupPosition;
                notifyDataSetChanged();
            }

            @Override
            public void onSlide(SwipeRevealLayout view, float slideOffset) {
                Log.d("SLIDE", String.valueOf(slideOffset) + " " + view.getTag());
                slidingViewHolder = view.getTag();
            }
        });

        if (patientObject.getAppointmentStatusId().equals(BOOKED) || patientObject.getAppointmentStatusId().equals(CONFIRM))
            viewHolder.swipe_layout.setLockDrag(false);
        else
            viewHolder.swipe_layout.setLockDrag(true);

        return convertView;

    }

    @SuppressLint("CheckResult")
    private void bind(final PatientList patientList, final int groupPosition, final int childPosition, final ChildViewHolder viewHolder, final String cityName, final String areaName) {

        String patientName = "";

        if (patientList.getSalutation() != 0)
            patientName = DMSConstants.SALUTATION[patientList.getSalutation() - 1] + toCamelCase(patientList.getPatientName());
        else patientName = toCamelCase(patientList.getPatientName());

        //---- START: Setting of hospitalID or referecne ID, reference is IS high priority than hospitalID.-----
        String dataToShowInPatientID = String.valueOf(patientList.getReferenceId());

        if (dataToShowInPatientID == null || DMSConstants.BLANK.equalsIgnoreCase(dataToShowInPatientID)) {
            dataToShowInPatientID = String.valueOf(patientList.getHospitalPatId());
        }
        //---- END------

        if (patientList.getSpannableString() != null) {
            //Spannable condition for PatientName
            if (patientList.getPatientName().toLowerCase().contains(patientList.getSpannableString().toLowerCase())) {
                SpannableString spannableString = new SpannableString(patientName);
                Pattern pattern = Pattern.compile(patientList.getSpannableString(), Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(patientName);
                while (matcher.find()) {
                    spannableString.setSpan(new ForegroundColorSpan(
                                    ContextCompat.getColor(mContext, R.color.tagColor)),
                            matcher.start(), matcher.end(),//hightlight mSearchString
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                viewHolder.patientNameTextView.setText(spannableString);
            } else {
                viewHolder.patientNameTextView.setText(patientName);
            }
            //Spannable condition for PatientPhoneNumber

            if (patientList.getPatientPhone().toLowerCase().contains(patientList.getSpannableString().toLowerCase())) {
                SpannableString spannablePhoneString = new SpannableString(patientList.getPatientPhone());
                Pattern pattern = Pattern.compile(patientList.getSpannableString(), Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(patientList.getPatientPhone());
                while (matcher.find()) {
                    spannablePhoneString.setSpan(new ForegroundColorSpan(
                                    ContextCompat.getColor(mContext, R.color.tagColor)),
                            matcher.start(), matcher.end(),//hightlight mSearchString
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                viewHolder.patientPhoneNumber.setText(spannablePhoneString);
            } else {
                viewHolder.patientPhoneNumber.setText(patientList.getPatientPhone());
            }
            //Spannable condition for PatientId
            if (dataToShowInPatientID.toLowerCase().contains(patientList.getSpannableString().toLowerCase())) {

                SpannableString spannableIdString = new SpannableString(mContext.getString(R.string.id) + " " + dataToShowInPatientID);
                Pattern pattern = Pattern.compile(patientList.getSpannableString(), Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(mContext.getString(R.string.id) + " " + dataToShowInPatientID);

                while (matcher.find()) {
                    spannableIdString.setSpan(new ForegroundColorSpan(
                                    ContextCompat.getColor(mContext, R.color.tagColor)),
                            matcher.start(), matcher.end(),//hightlight mSearchString
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                viewHolder.patientIdTextView.setText(spannableIdString);
            } else {
                viewHolder.patientIdTextView.setText(mContext.getString(R.string.id) + " " + dataToShowInPatientID);
            }
        } else {
            viewHolder.patientNameTextView.setText(patientName);
            viewHolder.patientPhoneNumber.setText(patientList.getPatientPhone());
            viewHolder.patientIdTextView.setText(mContext.getString(R.string.id) + " " + dataToShowInPatientID);
        }

        if (patientList.getAge().equals("") && !patientList.getDateOfBirth().equals("")) {
            viewHolder.patientAgeTextView.setVisibility(View.VISIBLE);
            String getTodayDate = CommonMethods.getCurrentDate(DMSConstants.DATE_PATTERN.YYYY_MM_DD);
            String getBirthdayDate = patientList.getDateOfBirth();
            DateTime todayDateTime = CommonMethods.convertToDateTime(getTodayDate, DMSConstants.DATE_PATTERN.YYYY_MM_DD);
            DateTime birthdayDateTime = CommonMethods.convertToDateTime(getBirthdayDate, DMSConstants.DATE_PATTERN.YYYY_MM_DD);
            viewHolder.patientAgeTextView.setText(CommonMethods.displayAgeAnalysis(todayDateTime, birthdayDateTime) + " " + mContext.getString(R.string.years));
        } else if (!patientList.getAge().equals("")) {
            viewHolder.patientAgeTextView.setVisibility(View.VISIBLE);
            viewHolder.patientAgeTextView.setText(patientList.getAge() + " " + mContext.getString(R.string.years));
        } else if (patientList.getAge().equals("") && patientList.getDateOfBirth().equals("")) {
            viewHolder.patientAgeTextView.setVisibility(View.GONE);
        }

        if (patientList.getAddedToWaiting().equals(0)) {
            viewHolder.waitingIcon.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.waitingIcon.setVisibility(View.VISIBLE);
        }

        viewHolder.patientGenderTextView.setText(CommonMethods.toCamelCase(patientList.getGender()));
        if (patientList.getAppointmentStatusId().equals(BOOKED)) {
            viewHolder.opdTypeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.book_color));
            viewHolder.opdTypeTextView.setText(mContext.getString(R.string.opd_appointment) + " " + mContext.getString(R.string.booked));
        } else if (patientList.getAppointmentStatusId().equals(COMPLETED)) {
            viewHolder.opdTypeTextView.setText(mContext.getString(R.string.opd_appointment) + " " + mContext.getString(R.string.capitalcompleted));
            viewHolder.opdTypeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.complete_color));
            viewHolder.waitingIcon.setVisibility(View.INVISIBLE);
        } else if (patientList.getAppointmentStatusId().equals(CONFIRM)) {
            viewHolder.opdTypeTextView.setText(mContext.getString(R.string.opd_appointment) + " " + patientList.getAppointmentStatus());
            viewHolder.opdTypeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.confirm_color));
        } else if (patientList.getAppointmentStatusId().equals(CANCEL)) {
            viewHolder.opdTypeTextView.setText(mContext.getString(R.string.opd_appointment) + " " + patientList.getAppointmentStatus());
            viewHolder.opdTypeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.cancel_color));
            viewHolder.waitingIcon.setVisibility(View.INVISIBLE);
        } else if (patientList.getAppointmentStatusId().equals(NO_SHOW)) {
            viewHolder.opdTypeTextView.setText(mContext.getString(R.string.opd_appointment) + " " + patientList.getAppointmentStatus());
            viewHolder.opdTypeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.no_show_color));
        } else if (patientList.getAppointmentStatusId().equals(OTHER)) {
            viewHolder.opdTypeTextView.setText(mContext.getString(R.string.opd_appointment) + " " + patientList.getAppointmentStatus());
            viewHolder.opdTypeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.other_color));
        }
        viewHolder.outstandingAmountTextView.setText(mContext.getString(R.string.outstanding_amount) + " ");

        if (patientList.getOutStandingAmount().equals("0.00") || patientList.getOutStandingAmount().equals("0.0") || patientList.getOutStandingAmount().equals("0")) {
            viewHolder.payableAmountTextView.setText(" " + mContext.getString(R.string.nil));
            viewHolder.payableAmountTextView.setTextColor(ContextCompat.getColor(mContext, R.color.rating_color));
        } else {
            viewHolder.payableAmountTextView.setText(" Rs." + patientList.getOutStandingAmount() + "/-");
            viewHolder.payableAmountTextView.setTextColor(ContextCompat.getColor(mContext, R.color.Red));
        }

        viewHolder.appointmentTime.setVisibility(View.VISIBLE);
        viewHolder.appointmentTime.setText(CommonMethods.formatDateTime(patientList.getAppointmentTime(), DMSConstants.DATE_PATTERN.hh_mm_a, DMSConstants.DATE_PATTERN.HH_mm_ss, DMSConstants.TIME).toLowerCase());
        TextDrawable textDrawable = CommonMethods.getTextDrawable(mContext, patientList.getPatientName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(false);
        requestOptions.placeholder(textDrawable);
        requestOptions.error(textDrawable);

        Glide.with(mContext)
                .load(patientList.getPatientImageUrl())
                .apply(requestOptions).thumbnail(0.5f)
                .into(viewHolder.patientImageView);
        viewHolder.checkbox.setChecked(patientList.isSelected());

        viewHolder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patientList.setSelected(viewHolder.checkbox.isChecked());
                int selected = getSelectedCount(mAppointmentListTemp.get(groupPosition).getPatientList());
                mAppointmentListTemp.get(groupPosition).setSelectedGroupCheckbox(selected == mAppointmentListTemp.get(groupPosition).getPatientList().size() && mAppointmentListTemp.get(groupPosition).getPatientHeader().isSelected());
                mOnDownArrowClicked.onCheckUncheckRemoveSelectAllSelection(viewHolder.checkbox.isChecked());
                notifyDataSetChanged();
            }
        });

        if (isLongPressed)
            viewHolder.checkbox.setVisibility(View.VISIBLE);
        else viewHolder.checkbox.setVisibility(View.GONE);

        viewHolder.patientDetailsClickLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isLongPressed = !isLongPressed;
                mOnDownArrowClicked.onLongPressOpenBottomMenu(groupPosition);
                notifyDataSetChanged();
                return false;
            }
        });
        viewHolder.patientDetailsClickLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDownArrowClicked.onClickOfPatientDetails(mAppointmentListTemp.get(groupPosition).getPatientList().get(childPosition), mAppointmentListTemp.get(groupPosition).getClinicId(), viewHolder.patientAgeTextView.getText().toString() + viewHolder.patientGenderTextView.getText().toString());
            }
        });
        viewHolder.appointmentComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnDownArrowClicked.onAppointmentClicked(patientList.getAptId(), patientList.getPatientId(), COMPLETED, "complete", childPosition, groupPosition);
                openedChildGroupPos = "";
                notifyDataSetChanged();

            }
        });
        viewHolder.appointmentReschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mOnDownArrowClicked.onAppointmentReshedule(patientList, viewHolder.patientAgeTextView.getText().toString() + viewHolder.patientGenderTextView.getText().toString(), cityName, areaName);
                openedChildGroupPos = "";
                notifyDataSetChanged();

            }
        });
        viewHolder.appointmentCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnDownArrowClicked.onAppointmentCancelled(patientList.getAptId(), patientList.getPatientId(), CANCEL, "cancel", childPosition, groupPosition);
                openedChildGroupPos = "";
                notifyDataSetChanged();
            }
        });
        viewHolder.patientPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnDownArrowClicked.onPhoneNoClick(patientList.getPatientPhone());
            }
        });

        // set height programmatically

        ViewTreeObserver vto = viewHolder.front_layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                viewHolder.front_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewGroup.LayoutParams layoutParams = viewHolder.delete_layout.getLayoutParams();
                layoutParams.height = viewHolder.front_layout.getMeasuredHeight();
                layoutParams.width = viewHolder.front_layout.getMeasuredWidth() / 2;
                viewHolder.delete_layout.setLayoutParams(layoutParams);
            }
        });
    }

    private int getSelectedCount(List<PatientList> patientList) {
        int selectedCount = 0;
        for (PatientList patientL : patientList) {
            if (patientL.isSelected())
                selectedCount += 1;
        }
        return selectedCount;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mAppointmentListTemp.get(groupPosition).getPatientList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mAppointmentListTemp.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mAppointmentListTemp.size();
    }

    public ArrayList<AppointmentList> getGroupList() {
        return mAppointmentListTemp;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded,
                             View convertView, final ViewGroup parent) {

        final GroupViewHolder groupViewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_appointment_patients_item_layout, parent, false);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        final AppointmentList appointmentListObject = mAppointmentListTemp.get(groupPosition);
        bindGroupItem(appointmentListObject, groupPosition, isExpanded, groupViewHolder);

        if (openedChildGroupPos.equals(String.valueOf(groupPosition)))
            groupViewHolder.swipe_layout.open(true);
        else groupViewHolder.swipe_layout.close(true);

        groupViewHolder.swipe_layout.setSwipeListener(new SwipeRevealLayout.SwipeListener() {
            @Override
            public void onClosed(SwipeRevealLayout view) {
                if (slidingViewHolder != null && ((View) view.getParent()).getTag() != null) {
                    if (((View) view.getParent()).getTag().equals(slidingViewHolder))
                        openedChildGroupPos = "";
                }
            }

            @Override
            public void onOpened(SwipeRevealLayout view) {
                openedChildGroupPos = String.valueOf(groupPosition);
                notifyDataSetChanged();
            }

            @Override
            public void onSlide(SwipeRevealLayout view, float slideOffset) {
                Log.d("SLIDE", String.valueOf(slideOffset) + " " + ((View) view.getParent()).getTag());
                slidingViewHolder = ((View) view.getParent()).getTag();
            }
        });

        if (appointmentListObject.getPatientHeader().getAppointmentStatusId().equals(BOOKED) || appointmentListObject.getPatientHeader().getAppointmentStatusId().equals(CONFIRM))
            groupViewHolder.swipe_layout.setLockDrag(false);
        else
            groupViewHolder.swipe_layout.setLockDrag(true);

        return convertView;
    }

    @SuppressLint("CheckResult")
    private void bindGroupItem(final AppointmentList appointmentListObject, final int groupPosition, final boolean isExpanded, final GroupViewHolder groupViewHolder) {
        groupViewHolder.mClinicNameTextView.setText(appointmentListObject.getClinicName() + " - ");
        groupViewHolder.mClinicAddress.setText(appointmentListObject.getArea() + ", " + appointmentListObject.getCity());

        int count = appointmentListObject.getPatientList().size();
        if (count < 10)
            groupViewHolder.mClinicPatientCount.setText("0" + count);
        else
            groupViewHolder.mClinicPatientCount.setText("" + count);

        //---- START: Setting of hospitalID or referecne ID, reference is IS high priority than hospitalID.-----

        PatientList patientHeader = appointmentListObject.getPatientHeader();
        String dataToShowInPatientID = String.valueOf(patientHeader.getReferenceId());

        if (dataToShowInPatientID == null || DMSConstants.BLANK.equalsIgnoreCase(dataToShowInPatientID)) {
            dataToShowInPatientID = String.valueOf(patientHeader.getHospitalPatId());
        }
        //---- END------

        groupViewHolder.mPatientIdTextView.setText(mContext.getString(R.string.id) + " " + dataToShowInPatientID + "");

        if (appointmentListObject.getPatientHeader().getSalutation() != 0)
            groupViewHolder.mPatientNameTextView.setText(DMSConstants.SALUTATION[appointmentListObject.getPatientHeader().getSalutation() - 1] + CommonMethods.toCamelCase(appointmentListObject.getPatientHeader().getPatientName()));
        else
            groupViewHolder.mPatientNameTextView.setText(CommonMethods.toCamelCase(appointmentListObject.getPatientHeader().getPatientName()));

        if (appointmentListObject.getPatientHeader().getAddedToWaiting().equals(0))
            groupViewHolder.waitingIcon.setVisibility(View.INVISIBLE);
        else
            groupViewHolder.waitingIcon.setVisibility(View.VISIBLE);

        if (appointmentListObject.getPatientHeader().getAge().equals("") && !appointmentListObject.getPatientHeader().getDateOfBirth().equals("")) {
            groupViewHolder.mPatientAgeTextView.setVisibility(View.VISIBLE);
            String getTodayDate = CommonMethods.getCurrentDate(DMSConstants.DATE_PATTERN.YYYY_MM_DD);
            String getBirthdayDate = appointmentListObject.getPatientHeader().getDateOfBirth();
            DateTime todayDateTime = CommonMethods.convertToDateTime(getTodayDate, DMSConstants.DATE_PATTERN.YYYY_MM_DD);
            DateTime birthdayDateTime = CommonMethods.convertToDateTime(getBirthdayDate, DMSConstants.DATE_PATTERN.YYYY_MM_DD);
            groupViewHolder.mPatientAgeTextView.setText(CommonMethods.displayAgeAnalysis(todayDateTime, birthdayDateTime) + " " + mContext.getString(R.string.years));
        } else if (!appointmentListObject.getPatientHeader().getAge().equals("")) {
            groupViewHolder.mPatientAgeTextView.setVisibility(View.VISIBLE);
            groupViewHolder.mPatientAgeTextView.setText(appointmentListObject.getPatientHeader().getAge() + " " + mContext.getString(R.string.years));
        } else if (appointmentListObject.getPatientHeader().getAge().equals("") && appointmentListObject.getPatientHeader().getDateOfBirth().equals("")) {
            groupViewHolder.mPatientAgeTextView.setVisibility(View.GONE);
        }


        if (isExpanded) {
            groupViewHolder.mDownArrow.setVisibility(View.GONE);
            groupViewHolder.upArrow.setVisibility(View.VISIBLE);
        } else {
            if (appointmentListObject.getPatientList().size() == 1) {
                groupViewHolder.mDownArrow.setVisibility(View.GONE);
                groupViewHolder.upArrow.setVisibility(View.GONE);
            } else {
                groupViewHolder.mDownArrow.setVisibility(View.VISIBLE);
                groupViewHolder.upArrow.setVisibility(View.GONE);
            }
        }

        groupViewHolder.mPatientGenderTextView.setText(CommonMethods.toCamelCase(appointmentListObject.getPatientHeader().getGender()));
        if (appointmentListObject.getPatientHeader().getAppointmentStatusId().equals(BOOKED)) {
            groupViewHolder.mOpdTypeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.book_color));
            groupViewHolder.mOpdTypeTextView.setText(mContext.getString(R.string.opd_appointment) + " " + mContext.getString(R.string.booked));
        } else if (appointmentListObject.getPatientHeader().getAppointmentStatusId().equals(COMPLETED)) {
            groupViewHolder.mOpdTypeTextView.setText(mContext.getString(R.string.opd_appointment) + " " + mContext.getString(R.string.capitalcompleted));
            groupViewHolder.mOpdTypeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.complete_color));
            groupViewHolder.waitingIcon.setVisibility(View.INVISIBLE);
        } else if (appointmentListObject.getPatientHeader().getAppointmentStatusId().equals(CONFIRM)) {
            groupViewHolder.mOpdTypeTextView.setText(mContext.getString(R.string.opd_appointment) + " " + appointmentListObject.getPatientHeader().getAppointmentStatus());
            groupViewHolder.mOpdTypeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.confirm_color));
        } else if (appointmentListObject.getPatientHeader().getAppointmentStatusId().equals(CANCEL)) {
            groupViewHolder.mOpdTypeTextView.setText(mContext.getString(R.string.opd_appointment) + " " + appointmentListObject.getPatientHeader().getAppointmentStatus());
            groupViewHolder.mOpdTypeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.cancel_color));
            groupViewHolder.waitingIcon.setVisibility(View.INVISIBLE);
        } else if (appointmentListObject.getPatientHeader().getAppointmentStatusId().equals(NO_SHOW)) {
            groupViewHolder.mOpdTypeTextView.setText(mContext.getString(R.string.opd_appointment) + " " + appointmentListObject.getPatientHeader().getAppointmentStatus());
            groupViewHolder.mOpdTypeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.no_show_color));
        } else if (appointmentListObject.getPatientHeader().getAppointmentStatusId().equals(OTHER)) {
            groupViewHolder.mOpdTypeTextView.setText(mContext.getString(R.string.opd_appointment) + " " + appointmentListObject.getPatientHeader().getAppointmentStatus());
            groupViewHolder.mOpdTypeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.other_color));
        }

        groupViewHolder.mPatientPhoneNumber.setText(appointmentListObject.getPatientHeader().getPatientPhone());
        groupViewHolder.mOutstandingAmountTextView.setText(mContext.getString(R.string.outstanding_amount) + " ");
        if (appointmentListObject.getPatientHeader().getOutStandingAmount().equals("0.00") || appointmentListObject.getPatientHeader().getOutStandingAmount().equals("0.0") || appointmentListObject.getPatientHeader().getOutStandingAmount().equals("0")) {
            groupViewHolder.mPayableAmountTextView.setText(" " + mContext.getString(R.string.nil));
            groupViewHolder.mPayableAmountTextView.setTextColor(ContextCompat.getColor(mContext, R.color.rating_color));
        } else {
            groupViewHolder.mPayableAmountTextView.setText(" Rs." + appointmentListObject.getPatientHeader().getOutStandingAmount() + "/-");
            groupViewHolder.mPayableAmountTextView.setTextColor(ContextCompat.getColor(mContext, R.color.Red));
        }

        groupViewHolder.mAppointmentTime.setVisibility(View.VISIBLE);
        groupViewHolder.mAppointmentTime.setText(CommonMethods.formatDateTime(appointmentListObject.getPatientHeader().getAppointmentTime(), DMSConstants.DATE_PATTERN.hh_mm_a, DMSConstants.DATE_PATTERN.HH_mm_ss, DMSConstants.TIME).toLowerCase());
        TextDrawable textDrawable = CommonMethods.getTextDrawable(mContext, appointmentListObject.getPatientHeader().getPatientName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(false);
        requestOptions.placeholder(textDrawable);
        requestOptions.error(textDrawable);

        Glide.with(mContext)
                .load(appointmentListObject.getPatientHeader().getPatientImageUrl())
                .apply(requestOptions).thumbnail(0.5f)
                .into(groupViewHolder.mPatientImageView);

        groupViewHolder.mHospitalDetailsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDownArrowClicked.onDownArrowSetClick(groupPosition, isExpanded);
            }
        });
        groupViewHolder.mGroupCheckbox.setChecked(appointmentListObject.isSelectedGroupCheckbox());

        groupViewHolder.mCheckbox.setChecked(appointmentListObject.getPatientHeader().isSelected());

        groupViewHolder.mCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentListObject.getPatientHeader().setSelected(groupViewHolder.mCheckbox.isChecked());
                int selected = getSelectedCount(appointmentListObject.getPatientList());
                appointmentListObject.setSelectedGroupCheckbox(selected == appointmentListObject.getPatientList().size() && appointmentListObject.getPatientHeader().isSelected());
                mOnDownArrowClicked.onCheckUncheckRemoveSelectAllSelection(groupViewHolder.mCheckbox.isChecked());

                notifyDataSetChanged();
            }
        });


        if (isLongPressed) {
            groupViewHolder.mCheckbox.setVisibility(View.VISIBLE);
            groupViewHolder.mGroupCheckbox.setVisibility(View.VISIBLE);
        } else {
            groupViewHolder.mCheckbox.setVisibility(View.GONE);
            groupViewHolder.mGroupCheckbox.setVisibility(View.GONE);
        }

        groupViewHolder.mGroupCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentListObject.setSelectedGroupCheckbox(groupViewHolder.mGroupCheckbox.isChecked());

                appointmentListObject.getPatientHeader().setSelected(groupViewHolder.mGroupCheckbox.isChecked());

                for (PatientList patient : appointmentListObject.getPatientList())
                    patient.setSelected(groupViewHolder.mGroupCheckbox.isChecked());
                mOnDownArrowClicked.onCheckUncheckRemoveSelectAllSelection(groupViewHolder.mGroupCheckbox.isChecked());

                notifyDataSetChanged();
            }
        });

        groupViewHolder.mPatientDetailsClickLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isLongPressed = !isLongPressed;
                mOnDownArrowClicked.onLongPressOpenBottomMenu(groupPosition);
                notifyDataSetChanged();
                return false;
            }
        });
        groupViewHolder.mPatientDetailsClickLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDownArrowClicked.onClickOfPatientDetails(appointmentListObject.getPatientHeader(), appointmentListObject.getClinicId(), groupViewHolder.mPatientAgeTextView.getText().toString() + groupViewHolder.mPatientGenderTextView.getText().toString());
            }
        });

        if (isExpanded) {
            groupViewHolder.swipe_layout.setVisibility(View.GONE);
            groupViewHolder.cardView.setVisibility(View.GONE);
        } else {
            groupViewHolder.cardView.setVisibility(View.VISIBLE);
            groupViewHolder.swipe_layout.setVisibility(View.VISIBLE);
        }

        groupViewHolder.mAppointmentComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnDownArrowClicked.onGroupAppointmentClicked(appointmentListObject.getPatientHeader().getAptId(), appointmentListObject.getPatientHeader().getPatientId(), 3, "complete", groupPosition);
                openedChildGroupPos = "";
                notifyDataSetChanged();

            }
        });
        groupViewHolder.mAppointmentReschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mOnDownArrowClicked.onAppointmentReshedule(appointmentListObject.getPatientHeader(), groupViewHolder.mPatientAgeTextView.getText().toString() + groupViewHolder.mPatientGenderTextView.getText().toString(), appointmentListObject.getCity(), appointmentListObject.getArea());
                openedChildGroupPos = "";
                notifyDataSetChanged();

            }
        });
        groupViewHolder.mAppointmentCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnDownArrowClicked.onGroupAppointmentCancelled(appointmentListObject.getPatientHeader().getAptId(), appointmentListObject.getPatientHeader().getPatientId(), 4, "cancel", groupPosition);
                openedChildGroupPos = "";
                notifyDataSetChanged();

            }
        });
        groupViewHolder.mPatientPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnDownArrowClicked.onPhoneNoClick(appointmentListObject.getPatientHeader().getPatientPhone());
            }
        });


        // set height programmatically

        ViewTreeObserver vto = groupViewHolder.front_layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                groupViewHolder.front_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewGroup.LayoutParams layoutParams = groupViewHolder.delete_layout.getLayoutParams();
                layoutParams.height = groupViewHolder.front_layout.getMeasuredHeight();
                layoutParams.width = groupViewHolder.front_layout.getMeasuredWidth() / 2;
                groupViewHolder.delete_layout.setLayoutParams(layoutParams);
            }
        });

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    // Sorting clicniclist by patientName , patientId , patientPhoneNo
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                ArrayList<AppointmentList> mListToShowAfterFilter;
                ArrayList<AppointmentList> mTempAppointmentListToIterate = new ArrayList<>(mDataList);

                if (charString.isEmpty()) {
                    mListToShowAfterFilter = new ArrayList<>();
                    for (AppointmentList AppointmentListObj : mTempAppointmentListToIterate) {

                        List<PatientList> patientLists = AppointmentListObj.getPatientList();
                        ArrayList<PatientList> sortedPatientLists = new ArrayList<>();
                        //--------------
                        AppointmentList tempAppointmentListObject = null;
                        try {
                            tempAppointmentListObject = (AppointmentList) AppointmentListObj.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }

                        //-----------------
                        for (PatientList patientListObject : patientLists) {
                            patientListObject.setSpannableString(null);
                            sortedPatientLists.add(patientListObject);
                        }
                        if (!sortedPatientLists.isEmpty()) {
                            tempAppointmentListObject.setPatientList(sortedPatientLists);
                            mListToShowAfterFilter.add(tempAppointmentListObject);
                        }
                    }

                } else {
                    mListToShowAfterFilter = new ArrayList<>();
                    for (AppointmentList AppointmentListObj : mTempAppointmentListToIterate) {

                        List<PatientList> patientLists = AppointmentListObj.getPatientList();
                        ArrayList<PatientList> sortedPatientLists = new ArrayList<>();
                        //--------------
                        AppointmentList tempAppointmentListObject = null;
                        try {
                            tempAppointmentListObject = (AppointmentList) AppointmentListObj.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }

                        //-----------------
                        for (PatientList patientListObject : patientLists) {
                            if (patientListObject.getPatientName().toLowerCase().contains(charString.toLowerCase())
                                    || patientListObject.getPatientPhone().contains(charString)
                                    || String.valueOf(patientListObject.getHospitalPatId()).contains(charString)
                                    || String.valueOf(patientListObject.getReferenceId().toLowerCase()).contains(charString.toLowerCase())) {
                                //--------
                                patientListObject.setSpannableString(charString);
                                sortedPatientLists.add(patientListObject);
                            }
                        }

                        if (!sortedPatientLists.isEmpty()) {
                            tempAppointmentListObject.setPatientList(sortedPatientLists);
                            mListToShowAfterFilter.add(tempAppointmentListObject);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mListToShowAfterFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mAppointmentListTemp.clear();
                mAppointmentListTemp.addAll((ArrayList<AppointmentList>) filterResults.values);

                if (mAppointmentListTemp.isEmpty()) {
                    notifyDataSetChanged();
                    mOnDownArrowClicked.onRecordFound(true);

                } else {
                    notifyDataSetChanged();
                    mOnDownArrowClicked.onRecordFound(false);
                    if (charSequence.toString().isEmpty()) {
                        mOnDownArrowClicked.collapseAll();
                    } else {
                        mOnDownArrowClicked.expandAll();
                    }
                }
            }
        };
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout delete_layout;
        private FrameLayout front_layout;
        private CheckBox checkbox;
        private SwipeRevealLayout swipe_layout;
        private RelativeLayout patientDetailsClickLinearLayout;
        private CustomTextView appointmentTime;
        private ImageView waitingIcon;
        private CustomTextView patientIdTextView;
        private CircularImageView patientImageView;
        private CustomTextView patientNameTextView;
        private CustomTextView patientAgeTextView;
        private CustomTextView patientGenderTextView;
        private CustomTextView opdTypeTextView;
        private CustomTextView patientPhoneNumber;
        private CustomTextView outstandingAmountTextView;
        private CustomTextView payableAmountTextView;
        private Button appointmentReschedule;
        private Button appointmentCancel;
        private Button appointmentComplete;

        ChildViewHolder(View convertView) {
            super(convertView);
            appointmentReschedule = (Button) convertView.findViewById(R.id.appointmentReschedule);
            front_layout = (FrameLayout) convertView.findViewById(R.id.front_layout);
            delete_layout = (FrameLayout) convertView.findViewById(R.id.delete_layout);
            waitingIcon = (ImageView) convertView.findViewById(R.id.waitingIcon);
            appointmentCancel = (Button) convertView.findViewById(R.id.appointmentCancelled);
            appointmentComplete = (Button) convertView.findViewById(R.id.appointmentComplete);
            swipe_layout = (SwipeRevealLayout) convertView.findViewById(R.id.swipe_layout);
            checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            patientDetailsClickLinearLayout = (RelativeLayout) convertView.findViewById(R.id.patientDetailsClickLinearLayout);
            appointmentTime = (CustomTextView) convertView.findViewById(R.id.appointmentTime);
            patientIdTextView = (CustomTextView) convertView.findViewById(R.id.patientIdTextView);
            patientImageView = (CircularImageView) convertView.findViewById(R.id.patientImageView);
            patientNameTextView = (CustomTextView) convertView.findViewById(R.id.patientNameTextView);
            patientAgeTextView = (CustomTextView) convertView.findViewById(R.id.patientAgeTextView);
            patientGenderTextView = (CustomTextView) convertView.findViewById(R.id.patientGenderTextView);
            opdTypeTextView = (CustomTextView) convertView.findViewById(R.id.opdTypeTextView);
            patientPhoneNumber = (CustomTextView) convertView.findViewById(R.id.patientPhoneNumber);
            outstandingAmountTextView = (CustomTextView) convertView.findViewById(R.id.outstandingAmountTextView);
            payableAmountTextView = (CustomTextView) convertView.findViewById(R.id.payableAmountTextView);
        }
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout delete_layout;
        private FrameLayout front_layout;

        private SwipeRevealLayout swipe_layout;
        private LinearLayout cardView;
        private CheckBox mCheckbox;
        private RelativeLayout mPatientDetailsClickLinearLayout;
        private CheckBox mGroupCheckbox;
        private RelativeLayout mHospitalDetailsLinearLayout;
        private CustomTextView mClinicNameTextView;
        private CustomTextView mClinicAddress;
        private CustomTextView mClinicPatientCount;
        private ImageView mDownArrow;
        private ImageView upArrow;
        private CustomTextView mPatientIdTextView;
        private CircularImageView mPatientImageView;
        private CustomTextView mPatientNameTextView;
        private ImageView waitingIcon;
        private CustomTextView mPatientAgeTextView;
        private CustomTextView mPatientGenderTextView;
        private CustomTextView mOpdTypeTextView;
        private CustomTextView mPatientPhoneNumber;
        private CustomTextView mOutstandingAmountTextView;
        private CustomTextView mPayableAmountTextView;
        private CustomTextView mAppointmentTime;
        private Button mAppointmentReschedule;
        private Button mAppointmentCancel;
        private Button mAppointmentComplete;

        GroupViewHolder(View convertView) {
            super(convertView);

            front_layout = (FrameLayout) convertView.findViewById(R.id.front_layout);
            delete_layout = (FrameLayout) convertView.findViewById(R.id.delete_layout);
            mAppointmentReschedule = (Button) convertView.findViewById(R.id.appointmentReschedule);

            waitingIcon = (ImageView) convertView.findViewById(R.id.waitingIcon);
            mAppointmentCancel = (Button) convertView.findViewById(R.id.appointmentCancelled);
            mAppointmentComplete = (Button) convertView.findViewById(R.id.appointmentComplete);
            swipe_layout = (SwipeRevealLayout) convertView.findViewById(R.id.swipe_layout);
            mCheckbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            mPatientDetailsClickLinearLayout = (RelativeLayout) convertView.findViewById(R.id.patientDetailsClickLinearLayout);
            mGroupCheckbox = (CheckBox) convertView.findViewById(R.id.groupCheckbox);
            cardView = (LinearLayout) convertView.findViewById(R.id.cardView);
            mHospitalDetailsLinearLayout = (RelativeLayout) convertView.findViewById(R.id.hospitalDetailsLinearLayout);
            mClinicNameTextView = (CustomTextView) convertView.findViewById(R.id.clinicNameTextView);
            mClinicAddress = (CustomTextView) convertView.findViewById(R.id.clinicAddress);
            mClinicPatientCount = (CustomTextView) convertView.findViewById(R.id.clinicPatientCount);
            mDownArrow = (ImageView) convertView.findViewById(R.id.downArrow);
            upArrow = (ImageView) convertView.findViewById(R.id.upArrow);
            mPatientIdTextView = (CustomTextView) convertView.findViewById(R.id.patientIdTextView);
            mPatientImageView = (CircularImageView) convertView.findViewById(R.id.patientImageView);
            mPatientNameTextView = (CustomTextView) convertView.findViewById(R.id.patientNameTextView);
            mPatientAgeTextView = (CustomTextView) convertView.findViewById(R.id.patientAgeTextView);
            mPatientGenderTextView = (CustomTextView) convertView.findViewById(R.id.patientGenderTextView);
            mOpdTypeTextView = (CustomTextView) convertView.findViewById(R.id.opdTypeTextView);
            mPatientPhoneNumber = (CustomTextView) convertView.findViewById(R.id.patientPhoneNumber);
            mOutstandingAmountTextView = (CustomTextView) convertView.findViewById(R.id.outstandingAmountTextView);
            mPayableAmountTextView = (CustomTextView) convertView.findViewById(R.id.payableAmountTextView);
            mAppointmentTime = (CustomTextView) convertView.findViewById(R.id.appointmentTime);
        }
    }

    public interface OnDownArrowClicked {
        void onDownArrowSetClick(int groupPosition, boolean isExpanded);

        void onLongPressOpenBottomMenu(int groupPosition);

        void onRecordFound(boolean isListEmpty);

        void onCheckUncheckRemoveSelectAllSelection(boolean ischecked);

        void onClickOfPatientDetails(PatientList patientListObject, int clinicId, String text);

        void onAppointmentClicked(Integer aptId, Integer patientId, int status, String type, int childPosition, int groupPosition);

        void onAppointmentCancelled(Integer aptId, Integer patientId, int status, String type, int childPosition, int groupPosition);

        void onGroupAppointmentClicked(Integer aptId, Integer patientId, int status, String type, int groupPosition);

        void onGroupAppointmentCancelled(Integer aptId, Integer patientId, int status, String type, int groupPosition);

        void onAppointmentReshedule(PatientList patientList, String text, String cityName, String areaName);

        void expandAll();

        void collapseAll();

        void onPhoneNoClick(String patientPhone);
    }

}