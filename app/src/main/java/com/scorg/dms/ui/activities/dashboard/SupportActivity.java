package com.scorg.dms.ui.activities.dashboard;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.scorg.dms.R;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.util.CommonMethods;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;


/**
 * Created by jeetal on 3/11/17.
 */

@RuntimePermissions
public class SupportActivity extends AppCompatActivity  {
    private static final String TAG = "SupportActivity";

    @BindView(R.id.callTextView)
    CustomTextView callTextView;
    @BindView(R.id.emailtextView)
    CustomTextView emailtextView;
    @BindView(R.id.backImageView)
    ImageView backImageView;
    @BindView(R.id.titleTextView)
    CustomTextView titleTextView;
    @BindView(R.id.userInfoTextView)
    CustomTextView userInfoTextView;
    @BindView(R.id.dateTextview)
    CustomTextView dateTextview;
    @BindView(R.id.versionText)
    CustomTextView versionText;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_base_layout);
        ButterKnife.bind(this);
        initialize();

    }

    private void initialize() {
        mContext = SupportActivity.this;
        titleTextView.setText(getString(R.string.support));
        backImageView.setVisibility(View.VISIBLE);
        String versionString = "v" + CommonMethods.getVersionName(mContext) + "(" + CommonMethods.getVersionCode(mContext) + ")";
        versionText.setText(versionString);
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    void doCallSupport() {
        callSupport();
    }

    private void callSupport() {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:9921385816"));
            startActivity(callIntent);
        }catch(SecurityException e){
            Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SupportActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }


    @OnClick({R.id.callTextView, R.id.emailtextView, R.id.backImageView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.callTextView:
                SupportActivityPermissionsDispatcher.doCallSupportWithCheck(this);
                break;
            case R.id.emailtextView:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "anirudh.kulkarni@scorgtechnologies.com"));
                    intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject");
                    intent.putExtra(Intent.EXTRA_TEXT, "your_text");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(mContext, "Respective Application not supported to this device", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.backImageView:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
