package com.example.demolocation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();
    // Used to link Location activity with this activity
    public static final int NEW_LOCATION_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLocationActivity();
            }
        });
    }

    private void openLocationActivity() {

        Intent intent = new Intent(this, LocationActivity.class);
        startActivityForResult(intent, NEW_LOCATION_REQUEST);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_LOCATION_REQUEST) { // Identify activity
            if (resultCode == RESULT_OK) {
                // Activity succeeded
                double latitude = data.getDoubleExtra(LocationActivity.EXTRA_REPLY_LATITUDE, 0);
                double longitude = data.getDoubleExtra(LocationActivity.EXTRA_REPLY_LONGITUDE, 0);
                Log.d(LOG_TAG, "Location Activity Returned " + latitude + ", " + longitude);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}