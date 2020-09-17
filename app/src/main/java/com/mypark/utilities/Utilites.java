package com.mypark.utilities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.mypark.R;
import com.mypark.fragments.SearchParkingFragment;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilites {


    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPasswordValid(String password) {
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        if (password.length() < 6) {
            return false;
        }
        if (password.matches(".*[a-zA-Z]+.*") && password.matches(".*[0-9]+.*")) {
            return true;
        }
        return false;
    }

    public static void checkPerimssion(Activity activity, String[] permiision) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, permiision, 999);
        }
    }

    public static Bitmap convert(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT
        );
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String convert(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static void setCalendearButton(View calnderView, final Context context, final EditText editTextDate) {
        calnderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCal = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextDate.setText(String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%02d", dayOfMonth));
                    }
                }, mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH), mCal.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });
    }


    public static void setTimes(final Context context, ImageView mStartTimeButton, final EditText mStartTimeDisplay, ImageView mFinishTimeButton, final EditText mFinishTimeDisplay) {
        final AtomicBoolean isStart = new AtomicBoolean(false);
        final Calendar cal = Calendar.getInstance();
        final TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = String.format("%02d", hourOfDay) + ":00";
                if (isStart.get()) {
                    mStartTimeDisplay.setText(time);
                } else {
                    int startTime = Integer.valueOf(mStartTimeDisplay.getText().toString().substring(0, 2));
                    if (hourOfDay <= startTime) {
                        Toast.makeText(context, "Please choose valid hour", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mFinishTimeDisplay.setText(time);
                }
            }
        };
        mStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(context, onTimeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), DateFormat.is24HourFormat(context));
                isStart.set(true);
                timePickerDialog.show();
            }
        });
        mFinishTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mStartTimeDisplay.getText())) {
                    Toast.makeText(context, "Please choose start time first", Toast.LENGTH_SHORT).show();
                    return;
                }
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, onTimeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), DateFormat.is24HourFormat(context));
                isStart.set(false);
                timePickerDialog.show();
            }
        });

    }

    public static void initGoogleMaps(GoogleMap mGoogleMap, SupportMapFragment mapFragment, OnMapReadyCallback readyCallback) {
        if (mGoogleMap == null) {
            mapFragment.getMapAsync(readyCallback);
        }
    }


    public static String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }


}
