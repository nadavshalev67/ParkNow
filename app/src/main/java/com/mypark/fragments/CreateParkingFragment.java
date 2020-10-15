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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mypark.R;
import com.mypark.network.RetrofitInst;
import com.mypark.utilities.Defines;
import com.mypark.utilities.Utilites;
import com.mypark.utilities.WorkaroundMapFragment;

import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateParkingFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private View mFragment;
    private Button mCreateButton;
    private ActivitytFragmentListener mListener;
    private GoogleMap mGoogleMap;
    private EditText mStartTimeDisplay, mFinishTimeDisplay, mPrice;
    private LatLng mLastChosenLocation = null;
    private AutocompleteSupportFragment mAutocompleteSupportFragment;
    private String mUserName;
    //Firebase
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.create_parking_fragment, container, false);
        mPrice = mFragment.findViewById(R.id.price_edit_text);
        getUserName();
        initTimers();
        initCreateButton();
        Utilites.initGoogleMaps(mGoogleMap, (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map), this);
        initGoogleSearchBar();
        return mFragment;
    }

    private void getUserName() {
        mDatabase.child(Defines.TableNames.USERS).child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUserName = (String) ((HashMap) snapshot.getValue()).get(Defines.TableFields.USERS_USERNAME);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                mCreateButton.setEnabled(false);
                final HashMap<String, Object> detailsMap = new HashMap<>();
                detailsMap.put("start_time", Integer.valueOf(mStartTimeDisplay.getText().toString().substring(0, 2)));
                detailsMap.put("finish_time", Integer.valueOf(mFinishTimeDisplay.getText().toString().substring(0, 2)));
                detailsMap.put("lat", mLastChosenLocation.latitude);
                detailsMap.put("lng", mLastChosenLocation.longitude);
                detailsMap.put("price", mPrice.getText().toString());
                detailsMap.put("user_name", mUserName);
                detailsMap.put("uuid", mAuth.getCurrentUser().getUid());
                detailsMap.put("adress", Utilites.getCompleteAddressString(getContext(), mLastChosenLocation.latitude, mLastChosenLocation.longitude));
                Call<Void> createCall = RetrofitInst.getInstance().executeCreateNewSpot(detailsMap);
                createCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getContext(), "Parking created", Toast.LENGTH_SHORT).show();
                        mListener.onParkingCreated();
                        mCreateButton.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        mCreateButton.setEnabled(true);
                        Toast.makeText(getContext(), "Error when saving the details", Toast.LENGTH_SHORT).show();
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
