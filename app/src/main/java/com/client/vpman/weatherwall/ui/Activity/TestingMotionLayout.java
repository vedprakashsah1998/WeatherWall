package com.client.vpman.weatherwall.ui.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.WindowManager;
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
import com.client.vpman.weatherwall.Adapter.TestingAdapter;
import com.client.vpman.weatherwall.model.ModelData;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.model.UnsplashModel;
import com.client.vpman.weatherwall.R;

import com.client.vpman.weatherwall.databinding.ActivityTestingMotionLayoutBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TestingMotionLayout extends AppCompatActivity {
    String query;
    TestingAdapter testingAdapter;
    private long mRequestStartTime;
    List<ModelData> modelData4List;
    SharedPref1 sharedPref1;
    private List<String> apiList;

    List<UnsplashModel> unsplashModelList;
    private Parcelable mListState;
    LinearLayoutManager linearLayoutManager;
    static int page = 1;
    public static final String BUNDLE_LIST_STATE = "list_state";
    ActivityTestingMotionLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestingMotionLayoutBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPref1 = new SharedPref1(TestingMotionLayout.this);
        Intent intent = getIntent();
        String mImg = intent.getStringExtra("img1");
        String sImg = intent.getStringExtra("img2");
        query = intent.getStringExtra("query");
        String Landscape = intent.getStringExtra("text");
        binding.titleData.setText(Landscape);
        if (sharedPref1.getTheme().equals("Light")) {
            binding.titleData.setTextColor(Color.parseColor("#000000"));
            binding.backMotion.setImageResource(R.drawable.ic_arrow_back);
            binding.motionBackground.setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.backgroundDesign.setImageResource(R.drawable.basic_design_customized_white);
        } else if (sharedPref1.getTheme().equals("Dark")) {
            binding.titleData.setTextColor(Color.parseColor("#FFFFFF"));
            binding.backMotion.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            binding.motionBackground.setBackgroundColor(Color.parseColor("#000000"));
            binding.backgroundDesign.setImageResource(R.drawable.basic_design_customized);

        } else {
            binding.titleData.setTextColor(Color.parseColor("#000000"));
            binding.backMotion.setImageResource(R.drawable.ic_arrow_back);
            binding.motionBackground.setBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.backgroundDesign.setImageResource(R.drawable.basic_design_customized_white);
        }


        binding.backMotion.setOnClickListener(v -> {
            onBackPressed();
        });

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(new ObjectKey(System.currentTimeMillis())).encodeQuality(70);
        requestOptions.priority(Priority.IMMEDIATE);
        requestOptions.skipMemoryCache(false);
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
            binding.toolbarImage.setImageBitmap(image);
        } else {
            Glide.with(TestingMotionLayout.this)
                    .load(mImg)
                    .thumbnail(
                            Glide.with(TestingMotionLayout.this).load(sImg)
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
                    .into(binding.toolbarImage);
        }
        LoadImage(page);

        Log.d("onScrolled","Data1");
        binding.recyclerviewTesting.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager.findLastVisibleItemPosition() == testingAdapter.getItemCount() - 1) {
                    page++;
                    Log.d("onScrolled","Data2");
                    LoadImage(page);
                }
            }
        });



    }

    public void LoadImage(int page) {
        modelData4List = new ArrayList<>();
        mRequestStartTime = System.currentTimeMillis();
        String Url = "https://api.pexels.com/v1/search?query=" + query + "&per_page=80&page=" + page + "";
        Log.d("urlData",Url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, response -> {
            Log.d("response", response);
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray wallArray = obj.getJSONArray("photos");
                for (int i = 0; i < wallArray.length(); i++) {
                    JSONObject wallobj = wallArray.getJSONObject(i);
                    JSONObject photographer = new JSONObject(String.valueOf(wallobj));
                    JSONObject PhotoUrl = new JSONObject(String.valueOf(wallobj));
                    Log.d("PhotoURL", wallobj.getString("url"));
                    JSONObject jsonObject = wallobj.getJSONObject("src");
                    JSONObject object = new JSONObject(String.valueOf(jsonObject));
                    ModelData modelData1 = new ModelData(object.getString("large2x"), photographer.getString("photographer"), object.getString("large"), object.getString("original"), wallobj.getString("url"));
                    modelData4List.add(modelData1);
                }
                Collections.shuffle(modelData4List);
                testingAdapter = new TestingAdapter(TestingMotionLayout.this, modelData4List);
                linearLayoutManager = new GridLayoutManager(TestingMotionLayout.this, 2, GridLayoutManager.VERTICAL, false);
                binding.recyclerviewTesting.setLayoutManager(linearLayoutManager);
                binding.recyclerviewTesting.setHasFixedSize(true);
                binding.recyclerviewTesting.setItemAnimator(new DefaultItemAnimator());
                binding.recyclerviewTesting.setNestedScrollingEnabled(true);
                int itemViewType = 0;
                binding.recyclerviewTesting.getRecycledViewPool().setMaxRecycledViews(itemViewType, 0);
                binding.recyclerviewTesting.setAdapter(testingAdapter);

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
                } catch (UnsupportedEncodingException | JSONException e1) {
                    // Couldn't properly decode data to string
                    e1.printStackTrace();
                } // returned data is not JSONObject?

            }

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                apiList = new ArrayList<>();
                apiList.add(getString(R.string.APIKEY1));
                apiList.add(getString(R.string.APIKEY2));
                apiList.add(getString(R.string.APIKEY3));
                apiList.add(getString(R.string.APIKEY4));
                apiList.add(getString(R.string.APIKEY5));
                Random random = new Random();
                int n = random.nextInt(apiList.size());
                params.put("Authorization",apiList.get(n));
                return params;
            }
        };
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(TestingMotionLayout.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

  /*  public void LoadUnsplashImage() {

        String Url = "https://api.unsplash.com/search/photos?per_page=20&page="+page+"&query=" + query + "&client_id=p8S-xjITsctkke0ZmKIdklrug3IMpYcMdObQuGx5xOY";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, Url, response -> {

            Log.d("UnsplashRes",response);
            try {
                JSONObject jsonObject=new JSONObject(response);
                JSONArray jsonArray=jsonObject.getJSONArray("results");
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    String blur_hash=jsonObject1.getString("blur_hash");
                    JSONObject urls=jsonObject1.getJSONObject("urls");
                    Log.d("ULLL", String.valueOf(urls));
                    String raw=urls.getString("raw");
                    String full=urls.getString("full");
                    String regular=urls.getString("regular");
                    String small=urls.getString("small");
                    String thumb=urls.getString("thumb");
                    JSONObject links=jsonObject1.getJSONObject("links");
                    String image_html=links.getString("html");
                    String sponsorship=jsonObject1.getString("sponsorship");
                    JSONObject user=jsonObject1.getJSONObject("user");
                    String username=user.getString("username");
                    String name=user.getString("name");
                    String twitter_username=user.getString("twitter_username");
                    String portfolio_url=user.getString("portfolio_url");
                    String bio=user.getString("bio");
                    String location=user.getString("location");
                    JSONObject user_links=user.getJSONObject("links");
                    String unsplash_portfolio=user_links.getString("html");

                    JSONObject profile_image=user.getJSONObject("profile_image");
                    String user_profile_small=profile_image.getString("small");
                    String user_profile_medium=profile_image.getString("medium");
                    String user_profile_large=profile_image.getString("large");
                    String instagram_username=user.getString("instagram_username");

                    UnsplashModel unsplashModel=new UnsplashModel(raw,full,regular,small,thumb,blur_hash,username,name,twitter_username,portfolio_url,bio,
                            location,image_html,sponsorship,unsplash_portfolio,user_profile_small,user_profile_medium,user_profile_large,instagram_username);

                    unsplashModelList.add(unsplashModel);
                    //Log.d("rawinghu",raw);
                }
                Collections.shuffle(unsplashModelList);


            } catch (JSONException e) {
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

        });
        stringRequest.setShouldCache(false);

        RequestQueue requestQueue = Volley.newRequestQueue(TestingMotionLayout.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }*/

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        mListState = linearLayoutManager.onSaveInstanceState();
        outState.putParcelable(BUNDLE_LIST_STATE, mListState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        if(mListState != null) {

            linearLayoutManager.onRestoreInstanceState(mListState);
        }else {
            Toast.makeText(this, "List is Empty", Toast.LENGTH_SHORT).show();
        }
        super.onResume();
    }



}
