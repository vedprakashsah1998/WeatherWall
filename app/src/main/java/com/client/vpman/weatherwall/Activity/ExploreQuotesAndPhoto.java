package com.client.vpman.weatherwall.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;


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

import com.client.vpman.weatherwall.Adapter.PopAdapterLast;
import com.client.vpman.weatherwall.CustomeUsefullClass.ModelData2;
import com.client.vpman.weatherwall.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textview.MaterialTextView;
import com.makeramen.roundedimageview.RoundedImageView;

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


public class ExploreQuotesAndPhoto extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener
{

    List<String> list,list1;

    private boolean isHideToolbarView = false;
    private RelativeLayout titleAppbar;
    private long mRequestStartTime;

    private Toolbar toolbar;
    List<ModelData2> list2;
    private AppBarLayout appBarLayout;
    PopAdapterLast popAdapter;
    RecyclerView recyclerView;
    String query;
    ImageView back,back1;
    RoundedImageView imageView;
    MaterialTextView textView,textView1,QuotesTextData;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_quotes_and_photo);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toolbar=findViewById(R.id.toolBarQuotes);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        appBarLayout = findViewById(R.id.appBarQuotes);
        cardView=findViewById(R.id.cardViewQuotes);
        appBarLayout.addOnOffsetChangedListener(this);
        back=findViewById(R.id.back9QuotesData);
        recyclerView=findViewById(R.id.Imgrecylerview);
        QuotesTextData=findViewById(R.id.QuotesTextData);
        back1=findViewById(R.id.backQuotes);
        titleAppbar=findViewById(R.id.title_appbarQuotes);
        textView=findViewById(R.id.ImgName);
        textView1=findViewById(R.id.tvQuotesMain);
        imageView=findViewById(R.id.roundQuotesImg);
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbarQuotes);
        collapsingToolbarLayout.setTitle("");
        Intent intent = getIntent();
        String mImg=intent.getStringExtra("imgData");
        String sImg=intent.getStringExtra("imgDataSmall");
        query=intent.getStringExtra("query");
        String Landscape=intent.getStringExtra("text");
        textView.setText(Landscape);
        textView1.setText(Landscape);
        back.setOnClickListener(view -> {

            finish();
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        });
        back1.setOnClickListener(view -> {

            finish();
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        });
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
        Bitmap image = memCache.get("imagefile");
        if (image != null) {
            //Bitmap exists in cache.
            imageView.setImageBitmap(image);
        } else
        {
            Glide.with(ExploreQuotesAndPhoto.this)
                    .load(mImg)
                    .thumbnail(
                            Glide.with(ExploreQuotesAndPhoto.this).load(sImg)
                    )
                    .apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //  spinKitView.setVisibility(View.GONE);


                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                        {

                            // spinKitView.setVisibility(View.GONE);

                            return false;
                        }
                    })

                    .into(imageView);
        }

        list2=new ArrayList<>();
        list=new ArrayList<>();
        list1=new ArrayList<>();
        LoadImage();
Quotes();
    }


    public void Quotes()
    {

        String QuotesUrl="https://www.forbes.com/forbesapi/thought/uri.json?enrich=true&query=1&relatedlimit=100";
        Log.d("sdfljh","khwqgdi");
        StringRequest stringRequest=new StringRequest(Request.Method.GET, QuotesUrl, response -> {

            Log.d("qoefg",response);

            JSONObject jsonObject= null;
            try {
                jsonObject = new JSONObject(response);

                JSONObject jsonObject1=jsonObject.getJSONObject("thought");
                Log.d("qeljg", String.valueOf(jsonObject1));

                JSONArray jsonArray=jsonObject1.getJSONArray("relatedThemeThoughts");
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject2=jsonArray.getJSONObject(i);


                    list.add(jsonObject2.getString("quote"));

                }
                Collections.shuffle(list);
                Random random=new Random();
                int n = random.nextInt(list.size());
                QuotesTextData.setText(list.get(n));
                /*Log.d("asljf",quote);*/
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }, error -> {

        });
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(ExploreQuotesAndPhoto.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
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
            cardView.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;

        } else if (percentage < 1f && !isHideToolbarView) {
            imageView.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.VISIBLE);
            titleAppbar.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }

    public void LoadImage()
    {
        mRequestStartTime = System.currentTimeMillis();
 /*       assert query != null;
        Log.d("iueho",query);*/
        if (query!=null&&query.equals("Amoled"))
        {
            query="Night";
            String Url="https://api.pexels.com/v1/search?query="+query+"&per_page=100&page=2";
            StringRequest stringRequest=new StringRequest(Request.Method.GET, Url, response -> {
                Log.d("response", response);




                try {
                    JSONObject obj = new JSONObject(response);
                    Log.d("mil gaya",String.valueOf(obj));
                    int totalRes=obj.getInt("total_results");

                    Log.d("werg", String.valueOf(totalRes));

                    JSONArray wallArray = obj.getJSONArray("photos");
                    for (int i = 0; i < wallArray.length(); i++)
                    {
                        JSONObject wallobj=wallArray.getJSONObject(i);
                        JSONObject photographer=new JSONObject(String.valueOf(wallobj));
                        JSONObject ProfileUrl=new JSONObject(String.valueOf(wallobj));
                        JSONObject jsonObject=wallobj.getJSONObject("src");
                        JSONObject object=new JSONObject(String.valueOf(jsonObject));
                        ModelData2 modelData1=new ModelData2(object.getString("large2x"),photographer.getString("photographer"),object.getString("large"),object.getString("original"));
                        list2.add(modelData1);


                    }


                    popAdapter=new PopAdapterLast(ExploreQuotesAndPhoto.this,list2);

                  LinearLayoutManager  linearLayoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    recyclerView.setHasFixedSize(true);

                    recyclerView.setNestedScrollingEnabled(true);
                    int itemViewType = 0;
                    recyclerView.getRecycledViewPool().setMaxRecycledViews(itemViewType, 0);
                    recyclerView.setAdapter(popAdapter);


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
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization","563492ad6f91700001000001572b44febff5465797575bcba703c98c");
                    return params;
                }
            };

            stringRequest.setShouldCache(false);

            RequestQueue requestQueue = Volley.newRequestQueue(ExploreQuotesAndPhoto.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }
        else
        {
            String Url="https://api.pexels.com/v1/search?query="+query+"&per_page=100&page=2";
            StringRequest stringRequest=new StringRequest(Request.Method.GET, Url, response -> {
                Log.d("response", response);




                try {
                    JSONObject obj = new JSONObject(response);
                    Log.d("mil gaya",String.valueOf(obj));
                    int totalRes=obj.getInt("total_results");
                /*if (totalRes<=2)
                {
                    UnSplash();
                }*/
                    Log.d("werg", String.valueOf(totalRes));

                    JSONArray wallArray = obj.getJSONArray("photos");
                    for (int i = 0; i < wallArray.length(); i++)
                    {
                        JSONObject wallobj=wallArray.getJSONObject(i);
                        JSONObject photographer=new JSONObject(String.valueOf(wallobj));
                        JSONObject ProfileUrl=new JSONObject(String.valueOf(wallobj));
                        JSONObject jsonObject=wallobj.getJSONObject("src");
                        JSONObject object=new JSONObject(String.valueOf(jsonObject));
                        ModelData2 modelData1=new ModelData2(object.getString("large2x"),photographer.getString("photographer"),object.getString("large"),object.getString("original"));
                        list2.add(modelData1);


                    }

                    popAdapter=new PopAdapterLast(ExploreQuotesAndPhoto.this,list2);

                    LinearLayoutManager  linearLayoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    recyclerView.setHasFixedSize(true);

                    recyclerView.setNestedScrollingEnabled(true);
                    int itemViewType = 0;
                    recyclerView.getRecycledViewPool().setMaxRecycledViews(itemViewType, 0);
                    recyclerView.setAdapter(popAdapter);



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
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization","563492ad6f9170000100000159cfcc02e88f40e987eba343ce9dba4e");
                    return params;
                }
            };

            stringRequest.setShouldCache(false);

            RequestQueue requestQueue = Volley.newRequestQueue(ExploreQuotesAndPhoto.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);        }
    }


}
