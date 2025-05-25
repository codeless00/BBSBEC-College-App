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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrainingDetail extends AppCompatActivity {

    RecyclerView recyclerView;
    CardView button;

    WebView webView;
    ProgressBar pb;
    RelativeLayout noInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_training_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.training_detail_main), (v, insets) -> {
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

        recyclerView = (RecyclerView) findViewById(R.id.training_detail_recylclerview);
        webView = (WebView) findViewById(R.id.training_webview);
        pb = (ProgressBar) findViewById(R.id.training_progress_bar);
        noInternet = (RelativeLayout) findViewById(R.id.training_nointernet);


        Intent i = getIntent();
        int position = i.getIntExtra("POSITION",0);
        String title = i.getStringExtra("TITLE");
        Log.d("FIRE", "TITIEL ++++++++++++++++" + title);

        getSupportActionBar().setTitle(title);





        String contact = InternalStorage.deserializeStringArray(this,"training_main_contact")[position];
        String url = InternalStorage.deserializeStringArray(this,"training_main_url")[position];
        String message = InternalStorage.deserializeStringArray(this,"training_main_message")[position];


//        List<TrainingDetailModel> mylist = new ArrayList<>();
//
////        String[] company_section = new String[]{"Future's Finder", "Infosys Training","Future's Finder", "Infosys Training", "Reliance Jio"};
////        String[] company_detail = new String[]{"A great training institute in Mohali, Chandigarh.", "Best Place for training for graduate Students. One of the best Institute's student goes there.","Best Training Institute in Ludhiana and Best affordable training center.", "Best Place for training for graduate Students. One of the best Institute's student goes there.","It's one of the biggest public company of India. It's revenue is more that 15 lakh.It's one of the biggest public company of India. It's revenue is more that 15 lakh.It's one of the biggest public company of India. It's revenue is more that 15 lakh."};
//
//        TrainingDetailModel trainingDetailModel = new TrainingDetailModel("About us","Future Finders is a ground-breaking platform that develops young Indian talent that is motivated to advance and forge successful careers in IT. We provide a variety of courses to help you launch your career and locate the employer that will assist you as you rise to the top. Our expertise with the latest tools and techniques, and the experience of our professional experts help us deliver high-end services to our esteemed clients.Future Finders educates students and developers about the most recent technologies that are now popular. ");
//        TrainingDetailModel trainingDetailModel1 = new TrainingDetailModel("Why choose future finders over others","It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section ");
//        TrainingDetailModel trainingDetailModel2 = new TrainingDetailModel("Offers","It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section ");
//        TrainingDetailModel trainingDetailModel3 = new TrainingDetailModel("Features","It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section ");
//        TrainingDetailModel trainingDetailModel4 = new TrainingDetailModel("Course Offered","It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section ");
//        TrainingDetailModel trainingDetailModel5 = new TrainingDetailModel("Fees Structure","It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section ");
//        TrainingDetailModel trainingDetailModel6 = new TrainingDetailModel("Contact Us","It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section ");
//        TrainingDetailModel trainingDetailModel7 = new TrainingDetailModel("Future's Finder 2","It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section ");
//        TrainingDetailModel trainingDetailModel8 = new TrainingDetailModel("Future's Finder","It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section ");
//        TrainingDetailModel trainingDetailModel9 = new TrainingDetailModel("Future's Finder 2","It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section ");
//        TrainingDetailModel trainingDetailModel10 = new TrainingDetailModel("Future's Finder","It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section ");
//        TrainingDetailModel trainingDetailModel11 = new TrainingDetailModel("Future's Finder 2","It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section It's detailed section ");
//
//
//        mylist.add(trainingDetailModel);
//        mylist.add(trainingDetailModel1);
//        mylist.add(trainingDetailModel2);
//        mylist.add(trainingDetailModel3);
//        mylist.add(trainingDetailModel4);
//        mylist.add(trainingDetailModel5);
//        mylist.add(trainingDetailModel6);
//        mylist.add(trainingDetailModel7);
//        mylist.add(trainingDetailModel8);
//        mylist.add(trainingDetailModel8);
//        mylist.add(trainingDetailModel9);
//        mylist.add(trainingDetailModel10);
//        mylist.add(trainingDetailModel11);
        if (Objects.equals(url, "null")){
            TrainingDetailModel[][] model = InternalStorage.deserializeModel(this,"training_main_model");
            Log.d("FIRE","model lenght" + model.length);

            TrainingDetailAdapter trainingAdapter = new TrainingDetailAdapter(this, model[position]);
            webView.setVisibility(View.GONE);
            pb.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(trainingAdapter);

        } else {
            training_loadWebView(url);
        }


        button = (CardView) findViewById(R.id.whatsappButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone=" + contact + "&text=" + message;
                if (isConnected()){
                    try {
//                PackageManager pm = this.getPackageManager();
//                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                        Intent in = new Intent(Intent.ACTION_VIEW);
                        in.setData(Uri.parse(url));
                        startActivity(in);
                    } catch (Exception e) {
                        Toast.makeText(TrainingDetail.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void training_loadWebView(String url) {
        if (isConnected()){
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient(){
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


//            web_view.setDownloadListener(new DownloadListener() {
//                @Override
//                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//
//                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//                    request.allowScanningByMediaScanner();
//                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                    String filename = URLUtil.guessFileName(url,contentDisposition,"pdf");
//                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename);
//                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                    dm.enqueue(request);
//                    Toast.makeText(getApplicationContext(),"Downlaod Started!",Toast.LENGTH_LONG).show();
//                }
//            });
        }

        else {
            noInternet.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
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



    public class TrainingDetailAdapter extends RecyclerView.Adapter<TrainingDetailAdapter.ViewHolder> {

        Context context;
        TrainingDetailModel[] movielist;

        public TrainingDetailAdapter(Context ctx, TrainingDetailModel[] movielst) {
            this.movielist = movielst;
            this.context = ctx;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.training_detail_recycleview_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TrainingDetailModel trainingDetailModel = movielist[position];
            holder.section_text.setText(Html.fromHtml(trainingDetailModel.getSection(), Html.FROM_HTML_MODE_LEGACY));
            holder.detail_text.setText(Html.fromHtml(trainingDetailModel.getDetail(), Html.FROM_HTML_MODE_LEGACY));

            boolean expanded = trainingDetailModel.isExpand();

            holder.expand.setVisibility(expanded ? View.VISIBLE : View.GONE);
            holder.arrow_down.setVisibility(expanded ? View.VISIBLE : View.GONE);
            holder.view.setVisibility(expanded ? View.VISIBLE : View.GONE);

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
            return movielist.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView section_text;
            TextView detail_text;
            ImageView arrow_down;
            ConstraintLayout expand;
            ConstraintLayout textListner;
            View view;
//            CardView cardView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                section_text = itemView.findViewById(R.id.training_detail_text);
                detail_text = itemView.findViewById(R.id.training_detail_detail_text);
                arrow_down = itemView.findViewById(R.id.image_arrow_down);
                expand = itemView.findViewById(R.id.training_detail_expand);
                textListner = itemView.findViewById(R.id.text_constraint_layout);
                view = itemView.findViewById(R.id.training_detail_divider);
//                cardView = itemView.findViewById(R.id.training_main_card);

                textListner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TrainingDetailModel model = movielist[getAdapterPosition()];
                        model.setExpand(!model.isExpand());
                        notifyItemChanged(getAdapterPosition());

                    }
                });

            }
        }
    }


}