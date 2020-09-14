package com.mypark;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mypark.utilities.Defines;
import com.mypark.utilities.Utilites;

import java.util.HashMap;
import java.util.Map;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

public class RegisterActivity extends AppCompatActivity {

    EditText mFirstName, mLastName, mUserName, mPassword, mEmail, mVerifyEmail;
    TextView mLicsncesButton, mIdButton;
    Button mRegister;
    ImageButton mImageLicence;
    ImageButton mImageId;
    final static int LICSENCE_REQUEEST_CODE = 111;
    final static int ID_REQUEEST_CODE = 222;
    final String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA};

    //Firebase
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        initViews();
        Utilites.checkPerimssion(this, PERMISSIONS);
    }

    private void initViews() {
        mFirstName = findViewById(R.id.name_continer);
        mLastName = findViewById(R.id.femilyName_continer);
        mUserName = findViewById(R.id.register_container);
        mPassword = findViewById(R.id.container_password);
        mEmail = findViewById(R.id.container_email);
        mVerifyEmail = findViewById(R.id.container_verifyEmail);
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
        String userName = mUserName.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "Missing username / User name already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = mFirstName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Missing name", Toast.LENGTH_SHORT).show();
            return;
        }

        String lastName = mLastName.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Missing last name", Toast.LENGTH_SHORT).show();
            return;
        }

        String password = mPassword.getText().toString();
        if (!Utilites.isPasswordValid(password)) {
            Toast.makeText(this, "Password is not valid,Must contain digit and charcter", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = mEmail.getText().toString();
        if (!Utilites.isEmailValid(email)) {
            Toast.makeText(this, "Email is not valid/ Already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        String verifyEmail = mVerifyEmail.getText().toString();
        if (!TextUtils.equals(email, verifyEmail)) {
            Toast.makeText(this, "Email should be the same", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.equals((CharSequence) mImageLicence.getTag(), "Valid") && !TextUtils.equals((CharSequence) mImageLicence.getTag(), "Valid")) {
            Toast.makeText(this, "Empty ID/Licsence", Toast.LENGTH_SHORT).show();
            return;
        }
        signUpUserByFireBase(email, password);


    }

    private void signUpUserByFireBase(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveAllFields();
                            Intent homeIntent = new Intent(RegisterActivity.this, HomeActivity.class);
                            homeIntent.putExtra(Defines.Intent.KEY_INTENT_SOURCE, Defines.Intent.VALUE_INTENT_SOURCE_REGISTER);
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(homeIntent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast toast = Toast.makeText(RegisterActivity.this, "Email is already exists", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                });
    }

    private void saveAllFields() {
        Map<String, Object> mDetailsMap = new HashMap<>();
        mDetailsMap.put(Defines.TableFields.USERS_FAMILY_NAME, mLastName.getText().toString());
        mDetailsMap.put(Defines.TableFields.USERS_USERNAME, mUserName.getText().toString());
        mDetailsMap.put(Defines.TableFields.USERS_FIRST_NAME, mFirstName.getText().toString());
        mDetailsMap.put(Defines.TableFields.USERS_ID_PIC, Utilites.convert(((BitmapDrawable) mImageId.getDrawable()).getBitmap()));
        mDetailsMap.put(Defines.TableFields.USERS_LICENSE_PIC, Utilites.convert(((BitmapDrawable) mImageLicence.getDrawable()).getBitmap()));
        mDatabase.child(Defines.TableNames.USERS).child(mAuth.getCurrentUser().getUid()).setValue(mDetailsMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    Toast.makeText(RegisterActivity.this, "Error when saving the details", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
}