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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;
import handler.Constants;

public class MainActivity extends AppCompatActivity {
    private EditText email_edit, password_edit;
    private ImageView btn_login;
    private TextView forgot_pass, log_to_reg;

    //Key Identifiers
    String email, password;

    //Init
    private FirebaseAuth app_auth;
    private FirebaseUser current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().hide();

        email_edit = findViewById(R.id.edt_log_email);
        password_edit = findViewById(R.id.edt_log_password);
        btn_login = findViewById(R.id.login_button);
        forgot_pass = findViewById(R.id.login_forgot);
        log_to_reg = findViewById(R.id.login_to_reg);

        //Proper init
        app_auth = FirebaseAuth.getInstance();
        current_user = app_auth.getCurrentUser();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempt_login();
            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        log_to_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Register.class));
                finish();
            }
        });
    }

    private void attempt_login() {
        email      = email_edit.getText().toString();
        password       = password_edit.getText().toString();

        boolean cancel = false;
        View focusView = null;

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

            if (!isOnline(MainActivity.this)) {
                Toasty.warning(getApplicationContext(),"NO INTERNET CONNECTION",
                        Toast.LENGTH_LONG, true).show();
            } else {
                loginprocess(email,password);

            }
        }
    }

    private void loginprocess(String email, String password) {
        app_auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser current_user = app_auth.getCurrentUser();
                            String uid = current_user.getUid();

                            Toasty.success(getApplicationContext(),"Welcome ",
                                    Toast.LENGTH_LONG, true).show();

                            startActivity(new Intent(MainActivity.this, TujueMain.class));
                            finish();
                        } else {
                            Log.d(Constants.TAG, "signInwithEmail:failure", task.getException());
                            Toasty.error(getApplicationContext(),"Invalid Credentials",
                                    Toast.LENGTH_LONG, true).show();

                        }

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (current_user != null){
            String uid = current_user.getUid();
            Log.d(Constants.TAG, uid);
            startActivity(new Intent(MainActivity.this, TujueMain.class));
            finish();
        }
    }

    private boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
