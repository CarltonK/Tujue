package com.example.tujue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;
import handler.Constants;

public class Register extends AppCompatActivity {
    private EditText email_edit, password_edit;
    private ImageView reg_button;

    //Firebase init
    private FirebaseAuth app_auth;
    private FirebaseUser current_user;

    //Key Identifiers
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getSupportActionBar().hide();

        //Proper init
        app_auth = FirebaseAuth.getInstance();
        current_user = app_auth.getCurrentUser();

        email_edit = findViewById(R.id.edt_reg_email);
        password_edit = findViewById(R.id.edt_reg_password);
        reg_button = findViewById(R.id.register_button);

        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempt_register();
            }
        });


    }

    private void attempt_register() {
        email      = email_edit.getText().toString();
        password       = password_edit.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //Check if inputs are empty
        if (TextUtils.isEmpty(email)) {
            email_edit.setError("Email is required");
            focusView = email_edit;
            cancel = true;

        }
        else if (TextUtils.isEmpty(password)) {
            password_edit.setError("Password is required");
            focusView = password_edit;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        else{

            //Check for network connection before attempting registration
            if (!isOnline(Register.this)) {
                Toasty.warning(getApplicationContext(),"NO INTERNET CONNECTION",
                        Toast.LENGTH_LONG, true).show();
            } else {
                registerprocess(email,password);

            }
        }
    }

    private void registerprocess(String email, String password) {
        app_auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendverification();
                            Toasty.success(getApplicationContext(),"Registration Successful",
                                    Toast.LENGTH_LONG, true).show();
                            startActivity(new Intent(Register.this, TujueMain.class));
                            finish();
                        }
                        else {
                            Log.d(Constants.TAG, "createUserWithEmail:failure", task.getException());
                            Toasty.error(getApplicationContext(),"ERROR",
                                    Toast.LENGTH_LONG, true).show();

                        }

                    }
                });
    }

    private void sendverification() {
        current_user = app_auth.getCurrentUser();
        current_user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toasty.success(getApplicationContext(),"Check your email inbox to verify your email",
                                    Toast.LENGTH_LONG, true).show();
                            Log.d(Constants.TAG, "Email sent.");
                        } else {
                            Log.d(Constants.TAG, "sendEmailVerification:failure", task.getException());
                            Toasty.error(getApplicationContext(),"Verification email not sent",
                                    Toast.LENGTH_LONG, true).show();

                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Register.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();


        if (current_user != null){
            String uid = current_user.getUid();
            Log.d(Constants.TAG, uid);
            startActivity(new Intent(Register.this, TujueMain.class));
            finish();
        } else {

        }
    }

    private boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
