package com.scorg.dms.adapters.dms_adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scorg.dms.R;
import com.scorg.dms.interfaces.ErrorDialogCallback;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.util.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by riteshpandhurkar on 24/2/17.
 */

public class PatientRecycleViewListAdapter extends RecyclerView.Adapter<PatientRecycleViewListAdapter.GroupViewHolder> {

    private static final String TAG = "PatientListActivity";
    private Context _context;
    private OnPatientListener onPatientListener;

    private List<SearchResult> _originalListDataHeader = new ArrayList<>(); // header titles

    private List<SearchResult> searchResultForPatientDetails = new ArrayList<>();

    public PatientRecycleViewListAdapter(Context context, List<SearchResult> searchResult) {
        this._context = context;
        addNewItems(searchResult);
        if (context instanceof OnPatientListener) {
            onPatientListener = (OnPatientListener) context;
        } else CommonMethods.Log(TAG, "Implement OnPatientListener in Activity");
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_patient_list_header, parent, false);
        return new GroupViewHolder(itemView);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(GroupViewHolder groupViewHolder, final int position) {
        GradientDrawable episodButtonBackground = new GradientDrawable();
        episodButtonBackground.setShape(GradientDrawable.RECTANGLE);
        episodButtonBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        episodButtonBackground.setCornerRadius(_context.getResources().getDimension(R.dimen.dp2));
        groupViewHolder.episodeList.setBackground(episodButtonBackground);

        final SearchResult groupHeader = _originalListDataHeader.get(position);
        groupViewHolder.uhid.setTextColor(Color.parseColor(DMSApplication.COLOR_APPOINTMENT_TEXT));
        groupViewHolder.patientId.setTextColor(Color.parseColor(DMSApplication.COLOR_APPOINTMENT_TEXT));
        groupViewHolder.bluelineImageView.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        groupViewHolder.userName.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        groupViewHolder.userName.setText(groupHeader.getPatientName().toUpperCase());
        groupViewHolder.patientId.setText(groupHeader.getPatientId());
        groupViewHolder.uhid.setText(DMSApplication.LABEL_UHID + " ");
        groupViewHolder.userGender.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));

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
//        Log.e("age","=="+groupHeader.getAge());
//        Log.e("gender","=="+groupHeader.getGender());

        if (groupHeader.getAge() != null) {
            groupViewHolder.userAge.setVisibility(View.VISIBLE);
            groupViewHolder.userAge.setText(groupHeader.getAge() + " " + _context.getString(R.string.years));
        }
        else {
            groupViewHolder.userAge.setVisibility(View.GONE);
        }


        if (groupHeader.getGender() != null) {
            groupViewHolder.userGender.setVisibility(View.VISIBLE);
            groupViewHolder.userGender.setText(groupHeader.getGender());
        }else {
            groupViewHolder.userGender.setVisibility(View.GONE);
        }

        groupViewHolder.mainContentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupHeader.isArchived()) {
                    if (_originalListDataHeader.size()>position) {
                        SearchResult groupHeader = _originalListDataHeader.get(position);
                        onPatientListener.onPatientListItemClick(groupHeader);
                    }
                } else {
                    CommonMethods.showErrorDialog(_context.getString(R.string.patient_not_having_record), _context, false, new ErrorDialogCallback() {
                        @Override
                        public void ok() {

                        }

                        @Override
                        public void retry() {

                        }
                    });
                }
            }
        });

        groupViewHolder.episodeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (groupHeader.isArchived()) {
                    if (_originalListDataHeader.size()>position) {
                        SearchResult groupHeader = _originalListDataHeader.get(position);
                        onPatientListener.onClickedOfEpisodeListButton(groupHeader);
                    }
                } else {
                    CommonMethods.showErrorDialog(_context.getString(R.string.patient_not_having_record), _context, false, new ErrorDialogCallback() {
                        @Override
                        public void ok() {

                        }

                        @Override
                        public void retry() {

                        }
                    });
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return _originalListDataHeader.size();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.cardView)
//        LinearLayout cardView;
        @BindView(R.id.mainContentLayout)
        LinearLayout mainContentLayout;
        @BindView(R.id.userName)
        TextView userName;
        @BindView(R.id.userAge)
        TextView userAge;
        @BindView(R.id.userGender)
        TextView userGender;
        @BindView(R.id.uhid)
        TextView uhid;
        @BindView(R.id.patientId)
        TextView patientId;
        @BindView(R.id.patientImageView)
        ImageView patientImageView;
        @BindView(R.id.bluelineImageView)
        ImageView bluelineImageView;
        @BindView(R.id.episodeList)
        TextView episodeList;

        //@BindView(R.id.divider)
        //View divider;

        GroupViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
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
        // void onCompareDialogShow(PatientFileData patientFileData1, PatientFileData patientFileData2, String mCheckedBoxGroupName, String tempName, boolean b);

        void onPatientListItemClick(SearchResult groupHeader);

        void onClickedOfEpisodeListButton(SearchResult groupHeader);

        void smoothScrollToPosition(int previousPosition);
    }

    public void removeAll() {
        this._originalListDataHeader.clear();
    }

    public void addNewItems(List<SearchResult> searchResult) {
        this._originalListDataHeader.addAll(searchResult);


    }

}