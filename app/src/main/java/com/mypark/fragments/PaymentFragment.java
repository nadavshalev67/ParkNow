package com.mypark.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mypark.R;


public class PaymentFragment extends Fragment {

    private View mFragment;
    private ActivitytFragmentListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.payment_fragment, container, false);
        return mFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        mListener = (ActivitytFragmentListener) getContext();

    }

}
