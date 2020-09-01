package com.mypark;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.mypark.fragments.ActivitytFragmentListener;
import com.mypark.fragments.DetailsFragment;
import com.mypark.fragments.HomeFragment;
import com.mypark.fragments.SearchParkingFragment;

public class HomeActivity extends AppCompatActivity implements ActivitytFragmentListener {

    private DetailsFragment mDetailsFragment = new DetailsFragment();
    private HomeFragment mHomeFragment = new HomeFragment();
    private SearchParkingFragment mSearchParkingFragment = new SearchParkingFragment();
    private FragmentManager mFragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        initFragments();
    }

    private void initFragments() {
        mSearchParkingFragment.setListener(this);
        mHomeFragment.setListener(this);
        mDetailsFragment.setListener(this);
        mFragmentManager.beginTransaction().replace(R.id.fragment_container, mDetailsFragment, "details").commit();
    }

    @Override
    public void onGotItClicked() {
        mFragmentManager.beginTransaction().replace(R.id.fragment_container, mHomeFragment, "home").commit();
    }

    @Override
    public void onSearchParkingClicked() {
        mFragmentManager.beginTransaction().replace(R.id.fragment_container, mSearchParkingFragment, "search").commit();
    }

    @Override
    public void onCreateNewParakingClicked() {

    }

}