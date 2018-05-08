package com.scorg.dms.ui.activities.patient_details;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.util.DMSConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.webViewLayout)
    WebView mWebViewObject;
    @BindView(R.id.backButton)
    AppCompatImageView backButton;
    @BindView(R.id.webViewTitle)
    TextView mWebViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        String url = getIntent().getStringExtra(getString(R.string.title_activity_selected_docs));
        String extension = getIntent().getStringExtra(getString(R.string.file_extension));
        String tempTitle = getIntent().getStringExtra(getString(R.string.title));
        if (!(DMSConstants.BLANK.equalsIgnoreCase(tempTitle) || tempTitle == null))
            mWebViewTitle.setText("" + tempTitle);
        // Hardcoded
//        String url = "http://che.org.il/wp-content/uploads/2016/12/pdf-sample.pdf";

        loadWebViewData(url, extension);
    }

    @OnClick(R.id.backButton)
    public void back() {
        onBackPressed();
    }

    private void loadWebViewData(String url, String extension) {
        if (url != null) {
            mWebViewObject.setVisibility(View.VISIBLE);

            // Let's display the progress in the activity title bar, like the
            // browser app does.

            mWebViewObject.getSettings().setJavaScriptEnabled(true);
//            mWebViewObject.getSettings().setSupportZoom(true);
//            mWebViewObject.getSettings().setBuiltInZoomControls(true);

            mWebViewObject.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    // Activities and WebViews measure progress with different scales.
                    // The progress meter will automatically disappear when we reach 100%
                    setProgress(progress * 1000);
                }
            });
            mWebViewObject.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                }
            });
            if (DMSConstants.BLANK.equalsIgnoreCase(extension) || extension == null)
                mWebViewObject.loadUrl(url);
            else {
                mWebViewObject.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
            }
        }
    }
}
