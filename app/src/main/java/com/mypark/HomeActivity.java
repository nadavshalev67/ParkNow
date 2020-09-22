package com.mypark;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mypark.fragments.ActivitytFragmentListener;
import com.mypark.fragments.CreateParkingFragment;
import com.mypark.fragments.DetailsFragment;
import com.mypark.fragments.HomeFragment;
import com.mypark.fragments.SearchParkingFragment;
import com.mypark.utilities.Defines;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements ActivitytFragmentListener, NavigationView.OnNavigationItemSelectedListener {

    private TextView mWellcomeTextView;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        initNavnigationView();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, getIntent().getStringExtra(Defines.Intent.KEY_INTENT_SOURCE) != null ? new DetailsFragment() : new HomeFragment(), "details").commit();
    }

    private void initNavnigationView() {
        NavigationView navigationView = findViewById(R.id.navignation_bar);
        navigationView.setNavigationItemSelectedListener(this);
        mWellcomeTextView = navigationView.getHeaderView(0).findViewById(R.id.wellcome_text_view);
        mDatabase.child(Defines.TableNames.USERS).child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mWellcomeTextView.setText("Hello " + (String) ((HashMap) snapshot.getValue()).get(Defines.TableFields.USERS_USERNAME));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onGotItClicked() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(), "home").addToBackStack(null).commit();
    }

    @Override
    public void onParkingCreated() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(), "home").addToBackStack(null).commit();
    }

    @Override
    public void onParkingRented() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(), "home").addToBackStack(null).commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.home_menu: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(), "home").addToBackStack(null).commit();
                return true;
            }
            case R.id.search_menu: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchParkingFragment(), "search").addToBackStack(null).commit();
                return true;
            }
            case R.id.create_menu: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateParkingFragment(), "create").addToBackStack(null).commit();
                return true;
            }

            case R.id.logout_menu: {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
                return true;
            }
        }
        return false;

    }
}