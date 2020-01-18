package com.example.wearapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.wear.ambient.AmbientModeSupport;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.tbouron.shakedetector.library.ShakeDetector;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity  {


    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private Boolean checkbluetooth;

    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private double Latitude;
    private double Longitude;

    private Map<String,String> jsonobject;
    private TextView text;



    private final SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);


    private Button But;
    private  Button close;

    private GoogleApiClient client;
    private static final long CONNECTION_TIME_OUT_MS = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("","Created");
        setContentView(R.layout.activity_main);


        text = (TextView) findViewById(R.id.text);

        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);

        jsonobject = new HashMap<String,String>();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        But = (Button) findViewById(R.id.Buton);




    /*    if (checkbluetooth.equals(false)){


        }
        */

        sendgps();

        close = (Button) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                System.exit(0);
            }
        });
    }



    public void sendgps() {
        if (!checkPermissions()) {
            requestPermissions();
        } else {

            getLastLocation();


            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            String url = "https://hmin309-embedded-systems.herokuapp.com/message-exchange/messages/";


            if (mWifi.isConnected()) {
                Log.i("", "Connected");

                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(this);

                JsonObjectRequest jsonpost = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonobject), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("Response", response.toString());
                        text.setText(response.toString());

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", error.toString());

                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {

                        jsonobject.put("Content-Type", "application/json; charset=utf-8");

                        return jsonobject;
                    }
                };
                queue.add(jsonpost);


            }

        }
    }

    public void activityHeart(){

                But.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("", "Clicked");
                        openActivity();
                    }
                });

                ShakeDetector.create(this, new ShakeDetector.OnShakeListener() {
                    @Override
                    public void OnShake() {
                        Toast.makeText(getApplicationContext(), "Device shaken!", Toast.LENGTH_SHORT).show();
                        openActivity();
                    }
                });


            }





    public void openActivity(){
        Intent intent = new Intent(this,Second.class);
        startActivity(intent);
    }







    @Override
    public void onStart() {
        Log.i("","Started");
        super.onStart();
        activityHeart();
    }





        /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     * <p>
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            Latitude = mLastLocation.getLatitude();
                            Longitude = mLastLocation.getLongitude();
                            String latitude = String.valueOf(Latitude);
                            String longitude = String.valueOf(Longitude);
                            jsonobject.put("id" , "21510339");
                            jsonobject.put("student_id","2150987");
                            jsonobject.put("gps_lat",latitude);
                            jsonobject.put("gps_long",longitude);
                            jsonobject.put("student_message","This is my GPS COORDINATES : latitude and longitude");

                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                            showSnackbar(getString(R.string.no_location_detected));
                        }
                    }
                });
    }

    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {
        View container = findViewById(R.id.main_activity);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }



    @Override
    protected void onResume() {
        Log.i(TAG,"Onresume()");
        super.onResume();

        ShakeDetector.start();
    }


    @Override
    protected void onStop() {
        Log.i(TAG,"Stopped");
        super.onStop();
        ShakeDetector.stop();
    }

    protected void onPause() {
        Log.d(TAG, "onPause()");

        super.onPause();

    }

    @Override
    protected void onDestroy() {
        Log.i(TAG,"Destroy()");
        super.onDestroy();
       ShakeDetector.destroy();
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }



    public void checkIfWearableConnected() {

        retrieveDeviceNode(new Callback() {
            @Override
            public void success(String nodeId) {
                checkbluetooth = true;
            }

            @Override
            public void failed(String message) {
                checkbluetooth = false;
            }
        });


    }

    private GoogleApiClient getGoogleApiClient(Context context) {
        if (client == null)
            client = new GoogleApiClient.Builder(context)
                    .addApi(Wearable.API)
                    .build();
        return client;
    }

    private interface Callback {
        public void success(final String nodeId);
        public void failed(final String message);
    }

    private void retrieveDeviceNode(final Callback callback) {
        final GoogleApiClient client = getGoogleApiClient(this);
        new Thread(new Runnable() {

            @Override
            public void run() {
                client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(client).await();
                List<Node> nodes = result.getNodes();
                if (nodes.size() > 0) {
                    String nodeId = nodes.get(0).getId();
                    callback.success(nodeId);
                } else {
                    callback.failed("no wearables found");
                }
                client.disconnect();
            }
        }).start();
    }




    }



