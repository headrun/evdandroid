package com.headrun.evidyaloka.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.headrun.evidyaloka.R;

/**
 * Created by sujith on 1/2/17.
 */

public class RequestErrorActivity extends AppCompatActivity {

    WebView webview;
    String error_data = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_error);

        webview = (WebView) findViewById(R.id.web_view);


        Bundle data = getIntent().getExtras();

        if (data != null) {
            error_data = data.getString("error");
        }

        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);

        webview.loadData(error_data, "text/html", "UTF-8");

    }
}
