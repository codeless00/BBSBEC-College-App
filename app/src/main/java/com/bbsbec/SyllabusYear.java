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
import android.widget.Toast;

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



import org.json.JSONException;

import java.util.Objects;

public class SyllabusYear extends AppCompatActivity {

    String branch;
    RecyclerView recyclerView;
    ActionBar syllabusToolbar;
    String[] cse_year = {"Syllabus 2018", "Syllabus 2021"};
    String[] bca_year = {"Syllabus 2015", "Syllabus 2019", "Syllabus 2021"};
//    AdView adView_year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_syllabus_year);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.syllabus_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Window window = getWindow();
        View decorview = window.getDecorView();
        WindowInsetsControllerCompat wic = new WindowInsetsControllerCompat(window, decorview);
        wic.setAppearanceLightStatusBars(false);

        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.status_bar));



        syllabusToolbar = getSupportActionBar();

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        Intent i = getIntent();
        branch = i.getStringExtra("BRANCH_NAME");

        recyclerView = (RecyclerView) findViewById(R.id.syllabus_year_recycleview);

//        SyllabusYearAdapter syllabusYearAdapter = null;

        String year_name = branch + "_syllabus";
        syllabusToolbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#162242")));
        syllabusToolbar.setTitle("SELECT SCHEME ("+branch.toUpperCase()+")");

        String[] year_list = InternalStorage.deserializeStringArray(getApplicationContext(),branch + "_syllabus_year");
//        syllabusYearAdapter = new SyllabusYearAdapter(getApplicationContext(), year_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SemesterYearAdapter subjectAdapter = new SemesterYearAdapter(this, year_list, getLayoutInflater());
        recyclerView.setAdapter(subjectAdapter);

//        adView_year = (AdView)findViewById(R.id.adview_year);
//
//        loadAds();


//        setSupportActionBar(syllabusToolbar);



//        syllabusToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

//        listView.setAdapter(syllabusYearAdapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//                    Intent i = new Intent(SyllabusYear.this,SyllabusSemester.class);
//                    i.putExtra("BRANCH_NAME",branch);
//                    String year = year_list[position];
//                    i.putExtra("YEAR",year);
//                    startActivity(i);
//                }
//
//        });
    }

//    private void loadAds() {
//        try {
//            String[] ad_setting = InternalStorage.deserializeStringArray(this,"ads_year_settings");
//            String show_banner_ads = ad_setting[0];
////            String banner_id = ad_setting[1];
//            if (Boolean.parseBoolean(show_banner_ads)){
//                Log.d("FIRE","LAUNCHING MAIN ADS");
////                adview_banner.setAdUnitId(banner_id);
//                adView_year.setVisibility(View.VISIBLE);
//                AdRequest adRequest = new AdRequest.Builder().build();
//                adView_year.loadAd(adRequest);
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


    public class SemesterYearAdapter extends RecyclerView.Adapter<SemesterYearAdapter.ViewHolder> {

        Context context;
        String[] semester;
        LayoutInflater inflater;

        public SemesterYearAdapter(Context ctx, String[] sem, LayoutInflater layoutInflater) {
            this.semester = sem;
            this.inflater = layoutInflater;
            this.context = ctx;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.syllabus_year_list, parent, false);
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
                    Intent i = new Intent(context,SyllabusSemester.class);
                    i.putExtra("BRANCH_NAME",branch);
                    String year = semester[holder.getAdapterPosition()];
                    i.putExtra("YEAR",year);
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
                subText = itemView.findViewById(R.id.semester_year_pyq_text);
                cardView_sub = itemView.findViewById(R.id.semester_year_pyq_card);
            }
        }
    }
}