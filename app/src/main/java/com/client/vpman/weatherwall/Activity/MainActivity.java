package com.client.vpman.weatherwall.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;
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
import com.client.vpman.weatherwall.CustomeDesignViewPager.VerticalViewPageAdapter;
import com.client.vpman.weatherwall.CustomeUsefullClass.Connectivity;
import com.client.vpman.weatherwall.CustomeUsefullClass.ModelData3;
import com.client.vpman.weatherwall.CustomeUsefullClass.OnDataPass;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;
import com.github.rongi.rotate_layout.layout.RotateLayout;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.material.tabs.TabLayout;
import com.kc.unsplash.Unsplash;
import com.kc.unsplash.models.Photo;
import com.kc.unsplash.models.SearchResults;
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

public class MainActivity extends AppCompatActivity implements OnDataPass,TabLayout.OnTabSelectedListener
{


    private DemoFragmentStateAdapter adapter;
    private VerticalViewPageAdapter mViewPager;
    List<String> slides = new ArrayList<>();

    private String JsonUrl;
    List<ModelData3> listModelData;


    private String Url;
    KenBurnsView imageView;
    private long mRequestStartTime;
    ProgressBar progressBar;
    String query;
    RotateLayout blackrotate,whiteRotate;


    private final String CLIENT_ID="fcd5073926c7fdd11b9eb62887dbd6398eafbb8f3c56073035b141ad57d1ab5f";
    private Unsplash unsplash;
    private TabLayout tabLayout,tabLayout1;

    SharedPref1 sharedPref1;
    ProgressBar spinKitView;
    Wave wanderingCubes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = findViewById(R.id.pager);

        /*
        progressBar = findViewById(R.id.progress);
         wanderingCubes = new WanderingCubes();
        progressBar.setIndeterminateDrawable(wanderingCubes);*/
        spinKitView=findViewById(R.id.spin_kit);
        wanderingCubes=new Wave();
        spinKitView.setIndeterminateDrawable(wanderingCubes);
        blackrotate=findViewById(R.id.rotateLayout);
        whiteRotate=findViewById(R.id.rotateLayout2);


        imageView=findViewById(R.id.imageView);
        tabLayout=findViewById(R.id.tabLayout);
        tabLayout1=findViewById(R.id.tabLayout2);
        tabLayout.addTab(tabLayout.newTab().setText("WEATHER"));
        tabLayout.addTab(tabLayout.newTab().setText("POPULAR"));
        tabLayout.addTab(tabLayout.newTab().setText("EXPLORE"));
        tabLayout.addTab(tabLayout.newTab().setText("4K WALLPAPER"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout1.addTab(tabLayout1.newTab().setText("WEATHER"));
        tabLayout1.addTab(tabLayout1.newTab().setText("POPULAR"));
        tabLayout1.addTab(tabLayout1.newTab().setText("EXPLORE"));
        tabLayout1.addTab(tabLayout1.newTab().setText("4K WALLPAPER"));
        tabLayout1.setTabGravity(TabLayout.GRAVITY_FILL);

        sharedPref1=new SharedPref1(MainActivity.this);
        if (sharedPref1.getTheme().equals("Light"))
        {
            blackrotate.setVisibility(View.VISIBLE);
            whiteRotate.setVisibility(View.GONE);
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout1.setVisibility(View.GONE);
        }
        else if (sharedPref1.getTheme().equals("Dark"))
        {
            blackrotate.setVisibility(View.GONE);
            whiteRotate.setVisibility(View.VISIBLE);
            tabLayout1.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.GONE);
        }
        else
        {
            blackrotate.setVisibility(View.VISIBLE);
            whiteRotate.setVisibility(View.GONE);
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout1.setVisibility(View.GONE);
        }


        Notification notification = new Notification(R.mipmap.ic_launcher,
                "Weather Wall",
                System.currentTimeMillis());
        notification.flags |= Notification.FLAG_NO_CLEAR
                | Notification.FLAG_ONGOING_EVENT;
        NotificationManager notifier = (NotificationManager)
                MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        notifier.notify(1, notification);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }









        imageView.setTransitionListener(new KenBurnsView.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                imageView.resume();

            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position==0)
                {
                    mViewPager.setCurrentItem(0);
                    imageView.setVisibility(View.VISIBLE);

                }
                else {
                    imageView.setVisibility(View.GONE);

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout1));
        adapter = new DemoFragmentStateAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        tabLayout.setOnTabSelectedListener(this);
        tabLayout1.setOnTabSelectedListener(MainActivity.this);
        listModelData=new ArrayList<>();



    }


public void loadImage()
{
    slides = new ArrayList<>();
    mRequestStartTime = System.currentTimeMillis();
    Log.d("iueho",Url);
    StringRequest stringRequest=new StringRequest(Request.Method.GET, Url, response -> {
        Log.d("response", response);




        try {
                JSONObject obj = new JSONObject(response);
                   Log.d("mil gaya",String.valueOf(obj));
            int totalRes=obj.getInt("total_results");
            if (totalRes<=2)
            {
                UnSplash();
            }
            Log.d("werg", String.valueOf(totalRes));

            JSONArray wallArray = obj.getJSONArray("photos");
                     for (int i = 0; i < wallArray.length(); i++)
                 {
                           JSONObject wallobj=wallArray.getJSONObject(i);
                        JSONObject photographer=new JSONObject(String.valueOf(wallobj));
                          JSONObject ProfileUrl=new JSONObject(String.valueOf(wallobj));
                        JSONObject jsonObject=wallobj.getJSONObject("src");
                           JSONObject object=new JSONObject(String.valueOf(jsonObject));
                           ModelData3 modelData3=new ModelData3(object.getString("large2x"),photographer.getString("photographer"),object.getString("large"));
                        listModelData.add(modelData3);
                         /*slides.add(object.getString("large2x"));*/

                 }
            Collections.shuffle(listModelData);

            Random random=new Random();
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
                    return image.getByteCount()/1024;
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
                imageView.setImageBitmap(image);
            } else
            {
                Glide.with(MainActivity.this)
                        .load(listModelData.get(n).getLarge2x())
                        .thumbnail(
                                Glide.with(MainActivity.this).load(listModelData.get(n).getLarge())
                        )
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                spinKitView.setVisibility(View.GONE);



                                return false;
                            }
                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                            {

                                spinKitView.setVisibility(View.GONE);




                                return false;
                            }
                        }).centerInside()

                        .into(imageView);
            }


               // Glide.with(MainActivity.this).load(slides.get(n)).preload(500,500);



        }
        catch (Exception e)
        {
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
        public Map<String, String>getHeaders() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Authorization","563492ad6f91700001000001572b44febff5465797575bcba703c98c");
            return params;
        }
    };

    stringRequest.setShouldCache(false);

    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    requestQueue.add(stringRequest);
}



    public void loadImage1()
    {
        slides = new ArrayList<>();
        mRequestStartTime = System.currentTimeMillis();
        Log.d("iueho",Url);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Url, response -> {
            Log.d("response", response);





            try {
                JSONObject obj = new JSONObject(response);
                Log.d("mil gaya",String.valueOf(obj));
                int totalRes=obj.getInt("total_results");
                if (totalRes<=2)
                {
                    loadPixabayImg();
                }
                Log.d("werg", String.valueOf(totalRes));

                JSONArray wallArray = obj.getJSONArray("photos");
                for (int i = 0; i < wallArray.length(); i++)
                {
                    JSONObject wallobj=wallArray.getJSONObject(i);
                    JSONObject photographer=new JSONObject(String.valueOf(wallobj));
                    JSONObject ProfileUrl=new JSONObject(String.valueOf(wallobj));
                    JSONObject jsonObject=wallobj.getJSONObject("src");
                    JSONObject object=new JSONObject(String.valueOf(jsonObject));
                    slides.add(object.getString("large2x"));

                }
                Collections.shuffle(slides);

                Random random=new Random();
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
                        return image.getByteCount()/1024;
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
                    imageView.setImageBitmap(image);
                } else
                {
                    Glide.with(MainActivity.this)
                            .load(slides.get(n))
                            .thumbnail(
                                    Glide.with(MainActivity.this).load(slides.get(n))
                            )
                            .apply(requestOptions)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    spinKitView.setVisibility(View.GONE);


                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                                {

                                    spinKitView.setVisibility(View.GONE);

                                    return false;
                                }
                            })

                            .into(imageView);

                }

                // Glide.with(MainActivity.this).load(slides.get(n)).preload(500,500);


            }
            catch (Exception e)
            {
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
            public Map<String, String>getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","563492ad6f91700001000001572b44febff5465797575bcba703c98c");
                return params;
            }
        };

        stringRequest.setShouldCache(false);

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void loadImage2()
    {
        slides = new ArrayList<>();
        mRequestStartTime = System.currentTimeMillis();
        Log.d("iueho",Url);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Url, response -> {
            Log.d("response", response);





            try {
                JSONObject obj = new JSONObject(response);
                Log.d("mil gaya",String.valueOf(obj));
                int totalRes=obj.getInt("total_results");
                if (totalRes<=2)
                {
                    loadPixabayImg();
                }
                Log.d("werg", String.valueOf(totalRes));

                JSONArray wallArray = obj.getJSONArray("photos");
                for (int i = 0; i < wallArray.length(); i++)
                {
                    JSONObject wallobj=wallArray.getJSONObject(i);
                    JSONObject photographer=new JSONObject(String.valueOf(wallobj));
                    JSONObject ProfileUrl=new JSONObject(String.valueOf(wallobj));
                    JSONObject jsonObject=wallobj.getJSONObject("src");
                    JSONObject object=new JSONObject(String.valueOf(jsonObject));
                    slides.add(object.getString("large2x"));

                }
                Collections.shuffle(slides);

                Random random=new Random();
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
                        return image.getByteCount()/1024;
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
                    imageView.setImageBitmap(image);
                } else
                {
                    Glide.with(MainActivity.this)
                            .load(slides.get(n))
                            .thumbnail(
                                    Glide.with(MainActivity.this).load(slides.get(n))
                            )
                            .apply(requestOptions)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    spinKitView.setVisibility(View.GONE);


                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                                {

                                    spinKitView.setVisibility(View.GONE);

                                    return false;
                                }
                            })

                            .into(imageView);
                }

                // Glide.with(MainActivity.this).load(slides.get(n)).preload(500,500);



            }
            catch (Exception e)
            {
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
            public Map<String, String>getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization","563492ad6f91700001000001572b44febff5465797575bcba703c98c");
                return params;
            }
        };

        stringRequest.setShouldCache(false);

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
    public void onDataPass(String data)
    {
        Log.d("djbvkj", data);

        query=data.replace(" ","%20");
      //  data="abcd";
       Url="https://api.pexels.com/v1/search?query="+query+"&per_page=150&page=1";
        if (Connectivity.isConnected(MainActivity.this)&&Connectivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, TelephonyManager.NETWORK_TYPE_EDGE))
        {
            UnSplash();
        }
        else if (Connectivity.isConnected(MainActivity.this)&&Connectivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, TelephonyManager.NETWORK_TYPE_CDMA))
        {
            UnSplash();
        }
        else if (Connectivity.isConnected(MainActivity.this)&&Connectivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, TelephonyManager.NETWORK_TYPE_1xRTT))
        {
            UnSplash();
        }
        else if (Connectivity.isConnected(MainActivity.this)&&Connectivity.isConnectedWifi(MainActivity.this)&&Connectivity.isConnectedFast(MainActivity.this))
        {
            loadImage();
        }
        else if (Connectivity.isConnected(MainActivity.this)&&Connectivity.isConnectedMobile(MainActivity.this)&&Connectivity.isConnectedFast(MainActivity.this))
        {
            loadImage1();
        }
        else
        {
            loadImage2();
        }

       // loadPixabayImg();
    }
    private boolean deviceOnWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MainActivity.this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return networkInfo.isConnected();
    }

    public void loadPixabayImg()
    {

        slides = new ArrayList<>();
        mRequestStartTime = System.currentTimeMillis();

        JsonUrl="https://pixabay.com/api/?key=13416003-ed8cefc0190df36d75e38fa93q="+query+"&image_type=photo&safesearch=true";
        Log.d("ihug",query);
        StringRequest stringRequest1=new StringRequest(Request.Method.GET, JsonUrl, response -> {

            Log.d("erg",response);
            mRequestStartTime = System.currentTimeMillis();

            try {
                JSONObject obj = new JSONObject(response);
                Log.d("milajewbfk", String.valueOf(obj));
                JSONArray wallArray = obj.getJSONArray("hits");
                for (int i = 0; i < wallArray.length(); i++) {
                    JSONObject wallObj = wallArray.getJSONObject(i);

                    slides.add(wallObj.getString("largeImageURL"));
                }
                Collections.shuffle(slides);

                Random random=new Random();
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




                // Glide.with(MainActivity.this).load(slides.get(n)).preload(500,500);
                Glide.with(MainActivity.this)
                        .load(slides.get(n))
                        .thumbnail(
                                Glide.with(MainActivity.this).load(slides.get(n))
                        )
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                spinKitView.setVisibility(View.GONE);


                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                            {

                                spinKitView.setVisibility(View.GONE);

                                return false;
                            }
                        })

                        .into(imageView);

            }
            catch (Exception e)
            {
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
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest1);

    }


    public void UnSplash()
    {
        unsplash=new Unsplash(CLIENT_ID);

        RequestOptions requestOptions = new RequestOptions();
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
        unsplash.searchPhotos(query, new Unsplash.OnSearchCompleteListener() {
            @Override
            public void onComplete(SearchResults results) {
                Log.d("Photos", "Total Results Found " + results.getTotal());

                List<Photo> photos = results.getResults();


                Random random=new Random();
                int n = random.nextInt(photos.size());

                LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                    @Override
                    protected int sizeOf(String key, Bitmap image) {
                        return image.getByteCount()/1024;
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
                    imageView.setImageBitmap(image);
                } else
                {
                    Glide.with(MainActivity.this)
                            .load(photos.get(n).getUrls().getFull())
                            .thumbnail(
                                    Glide.with(MainActivity.this).load(photos.get(n).getUrls().getRegular())
                            )
                            .apply(requestOptions)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    //  spinKitView.setVisibility(View.GONE);
                                    spinKitView.setVisibility(View.GONE);


                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                                {




                                    spinKitView.setVisibility(View.GONE);

                                    return false;
                                }
                            })

                            .into(imageView);
                }











            }

            @Override
            public void onError(String error) {
                Log.d("Unsplash", error);
            }
        });
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        }
        else {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }
}
