package com.bbsbec;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import java.io.File;

public class NotificationOperation {

    static String notificationFile = "NotificationCount.txt";

    public static void initializeNotification(Context context){
        File file = context.getFileStreamPath(notificationFile);
        if (!file.exists()){
            InternalStorage.writeToFile(notificationFile,"0",context);
            InternalStorage.writeToFile("syllabus_cse","Nothing",context);
        }

    }



    public static void increaseNotification(int count, Context context){
        int pastCount = Integer.parseInt(InternalStorage.readFromFile(notificationFile, context));
        int currentCount = pastCount + count;
        InternalStorage.writeToFile(notificationFile,String.valueOf(currentCount),context);

    }

    public static boolean areNotificationsEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For Android Oreo and above, use NotificationManagerCompat
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            return manager != null && manager.getImportance() != NotificationManager.IMPORTANCE_NONE;
        } else {
            // For older versions, use NotificationManagerCompat
            return NotificationManagerCompat.from(context).areNotificationsEnabled();
        }
    }

    public static void clearNotification(Context context){
        InternalStorage.writeToFile(notificationFile,"0",context);

    }

    public static int getNotificationCount(Context context){
        return Integer.parseInt(InternalStorage.readFromFile(notificationFile,context));
    }
}
