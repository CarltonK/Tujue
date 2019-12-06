package com.example.tujue;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

import adapters.DrawerAdapter;
import es.dmoral.toasty.Toasty;
import fragments.About;
import fragments.Business;
import fragments.Entertainment;
import fragments.Health;
import fragments.Profile;
import fragments.Science;
import fragments.Sports;
import fragments.Technology;
import fragments.TopHead;
import handler.Constants;
import handler.DrawerItem;
import handler.SimpleItem;
import handler.SpaceItem;

public class TujueMain extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener{
    private FirebaseAuth app_auth;
    private FirebaseUser current_user;
    private FirebaseAnalytics mFirebaseAnalytics;

    //Main Group
    private static final int POS_HEADLINES = 0;
    private static final int POS_BUSINESS = 1;
    private static final int POS_ENTERTAINMENT= 2;
    private static final int POS_HEALTH = 3;
    private static final int POS_SCIENCE = 4;
    private static final int POS_SPORTS = 5;
    private static final int POS_TECHNOLOGY = 6;

    //Subsidiary Group
    private static final int POS_ABOUT = 7;
    private static final int POS_PROFILE = 8;
    private static final int POS_LOGOUT = 9;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tujue_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.drawer_layout)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_HEADLINES).setChecked(true),
                createItemFor(POS_BUSINESS),
                createItemFor(POS_ENTERTAINMENT),
                createItemFor(POS_HEALTH),
                createItemFor(POS_SCIENCE),
                createItemFor(POS_SPORTS),
                createItemFor(POS_TECHNOLOGY),
                new SpaceItem(40),
                createItemFor(POS_ABOUT),
                createItemFor(POS_PROFILE),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_HEADLINES);

        app_auth = FirebaseAuth.getInstance();
        current_user = app_auth.getCurrentUser();
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

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

    @Override
    public void onItemSelected(int position) {
        Fragment fragment = null;

        switch (position){

            case 0:
                fragment = new TopHead();
                break;

            case 1:
                fragment = new Business();
                break;

            case 2:
                fragment = new Entertainment();
                break;

            case 3:
                fragment = new Health();
                break;

            case 4:
                fragment = new Science();
                break;

            case 5:
                fragment = new Sports();
                break;

            case 6:
                fragment = new Technology();
                break;

            case 7:
                fragment = new About();
                break;

            case 8:
                fragment = new Profile();
                break;

            case 9:
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
                break;

            default:
                break;

        }

        slidingRootNav.closeMenu();
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack("").commit();


        } else {
            Log.e(Constants.TAG, "Error in creating fragment");
        }
    }


    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.textColorSecondary))
                .withTextTint(color(R.color.textColorPrimary))
                .withSelectedIconTint(color(R.color.colorAccent))
                .withSelectedTextTint(color(R.color.colorAccent));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }


    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

}
