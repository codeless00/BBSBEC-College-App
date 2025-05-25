package com.bbsbec;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MyNotification extends AppCompatActivity {

    TextView notificationText;
    Toolbar notifciationToolbar;

    ArrayList<String> message = new ArrayList<>();
    ArrayList<String> date = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.notification_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Window window = getWindow();
        View decorview = window.getDecorView();
        WindowInsetsControllerCompat wic = new WindowInsetsControllerCompat(window, decorview);
        wic.setAppearanceLightStatusBars(false);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.black));

        notifciationToolbar = findViewById(R.id.notification_toolbar);
        notifciationToolbar.setTitle("Notification");

        setSupportActionBar(notifciationToolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        notifciationToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        notificationText = (TextView) findViewById(R.id.notification_text);
//        String htmlText = "<b><font color='black'> Unit 1: Electronics</font> </b><br><br>This is syllabus<br>This is line two";
//        notificationText.setText(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.notification_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyNotificationAdapter myNotificationAdapter;
//        String[] message ={"ECE syllabus has been updated to the latest ptu website.","CSE syllabus has been removed because it's wrong syllabus new will be uploaded soon.","ECE syllabus has been updated to the latest ptu website.","CSE syllabus has been removed because it's wrong syllabus new will be uploaded soon.","ECE syllabus has been updated to the latest ptu website.","CSE syllabus has been removed because it's wrong syllabus new will be uploaded soon.","ECE syllabus has been updated to the latest ptu website.","CSE syllabus has been removed because it's wrong syllabus new will be uploaded soon.","ECE syllabus has been updated to the latest ptu website.","CSE syllabus has been removed because it's wrong syllabus new will be uploaded soon.","ECE syllabus has been updated to the latest ptu website.","CSE syllabus has been removed because it's wrong syllabus new will be uploaded soon.","ECE syllabus has been updated to the latest ptu website.","CSE syllabus has been removed because it's wrong syllabus new will be uploaded soon."};
//        String[] date={"20-05-2023","28-07-2024","20-05-2023","28-07-2024","20-05-2023","28-07-2024","20-05-2023","28-07-2024","20-05-2023","28-07-2024","20-05-2023","28-07-2024","20-05-2023","28-07-2024"};

        initiateMessageDate();
        myNotificationAdapter = new MyNotificationAdapter(getApplicationContext(),reverse(message.toArray(new String[0])),reverse(date.toArray(new String[0])),getLayoutInflater());
        recyclerView.setAdapter(myNotificationAdapter);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, R.drawable.divider);
//        recyclerView.addItemDecoration(dividerItemDecoration);


        NotificationOperation.clearNotification(getApplicationContext());
        Intent intent = new Intent("UPDATE_NOTIFICATION");
        intent.setPackage(this.getPackageName());
        getApplicationContext().sendBroadcast(intent);
    }



    private void initiateMessageDate() {
        String jsonString = InternalStorage.readFromFile("notification_list.json",getApplicationContext());

        try {
            // Parse JSON string to JSONObject
            JSONObject jsonObject = new JSONObject(jsonString);

            // Initialize arrays to hold keys and values
            message = new ArrayList<>();
            date = new ArrayList<>();

            // Iterate over keys and populate arrays
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = jsonObject.getString(key);

                message.add(key);
                date.add(value);
            }




        } catch (JSONException ignored) {
        }
    }

    public static String[] reverse(String[] array) {
        int left = 0;
        int right = array.length - 1;

        while (left < right) {
            // Swap the elements at left and right indices
            String temp = array[left];
            array[left] = array[right];
            array[right] = temp;

            // Move the pointers towards the center
            left++;
            right--;
        }
        return array;
    }
}