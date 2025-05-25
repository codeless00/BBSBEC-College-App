package com.bbsbec;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyPDFViewer extends AppCompatActivity implements OnRenderListener {

    PDFView pdfView;
    LottieAnimationView lottieAnimationView;
    boolean tryToLoadOffline;
    Handler handler;
    String subName;
    Executor executor;
    InputStream inputStream;
    String url;
    String suffix;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_pdfviewer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mypdfview_main), (v, insets) -> {
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

        Intent i = getIntent();
        String url_raw = i.getStringExtra("URL");
        url = InternalStorage.convertToDirectDownloadLink(url_raw);
        subName = i.getStringExtra("SUBNAME");
        suffix = i.getStringExtra("SUFFIX");
        String title = i.getStringExtra("TITLE");
        tryToLoadOffline = i.getBooleanExtra("LOADOFFLINE",false);

        getSupportActionBar().setTitle(title);


        Uri uri = Uri.parse(url);
        context = getApplicationContext();


        pdfView = (PDFView) findViewById(R.id.pdfview);

        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animationView);

        executor = Executors.newSingleThreadExecutor();
        handler = new Handler();
//        String urls = url;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
//                    inputStream;
                    File pdfile;
                    if (tryToLoadOffline){
                        String filename = create_some_unique(suffix,subName);
                        if (fileExistsInInternalStorage(context, filename,suffix)){
                            ContextWrapper cw = new ContextWrapper(context);
                            File directory = cw.getDir(suffix, Context.MODE_PRIVATE);
                            pdfile = new File(directory, filename);
//                            pdfile = new File(context.getFilesDir(), filename);
                            inputStream = (InputStream) InternalStorage.deserializeInputStream(context,filename);
                        }
                        else{
                            inputStream = new URL(url).openStream();
                            pdfile = null;
//                            String data = convertInputStreamToString(inputStream);
                            downloadPdfToInternalStorage(context,url,filename,suffix);
//                            InternalStorage.writeToFile(filename,inputStream.toString(),context);
                        }
                    }else{
                        inputStream = new URL(url).openStream();
                        pdfile = null;

                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (isFinishing() || isDestroyed()) {
                                return; // Exit if the activity is no longer valid
                            }

                            // Ensure the Handler's Looper thread is alive
                            if (handler.getLooper().getThread().isAlive()) {
                            if (pdfile != null){
                                pdfView.fromFile(pdfile)
                                        .onRender(MyPDFViewer.this)
                                        .load();

                            }else {
                                pdfView.fromStream(inputStream)
                                        .onRender(MyPDFViewer.this)
                                        .load();
                            }
                            }


                        }
                    });
                } catch (IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyPDFViewer.this,"Check Your Internet Connection!",Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });
    }

    private String create_some_unique(String timetablePdf, String subName) {
        return timetablePdf +"_"+ subName;
    }

    @Override
    public void onInitiallyRendered(int nbPages) {
        lottieAnimationView.setVisibility(View.GONE);
    }

    public static String getFileNameFromUrl(String urlString) {
        try {
            // Decode URL to handle special characters properly
            String decodedUrl = URLDecoder.decode(urlString, "UTF-8");

            // Extract the filename from the URL
            String fileName = new File(new URL(decodedUrl).getPath()).getName();

            // Replace '/' with '_' in the filename
            fileName = fileName.replace('/', '_');

            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean fileExistsInInternalStorage(Context context, String fileName, String foldername) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(foldername, Context.MODE_PRIVATE);
        File file = new File(directory, fileName);
        return file.exists();
    }
    public static InputStream getInputStreamFromInternalStorage(Context context, String fileName) {
        try {
            File file = new File(context.getFilesDir(), fileName);
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void downloadPdfToInternalStorage(Context context, String url, String fileName, String foldername) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Failed to download file: " + response);
                }

                ContextWrapper cw = new ContextWrapper(context);
                File directory = cw.getDir(foldername, Context.MODE_PRIVATE);
                File file = new File(directory, fileName);

//                File file = new File(context.getFilesDir(), fileName);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(response.body().bytes());
                }

                runOnUiThread(() -> Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    public void onBackPressed() {

        // Shut down the executor to stop the background thread
        if (executor instanceof ExecutorService) {
            ((ExecutorService) executor).shutdownNow();
        }

        // Remove all pending runnables in the handler's message queue
        handler.removeCallbacksAndMessages(null);

        // If you have any open InputStreams, close them here
        // For example, you can store inputStream in a class variable and close it
//        if (inputStream != null) {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        new NetworkTask().execute();


//        if (inputStream != null) {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        // Recycle the PDFView to clean up resources
        if (pdfView != null) {
            pdfView.recycle();
        }

        // Now call the super method to proceed with the default back press behavior
        super.onBackPressed();
    }

    private class NetworkTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            // Perform network operations here
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
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
}