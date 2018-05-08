package com.scorg.dms.ui.fragments.patient.my_patient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.scorg.dms.R;
import com.scorg.dms.adapters.drawer_adapters.DrawerPatientsCityNameAdapter;
import com.scorg.dms.adapters.drawer_adapters.SortByPriceFilterAdapter;
import com.scorg.dms.helpers.myappointments.AppointmentHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.my_appointments.FilterSortByHighLowList;
import com.scorg.dms.model.my_patient_filter.CityList;
import com.scorg.dms.model.my_patient_filter.LocationsModel;
import com.scorg.dms.model.request_patients.FilterParams;
import com.scorg.dms.model.request_patients.RequestSearchPatients;
import com.scorg.dms.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.scorg.dms.util.DMSConstants.SUCCESS;
import static com.scorg.dms.util.DMSConstants.TASK_GET_DOCTOR_PATIENT_CITY;

/**
 * Created by jeetal on 12/2/18.
 */

public class DrawerForMyPatients extends Fragment implements HelperResponse, SortByPriceFilterAdapter.onSortByAmountMenuClicked, DrawerPatientsCityNameAdapter.CitySelectListener {

    @BindView(R.id.applyButton)
    Button applyButton;
    @BindView(R.id.titleTextView)
    CustomTextView titleTextView;
    @BindView(R.id.resetButton)
    Button resetButton;
    @BindView(R.id.sortingHeaderView)
    LinearLayout sortingHeaderView;
    @BindView(R.id.chooseOptionForSort)
    CustomTextView chooseOptionForSort;
    @BindView(R.id.chooseOptionToSort)
    LinearLayout chooseOptionToSort;
    @BindView(R.id.genderHeaderView)
    LinearLayout genderHeaderView;
    @BindView(R.id.selectGender)
    CustomTextView selectGender;
    @BindView(R.id.genderMaleIcon)
    ImageView genderMaleIcon;
    @BindView(R.id.genderMaleText)
    CustomTextView genderMaleText;
    @BindView(R.id.genderMaleLayout)
    LinearLayout genderMaleLayout;
    @BindView(R.id.genderFemaleIcon)
    ImageView genderFemaleIcon;
    @BindView(R.id.genderFemaleText)
    CustomTextView genderFemaleText;
    @BindView(R.id.genderFemaleLayout)
    LinearLayout genderFemaleLayout;
    @BindView(R.id.genderContentView)
    LinearLayout genderContentView;
    @BindView(R.id.clinicFeesHeaderView)
    LinearLayout clinicFeesHeaderView;
    @BindView(R.id.ageRange)
    CustomTextView ageRange;
    @BindView(R.id.clinicFeesSeekBarMinValue)
    CustomTextView clinicFeesSeekBarMinValue;
    @BindView(R.id.clinicFeesSeekBarMaxValue)
    CustomTextView clinicFeesSeekBarMaxValue;
    @BindView(R.id.clinicFeesSeekBar)
    CrystalRangeSeekbar clinicFeesSeekBar;
    @BindView(R.id.clinicFeesContentView)
    LinearLayout clinicFeesContentView;
    @BindView(R.id.clinicFeesView)
    LinearLayout clinicFeesView;
    @BindView(R.id.locationHeaderView)
    LinearLayout locationHeaderView;
    @BindView(R.id.selectCityTextLabel)
    CustomTextView selectCityTextLabel;
    @BindView(R.id.locationContentRecycleView)
    RecyclerView locationContentRecycleView;
    @BindView(R.id.locationView)
    LinearLayout locationView;
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScroll;
    @BindView(R.id.hideMainLayout)
    LinearLayout hideMainLayout;
    @BindView(R.id.doneButton)
    Button doneButton;
    @BindView(R.id.sortingTitleTextView)
    CustomTextView sortingTitleTextView;
    @BindView(R.id.resetSortingButton)
    Button resetSortingButton;
    @BindView(R.id.sortingView)
    LinearLayout sortingView;
    @BindView(R.id.sortRecyclerView)
    RecyclerView sortRecyclerView;
    @BindView(R.id.showSortLayout)
    LinearLayout showSortLayout;
    @BindView(R.id.mainParentLayout)
    LinearLayout mainParentLayout;
    View mLeftThumbView, mRightThumbView;
    @BindView(R.id.transGenderMaleIcon)
    ImageView transGenderMaleIcon;
    @BindView(R.id.transGenderMaleText)
    CustomTextView transGenderMaleText;
    @BindView(R.id.transGenderMaleLayout)
    LinearLayout transGenderMaleLayout;
    private Unbinder unbinder;
    private SortByPriceFilterAdapter mSortByPriceFilterAdapter;
    private String mSelectedGender;
    private OnDrawerInteractionListener mListener;
    private String lowToHigh = "(low to high)";
    private String highToLow = "(high to low)";
    private ArrayList<FilterSortByHighLowList> filterSortByHighLowLists = new ArrayList<>();
    private FilterSortByHighLowList mFilterSortByHighLowList;
    private String[] sortOptions = new String[]{"Outstanding Amt" + lowToHigh,
            "Outstanding Amt" + highToLow};
    private String sortOrder = "";
    private ArrayList<Integer> cityList = new ArrayList<>();
    private DrawerPatientsCityNameAdapter mDrawerPatientsCityNameAdapter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.mypatients_filter_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        initialize();
        return view;
    }

    private void initialize() {
        // mSelectedDays = new HashMap<>();
        for (String amtString : sortOptions) {
            FilterSortByHighLowList filterSortByHighLowListObject = new FilterSortByHighLowList();
            filterSortByHighLowListObject.setAmountHighOrLow(amtString);
            filterSortByHighLowLists.add(filterSortByHighLowListObject);
        }
        configureDrawerFieldsData();

        AppointmentHelper appointmentHelper = new AppointmentHelper(getContext(), this);
        appointmentHelper.getFilterLocationList();
    }

    private void configureDrawerFieldsData() {
        mSelectedGender = "";
        chooseOptionForSort.setText(getString(R.string.choose_one_option));
        mLeftThumbView = LayoutInflater.from(getActivity()).inflate(R.layout.seekbar_progress_thumb_layout, null, false);
        mRightThumbView = LayoutInflater.from(getActivity()).inflate(R.layout.seekbar_progress_thumb_layout, null, false);
        mLeftThumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mRightThumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mLeftThumbView.setDrawingCacheEnabled(true);
        setThumbValue(mLeftThumbView, "" + 1);
        mRightThumbView.setDrawingCacheEnabled(true);
        setThumbValue(mRightThumbView, "" + 100);

        SpannableString selectGenderString = new SpannableString("Select Gender");
        selectGenderString.setSpan(new UnderlineSpan(), 0, selectGenderString.length(), 0);
        selectGender.setText(selectGenderString);
        SpannableString selectCityString = new SpannableString("Select City");
        selectCityString.setSpan(new UnderlineSpan(), 0, selectCityString.length(), 0);
        selectCityTextLabel.setText(selectCityString);
        SpannableString ageRangeString = new SpannableString("Age Range");
        ageRangeString.setSpan(new UnderlineSpan(), 0, ageRangeString.length(), 0);
        ageRange.setText(ageRangeString);

        clinicFeesSeekBar
                .setCornerRadius(10f)
                .setBarColor(ContextCompat.getColor(getActivity(), R.color.seek_bar_default))
                .setBarHighlightColor(ContextCompat.getColor(getActivity(), R.color.seek_bar_progress))
                .setSteps(1)
                .setLeftThumbBitmap(null)
                .setLeftThumbHighlightBitmap(null)
                .setRightThumbBitmap(null)
                .setRightThumbHighlightBitmap(null)
                .setDataType(CrystalRangeSeekbar.DataType.INTEGER)
                .setMaxValue(100)
                .setMinValue(0)
                .setMaxStartValue(100)
                .setMinStartValue(0)
                .apply();

        // set listener
        clinicFeesSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {

                if (mLeftThumbView != null) {
                    Bitmap bitmap = setThumbValue(mLeftThumbView, String.valueOf(minValue));
                    clinicFeesSeekBar.setLeftThumbBitmap(bitmap);
                    clinicFeesSeekBar.setLeftThumbHighlightBitmap(bitmap);
                }
                if (mRightThumbView != null) {
                    Bitmap bitmap = setThumbValue(mRightThumbView, String.valueOf(maxValue));
                    clinicFeesSeekBar.setRightThumbBitmap(bitmap);
                    clinicFeesSeekBar.setRightThumbHighlightBitmap(bitmap);
                }
            }
        });
        mSortByPriceFilterAdapter = new SortByPriceFilterAdapter(getActivity(), filterSortByHighLowLists, this);
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        sortRecyclerView.setLayoutManager(linearlayoutManager);
        sortRecyclerView.setHasFixedSize(true);
        sortRecyclerView.setAdapter(mSortByPriceFilterAdapter);
    }

    public static DrawerForMyPatients newInstance() {
        DrawerForMyPatients fragment = new DrawerForMyPatients();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    public Bitmap setThumbValue(View view, String valueToSet) {
        TextView tvRightProgressValue = (TextView) view.findViewById(R.id.tvProgress);
        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        view.layout(0, 0, view.getRight(), view.getBottom());
        view.draw(c);
        tvRightProgressValue.setText(valueToSet);
        return b;
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equals(TASK_GET_DOCTOR_PATIENT_CITY)) {
            LocationsModel locationsModel = (LocationsModel) customResponse;
            if (locationsModel.getCommon().getStatusCode().equals(SUCCESS)) {
                ArrayList<CityList> cityList = locationsModel.getData().getCityList();
                mDrawerPatientsCityNameAdapter = new DrawerPatientsCityNameAdapter(DrawerForMyPatients.this, cityList);
                LinearLayoutManager layoutManagerClinicList = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                if (locationContentRecycleView != null) {
                    locationContentRecycleView.setLayoutManager(layoutManagerClinicList);
                    locationContentRecycleView.setHasFixedSize(true);
                    locationContentRecycleView.setNestedScrollingEnabled(false);
                    locationContentRecycleView.setAdapter(mDrawerPatientsCityNameAdapter);
                }
            }
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


    @OnClick({R.id.genderMaleLayout, R.id.genderFemaleLayout, R.id.transGenderMaleLayout, R.id.resetButton, R.id.applyButton, R.id.sortingHeaderView, R.id.hideMainLayout, R.id.doneButton, R.id.resetSortingButton, R.id.showSortLayout, R.id.mainParentLayout, R.id.chooseOptionToSort})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.applyButton:
                RequestSearchPatients mRequestSearchPatients = new RequestSearchPatients();
                FilterParams filterParams = new FilterParams();
                filterParams.setCityIds(cityList);
                filterParams.setGender(mSelectedGender);
                if (clinicFeesSeekBar.getSelectedMinValue().toString().equals("0") && clinicFeesSeekBar.getSelectedMaxValue().toString().equals("100")) {
                    filterParams.setAge("");
                } else {
                    filterParams.setAge(String.valueOf(clinicFeesSeekBar.getSelectedMinValue()) + "-" + clinicFeesSeekBar.getSelectedMaxValue());
                }

                mRequestSearchPatients.setFilterParams(filterParams);
                mRequestSearchPatients.setSortField("Outstanding");
                if (chooseOptionForSort.getText().toString().toLowerCase().contains("low to high")) {
                    sortOrder = "asc";
                } else if (chooseOptionForSort.getText().toString().toLowerCase().contains("high to low")) {
                    sortOrder = "desc";
                } else {
                    sortOrder = "";
                }
                mRequestSearchPatients.setSortOrder(sortOrder);
                mListener.onApply(mRequestSearchPatients, false);

                break;
            case R.id.resetButton:
                configureDrawerFieldsData();
                genderMaleIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.filtergraymaleicon));
                genderMaleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.gender_drawer));
                genderFemaleIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.filtergrayfemaleicon));
                genderFemaleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.gender_drawer));
                transGenderMaleIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.filtergraytransgendericon));
                transGenderMaleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.gender_drawer));
                for (FilterSortByHighLowList filterSortByHighLowListObject : mSortByPriceFilterAdapter.getAmountSortList()) {
                    filterSortByHighLowListObject.setSelected(false);
                }
                mSortByPriceFilterAdapter.notifyDataSetChanged();

                if (mDrawerPatientsCityNameAdapter != null)
                    mDrawerPatientsCityNameAdapter.reset();

                RequestSearchPatients mRequestSearchP = new RequestSearchPatients();
                mRequestSearchP.setSortField("Outstanding");
                mListener.onApply(mRequestSearchP, true);

                break;
            case R.id.sortingHeaderView:
                break;
            case R.id.hideMainLayout:
                break;
            case R.id.doneButton:
                hideMainLayout.setVisibility(View.VISIBLE);
                showSortLayout.setVisibility(View.GONE);

                if (mFilterSortByHighLowList != null)
                    chooseOptionForSort.setText(mFilterSortByHighLowList.getAmountHighOrLow());
                else
                    chooseOptionForSort.setText(getString(R.string.choose_one_option));
                //chooseOptionForSort.setText(mSortByPriceFilterAdapter.getAmountSortList().get(mSortByAmountAdapterPosition).getAmountHighOrLow());
                break;
            case R.id.resetSortingButton:
                for (FilterSortByHighLowList filterSortByHighLowListObject : mSortByPriceFilterAdapter.getAmountSortList()) {
                    filterSortByHighLowListObject.setSelected(false);
                }
                mFilterSortByHighLowList = null;
                mSortByPriceFilterAdapter.notifyDataSetChanged();
                break;
            case R.id.showSortLayout:
                break;
            case R.id.mainParentLayout:
                break;
            case R.id.chooseOptionToSort:
                hideMainLayout.setVisibility(View.GONE);
                showSortLayout.setVisibility(View.VISIBLE);

                break;
            case R.id.genderMaleLayout:
                mSelectedGender = genderMaleText.getText().toString();
                genderMaleIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.filterhighlightedmaleicon));
                genderMaleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.genderSelectionColor));
                genderFemaleIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.filtergrayfemaleicon));
                genderFemaleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.gender_drawer));
                transGenderMaleIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.filtergraytransgendericon));
                transGenderMaleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.gender_drawer));
                break;
            case R.id.genderFemaleLayout:
                mSelectedGender = genderFemaleText.getText().toString();
                genderMaleIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.filtergraymaleicon));
                genderMaleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.gender_drawer));
                genderFemaleIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.filterhighlightedfemaleicon));
                genderFemaleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.genderSelectionColor));
                transGenderMaleIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.filtergraytransgendericon));
                transGenderMaleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.gender_drawer));
                break;
            case R.id.transGenderMaleLayout:
                mSelectedGender = transGenderMaleText.getText().toString();
                genderMaleIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.filtergraymaleicon));
                genderMaleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.gender_drawer));
                genderFemaleIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.filtergrayfemaleicon));
                genderFemaleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.gender_drawer));
                transGenderMaleIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.filterhighlightedtransgendericon));
                transGenderMaleText.setTextColor(ContextCompat.getColor(getActivity(), R.color.genderSelectionColor));
                break;

        }
    }

    @Override
    public void onClickOfSortMenu(FilterSortByHighLowList filterSortByHighLowObject, int groupPosition) {
        mFilterSortByHighLowList = filterSortByHighLowObject;
    }

    @Override
    public void onChecked(ArrayList<CityList> cityList) {
        this.cityList.clear();
        for (CityList city : cityList) {
            if (city.isChecked())
                this.cityList.add(city.getCityId());
        }
    }

    public interface OnDrawerInteractionListener {
        void onApply(RequestSearchPatients mRequestSearchPatients, boolean isReset);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDrawerInteractionListener) {
            mListener = (OnDrawerInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDrawerInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
