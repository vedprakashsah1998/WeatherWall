package com.client.vpman.weatherwall.Activity;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.client.vpman.weatherwall.Adapter.PopAdapter;
import com.client.vpman.weatherwall.CustomeUsefullClass.ModelData1;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textview.MaterialTextView;
import com.kc.unsplash.Unsplash;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PopularList extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    ImageView back, back1;
    RecyclerView recyclerView;
    MaterialTextView textView, textView1;
    PopAdapter popAdapter;
    List<String> slides = new ArrayList<>();
    private long mRequestStartTime;
    RoundedImageView imageView;

    CoordinatorLayout coordinatorLayout;
    private boolean isHideToolbarView = false;
    private RelativeLayout titleAppbar;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    List<ModelData1> list = new ArrayList<>();
    private Unsplash unsplash;
    private final String CLIENT_ID = "fcd5073926c7fdd11b9eb62887dbd6398eafbb8f3c56073035b141ad57d1ab5f";
    private final String CLIENT_ID1 = "d3a92adcee2ef1d4cee1b52e80ae2c7f8ca95494ece74c74ae9c396fe8ba941a";
    String query;
    SharedPref1 sharedPref1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_list);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar = findViewById(R.id.toolBar);

        sharedPref1=new SharedPref1(PopularList.this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        appBarLayout = findViewById(R.id.appBar);
        appBarLayout.addOnOffsetChangedListener(this);
        back = findViewById(R.id.back9);
        back1 = findViewById(R.id.back10);
        titleAppbar = findViewById(R.id.title_appbar);
        coordinatorLayout=findViewById(R.id.cordinatorData);
        textView = findViewById(R.id.tv009);
        textView1 = findViewById(R.id.tv);
        imageView = findViewById(R.id.roundImage);
        recyclerView = findViewById(R.id.recyclerView991);
        unsplash = new Unsplash(CLIENT_ID);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        Intent intent = getIntent();
        String mImg = intent.getStringExtra("img1");
        String sImg = intent.getStringExtra("img2");
        query = intent.getStringExtra("query");
        String Landscape = intent.getStringExtra("text");
        textView.setText(Landscape);
        textView1.setText(Landscape);

        back.setOnClickListener(view -> {

            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        back1.setOnClickListener(view -> {

            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        if (sharedPref1.getTheme().equals("Light")) {

            coordinatorLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            collapsingToolbarLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
            back.setImageResource(R.drawable.ic_arrow_back);
            textView1.setTextColor(Color.parseColor("#000000"));

        } else if (sharedPref1.getTheme().equals("Dark")) {
            coordinatorLayout.setBackgroundColor(Color.parseColor("#000000"));
            collapsingToolbarLayout.setBackgroundColor(Color.parseColor("#000000"));
            toolbar.setBackgroundColor(Color.parseColor("#000000"));
            back.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            textView1.setTextColor(Color.parseColor("#FFFFFF"));



        } else {

            coordinatorLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            collapsingToolbarLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            toolbar.setBackgroundColor(Color.parseColor("#FFFFFF"));
            back.setImageResource(R.drawable.ic_arrow_back);
            textView1.setTextColor(Color.parseColor("#000000"));
        }




        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(new ObjectKey(System.currentTimeMillis())).encodeQuality(70);
        requestOptions.priority(Priority.IMMEDIATE);
        requestOptions.skipMemoryCache(false);
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.onlyRetrieveFromCache(true);
        requestOptions.priority(Priority.HIGH);
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.isMemoryCacheable();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);

        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
         requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.centerCrop();

        LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
            @Override
            protected int sizeOf(String key, Bitmap image) {
                return image.getByteCount() / 1024;
            }
        };
        Bitmap image = memCache.get("imagefile");
        if (image != null) {
            //Bitmap exists in cache.
            imageView.setImageBitmap(image);
        } else {
            Glide.with(PopularList.this)
                    .load(mImg)
                    .thumbnail(
                            Glide.with(PopularList.this).load(sImg)
                    )
                    .apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {


                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {


                            return false;
                        }
                    })

                    .into(imageView);
        }


        LoadImage();


    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(i) / (float) maxScroll;
        if (percentage == 1f && isHideToolbarView) {
            imageView.setVisibility(View.GONE);
            titleAppbar.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            imageView.setVisibility(View.VISIBLE);
            titleAppbar.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }

    public void LoadImage() {
        mRequestStartTime = System.currentTimeMillis();

        if (query != null) {
            String Url = "https://api.pexels.com/v1/search?query=" + query + "&per_page=100&page=1";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, response -> {
                Log.d("response", response);


                try {
                    JSONObject obj = new JSONObject(response);
                    Log.d("mil gaya", String.valueOf(obj));
                    int totalRes = obj.getInt("total_results");

                    Log.d("werg", String.valueOf(totalRes));

                    JSONArray wallArray = obj.getJSONArray("photos");
                    for (int i = 0; i < wallArray.length(); i++) {
                        JSONObject wallobj = wallArray.getJSONObject(i);
                        JSONObject photographer = new JSONObject(String.valueOf(wallobj));
                        JSONObject ProfileUrl = new JSONObject(String.valueOf(wallobj));
                        JSONObject jsonObject = wallobj.getJSONObject("src");
                        JSONObject object = new JSONObject(String.valueOf(jsonObject));
                        ModelData1 modelData1 = new ModelData1(object.getString("large2x"), photographer.getString("photographer"), object.getString("large"), object.getString("original"));
                        list.add(modelData1);
                    }
                    popAdapter = new PopAdapter(PopularList.this, list);
                    LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setNestedScrollingEnabled(true);
                    int itemViewType = 0;
                    recyclerView.getRecycledViewPool().setMaxRecycledViews(itemViewType, 0);
                    recyclerView.setAdapter(popAdapter);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }, error -> {

                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }

            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "563492ad6f917000010000010175b010e54243678613ef0d7fd3c497");
                    return params;
                }
            };

            stringRequest.setShouldCache(false);

            RequestQueue requestQueue = Volley.newRequestQueue(PopularList.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(this, "Network Failure", Toast.LENGTH_SHORT).show();
        }
    }


}
