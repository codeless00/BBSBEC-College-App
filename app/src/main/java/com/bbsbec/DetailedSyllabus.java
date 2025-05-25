package com.bbsbec;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;


public class DetailedSyllabus extends AppCompatActivity {

    TextView textView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detailed_syllabus);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detailed_syllabus_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Window window = getWindow();
        View decorview = window.getDecorView();
        WindowInsetsControllerCompat wic = new WindowInsetsControllerCompat(window, decorview);
        wic.setAppearanceLightStatusBars(false);

        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.status_bar));




        toolbar = (Toolbar) findViewById(R.id.detailed_syllabus_toolbar);

        Intent i = getIntent();
        String basefile = i.getStringExtra("BASEFILE");
        String position = i.getStringExtra("POSITION");
        String theoryfile = i.getStringExtra("THEORYFILE");
        assert position != null;
        String title = InternalStorage.deserializeStringArray(getApplicationContext(),theoryfile)[Integer.parseInt(position)];

        toolbar.setTitle(title);


        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        textView = (TextView) findViewById(R.id.detailed_syllabus_textview);
        String[] val = InternalStorage.deserializeStringArray(getApplicationContext(),basefile);
        assert position != null;
        String text = val[Integer.parseInt(position)];
        textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));

//        adView_syllabus = (AdView) findViewById(R.id.adview_syllabusdetailed);
//        loadAds();

    }

//    private void loadAds() {
//
//        try {
//            String[] ad_setting = InternalStorage.deserializeStringArray(this,"ads_syllabus_detailed_settings");
//            String show_banner_ads = ad_setting[0];
////            String banner_id = ad_setting[1];
//            if (Boolean.parseBoolean(show_banner_ads)){
//                Log.d("FIRE","LAUNCHING MAIN ADS");
////                adview_banner.setAdUnitId(banner_id);
//                adView_syllabus.setVisibility(View.VISIBLE);
//                AdRequest adRequest = new AdRequest.Builder().build();
//                adView_syllabus.loadAd(adRequest);
//            }
//        }catch (Exception ignored){
//
//        }
//    }
}