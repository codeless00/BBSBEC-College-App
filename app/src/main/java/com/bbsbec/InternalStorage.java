package com.bbsbec;


import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InternalStorage {

    public static void writeToFile(String fileName, String data, Context context){
        FileOutputStream fos = null;
        try{
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(data.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(fos != null){
                try{
                    fos.close();
                }catch (IOException e){
                    Toast.makeText(context, "Can't close fos stream", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public static String readFromFile(String fileName, Context context) {
        FileInputStream fis = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException ignored) {
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Toast.makeText(context, "Can't close fis stream", Toast.LENGTH_SHORT).show();
                }
            }
        }

        return stringBuilder.toString();
    }


//    ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
//        out.writeObject(array);

    public static void serializeStringArray(Context context, String[] array, String filename) {
        try (FileOutputStream fileOut = context.openFileOutput(filename, Context.MODE_PRIVATE);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(array);
//            System.out.println("Serialized data is saved in " + filename);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    // Method to deserialize String array from a file
    public static String[] deserializeStringArray(Context context, String filename) {
        String[] array = new String[]{};
        try (FileInputStream fileIn = context.openFileInput(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            array = (String[]) in.readObject();
//            System.out.println("Deserialized data from " + filename);
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
//            System.out.println("String[] class not found");
            c.printStackTrace();
        }
        return array;
    }

    public static void serializeInputStream(Context context, String fileName, InputStream inputStream) {
        try {
            byte[] byteArray = readInputStreamToByteArray(inputStream);
            serializeByteArrayToInternalStorage(context, fileName, byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] readInputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

    // Combined method to deserialize InputStream from internal storage
    public static InputStream deserializeInputStream(Context context, String fileName) {
        byte[] byteArray = deserializeByteArrayFromInternalStorage(context, fileName);
        if (byteArray != null) {
            return getInputStreamFromByteArray(byteArray);
        }
        return null;
    }

    // Method to serialize the byte array to internal storage
    private static void serializeByteArrayToInternalStorage(Context context, String fileName, byte[] byteArray) {
        try (FileOutputStream fos = new FileOutputStream(new File(context.getFilesDir(), fileName));
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to deserialize the byte array from internal storage
    private static byte[] deserializeByteArrayFromInternalStorage(Context context, String fileName) {
        try (FileInputStream fis = new FileInputStream(new File(context.getFilesDir(), fileName));
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (byte[]) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to create an InputStream from the deserialized byte array
    private static InputStream getInputStreamFromByteArray(byte[] byteArray) {
        return new ByteArrayInputStream(byteArray);
    }
    public static boolean checkAndDeleteFile(Context context, String fileName, String foldername) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(foldername, Context.MODE_PRIVATE);
        File file = new File(directory, fileName);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }


    public static void serializeModel(Context context, TrainingDetailModel[][] array, String filename) {
        try (FileOutputStream fileOut = context.openFileOutput(filename, Context.MODE_PRIVATE);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(array);
//            System.out.println("Serialized data is saved in " + filename);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    // Method to deserialize String array from a file
    public static TrainingDetailModel[][] deserializeModel(Context context, String filename) {
        TrainingDetailModel[][] array = new TrainingDetailModel[][]{};
        try (FileInputStream fileIn = context.openFileInput(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            array = (TrainingDetailModel[][]) in.readObject();
//            System.out.println("Deserialized data from " + filename);
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
//            System.out.println("String[] class not found");
            c.printStackTrace();
        }
        return array;
    }



    public static boolean dd(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = dd(new File(dir, child));
                    if (!success) {
                        return false; // If any delete fails, return false
                    }
                }
            }
        }
        // Delete the file or directory (empty by now)
        return dir.delete();
    }

    // Function to delete all files created by the app
    public static void deleteAllAppFiles(Context context) {
        // 1. Delete files in the app's internal storage (getFilesDir)
        File filesDir = context.getFilesDir();
        if (filesDir != null && filesDir.exists()) {
            dd(filesDir);  // Deletes all files and subdirectories
        }

        // 2. Delete files in the cache directory (getCacheDir)
        File cacheDir = context.getCacheDir();
        if (cacheDir != null && cacheDir.exists()) {
            dd(cacheDir);  // Deletes all files and subdirectories
        }

        // 3. Optionally, clear shared preferences (though these are not files, but data)
//        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)
//                .edit().clear().apply(); // Clear shared preferences data if needed

        // If you have more directories in private storage, you can delete them similarly.
    }



    public static Bitmap downloadImage(String url_raw) {
        String url = convertToDirectDownloadLink(url_raw);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                InputStream inputStream = response.body().byteStream();
                Bitmap imageraw = BitmapFactory.decodeStream(inputStream);
                if (imageraw == null){
                    Log.d("InternalStorage","Image raw is null");
//                    Log.d("InternalStorage",response.body().string());


                }
                Log.d("InternalStorage","Image Downloded");
                return imageraw;
            } else {
                throw new IOException("Failed to download file: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static synchronized String convertToDirectDownloadLink(String shareableLink) {
        // Check if the link is valid
        if (shareableLink == null || shareableLink.isEmpty()) {
            throw new IllegalArgumentException("Invalid shareable link");
        }

        // Extract the file ID from the shareable link
        String fileId = extractFileId(shareableLink);
        if (fileId == null || fileId.isEmpty()) {
            throw new IllegalArgumentException("Could not extract file ID from the shareable link");
        }

        // Construct the direct download link
        return "https://drive.usercontent.google.com/uc?id=" + fileId + "&export=download";
    }

    public static synchronized String extractFileId(String shareableLink) {
        // Regular expression to extract the file ID
        String regex = "([a-zA-Z0-9_-]{33,})";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(shareableLink);

        // Check if the pattern matches
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }



    public synchronized static void saveImageToInternalStorage(Bitmap bitmapImage, String imageName, String foldername, Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(foldername, Context.MODE_PRIVATE);
        File mypath = new File(directory, imageName + ".png");

        try (FileOutputStream fos = new FileOutputStream(mypath)) {
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap loadImageFromInternalStorage(String imageName, String foldername, Context context) {
        try {
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir(foldername, Context.MODE_PRIVATE);
            File mypath = new File(directory, imageName + ".png");

            return BitmapFactory.decodeStream(new FileInputStream(mypath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean deleteDirectory(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDirectory(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

    // Method to delete the imageDir in internal storage
    public static boolean deleteImageDir(Context context, String foldername) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(foldername, Context.MODE_PRIVATE);
        return deleteDirectory(directory);
    }

    public static boolean deleteImage(Context context, String foldername, String filename) {
        ContextWrapper cw = new ContextWrapper(context);
        // Get the directory
        File directory = cw.getDir(foldername, Context.MODE_PRIVATE);
        // Create the file object with the specified filename
        File file = new File(directory, filename);
        // Check if the file exists and delete it
        return file.exists() && file.delete();
    }



    public static void serializeLinkedHashMap(Context context, LinkedHashMap<String, ContributorDetailModel> map, String filename) {
        try (FileOutputStream fileOut = context.openFileOutput(filename, Context.MODE_PRIVATE);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(map);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static void serializeLinkedCourseDetailHashMap(Context context, LinkedHashMap<String, CourseDetailModel> map, String filename) {
        try (FileOutputStream fileOut = context.openFileOutput(filename, Context.MODE_PRIVATE);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(map);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static void serialize2DListToFile(Context context, List<List<String>> list, String FILE_NAME) {
        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<List<String>> deserialize2DListFromFile(Context context, String FILE_NAME) {
        List<List<String>> list = null;

        try (FileInputStream fis = context.openFileInput(FILE_NAME);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            list = (List<List<String>>) ois.readObject(); // Deserialize back to object
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static LinkedHashMap<String, ContributorDetailModel> deserializeLinkedHashMap(Context context, String filename) {
        LinkedHashMap<String, ContributorDetailModel> map = null;
        try (FileInputStream fileIn = context.openFileInput(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            map = (LinkedHashMap<String, ContributorDetailModel>) in.readObject();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
        return map;
    }

    public static LinkedHashMap<String, CourseDetailModel> deserializeLinkedCourseDetailsHashMap(Context context, String filename) {
        LinkedHashMap<String, CourseDetailModel> map = null;
        try (FileInputStream fileIn = context.openFileInput(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            map = (LinkedHashMap<String, CourseDetailModel>) in.readObject();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
        return map;
    }

    public static void serializeLinkedHashMapString(Context context, LinkedHashMap<String, String> map, String filename) {
        try (FileOutputStream fileOut = context.openFileOutput(filename, Context.MODE_PRIVATE);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(map);
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static LinkedHashMap<String, String> deserializeLinkedHashMapString(Context context, String filename) {
        LinkedHashMap<String, String> map = null;
        try (FileInputStream fileIn = context.openFileInput(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            map = (LinkedHashMap<String, String>) in.readObject();
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
        return map;
    }

    public static String formatNumber(String number) {
        long num = Long.parseLong(number);

        if (num >= 1000000000) {
            return String.format("%.1f\uD835\uDC01", num / 1000000000.0);
        } else if (num >= 1000000) {
            return String.format("%.1f\uD835\uDC0C", num / 1000000.0);
        } else if (num >= 1000) {
            return String.format("%.1f\uD835\uDC0A", num / 1000.0);
        } else {
            return String.valueOf(num);
        }
    }

    public static boolean doesFileExist(Context context, String folderName, String fileName) {
        // Get the folder in the internal storage
//        File folder = new File(context.getFilesDir(), folderName);

        ContextWrapper cw = new ContextWrapper(context);
        File folder = cw.getDir(folderName, Context.MODE_PRIVATE);

        // Check if the folder exists
        if (!folder.exists() || !folder.isDirectory()) {
            return false; // Folder doesn't exist or is not a directory
        }

        // Get the file in the specified folder
        File file = new File(folder, fileName);

        // Check if the file exists
        return file.exists();
    }

    public static boolean isFileExist(Context context, String fileName) {
        File file = context.getFileStreamPath(fileName);
        return file != null && file.exists();
    }

    public static boolean deleteFileIfExists(Context context, String fileName) {
        File file = context.getFileStreamPath(fileName);
        if (file != null && file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static int getFileCountInDirectory(Context context, String directoryName) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(directoryName, Context.MODE_PRIVATE);
        if (directory != null && directory.isDirectory()) {
            String[] children = directory.list();
            assert children != null;
            return children.length;
        }
        return 0;
    }

    public static String[] getAllFilesInDirectory(Context context, String directoryName) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(directoryName, Context.MODE_PRIVATE);
        if (directory != null && directory.isDirectory()) {
            return directory.list();
        }
        return new String[]{};
    }

}
