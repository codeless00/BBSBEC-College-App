package com.bbsbec;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.common.util.DataUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Boolean dynamic = true;

    NavigationView navigationView;
    BroadcastReceiver updateNotificationRegister;
    BroadcastReceiver sync_notification_register;
    BroadcastReceiver active_task_register;
    BroadcastReceiver banner_updater;

    BroadcastReceiver no_active_task_register;

    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    CardView banner_cardview;
    Toolbar toolbar;
    SliderView myslider;
    TextView notificationText;
    TextView marquee_text;
    CardView notificatoinContainer;
    CardView notificationImage;
    CardView developers;
    CardView faculty;
    CardView online_course;
    ConstraintLayout bell;
    ImageView red_dot_image;
    int pendingSmsCount = 1;
    int[] imagelist = {R.drawable.image_first,
            R.drawable.image_fourth,
            R.drawable.image_teacher,
            R.drawable.image_second,
            R.drawable.image_audi};

    CardView cardView;
    CardView cardView_two;
    CardView notes;
    CardView timeTable;
    CardView admissions;
    CardView paymentCard;
    CardView announcement;
    CardView training;
    CardView contributors;
    RelativeLayout update_layout;

    FirebaseAnalytics mFirebaseAnalytics;
    ProgressDialog progressDialog;

//    AdView adview_banner;

    @Override
    public void onClick(View v) {
        Intent i;
        if (v.getId() == R.id.cardview_first) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1111");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "SYLLABUS OPENED");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "IMAGE");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            i = new Intent(this, Department.class);
            i.putExtra("PAGE", "syllabus");
            startActivity(i);
        } else if (v.getId() == R.id.cardview_second) {
            i = new Intent(this, Department.class);
            i.putExtra("PAGE", "pyq");
            startActivity(i);
        } else if (v.getId() == R.id.banner_cardview) {
            i = new Intent(this,TrainingDetail.class);
            String[] contact_cr = InternalStorage.deserializeStringArray(this,"training_main_contact");
            String[] sett = InternalStorage.deserializeStringArray(this,"settings");
            Log.d("FIRE", String.valueOf(sett.length));
            int position;
            String banner_title;
            if (sett.length > 0){
                position = Integer.parseInt(sett[9]) - 1;
                banner_title = sett[10];
                if (position < contact_cr.length){
                    i.putExtra("POSITION",position);
                    i.putExtra("TITLE",banner_title);
                    startActivity(i);
                }else {
                    Toast.makeText(MainActivity.this, "Loading data, please wait...", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else if (v.getId() == R.id.cardview_notes) {
            i = new Intent(this, Department.class);
            i.putExtra("PAGE", "notes");
            startActivity(i);
        }
        else if (v.getId() == R.id.main_contributors) {
            i = new Intent(this, Contributors.class);
            i.putExtra("PAGE", "notes");
            startActivity(i);
        }
        else if (v.getId() == R.id.main_developers) {
            i = new Intent(this, Developers.class);
//            i.putExtra("PAGE", "notes");
            startActivity(i);
        }
        else if (v.getId() == R.id.cardview_faculty) {
            i = new Intent(getApplicationContext(), MyPDFViewer.class);
            String fac_link = InternalStorage.readFromFile("fac_link",getApplicationContext());
            i.putExtra("URL", fac_link);
            i.putExtra("SUBNAME", "fac_offline");
            i.putExtra("SUFFIX", "faculty");
            i.putExtra("LOADOFFLINE", true);
            i.putExtra("TITLE", "Our Faculties");
            startActivity(i);
        }
        else if (v.getId() == R.id.free_course) {
            i = new Intent(getApplicationContext(), OnlineCourse.class);
//            String fac_link = InternalStorage.readFromFile("fac_link",getApplicationContext());
//            i.putExtra("URL", fac_link);
//            i.putExtra("SUBNAME", "fac_offline");
//            i.putExtra("SUFFIX", "faculty");
//            i.putExtra("LOADOFFLINE", true);
//            i.putExtra("TITLE", "Our Faculties");
            startActivity(i);
        }
        else if (v.getId() == R.id.training_main) {
            i = new Intent(this, Training.class);
//            i.putExtra("PAGE", "notes");
            startActivity(i);
        }else if (v.getId() == R.id.cardview_announcement) {
            String url = "https://www.ptuexam.com/PublicAnnoucements";
            if (isConnected()){
                try {
                    Intent in = new Intent(Intent.ACTION_VIEW);
                    in.setData(Uri.parse(url));
                    startActivity(in);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Can't Found any Browser", Toast.LENGTH_SHORT).show();
                }

            }

        }
        else if (v.getId() == R.id.cardview_timetable) {
            i = new Intent(this, Department.class);
            i.putExtra("PAGE", "timetable");
            startActivity(i);
        }
//        else if (v.getId() == R.id.cardview_admission) {
//            String[] settings = InternalStorage.deserializeStringArray(this,"settings");
//            String contact = settings[0]; // use country code with your phone number
//            String message = settings[1];
//            openWhatsapp(contact,message);
////            String url = "https://api.whatsapp.com/send?phone=" + contact + "&text=" + message;
////            if (isConnected()){
////                try {
//////                PackageManager pm = this.getPackageManager();
//////                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
////                    Intent in = new Intent(Intent.ACTION_VIEW);
////                    in.setData(Uri.parse(url));
////                    startActivity(in);
////                } catch (Exception e) {
////                    Toast.makeText(MainActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
////                }
////            }
//
//
//        }
//        } else if (v.getId() == R.id.payment_card) {
//            i = new Intent(this, CheckOut.class);
////            i.putExtra("PAGE","pyq");
//            startActivity(i);
//        }

    }

    private void openWhatsapp(String num, String text){
        if (text == null){
            text = "";
        }
        String url = "https://api.whatsapp.com/send?phone=" + num + "&text=" + text;
        if (isConnected()){
            try {
//                PackageManager pm = this.getPackageManager();
//                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent in = new Intent(Intent.ACTION_VIEW);
                in.setData(Uri.parse(url));
                startActivity(in);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (!(activeNetwork != null && activeNetwork.isConnected())){
            Toast.makeText(MainActivity.this, "Check Your Internet Connection", Toast.LENGTH_LONG).show();
        }
        return activeNetwork != null && activeNetwork.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.meu, menu);
//        menu.getItem(2).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

//        MenuItem menuItem = menu.findItem(R.id.action_notification);
//
//        View actionView = MenuItemCompat.getActionView(menuItem);
        if (dynamic) {
            setUpBadge();
            NotificationOperation.initializeNotification(getApplicationContext());
            updateNotification();
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void updateNotification() {
//        sendNotification("DatabaseChanged!");
        int currentSms = NotificationOperation.getNotificationCount(getApplicationContext());
        if (currentSms == 0) {
            notificatoinContainer.setVisibility(View.GONE);
        } else {
            notificationText.setText(String.valueOf(Math.min(currentSms, 99)));
            if (notificatoinContainer.getVisibility() != View.VISIBLE) {
                notificatoinContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showBell() {
        bell = (ConstraintLayout) findViewById(R.id.bell_id);
        if (bell.getVisibility() != View.VISIBLE) {
            bell.setVisibility(View.VISIBLE);
        }
    }

    private void setUpBadge() {
        showBell();
        notificationText = (TextView) findViewById(R.id.notificationNumber);
        notificatoinContainer = (CardView) findViewById(R.id.notificationNumberContainer);
        notificationImage = (CardView) findViewById(R.id.notification_icon);

        notificationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MyNotification.class);
//                i.putExtra("PAGE","syllabus");
                startActivity(i);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

//        item.getItemId()
        if (item.getItemId() == R.id.delete) {
            String[] settings = InternalStorage.deserializeStringArray(this,"settings");
            String report = settings[4]; // use country code with your phone number
//            String message = settings[1];
            openWhatsapp(report,null);
        } else if (item.getItemId() == R.id.share) {
            shareApp();
        } else if (item.getItemId() == R.id.privacy) {
            Intent i = new Intent(this, PrivacyTerms.class);
            i.putExtra("PAGE", "privacy");
            startActivity(i);
        } else if (item.getItemId() == R.id.terms) {
            Intent i = new Intent(this, PrivacyTerms.class);
            i.putExtra("PAGE", "terms");
            startActivity(i);
        } else if (item.getItemId() == R.id.contact) {
            String[] settings = InternalStorage.deserializeStringArray(this,"settings");
            String contact = settings[3]; // use country code with your phone number
//            String message = settings[1];
            openWhatsapp(contact,null);
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_BBSBEC);  // Switch to AppTheme

        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.status_bar));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(MainActivity.this,R.color.navigation_bar)); // Use your desired color
        View decorview = window.getDecorView();
        WindowInsetsControllerCompat wic = new WindowInsetsControllerCompat(window, decorview);
        wic.setAppearanceLightStatusBars(false);


        toolbar = findViewById(R.id.MyToolBar);
//        update_layout = findViewById(R.id.update_layout);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false); // Disable the default title


        File file_version = this.getFileStreamPath("version_1.4");
        if(!file_version.exists()){
            InternalStorage.deleteAllAppFiles(getApplicationContext());

        }
        update_banner();


//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("message");
//
//        myRef.setValue("Hello, World!");
//        new Thread(
//                () -> {
//                    // Initialize the Google Mobile Ads SDK on a background thread.
//                    MobileAds.initialize(this, initializationStatus -> {});
//                })
//                .start();

//        adview_banner = (AdView) findViewById(R.id.adview_mainpage);
//        adview_banner.setAdUnitId("ca-app-pub-3940256099942544/9214589741");
//        adview_banner.setAdSize(getAdSize());
//        loadAds();


// Replace ad container with new ad view.
//        adContainerView.removeAllViews();
//        adContainerView.addView(adView);

        myslider = findViewById(R.id.slider_image);
        red_dot_image = (ImageView) findViewById(R.id.red_dot_image_main);

        if(InternalStorage.isFileExist(this,"FoundActiveTask")){
            show_red_dot();
        }

        Slider_Adapter sliderAdapter = new Slider_Adapter(imagelist);

        myslider.setSliderAdapter(sliderAdapter);
        myslider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        myslider.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        myslider.startAutoCycle();


        progressDialog = new ProgressDialog(this, com.airbnb.lottie.R.style.Base_Theme_AppCompat_Light_Dialog);
        File file = this.getFileStreamPath("updated_true");

        if (!file.exists()){
            progressDialog.setMessage("Syncing data, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                updateNotificationRegister = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        updateNotification();
                    }
                };

                banner_updater = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                update_banner();
                            }
                        });
                    }
                };

                sync_notification_register = new BroadcastReceiver(){

                    @Override
                    public void onReceive(Context context, Intent intent) {
                        show_sync();
                    }
                };

                active_task_register = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        show_red_dot();
                    }
                };

                no_active_task_register = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        remove_red_dot();
                    }
                };
                IntentFilter filter = new IntentFilter("UPDATE_NOTIFICATION");
                IntentFilter sync_filter = new IntentFilter("SHOW_SYNC");
                IntentFilter active_task_filter = new IntentFilter("FOUND_ACTIVE_TASK");
                IntentFilter no_task = new IntentFilter("NO_ACTIVE_TASK");
                IntentFilter banner_updated = new IntentFilter("UPDATED_BANNER");


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    registerReceiver(updateNotificationRegister, filter, Context.RECEIVER_NOT_EXPORTED);
                    registerReceiver(sync_notification_register, sync_filter, Context.RECEIVER_NOT_EXPORTED);
                    registerReceiver(active_task_register, active_task_filter, Context.RECEIVER_NOT_EXPORTED);
                    registerReceiver(no_active_task_register, no_task, Context.RECEIVER_NOT_EXPORTED);
                    registerReceiver(banner_updater, banner_updated, Context.RECEIVER_NOT_EXPORTED);


                }

                navigationView = findViewById(R.id.nav_view);
                drawerLayout = findViewById(R.id.main);
//
                toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.open, R.string.close);
                drawerLayout.addDrawerListener(toggle);



//
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // This code will be executed on the main thread
                        toggle.syncState();
                    }
                });
//                toggle.syncState();

                navigationView.setItemIconTintList(null);



                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        String to_open = null;
                        String bbsbec_website = "https://bbsbec.edu.in/";
                        String ptu_website = "https://ptu.ac.in/";
                        String drcc_website = "https://erp.bbsbec.edu.in/";
                        String ptu_exam = "https://www.ptuexam.com/";

                        if (menuItem.getItemId() == R.id.item_bbsbec){
                            to_open = bbsbec_website;
                        } else if (menuItem.getItemId() == R.id.item_ptu) {
                            to_open = ptu_website;
                        }
                        else if (menuItem.getItemId() == R.id.item_admission) {
                            to_open = null;
                            callAdmissionWhatsapp();
                        }
                        else if (menuItem.getItemId() == R.id.item_ptu_exam) {
                            to_open = ptu_exam;
                        }else if (menuItem.getItemId() == R.id.item_drcc) {
                            to_open = drcc_website;
                        }else if (menuItem.getItemId() == R.id.item_share) {
                            shareApp();
                        }else if (menuItem.getItemId() == R.id.item_rate) {
                            rateApp();
                        }


                        Intent in = new Intent(Intent.ACTION_VIEW);


                        if (isConnected()){
                            try {
                                if (to_open != null) {
                                    in.setData(Uri.parse(to_open));
                                    startActivity(in);
                                }
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, "Can't Found any Browser", Toast.LENGTH_SHORT).show();
                            }
                        }






                        drawerLayout.closeDrawer(GravityCompat.START);
                        return false;
                    }
                });

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                cardView = (CardView) findViewById(R.id.cardview_first);
                cardView_two = (CardView) findViewById(R.id.cardview_second);
                notes = (CardView) findViewById(R.id.cardview_notes);
                contributors = (CardView) findViewById(R.id.main_contributors);
                training = (CardView) findViewById(R.id.training_main);
                developers = (CardView) findViewById(R.id.main_developers);
                faculty = (CardView) findViewById(R.id.cardview_faculty);
                online_course = (CardView) findViewById(R.id.free_course);
                announcement = (CardView) findViewById(R.id.cardview_announcement);
//                admissions = (CardView) findViewById(R.id.cardview_admission);
                timeTable = (CardView) findViewById(R.id.cardview_timetable);
//                paymentCard = (CardView) findViewById(R.id.payment_card);

                cardView.setOnClickListener((View.OnClickListener) MainActivity.this);
                cardView_two.setOnClickListener((View.OnClickListener) MainActivity.this);
                notes.setOnClickListener((View.OnClickListener) MainActivity.this);
                contributors.setOnClickListener((View.OnClickListener) MainActivity.this);
                online_course.setOnClickListener((View.OnClickListener) MainActivity.this);
                training.setOnClickListener((View.OnClickListener) MainActivity.this);
                developers.setOnClickListener((View.OnClickListener) MainActivity.this);
//                admissions.setOnClickListener((View.OnClickListener) MainActivity.this);
                timeTable.setOnClickListener((View.OnClickListener) MainActivity.this);
                announcement.setOnClickListener((View.OnClickListener) MainActivity.this);
                faculty.setOnClickListener((View.OnClickListener) MainActivity.this);


//                paymentCard.setOnClickListener((View.OnClickListener) MainActivity.this);



                MyDataUtils.initializeDatabase(getApplicationContext(),drawerLayout);
            }
        }).start();

        Intent serviceIntent = new Intent(MainActivity.this, DatabaseChangeListenerService.class);
        startService(serviceIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (savedInstanceState == null) {
                // Delay permission request until after view creation
                getWindow().getDecorView().post(() -> requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 99));
            }

        }



//        if (getSupportActionBar() != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//
//        toolbar.setTitle("My toolbar");
//        toolbar.setSubtitle("Subtitle");
//
//
//





//        cardView_two.se

//        if (dynamic) {
//            NotificationTest notificationTest = new NotificationTest(getApplicationContext());
//            notificationTest.start();
//        }


        View toggleButton = toolbar.getChildAt(2);
        String viewClassName = toggleButton.getClass().getSimpleName();
//        loadAds();
        Log.d("GNOME", "View class name: " + viewClassName);
//        sendNotification(getApplicationContext(),"Did You Hear That?","Syllabus has been Updated.");

    }

    private void update_banner() {
        Log.e("FIRE","updatting banner");

        String[] sett = InternalStorage.deserializeStringArray(this,"settings");
        Log.d("FIRE", String.valueOf(sett.length));
        if (sett.length > 0){
            String show_banner = sett[7];
            String banner_text = sett[8];

            marquee_text = (TextView) findViewById(R.id.marqueetext);
            banner_cardview = (CardView) findViewById(R.id.banner_cardview);

            if (Objects.equals(show_banner, "true")){

                marquee_text.setText(Html.fromHtml(banner_text));
//                marquee_text.setText(Html.fromHtml("<font color=black>Get </font> <font color=red>FREE Certification </font><font color=black> and training in your dream company </font> by <font color=blue> Future Finders </font> \uD83D\uDD25\uD83D\uDD25"));
                marquee_text.setSelected(true);
                banner_cardview.setVisibility(View.VISIBLE);

                banner_cardview.setOnClickListener((View.OnClickListener) MainActivity.this);

            }else {
                banner_cardview.setVisibility(View.GONE);
            }

        }

    }

    private void callAdmissionWhatsapp() {

        String[] settings = InternalStorage.deserializeStringArray(this,"settings");
        String contact = settings[0]; // use country code with your phone number
        String message = settings[1];
        openWhatsapp(contact,message);
//            String url = "https://api.whatsapp.com/send?phone=" + contact + "&text=" + message;
//            if (isConnected()){
//                try {
////                PackageManager pm = this.getPackageManager();
////                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
//                    Intent in = new Intent(Intent.ACTION_VIEW);
//                    in.setData(Uri.parse(url));
//                    startActivity(in);
//                } catch (Exception e) {
//                    Toast.makeText(MainActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
//                }
//            }


//        }
//        } else if (v.getId() == R.id.payment_card) {
//            i = new Intent(this, CheckOut.class);
////            i.putExtra("PAGE","pyq");
//            startActivity(i);
//        }
    }

    private void rateApp() {
        try {
            // Attempt to open the Play Store app
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent rateIntent = new Intent(Intent.ACTION_VIEW, uri);
            rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            // If Play Store app is not installed, open the Play Store in the browser
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
            Intent rateIntent = new Intent(Intent.ACTION_VIEW, uri);
            rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(rateIntent);
        }
    }

    private void shareApp() {
        // The message or link to share
        String shareMessage = "Check out this BBSBEC College app:\n https://play.google.com/store/apps/details?id=" + getPackageName();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

        startActivity(Intent.createChooser(shareIntent, "Share this app using"));
    }

    private void remove_red_dot() {
        red_dot_image.setVisibility(View.GONE);
    }

    private void show_red_dot() {
        red_dot_image.setVisibility(View.VISIBLE);
    }

//    private void loadAds() {
//        try {
//            String[] ad_setting = InternalStorage.deserializeStringArray(this,"ads_main_settings");
//            String show_banner_ads = ad_setting[0];
////            String banner_id = ad_setting[1];
//            if (Boolean.parseBoolean(show_banner_ads)){
//                Log.d("FIRE","LAUNCHING MAIN ADS");
////                adview_banner.setAdUnitId(banner_id);
//                adview_banner.setVisibility(View.VISIBLE);
//                AdRequest adRequest = new AdRequest.Builder().build();
//                adview_banner.loadAd(adRequest);
//            }
//        }catch (Exception ignored){
//
//        }
//
//
//    }

    private void show_sync() {
        if (progressDialog != null && progressDialog.isShowing() && !isFinishing() && !isDestroyed()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }






    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateNotificationRegister);
        unregisterReceiver(sync_notification_register);
        unregisterReceiver(no_active_task_register);
        unregisterReceiver(active_task_register);
        unregisterReceiver(banner_updater);
    }
}