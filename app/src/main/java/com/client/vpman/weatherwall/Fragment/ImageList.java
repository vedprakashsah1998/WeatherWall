package com.client.vpman.weatherwall.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.LruCache;
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
import com.client.vpman.weatherwall.Activity.TestingMotionLayout;
import com.client.vpman.weatherwall.Adapter.TestingAdapter;
import com.client.vpman.weatherwall.CustomeUsefullClass.ModelData4;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageList extends Fragment {

    public ImageList() {
        // Required empty public constructor
    }

    View view;
    ImageView toolbar_image, backMotion;
    String query;
    TestingAdapter testingAdapter;

    private long mRequestStartTime;

    List<ModelData4> modelData4List;

    RecyclerView recyclerView;
    SharedPref1 sharedPref1;
    MotionLayout motionLayout;
    ImageView backgroundDesign;
    MaterialTextView materialTextView;

    BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_image_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerviewTesting);
        toolbar_image = view.findViewById(R.id.toolbar_image);
        materialTextView = view.findViewById(R.id.titleData);
        backMotion = view.findViewById(R.id.backMotion);
        motionLayout = view.findViewById(R.id.motionBackground);

        backgroundDesign = view.findViewById(R.id.backgroundDesign);
        bottomNavigationView=getActivity().findViewById(R.id.bottomNavigation);
        Intent intent = getActivity().getIntent();
        String mImg = intent.getStringExtra("img1");
        String sImg = intent.getStringExtra("img2");
        query = intent.getStringExtra("query");
        String Landscape = intent.getStringExtra("text");
        materialTextView.setText(Landscape);
        sharedPref1 = new SharedPref1(getActivity());
        if (sharedPref1.getTheme().equals("Light")) {
            materialTextView.setTextColor(Color.parseColor("#000000"));
            backMotion.setImageResource(R.drawable.ic_arrow_back);
            motionLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            backgroundDesign.setImageResource(R.drawable.basic_design_customized_white);
        } else if (sharedPref1.getTheme().equals("Dark")) {

            materialTextView.setTextColor(Color.parseColor("#FFFFFF"));
            backMotion.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            motionLayout.setBackgroundColor(Color.parseColor("#000000"));
            backgroundDesign.setImageResource(R.drawable.basic_design_customized);

        } else {

            materialTextView.setTextColor(Color.parseColor("#000000"));
            backMotion.setImageResource(R.drawable.ic_arrow_back);
            motionLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            backgroundDesign.setImageResource(R.drawable.basic_design_customized_white);
        }

        backMotion.setOnClickListener(v -> {
            getActivity().finish();
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
            toolbar_image.setImageBitmap(image);
        } else {
            Glide.with(getActivity())
                    .load(mImg)
                    .thumbnail(
                            Glide.with(getActivity()).load(sImg)
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

                    .into(toolbar_image);
        }

        modelData4List = new ArrayList<>();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && bottomNavigationView.isShown()) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else if (dy < 0 ) {
                    bottomNavigationView.setVisibility(View.VISIBLE);

                }
            }
        });

        LoadImage();

        return view;
    }

    public static ImageList newInstance(String text) {
        ImageList f = new ImageList();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    public void LoadImage() {
        mRequestStartTime = System.currentTimeMillis();


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
                    JSONObject PhotoUrl = new JSONObject(String.valueOf(wallobj));
                    Log.d("PhotoURL", wallobj.getString("url"));
                    JSONObject jsonObject = wallobj.getJSONObject("src");
                    JSONObject object = new JSONObject(String.valueOf(jsonObject));
                    ModelData4 modelData1 = new ModelData4(object.getString("large2x"), photographer.getString("photographer"), object.getString("large"), object.getString("original"), wallobj.getString("url"));
                    modelData4List.add(modelData1);
                }
                Collections.shuffle(modelData4List);
                testingAdapter = new TestingAdapter(getActivity(), modelData4List);
                LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);

                recyclerView.setHasFixedSize(true);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setNestedScrollingEnabled(true);
                int itemViewType = 0;
                recyclerView.getRecycledViewPool().setMaxRecycledViews(itemViewType, 0);
                recyclerView.setAdapter(testingAdapter);


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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

}
