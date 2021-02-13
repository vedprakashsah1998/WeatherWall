package com.client.vpman.weatherwall.ui.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.LruCache;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

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
import com.client.vpman.weatherwall.Adapter.DemoFragmentStateAdapter;
import com.client.vpman.weatherwall.CustomeUsefullClass.Connectivity;
import com.client.vpman.weatherwall.CustomeUsefullClass.Constant;
import com.client.vpman.weatherwall.model.ModelData1;
import com.client.vpman.weatherwall.CustomeUsefullClass.OnDataPass;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.client.vpman.weatherwall.databinding.ActivityMainBinding;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.material.tabs.TabLayout;

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
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements OnDataPass, TabLayout.OnTabSelectedListener {


    List<String> slides = new ArrayList<>();
    List<ModelData1> listModelData;
    private String Url;
    Timer timer = new Timer();
    String query;

    SharedPref1 sharedPref1;
    Wave wanderingCubes;
    ActivityMainBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("BatteryLife")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);
        wanderingCubes = new Wave();
        binding.spinKit.setIndeterminateDrawable(wanderingCubes);
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("WEATHER"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("DISCOVERY"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("POPULAR"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("EXPLORE"));

        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.tabLayout2.addTab(binding.tabLayout2.newTab().setText("WEATHER"));
        binding.tabLayout2.addTab(binding.tabLayout2.newTab().setText("DISCOVERY"));
        binding.tabLayout2.addTab(binding.tabLayout2.newTab().setText("POPULAR"));
        binding.tabLayout2.addTab(binding.tabLayout2.newTab().setText("EXPLORE"));

        binding.tabLayout2.setTabGravity(TabLayout.GRAVITY_FILL);


        sharedPref1 = new SharedPref1(MainActivity.this);
        if (sharedPref1.getTheme().equals("Light")) {
            binding.rotateLayout.setVisibility(View.VISIBLE);
            binding.rotateLayout2.setVisibility(View.GONE);
            binding.tabLayout.setVisibility(View.VISIBLE);
            binding.tabLayout2.setVisibility(View.GONE);
        } else if (sharedPref1.getTheme().equals("Dark")) {
            binding.rotateLayout.setVisibility(View.GONE);
            binding.rotateLayout2.setVisibility(View.VISIBLE);
            binding.tabLayout2.setVisibility(View.VISIBLE);
            binding.tabLayout.setVisibility(View.GONE);
        } else {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    binding.rotateLayout.setVisibility(View.GONE);
                    binding.rotateLayout2.setVisibility(View.VISIBLE);
                    binding.tabLayout2.setVisibility(View.VISIBLE);
                    binding.tabLayout.setVisibility(View.GONE);

                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    binding.rotateLayout.setVisibility(View.VISIBLE);
                    binding.rotateLayout2.setVisibility(View.GONE);
                    binding.tabLayout.setVisibility(View.VISIBLE);
                    binding.tabLayout2.setVisibility(View.GONE);
                    break;
            }

        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }
        binding.imageView.setTransitionListener(new KenBurnsView.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                binding.imageView.resume();
            }
        });
        binding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    binding.pager.setCurrentItem(0);
                    binding.imageView.setVisibility(View.VISIBLE);
                } else {
                    binding.imageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout2));
        DemoFragmentStateAdapter adapter = new DemoFragmentStateAdapter(getSupportFragmentManager());
        binding.pager.setAdapter(adapter);
        binding.tabLayout.addOnTabSelectedListener(MainActivity.this);
        binding.tabLayout2.addOnTabSelectedListener(MainActivity.this);
        listModelData = new ArrayList<>();
    }


    public void loadImage() {
        slides = new ArrayList<>();
        Log.d("iueho", Url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, response -> {
            Log.d("response", response);


            try {
                JSONObject obj = new JSONObject(response);
                JSONArray wallArray = obj.getJSONArray("photos");
                for (int i = 0; i < wallArray.length(); i++) {
                    JSONObject wallobj = wallArray.getJSONObject(i);
                    JSONObject photographer = new JSONObject(String.valueOf(wallobj));
                    JSONObject ProfileUrl = new JSONObject(String.valueOf(wallobj));
                    JSONObject jsonObject = wallobj.getJSONObject("src");
                    JSONObject object = new JSONObject(String.valueOf(jsonObject));
                    ModelData1 modelData3 = new ModelData1(object.getString("large2x"), photographer.getString("photographer"), object.getString("large"));
                    listModelData.add(modelData3);
                    /*slides.add(object.getString("large2x"));*/

                }
                Collections.shuffle(listModelData);

                Random random = new Random();
                int n = random.nextInt(listModelData.size());
                Log.d("regr", String.valueOf(listModelData.get(n)));
                RequestOptions requestOptions = new RequestOptions();
                // requestOptions.error(Utils.getRandomDrawbleColor());
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
                //   requestOptions.placeholder(Utils.getRandomDrawbleColor());
                requestOptions.centerCrop();

                LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                    @Override
                    protected int sizeOf(String key, Bitmap image) {
                        return image.getByteCount() / 1024;
                    }
                };
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x; //width of screen in pixels
                int height = size.y;
                Bitmap image = memCache.get("imagefile");
                if (image != null) {
                    //Bitmap exists in cache.
                    binding.imageView.setImageBitmap(image);
                } else {
                    Glide.with(MainActivity.this)
                            .load(listModelData.get(n).getLarge2x())
                            .thumbnail(
                                    Glide.with(MainActivity.this).load(listModelData.get(n).getLarge())
                            )
                            .apply(requestOptions)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    binding.spinKit.setVisibility(View.GONE);


                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                    binding.spinKit.setVisibility(View.GONE);


                                    return false;
                                }
                            }).centerInside()

                            .into(binding.imageView);
                }


                // Glide.with(MainActivity.this).load(slides.get(n)).preload(500,500);


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
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "563492ad6f91700001000001572b44febff5465797575bcba703c98c");
                return params;
            }
        };

        stringRequest.setShouldCache(false);

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void loadImage1() {
        slides = new ArrayList<>();
        Log.d("iueho", Url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, response -> {
            Log.d("response", response);


            try {
                JSONObject obj = new JSONObject(response);
                Log.d("mil gaya", String.valueOf(obj));
                int totalRes = obj.getInt("total_results");
                if (totalRes <= 2) {
                    loadPixabayImg();
                }
                Log.d("werg", String.valueOf(totalRes));

                JSONArray wallArray = obj.getJSONArray("photos");
                for (int i = 0; i < wallArray.length(); i++) {
                    JSONObject wallobj = wallArray.getJSONObject(i);
                    JSONObject photographer = new JSONObject(String.valueOf(wallobj));
                    JSONObject ProfileUrl = new JSONObject(String.valueOf(wallobj));
                    JSONObject jsonObject = wallobj.getJSONObject("src");
                    JSONObject object = new JSONObject(String.valueOf(jsonObject));
                    slides.add(object.getString("large2x"));

                }
                Collections.shuffle(slides);

                Random random = new Random();
                int n = random.nextInt(slides.size());
                Log.d("regr", String.valueOf(slides.get(n)));
                RequestOptions requestOptions = new RequestOptions();
                // requestOptions.error(Utils.getRandomDrawbleColor());
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
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x; //width of screen in pixels
                int height = size.y;
                Bitmap image = memCache.get("imagefile");
                if (image != null) {
                    //Bitmap exists in cache.
                    binding.imageView.setImageBitmap(image);
                } else {
                    Glide.with(MainActivity.this)
                            .load(slides.get(n))
                            .thumbnail(
                                    Glide.with(MainActivity.this).load(slides.get(n))
                            )
                            .apply(requestOptions)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    binding.spinKit.setVisibility(View.GONE);


                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                    binding.spinKit.setVisibility(View.GONE);

                                    return false;
                                }
                            })

                            .into(binding.imageView);

                }

                // Glide.with(MainActivity.this).load(slides.get(n)).preload(500,500);


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
                params.put("Authorization", "563492ad6f91700001000001572b44febff5465797575bcba703c98c");
                return params;
            }
        };

        stringRequest.setShouldCache(false);

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void loadImage2() {
        slides = new ArrayList<>();
        Log.d("iueho", Url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, response -> {
            Log.d("response", response);


            try {
                JSONObject obj = new JSONObject(response);
                Log.d("mil gaya", String.valueOf(obj));
                int totalRes = obj.getInt("total_results");
                if (totalRes <= 2) {
                    loadPixabayImg();
                }
                Log.d("werg", String.valueOf(totalRes));

                JSONArray wallArray = obj.getJSONArray("photos");
                for (int i = 0; i < wallArray.length(); i++) {
                    JSONObject wallobj = wallArray.getJSONObject(i);
                    JSONObject photographer = new JSONObject(String.valueOf(wallobj));
                    JSONObject ProfileUrl = new JSONObject(String.valueOf(wallobj));
                    JSONObject jsonObject = wallobj.getJSONObject("src");
                    JSONObject object = new JSONObject(String.valueOf(jsonObject));
                    slides.add(object.getString("large2x"));

                }
                Collections.shuffle(slides);

                Random random = new Random();
                int n = random.nextInt(slides.size());
                Log.d("regr", String.valueOf(slides.get(n)));
                RequestOptions requestOptions = new RequestOptions();
                // requestOptions.error(Utils.getRandomDrawbleColor());
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
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x; //width of screen in pixels
                int height = size.y;
                Bitmap image = memCache.get("imagefile");
                if (image != null) {
                    //Bitmap exists in cache.
                    binding.imageView.setImageBitmap(image);
                } else {
                    Glide.with(MainActivity.this)
                            .load(slides.get(n))
                            .thumbnail(
                                    Glide.with(MainActivity.this).load(slides.get(n))
                            )
                            .apply(requestOptions)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    binding.spinKit.setVisibility(View.GONE);


                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                    binding.spinKit.setVisibility(View.GONE);

                                    return false;
                                }
                            })

                            .into(binding.imageView);
                }

                // Glide.with(MainActivity.this).load(slides.get(n)).preload(500,500);


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
                params.put("Authorization", "563492ad6f91700001000001572b44febff5465797575bcba703c98c");
                return params;
            }
        };

        stringRequest.setShouldCache(false);

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    public void onDataPass(String data) {
        Log.d("djbvkj", data);

        query = data.replace(" ", "%20");
        Url = Constant.BASE_URL + query + "&per_page=150&page=1";
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (Connectivity.isConnected(MainActivity.this) && Connectivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, TelephonyManager.NETWORK_TYPE_EDGE)) {
                    loadImage2();
                } else if (Connectivity.isConnected(MainActivity.this) && Connectivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, TelephonyManager.NETWORK_TYPE_CDMA)) {
                    loadImage1();
                } else if (Connectivity.isConnected(MainActivity.this) && Connectivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, TelephonyManager.NETWORK_TYPE_1xRTT)) {
                    loadImage();

                } else if (Connectivity.isConnected(MainActivity.this) && Connectivity.isConnectedWifi(MainActivity.this) && Connectivity.isConnectedFast(MainActivity.this)) {
                    loadImage();
                } else if (Connectivity.isConnected(MainActivity.this) && Connectivity.isConnectedMobile(MainActivity.this) && Connectivity.isConnectedFast(MainActivity.this)) {
                    loadImage1();
                } else {
                    loadImage2();
                }
            }
        }, 0, 5 * 60 * 1000);


    }


    public void loadPixabayImg() {

        slides = new ArrayList<>();

        String jsonUrl = "https://pixabay.com/api/?key=13416003-ed8cefc0190df36d75e38fa93q=" + query + "&image_type=photo&safesearch=true";
        Log.d("ihug", query);
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, jsonUrl, response -> {

            Log.d("erg", response);

            try {
                JSONObject obj = new JSONObject(response);
                Log.d("milajewbfk", String.valueOf(obj));
                JSONArray wallArray = obj.getJSONArray("hits");
                for (int i = 0; i < wallArray.length(); i++) {
                    JSONObject wallObj = wallArray.getJSONObject(i);

                    slides.add(wallObj.getString("largeImageURL"));
                }
                Collections.shuffle(slides);

                Random random = new Random();
                int n = random.nextInt(slides.size());
                Log.d("regr", String.valueOf(slides.get(n)));
                RequestOptions requestOptions = new RequestOptions();
                // requestOptions.error(Utils.getRandomDrawbleColor());
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .signature(new ObjectKey(System.currentTimeMillis())).encodeQuality(70);
                requestOptions.priority(Priority.IMMEDIATE);
                requestOptions.skipMemoryCache(false);
                requestOptions.onlyRetrieveFromCache(true);
                requestOptions.priority(Priority.HIGH);
                requestOptions.isMemoryCacheable();
                requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);

                requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                requestOptions.placeholder(Utils.getRandomDrawbleColor());
                requestOptions.centerCrop();


                Glide.with(MainActivity.this)
                        .load(slides.get(n))
                        .thumbnail(
                                Glide.with(MainActivity.this).load(slides.get(n))
                        )
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                binding.spinKit.setVisibility(View.GONE);


                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                binding.spinKit.setVisibility(View.GONE);

                                return false;
                            }
                        })

                        .into(binding.imageView);

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

        });


        stringRequest1.setShouldCache(false);

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        stringRequest1.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest1);

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        binding.pager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onBackPressed() {
        if (binding.pager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            binding.pager.setCurrentItem(binding.pager.getCurrentItem() - 1);
        }
    }
}
