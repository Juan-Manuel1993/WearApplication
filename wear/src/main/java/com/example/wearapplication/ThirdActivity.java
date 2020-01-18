package com.example.wearapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.tbouron.shakedetector.library.ShakeDetector;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ThirdActivity extends AppCompatActivity {

    private Button closeapp;

    private  Button getfromserver;


    private final SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);

    private String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


    private TextView time;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);


        time = (TextView) findViewById(R.id.time);

        closeapp = (Button) findViewById(R.id.closeapp);

        getfromserver = (Button) findViewById(R.id.getfromserver);





        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                time.setText(sDateFormat.format(new Date()));
               // time.setText(currentDateTimeString);
                someHandler.postDelayed(this, 1000);
            }
        }, 10);


        closeapp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                System.exit(0);
            }
        });


        getfromserver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                openActivity();
            }
        });

    }


    public void openActivity(){
        Intent intent = new Intent(this,Second.class);
        startActivity(intent);
    }






    @Override
    protected void onResume() {
        Log.i("","Onresume()");
        super.onResume();

        ShakeDetector.start();
    }

    @Override
    protected void onStop() {
        Log.i("","Stopped");
        super.onStop();

        ShakeDetector.stop();
    }

    protected void onPause() {
        Log.d("", "onPause()");
        super.onPause();

    }





}
