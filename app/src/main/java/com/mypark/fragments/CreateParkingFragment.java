package com.mypark.fragments;


import android.content.res.Configuration;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.mypark.R;
import com.mypark.utilities.Utilites;
import com.mypark.utilities.WorkaroundMapFragment;

import java.util.Arrays;
import java.util.Locale;


public class CreateParkingFragment extends Fragment implements OnMapReadyCallback {

    private View mFragment;
    private ActivitytFragmentListener mListener;
    private GoogleMap mGoogleMap;
    private ImageView mStartTimeButton, mFinishTimeButton, mCalander;
    private EditText mDateDisplay, mStartTimeDisplay, mFinishTimeDisplay, mAdress;
    private LatLng tlvLocation = new LatLng(32.083010, 34.779690);
    private LatLng mLastChosenLocation = null;
    private AutocompleteSupportFragment mAutocompleteSupportFragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.create_parking_fragment, container, false);
        initDateAndTime();
        Utilites.initGoogleMaps(mGoogleMap, (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map), this);
        initGoogleSearchBar();
        return mFragment;
    }


    @Override
    public void onStart() {
        super.onStart();
        mListener = (ActivitytFragmentListener) getContext();
    }

    private void initDateAndTime() {
        mDateDisplay = mFragment.findViewById(R.id.date_display);
        mStartTimeDisplay = mFragment.findViewById(R.id.time_dispkay);
        mStartTimeButton = mFragment.findViewById(R.id.clock_bth);
        mFinishTimeDisplay = mFragment.findViewById(R.id.time_dispkay_finish);
        mFinishTimeButton = mFragment.findViewById(R.id.clock_bth_finish);
        mStartTimeDisplay.setKeyListener(null);
        mFinishTimeDisplay.setKeyListener(null);
        Utilites.setCalendearButton(mFragment.findViewById(R.id.calander_bth), getContext(), mDateDisplay);
        Utilites.setTimes(getContext(), mStartTimeButton, mStartTimeDisplay, mFinishTimeButton, mFinishTimeDisplay);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mGoogleMap == null) {
            mGoogleMap = googleMap;
            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                    mLastChosenLocation = point;
                    mGoogleMap.clear();
                    Log.d("nadav", Utilites.getCompleteAddressString(getContext(), point.latitude, point.longitude));
                    mGoogleMap.addMarker(new MarkerOptions().position(point));
                    mAutocompleteSupportFragment.setText(Utilites.getCompleteAddressString(getContext(), point.latitude, point.longitude));
                }
            });
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tlvLocation, 10));
            final ScrollView mScrollView = mFragment.findViewById(R.id.scrollview);
            ((WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                    .setListener(new WorkaroundMapFragment.OnTouchListener() {
                        @Override
                        public void onTouch() {
                            mScrollView.requestDisallowInterceptTouchEvent(true);
                        }
                    });
        }
    }

    private void initGoogleSearchBar() {
        mAutocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        mAutocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        mAutocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng point = place.getLatLng();
                Log.d("nadav", Utilites.getCompleteAddressString(getContext(), point.latitude, point.longitude));
                mGoogleMap.addMarker(new MarkerOptions().position(point));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 30));
            }


            @Override
            public void onError(Status status) {
                Log.d("nadav", status.toString());
            }
        });
    }
}
