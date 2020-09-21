package com.mypark.fragments;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mypark.R;
import com.mypark.utilities.Defines;
import com.mypark.utilities.Utilites;
import com.mypark.utilities.WorkaroundMapFragment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class CreateParkingFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private View mFragment;
    private Button mCreateButton;
    private ActivitytFragmentListener mListener;
    private GoogleMap mGoogleMap;
    private EditText mStartTimeDisplay, mFinishTimeDisplay, mPrice;
    private LatLng mLastChosenLocation = null;
    private AutocompleteSupportFragment mAutocompleteSupportFragment;

    //Firebase
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.create_parking_fragment, container, false);
        mPrice = mFragment.findViewById(R.id.price_edit_text);
        initTimers();
        initCreateButton();
        Utilites.initGoogleMaps(mGoogleMap, (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map), this);
        initGoogleSearchBar();
        return mFragment;
    }

    private void initCreateButton() {
        mCreateButton = mFragment.findViewById(R.id.park_create);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mPrice.getText().toString()) || TextUtils.isEmpty(mStartTimeDisplay.getText().toString()) || TextUtils.isEmpty(mFinishTimeDisplay.getText().toString()) || mLastChosenLocation == null) {
                    Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                final Map<String, Object> mDetailsMap = new HashMap<>();
                mDetailsMap.put(Defines.TableFields.PARKING_CREATE_START_TIME, mStartTimeDisplay.getText().toString());
                mDetailsMap.put(Defines.TableFields.PARKING_CREATE_FINISH_TIME, mFinishTimeDisplay.getText().toString());
                mDetailsMap.put(Defines.TableFields.PARKING_CREATE_LOCATION_LAT, mLastChosenLocation.latitude);
                mDetailsMap.put(Defines.TableFields.PARKING_CREATE_LOCATION_LNG, mLastChosenLocation.longitude);
                mDetailsMap.put(Defines.TableFields.PARKING_CREATE_PRICE, mPrice.getText().toString());
                mDatabase.child(Defines.TableNames.PARKING_CREATE).child(mAuth.getCurrentUser().getUid()).child(String.valueOf(mLastChosenLocation.hashCode())).setValue(mDetailsMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error != null) {
                            Toast.makeText(getContext(), "Error when saving the details", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(getContext(), "Parking created", Toast.LENGTH_SHORT).show();
                            mListener.onParkingCreated();
                        }
                    }
                });

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        mListener = (ActivitytFragmentListener) getContext();
    }

    private void initTimers() {
        mStartTimeDisplay = mFragment.findViewById(R.id.time_dispkay);
        mFinishTimeDisplay = mFragment.findViewById(R.id.time_dispkay_finish);
        Utilites.setTimes(getContext(), (ImageView) mFragment.findViewById(R.id.clock_bth), mStartTimeDisplay, (ImageView) mFragment.findViewById(R.id.clock_bth_finish), mFinishTimeDisplay);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mGoogleMap == null) {
            mGoogleMap = googleMap;
            Utilites.setDefaultSettingToGogleMaps(googleMap, (WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.map), (ScrollView) mFragment.findViewById(R.id.scrollview), this);
        }
    }

    private void initGoogleSearchBar() {
        mAutocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        mAutocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        mAutocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng point = place.getLatLng();
                mLastChosenLocation = point;
                mGoogleMap.clear();
                mGoogleMap.addMarker(new MarkerOptions().position(point));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 20));
            }

            @Override
            public void onError(Status status) {
            }
        });
    }

    @Override
    public void onMapClick(LatLng point) {
        mLastChosenLocation = point;
        mGoogleMap.clear();
        mGoogleMap.addMarker(new MarkerOptions().position(point));
        mAutocompleteSupportFragment.setText(Utilites.getCompleteAddressString(getContext(), point.latitude, point.longitude));
    }

}
