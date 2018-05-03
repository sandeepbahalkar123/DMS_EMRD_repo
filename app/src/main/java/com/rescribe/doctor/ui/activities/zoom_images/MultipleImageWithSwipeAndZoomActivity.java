package com.rescribe.doctor.ui.activities.zoom_images;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.doctor.R;
import com.rescribe.doctor.model.case_details.VisitCommonData;
import com.rescribe.doctor.ui.customesViews.zoomview.ZoomageView;
import com.rescribe.doctor.util.RescribeConstants;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MultipleImageWithSwipeAndZoomActivity extends AppCompatActivity {

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.backButton)
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        setContentView(R.layout.multi_image_with_swipe_and_zoom);
        ButterKnife.bind(this);

        ArrayList<VisitCommonData> parcelableArrayListExtra = getIntent().getParcelableArrayListExtra(RescribeConstants.ATTACHMENTS_LIST);

        String url = getIntent().getStringExtra(RescribeConstants.DOCUMENTS);

        //------------
        int clickedImage = 0;
        for (int i = 0; i < parcelableArrayListExtra.size(); i++) {
            if (parcelableArrayListExtra.get(i).getUrl().equalsIgnoreCase(url)) {
                clickedImage = i;
                break;
            }
        }
        //------------

        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this, parcelableArrayListExtra);
        pager.setAdapter(mCustomPagerAdapter);
        pager.setCurrentItem(clickedImage);
    }

    @OnClick(R.id.backButton)
    public void onViewClicked() {
        onBackPressed();
    }

    private class CustomPagerAdapter extends PagerAdapter {

        private ArrayList<VisitCommonData> visitCommonData;
        Context mContext;
        LayoutInflater mLayoutInflater;
        RequestOptions requestOptions;

        public CustomPagerAdapter(Context context, ArrayList<VisitCommonData> visitCommonData) {
            mContext = context;
            this.visitCommonData = visitCommonData;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            requestOptions = new RequestOptions();
            requestOptions.placeholder(droidninja.filepicker.R.drawable.image_placeholder);
            requestOptions.error(droidninja.filepicker.R.drawable.image_placeholder);
        }

        @Override
        public int getCount() {
            return visitCommonData.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ZoomageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.item_zoom_image_view, container, false);

            VisitCommonData visitCommonData = this.visitCommonData.get(position);

            ZoomageView zoomView = (ZoomageView) itemView.findViewById(R.id.zoomView);

            String tag = visitCommonData.getUrl();
            String fileExtension = tag.substring(tag.lastIndexOf("."));

            Glide.with(mContext).load(tag)
                    .apply(requestOptions)
                    .into(zoomView);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ZoomageView) object);
        }
    }
}