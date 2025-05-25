package com.bbsbec;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.C;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class DatabaseChangeListenerService extends Service {

    private DatabaseReference databaseReference;
    ImageDownloader imageDownloader;
    ImageDownloader ContributorImageDownloader;
    ImageDownloader CourseOnlineImageDownloader;




    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            JSONObject jsonData = null;
            try {
                jsonData = convertSnapshotToJSON(snapshot);
                // Do something with the JSON, e.g., log it or send it to a server
                Log.d("FIRE", jsonData.toString());
            } catch (JSONException e) {
                Log.d("FIRE","Cannot convert database to json " + e.getMessage());
            }
            try {
                assert jsonData != null;
                updateData(jsonData);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            File file_version = getApplication().getFileStreamPath("version_1.4");
            if(file_version.exists()){
                String[] sett = InternalStorage.deserializeStringArray(getApplicationContext(),"settings");
                String section_random = sett[11];
                String course_random = sett[12];
                List<List<String>> sorting = InternalStorage.deserialize2DListFromFile(getApplicationContext(),"course_sorting");
                MyDataUtils.random_course_shuffle(Objects.equals(section_random, "true"),Objects.equals(course_random, "true"),sorting,getApplicationContext());
            }



            Log.e("FIRE","Data changed");
//            sendNotification(getApplicationContext(),"Did You Hear That?","Syllabus has been Updated.");

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error){
            Log.e("FIRE","Data Fialed");

        }
    };

    private void updateData(JSONObject data) throws JSONException {
        String notification = null;

        if (data.has("NOTIFICATION") && isDataModified(data)){
            JSONObject settings = data.getJSONObject("SETTINGS");
            String[] setting = new String[13];
            setting[0] = settings.getString("admission_whatsapp");
            setting[1] = settings.getString("admission_query");
            setting[2] = settings.getString("contributor_link");
            setting[3] = settings.getString("contact_us");
            setting[4] = settings.getString("report_error");
            setting[5] = settings.getString("dev_first_des");
            setting[6] = settings.getString("dev_second_des");
            setting[7] = settings.getString("show_banner");
            setting[8] = settings.getString("banner");
            setting[9] = settings.getString("position");
            setting[10] = settings.getString("banner_title");
            setting[11] = settings.getString("course_section_random");
            setting[12] = settings.getString("course_details_random");

//            setting[2] = settings.getString("banner_main");
//            setting[3] = settings.getString("banner_department");
//            setting[4] = settings.getString("banner_year");
//            setting[5] = settings.getString("banner_semester");
//            setting[6] = settings.getString("banner_syllabus");
//            setting[7] = settings.getString("banner_subject");
//            setting[8] = settings.getString("banner_notification");


            InternalStorage.serializeStringArray(getApplicationContext(),setting,"settings");


            JSONObject notification_data = data.getJSONObject("NOTIFICATION");
            boolean enable = notification_data.getBoolean("show");
            boolean deleteNoti = notification_data.getBoolean("deleteAll");
            if (enable){
                if (deleteNoti){
                    InternalStorage.writeToFile("notification_list.json","{}",getApplicationContext());
                    NotificationOperation.clearNotification(getApplicationContext());
                    Intent intent = new Intent("UPDATE_NOTIFICATION");
                    intent.setPackage(getApplicationContext().getPackageName());
                    getApplicationContext().sendBroadcast(intent);
                }
                else {
                    notification = notification_data.getString("message");}
            }
        }
//        String current_data = data.toString();

        String past_data = InternalStorage.readFromFile("database.json",getApplicationContext());
        JSONObject current = new JSONObject(data.toString());
        JSONObject past = new JSONObject(past_data);





        InternalStorage.writeToFile("database.json",data.toString(),getApplicationContext());

        if (past_data.equals("{}")){
            Log.d("FIRE","ENTERING HUGE UPDATATION");
            updateTraining(false);
            updatePyq("PYQ",false,past,null);
            updatePyq("NOTES",false,past,null);
            updatePyq("TIMETABLE",false,past,null);
            updateSyllabus();
//            updateAds();
            updateTasks();
            updateContributorsLikes();
            updateContributorsProfile(false);
            updateCourseDetails();
            updateFac(false);


        }else{


            if (!current.getJSONObject("TRAINING").toString().equals(past.getJSONObject("TRAINING").toString())){
                updateTraining(true);
            }
            if (!current.getJSONObject("PYQ").toString().equals(past.getJSONObject("PYQ").toString())){
                updatePyq("PYQ",false,past,null);
            }
            if (!current.getJSONObject("NOTES").toString().equals(past.getJSONObject("NOTES").toString())){
                updatePyq("NOTES",true,past,"notes_pdf_offline");
            }
            if (!current.getJSONObject("TIMETABLE").toString().equals(past.getJSONObject("TIMETABLE").toString())){
                updatePyq("TIMETABLE",true,past,"timetable_pdf_offline");
            }
            if (!current.getJSONObject("SYLLABUS").toString().equals(past.getJSONObject("SYLLABUS").toString())){
                updateSyllabus();
            }
//            if (!current.getJSONObject("ADS").toString().equals(past.getJSONObject("ADS").toString())){
//                updateAds();
//            }
            if (!current.getJSONObject("TASKS").toString().equals(past.getJSONObject("TASKS").toString())){
                updateTasks();
            }
            if (!current.getJSONObject("LIKES").toString().equals(past.getJSONObject("LIKES").toString())){
                updateContributorsLikes();
            }
            if (!current.getJSONObject("CONTRIBUTORS").toString().equals(past.getJSONObject("CONTRIBUTORS").toString())){
                updateContributorsProfile(false);
            }
            if (!current.getString("FACULTIES").equals(past.getString("FACULTIES"))){
                updateFac(true);
            }
            if (!current.getString("COURSES").equals(past.getString("COURSES"))){
                updateCourseDetails();
            }




        }


        if (imageDownloader != null){
            imageDownloader.shutdown();
            try {
                if (imageDownloader.awaitTermination(60, TimeUnit.SECONDS)) {
                    Log.d("MainActivity", "All tasks completed successfully.");
                } else {
                    Log.e("MainActivity", "Timeout: Not all tasks completed.");
                }
            } catch (Exception e) {
                Log.e("MainActivity", "Error waiting for termination", e);
            }
        }
//        if (ContributorImageDownloader != null){
//            ContributorImageDownloader.shutdown();
//            try {
//                if (ContributorImageDownloader.awaitTermination(60, TimeUnit.SECONDS)) {
//                    Log.d("MainActivity", "Contributor image thread All tasks completed successfully.");
//                } else {
//                    Log.e("MainActivity", "Contributor image thread Timeout: Not all tasks completed.");
//                }
//            } catch (Exception e) {
//                Log.e("MainActivity", "Contributor image thread Error waiting for termination", e);
//            }
//        }





        if (notification != null){
            String current_notification = InternalStorage.readFromFile("notification_list.json",getApplicationContext());
            JSONObject cs = new JSONObject(current_notification);
            JSONObject js = new JSONObject(current_notification);
            String current_date = currentDate();
            js.put(notification,current_date);
            InternalStorage.writeToFile("notification_list.json",js.toString(),getApplicationContext());
            InternalStorage.writeToFile("updated_true","",getApplicationContext());
            InternalStorage.writeToFile("version_1.4","",getApplicationContext());

            int pastCount = Integer.parseInt(InternalStorage.readFromFile("NotificationCount.txt", getApplicationContext()));
            if ((js.length()-cs.length()) == 1){
                NotificationOperation.increaseNotification(1,getApplicationContext());
                Intent intent = new Intent("UPDATE_NOTIFICATION");
                intent.setPackage(this.getPackageName());
                getApplicationContext().sendBroadcast(intent);
                Intent sycn_intent = new Intent("SHOW_SYNC");
                sycn_intent.setPackage(this.getPackageName());
                getApplicationContext().sendBroadcast(sycn_intent);
                Log.e("FIRE","NOTIFICATION BROADCAST SEND");
                sendNotification(getApplicationContext(),"BBSBEC Notification",notification);
            }
        }

        Intent banner_updated = new Intent("UPDATED_BANNER");
        banner_updated.setPackage(this.getPackageName());
        getApplicationContext().sendBroadcast(banner_updated);

        Log.e("FIRE","ALL THE DATA IS UPDATED");

        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.e("FIRE","CREATING THE THREAD FOR PROFILE DOWNLAOD");


//                boolean flag = true;


                while (true) {
                    Log.d("FIRE","THREAD FOR PROFILE DOWNLAODcom IS RUNNING");
//                    if(ContributorImageDownloader == null) {
//                        Log.e("STATUS", "executor status: contributorrimagedownloader is null terminating the thread.");
//                        break;
//                    }
                    int filesize = InternalStorage.getFileCountInDirectory(getApplicationContext(),"ContributorImageDir");

                    Log.e("STATUS", "Size of directory: " + String.valueOf(filesize));

                    LinkedHashMap<String,ContributorDetailModel> rr = InternalStorage.deserializeLinkedHashMap(getApplicationContext(),"contributor_profile_map");
                    Log.e("STATUS", "profie size: " + String.valueOf(rr.size()));

                    if(filesize == rr.size()){
                        if (ContributorImageDownloader == null || ContributorImageDownloader.getExecutorService().isTerminated()) {
                            Log.d("FIRE", "EXISTING THE THREAD FOR PROFILE DOWNLAOD");
                            Intent intent = new Intent("DOWNLOAD_COMPLETE");
                            intent.setPackage(getApplicationContext().getPackageName());
                            getApplicationContext().sendBroadcast(intent);
                        }
//                        flag = false;
                        break;

                    }
                    if(ContributorImageDownloader == null || ContributorImageDownloader.getExecutorService().isTerminated()){
                        if (isConnected()){

                            try {
                                updateContributorsProfile(true);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    try {
                        Thread.sleep(2000); // Optional: sleep for a short period to prevent high CPU usage
                    } catch (InterruptedException ignored) {

                    }


                }
            }
        }).start();







        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.e("FIRE","CREATING THE THREAD FOR COURSE PROFILE DOWNLAOD");


//                boolean flag = true;


                while (true) {
                    Log.d("FIRE","THREAD FOR COURSE PROFILE DOWNLAODcom IS RUNNING");
//                    if(ContributorImageDownloader == null) {
//                        Log.e("STATUS", "executor status: contributorrimagedownloader is null terminating the thread.");
//                        break;
//                    }
                    int filesize = InternalStorage.getFileCountInDirectory(getApplicationContext(),"CourseImageDir");

                    Log.e("STATUS", "Size of course directory: " + String.valueOf(filesize));

                    String[] rr = InternalStorage.deserializeStringArray(getApplicationContext(),"course_all_ids");
                    Log.e("STATUS", "course profie size: " + String.valueOf(rr.length));

                    if(filesize == rr.length){
                        if (CourseOnlineImageDownloader == null || CourseOnlineImageDownloader.getExecutorService().isTerminated()) {
                            Log.d("FIRE", "EXISTING THE THREAD FOR PROFILE DOWNLAOD");
                            Intent intent = new Intent("COURSE_DOWNLOAD_COMPLETE");
                            intent.setPackage(getApplicationContext().getPackageName());
                            getApplicationContext().sendBroadcast(intent);
                        }
//                        flag = false;
                        break;

                    }
//                    Log.d("FIRE","COurse Executor: " + CourseOnlineImageDownloader.checkTerminated());
                    if(CourseOnlineImageDownloader == null || CourseOnlineImageDownloader.getExecutorService().isTerminated()){
                        if (isConnected()){

                            try {
                                updateCourseDetails();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    try {
                        Thread.sleep(2000); // Optional: sleep for a short period to prevent high CPU usage
                    } catch (InterruptedException ignored) {

                    }


                }
            }
        }).start();

    }



    private void updateFac(boolean checkpast) throws JSONException {
        String storefile = InternalStorage.readFromFile("database.json",getApplicationContext());
        JSONObject data = new JSONObject(storefile);
        if (data.has("FACULTIES")){
            String fac_link = data.getString("FACULTIES");
            if (checkpast){
                InternalStorage.checkAndDeleteFile(getApplicationContext(),"faculty_fac_offline","faculty");
            }
            InternalStorage.writeToFile("fac_link",fac_link,getApplicationContext());
        }
    }

    private void updateContributorsLikes() throws JSONException {

        String storefile = InternalStorage.readFromFile("database.json",getApplicationContext());
        JSONObject data = new JSONObject(storefile);
        if (data.has("LIKES")){
            JSONObject contributors = data.getJSONObject("LIKES");
//            JSONObject jsonObject = new JSONObject(jsonString);
            HashMap<String, String> hashMap = new HashMap<>();
            Iterator<String> keys_first = contributors.keys();

            while (keys_first.hasNext()) {
                String key = keys_first.next();
                hashMap.put(key, contributors.getString(key));
            }

            List<Map.Entry<String, String>> list = new ArrayList<>(hashMap.entrySet());

            // Sort the list in descending order based on values
            list.sort((entry1, entry2) -> Integer.parseInt(entry2.getValue()) - Integer.parseInt(entry1.getValue()));

            // Convert sorted list back to LinkedHashMap to maintain order
            LinkedHashMap<String, String> sortedMap = new LinkedHashMap<>();
            for (Map.Entry<String, String> entry : list) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }

            Set<String> keySet = sortedMap.keySet();
            String[] keys = keySet.toArray(new String[keySet.size()]);
            String[] values = sortedMap.values().toArray(new String[0]);

            String[] final_val = new String[values.length];

            int count = 0;
            for (String val: values){
                String num = InternalStorage.formatNumber(val);
                final_val[count] = num;
                count = count +1;

            }



            InternalStorage.serializeStringArray(getApplicationContext(),keys,"contributor_ordering");
            InternalStorage.serializeStringArray(getApplicationContext(),final_val,"contributor_likes");


        }
    }

    private void updateContributorsProfile(boolean fromthread) throws JSONException {

        Log.e("FIRE","ENTERING TO UPDATE PROFILE");

        String storefile = InternalStorage.readFromFile("database.json",getApplicationContext());
        JSONObject data = new JSONObject(storefile);
        if (data.has("CONTRIBUTORS")){
            LinkedHashMap<String, ContributorDetailModel> result = new LinkedHashMap<>();

            LinkedHashMap<String, String> past_images_list = InternalStorage.deserializeLinkedHashMapString(getApplicationContext(),"contributor_image_map");
            LinkedHashMap<String, String> current_image_list = new LinkedHashMap<>();

            List<String> file_to_delete = new ArrayList<>();
            List<String> file_to_download = new ArrayList<>();
            List<String> filename_to_download = new ArrayList<>();

            JSONObject contributors = data.getJSONObject("CONTRIBUTORS");
            Iterator<String> keys = contributors.keys();
            while (keys.hasNext()){
                String key = keys.next();
                JSONObject profile = contributors.getJSONObject(key);
                String name = profile.getString("name");
                String image = profile.getString("image_url");
                boolean bluetick = Boolean.parseBoolean(profile.getString("bluetick"));
                ContributorDetailModel model = new ContributorDetailModel(name,bluetick);
                result.put(key,model);
                current_image_list.put(key,image);
            }

            if (past_images_list != null) {
                for (String past_key : past_images_list.keySet()) {
                    if (!current_image_list.containsKey(past_key)) {
                        file_to_delete.add(past_key + "_image.png.png");
                    }
                }
            }

            for(String fn: file_to_delete) {
                Log.d("FIRE","Deleting Image: " + fn);
                InternalStorage.deleteImage(getApplicationContext(), "ContributorImageDir",fn );
            }

//            assert past_images_list != null;
//            if (past_images_list == null){
//                Log.d("FIRE","Past_data: " + "Null");
//
//            }else {
//                Log.d("FIRE","Past_data: " + past_images_list.toString());
//
//            }
//            Log.d("FIRE","current_data: " + current_image_list.toString());


            for(String current_key: current_image_list.keySet()){
//                Log.e("FIRE","current_key: " + current_key);
                boolean isfileexist = InternalStorage.doesFileExist(getApplicationContext(),"ContributorImageDir",current_key+"_image.png.png");
//                Log.d("FIRE","fileexist: " + String.valueOf(isfileexist));
                if (past_images_list != null && past_images_list.containsKey(current_key) && isfileexist){
//                    Log.e("FIRE","file_exist: " + current_key + "_image.png");
                    if (!Objects.equals(past_images_list.get(current_key), current_image_list.get(current_key))){
                        Log.e("FIRE","content_overiden Redonwloaded: " + current_key + "_image.png");
                        file_to_download.add(current_image_list.get(current_key));
                        filename_to_download.add(current_key + "_image.png");
                    }
                }else {
//                    Log.e("FIRE","file not found, past be null: downloading first time" + current_key + "_image.png");

                    file_to_download.add(current_image_list.get(current_key));
                    filename_to_download.add(current_key + "_image.png");
                }
            }




            ContributorImageDownloader = new ImageDownloader(10); // Use 4 threads for downloading
            ContributorImageDownloader.downloadAndSaveImages(file_to_download, filename_to_download,false,"ContributorImageDir",getApplicationContext());

//            if (fromthread){
//                if (ContributorImageDownloader.awaitTermination(5, TimeUnit.MINUTES));
//                int s = InternalStorage.getFileCountInDirectory(getApplicationContext(),"ContributorImageDir");
//                Log.d("SIZESIZE","num of file " + String.valueOf(s));
//                Log.d("SIZESIZE","size of list " + String.valueOf(current_image_list.size()));
////                if (s == current_image_list.size()){
////                    InternalStorage.deleteFileIfExists(getApplicationContext(),"con_profile_failed");
////                    InternalStorage.writeToFile("con_profile_success","",getApplicationContext());
//                }
//            }else {
            ContributorImageDownloader.shutdown();

//            }
            InternalStorage.serializeLinkedHashMapString(getApplicationContext(),current_image_list,"contributor_image_map");
            InternalStorage.serializeLinkedHashMap(getApplicationContext(),result,"contributor_profile_map");

        }

        Log.e("FIRE","EXISTING TO UPDATING PROFILE");

    }


    private void updateCourseDetails() throws JSONException {
        Log.e("FIRE","ENTERING TO UPDATE Course Details");

        String storefile = InternalStorage.readFromFile("database.json",getApplicationContext());
        JSONObject data = new JSONObject(storefile);
        if (data.has("COURSES")){
            LinkedHashMap<String, CourseDetailModel> course_details = new LinkedHashMap<>();

            LinkedHashMap<String, String> past_image = InternalStorage.deserializeLinkedHashMapString(getApplicationContext(),"contributor_image_map");
            LinkedHashMap<String, String> current_image_list = new LinkedHashMap<>();

            List<String> file_to_delete = new ArrayList<>();
            List<String> file_to_download = new ArrayList<>();
            List<String> filename_to_download = new ArrayList<>();
            List<String> all_ids = new ArrayList<>();

            JSONObject courses = data.getJSONObject("COURSES");
            Iterator<String> course_section_keys = courses.keys();

            List<List<String>> sorting = new ArrayList<List<String>>();

            while (course_section_keys.hasNext()){
                String section_key = course_section_keys.next();
                JSONObject course_keys_object = courses.getJSONObject(section_key);
                Iterator<String> course_keys = course_keys_object.keys();

                List<String> ordering_list = new ArrayList<>();
                while (course_keys.hasNext()){

                    String course_key = course_keys.next();
                    String course_name = course_keys_object.getJSONObject(course_key).getString("title");
                    String image = course_keys_object.getJSONObject(course_key).getString("image");
                    String rating = course_keys_object.getJSONObject(course_key).getString("rating");
                    String source = course_keys_object.getJSONObject(course_key).getString("source");
                    String certificate = course_keys_object.getJSONObject(course_key).getString("certificate");
                    String url = course_keys_object.getJSONObject(course_key).getString("url");
                    String is_youtube_link = course_keys_object.getJSONObject(course_key).getString("youtube_link");
                    String discounted_price = course_keys_object.getJSONObject(course_key).getString("discounted_price");
                    String actual_price = course_keys_object.getJSONObject(course_key).getString("actual_price");
                    String type = course_keys_object.getJSONObject(course_key).getString("type");

                    String url_id = InternalStorage.extractFileId(image);
                    boolean file_exist = InternalStorage.doesFileExist(getApplicationContext(),"CourseImageDir",url_id+"_image.png.png");
                    if (!file_exist){
                        file_to_download.add(image);
                        filename_to_download.add(url_id+"_image.png");
                    }
                    all_ids.add(url_id + "_image.png.png");


                    boolean cert_bool;
                    cert_bool = certificate.equals("true");

                    CourseDetailModel model = new CourseDetailModel(course_name,actual_price,discounted_price,source,image,cert_bool,rating,type,section_key,is_youtube_link,url,url_id);
                    Log.d("FIRE","COURSE NAME" + model.getCourse_description());
                    course_details.put(course_key,model);
                    ordering_list.add(course_key);

                }
                sorting.add(ordering_list);
            }

            String[] current_files_directory = InternalStorage.getAllFilesInDirectory(this,"CourseImageDir");
            for(String file: current_files_directory){
                if(!all_ids.contains(file)){
                    Log.d("FIRE","Delleting file of course_online: " + file);
                    InternalStorage.checkAndDeleteFile(this,file,"CourseImageDir");
                }
            }






            InternalStorage.serializeLinkedCourseDetailHashMap(this,course_details,"course_details");
            InternalStorage.serialize2DListToFile(this,sorting,"course_sorting");
            InternalStorage.serializeStringArray(this,all_ids.toArray(new String[0]),"course_all_ids");

            String[] settings = InternalStorage.deserializeStringArray(this,"settings");

            String section_random = settings[11];
            String course_random = settings[12];

            CourseOnlineImageDownloader = new ImageDownloader(4); // Use 4 threads for downloading
            Log.d("FIRE","COurse file to donwloadoad"+ file_to_download);
            Log.d("FIRE","File name file to donwloadoad"+ filename_to_download);

            CourseOnlineImageDownloader.downloadAndSaveImages(file_to_download, filename_to_download,false,"CourseImageDir",getApplicationContext());
            CourseOnlineImageDownloader.shutdown();

            MyDataUtils.random_course_shuffle(Objects.equals(section_random, "true"),Objects.equals(course_random, "true"),sorting,this);


        }
    }

    private void updateTasks() throws JSONException {
        if (InternalStorage.isFileExist(getApplicationContext(),"FoundActiveTask")){
            InternalStorage.deleteFileIfExists(getApplicationContext(),"FoundActiveTask");
        }
        String storefile = InternalStorage.readFromFile("database.json",getApplicationContext());
        JSONObject data = new JSONObject(storefile);
        if (data.has("TASKS")){
            JSONObject tasks = data.getJSONObject("TASKS");

            int length = tasks.length();
            String[] task = new String[length];
            String[] like = new String[length];

            int index = 0;
            Iterator<String> keys_first = tasks.keys();
            while (keys_first.hasNext()) {
                String key = keys_first.next();
                task[index] = key;
                like[index] = tasks.getString(key);
                index++;
            }

            if(!Objects.equals(task[0], "notask") && !Objects.equals(like[0], "notask")){
                InternalStorage.writeToFile("FoundActiveTask","",getApplicationContext());
                Intent intent = new Intent("FOUND_ACTIVE_TASK");
                intent.setPackage(this.getPackageName());
                getApplicationContext().sendBroadcast(intent);

            }else {
                Intent intent = new Intent("NO_ACTIVE_TASK");
                intent.setPackage(this.getPackageName());
                getApplicationContext().sendBroadcast(intent);
            }

            InternalStorage.serializeStringArray(getApplicationContext(),task, "active_task_names");
            InternalStorage.serializeStringArray(getApplicationContext(),like, "active_task_likes");

        }


        }

//    private void updateAds() throws JSONException {
//        String storefile = InternalStorage.readFromFile("database.json",getApplicationContext());
//        JSONObject data = new JSONObject(storefile);
//
//        String[] ads_list = new String[]{"main","department","year","semester","syllabus_detailed","notification","subject"};
//
//
//
//        if (data.has("ADS")){
//            for (String list: ads_list){
//                JSONObject ad_settings = data.getJSONObject("ADS").getJSONObject(list);
//                String[] settings = new String[2];
//                settings[0] = ad_settings.getString("banner_enable");
//                settings[1] = ad_settings.getString("native_enable");
//
//                InternalStorage.serializeStringArray(getApplicationContext(),settings,"ads_" + list + "_settings");
//
//            }
//        }
//    }

    private void updateTraining(boolean deleteImage) throws JSONException {

        if (deleteImage){
            InternalStorage.deleteImageDir(getApplicationContext(),"TrainingImageDir");
        }

        String storefile = InternalStorage.readFromFile("database.json",getApplicationContext());
        JSONObject data = new JSONObject(storefile);

        if (data.has("TRAINING")) {
            JSONObject trainingObject = data.getJSONObject("TRAINING");
            Log.d("FIRE","TRAINING OBJECT" + trainingObject.toString());

            int length = trainingObject.length();
            String[] names = new String[length];
            String[] titles = new String[length];
            List<String> images = new ArrayList<>();
            String[] contacts = new String[length];
            String[] url = new String[length];
            String[] messages = new String[length];

            TrainingDetailModel[][] trainingDetailArray = new TrainingDetailModel[length][];

            Iterator<String> keys = trainingObject.keys();
            Log.d("FIRE","Keys: " + keys.toString());

            int index = 0;

            while (keys.hasNext()) {
                String key = keys.next();
//                Log.d("FIRE","key a " + key);
                JSONObject item = trainingObject.getJSONObject(key);
//                Log.d("FIRE","item a " + item.toString());

                names[index] = item.getString("title");
                titles[index] = item.getString("subtitle");
                images.add(item.getString("image"));
                contacts[index] = item.getString("contact");
                url[index] = item.getString("url");
                messages[index] = item.getString("message");

                JSONObject section = item.getJSONObject("sections");
                String[][] sorted_section = getSortedPair(section);

                String[] all_section = sorted_section[0];
                String[] all_detail = sorted_section[1];

                TrainingDetailModel[] trainingModel = new TrainingDetailModel[section.length()];

//                Iterator<String> section_keys = section.keys();

                int count = 0;
                for (String sec: all_section){
                    TrainingDetailModel model = new TrainingDetailModel(sec,all_detail[count]);
                    trainingModel[count] = model;
                    count++;
                }
//                while (all_section.hasNext()) {
//                    String section_key = section_keys.next();
//                    String value = section.getString(section_key);
//                    String[] parts = section_key.split("_");
//                    if (parts.length > 1) {
//                        section_key = parts[1];
//                    }
//
//                    TrainingDetailModel model = new TrainingDetailModel(section_key,value);
//                    trainingModel[count] = model;
//                    count++;
//                }

                trainingDetailArray[index] = trainingModel;
                index++;
            }

//            String[][] final_section = new String[section_list.size()][];
//            for (int i = 0; i < section_list.size(); i++) {
//                final_section[i] = section_list.get(i);
//            }
//
//            String[][] final_detail = new String[detail_list.size()][];
//            for (int i = 0; i < detail_list.size(); i++) {
//                final_detail[i] = detail_list.get(i);
//            }

            imageDownloader = new ImageDownloader(images.size()); // Use 4 threads for downloading
            imageDownloader.downloadAndSaveImages(images, null,true,"TrainingImageDir",getApplicationContext());

            InternalStorage.serializeStringArray(getApplicationContext(),names,"training_main_title");
            InternalStorage.serializeStringArray(getApplicationContext(),titles,"training_main_subtitle");
            InternalStorage.serializeStringArray(getApplicationContext(),contacts,"training_main_contact");
            InternalStorage.serializeStringArray(getApplicationContext(),url,"training_main_url");

            InternalStorage.serializeStringArray(getApplicationContext(),messages,"training_main_message");

//            Log.d("FIRE","Final Training Detail" + trainingDetailArray.toString());

            InternalStorage.serializeModel(getApplicationContext(),trainingDetailArray,"training_main_model");


        }

    }

    private void updateSyllabus() throws JSONException {
        Log.e("FIRE","ENTERING UPDATING SYLLABUS");
        String[] department_list = new String[]{"bca_syllabus","bba_syllabus","mba_syllabus","applied_syllabus","cse_syllabus","ece_syllabus","ce_syllabus","ee_syllabus","me_syllabus","ae_syllabus"};
        String storefile = InternalStorage.readFromFile("database.json",getApplicationContext());
        JSONObject data = new JSONObject(storefile);

        if (data.has("SYLLABUS")) {

            for (String department : department_list) {
                if (data.getJSONObject("SYLLABUS").has(department)) {
                    JSONObject sorted = data.getJSONObject("SYLLABUS").getJSONObject(department);
                    Iterator<String> keys = sorted.keys();

                    // Storing keys in a String array
                    String[] keysArray = new String[sorted.length()];
                    int index = 0;
                    while (keys.hasNext()) {
                        keysArray[index++] = keys.next();
                    }

                    InternalStorage.serializeStringArray(getApplicationContext(),keysArray,department+"_year");

                    for (String year: keysArray){
                        JSONObject year_semester = data.getJSONObject("SYLLABUS").getJSONObject(department).getJSONObject(year);
                        Iterator<String> year_keys = year_semester.keys();
                        String[] sem_list = new String[year_semester.length()];

                        int year_index = 0;
                        while (year_keys.hasNext()) {
                            sem_list[year_index++] = year_keys.next();
                        }

                        String sem_file = department + "_" + year + "_" + "semester";
                        Arrays.sort(sem_list);

                        InternalStorage.serializeStringArray(getApplicationContext(),sem_list,sem_file);

                        for (String sem: sem_list){
//                            String logerdata = "logerdata: SYLLABUS_" + department + "_" + year + "_" + sem + "_Theory";
//                            Log.e("FIRE",logerdata);
                            JSONObject sem_theory = data.getJSONObject("SYLLABUS").getJSONObject(department).getJSONObject(year).getJSONObject(sem).getJSONObject("Theory");
                            JSONObject sem_practical = data.getJSONObject("SYLLABUS").getJSONObject(department).getJSONObject(year).getJSONObject(sem).getJSONObject("Practical");

                            String[][] operation = getSortedPair(sem_theory);
                            String[][] operation_second = getSortedPair(sem_practical);

                            String sub_filename = department + "_" + year + "_" + sem;
                            String detailed_filename = department + "_" + year + "_" + sem;

                            String[] subjects = operation[0];
                            String[] detailed_syllabus = operation[1];

                            String[] subjects_prac = operation_second[0];
                            String[] detailed_syllabus_prac = operation_second[1];

                            InternalStorage.serializeStringArray(getApplicationContext(),subjects,sub_filename + "_Theory_Sub");
                            InternalStorage.serializeStringArray(getApplicationContext(),detailed_syllabus,detailed_filename + "_Theory_Val");

                            InternalStorage.serializeStringArray(getApplicationContext(),subjects_prac,sub_filename + "_Practical_Sub");
                            InternalStorage.serializeStringArray(getApplicationContext(),detailed_syllabus_prac,detailed_filename + "_Practical_Val");


                        }






                    }




                }



            }
        }
        Log.e("FIRE","SYLLABUS UPDATED SUCCESSFULLY");
    }

    private void updatePyq(String section,boolean deleteOfflinePDF, JSONObject pastdata, String foldername) throws JSONException {

        String depart = section.toLowerCase();
        String[] department_list = new String[]{"bca_"+depart,"bba_"+depart,"mba_"+depart,"applied_"+depart,"cse_"+depart,"ece_"+depart,"ce_"+depart,"ee_"+depart,"me_"+depart,"ae_"+depart};
        String storefile = InternalStorage.readFromFile("database.json",getApplicationContext());
        JSONObject data = new JSONObject(storefile);

        if (deleteOfflinePDF){
            if(pastdata.has(section)){
                for (String department: department_list){
                    String[] subjects = new String[]{};
                    String[] values = new String[]{};

                    String subject_name = department + "_subject";
                    String subject_url = department + "_link";

                    String[] past_subject = InternalStorage.deserializeStringArray(getApplicationContext(),subject_name);
                    String[] past_value = InternalStorage.deserializeStringArray(getApplicationContext(),subject_url);

                    if (data.getJSONObject(section).has(department)) {

                        JSONObject unsort = data.getJSONObject(section).getJSONObject(department);
                        String[][] operation = getSortedPair(unsort);

                        subjects = operation[0];
                        values = operation[1];

                        Map<String, String> map = new HashMap<>();

                        for (int i = 0; i < subjects.length; i++) {
                            map.put(subjects[i], values[i]);
                        }

                        int count_index = 0;

                        Log.d("FIRE","current_subject: " + Arrays.toString(subjects));
                        Log.d("FIRE","past_subject: " + Arrays.toString(past_subject));

                        for(String sub: past_subject){
                            String filename = null;
                            if (!containsString(subjects, sub)){
                                filename = foldername + "_" + department + "_" + sub;
                            } else if (!Objects.equals(past_value[count_index], map.get(sub))) {
                                filename = foldername + "_" + department + "_" + sub;
                            }
                            if (filename != null) {
                                InternalStorage.checkAndDeleteFile(getApplicationContext(), filename, foldername);
                            }

                            count_index++;
                        }



                    }

//                    InternalStorage.serializeStringArray(getApplicationContext(),subjects,subject_name);
//                    InternalStorage.serializeStringArray(getApplicationContext(),values,subject_url);
                }
            }
        }

        if (data.has(section)){

            for (String department: department_list){
                String[] subjects = new String[]{};
                String[] values = new String[]{};

                String subject_name = department + "_subject";
                String subject_url = department + "_link";

                if (data.getJSONObject(section).has(department)) {

                    JSONObject unsort = data.getJSONObject(section).getJSONObject(department);
                    String[][] operation = getSortedPair(unsort);

                    subjects = operation[0];
                    values = operation[1];

                }

                InternalStorage.serializeStringArray(getApplicationContext(),subjects,subject_name);
                InternalStorage.serializeStringArray(getApplicationContext(),values,subject_url);
            }
        }
        Log.e("FIRE",section + " UPDATED SUCCESSFULLY");
    }

    public boolean containsString(String[] array, String target) {
        return Arrays.asList(array).contains(target);
    }
    private String[][] getSortedPair(JSONObject unsort) throws JSONException {
        Map<String, Object> sortedMap = new TreeMap<>();

        Iterator<String> keys_first = unsort.keys();
        while (keys_first.hasNext()) {
            String key = keys_first.next();
            sortedMap.put(key, unsort.get(key));
        }

        // Convert the sorted map back to a JSONObject
        JSONObject departmentObject = new JSONObject(sortedMap);

//                Log.d("FIRE",departmentObject.toString());
//                Log.d("FIRE","Datacurrent: " + InternalStorage.readFromFile("database.json",context));
        int length = departmentObject.length();
        String[] subjects = new String[]{};
        String[] values = new String[]{};
        subjects = new String[length];
        values = new String[length];

        // Iterate over the keys and fill the arrays
        Iterator<String> keys_two = departmentObject.keys();
        int index = 0;
        while (keys_two.hasNext()) {
            String key = keys_two.next();
            subjects[index] = key;
            values[index] = departmentObject.getString(key);
            index++;
        }

//        String subject_name = department + "_subject";
//        String subject_url = department + "_link";

        String[] subject_final = new String[subjects.length];
        for (int i = 0; i < subjects.length; i++) {
            String str = subjects[i];

            // Check if the string contains an underscore
            if (str.contains("_")) {
                // Split the string by the underscore and take the part on the right
                subject_final[i] = str.split("_", 2)[1];
            } else {
                // If there's no underscore, keep the original string
                subject_final[i] = str;
            }
        }
        return new String[][]{subject_final,values};

    }

    private boolean isDataModified(JSONObject data) {
        Log.e("FIRE","data is modified!");
        String current_data = data.toString();
        Log.e("FIRE","currentdata: " + current_data);
        String past_data = InternalStorage.readFromFile("database.json",getApplicationContext());
        Log.e("FIRE","pastdata:    " + past_data);
        return !current_data.equals(past_data);
    }

    private String currentDate() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("FIRE","Creating Service");
        FirebaseApp.initializeApp(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("");
        databaseReference.addValueEventListener(valueEventListener);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Log.d("FIRE","Thread is Running.");
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    // Handle the interruption


//                    Log.d("FIRE","Thread was interrupted.");
//                }
//            }
//        }).start();
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("FIRE","Exiting Service!!");
        if (databaseReference != null && valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }

    public static void sendNotification(Context context, String title, String message) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        if (notificationManager == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create a notification channel for Android Oreo and higher
            NotificationChannel channel = new NotificationChannel("45", "DEMO", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("CHANNEL_DESCRIPTION");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        Notification.Builder builder = null; // Replace with your own icon
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context, "45")
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.bbsbec_logo)
                    .setContentIntent(pendingIntent).setAutoCancel(true);
        }

        assert builder != null;
        if((NotificationOperation.areNotificationsEnabled(context))){
            Notification notification = builder.build();
            notificationManager.notify(/* Unique Notification ID */ 1, notification);
            Log.e("FIRE","NOTIFICATION SEND SUCCESSFULLY");
        }
    }

    private JSONObject convertSnapshotToJSON(DataSnapshot dataSnapshot) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            Object value = child.getValue();
            if (value instanceof Map) {
                jsonObject.put(child.getKey(), new JSONObject((Map) value));
            } else {
                jsonObject.put(child.getKey(), value);
            }
        }
        return jsonObject;
    }


    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
//        if (!(activeNetwork != null && activeNetwork.isConnected())){
//            Toast.makeText(MainActivity.this, "Check Your Internet Connection", Toast.LENGTH_LONG).show();
//        }
        return activeNetwork != null && activeNetwork.isConnected();
    }

}
