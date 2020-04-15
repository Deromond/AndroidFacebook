package com.example.fruitinius;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.FacebookSdk;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import bolts.AppLinks;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final int picturCount = 12;
    private final int openCardCount = 2;
    private static final String IMAGES_STATE_KEY = "STATE";
    private ImageView ico00, ico01, ico02, ico03;
    private ImageView ico10, ico11, ico12, ico13;
    private ImageView ico20, ico21, ico22, ico23;
    private ImageView firstImage;
    private TextView textScore;
    private TextView textLevel;
    private ImageView[] imar = new ImageView[picturCount];
    private boolean[] checkedArray = new boolean[picturCount];
    private Timer timer;
    private TimerTask timerTask;
    private int time = 0;
    private int level = 0;
    private int checkedCards = 0;
    private int score = 0;
    private int[] cards = new int[openCardCount];
    private int[] array = new int[picturCount];
    private boolean checked = false;
    private boolean start = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textScore = findViewById(R.id.score);
        textLevel = findViewById(R.id.level);

        ico00 = findViewById(R.id.ico00);
        addImagesToArray(ico00);
        ico01 = findViewById(R.id.ico01);
        addImagesToArray(ico01);
        ico02 = findViewById(R.id.ico02);
        addImagesToArray(ico02);
        ico03 = findViewById(R.id.ico03);
        addImagesToArray(ico03);
        ico10 = findViewById(R.id.ico10);
        addImagesToArray(ico10);
        ico11 = findViewById(R.id.ico11);
        addImagesToArray(ico11);
        ico12 = findViewById(R.id.ico12);
        addImagesToArray(ico12);
        ico13 = findViewById(R.id.ico13);
        addImagesToArray(ico13);
        ico20 = findViewById(R.id.ico20);
        addImagesToArray(ico20);
        ico21 = findViewById(R.id.ico21);
        addImagesToArray(ico21);
        ico22 = findViewById(R.id.ico22);
        addImagesToArray(ico22);
        ico23 = findViewById(R.id.ico23);
        addImagesToArray(ico23);
        score = 0;

        if (savedInstanceState!=null && savedInstanceState.containsKey(IMAGES_STATE_KEY)){
            array = savedInstanceState.getIntArray("images_id");
            checkedArray=savedInstanceState.getBooleanArray("check_arr");
            checkedCards = savedInstanceState.getInt("checkedCards");
            score = savedInstanceState.getInt("score");
            level = savedInstanceState.getInt("level");
            textScore.setText("Score: " + score + "");
            textLevel.setText(level+"");
            createLevel(false);
        }
        else createLevel(true);
    }

    private void addImagesToArray(ImageView view) {
        imar[score] = view;
        score++;
    }

    private void setImages(){
        for (int i = 0 ;i<checkedArray.length;i++){
            if (checkedArray[i]){
                imar[i].setImageResource(array[i]);
                unSetClickListener(imar[i]);
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ico00:
                Log.d("Hell", "onClick:00 ");
                checkCard(array[0], ico00);
                break;
            case R.id.ico01:
                Log.d("Hell", "onClick:01 ");
                checkCard(array[1], ico01);
                break;
            case R.id.ico02:
                Log.d("Hell", "onClick:02 ");
                checkCard(array[2], ico02);
                break;
            case R.id.ico03:
                Log.d("Hell", "onClick:03 ");
                checkCard(array[3], ico03);
                break;
            case R.id.ico10:
                Log.d("Hell", "onClick:10 ");
                checkCard(array[4], ico10);
                break;
            case R.id.ico11:
                Log.d("Hell", "onClick:11 ");
                checkCard(array[5], ico11);
                break;
            case R.id.ico12:
                Log.d("Hell", "onClick:12 ");
                checkCard(array[6], ico12);
                break;
            case R.id.ico13:
                Log.d("Hell", "onClick:13 ");
                checkCard(array[7], ico13);
                break;
            case R.id.ico20:
                Log.d("Hell", "onClick:20 ");
                checkCard(array[8], ico20);
                break;
            case R.id.ico21:
                Log.d("Hell", "onClick:21 ");
                checkCard(array[9], ico21);
                break;
            case R.id.ico22:
                Log.d("Hell", "onClick:22 ");
                checkCard(array[10], ico22);
                break;
            case R.id.ico23:
                Log.d("Hell", "onClick:23 ");
                checkCard(array[11], ico23);
                break;
        }
    }

    private void createLevel(boolean isNew) {
        if (isNew)
        startGame();
        else {
            for (int i = 0; i < imar.length; i++) {
                imar[i].setOnClickListener(this);
            }
            setImages();
        }

        timer = new Timer();
        timerTask = new myTimerTask();
        timer.schedule(timerTask, 1000, 1000);
    }

    private void startGame() {
        checkedCards = 0;
        level++;
        textLevel.setText("Level: " + level + "");
        for (int i = 0; i < imar.length; i++) {
            imar[i].setImageResource(R.drawable.icon);
            imar[i].setOnClickListener(this);
        }
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        array = fullRandomArray(currentImageArray(array, 5));// массив с рандомными id картинок
    }

    private int[] currentImageArray(int array[], int imgCount) {
        int circle = 0;
        for (int i = 0; i < array.length; i++) {
            if (circle < 2) {
                if (array[i] >= imgCount) array[i] -= imgCount;
                if (array[i] == 0) array[i] = R.drawable.kiwi;
                else if (array[i] == 1) array[i] = R.drawable.lemon;
                else if (array[i] == 2) array[i] = R.drawable.orange;
                else if (array[i] == 3) array[i] = R.drawable.raspberries;
                else {
                    array[i] = R.drawable.watermelon;
                    circle++;
                }
            } else array[i] = R.drawable.orange;
        }
        return array;
    }// массив с Id картинок по очереди 1-4 1-4 3.3

    private int[] fullRandomArray(int array[]) {

        List<Integer> list = new ArrayList<>();
        for (int i : array) {
            list.add(i);
        }
        Collections.shuffle(list);
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    } // рандомит массив

    private void checkCard(int id, ImageView view) {
        view.setImageResource(id);

        if (checked == false) {
            cards[0] = id;
            checked = true;
            firstImage = view;
            unSetClickListener(firstImage);
        } else if (checked == true) {
            cards[1] = id;
            checked = false;
            if (cards[0] != cards[1]) {
                setClickListener(firstImage);
                firstImage.setImageResource(R.drawable.icon);
                view.setImageResource(R.drawable.icon);
            } else {
                firstImage.setTag(id);
                view.setTag(id);
                upScore();
                unSetClickListener(view);
                checkedCards++;
                if (checkedCards >= picturCount / 2) {
                    createLevel(true);
//                    for (int i = 0;i<checkedArray.length;i++)
//                        checkedArray[i]=false;
                    start = true;
                }
            }
        }

    } // сравнивает id картинок

    private void setClickListener(ImageView view) {
        view.setOnClickListener(this);
    }

    private void unSetClickListener(ImageView view1) {
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("unsetClickListener", "onClick: " + v.getId());
            }
        });
    }

    private void upScore() {

        score += 100 * level;
        textScore.setText("Score: " + score + "");
        textLevel.setText(level+"");
    }

    private class myTimerTask extends TimerTask {
        @Override
        public void run() {
            time++;
            if (start == true) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "U did it from " + time + " seconds, GREAT!", Toast.LENGTH_SHORT).show();
                        time = 0;
                        start = false;
                    }

                });
            }
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveArray();
        outState.putString(IMAGES_STATE_KEY, "saved");
        outState.putBooleanArray("check_arr",checkedArray);
        outState.putIntArray("images_id", array);
        outState.putInt("checkedCards",checkedCards);
        outState.putInt("score",score);
        outState.putInt("level",level);
    }

    private void saveArray(){
        boolean correct=false;
        for (int j=0;j<picturCount;j++) {
            if (imar[j].getTag() == null) {
                correct=true;
            }
        }
        if (correct)
        for (int i = 0 ; i<picturCount;i++){
            if (imar[i].getTag()!=null){
                checkedArray[i]=true;
            }
        }

    }
}