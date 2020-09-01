package com.mypark;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mypark.utilities.Utilites;

public class SignInActivity extends AppCompatActivity {

    EditText mUserName, mPassword;
    Button mConnectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);
        initViews();
    }

    private void initViews() {
        mUserName = findViewById(R.id.username_enter_txt);
        mPassword = findViewById(R.id.psw_enter_txt);
        mConnectButton = findViewById(R.id.reg_buttoun);
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyLoginDetails();
            }
        });
    }

    private void verifyLoginDetails() {
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Utilites.isPasswordValid(password)) {
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
            return;
        }
        ///
        if (userExistsInDB()) {
            Intent homeIntent = new Intent(SignInActivity.this, HomeActivity.class);
            startActivity(homeIntent);
            finish();
        }


    }

    private boolean userExistsInDB() {
        return true;
        //Make query to the db in order to take Username + password and it should be equal!!!!! to mUserName + mPassword
    }
}