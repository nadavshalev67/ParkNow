package com.mypark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.mypark.fragments.ActivitytFragmentListener;
import com.mypark.fragments.CreateParkingFragment;
import com.mypark.fragments.DetailsFragment;
import com.mypark.fragments.HomeFragment;
import com.mypark.fragments.SearchParkingFragment;
import com.mypark.utilities.Defines;

public class HomeActivity extends AppCompatActivity implements ActivitytFragmentListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        findViewById(R.id.log_out_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, getIntent().getStringExtra(Defines.Intent.KEY_INTENT_SOURCE) != null ? new DetailsFragment() : new HomeFragment(), "details").commit();
    }

    @Override
    public void onGotItClicked() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(), "home").addToBackStack(null).commit();
    }

    @Override
    public void onSearchParkingClicked() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchParkingFragment(), "search").addToBackStack(null).commit();
    }

    @Override
    public void onCreateNewParakingClicked() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateParkingFragment(), "create").addToBackStack(null).commit();
    }


}