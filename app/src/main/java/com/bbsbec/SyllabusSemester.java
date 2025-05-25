package com.bbsbec;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.Arrays;
import java.util.Objects;

public class SyllabusSemester extends AppCompatActivity {

    String branch;
    String year;
    RecyclerView recyclerView;
    ActionBar semesterToolbar;
//    AdView adView_semester;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_syllabus_semester);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.syllabus_semester_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Window window = getWindow();
        View decorview = window.getDecorView();
        WindowInsetsControllerCompat wic = new WindowInsetsControllerCompat(window, decorview);
        wic.setAppearanceLightStatusBars(false);

        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.status_bar));

        semesterToolbar = getSupportActionBar();

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        Intent i = getIntent();
        branch = i.getStringExtra("BRANCH_NAME");
        year = i.getStringExtra("YEAR");

        assert semesterToolbar != null;
        semesterToolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#162242")));
        semesterToolbar.setTitle("SELECT " + branch.toUpperCase() + " SEMESTER (" + year + ")" );


        recyclerView = (RecyclerView) findViewById(R.id.syllabus_semester_recycleview);

        String sem_file = branch + "_syllabus_" + year + "_semester";
        String[] sem_list = InternalStorage.deserializeStringArray(getApplicationContext(),sem_file);
        Log.e("datal", Arrays.toString(sem_list));



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SemesterAdapter subjectAdapter = new SemesterAdapter(this, sem_list, getLayoutInflater());
        recyclerView.setAdapter(subjectAdapter);

//        adView_semester = (AdView) findViewById(R.id.adview_semester);
//        loadAds();

    }

//    private void loadAds() {
//        try {
//            String[] ad_setting = InternalStorage.deserializeStringArray(this,"ads_semester_settings");
//            String show_banner_ads = ad_setting[0];
////            String banner_id = ad_setting[1];
//            if (Boolean.parseBoolean(show_banner_ads)){
//                Log.d("FIRE","LAUNCHING MAIN ADS");
////                adview_banner.setAdUnitId(banner_id);
//                adView_semester.setVisibility(View.VISIBLE);
//                AdRequest adRequest = new AdRequest.Builder().build();
//                adView_semester.loadAd(adRequest);
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


    public class SemesterAdapter extends RecyclerView.Adapter<SemesterAdapter.ViewHolder> {

        Context context;
        String[] semester;
        LayoutInflater inflater;

        public SemesterAdapter(Context ctx, String[] sem, LayoutInflater layoutInflater) {
            this.semester = sem;
            this.inflater = layoutInflater;
            this.context = ctx;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.syllabus_semester_list, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.subText.setText(semester[position]);
//            holder.subText.setBackgroundColor(Color.TRANSPARENT);
//            holder.subText.setTextColor(holder.subText.getResources().getColor(R.color.color_grey));
            holder.cardView_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context,SyllabusFragActivity.class);
                    String basefile = branch + "_syllabus" + "_" + year + "_" + semester[holder.getAdapterPosition()];
                    i.putExtra("TITLE",semester[holder.getAdapterPosition()]);
                    i.putExtra("BRANCH",branch.toUpperCase());
                    i.putExtra("YEAR",year);
                    i.putExtra("BASEFILE",basefile);

                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return semester.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView subText;
            CardView cardView_sub;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                subText = itemView.findViewById(R.id.semester_pyq_text);
                cardView_sub = itemView.findViewById(R.id.semester_pyq_card);
            }
        }
    }
}