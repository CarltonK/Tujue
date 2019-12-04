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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;
import handler.Constants;

public class ForgotPass extends AppCompatActivity {
    private ImageView rst_button;
    private EditText rst_email;

    //Key Identifiers
    String email;

    //Firebase Init
    private FirebaseAuth app_auth;
    private FirebaseUser current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        this.getSupportActionBar().hide();

        app_auth = FirebaseAuth.getInstance();

        rst_button = findViewById(R.id.passrst_button);
        rst_email = findViewById(R.id.edt_fpass_email);

        rst_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptreset();
            }
        });
    }

    private void attemptreset() {
        email      = rst_email.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)) {
            rst_email.setError("Email is required");
            focusView = rst_email;
            cancel = true;

        }

        if (cancel) {
            focusView.requestFocus();
        }
        else{

            if (!isOnline(ForgotPass.this)) {
                Toasty.warning(getApplicationContext(),"NO INTERNET CONNECTION",
                        Toast.LENGTH_LONG, true).show();
            } else {
                resetprocess(email);

            }
        }
    }

    private void resetprocess(String email) {
        app_auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toasty.success(getApplicationContext(),"Email sent",
                                    Toast.LENGTH_LONG, true).show();
                            startActivity(new Intent(ForgotPass.this, MainActivity.class));
                            finish();
                            Log.d(Constants.TAG, "Email sent.");
                        } else {
                            Toasty.error(getApplicationContext(),"Error " + task.getException(),
                                    Toast.LENGTH_LONG, true).show();
                            Log.d(Constants.TAG, "resetPassword: failure", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ForgotPass.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (current_user != null){
            String uid = current_user.getUid();
            Log.d(Constants.TAG, uid);
            startActivity(new Intent(ForgotPass.this, TujueMain.class));
            finish();
        }
    }

    private boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
