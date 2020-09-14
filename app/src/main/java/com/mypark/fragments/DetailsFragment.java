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

public class DetailsFragment extends Fragment {

    private View mFragment;
    private Button mGotItButton;
    private ActivitytFragmentListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.details_fragment, container, false);
        mGotItButton = mFragment.findViewById(R.id.buttonGotIt);
        mGotItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGotItClicked();
            }
        });
        return mFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        mListener = (ActivitytFragmentListener) getContext();
    }

    public void setListener(ActivitytFragmentListener activitytFragmentListener) {
        mListener = activitytFragmentListener;
    }
}
