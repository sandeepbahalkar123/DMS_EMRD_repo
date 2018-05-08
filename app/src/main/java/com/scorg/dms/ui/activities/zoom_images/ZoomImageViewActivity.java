package com.scorg.dms.ui.activities.zoom_images;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scorg.dms.R;
import com.scorg.dms.ui.customesViews.zoomview.ZoomageView;
import com.scorg.dms.util.DMSConstants;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ZoomImageViewActivity extends AppCompatActivity {

    @BindView(R.id.zoomView)
    ZoomageView zoomView;
    @BindView(R.id.backButton)
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        setContentView(R.layout.activity_zoom_image_view);
        ButterKnife.bind(this);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(droidninja.filepicker.R.drawable.image_placeholder);
        requestOptions.error(droidninja.filepicker.R.drawable.image_placeholder);

        boolean isUrl = getIntent().getBooleanExtra(DMSConstants.IS_URL, false);
        String url = getIntent().getStringExtra(DMSConstants.DOCUMENTS);

        if (isUrl)
            Glide.with(this).load(url)
                    .apply(requestOptions)
                    .into(zoomView);
        else
            Glide.with(this).load(new File(getIntent().getStringExtra(DMSConstants.DOCUMENTS)))
                    .apply(requestOptions)
                    .into(zoomView);

    }

    @OnClick(R.id.backButton)
    public void onViewClicked() {
        onBackPressed();
    }
}