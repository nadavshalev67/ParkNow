package com.mypark.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mypark.R;
import com.mypark.models.AvaliableParking;
import com.mypark.models.Spot;
import com.mypark.network.RetrofitInterface;
import com.mypark.utilities.Defines;
import com.mypark.utilities.Utilites;
import com.mypark.utilities.WorkaroundMapFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchParkingFragment extends Fragment implements OnMapReadyCallback {


    private RetrofitInterface mRetrofitInterface;


    private View mFragment;
    private GoogleMap mGoogleMap;
    private EditText mStartTimeDisplay, mFinishTimeDisplay;
    private Button mSearchingButton, mCreateButton;
    private HashMap<String, String> usersKey = new HashMap<>();
    private ProgressBar mProgressBar;
    private ActivitytFragmentListener mListener;
    private boolean isValidSearch = false;
    private Marker mSelectedMarker = null;

    //Firebase
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.search_parking, container, false);

        initViews();
        return mFragment;
    }

    private void initViews() {
        initTime();
        initSearchButton();
        initCreateButton();
        mProgressBar = mFragment.findViewById(R.id.progress_bar);
        Utilites.initGoogleMaps(mGoogleMap, (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map), this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mListener = (ActivitytFragmentListener) getContext();
    }

    private void initCreateButton() {
        mCreateButton = mFragment.findViewById(R.id.park_serach_create);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedMarker != null) {
                    final Map<String, Object> mDetailsMap = new HashMap<>();
                    mDetailsMap.put(Defines.TableFields.PARKING_CREATE_START_TIME, mStartTimeDisplay.getText().toString());
                    mDetailsMap.put(Defines.TableFields.PARKING_CREATE_FINISH_TIME, mFinishTimeDisplay.getText().toString());
                    mDatabase.child(Defines.TableNames.PARKING_OCCUPIED).child((String) mSelectedMarker.getTag()).child(FirebaseAuth.getInstance().getUid()).setValue(mDetailsMap);
                    Toast.makeText(getContext(), "The place is rented for you", Toast.LENGTH_SHORT).show();
                    mListener.onParkingRented();

                } else {
                    Toast.makeText(getContext(), "Please choose valid place", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initSearchButton() {
        mSearchingButton = mFragment.findViewById(R.id.park_search);
        mSearchingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mStartTimeDisplay.getText().toString()) || TextUtils.isEmpty(mFinishTimeDisplay.getText().toString())) {
                    Toast.makeText(getContext(), "Choose valid hours", Toast.LENGTH_SHORT).show();
                    return;
                }
                isValidSearch = true;
                mProgressBar.setVisibility(View.VISIBLE);


            }
        });
    }


    private void initTime() {
        mStartTimeDisplay = mFragment.findViewById(R.id.time_dispkay);
        mFinishTimeDisplay = mFragment.findViewById(R.id.time_dispkay_finish);
        Utilites.setTimes(getContext(), (ImageView) mFragment.findViewById(R.id.clock_bth), mStartTimeDisplay, (ImageView) mFragment.findViewById(R.id.clock_bth_finish), mFinishTimeDisplay);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (isValidSearch) {
                    mSelectedMarker = marker;
                }
                return false;
            }
        });
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Defines.Location.tlvLocation, 10));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        showAllSpotsAvialable();
        ((WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).setListener(new WorkaroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                ((ScrollView) mFragment.findViewById(R.id.scrollview)).requestDisallowInterceptTouchEvent(true);
            }
        });

    }

    private void showAllSpotsAvialable() {



        Call<List<AvaliableParking>> call = mRetrofitInterface.executeGetAvaialbeParking(new HashMap<String, String>());
        call.enqueue(new Callback<List<AvaliableParking>>() {
            @Override
            public void onResponse(Call<List<AvaliableParking>> call, Response<List<AvaliableParking>> response) {
                if (response.code() == 200) {
                    List<AvaliableParking> list = response.body();
                    Toast.makeText(getContext(), " good", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "not good", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AvaliableParking>> call, Throwable t) {
                Toast.makeText(getContext(), "not good", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void onSpotsUpdated(List<Spot> spots) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mGoogleMap.clear();
        for (Spot spot : spots) {
            mGoogleMap.addMarker(new MarkerOptions().position(spot.getLatLng()).title(spot.getUserName()).snippet(String.format("Price : %d", spot.getPrice()))).setTag(spot.getHashCode());
        }
    }
}
