package com.client.vpman.weatherwall.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
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
import com.client.vpman.weatherwall.Adapter.ExploreAdapter;
import com.client.vpman.weatherwall.CustomeUsefullClass.ModelData5;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
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

public class ExploreAcitivity extends AppCompatActivity {

    private long mRequestStartTime;
    String query;
    RecyclerView recyclerView;
    List<ModelData5> model5;
    ExploreAdapter exploreAdapter;
    MotionLayout motionLayout;
    MaterialTextView textView;
    ImageView imageExplore, backMotionExp, backgroundDesignExp;
    SharedPref1 sharedPref1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_acitivity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView = findViewById(R.id.recylerViewExploreData);
        imageExplore = findViewById(R.id.imageExplore);
        sharedPref1 = new SharedPref1(ExploreAcitivity.this);
        textView = findViewById(R.id.titleDataExp);
        motionLayout = findViewById(R.id.motionBackgroundExplore);
        backgroundDesignExp = findViewById(R.id.backgroundDesignExp);
        backMotionExp = findViewById(R.id.backMotionExp);
        Intent intent = getIntent();
        String mImg = intent.getStringExtra("imgData");
        String sImg = intent.getStringExtra("imgDataSmall");
        query = intent.getStringExtra("query");
        String text = intent.getStringExtra("text");
        textView.setText(text);

        if (sharedPref1.getTheme().equals("Light")) {
            textView.setTextColor(Color.parseColor("#000000"));
            backMotionExp.setImageResource(R.drawable.ic_arrow_back);
            motionLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            backgroundDesignExp.setImageResource(R.drawable.basic_design_customized_white);
        } else if (sharedPref1.getTheme().equals("Dark")) {

            textView.setTextColor(Color.parseColor("#FFFFFF"));
            backMotionExp.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            motionLayout.setBackgroundColor(Color.parseColor("#000000"));
            backgroundDesignExp.setImageResource(R.drawable.basic_design_customized);

        } else {
            textView.setTextColor(Color.parseColor("#000000"));
            backMotionExp.setImageResource(R.drawable.ic_arrow_back);
            motionLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            backgroundDesignExp.setImageResource(R.drawable.basic_design_customized_white);
        }
        backMotionExp.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        });


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(new ObjectKey(System.currentTimeMillis())).encodeQuality(70);
        requestOptions.priority(Priority.IMMEDIATE);
        requestOptions.skipMemoryCache(false);
        requestOptions.onlyRetrieveFromCache(true);
        requestOptions.priority(Priority.HIGH);
        requestOptions.isMemoryCacheable();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);

        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        //   requestOptions.placeholder(Utils.getRandomDrawbleColor());
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
            imageExplore.setImageBitmap(image);
        } else {
            Glide.with(ExploreAcitivity.this)
                    .load(mImg)
                    .thumbnail(
                            Glide.with(ExploreAcitivity.this).load(sImg)
                    )
                    .apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //  spinKitView.setVisibility(View.GONE);


                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                            // spinKitView.setVisibility(View.GONE);

                            return false;
                        }
                    })

                    .into(imageExplore);
        }

        model5 = new ArrayList<>();
        LoadImage();
    }

    public void LoadImage() {
        mRequestStartTime = System.currentTimeMillis();


            String Url = "https://api.pexels.com/v1/search?query=" + query + "&per_page=100&page=3";
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
                        Log.d("PhotoURL", wallobj.getString("url"));
                        JSONObject jsonObject = wallobj.getJSONObject("src");
                        JSONObject object = new JSONObject(String.valueOf(jsonObject));
                        ModelData5 modelData1 = new ModelData5(object.getString("large2x"), photographer.getString("photographer"), object.getString("large"), object.getString("original"), wallobj.getString("url"));
                        model5.add(modelData1);
                    }
                    Collections.shuffle(model5);
                    exploreAdapter = new ExploreAdapter(ExploreAcitivity.this, model5);
                    LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setNestedScrollingEnabled(true);
                    int itemViewType = 0;
                    recyclerView.getRecycledViewPool().setMaxRecycledViews(itemViewType, 0);
                    recyclerView.setAdapter(exploreAdapter);


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

            RequestQueue requestQueue = Volley.newRequestQueue(ExploreAcitivity.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }




}
