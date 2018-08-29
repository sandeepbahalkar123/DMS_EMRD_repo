package com.scorg.dms.ui.activities.pending_approval_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;

import com.scorg.dms.R;
import com.scorg.dms.helpers.pending_approval.PendingApprovalHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.pending_approval_list.RequestedArchivedBaseModel;
import com.scorg.dms.model.pending_approval_list.PendingApprovalDataModel;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.ui.fragments.approval_list.ApprovalListFragment;
import com.scorg.dms.ui.fragments.approval_list.PendingListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 22/2/18.
 */

public class RequestedArchivedMainListActivity extends AppCompatActivity implements HelperResponse {

    public static final int RESULT_CLOSE_ACTIVITY_PENDINGAPPROVAL_LIST = 040;
    @BindView(R.id.backImageView)
    ImageView backImageView;
    @BindView(R.id.titleTextView)
    CustomTextView titleTextView;
    @BindView(R.id.userInfoTextView)
    CustomTextView userInfoTextView;
    @BindView(R.id.dateTextview)
    CustomTextView dateTextview;
    @BindView(R.id.year)
    Spinner year;
    @BindView(R.id.addImageView)
    ImageView addImageView;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    String[] mFragmentTitleList = new String[2];
    @BindView(R.id.leftFab)
    FloatingActionButton leftFab;
    private PendingListFragment mPendingListFragment;
    private ApprovalListFragment mApprovalListFragment;
    private PendingApprovalHelper mPendingApprovalHelper;
    private PendingApprovalDataModel pendingApprovalDataModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_base_layout);
        ButterKnife.bind(this);
        mFragmentTitleList[0] = getString(R.string.pending);
        mFragmentTitleList[1] = getString(R.string.submitted);
      //  mPendingApprovalHelper= new PendingApprovalHelper(this,this);
       // mPendingApprovalHelper.doGetPendingApprovalData(1,true);

        setupViewPager(viewpager);
        tabs.setupWithViewPager(viewpager);

    }

    private void setupViewPager(ViewPager viewPager) {
        titleTextView.setText(getString(R.string.approval_list));
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPendingListFragment = PendingListFragment.newInstance(new Bundle());
        mApprovalListFragment = ApprovalListFragment.newInstance(new Bundle());
        adapter.addFragment(mPendingListFragment, getString(R.string.pending));
        adapter.addFragment(mApprovalListFragment, getString(R.string.submitted));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (customResponse != null) {
           RequestedArchivedBaseModel requestedArchivedBaseModel = (RequestedArchivedBaseModel)customResponse;
            //pendingApprovalDataModel =requestedArchivedBaseModel.g();
            ///setupViewPager(viewpager);
          //  tabs.setupWithViewPager(viewpager);
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

    @OnClick({R.id.backImageView, R.id.titleTextView, R.id.userInfoTextView, R.id.leftFab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                finish();
                break;
            case R.id.titleTextView:
                break;
            case R.id.userInfoTextView:
                break;
            case R.id.leftFab:

                break;
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CLOSE_ACTIVITY_PENDINGAPPROVAL_LIST) {
            finish();
        }
    }


    public PendingApprovalDataModel getPendingApprovalDataModel() {
        return pendingApprovalDataModel;
    }

    public void setPendingApprovalDataModel(PendingApprovalDataModel pendingApprovalDataModel) {
        this.pendingApprovalDataModel = pendingApprovalDataModel;
    }
}