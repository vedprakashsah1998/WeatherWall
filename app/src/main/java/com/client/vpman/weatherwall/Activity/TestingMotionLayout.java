package com.client.vpman.weatherwall.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.client.vpman.weatherwall.Adapter.ImageViedeoPagerAdapter;
import com.client.vpman.weatherwall.Adapter.TestingAdapter;
import com.client.vpman.weatherwall.CustomeUsefullClass.ModelData4;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestingMotionLayout extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    MenuItem prevMenuItem;

    ViewPager viewPager;
    ImageViedeoPagerAdapter adapter;

    SharedPref1 sharedPref1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_motion_layout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        viewPager=findViewById(R.id.viewpagerImgVied);
        bottomNavigationView=findViewById(R.id.bottomNavigation);
        sharedPref1=new SharedPref1(TestingMotionLayout.this);
        ColorStateList iconsColorStates = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        Color.parseColor("#454545"),
                        Color.parseColor("#000000")
                });
        ColorStateList textColorStates = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        Color.parseColor("#454545"),
                        Color.parseColor("#000000")
                });

        ColorStateList iconsColorStatesBlack = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        Color.parseColor("#9B9B9C"),
                        Color.parseColor("#FFFFFF")
                });
        ColorStateList textColorStatesBlack = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        Color.parseColor("#9B9B9C"),
                        Color.parseColor("#FFFFFF")
                });

        if (sharedPref1.getTheme().equals("Light"))
        {
            bottomNavigationView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            bottomNavigationView.setItemIconTintList(iconsColorStates);
            bottomNavigationView.setItemTextColor(textColorStates);

        }
        else if (sharedPref1.getTheme().equals("Dark"))
        {
            bottomNavigationView.setBackgroundColor(Color.parseColor("#222222"));
            bottomNavigationView.setItemIconTintList(iconsColorStatesBlack);
            bottomNavigationView.setItemTextColor(textColorStatesBlack);
        }
        else
        {
            bottomNavigationView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            bottomNavigationView.setItemIconTintList(iconsColorStates);
            bottomNavigationView.setItemTextColor(textColorStates);
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapter=new ImageViedeoPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.imageList:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.videoList:
                    viewPager.setCurrentItem(1);
                    break;

            }

            return false;
        });
    }






}
