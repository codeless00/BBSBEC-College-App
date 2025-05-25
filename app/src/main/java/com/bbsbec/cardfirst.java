package com.bbsbec;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class cardfirst extends AppCompatActivity {

    WebView web_view;
    ProgressBar pb;
    RelativeLayout noInternet;
    Button retry;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cardfirst);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_card_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        Window window = getWindow();
        View decorview = window.getDecorView();
        WindowInsetsControllerCompat wic = new WindowInsetsControllerCompat(window, decorview);
        wic.setAppearanceLightStatusBars(false);

        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.status_bar));

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#162242")));

        web_view = findViewById(R.id.webview_activity);
        pb = findViewById(R.id.progress_bar);
        noInternet = findViewById(R.id.nointernet);
//        retry = (Button) findViewById(R.id.nointernetretry);

        Intent intent = getIntent();
        url = intent.getStringExtra("URL");
        String title = intent.getStringExtra("TITLE");
        if (url == null){
            url = "https://bbsbec.edu.in/";
        }
        getSupportActionBar().setTitle(title);

        loadWebView(url);

//        retry.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(cardfirst.this, "Retrying", Toast.LENGTH_SHORT).show();
//                noInternet.setVisibility(View.GONE);
//                web_view.setVisibility(View.VISIBLE);
//                loadWebView(url);
//            }
//        });










    }

    private void loadWebView(String url) {
        if (isConnected()){
            web_view.getSettings().setJavaScriptEnabled(true);
            web_view.loadUrl(url);
            web_view.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    pb.setVisibility(View.GONE);
                    super.onPageFinished(view, url);
                }
            });


            web_view.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    String filename = URLUtil.guessFileName(url,contentDisposition,"pdf");
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename);
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(),"Downlaod Started!",Toast.LENGTH_LONG).show();
                }
            });
        }

        else {
            noInternet.setVisibility(View.VISIBLE);
            web_view.setVisibility(View.GONE);
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    @Override
    public void onBackPressed() {
        if (web_view.canGoBack()){
            web_view.goBack();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}