package com.bbsbec;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.Time;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class Contributors extends AppCompatActivity {

    RecyclerView recyclerView;
    ContributorAdapter contributorAdapter;
    CardView button;
    BroadcastReceiver downloadComplete;
    BroadcastReceiver foundactivetask;
    BroadcastReceiver noactivetask;

    ProgressDialog progressDialog;
    ImageView red_dot_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contributors);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.contributor_main), (v, insets) -> {
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
        getSupportActionBar().setTitle("Contributors");

        recyclerView = (RecyclerView) findViewById(R.id.contributor_recylclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        String[] contributor_name = new String[]{"Raj Aaryan","Rahul Kumar","Raj Aaryan"};
//        String[] contributor_like = new String[]{"11 likes","15 likes","11 likes","15 likes","11 likes","15 likes","11 likes","15 likes","11 likes","15 likes"};
//        String[] contributor_bluetick = new String[]{"true","false","true","false","true","false","true","false","true","false"};
//        String[] company_title = InternalStorage.deserializeStringArray(getApplicationContext(),"training_main_title");
//        String[] company_subtitle = InternalStorage.deserializeStringArray(getApplicationContext(),"training_main_subtitle");;
//        String[] company_image = new String[1];
        LinkedHashMap<String,ContributorDetailModel> profile = InternalStorage.deserializeLinkedHashMap(getApplicationContext(),"contributor_profile_map");

        String[] likes = InternalStorage.deserializeStringArray(getApplicationContext(),"contributor_likes");
        String[] order = InternalStorage.deserializeStringArray(getApplicationContext(),"contributor_ordering");
        contributorAdapter = new ContributorAdapter(this, likes, order, profile);
        recyclerView.setAdapter(contributorAdapter);


        downloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                notifychange();
            }
        };

        foundactivetask = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                show_dot();
            }
        };

        noactivetask = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                remove_dot();
            }
        };

        IntentFilter filter = new IntentFilter("DOWNLOAD_COMPLETE");
        IntentFilter dot_filter = new IntentFilter("FOUND_ACTIVE_TASK");
        IntentFilter notask = new IntentFilter("NO_ACTIVE_TASK");




        progressDialog = new ProgressDialog(this, com.airbnb.lottie.R.style.Base_Theme_AppCompat_Light_Dialog);

        int filesize = InternalStorage.getFileCountInDirectory(getApplicationContext(),"ContributorImageDir");
        LinkedHashMap<String,ContributorDetailModel> rr = InternalStorage.deserializeLinkedHashMap(getApplicationContext(),"contributor_profile_map");

        if (filesize != rr.size()){
            progressDialog.setMessage("Loading content, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        red_dot_image = (ImageView) findViewById(R.id.red_dot_image);

        String[] task_name = InternalStorage.deserializeStringArray(this,"active_task_names");
        String[] task_like = InternalStorage.deserializeStringArray(this,"active_task_likes");

//        notask = (TextView) findViewById(R.id.text_notask);

        if(InternalStorage.isFileExist(this,"FoundActiveTask")){
            show_dot();
        }




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(downloadComplete, filter, Context.RECEIVER_NOT_EXPORTED);
            registerReceiver(foundactivetask,dot_filter,Context.RECEIVER_NOT_EXPORTED);
            registerReceiver(noactivetask,notask,Context.RECEIVER_NOT_EXPORTED);

        }


        button = (CardView) findViewById(R.id.contributor_main_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Contributors.this,ActiveTask.class);
                startActivity(i);
            }
        });
    }

    private void remove_dot() {
        red_dot_image.setVisibility(View.GONE);
    }

    private void show_dot() {
        red_dot_image.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(downloadComplete);
        unregisterReceiver(foundactivetask);
        unregisterReceiver(noactivetask);
    }

    private void notifychange() {
//        Toast.makeText(this,"Downlaod complete",Toast.LENGTH_LONG).show();
        recyclerView.setAdapter(contributorAdapter);
        if (progressDialog != null && progressDialog.isShowing() && !isFinishing() && !isDestroyed()) {
            progressDialog.dismiss();
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


    public class ContributorAdapter extends RecyclerView.Adapter<ContributorAdapter.ViewHolder> {

        Context context;
        String[] likes;
        String[] ordering;
        String[] names;
        LinkedHashMap<String,ContributorDetailModel> profiles;
//        String[] likes;
//        String[] blueticks;

        public ContributorAdapter(Context ctx, String[] like, String[] order, LinkedHashMap<String,ContributorDetailModel> profile) {
            this.profiles = profile;
            this.likes = like;
            this.ordering = order;
            this.context = ctx;
//            this.keysList = new ArrayList<>(like.keySet());
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.contributors_recycleview_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//            Log.d("FIRE",keysList.toString());
//            Log.d("FIRE",String.valueOf(position));
//            String key = keysList.get(position);
//            String value = likes.get(key);
            String cont_name = "Null";
            cont_name = Objects.requireNonNull(profiles.get(ordering[position])).getName();
//            boolean isbluetick = profiles.get(ordering[position]).getBluetick();

            Bitmap image = InternalStorage.loadImageFromInternalStorage(ordering[position] + "_image.png","ContributorImageDir",context);
            holder.contributor_profilepic.setImageResource(R.drawable.default_image);
            if (image != null){
                holder.contributor_profilepic.setImageBitmap(image);
            }
            holder.contributor_position.setTextSize(16);
            holder.contributor_name.setTextSize(16);
            holder.contributor_bluetick.setVisibility(View.GONE);

            if (position == 0){
                holder.contributor_position.setTextSize(24);
                holder.contributor_name.setTextSize(20);
                holder.contributor_bluetick.setVisibility(View.VISIBLE);
            } else if (position == 1) {
                holder.contributor_position.setTextSize(22);
                holder.contributor_name.setTextSize(18);
                holder.contributor_bluetick.setVisibility(View.VISIBLE);
            } else if (position == 2) {
                holder.contributor_position.setTextSize(21);
                holder.contributor_name.setTextSize(17);
                holder.contributor_bluetick.setVisibility(View.VISIBLE);
            } else if (position == 3) {
                holder.contributor_position.setTextSize(20);
                holder.contributor_name.setTextSize(17);
            } else if (position == 4) {
                holder.contributor_position.setTextSize(19);
                holder.contributor_name.setTextSize(16);
            } else if (position > 9){
                holder.contributor_position.setTextSize(14);
            }

            String text_position = String.valueOf(position + 1) ;
            holder.contributor_position.setText(text_position);

            holder.contributor_name.setText(cont_name);
            holder.contributor_like.setText(likes[position]);


//            if (isbluetick){
//                holder.contributor_bluetick.setVisibility(View.VISIBLE);
//            }
//            Bitmap image = InternalStorage.loadImageFromInternalStorage("Training_logo_" + position,context);
//            holder.image_url.setImageBitmap(image);
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
            return ordering.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView contributor_name;
            TextView contributor_like;
            ImageView contributor_bluetick;
            ImageView contributor_profilepic;
            TextView contributor_position;
//            CardView cardview_main_list;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                contributor_name = itemView.findViewById(R.id.contributor_name);
                contributor_like = itemView.findViewById(R.id.contributor_liketext);
                contributor_bluetick = itemView.findViewById(R.id.contributor_bluetick);
                contributor_profilepic = itemView.findViewById(R.id.contributor_image);
                contributor_position = itemView.findViewById(R.id.contributor_position);
//                cardview_main_list = itemView.findViewById(R.id.training_main_card);

//                cardview_main_list.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent i = new Intent(Training.this,TrainingDetail.class);
//                        i.putExtra("POSITION",getAdapterPosition());
//                        i.putExtra("TITLE",titles[getAdapterPosition()]);
//                        startActivity(i);
//                    }
//                });
            }
        }
    }




}