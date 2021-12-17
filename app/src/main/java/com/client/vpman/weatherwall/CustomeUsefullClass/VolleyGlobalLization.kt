package com.client.vpman.weatherwall.CustomeUsefullClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.util.LruCache;
import android.view.View;

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
import com.client.vpman.weatherwall.R;
import com.client.vpman.weatherwall.ui.Activity.ExploreAcitivity;
import com.makeramen.roundedimageview.RoundedImageView;

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

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;

public class VolleyGlobalLization {


    public VolleyGlobalLization() {
    }

    public static void LoadImageDiff( String query, RoundedImageView imageView, Context context) {
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
        requestOptions.centerCrop();
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
                        if (context != null) {
                            Glide.with(context)
                                    .load(object.getString("large"))
                                    .thumbnail(
                                            Glide.with(Objects.requireNonNull(context)).load(object.getString("large2x"))
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
                                Intent intent = new Intent(context, ExploreAcitivity.class);
                                try {
                                    intent.putExtra("imgDataSmall", object.getString("large2x"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    intent.putExtra("imgData", object.getString("large"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                intent.putExtra("query", query);
                                intent.putExtra("text", query);

                                Pair[] pairs = new Pair[1];
                                pairs[0] = new Pair<View, String>(imageView, "img1");


                                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        (Activity) context, pairs
                                );

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    context.startActivity(intent, optionsCompat.toBundle());
                                } else {
                                    context.startActivity(intent);
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
                 List<String> apiList = new ArrayList<>();
                apiList.add(context.getString(R.string.APIKEY1));
                apiList.add(context.getString(R.string.APIKEY2));
                apiList.add(context.getString(R.string.APIKEY3));
                apiList.add(context.getString(R.string.APIKEY4));
                apiList.add(context.getString(R.string.APIKEY5));
                Random random = new Random();
                int n = random.nextInt(apiList.size());
                params.put("Authorization", apiList.get(n));

                return params;
            }
        };
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }
}
