package com.example.tujue;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;
import fragments.Business;
import fragments.Entertainment;
import fragments.Health;
import fragments.Science;
import fragments.Sports;
import fragments.Technology;
import fragments.TopHead;
import handler.Constants;

public class TujueMain extends AppCompatActivity {
    private FirebaseAuth app_auth;
    private FirebaseUser current_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tujue_main);
        this.getSupportActionBar().hide();


        app_auth = FirebaseAuth.getInstance();
        current_user = app_auth.getCurrentUser();

    }
        @Override
        public void onBackPressed () {
            new AlertDialog.Builder(this)
                    .setTitle("EXIT")
                    .setMessage("Are you sure ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SignOut();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }


        private void SignOut () {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Toasty.info(getApplicationContext(), "See you next time....",
                            Toast.LENGTH_LONG, true).show();
                    app_auth.signOut();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }, 500);

        }

        @Override
        protected void onStart () {
            super.onStart();


            if (current_user != null) {
                String uid = current_user.getUid();
                Log.d(Constants.TAG, "currentUser" + uid);
            } else {
                SignOut();
            }


        }

    }
