package com.bbsbec;
import static android.icu.text.RelativeDateTimeFormatter.RelativeDateTimeUnit.MINUTE;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import java.util.concurrent.TimeUnit;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImageDownloader {
    private ExecutorService executorService;

    public ExecutorService getExecutorService() {
        return this.executorService;
    }

    public ImageDownloader(int numberOfThreads) {
        executorService = Executors.newFixedThreadPool(numberOfThreads);
    }

    public void downloadAndSaveImages(List<String> imageUrls, List<String> filename, boolean istraining, String foldername, Context context) {
        Log.d("FIRE","Starting Executor Service");
        int count = 0;
        for (String url : imageUrls) {
            String imageName;
            if (istraining){
                imageName = "Training_logo_" + String.valueOf(count);
            }else {
                imageName = filename.get(count);
            }
            executorService.submit(() -> {
                Bitmap bitmap = InternalStorage.downloadImage(url);
                if (bitmap != null) {
                    InternalStorage.saveImageToInternalStorage(bitmap, imageName,foldername, context);
                    Log.d("ImageDownloader", "Image saved: " + imageName);
                } else {
                    Log.e("ImageDownloader", "Failed to download image: " + url);
//                    if(!InternalStorage.isFileExist(context,"con_profile_failed")){
//                        InternalStorage.writeToFile("con_profile_failed","",context);
//                    }
                }
            });

            count ++;
        }
    }

    public void shutdown() {
        executorService.shutdown();

    }

    public void lazyshutdown(){
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        try {
//            // Wait for all tasks to complete (with a very long timeout)
//            while (!executorService.isTerminated()) {
//                // Wait a short time before checking again
//                Thread.sleep(100); // Avoid busy-waiting
//            }
//            System.out.println("All tasks completed.");
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            System.out.println("Thread was interrupted while waiting for termination.");
//        }
    }

    public boolean checkTerminated(){
        return  executorService.isTerminated();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) {
        try {
            return executorService.awaitTermination(timeout, unit);
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
