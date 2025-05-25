package com.bbsbec;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Training extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_training);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.training_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
        getSupportActionBar().setTitle("Training And Placement");

        recyclerView = (RecyclerView) findViewById(R.id.training_recylclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String[] company_title = InternalStorage.deserializeStringArray(getApplicationContext(),"training_main_title");
        String[] company_subtitle = InternalStorage.deserializeStringArray(getApplicationContext(),"training_main_subtitle");;
        String[] company_image = new String[1];

        TrainingAdapter trainingAdapter = new TrainingAdapter(this, company_title, company_subtitle, company_image);
        recyclerView.setAdapter(trainingAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class TrainingAdapter extends RecyclerView.Adapter<TrainingAdapter.ViewHolder> {

        Context context;
        String[] titles;
        String[] subTitles;
        String[] images;

        public TrainingAdapter(Context ctx, String[] title, String[] subTitle, String[] image) {
            this.titles = title;
            this.subTitles = subTitle;
            this.images = image;
            this.context = ctx;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.training_recycleview_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.title_text.setText(Html.fromHtml(titles[position], Html.FROM_HTML_MODE_LEGACY));
            holder.subTitle_text.setText(Html.fromHtml(subTitles[position], Html.FROM_HTML_MODE_LEGACY));
            Bitmap image = InternalStorage.loadImageFromInternalStorage("Training_logo_" + position,"TrainingImageDir",context);
            holder.image_url.setImageBitmap(image);
//            holder.subTitle.setTextColor(holder.subText.getResources().getColor(R.color.color_grey));
//            holder.cardView_sub.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(context,SyllabusFragActivity.class);
//                    String basefile = branch + "_syllabus" + "_" + year + "_" + semester[holder.getAdapterPosition()];
//                    i.putExtra("TITLE",semester[holder.getAdapterPosition()]);
//                    i.putExtra("BRANCH",branch.toUpperCase());
//                    i.putExtra("YEAR",year);
//                    i.putExtra("BASEFILE",basefile);
//
//                    startActivity(i);
//                }
//            });
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title_text;
            ImageView image_url;
            TextView subTitle_text;
            CardView cardview_main_list;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title_text = itemView.findViewById(R.id.training_title);
                subTitle_text = itemView.findViewById(R.id.training_subtitle);
                image_url = itemView.findViewById(R.id.training_image);
                cardview_main_list = itemView.findViewById(R.id.training_main_card);

                cardview_main_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Training.this,TrainingDetail.class);
                        i.putExtra("POSITION",getAdapterPosition());
                        i.putExtra("TITLE",titles[getAdapterPosition()]);
                        startActivity(i);
                    }
                });
            }
        }
    }
}