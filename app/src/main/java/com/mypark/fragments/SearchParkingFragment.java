package com.mypark.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mypark.R;
import com.mypark.utilities.Utilites;


import java.util.Calendar;

public class SearchParkingFragment extends Fragment implements OnMapReadyCallback {

    private View mFragment;
    private GoogleMap mGoogleMap;
    private ImageView mStartTimeButton, mFinishTimeButton;
    private EditText mDateDisplay, mStartTimeDisplay, mFinishTimeDisplay;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.search_parking, container, false);
        initViews();

        return mFragment;
    }

    private void initViews() {
        initDateAndTime();
        Utilites.initGoogleMaps(mGoogleMap, (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map), this);
    }


    private void initDateAndTime() {
        mDateDisplay = mFragment.findViewById(R.id.date_display);
        mStartTimeDisplay = mFragment.findViewById(R.id.time_dispkay);
        mStartTimeButton = mFragment.findViewById(R.id.clock_bth);
        mFinishTimeDisplay = mFragment.findViewById(R.id.time_dispkay_finish);
        mFinishTimeButton = mFragment.findViewById(R.id.clock_bth_finish);
        Utilites.setCalendearButton(mFragment.findViewById(R.id.calander_bth), getContext(), mDateDisplay);
        Utilites.setTimes(getContext(), mStartTimeButton, mStartTimeDisplay, mFinishTimeButton, mFinishTimeDisplay);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        LatLng blabla = new LatLng(32.083010, 34.779690);
        //mGoogleMap.addMarker(new MarkerOptions().position(blabla).snippet("asdadsadas").title("nadav"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(blabla, 10));
    }
}
