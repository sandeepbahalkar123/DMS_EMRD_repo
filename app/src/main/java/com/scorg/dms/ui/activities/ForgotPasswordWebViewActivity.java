package com.scorg.dms.ui.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.scorg.dms.R;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.ui.customesViews.CustomProgressDialog;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.util.Config;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordWebViewActivity extends AppCompatActivity implements HelperResponse {

    @BindView(R.id.webViewLayout)
    WebView mWebViewObject;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    String mUrl;
    @BindView(R.id.backImageView)
    ImageView backImageView;
    @BindView(R.id.titleTextView)
    CustomTextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_forgot_password);
        ButterKnife.bind(this);
        titleTextView.setText(getString(R.string.forgot_password_header));
        loadWebViewData(Config.FORGOT_PASSWORD_URL);
    }


    private void loadWebViewData(String url) {

        final CustomProgressDialog customProgressDialog = new CustomProgressDialog(this);
        customProgressDialog.show();

        if (url != null) {
            mWebViewObject.setVisibility(View.VISIBLE);

            // Let's display the progress in the activity title bar, like the
            // browser app does.

            WebSettings webSettings = mWebViewObject.getSettings();

            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(true);
            webSettings.setDefaultTextEncodingName("utf-8");

            mWebViewObject.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    progressBar.setProgress(progress);
                    if (progress > 90)
                        customProgressDialog.dismiss();
                }
            });


            mWebViewObject.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    progressBar.setVisibility(View.GONE);
                }

                public void onPageFinished(WebView view, String url) {
                    progressBar.setVisibility(View.GONE);
                   /* if (title != null)


                    if (mWebViewObject.canGoBack()) {

                    } else {

                    }*/

                }
            });

            mWebViewObject.loadUrl(url);
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebViewObject.canGoBack()) {
            mWebViewObject.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

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

    @OnClick({R.id.backImageView, R.id.titleTextView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                onBackPressed();
                break;
            case R.id.titleTextView:
                break;
        }
    }
}
