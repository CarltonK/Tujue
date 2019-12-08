package com.example.tujue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;
import handler.Article;
import handler.DatabaseLocal;

public class WebReadActivity extends AppCompatActivity {
    private DatabaseLocal databaseLocal;
    private FirebaseAnalytics mFirebaseAnalytics;
    private TextView author_tv, date_tv, source_tv, content_tv, title_tv, save_tv;
    private ImageView image_article;

    //Database Variables
    private DatabaseReference reference;
    private FirebaseDatabase firedata;

    private FirebaseAuth app_auth;
    private FirebaseUser current_user;

    //Identifiers
    private String author, date, source, content, image_url, title, description, url;

    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_read);
        this.getSupportActionBar().hide();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        databaseLocal = new DatabaseLocal(WebReadActivity.this);

        app_auth = FirebaseAuth.getInstance();
        current_user = app_auth.getCurrentUser();

        author_tv = findViewById(R.id.author_single_article);
        date_tv = findViewById(R.id.date_single_article);
        source_tv = findViewById(R.id.source_single_article);
        content_tv = findViewById(R.id.content_single_article);
        image_article = findViewById(R.id.image_single_article);
        title_tv = findViewById(R.id.title_single_article);
        save_tv = findViewById(R.id.save_single_article);

        loadPage();

        save_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveToDatabase();

                Toast.makeText(WebReadActivity.this,"Article saved",
                        Toast.LENGTH_SHORT).show();

            }
        });

        }

    private void saveToDatabase() {

        try {
            firedata = FirebaseDatabase.getInstance();
            reference = firedata.getReference();

        } catch (Exception e) {
            Log.d("Tujue","saveToDatabase:failure", e);
        } finally {
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            reference.child("likes").child(current_user.getUid())
                                    .push().setValue(article);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void loadPage() {
        try {

            //Load articles from local database
            databaseLocal.getDbArticle();
            Log.d("Tujue-Web", String.valueOf(databaseLocal.getDbArticle()));

            author = databaseLocal.getDbArticle().get("author");
            date = databaseLocal.getDbArticle().get("publishedAt");
            source = databaseLocal.getDbArticle().get("source");
            content = databaseLocal.getDbArticle().get("content");
            image_url = databaseLocal.getDbArticle().get("urlToImage");
            title = databaseLocal.getDbArticle().get("title");
            description = databaseLocal.getDbArticle().get("description");
            url = databaseLocal.getDbArticle().get("url");


        } catch (Exception e) {
            Log.d("Tujue", "errorFound:True", e);
        } finally {

            author_tv.setText("Written By: " + author);
            date_tv.setText("Published On: " + date);
            source_tv.setText("Source: " + source);
            content_tv.setText(content);
            title_tv.setText(title);

            Picasso.get()
                    .load(image_url)
                    .fit()
                    .error(R.drawable.ic_error_outline_white_24dp)
                    .into(image_article, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Log.i("Tujue", "imgLoad:success");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.i("Tujue", "imgLoad:failure", e);
                        }

                    });

            article = new Article(author, title, description, url, image_url, date);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
