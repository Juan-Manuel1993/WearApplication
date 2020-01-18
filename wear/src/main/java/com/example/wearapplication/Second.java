package com.example.wearapplication;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.CardScrollView;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.wear.ambient.AmbientModeSupport;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.tbouron.shakedetector.library.ShakeDetector;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Second extends AppCompatActivity{

    private android.widget.TextView TextView;

    private Timer timer;

    private ConnectivityManager connManager;

    private NetworkInfo mWifi;

    String url;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_main);
        CardScrollView cardScrollView = (CardScrollView) findViewById(R.id.card_scroll_view);
        cardScrollView.setCardGravity(Gravity.BOTTOM);

        TextView = (TextView) findViewById(R.id.text);

        timer = new Timer();



        url = "https://hmin309-embedded-systems.herokuapp.com/message-exchange/messages/";

        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        // Enables Always-on
       // setAmbientEnabled();


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
        LogOutTimerTask logoutTimeTask = new LogOutTimerTask();
        timer.schedule(logoutTimeTask, 7000);
        super.onPause();

    }

    protected void onStart() {
        super.onStart();
        if (mWifi.isConnected()) {

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);


            // Request a string response from the provided URL.


            JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {


                    TextView.setText("Response From Server: " + response.toString());



                }

            }, new Response.ErrorListener() {


                @Override
                public void onErrorResponse(VolleyError error) {
                    TextView.setText("Try again! maybe connexion doesn't work well! It works always the second time :)");
                    Log.e("", error.toString());
                }
            });

            // Add the request to the RequestQueue.
            queue.add(jsonRequest);



        }

    }



    private class LogOutTimerTask extends TimerTask {

        @Override
        public void run() {

            //redirect user to login screen
            Intent i = new Intent(Second.this, ThirdActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
    }


}









