package com.mypark;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mypark.utilities.Utilites;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

public class RegisterActivity extends AppCompatActivity {

    EditText mName, mLastName, mUserName, mPassword, mEmail, mVerifyEmail;
    TextView mLicsncesButton, mIdButton;
    Button mRegister;
    ImageButton mImageLicence;
    ImageButton mImageId;
    final static int LICSENCE_REQUEEST_CODE = 111;
    final static int ID_REQUEEST_CODE = 222;
    final String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        initViews();
        Utilites.checkPerimssion(this, PERMISSIONS);
    }

    private void initViews() {
        mName = findViewById(R.id.name_continer);
        mName.setText("Nadav");
        mLastName = findViewById(R.id.femilyName_continer);
        mLastName.setText("Nadav");
        mUserName = findViewById(R.id.register_container);
        mUserName.setText("Nadav");
        mPassword = findViewById(R.id.container_password);
        mPassword.setText("asdas1sad2");
        mEmail = findViewById(R.id.container_email);
        mEmail.setText("asda@walla.com");
        mVerifyEmail = findViewById(R.id.container_verifyEmail);
        mVerifyEmail.setText("asda@walla.com");
        mLicsncesButton = findViewById(R.id.license_text);
        mLicsncesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, LICSENCE_REQUEEST_CODE);
            }
        });
        mIdButton = findViewById(R.id.id_text);
        mIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, ID_REQUEEST_CODE);
            }
        });
        mRegister = findViewById(R.id.b_register);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyAllEditText();
            }
        });
        mImageId = findViewById(R.id.image_id);
        mImageLicence = findViewById(R.id.image_liceane);

    }


    private void verifyAllEditText() {
//        String userName = mUserName.getText().toString();
//        if (TextUtils.isEmpty(userName) || isUserAlreadyRegistered()) {
//            Toast.makeText(this, "Missing username / User name already exists", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String name = mName.getText().toString();
//        if (TextUtils.isEmpty(name)) {
//            Toast.makeText(this, "Missing name", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String lastName = mLastName.getText().toString();
//        if (TextUtils.isEmpty(lastName)) {
//            Toast.makeText(this, "Missing last name", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String password = mPassword.getText().toString();
//        if (!Utilites.isPasswordValid(password)) {
//            Toast.makeText(this, "Password is not valid,Must contain digit and charcter", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String email = mEmail.getText().toString();
//        if (!Utilites.isEmailValid(email)) {
//            Toast.makeText(this, "Email is not valid/ Already exists", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String verifyEmail = mVerifyEmail.getText().toString();
//        if (!TextUtils.equals(email, verifyEmail) || isEmailAlreadyExists()) {
//            Toast.makeText(this, "Email should be the same", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!TextUtils.equals((CharSequence) mImageLicence.getTag(), "Valid") && !TextUtils.equals((CharSequence) mImageLicence.getTag(), "Valid")) {
//            Toast.makeText(this, "Empty ID/Licsence", Toast.LENGTH_SHORT).show();
//            return;
//        }


        Intent homeIntent = new Intent(RegisterActivity.this, HomeActivity.class);
        startActivity(homeIntent);
        finish();

    }


    private boolean isUserAlreadyRegistered() {
        //Here goes the DB extraction username
        return false;
    }

    private boolean isEmailAlreadyExists() {
        //Here goes the DB extraction username
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LICSENCE_REQUEEST_CODE: {
                if (data != null && data.getExtras() != null) {
                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    mImageLicence.setImageBitmap(image);
                    mImageLicence.setTag("Valid");
                }
                break;
            }
            case ID_REQUEEST_CODE: {
                if (data != null && data.getExtras() != null) {
                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    mImageId.setImageBitmap(image);
                    mImageId.setTag("Valid");
                }
                break;
            }


        }

    }

    //to convert from image to base64->////Utilites.convert(((BitmapDrawable) mImageLicence.getDrawable()).getBitmap())
}