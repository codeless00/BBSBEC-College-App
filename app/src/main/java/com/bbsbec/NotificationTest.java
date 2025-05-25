package com.bbsbec;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NotificationTest extends Thread{
    private final Context context;

    public NotificationTest(Context con){
        this.context = con;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            Log.i("INFOO","Running thread");
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url("https://github.com/amritpalsong/pubamrit/raw/main/syllabus_cse.txt").build();
//        try(Response response = client.newCall(request).execute()){
//            int responseCode = response.code();
//            assert response.body() != null;
//            String responseBody = response.body().string();
//            Log.i("INFOO","Received txt: " + responseBody);
//            if(updateResource(responseBody)){
//                Log.i("INFOO","Updating NOti: " + responseBody);
//                writeNewResource(responseBody);
//                NotificationOperation.increaseNotification(1,context);
//                Intent intent = new Intent("UPDATE_NOTIFICATION");
//                context.sendBroadcast(intent);
//            }
//
//        }catch (Exception e){
//
//        }
//            NotificationOperation.increaseNotification(1,context);
//            Intent intent = new Intent("UPDATE_NOTIFICATION");
//            context.sendBroadcast(intent);
            boolean isMyServiceRunning = MyServiceUtils.isServiceRunning(context, DatabaseChangeListenerService.class);
            if (isMyServiceRunning) {
                Log.e("SERV", "MyService is running");
            } else {
                Log.e("SERV", "MyService is not running");
            }


        }
    }

    private void writeNewResource(String responseBody) {
        InternalStorage.writeToFile("syllabus_cse",responseBody,context);
    }

    private boolean updateResource(String responseBody) {
        String syllabusCurrent = InternalStorage.readFromFile("syllabus_cse",context);
        Log.i("INFOO","syllabus current: " + syllabusCurrent);
        return !syllabusCurrent.equals(responseBody);
    }
}
