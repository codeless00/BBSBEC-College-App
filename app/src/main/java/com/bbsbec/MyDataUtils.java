package com.bbsbec;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MyDataUtils {

    public static void initializeDatabase(Context context, DrawerLayout layout){



        if (!fileExists(context,"settings.json")){
            InternalStorage.writeToFile("database.json","{}",context);
            InternalStorage.writeToFile("notification_list.json","{}",context);

            try {
                // Create JSON object
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("initialized", true);
                jsonObject.put("notification",0);
                InternalStorage.writeToFile("settings.json", jsonObject.toString(),context);
            } catch (Exception e) {
                Log.d("FIRE",e.toString());
            }

        }
//        else {
//            String fd = InternalStorage.readFromFile("database.json",context);
//            if (fd.equals("{}")){
//                progressDialog.setMessage("Syncing data, please wait...");
//                progressDialog.setCancelable(false);
//                progressDialog.show();
//            }
//        }
    }

    private static void createSnackBar(Context context, DrawerLayout drawerLayout) {
        Snackbar.make(drawerLayout, "Turn on internet and click on update button to get latest update. ", Snackbar.LENGTH_INDEFINITE)
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .setBackgroundTint(Color.parseColor("#7A0000"))
                .setAction("Update", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Wait a minute!", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }


    public static boolean fileExists(Context context, String fileName){
        File file = context.getFileStreamPath(fileName);
        return file.exists();
    }

    public static String[][] pyq(String department, Context context) throws JSONException {

        String storefile = InternalStorage.readFromFile("database.json",context);
        JSONObject data = new JSONObject(storefile);

        if (data.has("PYQ")){

            String[] subjects = new String[]{};
            String[] values = new String[]{};

            if (data.getJSONObject("PYQ").has(department)) {

                JSONObject unsort = data.getJSONObject("PYQ").getJSONObject(department);

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
            }

            return new String[][] {subjects, values};

        }else {
            return null;
        }

    }

    public static void random_course_shuffle(boolean random_section, boolean random_course, List<List<String>> data_list, Context ctx){
        if(data_list != null){
            if (random_course) {
                for (List<String> innerList : data_list) {
                    Collections.shuffle(innerList);
                }
            }

            if (random_section) {
                Collections.shuffle(data_list);
            }


            List<String> flatList = new ArrayList<>();
            List<String> position = new ArrayList<>();


            int current_size = 0;

            for (List<String> innerList : data_list) {
                flatList.addAll(innerList);
                position.add(String.valueOf(current_size));
                current_size += innerList.size();
            }

            String[] resultArray = new String[flatList.size()];
            String[] resultPosition = new String[position.size()];

            flatList.toArray(resultArray);
            position.toArray(resultPosition);

            InternalStorage.serializeStringArray(ctx,resultArray,"course_sorting_details");
            InternalStorage.serializeStringArray(ctx,resultPosition,"course_position_sorting_details");

        }







    }

    public static String[] syllabus(Context context, String department) throws JSONException{

        String storefile = InternalStorage.readFromFile("database.json",context);
        JSONObject data = new JSONObject(storefile);

        if (data.has("SYLLABUS")){

            String[] subjects = new String[]{};

            if (data.getJSONObject("SYLLABUS").has(department)) {

                JSONObject unsort = data.getJSONObject("SYLLABUS").getJSONObject(department);
                Log.e("FIRE","yearlist " + unsort.toString());

            }

        }
        return new String[0];
    }
}
