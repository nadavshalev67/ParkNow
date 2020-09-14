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


import java.util.Calendar;

public class SearchParkingFragment extends Fragment implements OnMapReadyCallback {

    private View mFragment;
    private GoogleMap mGoogleMap;
    private ImageView mStartTimeButton, mFinishTimeButton, mCalander;
    private EditText mDateDisplay, mStartTimeDisplay, mFinishTimeDisplay;
    private ActivitytFragmentListener mListener;
    private Calendar mCal = Calendar.getInstance();
    private String mTimeChoose;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String date = String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);
            mDateDisplay.setText(date);
        }
    };

    private TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = String.format("%02d", hourOfDay) + ":00";
            if (TextUtils.equals(mTimeChoose, "Start")) {
                mStartTimeDisplay.setText(time);
            } else {
                int startTime = Integer.valueOf(mStartTimeDisplay.getText().toString().substring(0, 2));
                if (hourOfDay <= startTime) {
                    Toast.makeText(getContext(), "Please choose valid hour", Toast.LENGTH_SHORT).show();
                    return;
                }
                mFinishTimeDisplay.setText(time);
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        mListener = (ActivitytFragmentListener) getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = inflater.inflate(R.layout.search_parking, container, false);
        initViews();
        return mFragment;
    }

    private void initViews() {
        initTimers();
        initMaps();
        initCalender();
    }

    private void initMaps() {
        if (mGoogleMap == null) {
            SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFrag.getMapAsync(this);
        }
    }

    private void initCalender() {
        mDateDisplay = mFragment.findViewById(R.id.date_display);
        mCalander = mFragment.findViewById(R.id.calander_bth);
        mCalander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), mOnDateSetListener, mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH), mCal.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });
    }

    private void initTimers() {
        mStartTimeDisplay = mFragment.findViewById(R.id.time_dispkay);
        mStartTimeButton = mFragment.findViewById(R.id.clock_bth);
        mStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeChoose = "Start";
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), mOnTimeSetListener, mCal.get(Calendar.HOUR_OF_DAY), mCal.get(Calendar.MINUTE), DateFormat.is24HourFormat(getContext()));
                timePickerDialog.show();
            }
        });
        mFinishTimeDisplay = mFragment.findViewById(R.id.time_dispkay_finish);
        mFinishTimeButton = mFragment.findViewById(R.id.clock_bth_finish);
        mFinishTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mStartTimeDisplay.getText())) {
                    Toast.makeText(getContext(), "Please choose start time first", Toast.LENGTH_SHORT).show();
                    return;
                }
                mTimeChoose = "Finish";
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), mOnTimeSetListener, mCal.get(Calendar.HOUR_OF_DAY), mCal.get(Calendar.MINUTE), DateFormat.is24HourFormat(getContext()));
                timePickerDialog.show();
            }
        });
    }


    public void setListener(ActivitytFragmentListener activitytFragmentListener) {
        mListener = activitytFragmentListener;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        LatLng blabla = new LatLng(32.083010, 34.779690);
        mGoogleMap.addMarker(new MarkerOptions().position(blabla).snippet("asdadsadas").title("nadav"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(blabla));
    }
}
