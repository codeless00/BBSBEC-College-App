package com.bbsbec;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Arrays;
import java.util.Objects;

public class ActiveTask extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView notask;
    CardView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_active_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.active_task_main), (v, insets) -> {
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
        getSupportActionBar().setTitle("Active Task");

        recyclerView = (RecyclerView) findViewById(R.id.active_task_recylclerview);
        button = (CardView) findViewById(R.id.active_task_whatsapp_button);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] task_name = InternalStorage.deserializeStringArray(this,"active_task_names");
        String[] task_like = InternalStorage.deserializeStringArray(this,"active_task_likes");

        notask = (TextView) findViewById(R.id.text_notask);

        if (!InternalStorage.isFileExist(this,"FoundActiveTask")){
            notask.setVisibility(View.VISIBLE);
        }
        else {


//            Log.d("FIRE", Arrays.toString(task_name));
//            Log.d("FIRE", Arrays.toString(task_like));

            //        String[] contributor_bluetick = new String[]{"true","false","true","false","true","false","true","false","true","false"};
//        String[] company_title = InternalStorage.deserializeStringArray(getApplicationContext(),"training_main_title");
//        String[] company_subtitle = InternalStorage.deserializeStringArray(getApplicationContext(),"training_main_subtitle");;
//        String[] company_image = new String[1];

            ActiveTaskAdapter activeTaskAdapter = new ActiveTaskAdapter(this, task_name, task_like);
            recyclerView.setAdapter(activeTaskAdapter);

            String num = InternalStorage.deserializeStringArray(this,"settings")[2];
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnected()){
                        try {
//                PackageManager pm = this.getPackageManager();
//                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                            Intent in = new Intent(Intent.ACTION_VIEW);
                            in.setData(Uri.parse(num));
                            startActivity(in);
                        } catch (Exception e) {
                            Toast.makeText(ActiveTask.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }



    }
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (!(activeNetwork != null && activeNetwork.isConnected())){
            Toast.makeText(this, "Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
        return activeNetwork != null && activeNetwork.isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public class ActiveTaskAdapter extends RecyclerView.Adapter<ActiveTaskAdapter.ViewHolder> {

        Context context;
        String[] names;
        String[] likes;

        public ActiveTaskAdapter(Context ctx, String[] name, String[] like) {
            this.names = name;
            this.likes = like;
            this.context = ctx;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.active_task_recycleview_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.task_name.setText(names[position]);
            holder.task_like.setText(likes[position]);
//            boolean isbluetick = Boolean.parseBoolean(blueticks[position]);
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
            return names.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView task_name;
            TextView task_like;
//            ImageView contributor_bluetick;
//            CardView cardview_main_list;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                task_name = itemView.findViewById(R.id.active_task_name);
                task_like = itemView.findViewById(R.id.active_task_like);
//                contributor_bluetick = itemView.findViewById(R.id.contributor_bluetick);
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