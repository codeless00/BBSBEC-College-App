package com.bbsbec;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class SubjectView extends AppCompatActivity {

    RecyclerView recyclerView;
    String branch;
    String page;
    String fp;
    String[] subject;

    ActionBar subjectActionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_view);

        // Apply Window Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.subjectview_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Window window = getWindow();
        View decorView = window.getDecorView();
        WindowInsetsControllerCompat wic = new WindowInsetsControllerCompat(window, decorView);
        wic.setAppearanceLightStatusBars(false);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar));

        Intent i = getIntent();
        branch = i.getStringExtra("BRANCH");
        page = i.getStringExtra("PAGE");
        String title = "None";
        fp = null;

        subjectActionbar = getSupportActionBar();
        if (Objects.equals(page, "pyq")) {
            title = branch.toUpperCase() + " QUESTION PAPER";
            fp = branch + "_pyq";
        } else if (Objects.equals(page, "notes")) {
            title = branch.toUpperCase() + " NOTES";
            fp = branch + "_notes";
        } else if (Objects.equals(page, "timetable")) {
            title = branch.toUpperCase() + " TIME TABLE";
            fp = branch + "_timetable";
        }

        assert subjectActionbar != null;
        subjectActionbar.setTitle(title);
        subjectActionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#162242")));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        recyclerView = findViewById(R.id.subject_view_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        subject = InternalStorage.deserializeStringArray(getApplicationContext(), fp + "_subject");
        SubjectAdapter subjectAdapter = new SubjectAdapter(this, subject, getLayoutInflater());
        recyclerView.setAdapter(subjectAdapter);
    }

    private void callWebView(String url, String title) {
        Intent i = new Intent(getApplicationContext(), cardfirst.class);
        i.putExtra("URL", url);
        i.putExtra("TITLE", title);
        startActivity(i);
    }

    private void callPdfView(String url, String subName, String suffix, boolean loadOffline, String title) {
        Intent i = new Intent(getApplicationContext(), MyPDFViewer.class);
        i.putExtra("URL", url);
        i.putExtra("SUBNAME", subName);
        i.putExtra("SUFFIX", suffix);
        i.putExtra("LOADOFFLINE", loadOffline);
        i.putExtra("TITLE",title);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

        Context context;
        String[] subjects;
        LayoutInflater inflater;

        public SubjectAdapter(Context ctx, String[] subject, LayoutInflater layoutInflater) {
            this.subjects = subject;
            this.inflater = layoutInflater;
            this.context = ctx;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.subject_list, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.subText.setText(subjects[position]);
//            holder.subText.setBackgroundColor(Color.TRANSPARENT);
//            holder.subText.setTextColor(holder.subText.getResources().getColor(R.color.color_grey));
            if (Objects.equals(page, "notes")){
                holder.cardView_sub.setCardBackgroundColor(ContextCompat.getColor(SubjectView.this,R.color.notes_subject_background));
            }
            else if (Objects.equals(page, "pyq")){
                holder.cardView_sub.setCardBackgroundColor(ContextCompat.getColor(SubjectView.this,R.color.pyq_subject_background));
            }
            else if (Objects.equals(page, "timetable")){
                holder.cardView_sub.setCardBackgroundColor(ContextCompat.getColor(SubjectView.this,R.color.timetable_subject_background));
            }
            holder.cardView_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] links = InternalStorage.deserializeStringArray(getApplicationContext(),fp + "_link");
                    String url = links[holder.getAdapterPosition()];
                    if (Objects.equals(page, "notes")){
                        callPdfView(url,fp+"_"+subjects[holder.getAdapterPosition()],"notes_pdf_offline",true,branch.toUpperCase() + " " + subjects[holder.getAdapterPosition()] + " NOTES");
                    } else if (Objects.equals(page,"timetable")) {
                        callPdfView(url,fp+"_"+subjects[holder.getAdapterPosition()],"timetable_pdf_offline",true, branch.toUpperCase() + " " + subjects[holder.getAdapterPosition()]);
                    } else {
                        callWebView(url,subject[holder.getAdapterPosition()] + " PYQ");
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return subjects.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView subText;
            CardView cardView_sub;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                subText = itemView.findViewById(R.id.subject_pyq_text);
                cardView_sub = itemView.findViewById(R.id.subject_pyq_card);
            }
        }
    }
}
