package com.mypark;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mypark.fragments.ActivitytFragmentListener;
import com.mypark.fragments.ContactUsFragment;
import com.mypark.fragments.CreateParkingFragment;
import com.mypark.fragments.DetailsFragment;
import com.mypark.fragments.HomeFragment;
import com.mypark.fragments.PaymentFragment;
import com.mypark.fragments.SearchParkingFragment;
import com.mypark.utilities.Defines;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity implements ActivitytFragmentListener, NavigationView.OnNavigationItemSelectedListener {

    private TextView mWellcomeTextView;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        initNavnigationView();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, getIntent().getStringExtra(Defines.Intent.KEY_INTENT_SOURCE) != null ? new DetailsFragment() : new HomeFragment(), "details").commit();
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close) {
                @Override
                public void onDrawerClosed(View drawerView) {
                    supportInvalidateOptionsMenu();
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    supportInvalidateOptionsMenu();
                }
            };
        }
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


    }

    private void initNavnigationView() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navignation_bar);
        mNavigationView.setNavigationItemSelectedListener(this);
        mWellcomeTextView = mNavigationView.getHeaderView(0).findViewById(R.id.wellcome_text_view);
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
                break;
            }
            case R.id.search_menu: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchParkingFragment(), "search").addToBackStack(null).commit();
                break;
            }
            case R.id.create_menu: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreateParkingFragment(), "create").addToBackStack(null).commit();
                break;
            }

            case R.id.logout_menu: {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
                break;
            }
            case R.id.payment: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PaymentFragment(), "create").addToBackStack(null).commit();
                break;
            }
            case R.id.contact_us: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContactUsFragment(), "create").addToBackStack(null).commit();
                break;
            }
        }
        mDrawerLayout.closeDrawer(Gravity.LEFT, true);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


}