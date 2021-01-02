package com.client.vpman.weatherwall.ui.Fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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
import com.client.vpman.weatherwall.CustomeUsefullClass.Constant;
import com.client.vpman.weatherwall.ui.Activity.TestingMotionLayout;
import com.client.vpman.weatherwall.CustomeUsefullClass.Connectivity;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.client.vpman.weatherwall.databinding.FragmentPopularBinding;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.textview.MaterialTextView;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class Popular extends Fragment {

    View view;

    public Popular() {
        // Required empty public constructor
    }
    private List<String> apiList;


    private String query, query1, query2, query3, query4, query5, query6, query7, query8;
    private SharedPref1 sharedPref1;
    private Animation fromtop, bounce;
    private FragmentPopularBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPopularBinding.inflate(inflater, container, false);
        view = binding.getRoot();


        fromtop = AnimationUtils.loadAnimation(getActivity(), R.anim.fromtop);

        if (getActivity() != null) {
            sharedPref1 = new SharedPref1(getActivity());
            if (sharedPref1.getTheme().equals("Light")) {
                binding.relpop.setBackgroundColor(Color.parseColor("#FFFFFF"));
                binding.popPhoto1.setTextColor(Color.parseColor("#000000"));
                binding.SwipUp.setImageResource(R.drawable.ic_up_arow_black);
                binding.relLandscanpe.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else if (sharedPref1.getTheme().equals("Dark")) {
                binding.relpop.setBackgroundColor(Color.parseColor("#000000"));
                binding.popPhoto1.setTextColor(Color.parseColor("#FFFFFF"));
                binding.SwipUp.setImageResource(R.drawable.ic_up_arow);
                binding.relLandscanpe.setBackgroundColor(Color.parseColor("#000000"));
            } else {
                binding.relpop.setBackgroundColor(Color.parseColor("#FFFFFF"));
                binding.popPhoto1.setTextColor(Color.parseColor("#000000"));
                binding.SwipUp.setImageResource(R.drawable.ic_up_arow_black);
                binding.relLandscanpe.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }


        bounce = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

        bounce.setRepeatCount(Animation.INFINITE);
        bounce.setRepeatMode(Animation.INFINITE);
        bounce.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bounce = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);

                bounce.setRepeatCount(Animation.INFINITE);
                bounce.setRepeatMode(Animation.INFINITE);
                binding.SwipUp.startAnimation(bounce);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        binding.SwipUp.startAnimation(bounce);

        RequestOptions requestOptions = new RequestOptions();
        // requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(new ObjectKey(System.currentTimeMillis())).encodeQuality(70);
        requestOptions.priority(Priority.IMMEDIATE);
        requestOptions.skipMemoryCache(false);
        requestOptions.onlyRetrieveFromCache(true);
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.priority(Priority.HIGH);
        requestOptions.isMemoryCacheable();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);

        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        //   requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.centerCrop();

        if (Connectivity.isConnected(getActivity()) && Connectivity.isConnectedMobile(getActivity()) && Connectivity.isConnectedFast(getActivity()) ||
                Connectivity.isConnected(getActivity()) && Connectivity.isConnectedWifi(getActivity()) && Connectivity.isConnectedFast(getActivity())) {

            binding.droneView.setShapeAppearanceModel(binding.droneView.getShapeAppearanceModel()
                    .toBuilder().setTopLeftCorner(CornerFamily.ROUNDED, 150)
                    .setBottomLeftCorner(CornerFamily.ROUNDED, 150).build());
            query = "drone view";

            LoadImageDiff(requestOptions,query,binding.droneView);


            query1 = "nature";
            LoadImageDiff(requestOptions,query1,binding.nature);
            binding.nature.setShapeAppearanceModel(binding.nature.getShapeAppearanceModel()
                    .toBuilder()
                    .setTopRightCorner(CornerFamily.ROUNDED, 150)
                    .build());



            query2 = "food";
            LoadImageDiff(requestOptions,query2,binding.food);
            binding.food.setShapeAppearanceModel(binding.food.getShapeAppearanceModel()
                    .toBuilder()
                    .setBottomRightCorner(CornerFamily.ROUNDED, 150).build());



            query3 = "Landscape";

            LoadImage(requestOptions,query3,binding.Landscape,binding.landScape);


            query4 = "Cityscape";
            LoadImage(requestOptions,query4,binding.Cityscape,binding.cityScape);


            query5 = "Seascape";
            LoadImage(requestOptions,query5,binding.Seascape,binding.seaScape);



            query6 = "Twilight";
            LoadImage(requestOptions,query6,binding.Twilight,binding.twiLight);

            query7 = "Food";
            LoadImage(requestOptions,query7,binding.Food,binding.fOOD);

            query8 = "Drone View";
            LoadImage(requestOptions,query8,binding.DronView,binding.DroneView991);


        }


        Log.d("Popular", "onCreateView:");

        return view;
    }

    public static Popular newInstance(String text) {
        Popular f = new Popular();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    public void LoadImage(RequestOptions requestOptions, String query, CircularImageView imageView, MaterialTextView textView){

        String Url = Constant.BASE_URL + query + "&per_page=80&page=1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, response -> {
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
                        if (getActivity() != null) {
                            Glide.with(getActivity())
                                    .load(object.getString("large"))
                                    .thumbnail(
                                            Glide.with(Objects.requireNonNull(getActivity())).load(object.getString("large2x"))
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

                                            //    spinKitView.setVisibility(View.GONE);

                                            return false;
                                        }
                                    })
                                    .into(imageView);

                            imageView.setOnClickListener(v -> {
                                Intent intent = new Intent(getActivity(), TestingMotionLayout.class);
                                try {
                                    intent.putExtra("img1", object.getString("large2x"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    intent.putExtra("img2", object.getString("large"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                intent.putExtra("query", query);
                                intent.putExtra("text", query);

                                Pair[] pairs = new Pair[2];
                                pairs[0] = new Pair<View, String>(imageView, "img1");
                                pairs[1] = new Pair<View, String>(textView, "text");

                                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        getActivity(), pairs
                                );

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    startActivity(intent, optionsCompat.toBundle());
                                } else {
                                    startActivity(intent);
                                }
                            });

                        }

                    }
                }


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
                apiList = new ArrayList<>();
                apiList.add(getString(R.string.APIKEY1));
                apiList.add(getString(R.string.APIKEY2));
                apiList.add(getString(R.string.APIKEY3));
                apiList.add(getString(R.string.APIKEY4));
                apiList.add(getString(R.string.APIKEY5));
                Random random = new Random();
                int n = random.nextInt(apiList.size());
                params.put("Authorization", apiList.get(n));

                return params;
            }
        };
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    public void LoadImageDiff(RequestOptions requestOptions, String query, ShapeableImageView imageView){

        String Url = Constant.BASE_URL + query + "&per_page=80&page=1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, response -> {
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
                        if (getActivity() != null) {
                            Glide.with(getActivity())
                                    .load(object.getString("large"))
                                    .thumbnail(
                                            Glide.with(Objects.requireNonNull(getActivity())).load(object.getString("large2x"))
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

                                            //    spinKitView.setVisibility(View.GONE);

                                            return false;
                                        }
                                    })
                                    .into(imageView);

                            imageView.setOnClickListener(v -> {
                                Intent intent = new Intent(getActivity(), TestingMotionLayout.class);
                                try {
                                    intent.putExtra("img1", object.getString("large2x"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    intent.putExtra("img2", object.getString("large"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                intent.putExtra("query", query);
                                intent.putExtra("text", query);

                                Pair[] pairs = new Pair[1];
                                pairs[0] = new Pair<View, String>(imageView, "img1");


                                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        getActivity(), pairs
                                );

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    startActivity(intent, optionsCompat.toBundle());
                                } else {
                                    startActivity(intent);
                                }
                            });

                        }

                    }
                }


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
                apiList = new ArrayList<>();
                apiList.add(getString(R.string.APIKEY1));
                apiList.add(getString(R.string.APIKEY2));
                apiList.add(getString(R.string.APIKEY3));
                apiList.add(getString(R.string.APIKEY4));
                apiList.add(getString(R.string.APIKEY5));
                Random random = new Random();
                int n = random.nextInt(apiList.size());
                params.put("Authorization", apiList.get(n));

                return params;
            }
        };
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }


}