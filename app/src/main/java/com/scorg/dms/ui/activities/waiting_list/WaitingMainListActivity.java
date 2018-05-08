package com.scorg.dms.ui.activities.waiting_list;

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
import com.scorg.dms.helpers.myappointments.AppointmentHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.waiting_list.WaitingListBaseModel;
import com.scorg.dms.model.waiting_list.WaitingclinicList;
import com.scorg.dms.ui.activities.my_patients.MyPatientsActivity;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.ui.fragments.waiting_list.ActivePatientListFragment;
import com.scorg.dms.ui.fragments.waiting_list.ViewAllPatientListFragment;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.scorg.dms.util.DMSConstants.LOCATION_ID;

/**
 * Created by jeetal on 22/2/18.
 */

public class WaitingMainListActivity extends AppCompatActivity implements HelperResponse {

    public static final int RESULT_CLOSE_ACTIVITY_WAITING_LIST = 040;
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
    private ActivePatientListFragment mActivePatientListFragment;
    private ViewAllPatientListFragment mViewAllPatientListFragment;
    private ArrayList<WaitingclinicList> mWaitingClinicList;
    private Bundle bundle;
    private AppointmentHelper mAppointmentHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_base_layout);
        ButterKnife.bind(this);
        mFragmentTitleList[0] = getString(R.string.active);
        mFragmentTitleList[1] = getString(R.string.view_all);
        mAppointmentHelper = new AppointmentHelper(this, this);
        mAppointmentHelper.doGetWaitingList();
    }

    private void setupViewPager(ViewPager viewPager) {
        titleTextView.setText(getString(R.string.waiting_list));
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mActivePatientListFragment = ActivePatientListFragment.newInstance(bundle);
        mViewAllPatientListFragment = ViewAllPatientListFragment.newInstance(bundle);
        adapter.addFragment(mActivePatientListFragment, getString(R.string.active));
        adapter.addFragment(mViewAllPatientListFragment, getString(R.string.view_all));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (customResponse != null) {
            WaitingListBaseModel waitingListBaseModel = (WaitingListBaseModel) customResponse;
            mWaitingClinicList = waitingListBaseModel.getWaitingListDataModel().getWaitingclinicList();
            bundle = new Bundle();
            bundle.putParcelableArrayList(DMSConstants.WAITING_LIST_INFO, mWaitingClinicList);
            bundle.putInt(LOCATION_ID, getIntent().getIntExtra(LOCATION_ID, -1));
            setupViewPager(viewpager);
            tabs.setupWithViewPager(viewpager);
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

    @OnClick({R.id.backImageView, R.id.titleTextView, R.id.userInfoTextView,R.id.leftFab})
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
                Intent intent = new Intent(this, MyPatientsActivity.class);
                intent.putExtra(DMSConstants.ACTIVITY_LAUNCHED_FROM, DMSConstants.WAITING_LIST);
                startActivityForResult(intent,RESULT_CLOSE_ACTIVITY_WAITING_LIST);
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
        if (requestCode == RESULT_CLOSE_ACTIVITY_WAITING_LIST) {
            finish();
        }
    }
}