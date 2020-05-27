package com.client.vpman.weatherwall.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ccy.focuslayoutmanager.FocusLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
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
import com.client.vpman.weatherwall.Adapter.ExploreAdapter;
import com.client.vpman.weatherwall.Adapter.RescAdapter;
import com.client.vpman.weatherwall.Adapter.TempRecylerAdapter;
import com.client.vpman.weatherwall.CustomeUsefullClass.CenterZoomLayoutManager;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.Model.ModelData;
import com.client.vpman.weatherwall.Model.ModelData1;
import com.client.vpman.weatherwall.Model.ModelData5;
import com.client.vpman.weatherwall.R;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ccy.focuslayoutmanager.FocusLayoutManager.dp2px;

public class TempRecylerViewDesign extends AppCompatActivity {

    RecyclerView scrollView;
    private List<ModelData1> list;
    private long mRequestStartTime;
    private TempRecylerAdapter adapter;
    private String Url = "https://api.pexels.com/v1/curated?per_page=80&page=1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_recyler_view_design);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        scrollView=findViewById(R.id.picker);

        list=new ArrayList<>();

        LoadImage();
    }

    public void LoadImage() {
        mRequestStartTime = System.currentTimeMillis();


        String Url = "https://api.pexels.com/v1/search?query=nature&per_page=100&page=3";
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
                    ModelData1 modelData1 = new ModelData1(object.getString("large2x"), photographer.getString("photographer"), object.getString("large"), object.getString("original"), wallobj.getString("url"));
                    list.add(modelData1);
                }
                Collections.shuffle(list);
                adapter = new TempRecylerAdapter(list,TempRecylerViewDesign.this);
               /* LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);*/

                FocusLayoutManager focusLayoutManager=new FocusLayoutManager.Builder()
                        .layerPadding(dp2px(this, 14))
                        .normalViewGap(dp2px(this, 14))
                        .focusOrientation(FocusLayoutManager.FOCUS_LEFT)
                        .isAutoSelect(true)
                        .maxLayerCount(3)
                        .setOnFocusChangeListener((focusdPosition, lastFocusedPosition) -> {

                        }).build();
                scrollView.setHasFixedSize(true);
                scrollView.setNestedScrollingEnabled(true);


               /* int position=scrollView.getCurrentItem();
                Log.d("position", String.valueOf(position));*/

                scrollView.setLayoutManager(focusLayoutManager);
                scrollView.setAdapter(adapter);

                /*scrollView.setOverScrollEnabled(true);


                scrollView.setSlideOnFling(true);

                scrollView.addScrollStateChangeListener(new DiscreteScrollView.ScrollStateChangeListener<RecyclerView.ViewHolder>() {
                    @Override
                    public void onScrollStart(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                        scrollView.smoothScrollToPosition(i);
                    }

                    @Override
                    public void onScrollEnd(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                        *//*scrollView.smoothScrollToPosition(i);*//*

                    }

                    @Override
                    public void onScroll(float v, int i, int i1, @Nullable RecyclerView.ViewHolder viewHolder, @Nullable RecyclerView.ViewHolder t1) {
                        scrollView.smoothScrollToPosition(i);

                    }
                });
                InfiniteScrollAdapter wrapper = InfiniteScrollAdapter.wrap(adapter);
                scrollView.setAdapter(wrapper);*/



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

        RequestQueue requestQueue = Volley.newRequestQueue(TempRecylerViewDesign.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

/*    public void loadImage() {
        modelData = new ArrayList<>();


        Log.d("iuedqwljgdho", Url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, response -> {
            Log.d("resPonseData", response);


            try {
                JSONObject obj = new JSONObject(response);


                JSONArray wallArray = obj.getJSONArray("photos");
                for (int i = 0; i < wallArray.length(); i++) {
                    JSONObject wallobj = wallArray.getJSONObject(i);
                    JSONObject photographer = new JSONObject(String.valueOf(wallobj));
                    String phUrl = wallobj.getString("url");
                    JSONObject ProfileUrl = new JSONObject(String.valueOf(wallobj));
                    JSONObject jsonObject = wallobj.getJSONObject("src");
                    JSONObject object = new JSONObject(String.valueOf(jsonObject));
                    String userImg = object.getString("small");
                    String userImg1 = object.getString("tiny");


                    ModelData modelData1 = new ModelData(jsonObject.getString("large2x"), photographer.getString("photographer"), jsonObject.getString("large"), jsonObject.getString("original"),wallobj.getString("url"));
                    modelData.add(modelData1);


                }
                Collections.shuffle(modelData);

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
                    imageView.setImageBitmap(image);
                } else {
                    if (getActivity()!=null)
                    {
                        Glide.with(getActivity())
                                .load(modelData.get(0).getOriginal())
                                .thumbnail(
                                        Glide.with(getActivity()).load(modelData.get(0).getLarge2x())
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

                                .into(imageView);
                    }


                }
                imageView.setOnClickListener(view -> {
                    Intent intent = new Intent(getActivity(), FullImage.class);
                    ModelData modelData2 = modelData.get(position);
                    intent.putExtra("img", modelData2.getLarge2x());
                    intent.putExtra("imgSmall", modelData2.getLarge());
                    intent.putExtra("large", modelData2.getOriginal());
                    intent.putExtra("PhotoUrl",modelData2.getPhotoUrl());
                    startActivity(intent);
                });


                rescAdapter = new RescAdapter(modelData, getActivity());
                position = 0;
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        Log.d("fdsf", String.valueOf(newState));
                        Log.d("khfkhk", String.valueOf(position));
                        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                            //Dragging
                        } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            position = layoutManager.findFirstVisibleItemPosition();
                            Log.d("poslwhf", String.valueOf(position));
                            imageView.setOnClickListener(view -> {
                                Intent intent = new Intent(getActivity(), FullImage.class);
                                ModelData modelData2 = modelData.get(position);
                                intent.putExtra("img", modelData2.getLarge2x());
                                startActivity(intent);
                            });

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

                                if (getActivity()!=null)
                                {
                                    Glide.with(getActivity())
                                            .load(modelData.get(position).getLarge2x())
                                            .thumbnail(
                                                    Glide.with(getActivity()).load(modelData.get(position).getLarge2x())
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

                                            .into(imageView);
                                }


                            }


                        }
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }
                });


                recyclerView.setAdapter(rescAdapter);

                // Glide.with(MainActivity.this).load(slides.get(n)).preload(500,500);


            } catch (Exception e) {
                e.printStackTrace();
                Log.d("catchError", "Got the error" + e);
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "563492ad6f91700001000001fd351942a4524d62bb9a68308855b667");
                return params;
            }
        };

        stringRequest.setShouldCache(false);

        if (getActivity()!=null)
        {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }

    }*/
}
