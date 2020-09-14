package com.mypark.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mypark.R;

public class HomeFragment extends Fragment {

    private View mFragment;
    private Button mSearchButton, mCreateButton;
    private ActivitytFragmentListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.home_fragment, container, false);
        mCreateButton = mFragment.findViewById(R.id.createParking);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCreateNewParakingClicked();
            }
        });
        mSearchButton = mFragment.findViewById(R.id.searchParking);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSearchParkingClicked();
            }
        });
        return mFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        mListener = (ActivitytFragmentListener) getContext();
    }
}
