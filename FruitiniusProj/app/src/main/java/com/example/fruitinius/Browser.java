package com.example.fruitinius;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bolts.AppLinks;

public class Browser extends AppCompatActivity {

    private String last = "https://www.google.com";
    private String deepURL;
    private String data;
    private String url;
    private Bitmap backButton;
    public static final String APP_PREFERENCES = "Session";
    public static final String APP_PREFERENCES_PAGE = "Page";
    private SharedPreferences Settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        Settings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        Bundle arguments = getIntent().getExtras();
        deepURL = arguments.get("link").toString();
        data = arguments.get("data").toString();
        if (data.equals("second"))
            onResume();
        else{
        url = deepURL + data;
        Save(url);
        onPause();
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        openChromeCustomTabs(url);
        }
        backButton = BitmapFactory.decodeResource(getResources(), R.drawable.reloaded);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = Settings.edit();
        editor.putString(APP_PREFERENCES_PAGE, last);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Settings.contains(APP_PREFERENCES_PAGE)) {
            last = Settings.getString(APP_PREFERENCES_PAGE, url);
            openChromeCustomTabs(last);
        }

    }

    private void Save(String url) {
        last = url;
    }

    private void openChromeCustomTabs(String url) {
        Save(url);
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setCloseButtonIcon(backButton);
        builder.addDefaultShareMenuItem();
        CustomTabsIntent intent = builder.build();
        intent.launchUrl(this, Uri.parse(url));
    }

}