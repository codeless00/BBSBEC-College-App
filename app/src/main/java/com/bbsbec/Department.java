package com.bbsbec;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;


import java.util.Objects;

public class Department extends AppCompatActivity implements  View.OnClickListener{


    Toolbar department_toolbar;
    CardView cardView_bca;
    CardView cardView_bba;
    CardView cardView_mba;
    CardView cardView_applied;
    CardView cardView_cse;
    CardView cardView_ece;
    CardView cardView_ce;
    CardView cardView_ee;
    CardView cardView_me;
    CardView cardView_ae;

    String page;
//    AdView adview_department;



    @Override
    public void onClick(View v){
        String branch_name = "";

        if (v.getId() == R.id.department_bca) {
            branch_name = "bca";
        } else if (v.getId() == R.id.department_bba) {
            branch_name = "bba";
        }else if (v.getId() == R.id.department_mba) {
            branch_name = "mba";
        }else if (v.getId() == R.id.department_as) {
            branch_name = "applied";
        }else if (v.getId() == R.id.department_cse) {
            branch_name = "cse";
        }else if (v.getId() == R.id.department_ece) {
            branch_name = "ece";
        }else if (v.getId() == R.id.department_civil) {
            branch_name = "ce";
        }else if (v.getId() == R.id.department_ee) {
            branch_name = "ee";
        }else if (v.getId() == R.id.department_me) {
            branch_name = "me";
        }else if (v.getId() == R.id.department_ae) {
            branch_name = "ae";
        }

        if (Objects.equals(page, "syllabus")) {
            callSyllabusYearActivity(branch_name);
        } else if (Objects.equals(page, "pyq")) {
            callSubjectActivity(branch_name, page);
        }else if (Objects.equals(page, "notes")) {
            callSubjectActivity(branch_name, page);
        }else if (Objects.equals(page, "timetable")) {
            callSubjectActivity(branch_name, page);
        }
    }

    private void callSyllabusYearActivity(String name) {
        Intent i = new Intent(this,SyllabusYear.class);
        i.putExtra("BRANCH_NAME",name);
        startActivity(i);
    }

    private void callSubjectActivity(String branch, String page) {
        Intent i = new Intent(this,SubjectView.class);
        i.putExtra("BRANCH",branch);
        i.putExtra("PAGE",page);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_department);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.department_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(Department.this,R.color.navigation_bar)); // Use your desired color
        View decorview = window.getDecorView();
        WindowInsetsControllerCompat wic = new WindowInsetsControllerCompat(window, decorview);
        wic.setAppearanceLightStatusBars(false);

        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.status_bar));

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#162242"))); // Example color
//        department_toolbar = findViewById(R.id.department_toolbar);

        Intent i = getIntent();
        page = i.getStringExtra("PAGE");

        String drawable_filename = "gradient_department" + page;

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.gradient_department_syllabus);;

        if (Objects.equals(page, "syllabus")){
            drawable = ContextCompat.getDrawable(this, R.drawable.gradient_department_syllabus);
        } else if (Objects.equals(page, "pyq")) {
            drawable = ContextCompat.getDrawable(this, R.drawable.gradient_department_pyq);
        } else if (Objects.equals(page, "notes")) {
            drawable = ContextCompat.getDrawable(this, R.drawable.gradient_department_notes);
        } else if (Objects.equals(page, "timetable")) {
            drawable = ContextCompat.getDrawable(this, R.drawable.gradient_department_timetable);
        }



        if (Objects.equals(page, "syllabus")){
            Objects.requireNonNull(getSupportActionBar()).setTitle("SYLLABUS");

//            department_toolbar.setLogo(R.drawable.main_syllabus);


        } else if (Objects.equals(page, "pyq")) {
//            getSupportActionBar().setLogo();
//            getSupportActionBar().setIcon(R.drawable.pyq_logo);
//            department_toolbar.setLogo(R.drawable.pyq_logo);
            Objects.requireNonNull(getSupportActionBar()).setTitle("QUESTION PAPER");
        }
        else if (Objects.equals(page, "notes")) {
//            department_toolbar.setLogo(R.drawable.pyq_logo);
            Objects.requireNonNull(getSupportActionBar()).setTitle("NOTES");
        }
        else if (Objects.equals(page, "timetable")) {
//            department_toolbar.setLogo(R.drawable.pyq_logo);
            Objects.requireNonNull(getSupportActionBar()).setTitle("TIME TABLE");
        }


//        setSupportActionBar(department_toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

//        getSupportActionBar().setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });


//        adview_department = (AdView) findViewById(R.id.adview_department);
        cardView_bca = (CardView) findViewById(R.id.department_bca);
        cardView_bba = (CardView) findViewById(R.id.department_bba);
        cardView_mba = (CardView) findViewById(R.id.department_mba);
        cardView_applied = (CardView) findViewById(R.id.department_as);
        cardView_cse = (CardView) findViewById(R.id.department_cse);
        cardView_ece = (CardView) findViewById(R.id.department_ece);
        cardView_ce = (CardView) findViewById(R.id.department_civil);
        cardView_ee = (CardView) findViewById(R.id.department_ee);
        cardView_me = (CardView) findViewById(R.id.department_me);
        cardView_ae = (CardView) findViewById(R.id.department_ae);

        FrameLayout bca = findViewById(R.id.frame_bca);
        FrameLayout bba = findViewById(R.id.frame_bba);
        FrameLayout mba = findViewById(R.id.frame_mba);
        FrameLayout applied = findViewById(R.id.frame_applied);
        FrameLayout cse = findViewById(R.id.frame_cse);
        FrameLayout ece = findViewById(R.id.frame_ece);
        FrameLayout civil = findViewById(R.id.frame_ce);
        FrameLayout ee = findViewById(R.id.frame_ee);
        FrameLayout me = findViewById(R.id.frame_me);
        FrameLayout ae = findViewById(R.id.frame_ae);



        bca.setBackground(drawable);
        bba.setBackground(drawable);
        mba.setBackground(drawable);
        applied.setBackground(drawable);
        cse.setBackground(drawable);
        ece.setBackground(drawable);
        civil.setBackground(drawable);
        ee.setBackground(drawable);
        me.setBackground(drawable);
        ae.setBackground(drawable);



//        cardView_bba.setBackground(drawable);
//        cardView_mba.setBackground(drawable);
//        cardView_applied.setBackground(drawable);
//        cardView_cse.setBackground(drawable);
//        cardView_ece.setBackground(drawable);
//        cardView_ce.setBackground(drawable);
//        cardView_ee.setBackground(drawable);
//        cardView_me.setBackground(drawable);
//        cardView_ae.setBackground(drawable);




        cardView_bca.setOnClickListener((View.OnClickListener)this);
        cardView_bba.setOnClickListener((View.OnClickListener)this);
        cardView_mba.setOnClickListener((View.OnClickListener)this);
        cardView_applied.setOnClickListener((View.OnClickListener)this);
        cardView_cse.setOnClickListener((View.OnClickListener)this);
        cardView_ece.setOnClickListener((View.OnClickListener)this);
        cardView_ce.setOnClickListener((View.OnClickListener)this);
        cardView_ee.setOnClickListener((View.OnClickListener)this);
        cardView_me.setOnClickListener((View.OnClickListener)this);
        cardView_ae.setOnClickListener((View.OnClickListener)this);
//        loadAds();


    }

//    private void loadAds() {
//
//        try {
//            String[] ad_setting = InternalStorage.deserializeStringArray(this,"ads_department_settings");
//            String show_banner_ads = ad_setting[0];
////            String banner_id = ad_setting[1];
//            if (Boolean.parseBoolean(show_banner_ads)){
//                Log.d("FIRE","LAUNCHING MAIN ADS");
////                adview_department.setAdUnitId(banner_id);
////                adview_department.setVisibility(View.VISIBLE);
////                AdRequest adRequest = new AdRequest.Builder().build();
////                adview_department.loadAd(adRequest);
//            }
//        }catch (Exception ignored){
//
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}