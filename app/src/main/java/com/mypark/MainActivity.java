package com.mypark;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.Places;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Places.initialize(getApplicationContext(), "AIzaSyBSdsDNJFv_tED6OqLUNyyvReBcOKKqY1o");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(MainActivity.this, FirebaseAuth.getInstance().getCurrentUser() != null ? HomeActivity.class : LoginActivity.class);
                startActivity(homeIntent);
                finish();

            }
        }, 1500);
    }
}