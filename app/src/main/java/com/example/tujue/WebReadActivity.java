package com.example.tujue;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import es.dmoral.toasty.Toasty;
import handler.DatabaseLocal;

public class WebReadActivity extends AppCompatActivity {
    private DatabaseLocal databaseLocal;
    private WebView readarticle;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_read);
        this.getSupportActionBar().hide();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        databaseLocal = new DatabaseLocal(WebReadActivity.this);
        readarticle = findViewById(R.id.web_one_article);

        try {

            //Load articles from local database
            databaseLocal.getDbArticle();
            Log.d("Tujue-Web", String.valueOf(databaseLocal.getDbArticle()));

        } catch (Exception e) {
            Log.d("Tujue", "errorFound:True", e);
        } finally {

            readarticle.getSettings().setLoadsImagesAutomatically(true);
            readarticle.getSettings().setJavaScriptEnabled(true);
            readarticle.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            readarticle.loadUrl(databaseLocal.getDbArticle().get("url"));
        }


        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
