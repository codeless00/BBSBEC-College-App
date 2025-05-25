package com.bbsbec;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.w3c.dom.Text;

public class SyllabusFragActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    TextView syllabusTItle;
    Toolbar toolbar;
    SyllabusFragAdapter syllabusFragAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_syllabus_frag);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.syllabus_activity_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = (Toolbar) findViewById(R.id.syllabus_main_toolbar);
        syllabusTItle = (TextView) findViewById(R.id.syllabus_title);
        Intent i = getIntent();
        String title;
        title = i.getStringExtra("TITLE");
        String stitle = i.getStringExtra("BRANCH");
        String year = i.getStringExtra("YEAR");
        String basefile = i.getStringExtra("BASEFILE");
        toolbar.setTitle(title);

        setSupportActionBar(toolbar);
        syllabusTItle.setText(stitle + " SYLLABUS (" + year + ")");
        Log.d("POP",title);


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



        tabLayout = (TabLayout) findViewById(R.id.syllabus_tablayout);
        viewPager2 = (ViewPager2) findViewById(R.id.syllabus_viewpager);

        syllabusFragAdapter = new SyllabusFragAdapter(getSupportFragmentManager(),getLifecycle(),basefile);
        viewPager2.setAdapter(syllabusFragAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            if (position == 0){
                tab.setText("Theory");
            }else{
                tab.setText("Practical");
            }
        }).attach();
    }

}