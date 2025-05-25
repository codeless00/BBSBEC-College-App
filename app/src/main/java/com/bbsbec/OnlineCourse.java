package com.bbsbec;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import java.util.LinkedHashMap;
import java.util.Objects;

public class OnlineCourse extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    CourseAdapter courseAdapter;
    BroadcastReceiver imageDownloadComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_online_course);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.online_course_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Window window = getWindow();
        View decorview = window.getDecorView();
        WindowInsetsControllerCompat wic = new WindowInsetsControllerCompat(window, decorview);
        wic.setAppearanceLightStatusBars(false);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar));

        progressDialog = new ProgressDialog(this, com.airbnb.lottie.R.style.Base_Theme_AppCompat_Light_Dialog);

        int filesize = InternalStorage.getFileCountInDirectory(getApplicationContext(), "CourseImageDir");
        String[] rr = InternalStorage.deserializeStringArray(getApplicationContext(), "course_all_ids");

        imageDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                notifychange();
            }
        };
        IntentFilter filter = new IntentFilter("COURSE_DOWNLOAD_COMPLETE");


        if (filesize != rr.length) {
            progressDialog.setMessage("Loading Courses, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(imageDownloadComplete, filter, Context.RECEIVER_NOT_EXPORTED);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#162242")));
        getSupportActionBar().setTitle("Online Courses");


        recyclerView = (RecyclerView) findViewById(R.id.online_course_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        CourseDetailModel model = new CourseDetailModel("desc One","49","12","youtube.com","https",true,"4.0","paid","Airtifcial Intelligence","","");
//        CourseDetailModel model_two = new CourseDetailModel("desc two","49","12","youtube.com","https",true,"4.0","paid","Airtifcial Intelligence","","");
//        CourseDetailModel model_three = new CourseDetailModel("desc three","49","12","youtube.com","https",true,"4.0","paid","Airtifcial Intelligence","","");
//        CourseDetailModel model_four = new CourseDetailModel("desc three","49","12","youtube.com","https",true,"4.0","paid","Airtifcial Intelligence","","");
//        CourseDetailModel model_five = new CourseDetailModel("desc three","49","12","youtube.com","https",true,"4.0","free","Airtifcial Intelligence","","");
//        CourseDetailModel model_six = new CourseDetailModel("desc three","49","12","youtube.com","https",true,"4.0","paid","Airtifcial Intelligence","","");
//        CourseDetailModel model_seven = new CourseDetailModel("desc three","49","12","youtube.com","https",false,"4.0","free","Airtifcial Intelligence","","");
//        CourseDetailModel model_eight = new CourseDetailModel("desc three","49","12","youtube.com","https",true,"4.0","paid","Airtifcial Intelligence","","");
//        CourseDetailModel model_nine = new CourseDetailModel("desc three","49","12","youtube.com","https",true,"3.0","paid","Airtifcial Intelligence","false","");
//        CourseDetailModel model_ten = new CourseDetailModel("desc three","49","12","youtube.com","https",true,"4.0","paid","Airtifcial Intelligence","false","");
//        CourseDetailModel model_eleven = new CourseDetailModel("desc three","49","12","youtube.com","https",true,"4.0","paid","Airtifcial Intelligence","false","");

//        String[] sorting = {"a","b","c","d","e","f","g","h","i","j","k"};
//        int[] heading = {1};

        LinkedHashMap<String, CourseDetailModel> demo_test = InternalStorage.deserializeLinkedCourseDetailsHashMap(this, "course_details");
        String[] sorting = InternalStorage.deserializeStringArray(this, "course_sorting_details");
        String[] heading = InternalStorage.deserializeStringArray(this, "course_position_sorting_details");

//        demo_test.put("a",model);
//        demo_test.put("b",model_two);
//        demo_test.put("c",model_three);
//        demo_test.put("d",model_four);
//        demo_test.put("e",model_five);
//        demo_test.put("f",model_six);
//        demo_test.put("g",model_seven);
//        demo_test.put("h",model_eight);
//        demo_test.put("i",model_nine);
//        demo_test.put("j",model_ten);
//        demo_test.put("k",model_eleven);

        courseAdapter = new CourseAdapter(this, sorting, heading, demo_test);
        recyclerView.setAdapter(courseAdapter);


    }

    private void notifychange() {
//        Toast.makeText(this,"Downlaod complete",Toast.LENGTH_LONG).show();
        recyclerView.setAdapter(courseAdapter);
        if (progressDialog != null && progressDialog.isShowing() && !isFinishing() && !isDestroyed()) {
            progressDialog.dismiss();
        }
    }


    public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

        Context context;

        LinkedHashMap<String, CourseDetailModel> courseDetail;
        String[] sorting;
        String[] headings;

//        String[] likes;
//        String[] blueticks;

        public CourseAdapter(Context ctx, String[] sorting, String[] headings, LinkedHashMap<String, CourseDetailModel> courseDetail) {
            this.courseDetail = courseDetail;
            this.context = ctx;
            this.sorting = sorting;
            this.headings = headings;
//            this.keysList = new ArrayList<>(like.keySet());
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.online_course_recycleview_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//            Log.d("FIRE",keysList.toString());
//            Log.d("FIRE",String.valueOf(position));
//            String key = keysList.get(position);
//            String value = likes.get(key);
            Log.d("FIRE", "Sorting = " + Arrays.toString(sorting));
            Log.d("FIRE", "Course Netail = " + courseDetail);
            Log.d("FIRE", "Headings = " + Arrays.toString(headings));

            CourseDetailModel courseDetailModel = courseDetail.get(sorting[position]);

            assert courseDetailModel != null;

            String title = courseDetailModel.getCourse_description();
            String actual_fee = courseDetailModel.getCourse_actual_price();
            String discount_fee = courseDetailModel.getCourse_discounted_price();
            boolean certificate_included = courseDetailModel.isIs_course_certificate();
            String course_type = courseDetailModel.getCourse_type();
            String course_source = courseDetailModel.getCourse_source();
            String course_image = courseDetailModel.getUrl_id();
            String heading = courseDetailModel.getHeading();
            String course_rating = courseDetailModel.getCourse_rating();
            String course_url = courseDetailModel.getCourse_url();

            Log.d("FIRE", "Image to redner: " + course_image + "_image.png.png");
            Bitmap co_image = InternalStorage.loadImageFromInternalStorage(course_image + "_image.png", "CourseImageDir", context);

            holder.course_image.setImageResource(R.drawable.default_image);
            if (co_image != null) {
                holder.course_image.setImageBitmap(co_image);
            }


            holder.course_description.setText(title);

            holder.course_actual_price.setVisibility(View.GONE);
            holder.course_discounted_price.setVisibility(View.GONE);
            holder.course_free.setVisibility(View.VISIBLE);
            holder.course_paid.setVisibility(View.GONE);

            if (Objects.equals(course_type, "paid")) {
                holder.course_actual_price.setVisibility(View.VISIBLE);
                holder.course_discounted_price.setVisibility(View.VISIBLE);
                holder.course_actual_price.setText("₹" + actual_fee);
                holder.course_discounted_price.setText(discount_fee);
                holder.course_actual_price.setPaintFlags(holder.course_actual_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                holder.course_paid.setVisibility(View.VISIBLE);
                holder.course_free.setVisibility(View.GONE);
            }
            holder.course_source.setText(course_source);
            int index = Arrays.binarySearch(headings, String.valueOf(position));

            holder.course_heading.setVisibility(View.GONE);

            if (index >= 0) {
                holder.course_heading.setVisibility(View.VISIBLE);
                holder.course_heading.setText(heading);
                if (position == 0){
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.course_heading.getLayoutParams();
                    int topMarginInDp = 10; // Replace this with your desired margin
                    int topMarginInPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, topMarginInDp, getResources().getDisplayMetrics());
                    layoutParams.topMargin = topMarginInPx;
                    holder.course_heading.setLayoutParams(layoutParams);
                }
            }

            holder.course_certificate.setText("No");
            holder.course_certificate.setTextColor(Color.parseColor("#FF0000")); // Orange-red color

            if (certificate_included) {
                holder.course_certificate.setText("Yes");
                holder.course_certificate.setTextColor(Color.parseColor("#006C05")); // Orange-red color
            }
            holder.course_rating.setRating(Float.parseFloat(course_rating));


            holder.course_cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRedirectDialog(course_url, course_source);
                }
            });


        }

        @Override
        public int getItemCount() {
            return sorting.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView course_description;
            TextView course_actual_price;
            TextView course_discounted_price;
            TextView course_source;
            ImageView course_image;
            TextView course_certificate;
            ImageView course_free;
            TextView course_heading;
            ImageView course_paid;
            RatingBar course_rating;
            CardView course_cardview;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                course_description = itemView.findViewById(R.id.course_description);
                course_actual_price = itemView.findViewById(R.id.course_actual_price);
                course_discounted_price = itemView.findViewById(R.id.course_discounted_price);
                course_image = itemView.findViewById(R.id.course_image);
                course_source = itemView.findViewById(R.id.course_source);
                course_certificate = itemView.findViewById(R.id.course_certificate);
                course_free = itemView.findViewById(R.id.course_free);
                course_paid = itemView.findViewById(R.id.course_paid);
                course_rating = itemView.findViewById(R.id.course_rating);
                course_cardview = itemView.findViewById(R.id.course_cardview);
                course_heading = itemView.findViewById(R.id.course_heading);




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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showRedirectDialog(String url, String source) {
        // Create an AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set the message and title
        builder.setTitle("Redirect Notice")
                .setMessage("You are being redirected to (" + source + ") Do you want to proceed?")
                .setCancelable(true)

                // Add "Proceed" button
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Redirect to website on proceed
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    }
                });

                // Add "Cancel" button
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        // Dismiss the dialog
//                        dialog.dismiss();
//                    }


        // Show the dialog
        AlertDialog alert = builder.create();

        // Show the dialog before customizing buttons
        alert.show();

        // After showing the dialog, customize the buttons
        Button proceedButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
        proceedButton.setTextColor(getResources().getColor(R.color.red)); // Set the color to blue

//        Button cancelButton = alert.getButton(AlertDialog.BUTTON_NEGATIVE);
//        cancelButton.setTextColor(getResources().getColor(R.color.red)); // Set Cancel button to black
    }
}