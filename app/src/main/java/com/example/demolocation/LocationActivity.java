package com.example.demolocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class LocationActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY_LATITUDE = "com.example.demolocation.extra.LAT";
    public static final String EXTRA_REPLY_LONGITUDE = "com.example.demolocation.extra.LONG";
    // user as the value to link a request for location permission to the
    // onRequestPermissionsResult callback in this activity
    public static final int LOCATION_PERMISSION_ID = 100;

    // Fused location provider client does all the work of getting a location
    // relies on google play services SDK
    private FusedLocationProviderClient mFusedLocationClient;

    // Populated with the current location
    private double CurrentLat = 0;
    private double CurrentLng = 0;

    // Text box in the main activity
    private TextView locationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        // Update the text field on the activity
        locationText = findViewById(R.id.textLocation);
        locationText.setText("Turning on location updates");
        // Check for location being turned on on the phone if not ask for it.
        if(!isLocationEnabled()) promptUserToEnableLocationTracking();
        mFusedLocationClient = getFusedLocationProviderClient(this);
        requestLocationPermissions();

        FloatingActionButton fab = findViewById(R.id.fabAcceptLocation);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeActivity();
            }
        });

    }

    @SuppressLint("MissingPermission")
    public void getLastLocation(View view) {
        requestLocationPermissions();
    }

    public void closeActivity() {
        // Create an intent
        Intent replyIntent = new Intent();
        // Put the data to return into the extra
        replyIntent.putExtra(EXTRA_REPLY_LATITUDE, CurrentLat);
        replyIntent.putExtra(EXTRA_REPLY_LONGITUDE, CurrentLng);
        // Set the activity's result to RESULT_OK
        setResult(RESULT_OK, replyIntent);
        // Finish th is activity
        finish();
    }
    /**
     * Checks if either GPS or Network location is available
     * @return      Returns true if either service is enabled fl
     */
    private boolean isLocationEnabled()
    {
        LocationManager locationManager
                = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // Failed to create location manager
        assert locationManager != null : "Null location manager in private boolean isLocationEnabled()";

        return  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    //

    /**
     * Opens the location settings using an intent for user to turn on the service
     */
    private void promptUserToEnableLocationTracking()
    {
        Toast.makeText(this,"Please turn on your location..."
                , Toast.LENGTH_LONG).show();

        Intent intent
                = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    /* Android permissions methods */
    // method to check for permissions

    /**
     * Checks that permissions have been granted for fine and coarse location checking
     *
     * @return  boolean true if both permissions granted otherwise false
     */
    private boolean checkPermissions()
    {
        return  ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                &&

                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;

    }

    /**
     * Request the location permissions
     */
    @SuppressLint("MissingPermission")
    private void requestLocationPermissions()
    {
        if (!checkPermissions()) {
            ActivityCompat.requestPermissions(this
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                            , Manifest.permission.ACCESS_COARSE_LOCATION}
                    ,LOCATION_PERMISSION_ID);
        } else {

            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, this::update);
        }
        // The LOCATION_PERMISSION_ID is used by this activities onRequestPermissionsResult
        // to link the request to the callback (if implemented)
    }

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_PERMISSION_ID) {
            // If request is cancelled, the result arrays are empty.

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(this, this::update);
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     *
     * @param location
     */
    public void update(Location location){

        if (location != null) { // New location has now been determined
            String msg =
                    "Updated Location: "
                            + location.getLatitude()
                            + ","
                            + location.getLongitude();
            CurrentLat = location.getLatitude();
            CurrentLng = location.getLongitude();
            locationText.setText(msg);
        }
    }

}