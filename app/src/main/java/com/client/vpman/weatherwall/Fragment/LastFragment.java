package com.client.vpman.weatherwall.Fragment;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.LruCache;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.client.vpman.weatherwall.Activity.FullImage;
import com.client.vpman.weatherwall.Adapter.RescAdapter;
import com.client.vpman.weatherwall.CustomeUsefullClass.ModelData;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class LastFragment extends Fragment {

    private Timer timer = new Timer();
    public LastFragment() {
        // Required empty public constructor
    }

    View view;
    RecyclerView recyclerView;
    RescAdapter rescAdapter;
    List<ModelData> modelData = new ArrayList<>();
    ImageView imageView;
    List<String> slides = new ArrayList<>();
    LinearLayoutManager layoutManager;
    int position;
    int req_code = 101;
    View view1;
    MaterialTextView curatedText;
    SharedPref1 sharedPref1;
    String query = "4k wallpaper";
    private String Url = "https://api.pexels.com/v1/curated?per_page=80&page=1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_last, container, false);
        imageView = view.findViewById(R.id.lastImage);
        view1 = view.findViewById(R.id.viewcurated);
        recyclerView = view.findViewById(R.id.recyclerView);
        imageView = view.findViewById(R.id.lastImage);
        recyclerView.setHasFixedSize(true);
        curatedText=view.findViewById(R.id.curatedText);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(true);
        if (getActivity()!=null)
        {
            new Thread(() -> getActivity().runOnUiThread(() ->
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            loadImage();
                        }
                    }, 0, 5 * 60 * 1000))).start();
        }

        layoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(layoutManager);
        /*  recyclerView.setLayoutManager((new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, true)));*/
        rescAdapter = new RescAdapter(modelData, getActivity());

        recyclerView.setAdapter(rescAdapter);

        if (getActivity() != null) {
            sharedPref1 = new SharedPref1(getActivity());
            if (sharedPref1.getTheme().equals("Light")) {
                Resources res = getResources(); //resource handle
                Drawable drawable = res.getDrawable(R.drawable.basic_design1_white);
                view1.setBackground(drawable);
                curatedText.setTextColor(Color.parseColor("#000000"));

            } else if (sharedPref1.getTheme().equals("Dark")) {
                Resources res = getResources(); //resource handle
                Drawable drawable = res.getDrawable(R.drawable.basic_design1);
                view1.setBackground(drawable);
                curatedText.setTextColor(Color.parseColor("#FFFFFF"));

            } else {
                Resources res = getResources(); //resource handle
                Drawable drawable = res.getDrawable(R.drawable.basic_design1_white);
                view1.setBackground(drawable);
                curatedText.setTextColor(Color.parseColor("#000000"));

            }


        }
        return view;
    }


    public static LastFragment newInstance(String text) {
        LastFragment f = new LastFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    public void loadImage() {
        modelData = new ArrayList<>();
        /*slides=new ArrayList<>();*/

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

/*
                Random random=new Random();
                int n = random.nextInt(modelData.size());*/
/*
                Log.d("regr", String.valueOf(modelData.get(n)));
*/
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
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x; //width of screen in pixels
                int height = size.y;
                Bitmap image = memCache.get("imagefile");
                if (image != null) {
                    //Bitmap exists in cache.
                    imageView.setImageBitmap(image);
                } else {
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
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getActivity(), FullImage.class);
                                    ModelData modelData2 = modelData.get(position);
                                    intent.putExtra("img", modelData2.getLarge2x());
                                    startActivity(intent);
                                }
                            });

                            LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                                @Override
                                protected int sizeOf(String key, Bitmap image) {
                                    return image.getByteCount() / 1024;
                                }
                            };
                            Display display = getActivity().getWindowManager().getDefaultDisplay();
                            Point size = new Point();
                            display.getSize(size);
                            int width = size.x; //width of screen in pixels
                            int height = size.y;
                            Bitmap image = memCache.get("imagefile");
                            if (image != null) {
                                //Bitmap exists in cache.
                                imageView.setImageBitmap(image);
                            } else {

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

                            //  Glide.with(getActivity()).load(modelData.get(position).getLarge2x()).into(imageView);

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
                params.put("Authorization", "563492ad6f91700001000001fd351942a4524d62bb9a68308855b667");
                return params;
            }
        };

        stringRequest.setShouldCache(false);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


}
