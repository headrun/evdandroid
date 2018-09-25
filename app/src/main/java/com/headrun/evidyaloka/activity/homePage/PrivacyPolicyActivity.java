package com.headrun.evidyaloka.activity.homePage;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.headrun.evidyaloka.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private WebView wvPrivacyPolicy;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_privacy_policy);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.privacy_policy));

        wvPrivacyPolicy = findViewById(R.id.wvPrivacyPolicy);
        progressBar = findViewById(R.id.progressBar);

        wvPrivacyPolicy.getSettings().setJavaScriptEnabled(true);
        wvPrivacyPolicy.setWebViewClient(new PrivacyPolicyWebClient());
        wvPrivacyPolicy.loadUrl(getString(R.string.privacy_policy_url));
        wvPrivacyPolicy.setHorizontalScrollBarEnabled(false);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class PrivacyPolicyWebClient extends WebViewClient
    {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(view.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(PrivacyPolicyActivity.this,"Error loading Privacy Policy, Please check after some time.",Toast.LENGTH_LONG).show();
        }
    }
}
